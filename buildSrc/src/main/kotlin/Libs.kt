import org.gradle.api.artifacts.dsl.DependencyHandler

object Libs {

    object Core {
        private const val version = "1.1.5"
        const val DESUGAR = "com.android.tools:desugar_jdk_libs:${version}"
    }

    object Test {
        private const val junitVersion = "4.13.2"
        private const val robolectricVersion = "4.7.3"
        private const val truthVersion = "1.1.3"


        const val JUNIT = "junit:junit:${junitVersion}"


        const val ROBOLECTRIC = "org.robolectric:robolectric:$robolectricVersion"
        const val TRUTH = "com.google.truth:truth:$truthVersion"

    }

    object Debug {
        private const val timberVersion = "5.0.1"
        const val TIMBER = "com.jakewharton.timber:timber:${timberVersion}"
    }

    object Androidx {
        private const val coreKtxVersion = "1.7.0"
        private const val appcompatVersion = "1.4.1"
        private const val materialVersion = "1.5.0"
        private const val activityVersion = "1.4.0"
        private const val fragmentVersion = "1.4.1"
        private const val legacySupportVersion = "1.0.0"
        private const val constraintLayoutVersion = "2.1.3"

        const val CORE_KTX = "androidx.core:core-ktx:${coreKtxVersion}"
        const val APPCOMPAT = "androidx.appcompat:appcompat:${appcompatVersion}"
        const val ANDROID_MATERIAL = "com.google.android.material:material:${materialVersion}"
        const val ACTIVITY_KTX = "androidx.activity:activity-ktx:${activityVersion}"
        const val FRAGMENT_KTX = "androidx.fragment:fragment-ktx:${fragmentVersion}"
        const val LEGACY_SUPPORT = "androidx.legacy:legacy-support-v4:${legacySupportVersion}"
        const val CONSTRAINT_LAYOUT =
            "androidx.constraintlayout:constraintlayout:${constraintLayoutVersion}"

        object Test {
            private const val version = "1.4.0"
            private const val archCoreVersion = "2.1.0"
            private const val junitExtVersion = "1.1.3"
            private const val junitKtxVersion = "1.1.3"
            private const val espressoVersion = "3.4.0"

            const val JUNIT_EXT = "androidx.test.ext:junit:$junitExtVersion"
            const val JUNIT_EXT_KTX = "androidx.test.ext:junit-ktx:$junitKtxVersion"
            const val CORE_KTX = "androidx.test:core-ktx:${version}"
            const val ARCH_CORE = "androidx.arch.core:core-testing:${archCoreVersion}"
            const val RULES = "androidx.test:rules:${version}"
            const val RUNNER = "androidx.test:runner:${version}"

            const val ESPRESSO = "androidx.test.espresso:espresso-core:$espressoVersion"
        }

        object Lifecycle {
            private const val version = "2.4.1"
            const val RUNTIME = "androidx.lifecycle:lifecycle-runtime-ktx:${version}"
            const val LIVEDATA_KTX = "androidx.lifecycle:lifecycle-livedata-ktx:${version}"
            const val VIEW_MODEL_KTX = "androidx.lifecycle:lifecycle-viewmodel-ktx:${version}"
            const val SAVED_STATE = "androidx.lifecycle:lifecycle-viewmodel-savedstate:$version"
        }

        object Navigation {
            const val version = "2.4.2"
            const val RUNTIME_KTX = "androidx.navigation:navigation-runtime-ktx:${version}"
            const val FRAGMENT_KTX = "androidx.navigation:navigation-fragment-ktx:${version}"
            const val UI_KTX = "androidx.navigation:navigation-ui-ktx:${version}"
        }

        object Room {
            private const val version = "2.4.2"
            const val RUNTIME = "androidx.room:room-runtime:${version}"
            const val COMPILER = "androidx.room:room-compiler:${version}"
            const val KTX = "androidx.room:room-ktx:${version}"
            const val TEST = "androidx.room:room-testing:${version}"
            const val COMMON = "androidx.room:room-common:${version}"
        }

        private const val IMPLEMENTATION = "implementation"

        fun DependencyHandler.implementAppLibraries() {
            add(IMPLEMENTATION, CORE_KTX)
            add(IMPLEMENTATION, APPCOMPAT)
            add(IMPLEMENTATION, ANDROID_MATERIAL)
            add(IMPLEMENTATION, CONSTRAINT_LAYOUT)
        }
    }

    object Kotlinx {
        private const val version = "1.6.0"
        const val COROUTINES_ANDROID = "org.jetbrains.kotlinx:kotlinx-coroutines-android:${version}"
        const val COROUTINES_TEST = "org.jetbrains.kotlinx:kotlinx-coroutines-test:${version}"
    }

    object Retrofit {
        private const val version = "2.9.0"
        const val GSON = "com.google.code.gson:gson:${version}"
        const val RETROFIT2 = "com.squareup.retrofit2:retrofit:${version}"
        const val GSON_CONVERTER = "com.squareup.retrofit2:converter-gson:${version}"
    }

    object OkHttp {
        private const val version = "4.9.3"
        private const val testVersion = "3.8.1"
        const val OKHTTP3 = "com.squareup.okhttp3:okhttp:${version}"
        const val LOGGING_INTERCEPTOR = "com.squareup.okhttp3:logging-interceptor:${version}"
        const val MOCK_WEB_SERVER = "com.squareup.okhttp3:mockwebserver:${testVersion}"
    }

    object Hilt {
        const val version = "2.41"
        private const val hiltNavigationVersion = "1.0.0"
        private const val androidXCompilerVersion = "1.0.0"
        const val HILT = "com.google.dagger:hilt-android:${version}"
        const val NAVIGATION = "androidx.hilt:hilt-navigation-fragment:$hiltNavigationVersion"
        const val HILT_COMPILER = "com.google.dagger:hilt-compiler:${version}"
        const val ANDROIDX_HILT_COMPILER = "androidx.hilt:hilt-compiler:${androidXCompilerVersion}"
        const val TEST = "com.google.dagger:hilt-android-testing:${version}"
    }

    object Glide {
        private const val glideVersion = "4.13.0"
        const val GLIDE = "com.github.bumptech.glide:glide:${glideVersion}"
        const val COMPILER = "com.github.bumptech.glide:compiler:${glideVersion}"
    }

    object Intuit {
        private const val version = "1.0.6"
        const val SDP = "com.intuit.sdp:sdp-android:$version"
        const val SSP = "com.intuit.ssp:ssp-android:$version"
    }

    object Lottie {
        private const val lottieVersion = "3.4.0"
        const val LOTTIE = "com.airbnb.android:lottie:$lottieVersion"
    }

    object ImageSlider {
        private const val version = "1.4.0"
        const val AUTO_IMAGE_SLIDER = "com.github.smarteist:autoimageslider:$version"
    }

    object Indicator {
        private const val version = "4.3"
        const val INDICATOR = "com.tbuonomo:dotsindicator:${version}"
    }

}