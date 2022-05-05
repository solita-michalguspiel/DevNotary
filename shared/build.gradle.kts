plugins {
    kotlin("multiplatform")
    kotlin("native.cocoapods")
    id("com.android.library")
    id("com.squareup.sqldelight")
    kotlin("plugin.serialization") version "1.6.10"
    id("io.kotest.multiplatform") version "5.0.2"
}

version = "1.0"

object SqlDelight {
    const val version = "1.5.3"
    const val runtime = "com.squareup.sqldelight:runtime:$version"
    const val android = "com.squareup.sqldelight:android-driver:$version"
    const val native = "com.squareup.sqldelight:native-driver:$version"
}


kotlin {
    android()
    iosX64()
    iosArm64()
    iosSimulatorArm64()

    cocoapods {
        summary = "Some description for the Shared Module"
        homepage = "Link to the Shared Module homepage"
        ios.deploymentTarget = "14.1"
        podfile = project.file("../iosApp/Podfile")
        framework {
            baseName = "shared"
        }
    }

    sourceSets["commonMain"].dependencies {
        // Coroutines:
        implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.1")

        // DI with kodein:
        implementation("org.kodein.di:kodein-di:7.10.0")
        // Firebase auth and firestore:
        implementation("dev.gitlive:firebase-auth:1.6.1")
        implementation("dev.gitlive:firebase-firestore:1.6.1")
        // Database with sqldelight:
        implementation(SqlDelight.runtime)
        // Kotlinx-datetime:
        implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.3.2")
        //Key-value data storage
        implementation("com.russhwolf:multiplatform-settings-no-arg:0.9")
        //Serialization
        implementation ("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.2")
    }

    sourceSets["commonTest"].dependencies {
        implementation ("io.kotest:kotest-assertions-core:5.3.0")
        implementation("com.russhwolf:multiplatform-settings-test:0.9")
        implementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.6.1")
        implementation("org.xerial:sqlite-jdbc:3.34.0")
        implementation(SqlDelight.runtime)
    }

    sourceSets["androidMain"].dependencies {
        implementation(SqlDelight.android)
        implementation("org.kodein.di:kodein-di-framework-compose:7.10.0")
    }

    sourceSets["androidTest"].dependencies {
        implementation("androidx.test:core:1.4.0")
        implementation("org.robolectric:robolectric:4.8")
        }

    sourceSets {
        val commonMain by getting
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
                implementation ("io.kotest:kotest-assertions-core:5.3.0")
                implementation("com.russhwolf:multiplatform-settings-test:0.9")
            }
        }
        val androidMain by getting
        val androidTest by getting
        val iosX64Main by getting
        val iosArm64Main by getting
        val iosSimulatorArm64Main by getting
        val iosMain by creating {
            dependsOn(commonMain)
            iosX64Main.dependsOn(this)
            iosArm64Main.dependsOn(this)
            iosSimulatorArm64Main.dependsOn(this)
        }
        val iosX64Test by getting
        val iosArm64Test by getting
        val iosSimulatorArm64Test by getting
        val iosTest by creating {
            dependsOn(commonTest)
            iosX64Test.dependsOn(this)
            iosArm64Test.dependsOn(this)
            iosSimulatorArm64Test.dependsOn(this)
        }
    }
}

dependencies {
    commonMainApi("dev.icerock.moko:mvvm-core:0.13.0") // only ViewModel, EventsDispatcher, Dispatchers.UI
    commonMainApi("dev.icerock.moko:mvvm-flow:0.13.0") // api mvvm-core, CFlow for native and binding extensions
    commonMainApi("dev.icerock.moko:mvvm-livedata:0.13.0") // api mvvm-core, LiveData and extensions
    commonMainApi("dev.icerock.moko:mvvm-state:0.13.0") // api mvvm-livedata, ResourceState class and extensions
    commonMainApi("dev.icerock.moko:mvvm-livedata-resources:0.13.0") // api mvvm-core, moko-resources, extensions for LiveData with moko-resources
    commonTestImplementation("dev.icerock.moko:mvvm-test:0.13.0") // test utilities
}

android {
    compileSdk = 32
    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    defaultConfig {
        minSdk = 21
        targetSdk = 32
    }
    testOptions{
        unitTests{
            isIncludeAndroidResources = true
        }
    }

}

sqldelight {
    database("dev_notary_db") { // This will be the name of the generated database class.
        packageName = "com.solita.devnotary"
    }
}