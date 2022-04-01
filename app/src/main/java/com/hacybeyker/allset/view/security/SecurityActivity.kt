package com.hacybeyker.allset.view.security

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.hacybeyker.allset.BuildConfig
import com.hacybeyker.allset.R

class SecurityActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_security)
        loadSecretsGradlePlugin()
    }

    //secrets-gradle-plugin
    //Repository: https://github.com/google/secrets-gradle-plugin
    //Reference: https://www.youtube.com/watch?v=X8lYNW_Or2o
    fun loadSecretsGradlePlugin() {
        /*Steps
        1- Add: classpath("com.google.android.libraries.mapsplatform.secrets-gradle-plugin:secrets-gradle-plugin:2.0.1")
        2- Add: id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin")
        3- Add keys in your local.properties an build project.
        4- This keys are added in Buildconfig file, for obtain key insert your variable name.
        * */
        Log.d("TAG", "Here - loadSecretsGradlePlugin: ${BuildConfig.KEY_PROD}")
    }
}