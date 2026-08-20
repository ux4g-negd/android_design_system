package com.ux4g.designsystemv1.components.timeslot

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ux4g.designsystemv1.foundation.Ux4gColors
import com.ux4g.designsystemv1.foundation.Ux4gIconComponent
import com.ux4g.designsystemv1.foundation.Ux4gIcons
import com.ux4g.designsystemv1.foundation.defaultTypography
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.Locale

// ─── Enums & Models ─────────────────────────────────────────────────────────

/**
 * View mode for the time slot picker sheet.
 */
enum class SlotPickerViewMode {
    /** Expanded view showing slots in a vertical list. */
    Expanded,

    /** Compact view showing slots in a 2-column grid. */
    Compact
}

/**
 * State of an individual date cell in the calendar slot grid.
 */
enum class SlotDateStatus {
    /** Date has available booking slots (default). */
    Available,

    /** Date has no available booking slots. */
    NoSlots,

    /** Date is a public holiday. */
    PublicHoliday,

    /** Date falls on a weekly-off day (e.g. Saturday / Sunday). */
    WeeklyOff
}

/**
 * Represents a single date entry with its specific status override.
 */
data class SlotDateEntry(
    val date: LocalDate,
    val status: SlotDateStatus
)

/**
 * Top-level data model passed to [Ux4gTimeslot].
 *
 * @param year The calendar year to display.
 * @param month The calendar month to display (1 = Jan ... 12 = Dec).
 * @param selectedDate The currently active selected date.
 * @param today Reference date for "today" (defaults to current date).
 * @param weeklyOffWeekdays ISO weekday numbers (1=Mon ... 7=Sun) that are weekly-off by default (default [6, 7]).
 * @param dates List of explicit date status overrides.
 * @param allowTapOnPublicHoliday Whether tapping on a public holiday is allowed.
 * @param allowTapOnWeeklyOff Whether tapping on a weekly-off day is allowed.
 * @param viewMode View mode for the time slot picker sheet ([SlotPickerViewMode.Expanded] or [SlotPickerViewMode.Compact]).
 */
data class Ux4gTimeslotData(
    val year: Int,
    val month: Int,
    val selectedDate: LocalDate? = null,
    val today: LocalDate? = null,
    val weeklyOffWeekdays: List<Int> = listOf(6, 7),
    val dates: List<SlotDateEntry> = emptyList(),
    val allowTapOnPublicHoliday: Boolean = false,
    val allowTapOnWeeklyOff: Boolean = false,
    val viewMode: SlotPickerViewMode = SlotPickerViewMode.Expanded
)

// Backward-compatibility aliases
typealias SlotGridData = Ux4gTimeslotData
typealias TimeSlotData = Ux4gTimeslotData
typealias Ux4gTimeSlotData = Ux4gTimeslotData

/**
 * Availability status of a time slot.
 */
enum class SlotTimeStatus {
    /** Slot is open for booking. */
    Available,

    /** Slot has limited capacity remaining. */
    Limited,

    /** No slots available for this time. */
    NoSlots
}

/**
 * A single bookable time entry shown inside the time picker sheet.
 */
data class SlotTimeEntry(
    val time: String,
    val slotCount: Int,
    val status: SlotTimeStatus = SlotTimeStatus.Available
)

// ─── Colors & Defaults ──────────────────────────────────────────────────────

@Immutable
data class Ux4gTimeslotColors(
    val primary: Color,
    val onPrimary: Color,
    val surface: Color,
    val onSurface: Color,
    val warning: Color,
    val success: Color,
    val border: Color,
    val disabled: Color
)

object Ux4gTimeslotDefaults {
    @Composable
    fun colors(
        primary: Color = Ux4gColors.primary,
        onPrimary: Color = Ux4gColors.white,
        surface: Color = Ux4gColors.white,
        onSurface: Color = Ux4gColors.neutral900,
        warning: Color = Ux4gColors.orange600,
        success: Color = Ux4gColors.green600,
        border: Color = Ux4gColors.neutral900.copy(alpha = 0.12f),
        disabled: Color = Ux4gColors.neutral900.copy(alpha = 0.38f)
    ): Ux4gTimeslotColors = Ux4gTimeslotColors(
        primary = primary,
        onPrimary = onPrimary,
        surface = surface,
        onSurface = onSurface,
        warning = warning,
        success = success,
        border = border,
        disabled = disabled
    )
}

// ─── Main Timeslot Component ────────────────────────────────────────────────

