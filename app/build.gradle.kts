plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
   id("com.google.gms.google-services")


}

android {
    namespace = "com.example.librarymanagementsystem"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.librarymanagementsystem"
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
        jvmTarget = "1.8"
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.10.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("com.google.firebase:firebase-common-ktx:20.4.2")
//    implementation("com.google.firebase:firebase-firestore-ktx:24.9.1")
    implementation("com.google.firebase:firebase-messaging-ktx:23.3.1")
    implementation("com.google.firebase:firebase-auth-ktx:22.2.0")
    implementation("androidx.databinding:databinding-runtime:8.1.2")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
//    implementation ("com.firebaseui:firebase-ui-firestore:6.6.0")

    implementation ("com.google.firebase:firebase-firestore:23.0.3")
    implementation ("com.firebaseui:firebase-ui-firestore:8.0.0")

    implementation ("com.google.firebase:firebase-core:20.0.0")



    implementation(platform("com.google.firebase:firebase-bom:32.4.0"))

    implementation("com.google.firebase:firebase-analytics-ktx")

    implementation ("com.google.firebase:firebase-analytics:20.0.1") // or the latest version






}