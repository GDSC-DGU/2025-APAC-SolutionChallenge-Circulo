import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.devtools.ksp)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.dagger.hilt)
    alias(libs.plugins.ktlint)
    alias(libs.plugins.google.services)
    alias(libs.plugins.google.firebase.crashlytics)
}

val properties = Properties().apply {
    load(project.rootProject.file("local.properties").inputStream())
}

android {
    namespace = "com.example.circuler"
    compileSdk = libs.versions.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "com.example.circuler"
        minSdk = libs.versions.minSdk.get().toInt()
        targetSdk = libs.versions.targetSdk.get().toInt()
        versionCode = libs.versions.versionCode.get().toInt()
        versionName = libs.versions.versionName.get()

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        buildConfigField("String", "BASE_URL", properties["base.url"].toString())
        buildConfigField("String", "GOOGLE_CLIENT_ID", properties["google.client.id"].toString())
        buildConfigField("String", "TOKEN", properties["token"].toString())
        manifestPlaceholders["googleMapKey"] = properties["google.map.key"].toString()
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = libs.versions.jvmTarget.get()
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.kotlinCompilerExtensionVersion.get()
    }
}

dependencies {
    // Test
    testImplementation(libs.junit)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.bundles.test)

    // Debug
    debugImplementation(libs.bundles.debug)

    // AndroidX
    implementation(libs.bundles.androidx)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.exifinterface)

    // kotlinx
    implementation(libs.kotlinx.immutable)

    // Network
    implementation(platform(libs.okhttp.bom))
    implementation(libs.bundles.okhttp)
    implementation(libs.bundles.retrofit)
    implementation(libs.kotlinx.serialization.json)

    // Hilt
    implementation(libs.bundles.hilt)
    ksp(libs.hilt.compiler)

    // Coil
    implementation(libs.coil.compose)

    // Timber
    implementation(libs.timber)

    // Google
    implementation(platform(libs.google.firebase.bom))
    implementation(libs.google.firebase.crashlytics)
    implementation(libs.firebase.crashlytics.buildtools)
    implementation(libs.firebase.analytics)
    implementation(libs.google.play.services.auth)
    implementation(libs.google.credentail.service)
    implementation(libs.google.credential)
    implementation(libs.google.identity)
    implementation(libs.maps.compose)
    implementation(libs.play.services.maps)

    // Lottie
    implementation(libs.bundles.lottie)

    // CameraX
    implementation(libs.bundles.camerax)
}

ktlint {
    android = true
    debug = true
    coloredOutput = true
    verbose = true
    outputToConsole = true
}
