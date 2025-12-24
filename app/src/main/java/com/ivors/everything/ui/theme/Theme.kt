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
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

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

private val ChristmasDarkColorScheme = darkColorScheme(
    primary = Color(0xFFD32F2F), // Festive Red
    secondary = Color(0xFF388E3C), // Forest Green
    tertiary = Color(0xFFFBC02D), // Gold
    surface = Color(0xFF1B1B1B),
    onSurface = Color(0xFFF5F5F5),
    primaryContainer = Color(0xFF7F0000),
    onPrimaryContainer = Color(0xFFFFDADA),
    secondaryContainer = Color(0xFF005005),
    onSecondaryContainer = Color(0xFFD7FFD7)
)

private val ChristmasLightColorScheme = lightColorScheme(
    primary = Color(0xFFD32F2F), // Festive Red
    secondary = Color(0xFF388E3C), // Forest Green
    tertiary = Color(0xFFFBC02D), // Gold
    surface = Color(0xFFFFFBFF),
    onSurface = Color(0xFF201A1A),
    primaryContainer = Color(0xFFFFDADA),
    onPrimaryContainer = Color(0xFF410002),
    secondaryContainer = Color(0xFFCEFFCE),
    onSecondaryContainer = Color(0xFF002104)
)

private val WinterColorScheme = lightColorScheme(
    primary = Color(0xFF0288D1), // Ice Blue
    secondary = Color(0xFF78909C), // Silver Gray
    tertiary = Color(0xFFB3E5FC),
    surface = Color(0xFFF0F4F8),
    onSurface = Color(0xFF263238),
    primaryContainer = Color(0xFFE1F5FE),
    secondaryContainer = Color(0xFFECEFF1),
    onSecondaryContainer = Color(0xFF0288D1)
)

enum class ThemeChoice {
    DEFAULT, CHRISTMAS, WINTER
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun EverythingTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is disabled by default to preserve the Pink & Blue aesthetic
    dynamicColor: Boolean = false,
    themeChoice: ThemeChoice = ThemeChoice.DEFAULT,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        themeChoice == ThemeChoice.CHRISTMAS -> if (darkTheme) ChristmasDarkColorScheme else ChristmasLightColorScheme
        themeChoice == ThemeChoice.WINTER -> WinterColorScheme
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = Color.Transparent.toArgb()
            window.navigationBarColor = Color.Transparent.toArgb()
            
            // Disable contrast enforcement for truly transparent bars
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                window.isNavigationBarContrastEnforced = false
                window.isStatusBarContrastEnforced = false
            }
            
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
            WindowCompat.getInsetsController(window, view).isAppearanceLightNavigationBars = !darkTheme
        }
    }

    // Use MaterialExpressiveTheme with expressive motion scheme for bouncy, dynamic animations
    MaterialExpressiveTheme(
        colorScheme = colorScheme,
        typography = Typography,
        motionScheme = MotionScheme.expressive(),
        content = content
    )
}