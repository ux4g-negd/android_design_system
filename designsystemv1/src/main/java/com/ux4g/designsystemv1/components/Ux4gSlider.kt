package com.ux4g.designsystemv1.components

import android.content.res.Configuration
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.InteractionSource
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsDraggedAsState
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.ux4g.designsystemv1.components.inputfield.Ux4gInputField
import com.ux4g.designsystemv1.components.inputfield.Ux4gInputFieldSize
import com.ux4g.designsystemv1.components.tooltip.Ux4gTooltipBubble
import com.ux4g.designsystemv1.components.tooltip.Ux4gTooltipPlacement
import com.ux4g.designsystemv1.foundation.*
import com.ux4g.designsystemv1.theme.Ux4gTheme
import kotlin.math.roundToInt

enum class Ux4gSliderSize(val thumbSize: Dp, val trackHeight: Dp) {
    Small(16.dp, 4.dp),
    Medium(20.dp, 6.dp)
}

enum class Ux4gSliderCaptionVariant {
    Helper,
    Error,
    Warning,
    Success
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Ux4gSlider(
        value: Float,
        onValueChange: (Float) -> Unit,
        modifier: Modifier = Modifier,
        valueRange: ClosedFloatingPointRange<Float> = 0f..100f,
        steps: Int = 0, // e.g., 9 for steps of 10 in 0-100
        size: Ux4gSliderSize = Ux4gSliderSize.Small,
        enabled: Boolean = true,
        label: String? = null,
        isRequired: Boolean = false,
        labelIcon: Ux4gIcon? = null,
        startValueText: String? = null,
        endValueText: String? = null,
        caption: String? = null,
        captionVariant: Ux4gSliderCaptionVariant = Ux4gSliderCaptionVariant.Helper,
        showMarksAndValues: Boolean = false, // If true, renders ticks and numbers below
        showInputFields: Boolean =
                false, // If true, replaces start/end value texts with editable inputs
        showValueLabels: Boolean = false, // If true, defaults to showing plain text values
        showIndicator: Boolean = false, // If true, shows value above thumb while sliding
        valueFormatter: (Float) -> String = { "${it.toInt()}%" },
        interactionSource: MutableInteractionSource = remember { MutableInteractionSource() }
) {
    Column(modifier = modifier.fillMaxWidth()) {
        // Label Block
        if (label != null ||
                        startValueText != null ||
                        endValueText != null ||
                        showInputFields ||
                        showValueLabels
        ) {
            Column(modifier = Modifier.fillMaxWidth().padding(horizontal = size.thumbSize / 2)) {
                if (label != null) {
                    Row(
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                            verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                                text = label,
                                style = Ux4gTheme.typography.lM_default,
                                color =
                                        if (enabled) Ux4gTheme.colorScheme.onSurface
                                        else Ux4gTheme.colorScheme.onSurface.copy(alpha = 0.38f)
                        )
                        if (isRequired) {
                            Text(
                                    text = "*",
                                    style = Ux4gTheme.typography.lM_default,
                                    color = Ux4gTheme.colorScheme.error
                            )
                        }
                        if (labelIcon != null) {
                            Ux4gIconComponent(
                                    icon = labelIcon,
                                    contentDescription = null,
                                    modifier = Modifier.size(16.dp),
                                    tint =
                                            if (enabled) Ux4gTheme.colorScheme.onSurface
                                            else Ux4gTheme.colorScheme.onSurface.copy(alpha = 0.38f)
                            )
                        }
                    }
                }

                if (startValueText != null ||
                                endValueText != null ||
                                showInputFields ||
                                showValueLabels
                ) {
                    Row(
                            modifier = Modifier.fillMaxWidth().padding(top = 4.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (showInputFields) {
                            var textValue by
                                    remember(value) { mutableStateOf(value.toInt().toString()) }
                            val focusManager = LocalFocusManager.current
                            Ux4gInputField(
                                    value = textValue,
                                    onValueChange = { newText ->
                                        if (newText.all { it.isDigit() } || newText.isEmpty()) {
                                            textValue = newText
                                        }
                                    },
                                    modifier = Modifier.width(60.dp),
                                    size = Ux4gInputFieldSize.Small,
                                    postfixText = "%",
                                    enabled = enabled,
                                    singleLine = true,
                                    keyboardOptions =
                                            KeyboardOptions(
                                                    keyboardType = KeyboardType.Number,
                                                    imeAction = ImeAction.Done
                                            ),
                                    keyboardActions =
                                            KeyboardActions(
                                                    onDone = {
                                                        val parsed = textValue.toFloatOrNull()
                                                        if (parsed != null) {
                                                            val coerced =
                                                                    parsed.coerceIn(valueRange)
                                                            textValue = coerced.toInt().toString()
                                                            onValueChange(coerced)
                                                        } else {
                                                            textValue = value.toInt().toString()
                                                        }
                                                        focusManager.clearFocus()
                                                    }
                                            )
                            )
                        } else if (showValueLabels) {
                            Text(
                                    text = valueFormatter(value),
                                    style = Ux4gTheme.typography.lM_strong,
                                    color =
                                            if (enabled) Ux4gTheme.colorScheme.onSurface
                                            else Ux4gTheme.colorScheme.onSurface.copy(alpha = 0.38f)
                            )
                        } else {
                            if (startValueText != null) {
                                Text(
                                        text = startValueText,
                                        style = Ux4gTheme.typography.lM_strong,
                                        color =
                                                if (enabled) Ux4gTheme.colorScheme.onSurface
                                                else
                                                        Ux4gTheme.colorScheme.onSurface.copy(
                                                                alpha = 0.38f
                                                        )
                                )
                            } else {
                                Spacer(Modifier.width(1.dp))
                            }
                        }

                        if (endValueText != null) {
                            Text(
                                    text = endValueText,
                                    style = Ux4gTheme.typography.lM_strong,
                                    color =
                                            if (enabled) Ux4gTheme.colorScheme.onSurface
                                            else Ux4gTheme.colorScheme.onSurface.copy(alpha = 0.38f)
                            )
                        }
                    }
                }
            }
        }

        Slider(
                value = value,
                onValueChange = onValueChange,
                modifier = Modifier.fillMaxWidth().offset(y = (-12).dp),
                valueRange = valueRange,
                steps = steps,
                enabled = enabled,
                interactionSource = interactionSource,
                thumb = {
                    Ux4gThumb(
                            size = size,
                            enabled = enabled,
                            interactionSource = interactionSource,
                            value = value,
                            showIndicator = showIndicator
                    )
                },
                track = { sliderState ->
                    val activeColor = if (enabled) Ux4gTheme.colorScheme.primary else Ux4gTheme.colorScheme.onSurface.copy(alpha = 0.38f)
                    val inactiveColor = Ux4gTheme.colorScheme.onSurface.copy(alpha = 0.12f)
                    Canvas(Modifier.fillMaxWidth().height(size.trackHeight)) {
                        val trackHeightPx = size.trackHeight.toPx()
                        val widthOffset = this.size.width
                        // Inactive
                        drawLine(
                                color = inactiveColor,
                                start = Offset(0f, center.y),
                                end = Offset(widthOffset, center.y),
                                strokeWidth = trackHeightPx,
                                cap = StrokeCap.Round
                        )
                        // Active
                        val fraction = (sliderState.value - sliderState.valueRange.start) / (sliderState.valueRange.endInclusive - sliderState.valueRange.start)
                        drawLine(
                                color = activeColor,
                                start = Offset(0f, center.y),
                                end = Offset(widthOffset * fraction, center.y),
                                strokeWidth = trackHeightPx,
                                cap = StrokeCap.Round
                        )
                    }
                }
        )

        if (showMarksAndValues) {
            Ux4gSliderMarksAndValues(
                    valueRange = valueRange,
                    activeRange = valueRange.start..value,
                    steps = steps,
                    enabled = enabled,
                    thumbRadius = size.thumbSize / 2
            )
        }

        if (caption != null) {
            Ux4gSliderCaption(
                    caption = caption,
                    variant = captionVariant,
                    enabled = enabled,
                    thumbRadius = size.thumbSize / 2
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Ux4gRangeSlider(
        value: ClosedFloatingPointRange<Float>,
        onValueChange: (ClosedFloatingPointRange<Float>) -> Unit,
        modifier: Modifier = Modifier,
        valueRange: ClosedFloatingPointRange<Float> = 0f..100f,
        steps: Int = 0,
        size: Ux4gSliderSize = Ux4gSliderSize.Small,
        enabled: Boolean = true,
        label: String? = null,
        isRequired: Boolean = false,
        labelIcon: Ux4gIcon? = null,
        startValueText: String? = null,
        endValueText: String? = null,
        caption: String? = null,
        captionVariant: Ux4gSliderCaptionVariant = Ux4gSliderCaptionVariant.Helper,
        showMarksAndValues: Boolean = false,
        showInputFields: Boolean = false,
        showValueLabels: Boolean = false,
        showIndicator: Boolean = false,
        valueFormatter: (Float) -> String = { "${it.toInt()}%" }
) {
    Column(modifier = modifier.fillMaxWidth()) {
        if (label != null ||
                        startValueText != null ||
                        endValueText != null ||
                        showInputFields ||
                        showValueLabels
        ) {
            Column(modifier = Modifier.fillMaxWidth().padding(horizontal = size.thumbSize / 2)) {
                if (label != null) {
                    Row(
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                            verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                                text = label,
                                style = Ux4gTheme.typography.lM_default,
                                color =
                                        if (enabled) Ux4gTheme.colorScheme.onSurface
                                        else Ux4gTheme.colorScheme.onSurface.copy(alpha = 0.38f)
                        )
                        if (isRequired) {
                            Text(
                                    text = "*",
                                    style = Ux4gTheme.typography.lM_default,
                                    color = Ux4gTheme.colorScheme.error
                            )
                        }
                        if (labelIcon != null) {
                            Ux4gIconComponent(
                                    icon = labelIcon,
                                    contentDescription = null,
                                    modifier = Modifier.size(16.dp),
                                    tint =
                                            if (enabled) Ux4gTheme.colorScheme.onSurface
                                            else Ux4gTheme.colorScheme.onSurface.copy(alpha = 0.38f)
                            )
                        }
                    }
                }

                if (startValueText != null ||
                                endValueText != null ||
                                showInputFields ||
                                showValueLabels
                ) {
                    Row(
                            modifier = Modifier.fillMaxWidth().padding(top = 4.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (showInputFields) {
                            var textStart by
                                    remember(value.start) {
                                        mutableStateOf(value.start.toInt().toString())
                                    }
                            var textEnd by
                                    remember(value.endInclusive) {
                                        mutableStateOf(value.endInclusive.toInt().toString())
                                    }
                            val focusManager = LocalFocusManager.current
                            Ux4gInputField(
                                    value = textStart,
                                    onValueChange = { newText ->
                                        if (newText.all { it.isDigit() } || newText.isEmpty())
                                                textStart = newText
                                    },
                                    modifier = Modifier.width(60.dp),
                                    size = Ux4gInputFieldSize.Small,
                                    postfixText = "%",
                                    enabled = enabled,
                                    singleLine = true,
                                    keyboardOptions =
                                            KeyboardOptions(
                                                    keyboardType = KeyboardType.Number,
                                                    imeAction = ImeAction.Done
                                            ),
                                    keyboardActions =
                                            KeyboardActions(
                                                    onDone = {
                                                        val parsed = textStart.toFloatOrNull()
                                                        if (parsed != null) {
                                                            val coerced =
                                                                    parsed.coerceIn(valueRange)
                                                            textStart = coerced.toInt().toString()
                                                            if (coerced <= value.endInclusive) {
                                                                onValueChange(
                                                                        coerced..value.endInclusive
                                                                )
                                                            } else {
                                                                onValueChange(
                                                                        value.endInclusive..value.endInclusive
                                                                )
                                                            }
                                                        } else {
                                                            textStart =
                                                                    value.start.toInt().toString()
                                                        }
                                                        focusManager.clearFocus()
                                                    }
                                            )
                            )
                            Ux4gInputField(
                                    value = textEnd,
                                    onValueChange = { newText ->
                                        if (newText.all { it.isDigit() } || newText.isEmpty())
                                                textEnd = newText
                                    },
                                    modifier = Modifier.width(60.dp),
                                    size = Ux4gInputFieldSize.Small,
                                    postfixText = "%",
                                    enabled = enabled,
                                    singleLine = true,
                                    keyboardOptions =
                                            KeyboardOptions(
                                                    keyboardType = KeyboardType.Number,
                                                    imeAction = ImeAction.Done
                                            ),
                                    keyboardActions =
                                            KeyboardActions(
                                                    onDone = {
                                                        val parsed = textEnd.toFloatOrNull()
                                                        if (parsed != null) {
                                                            val coerced =
                                                                    parsed.coerceIn(valueRange)
                                                            textEnd = coerced.toInt().toString()
                                                            if (coerced >= value.start) {
                                                                onValueChange(value.start..coerced)
                                                            } else {
                                                                onValueChange(
                                                                        value.start..value.start
                                                                )
                                                            }
                                                        } else {
                                                            textEnd =
                                                                    value.endInclusive
                                                                            .toInt()
                                                                            .toString()
                                                        }
                                                        focusManager.clearFocus()
                                                    }
                                            )
                            )
                        } else if (showValueLabels) {
                            Text(
                                    text = valueFormatter(value.start),
                                    style = Ux4gTheme.typography.lM_strong,
                                    color =
                                            if (enabled) Ux4gTheme.colorScheme.onSurface
                                            else Ux4gTheme.colorScheme.onSurface.copy(alpha = 0.38f)
                            )
                            Text(
                                    text = valueFormatter(value.endInclusive),
                                    style = Ux4gTheme.typography.lM_strong,
                                    color =
                                            if (enabled) Ux4gTheme.colorScheme.onSurface
                                            else Ux4gTheme.colorScheme.onSurface.copy(alpha = 0.38f)
                            )
                        } else {
                            if (startValueText != null) {
                                Text(
                                        text = startValueText,
                                        style = Ux4gTheme.typography.lM_strong,
                                        color =
                                                if (enabled) Ux4gTheme.colorScheme.onSurface
                                                else
                                                        Ux4gTheme.colorScheme.onSurface.copy(
                                                                alpha = 0.38f
                                                        )
                                )
                            } else {
                                Spacer(Modifier.width(1.dp))
                            }
                            if (endValueText != null) {
                                Text(
                                        text = endValueText,
                                        style = Ux4gTheme.typography.lM_strong,
                                        color =
                                                if (enabled) Ux4gTheme.colorScheme.onSurface
                                                else
                                                        Ux4gTheme.colorScheme.onSurface.copy(
                                                                alpha = 0.38f
                                                        )
                                )
                            }
                        }
                    }
                }
            }
        }

        val startInteractionSource = remember { MutableInteractionSource() }
        val endInteractionSource = remember { MutableInteractionSource() }

        RangeSlider(
                value = value,
                onValueChange = onValueChange,
                modifier = Modifier.fillMaxWidth().offset(y = (-12).dp),
                valueRange = valueRange,
                steps = steps,
                enabled = enabled,
                startInteractionSource = startInteractionSource,
                endInteractionSource = endInteractionSource,
                startThumb = { _ ->
                    Ux4gThumb(
                            size = size,
                            enabled = enabled,
                            interactionSource = startInteractionSource,
                            value = value.start,
                            showIndicator = showIndicator
                    )
                },
                endThumb = { _ ->
                    Ux4gThumb(
                            size = size,
                            enabled = enabled,
                            interactionSource = endInteractionSource,
                            value = value.endInclusive,
                            showIndicator = showIndicator
                    )
                },
                track = { sliderState ->
                    val activeColor = if (enabled) Ux4gTheme.colorScheme.primary else Ux4gTheme.colorScheme.onSurface.copy(alpha = 0.38f)
                    val inactiveColor = Ux4gTheme.colorScheme.onSurface.copy(alpha = 0.12f)
                    Canvas(Modifier.fillMaxWidth().height(size.trackHeight)) {
                        val trackHeightPx = size.trackHeight.toPx()
                        val widthOffset = this.size.width
                        // Inactive
                        drawLine(
                                color = inactiveColor,
                                start = Offset(0f, center.y),
                                end = Offset(widthOffset, center.y),
                                strokeWidth = trackHeightPx,
                                cap = StrokeCap.Round
                        )
                        // Active
                        val rangeStart = (sliderState.activeRangeStart - sliderState.valueRange.start) / (sliderState.valueRange.endInclusive - sliderState.valueRange.start)
                        val rangeEnd = (sliderState.activeRangeEnd - sliderState.valueRange.start) / (sliderState.valueRange.endInclusive - sliderState.valueRange.start)
                        drawLine(
                                color = activeColor,
                                start = Offset(widthOffset * rangeStart, center.y),
                                end = Offset(widthOffset * rangeEnd, center.y),
                                strokeWidth = trackHeightPx,
                                cap = StrokeCap.Round
                        )
                    }
                }
        )

        if (showMarksAndValues) {
            Ux4gSliderMarksAndValues(
                    valueRange = valueRange,
                    activeRange = value,
                    steps = steps,
                    enabled = enabled,
                    thumbRadius = size.thumbSize / 2
            )
        }

        if (caption != null) {
            Ux4gSliderCaption(
                    caption = caption,
                    variant = captionVariant,
                    enabled = enabled,
                    thumbRadius = size.thumbSize / 2
            )
        }
    }
}

@Composable
private fun Ux4gThumb(
        size: Ux4gSliderSize,
        enabled: Boolean,
        interactionSource: InteractionSource = remember { MutableInteractionSource() },
        value: Float = 0f,
        showIndicator: Boolean = false
) {
    val isDragged by interactionSource.collectIsDraggedAsState()
    val isPressed by interactionSource.collectIsPressedAsState()
    val showNow = showIndicator && (isDragged || isPressed)

    Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.wrapContentSize(unbounded = true)
    ) {
        if (showNow) {
            Ux4gTooltipBubble(
                    text = value.toInt().toString(),
                    placement = Ux4gTooltipPlacement.Top,
                    modifier = Modifier.offset(y = (-36).dp)
            )
        }
        Box(
                modifier =
                        Modifier.size(size.thumbSize)
                                .shadow(
                                        if (enabled) 2.dp else 0.dp,
                                        RoundedCornerShape(percent = 50)
                                )
                                .background(
                                        Ux4gTheme.colorScheme.onPrimary,
                                        RoundedCornerShape(percent = 50)
                                )
                                // The border matches the shadow somewhat or we can place a faint
                                // border
                                .border(
                                        width = 0.5.dp,
                                        color =
                                                Ux4gTheme.colorScheme.onSurface.copy(
                                                        alpha = if (enabled) 0.12f else 0.38f
                                                ),
                                        shape = RoundedCornerShape(percent = 50)
                                )
        )
    }
}

@Composable
private fun sliderDefaultsColors() =
        SliderDefaults.colors(
                thumbColor = Ux4gTheme.colorScheme.onPrimary,
                activeTrackColor = Ux4gTheme.colorScheme.primary,
                inactiveTrackColor = Ux4gTheme.colorScheme.onSurface.copy(alpha = 0.12f),
                activeTickColor = Color.Transparent, // Ticks are rendered below
                inactiveTickColor = Color.Transparent,
                disabledThumbColor = Ux4gTheme.colorScheme.onPrimary,
                disabledActiveTrackColor = Ux4gTheme.colorScheme.onSurface.copy(alpha = 0.38f),
                disabledInactiveTrackColor = Ux4gTheme.colorScheme.onSurface.copy(alpha = 0.12f),
        )

@Composable
private fun Ux4gSliderMarksAndValues(
        valueRange: ClosedFloatingPointRange<Float>,
        activeRange: ClosedFloatingPointRange<Float>,
        steps: Int,
        enabled: Boolean,
        thumbRadius: Dp
) {
    if (steps < 0) return
    val totalTicks = steps + 2 // Number of major marks
    val stepValue = (valueRange.endInclusive - valueRange.start) / (totalTicks - 1)

    Column(
            modifier =
                    Modifier.fillMaxWidth().padding(horizontal = thumbRadius).offset(y = (-24).dp)
    ) {
        // Ticks
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            for (i in 0 until totalTicks) {
                val currentValue = valueRange.start + (stepValue * i)
                val isActive = currentValue in activeRange
                Box(
                        modifier =
                                Modifier.width(1.dp)
                                        .height(4.dp)
                                        .background(
                                                if (!enabled)
                                                        Ux4gTheme.colorScheme.onSurface.copy(
                                                                alpha = 0.12f
                                                        )
                                                else if (isActive) Ux4gTheme.colorScheme.primary
                                                else
                                                        Ux4gTheme.colorScheme.onSurface.copy(
                                                                alpha = 0.38f
                                                        )
                                        )
                )
            }
        }

        Spacer(modifier = Modifier.height(4.dp))

        // Values
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            for (i in 0 until totalTicks) {
                val currentValue = valueRange.start + (stepValue * i)
                val isActive = currentValue in activeRange
                val displayValue =
                        if (currentValue % 1f == 0f) {
                            currentValue.toInt().toString()
                        } else {
                            currentValue.toString()
                        }

                Text(
                        text = displayValue,
                        style = Ux4gTheme.typography.lM_default,
                        color =
                                if (!enabled) Ux4gTheme.colorScheme.onSurface.copy(alpha = 0.38f)
                                else if (isActive) Ux4gTheme.colorScheme.primary
                                else Ux4gTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                        modifier = Modifier.weight(1f, fill = true),
                        textAlign =
                                if (i == 0) TextAlign.Start
                                else if (i == totalTicks - 1) TextAlign.End else TextAlign.Center
                )
            }
        }
    }
}

@Composable
private fun Ux4gSliderCaption(
        caption: String,
        variant: Ux4gSliderCaptionVariant,
        enabled: Boolean,
        thumbRadius: Dp
) {
    val contentColor =
            when (variant) {
                Ux4gSliderCaptionVariant.Helper -> Ux4gTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                Ux4gSliderCaptionVariant.Error -> Ux4gTheme.colorScheme.error
                Ux4gSliderCaptionVariant.Warning -> Ux4gPalette.orange600
                Ux4gSliderCaptionVariant.Success -> Ux4gPalette.green600
            }

    val icon =
            when (variant) {
                Ux4gSliderCaptionVariant.Helper -> null
                Ux4gSliderCaptionVariant.Error -> Ux4gIcons.error
                Ux4gSliderCaptionVariant.Warning -> Ux4gIcons.warning
                Ux4gSliderCaptionVariant.Success -> Ux4gIcons.success_circle
            }

    Row(
            modifier = Modifier.fillMaxWidth().offset(y = (-24).dp).padding(start = thumbRadius),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment = Alignment.CenterVertically
    ) {
        if (icon != null) {
            Ux4gIconComponent(
                    icon = icon,
                    contentDescription = null,
                    tint = if (enabled) contentColor else contentColor.copy(alpha = 0.38f),
                    modifier = Modifier.size(14.dp)
            )
        }
        Text(
                text = caption,
                style = Ux4gTheme.typography.lS_default,
                color = if (enabled) contentColor else contentColor.copy(alpha = 0.38f)
        )
    }
}

@Preview(name = "Light", showBackground = true)
@Preview(name = "Dark", uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
private fun Ux4gSliderPreview() {
    Ux4gTheme {
        Surface(color = Ux4gTheme.colorScheme.background) {
            Column(
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                var value1 by remember { mutableFloatStateOf(50f) }
                Ux4gSlider(
                        value = value1,
                        onValueChange = { value1 = it },
                        label = "Single Slider",
                        isRequired = true,
                        labelIcon = Ux4gIcons.info_circle,
                        startValueText = "${value1.roundToInt()}%",
                        size = Ux4gSliderSize.Small,
                        showMarksAndValues = true,
                        steps = 9 // 0 to 100 with steps every 10
                )

                var value2 by remember { mutableStateOf(20f..70f) }
                Ux4gRangeSlider(
                        value = value2,
                        onValueChange = { value2 = it },
                        label = "Range Slider",
                        isRequired = true,
                        labelIcon = Ux4gIcons.info_circle,
                        startValueText = "${value2.start.roundToInt()}%",
                        endValueText = "${value2.endInclusive.roundToInt()}%",
                        size = Ux4gSliderSize.Small,
                        showMarksAndValues = true,
                        steps = 9,
                        caption = "Error message",
                        captionVariant = Ux4gSliderCaptionVariant.Error
                )

                var value3 by remember { mutableStateOf(20f..70f) }
                Ux4gRangeSlider(
                        value = value3,
                        onValueChange = { value3 = it },
                        label = "Medium Range",
                        isRequired = true,
                        labelIcon = Ux4gIcons.info_circle,
                        startValueText = "${value3.start.roundToInt()}%",
                        endValueText = "${value3.endInclusive.roundToInt()}%",
                        size = Ux4gSliderSize.Medium,
                        showMarksAndValues = true,
                        steps = 9,
                        enabled = true,
                        caption = "Helper text",
                        captionVariant = Ux4gSliderCaptionVariant.Helper
                )

                var value4 by remember { mutableFloatStateOf(30f) }
                Ux4gSlider(
                        value = value4,
                        onValueChange = { value4 = it },
                        label = "Slider with Indicator and Input",
                        showIndicator = true,
                        showInputFields = true,
                        showMarksAndValues = true,
                        steps = 9
                )
            }
        }
    }
}
