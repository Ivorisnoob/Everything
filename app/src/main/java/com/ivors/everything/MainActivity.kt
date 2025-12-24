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
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalFloatingToolbar
import androidx.compose.material3.IconButton
import androidx.compose.material3.FloatingToolbarDefaults
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.Box
import androidx.compose.ui.Alignment
import androidx.compose.foundation.layout.Column
import androidx.compose.ui.unit.dp
import androidx.compose.material3.MaterialTheme

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3ExpressiveApi::class)
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
                val showToolbar = currentDestination in listOf("tracker", "habits", "settings")
                var toolbarExpanded by remember { mutableStateOf(true) }

                Box(modifier = Modifier.fillMaxSize()) {
                    Scaffold(
                        modifier = Modifier.fillMaxSize()
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
                
                // HorizontalFloatingToolbar for navigation
                if (showToolbar) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .padding(bottom = 16.dp)
                    ) {
                        HorizontalFloatingToolbar(
                            expanded = toolbarExpanded,
                            floatingActionButton = {
                                FloatingToolbarDefaults.VibrantFloatingActionButton(
                                    onClick = { toolbarExpanded = !toolbarExpanded }
                                ) {
                                    Icon(
                                        if (toolbarExpanded) Icons.Filled.Home else Icons.Outlined.Home,
                                        contentDescription = "Toggle Menu"
                                    )
                                }
                            },
                            content = {
                                // Work navigation item
                                IconButton(
                                    onClick = { 
                                        if (currentDestination != "tracker") {
                                            navController.navigate("tracker") {
                                                popUpTo("tracker") { inclusive = true }
                                            }
                                        }
                                    }
                                ) {
                                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                        Icon(
                                            if (currentDestination == "tracker") Icons.Filled.Home else Icons.Outlined.Home,
                                            contentDescription = "Work",
                                            tint = if (currentDestination == "tracker") 
                                                MaterialTheme.colorScheme.primary 
                                            else MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                        Text(
                                            "Work",
                                            style = MaterialTheme.typography.labelSmall,
                                            color = if (currentDestination == "tracker") 
                                                MaterialTheme.colorScheme.primary 
                                            else MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                }
                                
                                Spacer(modifier = Modifier.width(8.dp))
                                
                                // Habits navigation item
                                IconButton(
                                    onClick = {
                                        if (currentDestination != "habits") {
                                            navController.navigate("habits") {
                                                launchSingleTop = true
                                            }
                                        }
                                    }
                                ) {
                                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                        Icon(
                                            if (currentDestination == "habits") Icons.Filled.List else Icons.Outlined.List,
                                            contentDescription = "Habits",
                                            tint = if (currentDestination == "habits") 
                                                MaterialTheme.colorScheme.primary 
                                            else MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                        Text(
                                            "Habits",
                                            style = MaterialTheme.typography.labelSmall,
                                            color = if (currentDestination == "habits") 
                                                MaterialTheme.colorScheme.primary 
                                            else MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                }
                                
                                Spacer(modifier = Modifier.width(8.dp))
                                
                                // Settings navigation item
                                IconButton(
                                    onClick = {
                                        if (currentDestination != "settings") {
                                            navController.navigate("settings") {
                                                launchSingleTop = true
                                            }
                                        }
                                    }
                                ) {
                                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                        Icon(
                                            if (currentDestination == "settings") Icons.Filled.Settings else Icons.Outlined.Settings,
                                            contentDescription = "Settings",
                                            tint = if (currentDestination == "settings") 
                                                MaterialTheme.colorScheme.primary 
                                            else MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                        Text(
                                            "Settings",
                                            style = MaterialTheme.typography.labelSmall,
                                            color = if (currentDestination == "settings") 
                                                MaterialTheme.colorScheme.primary 
                                            else MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                }
                            }
                        )
                    }
                }
            }
            }
        }
    }
}