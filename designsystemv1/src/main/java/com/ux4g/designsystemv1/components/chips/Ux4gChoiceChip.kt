package com.ux4g.designsystemv1.components

import android.content.res.Configuration
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.ux4g.designsystemv1.R
import com.ux4g.designsystemv1.foundation.Ux4gBorderWidth
import com.ux4g.designsystemv1.foundation.Ux4gIcon
import com.ux4g.designsystemv1.foundation.Ux4gIconComponent
import com.ux4g.designsystemv1.foundation.Ux4gIcons
import com.ux4g.designsystemv1.foundation.Ux4gRadius
import com.ux4g.designsystemv1.foundation.Ux4gSpace
import com.ux4g.designsystemv1.theme.Ux4gTheme

enum class Ux4gChoiceChipSize {
    S,
    M
}

@Composable
fun Ux4gChoiceChip(
        text: String,
        selected: Boolean,
        onClick: () -> Unit,
        modifier: Modifier = Modifier,
        enabled: Boolean = true,
        size: Ux4gChoiceChipSize = Ux4gChoiceChipSize.M,
        leadingContent: @Composable (() -> Unit)? = null,
        borderRadius: Dp = Ux4gRadius.Radius4,
        selectedBackgroundColor: Color = Ux4gTheme.colorScheme.primary,
        unselectedBackgroundColor: Color = Ux4gTheme.colorScheme.surface,
        selectedBorderColor: Color = Ux4gTheme.colorScheme.primary,
        unselectedBorderColor: Color = Ux4gTheme.colorScheme.onSurface.copy(alpha = 0.2f),
        selectedTextColor: Color = Ux4gTheme.colorScheme.onPrimary,
        unselectedTextColor: Color = Ux4gTheme.colorScheme.onSurface,
        selectedBorderWidth: Dp = Ux4gBorderWidth.Thin,
        unselectedBorderWidth: Dp = Ux4gBorderWidth.Thin
) {
    val height =
            when (size) {
                Ux4gChoiceChipSize.S -> 28.dp
                Ux4gChoiceChipSize.M -> 32.dp
            }

    val padding =
            when (size) {
                Ux4gChoiceChipSize.S -> PaddingValues(horizontal = Ux4gSpace.Space12)
                Ux4gChoiceChipSize.M -> PaddingValues(horizontal = Ux4gSpace.Space16)
            }

    val typography =
            when (size) {
                Ux4gChoiceChipSize.S -> Ux4gTheme.typography.lM_default
                Ux4gChoiceChipSize.M -> Ux4gTheme.typography.lL_default
            }

    val backgroundColor = if (selected) selectedBackgroundColor else unselectedBackgroundColor
    val borderColor = if (selected) selectedBorderColor else unselectedBorderColor
    val borderWidth = if (selected) selectedBorderWidth else unselectedBorderWidth
    val textColor = if (selected) selectedTextColor else unselectedTextColor

    Surface(
            onClick = onClick,
            modifier = modifier.height(height),
            enabled = enabled,
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
        }
    }
}

@Composable
fun Ux4gChoiceChipGroup(modifier: Modifier = Modifier, content: @Composable RowScope.() -> Unit) {
    Row(
            modifier = modifier.horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(Ux4gSpace.Space8),
            verticalAlignment = Alignment.CenterVertically
    ) { content() }
}

// -----------------------------------------------------------------------------------------
// Previews and Examples
// -----------------------------------------------------------------------------------------

@Composable
private fun SampleIconForChoiceChip(icon: Ux4gIcon, size: Ux4gChoiceChipSize, color: Color) {
    val iconSize = if (size == Ux4gChoiceChipSize.M) 20.dp else 16.dp
    Ux4gIconComponent(
            icon = icon,
            contentDescription = "Icon",
            modifier = Modifier.size(iconSize),
            tint = color
    )
}

@Composable
private fun SampleAvatarForChoiceChip(
        text: String,
        size: Ux4gChoiceChipSize,
        isSelected: Boolean = false
) {
    val avatarSize = if (size == Ux4gChoiceChipSize.M) 20.dp else 16.dp
    val textStyle = Ux4gTheme.typography.lS_strong

    val bgColor = if (isSelected) Ux4gTheme.colorScheme.onPrimary else Ux4gTheme.colorScheme.primary
    val textColor =
            if (isSelected) Ux4gTheme.colorScheme.primary else Ux4gTheme.colorScheme.onPrimary

    Box(
            modifier = Modifier.size(avatarSize).clip(CircleShape).background(bgColor),
            contentAlignment = Alignment.Center
    ) { Text(text = text, color = textColor, style = textStyle, textAlign = TextAlign.Center) }
}

@Composable
private fun SampleImageForChoiceChip(size: Ux4gChoiceChipSize) {
    val imageSize = if (size == Ux4gChoiceChipSize.M) 20.dp else 16.dp
    Image(
            painter =
                    painterResource(
                            id = R.drawable.ic_launcher_foreground
                    ), // Replace with actual image
            contentDescription = "Image",
            modifier =
                    Modifier.size(imageSize)
                            .clip(CircleShape)
                            .background(Ux4gTheme.colorScheme.onSurface.copy(alpha = 0.1f)),
            contentScale = ContentScale.Crop
    )
}

@Preview(name = "Light", showBackground = true)
@Preview(name = "Dark", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun Ux4gChoiceChipPreview() {
    Ux4gTheme {
        Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text("Choice Chips - Radio Group Behavior", style = Ux4gTheme.typography.hS_default)

            Ux4gChoiceChipGroup {
                // Radio group state: only one selected at a time
                val choices =
                        listOf(
                                Triple("Choice 1", Ux4gIcons.Home, "icon"),
                                Triple("Choice 2", null, "avatar"),
                                Triple("Choice 3", null, "image")
                        )

                var selectedChoice by remember {
                    mutableStateOf(choices[1].first)
                } // "Choice 2" selected by default

                choices.forEach { (text, icon, type) ->
                    val isSelected = selectedChoice == text
                    Ux4gChoiceChip(
                            text = text,
                            selected = isSelected,
                            onClick = { selectedChoice = text },
                            size = Ux4gChoiceChipSize.M,
                            leadingContent = {
                                when (type) {
                                    "icon" -> {
                                        val color =
                                                if (isSelected) Ux4gTheme.colorScheme.onPrimary
                                                else Ux4gTheme.colorScheme.onSurface
                                        SampleIconForChoiceChip(icon!!, Ux4gChoiceChipSize.M, color)
                                    }
                                    "avatar" -> {
                                        SampleAvatarForChoiceChip(
                                                "A",
                                                Ux4gChoiceChipSize.M,
                                                isSelected
                                        )
                                    }
                                    "image" -> {
                                        SampleImageForChoiceChip(Ux4gChoiceChipSize.M)
                                    }
                                }
                            }
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Adding a reset choice chip to demonstrate unselected state independently
            Ux4gChoiceChip(
                    text = "Reset",
                    selected = false,
                    onClick = { /* Handle clear/reset */},
                    size = Ux4gChoiceChipSize.M
            )
        }
    }
}
