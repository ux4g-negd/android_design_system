package com.ux4g.designsystemv1.components

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.ux4g.designsystemv1.foundation.Ux4gColors
import com.ux4g.designsystemv1.foundation.Ux4gRadius
import com.ux4g.designsystemv1.foundation.defaultTypography

// ═══════════════════════════════════════════════════════
// Choice Chip
// ═══════════════════════════════════════════════════════

enum class Ux4gChoiceChipSize { S, M }

/**
 * A choice chip that can be selected/unselected.
 * Supports leading and trailing content, two sizes, and custom colors.
 */
@Composable
fun Ux4gChoiceChip(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    size: Ux4gChoiceChipSize = Ux4gChoiceChipSize.M,
    leadingContent: @Composable (() -> Unit)? = null,
    trailingContent: @Composable (() -> Unit)? = null,
    borderRadius: Dp = Ux4gRadius.Radius4,
    selectedBackgroundColor: Color? = null,
    unselectedBackgroundColor: Color? = null,
    selectedBorderColor: Color? = null,
    unselectedBorderColor: Color? = null,
    selectedTextColor: Color? = null,
    unselectedTextColor: Color? = null,
    selectedBorderWidth: Dp = 1.dp,
    unselectedBorderWidth: Dp = 1.dp,
) {
    val paddingH = when (size) {
        Ux4gChoiceChipSize.S -> 12.dp
        Ux4gChoiceChipSize.M -> 16.dp
    }
    val paddingV = when (size) {
        Ux4gChoiceChipSize.S -> 6.dp
        Ux4gChoiceChipSize.M -> 8.dp
    }
    val textStyle: TextStyle = when (size) {
        Ux4gChoiceChipSize.S -> defaultTypography.lM_default
        Ux4gChoiceChipSize.M -> defaultTypography.lL_default
    }

    // Colors
    val bgColor = if (selected) {
        selectedBackgroundColor ?: Ux4gColors.primary
    } else {
        unselectedBackgroundColor ?: Ux4gColors.neutral0 // surface = white
    }

    val borderColor = if (selected) {
        selectedBorderColor ?: Ux4gColors.primary
    } else {
        unselectedBorderColor ?: Ux4gColors.neutral900.copy(alpha = 0.2f) // onSurface 20%
    }

    val borderWidth = if (selected) selectedBorderWidth else unselectedBorderWidth

    val textColor = if (selected) {
        selectedTextColor ?: Ux4gColors.neutral50 // onPrimary
    } else {
        unselectedTextColor ?: Ux4gColors.neutral900 // onSurface
    }

    val shape = RoundedCornerShape(borderRadius)

    Box(
        modifier = modifier
            .alpha(if (enabled) 1f else 0.5f)
            .clip(shape)
            .background(bgColor, shape)
            .border(borderWidth, borderColor, shape)
            .clickable(enabled = enabled, onClick = onClick)
            .padding(horizontal = paddingH, vertical = paddingV)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (leadingContent != null) {
                leadingContent()
                Spacer(modifier = Modifier.width(8.dp))
            }
            Text(
                text = text,
                style = textStyle.copy(color = textColor),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            if (trailingContent != null) {
                Spacer(modifier = Modifier.width(8.dp))
                trailingContent()
            }
        }
    }
}

// ═══════════════════════════════════════════════════════
// Filter Chip
// ═══════════════════════════════════════════════════════

enum class Ux4gFilterChipSize { S, M }

/**
 * A filter chip with selected/unselected states.
 * When selected, background uses primary at 8% alpha. Text uses primary color.
 */
