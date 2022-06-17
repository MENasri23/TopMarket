import java.io.FileInputStream
import java.util.*

plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("dagger.hilt.android.plugin")
    kotlin("kapt")
}

android {
    compileSdk = App.COMPILE_SDK

    val apiProperties = getProperties("${rootDir}/api.properties")

    defaultConfig {
        minSdk = App.MIN_SDK
        targetSdk = App.TARGET_SDK

        buildConfigField("String", "BASE_URL", apiProperties["base_url"] as String)
        buildConfigField("String", "CONSUMER_KEY", apiProperties["consumer_key"] as String)
        buildConfigField("String", "CONSUMER_SECRET", apiProperties["consumer_secret"] as String)

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

}