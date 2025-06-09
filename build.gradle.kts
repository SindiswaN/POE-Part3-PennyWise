plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.google.gms.google.services)

}

android {
    namespace = "vcmsa.projects.sindiswasinazo"
    compileSdk = 35
    buildFeatures {
        viewBinding = true
    }

    defaultConfig {
        applicationId = "vcmsa.projects.sindiswasinazo"
        minSdk = 24
        targetSdk = 35
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
    kotlinOptions {
        jvmTarget = "11"
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.firebase.database)
    implementation ("androidx.lifecycle:lifecycle-runtime-ktx:2.9.1")
    implementation("com.google.firebase:firebase-auth-ktx:22.1.1")
    implementation("com.google.firebase:firebase-database-ktx:21.0.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-play-services:1.7.3")
    implementation ("com.github.bumptech.glide:glide:4.15.1")  // or latest stable version

    implementation ("com.google.firebase:firebase-database-ktx")


    implementation ("com.diogobernardino:williamchart:3.11.0")
// Firebase BOM - manage firebase libs versions automatically
    implementation (platform("com.google.firebase:firebase-bom:32.1.1"))

    // Firebase libs without versions (will use BOM version)
    implementation ("com.google.firebase:firebase-auth-ktx")
    implementation ("com.google.firebase:firebase-storage-ktx")

    // Firebase App Check libs - required for Storage to work properly
    implementation ("com.google.firebase:firebase-appcheck-interop")
    implementation ("com.google.firebase:firebase-appcheck-debug")
    implementation(libs.firebase.firestore.ktx)

    apply(plugin = "com.google.gms.google-services")






    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}

