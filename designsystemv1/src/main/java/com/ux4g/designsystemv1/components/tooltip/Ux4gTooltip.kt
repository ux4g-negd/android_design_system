package com.ux4g.designsystemv1.components.tooltip

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TooltipBox
import androidx.compose.material3.TooltipState
import androidx.compose.material3.rememberTooltipState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
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
import com.ux4g.designsystemv1.foundation.ExperimentalUx4gApi
import com.ux4g.designsystemv1.foundation.Ux4gIcon
import com.ux4g.designsystemv1.foundation.Ux4gIconComponent
import com.ux4g.designsystemv1.foundation.Ux4gIcons
import com.ux4g.designsystemv1.foundation.Ux4gColors
import com.ux4g.designsystemv1.foundation.Ux4gRadius
import com.ux4g.designsystemv1.theme.Ux4gTheme

// -----------------------------------------------------------------------
// Placement Enum
// -----------------------------------------------------------------------

/**
 * Defines where the tooltip arrow points, which determines on which side of
 * the anchor the tooltip bubble will appear.
 *
 *  - Top*   → bubble appears ABOVE the anchor (arrow points down)
 *  - Bottom*→ bubble appears BELOW the anchor (arrow points up)
 *  - Left*  → bubble appears to the LEFT of the anchor (arrow points right)
 *  - Right* → bubble appears to the RIGHT of the anchor (arrow points left)
 */
enum class Ux4gTooltipPlacement {
    TopStart, Top, TopEnd,
    BottomStart, Bottom, BottomEnd,
    LeftStart, Left, LeftEnd,
    RightStart, Right, RightEnd
}

// -----------------------------------------------------------------------
// Custom Shape – draws the rounded rect body + directional arrow
// -----------------------------------------------------------------------

class TooltipShape(
    private val placement: Ux4gTooltipPlacement,
    private val arrowWidth: Dp,
    private val arrowHeight: Dp,
    private val cornerRadius: Dp
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

        // Minimum horizontal/vertical offset so the arrow tip clears the rounded corner
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
                    Ux4gTooltipPlacement.TopStart -> (rectLeft + tipOffset).coerceAtMost(size.width / 2f)
                    Ux4gTooltipPlacement.TopEnd   -> (rectRight - tipOffset).coerceAtLeast(size.width / 2f)
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
                    Ux4gTooltipPlacement.BottomStart -> (rectLeft + tipOffset).coerceAtMost(size.width / 2f)
                    Ux4gTooltipPlacement.BottomEnd   -> (rectRight - tipOffset).coerceAtLeast(size.width / 2f)
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
                    Ux4gTooltipPlacement.LeftStart -> (rectTop + tipOffset).coerceAtMost(size.height / 2f)
                    Ux4gTooltipPlacement.LeftEnd   -> (rectBottom - tipOffset).coerceAtLeast(size.height / 2f)
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
                    Ux4gTooltipPlacement.RightStart -> (rectTop + tipOffset).coerceAtMost(size.height / 2f)
                    Ux4gTooltipPlacement.RightEnd   -> (rectBottom - tipOffset).coerceAtLeast(size.height / 2f)
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

        return IntOffset(x, y)
    }
}

// -----------------------------------------------------------------------
// Plain Tooltip Bubble (visual content only, standalone composable)
// -----------------------------------------------------------------------

/**
 * The visual body of a plain tooltip. Renders the rounded-rect + arrow shape
 * with optional icon and text.
 *
 * Normally you don't call this directly – use [Ux4gTooltip] which wraps it
 * inside a [TooltipBox] so it floats above the UI as a popup.
 */
