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
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import com.ux4g.designsystemv1.foundation.Ux4gBorderWidth
import com.ux4g.designsystemv1.foundation.Ux4gColors
import com.ux4g.designsystemv1.foundation.Ux4gRadius
import com.ux4g.designsystemv1.foundation.Ux4gSpace
import com.ux4g.designsystemv1.theme.Ux4gTheme

enum class Ux4gAccordionChevronPosition {
    Leading,
    Trailing
}

@Composable
fun Ux4gAccordion(
    title: String,
    modifier: Modifier = Modifier,
    expanded: Boolean = false,
    enabled: Boolean = true,
    onExpandedChange: ((Boolean) -> Unit)? = null,
    backgroundColor: Color? = null,
    disabledBackgroundColor: Color? = null,
    contentBackgroundColor: Color? = null,
    collapsedBorderColor: Color? = null,
    expandedBorderColor: Color? = null,
    disabledBorderColor: Color? = null,
    titleColor: Color? = null,
    disabledTitleColor: Color? = null,
    iconColor: Color? = null,
    disabledIconColor: Color? = null,
    leadingIcon: (@Composable () -> Unit)? = null,
    chevronPosition: Ux4gAccordionChevronPosition = Ux4gAccordionChevronPosition.Trailing,
    content: @Composable () -> Unit = {}
) {
    val resolvedBackgroundColor = if (enabled) {
        backgroundColor ?: Ux4gTheme.colorScheme.surface
    } else {
        disabledBackgroundColor ?: backgroundColor ?: Ux4gColors.neutral100
    }
    val resolvedContentBackgroundColor = contentBackgroundColor ?: backgroundColor ?: Ux4gTheme.colorScheme.surface
    val resolvedCollapsedBorderColor =
        collapsedBorderColor ?: Ux4gTheme.colorScheme.onSurface.copy(alpha = 0.12f)
    val resolvedExpandedBorderColor =
        expandedBorderColor ?: resolvedCollapsedBorderColor
    val resolvedDisabledBorderColor =
        disabledBorderColor ?: Ux4gTheme.colorScheme.onSurface.copy(alpha = 0.12f)

    val resolvedTitleColor = titleColor ?: Ux4gTheme.colorScheme.onSurface
    val resolvedDisabledTitleColor =
        disabledTitleColor ?: Ux4gTheme.colorScheme.onSurface.copy(alpha = 0.38f)
    val resolvedIconColor = iconColor ?: Ux4gTheme.colorScheme.onSurface
    val resolvedDisabledIconColor =
        disabledIconColor ?: Ux4gTheme.colorScheme.onSurface.copy(alpha = 0.38f)

    val borderColor = when {
        !enabled -> resolvedDisabledBorderColor
        expanded -> resolvedExpandedBorderColor
        else -> resolvedCollapsedBorderColor
    }

    val currentTitleColor = if (enabled) resolvedTitleColor else resolvedDisabledTitleColor
    val currentIconColor = if (enabled) resolvedIconColor else resolvedDisabledIconColor

    val rotation by animateFloatAsState(
        targetValue = if (expanded) 180f else 0f,
        animationSpec = tween(durationMillis = 220),
        label = "accordionIconRotation"
    )

    val chevron = @Composable {
        Icon(
            imageVector = Icons.Outlined.KeyboardArrowDown,
            contentDescription = if (expanded) "Collapse accordion" else "Expand accordion",
            modifier = Modifier
                .size(Ux4gSpace.Space16)
                .rotate(rotation),
            tint = currentIconColor
        )
    }

    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .border(
                    width = Ux4gBorderWidth.Thin,
                    color = borderColor,
                    shape = RoundedCornerShape(Ux4gRadius.RadiusNone)
                )
                .background(resolvedBackgroundColor)
                .clickable(
                    enabled = enabled,
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) { onExpandedChange?.invoke(!expanded) }
                .padding(horizontal = Ux4gSpace.Space12, vertical = Ux4gSpace.Space8),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (chevronPosition == Ux4gAccordionChevronPosition.Leading) {
                chevron()
                Spacer(modifier = Modifier.width(Ux4gSpace.Space8))
            }

            Row(
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (leadingIcon != null) {
                    CompositionLocalProvider(LocalContentColor provides currentIconColor) {
                        Box(
                            modifier = Modifier.size(Ux4gSpace.Space16),
                            contentAlignment = Alignment.Center
                        ) {
                            leadingIcon()
                        }
                    }
                    Spacer(modifier = Modifier.width(Ux4gSpace.Space8))
                }

                Text(
                    text = title,
                    modifier = Modifier.weight(1f),
                    style = Ux4gTheme.typography.lL_default,
                    color = currentTitleColor
                )
            }

            if (chevronPosition == Ux4gAccordionChevronPosition.Trailing) {
                Spacer(modifier = Modifier.width(Ux4gSpace.Space8))
                chevron()
            }
        }

        AnimatedVisibility(visible = expanded && enabled) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(resolvedContentBackgroundColor)
                    .padding(top = Ux4gSpace.Space12, start = Ux4gSpace.Space12, end = Ux4gSpace.Space12)
            ) {
                content()
            }
        }
    }
}

data class Ux4gAccordionItem(
    val title: String,
    val enabled: Boolean = true,
    val leadingIcon: (@Composable () -> Unit)? = null,
    val chevronPosition: Ux4gAccordionChevronPosition = Ux4gAccordionChevronPosition.Trailing
)

@Composable
fun Ux4gAccordionGroup(
    items: List<Ux4gAccordionItem>,
    modifier: Modifier = Modifier,
    expandedIndex: Int? = null,
    onExpandedIndexChange: (Int?) -> Unit = {},
    itemSpacing: Dp = Ux4gSpace.Space20,
    content: @Composable (index: Int, item: Ux4gAccordionItem) -> Unit
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(itemSpacing)
    ) {
        items.forEachIndexed { index, item ->
            Ux4gAccordion(
                title = item.title,
                leadingIcon = item.leadingIcon,
                chevronPosition = item.chevronPosition,
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
            Text(text = "Accordion Group (with active & disabled items)", style = Ux4gTheme.typography.tS_strong)

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

            Spacer(modifier = Modifier.padding(top = Ux4gSpace.Space8))

            Text(text = "Standalone Disabled State", style = Ux4gTheme.typography.tS_strong)

            // Standalone Disabled State Showcase
            Ux4gAccordion(
                title = "Accordion Item",
                enabled = false,
                iconColor = Ux4gTheme.colorScheme.primary,
                disabledIconColor = Ux4gTheme.colorScheme.primary
            )
        }
    }
}
