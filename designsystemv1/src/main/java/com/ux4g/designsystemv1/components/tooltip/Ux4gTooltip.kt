package com.ux4g.designsystemv1.components.tooltip

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TooltipBox
import androidx.compose.material3.TooltipState
import androidx.compose.material3.rememberTooltipState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntRect
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.PopupPositionProvider
import com.ux4g.designsystemv1.foundation.Ux4gColors
import com.ux4g.designsystemv1.foundation.Ux4gIcon
import com.ux4g.designsystemv1.foundation.Ux4gIconComponent
import com.ux4g.designsystemv1.foundation.Ux4gIcons
import com.ux4g.designsystemv1.foundation.Ux4gRadius
import com.ux4g.designsystemv1.theme.Ux4gTheme
import kotlinx.coroutines.launch
import kotlin.math.max
import kotlin.math.min

// -----------------------------------------------------------------------
// Placement Enum (Matching Flutter Ux4gTooltipPlacement)
// -----------------------------------------------------------------------

/**
 * Defines where the tooltip arrow points, which determines on which side of
 * the anchor the tooltip bubble will appear.
 *
 *  - Top*    → bubble appears ABOVE the anchor (arrow points down to anchor)
 *  - Bottom* → bubble appears BELOW the anchor (arrow points up to anchor)
 *  - Left*   → bubble appears to the LEFT of the anchor (arrow points right to anchor)
 *  - Right*  → bubble appears to the RIGHT of the anchor (arrow points left to anchor)
 */
enum class Ux4gTooltipPlacement {
    TopStart, Top, TopEnd,
    BottomStart, Bottom, BottomEnd,
    LeftStart, Left, LeftEnd,
    RightStart, Right, RightEnd
}

// -----------------------------------------------------------------------
// Custom Tooltip Shape – draws the rounded rect body + directional arrow
// -----------------------------------------------------------------------

