package com.ivors.everything.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialExpressiveTheme
import androidx.compose.material3.MotionScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = Pink300,
    secondary = LightBlue300,
    tertiary = Orange300,
    surface = Color(0xFF121212),
    onSurface = Color.White,
    primaryContainer = Color(0xFF3D1E31),
    onPrimaryContainer = Pink300,
    secondaryContainer = Color(0xFF1E3D4D),
    onSecondaryContainer = LightBlue300,
    surfaceVariant = Color(0xFF1F1F1F),
    onSurfaceVariant = Color(0xFFCAC4D0)
)

private val LightColorScheme = lightColorScheme(
    primary = Pink500,
    secondary = LightBlue500,
    tertiary = Orange300,
    surface = SurfaceGray,
    onSurface = Color.Black,
    primaryContainer = Color(0xFFFCE4EC),
    onPrimaryContainer = Pink700,
    secondaryContainer = Color(0xFFE1F5FE),
    onSecondaryContainer = LightBlue700,
    surfaceVariant = Color(0xFFF5F5F5),
    onSurfaceVariant = Color(0xFF49454F)
)

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun EverythingTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is disabled by default to preserve the Pink & Blue aesthetic
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    // Use MaterialExpressiveTheme with expressive motion scheme for bouncy, dynamic animations
    MaterialExpressiveTheme(
        colorScheme = colorScheme,
        typography = Typography,
        motionScheme = MotionScheme.expressive(),
        content = content
    )
}