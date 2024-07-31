plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.tastebase"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.tastebase"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {
    implementation (libs.recyclerview)
    implementation (libs.material.v14)
    implementation (libs.core.v190)  // Check for the latest version
    implementation (libs.core)
    implementation (libs.recyclerview.v130)

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.cardview)
    implementation(libs.core)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    implementation (libs.material.v190)
    androidTestImplementation(libs.espresso.core)
}