plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization")
}

kotlin {
    jvm("android") {
        compilations.all {
            kotlinOptions.jvmTarget = "1.8"
        }
    }
    js {
        browser()
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
        commonMain {
            dependencies {
                api(project(":shared"))
            }
        }
        commonTest {
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
            }
        }

        @Suppress("UNUSED_VARIABLE")
        val androidMain by getting {
            dependencies {
                api(project(":shared", "jvmRuntime"))
            }
        }

        @Suppress("UNUSED_VARIABLE")
        val androidTest by getting {
            dependencies {
                implementation(kotlin("test"))
                implementation(kotlin("test-junit"))
            }
        }

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
