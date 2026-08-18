package com.ux4g.designsystemv1.components

import android.content.res.Configuration
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ux4g.designsystemv1.foundation.Ux4gColors
import com.ux4g.designsystemv1.foundation.Ux4gIcon
import com.ux4g.designsystemv1.foundation.Ux4gIconComponent
import com.ux4g.designsystemv1.foundation.Ux4gIcons
import com.ux4g.designsystemv1.foundation.Ux4gRadius
import com.ux4g.designsystemv1.foundation.Ux4gSpace
import com.ux4g.designsystemv1.foundation.defaultTypography
import com.ux4g.designsystemv1.theme.Ux4gTheme

// ─── Step State ─────────────────────────────────────────────────────────────

/**
 * State of a journey step.
 */
enum class Ux4gJourneyStepState {
    /** Completed step — filled circle with check icon (or stepNumber), primary colored line. */
    Completed,

    /** Current/active step — outlined filled circle, primary colored line. */
    Current,

    /** Upcoming step — gray outlined circle, gray line. */
    Upcoming
}

// ─── Step Card Content ──────────────────────────────────────────────────────

/**
 * Position of the status badge in the step card.
 */
enum class Ux4gJourneyBadgePosition {
    /** Badge renders at the bottom of the card (default, original behavior). */
    Bottom,

    /** Badge renders at the top-right corner next to the date. */
    TopRight
}

/**
 * Optional status indicator shown inside a step card.
 *
 * @param text Status text (e.g., "2 days remaining").
 * @param dotColor Color of the status dot.
 * @param badgeText Optional badge/tag text (e.g., "Pending").
 * @param badgeColor Badge background color override.
 * @param badgeTextColor Badge text color override.
 * @param badgePosition Position of the badge in the card. Default: [Ux4gJourneyBadgePosition.Bottom].
 */
data class Ux4gJourneyStepStatus(
    val text: String,
    val dotColor: Color? = null,
    val badgeText: String? = null,
    val badgeColor: Color? = null,
    val badgeTextColor: Color? = null,
    val badgePosition: Ux4gJourneyBadgePosition = Ux4gJourneyBadgePosition.Bottom
)

/**
 * Data model for a single step in the journey timeline.
 *
 * @param title Title text.
 * @param state State of this step (completed, current, upcoming).
 * @param date Date text shown at the top-left of the card.
 * @param tag Tag text shown at the top-right of the card.
 * @param helpingText Optional helping/description text below the title.
 * @param icon Optional icon below the title (e.g., link icon).
 * @param iconColor Optional icon color override.
 * @param status Optional status indicator with dot + badge.
 * @param customContent Optional custom content composable below the title area.
 * @param stepNumber Optional step number to show inside the indicator circle.
 * @param cardColor Optional background color override for this specific step card.
 * @param cardBorderColor Optional border color override for this specific step card.
 * @param dateColor Optional date text color override.
 * @param titleColor Optional title text color override.
 * @param dateStyle Optional date text style override.
 * @param titleStyle Optional title text style override.
 */
data class Ux4gJourneyStep(
    val title: String,
    val state: Ux4gJourneyStepState = Ux4gJourneyStepState.Upcoming,
    val date: String? = null,
    val tag: String? = null,
    val helpingText: String? = null,
    val icon: Ux4gIcon? = null,
    val iconColor: Color? = null,
    val status: Ux4gJourneyStepStatus? = null,
    val customContent: (@Composable () -> Unit)? = null,
    val stepNumber: String? = null,
    val cardColor: Color? = null,
    val cardBorderColor: Color? = null,
    val dateColor: Color? = null,
    val titleColor: Color? = null,
    val dateStyle: TextStyle? = null,
    val titleStyle: TextStyle? = null
)

// ─── Journey Timeline Header ────────────────────────────────────────────────

/**
 * Header data for the journey timeline.
 *
 * @param title Title text.
 * @param description Description text below the title.
 * @param icon Optional trailing icon.
 * @param onIconPressed Callback when trailing icon is tapped.
 */
