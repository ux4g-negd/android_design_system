package com.ux4g.designsystemv1.components

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CheckCircleOutline
import androidx.compose.material.icons.outlined.ErrorOutline
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.WarningAmber
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.ux4g.designsystemv1.foundation.Ux4gColors
import com.ux4g.designsystemv1.foundation.Ux4gIcon
import com.ux4g.designsystemv1.foundation.Ux4gIconComponent
import com.ux4g.designsystemv1.foundation.Ux4gIcons
import com.ux4g.designsystemv1.foundation.Ux4gRadius
import com.ux4g.designsystemv1.theme.Ux4gTheme

// ─── Enums ────────────────────────────────────────────────────────────────────

/**
 * Size variants for [Ux4gRadioButton].
 * Matches Flutter's `Ux4gRadioButtonSize` (small: 16dp, medium: 20dp, large: 24dp).
 */
enum class Ux4gRadioButtonSize(val sizeDp: Dp, val dotSizeDp: Dp) {
    Small(16.dp, 8.dp),
    Medium(20.dp, 10.dp),
    Large(24.dp, 12.dp);

    companion object {
        val small get() = Small
        val medium get() = Medium
        val large get() = Large
    }
}

/**
 * Description variant representing validation/helper context.
 * Matches Flutter's `Ux4gRadioButtonDescriptionVariant`.
 */
enum class Ux4gRadioButtonDescriptionVariant {
    Helper,
    Error,
    Warning,
    Success;

    companion object {
        val helper get() = Helper
        val error get() = Error
        val warning get() = Warning
        val success get() = Success
    }
}

/**
 * Visual status for the radio circle itself (independent of selection).
 * Drives the ring color so callers can render error / warning / success
 * states consistently (e.g. validation errors on the parent form).
 * Matches Flutter's `Ux4gRadioButtonStatus`.
 */
enum class Ux4gRadioButtonStatus {
    DefaultStatus,
    Error,
    Warning,
    Success;

    companion object {
        val defaultStatus get() = DefaultStatus
        val error get() = Error
        val warning get() = Warning
        val success get() = Success
    }
}

// ─── Generic Component ────────────────────────────────────────────────────────

/**
 * A UX4G Radio Button component matching Flutter's `Ux4gRadioButton<T>` 1:1.
 *
 * @param value The value represented by this radio button.
 * @param groupValue The currently selected value for this radio button group.
 * @param onValueChange Callback invoked when this radio button is clicked.
 * @param modifier Modifier applied to the outer row.
 * @param label Primary label text.
 * @param description Subtitle / helper / validation message below the label.
 * @param size Predefined size ([Ux4gRadioButtonSize.Small], [Ux4gRadioButtonSize.Medium], [Ux4gRadioButtonSize.Large]).
 * @param isRequired When true, displays an asterisk (*) next to the label.
 * @param trailingIcon Optional trailing icon next to the label.
 * @param trailingImageVector Optional trailing ImageVector next to the label.
 * @param descriptionVariant Severity style for the description (helper, error, warning, success).
 * @param status Ring status styling (defaultStatus, error, warning, success).
 * @param color Optional override for the radio circle color. Takes precedence over [status].
 * @param labelColor Custom color for the label text.
 * @param labelStyle Custom TextStyle override for the label.
 * @param descriptionStyle Custom TextStyle override for the description.
 * @param enabled Whether this radio button is interactive.
 */
@Composable
fun <T> Ux4gRadioButton(
    value: T,
    groupValue: T?,
    onValueChange: (T) -> Unit,
    modifier: Modifier = Modifier,
    label: String? = null,
    description: String? = null,
    size: Ux4gRadioButtonSize = Ux4gRadioButtonSize.Medium,
    isRequired: Boolean = false,
    trailingIcon: Ux4gIcon? = null,
    trailingImageVector: ImageVector? = null,
    descriptionVariant: Ux4gRadioButtonDescriptionVariant = Ux4gRadioButtonDescriptionVariant.Helper,
    status: Ux4gRadioButtonStatus = Ux4gRadioButtonStatus.DefaultStatus,
    color: Color? = null,
    labelColor: Color? = null,
    labelStyle: TextStyle? = null,
    descriptionStyle: TextStyle? = null,
    enabled: Boolean = true,
) {
    Ux4gRadioButton(
        selected = value == groupValue,
        onClick = { onValueChange(value) },
        modifier = modifier,
        label = label,
        description = description,
        size = size,
        isRequired = isRequired,
        trailingIcon = trailingIcon,
        trailingImageVector = trailingImageVector,
        descriptionVariant = descriptionVariant,
        status = status,
        color = color,
        labelColor = labelColor,
        labelStyle = labelStyle,
        descriptionStyle = descriptionStyle,
        enabled = enabled,
    )
}

