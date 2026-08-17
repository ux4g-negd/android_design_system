package com.ux4g.designsystemv1.components

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.indication
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
import androidx.compose.foundation.selection.triStateToggleable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.CheckboxColors
import androidx.compose.material3.CheckboxDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.state.ToggleableState
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.ux4g.designsystemv1.foundation.Ux4gIcon
import com.ux4g.designsystemv1.foundation.Ux4gIconComponent
import com.ux4g.designsystemv1.foundation.Ux4gIcons
import com.ux4g.designsystemv1.foundation.Ux4gColors
import com.ux4g.designsystemv1.foundation.Ux4gSpace
import com.ux4g.designsystemv1.theme.Ux4gTheme

enum class Ux4gCheckboxSize(val sizeDp: Dp, val scale: Float) {
        Small(16.dp, 16f / 20f),
        Medium(20.dp, 1f),
        Large(24.dp, 24f / 20f)
}

enum class Ux4gCheckboxDescriptionVariant {
        Helper,
        Error,
        Warning,
        Success
}

@Composable
fun Ux4gCheckbox(
        state: ToggleableState,
        onClick: () -> Unit,
        modifier: Modifier = Modifier,
        label: String? = null,
        description: String? = null,
        size: Ux4gCheckboxSize = Ux4gCheckboxSize.Medium,
        isRequired: Boolean = false,
        trailingIcon: Ux4gIcon? = null,
        checkedIcon: Ux4gIcon? = null,
        indeterminateIcon: Ux4gIcon? = null,
        descriptionVariant: Ux4gCheckboxDescriptionVariant = Ux4gCheckboxDescriptionVariant.Helper,
        enabled: Boolean = true,
        colors: CheckboxColors =
                CheckboxDefaults.colors(
                        checkedColor = Ux4gTheme.colorScheme.primary,
                        uncheckedColor = Ux4gTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                        checkmarkColor = Ux4gTheme.colorScheme.onPrimary
                ),
        interactionSource: MutableInteractionSource = remember { MutableInteractionSource() }
) {
        val descriptionColor =
                when (descriptionVariant) {
                        Ux4gCheckboxDescriptionVariant.Helper -> Ux4gTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                        Ux4gCheckboxDescriptionVariant.Error -> Ux4gTheme.colorScheme.error
                        Ux4gCheckboxDescriptionVariant.Warning -> Ux4gColors.orange600
                        Ux4gCheckboxDescriptionVariant.Success -> Ux4gColors.green600
                }

        val descriptionIcon =
                when (descriptionVariant) {
                        Ux4gCheckboxDescriptionVariant.Helper -> Ux4gIcons.info_circle
                        Ux4gCheckboxDescriptionVariant.Error -> Ux4gIcons.error
                        Ux4gCheckboxDescriptionVariant.Warning -> Ux4gIcons.warning
                        Ux4gCheckboxDescriptionVariant.Success -> Ux4gIcons.success_circle
                }

        val labelTextStyle =
                when (size) {
                        Ux4gCheckboxSize.Small -> Ux4gTheme.typography.lM_default
                        Ux4gCheckboxSize.Medium -> Ux4gTheme.typography.lL_default
                        Ux4gCheckboxSize.Large -> Ux4gTheme.typography.lXL_default
                }

        val descriptionTextStyle =
                when (size) {
                        Ux4gCheckboxSize.Small -> Ux4gTheme.typography.lS_default
                        Ux4gCheckboxSize.Medium -> Ux4gTheme.typography.lM_default
                        Ux4gCheckboxSize.Large -> Ux4gTheme.typography.lL_default
                }

        val descriptionIconSize =
                when (size) {
                        Ux4gCheckboxSize.Small -> 14.dp
                        Ux4gCheckboxSize.Medium -> 16.dp
                        Ux4gCheckboxSize.Large -> 18.dp
                }

        Row(
                modifier =
                        modifier.triStateToggleable(
                                        state = state,
                                        onClick = onClick,
                                        enabled = enabled,
                                        role = Role.Checkbox,
                                        interactionSource = interactionSource,
                                        indication = null
                                )
                                .padding(vertical = Ux4gSpace.Space4),
                verticalAlignment = Alignment.Top
        ) {
                // Adjust standard checkbox size through scaling
                val boxColor =
                        if (!enabled) {
                                Ux4gTheme.colorScheme.onSurface.copy(alpha = 0.12f)
                        } else if (state == ToggleableState.On ||
                                        state == ToggleableState.Indeterminate
                        ) {
                                Ux4gTheme.colorScheme.primary
                        } else {
                                Color.Transparent
                        }

                val borderColor =
                        if (!enabled) {
                                Color.Transparent
                        } else if (state == ToggleableState.On ||
                                        state == ToggleableState.Indeterminate
                        ) {
                                Color.Transparent
                        } else {
                                Ux4gTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        }

                val checkmarkColor =
                        if (!enabled) {
                                Ux4gTheme.colorScheme.onSurface.copy(alpha = 0.38f)
                        } else {
                                Ux4gTheme.colorScheme.onPrimary
                        }

                Box(
                        modifier =
                                Modifier.padding(top = 2.dp)
                                        .size(size.sizeDp)
                                        .indication(
                                                interactionSource = interactionSource,
                                                indication = ripple(bounded = false, radius = 24.dp)
                                        )
                                        .background(
                                                color = boxColor,
                                                shape = RoundedCornerShape(4.dp)
                                        )
                                        .border(
                                                width =
                                                        if (borderColor == Color.Transparent) 0.dp
                                                        else 1.5.dp,
                                                color = borderColor,
                                                shape = RoundedCornerShape(4.dp)
                                        ),
                        contentAlignment = Alignment.Center
                ) {
                        if (state == ToggleableState.On) {
                                if (checkedIcon != null) {
                                        Ux4gIconComponent(
                                                icon = checkedIcon,
                                                contentDescription = null,
                                                tint = checkmarkColor,
                                                modifier = Modifier.size(size.sizeDp * 0.8f)
                                        )
                                } else {
                                        Icon(
                                                imageVector = Icons.Filled.Check,
                                                contentDescription = null,
                                                tint = checkmarkColor,
                                                modifier = Modifier.size(size.sizeDp * 0.8f)
                                        )
                                }
                        } else if (state == ToggleableState.Indeterminate) {
                                if (indeterminateIcon != null) {
                                        Ux4gIconComponent(
                                                icon = indeterminateIcon,
                                                contentDescription = null,
                                                tint = checkmarkColor,
                                                modifier = Modifier.size(size.sizeDp * 0.8f)
                                        )
                                } else {
                                        Box(
                                                modifier =
                                                        Modifier.size(size.sizeDp * 0.6f, 2.dp)
                                                                .background(
                                                                        checkmarkColor,
                                                                        RoundedCornerShape(1.dp)
                                                                )
                                        )
                                }
                        }
                }

                if (label != null || description != null) {
                        Spacer(modifier = Modifier.width(8.dp))
                        Column(
                                modifier = Modifier.padding(top = 2.dp),
                                verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                                if (label != null) {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                                Text(
                                                        text = label,
                                                        style = labelTextStyle,
                                                        color =
                                                                if (enabled)
                                                                        Ux4gTheme.colorScheme
                                                                                .onSurface
                                                                else
                                                                        Ux4gTheme.colorScheme
                                                                                .onSurface.copy(
                                                                                alpha = 0.38f
                                                                        )
                                                )
                                                if (isRequired) {
                                                        Spacer(modifier = Modifier.width(4.dp))
                                                        Text(
                                                                text = "*",
                                                                style = labelTextStyle,
                                                                color = Ux4gTheme.colorScheme.error
                                                        )
                                                }
                                                if (trailingIcon != null) {
                                                        Spacer(modifier = Modifier.width(4.dp))
                                                        Ux4gIconComponent(
                                                                icon = trailingIcon,
                                                                contentDescription = null,
                                                                modifier =
                                                                        Modifier.size(size.sizeDp),
                                                                tint =
                                                                        if (enabled)
                                                                                Ux4gTheme
                                                                                        .colorScheme
                                                                                        .onSurface
                                                                        else
                                                                                Ux4gTheme
                                                                                        .colorScheme
                                                                                        .onSurface
                                                                                        .copy(
                                                                                                alpha =
                                                                                                        0.38f
                                                                                        )
                                                        )
                                                }
                                        }
                                }

                                if (description != null) {
                                        Row(
                                                horizontalArrangement = Arrangement.spacedBy(4.dp),
                                                verticalAlignment = Alignment.CenterVertically
                                        ) {
                                                if (descriptionVariant !=
                                                                Ux4gCheckboxDescriptionVariant
                                                                        .Helper
                                                ) {
                                                        Ux4gIconComponent(
                                                                icon = descriptionIcon,
                                                                contentDescription = null,
                                                                //
                                                                //
                                                                // modifier =
                                                                //
                                                                //
                                                                //    Modifier.size(
                                                                //
                                                                //
                                                                //            descriptionIconSize
                                                                //
                                                                //
                                                                //    ),
                                                                tint = descriptionColor
                                                        )
                                                } else {
                                                        Ux4gIconComponent(
                                                                icon = descriptionIcon,
                                                                contentDescription = null,
                                                                //
                                                                //
                                                                // modifier =
                                                                //
                                                                //
                                                                //    Modifier.size(
                                                                //
                                                                //
                                                                //            descriptionIconSize
                                                                //
                                                                //
                                                                //    ),
                                                                tint = descriptionColor
                                                        )
                                                }
                                                Text(
                                                        text = description,
                                                        style = descriptionTextStyle,
                                                        color = descriptionColor
                                                )
                                        }
                                }
                        }
                }
        }
}