data class Ux4gJourneyHeader(
    val title: String,
    val description: String? = null,
    val icon: Ux4gIcon? = null,
    val onIconPressed: (() -> Unit)? = null
)

// ─── Main Component ─────────────────────────────────────────────────────────

/**
 * Orientation for the journey timeline.
 */
enum class Ux4gJourneyOrientation {
    Vertical,
    Horizontal
}

/**
 * A journey timeline component that displays steps in a vertical or horizontal timeline.
 *
 * Each step shows a circle indicator connected by lines, with a card
 * containing date, tag, title, and optional content.
 *
 * Direct color combinations from the Flutter Ux4gColors design system palette.
 *
 * @param steps List of journey steps.
 * @param modifier Modifier applied to the outer container.
 * @param header Optional header above the timeline.
 * @param currentStep Current step index (0-based). Steps before this are completed,
 * this index is current, and after are upcoming. If null, uses each step's individual [state].
 * @param orientation Timeline orientation ([Ux4gJourneyOrientation.Vertical] or [Ux4gJourneyOrientation.Horizontal]).
 * @param indicatorSize Size of the step indicator circle. Default: 20.dp.
 * @param lineWidth Width of the connecting line. Default: 3.dp.
 * @param indicatorCardSpacing Spacing between the indicator and the card. Default: 12.dp.
 * @param stepSpacing Spacing between step cards. Default: 12.dp.
 * @param activeColor Color for completed/current indicators and lines. Defaults to [Ux4gColors.primary].
 * @param inactiveColor Color for upcoming indicators and lines. Defaults to [Ux4gColors.neutral900] with 25% opacity.
 * @param cardBorderRadius Card border radius. Default: 8.dp.
 * @param cardPadding Card inner padding.
 * @param cardColor Global card background color override. Defaults to [Ux4gColors.neutral0].
 * @param cardBorderColor Global card border color override. Defaults to [Ux4gColors.neutral900] with 10% opacity.
 * @param dateStyle Global default date text style override for all steps.
 * @param titleStyle Global default title text style override for all steps.
 */
