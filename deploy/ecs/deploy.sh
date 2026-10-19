#!/usr/bin/env bash
# Milestone 2 - deploy ITS microservices on ECS Fargate (run after the images are in ECR).
# Order matters: Eureka first, then user/project, then issue (needs the project IP), then the gateway.
# Tasks run in public subnets with a public IP (no NAT gateway) and share the its-ecs-sg security group.
set -euo pipefail
A=aws; R=us-east-1; CL=its-cluster; SG=sg-0e9335aac8276fd2e
SUBNETS="subnet-0248bbe90af9710c5,subnet-0c6cdc56d0495e541"   # us-east-1a, us-east-1b
HERE="$(cd "$(dirname "$0")" && pwd)"

register() { # name port env...
  python3 "$HERE/taskdef.py" "$@" > /tmp/td-$1.json
  $A ecs register-task-definition --region $R --cli-input-json file:///tmp/td-$1.json --query 'taskDefinition.revision' --output text >/dev/null
}
run_service() { # name [keep]   (keep = leave an already-running service untouched)
  if [ "${2:-}" = keep ] && [ "$($A ecs describe-services --region $R --cluster $CL --services its-$1 --query 'services[?status==`ACTIVE`]|length(@)' --output text)" != 0 ]; then return; fi
  if [ "$($A ecs describe-services --region $R --cluster $CL --services its-$1 --query 'services[?status==`ACTIVE`]|length(@)' --output text)" = 0 ]; then
    $A ecs create-service --region $R --cluster $CL --service-name its-$1 --task-definition its-$1 --desired-count 1 --launch-type FARGATE \
      --network-configuration "awsvpcConfiguration={subnets=[$SUBNETS],securityGroups=[$SG],assignPublicIp=ENABLED}" >/dev/null
  else
    $A ecs update-service --region $R --cluster $CL --service its-$1 --task-definition its-$1 --force-new-deployment >/dev/null
  fi
}
task_ip() { # name  -> private IP of the running task (waits until the rollout has finished)
  $A ecs wait services-stable --region $R --cluster $CL --services its-$1
  for _ in $(seq 1 60); do
    t=$($A ecs list-tasks --region $R --cluster $CL --service-name its-$1 --desired-status RUNNING --query 'taskArns[0]' --output text)
    if [ "$t" != None ]; then
      ip=$($A ecs describe-tasks --region $R --cluster $CL --tasks $t --query 'tasks[0].attachments[0].details[?name==`privateIPv4Address`].value' --output text)
      [ -n "$ip" ] && { echo "$ip"; return; }
    fi; sleep 5
  done; return 1
}

DB="jdbc:mysql://database-1.cps8agaaum93.us-east-1.rds.amazonaws.com:3306/week3_ecs?createDatabaseIfNotExist=true&useSSL=false&allowPublicKeyRetrieval=true"
# The RDS master password is NOT stored in git - export it before running:  export DB_PASSWORD='<your RDS password>'
: "${DB_PASSWORD:?export DB_PASSWORD (the RDS master password) before running this script}"
DBENV=("SPRING_DATASOURCE_URL=$DB" "SPRING_DATASOURCE_USERNAME=admin" "SPRING_DATASOURCE_PASSWORD=$DB_PASSWORD")

register eureka-server 8761; run_service eureka-server keep
EUREKA=$(task_ip eureka-server); echo "eureka task IP: $EUREKA"
# System properties (not env vars): Spring cannot bind the Eureka service-url map key from an env var, and
# Fargate also exposes a link-local 169.254.x.x address, so prefer the VPC (172.31.x.x) address when registering.
EU=("JAVA_TOOL_OPTIONS=-Deureka.client.service-url.defaultZone=http://$EUREKA:8761/eureka/ -Deureka.instance.prefer-ip-address=true -Dspring.cloud.inetutils.preferred-networks=172.31")

register user-service    8081 "${DBENV[@]}" "${EU[@]}"; run_service user-service
register project-service 8082 "${DBENV[@]}" "${EU[@]}"; run_service project-service
PROJECT=$(task_ip project-service); echo "project task IP: $PROJECT"
register issue-service   8083 "${DBENV[@]}" "${EU[@]}" "PROJECT_SERVICE_URL=http://$PROJECT:8082"; run_service issue-service
register api-gateway     8080 "${EU[@]}"; run_service api-gateway
GW=$(task_ip api-gateway); echo "gateway task IP: $GW"
