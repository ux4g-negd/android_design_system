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
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.luminance
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
import kotlin.math.atan2
import kotlin.math.min

// ─── Size Enum ────────────────────────────────────────────────────────────────

/**
 * Size variants for [Ux4gCircularProgress].
 * Matches Flutter's `Ux4gCircularProgressSize` enum values exactly.
 */
enum class Ux4gCircularProgressSize(val diameter: Dp) {
    XS(20.dp),
    S(28.dp),
    M(36.dp),
    L(48.dp),
    XL(88.dp),
    XXL(116.dp),
    XXXL(144.dp);

    companion object {
        val xs: Ux4gCircularProgressSize get() = XS
        val s: Ux4gCircularProgressSize get() = S
        val m: Ux4gCircularProgressSize get() = M
        val l: Ux4gCircularProgressSize get() = L
        val xl: Ux4gCircularProgressSize get() = XL
        val xxl: Ux4gCircularProgressSize get() = XXL
        val xxxl: Ux4gCircularProgressSize get() = XXXL
    }
}

// ─── Internal helpers ─────────────────────────────────────────────────────────

/** Clamp-based linear scale, identical to Flutter's `_scale`. */
private fun scale(ringSize: Float, factor: Float, min: Float, max: Float): Float =
    (ringSize * factor).coerceIn(min, max)

/** Rings smaller than this threshold cannot comfortably fit text inside. */
private const val INLINE_TEXT_THRESHOLD = 72f

// ─── Component ────────────────────────────────────────────────────────────────

/**
 * A full-circle (ring) progress indicator following the UX4G Design System,
 * matching Flutter's `Ux4gCircularProgress` **1 : 1**.
 *
 * Features:
 * - Seven preset sizes (XS → XXXL) or a custom [diameter].
 * - Auto-scaled stroke width, center value/description, label, and description text.
 * - Sweep gradient by default (overridable with [progressColor] or [progressGradientColors]).
 * - [StrokeCap.Butt] (default) or [StrokeCap.Round] with round-cap offset correction.
 * - Optional [backgroundColor] fill, label, description, footer, and [center] composable slot.
 *
 * @param value Progress between 0 f and 1 f.
 * @param modifier Modifier applied to the outer layout.
 * @param size Predefined size variant.
 * @param diameter Custom diameter override.
 * @param strokeWidth Custom stroke width override.
 * @param progressColor Solid progress color (disables the default gradient).
 * @param trackColor Track (background ring) color.
 * @param progressGradientColors Optional list of ≥ 2 colors for a sweep gradient.
 * @param progressBrush Arbitrary [Brush] override for the progress arc.
 * @param strokeCap [StrokeCap.Butt] (default) or [StrokeCap.Round].
 * @param startAngleDegrees Start angle in **degrees** (default −90 = 12-o'clock).
 * @param centerValueText Main value text rendered inside the ring (large sizes) or below it (small sizes).
 * @param centerDescription Secondary description rendered together with [centerValueText].
 * @param center Arbitrary composable rendered in the ring center (overrides text).
 * @param label Label text below the ring.
 * @param description Description text below the label.
 * @param footer Arbitrary composable rendered below label + description.
 * @param labelStyle Custom text style for the label.
 * @param descriptionStyle Custom text style for the description.
 * @param crossAxisAlignment Horizontal alignment of the column.
 * @param textAlign Text alignment for label / description.
 * @param gap Spacing between ring and metadata section.
 * @param backgroundColor Optional circle background fill color.
 */