@Composable
fun Ux4gJourneyTimeline(
    steps: List<Ux4gJourneyStep>,
    modifier: Modifier = Modifier,
    header: Ux4gJourneyHeader? = null,
    currentStep: Int? = null,
    orientation: Ux4gJourneyOrientation = Ux4gJourneyOrientation.Vertical,
    indicatorSize: Dp = 20.dp,
    lineWidth: Dp = 3.dp,
    indicatorCardSpacing: Dp = Ux4gSpace.Space12,
    stepSpacing: Dp = Ux4gSpace.Space12,
    activeColor: Color? = null,
    inactiveColor: Color? = null,
    cardBorderRadius: Dp = Ux4gRadius.Radius8,
    cardPadding: PaddingValues = PaddingValues(
        horizontal = Ux4gSpace.Space16,
        vertical = Ux4gSpace.Space12
    ),
    cardColor: Color? = null,
    cardBorderColor: Color? = null,
    dateStyle: TextStyle? = null,
    titleStyle: TextStyle? = null
) {
    val primary = Ux4gColors.primary
    val onSurface = Ux4gColors.neutral900

    val resolvedActiveColor = activeColor ?: primary
    val resolvedInactiveColor = inactiveColor ?: onSurface.copy(alpha = 0.25f)

    fun resolveState(index: Int): Ux4gJourneyStepState {
        if (currentStep != null) {
            return when {
                index < currentStep -> Ux4gJourneyStepState.Completed
                index == currentStep -> Ux4gJourneyStepState.Current
                else -> Ux4gJourneyStepState.Upcoming
            }
        }
        return steps[index].state
    }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {
        // Header
        if (header != null) {
            JourneyHeaderWidget(header = header)
            Spacer(modifier = Modifier.height(Ux4gSpace.Space16))
        }

        // Orientation
        if (orientation == Ux4gJourneyOrientation.Vertical) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.Top
            ) {
                steps.forEachIndexed { i, step ->
                    val state = resolveState(i)
                    val isFirst = i == 0
                    val isLast = i == steps.size - 1

                    JourneyStepRow(
                        step = step,
                        state = state,
                        isFirst = isFirst,
                        isLast = isLast,
                        indicatorSize = indicatorSize,
                        lineWidth = lineWidth,
                        indicatorCardSpacing = indicatorCardSpacing,
                        stepSpacing = if (!isLast) stepSpacing else 0.dp,
                        activeColor = resolvedActiveColor,
                        inactiveColor = resolvedInactiveColor,
                        cardBorderRadius = cardBorderRadius,
                        cardPadding = cardPadding,
                        cardColor = cardColor,
                        cardBorderColor = cardBorderColor,
                        dateStyle = dateStyle,
                        titleStyle = titleStyle
                    )
                }
            }
        } else {
            val activeIndex = currentStep ?: steps.indexOfFirst { it.state == Ux4gJourneyStepState.Current }.takeIf { it != -1 } ?: 0

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.Start
            ) {
                // Horizontal stepper row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    steps.forEachIndexed { i, step ->
                        StepIndicator(
                            state = resolveState(i),
                            size = indicatorSize,
                            activeColor = resolvedActiveColor,
                            inactiveColor = resolvedInactiveColor,
                            stepNumber = step.stepNumber
                        )

                        if (i < steps.size - 1) {
                            Spacer(modifier = Modifier.width(stepSpacing * 0.5f))
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .height(lineWidth)
                                    .background(
                                        if (resolveState(i) == Ux4gJourneyStepState.Completed) resolvedActiveColor else resolvedInactiveColor
                                    )
                            )
                            Spacer(modifier = Modifier.width(stepSpacing * 0.5f))
                        }
                    }
                }

                // Card below the active step
                if (activeIndex in steps.indices) {
                    Spacer(modifier = Modifier.height(indicatorCardSpacing))
                    JourneyStepCard(
                        step = steps[activeIndex],
                        state = resolveState(activeIndex),
                        borderRadius = cardBorderRadius,
                        padding = cardPadding,
                        cardColor = cardColor,
                        cardBorderColor = cardBorderColor,
                        dateStyle = steps[activeIndex].dateStyle ?: dateStyle,
                        titleStyle = steps[activeIndex].titleStyle ?: titleStyle
                    )
                }
            }
        }
    }
}

// ─── Header Widget ──────────────────────────────────────────────────────────

@Composable
private fun JourneyHeaderWidget(
    header: Ux4gJourneyHeader,
    modifier: Modifier = Modifier
) {
    val onSurface = Ux4gColors.neutral900

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Top
    ) {
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = header.title,
                    style = defaultTypography.tS_strong,
                    color = onSurface
                )
                if (header.icon != null) {
                    Spacer(modifier = Modifier.width(Ux4gSpace.Space8))
                    Box(
                        modifier = Modifier.clickable(
                            enabled = header.onIconPressed != null,
                            onClick = { header.onIconPressed?.invoke() }
                        )
                    ) {
                        Ux4gIconComponent(
                            icon = header.icon,
                            contentDescription = "Header action",
                            modifier = Modifier.size(20.dp),
                            tint = onSurface.copy(alpha = 0.6f)
                        )
                    }
                }
            }
            if (header.description != null) {
                Spacer(modifier = Modifier.height(Ux4gSpace.Space4))
                Text(
                    text = header.description,
                    style = defaultTypography.bS_default,
                    color = onSurface.copy(alpha = 0.6f)
                )
            }
        }
    }
}

// ─── Step Row ───────────────────────────────────────────────────────────────

