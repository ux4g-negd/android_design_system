package com.ux4g.designsystemv1.components

import android.content.res.Configuration
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.ux4g.designsystemv1.R
import com.ux4g.designsystemv1.foundation.Ux4gBorderWidth
import com.ux4g.designsystemv1.foundation.Ux4gIcon
import com.ux4g.designsystemv1.foundation.Ux4gIconComponent
import com.ux4g.designsystemv1.foundation.Ux4gIcons
import com.ux4g.designsystemv1.foundation.Ux4gColors
import com.ux4g.designsystemv1.foundation.Ux4gRadius
import com.ux4g.designsystemv1.theme.Ux4gTheme

enum class Ux4gTagSize {
    M, // 20px
    L // 24px
}

enum class Ux4gTagShape {
    Circular,
    Rectangular
}

enum class Ux4gTagStyle {
    Tonal,
    Filled,
    Outline,
    Text
}

enum class Ux4gTagColor {
    Neutral,
    Brand,
    Success,
    Warning,
    Error,
    Info
}

data class Ux4gTagColors(
        val backgroundColor: Color,
        val contentColor: Color,
        val borderColor: Color
)

@Composable
fun getUx4gTagColors(style: Ux4gTagStyle, tagColor: Ux4gTagColor): Ux4gTagColors {
    val (baseLight, baseNormal, baseDark) =
            when (tagColor) {
                Ux4gTagColor.Neutral ->
                        Triple(
                                Ux4gTheme.colorScheme.onSurface.copy(alpha = 0.1f),
                                Ux4gTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                                Ux4gTheme.colorScheme.onSurface.copy(alpha = 0.8f)
                        )
                Ux4gTagColor.Brand ->
                        Triple(
                                Ux4gColors.primary50,
                                Ux4gTheme.colorScheme.primary,
                                Ux4gColors.primary700
                        )
                Ux4gTagColor.Success ->
                        Triple(Ux4gColors.green50, Ux4gColors.green500, Ux4gColors.green700)
                Ux4gTagColor.Warning ->
                        Triple(Ux4gColors.orange50, Ux4gColors.orange500, Ux4gColors.orange700)
                Ux4gTagColor.Error ->
                        Triple(Ux4gColors.red50, Ux4gTheme.colorScheme.error, Ux4gColors.red700)
                Ux4gTagColor.Info ->
                        Triple(Ux4gColors.blue50, Ux4gColors.blue500, Ux4gColors.blue700)
            }

    return when (style) {
        Ux4gTagStyle.Tonal ->
                Ux4gTagColors(
                        backgroundColor = baseLight,
                        contentColor = baseDark,
                        borderColor = Color.Transparent
                )
        Ux4gTagStyle.Filled ->
                Ux4gTagColors(
                        backgroundColor = baseNormal,
                        contentColor = when (tagColor) {
                            Ux4gTagColor.Brand -> Ux4gTheme.colorScheme.onPrimary
                            Ux4gTagColor.Error -> Ux4gTheme.colorScheme.onError
                            else -> Ux4gTheme.colorScheme.surface
                        },
                        borderColor = Color.Transparent
                )
        Ux4gTagStyle.Outline ->
                Ux4gTagColors(
                        backgroundColor = Color.Transparent,
                        contentColor = baseNormal,
                        borderColor = baseNormal
                )
        Ux4gTagStyle.Text ->
                Ux4gTagColors(
                        backgroundColor = Color.Transparent,
                        contentColor = baseNormal,
                        borderColor = Color.Transparent
                )
    }
}

@Composable
fun Ux4gTag(
        text: String,
        modifier: Modifier = Modifier,
        size: Ux4gTagSize = Ux4gTagSize.M,
        shape: Ux4gTagShape = Ux4gTagShape.Circular,
        style: Ux4gTagStyle = Ux4gTagStyle.Tonal,
        colorScheme: Ux4gTagColor = Ux4gTagColor.Neutral,
        leadingContent: @Composable (() -> Unit)? = null,
        // Custom overrides allowing complete customisation
        customBackgroundColor: Color? = null,
        customContentColor: Color? = null,
        customBorderColor: Color? = null,
        customBorderWidth: Dp? = null,
        customTextStyle: TextStyle? = null,
        customCornerRadius: Dp? = null
) {
    val defaultColors = getUx4gTagColors(style, colorScheme)

    val backgroundColor = customBackgroundColor ?: defaultColors.backgroundColor
    val contentColor = customContentColor ?: defaultColors.contentColor
    val borderColor = customBorderColor ?: defaultColors.borderColor

    val height =
            when (size) {
                Ux4gTagSize.M -> 20.dp
                Ux4gTagSize.L -> 24.dp
            }

    val horizontalPadding =
            when (size) {
                Ux4gTagSize.M -> 8.dp
                Ux4gTagSize.L -> 12.dp
            }

    val defaultTextStyle =
            when (size) {
                Ux4gTagSize.M -> Ux4gTheme.typography.lS_default
                Ux4gTagSize.L -> Ux4gTheme.typography.lM_default
            }
    val typography = customTextStyle ?: defaultTextStyle

    val cornerRadius =
            customCornerRadius
                    ?: when (shape) {
                        Ux4gTagShape.Circular -> 50.dp
                        Ux4gTagShape.Rectangular -> Ux4gRadius.Radius4
                    }
    val theShape = RoundedCornerShape(cornerRadius)

    val borderWidth =
            customBorderWidth ?: if (style == Ux4gTagStyle.Outline) Ux4gBorderWidth.Thin else 0.dp

    Box(
            modifier =
                    modifier.height(height)
                            .clip(theShape)
                            .background(backgroundColor)
                            .then(
                                    if (borderWidth > 0.dp) {
                                        Modifier.border(
                                                BorderStroke(borderWidth, borderColor),
                                                theShape
                                        )
                                    } else Modifier
                            )
                            .padding(horizontal = horizontalPadding),
            contentAlignment = Alignment.Center
    ) {
        Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            if (leadingContent != null) {
                leadingContent()
            }
            Text(text = text, style = typography, color = contentColor)
        }
    }
}

