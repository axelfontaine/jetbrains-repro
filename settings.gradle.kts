rootProject.name = "jetbrains-repro"

include(":shared", ":client", ":js", ":android", ":server")

pluginManagement {
    repositories {
        google()
        jcenter()
        mavenCentral()
        gradlePluginPortal()
        maven("https://dl.bintray.com/kotlin/kotlin-eap")
    }
}