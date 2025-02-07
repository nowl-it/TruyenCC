plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.lukakuku.truyencc"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.lukakuku.truyencc"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        signingConfig = signingConfigs.getByName("debug")
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures {
        viewBinding = true
    }
    dependenciesInfo {
        includeInApk = true
        includeInBundle = true
    }

    buildToolsVersion = "34.0.0"
    ndkVersion = "28.0.12674087 rc2"
}

dependencies {
    implementation(libs.swiperefreshlayout)
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.navigation.fragment)
    implementation(libs.navigation.ui)
    implementation(libs.retrofit)
    implementation(libs.converter.scalars)
    implementation(libs.org.jetbrains.kotlin.plugin.serialization.gradle.plugin)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.volley)
    implementation(libs.gson)
    implementation(libs.converter.gson)
    implementation(libs.retrofit2.converter.gson)
    implementation(libs.glide)
    implementation(libs.core)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}

