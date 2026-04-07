package com.ux4g.designsystemv1.components

import android.content.res.Configuration
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.ux4g.designsystemv1.foundation.Ux4gBorderWidth
import com.ux4g.designsystemv1.foundation.Ux4gIconComponent
import com.ux4g.designsystemv1.foundation.Ux4gIcons
import com.ux4g.designsystemv1.foundation.Ux4gRadius
import com.ux4g.designsystemv1.foundation.Ux4gSpace
import com.ux4g.designsystemv1.theme.Ux4gTheme

enum class Ux4gFilterChipSize {
    S,
    M
}

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
        selectedBackgroundColor: Color = Ux4gTheme.colorScheme.primary.copy(alpha = 0.08f),
        unselectedBackgroundColor: Color = Ux4gTheme.colorScheme.surface,
        selectedBorderColor: Color = Ux4gTheme.colorScheme.primary,
        unselectedBorderColor: Color = Ux4gTheme.colorScheme.onSurface.copy(alpha = 0.2f),
        selectedTextColor: Color = Ux4gTheme.colorScheme.primary,
        unselectedTextColor: Color = Ux4gTheme.colorScheme.onSurface,
        selectedBorderWidth: Dp = Ux4gBorderWidth.Thick,
        unselectedBorderWidth: Dp = Ux4gBorderWidth.Thin
) {
    val height =
            when (size) {
                Ux4gFilterChipSize.S -> 28.dp
                Ux4gFilterChipSize.M -> 32.dp
            }

    val padding =
            when (size) {
                Ux4gFilterChipSize.S -> PaddingValues(horizontal = Ux4gSpace.Space12)
                Ux4gFilterChipSize.M -> PaddingValues(horizontal = Ux4gSpace.Space16)
            }

    val typography =
            when (size) {
                Ux4gFilterChipSize.S -> Ux4gTheme.typography.lM_default
                Ux4gFilterChipSize.M -> Ux4gTheme.typography.lL_default
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
fun Ux4gFilterChipGroup(modifier: Modifier = Modifier, content: @Composable RowScope.() -> Unit) {
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
fun SampleAvatarForChip(text: String, size: Ux4gFilterChipSize) {
    val avatarSize = if (size == Ux4gFilterChipSize.M) 20.dp else 16.dp
    val textStyle =
            if (size == Ux4gFilterChipSize.M) Ux4gTheme.typography.lS_strong
            else Ux4gTheme.typography.lS_strong

    Box(
            modifier =
                    Modifier.size(avatarSize)
                            .clip(CircleShape)
                            .background(Ux4gTheme.colorScheme.primary),
            contentAlignment = Alignment.Center
    ) {
        Text(
                text = text,
                color = Ux4gTheme.colorScheme.onPrimary,
                style = textStyle,
                textAlign = TextAlign.Center
        )
    }
}

@Composable
fun SampleIconForChip(size: Ux4gFilterChipSize, color: Color) {
    val iconSize = if (size == Ux4gFilterChipSize.M) 20.dp else 16.dp
    Ux4gIconComponent(
            icon = Ux4gIcons.Person,
            contentDescription = "Person",
            modifier = Modifier.size(iconSize),
            tint = color
    )
}

@Preview(name = "Light", showBackground = true)
@Preview(name = "Dark", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun Ux4gFilterChipPreview() {
    Ux4gTheme {
        Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text("Filter Chips - Size M", style = Ux4gTheme.typography.hS_default)
            Ux4gFilterChipGroup {
                var selectedChip1 by remember { mutableStateOf(false) }
                var selectedChip2 by remember { mutableStateOf(true) }
                var selectedChip3 by remember { mutableStateOf(false) }
                var selectedChip4 by remember { mutableStateOf(true) }

                Ux4gFilterChip(
                        text = "Filter 1",
                        selected = selectedChip1,
                        onClick = { selectedChip1 = !selectedChip1 },
                        size = Ux4gFilterChipSize.M,
                        leadingContent = { SampleAvatarForChip("F", Ux4gFilterChipSize.M) }
                )
                Ux4gFilterChip(
                        text = "Filter 2",
                        selected = selectedChip2,
                        onClick = { selectedChip2 = !selectedChip2 },
                        size = Ux4gFilterChipSize.M,
                        leadingContent = { SampleAvatarForChip("F", Ux4gFilterChipSize.M) },
                        //                        selectedBorderWidth = Ux4gBorderWidth.Thin // thin
                        // border example from image
                        )
                Ux4gFilterChip(
                        text = "Icon 3",
                        selected = selectedChip3,
                        onClick = { selectedChip3 = !selectedChip3 },
                        size = Ux4gFilterChipSize.M,
                        leadingContent = {
                            val color =
                                    if (selectedChip3) Ux4gTheme.colorScheme.primary
                                    else Ux4gTheme.colorScheme.onSurface
                            SampleIconForChip(Ux4gFilterChipSize.M, color)
                        }
                )
                Ux4gFilterChip(
                        text = "Filter 4",
                        selected = selectedChip4,
                        onClick = { selectedChip4 = !selectedChip4 },
                        size = Ux4gFilterChipSize.M,
                        leadingContent = { SampleAvatarForChip("F", Ux4gFilterChipSize.M) },
                        selectedBorderWidth =
                                Ux4gBorderWidth.Thick // thick border example from image
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text("Filter Chips - Size S", style = Ux4gTheme.typography.hS_default)
            Ux4gFilterChipGroup {
                var selectedChip1 by remember { mutableStateOf(false) }
                var selectedChip2 by remember { mutableStateOf(true) }

                Ux4gFilterChip(
                        text = "No Icon S",
                        selected = selectedChip1,
                        onClick = { selectedChip1 = !selectedChip1 },
                        size = Ux4gFilterChipSize.S
                )
                Ux4gFilterChip(
                        text = "Avatar S",
                        selected = selectedChip2,
                        onClick = { selectedChip2 = !selectedChip2 },
                        size = Ux4gFilterChipSize.S,
                        leadingContent = { SampleAvatarForChip("A", Ux4gFilterChipSize.S) }
                )
            }
        }
    }
}
