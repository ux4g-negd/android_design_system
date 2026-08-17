package com.ux4g.designsystemv1.components

import android.content.res.Configuration
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.outlined.CalendarToday
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.ux4g.designsystemv1.components.buttons.Ux4gButton
import com.ux4g.designsystemv1.components.buttons.Ux4gButtonVariant
import com.ux4g.designsystemv1.components.inputfield.Ux4gInputFieldStatus
import com.ux4g.designsystemv1.foundation.Ux4gColors
import com.ux4g.designsystemv1.foundation.Ux4gRadius
import com.ux4g.designsystemv1.foundation.defaultTypography
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.Locale

/** Defines the mode for the date picker. */
enum class Ux4gDatePickerMode {
    Single,
    Range
}

/** Represents a selected date range. */
@Immutable
data class Ux4gDateRange(
    val start: LocalDate,
    val end: LocalDate
)

/**
 * A customizable Date Picker component supporting single and range modes.
 * Matches UX4G Flutter design specifications.
 */
@Composable
fun Ux4gDatePicker(
    modifier: Modifier = Modifier,
    mode: Ux4gDatePickerMode = Ux4gDatePickerMode.Single,
    selectedDate: LocalDate? = null,
    selectedDateRange: Ux4gDateRange? = null,
    minDate: LocalDate? = null,
    maxDate: LocalDate? = null,
    onDateSelected: ((LocalDate) -> Unit)? = null,
    onDateRangeSelected: ((Ux4gDateRange) -> Unit)? = null,
    placeholder: String = "Select date",
    enabled: Boolean = true,
    label: String? = null,
    description: String? = null,
    isRequired: Boolean = false,
    status: Ux4gInputFieldStatus = Ux4gInputFieldStatus.Default,
) {
    var showDialog by remember { mutableStateOf(false) }

    // Formatted date string
    fun formatDate(date: LocalDate): String {
        val d = date.dayOfMonth.toString().padStart(2, '0')
        val m = date.monthValue.toString().padStart(2, '0')
        val y = date.year.toString().padStart(4, '0')
        return "$d/$m/$y"
    }

    val isSelected = when (mode) {
        Ux4gDatePickerMode.Single -> selectedDate != null
        Ux4gDatePickerMode.Range -> selectedDateRange != null
    }

    val onSurface = Ux4gColors.neutral900
    val onBackground = Ux4gColors.neutral900
    val errorColor = Ux4gColors.red600
    val warningColor = Ux4gColors.orange600
    val successColor = Ux4gColors.green600

    val borderColor: Color = if (!enabled) {
        onSurface.copy(alpha = 0.38f)
    } else {
        when (status) {
            Ux4gInputFieldStatus.Error -> errorColor
            Ux4gInputFieldStatus.Warning -> warningColor
            Ux4gInputFieldStatus.Success -> successColor
            Ux4gInputFieldStatus.Default -> if (isSelected) Ux4gColors.primary else onSurface.copy(alpha = 0.38f)
        }
    }

    val textStyle = defaultTypography.bM_default.copy(
        color = if (isSelected) onSurface else onSurface.copy(alpha = 0.38f)
    )

    val iconColor = if (isSelected) Ux4gColors.primary else onSurface.copy(alpha = 0.38f)

    @Composable
    fun DateInputBox(
        text: String,
        boxModifier: Modifier = Modifier
    ) {
        Box(
            modifier = boxModifier
                .clip(RoundedCornerShape(8.dp))
                .background(Ux4gColors.neutral0)
                .border(1.dp, borderColor, RoundedCornerShape(8.dp))
                .clickable(enabled = enabled) { showDialog = true }
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = text,
                    style = textStyle,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f, fill = false)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Icon(
                    imageVector = Icons.Outlined.CalendarToday,
                    contentDescription = "Select Date",
                    tint = iconColor,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Label
        if (label != null) {
            val labelColor = if (!enabled) onSurface.copy(alpha = 0.4f) else onBackground
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = label,
                    style = defaultTypography.bS_strong.copy(color = labelColor)
                )
                if (isRequired) {
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "*",
                        style = defaultTypography.bS_strong.copy(color = errorColor)
                    )
                }
            }
        }

        // Trigger Box
        if (mode == Ux4gDatePickerMode.Single) {
            val text = if (selectedDate != null) formatDate(selectedDate) else placeholder
            DateInputBox(text = text, boxModifier = Modifier.fillMaxWidth())
        } else {
            val startText = if (selectedDateRange != null) formatDate(selectedDateRange.start) else placeholder
            val endText = if (selectedDateRange != null) formatDate(selectedDateRange.end) else placeholder
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                DateInputBox(text = startText, boxModifier = Modifier.weight(1f))
                Text(
                    text = "–",
                    style = textStyle,
                    color = onSurface
                )
                DateInputBox(text = endText, boxModifier = Modifier.weight(1f))
            }
        }

        // Description / Status message
        if (description != null) {
            val descriptionColor = if (!enabled) {
                onSurface.copy(alpha = 0.4f)
            } else {
                when (status) {
                    Ux4gInputFieldStatus.Error -> errorColor
                    Ux4gInputFieldStatus.Warning -> warningColor
                    Ux4gInputFieldStatus.Success -> successColor
                    Ux4gInputFieldStatus.Default -> onSurface.copy(alpha = 0.6f)
                }
            }

            val statusIcon = when (status) {
                Ux4gInputFieldStatus.Error -> Icons.Default.Error
                Ux4gInputFieldStatus.Warning -> Icons.Default.Warning
                Ux4gInputFieldStatus.Success -> Icons.Default.CheckCircle
                Ux4gInputFieldStatus.Default -> Icons.Outlined.Info
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Icon(
                    imageVector = statusIcon,
                    contentDescription = null,
                    tint = descriptionColor,
                    modifier = Modifier.size(14.dp)
                )
                Text(
                    text = description,
                    style = defaultTypography.bXS_default.copy(color = descriptionColor)
                )
            }
        }
    }

    // Picker Dialog
    if (showDialog) {
        Ux4gDatePickerDialog(
            mode = mode,
            initialDate = selectedDate,
            initialDateRange = selectedDateRange,
            minDate = minDate,
            maxDate = maxDate,
            onDismissRequest = { showDialog = false },
            onConfirmSingle = { date ->
                onDateSelected?.invoke(date)
                showDialog = false
            },
            onConfirmRange = { range ->
                onDateRangeSelected?.invoke(range)
                showDialog = false
            }
        )
    }
}

