plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.hjlee.admopmediationexample"
    compileSdk {
        version = release(36) {
            minorApiLevel = 1
        }
    }

    defaultConfig {
        applicationId = "com.hjlee.admopmediationexample"
        minSdk = 24
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
}

dependencies {
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.core.ktx)
    implementation(libs.material)
    implementation("com.google.android.gms:play-services-ads:24.2.0")
    implementation("com.google.android.gms:play-services-ads-identifier:17.0.0")
    implementation("io.github.mobon:mobwithSDK:1.0.80")
    implementation("io.github.mobon:MobwithAdMobMediationAdapter:1.0.0-alpha2")
//    implementation(files("libs/admop_mediation-debug.aar"))
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.junit)
}