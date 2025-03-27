# Hello Spring MCP Server

## Run Locally

```
./gradlew bootRun
```

## Deploy on ECS

Prereqs:
- [Create ECR Repo](https://us-east-1.console.aws.amazon.com/ecr/private-registry/repositories/create?region=us-east-1)
- [Auth `docker` to ECR](https://docs.aws.amazon.com/AmazonECR/latest/userguide/registry_auth.html)

```
export ECR_REPO=<your account id>.dkr.ecr.us-east-1.amazonaws.com/<your repo path>

./gradlew bootBuildImage --imageName=$ECR_REPO

docker push $ECR_REPO:latest

rain deploy \
  --params=ContainerImage=$ECR_REPO:latest,ContainerPort=8080,ServiceName=hello-spring-mcp-server \
  infra.cfn \
  hello-spring-mcp-server
```
