package com.ux4g.designsystemv1.components

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.ux4g.designsystemv1.components.buttons.Ux4gButton
import com.ux4g.designsystemv1.components.buttons.Ux4gButtonVariant
import com.ux4g.designsystemv1.foundation.Ux4gColors
import com.ux4g.designsystemv1.foundation.Ux4gRadius
import com.ux4g.designsystemv1.foundation.defaultTypography
import kotlinx.coroutines.launch
import java.time.LocalTime
import java.util.Calendar
import kotlin.math.abs

/**
 * Data class representing a time of day with 12-hour AM/PM format support.
 */
data class Ux4gTimeOfDay(
    val hour: Int, // 0..23 (24-hour format)
    val minute: Int // 0..59
) {
    init {
        require(hour in 0..23) { "Hour must be between 0 and 23" }
        require(minute in 0..59) { "Minute must be between 0 and 59" }
    }

    val hourOfPeriod: Int
        get() {
            val h = hour % 12
            return if (h == 0) 12 else h
        }

    val isAm: Boolean
        get() = hour < 12

    fun format(placeholder: String = "Select time"): String {
        val hStr = hourOfPeriod.toString().padStart(2, '0')
        val mStr = minute.toString().padStart(2, '0')
        val period = if (isAm) "AM" else "PM"
        return "$hStr:$mStr $period"
    }

    fun toLocalTime(): LocalTime = LocalTime.of(hour, minute)

    companion object {
        fun now(): Ux4gTimeOfDay {
            val cal = Calendar.getInstance()
            return Ux4gTimeOfDay(
                hour = cal.get(Calendar.HOUR_OF_DAY),
                minute = cal.get(Calendar.MINUTE)
            )
        }

        fun fromLocalTime(localTime: LocalTime): Ux4gTimeOfDay {
            return Ux4gTimeOfDay(localTime.hour, localTime.minute)
        }

        fun of(hour: Int, minute: Int, isAm: Boolean): Ux4gTimeOfDay {
            var h = hour % 12
            if (!isAm) h += 12
            return Ux4gTimeOfDay(hour = h, minute = minute)
        }
    }
}

/**
 * Colors configuration for [Ux4gTimePicker] and [Ux4gTimePickerDialog].
 * Defaults directly match the UX4G Flutter TimePicker specifications.
 */
@Immutable
data class Ux4gTimePickerColors(
    val primary: Color,
    val onSurface: Color,
    val surface: Color,
    val error: Color,
    val borderColor: Color,
    val selectedBorderColor: Color,
    val placeholderColor: Color,
    val textColor: Color,
    val iconColor: Color,
    val selectedIconColor: Color,
    val dividerColor: Color,
    val headerTextColor: Color,
    val wheelItemSelectedBackground: Color,
    val wheelItemSelectedTextColor: Color,
    val wheelItemUnselectedTextColor: Color,
    val amPmSelectedBackground: Color,
    val amPmSelectedTextColor: Color,
    val amPmUnselectedTextColor: Color
)

object Ux4gTimePickerDefaults {
    @Composable
    fun colors(
        primary: Color = Ux4gColors.primary,
        onSurface: Color = Ux4gColors.neutral900,
        surface: Color = Ux4gColors.white,
        error: Color = Ux4gColors.red600,
        borderColor: Color = Ux4gColors.neutral900.copy(alpha = 0.12f),
        selectedBorderColor: Color = Ux4gColors.primary,
        placeholderColor: Color = Ux4gColors.neutral900.copy(alpha = 0.38f),
        textColor: Color = Ux4gColors.neutral900,
        iconColor: Color = Ux4gColors.neutral900.copy(alpha = 0.38f),
        selectedIconColor: Color = Ux4gColors.primary,
        dividerColor: Color = Ux4gColors.neutral900.copy(alpha = 0.12f),
        headerTextColor: Color = Ux4gColors.neutral900.copy(alpha = 0.38f),
        wheelItemSelectedBackground: Color = Ux4gColors.primary.copy(alpha = 0.08f),
        wheelItemSelectedTextColor: Color = Ux4gColors.primary,
        wheelItemUnselectedTextColor: Color = Ux4gColors.neutral900,
        amPmSelectedBackground: Color = Ux4gColors.primary.copy(alpha = 0.15f),
        amPmSelectedTextColor: Color = Ux4gColors.primary,
        amPmUnselectedTextColor: Color = Ux4gColors.neutral900
    ): Ux4gTimePickerColors = Ux4gTimePickerColors(
        primary = primary,
        onSurface = onSurface,
        surface = surface,
        error = error,
        borderColor = borderColor,
        selectedBorderColor = selectedBorderColor,
        placeholderColor = placeholderColor,
        textColor = textColor,
        iconColor = iconColor,
        selectedIconColor = selectedIconColor,
        dividerColor = dividerColor,
        headerTextColor = headerTextColor,
        wheelItemSelectedBackground = wheelItemSelectedBackground,
        wheelItemSelectedTextColor = wheelItemSelectedTextColor,
        wheelItemUnselectedTextColor = wheelItemUnselectedTextColor,
        amPmSelectedBackground = amPmSelectedBackground,
        amPmSelectedTextColor = amPmSelectedTextColor,
        amPmUnselectedTextColor = amPmUnselectedTextColor
    )
}

