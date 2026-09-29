package com.ux4g.designsystemv1.components

import android.content.res.Configuration
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.material3.ripple
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.ux4g.designsystemv1.foundation.Ux4gColors
import com.ux4g.designsystemv1.foundation.Ux4gIcon
import com.ux4g.designsystemv1.foundation.Ux4gIconComponent
import com.ux4g.designsystemv1.foundation.Ux4gIcons
import com.ux4g.designsystemv1.foundation.defaultTypography
import com.ux4g.designsystemv1.theme.Ux4gTheme

enum class Ux4gSwitchSize(
    val width: Dp,
    val height: Dp,
    val thumbSize: Dp,
    val thumbPadding: Dp
) {
    S(32.dp, 18.dp, 14.dp, 2.dp),
    M(40.dp, 22.dp, 18.dp, 2.dp),
    L(48.dp, 28.dp, 24.dp, 2.dp)
}

enum class Ux4gSwitchLabelPosition {
    NoLabel,
    Left,
    Right,
    BothSides
}

@Deprecated("Use Ux4gSwitchSize instead", ReplaceWith("Ux4gSwitchSize"))
typealias Ux4gToggleSize = Ux4gSwitchSize

@Deprecated("Use Ux4gSwitchLabelPosition instead", ReplaceWith("Ux4gSwitchLabelPosition"))
typealias Ux4gToggleLabelPosition = Ux4gSwitchLabelPosition

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
        checkedTrackColor: Color = Ux4gColors.primary600,
        uncheckedTrackColor: Color = Ux4gColors.neutral300,
        checkedThumbColor: Color = Ux4gColors.white,
        uncheckedThumbColor: Color = Ux4gColors.white,
        disabledCheckedTrackColor: Color = Ux4gColors.neutral200,
        disabledUncheckedTrackColor: Color = Ux4gColors.neutral200,
        disabledCheckedThumbColor: Color = Ux4gColors.white,
        disabledUncheckedThumbColor: Color = Ux4gColors.white
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

/**
 * Ux4gSwitch component (with Ux4gToggle alias) providing custom switch toggle controls
 * matching the Flutter specification for dimensions, colors, typography, and label layouts.
 */
