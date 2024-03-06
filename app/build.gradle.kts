plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    alias(libs.plugins.dagger.hilt.android)
    id("kotlin-kapt")
}

android {
    namespace = "com.example.weatherinfo"
    compileSdk = 34

    signingConfigs {
        create(KeyStore.RELEASE) {
            keyPassword = "weather"
            storeFile = file("D:\\Weather_KeyStore\\debug.keystore.jks")
            storePassword = "weather"
            keyAlias = "weather_keystore"
        }

        create(KeyStore.DEV) {
            keyAlias = "weather_keystore"
            keyPassword = "weather"
            storeFile = file("D:\\Weather_KeyStore\\release.keystore.jks")
            storePassword = "weather"
        }
    }

    defaultConfig {
        applicationId = "com.example.weatherinfo"
        minSdk = 29
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            isDebuggable = false
            applicationIdSuffix = ".weather.release"
            val apiVersion = "2.5"
            buildConfigField("String", "APIKEY", "\"9b8cb8c7f11c077f8c4e217974d9ee40\"")
            buildConfigField(
                "String",
                "BASEURL",
                "\"https://api.openweathermap.org/data/${apiVersion}/\""
            )
            buildConfigField("String", "APPLICATION_ID", "\"com.example.weatherinfo\"")
            buildConfigField("String", "API_VERSION", "\"${apiVersion}\"")
            buildConfigField("boolean", "LOG", "true")
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro", "retrofit2.pro"
            )
            signingConfig = signingConfigs.getByName(KeyStore.RELEASE)
        }

        debug {
            isMinifyEnabled = true
            isShrinkResources = true
            isDebuggable = true
            applicationIdSuffix = ".weather.debug"
            val apiVersion = "2.5"
            buildConfigField("String", "APIKEY", "\"9b8cb8c7f11c077f8c4e217974d9ee40\"")
            buildConfigField(
                "String",
                "BASEURL",
                "\"https://api.openweathermap.org/data/${apiVersion}/\""
            )
            buildConfigField("String", "APPLICATION_ID", "\"com.example.weatherinfo\"")
            buildConfigField("String", "API_VERSION", "\"${apiVersion}\"")
            buildConfigField("boolean", "LOG", "true")
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro", "retrofit2.pro"
            )
            signingConfig = signingConfigs.getByName(KeyStore.DEV)
        }

    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }

    buildFeatures {
        viewBinding = true
        buildConfig = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.bundles.lifecle)

    implementation(libs.logging.interceptor)
    implementation(libs.retrofit)
    implementation(libs.converter.gson)

    implementation(libs.lifecycle.livedata.ktx)

    implementation(libs.hilt.android)
    kapt(libs.hilt.android.compiler)

    implementation(libs.androidx.fragment.ktx)
    implementation(libs.androidx.activity.ktx)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}

object KeyStore {
    const val RELEASE = "release"
    const val DEV = "dev"
}