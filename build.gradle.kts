import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl

plugins {
    kotlin("multiplatform") version "2.1.0"
    `maven-publish`
}

group = "com.mylosoftworks"
version = "1.0"

repositories {
    mavenCentral()
}

publishing {
    publications {

    }
}

kotlin {
    jvm()
    js {
        browser()
        nodejs()
    }
    @OptIn(ExperimentalWasmDsl::class)
    wasmJs {
        browser()
        nodejs()
    }
}