/**
 * A custom Time Picker trigger & dialog supporting 12-hour AM/PM format, minute intervals,
 * and foundation typography & colors matching the Flutter UX4G specification.
 */
@Composable
fun Ux4gTimePicker(
    selectedTime: Ux4gTimeOfDay?,
    onTimeSelected: (Ux4gTimeOfDay) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = "Select time",
    enabled: Boolean = true,
    minuteInterval: Int = 1,
    label: String? = null,
    description: String? = null,
    isRequired: Boolean = false,
    colors: Ux4gTimePickerColors = Ux4gTimePickerDefaults.colors()
) {
    var showDialog by remember { mutableStateOf(false) }

    val isSelected = selectedTime != null
    val displayValue = selectedTime?.format(placeholder) ?: placeholder

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(0.dp)
    ) {
        // Optional Label
        if (label != null) {
            Text(
                text = buildAnnotatedString {
                    withStyle(SpanStyle(color = if (enabled) colors.onSurface else colors.onSurface.copy(alpha = 0.38f))) {
                        append(label)
                    }
                    if (isRequired) {
                        withStyle(SpanStyle(color = colors.error)) {
                            append(" *")
                        }
                    }
                },
                style = defaultTypography.lM_strong
            )
            Spacer(modifier = Modifier.height(8.dp))
        }

        // Trigger Box
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(Ux4gRadius.Radius8))
                .background(colors.surface)
                .border(
                    width = 1.dp,
                    color = if (isSelected) colors.selectedBorderColor else colors.borderColor,
                    shape = RoundedCornerShape(Ux4gRadius.Radius8)
                )
                .clickable(enabled = enabled) {
                    showDialog = true
                }
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = displayValue,
                    style = defaultTypography.bM_default,
                    color = if (isSelected) {
                        if (enabled) colors.textColor else colors.textColor.copy(alpha = 0.38f)
                    } else {
                        colors.placeholderColor
                    }
                )

                Icon(
                    imageVector = Icons.Default.AccessTime,
                    contentDescription = "Select Time",
                    tint = if (isSelected) {
                        if (enabled) colors.selectedIconColor else colors.selectedIconColor.copy(alpha = 0.38f)
                    } else {
                        colors.iconColor
                    },
                    modifier = Modifier.size(20.dp)
                )
            }
        }

        // Optional Description
        if (description != null) {
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = description,
                style = defaultTypography.lS_default,
                color = if (enabled) colors.onSurface.copy(alpha = 0.7f) else colors.onSurface.copy(alpha = 0.38f)
            )
        }
    }

    // Time Picker Dialog
    if (showDialog) {
        Ux4gTimePickerDialog(
            initialTime = selectedTime ?: Ux4gTimeOfDay.now(),
            hasInitialTime = selectedTime != null,
            minuteInterval = minuteInterval,
            colors = colors,
            onConfirm = { time ->
                showDialog = false
                onTimeSelected(time)
            },
            onDismiss = {
                showDialog = false
            }
        )
    }
}

/**
 * LocalTime overload for convenience when using java.time.LocalTime.
 */
@Composable
fun Ux4gTimePicker(
    selectedTime: LocalTime?,
    onTimeSelected: (LocalTime) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = "Select time",
    enabled: Boolean = true,
    minuteInterval: Int = 1,
    label: String? = null,
    description: String? = null,
    isRequired: Boolean = false,
    colors: Ux4gTimePickerColors = Ux4gTimePickerDefaults.colors()
) {
    Ux4gTimePicker(
        selectedTime = selectedTime?.let { Ux4gTimeOfDay.fromLocalTime(it) },
        onTimeSelected = { onTimeSelected(it.toLocalTime()) },
        modifier = modifier,
        placeholder = placeholder,
        enabled = enabled,
        minuteInterval = minuteInterval,
        label = label,
        description = description,
        isRequired = isRequired,
        colors = colors
    )
}

/**
 * Modal Dialog for picking hours, minutes, and AM/PM.
 */
