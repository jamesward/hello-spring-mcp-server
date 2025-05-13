plugins {
    kotlin("jvm") version "2.1.20"
    kotlin("plugin.spring") version "2.1.20"
    id("org.springframework.boot") version "3.4.5"
    id("io.spring.dependency-management") version "1.1.7"
    id("org.graalvm.buildtools.native") version "0.10.6"
}

group = "com.jamesward"
version = "0.0.1-SNAPSHOT"

kotlin {
    jvmToolchain(21)
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.ai:spring-ai-starter-mcp-server-webflux")
    implementation("io.projectreactor.kotlin:reactor-kotlin-extensions")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")
    implementation("org.webjars:bootstrap:5.3.5")
    implementation("org.jetbrains.kotlinx:kotlinx-html-jvm:0.12.0")
    runtimeOnly("org.webjars:webjars-locator-lite:1.0.1")
    developmentOnly("org.springframework.boot:spring-boot-devtools")
}

dependencyManagement {
    imports {
        mavenBom("org.springframework.ai:spring-ai-bom:1.0.0-RC1")
    }
}

// disable the plain jar because it confuses Heroku
tasks.named<Jar>("jar") {
    enabled = false
}


//java {
//    toolchain {
//        languageVersion = JavaLanguageVersion.of(21)
//        vendor = JvmVendorSpec.GRAAL_VM
//        nativeImageCapable = true
//    }
//}
//
//graalvmNative {
//    toolchainDetection = true
//}

//graalvmNative {
//    binaries {
//        named("main") {
//            javaLauncher = javaToolchains.launcherFor {
//                languageVersion = JavaLanguageVersion.of(23)
//                vendor = JvmVendorSpec.GRAAL_VM
//                nativeImageCapable = true
//            }
//        }
//    }
//}
