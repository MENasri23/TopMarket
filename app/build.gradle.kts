import Libs.Androidx.implementAppLibraries

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("dagger.hilt.android.plugin")
    kotlin("kapt")
}

android {
    compileSdk = App.COMPILE_SDK

    defaultConfig {
        applicationId = App.APPLICATION_ID
        minSdk = App.MIN_SDK
        targetSdk = App.TARGET_SDK
        versionCode = App.VERSION_CODE
        versionName = App.versionName

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        getByName("release") {
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
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {

    implementAppLibraries()

    // legacy
    implementation(Libs.Androidx.LEGACY_SUPPORT)

    // ktx
    implementation(Libs.Androidx.ACTIVITY_KTX)
    implementation(Libs.Androidx.FRAGMENT_KTX)

    // lifecycle
    implementation(Libs.Androidx.Lifecycle.RUNTIME)
    implementation(Libs.Androidx.Lifecycle.VIEW_MODEL_KTX)

    // navigation
    implementation(Libs.Androidx.Navigation.FRAGMENT_KTX)
    implementation(Libs.Androidx.Navigation.RUNTIME_KTX)
    implementation(Libs.Androidx.Navigation.UI_KTX)

    // coroutine
    implementation(Libs.Kotlinx.COROUTINES_ANDROID)


    // hilt
    implementation(Libs.Hilt.HILT)
    kapt(Libs.Hilt.HILT_COMPILER)
    kapt(Libs.Hilt.ANDROIDX_HILT_COMPILER)

    // okHttp
    implementation(Libs.OkHttp.OKHTTP3)
    implementation(Libs.OkHttp.LOGGING_INTERCEPTOR)

    // retrofit
    implementation(Libs.Retrofit.RETROFIT2)
    implementation(Libs.Retrofit.GSON_CONVERTER)
    implementation(Libs.Retrofit.GSON)

    // glide
    implementation(Libs.Glide.GLIDE)
    kapt(Libs.Glide.COMPILER)

    // timber
    implementation(Libs.Debug.TIMBER)

    testImplementation(Libs.Test.JUNIT)
    androidTestImplementation(Libs.Androidx.Test.JUNIT_EXT)
    androidTestImplementation(Libs.Androidx.Test.ESPRESSO)
}