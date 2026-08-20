package com.ux4g.designsystemv1.components.steppers

import android.content.res.Configuration
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ux4g.designsystemv1.foundation.Ux4gColors
import com.ux4g.designsystemv1.foundation.Ux4gIconComponent
import com.ux4g.designsystemv1.foundation.Ux4gIcons
import com.ux4g.designsystemv1.foundation.defaultTypography

// ─── Enums & Aliases ─────────────────────────────────────────────────────────

/**
 * Orientation for the Stepper component.
 */
enum class StepperOrientation {
    Horizontal,
    Vertical
}

/**
 * Typealias for backward compatibility with [StepperOrientation].
 */
typealias Ux4gStepperOrientation = StepperOrientation

/**
 * Visual style for connecting lines between steps.
 */
enum class StepperLineStyle {
    Solid,
    Dashed
}

/**
 * Typealias for backward compatibility with [StepperLineStyle].
 */
typealias Ux4gStepperLineStyle = StepperLineStyle

/**
 * Position of the connecting line relative to the step item.
 */
enum class StepperLinePlacement {
    Center,
    Bottom
}

/**
 * Typealias for backward compatibility with [StepperLinePlacement].
 */
typealias Ux4gStepperLinePlacement = StepperLinePlacement

// ─── Data Model ─────────────────────────────────────────────────────────────

/**
 * Data model for a single step in the Stepper.
 *
 * @param title The main label for the step.
 * @param description An optional description for the step.
 * @param statusLabel An optional override for the status text (e.g. "Completed", "In progress").
 * @param isError Whether the step is in an error state.
 * @param titleStyle Custom TextStyle for the title.
 * @param descriptionStyle Custom TextStyle for the description.
 * @param statusStyle Custom TextStyle for the status label.
 */
data class Ux4gStepItem(
    val title: String,
    val description: String? = null,
    val statusLabel: String? = null,
    val isError: Boolean = false,
    val titleStyle: TextStyle? = null,
    val descriptionStyle: TextStyle? = null,
    val statusStyle: TextStyle? = null
)

// ─── Colors & Defaults ──────────────────────────────────────────────────────

/**
 * Colors configuration for [Ux4gStepper].
 * Defaults directly match the UX4G Flutter specification.
 */
@Immutable
data class Ux4gStepperColors(
    val primary: Color,
    val onPrimary: Color,
    val onSurface: Color,
    val error: Color,
    val success: Color,
    val activeBackground: Color,
    val lineInactiveColor: Color,
    val stepPendingBorderColor: Color
)

object Ux4gStepperDefaults {
    @Composable
    fun colors(
        primary: Color = Ux4gColors.primary,
        onPrimary: Color = Ux4gColors.white,
        onSurface: Color = Ux4gColors.neutral900,
        error: Color = Ux4gColors.red600,
        success: Color = Ux4gColors.green600,
        activeBackground: Color = Ux4gColors.primary50,
        lineInactiveColor: Color = Ux4gColors.neutral900.copy(alpha = 0.2f),
        stepPendingBorderColor: Color = Ux4gColors.neutral900.copy(alpha = 0.2f)
    ): Ux4gStepperColors = Ux4gStepperColors(
        primary = primary,
        onPrimary = onPrimary,
        onSurface = onSurface,
        error = error,
        success = success,
        activeBackground = activeBackground,
        lineInactiveColor = lineInactiveColor,
        stepPendingBorderColor = stepPendingBorderColor
    )
}

// ─── Main Stepper Composable ────────────────────────────────────────────────

/**
 * A highly customizable Stepper component matching UX4G Flutter specification 1:1.
 *
 * Supports horizontal (center/bottom line placement, edge label alignment, active step background)
 * and vertical (with/without description alignment, custom step spacing) orientations.
 *
 * @param totalSteps The total number of steps to display.
 * @param currentStep The current active step (1-indexed).
 * @param modifier The modifier to apply to the container.
 * @param orientation Whether the stepper is displayed horizontally or vertically.
 * @param lineStyle Visual style of connecting lines (Solid or Dashed).
 * @param linePlacement Position of line relative to the step item (Center or Bottom).
 * @param steps List of [Ux4gStepItem] providing titles, descriptions, and statuses.
 * @param stepSize Diameter of the circular step icon indicator (default 32.dp).
 * @param showLabels Whether to display titles, descriptions, and statuses.
 * @param edgeLabelAlignment In horizontal layout, aligns first step to start and last step to end.
 * @param activeStepBackground In bottom-line horizontal layout, highlights active step column with light background.
 * @param stepSpacing Vertical spacing between steps in vertical orientation (default 24.dp).
 * @param alignIconWithDescription In vertical orientation, offsets top step icon to align with multi-line description.
 * @param colors Color palette configuration.
 */