@Composable
fun Ux4gTooltipBubble(
    text: String,
    modifier: Modifier = Modifier,
    icon: Ux4gIcon? = null,
    placement: Ux4gTooltipPlacement = Ux4gTooltipPlacement.Top,
    backgroundColor: Color = Ux4gTheme.colorScheme.onSurface,
    contentColor: Color = Ux4gTheme.colorScheme.surface,
    textStyle: TextStyle? = null,
    cornerRadius: Dp = Ux4gRadius.Radius4,
    arrowWidth: Dp = 10.dp,
    arrowHeight: Dp = 6.dp
) {
    val style = textStyle ?: Ux4gTheme.typography.lS_default
    val shape = TooltipShape(placement, arrowWidth, arrowHeight, cornerRadius)

    // Add padding on the arrow side so content doesn't overlap the arrow tip
    val paddingStart   = if (placement in listOf(Ux4gTooltipPlacement.RightStart, Ux4gTooltipPlacement.Right, Ux4gTooltipPlacement.RightEnd)) arrowHeight else 0.dp
    val paddingEnd     = if (placement in listOf(Ux4gTooltipPlacement.LeftStart, Ux4gTooltipPlacement.Left, Ux4gTooltipPlacement.LeftEnd)) arrowHeight else 0.dp
    val paddingTop     = if (placement in listOf(Ux4gTooltipPlacement.BottomStart, Ux4gTooltipPlacement.Bottom, Ux4gTooltipPlacement.BottomEnd)) arrowHeight else 0.dp
    val paddingBottom  = if (placement in listOf(Ux4gTooltipPlacement.TopStart, Ux4gTooltipPlacement.Top, Ux4gTooltipPlacement.TopEnd)) arrowHeight else 0.dp

    Box(
        modifier = modifier
            .background(color = backgroundColor, shape = shape)
            .padding(start = paddingStart, top = paddingTop, end = paddingEnd, bottom = paddingBottom)
            .padding(horizontal = 12.dp, vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
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
            Text(
                text = text,
                color = contentColor,
                style = style,
                textAlign = TextAlign.Start
            )
        }
    }
}

// -----------------------------------------------------------------------
// Rich Tooltip Bubble (visual content only, standalone composable)
// -----------------------------------------------------------------------

/**
 * The visual body of a rich tooltip. Supports an optional title, icon,
 * body text, and an action composable.
 *
 * Normally you don't call this directly – use [Ux4gRichTooltip].
 */
@Composable
fun Ux4gRichTooltipBubble(
    text: String,
    modifier: Modifier = Modifier,
    title: String? = null,
    icon: Ux4gIcon? = null,
    placement: Ux4gTooltipPlacement = Ux4gTooltipPlacement.Top,
    backgroundColor: Color = Ux4gTheme.colorScheme.onSurface,
    contentColor: Color = Ux4gTheme.colorScheme.surface,
    cornerRadius: Dp = Ux4gRadius.Radius4,
    arrowWidth: Dp = 10.dp,
    arrowHeight: Dp = 6.dp,
    action: (@Composable () -> Unit)? = null
) {
    val shape = TooltipShape(placement, arrowWidth, arrowHeight, cornerRadius)

    val paddingStart   = if (placement in listOf(Ux4gTooltipPlacement.RightStart, Ux4gTooltipPlacement.Right, Ux4gTooltipPlacement.RightEnd)) arrowHeight else 0.dp
    val paddingEnd     = if (placement in listOf(Ux4gTooltipPlacement.LeftStart, Ux4gTooltipPlacement.Left, Ux4gTooltipPlacement.LeftEnd)) arrowHeight else 0.dp
    val paddingTop     = if (placement in listOf(Ux4gTooltipPlacement.BottomStart, Ux4gTooltipPlacement.Bottom, Ux4gTooltipPlacement.BottomEnd)) arrowHeight else 0.dp
    val paddingBottom  = if (placement in listOf(Ux4gTooltipPlacement.TopStart, Ux4gTooltipPlacement.Top, Ux4gTooltipPlacement.TopEnd)) arrowHeight else 0.dp

    Box(
        modifier = modifier
            .background(color = backgroundColor, shape = shape)
            .padding(start = paddingStart, top = paddingTop, end = paddingEnd, bottom = paddingBottom)
            .padding(16.dp)
    ) {
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
            Text(
                text = text,
                color = contentColor,
                style = Ux4gTheme.typography.lS_default
            )
            if (action != null) {
                Spacer(modifier = Modifier.size(4.dp))
                Box(modifier = Modifier.align(Alignment.End)) {
                    action()
                }
            }
        }
    }
}

