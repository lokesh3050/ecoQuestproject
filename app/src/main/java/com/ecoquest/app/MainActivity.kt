package com.ecoquest.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.ecoquest.app.ui.navigation.AppNavigation
import com.ecoquest.app.ui.theme.CloudWhite
import com.ecoquest.app.ui.theme.EcoQuestTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            EcoQuestTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = CloudWhite) {
                    AppNavigation()
                }
            }
        }
    }
}
