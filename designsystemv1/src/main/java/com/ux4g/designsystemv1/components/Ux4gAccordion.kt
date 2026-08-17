package com.ux4g.designsystemv1.components

import android.content.res.Configuration
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.KeyboardArrowDown
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.ux4g.designsystemv1.foundation.Ux4gBorderWidth
import com.ux4g.designsystemv1.foundation.Ux4gColors
import com.ux4g.designsystemv1.foundation.Ux4gRadius
import com.ux4g.designsystemv1.foundation.Ux4gSpace
import com.ux4g.designsystemv1.theme.Ux4gTheme

@Composable
fun Ux4gAccordion(
    title: String,
    modifier: Modifier = Modifier,
    expanded: Boolean = false,
    enabled: Boolean = true,
    onExpandedChange: (Boolean) -> Unit = {},
    backgroundColor: Color = Ux4gTheme.colorScheme.surface,
    contentBackgroundColor: Color = Ux4gTheme.colorScheme.surface,
    collapsedBorderColor: Color = Ux4gColors.neutral200,
    expandedBorderColor: Color = Ux4gTheme.colorScheme.primary,
    titleColor: Color = Ux4gColors.neutral800,
    disabledTitleColor: Color = Ux4gColors.neutral300,
    iconColor: Color = Ux4gColors.neutral800,
    disabledIconColor: Color = Ux4gColors.neutral300,
    content: @Composable () -> Unit
) {
    val rotation by animateFloatAsState(
        targetValue = if (expanded) 180f else 0f,
        animationSpec = tween(durationMillis = 220),
        label = "accordionIconRotation"
    )

    val borderColor = when {
        !enabled -> Color.Transparent
        expanded -> expandedBorderColor
        else -> collapsedBorderColor
    }

    val currentTitleColor = if (enabled) titleColor else disabledTitleColor
    val currentIconColor = if (enabled) iconColor else disabledIconColor

    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .border(
                    width = if (enabled) Ux4gBorderWidth.Thin else Ux4gBorderWidth.None,
                    color = borderColor,
                    shape = RoundedCornerShape(Ux4gRadius.RadiusNone)
                )
                .background(backgroundColor)
                .clickable(
                    enabled = enabled,
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) { onExpandedChange(!expanded) }
                .padding(horizontal = Ux4gSpace.Space12, vertical = Ux4gSpace.Space8),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = title,
                modifier = Modifier.weight(1f),
                style = Ux4gTheme.typography.lL_default,
                color = currentTitleColor
            )

            Spacer(modifier = Modifier.width(Ux4gSpace.Space8))

            Icon(
                imageVector = Icons.Outlined.KeyboardArrowDown,
                contentDescription = if (expanded) "Collapse accordion" else "Expand accordion",
                modifier = Modifier
                    .size(Ux4gSpace.Space16)
                    .rotate(rotation),
                tint = currentIconColor
            )
        }

        AnimatedVisibility(visible = expanded && enabled) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = Ux4gSpace.Space12, start = Ux4gSpace.Space12, end = Ux4gSpace.Space12)
                    .background(contentBackgroundColor)
            ) {
                content()
            }
        }
    }
}

data class Ux4gAccordionItem(
    val title: String,
    val enabled: Boolean = true
)

@Composable
fun Ux4gAccordionGroup(
    items: List<Ux4gAccordionItem>,
    modifier: Modifier = Modifier,
    expandedIndex: Int? = null,
    onExpandedIndexChange: (Int?) -> Unit = {},
    itemSpacing: androidx.compose.ui.unit.Dp = Ux4gSpace.Space20,
    content: @Composable (index: Int, item: Ux4gAccordionItem) -> Unit
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(itemSpacing)
    ) {
        items.forEachIndexed { index, item ->
            Ux4gAccordion(
                title = item.title,
                expanded = expandedIndex == index,
                enabled = item.enabled,
                onExpandedChange = { isExpanded ->
                    onExpandedIndexChange(if (isExpanded) index else null)
                }
            ) {
                content(index, item)
            }
        }
    }
}

@Preview(name = "Accordion Light", showBackground = true)
@Preview(
    name = "Accordion Dark",
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true
)
@Composable
private fun Ux4gAccordionPreview() {
    Ux4gTheme {
        var expandedIndex by remember { mutableStateOf<Int?>(2) }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Ux4gColors.white)
                .padding(Ux4gSpace.Space24),
            verticalArrangement = Arrangement.spacedBy(Ux4gSpace.Space20)
        ) {
            Ux4gAccordionGroup(
                items = listOf(
                    Ux4gAccordionItem(title = "Accordion Item"),
                    Ux4gAccordionItem(title = "Accordion Item"),
                    Ux4gAccordionItem(title = "Accordion Item"),
                    Ux4gAccordionItem(title = "Accordion Item"),
                    Ux4gAccordionItem(title = "Accordion Item", enabled = false)
                ),
                expandedIndex = expandedIndex,
                onExpandedIndexChange = { expandedIndex = it }
            ) { index, _ ->
                if (index == 2) {
                    Text(
                        text = "Lorem ipsum is a dummy or placeholder text commonly used in graphic design, publishing, and web development. Its purpose is to permit a page layout to be designed, independently of the copy that will subsequently populate it.",
                        style = Ux4gTheme.typography.bXS_default,
                        color = Ux4gColors.neutral600
                    )
                }
            }
        }
    }
}
