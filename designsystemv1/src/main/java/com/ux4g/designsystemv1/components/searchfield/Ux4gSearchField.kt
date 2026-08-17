package com.ux4g.designsystemv1.components.searchfield

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
import com.ux4g.designsystemv1.components.Ux4gLoader
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ux4g.designsystemv1.foundation.Ux4gIconComponent
import com.ux4g.designsystemv1.foundation.Ux4gIcons
import com.ux4g.designsystemv1.foundation.Ux4gColors
import com.ux4g.designsystemv1.foundation.Ux4gRadius
import com.ux4g.designsystemv1.theme.Ux4gTheme

@Composable
fun Ux4gSearchField(
        value: String,
        onValueChange: (String) -> Unit,
        modifier: Modifier = Modifier,
        variant: Ux4gSearchFieldVariant = Ux4gSearchFieldVariant.BasicSearch,
        size: Ux4gSearchFieldSize = Ux4gSearchFieldSize.Medium,
        status: Ux4gSearchFieldStatus = Ux4gSearchFieldStatus.Default,
        buttonStyle: Ux4gSearchFieldButtonStyle = Ux4gSearchFieldButtonStyle.Filled,
        label: String? = null,
        placeholder: String? = null,
        caption: String? = null,
        options: List<String> = emptyList(),
        showVoiceIcon: Boolean = true,
        showClearIcon: Boolean = true,
        isLoading: Boolean = false,
        onVoiceClick: () -> Unit = {},
        onClearClick: () -> Unit = { onValueChange("") },
        onSubmitClick: (String) -> Unit = {},
        onOptionSelected: (String) -> Unit = {},
        enabled: Boolean = true,
        readOnly: Boolean = false
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isFocused by interactionSource.collectIsFocusedAsState()
    val focusRequester = remember { FocusRequester() }

    val borderColor =
            getBorderColor(
                    status = status,
                    isFocused = isFocused,
                    enabled = enabled,
                    isAutocompleteActive =
                            isFocused && variant == Ux4gSearchFieldVariant.Autocomplete
            )
    val backgroundColor = getBackgroundColor(enabled = enabled)
    val textColor = getTextColor(enabled = enabled)
    val placeholderColor = Ux4gTheme.colorScheme.onSurface.copy(alpha = 0.4f)
    val labelColor = getLabelColor(status = status, isFocused = isFocused, enabled = enabled)
    val captionColor = getCaptionColor(status = status, enabled = enabled)

    val textStyle = Ux4gTheme.typography.bM_default.copy(color = textColor)
    val labelStyle = Ux4gTheme.typography.bM_strong.copy(color = labelColor)
    val captionStyle = Ux4gTheme.typography.bXS_default.copy(color = captionColor)

    // Adjust corner radius depending on variant
    val isSubmitVariant = variant == Ux4gSearchFieldVariant.SearchWithSubmit
    val isAutocompleteActive =
            isFocused && variant == Ux4gSearchFieldVariant.Autocomplete && value.isNotEmpty()

    val inputShape =
            when {
                isSubmitVariant ->
                        RoundedCornerShape(
                                topStart = Ux4gRadius.Radius8,
                                bottomStart = Ux4gRadius.Radius8
                        )
                else -> RoundedCornerShape(Ux4gRadius.Radius8)
            }

    val paddingH =
            when (size) {
                Ux4gSearchFieldSize.XL, Ux4gSearchFieldSize.Large -> 16.dp
                Ux4gSearchFieldSize.Medium, Ux4gSearchFieldSize.Small -> 12.dp
            }

    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(8.dp)) {
        if (label != null) {
            Text(text = label, style = labelStyle)
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            // Input Box
            Box(
                    modifier =
                            Modifier.weight(1f)
                                    .height(size.height)
                                    .clip(inputShape)
                                    .background(backgroundColor)
                                    .border(
                                            width = if (isFocused) 2.dp else 1.dp,
                                            color = borderColor,
                                            shape = inputShape
                                    )
                                    .clickable(
                                            interactionSource = interactionSource,
                                            indication = null,
                                            enabled = enabled
                                    ) { focusRequester.requestFocus() }
                                    .padding(horizontal = paddingH),
                    contentAlignment = Alignment.CenterStart
            ) {
                Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                ) {
                    // Leading Search Icon
                    Ux4gIconComponent(
                            icon = Ux4gIcons.Search,
                            contentDescription = "Search",
                            tint = Ux4gTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                            modifier = Modifier.size(20.dp)
                    )

                    // Text Input
                    Box(modifier = Modifier.weight(1f)) {
                        if (value.isEmpty() && placeholder != null) {
                            Text(
                                    text = placeholder,
                                    style = textStyle.copy(color = placeholderColor)
                            )
                        }
                        BasicTextField(
                                value = value,
                                onValueChange = onValueChange,
                                modifier = Modifier.fillMaxWidth().focusRequester(focusRequester),
                                textStyle = textStyle,
                                enabled = enabled,
                                readOnly = readOnly,
                                singleLine = true,
                                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                                keyboardActions =
                                        KeyboardActions(onSearch = { onSubmitClick(value) }),
                                interactionSource = interactionSource,
                                cursorBrush = SolidColor(Ux4gTheme.colorScheme.primary),
                                decorationBox = { innerTextField -> innerTextField() }
                        )
                    }

                    // Trailing Icons Row
                    Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (isLoading) {
                            Ux4gLoader(
                                    size = 16.dp,
                                    percentage = 75f,
                                    color = Ux4gTheme.colorScheme.primary,
                                    strokeWidth = 2.dp
                            )
                        }
                        if (showVoiceIcon) {
                            Ux4gIconComponent(
                                    icon = Ux4gIcons.Mic,
                                    contentDescription = "Voice Search",
                                    tint = Ux4gTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                                    modifier =
                                            Modifier.size(20.dp).clickable(onClick = onVoiceClick)
                            )
                        }
                        if (showClearIcon && value.isNotEmpty()) {
                            Ux4gIconComponent(
                                    icon = Ux4gIcons.Close,
                                    contentDescription = "Clear",
                                    tint = Ux4gTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                                    modifier =
                                            Modifier.size(20.dp).clickable(onClick = onClearClick)
                            )
                        }
                    }
                }
            }

            // Submit Button
            if (isSubmitVariant) {
                val buttonBg =
                        if (buttonStyle == Ux4gSearchFieldButtonStyle.Filled) Ux4gTheme.colorScheme.primary
                        else Ux4gColors.primary100
                val buttonIconColor =
                        if (buttonStyle == Ux4gSearchFieldButtonStyle.Filled) Ux4gTheme.colorScheme.onPrimary
                        else Ux4gTheme.colorScheme.primary
                val buttonShape =
                        RoundedCornerShape(
                                topEnd = Ux4gRadius.Radius8,
                                bottomEnd = Ux4gRadius.Radius8
                        )

                Box(
                        modifier =
                                Modifier.height(size.height)
                                        .width(size.height) // Making it a square
                                        .clip(buttonShape)
                                        .background(buttonBg)
                                        .clickable { onSubmitClick(value) },
                        contentAlignment = Alignment.Center
                ) {
                    Ux4gIconComponent(
                            icon = Ux4gIcons.Search,
                            contentDescription = "Submit Search",
                            tint = buttonIconColor,
                            modifier = Modifier.size(20.dp)
                    )
                }
            }
        }

        // Caption logic beneath search field
        if (!caption.isNullOrBlank() || status == Ux4gSearchFieldStatus.Error) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                val cStatusIcon = getStatusIcon(status)
                if (cStatusIcon != null) {
                    Ux4gIconComponent(
                            icon = cStatusIcon,
                            contentDescription = "Caption Status Icon",
                            tint = captionColor,
                            modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                } else if (status == Ux4gSearchFieldStatus.Default) {
                    Ux4gIconComponent(
                            icon = Ux4gIcons.info_circle,
                            contentDescription = "Standard Caption",
                            tint = captionColor,
                            modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                }
                Text(text = caption ?: "", style = captionStyle)
            }
        }

        // Dropdown Menu specific to Autocomplete
        AnimatedVisibility(visible = isAutocompleteActive) {
            Box(
                    modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 4.dp)
                            .shadow(elevation = 6.dp, shape = RoundedCornerShape(Ux4gRadius.Radius8))
                            .background(color = Ux4gTheme.colorScheme.surface, shape = RoundedCornerShape(Ux4gRadius.Radius8))
                            .border(width = 1.dp, color = Ux4gTheme.colorScheme.onSurface.copy(alpha = 0.2f), shape = RoundedCornerShape(Ux4gRadius.Radius8))
                            .clip(RoundedCornerShape(Ux4gRadius.Radius8))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Searching for '$value'", style = textStyle)
                    Spacer(modifier = Modifier.height(16.dp))

                    if (isLoading) {
                        Spacer(modifier = Modifier.height(16.dp))
                        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                            Ux4gLoader(
                                    size = 24.dp,
                                    percentage = 75f,
                                    color = Ux4gTheme.colorScheme.primary,
                                    strokeWidth = 3.dp
                            )
                        }
                    } else if (options.isEmpty()) {
                        Spacer(modifier = Modifier.height(16.dp))
                        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                            Text(
                                    text = "No results found",
                                    style = Ux4gTheme.typography.bM_default.copy(color = Ux4gTheme.colorScheme.onSurface.copy(alpha = 0.5f))
                            )
                        }
                    } else {
                        // Display the filtered options
                        options.forEach { option ->
                            Box(
                                    modifier = Modifier
                                            .fillMaxWidth()
                                            .clickable {
                                                onOptionSelected(option)
                                                onValueChange(option)
                                            }
                                            .padding(vertical = 12.dp)
                            ) {
                                Text(
                                        text = option,
                                        style = Ux4gTheme.typography.bM_default.copy(color = Ux4gTheme.colorScheme.onSurface.copy(alpha = 0.8f))
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun getBorderColor(
        status: Ux4gSearchFieldStatus,
        isFocused: Boolean,
        enabled: Boolean,
        isAutocompleteActive: Boolean
): Color {
    if (!enabled) return Ux4gTheme.colorScheme.onSurface.copy(alpha = 0.3f)
    if (isAutocompleteActive)
            return Ux4gTheme.colorScheme.primary // In the design, active autocomplete has purple border
    return when (status) {
        Ux4gSearchFieldStatus.Error -> Ux4gTheme.colorScheme.error
        Ux4gSearchFieldStatus.Warning -> Ux4gTheme.colorScheme.secondary
        Ux4gSearchFieldStatus.Success -> Ux4gColors.green500
        Ux4gSearchFieldStatus.Default -> {
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
private fun getLabelColor(
        status: Ux4gSearchFieldStatus,
        isFocused: Boolean,
        enabled: Boolean
): Color {
    if (!enabled) return Ux4gTheme.colorScheme.onSurface.copy(alpha = 0.4f)
    return when (status) {
        Ux4gSearchFieldStatus.Error -> Ux4gTheme.colorScheme.error
        Ux4gSearchFieldStatus.Warning -> Ux4gTheme.colorScheme.secondary
        Ux4gSearchFieldStatus.Success -> Ux4gColors.green500
        Ux4gSearchFieldStatus.Default -> {
            if (isFocused) Ux4gTheme.colorScheme.primary else Ux4gTheme.colorScheme.onBackground
        }
    }
}

@Composable
private fun getCaptionColor(status: Ux4gSearchFieldStatus, enabled: Boolean): Color {
    if (!enabled) return Ux4gTheme.colorScheme.onSurface.copy(alpha = 0.4f)
    return when (status) {
        Ux4gSearchFieldStatus.Error -> Ux4gTheme.colorScheme.error
        Ux4gSearchFieldStatus.Warning -> Ux4gTheme.colorScheme.secondary
        Ux4gSearchFieldStatus.Success -> Ux4gColors.green500
        Ux4gSearchFieldStatus.Default -> Ux4gTheme.colorScheme.onSurface.copy(alpha = 0.6f)
    }
}

@Composable
private fun getStatusIcon(
        status: Ux4gSearchFieldStatus
): com.ux4g.designsystemv1.foundation.Ux4gIcon? {
    return when (status) {
        Ux4gSearchFieldStatus.Error -> Ux4gIcons.error
        Ux4gSearchFieldStatus.Warning -> Ux4gIcons.warning
        Ux4gSearchFieldStatus.Success -> Ux4gIcons.success_circle
        Ux4gSearchFieldStatus.Default -> null
    }
}

// ---------- Previews ----------
@Preview(showBackground = true, name = "1. Variants")
@Composable
fun Ux4gSearchFieldVariantsPreview() {
    Ux4gTheme {
        Column(
                modifier = Modifier.padding(24.dp).background(Ux4gTheme.colorScheme.background),
                verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Ux4gSearchField(
                    value = "",
                    onValueChange = {},
                    label = "Basic Search",
                    placeholder = "Search for...",
                    showVoiceIcon = true,
                    showClearIcon = true
            )

            Ux4gSearchField(
                    value = "",
                    onValueChange = {},
                    variant = Ux4gSearchFieldVariant.SearchWithSubmit,
                    buttonStyle = Ux4gSearchFieldButtonStyle.Filled,
                    label = "Search with Submit (Filled)",
                    placeholder = "Search for...",
                    showVoiceIcon = true
            )

            Ux4gSearchField(
                    value = "m",
                    onValueChange = {},
                    variant = Ux4gSearchFieldVariant.SearchWithSubmit,
                    buttonStyle = Ux4gSearchFieldButtonStyle.Tonal,
                    label = "Search with Submit (Tonal)",
                    placeholder = "Search for...",
                    showVoiceIcon = true
            )

            Ux4gSearchField(
                    value = "m",
                    onValueChange = {},
                    variant = Ux4gSearchFieldVariant.Autocomplete,
                    label = "Autocomplete",
                    placeholder = "Search for...",
                    showVoiceIcon = true,
                    isLoading = true // Inner search input loading spinner
            )
        }
    }
}

@Preview(showBackground = true, name = "2. Sizes")
@Composable
fun Ux4gSearchFieldSizesPreview() {
    Ux4gTheme(darkTheme = false) {
        Column(
                modifier = Modifier.padding(24.dp).background(Ux4gTheme.colorScheme.background),
                verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Ux4gSearchField(
                    value = "Search query...",
                    onValueChange = {},
                    label = "Size XL",
                    size = Ux4gSearchFieldSize.XL,
                    variant = Ux4gSearchFieldVariant.SearchWithSubmit
            )
            Ux4gSearchField(
                    value = "Search query...",
                    onValueChange = {},
                    label = "Size L",
                    size = Ux4gSearchFieldSize.Large,
                    variant = Ux4gSearchFieldVariant.SearchWithSubmit
            )
            Ux4gSearchField(
                    value = "Search query...",
                    onValueChange = {},
                    label = "Size M",
                    size = Ux4gSearchFieldSize.Medium,
                    variant = Ux4gSearchFieldVariant.SearchWithSubmit
            )
            Ux4gSearchField(
                    value = "Search query...",
                    onValueChange = {},
                    label = "Size S",
                    size = Ux4gSearchFieldSize.Small,
                    variant = Ux4gSearchFieldVariant.SearchWithSubmit
            )
        }
    }
}
