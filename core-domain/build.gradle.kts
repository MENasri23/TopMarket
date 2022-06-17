plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("dagger.hilt.android.plugin")
    kotlin("kapt")
}

android {
    compileSdk = App.COMPILE_SDK

    defaultConfig {
        minSdk = App.MIN_SDK
        targetSdk = App.TARGET_SDK

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
        isCoreLibraryDesugaringEnabled = true
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {

    implementation(project(":core-data"))
    implementation(project(":core-model"))
    implementation(project(":core-shared"))
    implementation(project(":core-database"))
    implementation(project(":core-network"))


    // legacy
    implementation(Libs.Androidx.LEGACY_SUPPORT)
    coreLibraryDesugaring(Libs.Core.DESUGAR)

    // coroutine
    implementation(Libs.Kotlinx.COROUTINES_ANDROID)

    // data store
    implementation(Libs.Androidx.DataStore.DATA_STORE)

    // work manager
    implementation(Libs.Androidx.WorkManager.RUNTIME_KTX)

    // hilt
    implementation(Libs.Hilt.HILT)
    kapt(Libs.Hilt.HILT_COMPILER)
    kapt(Libs.Hilt.ANDROIDX_HILT_COMPILER)
    implementation(Libs.Hilt.WORK_MANAGER)

    // timber
    implementation(Libs.Debug.TIMBER)
}