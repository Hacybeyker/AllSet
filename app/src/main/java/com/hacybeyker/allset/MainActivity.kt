package com.hacybeyker.allset

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.hacybeyker.allset.core.ui.theme.AllSetTheme
import com.hacybeyker.allset.navigation.AppNavHost

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AllSetTheme {
                AppNavHost()
            }
        }
    }
}
