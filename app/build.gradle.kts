plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    id("kotlin-kapt")
}

android {
    namespace = "com.example.tmdbdemotataaig"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.tmdbdemotataaig"
        minSdk = 28
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
        buildConfigField("String", "API_KEY", "\"YOUR_API_KEY\"")
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }

    buildFeatures {
        viewBinding = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    // android fast networking
    // GSON
    implementation (libs.gson)
    //Networking library
    implementation (libs.android.networking)
    implementation (libs.jackson.android.networking)

    implementation(libs.androidx.core.ktx.v190)
    implementation(libs.material.v190)

    // glide for loading the images
    implementation(libs.glide)

    // for movie details card
    implementation(libs.androidx.cardview)

    // Room Database
    implementation(libs.androidx.room.runtime)
    kapt ("androidx.room:room-compiler:2.6.1")
    implementation(libs.androidx.room.ktx)



}