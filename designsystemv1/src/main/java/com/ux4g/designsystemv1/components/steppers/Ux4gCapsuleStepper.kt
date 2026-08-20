package com.ux4g.designsystemv1.components.steppers

import android.content.res.Configuration
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ux4g.designsystemv1.foundation.Ux4gColors
import com.ux4g.designsystemv1.foundation.Ux4gIcon
import com.ux4g.designsystemv1.foundation.Ux4gIconComponent
import com.ux4g.designsystemv1.foundation.Ux4gIcons
import com.ux4g.designsystemv1.foundation.defaultTypography

// ─── Enums & Aliases ─────────────────────────────────────────────────────────

/**
 * Layout variants for the Compact (Capsule) Stepper matching the 5 UX4G Flutter variants.
 */
enum class Ux4gCompactStepperLayout {
    /** Navigation buttons on ends of top row, capsule indicator in center, labels below. */
    Linear,

    /** Navigation buttons and capsules on top row; Step count on bottom-left, Title/Description on bottom-right. */
    RightAligned,

    /** Capsules on top; Navigation buttons and Step count in middle row; Centered Title & Description below. */
    Centered,

    /** Capsules on top; Step count in middle; Bottom row with Left button, Center Title/Desc, Right button. */
    CenteredBetween,

    /** Capsules on top; Left side Title & Description; Right side Step count and navigation buttons. */
    Split
}

/**
 * Backward-compatibility alias for [Ux4gCompactStepperLayout].
 */
typealias Ux4gCapsuleStepperLayout = Ux4gCompactStepperLayout

// ─── Colors & Defaults ──────────────────────────────────────────────────────

/**
 * Colors configuration for [Ux4gCompactStepper].
 * Defaults directly match the UX4G Flutter specification.
 */
@Immutable
data class Ux4gCompactStepperColors(
    val primary: Color,
    val onSurface: Color,
    val surface: Color,
    val activeCapsuleColor: Color,
    val inactiveCapsuleColor: Color,
    val buttonEnabledBorderColor: Color,
    val buttonDisabledBorderColor: Color,
    val buttonEnabledContentColor: Color,
    val buttonDisabledContentColor: Color
)

object Ux4gCompactStepperDefaults {
    @Composable
    fun colors(
        primary: Color = Ux4gColors.primary,
        onSurface: Color = Ux4gColors.neutral900,
        surface: Color = Ux4gColors.white,
        activeCapsuleColor: Color = Ux4gColors.primary,
        inactiveCapsuleColor: Color = Ux4gColors.neutral900.copy(alpha = 0.2f),
        buttonEnabledBorderColor: Color = Ux4gColors.primary.copy(alpha = 0.12f),
        buttonDisabledBorderColor: Color = Ux4gColors.neutral900.copy(alpha = 0.2f),
        buttonEnabledContentColor: Color = Ux4gColors.primary,
        buttonDisabledContentColor: Color = Ux4gColors.neutral900.copy(alpha = 0.3f)
    ): Ux4gCompactStepperColors = Ux4gCompactStepperColors(
        primary = primary,
        onSurface = onSurface,
        surface = surface,
        activeCapsuleColor = activeCapsuleColor,
        inactiveCapsuleColor = inactiveCapsuleColor,
        buttonEnabledBorderColor = buttonEnabledBorderColor,
        buttonDisabledBorderColor = buttonDisabledBorderColor,
        buttonEnabledContentColor = buttonEnabledContentColor,
        buttonDisabledContentColor = buttonDisabledContentColor
    )
}

// ─── Main Compact Stepper Composable ────────────────────────────────────────