// -----------------------------------------------------------------------
// Ux4gTooltip – Interactive wrapper (plain)
// -----------------------------------------------------------------------

/**
 * Wraps [content] in Material 3's [TooltipBox] so the tooltip appears as a
 * **floating popup** (does NOT shift any layout) when the user:
 *  - **Long-presses** the content on touch screens, or
 *  - **Hovers** the cursor over it on desktop/emulator.
 *
 * The tooltip auto-dismisses after a short delay (plain) or stays until the
 * user taps elsewhere / calls `state.dismiss()` (rich / persistent).
 *
 * @param tooltipText Text to display inside the tooltip bubble.
 * @param modifier    Modifier applied to the [TooltipBox] itself.
 * @param icon        Optional leading icon shown inside the bubble.
 * @param placement   Which side of the anchor the tooltip should appear on.
 * @param state       Tooltip state – use [rememberTooltipState] or pass your own for programmatic control.
 * @param enableUserInput When true (default) long-press / hover gesture triggers the tooltip.
 * @param content     The composable that the user interacts with (button, icon, etc.).
 */
@OptIn(ExperimentalMaterial3Api::class)
@ExperimentalUx4gApi
@Composable
fun Ux4gTooltip(
    tooltipText: String,
    modifier: Modifier = Modifier,
    icon: Ux4gIcon? = null,
    placement: Ux4gTooltipPlacement = Ux4gTooltipPlacement.Top,
    backgroundColor: Color = Ux4gTheme.colorScheme.onSurface,
    contentColor: Color = Ux4gTheme.colorScheme.surface,
    textStyle: TextStyle? = null,
    cornerRadius: Dp = Ux4gRadius.Radius4,
    arrowWidth: Dp = 10.dp,
    arrowHeight: Dp = 6.dp,
    state: TooltipState = rememberTooltipState(),
    enableUserInput: Boolean = true,
    content: @Composable () -> Unit
) {
    val density = LocalDensity.current
    val spacingPx = with(density) { 8.dp.toPx().toInt() }
    val positionProvider = remember(placement, spacingPx) {
        Ux4gTooltipPositionProvider(placement, spacing = spacingPx)
    }

    TooltipBox(
        positionProvider = positionProvider,
        tooltip = {
            Ux4gTooltipBubble(
                text = tooltipText,
                icon = icon,
                placement = placement,
                backgroundColor = backgroundColor,
                contentColor = contentColor,
                textStyle = textStyle,
                cornerRadius = cornerRadius,
                arrowWidth = arrowWidth,
                arrowHeight = arrowHeight
            )
        },
        state = state,
        modifier = modifier,
        enableUserInput = enableUserInput,
        content = content
    )
}

// -----------------------------------------------------------------------
// Ux4gRichTooltip – Interactive wrapper (rich / persistent)
// -----------------------------------------------------------------------

/**
 * Wraps [content] in Material 3's [TooltipBox] using a **rich tooltip** bubble
 * that can display a title, icon, body text, and an action button.
 *
 * Rich tooltips are **persistent** by default – they stay visible until the
 * user taps outside or the action calls `state.dismiss()`.
 *
 * @param tooltipText Main body text.
 * @param title       Optional heading shown above the body.
 * @param icon        Optional icon next to the title.
 * @param action      Optional composable shown at the bottom (e.g. a button).
 * @param placement   Which side of the anchor the tooltip appears on.
 * @param state       Tooltip state – defaults to `rememberTooltipState(isPersistent = true)`.
 * @param content     The anchor composable.
 */
