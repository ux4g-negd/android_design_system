package com.ux4g.designsystemv1.components.dropdowns

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.state.ToggleableState
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ux4g.designsystemv1.components.Ux4gCheckbox
import com.ux4g.designsystemv1.components.Ux4gCheckboxSize
import com.ux4g.designsystemv1.components.chips.Ux4gInputChip
import com.ux4g.designsystemv1.components.chips.Ux4gInputChipSize
import com.ux4g.designsystemv1.foundation.Ux4gIcon
import com.ux4g.designsystemv1.foundation.Ux4gIconComponent
import com.ux4g.designsystemv1.foundation.Ux4gIcons
import com.ux4g.designsystemv1.foundation.Ux4gColors
import com.ux4g.designsystemv1.foundation.Ux4gRadius
import com.ux4g.designsystemv1.foundation.Ux4gSpace
import com.ux4g.designsystemv1.theme.Ux4gTheme

enum class Ux4gSelectionDropdownSize {
    S,
    M,
    L
}

enum class Ux4gSelectionDropdownMode {
    SingleSelect,
    MultiSelect
}

enum class Ux4gDropdownFilterType {
    Contains,
    StartsWith,
    StartsWithPerTerm
}

enum class Ux4gSelectionDropdownStatus {
    Default,
    Error,
    Disabled
}

