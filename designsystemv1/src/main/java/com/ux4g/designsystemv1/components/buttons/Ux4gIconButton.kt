package com.ux4g.designsystemv1.components.buttons

import android.content.res.Configuration
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ux4g.designsystemv1.components.Ux4gLoader
import com.ux4g.designsystemv1.foundation.Ux4gPalette
import com.ux4g.designsystemv1.foundation.Ux4gRadius
import com.ux4g.designsystemv1.theme.Ux4gTheme

enum class Ux4gIconButtonSize {
    XS, // 24px
    S, // 32px
    M, // 40px
    L, // 48px
    XL // 56px
}

enum class Ux4gIconButtonShape {
    Round,
    Rounded
}

enum class Ux4gIconButtonStyle {
    Filled,
    Outline,
    Text,
    Tonal
}

@Composable
fun Ux4gIconButton(
        onClick: () -> Unit,
        modifier: Modifier = Modifier,
        size: Ux4gIconButtonSize = Ux4gIconButtonSize.XL,
        shape: Ux4gIconButtonShape = Ux4gIconButtonShape.Round,
        type: Ux4gIconButtonStyle = Ux4gIconButtonStyle.Filled,
        enabled: Boolean = true,
        isLoading: Boolean = false,

        // Extracted customizable colors
        containerColor: Color? = null,
        contentColor: Color? = null,
        disabledContainerColor: Color? = null,
        disabledContentColor: Color? = null,
        borderColor: Color? = null,
        interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
        icon: @Composable () -> Unit
) {
    val (containerSize, iconScaleSize) =
            when (size) {
                Ux4gIconButtonSize.XL -> 56.dp to 28.dp // 50%
                Ux4gIconButtonSize.L -> 48.dp to 24.dp
                Ux4gIconButtonSize.M -> 40.dp to 20.dp
                Ux4gIconButtonSize.S -> 32.dp to 16.dp
                Ux4gIconButtonSize.XS -> 24.dp to 12.dp
            }

    val buttonShape: Shape =
            when (shape) {
                Ux4gIconButtonShape.Round -> CircleShape
                Ux4gIconButtonShape.Rounded -> RoundedCornerShape(Ux4gRadius.Radius8)
            }

    val defaultContainerColor =
            when (type) {
                Ux4gIconButtonStyle.Filled -> Ux4gTheme.colorScheme.primary
                Ux4gIconButtonStyle.Tonal -> Ux4gPalette.primary50
                Ux4gIconButtonStyle.Outline, Ux4gIconButtonStyle.Text -> Color.Transparent
            }

    val defaultContentColor =
            when (type) {
                Ux4gIconButtonStyle.Filled -> Ux4gTheme.colorScheme.onPrimary
                Ux4gIconButtonStyle.Tonal -> Ux4gPalette.primary700
                Ux4gIconButtonStyle.Outline, Ux4gIconButtonStyle.Text ->
                        Ux4gTheme.colorScheme.primary
            }

    val defaultDisabledContainerC =
            when (type) {
                Ux4gIconButtonStyle.Filled, Ux4gIconButtonStyle.Tonal ->
                        Ux4gTheme.colorScheme.onSurface.copy(alpha = 0.12f)
                Ux4gIconButtonStyle.Outline, Ux4gIconButtonStyle.Text -> Color.Transparent
            }

    val defaultDisabledContentC = Ux4gTheme.colorScheme.onSurface.copy(alpha = 0.38f)

    val actualContainerColor =
            if (enabled) {
                containerColor ?: defaultContainerColor
            } else {
                disabledContainerColor ?: defaultDisabledContainerC
            }

    val actualContentColor =
            if (enabled) {
                contentColor ?: defaultContentColor
            } else {
                disabledContentColor ?: defaultDisabledContentC
            }

    val actualBorderColor =
            borderColor
                    ?: if (type == Ux4gIconButtonStyle.Outline) {
                        if (enabled) Ux4gTheme.colorScheme.primary else defaultDisabledContentC
                    } else {
                        Color.Transparent
                    }

    val defaultBorderWidth = if (type == Ux4gIconButtonStyle.Outline) 1.dp else 0.dp
    val actualBorderWidth =
            if (borderColor != null &&
                            type != Ux4gIconButtonStyle.Outline &&
                            defaultBorderWidth == 0.dp
            )
                    1.dp
            else defaultBorderWidth

    Surface(
            onClick = onClick,
            modifier = modifier.size(containerSize),
            enabled = enabled && !isLoading,
            shape = buttonShape,
            color = actualContainerColor,
            contentColor = actualContentColor,
            border =
                    if (actualBorderWidth > 0.dp || type == Ux4gIconButtonStyle.Outline)
                            BorderStroke(actualBorderWidth, actualBorderColor)
                    else null,
            interactionSource = interactionSource
    ) {
        Box(contentAlignment = Alignment.Center) {
            if (isLoading) {
                Ux4gLoader(size = iconScaleSize, color = actualContentColor, strokeWidth = 3.dp)
            } else {
                CompositionLocalProvider(LocalContentColor provides actualContentColor) {
                    Box(contentAlignment = Alignment.Center) { icon() }
                }
            }
        }
    }
}