@Composable
private fun JourneyStepRow(
    step: Ux4gJourneyStep,
    state: Ux4gJourneyStepState,
    isFirst: Boolean,
    isLast: Boolean,
    indicatorSize: Dp,
    lineWidth: Dp,
    indicatorCardSpacing: Dp,
    stepSpacing: Dp,
    activeColor: Color,
    inactiveColor: Color,
    cardBorderRadius: Dp,
    cardPadding: PaddingValues,
    cardColor: Color?,
    cardBorderColor: Color?,
    dateStyle: TextStyle?,
    titleStyle: TextStyle?,
    modifier: Modifier = Modifier
) {
    val topLineColor = if (state == Ux4gJourneyStepState.Completed || state == Ux4gJourneyStepState.Current) {
        activeColor
    } else {
        inactiveColor
    }

    val bottomLineColor = if (state == Ux4gJourneyStepState.Completed) {
        activeColor
    } else {
        inactiveColor
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Indicator Column (top line + circle + bottom line)
        Column(
            modifier = Modifier
                .width(indicatorSize)
                .fillMaxHeight(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Top line (connects to previous step)
            if (!isFirst) {
                Box(
                    modifier = Modifier
                        .width(lineWidth)
                        .weight(1f)
                        .background(topLineColor)
                )
            } else {
                Spacer(modifier = Modifier.weight(1f))
            }

            // Circle indicator (centered)
            StepIndicator(
                state = state,
                size = indicatorSize,
                activeColor = activeColor,
                inactiveColor = inactiveColor,
                stepNumber = step.stepNumber
            )

            // Bottom line (connects to next step)
            if (!isLast) {
                Box(
                    modifier = Modifier
                        .width(lineWidth)
                        .weight(1f)
                        .background(bottomLineColor)
                )
            } else {
                Spacer(modifier = Modifier.weight(1f))
            }
        }

        Spacer(modifier = Modifier.width(indicatorCardSpacing))

        // Card + bottom spacing
        Box(
            modifier = Modifier
                .weight(1f)
                .padding(bottom = stepSpacing)
        ) {
            JourneyStepCard(
                step = step,
                state = state,
                borderRadius = cardBorderRadius,
                padding = cardPadding,
                cardColor = cardColor,
                cardBorderColor = cardBorderColor,
                dateStyle = step.dateStyle ?: dateStyle,
                titleStyle = step.titleStyle ?: titleStyle
            )
        }
    }
}

// ─── Step Indicator ─────────────────────────────────────────────────────────

@Composable
private fun StepIndicator(
    state: Ux4gJourneyStepState,
    size: Dp,
    activeColor: Color,
    inactiveColor: Color,
    stepNumber: String?,
    modifier: Modifier = Modifier
) {
    val surface = Ux4gColors.white

    when (state) {
        Ux4gJourneyStepState.Completed -> {
            Box(
                modifier = modifier
                    .size(size)
                    .clip(CircleShape)
                    .background(activeColor),
                contentAlignment = Alignment.Center
            ) {
                if (stepNumber != null) {
                    Text(
                        text = stepNumber,
                        style = TextStyle(
                            fontSize = (size.value * 0.55f).sp,
                            fontWeight = FontWeight.SemiBold,
                            color = surface
                        )
                    )
                } else {
                    Ux4gIconComponent(
                        icon = Ux4gIcons.Check,
                        contentDescription = "Completed",
                        modifier = Modifier.size(size * 0.6f),
                        tint = surface
                    )
                }
            }
        }

        Ux4gJourneyStepState.Current -> {
            if (stepNumber != null) {
                Box(
                    modifier = modifier
                        .size(size)
                        .clip(CircleShape)
                        .background(activeColor),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stepNumber,
                        style = TextStyle(
                            fontSize = (size.value * 0.55f).sp,
                            fontWeight = FontWeight.SemiBold,
                            color = surface
                        )
                    )
                }
            } else {
                Box(
                    modifier = modifier
                        .size(size)
                        .clip(CircleShape)
                        .background(surface)
                        .border(BorderStroke(2.5.dp, activeColor), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size(size * 0.45f)
                            .clip(CircleShape)
                            .background(activeColor)
                    )
                }
            }
        }

        Ux4gJourneyStepState.Upcoming -> {
            if (stepNumber != null) {
                Box(
                    modifier = modifier
                        .size(size)
                        .clip(CircleShape)
                        .background(activeColor),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stepNumber,
                        style = TextStyle(
                            fontSize = (size.value * 0.55f).sp,
                            fontWeight = FontWeight.SemiBold,
                            color = surface
                        )
                    )
                }
            } else {
                Box(
                    modifier = modifier
                        .size(size)
                        .clip(CircleShape)
                        .background(surface)
                        .border(BorderStroke(2.5.dp, inactiveColor), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size(size * 0.45f)
                            .clip(CircleShape)
                            .background(inactiveColor)
                    )
                }
            }
        }
    }
}