@Composable
fun Ux4gTimePickerDialog(
    initialTime: Ux4gTimeOfDay,
    hasInitialTime: Boolean,
    minuteInterval: Int = 1,
    colors: Ux4gTimePickerColors = Ux4gTimePickerDefaults.colors(),
    onConfirm: (Ux4gTimeOfDay) -> Unit,
    onDismiss: () -> Unit
) {
    var selectedHour by remember { mutableIntStateOf(initialTime.hourOfPeriod) }
    var isAm by remember { mutableStateOf(initialTime.isAm) }
    var hasInteracted by remember { mutableStateOf(hasInitialTime) }

    // Build minutes list according to interval
    val minutesList = remember(minuteInterval) {
        val interval = minuteInterval.coerceAtLeast(1)
        val list = mutableListOf<Int>()
        for (i in 0 until 60 step interval) {
            list.add(i)
        }
        list
    }

    // Find closest initial minute in interval
    val initialMinute = remember(initialTime, minutesList) {
        var closest = minutesList.first()
        var minDiff = 60
        for (m in minutesList) {
            val diff = abs(initialTime.minute - m)
            if (diff < minDiff) {
                minDiff = diff
                closest = m
            }
        }
        closest
    }

    var selectedMinute by remember { mutableIntStateOf(initialMinute) }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = true,
            usePlatformDefaultWidth = false
        )
    ) {
        Surface(
            modifier = Modifier
                .width(320.dp)
                .wrapContentHeight()
                .shadow(elevation = 10.dp, shape = RoundedCornerShape(12.dp)),
            shape = RoundedCornerShape(12.dp),
            color = colors.surface,
            tonalElevation = 6.dp
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // ── Header Row ──
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                        Text(
                            text = "HH",
                            style = defaultTypography.bM_strong,
                            color = colors.headerTextColor
                        )
                    }
                    Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                        Text(
                            text = "MM",
                            style = defaultTypography.bM_strong,
                            color = colors.headerTextColor
                        )
                    }
                    Box(modifier = Modifier.weight(1f)) // Empty space matching AM/PM column
                }

                HorizontalDivider(thickness = 1.dp, color = colors.dividerColor)

                // ── Body Columns (250dp height) ──
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(250.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Hours Wheel (1 to 12)
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                    ) {
                        TimeScrollWheel(
                            itemCount = 12,
                            selectedIndex = selectedHour - 1,
                            onItemClick = { index ->
                                selectedHour = index + 1
                                hasInteracted = true
                            },
                            itemContent = { index ->
                                val hour = index + 1
                                TimeWheelItem(
                                    text = hour.toString().padStart(2, '0'),
                                    isSelected = hour == selectedHour,
                                    showHighlight = hasInteracted,
                                    selectedBackgroundColor = colors.wheelItemSelectedBackground,
                                    selectedTextColor = colors.wheelItemSelectedTextColor,
                                    unselectedTextColor = colors.wheelItemUnselectedTextColor
                                )
                            }
                        )
                    }

                    VerticalDivider(
                        modifier = Modifier.fillMaxHeight(),
                        thickness = 1.dp,
                        color = colors.dividerColor
                    )

                    // Minutes Wheel
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                    ) {
                        val currentMinuteIndex = minutesList.indexOf(selectedMinute).coerceAtLeast(0)
                        TimeScrollWheel(
                            itemCount = minutesList.size,
                            selectedIndex = currentMinuteIndex,
                            onItemClick = { index ->
                                selectedMinute = minutesList[index]
                                hasInteracted = true
                            },
                            itemContent = { index ->
                                val minute = minutesList[index]
                                TimeWheelItem(
                                    text = minute.toString().padStart(2, '0'),
                                    isSelected = minute == selectedMinute,
                                    showHighlight = hasInteracted,
                                    selectedBackgroundColor = colors.wheelItemSelectedBackground,
                                    selectedTextColor = colors.wheelItemSelectedTextColor,
                                    unselectedTextColor = colors.wheelItemUnselectedTextColor
                                )
                            }
                        )
                    }

                    VerticalDivider(
                        modifier = Modifier.fillMaxHeight(),
                        thickness = 1.dp,
                        color = colors.dividerColor
                    )

                    // AM / PM Toggle Column
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        AmPmButton(
                            text = "AM",
                            isSelected = isAm,
                            selectedBackgroundColor = colors.amPmSelectedBackground,
                            selectedTextColor = colors.amPmSelectedTextColor,
                            unselectedTextColor = colors.amPmUnselectedTextColor,
                            onClick = {
                                isAm = true
                                hasInteracted = true
                            }
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        AmPmButton(
                            text = "PM",
                            isSelected = !isAm,
                            selectedBackgroundColor = colors.amPmSelectedBackground,
                            selectedTextColor = colors.amPmSelectedTextColor,
                            unselectedTextColor = colors.amPmUnselectedTextColor,
                            onClick = {
                                isAm = false
                                hasInteracted = true
                            }
                        )
                    }
                }

                HorizontalDivider(thickness = 1.dp, color = colors.dividerColor)

                // ── Footer Buttons ──
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Ux4gButton(
                        text = "Done",
                        variant = Ux4gButtonVariant.Primary,
                        enabled = hasInteracted,
                        modifier = Modifier.weight(1f),
                        onClick = {
                            var finalHour = selectedHour
                            if (isAm && finalHour == 12) {
                                finalHour = 0
                            } else if (!isAm && finalHour != 12) {
                                finalHour += 12
                            }
                            onConfirm(Ux4gTimeOfDay(hour = finalHour, minute = selectedMinute))
                        }
                    )

                    Ux4gButton(
                        text = "Cancel",
                        variant = Ux4gButtonVariant.Outline,
                        modifier = Modifier.weight(1f),
                        onClick = onDismiss
                    )
                }
            }
        }
    }
}

