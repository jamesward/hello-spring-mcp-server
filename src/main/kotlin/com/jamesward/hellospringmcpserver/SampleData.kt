package com.jamesward.hellospringmcpserver

import kotlin.random.Random

object SampleData {

    private val firstNames = listOf("James", "Mary", "John", "Patricia", "Robert", "Jennifer", "Michael", "Linda", "William", "Elizabeth")
    private val lastNames = listOf("Smith", "Johnson", "Williams", "Brown", "Jones", "Garcia", "Miller", "Davis", "Rodriguez", "Martinez")

    val skills = listOf(
        "Kotlin", "Java", "Python", "JavaScript", "TypeScript",
        "React", "Angular", "Spring Boot", "AWS", "Docker",
        "Kubernetes", "SQL", "MongoDB", "Git", "CI/CD",
        "Machine Learning", "DevOps", "Node.js", "REST API", "GraphQL"
    )

    val employees = List(100) {
        Employee(
            name = firstNames.random() + " " + lastNames.random(),
            skills = List(Random.nextInt(2, 6)) { skills.random() }.distinct()
        )
    }.distinctBy { it.name }

}
