import Libs.Androidx.implementAppLibraries
import java.io.FileInputStream
import java.util.Properties

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("dagger.hilt.android.plugin")
    kotlin("kapt")
}

android {
    compileSdk = App.COMPILE_SDK

    val apiProperties = getProperties("${rootDir}/api.properties")

    defaultConfig {
        applicationId = App.APPLICATION_ID
        minSdk = App.MIN_SDK
        targetSdk = App.TARGET_SDK
        versionCode = App.VERSION_CODE
        versionName = App.versionName

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        buildConfigField("String", "BASE_URL", apiProperties["base_url"] as String)
        buildConfigField("String", "CONSUMER_KEY", apiProperties["consumer_key"] as String)
        buildConfigField("String", "CONSUMER_SECRET", apiProperties["consumer_secret"] as String)
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

fun getProperties(file: String = "local.properties"): Properties {
    val properties = Properties()
    val propertiesFile = File(file)
    if (propertiesFile.isFile) {
        properties.load(FileInputStream(propertiesFile))
    } else error("File from not found")

    return properties
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
    testImplementation(Libs.Kotlinx.COROUTINES_TEST)
    testImplementation(Libs.Test.TRUTH)
    androidTestImplementation(Libs.Androidx.Test.JUNIT_EXT)
    androidTestImplementation(Libs.Androidx.Test.ESPRESSO)
}