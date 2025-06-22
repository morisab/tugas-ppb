plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.ksp)
    alias(libs.plugins.hilt)
}

android {
    namespace = "com.example.starbucks"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.starbucks"
        minSdk = 25
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

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.composeCompiler.get()
    }
}

// Force consistent versions to avoid conflicts
configurations.all {
    resolutionStrategy {
        force("com.squareup:javapoet:1.13.0")
        force("com.google.dagger:hilt-android:2.48")
        force("com.google.dagger:hilt-compiler:2.48")

        eachDependency {
            if (requested.group == "com.squareup" && requested.name == "javapoet") {
                useVersion("1.13.0")
                because("Force JavaPoet version to avoid Hilt conflicts")
            }
            if (requested.group == "com.google.dagger") {
                useVersion("2.48")
                because("Force Dagger/Hilt version consistency")
            }
        }
    }
}

dependencies {
    // AndroidX Core & Lifecycle
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)

    // Jetpack Compose
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.activity.compose)

    // Lifecycle & ViewModel
    implementation(libs.androidx.lifecycle.viewmodel.compose)

    // Navigation
    implementation(libs.androidx.navigation.compose)

    // Local Authentication Dependencies (Replacing Firebase)
    implementation(libs.androidx.security.crypto)  // Encrypted SharedPreferences
    implementation(libs.gson)                      // JSON serialization
    implementation(libs.kotlinx.coroutines.android) // Coroutines

    // Dependency Injection - Hilt (with KSP)
    implementation("com.squareup:javapoet:1.13.0")
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)
    implementation(libs.hilt.navigation.compose)

    // Testing
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)

    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}