class TooltipShape(
    private val placement: Ux4gTooltipPlacement,
    private val arrowWidth: Dp = 10.dp,
    private val arrowHeight: Dp = 6.dp,
    private val cornerRadius: Dp = Ux4gRadius.Radius4
) : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        val path = Path()
        val aw = with(density) { arrowWidth.toPx() }
        val ah = with(density) { arrowHeight.toPx() }
        val cr = with(density) { cornerRadius.toPx() }

        // Offset so the arrow tip clears the rounded corner
        val tipOffset = cr + (aw / 2f) + with(density) { 8.dp.toPx() }

        var rectLeft = 0f
        var rectTop = 0f
        var rectRight = size.width
        var rectBottom = size.height

        // Shrink the rect body on the side that holds the arrow
        when (placement) {
            Ux4gTooltipPlacement.TopStart,
            Ux4gTooltipPlacement.Top,
            Ux4gTooltipPlacement.TopEnd -> rectBottom -= ah

            Ux4gTooltipPlacement.BottomStart,
            Ux4gTooltipPlacement.Bottom,
            Ux4gTooltipPlacement.BottomEnd -> rectTop += ah

            Ux4gTooltipPlacement.LeftStart,
            Ux4gTooltipPlacement.Left,
            Ux4gTooltipPlacement.LeftEnd -> rectRight -= ah

            Ux4gTooltipPlacement.RightStart,
            Ux4gTooltipPlacement.Right,
            Ux4gTooltipPlacement.RightEnd -> rectLeft += ah
        }

        path.addRoundRect(
            RoundRect(
                left = rectLeft,
                top = rectTop,
                right = rectRight,
                bottom = rectBottom,
                cornerRadius = CornerRadius(cr, cr)
            )
        )

        // Draw the arrow triangle
        when (placement) {
            // ↓ Arrow pointing DOWN (bubble is above the anchor)
            Ux4gTooltipPlacement.TopStart,
            Ux4gTooltipPlacement.Top,
            Ux4gTooltipPlacement.TopEnd -> {
                val arrowTipY = size.height
                val arrowTipX = when (placement) {
                    Ux4gTooltipPlacement.TopStart -> min(rectLeft + tipOffset, size.width / 2f)
                    Ux4gTooltipPlacement.TopEnd   -> max(rectRight - tipOffset, size.width / 2f)
                    else                           -> size.width / 2f
                }
                path.moveTo(arrowTipX - aw / 2f, rectBottom)
                path.lineTo(arrowTipX, arrowTipY)
                path.lineTo(arrowTipX + aw / 2f, rectBottom)
            }
            // ↑ Arrow pointing UP (bubble is below the anchor)
            Ux4gTooltipPlacement.BottomStart,
            Ux4gTooltipPlacement.Bottom,
            Ux4gTooltipPlacement.BottomEnd -> {
                val arrowTipY = 0f
                val arrowTipX = when (placement) {
                    Ux4gTooltipPlacement.BottomStart -> min(rectLeft + tipOffset, size.width / 2f)
                    Ux4gTooltipPlacement.BottomEnd   -> max(rectRight - tipOffset, size.width / 2f)
                    else                              -> size.width / 2f
                }
                path.moveTo(arrowTipX - aw / 2f, rectTop)
                path.lineTo(arrowTipX, arrowTipY)
                path.lineTo(arrowTipX + aw / 2f, rectTop)
            }
            // → Arrow pointing RIGHT (bubble is to the left of the anchor)
            Ux4gTooltipPlacement.LeftStart,
            Ux4gTooltipPlacement.Left,
            Ux4gTooltipPlacement.LeftEnd -> {
                val arrowTipX = size.width
                val arrowTipY = when (placement) {
                    Ux4gTooltipPlacement.LeftStart -> min(rectTop + tipOffset, size.height / 2f)
                    Ux4gTooltipPlacement.LeftEnd   -> max(rectBottom - tipOffset, size.height / 2f)
                    else                            -> size.height / 2f
                }
                path.moveTo(rectRight, arrowTipY - aw / 2f)
                path.lineTo(arrowTipX, arrowTipY)
                path.lineTo(rectRight, arrowTipY + aw / 2f)
            }
            // ← Arrow pointing LEFT (bubble is to the right of the anchor)
            Ux4gTooltipPlacement.RightStart,
            Ux4gTooltipPlacement.Right,
            Ux4gTooltipPlacement.RightEnd -> {
                val arrowTipX = 0f
                val arrowTipY = when (placement) {
                    Ux4gTooltipPlacement.RightStart -> min(rectTop + tipOffset, size.height / 2f)
                    Ux4gTooltipPlacement.RightEnd   -> max(rectBottom - tipOffset, size.height / 2f)
                    else                             -> size.height / 2f
                }
                path.moveTo(rectLeft, arrowTipY - aw / 2f)
                path.lineTo(arrowTipX, arrowTipY)
                path.lineTo(rectLeft, arrowTipY + aw / 2f)
            }
        }
        path.close()
        return Outline.Generic(path)
    }
}

// -----------------------------------------------------------------------
// Custom Popup Position Provider
// -----------------------------------------------------------------------

