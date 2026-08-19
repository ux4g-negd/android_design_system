package com.ux4g.designsystemv1.components

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.layout
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ux4g.designsystemv1.foundation.Ux4gColors
import com.ux4g.designsystemv1.foundation.Ux4gIcon
import com.ux4g.designsystemv1.foundation.Ux4gIconComponent
import com.ux4g.designsystemv1.foundation.Ux4gIcons
import com.ux4g.designsystemv1.foundation.Ux4gSpace
import com.ux4g.designsystemv1.foundation.defaultTypography
import com.ux4g.designsystemv1.theme.Ux4gTheme

// ─── Enums ──────────────────────────────────────────────────────────────────

/**
 * Orientation of the status pipeline.
 */
enum class Ux4gPipelineOrientation {
    Vertical,
    Horizontal
}

/**
 * State of a pipeline step.
 */
enum class Ux4gPipelineStepState {
    /** Completed step — green circle with check icon. */
    Completed,

    /** Current/active step — primary colored circle with inner surface ring. */
    Current,

    /** Upcoming step — outlined circle with step number. */
    Upcoming,

    /** Error/failed step — red outlined circle with step number. */
    Error,

    /** Warning step — orange outlined circle with step number. */
    Warning
}

/**
 * Size presets for the pipeline.
 */
enum class Ux4gPipelineSize {
    /** Small — 20dp circle, 2dp line. */
    S,

    /** Medium — 32dp circle, 3dp line. */
    M,

    /** Large — 40dp circle, 3dp line. */
    L
}

// ─── Step Model ─────────────────────────────────────────────────────────────

/**
 * Data model for a single pipeline step.
 *
 * @param label Label text (e.g., "Submitted", "Verification").
 * @param description Description text (e.g., "5 Apr", "In progress").
 * @param state Step state — controls icon, color, and line style.
 * @param customIcon Custom icon composable to display inside the circle.
 * @param customIconVector Custom Ux4gIcon to display inside the circle.
 * @param customColor Custom color for the step circle (overrides state color).
 */
data class Ux4gPipelineStep(
    val label: String? = null,
    val description: String? = null,
    val state: Ux4gPipelineStepState = Ux4gPipelineStepState.Upcoming,
    val customIcon: (@Composable () -> Unit)? = null,
    val customIconVector: Ux4gIcon? = null,
    val customColor: Color? = null
)

// ─── Status Pipeline Component ──────────────────────────────────────────────

/**
 * A status pipeline component that displays process steps connected by lines.
 * Supports both vertical and horizontal orientations with state auto-resolution.
 *
 * Matches the Flutter Status Pipeline specification for layout, typography, and colors.
 */
