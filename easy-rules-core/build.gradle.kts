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
    js(IR) {
        browser {
            binaries.executable()
            testTask {
                useKarma {
                    useChromeHeadless()
                    webpackConfig.cssSupport.enabled = true
                }
            }


        }
    }

    sourceSets {

        val commonMain by getting {
            kotlin.srcDir("src/main/java")
            resources.srcDir("src/main/resource")
            dependencies {
                implementation("io.github.microutils:kotlin-logging:${extra.get("kotlin_logging_version")}")
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test-common:${extra.get("kotlin_version")}"))
                implementation(kotlin("test-annotations-common:${extra.get("kotlin_version")}"))
                implementation("io.mockk:mockk-common:${extra.get("mockk_version")}")
            }
        }
        val jvmMain by getting
        val jvmTest by getting {

            kotlin.srcDir("src/test/java")
            resources.srcDir("src/test/resource")
            dependencies {
                implementation(kotlin("test-junit"))
                implementation("io.mockk:mockk:${extra.get("mockk_version")}")

            }
        }
        val jsMain by getting {}
        val jsTest by getting {
        }
    }
}
