package com.jamesward.hellospringmcpserver

import io.micrometer.core.instrument.MeterRegistry
import io.micrometer.core.instrument.Statistic
import kotlinx.html.dom.serialize
import org.springframework.ai.tool.annotation.Tool
import org.springframework.ai.tool.annotation.ToolParam
import org.springframework.ai.tool.method.MethodToolCallbackProvider
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.bodyValueAndAwait
import org.springframework.web.reactive.function.server.coRouter

@SpringBootApplication
class Application {
    @Bean
    fun tools(myTools: MyTools): MethodToolCallbackProvider =
        MethodToolCallbackProvider.builder().toolObjects(myTools).build()

    @Bean
    fun http(mcps: MethodToolCallbackProvider) = coRouter {
        GET("/") {
            ServerResponse.ok().contentType(MediaType.TEXT_HTML).bodyValueAndAwait(Html(mcps.toolCallbacks).index.serialize(true))
        }
    }
}

@Service
class MyTools(val meterRegistry: MeterRegistry) {

    @Tool(description = "get the number of open connections to this server")
    fun numActiveConnections(): Double =
        meterRegistry
            .find("http.server.requests.active")
            .tag("method", "GET") // just the /sse GET
            .meter()
            ?.measure()
            ?.firstOrNull { it.statistic == Statistic.ACTIVE_TASKS }
            ?.value ?: throw Exception("could not get number of connections")

    @Tool(description = "get the number of connections made since the server started")
    fun numTotalConnections(@ToolParam(required = false, description = "HTTP Method to filter on") method: String?): Double = run {
        val search = meterRegistry.find("http.server.requests")

        val searchWithMaybeTag = if (method != null) {
            search.tag("method", method.uppercase())
        } else {
            search
        }

        searchWithMaybeTag
            .meter()
            ?.measure()
            ?.firstOrNull { it.statistic == Statistic.COUNT }
            ?.value ?: throw Exception("could not get number of connections")
    }
}

fun main(args: Array<String>) {
    runApplication<Application>(*args)
}
