package com.ux4g.designsystemv1.components

import android.content.res.Configuration
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import com.ux4g.designsystemv1.foundation.Ux4gColors
import com.ux4g.designsystemv1.foundation.defaultTypography

/**
 * Orientation of the divider line.
 */
enum class Ux4gDividerOrientation {
    Horizontal,
    Vertical
}

/**
 * Visual style of the divider stroke.
 */
enum class Ux4gDividerStyle {
    Solid,
    Dashed,
    Dotted
}

/**
 * A highly customizable Divider component supporting horizontal and vertical orientations,
 * solid, dashed, and dotted styles, indents, labels (e.g. "OR" separators), and custom dimensions.
 *
 * Matches UX4G Flutter design specifications 1:1.
 *
 * @param modifier Standard Compose modifier.
 * @param orientation [Ux4gDividerOrientation.Horizontal] (default) or [Ux4gDividerOrientation.Vertical].
 * @param color Stroke color. Defaults to [Ux4gColors.neutral900] with 20% alpha.
 * @param thickness Stroke thickness. Defaults to 1.dp.
 * @param style Stroke style - [Ux4gDividerStyle.Solid], [Ux4gDividerStyle.Dashed], or [Ux4gDividerStyle.Dotted].
 * @param startIndent Indent before the line (left for horizontal, top for vertical). Defaults to 0.dp.
 * @param endIndent Indent after the line (right for horizontal, bottom for vertical). Defaults to 0.dp.
 * @param label Optional composable label rendered at the center of the divider.
 * @param labelSpacing Space between the line and the label. Defaults to 8.dp.
 * @param width Optional custom width.
 * @param height Optional custom height.
 */
@Composable
fun Ux4gDivider(
    modifier: Modifier = Modifier,
    orientation: Ux4gDividerOrientation = Ux4gDividerOrientation.Horizontal,
    color: Color = Ux4gColors.neutral900.copy(alpha = 0.2f),
    thickness: Dp = 1.dp,
    style: Ux4gDividerStyle = Ux4gDividerStyle.Solid,
    startIndent: Dp = 0.dp,
    endIndent: Dp = 0.dp,
    label: @Composable (() -> Unit)? = null,
    labelSpacing: Dp = 8.dp,
    width: Dp? = null,
    height: Dp? = null
) {
    val sizeModifier = Modifier
        .then(if (width != null) Modifier.width(width) else Modifier)
        .then(if (height != null) Modifier.height(height) else Modifier)

    if (label != null) {
        if (orientation == Ux4gDividerOrientation.Horizontal) {
            Row(
                modifier = modifier
                    .then(if (width == null) Modifier.fillMaxWidth() else Modifier)
                    .then(sizeModifier),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Ux4gDividerLine(
                    modifier = Modifier.weight(1f),
                    orientation = orientation,
                    color = color,
                    thickness = thickness,
                    style = style,
                    startIndent = startIndent,
                    endIndent = 0.dp,
                    height = height
                )
                Spacer(modifier = Modifier.width(labelSpacing))
                label()
                Spacer(modifier = Modifier.width(labelSpacing))
                Ux4gDividerLine(
                    modifier = Modifier.weight(1f),
                    orientation = orientation,
                    color = color,
                    thickness = thickness,
                    style = style,
                    startIndent = 0.dp,
                    endIndent = endIndent,
                    height = height
                )
            }
        } else {
            Column(
                modifier = modifier
                    .then(if (height == null) Modifier.fillMaxHeight() else Modifier)
                    .then(sizeModifier),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Ux4gDividerLine(
                    modifier = Modifier.weight(1f),
                    orientation = orientation,
                    color = color,
                    thickness = thickness,
                    style = style,
                    startIndent = startIndent,
                    endIndent = 0.dp,
                    width = width
                )
                Spacer(modifier = Modifier.height(labelSpacing))
                label()
                Spacer(modifier = Modifier.height(labelSpacing))
                Ux4gDividerLine(
                    modifier = Modifier.weight(1f),
                    orientation = orientation,
                    color = color,
                    thickness = thickness,
                    style = style,
                    startIndent = 0.dp,
                    endIndent = endIndent,
                    width = width
                )
            }
        }
    } else {
        Ux4gDividerLine(
            modifier = modifier.then(sizeModifier),
            orientation = orientation,
            color = color,
            thickness = thickness,
            style = style,
            startIndent = startIndent,
            endIndent = endIndent,
            width = width,
            height = height
        )
    }
}

