plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.hacybeyker.allset"
    compileSdk {
        version = release(libs.versions.compileSdk.get().toInt())
    }

    defaultConfig {
        applicationId = providers.gradleProperty("APP_ID").get()
        minSdk = libs.versions.minSdk.get().toInt()
        targetSdk = libs.versions.targetSdk.get().toInt()
        versionCode = providers.gradleProperty("VERSION_CODE").get().toInt()
        versionName = providers.gradleProperty("VERSION_NAME").get()
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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

    buildFeatures {
        compose = true
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
                "TypographyQuotes"
            )
        )
        checkDependencies = true
        abortOnError = false
        ignoreWarnings = false
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.activity.compose)
    //Compose
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.material.icons.core)
    debugImplementation(libs.androidx.compose.ui.tooling)
    //Navigation
    implementation(libs.androidx.navigation3.runtime)
    implementation(libs.androidx.navigation3.ui)
    implementation(libs.kotlinx.serialization.core)
    //Lifecycle
    implementation(libs.androidx.lifecycle.runtime.compose)
    //Web
    implementation(libs.androidx.webkit)
    implementation(libs.androidx.browser)
    //Test
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
}
