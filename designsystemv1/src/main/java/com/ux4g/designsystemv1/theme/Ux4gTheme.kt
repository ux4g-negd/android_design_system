package com.ux4g.designsystemv1.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import com.ux4g.designsystemv1.foundation.LocalUx4gColors
import com.ux4g.designsystemv1.foundation.LocalUx4gTypography
import com.ux4g.designsystemv1.foundation.Ux4gColors
import com.ux4g.designsystemv1.foundation.Ux4gTypography
import com.ux4g.designsystemv1.foundation.darkColors
import com.ux4g.designsystemv1.foundation.defaultTypography
import com.ux4g.designsystemv1.foundation.lightColors

@Composable
fun Ux4gTheme(
        darkTheme: Boolean = isSystemInDarkTheme(),
        // User can override colorScheme here. Defaults to our standard set.
        colorScheme: Ux4gColors = if (darkTheme) darkColors else lightColors,
        // User can override typography here.
        typography: Ux4gTypography = defaultTypography,
        content: @Composable () -> Unit
) {
        val materialColorScheme =
                if (darkTheme) {
                        darkColorScheme(
                                primary = colorScheme.primary,
                                onPrimary = colorScheme.onPrimary,
                                secondary = colorScheme.secondary,
                                onSecondary = colorScheme.onSecondary,
                                background = colorScheme.background,
                                onBackground = colorScheme.onBackground,
                                surface = colorScheme.surface,
                                onSurface = colorScheme.onSurface,
                                error = colorScheme.error,
                                onError = colorScheme.onError
                        )
                } else {
                        lightColorScheme(
                                primary = colorScheme.primary,
                                onPrimary = colorScheme.onPrimary,
                                secondary = colorScheme.secondary,
                                onSecondary = colorScheme.onSecondary,
                                background = colorScheme.background,
                                onBackground = colorScheme.onBackground,
                                surface = colorScheme.surface,
                                onSurface = colorScheme.onSurface,
                                error = colorScheme.error,
                                onError = colorScheme.onError,
                        )
                }

        // Map our custom typography to Material 3 typography
        val materialTypography =
                androidx.compose.material3.Typography(
                        displaySmall = typography.hS_default, // Mapping h1 to displaySmall
                        headlineMedium = typography.hM_default, // Mapping h2 to headlineMedium
                        titleLarge = typography.hL_default, // Mapping h3 to titleLarge
                        bodyLarge = typography.bL_default, // Mapping body1 to bodyLarge
                        bodyMedium = typography.bM_default, // Mapping body2 to bodyMedium
                        labelLarge = typography.lL_default, // Mapping button to labelLarge
                        labelSmall = typography.lS_default // Mapping caption to labelSmall
                )

        CompositionLocalProvider(
                LocalUx4gColors provides colorScheme,
                LocalUx4gTypography provides typography
        ) {
                MaterialTheme(
                        colorScheme = materialColorScheme,
                        typography = materialTypography,
                        content = content
                )
        }
}

// Singleton object to access the values
object Ux4gTheme {
        val colorScheme: Ux4gColors
                @Composable get() = LocalUx4gColors.current

        val typography: Ux4gTypography
                @Composable get() = LocalUx4gTypography.current
}

fun androidx.compose.material3.ColorScheme.toUx4gColors(): Ux4gColors {
        return Ux4gColors(
                primary = this.primary,
                onPrimary = this.onPrimary,
                secondary = this.secondary,
                onSecondary = this.onSecondary,
                background = this.background,
                onBackground = this.onBackground,
                surface = this.surface,
                onSurface = this.onSurface,
                error = this.error,
                onError = this.onError
        )
}
