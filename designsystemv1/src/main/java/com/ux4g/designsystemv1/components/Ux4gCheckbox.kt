package com.ux4g.designsystemv1.components

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.state.ToggleableState
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.ux4g.designsystemv1.foundation.Ux4gColors
import com.ux4g.designsystemv1.foundation.Ux4gIcon
import com.ux4g.designsystemv1.foundation.Ux4gIconComponent
import com.ux4g.designsystemv1.foundation.Ux4gIcons
import com.ux4g.designsystemv1.foundation.Ux4gSpace
import com.ux4g.designsystemv1.foundation.defaultTypography

/**
 * Checkbox sizing options matching Flutter Ux4gCheckboxSize.
 */
enum class Ux4gCheckboxSize(val size: Dp) {
    Small(16.dp),
    Medium(20.dp),
    Large(24.dp);

    // Backwards-compatible alias
    val sizeDp: Dp get() = size
}

/**
 * Checkbox description variants for validation and helper feedback,
 * matching Flutter Ux4gCheckboxDescriptionVariant.
 */
enum class Ux4gCheckboxDescriptionVariant {
    Helper,
    Error,
    Warning,
    Success
}

/**
 * Tri-state Checkbox component with label, description, and state validation variants,
 * matching Flutter's Ux4gCheckbox feature parity, typography, and default colors.
 *
 * @param value Tri-state boolean value: `true` = On/Checked, `false` = Off/Unchecked, `null` = Indeterminate.
 * @param onValueChange Callback invoked when the user toggles the checkbox.
 * @param modifier Modifier applied to the outer row container.
 * @param label Optional text label displayed next to the checkbox.
 * @param description Optional helper/error/warning/success description text below the label.
 * @param size Preset size for the checkbox box and label (Small, Medium, Large).
 * @param isRequired If true, appends a red asterisk `*` to the label.
 * @param hasError If true, displays the checkbox border in the error color state.
 * @param descriptionVariant Visual tone of the description (Helper, Error, Warning, Success).
 * @param enabled Whether the checkbox is interactive.
 */
@Composable
fun Ux4gCheckbox(
    value: Boolean?,
    onValueChange: (Boolean?) -> Unit,
    modifier: Modifier = Modifier,
    label: String? = null,
    description: String? = null,
    size: Ux4gCheckboxSize = Ux4gCheckboxSize.Medium,
    isRequired: Boolean = false,
    hasError: Boolean = false,
    descriptionVariant: Ux4gCheckboxDescriptionVariant = Ux4gCheckboxDescriptionVariant.Helper,
    enabled: Boolean = true,
    trailingIcon: Ux4gIcon? = null,
    checkedIcon: Ux4gIcon? = null,
    indeterminateIcon: Ux4gIcon? = null,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() }
) {
    val descriptionColor = when (descriptionVariant) {
        Ux4gCheckboxDescriptionVariant.Helper -> Ux4gColors.neutral700
        Ux4gCheckboxDescriptionVariant.Error -> Ux4gColors.red600
        Ux4gCheckboxDescriptionVariant.Warning -> Ux4gColors.orange600
        Ux4gCheckboxDescriptionVariant.Success -> Ux4gColors.green600
    }

    val labelTextStyle = when (size) {
        Ux4gCheckboxSize.Small -> defaultTypography.lM_default
        Ux4gCheckboxSize.Medium -> defaultTypography.lL_default
        Ux4gCheckboxSize.Large -> defaultTypography.lXL_default
    }

    val hasDescription = !description.isNullOrBlank()
    val hasLabel = !label.isNullOrBlank()

    val isChecked = value == true
    val isIndeterminate = value == null

    val boxColor = when {
        !enabled -> if (isChecked || isIndeterminate) Ux4gColors.primary.copy(alpha = 0.38f) else Color.Transparent
        isChecked || isIndeterminate -> Ux4gColors.primary
        else -> Color.Transparent
    }

    val borderColor = when {
        !enabled -> if (isChecked || isIndeterminate) Color.Transparent else Ux4gColors.neutral900.copy(alpha = 0.12f)
        hasError || descriptionVariant == Ux4gCheckboxDescriptionVariant.Error -> Ux4gColors.red600
        isChecked || isIndeterminate -> Ux4gColors.primary
        else -> Ux4gColors.neutral300
    }

    val checkmarkColor = Ux4gColors.white

    Row(
        modifier = modifier
            .clickable(
                enabled = enabled,
                interactionSource = interactionSource,
                indication = ripple(bounded = false, radius = 24.dp)
            ) {
                onValueChange(if (value == null) true else !value)
            }
            .padding(vertical = Ux4gSpace.Space4),
        verticalAlignment = if (hasDescription) Alignment.Top else Alignment.CenterVertically
    ) {
        // Checkbox Square Box
        Box(
            modifier = Modifier
                .padding(top = if (hasDescription) 2.dp else 0.dp)
                .size(size.size)
                .clip(RoundedCornerShape(4.dp))
                .background(boxColor)
                .border(1.5.dp, borderColor, RoundedCornerShape(4.dp)),
            contentAlignment = Alignment.Center
        ) {
            if (isChecked) {
                if (checkedIcon != null) {
                    Ux4gIconComponent(
                        icon = checkedIcon,
                        contentDescription = null,
                        tint = checkmarkColor,
                        modifier = Modifier.size(size.size * 0.8f)
                    )
                } else {
                    Icon(
                        imageVector = Icons.Filled.Check,
                        contentDescription = null,
                        tint = checkmarkColor,
                        modifier = Modifier.size(size.size * 0.8f)
                    )
                }
            } else if (isIndeterminate) {
                if (indeterminateIcon != null) {
                    Ux4gIconComponent(
                        icon = indeterminateIcon,
                        contentDescription = null,
                        tint = checkmarkColor,
                        modifier = Modifier.size(size.size * 0.8f)
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .size(width = size.size * 0.55f, height = 2.dp)
                            .clip(RoundedCornerShape(1.dp))
                            .background(checkmarkColor)
                    )
                }
            }
        }

        // Label and Description Column
        if (hasLabel || hasDescription) {
            Spacer(modifier = Modifier.width(8.dp))
            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp),
                horizontalAlignment = Alignment.Start
            ) {
                if (hasLabel) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = buildAnnotatedString {
                                withStyle(
                                    SpanStyle(
                                        color = if (enabled) Ux4gColors.neutral900 else Ux4gColors.neutral900.copy(alpha = 0.38f)
                                    )
                                ) {
                                    append(label!!)
                                }
                                if (isRequired) {
                                    withStyle(SpanStyle(color = Ux4gColors.red600)) {
                                        append(" *")
                                    }
                                }
                            },
                            style = labelTextStyle
                        )

                        if (trailingIcon != null) {
                            Spacer(modifier = Modifier.width(4.dp))
                            Ux4gIconComponent(
                                icon = trailingIcon,
                                contentDescription = null,
                                modifier = Modifier.size(size.size * 0.9f),
                                tint = if (enabled) Ux4gColors.neutral900 else Ux4gColors.neutral900.copy(alpha = 0.38f)
                            )
                        }
                    }
                }

                if (hasDescription) {
                    Text(
                        text = description!!,
                        style = defaultTypography.lS_default.copy(
                            color = descriptionColor
                        )
                    )
                }
            }
        }
    }
}

