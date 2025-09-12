package com.jamesward.hellospringmcpserver

import org.springframework.ai.tool.annotation.Tool
import org.springframework.ai.tool.annotation.ToolParam
import org.springframework.ai.tool.method.MethodToolCallbackProvider
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Component

@SpringBootApplication
class Application {
    @Bean
    fun tools(myTools: MyTools): MethodToolCallbackProvider =
        MethodToolCallbackProvider.builder().toolObjects(myTools).build()
}

data class Employee(val name: String, val skills: List<String>)

data class EmployeeSkills(val skills: Set<String>)

data class Employees(val employees: List<Employee>)

@Component
class MyTools {
    @Tool(description = "the list of all possible employee skills")
    fun getSkills(): EmployeeSkills = run {
        println("getSkills")
        EmployeeSkills(
            SampleData.employees.flatMap { it.skills }.toSet()
        )
    }

    @Tool(description = "the employees that have a specific skill")
    fun getEmployeesWithSkill(@ToolParam(description = "skill") skill: String): Employees = run {
        println("getEmployeesWithSkill $skill")
        Employees(
            SampleData.employees.filter { employee ->
                employee.skills.any { it.equals(skill, ignoreCase = true) }
            }
        )
    }
}

fun main(args: Array<String>) {
    runApplication<Application>(*args)
}
