package com.ecoquest.app.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val EcoColorScheme = lightColorScheme(
    primary = EcoGreen,
    onPrimary = Color.White,
    primaryContainer = EcoGreenPale,
    onPrimaryContainer = EcoGreen,
    secondary = OceanTeal,
    onSecondary = Color.White,
    secondaryContainer = OceanTealLight,
    onSecondaryContainer = Color.White,
    tertiary = SunYellow,
    onTertiary = DeepEarth,
    tertiaryContainer = SunOrange,
    onTertiaryContainer = Color.White,
    background = CloudWhite,
    onBackground = DeepEarth,
    surface = Color.White,
    onSurface = DeepEarth,
    surfaceVariant = CloudWhite,
    onSurfaceVariant = SoilBrown,
    outline = MistGray,
    error = Color(0xFFD32F2F),
    onError = Color.White,
)

@Composable
fun EcoQuestTheme(
    content: @Composable () -> Unit
) {
    val colorScheme = EcoColorScheme
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = EcoGreen.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = false
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = EcoTypography,
        content = content
    )
}
