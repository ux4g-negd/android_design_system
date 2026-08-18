package com.ux4g.designsystemv1.components.dropdowns

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ux4g.designsystemv1.components.buttons.Ux4gButton
import com.ux4g.designsystemv1.components.buttons.Ux4gIconButton
import com.ux4g.designsystemv1.components.buttons.Ux4gIconButtonSize
import com.ux4g.designsystemv1.components.buttons.Ux4gIconButtonStyle
import com.ux4g.designsystemv1.foundation.Ux4gColors
import com.ux4g.designsystemv1.foundation.Ux4gRadius
import com.ux4g.designsystemv1.foundation.defaultTypography

data class Ux4gActionDropdownOption(
    val id: String,
    val label: String,
    val showTrailingArrow: Boolean = true
)

/**
 * Action Dropdown component for triggerable menus with optional trailing navigation arrows
 * or selected state checkmarks.
 *
 * Matches UX4G Flutter design specifications 1:1.
 */
@Composable
fun Ux4gActionDropdown(
    options: List<Ux4gActionDropdownOption>,
    onOptionClick: (Ux4gActionDropdownOption) -> Unit,
    modifier: Modifier = Modifier,
    selectedOptionId: String? = null,
    triggerContent: @Composable (onClick: () -> Unit) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Box(modifier = modifier) {
        triggerContent { expanded = true }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .background(Ux4gColors.neutral0)
                .border(1.dp, Ux4gColors.neutral900.copy(alpha = 0.12f), RoundedCornerShape(Ux4gRadius.Radius8))
        ) {
            options.forEach { option ->
                val isSelected = selectedOptionId != null && selectedOptionId == option.id
                val selectedBg = if (isSelected) Ux4gColors.primary.copy(alpha = 0.08f) else Color.Transparent
                val activeColor = if (isSelected) Ux4gColors.primary else Ux4gColors.neutral900

                DropdownMenuItem(
                    text = {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier
                                .fillMaxWidth()
                                .defaultMinSize(minWidth = 160.dp)
                        ) {
                            Text(
                                text = option.label,
                                style = defaultTypography.bS_default.copy(
                                    color = activeColor,
                                    fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal
                                ),
                                modifier = Modifier.weight(1f)
                            )
                            if (option.showTrailingArrow) {
                                Spacer(modifier = Modifier.width(8.dp))
                                Icon(
                                    imageVector = Icons.Default.KeyboardArrowRight,
                                    contentDescription = null,
                                    tint = activeColor,
                                    modifier = Modifier.size(20.dp)
                                )
                            } else if (isSelected) {
                                Spacer(modifier = Modifier.width(8.dp))
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = null,
                                    tint = Ux4gColors.primary,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                    },
                    modifier = Modifier.background(selectedBg),
                    onClick = {
                        expanded = false
                        onOptionClick(option)
                    }
                )
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Previews
// ─────────────────────────────────────────────────────────────────────────────

@Preview(name = "Action Dropdown - Light", showBackground = true, backgroundColor = 0xFFFFFFFF)
@Preview(name = "Action Dropdown - Dark", uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true, backgroundColor = 0xFF121212)
@Composable
private fun Ux4gActionDropdownPreview() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val mockOptions = listOf(
            Ux4gActionDropdownOption("1", "Settings", showTrailingArrow = true),
            Ux4gActionDropdownOption("2", "Profile", showTrailingArrow = true),
            Ux4gActionDropdownOption("3", "Privacy", showTrailingArrow = true),
            Ux4gActionDropdownOption("4", "Selected Item", showTrailingArrow = false)
        )

        var selectedId by remember { mutableStateOf("4") }

        Text("Icon Button Trigger", style = defaultTypography.hS_default)
        Ux4gActionDropdown(
            options = mockOptions,
            selectedOptionId = selectedId,
            onOptionClick = { selectedId = it.id }
        ) { onClick ->
            Ux4gIconButton(
                onClick = onClick,
                type = Ux4gIconButtonStyle.Tonal,
                size = Ux4gIconButtonSize.M
            ) {
                Icon(
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = "Menu",
                    modifier = Modifier.size(24.dp),
                    tint = Ux4gColors.neutral900
                )
            }
        }

        Text("Filled Button Trigger", style = defaultTypography.hS_default)
        Ux4gActionDropdown(
            options = mockOptions,
            selectedOptionId = selectedId,
            onOptionClick = { selectedId = it.id }
        ) { onClick ->
            Ux4gButton(onClick = onClick) { Text("Select Action") }
        }
    }
}