@Composable
fun Ux4gFilterChip(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    size: Ux4gFilterChipSize = Ux4gFilterChipSize.M,
    leadingContent: @Composable (() -> Unit)? = null,
    borderRadius: Dp = Ux4gRadius.Radius4,
    selectedBackgroundColor: Color? = null,
    unselectedBackgroundColor: Color? = null,
    selectedBorderColor: Color? = null,
    unselectedBorderColor: Color? = null,
    selectedTextColor: Color? = null,
    unselectedTextColor: Color? = null,
    selectedBorderWidth: Dp = 1.dp,
    unselectedBorderWidth: Dp = 1.dp,
) {
    val chipHeight = when (size) {
        Ux4gFilterChipSize.S -> 28.dp
        Ux4gFilterChipSize.M -> 32.dp
    }
    val paddingH = when (size) {
        Ux4gFilterChipSize.S -> 12.dp
        Ux4gFilterChipSize.M -> 16.dp
    }
    val textStyle: TextStyle = when (size) {
        Ux4gFilterChipSize.S -> defaultTypography.lM_default
        Ux4gFilterChipSize.M -> defaultTypography.lL_default
    }

    // Colors — selected bg uses primary at 8% alpha (matching Flutter .withValues(alpha: 0.08))
    val bgColor = if (selected) {
        selectedBackgroundColor ?: Ux4gColors.primary.copy(alpha = 0.08f)
    } else {
        unselectedBackgroundColor ?: Ux4gColors.neutral0 // surface
    }

    val borderColor = if (selected) {
        selectedBorderColor ?: Ux4gColors.primary
    } else {
        unselectedBorderColor ?: Ux4gColors.neutral900.copy(alpha = 0.2f) // onSurface 20%
    }

    val borderWidth = if (selected) selectedBorderWidth else unselectedBorderWidth

    // selected text uses primary; unselected uses onSurface
    val textColor = if (selected) {
        selectedTextColor ?: Ux4gColors.primary
    } else {
        unselectedTextColor ?: Ux4gColors.neutral900
    }

    val shape = RoundedCornerShape(borderRadius)

    Box(
        modifier = modifier
            .height(chipHeight)
            .alpha(if (enabled) 1f else 0.5f)
            .clip(shape)
            .background(bgColor, shape)
            .border(borderWidth, borderColor, shape)
            .clickable(enabled = enabled, onClick = onClick)
            .padding(horizontal = paddingH),
        contentAlignment = Alignment.CenterStart
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (leadingContent != null) {
                leadingContent()
                Spacer(modifier = Modifier.width(8.dp))
            }
            Text(
                text = text,
                style = textStyle.copy(color = textColor),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

// ═══════════════════════════════════════════════════════
// Input Chip
// ═══════════════════════════════════════════════════════

enum class Ux4gInputChipSize { XS, S, M }

/**
 * An input chip with a dismiss (close) button.
 * Supports 3 sizes (XS, S, M) and optional leading content.
 */
@Composable
fun Ux4gInputChip(
    text: String,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    size: Ux4gInputChipSize = Ux4gInputChipSize.M,
    leadingContent: @Composable (() -> Unit)? = null,
    borderRadius: Dp = Ux4gRadius.Radius4,
    backgroundColor: Color? = null,
    borderColor: Color? = null,
    textColor: Color? = null,
    borderWidth: Dp = 1.dp,
) {
    val chipHeight = when (size) {
        Ux4gInputChipSize.XS -> 20.dp
        Ux4gInputChipSize.S -> 28.dp
        Ux4gInputChipSize.M -> 32.dp
    }
    val paddingH = when (size) {
        Ux4gInputChipSize.XS -> 8.dp
        Ux4gInputChipSize.S -> 12.dp
        Ux4gInputChipSize.M -> 16.dp
    }
    val textStyleVal: TextStyle = when (size) {
        Ux4gInputChipSize.XS -> defaultTypography.lS_default
        Ux4gInputChipSize.S -> defaultTypography.lM_default
        Ux4gInputChipSize.M -> defaultTypography.lL_default
    }
    val iconSize = if (size == Ux4gInputChipSize.XS) 12.dp else 16.dp

    val finalBgColor = backgroundColor ?: Ux4gColors.neutral0 // surface
    val finalBorderColor = borderColor ?: Ux4gColors.neutral900.copy(alpha = 0.2f) // onSurface 20%
    val finalTextColor = textColor ?: Ux4gColors.neutral900 // onSurface

    val shape = RoundedCornerShape(borderRadius)

    Box(
        modifier = modifier
            .height(chipHeight)
            .alpha(if (enabled) 1f else 0.5f)
            .clip(shape)
            .background(finalBgColor, shape)
            .border(borderWidth, finalBorderColor, shape)
            .padding(horizontal = paddingH),
        contentAlignment = Alignment.CenterStart
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (leadingContent != null) {
                leadingContent()
                Spacer(modifier = Modifier.width(8.dp))
            }
            Text(
                text = text,
                style = textStyleVal.copy(color = finalTextColor),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.width(8.dp))
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "Dismiss",
                tint = finalTextColor.copy(alpha = 0.8f),
                modifier = Modifier
                    .size(iconSize)
                    .clickable(enabled = enabled, onClick = onDismiss)
            )
        }
    }
}

// ═══════════════════════════════════════════════════════
// Input Chip Field
// ═══════════════════════════════════════════════════════

/**
 * A field that allows adding chips via text input or a dropdown.
 * Contains an input/dropdown and displays added chips below.
 */
@Composable
fun Ux4gInputChipField(
    value: String,
    onValueChange: (String) -> Unit,
    onAddChip: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = "Please select..",
    leadingIcon: @Composable (() -> Unit)? = null,
    isDropdown: Boolean = false,
    dropdownOptions: List<String> = emptyList(),
    chips: List<@Composable () -> Unit> = emptyList(),
    arrangement: Ux4gChipGroupArrangement = Ux4gChipGroupArrangement.Horizontal,
) {
    Column(modifier = modifier) {
        if (isDropdown) {
            // Dropdown field
            var expanded by remember { mutableStateOf(false) }
            Box(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(Ux4gRadius.Radius4))
                        .border(
                            1.dp,
                            Ux4gColors.neutral900.copy(alpha = 0.2f),
                            RoundedCornerShape(Ux4gRadius.Radius4)
                        )
                        .background(Ux4gColors.neutral0)
                        .clickable { expanded = true }
                        .padding(horizontal = 12.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (leadingIcon != null) {
                        leadingIcon()
                        Spacer(modifier = Modifier.width(8.dp))
                    }
                    Text(
                        text = if (value.isEmpty()) placeholder else value,
                        style = defaultTypography.bM_default.copy(
                            color = if (value.isEmpty()) Ux4gColors.neutral400 else Ux4gColors.neutral900
                        ),
                        modifier = Modifier.weight(1f)
                    )
                    Icon(
                        imageVector = Icons.Default.ArrowDropDown,
                        contentDescription = "Dropdown",
                        tint = Ux4gColors.neutral600
                    )
                }
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    dropdownOptions.forEach { option ->
                        DropdownMenuItem(
                            text = { Text(option, style = defaultTypography.bM_default) },
                            onClick = {
                                onAddChip(option)
                                onValueChange("")
                                expanded = false
                            }
                        )
                    }
                }
            }
        } else {
            // Text input field with add button
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(Ux4gRadius.Radius4))
                    .border(
                        1.dp,
                        Ux4gColors.neutral900.copy(alpha = 0.2f),
                        RoundedCornerShape(Ux4gRadius.Radius4)
                    )
                    .background(Ux4gColors.neutral0)
                    .padding(horizontal = 12.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (leadingIcon != null) {
                    leadingIcon()
                    Spacer(modifier = Modifier.width(8.dp))
                }
                BasicTextField(
                    value = value,
                    onValueChange = onValueChange,
                    textStyle = defaultTypography.bM_default.copy(color = Ux4gColors.neutral900),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            if (value.isNotBlank()) {
                                onAddChip(value.trim())
                                onValueChange("")
                            }
                        }
                    ),
                    decorationBox = { innerTextField ->
                        Box(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.CenterStart
                        ) {
                            if (value.isEmpty()) {
                                Text(
                                    text = placeholder,
                                    style = defaultTypography.bM_default.copy(color = Ux4gColors.neutral400)
                                )
                            }
                            innerTextField()
                        }
                    },
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add chip",
                    tint = Ux4gColors.neutral600,
                    modifier = Modifier
                        .size(20.dp)
                        .clickable {
                            if (value.isNotBlank()) {
                                onAddChip(value.trim())
                                onValueChange("")
                            }
                        }
                )
            }
        }

        // Chips display
        if (chips.isNotEmpty()) {
            Spacer(modifier = Modifier.height(8.dp))
            Ux4gChipGroup(
                chips = chips,
                arrangement = arrangement
            )
        }
    }
}

