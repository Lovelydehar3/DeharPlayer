package com.dehar.player.core.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

// Dark Color Scheme
private val DarkColorScheme = darkColorScheme(
    primary = DeharBlueAccent,
    onPrimary = Color.White,
    primaryContainer = DeharBlueDark,
    onPrimaryContainer = DeharBlueLight,
    
    secondary = DeharBlue,
    onSecondary = Color.White,
    secondaryContainer = DeharBlueDark,
    onSecondaryContainer = DeharBlueLight,
    
    tertiary = DeharTeal,
    onTertiary = Color.White,
    tertiaryContainer = DeharTealDark,
    onTertiaryContainer = DeharTealLight,
    
    background = DarkBackground,
    onBackground = DarkOnBackground,
    
    surface = DarkSurface,
    onSurface = DarkOnSurface,
    surfaceVariant = DarkSurfaceVariant,
    onSurfaceVariant = DarkOnSurfaceVariant,
    surfaceTint = DeharBlueAccent,
    
    error = ErrorColor,
    onError = Color.White,
    
    outline = DarkOutline,
    outlineVariant = DarkOutlineVariant
)

// Light Color Scheme
private val LightColorScheme = lightColorScheme(
    primary = DeharBlue,
    onPrimary = Color.White,
    primaryContainer = DeharBlueLight,
    onPrimaryContainer = DeharBlueDark,
    
    secondary = DeharBlueAccent,
    onSecondary = Color.White,
    secondaryContainer = DeharBlueLight,
    onSecondaryContainer = DeharBlueDark,
    
    tertiary = DeharTeal,
    onTertiary = Color.White,
    tertiaryContainer = DeharTealLight,
    onTertiaryContainer = DeharTealDark,
    
    background = LightBackground,
    onBackground = LightOnBackground,
    
    surface = LightSurface,
    onSurface = LightOnSurface,
    surfaceVariant = LightSurfaceVariant,
    onSurfaceVariant = LightOnSurfaceVariant,
    surfaceTint = DeharBlue,
    
    error = ErrorColor,
    onError = Color.White,
    
    outline = LightOutline,
    outlineVariant = LightOutlineVariant
)

// Alternative Color Schemes
val RedColorScheme = DarkColorScheme.copy(
    primary = DeharRed,
    secondary = DeharRedLight,
    tertiary = DeharRedDark
)

val GreenColorScheme = DarkColorScheme.copy(
    primary = DeharGreen,
    secondary = DeharGreenLight,
    tertiary = DeharGreenDark
)

val OrangeColorScheme = DarkColorScheme.copy(
    primary = DeharOrange,
    secondary = DeharOrangeLight,
    tertiary = DeharOrangeDark
)

val PurpleColorScheme = DarkColorScheme.copy(
    primary = DeharPurple,
    secondary = DeharPurpleLight,
    tertiary = DeharPurpleDark
)

val TealColorScheme = DarkColorScheme.copy(
    primary = DeharTeal,
    secondary = DeharTealLight,
    tertiary = DeharTealDark
)

val PinkColorScheme = DarkColorScheme.copy(
    primary = DeharPink,
    secondary = DeharPinkLight,
    tertiary = DeharPinkDark
)

/**
 * Theme mode enum
 */
enum class ThemeMode {
    SYSTEM,
    LIGHT,
    DARK,
    AMOLED
}

/**
 * Accent color enum
 */
enum class AccentColor {
    BLUE,
    RED,
    GREEN,
    ORANGE,
    PURPLE,
    TEAL,
    PINK,
    DYNAMIC
}

/**
 * Get color scheme based on accent color
 */
@Composable
private fun getColorScheme(
    isDarkTheme: Boolean,
    accentColor: AccentColor,
    useDynamicColor: Boolean
): androidx.compose.material3.ColorScheme {
    // Dynamic color (Material You) for Android 12+
    if (useDynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        val context = LocalContext.current
        return if (isDarkTheme) {
            dynamicDarkColorScheme(context)
        } else {
            dynamicLightColorScheme(context)
        }
    }
    
    // AMOLED black theme
    if (isDarkTheme && accentColor == AccentColor.DYNAMIC) {
        return DarkColorScheme.copy(
            background = Color.Black,
            surface = Color.Black
        )
    }
    
    // Custom accent colors
    return when (accentColor) {
        AccentColor.RED -> if (isDarkTheme) RedColorScheme else LightColorScheme.copy(primary = DeharRed)
        AccentColor.GREEN -> if (isDarkTheme) GreenColorScheme else LightColorScheme.copy(primary = DeharGreen)
        AccentColor.ORANGE -> if (isDarkTheme) OrangeColorScheme else LightColorScheme.copy(primary = DeharOrange)
        AccentColor.PURPLE -> if (isDarkTheme) PurpleColorScheme else LightColorScheme.copy(primary = DeharPurple)
        AccentColor.TEAL -> if (isDarkTheme) TealColorScheme else LightColorScheme.copy(primary = DeharTeal)
        AccentColor.PINK -> if (isDarkTheme) PinkColorScheme else LightColorScheme.copy(primary = DeharPink)
        AccentColor.BLUE, AccentColor.DYNAMIC -> if (isDarkTheme) DarkColorScheme else LightColorScheme
    }
}

/**
 * Main Dehar Player Theme
 */
@Composable
fun DeharPlayerTheme(
    isDarkTheme: Boolean = isSystemInDarkTheme(),
    themeMode: ThemeMode = ThemeMode.SYSTEM,
    accentColor: AccentColor = AccentColor.BLUE,
    useDynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val effectiveDarkTheme = when (themeMode) {
        ThemeMode.SYSTEM -> isDarkTheme
        ThemeMode.LIGHT -> false
        ThemeMode.DARK -> true
        ThemeMode.AMOLED -> true
    }
    
    val colorScheme = getColorScheme(effectiveDarkTheme, accentColor, useDynamicColor)
    
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = Color.Transparent.toArgb()
            window.navigationBarColor = Color.Transparent.toArgb()
            WindowCompat.getInsetsController(window, view).apply {
                isAppearanceLightStatusBars = !effectiveDarkTheme
                isAppearanceLightNavigationBars = !effectiveDarkTheme
            }
        }
    }
    
    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

/**
 * Get the current primary color from the theme
 */
@Composable
fun getCurrentPrimaryColor(): Color {
    return MaterialTheme.colorScheme.primary
}

/**
 * Get the current surface color from the theme
 */
@Composable
fun getCurrentSurfaceColor(): Color {
    return MaterialTheme.colorScheme.surface
}

/**
 * Get the current background color from the theme
 */
@Composable
fun getCurrentBackgroundColor(): Color {
    return MaterialTheme.colorScheme.background
}