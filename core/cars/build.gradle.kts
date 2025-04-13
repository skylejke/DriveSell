plugins {
    kotlin("plugin.serialization") version "2.1.10"
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "ru.point.cars"
    compileSdk = 35

    defaultConfig {
        minSdk = 30
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlinOptions {
        jvmTarget = "11"
    }
    
    buildFeatures {
        buildConfig = true
        viewBinding = true
    }

    defaultConfig {
        buildConfigField("String", "BASE_URL", "\"http://192.168.1.23:8080\"")
    }
}

dependencies {
    implementation(libs.kotlinx.serialization)
    implementation(libs.kotlinx.serialization.json)

    implementation(libs.coil)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
}