package com.ux4g.designsystemv1.components.buttons

import android.content.res.Configuration
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ux4g.designsystemv1.R
import com.ux4g.designsystemv1.components.Ux4gLoader
import com.ux4g.designsystemv1.foundation.Ux4gBorderWidth
import com.ux4g.designsystemv1.foundation.Ux4gColors
import com.ux4g.designsystemv1.foundation.Ux4gRadius
import com.ux4g.designsystemv1.foundation.Ux4gSpace
import com.ux4g.designsystemv1.theme.Ux4gTheme

enum class Ux4gButtonVariant {
    Primary, // Filled
    Secondary, // Filled with secondary color
    Outline, // Border only
    Text, // Ghost — no fill, no border
    Tonal // Soft tinted fill
}

enum class Ux4gButtonSize(
        val horizontalPadding: Dp,
        val verticalPadding: Dp,
        val defaultHeight: Dp
) {
    XS(12.dp, 4.dp, 24.dp),
    Small(16.dp, 7.dp, 32.dp),
    Medium(20.dp, 10.dp, 40.dp),
    Large(24.dp, 14.dp, 48.dp),
    XL(28.dp, 16.dp, 56.dp)
}

private val Ux4gButtonVariant.defaultBackgroundColor: Color
        @Composable get() =
                when (this) {
                    Ux4gButtonVariant.Primary -> Ux4gTheme.colorScheme.primary
                    Ux4gButtonVariant.Secondary -> Ux4gTheme.colorScheme.secondary
                    Ux4gButtonVariant.Outline, Ux4gButtonVariant.Text -> Color.Transparent
                    Ux4gButtonVariant.Tonal -> Ux4gColors.primary50
                }

private val Ux4gButtonVariant.defaultContentColor: Color
        @Composable get() =
                when (this) {
                    Ux4gButtonVariant.Primary -> Ux4gTheme.colorScheme.onPrimary
                    Ux4gButtonVariant.Secondary -> Ux4gTheme.colorScheme.onSecondary
                    Ux4gButtonVariant.Outline, Ux4gButtonVariant.Text, Ux4gButtonVariant.Tonal ->
                            Ux4gTheme.colorScheme.primary
                }

private val Ux4gButtonVariant.defaultBorderColor: Color
        @Composable get() =
                when (this) {
                    Ux4gButtonVariant.Outline -> Ux4gTheme.colorScheme.primary
                    else -> Color.Transparent
                }

private val Ux4gButtonVariant.defaultBorderWidth: Dp
        get() =
                when (this) {
                    Ux4gButtonVariant.Outline -> Ux4gBorderWidth.Thin
                    else -> Ux4gBorderWidth.None
                }

@Composable
private fun Ux4gButtonSize.defaultTextStyle(): TextStyle =
        when (this) {
            Ux4gButtonSize.XS ->
                    Ux4gTheme.typography.lL_default.copy(
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold
                    )
            Ux4gButtonSize.Small -> Ux4gTheme.typography.lL_default
            Ux4gButtonSize.Medium, Ux4gButtonSize.Large -> Ux4gTheme.typography.lXL_default
            Ux4gButtonSize.XL ->
                    Ux4gTheme.typography.lXL_default.copy(
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold
                    )
        }

