plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("com.google.dagger.hilt.android")
    kotlin("kapt")
    id("com.google.gms.google-services")
}

android {
    namespace = "nhn.ntech.cinehub"
    compileSdk {
        version = release(36)
    }

    defaultConfig {
        applicationId = "nhn.ntech.cinehub"
        minSdk = 23
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
    kotlin {
        compilerOptions {
            jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_11)
            languageVersion.set(org.jetbrains.kotlin.gradle.dsl.KotlinVersion.KOTLIN_1_9)
        }
    }


    buildFeatures {
        dataBinding = true
        viewBinding = true
    }
}

dependencies {

    // Indicator
//    implementation("com.tbuonomo:dotsindicator:5.0")

    // Scalable Size Unit
    implementation("com.intuit.sdp:sdp-android:1.1.1")
    implementation("com.intuit.ssp:ssp-android:1.1.1")

    // Network
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.okhttp3:okhttp:4.12.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")

    // Navigation
    implementation("androidx.navigation:navigation-fragment:2.9.6")
    implementation("androidx.navigation:navigation-ui:2.9.6")
    implementation("androidx.navigation:navigation-dynamic-features-fragment:2.9.6")

    // Glide
    implementation("com.github.bumptech.glide:glide:5.0.5")

    // Dagger Hilt
    implementation("com.google.dagger:hilt-android:2.57.1")
    implementation(libs.firebase.auth)
    kapt("com.google.dagger:hilt-android-compiler:2.57.1")

    // Firebase
    implementation(platform("com.google.firebase:firebase-bom:34.7.0"))
    implementation("com.google.firebase:firebase-analytics")

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}