class Ux4gTooltipPositionProvider(
    private val placement: Ux4gTooltipPlacement,
    private val spacing: Int = 0
) : PopupPositionProvider {
    override fun calculatePosition(
        anchorBounds: IntRect,
        windowSize: IntSize,
        layoutDirection: LayoutDirection,
        popupContentSize: IntSize
    ): IntOffset {
        var x = 0
        var y = 0

        when (placement) {
            Ux4gTooltipPlacement.TopStart -> {
                y = anchorBounds.top - popupContentSize.height - spacing
                x = anchorBounds.left
            }
            Ux4gTooltipPlacement.Top -> {
                y = anchorBounds.top - popupContentSize.height - spacing
                x = anchorBounds.left + (anchorBounds.width - popupContentSize.width) / 2
            }
            Ux4gTooltipPlacement.TopEnd -> {
                y = anchorBounds.top - popupContentSize.height - spacing
                x = anchorBounds.right - popupContentSize.width
            }
            Ux4gTooltipPlacement.BottomStart -> {
                y = anchorBounds.bottom + spacing
                x = anchorBounds.left
            }
            Ux4gTooltipPlacement.Bottom -> {
                y = anchorBounds.bottom + spacing
                x = anchorBounds.left + (anchorBounds.width - popupContentSize.width) / 2
            }
            Ux4gTooltipPlacement.BottomEnd -> {
                y = anchorBounds.bottom + spacing
                x = anchorBounds.right - popupContentSize.width
            }
            Ux4gTooltipPlacement.LeftStart -> {
                x = anchorBounds.left - popupContentSize.width - spacing
                y = anchorBounds.top
            }
            Ux4gTooltipPlacement.Left -> {
                x = anchorBounds.left - popupContentSize.width - spacing
                y = anchorBounds.top + (anchorBounds.height - popupContentSize.height) / 2
            }
            Ux4gTooltipPlacement.LeftEnd -> {
                x = anchorBounds.left - popupContentSize.width - spacing
                y = anchorBounds.bottom - popupContentSize.height
            }
            Ux4gTooltipPlacement.RightStart -> {
                x = anchorBounds.right + spacing
                y = anchorBounds.top
            }
            Ux4gTooltipPlacement.Right -> {
                x = anchorBounds.right + spacing
                y = anchorBounds.top + (anchorBounds.height - popupContentSize.height) / 2
            }
            Ux4gTooltipPlacement.RightEnd -> {
                x = anchorBounds.right + spacing
                y = anchorBounds.bottom - popupContentSize.height
            }
        }

        // Clamp to screen bounds with margin
        x = x.coerceIn(8, max(8, windowSize.width - popupContentSize.width - 8))
        y = y.coerceIn(8, max(8, windowSize.height - popupContentSize.height - 8))

        return IntOffset(x, y)
    }
}

// -----------------------------------------------------------------------
// Plain Tooltip Bubble (visual content, standalone composable)
// -----------------------------------------------------------------------

/**
 * The visual body of a tooltip bubble. Renders the rounded-rect + arrow shape
 * with optional icon, title, text, action, or custom content.
 * 
 * Direct 1:1 parity with Flutter's `_TooltipBubble`.
 */
