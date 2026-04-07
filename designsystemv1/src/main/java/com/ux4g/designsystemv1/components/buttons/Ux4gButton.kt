package com.ux4g.designsystemv1.components.buttons

import android.content.res.Configuration
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.ux4g.designsystemv1.R
import com.ux4g.designsystemv1.components.Ux4gLoader
import com.ux4g.designsystemv1.foundation.Ux4gBorderWidth
import com.ux4g.designsystemv1.foundation.Ux4gRadius
import com.ux4g.designsystemv1.foundation.Ux4gSpace
import com.ux4g.designsystemv1.theme.Ux4gTheme

@Composable
fun Ux4gButton(
        onClick: () -> Unit,
        modifier: Modifier = Modifier,
        enabled: Boolean = true,
        isLoading: Boolean = false,

        // Styling Options
        backgroundColor: Color = Ux4gTheme.colorScheme.primary,
        contentColor: Color = Ux4gTheme.colorScheme.onPrimary,
        disabledBackgroundColor: Color = Ux4gTheme.colorScheme.onSurface.copy(alpha = 0.12f),
        disabledContentColor: Color = Ux4gTheme.colorScheme.onSurface.copy(alpha = 0.38f),
        borderColor: Color = Color.Transparent,
        borderWidth: Dp = Ux4gBorderWidth.None,
        borderRadius: Dp = Ux4gRadius.Radius8,
        elevation: Dp = 0.dp,
        contentPadding: PaddingValues =
                PaddingValues(horizontal = Ux4gSpace.Space24, vertical = Ux4gSpace.Space16),
        interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
        content: @Composable RowScope.() -> Unit
) {
        val shape: Shape = RoundedCornerShape(borderRadius)

        val border: BorderStroke? =
                if (borderWidth > 0.dp && borderColor != Color.Transparent) {
                        BorderStroke(borderWidth, borderColor)
                } else {
                        null
                }

        Button(
                onClick = onClick,
                modifier = modifier,
                enabled = enabled && !isLoading,
                shape = shape,
                colors =
                        ButtonDefaults.buttonColors(
                                containerColor = backgroundColor,
                                contentColor = contentColor,
                                disabledContainerColor = disabledBackgroundColor,
                                disabledContentColor = disabledContentColor
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
                        horizontalArrangement = Arrangement.spacedBy(Ux4gSpace.Space4),
                        verticalAlignment = Alignment.CenterVertically
                ) {
                        if (isLoading) {
                            Ux4gLoader(
                                size = 16.dp,
                                color = LocalContentColor.current,
                                strokeWidth = 2.dp
                            )
                        }

                        // Apply design system typography
                        CompositionLocalProvider(
                                LocalTextStyle provides Ux4gTheme.typography.lM_default
                        ) { content() }
                }
        }
}

@Composable
fun Ux4gTextButton(
        onClick: () -> Unit,
        modifier: Modifier = Modifier,
        enabled: Boolean = true,
        isLoading: Boolean = false,
        contentColor: Color = Ux4gTheme.colorScheme.primary,
        borderRadius: Dp = Ux4gRadius.Radius8,
        contentPadding: PaddingValues =
                PaddingValues(horizontal = Ux4gSpace.Space24, vertical = Ux4gSpace.Space16),
        content: @Composable RowScope.() -> Unit
) {
        val shape: Shape = RoundedCornerShape(borderRadius)

        TextButton(
                onClick = onClick,
                modifier = modifier,
                enabled = enabled && !isLoading,
                shape = shape,
                colors =
                        ButtonDefaults.textButtonColors(
                                contentColor = contentColor,
                                disabledContentColor =
                                        Ux4gTheme.colorScheme.onSurface.copy(alpha = 0.38f)
                        ),
                contentPadding = contentPadding,
                content = {
                        Row(
                                horizontalArrangement = Arrangement.spacedBy(Ux4gSpace.Space4),
                                verticalAlignment = Alignment.CenterVertically
                        ) {
                                if (isLoading) {
                                    Ux4gLoader(
                                        size = 16.dp,
                                        color = LocalContentColor.current,
                                        strokeWidth = 2.dp
                                    )
                                }
                                CompositionLocalProvider(
                                        LocalTextStyle provides Ux4gTheme.typography.lM_default
                                ) { content() }
                        }
                }
        )
}