/**
 * A highly versatile Compact (Capsule) Stepper component matching UX4G Flutter specification 1:1.
 *
 * Supports 5 layout arrangements ([Ux4gCompactStepperLayout]):
 * - [Ux4gCompactStepperLayout.Linear]
 * - [Ux4gCompactStepperLayout.RightAligned]
 * - [Ux4gCompactStepperLayout.Centered]
 * - [Ux4gCompactStepperLayout.CenteredBetween]
 * - [Ux4gCompactStepperLayout.Split]
 *
 * @param totalSteps Total number of steps.
 * @param currentStep Current active step (1-indexed).
 * @param stepLabel Primary title/label for the current step.
 * @param description Optional description for the current step.
 * @param onNext Callback invoked when clicking Next arrow.
 * @param onPrevious Callback invoked when clicking Previous arrow.
 * @param layout Layout arrangement variant.
 * @param labelAlignment Horizontal alignment of labels (used in Linear layout).
 * @param activeColor Optional override for active capsule color.
 * @param inactiveColor Optional override for inactive capsule color.
 * @param colors Color palette configuration.
 * @param modifier Modifier for the container.
 */
@Composable
fun Ux4gCompactStepper(
    totalSteps: Int,
    currentStep: Int,
    stepLabel: String,
    modifier: Modifier = Modifier,
    description: String? = null,
    onNext: () -> Unit = {},
    onPrevious: () -> Unit = {},
    layout: Ux4gCompactStepperLayout = Ux4gCompactStepperLayout.Linear,
    labelAlignment: Alignment.Horizontal = Alignment.Start,
    activeColor: Color? = null,
    inactiveColor: Color? = null,
    colors: Ux4gCompactStepperColors = Ux4gCompactStepperDefaults.colors()
) {
    val resolvedActiveColor = activeColor ?: colors.activeCapsuleColor
    val resolvedInactiveColor = inactiveColor ?: colors.inactiveCapsuleColor

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        when (layout) {
            Ux4gCompactStepperLayout.Linear -> {
                LinearCapsuleStepper(
                    totalSteps = totalSteps,
                    currentStep = currentStep,
                    stepLabel = stepLabel,
                    description = description,
                    onNext = onNext,
                    onPrevious = onPrevious,
                    labelAlignment = labelAlignment,
                    activeColor = resolvedActiveColor,
                    inactiveColor = resolvedInactiveColor,
                    colors = colors
                )
            }
            Ux4gCompactStepperLayout.RightAligned -> {
                RightAlignedCapsuleStepper(
                    totalSteps = totalSteps,
                    currentStep = currentStep,
                    stepLabel = stepLabel,
                    description = description,
                    onNext = onNext,
                    onPrevious = onPrevious,
                    activeColor = resolvedActiveColor,
                    inactiveColor = resolvedInactiveColor,
                    colors = colors
                )
            }
            Ux4gCompactStepperLayout.Centered -> {
                CenteredCapsuleStepper(
                    totalSteps = totalSteps,
                    currentStep = currentStep,
                    stepLabel = stepLabel,
                    description = description,
                    onNext = onNext,
                    onPrevious = onPrevious,
                    activeColor = resolvedActiveColor,
                    inactiveColor = resolvedInactiveColor,
                    colors = colors
                )
            }
            Ux4gCompactStepperLayout.CenteredBetween -> {
                CenteredBetweenCapsuleStepper(
                    totalSteps = totalSteps,
                    currentStep = currentStep,
                    stepLabel = stepLabel,
                    description = description,
                    onNext = onNext,
                    onPrevious = onPrevious,
                    activeColor = resolvedActiveColor,
                    inactiveColor = resolvedInactiveColor,
                    colors = colors
                )
            }
            Ux4gCompactStepperLayout.Split -> {
                SplitCapsuleStepper(
                    totalSteps = totalSteps,
                    currentStep = currentStep,
                    stepLabel = stepLabel,
                    description = description,
                    onNext = onNext,
                    onPrevious = onPrevious,
                    activeColor = resolvedActiveColor,
                    inactiveColor = resolvedInactiveColor,
                    colors = colors
                )
            }
        }
    }
}

/**
 * Backward compatibility alias for [Ux4gCompactStepper].
 */
