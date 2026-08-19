package com.ux4g.designsystemv1.components.searchfield

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Surface
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
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import com.ux4g.designsystemv1.components.Ux4gLoader
import com.ux4g.designsystemv1.foundation.Ux4gColors
import com.ux4g.designsystemv1.foundation.Ux4gIcon
import com.ux4g.designsystemv1.foundation.Ux4gIconComponent
import com.ux4g.designsystemv1.foundation.Ux4gIcons
import com.ux4g.designsystemv1.foundation.Ux4gRadius
import com.ux4g.designsystemv1.foundation.defaultTypography
import com.ux4g.designsystemv1.theme.Ux4gTheme

/**
 * Ux4gSearchField component providing search input, submit variant, voice search,
 * clear action, status validation, and autocomplete dropdown with search term filtering.
 * Uses foundation typography tokens (defaultTypography) matching the Flutter specification.
 */
@Composable
fun Ux4gSearchField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    filterType: Ux4gSearchFilterType = Ux4gSearchFilterType.Contains,
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
    onVoiceClick: (() -> Unit)? = null,
    onClearClick: (() -> Unit)? = null,
    onSubmitClick: ((String) -> Unit)? = null,
    onOptionSelected: ((String) -> Unit)? = null,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    placeholderStyle: TextStyle? = null,
    dropdownMinHeight: Dp = 0.dp,
    dropdownMaxHeight: Dp = 350.dp
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isFocused by interactionSource.collectIsFocusedAsState()
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current
    var fieldWidthPx by remember { mutableIntStateOf(0) }

    val onSurface = Ux4gTheme.colorScheme.onSurface
    val isDark = isSystemInDarkTheme()

    val borderColor = getBorderColor(
        status = status,
        isFocused = isFocused,
        enabled = enabled,
        hasOptionsOrAutocomplete = isFocused && (options.isNotEmpty() || variant == Ux4gSearchFieldVariant.Autocomplete)
    )
    val backgroundColor = getBackgroundColor(enabled = enabled)
    val textColor = getTextColor(enabled = enabled)
    val labelColor = getLabelColor(status = status, isFocused = isFocused, enabled = enabled)
    val captionColor = getCaptionColor(status = status, enabled = enabled)

    val bmDefault = defaultTypography.bM_default
    val bmStrong = defaultTypography.bM_strong
    val bxsDefault = defaultTypography.bXS_default

    val textStyle = bmDefault.copy(color = textColor)
    val labelStyle = bmStrong.copy(color = labelColor)
    val captionStyle = bxsDefault.copy(color = captionColor)

    val isSubmitVariant = variant == Ux4gSearchFieldVariant.SearchWithSubmit
    val showSubmitButton = isSubmitVariant || (variant == Ux4gSearchFieldVariant.Autocomplete && onSubmitClick != null)

    val inputShape = if (showSubmitButton) {
        RoundedCornerShape(
            topStart = Ux4gRadius.Radius8,
            bottomStart = Ux4gRadius.Radius8
        )
    } else {
        RoundedCornerShape(Ux4gRadius.Radius8)
    }

    // Autocomplete dropdown filtering logic
    val isDropdownVisible = isFocused && value.trim().isNotEmpty() &&
            (options.isNotEmpty() || variant == Ux4gSearchFieldVariant.Autocomplete)

    val query = value.lowercase().trim()
    val isExactSelection = remember(options, query) {
        options.any { it.lowercase().trim() == query }
    }

    val filteredOptions = remember(options, query, filterType, isExactSelection) {
        if (isExactSelection || query.isEmpty()) {
            options
        } else {
            options.filter { option ->
                val lowerOpt = option.lowercase()
                when (filterType) {
                    Ux4gSearchFilterType.Contains -> lowerOpt.contains(query)
                    Ux4gSearchFilterType.StartsWith -> lowerOpt.startsWith(query)
                    Ux4gSearchFilterType.StartsWithPerTerm -> {
                        val queryTerms = query.split(" ").filter { it.isNotEmpty() }
                        val labelTerms = lowerOpt.split(" ").filter { it.isNotEmpty() }
                        queryTerms.all { qTerm -> labelTerms.any { it.startsWith(qTerm) } }
                    }
                }
            }
        }
    }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(0.dp)
    ) {
        // Optional Label
        if (label != null) {
            Text(text = label, style = labelStyle)
            Spacer(modifier = Modifier.height(8.dp))
        }

        // Input & Submit Row + Dropdown anchor
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .onGloballyPositioned { coordinates ->
                    fieldWidthPx = coordinates.size.width
                }
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Input Field Box
                Box(
                    modifier = Modifier
                        .weight(1f)
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
                        .padding(horizontal = 12.dp),
                    contentAlignment = Alignment.CenterStart
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        // Leading Search Icon
                        Ux4gIconComponent(
                            icon = Ux4gIcons.Search,
                            contentDescription = "Search",
                            tint = onSurface.copy(alpha = 0.5f),
                            modifier = Modifier.size(20.dp)
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        // Text Input
                        Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.CenterStart) {
                            if (value.isEmpty() && placeholder != null) {
                                Text(
                                    text = placeholder,
                                    style = (placeholderStyle ?: bmDefault).copy(
                                        color = placeholderStyle?.color ?: onSurface.copy(alpha = 0.4f),
                                        fontWeight = placeholderStyle?.fontWeight ?: FontWeight.Normal
                                    )
                                )
                            }
                            BasicTextField(
                                value = value,
                                onValueChange = onValueChange,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .focusRequester(focusRequester),
                                textStyle = textStyle,
                                enabled = enabled,
                                readOnly = readOnly,
                                singleLine = true,
                                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                                keyboardActions = KeyboardActions(onSearch = { onSubmitClick?.invoke(value) }),
                                interactionSource = interactionSource,
                                cursorBrush = SolidColor(Ux4gTheme.colorScheme.primary),
                                decorationBox = { innerTextField -> innerTextField() }
                            )
                        }

                        // Trailing Loading Spinner
                        if (isLoading && variant != Ux4gSearchFieldVariant.Autocomplete) {
                            Spacer(modifier = Modifier.width(8.dp))
                            Ux4gLoader(
                                size = 16.dp,
                                percentage = 100f,
                                color = Ux4gTheme.colorScheme.primary,
                                strokeWidth = 2.dp
                            )
                        }

                        // Trailing Voice Search Icon
                        if (showVoiceIcon) {
                            Spacer(modifier = Modifier.width(8.dp))
                            Ux4gIconComponent(
                                icon = Ux4gIcons.Mic,
                                contentDescription = "Voice Search",
                                tint = onSurface.copy(alpha = 0.5f),
                                modifier = Modifier
                                    .size(20.dp)
                                    .clickable(enabled = enabled, onClick = { onVoiceClick?.invoke() })
                            )
                        }

                        // Trailing Clear Icon
                        if (showClearIcon && value.isNotEmpty()) {
                            Spacer(modifier = Modifier.width(8.dp))
                            Ux4gIconComponent(
                                icon = Ux4gIcons.Close,
                                contentDescription = "Clear",
                                tint = onSurface.copy(alpha = 0.5f),
                                modifier = Modifier
                                    .size(20.dp)
                                    .clickable(enabled = enabled, onClick = {
                                        onClearClick?.invoke() ?: onValueChange("")
                                    })
                            )
                        }
                    }
                }

                // Submit Button
                if (showSubmitButton) {
                    val buttonBg = if (buttonStyle == Ux4gSearchFieldButtonStyle.Filled) {
                        Ux4gTheme.colorScheme.primary
                    } else {
                        Ux4gTheme.colorScheme.primary.copy(alpha = 0.1f)
                    }
                    val buttonIconColor = if (buttonStyle == Ux4gSearchFieldButtonStyle.Filled) {
                        Ux4gTheme.colorScheme.onPrimary
                    } else {
                        Ux4gTheme.colorScheme.primary
                    }
                    val buttonShape = RoundedCornerShape(
                        topEnd = Ux4gRadius.Radius8,
                        bottomEnd = Ux4gRadius.Radius8
                    )

                    Box(
                        modifier = Modifier
                            .height(size.height)
                            .width(size.height)
                            .clip(buttonShape)
                            .background(buttonBg)
                            .clickable(enabled = enabled) { onSubmitClick?.invoke(value) },
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

            // Autocomplete Dropdown Popup Overlay
            if (isDropdownVisible && fieldWidthPx > 0) {
                val density = LocalDensity.current
                val fieldWidthDp = with(density) { fieldWidthPx.toDp() }
                val yOffsetPx = with(density) { (size.height + 4.dp).roundToPx() }

                Popup(
                    alignment = Alignment.TopStart,
                    offset = IntOffset(x = 0, y = yOffsetPx),
                    onDismissRequest = { /* dismiss handled by focus */ },
                    properties = PopupProperties(
                        focusable = false,
                        dismissOnClickOutside = true,
                        dismissOnBackPress = true
                    )
                ) {
                    val surfaceColor = Ux4gTheme.colorScheme.surface
                    val primaryColor = Ux4gTheme.colorScheme.primary

                    Surface(
                        modifier = Modifier
                            .width(fieldWidthDp)
                            .heightIn(min = dropdownMinHeight, max = dropdownMaxHeight)
                            .shadow(elevation = 6.dp, shape = RoundedCornerShape(Ux4gRadius.Radius8))
                            .border(
                                width = 1.dp,
                                color = if (isDark) Ux4gColors.neutral800 else Ux4gColors.neutral200,
                                shape = RoundedCornerShape(Ux4gRadius.Radius8)
                            ),
                        shape = RoundedCornerShape(Ux4gRadius.Radius8),
                        color = surfaceColor,
                        tonalElevation = 6.dp,
                        shadowElevation = 6.dp
                    ) {
                        if (isLoading) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Ux4gLoader(
                                    size = 24.dp,
                                    percentage = 100f,
                                    color = primaryColor,
                                    strokeWidth = 2.dp
                                )
                            }
                        } else if (options.isEmpty() || filteredOptions.isEmpty()) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp, vertical = 14.dp),
                                contentAlignment = Alignment.CenterStart
                            ) {
                                Text(
                                    text = "No results found",
                                    style = bmDefault.copy(
                                        fontSize = 14.sp,
                                        color = onSurface.copy(alpha = 0.5f)
                                    )
                                )
                            }
                        } else {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .verticalScroll(rememberScrollState())
                                    .padding(bottom = 6.dp)
                            ) {
                                val queryText = value.trim()
                                if (queryText.isNotEmpty()) {
                                    Text(
                                        text = buildAnnotatedString {
                                            withStyle(
                                                SpanStyle(
                                                    fontSize = 13.sp,
                                                    color = onSurface.copy(alpha = 0.6f)
                                                )
                                            ) {
                                                append("Searching for ")
                                            }
                                            withStyle(
                                                SpanStyle(
                                                    fontSize = 13.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    color = onSurface
                                                )
                                            ) {
                                                append("'$queryText'")
                                            }
                                        },
                                        modifier = Modifier.padding(
                                            start = 16.dp,
                                            end = 16.dp,
                                            top = 12.dp,
                                            bottom = 8.dp
                                        )
                                    )
                                }

                                filteredOptions.forEach { option ->
                                    val isSelected = value == option
                                    val itemBg = if (isSelected) {
                                        if (isDark) Ux4gColors.primary950 else Ux4gColors.primary50
                                    } else {
                                        Color.Transparent
                                    }
                                    val itemTextColor = if (isSelected) {
                                        if (isDark) Ux4gColors.primary300 else Ux4gColors.primary600
                                    } else {
                                        onSurface
                                    }

                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .background(itemBg)
                                            .clickable {
                                                onOptionSelected?.invoke(option)
                                                onValueChange(option)
                                                focusManager.clearFocus()
                                            }
                                            .padding(horizontal = 16.dp, vertical = 10.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = option,
                                            modifier = Modifier.weight(1f),
                                            style = bmDefault.copy(
                                                fontSize = 14.sp,
                                                fontWeight = FontWeight.Normal,
                                                color = itemTextColor
                                            )
                                        )
                                        if (isSelected) {
                                            Ux4gIconComponent(
                                                icon = Ux4gIcons.Check,
                                                contentDescription = "Selected",
                                                tint = if (isDark) Ux4gColors.primary300 else Ux4gColors.primary600,
                                                modifier = Modifier.size(18.dp)
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // Caption & Status indicator below the field
        if (!caption.isNullOrBlank()) {
            Spacer(modifier = Modifier.height(4.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                val cStatusIcon = getStatusIcon(status)
                Ux4gIconComponent(
                    icon = cStatusIcon,
                    contentDescription = "Caption Status Icon",
                    tint = captionColor,
                    modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = caption,
                    style = captionStyle
                )
            }
        }
    }
}

@Composable
private fun getBorderColor(
    status: Ux4gSearchFieldStatus,
    isFocused: Boolean,
    enabled: Boolean,
    hasOptionsOrAutocomplete: Boolean
): Color {
    val onSurface = Ux4gTheme.colorScheme.onSurface
    val primary = Ux4gTheme.colorScheme.primary
    val error = Ux4gTheme.colorScheme.error
    val warning = Ux4gTheme.colorScheme.warning
    val success = Ux4gTheme.colorScheme.success

    if (!enabled) return onSurface.copy(alpha = 0.3f)
    if (isFocused && hasOptionsOrAutocomplete) return primary
    return when (status) {
        Ux4gSearchFieldStatus.Error -> error
        Ux4gSearchFieldStatus.Warning -> warning
        Ux4gSearchFieldStatus.Success -> success
        Ux4gSearchFieldStatus.Default -> if (isFocused) primary else onSurface.copy(alpha = 0.3f)
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
    val onSurface = Ux4gTheme.colorScheme.onSurface
    val onBackground = Ux4gTheme.colorScheme.onBackground
    val primary = Ux4gTheme.colorScheme.primary
    val error = Ux4gTheme.colorScheme.error
    val warning = Ux4gTheme.colorScheme.warning
    val success = Ux4gTheme.colorScheme.success

    if (!enabled) return onSurface.copy(alpha = 0.4f)
    return when (status) {
        Ux4gSearchFieldStatus.Error -> error
        Ux4gSearchFieldStatus.Warning -> warning
        Ux4gSearchFieldStatus.Success -> success
        Ux4gSearchFieldStatus.Default -> if (isFocused) primary else onBackground
    }
}

@Composable
private fun getCaptionColor(status: Ux4gSearchFieldStatus, enabled: Boolean): Color {
    val onSurface = Ux4gTheme.colorScheme.onSurface
    val error = Ux4gTheme.colorScheme.error
    val warning = Ux4gTheme.colorScheme.warning
    val success = Ux4gTheme.colorScheme.success

    if (!enabled) return onSurface.copy(alpha = 0.4f)
    return when (status) {
        Ux4gSearchFieldStatus.Error -> error
        Ux4gSearchFieldStatus.Warning -> warning
        Ux4gSearchFieldStatus.Success -> success
        Ux4gSearchFieldStatus.Default -> onSurface.copy(alpha = 0.6f)
    }
}

@Composable
private fun getStatusIcon(status: Ux4gSearchFieldStatus): Ux4gIcon {
    return when (status) {
        Ux4gSearchFieldStatus.Error -> Ux4gIcons.error
        Ux4gSearchFieldStatus.Warning -> Ux4gIcons.warning
        Ux4gSearchFieldStatus.Success -> Ux4gIcons.success_circle
        Ux4gSearchFieldStatus.Default -> Ux4gIcons.info_circle
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Previews
// ─────────────────────────────────────────────────────────────────────────────

@Preview(showBackground = true, name = "1. Variants & Button Styles")
@Composable
fun Ux4gSearchFieldVariantsPreview() {
    Ux4gTheme {
        Column(
            modifier = Modifier
                .padding(24.dp)
                .background(Ux4gTheme.colorScheme.background),
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
                value = "Query",
                onValueChange = {},
                variant = Ux4gSearchFieldVariant.SearchWithSubmit,
                buttonStyle = Ux4gSearchFieldButtonStyle.Filled,
                label = "Search with Submit (Filled)",
                placeholder = "Search for...",
                showVoiceIcon = true
            )

            Ux4gSearchField(
                value = "Query",
                onValueChange = {},
                variant = Ux4gSearchFieldVariant.SearchWithSubmit,
                buttonStyle = Ux4gSearchFieldButtonStyle.Tonal,
                label = "Search with Submit (Tonal)",
                placeholder = "Search for...",
                showVoiceIcon = true
            )

            Ux4gSearchField(
                value = "Loading",
                onValueChange = {},
                label = "Search with Loading",
                placeholder = "Search...",
                isLoading = true
            )
        }
    }
}

@Preview(showBackground = true, name = "2. Sizes")
@Composable
fun Ux4gSearchFieldSizesPreview() {
    Ux4gTheme(darkTheme = false) {
        Column(
            modifier = Modifier
                .padding(24.dp)
                .background(Ux4gTheme.colorScheme.background),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Ux4gSearchField(
                value = "Size XL text",
                onValueChange = {},
                label = "Size XL (56dp)",
                size = Ux4gSearchFieldSize.XL,
                variant = Ux4gSearchFieldVariant.SearchWithSubmit
            )
            Ux4gSearchField(
                value = "Size L text",
                onValueChange = {},
                label = "Size Large (48dp)",
                size = Ux4gSearchFieldSize.Large,
                variant = Ux4gSearchFieldVariant.SearchWithSubmit
            )
            Ux4gSearchField(
                value = "Size M text",
                onValueChange = {},
                label = "Size Medium (40dp)",
                size = Ux4gSearchFieldSize.Medium,
                variant = Ux4gSearchFieldVariant.SearchWithSubmit
            )
            Ux4gSearchField(
                value = "Size S text",
                onValueChange = {},
                label = "Size Small (32dp)",
                size = Ux4gSearchFieldSize.Small,
                variant = Ux4gSearchFieldVariant.SearchWithSubmit
            )
        }
    }
}

@Preview(showBackground = true, name = "3. Statuses & Captions")
@Composable
fun Ux4gSearchFieldStatusesPreview() {
    Ux4gTheme {
        Column(
            modifier = Modifier
                .padding(24.dp)
                .background(Ux4gTheme.colorScheme.background),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Ux4gSearchField(
                value = "Normal text",
                onValueChange = {},
                label = "Default Status",
                caption = "This is a helper message.",
                status = Ux4gSearchFieldStatus.Default
            )

            Ux4gSearchField(
                value = "Success value",
                onValueChange = {},
                label = "Success Status",
                caption = "Search result verified successfully.",
                status = Ux4gSearchFieldStatus.Success
            )

            Ux4gSearchField(
                value = "Warning query",
                onValueChange = {},
                label = "Warning Status",
                caption = "This search may take longer to load.",
                status = Ux4gSearchFieldStatus.Warning
            )

            Ux4gSearchField(
                value = "Invalid search",
                onValueChange = {},
                label = "Error Status",
                caption = "Invalid search query. Please try again.",
                status = Ux4gSearchFieldStatus.Error
            )

            Ux4gSearchField(
                value = "Disabled text",
                onValueChange = {},
                label = "Disabled Search",
                caption = "Search is temporarily disabled.",
                enabled = false
            )
        }
    }
}

@Preview(showBackground = true, name = "4. Autocomplete Preview")
@Composable
fun Ux4gSearchFieldAutocompletePreview() {
    var query by remember { mutableStateOf("Ban") }
    val options = listOf("Apple", "Banana", "Bandung", "Blueberry", "Cherry", "Mango", "Orange")

    Ux4gTheme {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
                .background(Ux4gTheme.colorScheme.background)
        ) {
            Ux4gSearchField(
                value = query,
                onValueChange = { query = it },
                variant = Ux4gSearchFieldVariant.Autocomplete,
                filterType = Ux4gSearchFilterType.StartsWith,
                label = "Fruit Autocomplete (StartsWith)",
                placeholder = "Type fruit name...",
                options = options,
                onOptionSelected = { query = it }
            )
        }
    }
}
