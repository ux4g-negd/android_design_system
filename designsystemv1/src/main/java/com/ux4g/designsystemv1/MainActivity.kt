package com.ux4g.designsystemv1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ux4g.designsystemv1.components.buttons.Ux4gButton
import com.ux4g.designsystemv1.components.Ux4gCard
import com.ux4g.designsystemv1.components.Ux4gLoader
import com.ux4g.designsystemv1.components.buttons.Ux4gTextButton
import com.ux4g.designsystemv1.components.Ux4gToastCategory
import com.ux4g.designsystemv1.components.Ux4gToastHost
import com.ux4g.designsystemv1.components.Ux4gToastHostState
import com.ux4g.designsystemv1.components.searchfield.*
import com.ux4g.designsystemv1.foundation.Ux4gIconComponent
import com.ux4g.designsystemv1.foundation.Ux4gIcons
import com.ux4g.designsystemv1.foundation.Ux4gColors
import com.ux4g.designsystemv1.theme.Ux4gTheme
import com.ux4g.designsystemv1.components.Ux4gSwitch
import com.ux4g.designsystemv1.components.Ux4gSwitchSize
import com.ux4g.designsystemv1.components.Ux4gSwitchLabelPosition
import com.ux4g.designsystemv1.components.Ux4gToggle
import com.ux4g.designsystemv1.components.Ux4gStatusPipeline
import com.ux4g.designsystemv1.components.Ux4gPipelineStep
import com.ux4g.designsystemv1.components.Ux4gPipelineStepState
import com.ux4g.designsystemv1.components.Ux4gPipelineOrientation
import com.ux4g.designsystemv1.components.Ux4gPipelineSize
import com.ux4g.designsystemv1.components.Ux4gTimePicker
import com.ux4g.designsystemv1.components.Ux4gTimeOfDay
import com.ux4g.designsystemv1.components.Ux4gPaginationIndicator
import com.ux4g.designsystemv1.components.Ux4gPaginationVariant
import com.ux4g.designsystemv1.components.steppers.*
import com.ux4g.designsystemv1.components.timeslot.*
import java.time.LocalDate
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            // Use our custom theme
        Ux4gTheme {
            val toastHostState = remember { Ux4gToastHostState() }
            Scaffold(
                modifier = Modifier.fillMaxSize(),
                containerColor = Ux4gTheme.colorScheme.background, // Background color fix
                topBar = {
                    Ux4gToastHost(hostState = toastHostState)
                }
            ) { innerPadding ->
                DesignSystemShowcase(
                    toastHostState = toastHostState,
                    modifier = Modifier.padding(innerPadding)
                )
            }
        }
        }
    }
}