@Composable
fun Ux4gCapsuleStepper(
    totalSteps: Int,
    currentStep: Int,
    stepLabel: String,
    modifier: Modifier = Modifier,
    description: String? = null,
    onNext: () -> Unit = {},
    onPrevious: () -> Unit = {},
    layout: Ux4gCompactStepperLayout = Ux4gCompactStepperLayout.Linear,
    labelAlignment: Alignment.Horizontal = Alignment.Start,
    activeColor: Color? = null,
    inactiveColor: Color? = null,
    colors: Ux4gCompactStepperColors = Ux4gCompactStepperDefaults.colors()
) {
    Ux4gCompactStepper(
        totalSteps = totalSteps,
        currentStep = currentStep,
        stepLabel = stepLabel,
        modifier = modifier,
        description = description,
        onNext = onNext,
        onPrevious = onPrevious,
        layout = layout,
        labelAlignment = labelAlignment,
        activeColor = activeColor,
        inactiveColor = inactiveColor,
        colors = colors
    )
}

// ─── Layout 1: Linear ───────────────────────────────────────────────────────

@Composable
private fun LinearCapsuleStepper(
    totalSteps: Int,
    currentStep: Int,
    stepLabel: String,
    description: String?,
    onNext: () -> Unit,
    onPrevious: () -> Unit,
    labelAlignment: Alignment.Horizontal,
    activeColor: Color,
    inactiveColor: Color,
    colors: Ux4gCompactStepperColors
) {
    val centered = labelAlignment == Alignment.CenterHorizontally
    val textAlign = if (centered) TextAlign.Center else TextAlign.Start

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.Start
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            StepperIconButton(
                icon = Ux4gIcons.arrowLeft,
                enabled = currentStep > 1,
                onClick = onPrevious,
                colors = colors
            )

            Box(
                modifier = Modifier.weight(1f),
                contentAlignment = Alignment.Center
            ) {
                CapsuleIndicator(
                    totalSteps = totalSteps,
                    currentStep = currentStep,
                    activeColor = activeColor,
                    inactiveColor = inactiveColor
                )
            }

            StepperIconButton(
                icon = Ux4gIcons.arrowRight,
                enabled = currentStep < totalSteps,
                onClick = onNext,
                colors = colors
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = labelAlignment
        ) {
            Text(
                text = "Step $currentStep of $totalSteps",
                style = defaultTypography.lM_strong,
                color = colors.onSurface,
                textAlign = textAlign
            )
            Text(
                text = stepLabel,
                style = defaultTypography.lL_strong,
                color = colors.primary,
                textAlign = textAlign
            )
            if (description != null) {
                Text(
                    text = description,
                    style = defaultTypography.lM_default,
                    color = colors.onSurface.copy(alpha = 0.5f),
                    textAlign = textAlign
                )
            }
        }
    }
}

// ─── Layout 2: Right-Aligned ────────────────────────────────────────────────

@Composable
private fun RightAlignedCapsuleStepper(
    totalSteps: Int,
    currentStep: Int,
    stepLabel: String,
    description: String?,
    onNext: () -> Unit,
    onPrevious: () -> Unit,
    activeColor: Color,
    inactiveColor: Color,
    colors: Ux4gCompactStepperColors
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.Start
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            StepperIconButton(
                icon = Ux4gIcons.arrowLeft,
                enabled = currentStep > 1,
                onClick = onPrevious,
                colors = colors
            )

            Box(
                modifier = Modifier.weight(1f),
                contentAlignment = Alignment.Center
            ) {
                CapsuleIndicator(
                    totalSteps = totalSteps,
                    currentStep = currentStep,
                    activeColor = activeColor,
                    inactiveColor = inactiveColor
                )
            }

            StepperIconButton(
                icon = Ux4gIcons.arrowRight,
                enabled = currentStep < totalSteps,
                onClick = onNext,
                colors = colors
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
        ) {
            Text(
                text = "Step $currentStep of $totalSteps",
                style = defaultTypography.lM_strong,
                color = colors.onSurface
            )

            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = stepLabel,
                    style = defaultTypography.lL_strong,
                    color = colors.primary,
                    textAlign = TextAlign.End
                )
                if (description != null) {
                    Text(
                        text = description,
                        style = defaultTypography.lM_default,
                        color = colors.onSurface.copy(alpha = 0.5f),
                        textAlign = TextAlign.End
                    )
                }
            }
        }
    }
}

