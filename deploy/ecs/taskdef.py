#!/usr/bin/env python3
"""Generates an ECS Fargate task definition for one ITS microservice (Milestone 2).
usage: taskdef.py <name> <port> [KEY=VALUE ...]   -> prints JSON for `aws ecs register-task-definition`"""
import json, sys
name, port, env = sys.argv[1], int(sys.argv[2]), sys.argv[3:]
ACCOUNT, REGION = "992382744961", "us-east-1"
print(json.dumps({
  "family": f"its-{name}", "networkMode": "awsvpc", "requiresCompatibilities": ["FARGATE"],
  "cpu": "512", "memory": "1024",
  "executionRoleArn": f"arn:aws:iam::{ACCOUNT}:role/ecsTaskExecutionRole",
  "containerDefinitions": [{
    "name": name, "image": f"{ACCOUNT}.dkr.ecr.{REGION}.amazonaws.com/its-{name}:latest", "essential": True,
    "portMappings": [{"containerPort": port, "protocol": "tcp"}],
    "environment": [dict(zip(("name", "value"), e.split("=", 1))) for e in env],
    "logConfiguration": {"logDriver": "awslogs", "options": {
      "awslogs-group": "/ecs/its", "awslogs-region": REGION, "awslogs-stream-prefix": name}}}]}))