@Composable
private fun Ux4gDividerLine(
    modifier: Modifier = Modifier,
    orientation: Ux4gDividerOrientation,
    color: Color,
    thickness: Dp,
    style: Ux4gDividerStyle,
    startIndent: Dp,
    endIndent: Dp,
    width: Dp? = null,
    height: Dp? = null
) {
    if (orientation == Ux4gDividerOrientation.Horizontal) {
        val baseModifier = if (width == null) modifier.fillMaxWidth() else modifier
        Canvas(
            modifier = baseModifier.height(height ?: thickness)
        ) {
            val strokeWidthPx = thickness.toPx()
            val startPx = startIndent.toPx()
            val endPx = (size.width - endIndent.toPx()).coerceAtLeast(startPx)
            val y = size.height / 2f

            val pathEffect: PathEffect? = when (style) {
                Ux4gDividerStyle.Solid -> null
                Ux4gDividerStyle.Dashed -> PathEffect.dashPathEffect(
                    floatArrayOf(12.dp.toPx(), 8.dp.toPx()),
                    0f
                )
                Ux4gDividerStyle.Dotted -> PathEffect.dashPathEffect(
                    floatArrayOf(4.dp.toPx(), 4.dp.toPx()),
                    0f
                )
            }

            drawLine(
                color = color,
                start = Offset(startPx, y),
                end = Offset(endPx, y),
                strokeWidth = strokeWidthPx,
                pathEffect = pathEffect
            )
        }
    } else {
        val baseModifier = if (height == null) modifier.fillMaxHeight() else modifier
        Canvas(
            modifier = baseModifier.width(width ?: thickness)
        ) {
            val strokeWidthPx = thickness.toPx()
            val startPx = startIndent.toPx()
            val endPx = (size.height - endIndent.toPx()).coerceAtLeast(startPx)
            val x = size.width / 2f

            val pathEffect: PathEffect? = when (style) {
                Ux4gDividerStyle.Solid -> null
                Ux4gDividerStyle.Dashed -> PathEffect.dashPathEffect(
                    floatArrayOf(12.dp.toPx(), 8.dp.toPx()),
                    0f
                )
                Ux4gDividerStyle.Dotted -> PathEffect.dashPathEffect(
                    floatArrayOf(4.dp.toPx(), 4.dp.toPx()),
                    0f
                )
            }

            drawLine(
                color = color,
                start = Offset(x, startPx),
                end = Offset(x, endPx),
                strokeWidth = strokeWidthPx,
                pathEffect = pathEffect
            )
        }
    }
}

// ═══════════════════════════════════════════════════════
// Previews
// ═══════════════════════════════════════════════════════

@Preview(name = "Divider - Light", showBackground = true, backgroundColor = 0xFFFFFFFF)
@Preview(name = "Divider - Dark", uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true, backgroundColor = 0xFF121212)
@Composable
private fun Ux4gDividerPreview() {
    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        // Horizontal variants
        Text("Horizontal - Solid (Default)", style = defaultTypography.lS_default)
        Ux4gDivider()

        Text("Horizontal - Dashed", style = defaultTypography.lS_default)
        Ux4gDivider(style = Ux4gDividerStyle.Dashed)

        Text("Horizontal - Dotted", style = defaultTypography.lS_default)
        Ux4gDivider(style = Ux4gDividerStyle.Dotted)

        Text("Horizontal - Custom Color & Thickness (2dp)", style = defaultTypography.lS_default)
        Ux4gDivider(thickness = 2.dp, color = Ux4gColors.primary)

        Text("Horizontal - With Start & End Indent (32dp)", style = defaultTypography.lS_default)
        Ux4gDivider(startIndent = 32.dp, endIndent = 32.dp)

        Text("Horizontal - With Center Label (OR)", style = defaultTypography.lS_default)
        Ux4gDivider(
            label = {
                Text(
                    text = "OR",
                    style = defaultTypography.lS_strong,
                    color = Ux4gColors.neutral600
                )
            }
        )

        Text("Horizontal - Dashed with Brand Label", style = defaultTypography.lS_default)
        Ux4gDivider(
            style = Ux4gDividerStyle.Dashed,
            color = Ux4gColors.primary300,
            thickness = 1.5.dp,
            label = {
                Text(
                    text = "SECTION",
                    style = defaultTypography.lS_strong,
                    color = Ux4gColors.primary
                )
            }
        )

        // Vertical variants
        Text("Vertical - Solid / Dashed / Dotted", style = defaultTypography.lS_default)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp),
            horizontalArrangement = Arrangement.spacedBy(24.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Left", style = defaultTypography.bS_default)
            Ux4gDivider(orientation = Ux4gDividerOrientation.Vertical)
            Text("Middle", style = defaultTypography.bS_default)
            Ux4gDivider(
                orientation = Ux4gDividerOrientation.Vertical,
                style = Ux4gDividerStyle.Dashed
            )
            Text("Right", style = defaultTypography.bS_default)
            Ux4gDivider(
                orientation = Ux4gDividerOrientation.Vertical,
                style = Ux4gDividerStyle.Dotted,
                color = Ux4gColors.primary
            )
            Text("End", style = defaultTypography.bS_default)
        }
    }
}
