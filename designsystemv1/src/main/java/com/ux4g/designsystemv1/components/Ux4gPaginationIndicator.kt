package com.ux4g.designsystemv1.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.ux4g.designsystemv1.foundation.Ux4gColors
import com.ux4g.designsystemv1.foundation.Ux4gIcon
import com.ux4g.designsystemv1.foundation.Ux4gIconComponent
import com.ux4g.designsystemv1.foundation.Ux4gIcons
import com.ux4g.designsystemv1.theme.Ux4gTheme

/**
 * Pagination variant that defines the container style.
 */
enum class Ux4gPaginationVariant {
    Default,
    Capsule
}

/**
 * Pagination size presets with dotSize, spacing, and arrowSize.
 *
 * Small: dotSize = 10.dp, spacing = 8.dp, arrowSize = 24.dp
 * Medium: dotSize = 12.dp, spacing = 10.dp, arrowSize = 32.dp
 */
enum class Ux4gPaginationSize(val dotSize: Dp, val spacing: Dp, val arrowSize: Dp) {
    Small(10.dp, 8.dp, 24.dp),
    Medium(12.dp, 10.dp, 32.dp)
}

/**
 * Backward compatibility alias for [Ux4gPaginationDotted].
 */
@Deprecated(
    message = "Use Ux4gPaginationDotted instead",
    replaceWith = ReplaceWith("Ux4gPaginationDotted(totalPageCount, currentPageIndex, onPageChange, modifier, showArrows, arrowsOnRight, variant, size, enabled, activeColor, inactiveColor, inactiveBorderColor, width, height)")
)
@Composable
fun Ux4gPaginationIndicator(
    totalPageCount: Int,
    currentPageIndex: Int,
    onPageChange: (Int) -> Unit,
    modifier: Modifier = Modifier,
    showArrows: Boolean = true,
    arrowsOnRight: Boolean = false,
    variant: Ux4gPaginationVariant = Ux4gPaginationVariant.Default,
    size: Ux4gPaginationSize = Ux4gPaginationSize.Small,
    enabled: Boolean = true,
    activeColor: Color? = null,
    inactiveColor: Color? = null,
    inactiveBorderColor: Color? = null,
    width: Dp? = null,
    height: Dp? = null
) {
    Ux4gPaginationDotted(
        totalPageCount = totalPageCount,
        currentPageIndex = currentPageIndex,
        onPageChange = onPageChange,
        modifier = modifier,
        showArrows = showArrows,
        arrowsOnRight = arrowsOnRight,
        variant = variant,
        size = size,
        enabled = enabled,
        activeColor = activeColor,
        inactiveColor = inactiveColor,
        inactiveBorderColor = inactiveBorderColor,
        width = width,
        height = height
    )
}

/**
 * A pagination indicator component that shows dots for page count and optional arrows.
 * 
 * Direct 1:1 parity with Flutter's `Ux4gPaginationDotted` / `pagination.dart`.
 *
 * @param totalPageCount The total number of pages.
 * @param currentPageIndex The index of the currently active page (0-indexed).
 * @param onPageChange Callback when a page dot or arrow is clicked.
 * @param modifier Modifier for the container.
 * @param showArrows Whether to show previous and next arrows (default: true).
 * @param arrowsOnRight Whether to group navigation chevrons together on the right side (default: false).
 * @param variant The visual style of the indicator ([Ux4gPaginationVariant.Default] or [Ux4gPaginationVariant.Capsule]).
 * @param size The size of the component ([Ux4gPaginationSize.Small] or [Ux4gPaginationSize.Medium]).
 * @param enabled Whether the component is interactive (default: true).
 * @param activeColor Color of the active elongated dot (defaults to [Ux4gColors.primary]).
 * @param inactiveColor Background color of inactive dots and arrow containers (defaults to [Ux4gColors.primary100]).
 * @param inactiveBorderColor Border color of inactive dots (defaults to transparent).
 * @param width Optional fixed width constraint.
 * @param height Optional fixed height constraint.
 */
