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
            kotlin.srcDir("src/main/kotlin")
            resources.srcDir("src/main/resource")
            dependencies {
                implementation(project(":easy-rules-core"))
                implementation("org.jetbrains.kotlin:kotlin-reflect:${extra.get("kotlin_version")}")
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test-common:${extra.get("kotlin_version")}"))
                implementation(kotlin("test-annotations-common:${extra.get("kotlin_version")}"))
                implementation("io.mockk:mockk-common:${extra.get("mockk_version")}")
            }
        }
        val jvmMain by getting {
            dependencies {
                implementation("org.jetbrains.kotlin:kotlin-reflect:${extra.get("kotlin_version")}")
            }
        }
        val jvmTest by getting {

            kotlin.srcDir("src/test/kotlin")
            resources.srcDir("src/test/resource")
            dependencies {
                implementation(kotlin("test-junit"))

                implementation("io.mockk:mockk:${extra.get("mockk_version")}")

            }
        }
    }
}
