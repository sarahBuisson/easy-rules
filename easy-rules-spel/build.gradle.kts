plugins {
    kotlin("multiplatform")
    id("maven-publish")

}

kotlin {
    jvm {
        compilations.all {
            kotlinOptions.jvmTarget = "1.8"
        }
        testRuns["test"].executionTask.configure {
            useJUnit()
        }
    }

    sourceSets {
        val commonMain by getting {
            kotlin.srcDir("src/main/java")
            resources.srcDir("src/main/resource")
            dependencies {
                implementation(kotlin("stdlib"))
                implementation("io.github.microutils:kotlin-logging:${extra.get("kotlin_logging_version")}")
                implementation("org.springframework:spring-expression:5.3.1")
                implementation(project(":easy-rules-core"))
                implementation(project(":easy-rules-support"))
                implementation(project(":easy-rules-support-annotation"))
            }
        }
        val commonTest by getting {
            kotlin.srcDir("src/test/java")
            resources.srcDir("src/test/resource")
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
            }
        }
        val jvmMain by getting {
            dependencies {
                implementation("io.github.microutils:kotlin-logging-jvm:${extra.get("kotlin_logging_version")}")

            }
        }
        val jvmTest by getting {
            dependencies {
                implementation(kotlin("test-junit"))
            }
        }
    }
}
