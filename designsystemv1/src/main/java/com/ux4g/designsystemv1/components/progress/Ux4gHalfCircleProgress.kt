package com.ux4g.designsystemv1.components.progress

import android.content.res.Configuration
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.isSystemInDarkTheme
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
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ux4g.designsystemv1.foundation.Ux4gColors
import com.ux4g.designsystemv1.foundation.Ux4gSpace
import com.ux4g.designsystemv1.theme.Ux4gTheme
import kotlin.math.roundToInt

// ─── Size Enum ────────────────────────────────────────────────────────────────

/**
 * Size variants for [Ux4gHalfCircleProgress].
 */
enum class Ux4gHalfCircleProgressSize(val width: Dp) {
    S(80.dp),
    M(140.dp),
    L(200.dp),
    XL(280.dp);

    companion object {
        val s: Ux4gHalfCircleProgressSize get() = S
        val m: Ux4gHalfCircleProgressSize get() = M
        val l: Ux4gHalfCircleProgressSize get() = L
        val xl: Ux4gHalfCircleProgressSize get() = XL
    }
}

// ─── Component ────────────────────────────────────────────────────────────────

/**
 * A half-circle (gauge / semicircle) progress indicator following the UX4G Design System
 * matching Flutter's `Ux4gHalfCircleProgress`.
 *
 * The gauge sweeps from 0% (left) to 100% (right) in a 180° arc.
 * Percentage text and description auto-scale based on [size] / [width].
 *
 * Two visual variants controlled by [showScale]:
 * - **Without scale** (default): clean arc with percentage & description.
 * - **With scale**: adds 0% and 100% labels at the arc ends.
 *
 * @param value Progress value between 0.0f (empty) and 1.0f (full).
 * @param modifier Modifier to be applied to the half circle progress layout.
 * @param size Predefined size variant ([Ux4gHalfCircleProgressSize.S], [Ux4gHalfCircleProgressSize.M], [Ux4gHalfCircleProgressSize.L], [Ux4gHalfCircleProgressSize.XL]).
 * @param width Custom width override (height is calculated automatically).
 * @param strokeWidth Custom stroke width for the arc.
 * @param progressColor Solid color of the progress arc.
 * @param trackColor Color of the track (background arc).
 * @param gradientColors Optional gradient colors for the progress arc.
 * @param progressBrush Optional custom brush for the progress arc.
 * @param strokeCap Stroke cap style for the arcs (defaults to [StrokeCap.Round]).
 * @param valueText Text shown as the main value (defaults to percentage, e.g. "75%").
 * @param description Description text shown below the value.
 * @param showScale Whether to show 0% and 100% labels at the arc ends.
 * @param valueStyle Custom text style for the value text.
 * @param descriptionStyle Custom text style for the description.
 * @param scaleStyle Custom text style for scale labels.
 * @param gap Gap between arc and text content (defaults to [Ux4gSpace.Space4]).
 */
