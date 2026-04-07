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
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.ux4g.designsystemv1.foundation.Ux4gIcon
import com.ux4g.designsystemv1.foundation.Ux4gIconComponent
import com.ux4g.designsystemv1.foundation.Ux4gIcons
import com.ux4g.designsystemv1.foundation.Ux4gPalette
import com.ux4g.designsystemv1.foundation.Ux4gSpace
import com.ux4g.designsystemv1.theme.Ux4gTheme

enum class Ux4gRadioButtonSize(val sizeDp: Dp, val dotSizeDp: Dp) {
    Small(16.dp, 8.dp),
    Medium(20.dp, 10.dp),
    Large(24.dp, 12.dp)
}

enum class Ux4gRadioButtonDescriptionVariant {
    Helper,
    Error,
    Warning,
    Success
}

@Composable
fun Ux4gRadioButton(
        selected: Boolean,
        onClick: () -> Unit,
        modifier: Modifier = Modifier,
        label: String? = null,
        description: String? = null,
        size: Ux4gRadioButtonSize = Ux4gRadioButtonSize.Medium,
        isRequired: Boolean = false,
        trailingIcon: Ux4gIcon? = null,
        descriptionVariant: Ux4gRadioButtonDescriptionVariant =
                Ux4gRadioButtonDescriptionVariant.Helper,
        enabled: Boolean = true,
        interactionSource: MutableInteractionSource = remember { MutableInteractionSource() }
) {
    val descriptionColor =
            when (descriptionVariant) {
                Ux4gRadioButtonDescriptionVariant.Helper -> Ux4gTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                Ux4gRadioButtonDescriptionVariant.Error -> Ux4gTheme.colorScheme.error
                Ux4gRadioButtonDescriptionVariant.Warning -> Ux4gPalette.orange600
                Ux4gRadioButtonDescriptionVariant.Success -> Ux4gPalette.green600
            }

    val descriptionIcon =
            when (descriptionVariant) {
                Ux4gRadioButtonDescriptionVariant.Helper -> Ux4gIcons.info_circle
                Ux4gRadioButtonDescriptionVariant.Error -> Ux4gIcons.error
                Ux4gRadioButtonDescriptionVariant.Warning -> Ux4gIcons.warning
                Ux4gRadioButtonDescriptionVariant.Success -> Ux4gIcons.success_circle
            }

    val labelTextStyle =
            when (size) {
                Ux4gRadioButtonSize.Small -> Ux4gTheme.typography.lM_default
                Ux4gRadioButtonSize.Medium -> Ux4gTheme.typography.lL_default
                Ux4gRadioButtonSize.Large -> Ux4gTheme.typography.lXL_default
            }

    val descriptionTextStyle =
            when (size) {
                Ux4gRadioButtonSize.Small -> Ux4gTheme.typography.lS_default
                Ux4gRadioButtonSize.Medium -> Ux4gTheme.typography.lM_default
                Ux4gRadioButtonSize.Large -> Ux4gTheme.typography.lL_default
            }

    // val descriptionIconSize =
    //         when (size) {
    //                 Ux4gRadioButtonSize.Small -> 14.dp
    //                 Ux4gRadioButtonSize.Medium -> 16.dp
    //                 Ux4gRadioButtonSize.Large -> 18.dp
    //         }

    Row(
            modifier =
                    modifier.selectable(
                                    selected = selected,
                                    onClick = onClick,
                                    enabled = enabled,
                                    role = Role.RadioButton,
                                    interactionSource = interactionSource,
                                    indication = null
                            )
                            .padding(vertical = Ux4gSpace.Space4),
            verticalAlignment = Alignment.Top
    ) {
        val radioColor =
                if (!enabled) {
                    Ux4gTheme.colorScheme.onSurface.copy(alpha = 0.38f)
                } else if (selected) {
                    Ux4gTheme.colorScheme.primary
                } else {
                    Ux4gTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                }

        Box(
                modifier =
                        Modifier.padding(top = 2.dp)
                                .size(size.sizeDp)
                                .indication(
                                        interactionSource = interactionSource,
                                        indication = ripple(bounded = false, radius = 24.dp)
                                )
                                .border(width = 1.5.dp, color = radioColor, shape = CircleShape),
                contentAlignment = Alignment.Center
        ) {
            if (selected) {
                Box(modifier = Modifier.size(size.dotSizeDp).background(radioColor, CircleShape))
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
                                        if (enabled) Ux4gTheme.colorScheme.onSurface
                                        else Ux4gTheme.colorScheme.onSurface.copy(alpha = 0.38f)
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
                                    modifier = Modifier.size(size.sizeDp),
                                    tint =
                                            if (enabled) Ux4gTheme.colorScheme.onSurface
                                            else Ux4gTheme.colorScheme.onSurface.copy(alpha = 0.38f)
                            )
                        }
                    }
                }

                if (description != null) {
                    Row(
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                            verticalAlignment = Alignment.CenterVertically
                    ) {
                        Ux4gIconComponent(
                                icon = descriptionIcon,
                                contentDescription = null,
                                // modifier = Modifier.size(descriptionIconSize),
                                tint = descriptionColor
                        )
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
private fun Ux4gRadioButtonPreview() {
    Ux4gTheme {
        Surface(color = Ux4gTheme.colorScheme.background) {
            Column(
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                var selectedOption by remember { mutableStateOf("Option 1") }

                Ux4gRadioButton(
                        selected = selectedOption == "Option 1",
                        onClick = { selectedOption = "Option 1" },
                        label = "Medium Selected",
                        size = Ux4gRadioButtonSize.Medium,
                        description = "This is a helper description",
                        descriptionVariant = Ux4gRadioButtonDescriptionVariant.Helper
                )

                Ux4gRadioButton(
                        selected = selectedOption == "Option 2",
                        onClick = { selectedOption = "Option 2" },
                        label = "Large Unselected Required",
                        size = Ux4gRadioButtonSize.Large,
                        isRequired = true,
                        description = "This is an error",
                        descriptionVariant = Ux4gRadioButtonDescriptionVariant.Error
                )

                Ux4gRadioButton(
                        selected = selectedOption == "Option 3",
                        onClick = { selectedOption = "Option 3" },
                        label = "Small Warning",
                        size = Ux4gRadioButtonSize.Small,
                        description = "This is a warning",
                        descriptionVariant = Ux4gRadioButtonDescriptionVariant.Warning,
                        trailingIcon = com.ux4g.designsystemv1.foundation.Ux4gIcons.Settings
                )

                Ux4gRadioButton(
                        selected = selectedOption == "Option 4",
                        onClick = { selectedOption = "Option 4" },
                        label = "Medium Success",
                        size = Ux4gRadioButtonSize.Medium,
                        description = "Successfully validated",
                        descriptionVariant = Ux4gRadioButtonDescriptionVariant.Success
                )

                Text("Disabled States", style = Ux4gTheme.typography.hS_default)
                Ux4gRadioButton(
                        selected = false,
                        onClick = {},
                        label = "Label Unselected",
                        size = Ux4gRadioButtonSize.Medium,
                        isRequired = true,
                        enabled = false,
                        trailingIcon = com.ux4g.designsystemv1.foundation.Ux4gIcons.Settings
                )
                Ux4gRadioButton(
                        selected = true,
                        onClick = {},
                        label = "Label Selected",
                        size = Ux4gRadioButtonSize.Medium,
                        isRequired = true,
                        enabled = false,
                        trailingIcon = com.ux4g.designsystemv1.foundation.Ux4gIcons.Settings
                )
            }
        }
    }
}
