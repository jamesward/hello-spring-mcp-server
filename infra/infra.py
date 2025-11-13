import aws_cdk

from aws_cdk import (
    aws_cognito
)

from constructs import Construct

from buildpack_image_asset import BuildpackImageAsset
from bedrock_agentcore_runtime import BedrockAgentCoreRuntime

class BedrockAgentCoreStack(aws_cdk.Stack):
    def __init__(self, scope: Construct, construct_id: str, **kwargs):
        super().__init__(scope, construct_id, **kwargs)

        user_pool = aws_cognito.UserPool(self, "UserPool",
                                             password_policy=aws_cognito.PasswordPolicy(
                                                 min_length=8
                                             ),
                                             removal_policy=aws_cdk.RemovalPolicy.DESTROY
                                         )

        scope = aws_cognito.ResourceServerScope(scope_name="*", scope_description="Full access")

        resource_server = aws_cognito.UserPoolResourceServer(
            self, "MCPResourceServer",
            user_pool=user_pool,
            identifier="mcp",
            scopes=[scope],
        )

        user_pool_client = aws_cognito.UserPoolClient(self, "UserPoolClient",
                                                            user_pool=user_pool,
                                                            generate_secret=True,
                                                            # auth_flows=aws_cognito.AuthFlow(
                                                            #   user_password=True,
                                                            # )
                                                            o_auth=aws_cognito.OAuthSettings(
                                                                flows=aws_cognito.OAuthFlows(
                                                                    client_credentials=True
                                                                ),
                                                                scopes=[
                                                                    aws_cognito.OAuthScope.resource_server(resource_server, scope),
                                                                ],
                                                            )
                                                            )

        domain_prefix = f"my-m2m-{self.account}-{self.region}"

        user_pool.add_domain(
            "UserPoolDomain",
            cognito_domain=aws_cognito.CognitoDomainOptions(
                domain_prefix=domain_prefix
            ),
        )

        server_image = BuildpackImageAsset(self, "ServerImage",
                                                source_path="../",
                                                builder="paketobuildpacks/builder-noble-java-tiny",
                                                run_image="paketobuildpacks/ubuntu-noble-run-tiny",
                                                platform="linux/amd64",
                                                default_process="web",
                                                )

        server = BedrockAgentCoreRuntime(self, "ServerAgentCore",
                                                  repository=server_image.ecr_repo,
                                                  protocol="MCP",
                                                  discovery_url=f"https://cognito-idp.{self.region}.amazonaws.com/{user_pool.user_pool_id}/.well-known/openid-configuration",
                                                  client_id=user_pool_client.user_pool_client_id,
                                                  env={
                                                      "PORT":"8000"
                                                  }
                                                  )

        server.node.add_dependency(server_image)

        aws_cdk.CfnOutput(self, "CLIENT_ID", value=user_pool_client.user_pool_client_id)
        aws_cdk.CfnOutput(self, "CLIENT_SECRET", value=user_pool_client.node.default_child.attr_client_secret)
        aws_cdk.CfnOutput(self, "OAUTH_TOKEN_URL", value=f"https://{domain_prefix}.auth.{self.region}.amazoncognito.com/oauth2/token")
        aws_cdk.CfnOutput(self, "MCP_ARN", value=server.resource.ref)

app = aws_cdk.App()

BedrockAgentCoreStack(app, "HelloSpringMCPServer")

app.synth()
