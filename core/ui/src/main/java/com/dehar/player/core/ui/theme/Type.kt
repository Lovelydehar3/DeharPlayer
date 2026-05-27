package com.dehar.player.core.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

/**
 * Default Typography for Dehar Player
 * Uses system font family with fallback to sans-serif
 */
val Typography = Typography(
    // Display styles - Large headings
    displayLarge = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Bold,
        fontSize = 57.sp,
        lineHeight = 64.sp,
        letterSpacing = (-0.25).sp
    ),
    displayMedium = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Bold,
        fontSize = 45.sp,
        lineHeight = 52.sp,
        letterSpacing = 0.sp
    ),
    displaySmall = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Bold,
        fontSize = 36.sp,
        lineHeight = 44.sp,
        letterSpacing = 0.sp
    ),
    
    // Headline styles - Section headers
    headlineLarge = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.SemiBold,
        fontSize = 32.sp,
        lineHeight = 40.sp,
        letterSpacing = 0.sp
    ),
    headlineMedium = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.SemiBold,
        fontSize = 28.sp,
        lineHeight = 36.sp,
        letterSpacing = 0.sp
    ),
    headlineSmall = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.SemiBold,
        fontSize = 24.sp,
        lineHeight = 32.sp,
        letterSpacing = 0.sp
    ),
    
    // Title styles - Card titles, dialog titles
    titleLarge = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.SemiBold,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),
    titleMedium = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Medium,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.15.sp
    ),
    titleSmall = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.1.sp
    ),
    
    // Body styles - Main content text
    bodyLarge = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.25.sp
    ),
    bodySmall = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.4.sp
    ),
    
    // Label styles - Buttons, tabs, labels
    labelLarge = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.1.sp
    ),
    labelMedium = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Medium,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    ),
    labelSmall = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    )
)

/**
 * Compact Typography for dense UI layouts
 */
val CompactTypography = Typography(
    displayLarge = Typography.displayLarge.copy(fontSize = 48.sp),
    displayMedium = Typography.displayMedium.copy(fontSize = 38.sp),
    displaySmall = Typography.displaySmall.copy(fontSize = 30.sp),
    
    headlineLarge = Typography.headlineLarge.copy(fontSize = 26.sp),
    headlineMedium = Typography.headlineMedium.copy(fontSize = 22.sp),
    headlineSmall = Typography.headlineSmall.copy(fontSize = 18.sp),
    
    titleLarge = Typography.titleLarge.copy(fontSize = 18.sp),
    titleMedium = Typography.titleMedium.copy(fontSize = 14.sp),
    titleSmall = Typography.titleSmall.copy(fontSize = 12.sp),
    
    bodyLarge = Typography.bodyLarge.copy(fontSize = 14.sp),
    bodyMedium = Typography.bodyMedium.copy(fontSize = 12.sp),
    bodySmall = Typography.bodySmall.copy(fontSize = 10.sp),
    
    labelLarge = Typography.labelLarge.copy(fontSize = 12.sp),
    labelMedium = Typography.labelMedium.copy(fontSize = 11.sp),
    labelSmall = Typography.labelSmall.copy(fontSize = 10.sp)
)

/**
 * Large Typography for accessibility
 */
val LargeTypography = Typography(
    displayLarge = Typography.displayLarge.copy(fontSize = 68.sp),
    displayMedium = Typography.displayMedium.copy(fontSize = 54.sp),
    displaySmall = Typography.displaySmall.copy(fontSize = 44.sp),
    
    headlineLarge = Typography.headlineLarge.copy(fontSize = 40.sp),
    headlineMedium = Typography.headlineMedium.copy(fontSize = 34.sp),
    headlineSmall = Typography.headlineSmall.copy(fontSize = 28.sp),
    
    titleLarge = Typography.titleLarge.copy(fontSize = 26.sp),
    titleMedium = Typography.titleMedium.copy(fontSize = 20.sp),
    titleSmall = Typography.titleSmall.copy(fontSize = 18.sp),
    
    bodyLarge = Typography.bodyLarge.copy(fontSize = 20.sp),
    bodyMedium = Typography.bodyMedium.copy(fontSize = 18.sp),
    bodySmall = Typography.bodySmall.copy(fontSize = 16.sp),
    
    labelLarge = Typography.labelLarge.copy(fontSize = 18.sp),
    labelMedium = Typography.labelMedium.copy(fontSize = 16.sp),
    labelSmall = Typography.labelSmall.copy(fontSize = 14.sp)
)

// Predefined text styles for common use cases
object DeharTextStyles {
    // Video title in grid
    val videoGridTitle = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        lineHeight = 18.sp
    )
    
    // Video title in list
    val videoListTitle = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.SemiBold,
        fontSize = 16.sp,
        lineHeight = 20.sp
    )
    
    // Metadata text (duration, size, etc.)
    val metadata = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        lineHeight = 16.sp
    )
    
    // Badge text (4K, HD, etc.)
    val badge = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Bold,
        fontSize = 10.sp,
        lineHeight = 14.sp
    )
    
    // Player controls text
    val playerControls = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Medium,
        fontSize = 13.sp,
        lineHeight = 16.sp
    )
    
    // Now playing song title
    val nowPlayingTitle = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Bold,
        fontSize = 22.sp,
        lineHeight = 28.sp
    )
    
    // Now playing artist name
    val nowPlayingArtist = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp
    )
    
    // Settings item
    val settingsItem = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp
    )
    
    // Settings item title
    val settingsItemTitle = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        lineHeight = 20.sp
    )
}