/**
 * Calendar-style slot grid component matching UX4G Flutter specification 1:1.
 *
 * Displays a full calendar month grid with status styling (available, no slots,
 * public holidays, weekly offs, and today markers) and automatically manages the
 * interactive [Ux4gSlotTimePickerSheet] when a [timeSlotProvider] is supplied.
 */
@Composable
fun Ux4gTimeslot(
    data: Ux4gTimeslotData,
    modifier: Modifier = Modifier,
    onDateSelected: ((LocalDate) -> Unit)? = null,
    onMonthChanged: ((year: Int, month: Int) -> Unit)? = null,
    timeSlotProvider: ((LocalDate) -> List<SlotTimeEntry>)? = null,
    onSlotConfirmed: ((date: LocalDate, slot: SlotTimeEntry) -> Unit)? = null,
    colors: Ux4gTimeslotColors = Ux4gTimeslotDefaults.colors()
) {
    var currentYear by remember(data.year) { mutableIntStateOf(data.year) }
    var currentMonth by remember(data.month) { mutableIntStateOf(data.month) }
    var selectedDate by remember(data.selectedDate) { mutableStateOf(data.selectedDate) }

    val today = remember(data.today) { data.today ?: LocalDate.now() }
    val isCurrentMonth = currentYear == today.year && currentMonth == today.monthValue

    var sheetDate by remember { mutableStateOf<LocalDate?>(null) }
    var sheetSlots by remember { mutableStateOf<List<SlotTimeEntry>>(emptyList()) }

    fun statusFor(date: LocalDate): SlotDateStatus {
        for (entry in data.dates) {
            if (entry.date == date) return entry.status
        }
        if (data.weeklyOffWeekdays.contains(date.dayOfWeek.value)) {
            return SlotDateStatus.WeeklyOff
        }
        return SlotDateStatus.Available
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .shadow(elevation = 4.dp, shape = RoundedCornerShape(12.dp), spotColor = Color.Black.copy(alpha = 0.06f))
            .clip(RoundedCornerShape(12.dp))
            .background(colors.surface)
            .border(1.dp, colors.border, RoundedCornerShape(12.dp))
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Header: < Month Year >
            TimeslotHeader(
                year = currentYear,
                month = currentMonth,
                isPrevDisabled = isCurrentMonth,
                onPreviousMonth = {
                    if (!isCurrentMonth) {
                        if (currentMonth == 1) {
                            currentMonth = 12
                            currentYear -= 1
                        } else {
                            currentMonth -= 1
                        }
                        onMonthChanged?.invoke(currentYear, currentMonth)
                    }
                },
                onNextMonth = {
                    if (currentMonth == 12) {
                        currentMonth = 1
                        currentYear += 1
                    } else {
                        currentMonth += 1
                    }
                    onMonthChanged?.invoke(currentYear, currentMonth)
                },
                colors = colors
            )

            // Weekday Row: Mo Tu We Th Fr Sa Su
            WeekdayRow(
                weeklyOffWeekdays = data.weeklyOffWeekdays,
                colors = colors
            )

            // Dates Grid
            DatesGrid(
                year = currentYear,
                month = currentMonth,
                today = today,
                selectedDate = selectedDate,
                data = data,
                statusFor = ::statusFor,
                onDateClicked = { date ->
                    if (timeSlotProvider != null) {
                        val slots = timeSlotProvider(date)
                        sheetDate = date
                        sheetSlots = slots
                    } else {
                        selectedDate = date
                        onDateSelected?.invoke(date)
                    }
                },
                colors = colors
            )

            // Legend
            TimeslotLegend(colors = colors)
        }
    }

    // Modal Bottom Sheet for Time Slots
    if (sheetDate != null) {
        Ux4gSlotTimePickerSheet(
            date = sheetDate!!,
            slots = sheetSlots,
            today = today,
            viewMode = data.viewMode,
            onDismiss = { sheetDate = null },
            onConfirm = { slot ->
                val confirmedDate = sheetDate!!
                selectedDate = confirmedDate
                onDateSelected?.invoke(confirmedDate)
                onSlotConfirmed?.invoke(confirmedDate, slot)
                sheetDate = null
            },
            onPreviousDay = { newDate ->
                timeSlotProvider?.invoke(newDate) ?: emptyList()
            },
            onNextDay = { newDate ->
                timeSlotProvider?.invoke(newDate) ?: emptyList()
            },
            colors = colors
        )
    }
}