@Composable
fun Ux4gTooltipBubble(
    text: String? = null,
    modifier: Modifier = Modifier,
    title: String? = null,
    icon: Ux4gIcon? = null,
    placement: Ux4gTooltipPlacement = Ux4gTooltipPlacement.Top,
    backgroundColor: Color = Ux4gColors.neutral700,
    contentColor: Color = Ux4gColors.neutral50,
    textStyle: TextStyle? = null,
    cornerRadius: Dp = Ux4gRadius.Radius4,
    arrowWidth: Dp = 10.dp,
    arrowHeight: Dp = 6.dp,
    action: (@Composable () -> Unit)? = null,
    customContent: (@Composable () -> Unit)? = null,
    maxWidth: Dp? = null
) {
    val style = textStyle ?: Ux4gTheme.typography.lS_default
    val shape = TooltipShape(placement, arrowWidth, arrowHeight, cornerRadius)

    val isRich = title != null || action != null
    val horizontalPadding = if (isRich) 16.dp else 12.dp
    val verticalPadding = if (isRich) 16.dp else 8.dp

    // Directional padding so inner content never overlaps the arrow tip
    val paddingStart = if (placement in listOf(Ux4gTooltipPlacement.RightStart, Ux4gTooltipPlacement.Right, Ux4gTooltipPlacement.RightEnd)) arrowHeight else 0.dp
    val paddingEnd = if (placement in listOf(Ux4gTooltipPlacement.LeftStart, Ux4gTooltipPlacement.Left, Ux4gTooltipPlacement.LeftEnd)) arrowHeight else 0.dp
    val paddingTop = if (placement in listOf(Ux4gTooltipPlacement.BottomStart, Ux4gTooltipPlacement.Bottom, Ux4gTooltipPlacement.BottomEnd)) arrowHeight else 0.dp
    val paddingBottom = if (placement in listOf(Ux4gTooltipPlacement.TopStart, Ux4gTooltipPlacement.Top, Ux4gTooltipPlacement.TopEnd)) arrowHeight else 0.dp

    Box(
        modifier = modifier
            .widthIn(max = maxWidth ?: 240.dp)
            .background(color = backgroundColor, shape = shape)
            .padding(
                start = horizontalPadding + paddingStart,
                top = verticalPadding + paddingTop,
                end = horizontalPadding + paddingEnd,
                bottom = verticalPadding + paddingBottom
            ),
        contentAlignment = Alignment.CenterStart
    ) {
        if (customContent != null) {
            customContent()
        } else if (!isRich) {
            // Plain Tooltip (Single line / small paragraph with optional icon)
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (icon != null) {
                    Ux4gIconComponent(
                        icon = icon,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = contentColor
                    )
                }
                if (text != null) {
                    Text(
                        text = text,
                        color = contentColor,
                        style = style,
                        textAlign = TextAlign.Start
                    )
                }
            }
        } else {
            // Rich Tooltip (Title, Icon, Body Text, Action Button)
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                if (title != null || icon != null) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (icon != null) {
                            Ux4gIconComponent(
                                icon = icon,
                                contentDescription = null,
                                modifier = Modifier.size(20.dp),
                                tint = contentColor
                            )
                        }
                        if (title != null) {
                            Text(
                                text = title,
                                color = contentColor,
                                style = Ux4gTheme.typography.hXXS_strong
                            )
                        }
                    }
                }
                if (text != null) {
                    Text(
                        text = text,
                        color = contentColor,
                        style = style
                    )
                }
                if (action != null) {
                    Spacer(modifier = Modifier.size(4.dp))
                    Box(modifier = Modifier.align(Alignment.End)) {
                        action()
                    }
                }
            }
        }
    }
}

// -----------------------------------------------------------------------
// Rich Tooltip Bubble (dedicated helper composable)
// -----------------------------------------------------------------------

/**
 * Convenience visual body for a rich tooltip bubble.
 */
@Composable
fun Ux4gRichTooltipBubble(
    text: String? = null,
    modifier: Modifier = Modifier,
    title: String? = null,
    icon: Ux4gIcon? = null,
    placement: Ux4gTooltipPlacement = Ux4gTooltipPlacement.Top,
    backgroundColor: Color = Ux4gColors.neutral700,
    contentColor: Color = Ux4gColors.neutral50,
    cornerRadius: Dp = Ux4gRadius.Radius4,
    arrowWidth: Dp = 10.dp,
    arrowHeight: Dp = 6.dp,
    maxWidth: Dp? = null,
    action: (@Composable () -> Unit)? = null
) {
    Ux4gTooltipBubble(
        text = text,
        modifier = modifier,
        title = title,
        icon = icon,
        placement = placement,
        backgroundColor = backgroundColor,
        contentColor = contentColor,
        cornerRadius = cornerRadius,
        arrowWidth = arrowWidth,
        arrowHeight = arrowHeight,
        action = action,
        maxWidth = maxWidth
    )
}

// -----------------------------------------------------------------------
// Ux4gTooltip – Interactive Plain / Standard Tooltip
// -----------------------------------------------------------------------