@Composable
fun Ux4gSwitch(
    checked: Boolean,
    onCheckedChange: ((Boolean) -> Unit)?,
    modifier: Modifier = Modifier,
    label: String? = null,
    description: String? = null,
    size: Ux4gSwitchSize = Ux4gSwitchSize.M,
    labelPosition: Ux4gSwitchLabelPosition = Ux4gSwitchLabelPosition.Right,
    enabled: Boolean = true,
    isRequired: Boolean = false,
    icon: (@Composable () -> Unit)? = null,
    trailingIcon: Ux4gIcon? = null,
    labelStyle: TextStyle? = null,
    descriptionStyle: TextStyle? = null,
    secondaryLabel: String? = null,
    isSecondaryRequired: Boolean = false,
    secondaryIcon: (@Composable () -> Unit)? = null,
    trailingSecondaryIcon: Ux4gIcon? = null,
    colors: Ux4gToggleColors? = null,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() }
) {
    val isDark = isSystemInDarkTheme()

    val defaultCheckedTrack = if (isDark) Ux4gColors.primary300 else Ux4gColors.primary600
    val defaultUncheckedTrack = if (isDark) Ux4gColors.neutral700 else Ux4gColors.neutral300
    val defaultDisabledTrack = if (isDark) Ux4gColors.neutral800 else Ux4gColors.neutral200

    val checkedTrackColor = colors?.checkedTrackColor ?: defaultCheckedTrack
    val uncheckedTrackColor = colors?.uncheckedTrackColor ?: defaultUncheckedTrack
    val disabledCheckedTrackColor = colors?.disabledCheckedTrackColor ?: defaultDisabledTrack
    val disabledUncheckedTrackColor = colors?.disabledUncheckedTrackColor ?: defaultDisabledTrack

    val defaultCheckedThumb = if (isDark && checked && enabled) Ux4gColors.neutral900 else Ux4gColors.white
    val defaultUncheckedThumb = Ux4gColors.white
    val checkedThumbColor = colors?.checkedThumbColor ?: defaultCheckedThumb
    val uncheckedThumbColor = colors?.uncheckedThumbColor ?: defaultUncheckedThumb
    val disabledCheckedThumbColor = colors?.disabledCheckedThumbColor ?: Ux4gColors.white
    val disabledUncheckedThumbColor = colors?.disabledUncheckedThumbColor ?: Ux4gColors.white

    val targetTrackColor = when {
        !enabled && checked -> disabledCheckedTrackColor
        !enabled && !checked -> disabledUncheckedTrackColor
        checked -> checkedTrackColor
        else -> uncheckedTrackColor
    }

    val targetThumbColor = when {
        !enabled && checked -> disabledCheckedThumbColor
        !enabled && !checked -> disabledUncheckedThumbColor
        checked -> checkedThumbColor
        else -> uncheckedThumbColor
    }

    val animatedTrackColor by animateColorAsState(
        targetValue = targetTrackColor,
        animationSpec = tween(durationMillis = 150),
        label = "trackColor"
    )

    val animatedThumbColor by animateColorAsState(
        targetValue = targetThumbColor,
        animationSpec = tween(durationMillis = 150),
        label = "thumbColor"
    )

    val endOffset = size.width - size.thumbSize - size.thumbPadding
    val startOffset = size.thumbPadding

    val thumbOffset by animateDpAsState(
        targetValue = if (checked) endOffset else startOffset,
        animationSpec = tween(durationMillis = 150),
        label = "thumbOffset"
    )

    val toggleControl: @Composable () -> Unit = {
        Box(
            modifier = Modifier
                .minimumInteractiveComponentSize()
                .clickable(
                    interactionSource = interactionSource,
                    indication = null,
                    enabled = enabled && onCheckedChange != null,
                    role = Role.Switch
                ) {
                    onCheckedChange?.invoke(!checked)
                },
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .size(width = size.width, height = size.height)
                    .background(color = animatedTrackColor, shape = CircleShape),
                contentAlignment = Alignment.CenterStart
            ) {
                Box(
                    modifier = Modifier
                        .offset(x = thumbOffset)
                        .size(size.thumbSize)
                        .indication(
                            interactionSource = interactionSource,
                            indication = ripple(bounded = false, radius = 24.dp)
                        )
                        .shadow(
                            elevation = if (enabled) 2.dp else 0.dp,
                            shape = CircleShape
                        )
                        .background(color = animatedThumbColor, shape = CircleShape)
                )
            }
        }
    }

    // Typography resolution matching Flutter
    val labelStyleFallback = when (size) {
        Ux4gSwitchSize.S -> defaultTypography.lM_default
        Ux4gSwitchSize.M -> defaultTypography.lL_default
        Ux4gSwitchSize.L -> defaultTypography.lXL_default
    }

    val descStyleFallback = when (size) {
        Ux4gSwitchSize.S -> defaultTypography.lS_default
        Ux4gSwitchSize.M -> defaultTypography.lM_default
        Ux4gSwitchSize.L -> defaultTypography.lL_default
    }

    val resolvedLabelStyle = labelStyle ?: labelStyleFallback
    val resolvedDescStyle = descriptionStyle ?: descStyleFallback

    val onSurface = if (isDark) Ux4gColors.neutral0 else Ux4gColors.neutral900
    val labelColor = if (enabled) onSurface else onSurface.copy(alpha = 0.38f)
    val descColor = if (enabled) onSurface.copy(alpha = 0.7f) else onSurface.copy(alpha = 0.38f)
    val errorColor = Ux4gColors.red600
    val iconTint = if (enabled) onSurface else onSurface.copy(alpha = 0.38f)

    val effectiveIconComposable: (@Composable () -> Unit)? = icon ?: trailingIcon?.let {
        {
            Ux4gIconComponent(
                icon = it,
                contentDescription = null,
                tint = iconTint,
                modifier = Modifier.size(16.dp)
            )
        }
    }

    val effectiveSecondaryIconComposable: (@Composable () -> Unit)? = secondaryIcon ?: trailingSecondaryIcon?.let {
        {
            Ux4gIconComponent(
                icon = it,
                contentDescription = null,
                tint = iconTint,
                modifier = Modifier.size(16.dp)
            )
        }
    }

    val buildLabelWidget: @Composable (String, Boolean, (@Composable () -> Unit)?) -> Unit = { text, required, trIcon ->
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(
                text = buildAnnotatedString {
                    withStyle(SpanStyle(color = labelColor)) {
                        append(text)
                    }
                    if (required) {
                        withStyle(SpanStyle(color = errorColor)) {
                            append(" *")
                        }
                    }
                },
                style = resolvedLabelStyle
            )
            if (trIcon != null) {
                trIcon()
            }
        }
    }

    val buildTextsWidget: @Composable (String?, Boolean, (@Composable () -> Unit)?, String?) -> Unit =
        { mainLabel, required, trIcon, desc ->
            if (mainLabel != null || desc != null) {
                Column(
                    modifier = Modifier.padding(vertical = 2.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    if (mainLabel != null) {
                        buildLabelWidget(mainLabel, required, trIcon)
                    }
                    if (desc != null) {
                        Text(
                            text = desc,
                            style = resolvedDescStyle.copy(color = descColor)
                        )
                    }
                }
            }
        }

    val toggleModifier = if (onCheckedChange != null) {
        Modifier
            .defaultMinSize(minHeight = 48.dp)
            .toggleable(
                value = checked,
                onValueChange = onCheckedChange,
                enabled = enabled,
                role = Role.Switch,
                interactionSource = interactionSource,
                indication = null
            )
    } else Modifier.defaultMinSize(minHeight = 48.dp)

    when (labelPosition) {
        Ux4gSwitchLabelPosition.NoLabel -> {
            Box(modifier = modifier) {
                toggleControl()
            }
        }
        Ux4gSwitchLabelPosition.BothSides -> {
            Row(
                modifier = modifier.then(toggleModifier),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                buildTextsWidget(label, isRequired, effectiveIconComposable, description)
                toggleControl()
                if (secondaryLabel != null) {
                    buildTextsWidget(secondaryLabel, isSecondaryRequired, effectiveSecondaryIconComposable, null)
                }
            }
        }
        Ux4gSwitchLabelPosition.Left -> {
            Row(
                modifier = modifier.then(toggleModifier),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                buildTextsWidget(label, isRequired, effectiveIconComposable, description)
                toggleControl()
            }
        }
        Ux4gSwitchLabelPosition.Right -> {
            Row(
                modifier = modifier.then(toggleModifier),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                toggleControl()
                buildTextsWidget(label, isRequired, effectiveIconComposable, description)
            }
        }
    }
}