// Aliases for compatibility
@Composable
fun Ux4gSlotGrid(
    data: Ux4gTimeslotData,
    modifier: Modifier = Modifier,
    onDateSelected: ((LocalDate) -> Unit)? = null,
    onMonthChanged: ((year: Int, month: Int) -> Unit)? = null,
    timeSlotProvider: ((LocalDate) -> List<SlotTimeEntry>)? = null,
    onSlotConfirmed: ((date: LocalDate, slot: SlotTimeEntry) -> Unit)? = null,
    colors: Ux4gTimeslotColors = Ux4gTimeslotDefaults.colors()
) {
    Ux4gTimeslot(
        data = data,
        modifier = modifier,
        onDateSelected = onDateSelected,
        onMonthChanged = onMonthChanged,
        timeSlotProvider = timeSlotProvider,
        onSlotConfirmed = onSlotConfirmed,
        colors = colors
    )
}

@Composable
fun Ux4gTimeSlot(
    data: Ux4gTimeslotData,
    modifier: Modifier = Modifier,
    onDateSelected: ((LocalDate) -> Unit)? = null,
    onMonthChanged: ((year: Int, month: Int) -> Unit)? = null,
    timeSlotProvider: ((LocalDate) -> List<SlotTimeEntry>)? = null,
    onSlotConfirmed: ((date: LocalDate, slot: SlotTimeEntry) -> Unit)? = null,
    colors: Ux4gTimeslotColors = Ux4gTimeslotDefaults.colors()
) {
    Ux4gTimeslot(
        data = data,
        modifier = modifier,
        onDateSelected = onDateSelected,
        onMonthChanged = onMonthChanged,
        timeSlotProvider = timeSlotProvider,
        onSlotConfirmed = onSlotConfirmed,
        colors = colors
    )
}

// ─── Header ─────────────────────────────────────────────────────────────────

@Composable
private fun TimeslotHeader(
    year: Int,
    month: Int,
    isPrevDisabled: Boolean,
    onPreviousMonth: () -> Unit,
    onNextMonth: () -> Unit,
    colors: Ux4gTimeslotColors
) {
    val monthName = YearMonth.of(year, month).month.getDisplayName(TextStyle.FULL, Locale.getDefault())

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        NavArrowButton(
            icon = Ux4gIcons.arrowLeft,
            disabled = isPrevDisabled,
            onClick = onPreviousMonth,
            colors = colors
        )

        Text(
            text = "$monthName $year",
            style = defaultTypography.tM_strong.copy(
                fontWeight = FontWeight.Bold,
                color = colors.primary
            )
        )

        NavArrowButton(
            icon = Ux4gIcons.arrowRight,
            disabled = false,
            onClick = onNextMonth,
            colors = colors
        )
    }
}

@Composable
private fun NavArrowButton(
    icon: com.ux4g.designsystemv1.foundation.Ux4gIcon,
    disabled: Boolean,
    onClick: () -> Unit,
    colors: Ux4gTimeslotColors
) {
    Box(
        modifier = Modifier
            .size(32.dp)
            .clip(RoundedCornerShape(6.dp))
            .background(if (disabled) colors.onSurface.copy(alpha = 0.05f) else colors.surface)
            .border(1.dp, colors.onSurface.copy(alpha = 0.12f), RoundedCornerShape(6.dp))
            .clickable(enabled = !disabled, onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Ux4gIconComponent(
            icon = icon,
            contentDescription = null,
            tint = if (disabled) colors.onSurface.copy(alpha = 0.12f) else colors.onSurface.copy(alpha = 0.6f),
            modifier = Modifier.size(16.dp)
        )
    }
}

// ─── Weekday Row ────────────────────────────────────────────────────────────

@Composable
private fun WeekdayRow(
    weeklyOffWeekdays: List<Int>,
    colors: Ux4gTimeslotColors
) {
    val labels = listOf("Mo", "Tu", "We", "Th", "Fr", "Sa", "Su")

    Row(modifier = Modifier.fillMaxWidth()) {
        for (i in 0 until 7) {
            val weekday = i + 1
            val isWeeklyOff = weeklyOffWeekdays.contains(weekday)
            Box(
                modifier = Modifier.weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = labels[i],
                    style = defaultTypography.lM_strong.copy(
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isWeeklyOff) colors.onSurface.copy(alpha = 0.38f) else colors.onSurface.copy(alpha = 0.6f)
                    )
                )
            }
        }
    }
}