@Composable
fun Ux4gPaginationDotted(
    totalPageCount: Int,
    currentPageIndex: Int,
    onPageChange: (Int) -> Unit,
    modifier: Modifier = Modifier,
    showArrows: Boolean = true,
    arrowsOnRight: Boolean = false,
    variant: Ux4gPaginationVariant = Ux4gPaginationVariant.Default,
    size: Ux4gPaginationSize = Ux4gPaginationSize.Small,
    enabled: Boolean = true,
    activeColor: Color? = null,
    inactiveColor: Color? = null,
    inactiveBorderColor: Color? = null,
    width: Dp? = null,
    height: Dp? = null
) {
    val effectiveActiveColor = activeColor ?: Ux4gColors.primary
    val effectiveInactiveColor = inactiveColor ?: Ux4gColors.primary100
    val effectiveInactiveBorderColor = inactiveBorderColor ?: Color.Transparent

    val sizeModifier = Modifier
        .then(if (width != null) Modifier.width(width) else Modifier)
        .then(if (height != null) Modifier.height(height) else Modifier)

    // Dots Row
    val dotsRow = @Composable {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            for (index in 0 until totalPageCount) {
                PaginationDot(
                    isSelected = index == currentPageIndex,
                    onClick = { onPageChange(index) },
                    size = size,
                    enabled = enabled,
                    activeColor = effectiveActiveColor,
                    inactiveColor = effectiveInactiveColor,
                    inactiveBorderColor = effectiveInactiveBorderColor
                )
                if (index < totalPageCount - 1) {
                    Spacer(modifier = Modifier.width(size.spacing))
                }
            }
        }
    }

    if (arrowsOnRight) {
        // Dots Content (wrapped in Capsule container if variant == Capsule)
        val dotsContent = @Composable {
            if (variant == Ux4gPaginationVariant.Capsule) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(999.dp))
                        .background(Ux4gColors.white)
                        .padding(horizontal = 14.dp, vertical = 8.dp)
                ) {
                    dotsRow()
                }
            } else {
                dotsRow()
            }
        }

        // Arrows Group (Left chevron + spacing + Right chevron)
        val arrowsGroup = @Composable {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                PaginationArrow(
                    icon = Ux4gIcons.ic_left_arrow,
                    canClick = enabled && currentPageIndex > 0,
                    onClick = { onPageChange(currentPageIndex - 1) },
                    size = size,
                    activeColor = effectiveActiveColor,
                    inactiveColor = effectiveInactiveColor
                )
                Spacer(modifier = Modifier.width(size.spacing * 1.2f))
                PaginationArrow(
                    icon = Ux4gIcons.ic_right_arrow,
                    canClick = enabled && currentPageIndex < totalPageCount - 1,
                    onClick = { onPageChange(currentPageIndex + 1) },
                    size = size,
                    activeColor = effectiveActiveColor,
                    inactiveColor = effectiveInactiveColor
                )
            }
        }

        Row(
            modifier = modifier
                .fillMaxWidth()
                .then(sizeModifier),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            dotsContent()
            if (showArrows) {
                arrowsGroup()
            }
        }
    } else {
        val content = @Composable {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (showArrows) {
                    PaginationArrow(
                        icon = Ux4gIcons.ic_left_arrow,
                        canClick = enabled && currentPageIndex > 0,
                        onClick = { onPageChange(currentPageIndex - 1) },
                        size = size,
                        activeColor = effectiveActiveColor,
                        inactiveColor = effectiveInactiveColor
                    )
                    Spacer(modifier = Modifier.width(size.spacing))
                }

                dotsRow()

                if (showArrows) {
                    Spacer(modifier = Modifier.width(size.spacing))
                    PaginationArrow(
                        icon = Ux4gIcons.ic_right_arrow,
                        canClick = enabled && currentPageIndex < totalPageCount - 1,
                        onClick = { onPageChange(currentPageIndex + 1) },
                        size = size,
                        activeColor = effectiveActiveColor,
                        inactiveColor = effectiveInactiveColor
                    )
                }
            }
        }

        if (variant == Ux4gPaginationVariant.Capsule) {
            Box(
                modifier = modifier
                    .then(sizeModifier)
                    .clip(RoundedCornerShape(999.dp))
                    .background(Ux4gColors.white)
                    .border(
                        1.dp,
                        effectiveActiveColor.copy(alpha = 0.12f),
                        RoundedCornerShape(999.dp)
                    )
                    .padding(horizontal = 8.dp, vertical = 4.dp),
                contentAlignment = Alignment.Center
            ) {
                content()
            }
        } else {
            Box(
                modifier = modifier.then(sizeModifier),
                contentAlignment = Alignment.Center
            ) {
                content()
            }
        }
    }
}

@Composable
private fun PaginationDot(
    isSelected: Boolean,
    onClick: () -> Unit,
    size: Ux4gPaginationSize,
    enabled: Boolean,
    activeColor: Color,
    inactiveColor: Color,
    inactiveBorderColor: Color
) {
    val animatedWidth by animateDpAsState(
        targetValue = if (isSelected) size.dotSize * 2.5f else size.dotSize,
        animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing),
        label = "pagination_dot_width"
    )

    val backgroundColor = if (isSelected) {
        if (enabled) activeColor else activeColor.copy(alpha = 0.38f)
    } else {
        inactiveColor
    }

    val hasBorder = !isSelected && inactiveBorderColor != Color.Transparent

    Box(
        modifier = Modifier
            .width(animatedWidth)
            .height(size.dotSize)
            .clip(RoundedCornerShape(999.dp))
            .background(backgroundColor)
            .then(
                if (hasBorder) {
                    Modifier.border(1.dp, inactiveBorderColor, RoundedCornerShape(999.dp))
                } else Modifier
            )
            .clickable(enabled = enabled) { onClick() }
    )
}

@Composable
private fun PaginationArrow(
    icon: Ux4gIcon,
    canClick: Boolean,
    onClick: () -> Unit,
    size: Ux4gPaginationSize,
    activeColor: Color,
    inactiveColor: Color
) {
    val iconTint = if (canClick) activeColor else activeColor.copy(alpha = 0.38f)

    Box(
        modifier = Modifier
            .size(size.arrowSize)
            .clip(CircleShape)
            .background(inactiveColor)
            .clickable(enabled = canClick) { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Ux4gIconComponent(
            icon = icon,
            contentDescription = null,
            tint = iconTint,
            modifier = Modifier.size(size.arrowSize * 0.5f)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun Ux4gPaginationDottedPreview() {
    Ux4gTheme {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Ux4gPaginationDotted(
                totalPageCount = 7,
                currentPageIndex = 1,
                onPageChange = {}
            )

            Ux4gPaginationDotted(
                totalPageCount = 7,
                currentPageIndex = 1,
                onPageChange = {},
                variant = Ux4gPaginationVariant.Capsule
            )

            Ux4gPaginationDotted(
                totalPageCount = 7,
                currentPageIndex = 0,
                onPageChange = {},
                showArrows = false
            )

            Ux4gPaginationDotted(
                totalPageCount = 7,
                currentPageIndex = 2,
                onPageChange = {},
                arrowsOnRight = true,
                size = Ux4gPaginationSize.Medium
            )

            Ux4gPaginationDotted(
                totalPageCount = 7,
                currentPageIndex = 2,
                onPageChange = {},
                arrowsOnRight = true,
                variant = Ux4gPaginationVariant.Capsule,
                size = Ux4gPaginationSize.Medium
            )
        }
    }
}