// -----------------------------------------------------------------------------------------
// Previews and Examples
// -----------------------------------------------------------------------------------------

@Preview(name = "Light", showBackground = true)
@Preview(name = "Dark", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun Ux4gIconButtonPreview() {
    Ux4gTheme {
        Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text("Icon Button - Types (XL)", style = Ux4gTheme.typography.hS_default)
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Ux4gIconButton(
                        onClick = {},
                        type = Ux4gIconButtonStyle.Filled,
                        size = Ux4gIconButtonSize.XL
                ) { Text("+", style = Ux4gTheme.typography.hM_strong) }
                Ux4gIconButton(
                        onClick = {},
                        type = Ux4gIconButtonStyle.Outline,
                        size = Ux4gIconButtonSize.XL
                ) { Text("+", style = Ux4gTheme.typography.hM_strong) }
                Ux4gIconButton(
                        onClick = {},
                        type = Ux4gIconButtonStyle.Text,
                        size = Ux4gIconButtonSize.XL
                ) { Text("+", style = Ux4gTheme.typography.hM_strong) }
                Ux4gIconButton(
                        onClick = {},
                        type = Ux4gIconButtonStyle.Tonal,
                        size = Ux4gIconButtonSize.XL
                ) { Text("+", style = Ux4gTheme.typography.hM_strong) }
            }

            Text("Icon Button - Shapes (L)", style = Ux4gTheme.typography.hS_default)
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Ux4gIconButton(
                        onClick = {},
                        type = Ux4gIconButtonStyle.Filled,
                        size = Ux4gIconButtonSize.L,
                        shape = Ux4gIconButtonShape.Round
                ) { Text("+", style = Ux4gTheme.typography.hS_strong) }
                Ux4gIconButton(
                        onClick = {},
                        type = Ux4gIconButtonStyle.Filled,
                        size = Ux4gIconButtonSize.L,
                        shape = Ux4gIconButtonShape.Rounded
                ) { Text("+", style = Ux4gTheme.typography.hS_strong) }
            }

            Text("Icon Button - Sizes (Filled)", style = Ux4gTheme.typography.hS_default)
            Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
            ) {
                Ux4gIconButton(onClick = {}, size = Ux4gIconButtonSize.XL) { Text("+") }
                Ux4gIconButton(onClick = {}, size = Ux4gIconButtonSize.L) { Text("+") }
                Ux4gIconButton(onClick = {}, size = Ux4gIconButtonSize.M) { Text("+") }
                Ux4gIconButton(onClick = {}, size = Ux4gIconButtonSize.S) { Text("+") }
                Ux4gIconButton(onClick = {}, size = Ux4gIconButtonSize.XS) { Text("+") }
            }

            Text("Icon Button - States", style = Ux4gTheme.typography.hS_default)
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Ux4gIconButton(onClick = {}, type = Ux4gIconButtonStyle.Filled, enabled = false) {
                    Text("+")
                }
                Ux4gIconButton(onClick = {}, type = Ux4gIconButtonStyle.Outline, enabled = false) {
                    Text("+")
                }
                Ux4gIconButton(onClick = {}, type = Ux4gIconButtonStyle.Tonal, isLoading = true) {
                    Text("+")
                }
            }
        }
    }
}
