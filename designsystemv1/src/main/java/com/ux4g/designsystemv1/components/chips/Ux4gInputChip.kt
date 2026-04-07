package com.ux4g.designsystemv1.components.chips

import android.content.res.Configuration
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.ux4g.designsystemv1.foundation.Ux4gBorderWidth
import com.ux4g.designsystemv1.foundation.Ux4gIcon
import com.ux4g.designsystemv1.foundation.Ux4gIconComponent
import com.ux4g.designsystemv1.foundation.Ux4gIcons
import com.ux4g.designsystemv1.foundation.Ux4gRadius
import com.ux4g.designsystemv1.foundation.Ux4gSpace
import com.ux4g.designsystemv1.theme.Ux4gTheme

enum class Ux4gInputChipSize {
    XS,
    S,
    M
}

@Composable
fun Ux4gInputChip(
        text: String,
        onDismiss: () -> Unit,
        modifier: Modifier = Modifier,
        enabled: Boolean = true,
        size: Ux4gInputChipSize = Ux4gInputChipSize.M,
        leadingContent: @Composable (() -> Unit)? = null,
        borderRadius: Dp = Ux4gRadius.Radius4,
        backgroundColor: Color = Ux4gTheme.colorScheme.surface,
        borderColor: Color = Ux4gTheme.colorScheme.onSurface.copy(alpha = 0.2f),
        textColor: Color = Ux4gTheme.colorScheme.onSurface,
        borderWidth: Dp = Ux4gBorderWidth.Thin
) {
    val height =
            when (size) {
                Ux4gInputChipSize.XS -> 20.dp
                Ux4gInputChipSize.S -> 28.dp
                Ux4gInputChipSize.M -> 32.dp
            }

    val padding =
            when (size) {
                Ux4gInputChipSize.XS -> PaddingValues(horizontal = Ux4gSpace.Space8)
                Ux4gInputChipSize.S -> PaddingValues(horizontal = Ux4gSpace.Space12)
                Ux4gInputChipSize.M -> PaddingValues(horizontal = Ux4gSpace.Space16)
            }

    val typography =
            when (size) {
                Ux4gInputChipSize.XS -> Ux4gTheme.typography.lS_default
                Ux4gInputChipSize.S -> Ux4gTheme.typography.lM_default
                Ux4gInputChipSize.M -> Ux4gTheme.typography.lL_default
            }

    val closeIconSize =
            when (size) {
                Ux4gInputChipSize.XS -> 12.dp
                Ux4gInputChipSize.S -> 16.dp
                Ux4gInputChipSize.M -> 16.dp
            }

    val closeIconColor = textColor.copy(alpha = 0.8f)

    Surface(
            modifier = modifier.height(height),
            shape = RoundedCornerShape(borderRadius),
            color = backgroundColor,
            border = BorderStroke(borderWidth, borderColor),
    ) {
        Row(
                modifier = Modifier.padding(padding),
                horizontalArrangement = Arrangement.spacedBy(Ux4gSpace.Space8),
                verticalAlignment = Alignment.CenterVertically
        ) {
            if (leadingContent != null) {
                leadingContent()
            }
            Text(text = text, style = typography, color = textColor)
            // Trailing dismiss icon
            Ux4gIconComponent(
                    icon = Ux4gIcons.Close,
                    contentDescription = "Dismiss",
                    modifier =
                            Modifier.size(closeIconSize)
                                    .clip(CircleShape)
                                    .clickable(enabled = enabled, onClick = onDismiss),
                    tint = closeIconColor
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
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
        chipsContent: @Composable () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Column(
            modifier = modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(Ux4gSpace.Space8)
    ) {
        if (isDropdown) {
            ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = it }) {
                OutlinedTextField(
                        value = value,
                        onValueChange = onValueChange,
                        modifier = Modifier.fillMaxWidth().menuAnchor(),
                        placeholder = { Text(placeholder) },
                        leadingIcon = leadingIcon,
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                        },
                        singleLine = true,
                        colors =
                                OutlinedTextFieldDefaults.colors(
                                        focusedBorderColor = Ux4gTheme.colorScheme.primary,
                                        unfocusedBorderColor =
                                                Ux4gTheme.colorScheme.onSurface.copy(alpha = 0.2f),
                                        focusedContainerColor = Ux4gTheme.colorScheme.surface,
                                        unfocusedContainerColor = Ux4gTheme.colorScheme.surface
                                ),
                        shape = RoundedCornerShape(Ux4gRadius.Radius8)
                )
                if (dropdownOptions.isNotEmpty()) {
                    ExposedDropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false }
                    ) {
                        dropdownOptions.forEach { option ->
                            DropdownMenuItem(
                                    text = { Text(option) },
                                    onClick = {
                                        onAddChip(option)
                                        expanded = false
                                        onValueChange("")
                                    }
                            )
                        }
                    }
                }
            }
        } else {
            OutlinedTextField(
                    value = value,
                    onValueChange = onValueChange,
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text(placeholder) },
                    leadingIcon = leadingIcon,
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    keyboardActions =
                            KeyboardActions(
                                    onDone = {
                                        if (value.isNotBlank()) {
                                            onAddChip(value)
                                            onValueChange(
                                                    ""
                                            ) // Clear input field upon submitting chip text
                                        }
                                    }
                            ),
                    colors =
                            OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = Ux4gTheme.colorScheme.primary,
                                    unfocusedBorderColor =
                                            Ux4gTheme.colorScheme.onSurface.copy(alpha = 0.2f),
                                    focusedContainerColor = Ux4gTheme.colorScheme.surface,
                                    unfocusedContainerColor = Ux4gTheme.colorScheme.surface
                            ),
                    shape = RoundedCornerShape(Ux4gRadius.Radius8)
            )
        }

        // Horizontal scrolling row for the chips below the input box
        Row(
                modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(Ux4gSpace.Space8),
                verticalAlignment = Alignment.CenterVertically
        ) { chipsContent() }
    }
}

