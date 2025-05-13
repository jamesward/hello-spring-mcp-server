package com.jamesward.hellospringmcpserver

import kotlinx.html.*
import kotlinx.html.dom.createHTMLDocument
import org.springframework.ai.tool.ToolCallback
import org.w3c.dom.Document

class Html(tools: Array<ToolCallback>) {

    private val indexHTML: HTML.() -> Unit = {
        head {
            link("/webjars/bootstrap/css/bootstrap.min.css", LinkRel.stylesheet)
        }
        body {
            header("bg-primary text-white py-5") {
                div("container") {
                    h1("display-4") {
                        +"MCP Test Server"
                    }
                    p("lead") {
                        +"A basic MCP server for testing - Built with Spring AI & Kotlin"
                    }
                }
            }
            main("container my-5") {
                div("row") {
                    div("col-lg-8") {
                        h2 {
                            +"Supports SSE (Streamable HTTP coming soon)"
                        }
                        span {
                            +"Point your MCP client to: "
                        }
                        code {
                            // todo get from req?
                            +"https://mcp-test.jamesward.com/sse"
                        }

                        hr()

                        h3 {
                            +"Features"
                        }
                        ul {
                            li {
                                h5 {
                                    +"Tools:"
                                }
                                ul {
                                    tools.forEach { tool ->
                                        li {
                                            +tool.toolDefinition.description()
                                            // todo: params
                                        }
                                    }
                                }
                            }
                            li {
                                h5 {
                                    +"Server Instructions"
                                }
                            }
                            li {
                                h5 {
                                    +"Prompts (Coming soon)"
                                }
                            }
                            li {
                                h5 {
                                    +"Resources (Coming soon)"
                                }
                            }
                        }

                        hr()

                        span {
                            +"Source: "
                        }
                        a("https://github.com/jamesward/hello-spring-mcp-server") {
                            +"github.com/jamesward/hello-spring-mcp-server"
                        }
                    }
                }
            }
            footer("bg-light text-center text-muted py-4 mt-5") {
                div("container") {
                    span {
                        +"Built by "
                    }
                    a("https://jamesward.com") {
                        +"James Ward"
                    }
                }
            }
        }
    }

    val index: Document = createHTMLDocument().html(block = indexHTML)

}
