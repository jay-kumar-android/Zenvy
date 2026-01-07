package com.example.zenvy.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

/**
 * [Purpose] - Zenvy Material3 theme configuration
 * Architecture Layer: UI
 * 
 * WHY: Applies Zenvy brand colors to Material3 components consistently
 */

private val LightColorScheme = lightColorScheme(
    primary = PrimaryPurple,
    onPrimary = Color.White,
    primaryContainer = PrimaryPurpleLight,
    onPrimaryContainer = PrimaryPurpleDark,
    
    secondary = SecondaryPurple,
    onSecondary = Color.White,
    secondaryContainer = SecondaryPurple.copy(alpha = 0.2f),
    onSecondaryContainer = PrimaryPurpleDark,
    
    tertiary = AccentPink,
    onTertiary = Color.White,
    
    background = BackgroundLight,
    onBackground = TextPrimary,
    
    surface = BackgroundWhite,
    onSurface = TextPrimary,
    surfaceVariant = SurfaceLight,
    onSurfaceVariant = TextSecondary,
    
    error = ErrorRed,
    onError = Color.White,
    
    outline = Gray300,
    outlineVariant = Gray200
)

@Composable
fun ZenvyTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    // WHY: Using light theme only for this UI-only phase
    // Dark theme and dynamic colors will be implemented in future phases
    val colorScheme = LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}