plugins {
    kotlin("multiplatform") version "1.4.21" apply false
    id("maven-publish")
    id("net.akehurst.kotlin.kt2ts") version ("1.6.0")
}
allprojects {
    group = "org.jeasy"
    version = "4.1.1-SNAPSHOT"
    repositories {
        mavenLocal()
        mavenCentral()
        jcenter()
    }
    extra.set("kotlin_version" ,"1.4.21")
    extra["kotlin_logging_version"] = "2.0.4"
    extra["mockk_version"] = "1.10.4"

}
subprojects {
    apply {
        plugin("maven-publish")
    }


}