@OptIn(ExperimentalMaterial3Api::class)
@ExperimentalUx4gApi
@Composable
fun Ux4gRichTooltip(
    tooltipText: String,
    modifier: Modifier = Modifier,
    title: String? = null,
    icon: Ux4gIcon? = null,
    placement: Ux4gTooltipPlacement = Ux4gTooltipPlacement.Top,
    backgroundColor: Color = Ux4gTheme.colorScheme.onSurface,
    contentColor: Color = Ux4gTheme.colorScheme.surface,
    cornerRadius: Dp = Ux4gRadius.Radius4,
    arrowWidth: Dp = 10.dp,
    arrowHeight: Dp = 6.dp,
    state: TooltipState = rememberTooltipState(isPersistent = true),
    enableUserInput: Boolean = true,
    action: (@Composable () -> Unit)? = null,
    content: @Composable () -> Unit
) {
    val density = LocalDensity.current
    val spacingPx = with(density) { 8.dp.toPx().toInt() }
    val positionProvider = remember(placement, spacingPx) {
        Ux4gTooltipPositionProvider(placement, spacing = spacingPx)
    }

    TooltipBox(
        positionProvider = positionProvider,
        tooltip = {
            Ux4gRichTooltipBubble(
                text = tooltipText,
                title = title,
                icon = icon,
                placement = placement,
                backgroundColor = backgroundColor,
                contentColor = contentColor,
                cornerRadius = cornerRadius,
                arrowWidth = arrowWidth,
                arrowHeight = arrowHeight,
                action = action
            )
        },
        state = state,
        modifier = modifier,
        enableUserInput = enableUserInput,
        content = content
    )
}

// -----------------------------------------------------------------------
// Previews
// -----------------------------------------------------------------------

@OptIn(ExperimentalMaterial3Api::class)
@Preview(name = "Light", showBackground = true)
@Preview(name = "Dark", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun Ux4gTooltipBubblePreview() {
    Ux4gTheme {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Tooltip Bubble Placements", style = Ux4gTheme.typography.hS_default)

            // Top group (arrow points down)
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Ux4gTooltipBubble("TopStart", icon = Ux4gIcons.info_circle, placement = Ux4gTooltipPlacement.TopStart)
                Ux4gTooltipBubble("Top",      icon = Ux4gIcons.info_circle, placement = Ux4gTooltipPlacement.Top)
                Ux4gTooltipBubble("TopEnd",   icon = Ux4gIcons.info_circle, placement = Ux4gTooltipPlacement.TopEnd)
            }

            // Bottom group (arrow points up)
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Ux4gTooltipBubble("BottomStart", icon = Ux4gIcons.info_circle, placement = Ux4gTooltipPlacement.BottomStart)
                Ux4gTooltipBubble("Bottom",      icon = Ux4gIcons.info_circle, placement = Ux4gTooltipPlacement.Bottom)
                Ux4gTooltipBubble("BottomEnd",   icon = Ux4gIcons.info_circle, placement = Ux4gTooltipPlacement.BottomEnd)
            }

            // Left group (arrow points right)
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Ux4gTooltipBubble("LeftStart", icon = Ux4gIcons.info_circle, placement = Ux4gTooltipPlacement.LeftStart)
                Ux4gTooltipBubble("Left",      icon = Ux4gIcons.info_circle, placement = Ux4gTooltipPlacement.Left)
                Ux4gTooltipBubble("LeftEnd",   icon = Ux4gIcons.info_circle, placement = Ux4gTooltipPlacement.LeftEnd)
            }

            // Right group (arrow points left)
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Ux4gTooltipBubble("RightStart", icon = Ux4gIcons.info_circle, placement = Ux4gTooltipPlacement.RightStart)
                Ux4gTooltipBubble("Right",      icon = Ux4gIcons.info_circle, placement = Ux4gTooltipPlacement.Right)
                Ux4gTooltipBubble("RightEnd",   icon = Ux4gIcons.info_circle, placement = Ux4gTooltipPlacement.RightEnd)
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