@Composable
fun Ux4gCircularProgress(
    value: Float,
    modifier: Modifier = Modifier,
    size: Ux4gCircularProgressSize = Ux4gCircularProgressSize.L,
    diameter: Dp? = null,
    strokeWidth: Dp? = null,
    progressColor: Color? = null,
    trackColor: Color? = null,
    progressGradientColors: List<Color>? = null,
    progressBrush: Brush? = null,
    strokeCap: StrokeCap = StrokeCap.Butt,
    startAngleDegrees: Float = -90f,
    centerValueText: String? = null,
    centerDescription: String? = null,
    center: (@Composable () -> Unit)? = null,
    label: String? = null,
    description: String? = null,
    footer: (@Composable () -> Unit)? = null,
    labelStyle: TextStyle? = null,
    descriptionStyle: TextStyle? = null,
    crossAxisAlignment: Alignment.Horizontal = Alignment.CenterHorizontally,
    textAlign: TextAlign = TextAlign.Center,
    gap: Dp = Ux4gSpace.Space8,
    backgroundColor: Color? = null,
) {
    val clampedValue = value.coerceIn(0f, 1f)

    val isDark = isSystemInDarkTheme()

    // ── Resolve dimensions ───────────────────────────────────────────────
    val ringSize = diameter ?: size.diameter
    val ringSizePx = ringSize.value // approximate dp → "virtual px" for scaling math
    val resolvedStrokeWidth = strokeWidth ?: (ringSizePx * 0.1f).coerceIn(3f, 12f).dp
    val resolvedGap = gap.value.coerceIn(4f, 16f).dp

    // ── Resolve colors ───────────────────────────────────────────────────
    val onSurface = Ux4gTheme.colorScheme.onSurface
    val primaryColor = Ux4gTheme.colorScheme.primary
    val surface = Ux4gTheme.colorScheme.surface

    val resolvedProgressColor = progressColor ?: primaryColor
    val resolvedTrackColor = trackColor
        ?: if (isDark) Ux4gColors.neutral600 else Ux4gColors.neutral300

    // ── Resolve brush (sweep gradient by default, matching Flutter) ──────
    val resolvedBrush: Brush = progressBrush
        ?: if (progressGradientColors != null && progressGradientColors.size >= 2) {
            Brush.sweepGradient(progressGradientColors)
        } else if (progressColor == null) {
            val gradColors = if (isDark) {
                listOf(Ux4gColors.primary700, Ux4gColors.primary300)
            } else {
                listOf(Ux4gColors.primary200, Ux4gColors.primary600)
            }
            Brush.sweepGradient(gradColors)
        } else {
            SolidColor(resolvedProgressColor)
        }

    // ── Content color (auto-contrast when backgroundColor is set) ────────
    val contentColor: Color = if (backgroundColor != null) {
        // Blend background over surface, estimate brightness
        val blended = blendColor(backgroundColor, surface)
        if (blended.luminance() < 0.5f) Color.White else onSurface
    } else {
        onSurface
    }

    // ── Typography ───────────────────────────────────────────────────────
    val centerValueFontSize = scale(ringSizePx, 0.18f, 7f, 22f).sp
    val centerDescFontSize = scale(ringSizePx, 0.1f, 6f, 12f).sp
    val labelFontSize = scale(ringSizePx, 0.16f, 10f, 16f).sp
    val descFontSize = scale(ringSizePx, 0.14f, 9f, 14f).sp

    val resolvedCenterValueStyle = Ux4gTheme.typography.bS_strong.copy(
        fontSize = centerValueFontSize,
        fontWeight = FontWeight.W700,
        lineHeight = centerValueFontSize * 1.1f,
        color = contentColor
    )

    val resolvedCenterDescStyle = Ux4gTheme.typography.bXS_default.copy(
        fontSize = centerDescFontSize,
        fontWeight = FontWeight.W600,
        lineHeight = centerDescFontSize * 1.15f,
        color = contentColor.copy(alpha = 0.6f)
    )

    val resolvedLabelStyle = (labelStyle ?: Ux4gTheme.typography.tS_strong).copy(
        fontSize = labelFontSize,
        fontWeight = FontWeight.W700,
        lineHeight = labelFontSize * 1.2f,
        color = labelStyle?.color ?: onSurface
    )

    val resolvedDescStyle = (descriptionStyle ?: Ux4gTheme.typography.bS_default).copy(
        fontSize = descFontSize,
        fontWeight = FontWeight.W600,
        lineHeight = descFontSize * 1.25f,
        color = descriptionStyle?.color ?: onSurface.copy(alpha = 0.62f)
    )

    // ── Layout flags ─────────────────────────────────────────────────────
    val inlineCenter = ringSizePx >= INLINE_TEXT_THRESHOLD
    val hasMeta = label != null || description != null || footer != null
    val hasSmallCenterText =
        !inlineCenter && (centerValueText != null || centerDescription != null) && label == null

    // ── Compose tree ─────────────────────────────────────────────────────
    Column(
        modifier = modifier.wrapContentSize(),
        horizontalAlignment = crossAxisAlignment
    ) {
        // ── Ring ─────────────────────────────────────────────────────────
        Box(
            modifier = Modifier.size(ringSize),
            contentAlignment = Alignment.Center
        ) {
            Canvas(modifier = Modifier.size(ringSize)) {
                drawCircularProgress(
                    value = clampedValue,
                    strokeWidth = resolvedStrokeWidth.toPx(),
                    progressBrush = resolvedBrush,
                    progressColor = resolvedProgressColor,
                    trackColor = resolvedTrackColor,
                    backgroundColor = backgroundColor,
                    strokeCap = strokeCap,
                    startAngleDegrees = startAngleDegrees,
                )
            }

            // Center content
            if (center != null) {
                center()
            } else if (inlineCenter && (centerValueText != null || centerDescription != null)) {
                CircularCenterContent(
                    valueText = centerValueText,
                    description = centerDescription,
                    valueStyle = resolvedCenterValueStyle,
                    descriptionStyle = resolvedCenterDescStyle,
                )
            }
        }

        // ── Center text below ring (small sizes only) ───────────────────
        if (hasSmallCenterText) {
            Spacer(modifier = Modifier.height(resolvedGap * 0.5f))
            CircularCenterContent(
                valueText = centerValueText,
                description = centerDescription,
                valueStyle = resolvedCenterValueStyle.copy(fontSize = 11.sp),
                descriptionStyle = resolvedCenterDescStyle.copy(fontSize = 9.sp),
            )
        }

        // ── Label / description / footer ────────────────────────────────
        if (hasMeta) {
            Spacer(modifier = Modifier.height(resolvedGap))
        }
        if (label != null) {
            Text(
                text = label,
                textAlign = textAlign,
                style = resolvedLabelStyle,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
        if (label != null && description != null) {
            Spacer(modifier = Modifier.height(scale(ringSizePx, 0.035f, 2f, 6f).dp))
        }
        if (description != null) {
            Text(
                text = description,
                textAlign = textAlign,
                style = resolvedDescStyle,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
            )
        }
        if ((label != null || description != null) && footer != null) {
            Spacer(modifier = Modifier.height(scale(ringSizePx, 0.06f, 4f, 10f).dp))
        }
        if (footer != null) {
            footer()
        }
    }
}

// ─── Internal center content ──────────────────────────────────────────────────

@Composable
private fun CircularCenterContent(
    valueText: String?,
    description: String?,
    valueStyle: TextStyle,
    descriptionStyle: TextStyle,
) {
    if (valueText == null && description == null) return
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        if (valueText != null) {
            Text(
                text = valueText,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Center,
                style = valueStyle,
            )
        }
        if (valueText != null && description != null) {
            Spacer(modifier = Modifier.height(Ux4gSpace.Space2))
        }
        if (description != null) {
            Text(
                text = description,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Center,
                style = descriptionStyle,
            )
        }
    }
}

// ─── Canvas painting ──────────────────────────────────────────────────────────

/**
 * Custom Canvas drawing that mirrors Flutter's `_Ux4gCircularProgressPainter` exactly:
 * background fill → rotated canvas coordinate space (startAngle) → track arc → progress arc (with round-cap offset correction).
 */
private fun DrawScope.drawCircularProgress(
    value: Float,
    strokeWidth: Float,
    progressBrush: Brush,
    progressColor: Color,
    trackColor: Color,
    backgroundColor: Color?,
    strokeCap: StrokeCap,
    startAngleDegrees: Float,
) {
    val shortestSide = min(size.width, size.height)
    val radius = (shortestSide - strokeWidth) / 2f
    val center = Offset(size.width / 2f, size.height / 2f)
    val arcRect = Rect(
        left = center.x - radius,
        top = center.y - radius,
        right = center.x + radius,
        bottom = center.y + radius,
    )
    val arcSize = Size(radius * 2f, radius * 2f)
    val arcTopLeft = Offset(arcRect.left, arcRect.top)

    // ── Background fill ─────────────────────────────────────────────────
    if (backgroundColor != null) {
        drawCircle(
            color = backgroundColor,
            radius = radius + strokeWidth / 2f,
            center = center,
        )
    }

    // Rotate canvas around center by startAngleDegrees (-90° / 12 o'clock by default),
    // matching Flutter's SweepGradient(transform: GradientRotation(-math.pi / 2)).
    // In this rotated coordinate frame, angle 0 is at 12 o'clock.
    rotate(degrees = startAngleDegrees, pivot = center) {
        // ── Track ────────────────────────────────────────────────────────
        if (value < 1f) {
            if (strokeCap == StrokeCap.Round) {
                // Round caps: full circle track underneath
                drawArc(
                    color = trackColor,
                    startAngle = 0f,
                    sweepAngle = 360f,
                    useCenter = false,
                    topLeft = arcTopLeft,
                    size = arcSize,
                    style = Stroke(width = strokeWidth, cap = StrokeCap.Butt),
                )
            } else {
                // Butt caps: only where progress isn't
                val trackStart = 360f * value
                val trackSweep = 360f * (1f - value)
                drawArc(
                    color = trackColor,
                    startAngle = trackStart,
                    sweepAngle = trackSweep,
                    useCenter = false,
                    topLeft = arcTopLeft,
                    size = arcSize,
                    style = Stroke(width = strokeWidth, cap = StrokeCap.Butt),
                )
            }
        }

        if (value > 0f) {
            val sweepAngle = 360f * value

            if (strokeCap == StrokeCap.Round && value < 1f) {
                // Offset the round cap so it tucks behind the track start
                val capOffsetRad = atan2(strokeWidth * 0.5f, radius)
                val capOffsetDeg = Math.toDegrees(capOffsetRad.toDouble()).toFloat()
                val adjustedSweep = sweepAngle - capOffsetDeg
                if (adjustedSweep > 0f) {
                    drawArc(
                        brush = progressBrush,
                        startAngle = capOffsetDeg,
                        sweepAngle = adjustedSweep,
                        useCenter = false,
                        topLeft = arcTopLeft,
                        size = arcSize,
                        style = Stroke(width = strokeWidth, cap = StrokeCap.Round),
                    )
                }
            } else {
                drawArc(
                    brush = progressBrush,
                    startAngle = 0f,
                    sweepAngle = sweepAngle,
                    useCenter = false,
                    topLeft = arcTopLeft,
                    size = arcSize,
                    style = Stroke(width = strokeWidth, cap = strokeCap),
                )
            }
        }
    }
}

// ─── Color utilities ──────────────────────────────────────────────────────────

/** Simple alpha-blend of [foreground] over [background]. */
private fun blendColor(foreground: Color, background: Color): Color {
    val a = foreground.alpha
    return Color(
        red = foreground.red * a + background.red * (1f - a),
        green = foreground.green * a + background.green * (1f - a),
        blue = foreground.blue * a + background.blue * (1f - a),
        alpha = 1f,
    )
}

// ─── Animated Version ─────────────────────────────────────────────────────────

/**
 * Animated version of [Ux4gCircularProgress] that smoothly transitions when [value] changes.
 * Matches Flutter's `Ux4gAnimatedCircularProgress` 1 : 1 (700 ms, easeInOut by default).
 *
 * @param value Target progress value between 0 f and 1 f.
 * @param animationDurationMs Duration of the transition animation in milliseconds.
 */
@Composable
fun Ux4gAnimatedCircularProgress(
    value: Float,
    modifier: Modifier = Modifier,
    size: Ux4gCircularProgressSize = Ux4gCircularProgressSize.L,
    diameter: Dp? = null,
    strokeWidth: Dp? = null,
    progressColor: Color? = null,
    trackColor: Color? = null,
    progressGradientColors: List<Color>? = null,
    progressBrush: Brush? = null,
    strokeCap: StrokeCap = StrokeCap.Butt,
    startAngleDegrees: Float = -90f,
    centerValueText: String? = null,
    centerDescription: String? = null,
    center: (@Composable () -> Unit)? = null,
    label: String? = null,
    description: String? = null,
    footer: (@Composable () -> Unit)? = null,
    labelStyle: TextStyle? = null,
    descriptionStyle: TextStyle? = null,
    crossAxisAlignment: Alignment.Horizontal = Alignment.CenterHorizontally,
    textAlign: TextAlign = TextAlign.Center,
    gap: Dp = Ux4gSpace.Space8,
    backgroundColor: Color? = null,
    animationDurationMs: Int = 700,
) {
    val animatedValue by animateFloatAsState(
        targetValue = value.coerceIn(0f, 1f),
        animationSpec = tween(durationMillis = animationDurationMs, easing = FastOutSlowInEasing),
        label = "CircularProgressAnimation"
    )

    Ux4gCircularProgress(
        value = animatedValue,
        modifier = modifier,
        size = size,
        diameter = diameter,
        strokeWidth = strokeWidth,
        progressColor = progressColor,
        trackColor = trackColor,
        progressGradientColors = progressGradientColors,
        progressBrush = progressBrush,
        strokeCap = strokeCap,
        startAngleDegrees = startAngleDegrees,
        centerValueText = centerValueText,
        centerDescription = centerDescription,
        center = center,
        label = label,
        description = description,
        footer = footer,
        labelStyle = labelStyle,
        descriptionStyle = descriptionStyle,
        crossAxisAlignment = crossAxisAlignment,
        textAlign = textAlign,
        gap = gap,
        backgroundColor = backgroundColor,
    )
}

// ─── Previews ─────────────────────────────────────────────────────────────────

@Preview(name = "Light - All Sizes", showBackground = true)
@Preview(name = "Dark - All Sizes", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun Ux4gCircularProgressSizesPreview() {
    Ux4gTheme {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "All Sizes", style = Ux4gTheme.typography.hXXS_strong)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.Bottom
            ) {
                Ux4gCircularProgressSize.entries.forEach { s ->
                    Ux4gCircularProgress(
                        value = 0.65f,
                        size = s,
                    )
                }
            }
        }
    }
}