@Composable
fun Ux4gStatusPipeline(
    steps: List<Ux4gPipelineStep>,
    modifier: Modifier = Modifier,
    currentStep: Int = 0,
    orientation: Ux4gPipelineOrientation = Ux4gPipelineOrientation.Vertical,
    size: Ux4gPipelineSize = Ux4gPipelineSize.M,
    showLabels: Boolean = true,
    showDescriptions: Boolean = true,
    activeLineWidth: Dp? = null,
    inactiveLineWidth: Dp? = null,
    completedColor: Color? = null,
    currentColor: Color? = null,
    upcomingColor: Color? = null,
    errorColor: Color? = null,
    warningColor: Color? = null,
    completedLineColor: Color? = null,
    upcomingLineColor: Color? = null,
    labelSpacing: Dp? = null,
    circleSize: Dp? = null
) {
    val isDark = isSystemInDarkTheme()
    val onSurface = if (isDark) Ux4gColors.neutral0 else Ux4gColors.neutral900

    val resolvedCircleSize = circleSize ?: when (size) {
        Ux4gPipelineSize.S -> 20.dp
        Ux4gPipelineSize.M -> 32.dp
        Ux4gPipelineSize.L -> 40.dp
    }

    val resolvedActiveLineW = activeLineWidth ?: when (size) {
        Ux4gPipelineSize.S -> 2.dp
        Ux4gPipelineSize.M -> 3.dp
        Ux4gPipelineSize.L -> 3.dp
    }

    val resolvedInactiveLineW = inactiveLineWidth ?: when (size) {
        Ux4gPipelineSize.S -> 1.dp
        Ux4gPipelineSize.M -> 2.dp
        Ux4gPipelineSize.L -> 2.dp
    }

    val resolvedIconSize = when (size) {
        Ux4gPipelineSize.S -> 12.dp
        Ux4gPipelineSize.M -> 18.dp
        Ux4gPipelineSize.L -> 22.dp
    }

    val resolvedFontSize = when (size) {
        Ux4gPipelineSize.S -> 10.sp
        Ux4gPipelineSize.M -> 12.sp
        Ux4gPipelineSize.L -> 14.sp
    }

    val stepColor: (Ux4gPipelineStepState) -> Color = { state ->
        when (state) {
            Ux4gPipelineStepState.Completed -> completedColor ?: Ux4gColors.green600
            Ux4gPipelineStepState.Current -> currentColor ?: (if (isDark) Ux4gColors.primary300 else Ux4gColors.primary600)
            Ux4gPipelineStepState.Upcoming -> upcomingColor ?: onSurface.copy(alpha = 0.3f)
            Ux4gPipelineStepState.Error -> errorColor ?: Ux4gColors.red600
            Ux4gPipelineStepState.Warning -> warningColor ?: Ux4gColors.orange600
        }
    }

    val lineColor: (Ux4gPipelineStepState) -> Color = { fromState ->
        val isActive = fromState == Ux4gPipelineStepState.Completed
        if (isActive) {
            completedLineColor ?: (completedColor ?: Ux4gColors.green600)
        } else {
            upcomingLineColor ?: onSurface.copy(alpha = 0.15f)
        }
    }

    val lineWidth: (Ux4gPipelineStepState) -> Dp = { fromState ->
        val isActive = fromState == Ux4gPipelineStepState.Completed
        if (isActive) resolvedActiveLineW else resolvedInactiveLineW
    }

    val labelStyle: (Ux4gPipelineStepState) -> TextStyle = { state ->
        val isActive = state == Ux4gPipelineStepState.Completed || state == Ux4gPipelineStepState.Current
        val textColor = when (state) {
            Ux4gPipelineStepState.Error -> if (isDark) Ux4gColors.red300 else Ux4gColors.red800
            else -> if (isActive) onSurface else onSurface.copy(alpha = 0.45f)
        }
        val baseStyle = when (size) {
            Ux4gPipelineSize.S -> defaultTypography.bXS_strong
            Ux4gPipelineSize.M -> defaultTypography.bS_strong
            Ux4gPipelineSize.L -> defaultTypography.bM_strong
        }
        baseStyle.copy(color = textColor)
    }

    val descStyle: (Ux4gPipelineStepState) -> TextStyle = { state ->
        val isActive = state == Ux4gPipelineStepState.Completed || state == Ux4gPipelineStepState.Current
        val textColor = when (state) {
            Ux4gPipelineStepState.Error -> if (isDark) Ux4gColors.red300 else Ux4gColors.red800
            else -> if (isActive) onSurface.copy(alpha = 0.6f) else onSurface.copy(alpha = 0.35f)
        }
        val baseStyle = when (size) {
            Ux4gPipelineSize.S -> defaultTypography.bXS_default
            Ux4gPipelineSize.M -> defaultTypography.bXS_default
            Ux4gPipelineSize.L -> defaultTypography.bS_default
        }
        baseStyle.copy(color = textColor)
    }

    if (orientation == Ux4gPipelineOrientation.Vertical) {
        Column(
            modifier = modifier,
            verticalArrangement = Arrangement.spacedBy(0.dp)
        ) {
            for (i in steps.indices) {
                val step = steps[i]
                val state = resolveState(i, step, currentStep)
                val isLast = i == steps.size - 1
                val hasLabel = showLabels && step.label != null
                val hasDesc = showDescriptions && step.description != null
                val spacing = labelSpacing ?: Ux4gSpace.Space12
                val minLineHeight = if (hasLabel || hasDesc) 28.dp else 24.dp

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(IntrinsicSize.Min),
                    verticalAlignment = Alignment.Top
                ) {
                    // Circle + connecting vertical line column
                    Column(
                        modifier = Modifier.width(resolvedCircleSize),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        PipelineStepCircle(
                            state = state,
                            index = i,
                            size = resolvedCircleSize,
                            iconSize = resolvedIconSize,
                            fontSize = resolvedFontSize,
                            color = step.customColor ?: stepColor(state),
                            customIcon = step.customIcon,
                            customIconVector = step.customIconVector
                        )

                        if (!isLast) {
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .defaultMinSize(minHeight = minLineHeight)
                                    .width(lineWidth(state))
                                    .background(lineColor(state))
                            )
                        }
                    }

                    // Label and Description column
                    if (hasLabel || hasDesc) {
                        Spacer(modifier = Modifier.width(spacing))
                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .defaultMinSize(minHeight = resolvedCircleSize)
                                .padding(bottom = if (isLast) 0.dp else Ux4gSpace.Space8),
                            verticalArrangement = Arrangement.Center
                        ) {
                            if (hasLabel) {
                                Text(
                                    text = step.label!!,
                                    style = labelStyle(state)
                                )
                            }
                            if (hasLabel && hasDesc) {
                                Spacer(modifier = Modifier.height(2.dp))
                            }
                            if (hasDesc) {
                                Text(
                                    text = step.description!!,
                                    style = descStyle(state)
                                )
                            }
                        }
                    }
                }
            }
        }
    } else {
        // Horizontal Layout
        Column(
            modifier = modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Circles and horizontal connecting lines row
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                for (i in steps.indices) {
                    val step = steps[i]
                    val state = resolveState(i, step, currentStep)
                    val isLast = i == steps.size - 1

                    PipelineStepCircle(
                        state = state,
                        index = i,
                        size = resolvedCircleSize,
                        iconSize = resolvedIconSize,
                        fontSize = resolvedFontSize,
                        color = step.customColor ?: stepColor(state),
                        customIcon = step.customIcon,
                        customIconVector = step.customIconVector
                    )

                    if (!isLast) {
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(lineWidth(state))
                                .background(lineColor(state))
                        )
                    }
                }
            }

            // Labels and Descriptions row below circles
            val hasAnyLabel = showLabels && steps.any { it.label != null }
            val hasAnyDesc = showDescriptions && steps.any { it.description != null }

            if (hasAnyLabel || hasAnyDesc) {
                Spacer(modifier = Modifier.height(labelSpacing ?: Ux4gSpace.Space6))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.Top
                ) {
                    for (i in steps.indices) {
                        val step = steps[i]
                        val state = resolveState(i, step, currentStep)
                        val isLast = i == steps.size - 1

                        Box(
                            modifier = Modifier
                                .width(resolvedCircleSize)
                                .layout { measurable, constraints ->
                                    val targetMaxWidth = 120.dp.roundToPx()
                                    val placeable = measurable.measure(
                                        constraints.copy(
                                            minWidth = 0,
                                            maxWidth = targetMaxWidth
                                        )
                                    )
                                    layout(constraints.maxWidth, placeable.height) {
                                        val xOffset = (constraints.maxWidth - placeable.width) / 2
                                        placeable.placeRelative(xOffset, 0)
                                    }
                                },
                            contentAlignment = Alignment.TopCenter
                        ) {
                            Column(
                                modifier = Modifier.widthIn(max = 120.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                if (showLabels && step.label != null) {
                                    Text(
                                        text = step.label,
                                        style = labelStyle(state),
                                        textAlign = TextAlign.Center
                                    )
                                }
                                if (showLabels && step.label != null && showDescriptions && step.description != null) {
                                    Spacer(modifier = Modifier.height(2.dp))
                                }
                                if (showDescriptions && step.description != null) {
                                    Text(
                                        text = step.description,
                                        style = descStyle(state),
                                        textAlign = TextAlign.Center
                                    )
                                }
                            }
                        }

                        if (!isLast) {
                            Spacer(modifier = Modifier.weight(1f))
                        }
                    }
                }
            }
        }
    }
}

