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
                implementation("org.mvel:mvel2:2.4.10.Final")
                implementation(project(":easy-rules-core"))
                implementation("io.github.microutils:kotlin-logging:${extra.get("kotlin_logging_version")}")

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
        val jvmMain by getting
        val jvmTest by getting {
            dependencies {
                implementation(kotlin("test-junit"))
            }
        }
    }
}
