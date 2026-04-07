package com.ux4g.designsystemv1.components.steppers

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ux4g.designsystemv1.foundation.Ux4gIcon
import com.ux4g.designsystemv1.foundation.Ux4gIconComponent
import com.ux4g.designsystemv1.foundation.Ux4gIcons
import com.ux4g.designsystemv1.theme.Ux4gTheme

/**
 * Layout variants for the Capsule (Compact) Stepper.
 */
enum class Ux4gCapsuleStepperLayout {

    Linear,         // Arrows and capsules in one row, labels below
    RightAligned,   // Step info left, Title/Description Right (Variant 2)
    Centered,       // Progress top, arrows & step count middle, labels bottom centered (Variant 3 & 4)
    Split           // Progress top, Info split into rows, arrows bottom right (Variant 5)
}

/**
 * A highly flexible capsule-based stepper component also known as Compact Stepper.
 *
 * @param totalSteps Total number of steps.
 * @param currentStep Current active step (1-indexed).
 * @param stepLabel Primary label for the current step.
 * @param description Optional description for the current step (can be multi-line).
 * @param onNext Callback for next button click.
 * @param onPrevious Callback for previous button click.
 * @param layout The layout variant to use.
 * @param modifier Modifier for the container.
 * @param labelAlignment Alignment for the labels in Linear layout (Start, Center, End).
 */
@Composable
fun Ux4gCapsuleStepper(
    totalSteps: Int,
    currentStep: Int,
    stepLabel: String,
    description: String? = null,
    onNext: () -> Unit = {},
    onPrevious: () -> Unit = {},
    layout: Ux4gCapsuleStepperLayout = Ux4gCapsuleStepperLayout.Linear,
    labelAlignment: Alignment.Horizontal = Alignment.Start,
    activeColor: Color = Ux4gTheme.colorScheme.primary,
    inactiveColor: Color = Ux4gTheme.colorScheme.onSurface.copy(alpha = 0.2f),
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        when (layout) {
            Ux4gCapsuleStepperLayout.Linear -> {
                LinearStepper(
                    totalSteps = totalSteps,
                    currentStep = currentStep,
                    stepLabel = stepLabel,
                    description = description,
                    onNext = onNext,
                    onPrevious = onPrevious,
                    labelAlignment = labelAlignment,
                    activeColor = activeColor,
                    inactiveColor = inactiveColor
                )
            }
            Ux4gCapsuleStepperLayout.RightAligned -> {
                RightAlignedStepper(
                    totalSteps = totalSteps,
                    currentStep = currentStep,
                    stepLabel = stepLabel,
                    description = description,
                    onNext = onNext,
                    onPrevious = onPrevious,
                    activeColor = activeColor,
                    inactiveColor = inactiveColor
                )
            }
            Ux4gCapsuleStepperLayout.Centered -> {
                CenteredStepper(
                    totalSteps = totalSteps,
                    currentStep = currentStep,
                    stepLabel = stepLabel,
                    description = description,
                    onNext = onNext,
                    onPrevious = onPrevious,
                    activeColor = activeColor,
                    inactiveColor = inactiveColor
                )
            }
            Ux4gCapsuleStepperLayout.Split -> {
                SplitStepper(
                    totalSteps = totalSteps,
                    currentStep = currentStep,
                    stepLabel = stepLabel,
                    description = description,
                    onNext = onNext,
                    onPrevious = onPrevious,
                    activeColor = activeColor,
                    inactiveColor = inactiveColor
                )
            }
        }
    }
}

@Composable
private fun CapsuleIndicator(
    totalSteps: Int,
    currentStep: Int,
    activeColor: Color,
    inactiveColor: Color,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        for (i in 1..totalSteps) {
            val isActive = i == currentStep
            val color by animateColorAsState(
                targetValue = if (isActive) activeColor else inactiveColor,
                animationSpec = tween(300)
            )
            val width by animateDpAsState(
                targetValue = if (isActive) 32.dp else 16.dp,
                animationSpec = tween(300)
            )

            Box(
                modifier = Modifier
                    .requiredWidth(width)
                    .requiredHeight(8.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(color)
            )
        }
    }
}

