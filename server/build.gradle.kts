import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val logbackVersion = "1.2.3"

plugins {
    kotlin("jvm")
    kotlin("plugin.serialization")
    application
}

tasks.withType<KotlinCompile>().configureEach {
    kotlinOptions {
        jvmTarget = Versions.JVM
        apiVersion = "1.4"
        languageVersion = "1.4"
        allWarningsAsErrors = true
    }
}

dependencies {
    implementation(project(":shared", "jvmRuntime"))
    implementation(project(":js", "jvmRuntime"))
    implementation("io.ktor:ktor-server-netty:${Versions.KTOR}")
    implementation("io.ktor:ktor-serialization:${Versions.KTOR}")
    implementation("io.ktor:ktor-html-builder:${Versions.KTOR}")
    implementation("ch.qos.logback:logback-classic:$logbackVersion")
    testImplementation(kotlin("test"))
    testImplementation(kotlin("test-junit"))
}

application {
    mainClassName = "com.racepartyapp.server.MainKt"
}
