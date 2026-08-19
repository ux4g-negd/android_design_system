package com.ux4g.designsystemv1.components.progress

import android.content.res.Configuration
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ux4g.designsystemv1.foundation.Ux4gColors
import com.ux4g.designsystemv1.foundation.Ux4gIcon
import com.ux4g.designsystemv1.foundation.Ux4gIconComponent
import com.ux4g.designsystemv1.foundation.Ux4gIcons
import com.ux4g.designsystemv1.foundation.defaultTypography
import com.ux4g.designsystemv1.theme.Ux4gTheme
import kotlin.math.roundToInt

// ─── Enums ────────────────────────────────────────────────────────────────────

/**
 * Controls how rounded the progress bar corners are.
 */
enum class Ux4gProgressShape {
    /** No rounding — square ends. */
    Sharp,

    /** Fully rounded pill-shaped ends. */
    Rounded
}

/**
 * Where the percentage label is rendered.
 */
enum class Ux4gProgressLabelPosition {
    /** Percentage shown to the top-right, outside the bar. */
    Outside,

    /** Percentage shown inside the filled portion of the bar. */
    Inside
}

// ─── Component ────────────────────────────────────────────────────────────────

/**
 * Backward compatibility alias for [Ux4gLinearProgressBar].
 */
@Deprecated(
    message = "Use Ux4gLinearProgressBar instead",
    replaceWith = ReplaceWith("Ux4gLinearProgressBar(value, modifier, icon, iconColor, iconBackgroundColor, label, hint, height, shape, customBorderRadius, color, gradientColors, trackColor, showPercentage, labelPosition, insideLabelStyle)")
)
@Composable
fun Ux4gLinearProgress(
    value: Float,
    modifier: Modifier = Modifier,
    icon: Ux4gIcon? = null,
    iconColor: Color? = null,
    iconBackgroundColor: Color? = null,
    label: String? = null,
    hint: String? = null,
    height: Dp = 8.dp,
    shape: Ux4gProgressShape = Ux4gProgressShape.Rounded,
    customBorderRadius: Dp? = null,
    color: Color? = null,
    gradientColors: List<Color>? = null,
    trackColor: Color? = null,
    showPercentage: Boolean = false,
    labelPosition: Ux4gProgressLabelPosition = Ux4gProgressLabelPosition.Outside,
    insideLabelStyle: TextStyle? = null,
) {
    Ux4gLinearProgressBar(
        value = value,
        modifier = modifier,
        icon = icon,
        iconColor = iconColor,
        iconBackgroundColor = iconBackgroundColor,
        label = label,
        hint = hint,
        height = height,
        shape = shape,
        customBorderRadius = customBorderRadius,
        color = color,
        gradientColors = gradientColors,
        trackColor = trackColor,
        showPercentage = showPercentage,
        labelPosition = labelPosition,
        insideLabelStyle = insideLabelStyle
    )
}

/**
 * A customizable linear progress indicator following the UX4G Design System matching Flutter's `Ux4gLinearProgressBar`.
 *
 * @param value Progress value between 0.0f (empty) and 1.0f (full).
 * @param icon Optional leading icon shown to the left of the label row.
 * @param iconColor Optional icon color (defaults to [Ux4gColors.primary]).
 * @param iconBackgroundColor Optional icon circular background color.
 * @param label Optional main label text shown above-left of the bar.
 * @param hint Optional hint text shown above-right of the bar.
 * @param height Height of the progress track (default: 8.dp).
 * @param shape Corner shape ([Ux4gProgressShape.Rounded] for pill or [Ux4gProgressShape.Sharp] for flat ends).
 * @param customBorderRadius Custom corner radius overriding [shape].
 * @param color Single solid fill color. Ignored when [gradientColors] is provided.
 * @param gradientColors Two or more colors for a left-to-right gradient fill.
 * @param trackColor Background track color (defaults to neutral 12% opacity).
 * @param showPercentage Whether to render percentage text.
 * @param labelPosition Outside (top-right header) or Inside (within the filled bar).
 * @param insideLabelStyle Text style for percentage text rendered inside the bar.
 */
