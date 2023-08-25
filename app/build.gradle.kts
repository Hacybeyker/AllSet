plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("kapt")
    id("kotlin-parcelize")
    id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin")
}

android {
    namespace = ConfigureApp.applicationId
    compileSdk = AppVersion.compileSdkVersion

    defaultConfig {
        applicationId = ConfigureApp.applicationId
        minSdk = AppVersion.minSdkVersion
        targetSdk = AppVersion.targetSdkVersion
        versionCode = ConfigureApp.versionCode
        versionName = ConfigureApp.versionName
        testInstrumentationRunner = AppVersion.testInstrumentationRunner
        renderscriptSupportModeEnabled = true
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions.jvmTarget = JavaVersion.VERSION_17.toString()

    buildFeatures {
        dataBinding = true
        viewBinding = true
        buildConfig = true
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

    lint {
        disable.addAll(
            listOf(
                "TypographyFractions",
                "TypographyQuotes",
                "JvmStaticProvidesInObjectDetector",
                "FieldSiteTargetOnQualifierAnnotation",
                "ModuleCompanionObjects",
                "ModuleCompanionObjectsNotInModuleParent"
            )
        )
        checkDependencies = true
        abortOnError = false
        ignoreWarnings = false
    }
}

dependencies {
    //Libs
    implementation(fileTree("libs") { include(listOf("*.jar", "*.aar")) })
    //kotlin
    //implementation(AppDependencies.kotlinStdlib)
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
    implementation(AppDependencies.tapTargetView)
    implementation(AppDependencies.gmsMaps)
}