// ─── Step Circle Component ──────────────────────────────────────────────────

@Composable
private fun PipelineStepCircle(
    state: Ux4gPipelineStepState,
    index: Int,
    size: Dp,
    iconSize: Dp,
    fontSize: androidx.compose.ui.unit.TextUnit,
    color: Color,
    customIcon: (@Composable () -> Unit)? = null,
    customIconVector: Ux4gIcon? = null
) {
    if (customIcon != null) {
        Box(
            modifier = Modifier
                .size(size)
                .clip(CircleShape)
                .background(color),
            contentAlignment = Alignment.Center
        ) {
            customIcon()
        }
        return
    }

    if (customIconVector != null) {
        Box(
            modifier = Modifier
                .size(size)
                .clip(CircleShape)
                .background(color),
            contentAlignment = Alignment.Center
        ) {
            Ux4gIconComponent(
                icon = customIconVector,
                contentDescription = null,
                tint = Ux4gColors.white,
                modifier = Modifier.size(iconSize)
            )
        }
        return
    }

    when (state) {
        Ux4gPipelineStepState.Completed -> {
            Box(
                modifier = Modifier
                    .size(size)
                    .clip(CircleShape)
                    .background(color),
                contentAlignment = Alignment.Center
            ) {
                Ux4gIconComponent(
                    icon = Ux4gIcons.Check,
                    contentDescription = "Completed",
                    tint = Ux4gColors.white,
                    modifier = Modifier.size(iconSize)
                )
            }
        }
        Ux4gPipelineStepState.Current -> {
            val innerSize = size * 0.55f
            val isDark = isSystemInDarkTheme()
            val surfaceColor = if (isDark) Ux4gColors.neutral900 else Ux4gColors.white

            Box(
                modifier = Modifier
                    .size(size)
                    .clip(CircleShape)
                    .background(color),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(innerSize)
                        .clip(CircleShape)
                        .background(surfaceColor)
                )
            }
        }
        Ux4gPipelineStepState.Upcoming,
        Ux4gPipelineStepState.Error,
        Ux4gPipelineStepState.Warning -> {
            Box(
                modifier = Modifier
                    .size(size)
                    .border(width = 1.5.dp, color = color, shape = CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "${index + 1}",
                    color = color,
                    fontSize = fontSize,
                    fontWeight = FontWeight.W600,
                    lineHeight = fontSize
                )
            }
        }
    }
}

