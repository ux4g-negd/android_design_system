package com.ux4g.designsystemv1.components

import android.content.res.Configuration
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.ux4g.designsystemv1.foundation.Ux4gPalette
import com.ux4g.designsystemv1.theme.Ux4gTheme

enum class Ux4gToggleSize(val width: Dp, val height: Dp, val thumbSize: Dp, val thumbPadding: Dp) {
    S(32.dp, 18.dp, 14.dp, 2.dp),
    M(40.dp, 22.dp, 18.dp, 2.dp),
    L(48.dp, 28.dp, 24.dp, 2.dp)
}

enum class Ux4gToggleLabelPosition {
    NoLabel,
    Left,
    Right,
    BothSides
}

@Immutable
data class Ux4gToggleColors(
        val checkedTrackColor: Color,
        val uncheckedTrackColor: Color,
        val checkedThumbColor: Color,
        val uncheckedThumbColor: Color,
        val disabledCheckedTrackColor: Color,
        val disabledUncheckedTrackColor: Color,
        val disabledCheckedThumbColor: Color,
        val disabledUncheckedThumbColor: Color
)

object Ux4gToggleDefaults {
    @Composable
    fun colors(
            checkedTrackColor: Color = Ux4gTheme.colorScheme.primary,
            uncheckedTrackColor: Color = Ux4gTheme.colorScheme.onSurface.copy(alpha = 0.3f),
            checkedThumbColor: Color = Ux4gTheme.colorScheme.surface,
            uncheckedThumbColor: Color = Ux4gTheme.colorScheme.surface,
            disabledCheckedTrackColor: Color = Ux4gTheme.colorScheme.primary.copy(alpha = 0.4f),
            disabledUncheckedTrackColor: Color = Ux4gTheme.colorScheme.onSurface.copy(alpha = 0.2f),
            disabledCheckedThumbColor: Color = Ux4gTheme.colorScheme.surface,
            disabledUncheckedThumbColor: Color = Ux4gTheme.colorScheme.surface
    ): Ux4gToggleColors =
            Ux4gToggleColors(
                    checkedTrackColor,
                    uncheckedTrackColor,
                    checkedThumbColor,
                    uncheckedThumbColor,
                    disabledCheckedTrackColor,
                    disabledUncheckedTrackColor,
                    disabledCheckedThumbColor,
                    disabledUncheckedThumbColor
            )
}

