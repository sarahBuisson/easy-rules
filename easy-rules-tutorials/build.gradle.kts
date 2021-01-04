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
    js(LEGACY) {
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
                implementation(kotlin("stdlib"))
                implementation(project(":easy-rules-core"))
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
        val jsMain by getting {}
        val jsTest by getting {
            dependencies {
                implementation(kotlin("test-js"))
            }
        }
    }
}
