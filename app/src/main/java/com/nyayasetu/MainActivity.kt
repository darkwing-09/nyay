package com.nyayasetu

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import androidx.activity.compose.setContent
import androidx.compose.material3.ExperimentalMaterial3Api
import com.nyayasetu.ui.navigation.AppNavGraph
import com.nyayasetu.ui.theme.NyayaSetuTheme
import dagger.hilt.android.AndroidEntryPoint

@OptIn(ExperimentalMaterial3Api::class)
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
