package com.ux4g.designsystemv1.components.textarea

import android.content.res.Configuration
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ux4g.designsystemv1.foundation.Ux4gColors
import com.ux4g.designsystemv1.foundation.Ux4gIcon
import com.ux4g.designsystemv1.foundation.Ux4gIconComponent
import com.ux4g.designsystemv1.foundation.Ux4gIcons
import com.ux4g.designsystemv1.foundation.Ux4gRadius
import com.ux4g.designsystemv1.foundation.defaultTypography
import com.ux4g.designsystemv1.theme.Ux4gTheme

/**
 * Multi-line Text Area Component matching Flutter's `text_area.dart`.
 *
 * @param value The current text value.
 * @param onValueChange Callback when text changes.
 * @param size Small (padding 12dp) or Large (padding 16dp).
 * @param minHeight Small (80dp), Medium (120dp), or Large (160dp).
 * @param status Default, Success, Warning, or Error.
 * @param label Optional heading label.
 * @param required Whether to show red required asterisk `*`.
 * @param placeholder Placeholder text when empty.
 * @param caption Optional caption/helper text below input.
 * @param showCaptionIcon Whether to show status icon before caption.
 * @param trailingIconLabel Optional icon after label.
 * @param characterCountText Custom counter text or automatic counter if maxLength provided.
 * @param enabled Whether input is interactive.
 * @param readOnly Whether input can be modified.
 * @param maxLength Maximum allowed characters.
 */