// ─── Step Card ──────────────────────────────────────────────────────────────

@Composable
private fun JourneyStepCard(
    step: Ux4gJourneyStep,
    state: Ux4gJourneyStepState,
    borderRadius: Dp,
    padding: PaddingValues,
    cardColor: Color?,
    cardBorderColor: Color?,
    dateStyle: TextStyle?,
    titleStyle: TextStyle?,
    modifier: Modifier = Modifier
) {
    val isUpcoming = state == Ux4gJourneyStepState.Upcoming
    val textOpacity = if (isUpcoming) 0.5f else 1.0f

    val onSurface = Ux4gColors.neutral900
    val primary = Ux4gColors.primary
    val warning = Ux4gColors.secondary
    val surface = Ux4gColors.neutral0

    val background = step.cardColor ?: cardColor ?: surface
    val border = step.cardBorderColor ?: cardBorderColor ?: onSurface.copy(alpha = 0.1f)
    val shape = RoundedCornerShape(borderRadius)

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(shape)
            .background(background)
            .border(BorderStroke(1.dp, border), shape)
            .padding(padding)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.Start
        ) {
            // Date + Tag / Status badge row
            val hasTopRightBadge = step.status != null &&
                step.status.badgeText != null &&
                step.status.badgePosition == Ux4gJourneyBadgePosition.TopRight

            if (step.date != null || step.tag != null || hasTopRightBadge) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (step.date != null) {
                        Text(
                            text = step.date,
                            style = (step.dateStyle ?: dateStyle ?: defaultTypography.bXS_default).copy(
                                fontSize = 12.sp
                            ),
                            color = step.dateColor ?: onSurface.copy(
                                alpha = if (isUpcoming) 0.4f else 0.6f
                            )
                        )
                    } else {
                        Spacer(modifier = Modifier.width(1.dp))
                    }

                    if (hasTopRightBadge) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(Ux4gRadius.Radius4))
                                .background(step.status!!.badgeColor ?: onSurface.copy(alpha = 0.06f))
                                .padding(horizontal = Ux4gSpace.Space8, vertical = Ux4gSpace.Space2),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = step.status.badgeText!!,
                                style = defaultTypography.bXS_default.copy(
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.SemiBold
                                ),
                                color = step.status.badgeTextColor ?: onSurface.copy(alpha = 0.6f)
                            )
                        }
                    } else if (step.tag != null) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(Ux4gRadius.Radius4))
                                .background(onSurface.copy(alpha = 0.06f))
                                .padding(horizontal = Ux4gSpace.Space8, vertical = Ux4gSpace.Space2),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = step.tag,
                                style = defaultTypography.bXS_default.copy(
                                    fontSize = 11.sp
                                ),
                                color = onSurface.copy(
                                    alpha = if (isUpcoming) 0.4f else 0.6f
                                )
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(Ux4gSpace.Space6))
            }

            // Title
            Text(
                text = step.title,
                style = (step.titleStyle ?: titleStyle ?: defaultTypography.tS_strong).copy(
                    fontSize = 14.sp
                ),
                color = step.titleColor ?: onSurface.copy(alpha = textOpacity)
            )

            // Helping text
            if (step.helpingText != null) {
                Spacer(modifier = Modifier.height(Ux4gSpace.Space4))
                Text(
                    text = step.helpingText,
                    style = defaultTypography.bXS_default.copy(
                        fontSize = 12.sp
                    ),
                    color = onSurface.copy(alpha = 0.5f)
                )
            }

            // Icon
            if (step.icon != null) {
                Spacer(modifier = Modifier.height(Ux4gSpace.Space6))
                Ux4gIconComponent(
                    icon = step.icon,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp),
                    tint = step.iconColor ?: primary.copy(alpha = if (isUpcoming) 0.4f else 1.0f)
                )
            }

            // Status indicator at bottom (unified pill tag)
            if (step.status != null && step.status.badgePosition == Ux4gJourneyBadgePosition.Bottom) {
                Spacer(modifier = Modifier.height(Ux4gSpace.Space8))
                val segments = buildList {
                    add(
                        Ux4gPillSegment(
                            text = step.status.text,
                            leading = {
                                Box(
                                    modifier = Modifier
                                        .size(8.dp)
                                        .clip(CircleShape)
                                        .background(step.status.dotColor ?: warning)
                                )
                            }
                        )
                    )
                    if (step.status.badgeText != null) {
                        add(
                            Ux4gPillSegment(
                                text = step.status.badgeText,
                                bold = true,
                                textColor = step.status.badgeTextColor ?: warning
                            )
                        )
                    }
                }
                Ux4gUnifiedPillTag(
                    segments = segments,
                    size = Ux4gTagSize.L,
                    customBorderRadius = Ux4gRadius.Radius8
                )
            }

            // Custom content
            if (step.customContent != null) {
                Spacer(modifier = Modifier.height(Ux4gSpace.Space8))
                step.customContent.invoke()
            }
        }
    }
}