@Preview(name = "Light - Center Content", showBackground = true)
@Preview(name = "Dark - Center Content", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun Ux4gCircularProgressCenterPreview() {
    Ux4gTheme {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Center Value + Label", style = Ux4gTheme.typography.hXXS_strong)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.Top
            ) {
                // XXL with center text + label + description
                Ux4gCircularProgress(
                    value = 0.75f,
                    size = Ux4gCircularProgressSize.XXXL,
                    centerValueText = "75%",
                    centerDescription = "Used",
                    label = "Storage",
                    description = "18 GB of 24 GB",
                )
                // XL
                Ux4gCircularProgress(
                    value = 0.42f,
                    size = Ux4gCircularProgressSize.XXL,
                    centerValueText = "42%",
                    centerDescription = "Done",
                    label = "Progress",
                )
            }

            // Small sizes show center text below ring
            Text(text = "Small – Text Below Ring", style = Ux4gTheme.typography.hXXS_strong)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.Top
            ) {
                Ux4gCircularProgress(
                    value = 0.30f,
                    size = Ux4gCircularProgressSize.M,
                    centerValueText = "30%",
                    centerDescription = "CPU",
                )
                Ux4gCircularProgress(
                    value = 0.90f,
                    size = Ux4gCircularProgressSize.S,
                    centerValueText = "90%",
                )
                Ux4gCircularProgress(
                    value = 0.55f,
                    size = Ux4gCircularProgressSize.XS,
                    centerValueText = "55%",
                )
            }
        }
    }
}

