package com.ux4g.ux4gdesignsystem.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.ux4g.designsystemv1.foundation.Ux4gColors
import com.ux4g.designsystemv1.foundation.Ux4gThemeColors
import com.ux4g.designsystemv1.theme.Ux4gTheme
import com.ux4g.designsystemv1.theme.toUx4gColors

// Define your Dark Color Scheme using Ux4gColors
private val DarkColorScheme =
        Ux4gThemeColors(
                primary = Ux4gColors.primary,
                onPrimary = Ux4gColors.gray900,
                secondary = PurpleGrey80,
                onSecondary = Ux4gColors.white,
                background = Ux4gColors.gray900,
                onBackground = Ux4gColors.white,
                surface = Ux4gColors.gray800,
                onSurface = Ux4gColors.white,
                error = Ux4gColors.red400,
                onError = Ux4gColors.gray900
        )

// Define your Light Color Scheme using Ux4gColors
private val LightColorScheme =
        Ux4gThemeColors(
                primary = Ux4gColors.primary400,
                onPrimary = Ux4gColors.white,
                secondary = PurpleGrey40,
                onSecondary = Ux4gColors.gray900,
                background = Ux4gColors.gray100,
                onBackground = Ux4gColors.gray900,
                surface = Ux4gColors.white,
                onSurface = Ux4gColors.gray900,
                error = Ux4gColors.red,
                onError = Ux4gColors.white
        )

@Composable
fun Ux4GDesignSystemTheme(
        darkTheme: Boolean = isSystemInDarkTheme(),
        // Dynamic color is available on Android 12+
        dynamicColor: Boolean = true,
        content: @Composable () -> Unit
) {
    val colorScheme =
            when {
                dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
                    val context = LocalContext.current
                    // Convert dynamic Material colors to Ux4gColors
                    if (darkTheme) dynamicDarkColorScheme(context).toUx4gColors()
                    else dynamicLightColorScheme(context).toUx4gColors()
                }
                darkTheme -> DarkColorScheme // Use your custom defined Dark Scheme
                else -> LightColorScheme // Use your custom defined Light Scheme
            }

    // Pass the selected colorScheme to Ux4gTheme
    // You can also pass 'typography' here if you have a custom definition.
    Ux4gTheme(
            darkTheme = darkTheme,
            colorScheme = colorScheme,
            typography = MyTypography, // Uncomment to pass custom typography
            content = content
    )
}