@Composable
fun Ux4gOutlineButton(
        onClick: () -> Unit,
        modifier: Modifier = Modifier,
        enabled: Boolean = true,
        isLoading: Boolean = false,
        color: Color = Ux4gTheme.colorScheme.primary,
        borderWidth: Dp = Ux4gBorderWidth.Thin,
        borderRadius: Dp = Ux4gRadius.Radius8,
        contentPadding: PaddingValues =
                PaddingValues(horizontal = Ux4gSpace.Space24, vertical = Ux4gSpace.Space16),
        interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
        content: @Composable RowScope.() -> Unit
) {
        val shape: Shape = RoundedCornerShape(borderRadius)
        val borderColor =
                if (enabled) color else Ux4gTheme.colorScheme.onSurface.copy(alpha = 0.12f)
        val contentColor =
                if (enabled) color else Ux4gTheme.colorScheme.onSurface.copy(alpha = 0.38f)

    OutlinedButton(
            onClick = onClick,
            modifier = modifier,
            enabled = enabled && !isLoading,
            shape = shape,
            colors =
                    ButtonDefaults.outlinedButtonColors(
                            containerColor = Color.Transparent,
                            contentColor = contentColor,
                            disabledContainerColor = Color.Transparent,
                            disabledContentColor = contentColor
                    ),
            border = BorderStroke(borderWidth, borderColor),
            contentPadding = contentPadding,
            interactionSource = interactionSource
    ) {
            Row(
                    horizontalArrangement = Arrangement.spacedBy(Ux4gSpace.Space4),
                    verticalAlignment = Alignment.CenterVertically
            ) {
                    if (isLoading) {
                        Ux4gLoader(
                            size = 16.dp,
                            color = LocalContentColor.current,
                            strokeWidth = 2.dp
                        )
                    }

                    CompositionLocalProvider(
                            LocalTextStyle provides Ux4gTheme.typography.lM_default
                    ) { content() }
            }
    }
}

@Preview(name = "Light", showBackground = true)
@Preview(name = "Dark", uiMode = Configuration.UI_MODE_NIGHT_YES)
// @Preview(showBackground = true)
@Composable
private fun Ux4gButtonPreview() {
        Ux4gTheme {
                Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                        // Primary Button
                        Ux4gButton(onClick = {}) { Text("Primary Button") }

                        // Button with Icon (Verify spacing)
                        Ux4gButton(onClick = {}) {
                                // For demonstration, using simple text instead of actual Icon

                                // set Image from resource
                                Image(
                                        painter =
                                                painterResource(
                                                        id = R.drawable.ic_launcher_foreground
                                                ),
                                        contentDescription = "Search",
                                        modifier = Modifier.size(24.dp)
                                )
                                Text("Search")

                                Image(
                                        painter =
                                                painterResource(
                                                        id = R.drawable.ic_launcher_foreground
                                                ),
                                        contentDescription = "Search",
                                        modifier = Modifier.size(24.dp)
                                )
                        }
                        // Outline Button (New Component)
                        Ux4gOutlineButton(onClick = {}, color = Ux4gTheme.colorScheme.primary) {
                                Image(
                                        painter =
                                                painterResource(
                                                        id = R.drawable.ic_launcher_foreground
                                                ),
                                        contentDescription = "Search",
                                        modifier = Modifier.size(24.dp)
                                )
                                Text("Outline Button")
                        }

                        // Secondary Custom Button
                        Ux4gButton(
                                onClick = {},
                                backgroundColor = Ux4gTheme.colorScheme.secondary,
                                borderRadius = Ux4gRadius.Radius24
                        ) { Text("Secondary Rounded") }

                        // Loading Button
                        Ux4gButton(onClick = {}, isLoading = true) { Text("Loading...") }

                        // Text Button
                        Ux4gTextButton(onClick = {}) {
                                Image(
                                        painter =
                                                painterResource(
                                                        id = R.drawable.ic_launcher_foreground
                                                ),
                                        contentDescription = "Search",
                                        modifier = Modifier.size(24.dp)
                                )
                                Text("Text Button")
                        }

                        // Disabled Button
                        Ux4gButton(onClick = {}, enabled = false) { Text("Disabled Button") }
                }
        }
}
