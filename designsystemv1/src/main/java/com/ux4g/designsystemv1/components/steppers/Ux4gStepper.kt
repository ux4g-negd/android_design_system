package com.ux4g.designsystemv1.components.steppers

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import android.content.res.Configuration
import com.ux4g.designsystemv1.foundation.Ux4gColors
import com.ux4g.designsystemv1.foundation.Ux4gIcons
import com.ux4g.designsystemv1.foundation.Ux4gIconComponent
import com.ux4g.designsystemv1.theme.Ux4gTheme
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.foundation.Canvas
import androidx.compose.ui.geometry.Offset
/**
 * Orientation for the Stepper component.
 */
enum class StepperOrientation {
    Horizontal, Vertical
}

/**
 * Visual style for the connecting line between steps.
 */
enum class StepperLineStyle {
    Solid, Dashed
}

/**
 * Data model for a single step in the Stepper.
 * @param title The main label for the step.
 * @param description An optional description for the step.
 * @param statusLabel An optional override for the status text (e.g. "Completed", "In progress").
 *                     If not provided, default labels may be used or hidden based on state.
 */
data class Ux4gStepItem(
    val title: String,
    val description: String? = null,
    val statusLabel: String? = null,
    val isError: Boolean = false
)

/**
 * A highly customizable Stepper component for Ux4G Design System.
 *
 * @param totalSteps The total number of steps to display.
 * @param currentStep The current active step (1-indexed).
 * @param orientation Whether the stepper is displayed Horizontally or Vertically.
 * @param steps An optional list of [Ux4gStepItem] to provide titles and descriptions.
 *              If the list is smaller than [totalSteps], indices will be used for titles.
 * @param modifier The modifier to be applied to the stepper.
 */
@Composable
fun Ux4gStepper(
    totalSteps: Int,
    currentStep: Int,
    orientation: StepperOrientation = StepperOrientation.Horizontal,
    lineStyle: StepperLineStyle = StepperLineStyle.Solid,
    steps: List<Ux4gStepItem> = emptyList(),
    modifier: Modifier = Modifier
) {
    if (orientation == StepperOrientation.Horizontal) {
        HorizontalStepper(totalSteps, currentStep, lineStyle, steps, modifier)
    } else {
        VerticalStepper(totalSteps, currentStep, lineStyle, steps, modifier)
    }
}

