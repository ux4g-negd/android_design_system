package com.ux4g.designsystemv1.foundation

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.ux4g.designsystemv1.R

// Define the Custom Font Family
val NotoSansFontFamily =
        FontFamily(
                Font(R.font.notosans_regular, FontWeight.Normal),
                Font(R.font.notosans_medium, FontWeight.Medium),
                Font(R.font.notosans_semibold, FontWeight.SemiBold),
                Font(R.font.notosans_bold, FontWeight.Bold)
        )

// 1. Define the Typography System dataclass
@Immutable
data class Ux4gTypography(
        // header
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

        // display
        val dXS_default: TextStyle,
        val dXS_strong: TextStyle,
        val dS_default: TextStyle,
        val dS_strong: TextStyle,
        val dM_default: TextStyle,
        val dM_strong: TextStyle,
        val dL_default: TextStyle,
        val dL_strong: TextStyle,

        // body
        val bXS_default: TextStyle,
        val bXS_strong: TextStyle,
        val bS_default: TextStyle,
        val bS_strong: TextStyle,
        val bM_default: TextStyle,
        val bM_strong: TextStyle,
        val bL_default: TextStyle,
        val bL_strong: TextStyle,

        // label
        val lS_default: TextStyle,
        val lS_strong: TextStyle,
        val lM_default: TextStyle,
        val lM_strong: TextStyle,
        val lL_default: TextStyle,
        val lL_strong: TextStyle,
        val lXL_default: TextStyle,
        val lXL_strong: TextStyle,

        // title
        val tS_default: TextStyle,
        val tS_strong: TextStyle,
        val tM_default: TextStyle,
        val tM_strong: TextStyle,
        val tL_default: TextStyle,
        val tL_strong: TextStyle,
)