@Composable
fun Ux4gStepper(
    totalSteps: Int,
    currentStep: Int,
    modifier: Modifier = Modifier,
    orientation: StepperOrientation = StepperOrientation.Horizontal,
    lineStyle: StepperLineStyle = StepperLineStyle.Solid,
    linePlacement: StepperLinePlacement = StepperLinePlacement.Center,
    steps: List<Ux4gStepItem> = emptyList(),
    stepSize: Dp = 32.dp,
    showLabels: Boolean = true,
    edgeLabelAlignment: Boolean = false,
    activeStepBackground: Boolean = false,
    stepSpacing: Dp = 24.dp,
    alignIconWithDescription: Boolean = false,
    colors: Ux4gStepperColors = Ux4gStepperDefaults.colors()
) {
    if (orientation == StepperOrientation.Horizontal) {
        HorizontalStepper(
            totalSteps = totalSteps,
            currentStep = currentStep,
            lineStyle = lineStyle,
            linePlacement = linePlacement,
            steps = steps,
            stepSize = stepSize,
            showLabels = showLabels,
            edgeLabelAlignment = edgeLabelAlignment,
            activeStepBackground = activeStepBackground,
            colors = colors,
            modifier = modifier
        )
    } else {
        VerticalStepper(
            totalSteps = totalSteps,
            currentStep = currentStep,
            lineStyle = lineStyle,
            steps = steps,
            stepSize = stepSize,
            showLabels = showLabels,
            stepSpacing = stepSpacing,
            alignIconWithDescription = alignIconWithDescription,
            colors = colors,
            modifier = modifier
        )
    }
}

// ─── Horizontal Stepper ─────────────────────────────────────────────────────

