package com.ux4g.designsystemv1.components.biometric

import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Colors used across the biometric verification flow.
 * Exact parity with Flutter `BiometricColors`.
 * Fixed hex values matching Flutter Ux4gColors (not from theme).
 */
@Immutable
data class BiometricColors(
    val primary: Color = Color(0xFF6A4EFF),          // Flutter: Ux4gColors.primary500
    val success: Color = Color(0xFF1AA64A),           // Flutter: Ux4gColors.green500
    val error: Color = Color(0xFFF55E57),             // Flutter: Ux4gColors.red500
    val warning: Color = Color(0xFFC47D00),           // Flutter: Ux4gColors.secondary500
    val surface: Color = Color(0xFFFFFFFF),            // Flutter: Ux4gColors.white
    val onSurface: Color = Color(0xFF121212),          // Flutter: Ux4gColors.gray900
    val overlay: Color = Color(0xCC000000),             // Flutter: Color(0xCC000000)
    val ovalGuide: Color = Color(0xFFFFFFFF),          // Flutter: Ux4gColors.white
    val ovalActive: Color = Color(0xFF1AA64A),         // Flutter: Ux4gColors.green500
    val ovalBlink: Color = Color(0xFF6A4EFF),          // Flutter: Ux4gColors.primary500
    val ovalError: Color = Color(0xFFF55E57),          // Flutter: Ux4gColors.red500
    val cardBackground: Color = Color(0xFFF8F8F8),     // Flutter: Color(0xFFF8F8F8)
    val instructionCardBg: Color = Color(0xFFFFFFFF),  // Flutter: Ux4gColors.white
    val shimmer: Color = Color(0xFFE0E0E0),            // Flutter: Color(0xFFE0E0E0)
    val statusBadgeBg: Color = Color(0xFF1AA64A),      // Flutter: Ux4gColors.green500
    val attemptWarningBg: Color = Color(0xFFFFF8E1),   // Flutter: Color(0xFFFFF8E1)
    val attemptWarningBorder: Color = Color(0xFFFFE082), // Flutter: Color(0xFFFFE082)
    val attemptLastBg: Color = Color(0xFFFFEBEE),      // Flutter: Color(0xFFFFEBEE)
    val attemptLastBorder: Color = Color(0xFFEF9A9A)   // Flutter: Color(0xFFEF9A9A)
)

/**
 * Typography presets for biometric screens.
 * Exact parity with Flutter `BiometricTypography`.
 */
@Immutable
data class BiometricTypography(
    val headline: TextStyle = TextStyle(
        fontSize = 24.sp,
        fontWeight = FontWeight.Bold,
        lineHeight = (24 * 1.3).sp
    ),
    val title: TextStyle = TextStyle(
        fontSize = 18.sp,
        fontWeight = FontWeight.SemiBold,
        lineHeight = (18 * 1.4).sp
    ),
    val body: TextStyle = TextStyle(
        fontSize = 14.sp,
        fontWeight = FontWeight.Normal,
        lineHeight = (14 * 1.5).sp
    ),
    val label: TextStyle = TextStyle(
        fontSize = 14.sp,
        fontWeight = FontWeight.SemiBold,
        lineHeight = (14 * 1.4).sp
    ),
    val caption: TextStyle = TextStyle(
        fontSize = 12.sp,
        fontWeight = FontWeight.Normal,
        lineHeight = (12 * 1.5).sp
    ),
    val button: TextStyle = TextStyle(
        fontSize = 16.sp,
        fontWeight = FontWeight.SemiBold,
        lineHeight = 16.sp
    ),
    val instruction: TextStyle = TextStyle(
        fontSize = 16.sp,
        fontWeight = FontWeight.Medium,
        lineHeight = (16 * 1.4).sp
    )
)

/**
 * Theme configuration for the biometric verification flow.
 * Exact parity with Flutter `BiometricThemeData`.
 */
@Immutable
data class BiometricThemeData(
    val colors: BiometricColors = BiometricColors(),
    val typography: BiometricTypography = BiometricTypography(),
    val animationDurationMs: Int = 300,
    val cardRadius: Dp = 16.dp,
    val buttonRadius: Dp = 12.dp,
    val screenPadding: PaddingValues = PaddingValues(horizontal = 24.dp)
)

val LocalBiometricTheme = staticCompositionLocalOf { BiometricThemeData() }

object BiometricTheme {
    val current: BiometricThemeData
        @Composable get() = LocalBiometricTheme.current
}

/**
 * Provides [BiometricThemeData] to the Composable tree.
 */
@Composable
fun BiometricThemeProvider(
    data: BiometricThemeData = BiometricThemeData(),
    content: @Composable () -> Unit
) {
    CompositionLocalProvider(LocalBiometricTheme provides data) {
        content()
    }
}
