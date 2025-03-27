package com.jamesward.hellospringmcpserver

import org.springframework.ai.tool.annotation.Tool
import org.springframework.ai.tool.annotation.ToolParam
import org.springframework.ai.tool.method.MethodToolCallbackProvider
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Service

@SpringBootApplication
class Application {
    @Bean
    fun weatherTools(weatherService: WeatherService): MethodToolCallbackProvider =
        MethodToolCallbackProvider.builder().toolObjects(weatherService).build()
}

@Service
class WeatherService {

    @Tool(description = "says hello to someone")
    fun sayHello(@ToolParam(description = "name of person") name: String): String =
        "hello, $name"

}

fun main(args: Array<String>) {
    runApplication<Application>(*args)
}