/**
 * Backwards compatible Ux4gToggle composable mapping directly to Ux4gSwitch.
 */
@Composable
fun Ux4gToggle(
    checked: Boolean,
    onCheckedChange: ((Boolean) -> Unit)?,
    modifier: Modifier = Modifier,
    label: String? = null,
    description: String? = null,
    size: Ux4gSwitchSize = Ux4gSwitchSize.M,
    labelPosition: Ux4gSwitchLabelPosition = Ux4gSwitchLabelPosition.Right,
    enabled: Boolean = true,
    isRequired: Boolean = false,
    icon: (@Composable () -> Unit)? = null,
    trailingIcon: Ux4gIcon? = null,
    labelStyle: TextStyle? = null,
    descriptionStyle: TextStyle? = null,
    secondaryLabel: String? = null,
    isSecondaryRequired: Boolean = false,
    secondaryIcon: (@Composable () -> Unit)? = null,
    trailingSecondaryIcon: Ux4gIcon? = null,
    colors: Ux4gToggleColors? = null,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() }
) {
    Ux4gSwitch(
        checked = checked,
        onCheckedChange = onCheckedChange,
        modifier = modifier,
        label = label,
        description = description,
        size = size,
        labelPosition = labelPosition,
        enabled = enabled,
        isRequired = isRequired,
        icon = icon,
        trailingIcon = trailingIcon,
        labelStyle = labelStyle,
        descriptionStyle = descriptionStyle,
        secondaryLabel = secondaryLabel,
        isSecondaryRequired = isSecondaryRequired,
        secondaryIcon = secondaryIcon,
        trailingSecondaryIcon = trailingSecondaryIcon,
        colors = colors,
        interactionSource = interactionSource
    )
}