/**
 * Interactive tooltip wrapper matching Flutter's `Ux4gTooltip`.
 * Wraps [content] in Compose's [TooltipBox] so the tooltip appears as a
 * floating popup when hovered, long-pressed, or tapped.
 *
 * @param tooltipText Text to display inside the tooltip bubble.
 * @param title Optional title heading.
 * @param icon Optional leading icon.
 * @param placement Tooltip placement relative to the anchor (default: [Ux4gTooltipPlacement.Top]).
 * @param backgroundColor Background color of the tooltip bubble (default: [Ux4gColors.neutral700]).
 * @param contentColor Foreground text and icon color (default: [Ux4gColors.neutral50]).
 * @param textStyle Optional custom text style for the body text.
 * @param cornerRadius Bubble corner radius (default: 4.dp).
 * @param arrowWidth Arrow base width (default: 10.dp).
 * @param arrowHeight Arrow height (default: 6.dp).
 * @param enableUserInput Whether user interactions (hover, long press, tap) trigger the tooltip.
 * @param isPersistent Whether tooltip stays open until dismissed (default: false).
 * @param action Optional action button / composable.
 * @param customContent Optional fully custom bubble content.
 * @param maxWidth Optional max width constraint (default: 240.dp).
 * @param state Tooltip state for programmatic control.
 * @param content The anchor composable that the user interacts with.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Ux4gTooltip(
    tooltipText: String? = null,
    modifier: Modifier = Modifier,
    title: String? = null,
    icon: Ux4gIcon? = null,
    placement: Ux4gTooltipPlacement = Ux4gTooltipPlacement.Top,
    backgroundColor: Color = Ux4gColors.neutral700,
    contentColor: Color = Ux4gColors.neutral50,
    textStyle: TextStyle? = null,
    cornerRadius: Dp = Ux4gRadius.Radius4,
    arrowWidth: Dp = 10.dp,
    arrowHeight: Dp = 6.dp,
    enableUserInput: Boolean = true,
    isPersistent: Boolean = false,
    action: (@Composable () -> Unit)? = null,
    customContent: (@Composable () -> Unit)? = null,
    maxWidth: Dp? = null,
    state: TooltipState = rememberTooltipState(isPersistent = isPersistent),
    content: @Composable () -> Unit
) {
    val density = LocalDensity.current
    val spacingPx = with(density) { 8.dp.toPx().toInt() }
    val positionProvider = remember(placement, spacingPx) {
        Ux4gTooltipPositionProvider(placement, spacing = spacingPx)
    }
    val coroutineScope = rememberCoroutineScope()

    TooltipBox(
        positionProvider = positionProvider,
        tooltip = {
            Ux4gTooltipBubble(
                text = tooltipText,
                title = title,
                icon = icon,
                placement = placement,
                backgroundColor = backgroundColor,
                contentColor = contentColor,
                textStyle = textStyle,
                cornerRadius = cornerRadius,
                arrowWidth = arrowWidth,
                arrowHeight = arrowHeight,
                action = action,
                customContent = customContent,
                maxWidth = maxWidth
            )
        },
        state = state,
        modifier = modifier,
        enableUserInput = enableUserInput,
        content = {
            Box(
                modifier = if (enableUserInput) {
                    Modifier.clickable {
                        coroutineScope.launch {
                            if (state.isVisible) state.dismiss() else state.show()
                        }
                    }
                } else {
                    Modifier
                }
            ) {
                content()
            }
        }
    )
}

// -----------------------------------------------------------------------
// Ux4gRichTooltip – Interactive Rich / Persistent Tooltip
// -----------------------------------------------------------------------

/**
 * Interactive rich tooltip wrapper matching Flutter's `Ux4gRichTooltip`.
 * Defaults to `isPersistent = true` and supports title, icon, body text, and action buttons.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Ux4gRichTooltip(
    tooltipText: String? = null,
    modifier: Modifier = Modifier,
    title: String? = null,
    icon: Ux4gIcon? = null,
    placement: Ux4gTooltipPlacement = Ux4gTooltipPlacement.Top,
    backgroundColor: Color = Ux4gColors.neutral700,
    contentColor: Color = Ux4gColors.neutral50,
    textStyle: TextStyle? = null,
    cornerRadius: Dp = Ux4gRadius.Radius4,
    arrowWidth: Dp = 10.dp,
    arrowHeight: Dp = 6.dp,
    enableUserInput: Boolean = true,
    action: (@Composable () -> Unit)? = null,
    customContent: (@Composable () -> Unit)? = null,
    maxWidth: Dp? = null,
    state: TooltipState = rememberTooltipState(isPersistent = true),
    content: @Composable () -> Unit
) {
    Ux4gTooltip(
        tooltipText = tooltipText,
        modifier = modifier,
        title = title,
        icon = icon,
        placement = placement,
        backgroundColor = backgroundColor,
        contentColor = contentColor,
        textStyle = textStyle,
        cornerRadius = cornerRadius,
        arrowWidth = arrowWidth,
        arrowHeight = arrowHeight,
        enableUserInput = enableUserInput,
        isPersistent = true,
        action = action,
        customContent = customContent,
        maxWidth = maxWidth,
        state = state,
        content = content
    )
}

// -----------------------------------------------------------------------
// Previews
// -----------------------------------------------------------------------

@Preview(name = "Light", showBackground = true)
@Preview(name = "Dark", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun Ux4gTooltipBubblePreview() {
    Ux4gTheme {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Tooltip Bubble Placements", style = Ux4gTheme.typography.hS_default)

            // Top group (arrow points down)
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Ux4gTooltipBubble(text = "TopStart", icon = Ux4gIcons.info_circle, placement = Ux4gTooltipPlacement.TopStart)
                Ux4gTooltipBubble(text = "Top", icon = Ux4gIcons.info_circle, placement = Ux4gTooltipPlacement.Top)
                Ux4gTooltipBubble(text = "TopEnd", icon = Ux4gIcons.info_circle, placement = Ux4gTooltipPlacement.TopEnd)
            }

            // Bottom group (arrow points up)
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Ux4gTooltipBubble(text = "BottomStart", icon = Ux4gIcons.info_circle, placement = Ux4gTooltipPlacement.BottomStart)
                Ux4gTooltipBubble(text = "Bottom", icon = Ux4gIcons.info_circle, placement = Ux4gTooltipPlacement.Bottom)
                Ux4gTooltipBubble(text = "BottomEnd", icon = Ux4gIcons.info_circle, placement = Ux4gTooltipPlacement.BottomEnd)
            }

            // Left group (arrow points right)
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Ux4gTooltipBubble(text = "LeftStart", icon = Ux4gIcons.info_circle, placement = Ux4gTooltipPlacement.LeftStart)
                Ux4gTooltipBubble(text = "Left", icon = Ux4gIcons.info_circle, placement = Ux4gTooltipPlacement.Left)
                Ux4gTooltipBubble(text = "LeftEnd", icon = Ux4gIcons.info_circle, placement = Ux4gTooltipPlacement.LeftEnd)
            }

            // Right group (arrow points left)
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Ux4gTooltipBubble(text = "RightStart", icon = Ux4gIcons.info_circle, placement = Ux4gTooltipPlacement.RightStart)
                Ux4gTooltipBubble(text = "Right", icon = Ux4gIcons.info_circle, placement = Ux4gTooltipPlacement.Right)
                Ux4gTooltipBubble(text = "RightEnd", icon = Ux4gIcons.info_circle, placement = Ux4gTooltipPlacement.RightEnd)
            }

            Spacer(Modifier.size(8.dp))
            Text("Rich Tooltip Bubble", style = Ux4gTheme.typography.lM_strong)

            Ux4gRichTooltipBubble(
                title = "Tooltip Title",
                text = "This is a rich tooltip bubble with a title, an icon, and body text.",
                icon = Ux4gIcons.info_circle,
                placement = Ux4gTooltipPlacement.Top,
                action = {
                    Text("Action", color = Ux4gColors.primary, style = Ux4gTheme.typography.lS_strong)
                }
            )
        }
    }
}