/**
 * Dialog popup containing the UX4G date picker calendar view and month/year selector.
 */
@Composable
fun Ux4gDatePickerDialog(
    mode: Ux4gDatePickerMode,
    initialDate: LocalDate? = null,
    initialDateRange: Ux4gDateRange? = null,
    minDate: LocalDate? = null,
    maxDate: LocalDate? = null,
    onDismissRequest: () -> Unit,
    onConfirmSingle: ((LocalDate) -> Unit)? = null,
    onConfirmRange: ((Ux4gDateRange) -> Unit)? = null,
) {
    val now = remember { LocalDate.now() }
    val initialDisplayedMonth = remember {
        when (mode) {
            Ux4gDatePickerMode.Single -> initialDate ?: now
            Ux4gDatePickerMode.Range -> initialDateRange?.start ?: now
        }
    }

    var currentDisplayedMonth by remember { mutableStateOf(YearMonth.from(initialDisplayedMonth)) }
    var tempSelectedDate by remember { mutableStateOf(initialDate) }
    var tempRangeStart by remember { mutableStateOf(initialDateRange?.start) }
    var tempRangeEnd by remember { mutableStateOf(initialDateRange?.end) }
    var showMonthYearPicker by remember { mutableStateOf(false) }
    var yearGridStart by remember {
        mutableIntStateOf(currentDisplayedMonth.year - (currentDisplayedMonth.year % 8))
    }

    val weekdays = remember { listOf("Mo", "Tu", "We", "Th", "Fr", "Sa", "Su") }
    val months = remember {
        listOf("Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec")
    }

    fun isSelectable(date: LocalDate): Boolean {
        if (minDate != null && date.isBefore(minDate)) return false
        if (maxDate != null && date.isAfter(maxDate)) return false
        return true
    }

    fun handleDaySelect(date: LocalDate) {
        if (!isSelectable(date)) return
        if (mode == Ux4gDatePickerMode.Single) {
            tempSelectedDate = date
        } else {
            if (tempRangeStart == null || (tempRangeStart != null && tempRangeEnd != null)) {
                tempRangeStart = date
                tempRangeEnd = null
            } else {
                if (date.isBefore(tempRangeStart!!)) {
                    tempRangeEnd = tempRangeStart
                    tempRangeStart = date
                } else {
                    tempRangeEnd = date
                }
            }
        }
    }

    Dialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 24.dp)
                .width(320.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(Ux4gColors.neutral0)
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                // Header
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = {
                            if (showMonthYearPicker) {
                                yearGridStart -= 8
                            } else {
                                currentDisplayedMonth = currentDisplayedMonth.minusMonths(1)
                            }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Previous",
                            tint = Ux4gColors.neutral900
                        )
                    }

                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .clickable { showMonthYearPicker = !showMonthYearPicker }
                            .padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        val headerText = if (showMonthYearPicker) {
                            "$yearGridStart-${yearGridStart + 7}"
                        } else {
                            val monthName = currentDisplayedMonth.month.getDisplayName(TextStyle.FULL, Locale.ENGLISH)
                            "$monthName ${currentDisplayedMonth.year}"
                        }
                        Text(
                            text = headerText,
                            style = defaultTypography.bM_strong.copy(color = Ux4gColors.primary)
                        )
                        Icon(
                            imageVector = if (showMonthYearPicker) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                            contentDescription = "Toggle Month/Year Picker",
                            tint = Ux4gColors.primary,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    IconButton(
                        onClick = {
                            if (showMonthYearPicker) {
                                yearGridStart += 8
                            } else {
                                currentDisplayedMonth = currentDisplayedMonth.plusMonths(1)
                            }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = "Next",
                            tint = Ux4gColors.neutral900
                        )
                    }
                }

                HorizontalDivider(color = Ux4gColors.neutral900.copy(alpha = 0.12f))

                // Body (Fixed Height 280dp)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(280.dp)
                        .padding(16.dp)
                ) {
                    Crossfade(
                        targetState = showMonthYearPicker,
                        label = "DatePickerViewSwitcher"
                    ) { isPickerOpen ->
                        if (isPickerOpen) {
                            // Month & Year Picker
                            Column(
                                modifier = Modifier.fillMaxWidth(),
                                verticalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                // Year Grid (8 years)
                                val years = (0..7).map { yearGridStart + it }
                                LazyVerticalGrid(
                                    columns = GridCells.Fixed(4),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                    verticalArrangement = Arrangement.spacedBy(8.dp),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    items(years) { year ->
                                        val isSelected = year == currentDisplayedMonth.year
                                        val isSelectable = maxDate == null || year <= maxDate.year
                                        Box(
                                            modifier = Modifier
                                                .height(32.dp)
                                                .clip(RoundedCornerShape(4.dp))
                                                .background(
                                                    if (isSelected) Ux4gColors.primary.copy(alpha = 0.2f)
                                                    else Color.Transparent
                                                )
                                                .clickable(enabled = isSelectable) {
                                                    currentDisplayedMonth = YearMonth.of(year, currentDisplayedMonth.monthValue)
                                                },
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(
                                                text = year.toString(),
                                                style = defaultTypography.bM_default.copy(
                                                    color = when {
                                                        isSelected -> Ux4gColors.primary
                                                        isSelectable -> Ux4gColors.neutral900
                                                        else -> Ux4gColors.neutral900.copy(alpha = 0.38f)
                                                    }
                                                )
                                            )
                                        }
                                    }
                                }

                                HorizontalDivider(color = Ux4gColors.neutral900.copy(alpha = 0.12f))

                                // Month Grid (12 months)
                                LazyVerticalGrid(
                                    columns = GridCells.Fixed(4),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                    verticalArrangement = Arrangement.spacedBy(8.dp),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    items((1..12).toList()) { monthValue ->
                                        val isSelected = monthValue == currentDisplayedMonth.monthValue
                                        val isSelectable = maxDate == null ||
                                                currentDisplayedMonth.year < maxDate.year ||
                                                (currentDisplayedMonth.year == maxDate.year && monthValue <= maxDate.monthValue)
                                        Box(
                                            modifier = Modifier
                                                .height(32.dp)
                                                .clip(RoundedCornerShape(4.dp))
                                                .background(
                                                    if (isSelected) Ux4gColors.primary.copy(alpha = 0.2f)
                                                    else Color.Transparent
                                                )
                                                .clickable(enabled = isSelectable) {
                                                    currentDisplayedMonth = YearMonth.of(currentDisplayedMonth.year, monthValue)
                                                },
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(
                                                text = months[monthValue - 1],
                                                style = defaultTypography.bM_default.copy(
                                                    color = when {
                                                        isSelected -> Ux4gColors.primary
                                                        isSelectable -> Ux4gColors.neutral900
                                                        else -> Ux4gColors.neutral900.copy(alpha = 0.38f)
                                                    }
                                                )
                                            )
                                        }
                                    }
                                }
                            }
                        } else {
                            // Calendar View
                            Column(modifier = Modifier.fillMaxWidth()) {
                                // Weekday Row
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceAround
                                ) {
                                    weekdays.forEach { dayName ->
                                        Text(
                                            text = dayName,
                                            style = defaultTypography.bS_strong.copy(color = Ux4gColors.neutral900),
                                            textAlign = TextAlign.Center,
                                            modifier = Modifier.weight(1f)
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(8.dp))

                                // Days Grid
                                val firstDayOfMonth = currentDisplayedMonth.atDay(1)
                                val daysInMonth = currentDisplayedMonth.lengthOfMonth()
                                // Monday = 1, Sunday = 7 -> leading empty days = dayOfWeek.value - 1
                                val leadingDays = firstDayOfMonth.dayOfWeek.value - 1
                                val totalCells = leadingDays + daysInMonth
                                val rows = (totalCells + 6) / 7

                                LazyVerticalGrid(
                                    columns = GridCells.Fixed(7),
                                    horizontalArrangement = Arrangement.spacedBy(0.dp),
                                    verticalArrangement = Arrangement.spacedBy(4.dp),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    items(rows * 7) { index ->
                                        if (index < leadingDays || index >= leadingDays + daysInMonth) {
                                            Box(modifier = Modifier.size(36.dp))
                                        } else {
                                            val day = index - leadingDays + 1
                                            val date = currentDisplayedMonth.atDay(day)
                                            val isToday = date == now
                                            val selectable = isSelectable(date)

                                            var isSelectedSingle = false
                                            var isRangeStart = false
                                            var isRangeEnd = false
                                            var isInRange = false

                                            if (mode == Ux4gDatePickerMode.Single) {
                                                isSelectedSingle = tempSelectedDate == date
                                            } else {
                                                if (tempRangeStart != null && date == tempRangeStart) {
                                                    isRangeStart = true
                                                }
                                                if (tempRangeEnd != null && date == tempRangeEnd) {
                                                    isRangeEnd = true
                                                }
                                                if (tempRangeStart != null && tempRangeEnd != null &&
                                                    date.isAfter(tempRangeStart) && date.isBefore(tempRangeEnd)
                                                ) {
                                                    isInRange = true
                                                }
                                            }

                                            val isSolidPurple = isSelectedSingle || isRangeStart || isRangeEnd
                                            val isLightPurple = isInRange

                                            val shape = when {
                                                isSolidPurple -> RoundedCornerShape(4.dp)
                                                isRangeStart -> RoundedCornerShape(topStart = 4.dp, bottomStart = 4.dp)
                                                isRangeEnd -> RoundedCornerShape(topEnd = 4.dp, bottomEnd = 4.dp)
                                                else -> RoundedCornerShape(0.dp)
                                            }

                                            val bg = when {
                                                isSolidPurple -> Ux4gColors.primary
                                                isLightPurple -> Ux4gColors.primary.copy(alpha = 0.1f)
                                                else -> Color.Transparent
                                            }

                                            val txtColor = when {
                                                isSolidPurple -> Ux4gColors.neutral50
                                                selectable -> Ux4gColors.neutral900
                                                else -> Ux4gColors.neutral900.copy(alpha = 0.38f)
                                            }

                                            Box(
                                                modifier = Modifier
                                                    .size(36.dp)
                                                    .clip(shape)
                                                    .background(bg)
                                                    .clickable(enabled = selectable) { handleDaySelect(date) },
                                                contentAlignment = Alignment.Center
                                            ) {
                                                Column(
                                                    horizontalAlignment = Alignment.CenterHorizontally,
                                                    verticalArrangement = Arrangement.Center
                                                ) {
                                                    Text(
                                                        text = day.toString(),
                                                        style = defaultTypography.bM_default.copy(color = txtColor),
                                                        textAlign = TextAlign.Center
                                                    )
                                                    if (isToday && !isSolidPurple) {
                                                        Spacer(modifier = Modifier.height(2.dp))
                                                        Box(
                                                            modifier = Modifier
                                                                .size(4.dp)
                                                                .clip(CircleShape)
                                                                .background(Ux4gColors.primary)
                                                        )
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                HorizontalDivider(color = Ux4gColors.neutral900.copy(alpha = 0.12f))

                // Footer Actions
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Box(modifier = Modifier.weight(1f)) {
                        Ux4gButton(
                            text = "Cancel",
                            variant = Ux4gButtonVariant.Outline,
                            backgroundColor = Color.Transparent,
                            contentColor = Ux4gColors.primary,
                            borderColor = Ux4gColors.primary,
                            borderWidth = 1.dp,
                            borderRadius = Ux4gRadius.Radius8,
                            onClick = onDismissRequest,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                    Box(modifier = Modifier.weight(1f)) {
                        Ux4gButton(
                            text = if (showMonthYearPicker) "Select date" else "Confirm",
                            variant = Ux4gButtonVariant.Primary,
                            backgroundColor = Ux4gColors.primary,
                            contentColor = Ux4gColors.neutral50,
                            borderRadius = Ux4gRadius.Radius8,
                            onClick = {
                                if (showMonthYearPicker) {
                                    showMonthYearPicker = false
                                } else {
                                    if (mode == Ux4gDatePickerMode.Single) {
                                        tempSelectedDate?.let { onConfirmSingle?.invoke(it) }
                                    } else {
                                        if (tempRangeStart != null && tempRangeEnd != null) {
                                            onConfirmRange?.invoke(
                                                Ux4gDateRange(start = tempRangeStart!!, end = tempRangeEnd!!)
                                            )
                                        }
                                    }
                                }
                            },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        }
    }
}

// ═══════════════════════════════════════════════════════
// Previews
// ═══════════════════════════════════════════════════════

@Preview(name = "Date Picker - Light", showBackground = true, backgroundColor = 0xFFFFFFFF)
@Preview(name = "Date Picker - Dark", uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true, backgroundColor = 0xFF121212)
@Composable
private fun Ux4gDatePickerPreview() {
    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Text("Single Date Picker", style = defaultTypography.hS_strong)
        var singleDate by remember { mutableStateOf<LocalDate?>(LocalDate.now()) }
        Ux4gDatePicker(
            label = "Date of Birth",
            isRequired = true,
            selectedDate = singleDate,
            onDateSelected = { singleDate = it },
            description = "Select your birth date for verification"
        )

        Text("Date Range Picker", style = defaultTypography.hS_strong)
        var rangeDate by remember {
            mutableStateOf<Ux4gDateRange?>(
                Ux4gDateRange(LocalDate.now(), LocalDate.now().plusDays(5))
            )
        }
        Ux4gDatePicker(
            mode = Ux4gDatePickerMode.Range,
            label = "Booking Period",
            selectedDateRange = rangeDate,
            onDateRangeSelected = { rangeDate = it },
            description = "Start and end dates"
        )

        Text("Validation States", style = defaultTypography.hS_strong)
        Ux4gDatePicker(
            label = "Error State",
            status = Ux4gInputFieldStatus.Error,
            description = "Please select a valid future date"
        )
        Ux4gDatePicker(
            label = "Warning State",
            status = Ux4gInputFieldStatus.Warning,
            description = "Selected date is on a public holiday"
        )
        Ux4gDatePicker(
            label = "Success State",
            status = Ux4gInputFieldStatus.Success,
            selectedDate = LocalDate.now(),
            description = "Date slot available"
        )
        Ux4gDatePicker(
            label = "Disabled State",
            enabled = false,
            description = "This field is locked"
        )
    }
}
