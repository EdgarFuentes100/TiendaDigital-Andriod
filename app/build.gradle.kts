plugins {
    id("com.android.application")
}

android {
    namespace = "com.example.myappstore"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.myappstore"
        minSdk = 24
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
    viewBinding {
        viewBinding.enable = true
    }
}

dependencies {
    implementation("com.squareup.retrofit2:retrofit:2.6.2")
    implementation ("com.google.android.gms:play-services-location:21.0.1")

    implementation("com.squareup.retrofit2:converter-gson:2.6.2")
    implementation("com.github.bumptech.glide:glide:4.15.1")
    implementation ("com.google.android.gms:play-services-auth:20.6.0")
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.2.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")
}