plugins {
    alias(libs.plugins.android.application)
    id("org.jetbrains.kotlin.android") // Kotlin 플러그인 추가
    id ("kotlin-kapt")
    id ("com.google.dagger.hilt.android")
}

android {
    namespace = "com.example.crudapplication"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.crudapplication"
        minSdk = 24
        targetSdk = 34
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8" // Kotlin JVM 타겟 설정
    }
}

kapt {
    correctErrorTypes = true
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

    // Retrofit
    implementation("com.squareup.retrofit2:converter-gson:2.11.0")
    implementation ("com.squareup.retrofit2:retrofit:2.11.0")

    // Hilt
    implementation ("com.google.dagger:hilt-android:2.51.1")
    kapt ("com.google.dagger:hilt-compiler:2.51.1")

    // ViewModels KTX
//    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.8.7") // by viewModels() 지원
//    implementation ("androidx.core:core-ktx:1.15.0")
//    implementation ("androidx.activity:activity-ktx:1.9.3")
//    implementation ("androidx.fragment:fragment-ktx:1.8.5")
}