// ─── Boolean Overload Component ───────────────────────────────────────────────

/**
 * Boolean-based [Ux4gRadioButton] composable matching Flutter's UI & design tokens 1:1.
 */
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
    trailingImageVector: ImageVector? = null,
    descriptionVariant: Ux4gRadioButtonDescriptionVariant = Ux4gRadioButtonDescriptionVariant.Helper,
    status: Ux4gRadioButtonStatus = Ux4gRadioButtonStatus.DefaultStatus,
    color: Color? = null,
    labelColor: Color? = null,
    labelStyle: TextStyle? = null,
    descriptionStyle: TextStyle? = null,
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() }
) {
    val isDark = isSystemInDarkTheme()
    val onSurface = Ux4gTheme.colorScheme.onSurface
    val primaryColor = Ux4gTheme.colorScheme.primary

    // ── Description Color & Icon matching Flutter ─────────────────────────
    val descriptionColor = when (descriptionVariant) {
        Ux4gRadioButtonDescriptionVariant.Helper -> onSurface.copy(alpha = 0.7f)
        Ux4gRadioButtonDescriptionVariant.Error -> Ux4gTheme.colorScheme.error
        Ux4gRadioButtonDescriptionVariant.Warning -> Ux4gColors.secondary600
        Ux4gRadioButtonDescriptionVariant.Success -> Ux4gColors.green600
    }

    val descriptionIcon: ImageVector = when (descriptionVariant) {
        Ux4gRadioButtonDescriptionVariant.Helper -> Icons.Outlined.Info
        Ux4gRadioButtonDescriptionVariant.Error -> Icons.Outlined.ErrorOutline
        Ux4gRadioButtonDescriptionVariant.Warning -> Icons.Outlined.WarningAmber
        Ux4gRadioButtonDescriptionVariant.Success -> Icons.Outlined.CheckCircleOutline
    }

    // ── Typography resolution matching Flutter ────────────────────────────
    val resolvedLabelTextStyle = labelStyle ?: when (size) {
        Ux4gRadioButtonSize.Small -> Ux4gTheme.typography.lM_default
        Ux4gRadioButtonSize.Medium -> Ux4gTheme.typography.lL_default
        Ux4gRadioButtonSize.Large -> Ux4gTheme.typography.lXL_default
    }

    val resolvedDescriptionTextStyle = descriptionStyle ?: when (size) {
        Ux4gRadioButtonSize.Small -> Ux4gTheme.typography.lS_default
        Ux4gRadioButtonSize.Medium -> Ux4gTheme.typography.lM_default
        Ux4gRadioButtonSize.Large -> Ux4gTheme.typography.lL_default
    }

    // ── Status Color resolution matching Flutter ──────────────────────────
    val statusColor: Color? = when (status) {
        Ux4gRadioButtonStatus.Error -> Ux4gTheme.colorScheme.error
        Ux4gRadioButtonStatus.Warning -> Ux4gColors.secondary600
        Ux4gRadioButtonStatus.Success -> Ux4gColors.green600
        Ux4gRadioButtonStatus.DefaultStatus -> null
    }

    val unselectedBorderColor = if (isDark) Ux4gColors.neutral700 else Ux4gColors.neutral300

    val radioColor = if (!enabled) {
        onSurface.copy(alpha = 0.38f)
    } else if (selected) {
        color ?: statusColor ?: primaryColor
    } else {
        color ?: statusColor ?: unselectedBorderColor
    }

    // Selected state renders as a thick filled ring with a white inner disc (donut).
    // Unselected state is a thin 1.5px outlined circle.
    val selectedRingThickness = size.sizeDp * 0.28f
    val innerDiscSize = size.sizeDp - selectedRingThickness * 2
    val disabledFillColor = onSurface.copy(alpha = 0.1f)

    Row(
        modifier = modifier
            .selectable(
                selected = selected,
                onClick = onClick,
                enabled = enabled,
                role = Role.RadioButton,
                interactionSource = interactionSource,
                indication = null
            )
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.Top
    ) {
        // ── Radio Circle with centered unbounded circular ripple ──
        Box(
            modifier = Modifier
                .padding(top = 2.dp)
                .indication(
                    interactionSource = interactionSource,
                    indication = if (enabled) ripple(bounded = false, radius = 20.dp, color = radioColor) else null
                ),
            contentAlignment = Alignment.Center
        ) {
            if (selected) {
                Box(
                    modifier = Modifier
                        .size(size.sizeDp)
                        .background(
                            color = if (!enabled) disabledFillColor else radioColor,
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size(innerDiscSize)
                            .background(
                                color = if (!enabled) radioColor else (if (isDark) Color.Black else Color.White),
                                shape = CircleShape
                            )
                    )
                }
            } else {
                Box(
                    modifier = Modifier
                        .size(size.sizeDp)
                        .then(
                            if (!enabled) {
                                Modifier.background(disabledFillColor, CircleShape)
                            } else {
                                Modifier.border(width = 1.5.dp, color = radioColor, shape = CircleShape)
                            }
                        )
                )
            }
        }

        // ── Label, isRequired asterisk, trailing icon, & description ─────────
        if (label != null || description != null) {
            Spacer(modifier = Modifier.width(8.dp))
            Column(
                modifier = Modifier.weight(1f, fill = false),
                verticalArrangement = Arrangement.Top
            ) {
                if (label != null) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Start
                    ) {
                        Text(
                            text = label,
                            style = resolvedLabelTextStyle.copy(
                                color = if (!enabled) onSurface.copy(alpha = 0.38f) else (labelColor ?: onSurface)
                            )
                        )
                        if (isRequired) {
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "*",
                                style = resolvedLabelTextStyle.copy(
                                    color = Ux4gTheme.colorScheme.error
                                )
                            )
                        }
                        if (trailingIcon != null) {
                            Spacer(modifier = Modifier.width(4.dp))
                            Ux4gIconComponent(
                                icon = trailingIcon,
                                contentDescription = null,
                                modifier = Modifier.size(size.sizeDp),
                                tint = if (enabled) onSurface else onSurface.copy(alpha = 0.38f)
                            )
                        } else if (trailingImageVector != null) {
                            Spacer(modifier = Modifier.width(4.dp))
                            Icon(
                                imageVector = trailingImageVector,
                                contentDescription = null,
                                modifier = Modifier.size(size.sizeDp),
                                tint = if (enabled) onSurface else onSurface.copy(alpha = 0.38f)
                            )
                        }
                    }
                }

                if (description != null) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = descriptionIcon,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp),
                            tint = descriptionColor
                        )
                        Text(
                            text = description,
                            style = resolvedDescriptionTextStyle.copy(
                                color = descriptionColor
                            )
                        )
                    }
                }
            }
        }
    }
}

