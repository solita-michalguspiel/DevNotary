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
    implementation ("androidx.constraintlayout:constraintlayout-compose:1.0.0")

    implementation ("androidx.compose.material:material-icons-extended:$composeVersion")

    //NAVIGATION
    val nav_version = "2.4.2"
    implementation("androidx.navigation:navigation-compose:$nav_version")
    implementation ("com.google.accompanist:accompanist-navigation-animation:0.24.9-beta")

    // FIREBASE
    implementation("com.google.firebase:firebase-common-ktx:20.1.0")

    //Key-Value storage
    implementation("com.russhwolf:multiplatform-settings:0.9")
    // kodein
    implementation("org.kodein.di:kodein-di:7.10.0")
    implementation("org.kodein.di:kodein-di-framework-compose:7.10.0")

    //DateTime
    implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.3.2")

    //Accompanist
    implementation ("com.google.accompanist:accompanist-swiperefresh:0.24.8-beta")
    implementation ("com.google.accompanist:accompanist-systemuicontroller:0.24.9-beta")

    //GSON
    implementation ("com.google.code.gson:gson:2.8.9")



}