/**
 * Overload of [Ux4gCheckbox] supporting [ToggleableState] for backwards compatibility.
 */
@Composable
fun Ux4gCheckbox(
    state: ToggleableState,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    label: String? = null,
    description: String? = null,
    size: Ux4gCheckboxSize = Ux4gCheckboxSize.Medium,
    isRequired: Boolean = false,
    hasError: Boolean = false,
    trailingIcon: Ux4gIcon? = null,
    checkedIcon: Ux4gIcon? = null,
    indeterminateIcon: Ux4gIcon? = null,
    descriptionVariant: Ux4gCheckboxDescriptionVariant = Ux4gCheckboxDescriptionVariant.Helper,
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() }
) {
    val boolValue = when (state) {
        ToggleableState.On -> true
        ToggleableState.Off -> false
        ToggleableState.Indeterminate -> null
    }

    Ux4gCheckbox(
        value = boolValue,
        onValueChange = { onClick() },
        modifier = modifier,
        label = label,
        description = description,
        size = size,
        isRequired = isRequired,
        hasError = hasError,
        trailingIcon = trailingIcon,
        checkedIcon = checkedIcon,
        indeterminateIcon = indeterminateIcon,
        descriptionVariant = descriptionVariant,
        enabled = enabled,
        interactionSource = interactionSource
    )
}

// ─────────────────────────────────────────────────────────────────────────────
// Previews
// ─────────────────────────────────────────────────────────────────────────────

@Preview(name = "Light", showBackground = true)
@Preview(name = "Dark", uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
private fun Ux4gCheckboxPreview() {
    Surface(color = Ux4gColors.white) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            var value1 by remember { mutableStateOf<Boolean?>(true) }
            Ux4gCheckbox(
                value = value1,
                onValueChange = { value1 = it },
                label = "Medium Checked",
                size = Ux4gCheckboxSize.Medium,
                description = "This is a helper description",
                descriptionVariant = Ux4gCheckboxDescriptionVariant.Helper
            )

            var value2 by remember { mutableStateOf<Boolean?>(false) }
            Ux4gCheckbox(
                value = value2,
                onValueChange = { value2 = it },
                label = "Large Unchecked Required",
                size = Ux4gCheckboxSize.Large,
                isRequired = true,
                description = "This is an error",
                descriptionVariant = Ux4gCheckboxDescriptionVariant.Error
            )

            var value3 by remember { mutableStateOf<Boolean?>(null) }
            Ux4gCheckbox(
                value = value3,
                onValueChange = { value3 = it },
                label = "Small Indeterminate Warning",
                size = Ux4gCheckboxSize.Small,
                description = "This is a warning",
                descriptionVariant = Ux4gCheckboxDescriptionVariant.Warning,
                trailingIcon = Ux4gIcons.Settings
            )

            var value4 by remember { mutableStateOf<Boolean?>(true) }
            Ux4gCheckbox(
                value = value4,
                onValueChange = { value4 = it },
                label = "Medium Success",
                size = Ux4gCheckboxSize.Medium,
                description = "Successfully validated",
                descriptionVariant = Ux4gCheckboxDescriptionVariant.Success
            )

            Text(
                text = "Disabled States",
                style = defaultTypography.hS_default,
                color = Ux4gColors.neutral900
            )

            Ux4gCheckbox(
                value = false,
                onValueChange = {},
                label = "Disabled Unchecked",
                size = Ux4gCheckboxSize.Medium,
                isRequired = true,
                enabled = false
            )

            Ux4gCheckbox(
                value = true,
                onValueChange = {},
                label = "Disabled Checked",
                size = Ux4gCheckboxSize.Medium,
                isRequired = true,
                enabled = false
            )

            Ux4gCheckbox(
                value = null,
                onValueChange = {},
                label = "Disabled Indeterminate",
                size = Ux4gCheckboxSize.Medium,
                isRequired = true,
                enabled = false
            )
        }
    }
}
