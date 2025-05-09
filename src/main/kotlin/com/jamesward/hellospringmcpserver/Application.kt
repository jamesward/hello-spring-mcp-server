package com.jamesward.hellospringmcpserver

import io.micrometer.core.instrument.MeterRegistry
import io.micrometer.core.instrument.Statistic
import org.springframework.ai.tool.annotation.Tool
import org.springframework.ai.tool.method.MethodToolCallbackProvider
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Service

@SpringBootApplication
class Application {
    @Bean
    fun tools(myTools: MyTools): MethodToolCallbackProvider =
        MethodToolCallbackProvider.builder().toolObjects(myTools).build()
}

@Service
class MyTools(val meterRegistry: MeterRegistry) {

    @Tool(description = "get the number of open connections to this server")
    fun numConnections(): Double =
        meterRegistry
            .find("http.server.requests.active")
//            .tag("uri", "/sse") // todo: there seems to be 5 active connections with the default usage. But this should be 1 and it'd be nice to filter to the /sse uri but that doesn't seem to work
            .meter()
            ?.measure()
            ?.firstOrNull { it.statistic == Statistic.ACTIVE_TASKS }
            ?.value ?: throw Exception("could not get number of connections")

}

fun main(args: Array<String>) {
    runApplication<Application>(*args)
}
