package com.ivors.everything.ui.theme

import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

/**
 * Material 3 Expressive Typography
 *
 * The M3 Expressive type scale includes 15 base styles + 15 emphasized variants (30 total).
 * Categories: Display, Headline, Title, Body, Label (each with Large, Medium, Small sizes)
 *
 * Emphasized styles add dynamism and personality - use them to:
 * - Create clearer division of content
 * - Draw users' eyes to relevant material
 * - Add visual hierarchy and emphasis
 *
 * Default font: Roboto (or Roboto Flex for variable font support on API 26+)
 */

// Base Typography (standard M3 type scale)
val Typography = Typography(
    // Display - Large, short strings for hero information
    displayLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 57.sp,
        lineHeight = 64.sp,
        letterSpacing = (-0.25).sp
    ),
    displayMedium = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 45.sp,
        lineHeight = 52.sp,
        letterSpacing = 0.sp
    ),
    displaySmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 36.sp,
        lineHeight = 44.sp,
        letterSpacing = 0.sp
    ),

    // Headline - Short, important text or numerals
    headlineLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 32.sp,
        lineHeight = 40.sp,
        letterSpacing = 0.sp
    ),
    headlineMedium = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 28.sp,
        lineHeight = 36.sp,
        letterSpacing = 0.sp
    ),
    headlineSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 24.sp,
        lineHeight = 32.sp,
        letterSpacing = 0.sp
    ),

    // Title - Medium-emphasis text, shorter in length
    titleLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),
    titleMedium = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.15.sp
    ),
    titleSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.1.sp
    ),

    // Body - Long-form writing, works well for small text sizes
    bodyLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.25.sp
    ),
    bodySmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.4.sp
    ),

    // Label - Call to action, buttons, tabs, dialogs
    labelLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.1.sp
    ),
    labelMedium = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    ),
    labelSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    )
)

/**
 * Expressive Typography with Emphasized Styles
 *
 * Use this with MaterialExpressiveTheme for the full M3 Expressive experience.
 * Emphasized styles use SemiBold weight for added visual impact.
 *
 * Example usage:
 * ```
 * Text(
 *     text = "Important Header",
 *     style = MaterialTheme.typography.headlineLargeEmphasized
 * )
 * ```
 */
@OptIn(ExperimentalMaterial3ExpressiveApi::class)
val ExpressiveTypography = Typography(
    // Base styles
    displayLarge = Typography.displayLarge,
    displayMedium = Typography.displayMedium,
    displaySmall = Typography.displaySmall,
    headlineLarge = Typography.headlineLarge,
    headlineMedium = Typography.headlineMedium,
    headlineSmall = Typography.headlineSmall,
    titleLarge = Typography.titleLarge,
    titleMedium = Typography.titleMedium,
    titleSmall = Typography.titleSmall,
    bodyLarge = Typography.bodyLarge,
    bodyMedium = Typography.bodyMedium,
    bodySmall = Typography.bodySmall,
    labelLarge = Typography.labelLarge,
    labelMedium = Typography.labelMedium,
    labelSmall = Typography.labelSmall,

    // Emphasized styles - add dynamism and personality
    displayLargeEmphasized = Typography.displayLarge.copy(fontWeight = FontWeight.SemiBold),
    displayMediumEmphasized = Typography.displayMedium.copy(fontWeight = FontWeight.SemiBold),
    displaySmallEmphasized = Typography.displaySmall.copy(fontWeight = FontWeight.SemiBold),
    headlineLargeEmphasized = Typography.headlineLarge.copy(fontWeight = FontWeight.SemiBold),
    headlineMediumEmphasized = Typography.headlineMedium.copy(fontWeight = FontWeight.SemiBold),
    headlineSmallEmphasized = Typography.headlineSmall.copy(fontWeight = FontWeight.SemiBold),
    titleLargeEmphasized = Typography.titleLarge.copy(fontWeight = FontWeight.SemiBold),
    titleMediumEmphasized = Typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
    titleSmallEmphasized = Typography.titleSmall.copy(fontWeight = FontWeight.SemiBold),
    bodyLargeEmphasized = Typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold),
    bodyMediumEmphasized = Typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
    bodySmallEmphasized = Typography.bodySmall.copy(fontWeight = FontWeight.SemiBold),
    labelLargeEmphasized = Typography.labelLarge.copy(fontWeight = FontWeight.SemiBold),
    labelMediumEmphasized = Typography.labelMedium.copy(fontWeight = FontWeight.SemiBold),
    labelSmallEmphasized = Typography.labelSmall.copy(fontWeight = FontWeight.SemiBold)
)
