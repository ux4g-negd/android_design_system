package com.ux4g.designsystemv1.components.textarea

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ux4g.designsystemv1.foundation.Ux4gIcon
import com.ux4g.designsystemv1.foundation.Ux4gIconComponent
import com.ux4g.designsystemv1.foundation.Ux4gIcons
import com.ux4g.designsystemv1.foundation.Ux4gColors
import com.ux4g.designsystemv1.foundation.Ux4gRadius
import com.ux4g.designsystemv1.theme.Ux4gTheme

@Composable
fun Ux4gTextArea(
        value: String,
        onValueChange: (String) -> Unit,
        modifier: Modifier = Modifier,
        size: Ux4gTextAreaSize = Ux4gTextAreaSize.Large, // Large seems default per images
        minHeight: Ux4gTextAreaMinHeight = Ux4gTextAreaMinHeight.Medium,
        status: Ux4gTextAreaStatus = Ux4gTextAreaStatus.Default,
        label: String? = null,
        required: Boolean = false,
        placeholder: String? = null,
        caption: String? = null,
        showCaptionIcon: Boolean = true,
        trailingIconLabel: Ux4gIcon? = null,
        characterCountText: String? = null, // e.g., "0/200"
        enabled: Boolean = true,
        readOnly: Boolean = false,
        keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
        keyboardActions: KeyboardActions = KeyboardActions.Default,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isFocused by interactionSource.collectIsFocusedAsState()
    val focusRequester = remember { FocusRequester() }

    // Colors
    val borderColor = getBorderColor(status = status, isFocused = isFocused, enabled = enabled)
    val backgroundColor = getBackgroundColor(enabled = enabled)
    val textColor = getTextColor(enabled = enabled)
    val placeholderColor = Ux4gTheme.colorScheme.onSurface.copy(alpha = 0.4f)
    val labelColor = getLabelColor(status = status, isFocused = isFocused, enabled = enabled)
    val captionColor = getCaptionColor(status = status, enabled = enabled)

    // Typography
    val textStyle = Ux4gTheme.typography.bM_default.copy(color = textColor)
    val labelStyle = Ux4gTheme.typography.bM_strong.copy(color = labelColor)
    val captionStyle = Ux4gTheme.typography.bXS_default.copy(color = captionColor)
    val charCountStyle = Ux4gTheme.typography.bXS_default.copy(color = Ux4gTheme.colorScheme.onSurface.copy(alpha = 0.5f))

    val contentPadding = if (size == Ux4gTextAreaSize.Large) 16.dp else 12.dp

    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(8.dp)) {
        // Label Section
        if (label != null) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = label, style = labelStyle)
                if (required) {
                    Text(text = " *", style = labelStyle.copy(color = Ux4gTheme.colorScheme.error))
                }
                if (trailingIconLabel != null) {
                    Spacer(modifier = Modifier.width(4.dp))
                    Ux4gIconComponent(
                            icon = trailingIconLabel,
                            contentDescription = "Trailing Label Icon",
                            tint = Ux4gTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                            modifier = Modifier.size(16.dp)
                    )
                }
            }
        }

        // Input Container
        Box(
                modifier =
                        Modifier.fillMaxWidth()
                                .heightIn(min = minHeight.height)
                                .clip(RoundedCornerShape(Ux4gRadius.Radius8))
                                .background(backgroundColor)
                                .border(
                                        width = if (isFocused) 2.dp else 1.dp,
                                        color = borderColor,
                                        shape = RoundedCornerShape(Ux4gRadius.Radius8)
                                )
                                .clickable(
                                        interactionSource = interactionSource,
                                        indication = null,
                                        enabled = enabled
                                ) { focusRequester.requestFocus() }
                                .padding(contentPadding)
        ) {
            // Placeholder and TextField
            Box(modifier = Modifier.fillMaxWidth()) {
                if (value.isEmpty() && placeholder != null) {
                    Text(
                            text = placeholder,
                            style =
                                    textStyle.copy(
                                            color = placeholderColor
                                    ) // Placeholder custom style
                    )
                }
                BasicTextField(
                        value = value,
                        onValueChange = onValueChange,
                        modifier = Modifier.fillMaxWidth().focusRequester(focusRequester),
                        textStyle = textStyle,
                        enabled = enabled,
                        readOnly = readOnly,
                        keyboardOptions = keyboardOptions,
                        keyboardActions = keyboardActions,
                        interactionSource = interactionSource,
                        cursorBrush = SolidColor(Ux4gTheme.colorScheme.primary),
                        decorationBox = { innerTextField -> innerTextField() }
                )
            }

            // Bottom-Right Corner: Character Count & Resize handle
            Row(
                    modifier = Modifier.align(Alignment.BottomEnd),
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically
            ) {
                if (characterCountText != null) {
                    Text(text = characterCountText, style = charCountStyle)
                }

                // Generic resize dots icon using Unicode or small SVG if available.
                // We will use standard dots representation for now.
                Text(
                        text = "◢",
                        style =
                                charCountStyle.copy(
                                        color = Ux4gTheme.colorScheme.onSurface.copy(alpha = 0.4f),
                                        fontSize = Ux4gTheme.typography.bS_default.fontSize
                                )
                )
            }
        }

        // Caption Section
        AnimatedVisibility(
                visible = !caption.isNullOrBlank() || status == Ux4gTextAreaStatus.Error
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                if (showCaptionIcon) {
                    val cStatusIcon = getStatusIcon(status)
                    if (cStatusIcon != null) {
                        Ux4gIconComponent(
                                icon = cStatusIcon,
                                contentDescription = "Caption Status Icon",
                                tint = captionColor,
                                modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                    } else if (status == Ux4gTextAreaStatus.Default) {
                        Ux4gIconComponent(
                                icon = Ux4gIcons.info_circle,
                                contentDescription = "Standard Caption Icon",
                                tint = captionColor,
                                modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                    }
                }
                Text(text = caption ?: "", style = captionStyle)
            }
        }
    }
}

