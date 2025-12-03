package com.jamesward.hellospringmcpserver

import org.springaicommunity.mcp.annotation.McpTool
import org.springaicommunity.mcp.annotation.McpToolParam
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

data class Employee(val name: String, val skills: List<String>)

data class EmployeeSkills(val skills: Set<String>)

data class Employees(val employees: List<Employee>)

@SpringBootApplication
class Application {

    @McpTool(description = "the list of all possible employee skills", generateOutputSchema = true)
    fun getSkills(): EmployeeSkills = run {
        println("getSkills")
        EmployeeSkills(
            SampleData.employees.flatMap { it.skills }.toSet()
        )
    }

    @McpTool(description = "the employees that have a specific skill", generateOutputSchema = true)
    fun getEmployeesWithSkill(@McpToolParam(description = "skill") skill: String): Employees = run {
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
