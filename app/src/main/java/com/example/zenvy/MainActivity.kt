package com.example.zenvy

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.example.zenvy.navigation.AppNavGraph
import com.example.zenvy.ui.theme.ZenvyTheme
import dagger.hilt.android.AndroidEntryPoint

/**
 * [Purpose] - Main entry point activity for Zenvy app
 * Architecture Layer: UI
 * 
 * WHY: Hosts the entire navigation graph and applies app theme
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ZenvyTheme {
                Surface(
                    modifier = Modifier.fillMaxSize()
                ) {
                    val navController = rememberNavController()
                    AppNavGraph(navController = navController)
                }
            }
        }
    }
}