@Composable
fun Ux4gTextArea(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    size: Ux4gTextAreaSize = Ux4gTextAreaSize.Large,
    minHeight: Ux4gTextAreaMinHeight = Ux4gTextAreaMinHeight.Medium,
    status: Ux4gTextAreaStatus = Ux4gTextAreaStatus.Default,
    label: String? = null,
    required: Boolean = false,
    placeholder: String? = null,
    caption: String? = null,
    showCaptionIcon: Boolean = true,
    trailingIconLabel: Ux4gIcon? = null,
    characterCountText: String? = null,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    maxLength: Int? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isFocused by interactionSource.collectIsFocusedAsState()
    val focusRequester = remember { FocusRequester() }

    // Direct Colors (Flutter Parity)
    val onSurface = Ux4gColors.neutral900
    val onBackground = Ux4gColors.neutral900
    val primaryColor = Ux4gColors.primary
    val errorColor = Ux4gColors.red600
    val warningColor = Ux4gColors.orange500
    val successColor = Ux4gColors.green500

    val borderColor = when {
        !enabled -> onSurface.copy(alpha = 0.3f)
        status == Ux4gTextAreaStatus.Error -> errorColor
        status == Ux4gTextAreaStatus.Warning -> warningColor
        status == Ux4gTextAreaStatus.Success -> successColor
        isFocused -> primaryColor
        else -> onSurface.copy(alpha = 0.3f)
    }

    val backgroundColor = if (enabled) Ux4gColors.white else onSurface.copy(alpha = 0.05f)
    val textColor = if (enabled) onBackground else onSurface.copy(alpha = 0.4f)
    val placeholderColor = onSurface.copy(alpha = 0.4f)

    val labelColor = when {
        !enabled -> onSurface.copy(alpha = 0.4f)
        status == Ux4gTextAreaStatus.Error -> errorColor
        status == Ux4gTextAreaStatus.Warning -> warningColor
        status == Ux4gTextAreaStatus.Success -> successColor
        else -> onBackground
    }

    val captionColor = when {
        !enabled -> onSurface.copy(alpha = 0.4f)
        status == Ux4gTextAreaStatus.Error -> errorColor
        status == Ux4gTextAreaStatus.Warning -> warningColor
        status == Ux4gTextAreaStatus.Success -> successColor
        else -> onSurface.copy(alpha = 0.6f)
    }

    // Direct Typography (Flutter Parity)
    val textStyle = defaultTypography.bM_default.copy(color = textColor)
    val labelStyle = defaultTypography.lL_default.copy(color = labelColor)
    val requiredStyle = defaultTypography.bS_strong.copy(color = errorColor)
    val captionStyle = defaultTypography.bXS_default.copy(color = captionColor)
    val charCountStyle = defaultTypography.bXS_default.copy(color = onSurface.copy(alpha = 0.5f))

    val contentPadding = if (size == Ux4gTextAreaSize.Large) 16.dp else 12.dp

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Label Section
        if (label != null) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(text = label, style = labelStyle)
                if (required) {
                    Text(text = "*", style = requiredStyle)
                }
                if (trailingIconLabel != null) {
                    Ux4gIconComponent(
                        icon = trailingIconLabel,
                        contentDescription = "Trailing Label Icon",
                        tint = onSurface.copy(alpha = 0.5f),
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }

        // Input Container
        Box(
            modifier = Modifier
                .fillMaxWidth()
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
                    enabled = enabled && !readOnly
                ) {
                    focusRequester.requestFocus()
                }
                .padding(contentPadding)
        ) {
            // Placeholder and BasicTextField
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = if (characterCountText != null || maxLength != null) 16.dp else 0.dp)
            ) {
                if (value.isEmpty() && placeholder != null) {
                    Text(
                        text = placeholder,
                        style = defaultTypography.bM_default.copy(color = placeholderColor)
                    )
                }
                BasicTextField(
                    value = value,
                    onValueChange = { newValue ->
                        if (maxLength == null || newValue.length <= maxLength) {
                            onValueChange(newValue)
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .focusRequester(focusRequester),
                    textStyle = textStyle,
                    enabled = enabled,
                    readOnly = readOnly,
                    keyboardOptions = keyboardOptions,
                    keyboardActions = keyboardActions,
                    interactionSource = interactionSource,
                    cursorBrush = SolidColor(primaryColor),
                    decorationBox = { innerTextField -> innerTextField() }
                )
            }

            // Bottom-Right Corner: Character Counter & Resize Grip
            Row(
                modifier = Modifier.align(Alignment.BottomEnd),
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                val counterText = characterCountText ?: if (maxLength != null) "${value.length}/$maxLength" else null
                if (counterText != null) {
                    Text(text = counterText, style = charCountStyle)
                }

                // Custom Diagonal Resize Grip Lines (matching Flutter _TextAreaResizeGripPainter)
                Canvas(modifier = Modifier.size(6.dp)) {
                    val strokeW = 1.2.dp.toPx()
                    val gripColor = onSurface.copy(alpha = 0.4f)
                    val canvasW = this.size.width
                    val canvasH = this.size.height
                    // Bottom-right line
                    drawLine(
                        color = gripColor,
                        start = Offset(canvasW * 0.3f, canvasH),
                        end = Offset(canvasW, canvasH * 0.3f),
                        strokeWidth = strokeW,
                        cap = StrokeCap.Round
                    )
                    // Top-left line
                    drawLine(
                        color = gripColor,
                        start = Offset(0f, canvasH * 0.7f),
                        end = Offset(canvasW * 0.7f, 0f),
                        strokeWidth = strokeW,
                        cap = StrokeCap.Round
                    )
                }
            }
        }

        // Caption Section
        val hasCaption = !caption.isNullOrBlank() || status == Ux4gTextAreaStatus.Error
        AnimatedVisibility(visible = hasCaption) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                if (showCaptionIcon) {
                    val statusIcon = when (status) {
                        Ux4gTextAreaStatus.Error -> Ux4gIcons.error
                        Ux4gTextAreaStatus.Warning -> Ux4gIcons.warning
                        Ux4gTextAreaStatus.Success -> Ux4gIcons.success_circle
                        Ux4gTextAreaStatus.Default -> Ux4gIcons.info_circle
                    }
                    Ux4gIconComponent(
                        icon = statusIcon,
                        contentDescription = "Caption Status Icon",
                        tint = captionColor,
                        modifier = Modifier.size(14.dp)
                    )
                }
                Text(text = caption ?: "", style = captionStyle)
            }
        }
    }
}

// ══════════════════════════════════════════════════════════════════
// PREVIEWS
// ══════════════════════════════════════════════════════════════════

@Preview(showBackground = true, name = "Light Mode")
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, name = "Dark Mode")
@Composable
fun Ux4gTextAreaPreview() {
    Ux4gTheme {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Ux4gTextArea(
                value = "",
                onValueChange = {},
                label = "Label",
                required = true,
                placeholder = "Tell us more about your experience",
                caption = "This is a helper caption",
                maxLength = 200
            )

            Ux4gTextArea(
                value = "Some sample text entered here.",
                onValueChange = {},
                label = "Feedback",
                status = Ux4gTextAreaStatus.Success,
                caption = "Looks good!",
                maxLength = 200
            )
        }
    }
}
