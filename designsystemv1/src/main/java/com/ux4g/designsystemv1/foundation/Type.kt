package com.ux4g.designsystemv1.foundation

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.ux4g.designsystemv1.R

// Kept available for consumers that explicitly want the bundled Android font.
val NotoSansFontFamily =
        FontFamily(
                Font(R.font.notosans_regular, FontWeight.Normal),
                Font(R.font.notosans_medium, FontWeight.Medium),
                Font(R.font.notosans_semibold, FontWeight.SemiBold),
                Font(R.font.notosans_bold, FontWeight.Bold)
        )

private fun ux4gTextStyle(weight: FontWeight, fontSize: Int, lineHeight: Int): TextStyle =
        TextStyle(
                fontWeight = weight,
                fontSize = fontSize.sp,
                lineHeight = lineHeight.sp
        )

@Immutable
data class Ux4gTypography(
        // Header
        val hXXS_default: TextStyle,
        val hXXS_strong: TextStyle,
        val hXS_default: TextStyle,
        val hXS_strong: TextStyle,
        val hS_default: TextStyle,
        val hS_strong: TextStyle,
        val hM_default: TextStyle,
        val hM_strong: TextStyle,
        val hL_default: TextStyle,
        val hL_strong: TextStyle,
        val hXL_default: TextStyle,
        val hXL_strong: TextStyle,
        val hXXL_default: TextStyle,
        val hXXL_strong: TextStyle,

        // Display
        val dXS_default: TextStyle,
        val dXS_strong: TextStyle,
        val dS_default: TextStyle,
        val dS_strong: TextStyle,
        val dM_default: TextStyle,
        val dM_strong: TextStyle,
        val dL_default: TextStyle,
        val dL_strong: TextStyle,

        // Body
        val bXS_default: TextStyle,
        val bXS_strong: TextStyle,
        val bS_default: TextStyle,
        val bS_strong: TextStyle,
        val bM_default: TextStyle,
        val bM_strong: TextStyle,
        val bL_default: TextStyle,
        val bL_strong: TextStyle,

        // Label
        val lS_default: TextStyle,
        val lS_strong: TextStyle,
        val lM_default: TextStyle,
        val lM_strong: TextStyle,
        val lL_default: TextStyle,
        val lL_strong: TextStyle,
        val lXL_default: TextStyle,
        val lXL_strong: TextStyle,

        // Title
        val tS_default: TextStyle,
        val tS_strong: TextStyle,
        val tM_default: TextStyle,
        val tM_strong: TextStyle,
        val tL_default: TextStyle,
        val tL_strong: TextStyle,
)

val defaultTypography =
        Ux4gTypography(
                hXXS_default = ux4gTextStyle(FontWeight.SemiBold, 14, 16),
                hXXS_strong = ux4gTextStyle(FontWeight.Bold, 14, 16),
                hXS_default = ux4gTextStyle(FontWeight.SemiBold, 16, 20),
                hXS_strong = ux4gTextStyle(FontWeight.Bold, 16, 20),
                hS_default = ux4gTextStyle(FontWeight.SemiBold, 20, 24),
                hS_strong = ux4gTextStyle(FontWeight.Bold, 20, 24),
                hM_default = ux4gTextStyle(FontWeight.SemiBold, 24, 28),
                hM_strong = ux4gTextStyle(FontWeight.Bold, 24, 28),
                hL_default = ux4gTextStyle(FontWeight.SemiBold, 28, 32),
                hL_strong = ux4gTextStyle(FontWeight.Bold, 28, 32),
                hXL_default = ux4gTextStyle(FontWeight.SemiBold, 32, 36),
                hXL_strong = ux4gTextStyle(FontWeight.Bold, 32, 36),
                hXXL_default = ux4gTextStyle(FontWeight.SemiBold, 40, 44),
                hXXL_strong = ux4gTextStyle(FontWeight.Bold, 40, 44),

                dXS_default = ux4gTextStyle(FontWeight.SemiBold, 36, 44),
                dXS_strong = ux4gTextStyle(FontWeight.Bold, 36, 44),
                dS_default = ux4gTextStyle(FontWeight.SemiBold, 40, 52),
                dS_strong = ux4gTextStyle(FontWeight.Bold, 40, 52),
                dM_default = ux4gTextStyle(FontWeight.SemiBold, 52, 72),
                dM_strong = ux4gTextStyle(FontWeight.Bold, 52, 72),
                dL_default = ux4gTextStyle(FontWeight.SemiBold, 60, 80),
                dL_strong = ux4gTextStyle(FontWeight.Bold, 60, 80),

                bXS_default = ux4gTextStyle(FontWeight.Medium, 11, 14),
                bXS_strong = ux4gTextStyle(FontWeight.Bold, 11, 14),
                bS_default = ux4gTextStyle(FontWeight.Medium, 12, 16),
                bS_strong = ux4gTextStyle(FontWeight.Bold, 12, 16),
                bM_default = ux4gTextStyle(FontWeight.Medium, 14, 18),
                bM_strong = ux4gTextStyle(FontWeight.Bold, 14, 18),
                bL_default = ux4gTextStyle(FontWeight.Medium, 16, 20),
                bL_strong = ux4gTextStyle(FontWeight.Bold, 16, 20),

                lS_default = ux4gTextStyle(FontWeight.Medium, 11, 14),
                lS_strong = ux4gTextStyle(FontWeight.Bold, 11, 14),
                lM_default = ux4gTextStyle(FontWeight.Medium, 12, 16),
                lM_strong = ux4gTextStyle(FontWeight.Bold, 12, 16),
                lL_default = ux4gTextStyle(FontWeight.Medium, 14, 18),
                lL_strong = ux4gTextStyle(FontWeight.Bold, 14, 18),
                lXL_default = ux4gTextStyle(FontWeight.Medium, 16, 20),
                lXL_strong = ux4gTextStyle(FontWeight.Bold, 16, 20),

                tS_default = ux4gTextStyle(FontWeight.Normal, 14, 20),
                tS_strong = ux4gTextStyle(FontWeight.SemiBold, 14, 20),
                tM_default = ux4gTextStyle(FontWeight.Normal, 16, 24),
                tM_strong = ux4gTextStyle(FontWeight.SemiBold, 16, 24),
                tL_default = ux4gTextStyle(FontWeight.Normal, 18, 24),
                tL_strong = ux4gTextStyle(FontWeight.SemiBold, 18, 24),
        )

val LocalUx4gTypography = staticCompositionLocalOf { defaultTypography }