@Composable
fun Ux4gHalfCircleProgress(
    value: Float,
    modifier: Modifier = Modifier,
    size: Ux4gHalfCircleProgressSize = Ux4gHalfCircleProgressSize.L,
    width: Dp? = null,
    strokeWidth: Dp? = null,
    progressColor: Color? = null,
    trackColor: Color? = null,
    gradientColors: List<Color>? = null,
    progressBrush: Brush? = null,
    strokeCap: StrokeCap = StrokeCap.Round,
    valueText: String? = null,
    description: String? = null,
    showScale: Boolean = false,
    valueStyle: TextStyle? = null,
    descriptionStyle: TextStyle? = null,
    scaleStyle: TextStyle? = null,
    gap: Dp = Ux4gSpace.Space4,
) {
    val isDark = isSystemInDarkTheme()
    val clampedValue = value.coerceIn(0f, 1f)
    val percent = (clampedValue * 100).roundToInt()
    val displayValue = valueText ?: "$percent%"

    // Dimensions calculations
    val w = width ?: size.width
    val sw = strokeWidth ?: (w.value * 0.08f).coerceIn(4f, 22f).dp
    val arcHeight = (w / 2) + (sw / 2)

    // Font size auto-scaling formulas matching Flutter 1:1
    val valueFontSize = (w.value * 0.18f).coerceIn(10f, 48f).sp
    val descriptionFontSize = (w.value * 0.10f).coerceIn(7f, 20f).sp
    val scaleFontSize = (w.value * 0.075f).coerceIn(6f, 14f).sp

    // Color resolution
    val onSurface = Ux4gTheme.colorScheme.onSurface
    val primaryColor = Ux4gTheme.colorScheme.primary

    val resolvedProgressColor = progressColor ?: primaryColor
    val resolvedTrackColor = trackColor ?: onSurface.copy(alpha = 0.16f)

    val resolvedBrush: Brush = progressBrush ?: if (gradientColors != null && gradientColors.size >= 2) {
        Brush.horizontalGradient(gradientColors)
    } else if (progressColor == null) {
        val defaultGrad = if (isDark) {
            listOf(Ux4gColors.primary300, Ux4gColors.primary950)
        } else {
            listOf(Ux4gColors.primary600, Ux4gColors.primary200)
        }
        Brush.horizontalGradient(defaultGrad)
    } else {
        SolidColor(resolvedProgressColor)
    }

    // Typography styling matching UX4G tokens
    val resolvedValueStyle = (valueStyle ?: Ux4gTheme.typography.bS_strong).copy(
        fontSize = valueFontSize,
        fontWeight = FontWeight.Bold,
        lineHeight = valueFontSize * 1.1f,
        color = onSurface
    )

    val resolvedDescriptionStyle = (descriptionStyle ?: Ux4gTheme.typography.bXS_default).copy(
        fontSize = descriptionFontSize,
        fontWeight = FontWeight.Medium,
        lineHeight = descriptionFontSize * 1.2f,
        color = onSurface.copy(alpha = 0.6f)
    )

    val resolvedScaleStyle = (scaleStyle ?: Ux4gTheme.typography.bXS_default).copy(
        fontSize = scaleFontSize,
        fontWeight = FontWeight.Normal,
        lineHeight = scaleFontSize * 1.2f,
        color = onSurface.copy(alpha = 0.5f)
    )

    // Description fits inside the arc only for larger sizes (width >= 120.dp)
    val descriptionFitsInside = w >= 120.dp

    // Scale label height to account for in layout
    val scaleLabelHeight = if (showScale) (scaleFontSize.value * 1.3f).dp else 0.dp

    Column(
        modifier = modifier.wrapContentSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Arc + centered text + scale labels Box
        Box(
            modifier = Modifier
                .width(w)
                .height(arcHeight + scaleLabelHeight)
        ) {
            // Half circle arc canvas
            Canvas(
                modifier = Modifier
                    .width(w)
                    .height(arcHeight)
            ) {
                val canvasW = this.size.width
                val swPx = sw.toPx()
                val diameter = canvasW - swPx
                val arcTopLeft = Offset(swPx / 2f, swPx / 2f)
                val arcSize = Size(diameter, diameter)

                // Track (full semicircle dome: left → top → right, clockwise sweep of 180°)
                drawArc(
                    color = resolvedTrackColor,
                    startAngle = 180f,
                    sweepAngle = 180f,
                    useCenter = false,
                    topLeft = arcTopLeft,
                    size = arcSize,
                    style = Stroke(width = swPx, cap = strokeCap)
                )

                // Progress arc
                if (clampedValue > 0f) {
                    val sweepAngle = 180f * clampedValue
                    drawArc(
                        brush = resolvedBrush,
                        startAngle = 180f,
                        sweepAngle = sweepAngle,
                        useCenter = false,
                        topLeft = arcTopLeft,
                        size = arcSize,
                        style = Stroke(width = swPx, cap = strokeCap)
                    )
                }
            }

            // Percentage always inside; description inside only if fits
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .padding(
                        start = sw,
                        end = sw,
                        bottom = scaleLabelHeight
                    ),
                contentAlignment = Alignment.BottomCenter
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = displayValue,
                        style = resolvedValueStyle,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        textAlign = TextAlign.Center
                    )
                    if (description != null && descriptionFitsInside) {
                        Spacer(modifier = Modifier.height(gap * 0.25f))
                        Text(
                            text = description,
                            style = resolvedDescriptionStyle,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }

            // Scale labels at arc endpoints (directly below arc ends)
            if (showScale) {
                Box(
                    modifier = Modifier.matchParentSize(),
                    contentAlignment = Alignment.BottomStart
                ) {
                    Text(
                        text = "0%",
                        style = resolvedScaleStyle,
                        modifier = Modifier.padding(start = 0.dp)
                    )
                }
                Box(
                    modifier = Modifier.matchParentSize(),
                    contentAlignment = Alignment.BottomEnd
                ) {
                    Text(
                        text = "100%",
                        style = resolvedScaleStyle,
                        modifier = Modifier.padding(end = 0.dp)
                    )
                }
            }
        }

        // Description below the arc for small sizes (w < 120.dp)
        if (description != null && !descriptionFitsInside) {
            Spacer(modifier = Modifier.height(gap * 0.5f))
            Text(
                text = description,
                style = resolvedDescriptionStyle.copy(
                    fontSize = (descriptionFontSize.value * 0.85f).sp
                ),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Center
            )
        }
    }
}