// ─── Layout 3: Centered ─────────────────────────────────────────────────────

@Composable
private fun CenteredCapsuleStepper(
    totalSteps: Int,
    currentStep: Int,
    stepLabel: String,
    description: String?,
    onNext: () -> Unit,
    onPrevious: () -> Unit,
    activeColor: Color,
    inactiveColor: Color,
    colors: Ux4gCompactStepperColors
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            CapsuleIndicator(
                totalSteps = totalSteps,
                currentStep = currentStep,
                activeColor = activeColor,
                inactiveColor = inactiveColor
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            StepperIconButton(
                icon = Ux4gIcons.arrowLeft,
                enabled = currentStep > 1,
                onClick = onPrevious,
                colors = colors
            )

            Text(
                text = "Step $currentStep of $totalSteps",
                style = defaultTypography.lM_strong,
                color = colors.onSurface,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            StepperIconButton(
                icon = Ux4gIcons.arrowRight,
                enabled = currentStep < totalSteps,
                onClick = onNext,
                colors = colors
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = stepLabel,
            style = defaultTypography.lL_strong,
            color = colors.primary,
            textAlign = TextAlign.Center
        )

        if (description != null) {
            Text(
                text = description,
                style = defaultTypography.lM_default,
                color = colors.onSurface.copy(alpha = 0.5f),
                textAlign = TextAlign.Center
            )
        }
    }
}

// ─── Layout 4: Centered-Between ─────────────────────────────────────────────

@Composable
private fun CenteredBetweenCapsuleStepper(
    totalSteps: Int,
    currentStep: Int,
    stepLabel: String,
    description: String?,
    onNext: () -> Unit,
    onPrevious: () -> Unit,
    activeColor: Color,
    inactiveColor: Color,
    colors: Ux4gCompactStepperColors
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            CapsuleIndicator(
                totalSteps = totalSteps,
                currentStep = currentStep,
                activeColor = activeColor,
                inactiveColor = inactiveColor
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Step $currentStep of $totalSteps",
            style = defaultTypography.lM_strong,
            color = colors.onSurface
        )

        Spacer(modifier = Modifier.height(4.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            StepperIconButton(
                icon = Ux4gIcons.arrowLeft,
                enabled = currentStep > 1,
                onClick = onPrevious,
                colors = colors
            )

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stepLabel,
                    style = defaultTypography.lL_strong,
                    color = colors.primary,
                    textAlign = TextAlign.Center
                )
                if (description != null) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = description,
                        style = defaultTypography.lM_default,
                        color = colors.onSurface.copy(alpha = 0.5f),
                        textAlign = TextAlign.Center
                    )
                }
            }

            StepperIconButton(
                icon = Ux4gIcons.arrowRight,
                enabled = currentStep < totalSteps,
                onClick = onNext,
                colors = colors
            )
        }
    }
}

// ─── Layout 5: Split ────────────────────────────────────────────────────────

@Composable
private fun SplitCapsuleStepper(
    totalSteps: Int,
    currentStep: Int,
    stepLabel: String,
    description: String?,
    onNext: () -> Unit,
    onPrevious: () -> Unit,
    activeColor: Color,
    inactiveColor: Color,
    colors: Ux4gCompactStepperColors
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.Start
    ) {
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            CapsuleIndicator(
                totalSteps = totalSteps,
                currentStep = currentStep,
                activeColor = activeColor,
                inactiveColor = inactiveColor
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
        ) {
            // Left Column: Title & Description
            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = stepLabel,
                    style = defaultTypography.lL_strong,
                    color = colors.primary
                )
                if (description != null) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = description,
                        style = defaultTypography.lM_default,
                        color = colors.onSurface.copy(alpha = 0.5f)
                    )
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Right Column: Step Count & Arrow Buttons
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = "Step $currentStep of $totalSteps",
                    style = defaultTypography.lM_strong,
                    color = colors.onSurface
                )
                Spacer(modifier = Modifier.height(12.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    StepperIconButton(
                        icon = Ux4gIcons.arrowLeft,
                        enabled = currentStep > 1,
                        onClick = onPrevious,
                        colors = colors
                    )
                    StepperIconButton(
                        icon = Ux4gIcons.arrowRight,
                        enabled = currentStep < totalSteps,
                        onClick = onNext,
                        colors = colors
                    )
                }
            }
        }
    }
}