@Composable
private fun HorizontalStepper(
    totalSteps: Int,
    currentStep: Int,
    lineStyle: StepperLineStyle,
    steps: List<Ux4gStepItem>,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier.fillMaxWidth()) {
        // 1. Lines Layer (Background)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp), // Align with center of 32dp icon
            verticalAlignment = Alignment.CenterVertically
        ) {
            for (i in 0 until totalSteps) {
                // Left Line Segment (for this step's half)
                if (i == 0) {
                    Spacer(modifier = Modifier.weight(0.5f))
                } else {
                    StepperLine(
                        isCompleted = currentStep > i,
                        orientation = StepperOrientation.Horizontal,
                        lineStyle = lineStyle,
                        modifier = Modifier.weight(0.5f)
                    )
                }

                // Constant space for the icon circle to prevent line overlap
                Spacer(modifier = Modifier.width(32.dp))

                // Right Line Segment (for this step's half)
                if (i == totalSteps - 1) {
                    Spacer(modifier = Modifier.weight(0.5f))
                } else {
                    StepperLine(
                        isCompleted = currentStep > i + 1,
                        orientation = StepperOrientation.Horizontal,
                        lineStyle = lineStyle,
                        modifier = Modifier.weight(0.5f)
                    )
                }
            }
        }

        // 2. Steps Layer (Icons and Labels)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            for (i in 0 until totalSteps) {
                val stepIndex = i + 1
                val isCompleted = currentStep > stepIndex
                val isActive = currentStep == stepIndex
                val isPending = currentStep < stepIndex

                val stepData = steps.getOrNull(i)

                Column(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Box for icon to ensure vertical centering for lines
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.size(32.dp)
                    ) {
                        StepIcon(
                            stepIndex = stepIndex,
                            isCompleted = isCompleted,
                            isActive = isActive,
                            isPending = isPending,
                            isError = stepData?.isError ?: false
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Labels
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(horizontal = 4.dp)
                    ) {
                        Text(
                            text = stepData?.title ?: "Step $stepIndex",
                            style = Ux4gTheme.typography.lL_strong,
                            color = when {
                                stepData?.isError == true -> Ux4gTheme.colorScheme.error
                                isPending -> Ux4gTheme.colorScheme.onSurface.copy(alpha = 0.4f)
                                else -> Ux4gTheme.colorScheme.onSurface
                            },
                            textAlign = TextAlign.Center
                        )
                        if (stepData?.description != null) {
                            Text(
                                text = stepData.description,
                                style = Ux4gTheme.typography.lM_default,
                                color = if (stepData.isError) Ux4gColors.red400 else Ux4gTheme.colorScheme.onSurface.copy(alpha = 0.4f),
                                textAlign = TextAlign.Center
                            )
                        }
                        val statusText = stepData?.statusLabel ?: when {
                            stepData?.isError == true -> "Error"
                            isCompleted -> "Completed"
                            isActive -> "In progress"
                            else -> null
                        }
                        if (statusText != null) {
                            Text(
                                text = statusText,
                                style = Ux4gTheme.typography.lS_strong,
                                color = when {
                                    stepData?.isError == true -> Ux4gTheme.colorScheme.error
                                    isCompleted -> Ux4gColors.green
                                    isActive -> Ux4gTheme.colorScheme.primary
                                    else -> Ux4gTheme.colorScheme.onSurface.copy(alpha = 0.4f)
                                },
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun VerticalStepper(
    totalSteps: Int,
    currentStep: Int,
    lineStyle: StepperLineStyle,
    steps: List<Ux4gStepItem>,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        for (i in 0 until totalSteps) {
            val stepIndex = i + 1
            val isCompleted = currentStep > stepIndex
            val isActive = currentStep == stepIndex
            val isPending = currentStep < stepIndex

            val stepData = steps.getOrNull(i)

            // Use IntrinsicSize.Max to allow left column to match right content height
            Row(modifier = Modifier.height(IntrinsicSize.Max)) {
                // Left Column with Icon and Line
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .width(32.dp)
                        .fillMaxHeight()
                ) {
                    StepIcon(
                        stepIndex = stepIndex,
                        isCompleted = isCompleted,
                        isActive = isActive,
                        isPending = isPending,
                        isError = stepData?.isError ?: false
                    )
                    if (i < totalSteps - 1) {
                        StepperLine(
                            isCompleted = isCompleted,
                            orientation = StepperOrientation.Vertical,
                            lineStyle = lineStyle,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

                Spacer(modifier = Modifier.width(12.dp))

                // Content Column
                Column(
                    modifier = Modifier
                        .padding(top = 4.dp, bottom = if (i < totalSteps - 1) 24.dp else 0.dp)
                ) {
                    Text(
                        text = stepData?.title ?: "Step $stepIndex",
                        style = Ux4gTheme.typography.lL_strong,
                        color = when {
                            stepData?.isError == true -> Ux4gTheme.colorScheme.error
                            isPending -> Ux4gTheme.colorScheme.onSurface.copy(alpha = 0.4f)
                            else -> Ux4gTheme.colorScheme.onSurface
                        }
                    )
                    if (stepData?.description != null) {
                        Text(
                            text = stepData.description,
                            style = Ux4gTheme.typography.lM_default,
                            color = if (stepData.isError) Ux4gColors.red400 else Ux4gTheme.colorScheme.onSurface.copy(alpha = 0.4f)
                        )
                    }
                    val statusText = stepData?.statusLabel ?: when {
                        stepData?.isError == true -> "Error"
                        isCompleted -> "Completed"
                        isActive -> "In progress"
                        else -> null
                    }
                    if (statusText != null) {
                        Text(
                            text = statusText,
                            style = Ux4gTheme.typography.lS_strong,
                            color = when {
                                stepData?.isError == true -> Ux4gTheme.colorScheme.error
                                isCompleted -> Ux4gColors.green
                                isActive -> Ux4gTheme.colorScheme.primary
                                else -> Ux4gTheme.colorScheme.onSurface.copy(alpha = 0.4f)
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun StepIcon(
    stepIndex: Int,
    isCompleted: Boolean,
    isActive: Boolean,
    isPending: Boolean,
    isError: Boolean = false
) {
    val backgroundColor by animateColorAsState(
        targetValue = when {
            isError -> Color.Transparent
            isCompleted -> Ux4gTheme.colorScheme.primary
            else -> Color.Transparent
        },
        animationSpec = tween(durationMillis = 300)
    )
    val borderColor by animateColorAsState(
        targetValue = when {
            isError -> Ux4gTheme.colorScheme.error
            isCompleted -> Ux4gTheme.colorScheme.primary
            isActive -> Ux4gTheme.colorScheme.primary
            else -> Ux4gTheme.colorScheme.onSurface.copy(alpha = 0.2f)
        },
        animationSpec = tween(durationMillis = 300)
    )

    Box(
        modifier = Modifier
            .size(32.dp)
            .clip(CircleShape)
            .background(backgroundColor)
            .border(
                width = if (isActive || isPending) 2.dp else 0.dp,
                color = borderColor,
                shape = CircleShape
            ),
        contentAlignment = Alignment.Center
    ) {
        when {
            isError -> {
                Ux4gIconComponent(
                    icon = Ux4gIcons.outline_error,
                    contentDescription = "Error",
                    tint = Ux4gTheme.colorScheme.error,
                    modifier = Modifier.size(20.dp)
                )
            }
            isCompleted -> {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Completed",
                    tint = Color.White,
                    modifier = Modifier.size(20.dp)
                )
            }

            isActive -> {
                Box(
                    modifier = Modifier
                        .size(12.dp)
                        .clip(CircleShape)
                        .background(Ux4gTheme.colorScheme.primary)
                )
            }

            else -> {
                Text(
                    text = stepIndex.toString(),
                    style = Ux4gTheme.typography.lM_default,
                    color = Ux4gTheme.colorScheme.onSurface.copy(alpha = 0.3f),
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
private fun StepperLine(
    isCompleted: Boolean,
    orientation: StepperOrientation,
    lineStyle: StepperLineStyle,
    modifier: Modifier = Modifier
) {
    val lineColor by animateColorAsState(
        targetValue = if (isCompleted) Ux4gTheme.colorScheme.primary else Ux4gTheme.colorScheme.onSurface.copy(alpha = 0.2f),
        animationSpec = tween(durationMillis = 500)
    )

    val pathEffect = if (lineStyle == StepperLineStyle.Dashed) {
        PathEffect.dashPathEffect(floatArrayOf(12f, 8f), 0f)
    } else null

    Canvas(
        modifier = modifier
            .then(
                if (orientation == StepperOrientation.Horizontal) {
                    Modifier.height(2.dp)
                } else {
                    Modifier
                        .width(2.dp)
                        .fillMaxHeight()
                }
            )
    ) {
        val strokeWidth = 2.dp.toPx()
        if (orientation == StepperOrientation.Horizontal) {
            drawLine(
                color = lineColor,
                start = Offset(0f, size.height / 2),
                end = Offset(size.width, size.height / 2),
                strokeWidth = strokeWidth,
                pathEffect = pathEffect
            )
        } else {
            drawLine(
                color = lineColor,
                start = Offset(size.width / 2, 0f),
                end = Offset(size.width / 2, size.height),
                strokeWidth = strokeWidth,
                pathEffect = pathEffect
            )
        }
    }
}

@Preview(name = "Horizontal Stepper - Dashed", showBackground = true)
@Composable
fun Ux4gStepperHorizontalDashedPreview() {
    Ux4gTheme {
        Box(modifier = Modifier.padding(16.dp)) {
            Ux4gStepper(
                totalSteps = 4,
                currentStep = 2,
                orientation = StepperOrientation.Horizontal,
                lineStyle = StepperLineStyle.Dashed,
                steps = listOf(
                    Ux4gStepItem("Step 1", "Desc"),
                    Ux4gStepItem("Step 2", "Desc"),
                    Ux4gStepItem("Step 3", "Desc"),
                    Ux4gStepItem("Step 4", "Desc")
                )
            )
        }
    }
}

@Preview(name = "Horizontal Stepper", showBackground = true)
@Composable
fun Ux4gStepperHorizontalPreview() {
    Ux4gTheme {
        Box(modifier = Modifier.padding(16.dp)) {
            Ux4gStepper(
                totalSteps = 4,
                currentStep = 2,
                orientation = StepperOrientation.Horizontal,
                lineStyle = StepperLineStyle.Solid,
                steps = listOf(
                    Ux4gStepItem("Account", "Login details"),
                    Ux4gStepItem("Profile", "Bio and Avatar"),
                    Ux4gStepItem("Payment", "Card info"),
                    Ux4gStepItem("Done", "Confirmation")
                )
            )
        }
    }
}

@Preview(name = "Vertical Stepper", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun Ux4gStepperVerticalPreview() {
    Ux4gTheme(darkTheme = true) {
        Box(modifier = Modifier.padding(16.dp).background(Ux4gTheme.colorScheme.background)) {
            Ux4gStepper(
                totalSteps = 3,
                currentStep = 1,
                orientation = StepperOrientation.Vertical,
                lineStyle = StepperLineStyle.Dashed,
            steps = listOf(
                    Ux4gStepItem("Step One", "This is the first step"),
                    Ux4gStepItem("Step Two", "This is the second step"),
                    Ux4gStepItem("Step Three", "This is the third step")
                )
            )
        }
    }
}
@Preview(name = "Stepper with Error", showBackground = true)
@Composable
fun Ux4gStepperErrorPreview() {
    Ux4gTheme {
        Box(modifier = Modifier.padding(16.dp)) {
            Ux4gStepper(
                totalSteps = 4,
                currentStep = 3,
                orientation = StepperOrientation.Horizontal,
                steps = listOf(
                    Ux4gStepItem("Account", "Completed"),
                    Ux4gStepItem("Profile", "Completed"),
                    Ux4gStepItem("Payment", "There was an issue", isError = true),
                    Ux4gStepItem("Done", "Pending")
                )
            )
        }
    }
}