@Composable
private fun StepperIconButton(
    icon: Ux4gIcon,
    enabled: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val contentColor = if (enabled) Ux4gTheme.colorScheme.primary else Ux4gTheme.colorScheme.onSurface.copy(alpha = 0.3f)
    val borderColor = if (enabled) Ux4gTheme.colorScheme.primary.copy(alpha = 0.12f) else Ux4gTheme.colorScheme.onSurface.copy(alpha = 0.2f)

    Box(
        modifier = modifier
            .requiredSize(40.dp)
            .clip(CircleShape)
            .background(Ux4gTheme.colorScheme.surface)
            .border(1.dp, borderColor, CircleShape)
            .clickable(enabled = enabled) { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Ux4gIconComponent(
            icon = icon,
            contentDescription = null,
            tint = contentColor,
            modifier = Modifier.size(20.dp)
        )
    }
}

@Composable
private fun LinearStepper(
    totalSteps: Int,
    currentStep: Int,
    stepLabel: String,
    description: String?,
    onNext: () -> Unit,
    onPrevious: () -> Unit,
    labelAlignment: Alignment.Horizontal,
    activeColor: Color,
    inactiveColor: Color
) {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            StepperIconButton(icon = Ux4gIcons.ic_left_arrow, enabled = currentStep > 1, onClick = onPrevious)
            Spacer(modifier = Modifier.weight(1f))
            CapsuleIndicator(
                totalSteps = totalSteps,
                currentStep = currentStep,
                activeColor = activeColor,
                inactiveColor = inactiveColor
            )
            Spacer(modifier = Modifier.weight(1f))
            StepperIconButton(icon = Ux4gIcons.ic_right_arrow, enabled = currentStep < totalSteps, onClick = onNext)
        }
        
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = labelAlignment
        ) {
            Text(
                text = "Step $currentStep of $totalSteps",
                style = Ux4gTheme.typography.lM_strong,
                color = Ux4gTheme.colorScheme.onSurface,
                textAlign = if (labelAlignment == Alignment.CenterHorizontally) TextAlign.Center else TextAlign.Unspecified
            )
            Text(
                text = stepLabel,
                style = Ux4gTheme.typography.lL_strong,
                color = Ux4gTheme.colorScheme.primary,
                textAlign = if (labelAlignment == Alignment.CenterHorizontally) TextAlign.Center else TextAlign.Unspecified
            )
            description?.let {
                Text(
                    text = it,
                    style = Ux4gTheme.typography.lM_default,
                    color = Ux4gTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                    textAlign = if (labelAlignment == Alignment.CenterHorizontally) TextAlign.Center else TextAlign.Unspecified
                )
            }
        }
    }
}

@Composable
private fun RightAlignedStepper(
    totalSteps: Int,
    currentStep: Int,
    stepLabel: String,
    description: String?,
    onNext: () -> Unit,
    onPrevious: () -> Unit,
    activeColor: Color,
    inactiveColor: Color
) {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            StepperIconButton(icon = Ux4gIcons.ic_left_arrow, enabled = currentStep > 1, onClick = onPrevious)
            Spacer(modifier = Modifier.weight(1f))
            CapsuleIndicator(
                totalSteps = totalSteps,
                currentStep = currentStep,
                activeColor = activeColor,
                inactiveColor = inactiveColor
            )
            Spacer(modifier = Modifier.weight(1f))
            StepperIconButton(icon = Ux4gIcons.ic_right_arrow, enabled = currentStep < totalSteps, onClick = onNext)
        }
        
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(
                text = "Step $currentStep of $totalSteps",
                style = Ux4gTheme.typography.lM_strong,
                color = Ux4gTheme.colorScheme.onSurface
            )
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = stepLabel,
                    style = Ux4gTheme.typography.lL_strong,
                    color = Ux4gTheme.colorScheme.primary
                )
                description?.let {
                    Text(
                        text = it,
                        style = Ux4gTheme.typography.lM_default,
                        color = Ux4gTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                    )
                }
            }
        }
    }
}