// -----------------------------------------------------------------------------------------
// Previews and Examples
// -----------------------------------------------------------------------------------------

@Composable
private fun SampleIconForTag(icon: Ux4gIcon, size: Ux4gTagSize, color: Color) {
    val iconSize = if (size == Ux4gTagSize.M) 14.dp else 16.dp
    Ux4gIconComponent(
            icon = icon,
            contentDescription = "Icon",
            modifier = Modifier.size(iconSize),
            tint = color
    )
}

@Composable
private fun SampleAvatarForTag(
        text: String,
        size: Ux4gTagSize,
        color: Color,
        backgroundColor: Color
) {
    val avatarSize = if (size == Ux4gTagSize.M) 16.dp else 20.dp
    val textStyle = Ux4gTheme.typography.lS_strong

    Box(
            modifier = Modifier.size(avatarSize).clip(CircleShape).background(backgroundColor),
            contentAlignment = Alignment.Center
    ) { Text(text = text, color = color, style = textStyle, textAlign = TextAlign.Center) }
}

@Composable
private fun SampleImageForTag(size: Ux4gTagSize) {
    val imageSize = if (size == Ux4gTagSize.M) 16.dp else 20.dp
    Image(
            painter =
                    painterResource(
                            id = R.drawable.ic_launcher_foreground
                    ), // Replace with actual image
            contentDescription = "Image",
            modifier =
                    Modifier.size(imageSize)
                            .clip(CircleShape)
                            .background(Ux4gTheme.colorScheme.onSurface.copy(alpha = 0.1f)),
            contentScale = ContentScale.Crop
    )
}

@Preview(name = "Light", showBackground = true)
@Preview(name = "Dark", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun Ux4gTagPreview() {
    Ux4gTheme {
        Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text("Tags - Completely Customizable", style = Ux4gTheme.typography.hS_default)

            // Show size and shape
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Ux4gTag(
                        text = "Label",
                        size = Ux4gTagSize.M,
                        shape = Ux4gTagShape.Circular,
                        style = Ux4gTagStyle.Tonal,
                        colorScheme = Ux4gTagColor.Success
                )
                Ux4gTag(
                        text = "Status Tag",
                        size = Ux4gTagSize.M,
                        shape = Ux4gTagShape.Rectangular,
                        style = Ux4gTagStyle.Outline,
                        colorScheme = Ux4gTagColor.Success
                )
            }

            // Show styles
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Ux4gTag(
                        text = "Tonal",
                        style = Ux4gTagStyle.Tonal,
                        colorScheme = Ux4gTagColor.Brand
                )
                Ux4gTag(
                        text = "Filled",
                        style = Ux4gTagStyle.Filled,
                        colorScheme = Ux4gTagColor.Brand
                )
                Ux4gTag(
                        text = "Outline",
                        style = Ux4gTagStyle.Outline,
                        colorScheme = Ux4gTagColor.Brand
                )
                Ux4gTag(text = "Text", style = Ux4gTagStyle.Text, colorScheme = Ux4gTagColor.Brand)
            }

            // Show colors
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Ux4gTag(
                        text = "Neutral",
                        style = Ux4gTagStyle.Filled,
                        colorScheme = Ux4gTagColor.Neutral
                )
                Ux4gTag(
                        text = "Warning",
                        style = Ux4gTagStyle.Filled,
                        colorScheme = Ux4gTagColor.Warning
                )
                Ux4gTag(
                        text = "Error",
                        style = Ux4gTagStyle.Filled,
                        colorScheme = Ux4gTagColor.Error
                )
                Ux4gTag(text = "Info", style = Ux4gTagStyle.Filled, colorScheme = Ux4gTagColor.Info)
            }

            // Show leading content
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Ux4gTag(
                        text = "Icon",
                        style = Ux4gTagStyle.Tonal,
                        colorScheme = Ux4gTagColor.Success,
                        leadingContent = {
                            SampleIconForTag(
                                    Ux4gIcons.Home,
                                    size = Ux4gTagSize.M,
                                    color = Ux4gColors.green700
                            )
                        }
                )
                Ux4gTag(
                        text = "Avatar",
                        style = Ux4gTagStyle.Outline,
                        colorScheme = Ux4gTagColor.Neutral,
                        leadingContent = {
                            SampleAvatarForTag(
                                    "A",
                                    size = Ux4gTagSize.M,
                                    color = Ux4gColors.white,
                                    backgroundColor = Ux4gTheme.colorScheme.primary
                            )
                        }
                )
            }

            // Fully custom overrides
            Ux4gTag(
                    text = "Custom Colors & Borders",
                    customBackgroundColor = Color(0xFFF3E5F5),
                    customContentColor = Color(0xFF9C27B0),
                    customBorderColor = Color(0xFFE1BEE7),
                    customBorderWidth = 2.dp,
                    customCornerRadius = 12.dp
            )
        }
    }
}
