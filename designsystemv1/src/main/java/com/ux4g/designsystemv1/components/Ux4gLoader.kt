package com.ux4g.designsystemv1.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.ux4g.designsystemv1.foundation.Ux4gPalette
import com.ux4g.designsystemv1.theme.Ux4gTheme
import java.lang.Math.toRadians
import kotlin.math.cos
import kotlin.math.sin

/**
 * A circular loader component for the Ux4G Design System.
 *
 * @param modifier Modifier to be applied to the loader.
 * @param size The diameter of the loader.
 * @param color The color of the loader arc.
 * @param percentage The visible percentage of the circle (0 to 100).
 * ```
 *                   Controls the sweep angle (e.g., 50 means a semi-circle).
 * @param strokeWidth
 * ```
 * The thickness of the loader arc.
 * @param rotationDuration The time in milliseconds for one full rotation.
 */
@Composable
fun Ux4gLoader(
        modifier: Modifier = Modifier,
        size: Dp = 40.dp,
        color: Color = Ux4gTheme.colorScheme.primary,
        percentage: Float = 100f,
        strokeWidth: Dp = 4.dp,
        rotationDuration: Int = 1200
) {
        val infiniteTransition = rememberInfiniteTransition(label = "LoaderRotation")

        // Smooth infinite rotation
        val rotation by
                infiniteTransition.animateFloat(
                        initialValue = 0f,
                        targetValue = 360f,
                        animationSpec =
                                infiniteRepeatable(
                                        animation = tween(rotationDuration, easing = LinearEasing),
                                        repeatMode = RepeatMode.Restart
                                ),
                        label = "RotationAngle"
                )

        Canvas(modifier = modifier.size(size)) {
                // Calculate sweep angle based on percentage (0-100)
                val sweepAngle = 360f * (percentage / 100f).coerceIn(0f, 1f)

                // Create a sweep gradient from transparent to the base color, mapped strictly to
                // the percentage
                val brush =
                        Brush.sweepGradient(
                                0f to color.copy(alpha = 0f),
                                (percentage / 100f) to color
                        )

                rotate(degrees = rotation) {
                        drawArc(
                                brush = brush,
                                startAngle = 0f,
                                sweepAngle = sweepAngle,
                                useCenter = false,
                                style = Stroke(width = strokeWidth.toPx(), cap = StrokeCap.Butt)
                        )

                        // Calculate position for the head cap (solid end)
                        // The arc is drawn on the edge of the size bounds
                        val radius = size.toPx() / 2.0f
                        val angleRad = toRadians(sweepAngle.toDouble())
                        val capX = center.x + radius * cos(angleRad).toFloat()
                        val capY = center.y + radius * sin(angleRad).toFloat()

                        // Draw round cap at the head of the arc
                        drawCircle(
                                color = color,
                                radius = strokeWidth.toPx() / 2,
                                center = Offset(capX, capY)
                        )
                }
        }
}

@Preview(showBackground = true, backgroundColor = 0xFFD9D9D9)
@Composable
private fun Ux4gLoaderGridPreview() {
        Ux4gTheme {
                Column(
                        modifier =
                                Modifier.padding(16.dp)
                                        .background(
                                                Color(0xFFE5E5E5)
                                        ) // Match background from image
                                        .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(32.dp)
                ) {
                        // Purple Section (Primary)
                        LoaderSetPreview(
                                colors =
                                        listOf(
                                                Ux4gPalette.primary,
                                                Ux4gPalette.primary400,
                                                Ux4gPalette.primary300
                                        )
                        )

                        // White Section (Neutral)
                        LoaderSetPreview(
                                colors =
                                        listOf(
                                                Ux4gPalette.white,
                                                Ux4gTheme.colorScheme.onSurface.copy(alpha = 0.2f),
                                                Ux4gTheme.colorScheme.onSurface.copy(alpha = 0.1f)
                                        )
                        )

                        // Red Section
                        LoaderSetPreview(
                                colors =
                                        listOf(
                                                Ux4gPalette.red,
                                                Ux4gPalette.red400,
                                                Ux4gPalette.red300
                                        )
                        )
                }
        }
}

@Composable
private fun LoaderSetPreview(colors: List<Color>) {
        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                val sizes = listOf(48.dp, 36.dp, 24.dp, 16.dp, 12.dp)
                val percentages = listOf(90f, 75f, 50f, 25f, 10f)

                sizes.forEach { size ->
                        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                                percentages.forEach { pct ->
                                        Ux4gLoader(
                                                size = size,
                                                color = colors[0], // Using base color for the set
                                                percentage = pct,
                                                strokeWidth =
                                                        (size.value / 8).dp.coerceAtLeast(2.dp)
                                        )
                                }
                        }
                }
        }
}