// ─── Helper Components ──────────────────────────────────────────────────────

@Composable
internal fun CapsuleIndicator(
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

            val animatedColor by animateColorAsState(
                targetValue = if (isActive) activeColor else inactiveColor,
                animationSpec = tween(300),
                label = "capsule_color"
            )

            val animatedWidth by animateDpAsState(
                targetValue = if (isActive) 32.dp else 16.dp,
                animationSpec = tween(300),
                label = "capsule_width"
            )

            Box(
                modifier = Modifier
                    .width(animatedWidth)
                    .height(8.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(animatedColor)
            )
        }
    }
}

@Composable
internal fun StepperIconButton(
    icon: Ux4gIcon,
    enabled: Boolean,
    onClick: () -> Unit,
    colors: Ux4gCompactStepperColors,
    modifier: Modifier = Modifier
) {
    val borderColor = if (enabled) colors.buttonEnabledBorderColor else colors.buttonDisabledBorderColor
    val iconTint = if (enabled) colors.buttonEnabledContentColor else colors.buttonDisabledContentColor

    Box(
        modifier = modifier
            .size(40.dp)
            .clip(CircleShape)
            .background(colors.surface)
            .border(1.dp, borderColor, CircleShape)
            .clickable(enabled = enabled, onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Ux4gIconComponent(
            icon = icon,
            contentDescription = null,
            tint = iconTint,
            modifier = Modifier.size(20.dp)
        )
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Previews
// ─────────────────────────────────────────────────────────────────────────────

@Preview(name = "1. Linear Layout", showBackground = true)
@Composable
fun Ux4gCompactStepperLinearPreview() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Ux4gCompactStepper(
            totalSteps = 5,
            currentStep = 2,
            stepLabel = "Personal Information",
            description = "Please fill out your personal details",
            layout = Ux4gCompactStepperLayout.Linear
        )
    }
}

@Preview(name = "2. Right-Aligned Layout", showBackground = true)
@Composable
fun Ux4gCompactStepperRightAlignedPreview() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Ux4gCompactStepper(
            totalSteps = 5,
            currentStep = 3,
            stepLabel = "Account Verification",
            description = "Verify with mobile OTP",
            layout = Ux4gCompactStepperLayout.RightAligned
        )
    }
}

@Preview(name = "3. Centered Layout", showBackground = true)
@Composable
fun Ux4gCompactStepperCenteredPreview() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Ux4gCompactStepper(
            totalSteps = 4,
            currentStep = 2,
            stepLabel = "Document Upload",
            description = "Upload proof of identity",
            layout = Ux4gCompactStepperLayout.Centered
        )
    }
}

@Preview(name = "4. Centered-Between Layout", showBackground = true)
@Composable
fun Ux4gCompactStepperCenteredBetweenPreview() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Ux4gCompactStepper(
            totalSteps = 4,
            currentStep = 3,
            stepLabel = "Payment Options",
            description = "Select payment method",
            layout = Ux4gCompactStepperLayout.CenteredBetween
        )
    }
}

@Preview(name = "5. Split Layout", showBackground = true)
@Composable
fun Ux4gCompactStepperSplitPreview() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Ux4gCompactStepper(
            totalSteps = 5,
            currentStep = 4,
            stepLabel = "Final Review",
            description = "Confirm and submit your application",
            layout = Ux4gCompactStepperLayout.Split
        )
    }
}
