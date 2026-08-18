package com.ux4g.designsystemv1.components.dropdowns

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.res.painterResource
import com.ux4g.designsystemv1.components.Ux4gCheckbox
import com.ux4g.designsystemv1.components.Ux4gCheckboxSize
import com.ux4g.designsystemv1.components.Ux4gDivider
import com.ux4g.designsystemv1.components.Ux4gInputChip
import com.ux4g.designsystemv1.components.Ux4gInputChipSize
import com.ux4g.designsystemv1.components.buttons.Ux4gButton
import com.ux4g.designsystemv1.components.buttons.Ux4gIconButton
import com.ux4g.designsystemv1.components.buttons.Ux4gIconButtonSize
import com.ux4g.designsystemv1.components.buttons.Ux4gIconButtonStyle
import com.ux4g.designsystemv1.foundation.Ux4gColors
import com.ux4g.designsystemv1.foundation.Ux4gIcon
import com.ux4g.designsystemv1.foundation.Ux4gIconComponent
import com.ux4g.designsystemv1.foundation.Ux4gRadius
import com.ux4g.designsystemv1.foundation.defaultTypography

@Composable
private fun DropdownOptionLeadingIcon(
    icon: Any?,
    tint: Color,
    modifier: Modifier = Modifier.size(20.dp)
) {
    when (icon) {
        is ImageVector -> Icon(imageVector = icon, contentDescription = null, tint = tint, modifier = modifier)
        is Ux4gIcon -> Ux4gIconComponent(icon = icon, contentDescription = null, tint = tint, modifier = modifier)
        is Int -> Icon(painter = painterResource(id = icon), contentDescription = null, tint = tint, modifier = modifier)
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Enums & Option Models
// ─────────────────────────────────────────────────────────────────────────────

enum class Ux4gDropdownFilterType {
    Contains,
    StartsWith,
    StartsWithPerTerm
}

enum class Ux4gDropdownSize {
    S,
    M,
    L
}
typealias Ux4gSelectionDropdownSize = Ux4gDropdownSize

enum class Ux4gDropdownMode {
    Single,
    Multi;

    companion object {
        val SingleSelect = Single
        val MultiSelect = Multi
    }
}
typealias Ux4gSelectionDropdownMode = Ux4gDropdownMode

enum class Ux4gDropdownStatus {
    Default,
    Error,
    Disabled,
    Warning,
    Success
}
typealias Ux4gSelectionDropdownStatus = Ux4gDropdownStatus

data class Ux4gDropdownOption(
    val id: String,
    val label: String,
    val leadingIcon: Any? = null
)

// ─────────────────────────────────────────────────────────────────────────────
// Selection Dropdown
// ─────────────────────────────────────────────────────────────────────────────

/**
 * A highly customizable Selection Dropdown component supporting single and multi-select modes,
 * search filtering, chips display, validation statuses (Error, Warning, Success, Disabled),
 * sizes, and label/description styling.
 *
 * Matches UX4G Flutter design specifications 1:1.
 */
@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun Ux4gSelectionDropdown(
    options: List<Ux4gDropdownOption>,
    selectedOptionIds: List<String>,
    onSelectionChange: (List<String>) -> Unit,
    modifier: Modifier = Modifier,
    label: String? = null,
    isRequired: Boolean = false,
    description: String? = null,
    placeholder: String = "Please select..",
    size: Ux4gDropdownSize = Ux4gDropdownSize.M,
    mode: Ux4gDropdownMode = Ux4gDropdownMode.Single,
    status: Ux4gDropdownStatus = Ux4gDropdownStatus.Default,
    searchEnabled: Boolean = false,
    filterType: Ux4gDropdownFilterType = Ux4gDropdownFilterType.Contains,
    leadingIcon: ImageVector? = null,
    labelTextStyle: TextStyle? = null,
    valueTextStyle: TextStyle? = null
) {
    var expanded by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }

    val filteredOptions = remember(options, searchQuery, filterType, searchEnabled) {
        if (!searchEnabled || searchQuery.isEmpty()) options
        else {
            val query = searchQuery.trim().lowercase()
            if (query.isEmpty()) options
            else {
                options.filter { option ->
                    val labelLower = option.label.lowercase()
                    when (filterType) {
                        Ux4gDropdownFilterType.Contains -> labelLower.contains(query)
                        Ux4gDropdownFilterType.StartsWith -> labelLower.startsWith(query)
                        Ux4gDropdownFilterType.StartsWithPerTerm -> {
                            val queryTerms = query.split(" ").filter { it.isNotEmpty() }
                            val labelTerms = labelLower.split(" ").filter { it.isNotEmpty() }
                            queryTerms.all { qTerm ->
                                labelTerms.any { lTerm -> lTerm.startsWith(qTerm) }
                            }
                        }
                    }
                }
            }
        }
    }

    val (minHeight, verticalPadding) = when (size) {
        Ux4gDropdownSize.S -> 32.dp to 6.dp
        Ux4gDropdownSize.M -> 40.dp to 8.dp
        Ux4gDropdownSize.L -> 48.dp to 12.dp
    }

    val defaultTextStyle = when (size) {
        Ux4gDropdownSize.S -> defaultTypography.lM_default
        Ux4gDropdownSize.M -> defaultTypography.lL_default
        Ux4gDropdownSize.L -> defaultTypography.lXL_default
    }

    val onSurface = Ux4gColors.neutral900
    val defaultBgColor = Ux4gColors.neutral0
    val defaultBorderColor = Ux4gColors.neutral200

    val borderColor = when (status) {
        Ux4gDropdownStatus.Disabled -> onSurface.copy(alpha = 0.2f)
        Ux4gDropdownStatus.Error -> Ux4gColors.red600
        Ux4gDropdownStatus.Warning -> Ux4gColors.orange600
        Ux4gDropdownStatus.Success -> Ux4gColors.green600
        Ux4gDropdownStatus.Default -> if (expanded) Ux4gColors.primary else defaultBorderColor
    }

    val descriptionColor = when (status) {
        Ux4gDropdownStatus.Error -> Ux4gColors.red600
        Ux4gDropdownStatus.Warning -> Ux4gColors.orange600
        Ux4gDropdownStatus.Success -> Ux4gColors.green600
        else -> onSurface.copy(alpha = 0.7f)
    }

    fun handleSelection(id: String) {
        if (mode == Ux4gDropdownMode.Single) {
            onSelectionChange(listOf(id))
            expanded = false
            searchQuery = ""
        } else {
            val newSelection = if (selectedOptionIds.contains(id)) {
                selectedOptionIds - id
            } else {
                selectedOptionIds + id
            }
            onSelectionChange(newSelection)
        }
    }

    Column(modifier = modifier.fillMaxWidth()) {
        // Label
        if (label != null) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 4.dp)
            ) {
                Text(
                    text = label,
                    style = (labelTextStyle ?: defaultTypography.lL_default).copy(
                        color = if (status == Ux4gDropdownStatus.Disabled) onSurface.copy(alpha = 0.38f) else onSurface
                    )
                )
                if (isRequired) {
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "*",
                        style = defaultTypography.bS_strong.copy(color = Ux4gColors.red600)
                    )
                }
            }
        }

        // Trigger Box & Exposed Dropdown Menu
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = {
                if (status != Ux4gDropdownStatus.Disabled) {
                    expanded = it
                    if (!it) searchQuery = ""
                }
            }
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = minHeight)
                    .menuAnchor(
                        type = MenuAnchorType.PrimaryNotEditable,
                        enabled = status != Ux4gDropdownStatus.Disabled
                    )
                    .clip(RoundedCornerShape(Ux4gRadius.Radius8))
                    .background(
                        if (status == Ux4gDropdownStatus.Disabled) onSurface.copy(alpha = 0.04f)
                        else defaultBgColor
                    )
                    .border(
                        1.dp,
                        borderColor,
                        RoundedCornerShape(Ux4gRadius.Radius8)
                    )
                    .padding(horizontal = 12.dp, vertical = verticalPadding),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (leadingIcon != null) {
                    Icon(
                        imageVector = leadingIcon,
                        contentDescription = null,
                        tint = onSurface.copy(alpha = 0.6f),
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                }

                Box(modifier = Modifier.weight(1f)) {
                    if (selectedOptionIds.isEmpty()) {
                        Text(
                            text = placeholder,
                            style = defaultTextStyle.copy(color = Ux4gColors.neutral500)
                        )
                    } else if (mode == Ux4gDropdownMode.Single) {
                        val selectedOpt = options.find { it.id == selectedOptionIds.firstOrNull() }
                        Text(
                            text = selectedOpt?.label ?: "",
                            style = valueTextStyle ?: defaultTextStyle.copy(color = onSurface)
                        )
                    } else {
                        // Multi Select Chips
                        FlowRow(
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                            verticalArrangement = Arrangement.spacedBy(4.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            selectedOptionIds.forEach { id ->
                                val selectedOpt = options.find { it.id == id }
                                if (selectedOpt != null) {
                                    Ux4gInputChip(
                                        text = selectedOpt.label,
                                        onDismiss = {
                                            if (status != Ux4gDropdownStatus.Disabled) {
                                                onSelectionChange(selectedOptionIds - id)
                                            }
                                        },
                                        size = if (size == Ux4gDropdownSize.S) Ux4gInputChipSize.XS else Ux4gInputChipSize.S
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.width(8.dp))
                Icon(
                    imageVector = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                    contentDescription = "Toggle Dropdown",
                    tint = onSurface.copy(alpha = 0.6f),
                    modifier = Modifier.size(20.dp)
                )
            }

            // Dropdown Menu Popover
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = {
                    expanded = false
                    searchQuery = ""
                },
                modifier = Modifier
                    .background(Ux4gColors.neutral0)
                    .border(1.dp, Ux4gColors.neutral900.copy(alpha = 0.12f), RoundedCornerShape(Ux4gRadius.Radius8))
            ) {
                // Search Field inside dropdown
                if (searchEnabled) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp, vertical = 8.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .border(1.dp, Ux4gColors.neutral300, RoundedCornerShape(8.dp))
                                .background(Ux4gColors.neutral0)
                                .padding(horizontal = 10.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = "Search",
                                tint = Ux4gColors.neutral500,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            BasicTextField(
                                value = searchQuery,
                                onValueChange = { searchQuery = it },
                                textStyle = defaultTypography.bM_default.copy(color = Ux4gColors.neutral900),
                                singleLine = true,
                                cursorBrush = SolidColor(Ux4gColors.primary),
                                decorationBox = { innerTextField ->
                                    if (searchQuery.isEmpty()) {
                                        Text(
                                            text = "Start typing..",
                                            style = defaultTypography.bM_default.copy(color = Ux4gColors.neutral500)
                                        )
                                    }
                                    innerTextField()
                                },
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                }

                // Options List
                if (filteredOptions.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No options found",
                            style = defaultTypography.bS_default.copy(color = Ux4gColors.neutral500)
                        )
                    }
                } else {
                    filteredOptions.forEach { option ->
                        val isSelected = selectedOptionIds.contains(option.id)
                        val selectedBg = if (isSelected) Ux4gColors.primary50 else Color.Transparent
                        val activeColor = if (isSelected) Ux4gColors.primary600 else Ux4gColors.neutral900

                        DropdownMenuItem(
                            text = {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    if (mode == Ux4gDropdownMode.Multi) {
                                        Ux4gCheckbox(
                                            value = isSelected,
                                            onValueChange = { handleSelection(option.id) },
                                            size = Ux4gCheckboxSize.Medium
                                        )
                                        Spacer(modifier = Modifier.width(12.dp))
                                    } else if (option.leadingIcon != null) {
                                        DropdownOptionLeadingIcon(
                                            icon = option.leadingIcon,
                                            tint = if (isSelected) activeColor else onSurface.copy(alpha = 0.6f),
                                            modifier = Modifier.size(20.dp)
                                        )
                                        Spacer(modifier = Modifier.width(12.dp))
                                    }

                                    Text(
                                        text = option.label,
                                        style = defaultTypography.bS_default.copy(
                                            color = if (isSelected) activeColor else onSurface,
                                            fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal
                                        ),
                                        modifier = Modifier.weight(1f)
                                    )

                                    if (mode == Ux4gDropdownMode.Single && isSelected) {
                                        Icon(
                                            imageVector = Icons.Default.Check,
                                            contentDescription = "Selected",
                                            tint = Ux4gColors.primary,
                                            modifier = Modifier.size(20.dp)
                                        )
                                    }
                                }
                            },
                            modifier = Modifier.background(selectedBg),
                            onClick = { handleSelection(option.id) }
                        )
                    }
                }
            }
        }

        // Description / Status Icon
        if (description != null) {
            Spacer(modifier = Modifier.height(4.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                val statusIcon = when (status) {
                    Ux4gDropdownStatus.Error -> Icons.Default.Error
                    Ux4gDropdownStatus.Warning -> Icons.Default.Warning
                    Ux4gDropdownStatus.Success -> Icons.Default.CheckCircle
                    else -> Icons.Outlined.Info
                }
                Icon(
                    imageVector = statusIcon,
                    contentDescription = null,
                    tint = descriptionColor,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = description,
                    style = defaultTypography.lS_default.copy(color = descriptionColor)
                )
            }
        }
    }
}

// ═══════════════════════════════════════════════════════
// Previews
// ═══════════════════════════════════════════════════════

@Preview(name = "Selection Dropdown - Light", showBackground = true, backgroundColor = 0xFFFFFFFF)
@Preview(name = "Selection Dropdown - Dark", uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true, backgroundColor = 0xFF121212)
@Composable
private fun Ux4gSelectionDropdownPreview() {
    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        val sampleOptions = listOf(
            Ux4gDropdownOption("1", "Option 1 (English)"),
            Ux4gDropdownOption("2", "Option 2 (Hindi)"),
            Ux4gDropdownOption("3", "Option 3 (Spanish)"),
            Ux4gDropdownOption("4", "Option 4 (French)")
        )

        // Single Select
        var singleSelected by remember { mutableStateOf(listOf("1")) }
        Ux4gSelectionDropdown(
            label = "Language Preference",
            isRequired = true,
            options = sampleOptions,
            selectedOptionIds = singleSelected,
            onSelectionChange = { singleSelected = it },
            searchEnabled = true,
            description = "Select your preferred application language"
        )

        // Multi Select
        var multiSelected by remember { mutableStateOf(listOf("1", "2")) }
        Ux4gSelectionDropdown(
            label = "Target Regions",
            mode = Ux4gDropdownMode.Multi,
            options = sampleOptions,
            selectedOptionIds = multiSelected,
            onSelectionChange = { multiSelected = it },
            searchEnabled = true,
            description = "Choose one or more target regions"
        )

        // Status Variants
        Ux4gSelectionDropdown(
            label = "Error State",
            options = sampleOptions,
            selectedOptionIds = emptyList(),
            onSelectionChange = {},
            status = Ux4gDropdownStatus.Error,
            description = "This field is required"
        )

        Ux4gSelectionDropdown(
            label = "Warning State",
            options = sampleOptions,
            selectedOptionIds = listOf("2"),
            onSelectionChange = {},
            status = Ux4gDropdownStatus.Warning,
            description = "Selected region has partial service availability"
        )

        Ux4gSelectionDropdown(
            label = "Success State",
            options = sampleOptions,
            selectedOptionIds = listOf("1"),
            onSelectionChange = {},
            status = Ux4gDropdownStatus.Success,
            description = "Region confirmed"
        )

        Ux4gSelectionDropdown(
            label = "Disabled State",
            options = sampleOptions,
            selectedOptionIds = listOf("1"),
            onSelectionChange = {},
            status = Ux4gDropdownStatus.Disabled,
            description = "Selection locked by administrator"
        )
    }
}
