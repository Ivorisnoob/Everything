package com.ivors.everything.ui.settings

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Palette
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.ivors.everything.ui.theme.ThemeChoice
import androidx.compose.foundation.shape.RoundedCornerShape

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun SettingsScreen(
    currentTheme: ThemeChoice,
    onThemeChange: (ThemeChoice) -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            LargeFlexibleTopAppBar(
                title = { Text("Settings") },
                subtitle = { Text("Personalize your experience") },
                scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
            )
        },
        modifier = modifier
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(24.dp)
                .fillMaxSize()
        ) {
            Text(
                text = "App Theme",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(16.dp))
            
            // Using ButtonGroup for theme selection as per M3 Expressive guidelines
            ButtonGroup(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(ButtonGroupDefaults.ConnectedSpaceBetween)
            ) {
                ThemeChoice.values().forEachIndexed { index, choice ->
                    val isSelected = currentTheme == choice
                    val shape = when (index) {
                        0 -> ButtonGroupDefaults.connectedLeadingButtonShapes()
                        ThemeChoice.values().size - 1 -> ButtonGroupDefaults.connectedTrailingButtonShapes()
                        else -> ButtonGroupDefaults.connectedMiddleButtonShapes()
                    }
                    
                    ToggleButton(
                        checked = isSelected,
                        onCheckedChange = { if (it) onThemeChange(choice) },
                        shapes = shape,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(choice.name.lowercase().replaceFirstChar { it.uppercase() })
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(32.dp))
            
            // placeholder for other settings
            OutlinedCard(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp)
            ) {
                ListItem(
                    headlineContent = { Text("About Everything") },
                    supportingContent = { Text("Built for personal needs.") },
                    leadingContent = { Icon(Icons.Outlined.Palette, null) }
                )
            }
        }
    }
}
