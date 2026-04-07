package com.ux4g.ux4gdesignsystem.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.ux4g.designsystemv1.foundation.defaultTypography

// Set of Material typography styles to start with
val Typography =
        Typography(
                bodyLarge =
                        TextStyle(
                                fontFamily = FontFamily.Default,
                                fontWeight = FontWeight.Normal,
                                fontSize = 16.sp,
                                lineHeight = 24.sp,
                                letterSpacing = 0.5.sp
                        )
        )

// Define your Custom Typography
// Define your Custom Typography
val MyTypography =
        defaultTypography.copy(
                // Option 1: Modify the existing default style (inherits other properties)
                hL_default =
                        defaultTypography.hL_default.copy(
                                fontFamily = FontFamily.Serif,
                                fontWeight = FontWeight.Bold
                        ),
                // Option 2: Directly pass a completely new TextStyle
                bM_default =
                        TextStyle(
                                fontFamily = FontFamily.Monospace,
                                fontWeight = FontWeight.Normal,
                                fontSize = 18.sp,
                                lineHeight = 26.sp
                        )
        )
