package com.dehar.player.ui.theme

import android.app.Activity
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DeharDarkColorScheme = darkColorScheme(
    primary = DeharBlue,
    onPrimary = DeharOnPrimary,
    primaryContainer = DeharBlueDark,
    onPrimaryContainer = DeharBlueLight,
    secondary = DeharBlueVariant,
    background = DeharBackground,
    onBackground = DeharOnBackground,
    surface = DeharSurface,
    onSurface = DeharOnSurface,
    surfaceVariant = DeharSurfaceVariant,
    onSurfaceVariant = DeharOnSurfaceVariant,
    error = DeharError,
    onError = DeharOnBackground
)

@Composable
@Suppress("DEPRECATION")
fun DeharPlayerTheme(content: @Composable () -> Unit) {
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = DeharBackground.toArgb()
            window.navigationBarColor = DeharBackground.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = false
        }
    }
    MaterialTheme(
        colorScheme = DeharDarkColorScheme,
        typography = DeharTypography,
        content = content
    )
}
