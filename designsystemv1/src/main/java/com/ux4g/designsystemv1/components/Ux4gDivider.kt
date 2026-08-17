package com.ux4g.designsystemv1.components

import android.content.res.Configuration
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.ux4g.designsystemv1.foundation.Ux4gBorderWidth
import com.ux4g.designsystemv1.foundation.Ux4gColors
import com.ux4g.designsystemv1.theme.Ux4gTheme

// ─────────────────────────────────────────────────────────────────────────────
// Enum: Orientation
// ─────────────────────────────────────────────────────────────────────────────

/**
 * Orientation of the divider line.
 */
enum class Ux4gDividerOrientation {
    /** A full-width horizontal rule (default). */
    Horizontal,
    /** A full-height vertical rule. */
    Vertical
}

// ─────────────────────────────────────────────────────────────────────────────
// Enum: Line Style
// ─────────────────────────────────────────────────────────────────────────────

/**
 * Visual style of the divider stroke.
 */
enum class Ux4gDividerStyle {
    /** Continuous solid line (default). */
    Solid,
    /** Evenly-spaced dashes. */
    Dashed,
    /** Closely-spaced dots. */
    Dotted
}

// ─────────────────────────────────────────────────────────────────────────────
// Composable: Ux4gDivider
// ─────────────────────────────────────────────────────────────────────────────

/**
 * A flexible divider that renders a horizontal or vertical separator line.
 *
 * Matches the UX4G Design System specification:
 *   - Width / Height : Fill (expands to parent)
 *   - Thickness      : 1 dp  (Ux4gBorderWidth.Thin by default)
 *   - Color          : Border/Neutral/Subtle → Ux4gTheme.colorScheme.onSurface.copy(alpha = 0.2f)
 *   - Alignment      : Center (line is centred within its layout slot)
 *
 * @param modifier            Standard Compose modifier applied to the outer container.
 * @param orientation         [Ux4gDividerOrientation.Horizontal] (default) or
 *                            [Ux4gDividerOrientation.Vertical].
 * @param color               Stroke color. Defaults to [Ux4gTheme.colorScheme.onSurface.copy(alpha = 0.2f)].
 * @param thickness           Stroke thickness. Defaults to [Ux4gBorderWidth.Thin] (1 dp).
 * @param style               Stroke style – Solid, Dashed, or Dotted.
 * @param startIndent         Empty space before the line (left for horizontal, top for vertical).
 * @param endIndent           Empty space after the line (right for horizontal, bottom for vertical).
 * @param label               Optional composable label rendered at the centre of the divider.
 *                            Useful for "OR" separators.
 * @param labelSpacing        Horizontal (or vertical) gap between the line and the label.
 */
