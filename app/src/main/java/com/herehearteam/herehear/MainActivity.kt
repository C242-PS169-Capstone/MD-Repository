package com.herehearteam.herehear

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.herehearteam.herehear.navigation.NavigationGraph
import com.herehearteam.herehear.ui.components.BottomNavigationBar
import com.herehearteam.herehear.ui.theme.HereHearTheme
import com.herehearteam.herehear.utils.shouldShowBottomBar

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            HereHearTheme {
                AppContent()

            }
        }
    }
}

@Composable
fun AppContent(){
    val navController = rememberNavController()

    Box(modifier = Modifier.fillMaxSize()) {
        NavigationGraph(navController = navController)

        if (shouldShowBottomBar(navController)) {
            Scaffold(
                bottomBar = {
                    BottomNavigationBar(navController = navController)
                }
            ) { paddingValues ->
                Box(modifier = Modifier.padding(paddingValues)) {
                    NavigationGraph(navController = navController)
                }
            }
        }
    }
}