@Composable
private fun HorizontalStepper(
    totalSteps: Int,
    currentStep: Int,
    lineStyle: StepperLineStyle,
    linePlacement: StepperLinePlacement,
    steps: List<Ux4gStepItem>,
    stepSize: Dp,
    showLabels: Boolean,
    edgeLabelAlignment: Boolean,
    activeStepBackground: Boolean,
    colors: Ux4gStepperColors,
    modifier: Modifier = Modifier
) {
    if (linePlacement == StepperLinePlacement.Bottom) {
        // Bottom Line Placement Layout
        Row(
            modifier = modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min),
            verticalAlignment = Alignment.Top
        ) {
            for (i in 0 until totalSteps) {
                val stepIndex = i + 1
                val isCompleted = currentStep > stepIndex
                val isActive = currentStep == stepIndex
                val isPending = currentStep < stepIndex
                val stepData = steps.getOrNull(i)

                val showActiveBg = activeStepBackground && isActive
                val bgColor = if (showActiveBg) colors.activeBackground else Color.Transparent

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .background(bgColor)
                        .padding(top = 16.dp)
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier.size(stepSize),
                            contentAlignment = Alignment.Center
                        ) {
                            StepIcon(
                                stepIndex = stepIndex,
                                isCompleted = isCompleted,
                                isActive = isActive,
                                isPending = isPending,
                                isError = stepData?.isError ?: false,
                                size = stepSize,
                                colors = colors
                            )
                        }

                        if (showLabels) {
                            Spacer(modifier = Modifier.height(8.dp))
                            StepLabels(
                                title = stepData?.title ?: "Step $stepIndex",
                                description = stepData?.description,
                                statusLabel = stepData?.statusLabel,
                                isCompleted = isCompleted,
                                isActive = isActive,
                                isPending = isPending,
                                isError = stepData?.isError ?: false,
                                textAlign = TextAlign.Center,
                                titleStyle = stepData?.titleStyle,
                                descriptionStyle = stepData?.descriptionStyle,
                                statusStyle = stepData?.statusStyle,
                                colors = colors,
                                modifier = Modifier.padding(horizontal = 4.dp)
                            )
                        }

                        Spacer(modifier = Modifier.weight(1f))
                        Spacer(modifier = Modifier.height(12.dp))

                        val leftPadding = if (i == 0) 0.dp else 4.dp
                        val rightPadding = if (i == totalSteps - 1) 0.dp else 4.dp
                        StepperLine(
                            isCompleted = isCompleted,
                            orientation = StepperOrientation.Horizontal,
                            lineStyle = lineStyle,
                            colors = colors,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = leftPadding, end = rightPadding)
                        )
                    }
                }
            }
        }
    } else {
        // Center Line Placement Layout with layered Stack
        Box(modifier = modifier.fillMaxWidth()) {
            // Layer 1: Background Connecting Lines
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = stepSize / 2),
                verticalAlignment = Alignment.CenterVertically
            ) {
                for (i in 0 until totalSteps) {
                    Row(
                        modifier = Modifier.weight(1f),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (edgeLabelAlignment && i == 0) {
                            Spacer(modifier = Modifier.width(stepSize))
                        } else if (i == 0) {
                            Spacer(modifier = Modifier.weight(1f))
                        } else {
                            StepperLine(
                                isCompleted = currentStep > i,
                                orientation = StepperOrientation.Horizontal,
                                lineStyle = lineStyle,
                                colors = colors,
                                modifier = Modifier.weight(1f)
                            )
                        }

                        if (!(edgeLabelAlignment && i == 0) && !(edgeLabelAlignment && i == totalSteps - 1)) {
                            Spacer(modifier = Modifier.width(stepSize))
                        }

                        if (edgeLabelAlignment && i == totalSteps - 1) {
                            Spacer(modifier = Modifier.width(stepSize))
                        } else if (i == totalSteps - 1) {
                            Spacer(modifier = Modifier.weight(1f))
                        } else {
                            StepperLine(
                                isCompleted = currentStep > i + 1,
                                orientation = StepperOrientation.Horizontal,
                                lineStyle = lineStyle,
                                colors = colors,
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }
            }

            // Layer 2: Icons & Labels
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                for (i in 0 until totalSteps) {
                    val stepIndex = i + 1
                    val isCompleted = currentStep > stepIndex
                    val isActive = currentStep == stepIndex
                    val isPending = currentStep < stepIndex
                    val stepData = steps.getOrNull(i)

                    val textAlign = when {
                        edgeLabelAlignment && i == 0 -> TextAlign.Start
                        edgeLabelAlignment && i == totalSteps - 1 -> TextAlign.End
                        else -> TextAlign.Center
                    }
                    val columnAlignment = when {
                        edgeLabelAlignment && i == 0 -> Alignment.Start
                        edgeLabelAlignment && i == totalSteps - 1 -> Alignment.End
                        else -> Alignment.CenterHorizontally
                    }

                    Column(
                        modifier = Modifier.weight(1f),
                        horizontalAlignment = columnAlignment
                    ) {
                        Box(
                            modifier = Modifier.size(stepSize),
                            contentAlignment = Alignment.Center
                        ) {
                            StepIcon(
                                stepIndex = stepIndex,
                                isCompleted = isCompleted,
                                isActive = isActive,
                                isPending = isPending,
                                isError = stepData?.isError ?: false,
                                size = stepSize,
                                colors = colors
                            )
                        }

                        if (showLabels) {
                            Spacer(modifier = Modifier.height(8.dp))
                            StepLabels(
                                title = stepData?.title ?: "Step $stepIndex",
                                description = stepData?.description,
                                statusLabel = stepData?.statusLabel,
                                isCompleted = isCompleted,
                                isActive = isActive,
                                isPending = isPending,
                                isError = stepData?.isError ?: false,
                                textAlign = textAlign,
                                titleStyle = stepData?.titleStyle,
                                descriptionStyle = stepData?.descriptionStyle,
                                statusStyle = stepData?.statusStyle,
                                colors = colors,
                                modifier = Modifier.padding(horizontal = 4.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

// ─── Vertical Stepper ───────────────────────────────────────────────────────

@Composable
private fun VerticalStepper(
    totalSteps: Int,
    currentStep: Int,
    lineStyle: StepperLineStyle,
    steps: List<Ux4gStepItem>,
    stepSize: Dp,
    showLabels: Boolean,
    stepSpacing: Dp,
    alignIconWithDescription: Boolean,
    colors: Ux4gStepperColors,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        for (i in 0 until totalSteps) {
            val stepIndex = i + 1
            val isCompleted = currentStep > stepIndex
            val isActive = currentStep == stepIndex
            val isPending = currentStep < stepIndex
            val stepData = steps.getOrNull(i)

            IntrinsicRow(
                modifier = Modifier.fillMaxWidth()
            ) {
                // Column containing Icon and connecting line
                Column(
                    modifier = Modifier.width(stepSize),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    if (alignIconWithDescription && stepData != null && stepData.title.isNotEmpty()) {
                        Box(modifier = Modifier.height(28.dp)) {
                            if (i > 0) {
                                StepperLine(
                                    isCompleted = currentStep > i,
                                    orientation = StepperOrientation.Vertical,
                                    lineStyle = lineStyle,
                                    colors = colors,
                                    modifier = Modifier.fillMaxHeight()
                                )
                            }
                        }
                    }

                    StepIcon(
                        stepIndex = stepIndex,
                        isCompleted = isCompleted,
                        isActive = isActive,
                        isPending = isPending,
                        isError = stepData?.isError ?: false,
                        size = stepSize,
                        colors = colors
                    )

                    if (i < totalSteps - 1) {
                        if (showLabels) {
                            StepperLine(
                                isCompleted = isCompleted,
                                orientation = StepperOrientation.Vertical,
                                lineStyle = lineStyle,
                                colors = colors,
                                modifier = Modifier
                                    .weight(1f)
                                    .defaultMinSize(minHeight = stepSpacing)
                            )
                        } else {
                            StepperLine(
                                isCompleted = isCompleted,
                                orientation = StepperOrientation.Vertical,
                                lineStyle = lineStyle,
                                colors = colors,
                                modifier = Modifier.height(stepSpacing)
                            )
                        }
                    }
                }

                // Labels Column
                if (showLabels) {
                    Spacer(modifier = Modifier.width(12.dp))
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .padding(
                                top = 4.dp,
                                bottom = if (i < totalSteps - 1) stepSpacing else 0.dp
                            )
                    ) {
                        StepLabels(
                            title = stepData?.title ?: "Step $stepIndex",
                            description = stepData?.description,
                            statusLabel = stepData?.statusLabel,
                            isCompleted = isCompleted,
                            isActive = isActive,
                            isPending = isPending,
                            isError = stepData?.isError ?: false,
                            textAlign = TextAlign.Start,
                            titleStyle = stepData?.titleStyle,
                            descriptionStyle = stepData?.descriptionStyle,
                            statusStyle = stepData?.statusStyle,
                            colors = colors
                        )
                    }
                }
            }
        }
    }
}

/**
 * Helper row that enforces intrinsic height behavior for child columns.
 */
@Composable
private fun IntrinsicRow(
    modifier: Modifier = Modifier,
    content: @Composable RowScope.() -> Unit
) {
    Row(
        modifier = modifier.height(IntrinsicSize.Min),
        verticalAlignment = Alignment.Top,
        content = content
    )
}

// ─── Step Icon ──────────────────────────────────────────────────────────────

@Composable
internal fun StepIcon(
    stepIndex: Int,
    isCompleted: Boolean,
    isActive: Boolean,
    isPending: Boolean,
    isError: Boolean,
    size: Dp,
    colors: Ux4gStepperColors,
    modifier: Modifier = Modifier
) {
    val targetBackgroundColor = when {
        isError -> Color.Transparent
        isCompleted -> colors.primary
        else -> Color.Transparent
    }

    val targetBorderColor = when {
        isError -> colors.error
        isCompleted || isActive -> colors.primary
        else -> colors.stepPendingBorderColor
    }

    val animatedBg by animateColorAsState(
        targetValue = targetBackgroundColor,
        animationSpec = tween(300),
        label = "step_bg_color"
    )

    val animatedBorder by animateColorAsState(
        targetValue = targetBorderColor,
        animationSpec = tween(300),
        label = "step_border_color"
    )

    val borderWidth = if (isActive || isPending || isError) 2.dp else 0.dp
    val iconSize = size * 0.625f

    Box(
        modifier = modifier
            .size(size)
            .clip(CircleShape)
            .background(animatedBg)
            .then(
                if (borderWidth > 0.dp) {
                    Modifier.border(borderWidth, animatedBorder, CircleShape)
                } else Modifier
            ),
        contentAlignment = Alignment.Center
    ) {
        when {
            isError -> {
                Ux4gIconComponent(
                    icon = Ux4gIcons.error,
                    contentDescription = "Error",
                    tint = colors.error,
                    modifier = Modifier.size(iconSize)
                )
            }
            isCompleted -> {
                Ux4gIconComponent(
                    icon = Ux4gIcons.Check,
                    contentDescription = "Completed",
                    tint = colors.onPrimary,
                    modifier = Modifier.size(iconSize)
                )
            }
            isActive -> {
                val dotSize = size * 0.375f
                Box(
                    modifier = Modifier
                        .size(dotSize)
                        .clip(CircleShape)
                        .background(colors.primary)
                )
            }
            else -> {
                Text(
                    text = "$stepIndex",
                    style = defaultTypography.lM_strong.copy(
                        color = colors.onSurface.copy(alpha = 0.3f),
                        fontSize = (size.value * 0.375f).sp
                    )
                )
            }
        }
    }
}

// ─── Step Labels ───────────────────────────────────────────────────────────

@Composable
internal fun StepLabels(
    title: String,
    description: String?,
    statusLabel: String?,
    isCompleted: Boolean,
    isActive: Boolean,
    isPending: Boolean,
    isError: Boolean,
    textAlign: TextAlign,
    titleStyle: TextStyle?,
    descriptionStyle: TextStyle?,
    statusStyle: TextStyle?,
    colors: Ux4gStepperColors,
    modifier: Modifier = Modifier
) {
    val columnAlignment = when (textAlign) {
        TextAlign.End, TextAlign.Right -> Alignment.End
        TextAlign.Center -> Alignment.CenterHorizontally
        else -> Alignment.Start
    }

    val titleColor = when {
        isError -> colors.error
        isPending -> colors.onSurface.copy(alpha = 0.4f)
        else -> colors.onSurface
    }

    val resolvedDescriptionColor = if (isError) colors.error else colors.onSurface.copy(alpha = 0.4f)

    val resolvedStatus = statusLabel ?: when {
        isError -> "Error"
        isCompleted -> "Completed"
        isActive -> "In progress"
        else -> null
    }

    val statusColor = when {
        isError -> colors.error
        isCompleted -> colors.success
        isActive -> colors.primary
        else -> colors.onSurface.copy(alpha = 0.4f)
    }

    Column(
        modifier = modifier,
        horizontalAlignment = columnAlignment
    ) {
        // Title
        Text(
            text = title,
            style = (titleStyle ?: defaultTypography.lL_strong).copy(
                color = titleStyle?.color ?: titleColor
            ),
            textAlign = textAlign
        )

        // Description
        if (description != null) {
            Text(
                text = description,
                style = (descriptionStyle ?: defaultTypography.lM_default).copy(
                    color = descriptionStyle?.color ?: resolvedDescriptionColor
                ),
                textAlign = textAlign
            )
        }

        // Status Label
        if (resolvedStatus != null) {
            Text(
                text = resolvedStatus,
                style = (statusStyle ?: defaultTypography.lS_strong).copy(
                    color = statusStyle?.color ?: statusColor
                ),
                textAlign = textAlign
            )
        }
    }
}

// ─── Stepper Line ───────────────────────────────────────────────────────────

@Composable
internal fun StepperLine(
    isCompleted: Boolean,
    orientation: StepperOrientation,
    lineStyle: StepperLineStyle,
    colors: Ux4gStepperColors,
    modifier: Modifier = Modifier
) {
    val targetColor = if (isCompleted) colors.primary else colors.lineInactiveColor

    val animatedColor by animateColorAsState(
        targetValue = targetColor,
        animationSpec = tween(500),
        label = "line_color"
    )

    val isHorizontal = orientation == StepperOrientation.Horizontal

    Canvas(
        modifier = modifier.then(
            if (isHorizontal) Modifier.height(2.dp) else Modifier.width(2.dp)
        )
    ) {
        val start = if (isHorizontal) Offset(0f, size.height / 2f) else Offset(size.width / 2f, 0f)
        val end = if (isHorizontal) Offset(size.width, size.height / 2f) else Offset(size.width / 2f, size.height)

        if (lineStyle == StepperLineStyle.Solid) {
            drawLine(
                color = animatedColor,
                start = start,
                end = end,
                strokeWidth = 2.dp.toPx()
            )
        } else {
            val dashWidth = 12.dp.toPx()
            val dashSpace = 8.dp.toPx()
            val totalDistance = if (isHorizontal) size.width else size.height

            if (totalDistance > 0f) {
                val dirX = (end.x - start.x) / totalDistance
                val dirY = (end.y - start.y) / totalDistance

                var currentDist = 0f
                while (currentDist < totalDistance) {
                    val nextDist = (currentDist + dashWidth).coerceAtMost(totalDistance)
                    val p1 = Offset(start.x + dirX * currentDist, start.y + dirY * currentDist)
                    val p2 = Offset(start.x + dirX * nextDist, start.y + dirY * nextDist)
                    drawLine(
                        color = animatedColor,
                        start = p1,
                        end = p2,
                        strokeWidth = 2.dp.toPx()
                    )
                    currentDist += dashWidth + dashSpace
                }
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Previews
// ─────────────────────────────────────────────────────────────────────────────

@Preview(name = "1. Horizontal Stepper - Default", showBackground = true)
@Composable
fun Ux4gStepperHorizontalPreview() {
    val steps = listOf(
        Ux4gStepItem(title = "Account Details", description = "Create login"),
        Ux4gStepItem(title = "Personal Info", description = "Enter name & email"),
        Ux4gStepItem(title = "Verification", description = "Verify OTP"),
        Ux4gStepItem(title = "Payment", description = "Select plan")
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        Ux4gStepper(
            totalSteps = 4,
            currentStep = 2,
            steps = steps,
            lineStyle = StepperLineStyle.Solid
        )

        Ux4gStepper(
            totalSteps = 4,
            currentStep = 3,
            steps = steps,
            lineStyle = StepperLineStyle.Dashed
        )
    }
}

@Preview(name = "2. Horizontal Stepper - Bottom Line Placement & Active Bg", showBackground = true)
@Composable
fun Ux4gStepperBottomLinePreview() {
    val steps = listOf(
        Ux4gStepItem(title = "Cart", description = "2 items"),
        Ux4gStepItem(title = "Address", description = "Shipping"),
        Ux4gStepItem(title = "Payment", description = "Cards/UPI"),
        Ux4gStepItem(title = "Review", description = "Confirmation")
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Ux4gStepper(
            totalSteps = 4,
            currentStep = 2,
            steps = steps,
            linePlacement = StepperLinePlacement.Bottom,
            activeStepBackground = true
        )
    }
}

@Preview(name = "3. Vertical Stepper - With Status & Error", showBackground = true)
@Composable
fun Ux4gStepperVerticalPreview() {
    val steps = listOf(
        Ux4gStepItem(title = "KYC Upload", description = "Completed 10:30 AM"),
        Ux4gStepItem(title = "Bank Verification", description = "Invalid account number", isError = true),
        Ux4gStepItem(title = "Admin Review", description = "Est. 2 days"),
        Ux4gStepItem(title = "Activation", description = "Account activation")
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Ux4gStepper(
            totalSteps = 4,
            currentStep = 2,
            orientation = StepperOrientation.Vertical,
            steps = steps,
            alignIconWithDescription = true
        )
    }
}