// ─── Animated Version ─────────────────────────────────────────────────────────

/**
 * Animated version of [Ux4gHalfCircleProgress] that smoothly transitions when [value] changes.
 *
 * @param value Target progress value between 0.0f and 1.0f.
 * @param animationDurationMs Duration of the transition animation in milliseconds (defaults to 700ms).
 */
@Composable
fun Ux4gAnimatedHalfCircleProgress(
    value: Float,
    modifier: Modifier = Modifier,
    size: Ux4gHalfCircleProgressSize = Ux4gHalfCircleProgressSize.L,
    width: Dp? = null,
    strokeWidth: Dp? = null,
    progressColor: Color? = null,
    trackColor: Color? = null,
    gradientColors: List<Color>? = null,
    progressBrush: Brush? = null,
    strokeCap: StrokeCap = StrokeCap.Round,
    valueText: String? = null,
    description: String? = null,
    showScale: Boolean = false,
    valueStyle: TextStyle? = null,
    descriptionStyle: TextStyle? = null,
    scaleStyle: TextStyle? = null,
    gap: Dp = Ux4gSpace.Space4,
    animationDurationMs: Int = 700,
) {
    val animatedValue by animateFloatAsState(
        targetValue = value.coerceIn(0f, 1f),
        animationSpec = tween(durationMillis = animationDurationMs, easing = FastOutSlowInEasing),
        label = "HalfCircleProgressAnimation"
    )

    val percent = (animatedValue * 100).roundToInt()
    val displayValue = valueText ?: "$percent%"

    Ux4gHalfCircleProgress(
        value = animatedValue,
        modifier = modifier,
        size = size,
        width = width,
        strokeWidth = strokeWidth,
        progressColor = progressColor,
        trackColor = trackColor,
        gradientColors = gradientColors,
        progressBrush = progressBrush,
        strokeCap = strokeCap,
        valueText = displayValue,
        description = description,
        showScale = showScale,
        valueStyle = valueStyle,
        descriptionStyle = descriptionStyle,
        scaleStyle = scaleStyle,
        gap = gap
    )
}

// ─── Previews ─────────────────────────────────────────────────────────────────

@Preview(name = "Light Mode - Sizes & Scales", showBackground = true)
@Preview(name = "Dark Mode - Sizes & Scales", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun Ux4gHalfCircleProgressPreview() {
    Ux4gTheme {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // Standard Sizes (S, M, L, XL)
            Text(text = "Standard Sizes", style = Ux4gTheme.typography.hXXS_strong)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.Bottom
            ) {
                Ux4gHalfCircleProgress(
                    value = 0.5f,
                    size = Ux4gHalfCircleProgressSize.S,
                    description = "Description"
                )
                Ux4gHalfCircleProgress(
                    value = 0.5f,
                    size = Ux4gHalfCircleProgressSize.M,
                    description = "Description"
                )
                Ux4gHalfCircleProgress(
                    value = 0.5f,
                    size = Ux4gHalfCircleProgressSize.L,
                    description = "Description"
                )
            }

            // XL Size
            Ux4gHalfCircleProgress(
                value = 0.65f,
                size = Ux4gHalfCircleProgressSize.XL,
                description = "Storage Used"
            )

            // With Scale (0% - 100%)
            Text(text = "With Scale", style = Ux4gTheme.typography.hXXS_strong)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.Bottom
            ) {
                Ux4gHalfCircleProgress(
                    value = 0.75f,
                    size = Ux4gHalfCircleProgressSize.S,
                    showScale = true,
                    description = "Speed"
                )
                Ux4gHalfCircleProgress(
                    value = 0.85f,
                    size = Ux4gHalfCircleProgressSize.M,
                    showScale = true,
                    description = "Completed"
                )
                Ux4gHalfCircleProgress(
                    value = 0.90f,
                    size = Ux4gHalfCircleProgressSize.L,
                    showScale = true,
                    description = "Performance"
                )
            }

            // Custom Colors & Gradients
            Text(text = "Custom Solid & Gradients", style = Ux4gTheme.typography.hXXS_strong)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.Bottom
            ) {
                Ux4gHalfCircleProgress(
                    value = 0.6f,
                    size = Ux4gHalfCircleProgressSize.M,
                    progressColor = Ux4gColors.green600,
                    description = "Success Rate"
                )
                Ux4gHalfCircleProgress(
                    value = 0.45f,
                    size = Ux4gHalfCircleProgressSize.M,
                    gradientColors = listOf(Ux4gColors.orange600, Ux4gColors.yellow500),
                    description = "Battery Level"
                )
            }
        }
    }
}