// ─── Dates Grid ─────────────────────────────────────────────────────────────

@Composable
private fun DatesGrid(
    year: Int,
    month: Int,
    today: LocalDate,
    selectedDate: LocalDate?,
    data: Ux4gTimeslotData,
    statusFor: (LocalDate) -> SlotDateStatus,
    onDateClicked: (LocalDate) -> Unit,
    colors: Ux4gTimeslotColors
) {
    val firstDayOfMonth = LocalDate.of(year, month, 1)
    val startOffset = firstDayOfMonth.dayOfWeek.value - 1 // 0 for Monday ... 6 for Sunday
    val daysInMonth = YearMonth.of(year, month).lengthOfMonth()

    val totalCells = startOffset + daysInMonth
    val rowCount = (totalCells + 6) / 7

    Column(modifier = Modifier.fillMaxWidth()) {
        for (row in 0 until rowCount) {
            Row(modifier = Modifier.fillMaxWidth()) {
                for (col in 0 until 7) {
                    val cellIndex = row * 7 + col
                    val dayOffset = cellIndex - startOffset + 1

                    Box(
                        modifier = Modifier.weight(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        if (dayOffset < 1 || dayOffset > daysInMonth) {
                            // Out of current month cell
                            val outOfMonthDay = if (dayOffset < 1) {
                                val prevYM = YearMonth.of(year, month).minusMonths(1)
                                prevYM.lengthOfMonth() + dayOffset
                            } else {
                                dayOffset - daysInMonth
                            }
                            Box(
                                modifier = Modifier
                                    .size(40.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "$outOfMonthDay",
                                    style = defaultTypography.bS_default.copy(
                                        color = colors.onSurface.copy(alpha = 0.38f)
                                    )
                                )
                            }
                        } else {
                            val date = LocalDate.of(year, month, dayOffset)
                            val status = statusFor(date)
                            val isSelected = selectedDate == date
                            val isToday = today == date
                            val isPast = date.isBefore(today)

                            val canTap = !isPast && (
                                status == SlotDateStatus.Available ||
                                (status == SlotDateStatus.PublicHoliday && data.allowTapOnPublicHoliday) ||
                                (status == SlotDateStatus.WeeklyOff && data.allowTapOnWeeklyOff)
                            )

                            Column(
                                modifier = Modifier
                                    .size(48.dp)
                                    .clickable(enabled = canTap) { onDateClicked(date) },
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                DateCellBox(
                                    day = dayOffset,
                                    status = status,
                                    isSelected = isSelected,
                                    isPast = isPast,
                                    colors = colors
                                )

                                if (isToday && !isSelected) {
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Box(
                                        modifier = Modifier
                                            .size(4.dp)
                                            .clip(CircleShape)
                                            .background(colors.primary)
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

@Composable
private fun DateCellBox(
    day: Int,
    status: SlotDateStatus,
    isSelected: Boolean,
    isPast: Boolean,
    colors: Ux4gTimeslotColors
) {
    if (isPast) {
        Box(
            modifier = Modifier.size(32.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "$day",
                style = defaultTypography.bS_default.copy(
                    color = colors.onSurface.copy(alpha = 0.38f)
                )
            )
        }
        return
    }

    if (isSelected) {
        Box(
            modifier = Modifier
                .size(32.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(colors.primary),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "$day",
                style = defaultTypography.bS_strong.copy(
                    fontWeight = FontWeight.Bold,
                    color = colors.onPrimary
                )
            )
        }
        return
    }

    when (status) {
        SlotDateStatus.PublicHoliday -> {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(colors.warning.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "$day",
                    style = defaultTypography.bS_strong.copy(
                        fontWeight = FontWeight.W500,
                        color = colors.warning
                    )
                )
            }
        }
        SlotDateStatus.NoSlots, SlotDateStatus.WeeklyOff -> {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(colors.onSurface.copy(alpha = 0.05f)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "$day",
                    style = defaultTypography.bS_default.copy(
                        color = colors.onSurface.copy(alpha = 0.38f)
                    )
                )
            }
        }
        SlotDateStatus.Available -> {
            Box(
                modifier = Modifier.size(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "$day",
                    style = defaultTypography.bS_default.copy(
                        color = colors.onSurface
                    )
                )
            }
        }
    }
}

// ─── Legend ─────────────────────────────────────────────────────────────────

@Composable
private fun TimeslotLegend(colors: Ux4gTimeslotColors) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        LegendItem(
            color = colors.onSurface.copy(alpha = 0.05f),
            label = "No slots",
            colors = colors
        )
        Spacer(modifier = Modifier.width(16.dp))
        LegendItem(
            color = colors.warning.copy(alpha = 0.1f),
            borderColor = colors.warning,
            label = "Public holiday",
            colors = colors
        )
        Spacer(modifier = Modifier.width(16.dp))
        LegendItem(
            color = colors.onSurface.copy(alpha = 0.05f),
            label = "Weekly off",
            colors = colors
        )
    }
}

@Composable
private fun LegendItem(
    color: Color,
    borderColor: Color? = null,
    label: String,
    colors: Ux4gTimeslotColors
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Box(
            modifier = Modifier
                .size(14.dp)
                .clip(RoundedCornerShape(3.dp))
                .background(color)
                .border(1.dp, borderColor ?: colors.onSurface.copy(alpha = 0.12f), RoundedCornerShape(3.dp))
        )
        Text(
            text = label,
            style = defaultTypography.lS_default.copy(
                fontSize = 11.sp,
                color = colors.onSurface.copy(alpha = 0.6f)
            )
        )
    }
}

// ─── Time Slot Picker Sheet ─────────────────────────────────────────────────

/**
 * Modal bottom sheet displaying available time slots for booking matching UX4G specification.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Ux4gSlotTimePickerSheet(
    date: LocalDate,
    slots: List<SlotTimeEntry>,
    today: LocalDate,
    viewMode: SlotPickerViewMode,
    onDismiss: () -> Unit,
    onConfirm: ((SlotTimeEntry) -> Unit)? = null,
    onPreviousDay: (suspend (LocalDate) -> List<SlotTimeEntry>)? = null,
    onNextDay: (suspend (LocalDate) -> List<SlotTimeEntry>)? = null,
    colors: Ux4gTimeslotColors = Ux4gTimeslotDefaults.colors()
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val coroutineScope = rememberCoroutineScope()

    var currentDate by remember(date) { mutableStateOf(date) }
    var currentSlots by remember(slots) { mutableStateOf(slots) }
    var selectedIndex by remember { mutableStateOf<Int?>(null) }
    var isLoading by remember { mutableStateOf(false) }

    val isToday = currentDate == today
    val canGoPrevious = currentDate.isAfter(today)

    val dateLabel = remember(currentDate) {
        val wd = currentDate.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.getDefault())
        val mo = currentDate.month.getDisplayName(TextStyle.SHORT, Locale.getDefault())
        "$wd ${currentDate.dayOfMonth} $mo"
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = colors.surface,
        shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
        dragHandle = {
            Box(
                modifier = Modifier
                    .padding(vertical = 12.dp)
                    .width(40.dp)
                    .height(4.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(colors.onSurface.copy(alpha = 0.12f))
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        ) {
            // Sheet Header: < Mon 14 Apr >
            SheetHeader(
                dateLabel = dateLabel,
                isToday = isToday,
                canGoPrevious = canGoPrevious && !isLoading,
                canGoNext = !isLoading,
                onPreviousDay = {
                    if (canGoPrevious && onPreviousDay != null) {
                        coroutineScope.launch {
                            isLoading = true
                            try {
                                val prevDate = currentDate.minusDays(1)
                                val newSlots = onPreviousDay(prevDate)
                                currentDate = prevDate
                                currentSlots = newSlots
                                selectedIndex = null
                            } finally {
                                isLoading = false
                            }
                        }
                    }
                },
                onNextDay = {
                    if (onNextDay != null) {
                        coroutineScope.launch {
                            isLoading = true
                            try {
                                val nextDate = currentDate.plusDays(1)
                                val newSlots = onNextDay(nextDate)
                                currentDate = nextDate
                                currentSlots = newSlots
                                selectedIndex = null
                            } finally {
                                isLoading = false
                            }
                        }
                    }
                },
                colors = colors
            )

            Divider(color = colors.onSurface.copy(alpha = 0.08f), thickness = 1.dp)

            // Slot List or Grid
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 200.dp, max = 360.dp)
            ) {
                if (viewMode == SlotPickerViewMode.Compact) {
                    CompactSlotGrid(
                        slots = currentSlots,
                        selectedIndex = selectedIndex,
                        onSelectSlot = { selectedIndex = it },
                        colors = colors
                    )
                } else {
                    ExpandedSlotList(
                        slots = currentSlots,
                        selectedIndex = selectedIndex,
                        onSelectSlot = { selectedIndex = it },
                        colors = colors
                    )
                }
            }

            Divider(color = colors.onSurface.copy(alpha = 0.08f), thickness = 1.dp)

            // Sheet Legend
            SheetLegend(colors = colors)

            Divider(color = colors.onSurface.copy(alpha = 0.08f), thickness = 1.dp)

            // Sheet Footer: Cancel & Confirm buttons
            SheetFooter(
                hasSelection = selectedIndex != null,
                isLoading = isLoading,
                onCancel = {
                    coroutineScope.launch { sheetState.hide() }
                    onDismiss()
                },
                onConfirm = {
                    if (selectedIndex != null) {
                        val chosen = currentSlots[selectedIndex!!]
                        onConfirm?.invoke(chosen)
                        coroutineScope.launch { sheetState.hide() }
                        onDismiss()
                    }
                },
                colors = colors
            )
        }
    }
}

// ─── Sheet Header ───────────────────────────────────────────────────────────

@Composable
private fun SheetHeader(
    dateLabel: String,
    isToday: Boolean,
    canGoPrevious: Boolean,
    canGoNext: Boolean,
    onPreviousDay: () -> Unit,
    onNextDay: () -> Unit,
    colors: Ux4gTimeslotColors
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            NavArrowButton(
                icon = Ux4gIcons.arrowLeft,
                disabled = !canGoPrevious,
                onClick = onPreviousDay,
                colors = colors
            )

            Text(
                text = dateLabel,
                style = defaultTypography.tM_strong.copy(
                    fontWeight = FontWeight.Bold,
                    color = colors.onSurface
                )
            )

            NavArrowButton(
                icon = Ux4gIcons.arrowRight,
                disabled = !canGoNext,
                onClick = onNextDay,
                colors = colors
            )
        }

        if (isToday) {
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = "Today",
                style = defaultTypography.lS_strong.copy(
                    fontWeight = FontWeight.Bold,
                    color = colors.primary
                )
            )
        }
    }
}

// ─── Expanded Slot List ─────────────────────────────────────────────────────

@Composable
private fun ExpandedSlotList(
    slots: List<SlotTimeEntry>,
    selectedIndex: Int?,
    onSelectSlot: (Int) -> Unit,
    colors: Ux4gTimeslotColors
) {
    if (slots.isEmpty()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "No slots available",
                style = defaultTypography.bS_default.copy(color = colors.disabled)
            )
        }
        return
    }

    LazyColumn(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(vertical = 8.dp)
    ) {
        itemsIndexed(slots) { index, slot ->
            val isSelected = selectedIndex == index
            val isNoSlots = slot.status == SlotTimeStatus.NoSlots

            if (isNoSlots) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .background(colors.onSurface.copy(alpha = 0.04f)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No slots available",
                        style = defaultTypography.bS_default.copy(color = colors.disabled)
                    )
                }
            } else {
                val animatedBg by animateColorAsState(
                    targetValue = if (isSelected) colors.primary.copy(alpha = 0.08f) else colors.surface,
                    animationSpec = tween(150),
                    label = "slot_bg"
                )
                val animatedBorder by animateColorAsState(
                    targetValue = if (isSelected) colors.primary else colors.border,
                    animationSpec = tween(150),
                    label = "slot_border"
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .padding(horizontal = 16.dp, vertical = 4.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(animatedBg)
                        .border(if (isSelected) 1.5.dp else 1.dp, animatedBorder, RoundedCornerShape(8.dp))
                        .clickable { onSelectSlot(index) },
                    contentAlignment = Alignment.Center
                ) {
                    if (isSelected) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Ux4gIconComponent(
                                icon = Ux4gIcons.Check,
                                contentDescription = "Selected",
                                tint = colors.primary,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Selected",
                                style = defaultTypography.bS_strong.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = colors.primary
                                )
                            )
                        }
                    } else {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = slot.time,
                                style = defaultTypography.bS_strong.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = colors.onSurface
                                )
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "${slot.slotCount} slots",
                                style = defaultTypography.lS_default.copy(
                                    color = if (slot.status == SlotTimeStatus.Limited) colors.warning else colors.onSurface.copy(alpha = 0.38f)
                                )
                            )
                        }
                    }
                }
            }

            if (index < slots.size - 1) {
                Divider(color = colors.onSurface.copy(alpha = 0.08f), thickness = 1.dp)
            }
        }
    }
}

// ─── Compact Slot Grid ──────────────────────────────────────────────────────

@Composable
private fun CompactSlotGrid(
    slots: List<SlotTimeEntry>,
    selectedIndex: Int?,
    onSelectSlot: (Int) -> Unit,
    colors: Ux4gTimeslotColors
) {
    val rows = remember(slots) {
        val list = mutableListOf<List<Pair<Int, SlotTimeEntry>>>()
        var i = 0
        while (i < slots.size) {
            val row = mutableListOf<Pair<Int, SlotTimeEntry>>()
            row.add(Pair(i, slots[i]))
            if (i + 1 < slots.size) {
                row.add(Pair(i + 1, slots[i + 1]))
            }
            list.add(row)
            i += 2
        }
        list
    }

    LazyColumn(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(vertical = 8.dp)
    ) {
        itemsIndexed(rows) { rowIndex, row ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Min),
                verticalAlignment = Alignment.CenterVertically
            ) {
                for (cellIndex in row.indices) {
                    val (index, slot) = row[cellIndex]
                    val isSelected = selectedIndex == index
                    val isNoSlots = slot.status == SlotTimeStatus.NoSlots

                    if (cellIndex > 0) {
                        Box(
                            modifier = Modifier
                                .fillMaxHeight()
                                .width(1.dp)
                                .background(colors.onSurface.copy(alpha = 0.08f))
                        )
                    }

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .background(if (isSelected) colors.primary.copy(alpha = 0.08f) else Color.Transparent)
                            .clickable(enabled = !isNoSlots) { onSelectSlot(index) }
                            .padding(horizontal = 12.dp, vertical = 14.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = slot.time,
                                style = defaultTypography.bS_default.copy(
                                    fontWeight = if (isSelected) FontWeight.W600 else FontWeight.W400,
                                    color = when {
                                        isNoSlots -> colors.onSurface.copy(alpha = 0.38f)
                                        isSelected -> colors.primary
                                        else -> colors.onSurface
                                    }
                                )
                            )
                            Spacer(modifier = Modifier.width(6.dp))

                            // Circular Badge
                            CompactSlotBadge(
                                slot = slot,
                                isSelected = isSelected,
                                colors = colors
                            )
                        }
                    }
                }

                // Fill empty column if odd item in row
                if (row.size == 1) {
                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .width(1.dp)
                            .background(colors.onSurface.copy(alpha = 0.08f))
                    )
                    Spacer(modifier = Modifier.weight(1f))
                }
            }

            if (rowIndex < rows.size - 1) {
                Divider(color = colors.onSurface.copy(alpha = 0.08f), thickness = 1.dp)
            }
        }
    }
}

@Composable
private fun CompactSlotBadge(
    slot: SlotTimeEntry,
    isSelected: Boolean,
    colors: Ux4gTimeslotColors
) {
    if (slot.status == SlotTimeStatus.NoSlots) {
        Box(
            modifier = Modifier
                .size(22.dp)
                .clip(CircleShape)
                .background(colors.onSurface.copy(alpha = 0.12f)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "0",
                style = defaultTypography.lS_strong.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 11.sp,
                    color = colors.onSurface.copy(alpha = 0.38f)
                )
            )
        }
        return
    }

    val badgeColor = when {
        isSelected -> colors.primary
        slot.status == SlotTimeStatus.Limited -> colors.warning
        else -> colors.success
    }

    Box(
        modifier = Modifier
            .size(22.dp)
            .clip(CircleShape)
            .background(badgeColor),
        contentAlignment = Alignment.Center
    ) {
        if (isSelected) {
            Ux4gIconComponent(
                icon = Ux4gIcons.Check,
                contentDescription = null,
                tint = colors.onPrimary,
                modifier = Modifier.size(13.dp)
            )
        } else {
            Text(
                text = "${slot.slotCount}",
                style = defaultTypography.lS_strong.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 11.sp,
                    color = colors.onPrimary
                )
            )
        }
    }
}

// ─── Sheet Legend ───────────────────────────────────────────────────────────

@Composable
private fun SheetLegend(colors: Ux4gTimeslotColors) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 10.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            SheetLegendItem(
                boxColor = colors.surface,
                borderColor = colors.onSurface.copy(alpha = 0.38f),
                label = "Available",
                colors = colors
            )
            SheetLegendItem(
                boxColor = colors.primary.copy(alpha = 0.08f),
                borderColor = colors.primary,
                icon = Ux4gIcons.Check,
                iconTint = colors.primary,
                label = "Selected",
                colors = colors
            )
            SheetLegendItem(
                boxColor = colors.onSurface.copy(alpha = 0.04f),
                borderColor = colors.onSurface.copy(alpha = 0.12f),
                label = "No slots",
                colors = colors
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            SheetLegendItem(
                boxColor = colors.warning.copy(alpha = 0.08f),
                borderColor = colors.warning,
                label = "Limited slots",
                colors = colors
            )
            SheetLegendItem(
                boxColor = colors.warning.copy(alpha = 0.08f),
                borderColor = colors.warning,
                label = "Public holiday",
                colors = colors
            )
            SheetLegendItem(
                boxColor = colors.onSurface.copy(alpha = 0.08f),
                borderColor = colors.onSurface.copy(alpha = 0.12f),
                label = "Weekly off",
                colors = colors
            )
        }
    }
}

@Composable
private fun SheetLegendItem(
    boxColor: Color,
    borderColor: Color,
    label: String,
    colors: Ux4gTimeslotColors,
    icon: com.ux4g.designsystemv1.foundation.Ux4gIcon? = null,
    iconTint: Color? = null
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Box(
            modifier = Modifier
                .size(14.dp)
                .clip(RoundedCornerShape(3.dp))
                .background(boxColor)
                .border(1.dp, borderColor, RoundedCornerShape(3.dp)),
            contentAlignment = Alignment.Center
        ) {
            if (icon != null) {
                Ux4gIconComponent(
                    icon = icon,
                    contentDescription = null,
                    tint = iconTint ?: colors.primary,
                    modifier = Modifier.size(10.dp)
                )
            }
        }
        Text(
            text = label,
            style = defaultTypography.lS_default.copy(
                fontSize = 11.sp,
                color = colors.onSurface.copy(alpha = 0.6f)
            )
        )
    }
}

// ─── Sheet Footer ───────────────────────────────────────────────────────────

@Composable
private fun SheetFooter(
    hasSelection: Boolean,
    isLoading: Boolean,
    onCancel: () -> Unit,
    onConfirm: () -> Unit,
    colors: Ux4gTimeslotColors
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        OutlinedButton(
            onClick = onCancel,
            enabled = !isLoading,
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier
                .weight(1f)
                .height(48.dp)
        ) {
            Text(
                text = "Cancel",
                style = defaultTypography.bS_strong.copy(
                    fontWeight = FontWeight.Bold,
                    color = if (isLoading) colors.disabled else colors.onSurface
                )
            )
        }

        Button(
            onClick = onConfirm,
            enabled = hasSelection && !isLoading,
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = colors.primary,
                disabledContainerColor = colors.onSurface.copy(alpha = 0.12f)
            ),
            modifier = Modifier
                .weight(1f)
                .height(48.dp)
        ) {
            Text(
                text = "Confirm",
                style = defaultTypography.bS_strong.copy(
                    fontWeight = FontWeight.Bold,
                    color = if (hasSelection && !isLoading) colors.onPrimary else colors.disabled
                )
            )
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Previews
// ─────────────────────────────────────────────────────────────────────────────

@Preview(name = "Timeslot Calendar Preview", showBackground = true)
@Composable
fun Ux4gTimeslotPreview() {
    val today = LocalDate.of(2026, 4, 15)
    val data = Ux4gTimeslotData(
        year = 2026,
        month = 4,
        selectedDate = LocalDate.of(2026, 4, 23),
        today = today,
        weeklyOffWeekdays = listOf(6, 7),
        dates = listOf(
            SlotDateEntry(LocalDate.of(2026, 4, 9), SlotDateStatus.PublicHoliday),
            SlotDateEntry(LocalDate.of(2026, 4, 21), SlotDateStatus.NoSlots),
            SlotDateEntry(LocalDate.of(2026, 4, 5), SlotDateStatus.WeeklyOff)
        )
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Ux4gTimeslot(
            data = data,
            timeSlotProvider = {
                listOf(
                    SlotTimeEntry("09:00 AM - 10:00 AM", 8, SlotTimeStatus.Available),
                    SlotTimeEntry("10:00 AM - 11:00 AM", 2, SlotTimeStatus.Limited),
                    SlotTimeEntry("11:00 AM - 12:00 PM", 0, SlotTimeStatus.NoSlots),
                    SlotTimeEntry("02:00 PM - 03:00 PM", 5, SlotTimeStatus.Available)
                )
            }
        )
    }
}
