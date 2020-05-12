import org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpack

plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization")
}

kotlin {
    // jvm is only included in order to package up the compiled JS client artifact so it can be consumed by the server
    jvm()

    js {
        produceExecutable()
        browser {
            // Workaround for old JS compiler backend and https://github.com/ktorio/ktor/issues/1400
            @Suppress("EXPERIMENTAL_API_USAGE")
            dceTask {
                keep("ktor-ktor-io.\$\$importsForInline\$\$.ktor-ktor-io.io.ktor.utils.io")
            }
        }
        compilations.all {
            kotlinOptions {
                // Limitation of JS compiler IR backend in 1.4-M1
                sourceMap = false
            }
        }
    }

    targets.all {
        compilations.all {
            kotlinOptions {
                apiVersion = "1.4"
                languageVersion = "1.4"
                allWarningsAsErrors = true
            }
        }
    }
    sourceSets {
        @Suppress("UNUSED_VARIABLE")
        val jsMain by getting {
            dependencies {
                api(project(":shared", "jsRuntime"))
                api("org.jetbrains.kotlinx:kotlinx-coroutines-core-js:${Versions.COROUTINES}")
                api("io.ktor:ktor-client-js:${Versions.KTOR}")
                api("io.ktor:ktor-client-json-js:${Versions.KTOR}")
                api("io.ktor:ktor-client-serialization-js:${Versions.KTOR}")

                // Workarounds for https://github.com/ktorio/ktor/issues/1400
                implementation(npm("text-encoding", "0.7.0"))
                implementation(npm("abort-controller", "3.0.0"))
            }
        }

        @Suppress("UNUSED_VARIABLE")
        val jsTest by getting {
            dependencies {
                implementation(kotlin("test-js"))
            }
        }
    }
}

val jsBrowserProductionWebpack = tasks.getByName<KotlinWebpack>("jsBrowserProductionWebpack") {
    outputFileName = "web.js"
}

val jsBrowserDevelopmentWebpack = tasks.getByName<KotlinWebpack>("jsBrowserDevelopmentWebpack") {
    outputFileName = "web.js"
}

val jvmJar = tasks.getByName<Jar>("jvmJar") {
    val jsBrowserWebpack =
        if (project.findProperty("production") == "true")
            jsBrowserProductionWebpack
        else
            jsBrowserDevelopmentWebpack

    dependsOn(jsBrowserWebpack)
    from(jsBrowserWebpack.outputFile)
}