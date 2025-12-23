package com.ivors.everything

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.ivors.everything.data.AppDatabase
import com.ivors.everything.ui.theme.EverythingTheme
import com.ivors.everything.ui.theme.ThemeChoice
import com.ivors.everything.ui.worktracker.InsightsScreen
import com.ivors.everything.ui.worktracker.WorkTrackerScreen
import com.ivors.everything.ui.worktracker.WorkTrackerViewModel
import com.ivors.everything.ui.habits.HabitTrackerScreen
import com.ivors.everything.ui.habits.HabitTrackerViewModel
import com.ivors.everything.ui.settings.SettingsScreen
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.List
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ShortNavigationBar
import androidx.compose.material3.ShortNavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.compose.foundation.layout.padding

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        
        val database = AppDatabase.getDatabase(applicationContext)
        val workLogDao = database.workLogDao()
        val habitDao = database.habitDao()
        
        setContent {
            var currentTheme by remember { mutableStateOf(ThemeChoice.DEFAULT) }
            
            EverythingTheme(themeChoice = currentTheme) {
                val workViewModel: WorkTrackerViewModel = viewModel(
                    factory = WorkTrackerViewModel.Factory(workLogDao)
                )
                val habitViewModel: HabitTrackerViewModel = viewModel(
                    factory = HabitTrackerViewModel.Factory(habitDao)
                )
                val navController = rememberNavController()
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination?.route

                Scaffold(
                    bottomBar = {
                        val showBottomBar = currentDestination in listOf("tracker", "habits", "settings")
                        if (showBottomBar) {
                            ShortNavigationBar {
                                ShortNavigationBarItem(
                                    selected = currentDestination == "tracker",
                                    onClick = { 
                                        if (currentDestination != "tracker") {
                                            navController.navigate("tracker") {
                                                popUpTo("tracker") { inclusive = true }
                                            }
                                        }
                                    },
                                    icon = { 
                                        Icon(
                                            if (currentDestination == "tracker") Icons.Filled.Home else Icons.Outlined.Home,
                                            contentDescription = "Work"
                                        ) 
                                    },
                                    label = { Text("Work") }
                                )
                                ShortNavigationBarItem(
                                    selected = currentDestination == "habits",
                                    onClick = {
                                        if (currentDestination != "habits") {
                                            navController.navigate("habits") {
                                                launchSingleTop = true
                                            }
                                        }
                                    },
                                    icon = { 
                                        Icon(
                                            if (currentDestination == "habits") Icons.Filled.List else Icons.Outlined.List,
                                            contentDescription = "Habits"
                                        ) 
                                    },
                                    label = { Text("Habits") }
                                )
                                ShortNavigationBarItem(
                                    selected = currentDestination == "settings",
                                    onClick = {
                                        if (currentDestination != "settings") {
                                            navController.navigate("settings") {
                                                launchSingleTop = true
                                            }
                                        }
                                    },
                                    icon = { 
                                        Icon(
                                            if (currentDestination == "settings") Icons.Filled.Settings else Icons.Outlined.Settings,
                                            contentDescription = "Settings"
                                        ) 
                                    },
                                    label = { Text("Settings") }
                                )
                            }
                        }
                    }
                ) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = "tracker",
                        modifier = Modifier.padding(innerPadding),
                        enterTransition = {
                            slideInHorizontally(
                                initialOffsetX = { fullWidth -> fullWidth / 3 },
                                animationSpec = spring(
                                    dampingRatio = Spring.DampingRatioLowBouncy,
                                    stiffness = Spring.StiffnessMediumLow
                                )
                            ) + fadeIn(
                                animationSpec = spring(
                                    dampingRatio = Spring.DampingRatioNoBouncy,
                                    stiffness = Spring.StiffnessMedium
                                )
                            )
                        },
                        exitTransition = {
                            slideOutHorizontally(
                                targetOffsetX = { fullWidth -> -fullWidth / 4 },
                                animationSpec = spring(
                                    dampingRatio = Spring.DampingRatioNoBouncy,
                                    stiffness = Spring.StiffnessMedium
                                )
                            ) + fadeOut(
                                animationSpec = spring(
                                    dampingRatio = Spring.DampingRatioNoBouncy,
                                    stiffness = Spring.StiffnessMedium
                                )
                            )
                        },
                        popEnterTransition = {
                            slideInHorizontally(
                                initialOffsetX = { fullWidth -> -fullWidth / 3 },
                                animationSpec = spring(
                                    dampingRatio = Spring.DampingRatioLowBouncy,
                                    stiffness = Spring.StiffnessMediumLow
                                )
                            ) + fadeIn(
                                animationSpec = spring(
                                    dampingRatio = Spring.DampingRatioNoBouncy,
                                    stiffness = Spring.StiffnessMedium
                                )
                            )
                        },
                        popExitTransition = {
                            slideOutHorizontally(
                                targetOffsetX = { fullWidth -> fullWidth / 4 },
                                animationSpec = spring(
                                    dampingRatio = Spring.DampingRatioNoBouncy,
                                    stiffness = Spring.StiffnessMedium
                                )
                            ) + fadeOut(
                                animationSpec = spring(
                                    dampingRatio = Spring.DampingRatioNoBouncy,
                                    stiffness = Spring.StiffnessMedium
                                )
                            )
                        }
                    ) {
                        composable("tracker") {
                            WorkTrackerScreen(
                                viewModel = workViewModel,
                                onNavigateToInsights = { navController.navigate("insights") },
                                modifier = Modifier.fillMaxSize()
                            )
                        }

                        composable("insights") {
                            InsightsScreen(
                                viewModel = workViewModel,
                                onBack = { navController.popBackStack() },
                                modifier = Modifier.fillMaxSize()
                            )
                        }

                        composable("habits") {
                            HabitTrackerScreen(
                                viewModel = habitViewModel,
                                modifier = Modifier.fillMaxSize()
                            )
                        }

                        composable("settings") {
                            SettingsScreen(
                                currentTheme = currentTheme,
                                onThemeChange = { currentTheme = it },
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                    }
                }
            }
        }
    }
}