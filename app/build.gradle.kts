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
        applicationId = ConfigureApp.applicationId
        minSdk = AppVersion.minSdkVersion
        targetSdk = AppVersion.targetSdkVersion
        versionCode = ConfigureApp.versionCode
        versionName = ConfigureApp.versionName
        testInstrumentationRunner = AppVersion.testInstrumentationRunner
        vectorDrawables.useSupportLibrary = true
    }

    signingConfigs {
        create("release") {
            keyAlias =
                findProperty("SIGNING_KEY_ALIAS_ALLSET") as String?
                    ?: System.getenv("SIGNING_KEY_ALIAS")
            keyPassword = findProperty("SIGNING_KEY_PASSWORD_HACYBEYKER") as String?
                ?: System.getenv("SIGNING_KEY_PASSWORD")
            storeFile = file("../.signing/release-allset-key.jks")
            storePassword = findProperty("SIGNING_STORE_PASSWORD_HACYBEYKER") as String?
                ?: System.getenv("SIGNING_STORE_PASSWORD")
        }
    }

    buildTypes {
        getByName("release") {
            isDebuggable = false
            isMinifyEnabled = true
            isShrinkResources = true
            signingConfig = signingConfigs.getByName("release")
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlinOptions.jvmTarget = JavaVersion.VERSION_11.toString()

    buildFeatures {
        dataBinding = true
        viewBinding = true
    }

    bundle {
        language {
            enableSplit = true
        }
        density {
            enableSplit = true
        }
        abi {
            enableSplit = true
        }
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