@Composable
fun Ux4gButton(
        onClick: () -> Unit,
        modifier: Modifier = Modifier,
        text: String? = null,
        variant: Ux4gButtonVariant = Ux4gButtonVariant.Primary,
        size: Ux4gButtonSize = Ux4gButtonSize.Medium,
        enabled: Boolean = true,
        isLoading: Boolean = false,

        // Styling — null uses the variant default, pass a value to override.
        backgroundColor: Color? = null,
        contentColor: Color? = null,
        disabledBackgroundColor: Color? = null,
        disabledContentColor: Color? = null,
        borderColor: Color? = null,
        borderWidth: Dp? = null,
        borderRadius: Dp = Ux4gRadius.Radius8,
        elevation: Dp = 0.dp,
        contentPadding: PaddingValues =
                PaddingValues(
                        horizontal = size.horizontalPadding,
                        vertical = size.verticalPadding
                ),
        leadingIcon: (@Composable () -> Unit)? = null,
        trailingIcon: (@Composable () -> Unit)? = null,
        iconSize: Dp = 18.dp,
        textStyle: TextStyle? = null,
        interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
        content: (@Composable RowScope.() -> Unit)? = null
) {
        check(text != null || content != null || leadingIcon != null || trailingIcon != null) {
                "Ux4gButton requires text, content, leadingIcon or trailingIcon."
        }

        val shape: Shape = RoundedCornerShape(borderRadius)
        val isDark = isSystemInDarkTheme()

        val effectiveBackgroundColor = backgroundColor ?: variant.defaultBackgroundColor
        val effectiveContentColor = contentColor ?: variant.defaultContentColor
        val effectiveDisabledBackgroundColor =
                disabledBackgroundColor
                        ?: when (variant) {
                            Ux4gButtonVariant.Primary,
                            Ux4gButtonVariant.Secondary,
                            Ux4gButtonVariant.Tonal ->
                                    if (isDark) Ux4gColors.neutral700 else Ux4gColors.neutral200
                            Ux4gButtonVariant.Outline, Ux4gButtonVariant.Text ->
                                    Color.Transparent
                        }
        val effectiveDisabledContentColor = disabledContentColor ?: Ux4gColors.neutral400
        val effectiveBorderColor = borderColor ?: variant.defaultBorderColor
        val effectiveBorderWidth = borderWidth ?: variant.defaultBorderWidth

        val borderColorResolved =
                if (!enabled && variant != Ux4gButtonVariant.Text && variant != Ux4gButtonVariant.Tonal) {
                        if (isDark) Color.Transparent else Ux4gColors.neutral300
                } else {
                        effectiveBorderColor
                }

        val border: BorderStroke? =
                if (effectiveBorderWidth > 0.dp && borderColorResolved != Color.Transparent) {
                        BorderStroke(effectiveBorderWidth, borderColorResolved)
                } else {
                        null
                }

        val resolvedTextStyle = textStyle ?: size.defaultTextStyle()

        Button(
                onClick = onClick,
                modifier = modifier.height(size.defaultHeight),
                enabled = enabled && !isLoading,
                shape = shape,
                colors =
                        ButtonDefaults.buttonColors(
                                containerColor = effectiveBackgroundColor,
                                contentColor = effectiveContentColor,
                                disabledContainerColor = effectiveDisabledBackgroundColor,
                                disabledContentColor = effectiveDisabledContentColor
                        ),
                elevation =
                        ButtonDefaults.buttonElevation(
                                defaultElevation = elevation,
                                pressedElevation = elevation,
                                focusedElevation = elevation,
                                hoveredElevation = elevation,
                                disabledElevation = 0.dp
                        ),
                border = border,
                contentPadding = contentPadding,
                interactionSource = interactionSource
        ) {
                Row(
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                ) {
                        if (isLoading) {
                                Ux4gLoader(
                                        size = 16.dp,
                                        color = LocalContentColor.current,
                                        strokeWidth = 2.dp
                                )
                                Spacer(modifier = Modifier.width(Ux4gSpace.Space8))
                        }

                        if (leadingIcon != null && !isLoading) {
                                Box(
                                        modifier = Modifier.size(iconSize),
                                        contentAlignment = Alignment.Center
                                ) { leadingIcon() }
                                if (text != null || content != null) {
                                        Spacer(modifier = Modifier.width(Ux4gSpace.Space8))
                                }
                        }

                        if (text != null) {
                                Text(
                                        text = text,
                                        style = resolvedTextStyle.copy(
                                                color = LocalContentColor.current
                                        ),
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                )
                        } else if (content != null) {
                                CompositionLocalProvider(
                                        LocalTextStyle provides resolvedTextStyle
                                ) { content() }
                        }

                        if (trailingIcon != null) {
                                if (text != null || content != null || leadingIcon != null) {
                                        Spacer(modifier = Modifier.width(Ux4gSpace.Space8))
                                }
                                Box(
                                        modifier = Modifier.size(iconSize),
                                        contentAlignment = Alignment.Center
                                ) { trailingIcon() }
                        }
                }
        }
}

@Composable
fun Ux4gTextButton(
        onClick: () -> Unit,
        modifier: Modifier = Modifier,
        text: String? = null,
        size: Ux4gButtonSize = Ux4gButtonSize.Medium,
        enabled: Boolean = true,
        isLoading: Boolean = false,
        contentColor: Color? = null,
        borderRadius: Dp = Ux4gRadius.Radius8,
        contentPadding: PaddingValues =
                PaddingValues(
                        horizontal = size.horizontalPadding,
                        vertical = size.verticalPadding
                ),
        leadingIcon: (@Composable () -> Unit)? = null,
        trailingIcon: (@Composable () -> Unit)? = null,
        iconSize: Dp = 18.dp,
        textStyle: TextStyle? = null,
        interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
        content: (@Composable RowScope.() -> Unit)? = null
) {
        Ux4gButton(
                onClick = onClick,
                modifier = modifier,
                text = text,
                variant = Ux4gButtonVariant.Text,
                size = size,
                enabled = enabled,
                isLoading = isLoading,
                contentColor = contentColor,
                borderRadius = borderRadius,
                contentPadding = contentPadding,
                leadingIcon = leadingIcon,
                trailingIcon = trailingIcon,
                iconSize = iconSize,
                textStyle = textStyle,
                interactionSource = interactionSource,
                content = content
        )
}