// 2. Define the implementations
val defaultTypography =
        Ux4gTypography(
                hXXS_default =
                        TextStyle(
                                fontFamily = NotoSansFontFamily,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 14.sp,
                                lineHeight = 16.sp,
                        ),
                hXXS_strong =
                        TextStyle(
                                fontFamily = NotoSansFontFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                lineHeight = 16.sp,
                        ),
                hXS_default =
                        TextStyle(
                                fontFamily = NotoSansFontFamily,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 16.sp,
                                lineHeight = 20.sp,
                        ),
                hXS_strong =
                        TextStyle(
                                fontFamily = NotoSansFontFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                lineHeight = 20.sp,
                        ),
                hS_default =
                        TextStyle(
                                fontFamily = NotoSansFontFamily,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 20.sp,
                                lineHeight = 24.sp
                        ),
                hS_strong =
                        TextStyle(
                                fontFamily = NotoSansFontFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 20.sp,
                                lineHeight = 24.sp,
                        ),
                hM_default =
                        TextStyle(
                                fontFamily = NotoSansFontFamily,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 24.sp,
                                lineHeight = 28.sp
                        ),
                hM_strong =
                        TextStyle(
                                fontFamily = NotoSansFontFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 24.sp,
                                lineHeight = 28.sp
                        ),
                hL_default =
                        TextStyle(
                                fontFamily = NotoSansFontFamily,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 28.sp,
                                lineHeight = 32.sp
                        ),
                hL_strong =
                        TextStyle(
                                fontFamily = NotoSansFontFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 28.sp,
                                lineHeight = 32.sp
                        ),
                hXL_default =
                        TextStyle(
                                fontFamily = NotoSansFontFamily,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 32.sp,
                                lineHeight = 36.sp
                        ),
                hXL_strong =
                        TextStyle(
                                fontFamily = NotoSansFontFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 32.sp,
                                lineHeight = 36.sp
                        ),
                hXXL_default =
                        TextStyle(
                                fontFamily = NotoSansFontFamily,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 40.sp,
                                lineHeight = 44.sp
                        ),
                hXXL_strong =
                        TextStyle(
                                fontFamily = NotoSansFontFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 40.sp,
                                lineHeight = 44.sp
                        ),
                // Display styles

                dXS_default =
                        TextStyle(
                                fontFamily = NotoSansFontFamily,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 36.sp,
                                lineHeight = 44.sp
                        ),
                dXS_strong =
                        TextStyle(
                                fontFamily = NotoSansFontFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 36.sp,
                                lineHeight = 44.sp
                        ),
                dS_default =
                        TextStyle(
                                fontFamily = NotoSansFontFamily,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 40.sp,
                                lineHeight = 52.sp
                        ),
                dS_strong =
                        TextStyle(
                                fontFamily = NotoSansFontFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 40.sp,
                                lineHeight = 52.sp
                        ),
                dM_default =
                        TextStyle(
                                fontFamily = NotoSansFontFamily,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 52.sp,
                                lineHeight = 72.sp
                        ),
                dM_strong =
                        TextStyle(
                                fontFamily = NotoSansFontFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 52.sp,
                                lineHeight = 72.sp
                        ),
                dL_default =
                        TextStyle(
                                fontFamily = NotoSansFontFamily,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 60.sp,
                                lineHeight = 80.sp
                        ),
                dL_strong =
                        TextStyle(
                                fontFamily = NotoSansFontFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 60.sp,
                                lineHeight = 80.sp
                        ),

                // body styles

                bXS_default =
                        TextStyle(
                                fontFamily = NotoSansFontFamily,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 12.sp,
                                lineHeight = 16.sp
                        ),
                bXS_strong =
                        TextStyle(
                                fontFamily = NotoSansFontFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp,
                                lineHeight = 16.sp
                        ),
                bS_default =
                        TextStyle(
                                fontFamily = NotoSansFontFamily,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 14.sp,
                                lineHeight = 40.sp
                        ),
                bS_strong =
                        TextStyle(
                                fontFamily = NotoSansFontFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                lineHeight = 40.sp
                        ),
                bM_default =
                        TextStyle(
                                fontFamily = NotoSansFontFamily,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 16.sp,
                                lineHeight = 24.sp
                        ),
                bM_strong =
                        TextStyle(
                                fontFamily = NotoSansFontFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                lineHeight = 24.sp
                        ),
                bL_default =
                        TextStyle(
                                fontFamily = NotoSansFontFamily,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 18.sp,
                                lineHeight = 24.sp
                        ),
                bL_strong =
                        TextStyle(
                                fontFamily = NotoSansFontFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp,
                                lineHeight = 24.sp
                        ),

                // label styles

                lS_default =
                        TextStyle(
                                fontFamily = NotoSansFontFamily,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 11.sp,
                                lineHeight = 14.sp
                        ),
                lS_strong =
                        TextStyle(
                                fontFamily = NotoSansFontFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.sp,
                                lineHeight = 14.sp
                        ),
                lM_default =
                        TextStyle(
                                fontFamily = NotoSansFontFamily,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 12.sp,
                                lineHeight = 16.sp
                        ),
                lM_strong =
                        TextStyle(
                                fontFamily = NotoSansFontFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp,
                                lineHeight = 16.sp
                        ),
                lL_default =
                        TextStyle(
                                fontFamily = NotoSansFontFamily,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 14.sp,
                                lineHeight = 18.sp
                        ),
                lL_strong =
                        TextStyle(
                                fontFamily = NotoSansFontFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                lineHeight = 18.sp
                        ),
                lXL_default =
                        TextStyle(
                                fontFamily = NotoSansFontFamily,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 16.sp,
                                lineHeight = 20.sp
                        ),
                lXL_strong =
                        TextStyle(
                                fontFamily = NotoSansFontFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                lineHeight = 20.sp
                        ),

                // title styles

                tS_default =
                        TextStyle(
                                fontFamily = NotoSansFontFamily,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 16.sp,
                                lineHeight = 20.sp
                        ),
                tS_strong =
                        TextStyle(
                                fontFamily = NotoSansFontFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                lineHeight = 20.sp
                        ),
                tM_default =
                        TextStyle(
                                fontFamily = NotoSansFontFamily,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 20.sp,
                                lineHeight = 24.sp
                        ),
                tM_strong =
                        TextStyle(
                                fontFamily = NotoSansFontFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 20.sp,
                                lineHeight = 24.sp
                        ),
                tL_default =
                        TextStyle(
                                fontFamily = NotoSansFontFamily,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 24.sp,
                                lineHeight = 28.sp
                        ),
                tL_strong =
                        TextStyle(
                                fontFamily = NotoSansFontFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 24.sp,
                                lineHeight = 28.sp
                        ),
        )

// 3. CompositionLocal
val LocalUx4gTypography = staticCompositionLocalOf { defaultTypography }