// ---------- Helpers for Colors and Icons ----------

@Composable
private fun getBorderColor(
        status: Ux4gTextAreaStatus,
        isFocused: Boolean,
        enabled: Boolean
): Color {
    if (!enabled) return Ux4gTheme.colorScheme.onSurface.copy(alpha = 0.3f)
    return when (status) {
        Ux4gTextAreaStatus.Error -> Ux4gTheme.colorScheme.error
        Ux4gTextAreaStatus.Warning -> Ux4gTheme.colorScheme.secondary
        Ux4gTextAreaStatus.Success -> Ux4gColors.green500
        Ux4gTextAreaStatus.Default -> {
            if (isFocused) Ux4gTheme.colorScheme.primary else Ux4gTheme.colorScheme.onSurface.copy(alpha = 0.3f)
        }
    }
}

@Composable
private fun getBackgroundColor(enabled: Boolean): Color {
    return if (enabled) Ux4gTheme.colorScheme.surface else Ux4gTheme.colorScheme.onSurface.copy(alpha = 0.05f)
}

@Composable
private fun getTextColor(enabled: Boolean): Color {
    return if (enabled) Ux4gTheme.colorScheme.onBackground else Ux4gTheme.colorScheme.onSurface.copy(alpha = 0.4f)
}

@Composable
private fun getLabelColor(status: Ux4gTextAreaStatus, isFocused: Boolean, enabled: Boolean): Color {
    if (!enabled) return Ux4gTheme.colorScheme.onSurface.copy(alpha = 0.4f)
    return when (status) {
        Ux4gTextAreaStatus.Error -> Ux4gTheme.colorScheme.error
        Ux4gTextAreaStatus.Warning -> Ux4gTheme.colorScheme.secondary
        Ux4gTextAreaStatus.Success -> Ux4gColors.green500
        Ux4gTextAreaStatus.Default -> {
            if (isFocused) Ux4gTheme.colorScheme.primary else Ux4gTheme.colorScheme.onBackground
        }
    }
}

