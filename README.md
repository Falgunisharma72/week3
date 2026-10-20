# Issue Tracking System - Spring Boot Microservices (backend)

Five Spring Boot 3.3 / Spring Cloud 2023.0.x services (Java **21**):

| Folder | Port | Role |
|---|---|---|
| `EurekaServer` | 8761 | Service registry |
| `UserService` | 8081 | Users, signup / login |
| `ProjectService` | 8082 | Projects |
| `IssueService` | 8083 | Issues |
| `ApiGateway` | 8080 | Single entry point (`/api/users/**`, `/api/projects/**`, `/api/issues/**`), CORS for the React app |

Frontend: the `week4` repo (React, calls the gateway on `http://localhost:8080`).

## Run locally in Spring Tool Suite (STS)

1. **Java 21** - STS: *Window → Preferences → Java → Installed JREs* must contain a JDK 21 (the services do not compile on 17).
2. **Database: nothing to install.** The three services connect to the AWS RDS MySQL instance `database-1`
   (endpoint, `admin` user and password are hard-coded in each `src/main/resources/application.properties`).
   The `week3` schema and tables are created automatically. Each service uses at most 3 connections
   (`spring.datasource.hikari.maximum-pool-size=3`) because the small RDS instance only allows ~60 in total.
   The laptop needs internet access to `database-1...rds.amazonaws.com:3306`.
3. *File → Import → Existing Maven Projects* → select this folder → tick all five projects.
4. Start them **in this order** (right-click → *Run As → Spring Boot App*), waiting for each to finish starting:
   `EurekaServer` → `UserService`, `ProjectService`, `IssueService` → `ApiGateway`.
5. Check http://localhost:8761 - all four services must be listed as UP (give the gateway ~30 s to see them).
6. Start the frontend (`week4`: `npm install && npm start`) and open http://localhost:3000.

Port 8080 must be free for the gateway (stop anything else using it).
The first page load can take a few seconds - every query travels to RDS in us-east-1.
CORS: the gateway accepts requests from any origin, so React may run on any port or host.

## API quick check

```
POST http://localhost:8080/api/users          {"name":"A","email":"a@x.com","password":"p","role":"PROJECT_OWNER"}
POST http://localhost:8080/api/users/login    {"email":"a@x.com","password":"p"}
GET  http://localhost:8080/api/projects
GET  http://localhost:8080/api/issues
```

## Live on AWS

* **Milestone 1 (EC2):** http://issue-tracking-bucket-027fs.s3-website-us-east-1.amazonaws.com - gateway on the `apiS` instance,
  reachable on port 80 (iptables forwards 80 → 8080; many office networks block 8080).
* **Milestone 2 (ECS Fargate):** http://its-ecs-frontend-027fs.s3-website-us-east-1.amazonaws.com - the gateway service sits behind the
  Application Load Balancer `its-ecs-alb` (listener :80 → target group `its-gateway-tg`, container port 8080, health check `/api/users`),
  which gives a fixed address even though Fargate task IPs change on every deployment.

## AWS deployment (case study milestones)

* **Milestone 1** - JARs on EC2 (one instance per service, Java 21, systemd), RDS MySQL, frontend on S3.
  For AWS the `application.properties` need: the RDS JDBC URL (`...?createDatabaseIfNotExist=true&useSSL=false&allowPublicKeyRetrieval=true`),
  `eureka.client.service-url.defaultZone=http://<eureka-private-ip>:8761/eureka/`, `eureka.instance.prefer-ip-address=true`,
  the S3 website origin in `CorsConfig`, and `project.service.url=http://<project-private-ip>:8082` for `IssueService`.
* **Milestone 2** - each service has a `Dockerfile` (build the JAR with `mvn -DskipTests package` first), images go to Amazon ECR and
  run on ECS Fargate. `deploy/ecs/deploy.sh` + `taskdef.py` create the task definitions and services
  (`export DB_PASSWORD=<rds password>` first; the password is never stored in git).
  Containers are configured with environment variables (`SPRING_DATASOURCE_*`, `JAVA_TOOL_OPTIONS` for Eureka, `PROJECT_SERVICE_URL`).

## Notes / known limitations

* Passwords are stored in plain text (case-study scope) - use BCrypt + Spring Security for real use.
* `IssueService` does not verify that a project/assignee exists when creating an issue.
* There are no DELETE endpoints (the frontend has no delete button).
