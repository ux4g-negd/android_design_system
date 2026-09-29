package com.ux4g.designsystemv1.components.inputfield

import android.content.res.Configuration
import androidx.annotation.DrawableRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ux4g.designsystemv1.foundation.Ux4gColors
import com.ux4g.designsystemv1.foundation.Ux4gIcon
import com.ux4g.designsystemv1.foundation.Ux4gIconComponent
import com.ux4g.designsystemv1.foundation.Ux4gRadius
import com.ux4g.designsystemv1.foundation.defaultTypography

/**
 * A comprehensive Input Field component matching UX4G design system specifications 1:1.
 *
 * Supports labels with required asterisks, trailing label icons, leading/trailing icons,
 * prefix/postfix affixes, password toggle, multiple sizes (Small, Medium, Large, XL),
 * validation statuses (Default, Error, Warning, Success), caption icons, and custom typography/colors.
 */
@Composable
fun Ux4gInputField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    size: Ux4gInputFieldSize = Ux4gInputFieldSize.Medium,
    type: Ux4gInputFieldType = Ux4gInputFieldType.Text,
    status: Ux4gInputFieldStatus = Ux4gInputFieldStatus.Default,
    label: String? = null,
    required: Boolean = false,
    placeholder: String? = null,
    caption: String? = null,
    leadingIcon: Any? = null,
    trailingIcon: Any? = null,
    onTrailingIconPressed: (() -> Unit)? = null,
    prefixText: String? = null,
    postfixText: String? = null,
    trailingIconLabel: Any? = null,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    singleLine: Boolean = true,
    maxLines: Int = if (singleLine) 1 else Int.MAX_VALUE,
    maxLength: Int? = null,
    textAlign: TextAlign = TextAlign.Start,
    style: TextStyle? = null,
    placeholderStyle: TextStyle? = null,
    labelStyle: TextStyle? = null,
    captionStyle: TextStyle? = null,
    backgroundColor: Color? = null,
    borderColor: Color? = null,
    disabledBorderColor: Color? = null,
    borderWidth: Dp = 1.dp,
    disabledBorderWidth: Dp = 0.dp,
    showPasswordToggle: Boolean = true,
    visualTransformation: VisualTransformation? = null,
    keyboardOptions: KeyboardOptions? = null,
    keyboardActions: KeyboardActions = KeyboardActions.Default
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isFocused by interactionSource.collectIsFocusedAsState()
    val focusRequester = remember { FocusRequester() }

    var passwordVisible by remember { mutableStateOf(false) }
    val isDark = isSystemInDarkTheme()

    // ── Colors ───────────────────────────────────────────────────────────────
    val primary = Ux4gColors.primary
    val onSurface = if (isDark) Ux4gColors.neutral100 else Ux4gColors.neutral900
    val errorColor = if (isDark) Ux4gColors.red300 else Ux4gColors.red600
    val warningColor = Ux4gColors.orange500
    val successColor = Ux4gColors.green600

    val defaultBgColor = if (isDark) Ux4gColors.neutral950 else Ux4gColors.neutral0
    val resolvedBgColor = backgroundColor ?: if (enabled) defaultBgColor else onSurface.copy(alpha = 0.05f)

    val defaultBorderColor = if (isDark) Ux4gColors.neutral700 else Ux4gColors.neutral200
    val resolvedBorderColor = when {
        !enabled -> disabledBorderColor ?: onSurface.copy(alpha = 0.3f)
        status == Ux4gInputFieldStatus.Error -> errorColor
        status == Ux4gInputFieldStatus.Warning -> warningColor
        status == Ux4gInputFieldStatus.Success -> successColor
        isFocused -> primary
        else -> borderColor ?: defaultBorderColor
    }

    val resolvedBorderWidth = when {
        !enabled -> disabledBorderWidth
        isFocused -> 2.dp
        else -> borderWidth
    }

    val resolvedLabelColor = if (enabled) {
        if (isDark) Ux4gColors.neutral200 else Ux4gColors.neutral700
    } else {
        onSurface.copy(alpha = 0.4f)
    }

    val resolvedTextColor = if (enabled) onSurface else onSurface.copy(alpha = 0.4f)
    val resolvedPlaceholderColor = placeholderStyle?.color ?: (if (isDark) Ux4gColors.neutral400 else Ux4gColors.neutral500)

    val resolvedCaptionColor = when {
        !enabled -> onSurface.copy(alpha = 0.4f)
        status == Ux4gInputFieldStatus.Error -> if (isDark) Ux4gColors.red300 else Ux4gColors.red800
        status == Ux4gInputFieldStatus.Warning -> warningColor
        status == Ux4gInputFieldStatus.Success -> successColor
        else -> onSurface.copy(alpha = 0.6f)
    }

    val resolvedCaptionIconColor = when {
        !enabled -> onSurface.copy(alpha = 0.4f)
        status == Ux4gInputFieldStatus.Error -> errorColor
        status == Ux4gInputFieldStatus.Warning -> warningColor
        status == Ux4gInputFieldStatus.Success -> successColor
        else -> onSurface.copy(alpha = 0.6f)
    }

    // ── Typography ───────────────────────────────────────────────────────────
    val resolvedLabelStyle = (labelStyle ?: defaultTypography.lL_default).copy(color = resolvedLabelColor)
    val resolvedTextStyle = (style ?: defaultTypography.bM_default).copy(color = resolvedTextColor, textAlign = textAlign)
    val resolvedPlaceholderStyle = (placeholderStyle ?: defaultTypography.bM_default).copy(color = resolvedPlaceholderColor, textAlign = textAlign)
    val resolvedCaptionStyle = (captionStyle ?: defaultTypography.bXS_default).copy(color = resolvedCaptionColor)
    val prefixStyle = defaultTypography.bS_default.copy(color = if (isDark) Ux4gColors.neutral200 else Ux4gColors.neutral700)
    val postfixStyle = defaultTypography.bM_default.copy(color = onSurface.copy(alpha = 0.5f))

    // ── Keyboard & Transformation ────────────────────────────────────────────
    val defaultKeyboardType = when (type) {
        Ux4gInputFieldType.Password -> KeyboardType.Password
        Ux4gInputFieldType.Number -> KeyboardType.Number
        Ux4gInputFieldType.Email -> KeyboardType.Email
        Ux4gInputFieldType.Text -> KeyboardType.Text
    }

    val effectiveKeyboardOptions = keyboardOptions ?: KeyboardOptions(keyboardType = defaultKeyboardType)

    val effectiveVisualTransformation = visualTransformation ?: if (type == Ux4gInputFieldType.Password && !passwordVisible) {
        PasswordVisualTransformation()
    } else {
        VisualTransformation.None
    }

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // ── Label Section ────────────────────────────────────────────────────
        if (label != null) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(text = label, style = resolvedLabelStyle)
                if (required) {
                    Text(
                        text = "*",
                        style = defaultTypography.bS_strong.copy(color = errorColor)
                    )
                }
                if (trailingIconLabel != null) {
                    RenderInputFieldIcon(
                        icon = trailingIconLabel,
                        tint = onSurface.copy(alpha = 0.5f),
                        size = 16.dp
                    )
                }
            }
        }

        // ── Input Container Box ──────────────────────────────────────────────
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .then(
                    if (singleLine) {
                        Modifier.height(size.height)
                    } else {
                        Modifier.defaultMinSize(minHeight = 100.dp)
                    }
                )
                .clip(RoundedCornerShape(Ux4gRadius.Radius8))
                .background(resolvedBgColor)
                .then(
                    if (resolvedBorderWidth > 0.dp) {
                        Modifier.border(
                            width = resolvedBorderWidth,
                            color = resolvedBorderColor,
                            shape = RoundedCornerShape(Ux4gRadius.Radius8)
                        )
                    } else {
                        Modifier
                    }
                )
                .clickable(
                    interactionSource = interactionSource,
                    indication = null,
                    enabled = enabled
                ) { focusRequester.requestFocus() }
                .padding(horizontal = 12.dp),
            contentAlignment = if (singleLine) Alignment.CenterStart else Alignment.TopStart
        ) {
            Row(
                modifier = Modifier.fillMaxWidth().then(if (!singleLine) Modifier.padding(top = 10.dp) else Modifier),
                verticalAlignment = if (singleLine) Alignment.CenterVertically else Alignment.Top
            ) {
                // Leading Icon
                if (leadingIcon != null) {
                    RenderInputFieldIcon(
                        icon = leadingIcon,
                        tint = onSurface.copy(alpha = 0.5f),
                        size = 20.dp
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                }

                // Prefix Text
                if (prefixText != null) {
                    Text(text = prefixText, style = prefixStyle)
                    Spacer(modifier = Modifier.width(4.dp))
                }

                // Core Text Field & Placeholder
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .then(if (singleLine) Modifier else Modifier.defaultMinSize(minHeight = 80.dp)),
                    contentAlignment = Alignment.CenterStart
                ) {
                    if (value.isEmpty() && placeholder != null) {
                        Text(
                            text = placeholder,
                            style = resolvedPlaceholderStyle,
                            maxLines = maxLines
                        )
                    }

                    BasicTextField(
                        value = value,
                        onValueChange = { newValue ->
                            val truncated = if (maxLength != null && newValue.length > maxLength) {
                                newValue.take(maxLength)
                            } else {
                                newValue
                            }
                            onValueChange(truncated)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .focusRequester(focusRequester),
                        textStyle = resolvedTextStyle,
                        singleLine = singleLine,
                        maxLines = maxLines,
                        enabled = enabled,
                        readOnly = readOnly,
                        keyboardOptions = effectiveKeyboardOptions,
                        keyboardActions = keyboardActions,
                        interactionSource = interactionSource,
                        visualTransformation = effectiveVisualTransformation,
                        cursorBrush = SolidColor(primary),
                        decorationBox = { innerTextField -> innerTextField() }
                    )
                }

                // Postfix Text
                if (postfixText != null) {
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = postfixText, style = postfixStyle)
                }

                // Trailing Status Icon (Warning / Success / Error in field)
                val statusIconVector = when (status) {
                    Ux4gInputFieldStatus.Error -> Icons.Default.Error
                    Ux4gInputFieldStatus.Warning -> Icons.Default.Warning
                    Ux4gInputFieldStatus.Success -> Icons.Default.CheckCircle
                    Ux4gInputFieldStatus.Default -> null
                }

                if (type != Ux4gInputFieldType.Password && trailingIcon == null && statusIconVector != null) {
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(
                        imageVector = statusIconVector,
                        contentDescription = "Status",
                        tint = resolvedCaptionIconColor,
                        modifier = Modifier.size(20.dp)
                    )
                }

                // Password Eye Toggle
                if (type == Ux4gInputFieldType.Password && showPasswordToggle) {
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(
                        imageVector = if (passwordVisible) Icons.Outlined.Visibility else Icons.Outlined.VisibilityOff,
                        contentDescription = if (passwordVisible) "Hide Password" else "Show Password",
                        tint = onSurface.copy(alpha = 0.5f),
                        modifier = Modifier
                            .size(20.dp)
                            .clickable { passwordVisible = !passwordVisible }
                    )
                } else if (trailingIcon != null) {
                    // Custom Trailing Icon
                    Spacer(modifier = Modifier.width(8.dp))
                    Box(
                        modifier = Modifier.then(
                            if (onTrailingIconPressed != null) {
                                Modifier.clickable(onClick = onTrailingIconPressed)
                            } else {
                                Modifier
                            }
                        )
                    ) {
                        RenderInputFieldIcon(
                            icon = trailingIcon,
                            tint = onSurface.copy(alpha = 0.5f),
                            size = 20.dp
                        )
                    }
                }
            }
        }

        // ── Caption Section ──────────────────────────────────────────────────
        val showCaption = !caption.isNullOrBlank()
        if (showCaption) {
            val captionIconVector = when (status) {
                Ux4gInputFieldStatus.Error -> Icons.Default.Error
                Ux4gInputFieldStatus.Warning -> Icons.Default.Warning
                Ux4gInputFieldStatus.Success -> Icons.Default.CheckCircle
                Ux4gInputFieldStatus.Default -> Icons.Default.Info
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Icon(
                    imageVector = captionIconVector,
                    contentDescription = null,
                    tint = resolvedCaptionIconColor,
                    modifier = Modifier.size(14.dp)
                )
                Text(
                    text = caption ?: "",
                    style = resolvedCaptionStyle,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Icon Rendering Helper
// ─────────────────────────────────────────────────────────────────────────────

@Composable
private fun RenderInputFieldIcon(
    icon: Any,
    tint: Color,
    size: Dp,
    modifier: Modifier = Modifier
) {
    when (icon) {
        is ImageVector -> {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = tint,
                modifier = modifier.size(size)
            )
        }
        is Ux4gIcon -> {
            Ux4gIconComponent(
                icon = icon,
                contentDescription = null,
                tint = tint,
                modifier = modifier.size(size)
            )
        }
        is Int -> {
            Icon(
                painter = painterResource(id = icon),
                contentDescription = null,
                tint = tint,
                modifier = modifier.size(size)
            )
        }
    }
}

// ═══════════════════════════════════════════════════════
// Previews
// ═══════════════════════════════════════════════════════

@Preview(name = "1. Input Field Sizes - Light", showBackground = true, backgroundColor = 0xFFFFFFFF)
@Preview(name = "1. Input Field Sizes - Dark", uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true, backgroundColor = 0xFF121212)
@Composable
private fun Ux4gInputFieldSizesPreview() {
    Column(
        modifier = Modifier.padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Ux4gInputField(
            value = "",
            onValueChange = {},
            label = "XL Size (56dp)",
            placeholder = "Enter value",
            caption = "Extra large height input field",
            size = Ux4gInputFieldSize.XL,
            leadingIcon = Icons.Default.Person,
            trailingIcon = Icons.Default.Close
        )
        Ux4gInputField(
            value = "",
            onValueChange = {},
            label = "Large Size (48dp)",
            placeholder = "Enter value",
            caption = "Large height input field",
            size = Ux4gInputFieldSize.Large,
            leadingIcon = Icons.Default.Person,
            trailingIcon = Icons.Default.Close
        )
        Ux4gInputField(
            value = "",
            onValueChange = {},
            label = "Medium Size (40dp) [Default]",
            placeholder = "Enter value",
            caption = "Standard input field",
            size = Ux4gInputFieldSize.Medium,
            leadingIcon = Icons.Default.Person,
            trailingIcon = Icons.Default.Close
        )
        Ux4gInputField(
            value = "",
            onValueChange = {},
            label = "Small Size (32dp)",
            placeholder = "Enter value",
            caption = "Compact input field",
            size = Ux4gInputFieldSize.Small,
            leadingIcon = Icons.Default.Person,
            trailingIcon = Icons.Default.Close
        )
    }
}

@Preview(name = "2. Validation & Statuses", showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
private fun Ux4gInputFieldStatusesPreview() {
    Column(
        modifier = Modifier.padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Ux4gInputField(
            value = "John Doe",
            onValueChange = {},
            label = "Default State",
            caption = "This is a helpful hint"
        )
        Ux4gInputField(
            value = "invalid-email@",
            onValueChange = {},
            label = "Error State",
            status = Ux4gInputFieldStatus.Error,
            caption = "Please enter a valid email address."
        )
        Ux4gInputField(
            value = "pass123",
            onValueChange = {},
            label = "Warning State",
            status = Ux4gInputFieldStatus.Warning,
            caption = "Password strength is weak."
        )
        Ux4gInputField(
            value = "secure_user_99",
            onValueChange = {},
            label = "Success State",
            status = Ux4gInputFieldStatus.Success,
            caption = "Username is available!"
        )
        Ux4gInputField(
            value = "Readonly / Disabled",
            onValueChange = {},
            label = "Disabled State",
            enabled = false,
            caption = "This field cannot be edited."
        )
    }
}