@Composable
fun Ux4gOutlineButton(
        onClick: () -> Unit,
        modifier: Modifier = Modifier,
        text: String? = null,
        size: Ux4gButtonSize = Ux4gButtonSize.Medium,
        enabled: Boolean = true,
        isLoading: Boolean = false,
        color: Color? = null,
        borderWidth: Dp? = null,
        borderRadius: Dp = Ux4gRadius.Radius8,
        contentPadding: PaddingValues =
                PaddingValues(
                        horizontal = size.horizontalPadding,
                        vertical = size.verticalPadding
                ),
        leadingIcon: (@Composable () -> Unit)? = null,
        trailingIcon: (@Composable () -> Unit)? = null,
        iconSize: Dp = 18.dp,
        textStyle: TextStyle? = null,
        interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
        content: (@Composable RowScope.() -> Unit)? = null
) {
        Ux4gButton(
                onClick = onClick,
                modifier = modifier,
                text = text,
                variant = Ux4gButtonVariant.Outline,
                size = size,
                enabled = enabled,
                isLoading = isLoading,
                contentColor = color,
                borderColor = color,
                borderWidth = borderWidth,
                borderRadius = borderRadius,
                contentPadding = contentPadding,
                leadingIcon = leadingIcon,
                trailingIcon = trailingIcon,
                iconSize = iconSize,
                textStyle = textStyle,
                interactionSource = interactionSource,
                content = content
        )
}

@Preview(name = "Light", showBackground = true)
@Preview(name = "Dark", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun Ux4gButtonPreview() {
        Ux4gTheme {
                Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                        Text("Variants", style = Ux4gTheme.typography.hS_default)

                        Ux4gButton(onClick = {}, text = "Primary")
                        Ux4gButton(onClick = {}, text = "Secondary", variant = Ux4gButtonVariant.Secondary)
                        Ux4gOutlineButton(onClick = {}, text = "Outline")
                        Ux4gTextButton(onClick = {}, text = "Text / Ghost")
                        Ux4gButton(onClick = {}, text = "Tonal", variant = Ux4gButtonVariant.Tonal)

                        Text("Sizes", style = Ux4gTheme.typography.hS_default)

                        Ux4gButton(onClick = {}, text = "XS", size = Ux4gButtonSize.XS)
                        Ux4gButton(onClick = {}, text = "Small", size = Ux4gButtonSize.Small)
                        Ux4gButton(onClick = {}, text = "Medium", size = Ux4gButtonSize.Medium)
                        Ux4gButton(onClick = {}, text = "Large", size = Ux4gButtonSize.Large)
                        Ux4gButton(onClick = {}, text = "XL", size = Ux4gButtonSize.XL)

                        Text("Icons", style = Ux4gTheme.typography.hS_default)

                        Ux4gButton(
                                onClick = {},
                                text = "Leading Icon",
                                leadingIcon = {
                                        Image(
                                                painter =
                                                        painterResource(
                                                                id = R.drawable.ic_launcher_foreground
                                                        ),
                                                contentDescription = null,
                                                modifier = Modifier.size(16.dp)
                                        )
                                }
                        )
                        Ux4gButton(
                                onClick = {},
                                text = "Trailing Icon",
                                trailingIcon = {
                                        Image(
                                                painter =
                                                        painterResource(
                                                                id = R.drawable.ic_launcher_foreground
                                                        ),
                                                contentDescription = null,
                                                modifier = Modifier.size(16.dp)
                                        )
                                }
                        )

                        Text("States", style = Ux4gTheme.typography.hS_default)

                        Ux4gButton(onClick = {}, text = "Loading", isLoading = true)
                        Ux4gButton(onClick = {}, text = "Disabled", enabled = false)
                        Ux4gOutlineButton(onClick = {}, text = "Outline Disabled", enabled = false)

                        Text("Custom Style", style = Ux4gTheme.typography.hS_default)

                        Ux4gButton(
                                onClick = {},
                                text = "Secondary Rounded",
                                backgroundColor = Ux4gTheme.colorScheme.secondary,
                                borderRadius = Ux4gRadius.Radius24
                        )

                        Ux4gButton(onClick = {}) {
                                Image(
                                        painter =
                                                painterResource(
                                                        id = R.drawable.ic_launcher_foreground
                                                ),
                                        contentDescription = "Search",
                                        modifier = Modifier.size(24.dp)
                                )
                                Text("Search")
                        }
                }
        }
}
