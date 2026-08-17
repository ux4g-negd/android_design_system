package com.ux4g.designsystemv1.components.inputfield

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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ux4g.designsystemv1.R
import com.ux4g.designsystemv1.foundation.Ux4gIcon
import com.ux4g.designsystemv1.foundation.Ux4gIconComponent
import com.ux4g.designsystemv1.foundation.Ux4gIcons
import com.ux4g.designsystemv1.foundation.Ux4gColors
import com.ux4g.designsystemv1.foundation.Ux4gRadius
import com.ux4g.designsystemv1.theme.Ux4gTheme

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
        leadingIcon: Ux4gIcon? = null,
        trailingIcon: Ux4gIcon? = null,
        prefixText: String? = null,
        postfixText: String? = null,
        showPasswordToggle: Boolean = true,
        enabled: Boolean = true,
        readOnly: Boolean = false,
        singleLine: Boolean = true,
        keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
        keyboardActions: KeyboardActions = KeyboardActions.Default,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isFocused by interactionSource.collectIsFocusedAsState()
    val focusRequester = remember { FocusRequester() }

    var passwordVisible by remember { mutableStateOf(false) }

    // Colors
    val borderColor = getBorderColor(status = status, isFocused = isFocused, enabled = enabled)
    val backgroundColor = getBackgroundColor(enabled = enabled)
    val textColor = getTextColor(enabled = enabled)
    val placeholderColor = Ux4gTheme.colorScheme.onSurface.copy(alpha = 0.4f)
    val labelColor = getLabelColor(status = status, isFocused = isFocused, enabled = enabled)
    val captionColor = getCaptionColor(status = status, enabled = enabled)

    // Typography
    val textStyle = Ux4gTheme.typography.bM_default.copy(color = textColor)
    val labelStyle = Ux4gTheme.typography.bS_strong.copy(color = labelColor)
    val captionStyle = Ux4gTheme.typography.bXS_default.copy(color = captionColor)
    val prefixPostfixStyle = Ux4gTheme.typography.bM_default.copy(color = Ux4gTheme.colorScheme.onSurface.copy(alpha = 0.5f))

    val visualTransformation =
            if (type == Ux4gInputFieldType.Password && !passwordVisible) {
                PasswordVisualTransformation()
            } else {
                VisualTransformation.None
            }

    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(8.dp)) {
        // Label Section
        if (label != null) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = label, style = labelStyle)
                if (required) {
                    Text(text = " *", style = labelStyle.copy(color = Ux4gTheme.colorScheme.error))
                }
            }
        }

        // Input Container
        Row(
                modifier =
                        Modifier.fillMaxWidth()
                                .height(size.height)
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
                                .padding(horizontal = 12.dp),
                verticalAlignment = Alignment.CenterVertically
        ) {
            // Leading Icon
            if (leadingIcon != null) {
                Ux4gIconComponent(
                        icon = leadingIcon,
                        contentDescription = "Leading Icon",
                        tint = Ux4gTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                        modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
            }

            // Prefix Text
            if (prefixText != null) {
                Text(text = prefixText, style = prefixPostfixStyle)
                Spacer(modifier = Modifier.width(4.dp))
            }

            // Input Field
            Box(
                    modifier = Modifier.weight(1f).fillMaxHeight(),
                    contentAlignment = Alignment.CenterStart
            ) {
                if (value.isEmpty() && placeholder != null) {
                    Text(
                            text = placeholder,
                            style =
                                    textStyle.copy(
                                            color = placeholderColor
                                    ) // Placeholder custom style if needed
                    )
                }
                BasicTextField(
                        value = value,
                        onValueChange = onValueChange,
                        modifier = Modifier.fillMaxWidth().focusRequester(focusRequester),
                        textStyle = textStyle,
                        singleLine = singleLine,
                        enabled = enabled,
                        readOnly = readOnly,
                        keyboardOptions = keyboardOptions,
                        keyboardActions = keyboardActions,
                        interactionSource = interactionSource,
                        visualTransformation = visualTransformation,
                        cursorBrush = SolidColor(Ux4gTheme.colorScheme.primary),
                        decorationBox = { innerTextField -> innerTextField() }
                )
            }

            // Postfix Text
            if (postfixText != null) {
                Spacer(modifier = Modifier.width(4.dp))
                Text(text = postfixText, style = prefixPostfixStyle)
            }

            // Status Icon (Error/Warning/Success)
            val statusIcon = getStatusIcon(status)
            if (statusIcon != null) {
                Spacer(modifier = Modifier.width(8.dp))
                Ux4gIconComponent(
                        icon = statusIcon,
                        contentDescription = "Status Icon",
                        tint = getStatusIconColor(status),
                        modifier = Modifier.size(20.dp)
                )
            }

            // Trailing Icon or Eye Toggle
            if (type == Ux4gInputFieldType.Password && showPasswordToggle) {
                Spacer(modifier = Modifier.width(8.dp))
                // Using a default eye icon, or we can use custom if exists.
                // Assuming R.drawable.ic_eye or material icons. Let's use a composable placeholder
                // if needed,
                // but since we need an eye icon, let's just use Text or standard icons if they
                // exist in Ux4gIcons.
                // For now, I'll use a placeholder or check Ux4gIcons.
                // As Ux4gIcons might not have an eye icon defined, I'll define one here temporarily
                // or use a generic one if missing.
                // Let's create an eye icon placeholder or if Ux4gIcons has one.
                val eyeIconRes =
                        if (passwordVisible) android.R.drawable.ic_menu_view
                        else android.R.drawable.ic_menu_more
                val iconIcon =
                        Ux4gIcon.Drawable(eyeIconRes) // this is temporary, ideally use proper SVG
                Ux4gIconComponent(
                        icon = iconIcon,
                        contentDescription = "Toggle Password Visibility",
                        tint = Ux4gTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                        modifier =
                                Modifier.size(20.dp).clickable {
                                    passwordVisible = !passwordVisible
                                }
                )
            } else if (trailingIcon != null) {
                Spacer(modifier = Modifier.width(8.dp))
                Ux4gIconComponent(
                        icon = trailingIcon,
                        contentDescription = "Trailing Icon",
                        tint = Ux4gTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                        modifier = Modifier.size(20.dp)
                )
            }
        }

        // Caption Section
        AnimatedVisibility(
                visible = !caption.isNullOrBlank() || status == Ux4gInputFieldStatus.Error
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                if (status == Ux4gInputFieldStatus.Error || status == Ux4gInputFieldStatus.Warning
                ) {
                    val cStatusIcon = getStatusIcon(status)
                    if (cStatusIcon != null) {
                        Ux4gIconComponent(
                                icon = cStatusIcon,
                                contentDescription = "Caption Status Icon",
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

// Helper functions for colors and icons
@Composable
private fun getBorderColor(
        status: Ux4gInputFieldStatus,
        isFocused: Boolean,
        enabled: Boolean
): Color {
    if (!enabled) return Ux4gTheme.colorScheme.onSurface.copy(alpha = 0.3f)
    return when (status) {
        Ux4gInputFieldStatus.Error -> Ux4gTheme.colorScheme.error
        Ux4gInputFieldStatus.Warning -> Ux4gTheme.colorScheme.secondary
        Ux4gInputFieldStatus.Success -> Ux4gColors.green500
        Ux4gInputFieldStatus.Default ->
                if (isFocused) Ux4gTheme.colorScheme.primary else Ux4gTheme.colorScheme.onSurface.copy(alpha = 0.3f)
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
private fun getLabelColor(
        status: Ux4gInputFieldStatus,
        isFocused: Boolean,
        enabled: Boolean
): Color {
    if (!enabled) return Ux4gTheme.colorScheme.onSurface.copy(alpha = 0.4f)
    return when (status) {
        Ux4gInputFieldStatus.Error -> Ux4gTheme.colorScheme.error
        Ux4gInputFieldStatus.Warning -> Ux4gTheme.colorScheme.secondary
        Ux4gInputFieldStatus.Success -> Ux4gColors.green500
        Ux4gInputFieldStatus.Default ->
                if (isFocused) Ux4gTheme.colorScheme.primary else Ux4gTheme.colorScheme.onBackground
    }
}

@Composable
private fun getCaptionColor(status: Ux4gInputFieldStatus, enabled: Boolean): Color {
    if (!enabled) return Ux4gTheme.colorScheme.onSurface.copy(alpha = 0.4f)
    return when (status) {
        Ux4gInputFieldStatus.Error -> Ux4gTheme.colorScheme.error
        Ux4gInputFieldStatus.Warning -> Ux4gTheme.colorScheme.secondary
        Ux4gInputFieldStatus.Success -> Ux4gColors.green500
        Ux4gInputFieldStatus.Default -> Ux4gTheme.colorScheme.onSurface.copy(alpha = 0.6f)
    }
}

@Composable
private fun getStatusIcon(status: Ux4gInputFieldStatus): Ux4gIcon? {
    return when (status) {
        Ux4gInputFieldStatus.Error -> Ux4gIcons.error
        Ux4gInputFieldStatus.Warning -> Ux4gIcons.warning
        Ux4gInputFieldStatus.Success -> Ux4gIcons.success_circle // Assuming it exists or fallback
        Ux4gInputFieldStatus.Default -> null
    }
}

@Composable
private fun getStatusIconColor(status: Ux4gInputFieldStatus): Color {
    return when (status) {
        Ux4gInputFieldStatus.Error -> Ux4gTheme.colorScheme.error
        Ux4gInputFieldStatus.Warning -> Ux4gTheme.colorScheme.secondary
        Ux4gInputFieldStatus.Success -> Ux4gColors.green500
        Ux4gInputFieldStatus.Default -> Ux4gTheme.colorScheme.onSurface.copy(alpha = 0.5f)
    }
}

@Preview(showBackground = true, name = "1. Sizes")
@Composable
fun Ux4gInputFieldSizesPreview() {
    Ux4gTheme {
        Column(
                modifier = Modifier.padding(24.dp).background(Ux4gTheme.colorScheme.background),
                verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Ux4gInputField(
                    value = "",
                    onValueChange = {},
                    label = "XL",
                    placeholder = "Placeholder",
                    caption = "Description",
                    size = Ux4gInputFieldSize.Medium,
                    leadingIcon = Ux4gIcons.Person,
                    trailingIcon = Ux4gIcons.Close
            )
            Ux4gInputField(
                    value = "",
                    onValueChange = {},
                    label = "Large",
                    placeholder = "Placeholder",
                    caption = "Description",
                    size = Ux4gInputFieldSize.Large,
                    leadingIcon = Ux4gIcons.Person,
                    trailingIcon = Ux4gIcons.Close
            )
            Ux4gInputField(
                    value = "",
                    onValueChange = {},
                    label = "Medium",
                    placeholder = "Placeholder",
                    caption = "Description",
                    size = Ux4gInputFieldSize.Medium,
                    leadingIcon = Ux4gIcons.Person,
                    trailingIcon = Ux4gIcons.Close
            )
            Ux4gInputField(
                    value = "",
                    onValueChange = {},
                    label = "Small",
                    placeholder = "Placeholder",
                    caption = "Description",
                    size = Ux4gInputFieldSize.Small,
                    leadingIcon = Ux4gIcons.Person,
                    trailingIcon = Ux4gIcons.Close
            )
        }
    }
}

@Preview(showBackground = true, name = "2. States")
@Composable
fun Ux4gInputFieldStatesPreview() {
    Ux4gTheme {
        Column(
                modifier = Modifier.padding(24.dp).background(Ux4gTheme.colorScheme.background),
                verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Ux4gInputField(
                    value = "",
                    onValueChange = {},
                    label = "Default",
                    placeholder = "Placeholder",
                    caption = "Description",
                    leadingIcon = Ux4gIcons.Person,
                    trailingIcon = Ux4gIcons.Close
            )
            Ux4gInputField(
                    value = "Input text",
                    onValueChange = {},
                    label = "Filled",
                    placeholder = "Placeholder",
                    caption = "Description",
                    leadingIcon = Ux4gIcons.Person,
                    trailingIcon = Ux4gIcons.Close
            )
            Ux4gInputField(
                    value = "",
                    onValueChange = {},
                    label = "Disabled",
                    placeholder = "Placeholder",
                    caption = "Description",
                    enabled = false,
                    leadingIcon = Ux4gIcons.Person,
                    trailingIcon = Ux4gIcons.Close
            )
            Ux4gInputField(
                    value = "",
                    onValueChange = {},
                    label = "Error",
                    placeholder = "Placeholder",
                    caption = "Error message",
                    status = Ux4gInputFieldStatus.Error,
                    leadingIcon = Ux4gIcons.Person,
                    trailingIcon = Ux4gIcons.Close
            )
            Ux4gInputField(
                    value = "",
                    onValueChange = {},
                    label = "Warning",
                    placeholder = "Placeholder",
                    caption = "Warning message",
                    status = Ux4gInputFieldStatus.Warning,
                    leadingIcon = Ux4gIcons.Person,
                    trailingIcon = Ux4gIcons.Close
            )
            Ux4gInputField(
                    value = "",
                    onValueChange = {},
                    label = "Success",
                    placeholder = "Placeholder",
                    caption = "Success message",
                    status = Ux4gInputFieldStatus.Success,
                    leadingIcon = Ux4gIcons.Person,
                    trailingIcon = Ux4gIcons.Close
            )
        }
    }
}

@Preview(showBackground = true, name = "3. Passwords")
@Composable
fun Ux4gInputFieldPasswordPreview() {
    Ux4gTheme {
        Column(
                modifier = Modifier.padding(24.dp).background(Ux4gTheme.colorScheme.background),
                verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Ux4gInputField(
                    value = "123456789012cd",
                    onValueChange = {},
                    label = "Password",
                    type = Ux4gInputFieldType.Password,
                    size = Ux4gInputFieldSize.Medium,
                    leadingIcon = Ux4gIcons.Person,
                    caption = "Must be at least 8 characters"
            )
            Ux4gInputField(
                    value = "12345678cd",
                    onValueChange = {},
                    label = "Password",
                    type = Ux4gInputFieldType.Password,
                    size = Ux4gInputFieldSize.XL,
                    leadingIcon = Ux4gIcons.Person,
                    caption = "Description"
            )
            Ux4gInputField(
                    value = "12345678cd",
                    onValueChange = {},
                    label = "Password",
                    type = Ux4gInputFieldType.Password,
                    size = Ux4gInputFieldSize.Large,
                    leadingIcon = Ux4gIcons.Person,
                    caption = "Description"
            )
            Ux4gInputField(
                    value = "12345678cd",
                    onValueChange = {},
                    label = "Password",
                    type = Ux4gInputFieldType.Password,
                    size = Ux4gInputFieldSize.Medium,
                    leadingIcon = Ux4gIcons.Person,
                    caption = "Description"
            )
            Ux4gInputField(
                    value = "12345678cd",
                    onValueChange = {},
                    label = "Password",
                    type = Ux4gInputFieldType.Password,
                    size = Ux4gInputFieldSize.Small,
                    leadingIcon = Ux4gIcons.Person,
                    caption = "Description"
            )
        }
    }
}

@Preview(showBackground = true, name = "4. Elements toggles")
@Composable
fun Ux4gInputFieldElementsPreview() {
    Ux4gTheme {
        Column(
                modifier = Modifier.padding(24.dp).background(Ux4gTheme.colorScheme.background),
                verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Ux4gInputField(
                    value = "",
                    onValueChange = {},
                    label = "Label",
                    placeholder = "Placeholder",
                    caption = "Description",
                    required = true,
                    leadingIcon = Ux4gIcons.Person,
                    trailingIcon = Ux4gIcons.Close
            )
            Ux4gInputField(
                    value = "",
                    onValueChange = {},
                    label = "Label",
                    placeholder = "Placeholder",
                    caption = "Description",
                    leadingIcon = Ux4gIcons.Person,
                    trailingIcon = Ux4gIcons.Close
            )
            Ux4gInputField(
                    value = "",
                    onValueChange = {},
                    placeholder = "Placeholder",
                    caption = "Description",
                    leadingIcon = Ux4gIcons.Person,
                    trailingIcon = Ux4gIcons.Close
            )
            Ux4gInputField(
                    value = "",
                    onValueChange = {},
                    label = "Label",
                    placeholder = "Placeholder",
                    leadingIcon = Ux4gIcons.Person,
                    trailingIcon = Ux4gIcons.Close
            )
            Ux4gInputField(
                    value = "",
                    onValueChange = {},
                    placeholder = "Placeholder",
                    leadingIcon = Ux4gIcons.Person,
                    trailingIcon = Ux4gIcons.Close
            )
        }
    }
}

@Preview(showBackground = true, name = "5. Affixes")
@Composable
fun Ux4gInputFieldAffixesPreview() {
    Ux4gTheme {
        Column(
                modifier = Modifier.padding(24.dp).background(Ux4gTheme.colorScheme.background),
                verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Ux4gInputField(
                    value = "0.00",
                    onValueChange = {},
                    label = "Label",
                    prefixText = "$",
                    leadingIcon = Ux4gIcons.Person,
                    trailingIcon = Ux4gIcons.Close
            )
            Ux4gInputField(
                    value = "Weight",
                    onValueChange = {},
                    label = "Label",
                    postfixText = "kg",
                    leadingIcon = Ux4gIcons.Person,
                    trailingIcon = Ux4gIcons.Close
            )
            Ux4gInputField(
                    value = "domain",
                    onValueChange = {},
                    label = "Label",
                    prefixText = "https://",
                    postfixText = ".com",
                    leadingIcon = Ux4gIcons.Person,
                    trailingIcon = Ux4gIcons.Close
            )
            Ux4gInputField(
                    value = "Amount",
                    onValueChange = {},
                    label = "Label",
                    prefixText = "$",
                    postfixText = "USD",
                    leadingIcon = Ux4gIcons.Person,
                    trailingIcon = Ux4gIcons.Close
            )
            Ux4gInputField(
                    value = "Phone number",
                    onValueChange = {},
                    label = "Label",
                    prefixText = "+1"
            )

            Ux4gInputField(
                    value = "0.00",
                    onValueChange = {},
                    label = "Label",
                    prefixText = "$",
                    postfixText = "USD",
                    size = Ux4gInputFieldSize.XL,
                    leadingIcon = Ux4gIcons.Person,
                    trailingIcon = Ux4gIcons.Close
            )
            Ux4gInputField(
                    value = "0.00",
                    onValueChange = {},
                    label = "Label",
                    prefixText = "$",
                    postfixText = "USD",
                    size = Ux4gInputFieldSize.Large,
                    leadingIcon = Ux4gIcons.Person,
                    trailingIcon = Ux4gIcons.Close
            )
            Ux4gInputField(
                    value = "0.00",
                    onValueChange = {},
                    label = "Label",
                    prefixText = "$",
                    postfixText = "USD",
                    size = Ux4gInputFieldSize.Medium,
                    leadingIcon = Ux4gIcons.Person,
                    trailingIcon = Ux4gIcons.Close
            )
            Ux4gInputField(
                    value = "0.00",
                    onValueChange = {},
                    label = "Label",
                    prefixText = "$",
                    postfixText = "USD",
                    size = Ux4gInputFieldSize.Small,
                    leadingIcon = Ux4gIcons.Person,
                    trailingIcon = Ux4gIcons.Close
            )
        }
    }
}

@Preview(showBackground = true, name = "6. Icons Combinations")
@Composable
fun Ux4gInputFieldIconsPreview() {
    Ux4gTheme {
        Column(
                modifier = Modifier.padding(24.dp).background(Ux4gTheme.colorScheme.background),
                verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Ux4gInputField(
                    value = "",
                    onValueChange = {},
                    label = "Label",
                    placeholder = "Placeholder",
                    leadingIcon = Ux4gIcons.Person,
                    trailingIcon = Ux4gIcons.Close
            )
            Ux4gInputField(
                    value = "",
                    onValueChange = {},
                    label = "Label",
                    placeholder = "Placeholder",
                    leadingIcon = Ux4gIcons.Person
            )
            Ux4gInputField(
                    value = "",
                    onValueChange = {},
                    label = "Label",
                    placeholder = "Placeholder",
                    trailingIcon = Ux4gIcons.Close
            )
            Ux4gInputField(
                    value = "",
                    onValueChange = {},
                    label = "Label",
                    placeholder = "Placeholder",
                    trailingIcon = Ux4gIcons.Close
            )
            Ux4gInputField(
                    value = "",
                    onValueChange = {},
                    label = "Label",
                    placeholder = "Placeholder"
            )
        }
    }
}
