buildscript {
    dependencies {
        classpath("com.android.tools.build:gradle:3.6.3")
        classpath(kotlin("gradle-plugin", version = Versions.KOTLIN))
    }
}

allprojects {
    group = "com.racepartyapp"
    version = "0.0.0-SNAPSHOT"

    repositories {
        google()
        jcenter()
        mavenCentral()
        maven("https://dl.bintray.com/kotlin/kotlin-eap")
        maven("https://kotlin.bintray.com/kotlinx")
        maven("https://kotlin.bintray.com/kotlin-js-wrappers")
    }
}

plugins {
    kotlin("jvm") version Versions.KOTLIN apply false
    kotlin("multiplatform") version Versions.KOTLIN apply false
    kotlin("plugin.serialization") version Versions.KOTLIN apply false
}