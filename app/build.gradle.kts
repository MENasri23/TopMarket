import Libs.Androidx.implementAppLibraries
import java.io.FileInputStream
import java.util.Properties

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("androidx.navigation.safeargs")
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

        javaCompileOptions {
            annotationProcessorOptions {
                arguments += mapOf(
                    "room.schemaLocation" to "$projectDir/schemas",
                    "room.incremental" to "true",
                    "room.expandProjection" to "true"
                )
            }
        }


        signingConfigs {
            create("staging") {
                val keystoreProperties = getProperties("${rootDir}/keystore.properties")
                keyAlias = keystoreProperties["keyAlias"] as String
                keyPassword = keystoreProperties["keyPassword"] as String
                storeFile = file(keystoreProperties["storeFile"] as String)
                storePassword = keystoreProperties["keyPassword"] as String
            }

        }
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )

        }
        create("staging") {
            initWith(getByName("debug"))
            matchingFallbacks.add("debug")
            signingConfig = signingConfigs.getByName("staging")
        }
        sourceSets {
            getByName("staging") {
                java.srcDir("src/release/java")
            }
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

    buildFeatures {
        viewBinding = true
        dataBinding = true
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
    coreLibraryDesugaring(Libs.Core.DESUGAR)

    // ktx
    implementation(Libs.Androidx.ACTIVITY_KTX)
    implementation(Libs.Androidx.FRAGMENT_KTX)

    // lifecycle
    implementation(Libs.Androidx.Lifecycle.RUNTIME)
    implementation(Libs.Androidx.Lifecycle.LIVEDATA_KTX)
    implementation(Libs.Androidx.Lifecycle.VIEW_MODEL_KTX)
    implementation(Libs.Androidx.Lifecycle.SAVED_STATE)

    // navigation
    implementation(Libs.Androidx.Navigation.FRAGMENT_KTX)
    implementation(Libs.Androidx.Navigation.RUNTIME_KTX)
    implementation(Libs.Androidx.Navigation.UI_KTX)

    // coroutine
    implementation(Libs.Kotlinx.COROUTINES_ANDROID)

    // room
    implementation(Libs.Androidx.Room.RUNTIME)
    implementation(Libs.Androidx.Room.KTX)
    kapt(Libs.Androidx.Room.COMPILER)

    // data store
    implementation(Libs.Androidx.DataStore.DATA_STORE)

    // work manager
    implementation(Libs.Androidx.WorkManager.RUNTIME_KTX)


    // hilt
    implementation(Libs.Hilt.HILT)
    implementation(Libs.Hilt.NAVIGATION)
    implementation(Libs.Hilt.WORK_MANAGER)
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

    // intuit for text and unit sizes
    implementation(Libs.Intuit.SDP)
    implementation(Libs.Intuit.SSP)

    // lottie
    implementation(Libs.Lottie.LOTTIE)

    // timber
    implementation(Libs.Debug.TIMBER)

    // slider indicator
    implementation(Libs.Indicator.INDICATOR)

    testImplementation(Libs.Test.JUNIT)
    testImplementation(Libs.Kotlinx.COROUTINES_TEST)
    testImplementation(Libs.Test.TRUTH)
    androidTestImplementation(Libs.Androidx.Test.JUNIT_EXT)
    androidTestImplementation(Libs.Androidx.Test.ESPRESSO)
}