@Composable
fun DesignSystemShowcase(
    toastHostState: Ux4gToastHostState = remember { Ux4gToastHostState() },
    modifier: Modifier = Modifier
) {
    val scope = rememberCoroutineScope()
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Typography usage
        Text(
            text = "Design System Showcase",
            style = Ux4gTheme.typography.hS_default,
            color = Ux4gTheme.colorScheme.onBackground
        )

        Text(
            text = "This screen demonstrates the use of our custom design system architecture: colors, typography, icons, and components.",
            style = Ux4gTheme.typography.bS_default,
            color = Ux4gTheme.colorScheme.onBackground
        )

        // Toast Showcase
        Text(
            text = "Toasts",
            style = Ux4gTheme.typography.hL_default,
            color = Ux4gTheme.colorScheme.onBackground
        )
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Ux4gButton(onClick = {
                scope.launch {
                    toastHostState.showToast(
                        category = Ux4gToastCategory.Success,
                        title = "Success",
                        subtitle = "Your changes have been saved.",
                        actionText = "Undo",
                        onActionClick = { toastHostState.dismiss() }
                    )
                }
            }) {
                Text("Show Success")
            }

            Ux4gButton(onClick = {
                scope.launch {
                    toastHostState.showToast(
                        category = Ux4gToastCategory.Error,
                        title = "Error",
                        subtitle = "Failed to upload file.",
                        autoClose = false // Interactive only
                    )
                }
            }) {
                Text("Show Persistent Error")
            }
        }

        // Buttons & Icons
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Ux4gButton(onClick = { /* TODO */ }) {
                Ux4gIconComponent(
                    icon = Ux4gIcons.Check,
                    contentDescription = null,
                    modifier = Modifier.padding(end = 8.dp)
                )
                Text("Primary Action")
            }

            Ux4gTextButton(onClick = { /* TODO */ }) {
                Ux4gIconComponent(
                    icon = Ux4gIcons.Settings,
                    contentDescription = null,
                    modifier = Modifier.padding(end = 8.dp)
                )
                Text("Settings")
            }
        }

        // Loaders showcase
        Text(
            text = "Loaders",
            style = Ux4gTheme.typography.hL_default,
            color = Ux4gTheme.colorScheme.onBackground
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Ux4gLoader(size = 48.dp, percentage = 90f, color = Ux4gColors.primary)
            Ux4gLoader(size = 40.dp, percentage = 75f, color = Ux4gColors.secondary)
            Ux4gLoader(size = 32.dp, percentage = 50f, color = Ux4gColors.tertiary)
            Ux4gLoader(size = 24.dp, percentage = 25f, color = Ux4gColors.red)
        }

        // Pagination showcase
        Text(
            text = "Pagination Indicators",
            style = Ux4gTheme.typography.hL_default,
            color = Ux4gTheme.colorScheme.onBackground
        )

        var pageIndex by remember { mutableIntStateOf(1) }
        
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Ux4gPaginationIndicator(
                totalPageCount = 7,
                currentPageIndex = pageIndex,
                onPageChange = { pageIndex = it }
            )
            
            Ux4gPaginationIndicator(
                totalPageCount = 7,
                currentPageIndex = pageIndex,
                onPageChange = { pageIndex = it },
                variant = Ux4gPaginationVariant.Capsule
            )
            
            Ux4gPaginationIndicator(
                totalPageCount = 7,
                currentPageIndex = pageIndex,
                onPageChange = { pageIndex = it },
                showArrows = false
            )
            
            Ux4gPaginationIndicator(
                totalPageCount = 7,
                currentPageIndex = pageIndex,
                onPageChange = { pageIndex = it },
                showArrows = false,
                variant = Ux4gPaginationVariant.Capsule
            )
        }

        // Search Field Showcase
        Text(
            text = "Search Fields",
            style = Ux4gTheme.typography.hL_default,
            color = Ux4gTheme.colorScheme.onBackground
        )

        var searchVal1 by remember { mutableStateOf("") }
        var searchVal2 by remember { mutableStateOf("Android Compose") }
        var searchVal3 by remember { mutableStateOf("Ban") }

        Ux4gSearchField(
            value = searchVal1,
            onValueChange = { searchVal1 = it },
            label = "Basic Search",
            placeholder = "Search for components...",
            showVoiceIcon = true,
            showClearIcon = true
        )

        Ux4gSearchField(
            value = searchVal2,
            onValueChange = { searchVal2 = it },
            variant = Ux4gSearchFieldVariant.SearchWithSubmit,
            buttonStyle = Ux4gSearchFieldButtonStyle.Filled,
            label = "Search with Submit",
            placeholder = "Search query...",
            onSubmitClick = { query ->
                scope.launch {
                    toastHostState.showToast(
                        category = Ux4gToastCategory.Success,
                        title = "Submitted",
                        subtitle = "Search: $query"
                    )
                }
            }
        )

        Ux4gSearchField(
            value = searchVal3,
            onValueChange = { searchVal3 = it },
            variant = Ux4gSearchFieldVariant.Autocomplete,
            filterType = Ux4gSearchFilterType.StartsWith,
            label = "Autocomplete (StartsWith)",
            placeholder = "Type fruit...",
            options = listOf("Apple", "Banana", "Bandung", "Blueberry", "Cherry", "Mango"),
            onOptionSelected = { searchVal3 = it }
        )

        // Switch / Toggle Showcase
        Text(
            text = "Switch / Toggle",
            style = Ux4gTheme.typography.hL_default,
            color = Ux4gTheme.colorScheme.onBackground
        )

        var switchState1 by remember { mutableStateOf(true) }
        var switchState2 by remember { mutableStateOf(false) }
        var switchState3 by remember { mutableStateOf(true) }
        var switchState4 by remember { mutableStateOf(true) }
        var switchState5 by remember { mutableStateOf(false) }

        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Ux4gSwitch(
                checked = switchState1,
                onCheckedChange = { switchState1 = it },
                label = "Enable Notifications",
                description = "Receive updates directly on your device",
                size = Ux4gSwitchSize.M,
                labelPosition = Ux4gSwitchLabelPosition.Right
            )

            Ux4gSwitch(
                checked = switchState2,
                onCheckedChange = { switchState2 = it },
                label = "Compact Mode (S)",
                description = "32x18 small size switch",
                size = Ux4gSwitchSize.S,
                labelPosition = Ux4gSwitchLabelPosition.Right
            )

            Ux4gSwitch(
                checked = switchState3,
                onCheckedChange = { switchState3 = it },
                label = "Prominent Mode (L)",
                description = "48x28 large size switch",
                size = Ux4gSwitchSize.L,
                labelPosition = Ux4gSwitchLabelPosition.Right
            )

            Ux4gSwitch(
                checked = switchState4,
                onCheckedChange = { switchState4 = it },
                label = "Monthly Billing",
                isRequired = true,
                secondaryLabel = "Annual Billing (Save 20%)",
                labelPosition = Ux4gSwitchLabelPosition.BothSides
            )

            Ux4gSwitch(
                checked = switchState5,
                onCheckedChange = { switchState5 = it },
                label = "Required Setting with Icon",
                isRequired = true,
                trailingIcon = Ux4gIcons.info_circle,
                description = "Click info icon for more details",
                labelPosition = Ux4gSwitchLabelPosition.Left
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Ux4gSwitch(
                    checked = false,
                    onCheckedChange = null,
                    enabled = false,
                    label = "Disabled Off",
                    labelPosition = Ux4gSwitchLabelPosition.Right
                )
                Ux4gSwitch(
                    checked = true,
                    onCheckedChange = null,
                    enabled = false,
                    label = "Disabled On",
                    labelPosition = Ux4gSwitchLabelPosition.Right
                )
            }
        }

        // Status Pipeline Showcase
        Text(
            text = "Status Pipeline",
            style = Ux4gTheme.typography.hL_default,
            color = Ux4gTheme.colorScheme.onBackground
        )

        // 1. Vertical Pipeline
        Text(
            text = "Vertical Pipeline (Size M)",
            style = Ux4gTheme.typography.lM_strong,
            color = Ux4gTheme.colorScheme.onBackground
        )
        Ux4gStatusPipeline(
            orientation = Ux4gPipelineOrientation.Vertical,
            size = Ux4gPipelineSize.M,
            currentStep = 1,
            steps = listOf(
                Ux4gPipelineStep(label = "Application Submitted", description = "5 Apr, 10:30 AM"),
                Ux4gPipelineStep(label = "Document Verification", description = "In progress"),
                Ux4gPipelineStep(label = "Field Inspection", description = "Est. 10 Apr"),
                Ux4gPipelineStep(label = "Final Decision", description = "Est. 18 Apr"),
                Ux4gPipelineStep(label = "Certificate Issued", description = "Est. 20 Apr")
            )
        )

        // 2. Horizontal Pipeline
        Text(
            text = "Horizontal Pipeline (Size M)",
            style = Ux4gTheme.typography.lM_strong,
            color = Ux4gTheme.colorScheme.onBackground
        )
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

        // 3. Mixed State Pipeline (Error & Warning)
        Text(
            text = "Status Pipeline with Error & Warning",
            style = Ux4gTheme.typography.lM_strong,
            color = Ux4gTheme.colorScheme.onBackground
        )
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
                    description = "Warning: Incomplete doc",
                    state = Ux4gPipelineStepState.Warning
                ),
                Ux4gPipelineStep(
                    label = "Bank Details",
                    description = "Failed: Invalid IFSC",
                    state = Ux4gPipelineStepState.Error
                ),
                Ux4gPipelineStep(
                    label = "Final Activation",
                    description = "Pending verification",
                    state = Ux4gPipelineStepState.Upcoming
                )
            )
        )

        // Time Picker Showcase
        Text(
            text = "Time Picker",
            style = Ux4gTheme.typography.hL_default,
            color = Ux4gTheme.colorScheme.onBackground
        )

        var timePickerVal1 by remember { mutableStateOf<Ux4gTimeOfDay?>(Ux4gTimeOfDay(10, 30)) }
        var timePickerVal2 by remember { mutableStateOf<Ux4gTimeOfDay?>(null) }

        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Ux4gTimePicker(
                selectedTime = timePickerVal1,
                onTimeSelected = {
                    timePickerVal1 = it
                    scope.launch {
                        toastHostState.showToast(
                            category = Ux4gToastCategory.Success,
                            title = "Time Selected",
                            subtitle = it.format()
                        )
                    }
                },
                label = "Meeting Start Time",
                description = "Choose a time in 12-hour format with AM/PM",
                isRequired = true
            )

            Ux4gTimePicker(
                selectedTime = timePickerVal2,
                onTimeSelected = { timePickerVal2 = it },
                label = "Reminder Interval (15-min intervals)",
                minuteInterval = 15,
                placeholder = "Pick reminder time"
            )
        }

        // Stepper Showcase
        Text(
            text = "Steppers",
            style = Ux4gTheme.typography.hL_default,
            color = Ux4gTheme.colorScheme.onBackground
        )

        var stepperCurrentStep by remember { mutableIntStateOf(2) }
        val stepperItems = listOf(
            Ux4gStepItem(title = "Account", description = "Create login"),
            Ux4gStepItem(title = "Personal Info", description = "Enter details"),
            Ux4gStepItem(title = "Verification", description = "Verify OTP"),
            Ux4gStepItem(title = "Payment", description = "Select plan")
        )

        // 1. Horizontal Stepper
        Text(
            text = "Horizontal Stepper (Center Line Placement)",
            style = Ux4gTheme.typography.lM_strong,
            color = Ux4gTheme.colorScheme.onBackground
        )
        Ux4gStepper(
            totalSteps = 4,
            currentStep = stepperCurrentStep,
            steps = stepperItems,
            lineStyle = StepperLineStyle.Solid
        )

        // 2. Horizontal Stepper (Bottom Line with Active Step Background)
        Text(
            text = "Horizontal Stepper (Bottom Line & Active Background)",
            style = Ux4gTheme.typography.lM_strong,
            color = Ux4gTheme.colorScheme.onBackground
        )
        Ux4gStepper(
            totalSteps = 4,
            currentStep = stepperCurrentStep,
            steps = stepperItems,
            linePlacement = StepperLinePlacement.Bottom,
            activeStepBackground = true
        )

        // 3. Compact Steppers (All Layouts)
        Text(
            text = "Compact Stepper (Linear Layout)",
            style = Ux4gTheme.typography.lM_strong,
            color = Ux4gTheme.colorScheme.onBackground
        )
        Ux4gCompactStepper(
            totalSteps = 4,
            currentStep = stepperCurrentStep,
            stepLabel = stepperItems.getOrNull(stepperCurrentStep - 1)?.title ?: "Step $stepperCurrentStep",
            description = stepperItems.getOrNull(stepperCurrentStep - 1)?.description,
            onPrevious = { if (stepperCurrentStep > 1) stepperCurrentStep-- },
            onNext = { if (stepperCurrentStep < 4) stepperCurrentStep++ },
            layout = Ux4gCompactStepperLayout.Linear
        )

        Text(
            text = "Compact Stepper (Right-Aligned Layout)",
            style = Ux4gTheme.typography.lM_strong,
            color = Ux4gTheme.colorScheme.onBackground
        )
        Ux4gCompactStepper(
            totalSteps = 4,
            currentStep = stepperCurrentStep,
            stepLabel = stepperItems.getOrNull(stepperCurrentStep - 1)?.title ?: "Step $stepperCurrentStep",
            description = stepperItems.getOrNull(stepperCurrentStep - 1)?.description,
            onPrevious = { if (stepperCurrentStep > 1) stepperCurrentStep-- },
            onNext = { if (stepperCurrentStep < 4) stepperCurrentStep++ },
            layout = Ux4gCompactStepperLayout.RightAligned
        )

        Text(
            text = "Compact Stepper (Centered Layout)",
            style = Ux4gTheme.typography.lM_strong,
            color = Ux4gTheme.colorScheme.onBackground
        )
        Ux4gCompactStepper(
            totalSteps = 4,
            currentStep = stepperCurrentStep,
            stepLabel = stepperItems.getOrNull(stepperCurrentStep - 1)?.title ?: "Step $stepperCurrentStep",
            description = stepperItems.getOrNull(stepperCurrentStep - 1)?.description,
            onPrevious = { if (stepperCurrentStep > 1) stepperCurrentStep-- },
            onNext = { if (stepperCurrentStep < 4) stepperCurrentStep++ },
            layout = Ux4gCompactStepperLayout.Centered
        )

        Text(
            text = "Compact Stepper (Split Layout)",
            style = Ux4gTheme.typography.lM_strong,
            color = Ux4gTheme.colorScheme.onBackground
        )
        Ux4gCompactStepper(
            totalSteps = 4,
            currentStep = stepperCurrentStep,
            stepLabel = stepperItems.getOrNull(stepperCurrentStep - 1)?.title ?: "Step $stepperCurrentStep",
            description = stepperItems.getOrNull(stepperCurrentStep - 1)?.description,
            onPrevious = { if (stepperCurrentStep > 1) stepperCurrentStep-- },
            onNext = { if (stepperCurrentStep < 4) stepperCurrentStep++ },
            layout = Ux4gCompactStepperLayout.Split
        )

        // Timeslot / Slot Grid Showcase
        Text(
            text = "Timeslot & Slot Grid",
            style = Ux4gTheme.typography.hL_default,
            color = Ux4gTheme.colorScheme.onBackground
        )

        // 1. Compact View Mode (2-Column Grid Sheet)
        Text(
            text = "Timeslot (Compact 2-Column Grid Mode)",
            style = Ux4gTheme.typography.lM_strong,
            color = Ux4gTheme.colorScheme.onBackground
        )
        val compactTimeslotData = remember {
            Ux4gTimeslotData(
                year = 2026,
                month = 4,
                selectedDate = LocalDate.of(2026, 4, 23),
                today = LocalDate.of(2026, 4, 15),
                weeklyOffWeekdays = listOf(6, 7),
                dates = listOf(
                    SlotDateEntry(LocalDate.of(2026, 4, 9), SlotDateStatus.PublicHoliday),
                    SlotDateEntry(LocalDate.of(2026, 4, 21), SlotDateStatus.NoSlots),
                    SlotDateEntry(LocalDate.of(2026, 4, 5), SlotDateStatus.WeeklyOff)
                ),
                viewMode = SlotPickerViewMode.Compact
            )
        }
        Ux4gTimeslot(
            data = compactTimeslotData,
            timeSlotProvider = { date ->
                listOf(
                    SlotTimeEntry("09:00 AM - 10:00 AM", 8, SlotTimeStatus.Available),
                    SlotTimeEntry("10:00 AM - 11:00 AM", 2, SlotTimeStatus.Limited),
                    SlotTimeEntry("11:00 AM - 12:00 PM", 0, SlotTimeStatus.NoSlots),
                    SlotTimeEntry("02:00 PM - 03:00 PM", 5, SlotTimeStatus.Available),
                    SlotTimeEntry("03:00 PM - 04:00 PM", 1, SlotTimeStatus.Limited),
                    SlotTimeEntry("04:00 PM - 05:00 PM", 6, SlotTimeStatus.Available)
                )
            },
            onSlotConfirmed = { date, slot ->
                scope.launch {
                    toastHostState.showToast(
                        category = Ux4gToastCategory.Success,
                        title = "Slot Confirmed",
                        subtitle = "${date} at ${slot.time}"
                    )
                }
            }
        )

        // 2. Expanded View Mode (1-Column List Sheet)
        Text(
            text = "Timeslot (Expanded 1-Column List Mode)",
            style = Ux4gTheme.typography.lM_strong,
            color = Ux4gTheme.colorScheme.onBackground
        )
        val expandedTimeslotData = remember {
            Ux4gTimeslotData(
                year = 2026,
                month = 4,
                selectedDate = LocalDate.of(2026, 4, 18),
                today = LocalDate.of(2026, 4, 15),
                weeklyOffWeekdays = listOf(6, 7),
                dates = listOf(
                    SlotDateEntry(LocalDate.of(2026, 4, 9), SlotDateStatus.PublicHoliday),
                    SlotDateEntry(LocalDate.of(2026, 4, 21), SlotDateStatus.NoSlots),
                    SlotDateEntry(LocalDate.of(2026, 4, 5), SlotDateStatus.WeeklyOff)
                ),
                viewMode = SlotPickerViewMode.Expanded
            )
        }
        Ux4gTimeslot(
            data = expandedTimeslotData,
            timeSlotProvider = { date ->
                listOf(
                    SlotTimeEntry("09:00 AM - 10:00 AM", 8, SlotTimeStatus.Available),
                    SlotTimeEntry("10:00 AM - 11:00 AM", 2, SlotTimeStatus.Limited),
                    SlotTimeEntry("11:00 AM - 12:00 PM", 0, SlotTimeStatus.NoSlots),
                    SlotTimeEntry("02:00 PM - 03:00 PM", 5, SlotTimeStatus.Available),
                    SlotTimeEntry("03:00 PM - 04:00 PM", 1, SlotTimeStatus.Limited),
                    SlotTimeEntry("04:00 PM - 05:00 PM", 6, SlotTimeStatus.Available)
                )
            },
            onSlotConfirmed = { date, slot ->
                scope.launch {
                    toastHostState.showToast(
                        category = Ux4gToastCategory.Success,
                        title = "Slot Confirmed",
                        subtitle = "${date} at ${slot.time}"
                    )
                }
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    Ux4gTheme { DesignSystemShowcase() }
}