// -----------------------------------------------------------------------------------------
// Previews and Examples
// -----------------------------------------------------------------------------------------

@Composable
private fun SampleIconForInputChip(
        icon: Ux4gIcon,
        size: Ux4gInputChipSize,
        color: Color = Ux4gTheme.colorScheme.onSurface
) {
    val iconSize =
            if (size == Ux4gInputChipSize.M) 20.dp
            else if (size == Ux4gInputChipSize.S) 16.dp else 12.dp
    Ux4gIconComponent(
            icon = icon,
            contentDescription = "Icon",
            modifier = Modifier.size(iconSize),
            tint = color
    )
}

@Preview(name = "Light", showBackground = true)
@Preview(name = "Dark", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun Ux4gInputChipPreview() {
    Ux4gTheme {
        Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Text("Input Chips - Text Input (Press Enter)", style = Ux4gTheme.typography.hS_default)

            // State list managed explicitly by the preview caller! This makes it flexible
            val inputChips = remember { mutableStateListOf("Selection 1", "Selection 2") }
            var textInputValue by remember { mutableStateOf("") }

            Ux4gInputChipField(
                    value = textInputValue,
                    onValueChange = { textInputValue = it },
                    isDropdown = false,
                    onAddChip = { newChip ->
                        if (!inputChips.contains(newChip)) inputChips.add(newChip)
                    },
                    leadingIcon = {
                        Ux4gIconComponent(
                                icon = Ux4gIcons.Settings,
                                contentDescription = "Icon",
                                modifier = Modifier.size(20.dp)
                        )
                    },
                    chipsContent = {
                        inputChips.forEach { chipText ->
                            Ux4gInputChip(
                                    text = chipText,
                                    onDismiss = { inputChips.remove(chipText) },
                                    size = Ux4gInputChipSize.M,
                                    leadingContent = {
                                        SampleIconForInputChip(
                                                icon = Ux4gIcons.Person,
                                                size = Ux4gInputChipSize.M
                                        )
                                    }
                            )
                        }
                    }
            )

            Text("Input Chips - Dropdown", style = Ux4gTheme.typography.hS_default)

            val dropdownChips = remember { mutableStateListOf("Email Selection") }
            var dropdownValue by remember { mutableStateOf("") }

            Ux4gInputChipField(
                    value = dropdownValue,
                    onValueChange = { dropdownValue = it },
                    isDropdown = true,
                    dropdownOptions =
                            listOf("Email Selection", "Phone Selection", "Location Selection"),
                    onAddChip = { newChip ->
                        if (!dropdownChips.contains(newChip)) dropdownChips.add(newChip)
                    },
                    leadingIcon = {
                        Ux4gIconComponent(
                                icon = Ux4gIcons.Home,
                                contentDescription = "Icon",
                                modifier = Modifier.size(20.dp)
                        )
                    },
                    chipsContent = {
                        dropdownChips.forEach { chipText ->
                            Ux4gInputChip(
                                    text = chipText,
                                    onDismiss = { dropdownChips.remove(chipText) },
                                    size = Ux4gInputChipSize.S,
                                    leadingContent = {
                                        SampleIconForInputChip(
                                                icon = Ux4gIcons.Person,
                                                size = Ux4gInputChipSize.S
                                        )
                                    }
                            )
                        }
                    }
            )
        }
    }
}
