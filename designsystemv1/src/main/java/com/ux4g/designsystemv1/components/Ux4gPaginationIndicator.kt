package com.ux4g.designsystemv1.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.ux4g.designsystemv1.foundation.Ux4gIcon
import com.ux4g.designsystemv1.foundation.Ux4gIconComponent
import com.ux4g.designsystemv1.foundation.Ux4gIcons
import com.ux4g.designsystemv1.foundation.Ux4gColors
import com.ux4g.designsystemv1.theme.Ux4gTheme

/**
 * Pagination variant that defines the container style.
 */
enum class Ux4gPaginationVariant {
    Default,
    Capsule
}

/**
 * Pagination size presets.
 */
enum class Ux4gPaginationSize(val dotSize: Dp, val spacing: Dp, val arrowSize: Dp) {
    Small(10.dp, 8.dp, 32.dp),
    Medium(12.dp, 10.dp, 40.dp)
}

/**
 * A pagination indicator component that shows dots for page count and optional arrows.
 * 
 * @param totalPageCount The total number of pages.
 * @param currentPageIndex The index of the currently active page (0-indexed).
 * @param onPageChange Callback when a page dot or arrow is clicked.
 * @param modifier Modifier for the container.
 * @param showArrows Whether to show previous and next arrows.
 * @param variant The visual style of the indicator (Default or Capsule).
 * @param size The size of the component (Small or Medium).
 * @param enabled Whether the component is interactive.
 */
@Composable
fun Ux4gPaginationIndicator(
    totalPageCount: Int,
    currentPageIndex: Int,
    onPageChange: (Int) -> Unit,
    modifier: Modifier = Modifier,
    showArrows: Boolean = true,
    variant: Ux4gPaginationVariant = Ux4gPaginationVariant.Default,
    size: Ux4gPaginationSize = Ux4gPaginationSize.Small,
    enabled: Boolean = true,
    activeColor: Color = Ux4gTheme.colorScheme.primary,
    inactiveColor: Color = Ux4gTheme.colorScheme.surface,
    inactiveBorderColor: Color = Ux4gTheme.colorScheme.onSurface.copy(alpha = 0.3f)
) {
    val containerModifier = if (variant == Ux4gPaginationVariant.Capsule) {
        modifier
            .background(Ux4gTheme.colorScheme.surface, RoundedCornerShape(percent = 50))
            .border(1.dp, activeColor.copy(alpha = 0.12f), RoundedCornerShape(percent = 50))
            .padding(horizontal = 8.dp, vertical = 4.dp)
    } else {
        modifier
    }

    Row(
        modifier = containerModifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(size.spacing)
    ) {
        if (showArrows) {
            PaginationArrow(
                icon = Ux4gIcons.ic_left_arrow,
                enabled = enabled && currentPageIndex > 0,
                onClick = { onPageChange(currentPageIndex - 1) },
                size = size.arrowSize,
                color = activeColor
            )
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(size.spacing),
            verticalAlignment = Alignment.CenterVertically
        ) {
            for (i in 0 until totalPageCount) {
                PaginationDot(
                    isSelected = i == currentPageIndex,
                    onClick = { onPageChange(i) },
                    size = size.dotSize,
                    enabled = enabled,
                    activeColor = activeColor,
                    inactiveColor = inactiveColor,
                    inactiveBorderColor = inactiveBorderColor
                )
            }
        }

        if (showArrows) {
            PaginationArrow(
                icon = Ux4gIcons.ic_right_arrow,
                enabled = enabled && currentPageIndex < totalPageCount - 1,
                onClick = { onPageChange(currentPageIndex + 1) },
                size = size.arrowSize,
                color = activeColor
            )
        }
    }
}

@Composable
private fun PaginationDot(
    isSelected: Boolean,
    onClick: () -> Unit,
    size: Dp,
    enabled: Boolean,
    activeColor: Color,
    inactiveColor: Color,
    inactiveBorderColor: Color
) {
    val backgroundColor = when {
        isSelected -> if (enabled) activeColor else activeColor.copy(alpha = 0.38f)
        else -> inactiveColor
    }
    
    val borderColor = if (isSelected) Color.Transparent else inactiveBorderColor

    Box(
        modifier = Modifier
            .size(size)
            .clip(CircleShape)
            .background(backgroundColor)
            .border(if (isSelected) 0.dp else 1.dp, borderColor, CircleShape)
            .clickable(enabled = enabled) { onClick() }
    )
}

@Composable
private fun PaginationArrow(
    icon: Ux4gIcon,
    enabled: Boolean,
    onClick: () -> Unit,
    size: Dp,
    color: Color
) {
    val contentColor = if (enabled) color else color.copy(alpha = 0.38f)
    
    Box(
        modifier = Modifier
            .size(size)
            .clip(CircleShape)
            .background(Ux4gTheme.colorScheme.surface)
            .border(1.dp, color.copy(alpha = 0.12f), CircleShape)
            .clickable(enabled = enabled) { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Ux4gIconComponent(
            icon = icon,
            contentDescription = null,
            tint = contentColor,
            modifier = Modifier.size(size * 0.5f)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun Ux4gPaginationIndicatorPreview() {
    Ux4gTheme {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Ux4gPaginationIndicator(
                totalPageCount = 7,
                currentPageIndex = 1,
                onPageChange = {}
            )
            
            Ux4gPaginationIndicator(
                totalPageCount = 7,
                currentPageIndex = 1,
                onPageChange = {},
                variant = Ux4gPaginationVariant.Capsule
            )
            
            Ux4gPaginationIndicator(
                totalPageCount = 7,
                currentPageIndex = 0,
                onPageChange = {},
                showArrows = false
            )
            
            Ux4gPaginationIndicator(
                totalPageCount = 7,
                currentPageIndex = 0,
                onPageChange = {},
                showArrows = false,
                variant = Ux4gPaginationVariant.Capsule
            )
        }
    }
}
