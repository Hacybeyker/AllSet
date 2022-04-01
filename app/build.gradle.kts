plugins {
    id("com.android.application")
    id("kotlin-android")
    id("kotlin-kapt")
    id("kotlin-parcelize")
    //id("com.google.secrets_gradle_plugin").version("0.4")
    id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin")

}

android {
    compileSdk = AppVersion.compileSdkVersion
    buildToolsVersion = AppVersion.buildToolsVersion

    defaultConfig {
        applicationId = Configuration.applicationId
        minSdk = AppVersion.minSdkVersion
        targetSdk = AppVersion.targetSdkVersion
        versionCode = Configuration.versionCode
        versionName = Configuration.versionName
        testInstrumentationRunner = AppVersion.testInstrumentationRunner
        vectorDrawables.useSupportLibrary = true
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions.jvmTarget = JavaVersion.VERSION_1_8.toString()

    buildFeatures {
        dataBinding = true
        viewBinding = true
    }
}

dependencies {
    //Libs
    implementation(fileTree("libs") { include(listOf("*.jar", "*.aar")) })
    //kotlin
    implementation(AppDependencies.kotlinStdlib)
    implementation(AppDependencies.coreKtx)
    //View
    implementation(AppDependencies.appCompat)
    implementation(AppDependencies.material)
    implementation(AppDependencies.constraintLayout)
    //Test
    testImplementation(TestDependencies.junit)
    androidTestImplementation(TestDependencies.extJUnit)
    androidTestImplementation(TestDependencies.espressoCore)
    //AndroidX
    implementation(AppDependencies.viewPager2)
    implementation(AppDependencies.cardView)
    implementation(AppDependencies.palette)
    implementation(AppDependencies.preference)
    implementation(AppDependencies.legacySupportV4)
    implementation(AppDependencies.navigationFragment)
    implementation(AppDependencies.navigationUI)
    //Pictures
    implementation(AppDependencies.glide)
    kapt(AppDependencies.glideCompiler)
    implementation(AppDependencies.coil)
    //LibsExternals
    implementation(AppDependencies.roundedImageView)
    implementation(AppDependencies.kenburnsView)
    implementation(AppDependencies.touchImageView)
    implementation(AppDependencies.zxingAndroid) { isTransitive = false }
    implementation(AppDependencies.zxingCore)
    implementation(AppDependencies.slidableActivity)
    implementation("com.getkeepsafe.taptargetview:taptargetview:1.13.3")
    implementation("com.google.android.gms:play-services-maps:18.0.2")
}