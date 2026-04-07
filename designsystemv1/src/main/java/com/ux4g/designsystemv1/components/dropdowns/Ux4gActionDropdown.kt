package com.ux4g.designsystemv1.components.dropdowns

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ux4g.designsystemv1.components.buttons.Ux4gButton
import com.ux4g.designsystemv1.components.buttons.Ux4gIconButton
import com.ux4g.designsystemv1.components.buttons.Ux4gIconButtonSize
import com.ux4g.designsystemv1.components.buttons.Ux4gIconButtonStyle
import com.ux4g.designsystemv1.foundation.Ux4gIcon
import com.ux4g.designsystemv1.foundation.Ux4gIconComponent
import com.ux4g.designsystemv1.theme.Ux4gTheme

data class Ux4gActionDropdownOption(
        val id: String,
        val label: String,
        val showTrailingArrow: Boolean = true
)

@Composable
fun Ux4gActionDropdown(
        options: List<Ux4gActionDropdownOption>,
        onOptionClick: (Ux4gActionDropdownOption) -> Unit,
        modifier: Modifier = Modifier,
        triggerContent: @Composable (onClick: () -> Unit) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Box(modifier = modifier) {
        triggerContent { expanded = true }

        DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier.background(Ux4gTheme.colorScheme.surface)
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                        text = {
                            Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    modifier =
                                            Modifier.fillMaxWidth()
                                                    .defaultMinSize(minWidth = 150.dp)
                            ) {
                                Text(
                                        text = option.label,
                                        style = Ux4gTheme.typography.lL_default,
                                        color = Ux4gTheme.colorScheme.onSurface,
                                        modifier = Modifier.weight(1f)
                                )
                                if (option.showTrailingArrow) {
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Ux4gIconComponent(
                                            icon = Ux4gIcon.Vector(Icons.Filled.KeyboardArrowRight),
                                            contentDescription = null,
                                            tint = Ux4gTheme.colorScheme.onSurface,
                                            modifier = Modifier.size(20.dp)
                                    )
                                }
                            }
                        },
                        modifier = Modifier.background(Color.Transparent),
                        onClick = {
                            expanded = false
                            onOptionClick(option)
                        }
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
private fun Ux4gActionDropdownPreview() {
    Ux4gTheme {
        Column(
                modifier = Modifier.fillMaxWidth().padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val mockOptions =
                    listOf(
                            Ux4gActionDropdownOption("1", "Label 1", showTrailingArrow = true),
                            Ux4gActionDropdownOption("2", "Label 2", showTrailingArrow = true),
                            Ux4gActionDropdownOption("3", "Label 3", showTrailingArrow = true),
                            Ux4gActionDropdownOption(
                                    "4",
                                    "Label 4 (No Arrow)",
                                    showTrailingArrow = false
                            )
                    )

            Text("Icon Button Trigger", style = Ux4gTheme.typography.hS_default)
            Ux4gActionDropdown(options = mockOptions, onOptionClick = {}) { onClick ->
                Ux4gIconButton(
                    onClick = onClick,
                    type = Ux4gIconButtonStyle.Tonal,
                    size = Ux4gIconButtonSize.M
                ) {
                    Ux4gIconComponent(
                        icon = Ux4gIcon.Vector(Icons.Filled.MoreVert),
                        contentDescription = "Menu",
                        modifier = Modifier.size(24.dp),
                        tint = Ux4gTheme.colorScheme.onSurface
                    )
                }
            }

            Text("Filled Button Trigger", style = Ux4gTheme.typography.hS_default)
            Ux4gActionDropdown(options = mockOptions, onOptionClick = {}) { onClick ->
                Ux4gButton(onClick = onClick) { Text("Placeholder") }
            }
        }
    }
}