// -----------------------------------------------------------------------------------------
// Previews
// -----------------------------------------------------------------------------------------

@Preview(name = "1. Sizes and Positions", showBackground = true)
@Composable
fun Ux4gSwitchSizesPreview() {
    Ux4gTheme {
        Column(
            modifier = Modifier
                .padding(24.dp)
                .background(Ux4gTheme.colorScheme.background),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            var s1 by remember { mutableStateOf(true) }
            Ux4gSwitch(
                checked = s1,
                onCheckedChange = { s1 = it },
                label = "Size Small (S)",
                description = "Compact 32x18 switch",
                size = Ux4gSwitchSize.S,
                labelPosition = Ux4gSwitchLabelPosition.Right
            )

            var s2 by remember { mutableStateOf(true) }
            Ux4gSwitch(
                checked = s2,
                onCheckedChange = { s2 = it },
                label = "Size Medium (M)",
                description = "Standard 40x22 switch",
                size = Ux4gSwitchSize.M,
                labelPosition = Ux4gSwitchLabelPosition.Right
            )

            var s3 by remember { mutableStateOf(false) }
            Ux4gSwitch(
                checked = s3,
                onCheckedChange = { s3 = it },
                label = "Size Large (L)",
                description = "Prominent 48x28 switch",
                size = Ux4gSwitchSize.L,
                labelPosition = Ux4gSwitchLabelPosition.Right
            )
        }
    }
}

@Preview(name = "2. Label Positions & BothSides", showBackground = true)
@Composable
fun Ux4gSwitchPositionsPreview() {
    Ux4gTheme {
        Column(
            modifier = Modifier
                .padding(24.dp)
                .background(Ux4gTheme.colorScheme.background),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            var s1 by remember { mutableStateOf(true) }
            Ux4gSwitch(
                checked = s1,
                onCheckedChange = { s1 = it },
                label = "Label on Left",
                description = "Toggle control follows text",
                labelPosition = Ux4gSwitchLabelPosition.Left
            )

            var s2 by remember { mutableStateOf(true) }
            Ux4gSwitch(
                checked = s2,
                onCheckedChange = { s2 = it },
                label = "Monthly",
                isRequired = true,
                secondaryLabel = "Annual (Save 20%)",
                isSecondaryRequired = false,
                labelPosition = Ux4gSwitchLabelPosition.BothSides
            )

            var s3 by remember { mutableStateOf(true) }
            Ux4gSwitch(
                checked = s3,
                onCheckedChange = { s3 = it },
                label = "Required with Icon",
                isRequired = true,
                trailingIcon = Ux4gIcons.info_circle,
                description = "Shows required asterisk and info icon",
                labelPosition = Ux4gSwitchLabelPosition.Right
            )
        }
    }
}

@Preview(name = "3. Disabled & Dark", showBackground = true)
@Preview(name = "3. Dark Mode", uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun Ux4gSwitchDisabledPreview() {
    Ux4gTheme {
        Surface(color = Ux4gTheme.colorScheme.background) {
            Column(
                modifier = Modifier
                    .padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                Ux4gSwitch(
                    checked = false,
                    onCheckedChange = null,
                    enabled = false,
                    label = "Disabled Unchecked",
                    description = "Cannot be toggled",
                    labelPosition = Ux4gSwitchLabelPosition.Right
                )

                Ux4gSwitch(
                    checked = true,
                    onCheckedChange = null,
                    enabled = false,
                    label = "Disabled Checked",
                    description = "Active but disabled state",
                    labelPosition = Ux4gSwitchLabelPosition.Right
                )
            }
        }
    }
}