// ─── Previews ───────────────────────────────────────────────────────────────

@Preview(name = "Vertical Light", showBackground = true)
@Preview(name = "Vertical Dark", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun Ux4gJourneyTimelineVerticalPreview() {
    Ux4gTheme {
        Box(modifier = Modifier.padding(16.dp)) {
            Ux4gJourneyTimeline(
                header = Ux4gJourneyHeader(
                    title = "Application Progress",
                    description = "Track the status of your request in real time",
                    icon = Ux4gIcons.info
                ),
                steps = listOf(
                    Ux4gJourneyStep(
                        state = Ux4gJourneyStepState.Completed,
                        date = "12 Aug, 10:30 AM",
                        tag = "Verified",
                        title = "Application Submitted",
                        helpingText = "All required documents were uploaded successfully."
                    ),
                    Ux4gJourneyStep(
                        state = Ux4gJourneyStepState.Completed,
                        date = "13 Aug, 02:15 PM",
                        tag = "Approved",
                        title = "Identity Verification",
                        helpingText = "Aadhaar and PAN details verified."
                    ),
                    Ux4gJourneyStep(
                        state = Ux4gJourneyStepState.Current,
                        date = "15 Aug, 11:00 AM",
                        title = "Under Review by Officer",
                        helpingText = "Your application is currently being evaluated.",
                        status = Ux4gJourneyStepStatus(
                            text = "2 days remaining",
                            badgeText = "In Progress",
                            badgePosition = Ux4gJourneyBadgePosition.Bottom
                        )
                    ),
                    Ux4gJourneyStep(
                        state = Ux4gJourneyStepState.Upcoming,
                        title = "Final Certificate Issuance",
                        helpingText = "Digital certificate will be generated upon approval.",
                        status = Ux4gJourneyStepStatus(
                            text = "Pending",
                            badgeText = "Queued",
                            badgePosition = Ux4gJourneyBadgePosition.TopRight
                        )
                    )
                )
            )
        }
    }
}

@Preview(name = "Horizontal Preview", showBackground = true)
@Composable
private fun Ux4gJourneyTimelineHorizontalPreview() {
    Ux4gTheme {
        Box(modifier = Modifier.padding(16.dp)) {
            Ux4gJourneyTimeline(
                orientation = Ux4gJourneyOrientation.Horizontal,
                currentStep = 1,
                steps = listOf(
                    Ux4gJourneyStep(
                        title = "Step 1: Details",
                        date = "Completed"
                    ),
                    Ux4gJourneyStep(
                        title = "Step 2: Verification",
                        date = "Current Step",
                        helpingText = "Please verify your email and phone.",
                        status = Ux4gJourneyStepStatus(
                            text = "Action Needed",
                            badgeText = "Active"
                        )
                    ),
                    Ux4gJourneyStep(
                        title = "Step 3: Confirmation",
                        date = "Upcoming"
                    )
                )
            )
        }
    }
}
