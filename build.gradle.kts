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
}

dependencyManagement {
    imports {
        mavenBom("org.springframework.ai:spring-ai-bom:1.0.0-M8")
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