@Composable
private fun CenteredStepper(
    totalSteps: Int,
    currentStep: Int,
    stepLabel: String,
    description: String?,
    onNext: () -> Unit,
    onPrevious: () -> Unit,
    activeColor: Color,
    inactiveColor: Color
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        CapsuleIndicator(
            totalSteps = totalSteps,
            currentStep = currentStep,
            activeColor = activeColor,
            inactiveColor = inactiveColor
        )
        
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            StepperIconButton(
                icon = Ux4gIcons.ic_left_arrow,
                enabled = currentStep > 1,
                onClick = onPrevious
            )
            Text(
                text = "Step $currentStep of $totalSteps",
                style = Ux4gTheme.typography.lM_strong,
                color = Ux4gTheme.colorScheme.onSurface,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            StepperIconButton(
                icon = Ux4gIcons.ic_right_arrow,
                enabled = currentStep < totalSteps,
                onClick = onNext
            )
        }
        
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = stepLabel,
                style = Ux4gTheme.typography.lL_strong,
                color = Ux4gTheme.colorScheme.primary,
                textAlign = TextAlign.Center
            )
            description?.let {
                Text(
                    text = it,
                    style = Ux4gTheme.typography.lM_default,
                    color = Ux4gTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
private fun SplitStepper(
    totalSteps: Int,
    currentStep: Int,
    stepLabel: String,
    description: String?,
    onNext: () -> Unit,
    onPrevious: () -> Unit,
    activeColor: Color,
    inactiveColor: Color
) {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        CapsuleIndicator(
            totalSteps = totalSteps,
            currentStep = currentStep,
            activeColor = activeColor,
            inactiveColor = inactiveColor,
            modifier = Modifier.fillMaxWidth()
        )
        
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(
                text = stepLabel,
                style = Ux4gTheme.typography.lL_strong,
                color = Ux4gTheme.colorScheme.primary
            )
            Text(
                text = "Step $currentStep of $totalSteps",
                style = Ux4gTheme.typography.lM_strong,
                color = Ux4gTheme.colorScheme.onSurface
            )
        }
        
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.Bottom) {
            Box(modifier = Modifier.weight(1f)) {
                description?.let {
                    Text(
                        text = it,
                        style = Ux4gTheme.typography.lM_default,
                        color = Ux4gTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                    )
                }
            }
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                StepperIconButton(
                    icon = Ux4gIcons.ic_left_arrow,
                    enabled = currentStep > 1,
                    onClick = onPrevious
                )
                StepperIconButton(
                    icon = Ux4gIcons.ic_right_arrow,
                    enabled = currentStep < totalSteps,
                    onClick = onNext
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun Ux4gCapsuleStepperPreview() {
    Ux4gTheme {
        Column(verticalArrangement = Arrangement.spacedBy(32.dp)) {
            Ux4gCapsuleStepper(
                totalSteps = 12,
                currentStep = 1,
                stepLabel = "Step label",
                description = "Description",
                layout = Ux4gCapsuleStepperLayout.Linear
            )
            
            Ux4gCapsuleStepper(
                totalSteps = 12,
                currentStep = 1,
                stepLabel = "Step label",
                description = "Description",
                layout = Ux4gCapsuleStepperLayout.RightAligned
            )

            Ux4gCapsuleStepper(
                totalSteps = 12,
                currentStep = 1,
                stepLabel = "Step label",
                description = "Description",
                layout = Ux4gCapsuleStepperLayout.Centered
            )

            Ux4gCapsuleStepper(
                totalSteps = 12,
                currentStep = 1,
                stepLabel = "Step label",
                description = "Description",
                layout = Ux4gCapsuleStepperLayout.Split
            )
        }
    }
}