@Composable
fun Ux4gDivider(
    modifier: Modifier = Modifier,
    orientation: Ux4gDividerOrientation = Ux4gDividerOrientation.Horizontal,
    color: Color = Ux4gTheme.colorScheme.onSurface.copy(alpha = 0.2f),
    thickness: Dp = Ux4gBorderWidth.Thin,
    style: Ux4gDividerStyle = Ux4gDividerStyle.Solid,
    startIndent: Dp = 0.dp,
    endIndent: Dp = 0.dp,
    label: @Composable (() -> Unit)? = null,
    labelSpacing: Dp = 8.dp
) {
    if (label != null) {
        // When a label is present, wrap [line – label – line] in a row / column.
        when (orientation) {
            Ux4gDividerOrientation.Horizontal -> {
                Row(
                    modifier = modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(labelSpacing)
                ) {
                    Ux4gDividerLine(
                        modifier = Modifier.weight(1f),
                        orientation = orientation,
                        color = color,
                        thickness = thickness,
                        style = style,
                        startIndent = startIndent,
                        endIndent = 0.dp
                    )
                    label()
                    Ux4gDividerLine(
                        modifier = Modifier.weight(1f),
                        orientation = orientation,
                        color = color,
                        thickness = thickness,
                        style = style,
                        startIndent = 0.dp,
                        endIndent = endIndent
                    )
                }
            }

            Ux4gDividerOrientation.Vertical -> {
                Column(
                    modifier = modifier.fillMaxHeight(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(labelSpacing)
                ) {
                    Ux4gDividerLine(
                        modifier = Modifier.weight(1f),
                        orientation = orientation,
                        color = color,
                        thickness = thickness,
                        style = style,
                        startIndent = startIndent,
                        endIndent = 0.dp
                    )
                    label()
                    Ux4gDividerLine(
                        modifier = Modifier.weight(1f),
                        orientation = orientation,
                        color = color,
                        thickness = thickness,
                        style = style,
                        startIndent = 0.dp,
                        endIndent = endIndent
                    )
                }
            }
        }
    } else {
        Ux4gDividerLine(
            modifier = modifier,
            orientation = orientation,
            color = color,
            thickness = thickness,
            style = style,
            startIndent = startIndent,
            endIndent = endIndent
        )
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Internal: bare line helper
// ─────────────────────────────────────────────────────────────────────────────

@Composable
private fun Ux4gDividerLine(
    modifier: Modifier = Modifier,
    orientation: Ux4gDividerOrientation,
    color: Color,
    thickness: Dp,
    style: Ux4gDividerStyle,
    startIndent: Dp,
    endIndent: Dp
) {
    val pathEffect: PathEffect? = when (style) {
        Ux4gDividerStyle.Solid  -> null
        Ux4gDividerStyle.Dashed -> PathEffect.dashPathEffect(floatArrayOf(12f, 8f), 0f)
        Ux4gDividerStyle.Dotted -> PathEffect.dashPathEffect(floatArrayOf(4f, 4f), 0f)
    }

    when (orientation) {
        Ux4gDividerOrientation.Horizontal -> {
            Canvas(
                modifier = modifier
                    .fillMaxWidth()
                    .height(thickness)
            ) {
                val strokeWidthPx = thickness.toPx()
                val startPx      = startIndent.toPx()
                val endPx        = size.width - endIndent.toPx()
                val y            = size.height / 2f

                drawLine(
                    color       = color,
                    start       = Offset(startPx, y),
                    end         = Offset(endPx, y),
                    strokeWidth = strokeWidthPx,
                    pathEffect  = pathEffect
                )
            }
        }

        Ux4gDividerOrientation.Vertical -> {
            Canvas(
                modifier = modifier
                    .fillMaxHeight()
                    .width(thickness)
            ) {
                val strokeWidthPx = thickness.toPx()
                val startPx       = startIndent.toPx()
                val endPx         = size.height - endIndent.toPx()
                val x             = size.width / 2f

                drawLine(
                    color       = color,
                    start       = Offset(x, startPx),
                    end         = Offset(x, endPx),
                    strokeWidth = strokeWidthPx,
                    pathEffect  = pathEffect
                )
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Previews & Examples
// ─────────────────────────────────────────────────────────────────────────────

@Preview(name = "Light", showBackground = true)
@Preview(name = "Dark", uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
private fun Ux4gDividerPreview() {
    Ux4gTheme {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {

            // ── Horizontal variants ─────────────────────────────────────────

            Text("Horizontal – Solid (default)", style = Ux4gTheme.typography.lS_default)
            Ux4gDivider()

            Text("Horizontal – Dashed", style = Ux4gTheme.typography.lS_default)
            Ux4gDivider(style = Ux4gDividerStyle.Dashed)

            Text("Horizontal – Dotted", style = Ux4gTheme.typography.lS_default)
            Ux4gDivider(style = Ux4gDividerStyle.Dotted)

            Text("Horizontal – Thick (2 dp) Primary", style = Ux4gTheme.typography.lS_default)
            Ux4gDivider(thickness = 2.dp, color = Ux4gTheme.colorScheme.primary)

            Text("Horizontal – With Start Indent 32 dp", style = Ux4gTheme.typography.lS_default)
            Ux4gDivider(startIndent = 32.dp)

            Text("Horizontal – With Label (OR)", style = Ux4gTheme.typography.lS_default)
            Ux4gDivider(
                label = {
                    Text(
                        text = "OR",
                        style = Ux4gTheme.typography.lS_strong,
                        color = Ux4gTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                    )
                }
            )

            Text("Horizontal – Dashed, Brand Color, Label", style = Ux4gTheme.typography.lS_default)
            Ux4gDivider(
                style = Ux4gDividerStyle.Dashed,
                color = Ux4gColors.primary300,
                thickness = 1.5.dp,
                label = {
                    Text(
                        text = "SECTION",
                        style = Ux4gTheme.typography.lS_strong,
                        color = Ux4gTheme.colorScheme.primary
                    )
                }
            )

            // ── Vertical variants ───────────────────────────────────────────

            Text("Vertical – Solid / Dashed / Dotted", style = Ux4gTheme.typography.lS_default)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp),
                horizontalArrangement = Arrangement.spacedBy(24.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Left", style = Ux4gTheme.typography.bS_default)
                Ux4gDivider(orientation = Ux4gDividerOrientation.Vertical)
                Text("Middle", style = Ux4gTheme.typography.bS_default)
                Ux4gDivider(
                    orientation = Ux4gDividerOrientation.Vertical,
                    style = Ux4gDividerStyle.Dashed
                )
                Text("Right", style = Ux4gTheme.typography.bS_default)
                Ux4gDivider(
                    orientation = Ux4gDividerOrientation.Vertical,
                    style = Ux4gDividerStyle.Dotted,
                    color = Ux4gTheme.colorScheme.primary
                )
                Text("End", style = Ux4gTheme.typography.bS_default)
            }

            Text("Vertical – Thick (3 dp) Secondary", style = Ux4gTheme.typography.lS_default)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Item A", style = Ux4gTheme.typography.bS_default)
                Ux4gDivider(
                    orientation = Ux4gDividerOrientation.Vertical,
                    thickness = 3.dp,
                    color = Ux4gTheme.colorScheme.secondary
                )
                Text("Item B", style = Ux4gTheme.typography.bS_default)
            }
        }
    }
}
