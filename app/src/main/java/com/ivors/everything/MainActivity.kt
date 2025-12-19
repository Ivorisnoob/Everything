package com.ivors.everything

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.ivors.everything.ui.worktracker.InsightsScreen
import com.ivors.everything.data.AppDatabase
import com.ivors.everything.ui.theme.EverythingTheme
import com.ivors.everything.ui.worktracker.WorkTrackerScreen
import com.ivors.everything.ui.worktracker.WorkTrackerViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        
        val database = AppDatabase.getDatabase(applicationContext)
        val dao = database.workLogDao()
        
        setContent {
            EverythingTheme {
                val viewModel: WorkTrackerViewModel = viewModel(
                    factory = WorkTrackerViewModel.Factory(dao)
                )
                val navController = rememberNavController()

                NavHost(
                    navController = navController,
                    startDestination = "tracker",
                    enterTransition = { fadeIn(animationSpec = tween(300)) },
                    exitTransition = { fadeOut(animationSpec = tween(300)) }
                ) {
                    composable("tracker") {
                        WorkTrackerScreen(
                            viewModel = viewModel,
                            onNavigateToInsights = { navController.navigate("insights") },
                            modifier = Modifier.fillMaxSize()
                        )
                    }

                    composable("insights") {
                        InsightsScreen(
                            viewModel = viewModel,
                            onBack = { navController.popBackStack() },
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }
            }
        }
    }
}