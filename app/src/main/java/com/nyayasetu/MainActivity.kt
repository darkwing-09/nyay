package com.nyayasetu

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import androidx.activity.compose.setContent
import com.nyayasetu.ui.navigation.AppNavGraph
import com.nyayasetu.ui.theme.NyayaSetuTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NyayaSetuTheme {
                AppNavGraph()
            }
        }
    }
}
