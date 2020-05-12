plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("android.extensions")
    kotlin("plugin.serialization")
}

android {
    compileSdkVersion(29)
    defaultConfig {
        applicationId = "com.racepartyapp"
        minSdkVersion(21)
        targetSdkVersion(29)
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
        }
    }
    sourceSets["main"].java.srcDir("src/main/kotlin")
}

dependencies {
    implementation(project(":shared", "jvmRuntime"))
    implementation(project(":client", "androidRuntime"))
    implementation("androidx.appcompat:appcompat:1.1.0")
    implementation("androidx.constraintlayout:constraintlayout:1.1.3")
    implementation("androidx.localbroadcastmanager:localbroadcastmanager:1.0.0")
    implementation("com.google.android.gms:play-services-location:17.0.0")
    implementation("io.ktor:ktor-client-android:${Versions.KTOR}")
    testImplementation("junit:junit:4.12")
    androidTestImplementation("androidx.test:runner:1.2.0")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.2.0")
}
