@file:Suppress("UnstableApiUsage")

plugins {
    id("com.android.application")
    kotlin("android")
}

dependencies {
    val libChannel: String by rootProject.extra
    implementation(project(":api"))
    implementation(project(":preset"))
    implementation(project(":dashboard"))
    implementation("com.github.bumptech.glide:glide:4.15.1")
    annotationProcessor("com.github.bumptech.glide:compiler:4.15.1")
}

android {
    compileSdk = rootProject.ext.get("compileSdkVersion") as Int
    buildToolsVersion = rootProject.ext.get("buildToolsVersion") as String

    defaultConfig {

        /**
         * CHANGE THIS TO YOUR APP PKG NAME THIS CANNOT BE CHANGED AFTER FIRST RELEASE
         */
        applicationId = "whatever.kompany.skin"

        /**
         * INCREMENT THIS IF YOU WANT TO RELEASE UPDATES
         */
        versionCode = 1
        versionName = "1"

        minSdk = 23
        targetSdk = rootProject.ext.get("targetSdkVersion") as Int
    }

    buildTypes {
        release {
            isShrinkResources = false
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules")
        }
        aaptOptions.setNoCompress("zip", "komp", "klwp", "kwgt", "klck", "kwch", "kntf")
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
        kotlinOptions {
            jvmTarget = "1.8"
        }
    }

    namespace = "whatever.kompany.skin"
}