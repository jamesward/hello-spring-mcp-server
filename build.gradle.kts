plugins {
    id("com.skillsjars.gradle-plugin") version "0.2.0"
    kotlin("jvm") version "2.4.21"
    kotlin("plugin.spring") version "2.4.21"
    id("org.springframework.boot") version "4.1.2"
    id("io.spring.dependency-management") version "1.1.7"
}

group = "com.jamesward"
version = "0.0.1-SNAPSHOT"

kotlin {
    jvmToolchain(25)
    compilerOptions {
        allWarningsAsErrors = true
    }
}

dependencies {
    implementation(platform("org.springframework.ai:spring-ai-bom:2.0.2"))
    implementation("org.springframework.ai:spring-ai-starter-mcp-server-webflux")
    implementation("io.projectreactor.kotlin:reactor-kotlin-extensions")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")
    developmentOnly("org.springframework.boot:spring-boot-devtools")
}

// disable the plain jar because it confuses Heroku
tasks.named<Jar>("jar") {
    enabled = false
}

// Agent Skills, extracted with ./gradlew extractSkillsJars
dependencies {
    skill("com.jamesward:skills:0.0.18")
}

skillsjars {
    outputDir.set(layout.projectDirectory.dir(".kiro/skills"))
}