// ═══════════════════════════════════════════════════════
// Chip Group
// ═══════════════════════════════════════════════════════

enum class Ux4gChipGroupArrangement { Horizontal, Wrap }

/**
 * A group/layout container for chips.
 * Supports horizontal scrolling or wrapping arrangement.
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun Ux4gChipGroup(
    chips: List<@Composable () -> Unit>,
    modifier: Modifier = Modifier,
    arrangement: Ux4gChipGroupArrangement = Ux4gChipGroupArrangement.Horizontal,
    spacing: Dp = 8.dp,
    runSpacing: Dp = 8.dp,
) {
    when (arrangement) {
        Ux4gChipGroupArrangement.Horizontal -> {
            Row(
                modifier = modifier.horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(spacing)
            ) {
                chips.forEach { chip ->
                    chip()
                }
            }
        }
        Ux4gChipGroupArrangement.Wrap -> {
            FlowRow(
                modifier = modifier,
                horizontalArrangement = Arrangement.spacedBy(spacing),
                verticalArrangement = Arrangement.spacedBy(runSpacing)
            ) {
                chips.forEach { chip ->
                    chip()
                }
            }
        }
    }
}

// ═══════════════════════════════════════════════════════
// Previews
// ═══════════════════════════════════════════════════════

@Preview(name = "Chips - Light", showBackground = true, backgroundColor = 0xFFFFFFFF)
@Preview(name = "Chips - Dark", uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true, backgroundColor = 0xFF121212)
@Composable
private fun Ux4gChipsPreview() {
    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Choice Chips
        Text("Choice Chips (Medium & Small, with leading/trailing)", style = defaultTypography.lL_strong)
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Ux4gChoiceChip(
                text = "Selected",
                selected = true,
                onClick = {},
                leadingContent = {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = Ux4gColors.neutral50
                    )
                }
            )
            Ux4gChoiceChip(text = "Unselected", selected = false, onClick = {})
            Ux4gChoiceChip(
                text = "Small",
                selected = true,
                onClick = {},
                size = Ux4gChoiceChipSize.S,
                trailingContent = {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = null,
                        modifier = Modifier.size(12.dp),
                        tint = Ux4gColors.neutral50
                    )
                }
            )
        }

        // Filter Chips
        Text("Filter Chips (Medium & Small)", style = defaultTypography.lL_strong)
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Ux4gFilterChip(text = "Selected", selected = true, onClick = {})
            Ux4gFilterChip(text = "Unselected", selected = false, onClick = {})
            Ux4gFilterChip(text = "Small Filter", selected = true, onClick = {}, size = Ux4gFilterChipSize.S)
        }

        // Input Chips
        Text("Input Chips (Medium, Small, XS)", style = defaultTypography.lL_strong)
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Ux4gInputChip(
                text = "Medium",
                onDismiss = {},
                leadingContent = {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = Ux4gColors.neutral700
                    )
                }
            )
            Ux4gInputChip(text = "Small", onDismiss = {}, size = Ux4gInputChipSize.S)
            Ux4gInputChip(text = "XS Chip", onDismiss = {}, size = Ux4gInputChipSize.XS)
        }

        // Disabled
        Text("Disabled Chips", style = defaultTypography.lL_strong)
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Ux4gChoiceChip(text = "Disabled Choice", selected = true, onClick = {}, enabled = false)
            Ux4gFilterChip(text = "Disabled Filter", selected = true, onClick = {}, enabled = false)
            Ux4gInputChip(text = "Disabled Input", onDismiss = {}, enabled = false)
        }
    }
}