@Composable
fun Ux4gLinearProgressBar(
    value: Float,
    modifier: Modifier = Modifier,
    icon: Ux4gIcon? = null,
    iconColor: Color? = null,
    iconBackgroundColor: Color? = null,
    label: String? = null,
    hint: String? = null,
    height: Dp = 8.dp,
    shape: Ux4gProgressShape = Ux4gProgressShape.Rounded,
    customBorderRadius: Dp? = null,
    color: Color? = null,
    gradientColors: List<Color>? = null,
    trackColor: Color? = null,
    showPercentage: Boolean = false,
    labelPosition: Ux4gProgressLabelPosition = Ux4gProgressLabelPosition.Outside,
    insideLabelStyle: TextStyle? = null,
) {
    val isDark = isSystemInDarkTheme()
    val clampedValue = value.coerceIn(0f, 1f)
    val percent = (clampedValue * 100).roundToInt()

    // Direct Colors (Flutter Parity)
    val onSurface = Ux4gColors.neutral900
    val primaryColor = Ux4gColors.primary

    val resolvedFillColor = color ?: primaryColor
    val resolvedTrackColor = trackColor ?: onSurface.copy(alpha = 0.12f)

    val resolvedGradient = gradientColors ?: if (color == null) {
        if (isDark) {
            listOf(Ux4gColors.primary300, Ux4gColors.primary950)
        } else {
            listOf(Ux4gColors.primary600, Ux4gColors.primary200)
        }
    } else null

    val resolvedRadius = customBorderRadius ?: if (shape == Ux4gProgressShape.Rounded) height / 2 else 0.dp

    val hasHeader = icon != null || label != null || hint != null ||
            (showPercentage && labelPosition == Ux4gProgressLabelPosition.Outside)

    val textMeasurer = rememberTextMeasurer()

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // ── Header row ────────────────────────────────────────────────
        if (hasHeader) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Leading icon bubble
                if (icon != null) {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(iconBackgroundColor ?: primaryColor.copy(alpha = 0.12f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Ux4gIconComponent(
                            icon = icon,
                            contentDescription = "Progress Icon",
                            tint = iconColor ?: primaryColor,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                }

                // Label
                if (label != null) {
                    Text(
                        text = label,
                        style = defaultTypography.bS_strong.copy(color = onSurface),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f)
                    )
                } else {
                    Spacer(modifier = Modifier.weight(1f))
                }

                // Hint + Outside percentage
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (hint != null) {
                        Text(
                            text = hint,
                            style = defaultTypography.bS_default.copy(
                                color = onSurface.copy(alpha = 0.5f)
                            )
                        )
                    }
                    if (showPercentage && labelPosition == Ux4gProgressLabelPosition.Outside) {
                        Text(
                            text = "$percent%",
                            style = defaultTypography.bS_strong.copy(color = onSurface)
                        )
                    }
                }
            }
        }

        // ── Progress track ─────────────────────────────────────────────
        val resolvedInsideStyle = insideLabelStyle ?: defaultTypography.lS_strong.copy(
            color = Color.White,
            fontSize = if (height > 14.dp) 11.sp else (height.value * 0.7f).coerceIn(8f, 11f).sp
        )

        val shapeRadius = RoundedCornerShape(resolvedRadius)

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(height)
                .clip(shapeRadius)
        ) {
            Canvas(modifier = Modifier.matchParentSize()) {
                val canvasW = size.width
                val canvasH = size.height
                val rPx = resolvedRadius.toPx()

                val trackRect = RoundRect(
                    rect = Rect(0f, 0f, canvasW, canvasH),
                    cornerRadius = CornerRadius(rPx, rPx)
                )
                val trackPath = Path().apply { addRoundRect(trackRect) }

                // Draw Track
                drawRoundRect(
                    color = resolvedTrackColor,
                    size = size,
                    cornerRadius = CornerRadius(rPx, rPx)
                )

                if (clampedValue > 0f) {
                    val fillWidth = canvasW * clampedValue

                    val fillBrush = if (resolvedGradient != null && resolvedGradient.size >= 2) {
                        Brush.horizontalGradient(
                            colors = resolvedGradient,
                            startX = 0f,
                            endX = fillWidth
                        )
                    } else {
                        SolidColor(resolvedFillColor)
                    }

                    // Clip progress to track bounds to ensure smooth rounded edges
                    clipPath(trackPath) {
                        drawRoundRect(
                            brush = fillBrush,
                            size = Size(fillWidth, canvasH),
                            cornerRadius = CornerRadius(rPx, rPx)
                        )

                        // Inside Label
                        if (showPercentage && labelPosition == Ux4gProgressLabelPosition.Inside && fillWidth > 24.dp.toPx()) {
                            val labelText = "$percent%"
                            val measured = textMeasurer.measure(
                                text = labelText,
                                style = resolvedInsideStyle
                            )
                            val textDx = fillWidth - measured.size.width - 6.dp.toPx()
                            val textDy = (canvasH - measured.size.height) / 2f
                            if (textDx > 0f) {
                                drawText(
                                    textLayoutResult = measured,
                                    topLeft = Offset(textDx, textDy)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

// ─── Animated Version ─────────────────────────────────────────────────────────

/**
 * Backward compatibility alias for [Ux4gAnimatedLinearProgressBar].
 */
@Deprecated(
    message = "Use Ux4gAnimatedLinearProgressBar instead",
    replaceWith = ReplaceWith("Ux4gAnimatedLinearProgressBar(value, modifier, icon, iconColor, iconBackgroundColor, label, hint, height, shape, customBorderRadius, color, gradientColors, trackColor, showPercentage, labelPosition, insideLabelStyle, animationDurationMs)")
)
@Composable
fun Ux4gAnimatedLinearProgress(
    value: Float,
    modifier: Modifier = Modifier,
    icon: Ux4gIcon? = null,
    iconColor: Color? = null,
    iconBackgroundColor: Color? = null,
    label: String? = null,
    hint: String? = null,
    height: Dp = 8.dp,
    shape: Ux4gProgressShape = Ux4gProgressShape.Rounded,
    customBorderRadius: Dp? = null,
    color: Color? = null,
    gradientColors: List<Color>? = null,
    trackColor: Color? = null,
    showPercentage: Boolean = false,
    labelPosition: Ux4gProgressLabelPosition = Ux4gProgressLabelPosition.Outside,
    insideLabelStyle: TextStyle? = null,
    animationDurationMs: Int = 600
) {
    Ux4gAnimatedLinearProgressBar(
        value = value,
        modifier = modifier,
        icon = icon,
        iconColor = iconColor,
        iconBackgroundColor = iconBackgroundColor,
        label = label,
        hint = hint,
        height = height,
        shape = shape,
        customBorderRadius = customBorderRadius,
        color = color,
        gradientColors = gradientColors,
        trackColor = trackColor,
        showPercentage = showPercentage,
        labelPosition = labelPosition,
        insideLabelStyle = insideLabelStyle,
        animationDurationMs = animationDurationMs
    )
}

/**
 * Animated version of [Ux4gLinearProgressBar] that smoothly transitions when [value] changes.
 */
@Composable
fun Ux4gAnimatedLinearProgressBar(
    value: Float,
    modifier: Modifier = Modifier,
    icon: Ux4gIcon? = null,
    iconColor: Color? = null,
    iconBackgroundColor: Color? = null,
    label: String? = null,
    hint: String? = null,
    height: Dp = 8.dp,
    shape: Ux4gProgressShape = Ux4gProgressShape.Rounded,
    customBorderRadius: Dp? = null,
    color: Color? = null,
    gradientColors: List<Color>? = null,
    trackColor: Color? = null,
    showPercentage: Boolean = false,
    labelPosition: Ux4gProgressLabelPosition = Ux4gProgressLabelPosition.Outside,
    insideLabelStyle: TextStyle? = null,
    animationDurationMs: Int = 600
) {
    val animatedProgress by animateFloatAsState(
        targetValue = value.coerceIn(0f, 1f),
        animationSpec = tween(durationMillis = animationDurationMs, easing = FastOutSlowInEasing),
        label = "LinearProgressAnimation"
    )

    Ux4gLinearProgressBar(
        value = animatedProgress,
        modifier = modifier,
        icon = icon,
        iconColor = iconColor,
        iconBackgroundColor = iconBackgroundColor,
        label = label,
        hint = hint,
        height = height,
        shape = shape,
        customBorderRadius = customBorderRadius,
        color = color,
        gradientColors = gradientColors,
        trackColor = trackColor,
        showPercentage = showPercentage,
        labelPosition = labelPosition,
        insideLabelStyle = insideLabelStyle
    )
}

// ─── Previews ─────────────────────────────────────────────────────────────────

@Preview(name = "Light Mode", showBackground = true)
@Preview(name = "Dark Mode", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun Ux4gLinearProgressBarPreview() {
    Ux4gTheme {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // 1. Basic Progress Bar
            Ux4gLinearProgressBar(value = 0.4f)

            // 2. With Label & Outside Percentage
            Ux4gLinearProgressBar(
                value = 0.75f,
                label = "Uploading documents",
                hint = "3 of 4 files",
                showPercentage = true,
                labelPosition = Ux4gProgressLabelPosition.Outside
            )

            // 3. With Icon & Inside Percentage
            Ux4gLinearProgressBar(
                value = 0.65f,
                icon = Ux4gIcons.info_circle,
                label = "Application Verification",
                height = 18.dp,
                showPercentage = true,
                labelPosition = Ux4gProgressLabelPosition.Inside
            )

            // 4. Sharp Shape
            Ux4gLinearProgressBar(
                value = 0.5f,
                shape = Ux4gProgressShape.Sharp,
                height = 10.dp,
                color = Ux4gColors.green600,
                showPercentage = true
            )
        }
    }
}
