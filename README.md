# Hello Spring MCP Server

## Run Locally

```
./gradlew bootRun
```

## Test the container

```
./gradlew bootBuildImage
docker run -it -p8000:8000 -ePORT=8000 hello-spring-mcp-server:0.0.1-SNAPSHOT
```

## Run on AgentCore:

1. Setup local AWS credentials
1. Install node & uv
1. Deploy the CDK stack:
    ```
    npx aws-cdk bootstrap
    npx aws-cdk deploy
    ```

## Test on AgentCore:

1. Install curl
1. Set env vars from CDK output:
    ```
    export CLIENT_ID=<YOURS>
    export CLIENT_SECRET=<YOURS>
    export OAUTH_TOKEN_URL=<YOURS>
    ```

1. Get a Bearer token:
    ```
    curl -X POST \
      $OAUTH_TOKEN_URL \
      -u "$CLIENT_ID:$CLIENT_SECRET" \
      -d "grant_type=client_credentials"
    ```

1. Get the URL to the MCP server:
    ```
    export AWS_REGION=<us-east-1 or your region>
    export AGENT_ARN=<YOURS FROM SDK OUTPUT>
    export ENCODED_ARN=$(echo "$AGENT_ARN" | sed 's/:/%3A/g' | sed 's/\//%2F/g')
    echo "https://bedrock-agentcore.$AWS_REGION.amazonaws.com/runtimes/$ENCODED_ARN/invocations?qualifier=DEFAULT"
    ```

1. Open the MCP Inspector:
    ```
    npx -y @modelcontextprotocol/inspector
    ```

1. Set the protocol to Streamable HTTP
1. Set the URL to the MCP server
1. Set the Bearer token in the Authentication settings (prefixing the value with `Bearer `)
1. Connect to the server
1. List the tools and invoke a tool