@Preview(name = "Light", showBackground = true)
@Preview(name = "Dark", uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
private fun Ux4gCheckboxPreview() {
        Ux4gTheme {
                Surface(color = Ux4gTheme.colorScheme.background) {
                        Column(
                                modifier = Modifier.fillMaxWidth().padding(16.dp),
                                verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                                var state1 by remember { mutableStateOf(ToggleableState.On) }
                                Ux4gCheckbox(
                                        state = state1,
                                        onClick = {
                                                state1 =
                                                        if (state1 == ToggleableState.On)
                                                                ToggleableState.Off
                                                        else ToggleableState.On
                                        },
                                        label = "Medium Checked",
                                        size = Ux4gCheckboxSize.Medium,
                                        description = "This is a helper description",
                                        descriptionVariant = Ux4gCheckboxDescriptionVariant.Helper
                                )

                                var state2 by remember { mutableStateOf(ToggleableState.Off) }
                                Ux4gCheckbox(
                                        state = state2,
                                        onClick = {
                                                state2 =
                                                        if (state2 == ToggleableState.On)
                                                                ToggleableState.Off
                                                        else ToggleableState.On
                                        },
                                        label = "Large Unchecked Required",
                                        size = Ux4gCheckboxSize.Large,
                                        isRequired = true,
                                        description = "This is an error",
                                        descriptionVariant = Ux4gCheckboxDescriptionVariant.Error
                                )

                                var state3 by remember {
                                        mutableStateOf(ToggleableState.Indeterminate)
                                }
                                Ux4gCheckbox(
                                        state = state3,
                                        onClick = {
                                                state3 =
                                                        if (state3 == ToggleableState.On)
                                                                ToggleableState.Off
                                                        else ToggleableState.On
                                        },
                                        label = "Small Indeterminate Warning",
                                        size = Ux4gCheckboxSize.Small,
                                        description = "This is a warning",
                                        descriptionVariant = Ux4gCheckboxDescriptionVariant.Warning,
                                        trailingIcon =
                                                com.ux4g.designsystemv1.foundation.Ux4gIcons
                                                        .Settings
                                )

                                var state4 by remember { mutableStateOf(ToggleableState.On) }
                                Ux4gCheckbox(
                                        state = state4,
                                        onClick = {
                                                state4 =
                                                        if (state4 == ToggleableState.On)
                                                                ToggleableState.Off
                                                        else ToggleableState.On
                                        },
                                        label = "Medium Success",
                                        size = Ux4gCheckboxSize.Medium,
                                        description = "Successfully validated",
                                        descriptionVariant = Ux4gCheckboxDescriptionVariant.Success
                                )

                                Text("Disabled States", style = Ux4gTheme.typography.hS_default)
                                Ux4gCheckbox(
                                        state = ToggleableState.Off,
                                        onClick = {},
                                        label = "Label",
                                        size = Ux4gCheckboxSize.Medium,
                                        isRequired = true,
                                        enabled = false,
                                        trailingIcon =
                                                com.ux4g.designsystemv1.foundation.Ux4gIcons
                                                        .Settings
                                )
                                Ux4gCheckbox(
                                        state = ToggleableState.On,
                                        onClick = {},
                                        label = "Label",
                                        size = Ux4gCheckboxSize.Medium,
                                        isRequired = true,
                                        enabled = false,
                                        trailingIcon =
                                                com.ux4g.designsystemv1.foundation.Ux4gIcons
                                                        .Settings
                                )
                                Ux4gCheckbox(
                                        state = ToggleableState.Indeterminate,
                                        onClick = {},
                                        label = "Label",
                                        size = Ux4gCheckboxSize.Medium,
                                        isRequired = true,
                                        enabled = false,
                                        trailingIcon =
                                                com.ux4g.designsystemv1.foundation.Ux4gIcons
                                                        .Settings
                                )
                        }
                }
        }
}