@Preview(name = "Light - Custom Colors", showBackground = true)
@Preview(name = "Dark - Custom Colors", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun Ux4gCircularProgressColorsPreview() {
    Ux4gTheme {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Custom Colors & Gradients", style = Ux4gTheme.typography.hXXS_strong)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.Top
            ) {
                // Solid green
                Ux4gCircularProgress(
                    value = 0.80f,
                    size = Ux4gCircularProgressSize.XL,
                    progressColor = Ux4gColors.green600,
                    centerValueText = "80%",
                    label = "Success",
                )
                // Orange gradient
                Ux4gCircularProgress(
                    value = 0.60f,
                    size = Ux4gCircularProgressSize.XL,
                    progressGradientColors = listOf(
                        Ux4gColors.orange600,
                        Ux4gColors.yellow500,
                    ),
                    centerValueText = "60%",
                    label = "Battery",
                )
                // With background fill
                Ux4gCircularProgress(
                    value = 0.50f,
                    size = Ux4gCircularProgressSize.XL,
                    backgroundColor = Ux4gColors.primary900,
                    centerValueText = "50%",
                    label = "Dark BG",
                )
            }

            Text(text = "Round Cap", style = Ux4gTheme.typography.hXXS_strong)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.Top
            ) {
                Ux4gCircularProgress(
                    value = 0.35f,
                    size = Ux4gCircularProgressSize.XXL,
                    strokeCap = StrokeCap.Round,
                    centerValueText = "35%",
                    label = "Round",
                )
                Ux4gCircularProgress(
                    value = 0.85f,
                    size = Ux4gCircularProgressSize.XXL,
                    strokeCap = StrokeCap.Round,
                    progressColor = Ux4gColors.cyan600,
                    centerValueText = "85%",
                    label = "Round Solid",
                )
            }
        }
    }
}