@Composable
fun Ux4gToggle(
        checked: Boolean,
        onCheckedChange: ((Boolean) -> Unit)?,
        modifier: Modifier = Modifier,
        label: String? = null,
        description: String? = null,
        size: Ux4gToggleSize = Ux4gToggleSize.M,
        labelPosition: Ux4gToggleLabelPosition = Ux4gToggleLabelPosition.Right,
        enabled: Boolean = true,
        colors: Ux4gToggleColors = Ux4gToggleDefaults.colors(),
        interactionSource: MutableInteractionSource = remember { MutableInteractionSource() }
) {
    val trackColor by
            animateColorAsState(
                    targetValue =
                            when {
                                !enabled && checked -> colors.disabledCheckedTrackColor
                                !enabled && !checked -> colors.disabledUncheckedTrackColor
                                checked -> colors.checkedTrackColor
                                else -> colors.uncheckedTrackColor
                            },
                    label = "trackColor"
            )

    val thumbColor by
            animateColorAsState(
                    targetValue =
                            when {
                                !enabled && checked -> colors.disabledCheckedThumbColor
                                !enabled && !checked -> colors.disabledUncheckedThumbColor
                                checked -> colors.checkedThumbColor
                                else -> colors.uncheckedThumbColor
                            },
                    label = "thumbColor"
            )

    val endOffset = size.width - size.thumbSize - size.thumbPadding
    val startOffset = size.thumbPadding

    val thumbOffset by
            animateDpAsState(
                    targetValue = if (checked) endOffset else startOffset,
                    animationSpec = tween(durationMillis = 150),
                    label = "thumbOffset"
            )

    val toggleModifier =
            if (onCheckedChange != null) {
                Modifier.toggleable(
                        value = checked,
                        onValueChange = onCheckedChange,
                        enabled = enabled,
                        role = Role.Switch,
                        interactionSource = interactionSource,
                        indication = null // Ripple goes on thumb
                )
            } else Modifier

    val toggleControl: @Composable (Modifier) -> Unit = { overrideModifier ->
        Box(
                modifier =
                        overrideModifier
                                .size(width = size.width, height = size.height)
                                .background(color = trackColor, shape = CircleShape),
                contentAlignment = Alignment.CenterStart
        ) {
            Box(
                    modifier =
                            Modifier.offset(x = thumbOffset)
                                    .size(size.thumbSize)
                                    .indication(
                                            interactionSource = interactionSource,
                                            indication = ripple(bounded = false, radius = 24.dp)
                                    )
                                    .shadow(
                                            elevation = if (enabled) 2.dp else 0.dp,
                                            shape = CircleShape
                                    )
                                    .background(color = thumbColor, shape = CircleShape)
            )
        }
    }

    val labelTextStyle =
            when (size) {
                Ux4gToggleSize.S -> Ux4gTheme.typography.lM_default
                Ux4gToggleSize.M -> Ux4gTheme.typography.lL_default
                Ux4gToggleSize.L -> Ux4gTheme.typography.lXL_default
            }

    val descriptionTextStyle =
            when (size) {
                Ux4gToggleSize.S -> Ux4gTheme.typography.lS_default
                Ux4gToggleSize.M -> Ux4gTheme.typography.lM_default
                Ux4gToggleSize.L -> Ux4gTheme.typography.lL_default
            }

    val texts: @Composable (Modifier) -> Unit = { innerModifier ->
        if (label != null || description != null) {
            Column(
                    modifier = innerModifier.padding(vertical = 2.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                if (label != null) {
                    Text(
                            text = label,
                            style = labelTextStyle,
                            color =
                                    if (enabled) Ux4gTheme.colorScheme.onSurface
                                    else Ux4gTheme.colorScheme.onSurface.copy(alpha = 0.38f)
                    )
                }
                if (description != null) {
                    Text(
                            text = description,
                            style = descriptionTextStyle,
                            color =
                                    if (enabled) Ux4gTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                                    else Ux4gTheme.colorScheme.onSurface.copy(alpha = 0.38f)
                    )
                }
            }
        }
    }

    if (labelPosition == Ux4gToggleLabelPosition.NoLabel) {
        toggleControl(modifier.then(toggleModifier))
    } else {
        Row(
                modifier =
                        modifier
                                .run {
                                    if (labelPosition == Ux4gToggleLabelPosition.BothSides)
                                            fillMaxWidth()
                                    else this
                                }
                                .then(toggleModifier),
                verticalAlignment = Alignment.CenterVertically
        ) {
            if (labelPosition == Ux4gToggleLabelPosition.Right) {
                toggleControl(Modifier)
                Spacer(modifier = Modifier.width(12.dp))
                texts(Modifier)
            } else if (labelPosition == Ux4gToggleLabelPosition.Left) {
                texts(Modifier)
                Spacer(modifier = Modifier.width(12.dp))
                toggleControl(Modifier)
            } else if (labelPosition == Ux4gToggleLabelPosition.BothSides) {
                texts(Modifier.weight(1f))
                Spacer(modifier = Modifier.width(12.dp))
                toggleControl(Modifier)
            }
        }
    }
}

// -----------------------------------------------------------------------------------------
// Previews and Examples
// -----------------------------------------------------------------------------------------

@Preview(name = "Light", showBackground = true)
@Preview(name = "Dark", uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
private fun Ux4gTogglePreview() {
    Ux4gTheme {
        Surface(color = Ux4gTheme.colorScheme.background) {
            Column(
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                var s1 by remember { mutableStateOf(false) }
                Ux4gToggle(
                        checked = s1,
                        onCheckedChange = { s1 = it },
                        label = "Medium Default (Right)",
                        size = Ux4gToggleSize.M
                )

                var s2 by remember { mutableStateOf(true) }
                Ux4gToggle(
                        checked = s2,
                        onCheckedChange = { s2 = it },
                        label = "Large Label on Left",
                        description = "Description text goes below",
                        size = Ux4gToggleSize.L,
                        labelPosition = Ux4gToggleLabelPosition.Left
                )

                var s3 by remember { mutableStateOf(true) }
                Ux4gToggle(
                        checked = s3,
                        onCheckedChange = { s3 = it },
                        label = "Small Both Sides Layout",
                        description = "This stretches across the entire row width perfectly.",
                        size = Ux4gToggleSize.S,
                        labelPosition = Ux4gToggleLabelPosition.BothSides
                )

                Text("Disabled States", style = Ux4gTheme.typography.hS_default)
                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    Ux4gToggle(
                            checked = false,
                            onCheckedChange = null,
                            enabled = false,
                            labelPosition = Ux4gToggleLabelPosition.NoLabel
                    )
                    Ux4gToggle(
                            checked = true,
                            onCheckedChange = null,
                            enabled = false,
                            labelPosition = Ux4gToggleLabelPosition.NoLabel
                    )
                }
            }
        }
    }
}
