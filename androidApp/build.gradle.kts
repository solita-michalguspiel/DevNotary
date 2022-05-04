plugins {
    id("com.android.application")
    kotlin("android")
    id("com.google.gms.google-services")
}

val composeVersion = "1.1.1"

android {
    compileSdk = 32
    defaultConfig {
        applicationId = "com.solita.devnotary.android"
        minSdk = 21
        targetSdk = 32
        versionCode = 1
        versionName = "1.0"
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    // Added for Jetpack Compose
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = composeVersion
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }

}

dependencies {
    implementation(project(":shared"))
    implementation("com.google.android.material:material:1.4.0")
    implementation("androidx.appcompat:appcompat:1.3.1")
    implementation("androidx.constraintlayout:constraintlayout:2.1.0")

    //Compose
    implementation("androidx.activity:activity-compose:1.4.0")
    implementation("androidx.compose.material:material:$composeVersion")
    implementation("androidx.compose.ui:ui-tooling:$composeVersion")
    //NAVIGATION
    val nav_version = "2.4.2"
    implementation("androidx.navigation:navigation-compose:$nav_version")

    // FIREBASE
    implementation("com.google.firebase:firebase-common-ktx:20.1.0")

    //Key-Value storage
    implementation("com.russhwolf:multiplatform-settings:0.9")
}