data class Ux4gDropdownOption(val id: String, val label: String, val leadingIcon: Ux4gIcon? = null)

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun Ux4gSelectionDropdown(
        options: List<Ux4gDropdownOption>,
        selectedOptionIds: List<String>,
        onSelectionChange: (List<String>) -> Unit,
        modifier: Modifier = Modifier,
        label: String? = null,
        description: String? = null,
        placeholder: String = "Please select..",
        size: Ux4gSelectionDropdownSize = Ux4gSelectionDropdownSize.M,
        mode: Ux4gSelectionDropdownMode = Ux4gSelectionDropdownMode.SingleSelect,
        status: Ux4gSelectionDropdownStatus = Ux4gSelectionDropdownStatus.Default,
        searchEnabled: Boolean = false,
        filterType: Ux4gDropdownFilterType = Ux4gDropdownFilterType.Contains,
) {
    var expanded by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(expanded) {
        if (expanded && searchEnabled) {
            focusRequester.requestFocus()
        }
    }

    val filteredOptions = remember(options, searchQuery, filterType, searchEnabled) {
        if (!searchEnabled || searchQuery.isEmpty()) options
        else {
            val query = searchQuery.trim()
            if (query.isEmpty()) options
            else {
                options.filter { option ->
                    when (filterType) {
                        Ux4gDropdownFilterType.Contains ->
                            option.label.contains(query, ignoreCase = true)
                        Ux4gDropdownFilterType.StartsWith ->
                            option.label.startsWith(query, ignoreCase = true)
                        Ux4gDropdownFilterType.StartsWithPerTerm -> {
                            val queryTerms = query.split(" ").filter { it.isNotEmpty() }
                            val labelTerms = option.label.split(" ").filter { it.isNotEmpty() }
                            queryTerms.all { qTerm ->
                                labelTerms.any { lTerm -> lTerm.startsWith(qTerm, ignoreCase = true) }
                            }
                        }
                    }
                }
            }
        }
    }

    val (minHeight, verticalPadding) =
            when (size) {
                Ux4gSelectionDropdownSize.S -> 32.dp to 6.dp
                Ux4gSelectionDropdownSize.M -> 40.dp to 8.dp
                Ux4gSelectionDropdownSize.L -> 48.dp to 12.dp
            }

    val textStyle =
            when (size) {
                Ux4gSelectionDropdownSize.S -> Ux4gTheme.typography.lM_default
                Ux4gSelectionDropdownSize.M -> Ux4gTheme.typography.lL_default
                Ux4gSelectionDropdownSize.L -> Ux4gTheme.typography.lXL_default
            }

    val borderColor =
            when (status) {
                Ux4gSelectionDropdownStatus.Disabled ->
                        Ux4gTheme.colorScheme.onSurface.copy(alpha = 0.2f)
                Ux4gSelectionDropdownStatus.Error -> Ux4gTheme.colorScheme.error
                Ux4gSelectionDropdownStatus.Default ->
                        if (expanded) Ux4gTheme.colorScheme.primary
                        else Ux4gTheme.colorScheme.onSurface.copy(alpha = 0.2f)
            }

    val backgroundColor =
            when (status) {
                Ux4gSelectionDropdownStatus.Disabled ->
                        Ux4gTheme.colorScheme.onSurface.copy(alpha = 0.04f)
                else -> Ux4gTheme.colorScheme.surface
            }

    val descriptionColor =
            if (status == Ux4gSelectionDropdownStatus.Error) Ux4gTheme.colorScheme.error
            else Ux4gTheme.colorScheme.onSurface.copy(alpha = 0.7f)
    val descriptionIcon =
            if (status == Ux4gSelectionDropdownStatus.Error) Ux4gIcons.error
            else Ux4gIcons.info_circle

    Column(modifier = modifier.fillMaxWidth()) {
        if (label != null) {
            Text(
                    text = label,
                    style = Ux4gTheme.typography.hS_default,
                    color =
                            if (status == Ux4gSelectionDropdownStatus.Disabled)
                                    Ux4gTheme.colorScheme.onSurface.copy(alpha = 0.38f)
                            else Ux4gTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(bottom = Ux4gSpace.Space4)
            )
        }

        ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = {
                    if (status != Ux4gSelectionDropdownStatus.Disabled) expanded = it
                }
        ) {
            Row(
                    modifier =
                            Modifier.fillMaxWidth()
                                    .heightIn(min = minHeight)
                                    .menuAnchor(
                                            type = if (searchEnabled) MenuAnchorType.PrimaryEditable else MenuAnchorType.PrimaryNotEditable,
                                            enabled = status != Ux4gSelectionDropdownStatus.Disabled
                                    )
                                    .background(
                                            backgroundColor,
                                            RoundedCornerShape(Ux4gRadius.Radius8)
                                    )
                                    .border(
                                            1.dp,
                                            borderColor,
                                            RoundedCornerShape(Ux4gRadius.Radius8)
                                    )
                                    .padding(horizontal = 12.dp, vertical = verticalPadding),
                    verticalAlignment = Alignment.CenterVertically
            ) {
                if (selectedOptionIds.isEmpty() && (!searchEnabled || !expanded)) {
                    Row(
                        modifier = Modifier.weight(1f),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (searchEnabled && expanded) {
                            Ux4gIconComponent(
                                icon = Ux4gIcons.Search,
                                contentDescription = null,
                                modifier = Modifier.size(20.dp),
                                tint = Ux4gTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                        }
                        Text(
                            text = placeholder,
                            style = textStyle,
                            color = Ux4gTheme.colorScheme.onSurface.copy(alpha = 0.38f),
                            modifier = Modifier.weight(1f)
                        )
                    }
                } else {
                    if (mode == Ux4gSelectionDropdownMode.SingleSelect) {
                        val selectedOpt = options.find { it.id == selectedOptionIds.firstOrNull() }
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.weight(1f)
                        ) {
                            if (searchEnabled && expanded) {
                                Ux4gIconComponent(
                                    icon = Ux4gIcons.Search,
                                    contentDescription = null,
                                    modifier = Modifier.size(20.dp),
                                    tint = Ux4gTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                BasicTextField(
                                    value = searchQuery,
                                    onValueChange = { searchQuery = it },
                                    modifier = Modifier.weight(1f).focusRequester(focusRequester),
                                    textStyle = textStyle.copy(color = Ux4gTheme.colorScheme.onSurface),
                                    cursorBrush = SolidColor(Ux4gTheme.colorScheme.primary),
                                    decorationBox = { innerTextField ->
                                        if (searchQuery.isEmpty()) {
                                            Text(
                                                text = "Start typing..",
                                                style = textStyle,
                                                color = Ux4gTheme.colorScheme.onSurface.copy(alpha = 0.38f)
                                            )
                                        }
                                        innerTextField()
                                    }
                                )
                            } else {
                                if (selectedOpt?.leadingIcon != null) {
                                    Ux4gIconComponent(
                                        icon = selectedOpt.leadingIcon,
                                        contentDescription = null,
                                        modifier = Modifier.size(20.dp),
                                        tint = Ux4gTheme.colorScheme.onSurface
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                }
                                Text(
                                    text = selectedOpt?.label ?: "",
                                    style = textStyle,
                                    color = Ux4gTheme.colorScheme.onSurface,
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }
                    } else {
                        // Multi Select Mode
                        FlowRow(
                            modifier = Modifier.weight(1f),
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            selectedOptionIds.forEach { id ->
                                val selectedOpt = options.find { it.id == id }
                                if (selectedOpt != null) {
                                    Ux4gInputChip(
                                        text = selectedOpt.label,
                                        onDismiss = {
                                            if (status != Ux4gSelectionDropdownStatus.Disabled) {
                                                onSelectionChange(selectedOptionIds - id)
                                            }
                                        },
                                        size = if (size == Ux4gSelectionDropdownSize.S) Ux4gInputChipSize.S else Ux4gInputChipSize.M,
                                        leadingContent = selectedOpt.leadingIcon?.let { icon ->
                                            {
                                                Ux4gIconComponent(
                                                    icon = icon,
                                                    contentDescription = null,
                                                    modifier = Modifier.size(16.dp),
                                                    tint = Ux4gTheme.colorScheme.onSurface
                                                )
                                            }
                                        }
                                    )
                                }
                            }
                            if (searchEnabled && expanded) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.width(IntrinsicSize.Min).defaultMinSize(minWidth = 100.dp)
                                ) {
                                    Ux4gIconComponent(
                                        icon = Ux4gIcons.Search,
                                        contentDescription = null,
                                        modifier = Modifier.size(16.dp),
                                        tint = Ux4gTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    BasicTextField(
                                        value = searchQuery,
                                        onValueChange = { searchQuery = it },
                                        modifier = Modifier.widthIn(min = 80.dp).focusRequester(focusRequester),
                                        textStyle = textStyle.copy(color = Ux4gTheme.colorScheme.onSurface),
                                        cursorBrush = SolidColor(Ux4gTheme.colorScheme.primary),
                                        decorationBox = { innerTextField ->
                                            if (searchQuery.isEmpty()) {
                                                Text(
                                                    text = "Start typing..",
                                                    style = textStyle,
                                                    color = Ux4gTheme.colorScheme.onSurface.copy(alpha = 0.38f)
                                                )
                                            }
                                            innerTextField()
                                        }
                                    )
                                }
                            }
                        }
                    }
                }

                Ux4gIconComponent(
                        icon =
                                Ux4gIcon.Vector(
                                        if (expanded) Icons.Filled.KeyboardArrowUp
                                        else Icons.Filled.KeyboardArrowDown
                                ),
                        contentDescription = null,
                        modifier = Modifier.size(20.dp),
                        tint = Ux4gTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            }

            ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { 
                        expanded = false
                        searchQuery = ""
                    },
                    modifier =
                            Modifier.background(Ux4gTheme.colorScheme.surface).exposedDropdownSize()
            ) {
                filteredOptions.forEach { option ->
                    val isSelected = selectedOptionIds.contains(option.id)
                    val bg =
                            if (isSelected && mode == Ux4gSelectionDropdownMode.SingleSelect)
                                    Ux4gColors.primary50
                            else Color.Transparent

                    DropdownMenuItem(
                            text = {
                                Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        verticalAlignment = Alignment.CenterVertically
                                ) {
                                    if (mode == Ux4gSelectionDropdownMode.MultiSelect) {
                                        Ux4gCheckbox(
                                            state =
                                                if (isSelected) ToggleableState.On
                                                else ToggleableState.Off,
                                            onClick = {
                                                val newIds =
                                                    if (isSelected) {
                                                        selectedOptionIds - option.id
                                                    } else {
                                                        selectedOptionIds + option.id
                                                    }
                                                onSelectionChange(newIds)
                                                // Does not auto-close for multi-select
                                            },
                                            label = option.label,
                                            size = Ux4gCheckboxSize.Medium
                                        )
                                    } else {
                                        if (option.leadingIcon != null) {
                                            Ux4gIconComponent(
                                                    icon = option.leadingIcon,
                                                    contentDescription = null,
                                                    modifier = Modifier.size(20.dp),
                                                    tint =
                                                            if (isSelected)
                                                                    Ux4gTheme.colorScheme.primary
                                                            else Ux4gTheme.colorScheme.onSurface
                                            )
                                            Spacer(modifier = Modifier.width(8.dp))
                                        }

                                        Text(
                                                text = option.label,
                                                style = Ux4gTheme.typography.lL_default,
                                                color =
                                                        if (isSelected)
                                                                Ux4gTheme.colorScheme.primary
                                                        else Ux4gTheme.colorScheme.onSurface,
                                                modifier = Modifier.weight(1f)
                                        )

                                        if (isSelected) {
                                            Ux4gIconComponent(
                                                    icon = Ux4gIcons.Check,
                                                    contentDescription = null,
                                                    modifier = Modifier.size(20.dp),
                                                    tint = Ux4gTheme.colorScheme.primary
                                            )
                                        }
                                    }
                                }
                            },
                            modifier = Modifier.background(bg),
                            onClick = {
                                if (mode == Ux4gSelectionDropdownMode.SingleSelect) {
                                    onSelectionChange(listOf(option.id))
                                    expanded = false
                                    searchQuery = ""
                                } else {
                                    val newIds =
                                            if (isSelected) {
                                                selectedOptionIds - option.id
                                            } else {
                                                selectedOptionIds + option.id
                                            }
                                    onSelectionChange(newIds)
                                }
                            }
                    )
                }
            }
        }

        if (description != null) {
            Row(
                    modifier = Modifier.padding(top = 4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Ux4gIconComponent(
                        icon = descriptionIcon,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = descriptionColor
                )
                Text(
                        text = description,
                        style = Ux4gTheme.typography.lS_default,
                        color = descriptionColor
                )
            }
        }
    }
}

// -----------------------------------------------------------------------------------------
// Previews and Examples
// -----------------------------------------------------------------------------------------

@Preview(name = "Light", showBackground = true)
@Preview(name = "Dark", uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
private fun Ux4gSelectionDropdownPreview() {
    Ux4gTheme {
        Surface(color = Ux4gTheme.colorScheme.background) {
            Column(
                    modifier = Modifier.fillMaxWidth().padding(24.dp),
                    verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                // Mock dropdown options
                val mockOptions =
                        listOf(
                                Ux4gDropdownOption("opt_1", "Option A"),
                                Ux4gDropdownOption("opt_2", "Selected option"),
                                Ux4gDropdownOption(
                                        "opt_3",
                                        "Option C",
                                        leadingIcon = Ux4gIcons.Person
                                ),
                                Ux4gDropdownOption("opt_4", "Option D")
                        )

                var singleSelected by remember { mutableStateOf(listOf("opt_3")) }
                var singleSelectedError by remember { mutableStateOf(listOf("opt_3")) }
                var multiSelected by remember { mutableStateOf(listOf("opt_2", "opt_3")) }

                Text("Single Select Configuration", style = Ux4gTheme.typography.hS_default)
                Ux4gSelectionDropdown(
                        options = mockOptions,
                        selectedOptionIds = singleSelected,
                        onSelectionChange = { singleSelected = it },
                        label = "Please select (Single)",
                        description = "Description message under field",
                        mode = Ux4gSelectionDropdownMode.SingleSelect
                )

                Text("Single Select - Error variant", style = Ux4gTheme.typography.hS_default)
                Ux4gSelectionDropdown(
                        options = mockOptions,
                        selectedOptionIds = singleSelectedError,
                        onSelectionChange = { singleSelectedError = it },
                        label = "Error Selection",
                        description = "Error message",
                        status = Ux4gSelectionDropdownStatus.Error,
                        mode = Ux4gSelectionDropdownMode.SingleSelect
                )

                Text("Multi Select Configuration", style = Ux4gTheme.typography.hS_default)
                Ux4gSelectionDropdown(
                        options = mockOptions,
                        selectedOptionIds = multiSelected,
                        onSelectionChange = { multiSelected = it },
                        label = "Please select (Multi)",
                        description = "Select multiple options",
                        mode = Ux4gSelectionDropdownMode.MultiSelect
                )
            }
        }
    }
}

@Preview(name = "Searchable", showBackground = true)
@Composable
private fun Ux4gSelectionDropdownSearchPreview() {
    Ux4gTheme {
        Surface(color = Ux4gTheme.colorScheme.background) {
            Column(
                    modifier = Modifier.fillMaxWidth().padding(24.dp),
                    verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                val stateOptions = listOf(
                    Ux4gDropdownOption("ap", "Andhra Pradesh"),
                    Ux4gDropdownOption("arp", "Arunachal Pradesh"),
                    Ux4gDropdownOption("as", "Assam"),
                    Ux4gDropdownOption("br", "Bihar"),
                    Ux4gDropdownOption("cg", "Chhattisgarh"),
                    Ux4gDropdownOption("ga", "Goa"),
                    Ux4gDropdownOption("gj", "Gujarat"),
                )

                var singleSelected by remember { mutableStateOf(listOf("br")) }
                var multiSelected by remember { mutableStateOf(listOf("arp", "br")) }

                Text("Searchable - Single Select", style = Ux4gTheme.typography.hS_default)
                Ux4gSelectionDropdown(
                    options = stateOptions,
                    selectedOptionIds = singleSelected,
                    onSelectionChange = { singleSelected = it },
                    label = "Label",
                    description = "Description",
                    searchEnabled = true,
                    mode = Ux4gSelectionDropdownMode.SingleSelect
                )

                Text("Searchable - Multi Select", style = Ux4gTheme.typography.hS_default)
                Ux4gSelectionDropdown(
                    options = stateOptions,
                    selectedOptionIds = multiSelected,
                    onSelectionChange = { multiSelected = it },
                    label = "Label",
                    description = "Description",
                    searchEnabled = true,
                    mode = Ux4gSelectionDropdownMode.MultiSelect
                )
            }
        }
    }
}