/**
 * Resolve state of step from current active step index.
 */
private fun resolveState(index: Int, step: Ux4gPipelineStep, currentStep: Int): Ux4gPipelineStepState {
    if (currentStep < 0) return step.state
    if (step.state == Ux4gPipelineStepState.Error || step.state == Ux4gPipelineStepState.Warning) {
        return step.state
    }
    return when {
        index < currentStep -> Ux4gPipelineStepState.Completed
        index == currentStep -> Ux4gPipelineStepState.Current
        else -> Ux4gPipelineStepState.Upcoming
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Previews
// ─────────────────────────────────────────────────────────────────────────────

@Preview(name = "1. Vertical Pipeline", showBackground = true)
@Composable
fun Ux4gStatusPipelineVerticalPreview() {
    Ux4gTheme {
        Surface(
            modifier = Modifier.padding(24.dp),
            color = Ux4gTheme.colorScheme.background
        ) {
            Ux4gStatusPipeline(
                orientation = Ux4gPipelineOrientation.Vertical,
                size = Ux4gPipelineSize.M,
                currentStep = 1,
                steps = listOf(
                    Ux4gPipelineStep(label = "Submitted", description = "5 Apr, 10:30 AM"),
                    Ux4gPipelineStep(label = "Verification", description = "In progress"),
                    Ux4gPipelineStep(label = "Inspection", description = "Est. 10 Apr"),
                    Ux4gPipelineStep(label = "Decision", description = "Est. 18 Apr"),
                    Ux4gPipelineStep(label = "Issued", description = "Est. 20 Apr")
                )
            )
        }
    }
}

@Preview(name = "2. Horizontal Pipeline", showBackground = true)
@Composable
fun Ux4gStatusPipelineHorizontalPreview() {
    Ux4gTheme {
        Surface(
            modifier = Modifier.padding(24.dp),
            color = Ux4gTheme.colorScheme.background
        ) {
            Ux4gStatusPipeline(
                orientation = Ux4gPipelineOrientation.Horizontal,
                size = Ux4gPipelineSize.M,
                currentStep = 2,
                steps = listOf(
                    Ux4gPipelineStep(label = "Ordered", description = "Paid"),
                    Ux4gPipelineStep(label = "Shipped", description = "In transit"),
                    Ux4gPipelineStep(label = "Out for Delivery", description = "Today"),
                    Ux4gPipelineStep(label = "Delivered", description = "Pending")
                )
            )
        }
    }
}

@Preview(name = "3. Error & Warning States", showBackground = true)
@Preview(name = "3. Dark Mode", uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun Ux4gStatusPipelineStatesPreview() {
    Ux4gTheme {
        Surface(
            modifier = Modifier.padding(24.dp),
            color = Ux4gTheme.colorScheme.background
        ) {
            Ux4gStatusPipeline(
                currentStep = -1,
                steps = listOf(
                    Ux4gPipelineStep(
                        label = "KYC Uploaded",
                        description = "Approved",
                        state = Ux4gPipelineStepState.Completed
                    ),
                    Ux4gPipelineStep(
                        label = "Address Verification",
                        description = "Warning: Incomplete document",
                        state = Ux4gPipelineStepState.Warning
                    ),
                    Ux4gPipelineStep(
                        label = "Bank Details",
                        description = "Failed: Invalid IFSC Code",
                        state = Ux4gPipelineStepState.Error
                    ),
                    Ux4gPipelineStep(
                        label = "Final Activation",
                        description = "Waiting for verification",
                        state = Ux4gPipelineStepState.Upcoming
                    )
                )
            )
        }
    }
}
