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
                implementation(project(":easy-rules-core"))
                implementation(project(":easy-rules-support"))
                implementation(project(":easy-rules-support-annotation"))
                implementation("org.apache.commons:commons-jexl3:3.1")
                implementation("io.github.microutils:kotlin-logging:${extra.get("kotlin_logging_version")}")


            }
        }
        val commonTest by getting {
            dependencies {
                kotlin.srcDir("src/test/java")
                resources.srcDir("src/test/resource")
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
            }
        }
        val jvmMain by getting{
            dependencies {
                implementation("org.apache.commons:commons-jexl3:3.1")
            }
        }
        val jvmTest by getting {
            dependencies {
                implementation(kotlin("test-junit"))
            }
        }
    }
}