// ─── Previews ─────────────────────────────────────────────────────────────────

@Preview(name = "Light Mode", showBackground = true)
@Preview(name = "Dark Mode", uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
private fun Ux4gRadioButtonPreview() {
    Ux4gTheme {
        Surface(color = Ux4gTheme.colorScheme.background) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                var selectedOption by remember { mutableStateOf("Option 1") }

                Ux4gRadioButton(
                    value = "Option 1",
                    groupValue = selectedOption,
                    onValueChange = { selectedOption = it },
                    label = "Medium Selected",
                    size = Ux4gRadioButtonSize.Medium,
                    description = "This is a helper description",
                    descriptionVariant = Ux4gRadioButtonDescriptionVariant.Helper
                )

                Ux4gRadioButton(
                    value = "Option 2",
                    groupValue = selectedOption,
                    onValueChange = { selectedOption = it },
                    label = "Large Unselected Required",
                    size = Ux4gRadioButtonSize.Large,
                    isRequired = true,
                    description = "This is an error message",
                    descriptionVariant = Ux4gRadioButtonDescriptionVariant.Error
                )

                Ux4gRadioButton(
                    value = "Option 3",
                    groupValue = selectedOption,
                    onValueChange = { selectedOption = it },
                    label = "Small Warning with Status",
                    size = Ux4gRadioButtonSize.Small,
                    status = Ux4gRadioButtonStatus.Warning,
                    description = "This is a warning",
                    descriptionVariant = Ux4gRadioButtonDescriptionVariant.Warning,
                    trailingIcon = Ux4gIcons.Settings
                )

                Ux4gRadioButton(
                    value = "Option 4",
                    groupValue = selectedOption,
                    onValueChange = { selectedOption = it },
                    label = "Medium Success",
                    size = Ux4gRadioButtonSize.Medium,
                    status = Ux4gRadioButtonStatus.Success,
                    description = "Successfully validated",
                    descriptionVariant = Ux4gRadioButtonDescriptionVariant.Success
                )

                Text("Disabled States", style = Ux4gTheme.typography.hS_default)
                Ux4gRadioButton(
                    selected = false,
                    onClick = {},
                    label = "Label Unselected Disabled",
                    size = Ux4gRadioButtonSize.Medium,
                    isRequired = true,
                    enabled = false,
                    trailingIcon = Ux4gIcons.Settings
                )
                Ux4gRadioButton(
                    selected = true,
                    onClick = {},
                    label = "Label Selected Disabled",
                    size = Ux4gRadioButtonSize.Medium,
                    isRequired = true,
                    enabled = false,
                    trailingIcon = Ux4gIcons.Settings
                )
            }
        }
    }
}