// ─── Internal Helper Components ─────────────────────────────────────────────

@Composable
private fun TimeScrollWheel(
    itemCount: Int,
    selectedIndex: Int,
    onItemClick: (Int) -> Unit,
    itemContent: @Composable (Int) -> Unit
) {
    val listState = rememberLazyListState(
        initialFirstVisibleItemIndex = (selectedIndex - 2).coerceAtLeast(0)
    )
    val coroutineScope = rememberCoroutineScope()
    val flingBehavior = rememberSnapFlingBehavior(lazyListState = listState)

    LaunchedEffect(selectedIndex) {
        val targetScrollIndex = (selectedIndex - 2).coerceAtLeast(0)
        listState.animateScrollToItem(targetScrollIndex)
    }

    LazyColumn(
        state = listState,
        flingBehavior = flingBehavior,
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(vertical = 0.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        items(itemCount) { index ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .clickable {
                        onItemClick(index)
                        coroutineScope.launch {
                            val target = (index - 2).coerceAtLeast(0)
                            listState.animateScrollToItem(target)
                        }
                    },
                contentAlignment = Alignment.Center
            ) {
                itemContent(index)
            }
        }
    }
}

@Composable
private fun TimeWheelItem(
    text: String,
    isSelected: Boolean,
    showHighlight: Boolean,
    selectedBackgroundColor: Color,
    selectedTextColor: Color,
    unselectedTextColor: Color
) {
    val isHighlighted = isSelected && showHighlight
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(if (isHighlighted) selectedBackgroundColor else Color.Transparent),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            style = defaultTypography.bM_default,
            color = if (isHighlighted) selectedTextColor else unselectedTextColor
        )
    }
}

@Composable
private fun AmPmButton(
    text: String,
    isSelected: Boolean,
    selectedBackgroundColor: Color,
    selectedTextColor: Color,
    unselectedTextColor: Color,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .width(60.dp)
            .height(36.dp)
            .clip(RoundedCornerShape(6.dp))
            .background(if (isSelected) selectedBackgroundColor else Color.Transparent)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            style = defaultTypography.bM_default,
            color = if (isSelected) selectedTextColor else unselectedTextColor
        )
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Previews
// ─────────────────────────────────────────────────────────────────────────────

@Preview(name = "1. Light Mode - Default", showBackground = true)
@Composable
fun Ux4gTimePickerPreview() {
    var time by remember { mutableStateOf<Ux4gTimeOfDay?>(Ux4gTimeOfDay(14, 30)) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp)
            .background(Ux4gColors.white),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Ux4gTimePicker(
            selectedTime = time,
            onTimeSelected = { time = it },
            label = "Meeting Start Time",
            description = "Select time in 12-hour format with AM/PM",
            isRequired = true
        )

        var emptyTime by remember { mutableStateOf<Ux4gTimeOfDay?>(null) }
        Ux4gTimePicker(
            selectedTime = emptyTime,
            onTimeSelected = { emptyTime = it },
            label = "Reminder Time (5-min intervals)",
            minuteInterval = 5,
            placeholder = "Choose a time"
        )
    }
}

@Preview(name = "2. Dark Background Preview", showBackground = true)
@Composable
fun Ux4gTimePickerDarkPreview() {
    var time by remember { mutableStateOf<Ux4gTimeOfDay?>(Ux4gTimeOfDay(9, 15)) }

    Surface(
        modifier = Modifier.padding(24.dp),
        color = Ux4gColors.neutral50
    ) {
        Ux4gTimePicker(
            selectedTime = time,
            onTimeSelected = { time = it },
            label = "Alarm Time",
            isRequired = true
        )
    }
}