@Composable
private fun getCaptionColor(status: Ux4gTextAreaStatus, enabled: Boolean): Color {
    if (!enabled) return Ux4gTheme.colorScheme.onSurface.copy(alpha = 0.4f)
    return when (status) {
        Ux4gTextAreaStatus.Error -> Ux4gTheme.colorScheme.error
        Ux4gTextAreaStatus.Warning -> Ux4gTheme.colorScheme.secondary
        Ux4gTextAreaStatus.Success -> Ux4gColors.green500
        Ux4gTextAreaStatus.Default -> Ux4gTheme.colorScheme.onSurface.copy(alpha = 0.6f)
    }
}

@Composable
private fun getStatusIcon(status: Ux4gTextAreaStatus): Ux4gIcon? {
    return when (status) {
        Ux4gTextAreaStatus.Error -> Ux4gIcons.error
        Ux4gTextAreaStatus.Warning -> Ux4gIcons.warning
        Ux4gTextAreaStatus.Success -> Ux4gIcons.success_circle
        Ux4gTextAreaStatus.Default -> null
    }
}

// ---------- PREVIEWS ----------

@Preview(showBackground = true, name = "1. Light Mode")
@Composable
fun Ux4gTextAreaLightPreview() {
    Ux4gTheme(darkTheme = false) {
        Column(
                modifier = Modifier.padding(24.dp).background(Ux4gTheme.colorScheme.background),
                verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Ux4gTextArea(
                    value = "",
                    onValueChange = {},
                    label = "Label",
                    required = true,
                    placeholder = "Placeholder",
                    caption = "Description",
                    characterCountText = "0/200",
                    trailingIconLabel = Ux4gIcons.info_circle,
                    size = Ux4gTextAreaSize.Large,
                    minHeight = Ux4gTextAreaMinHeight.Medium
            )

            Ux4gTextArea(
                    value = "Input text that is very long. ".repeat(4),
                    onValueChange = {},
                    label = "Label (Error)",
                    required = true,
                    placeholder = "Placeholder",
                    caption = "Error message",
                    status = Ux4gTextAreaStatus.Error,
                    characterCountText = "120/200",
                    trailingIconLabel = Ux4gIcons.info_circle
            )

            Ux4gTextArea(
                    value = "Input text",
                    onValueChange = {},
                    label = "Disabled",
                    placeholder = "Placeholder",
                    caption = "Disabled description",
                    enabled = false
            )
        }
    }
}

@Preview(showBackground = true, name = "2. Dark Mode")
@Composable
fun Ux4gTextAreaDarkPreview() {
    Ux4gTheme(darkTheme = true) {
        Column(
                modifier = Modifier.padding(24.dp).background(Ux4gTheme.colorScheme.background),
                verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Ux4gTextArea(
                    value = "",
                    onValueChange = {},
                    label = "Label",
                    required = true,
                    placeholder = "Placeholder",
                    caption = "Description",
                    characterCountText = "0/200",
                    trailingIconLabel = Ux4gIcons.info_circle,
                    size = Ux4gTextAreaSize.Large,
                    minHeight = Ux4gTextAreaMinHeight.Medium
            )

            Ux4gTextArea(
                    value = "Input text that is very long. ".repeat(4),
                    onValueChange = {},
                    label = "Label (Error)",
                    required = true,
                    placeholder = "Placeholder",
                    caption = "Error message",
                    status = Ux4gTextAreaStatus.Error,
                    characterCountText = "120/200",
                    trailingIconLabel = Ux4gIcons.info_circle
            )

            Ux4gTextArea(
                    value = "Input text",
                    onValueChange = {},
                    label = "Disabled",
                    placeholder = "Placeholder",
                    caption = "Disabled description",
                    enabled = false
            )
        }
    }
}
