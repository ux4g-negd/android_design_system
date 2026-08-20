package com.ux4g.designsystemv1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FaceRetouchingNatural
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.ux4g.designsystemv1.foundation.defaultTypography
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
import com.ux4g.designsystemv1.components.*
import com.ux4g.designsystemv1.components.steppers.*
import com.ux4g.designsystemv1.components.timeslot.*
import com.ux4g.designsystemv1.components.biometric.*
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

        // =========================================================================
        // Result List & Result Row Showcase (Matching Flutter Widgetbook 1:1)
        // =========================================================================
        Text(
            text = "Result List & Result Row",
            style = Ux4gTheme.typography.hL_default,
            color = Ux4gTheme.colorScheme.onBackground
        )

        // ─── 1. Visual Showcase (4 Core Variants) ───────────────────────────
        Text(
            text = "1. Visual Showcase (Core Variants)",
            style = Ux4gTheme.typography.lM_strong,
            color = Ux4gTheme.colorScheme.onBackground
        )

        // Variant 1: Approved Result Row with Details
        Ux4gCard(
            modifier = Modifier.fillMaxWidth(),
            cornerRadius = 12.dp,
            borderColor = Ux4gColors.neutral900.copy(alpha = 0.12f),
            borderWidth = 1.dp
        ) {
            Ux4gResultList(
                title = "Application #2024-001",
                statusTag = "Approved",
                tagColorScheme = Ux4gTagColor.Success,
                actionButtonText = "View Details",
                onActionPressed = {
                    scope.launch {
                        toastHostState.showToast(
                            category = Ux4gToastCategory.Success,
                            title = "Viewing Details",
                            subtitle = "Application #2024-001"
                        )
                    }
                },
                details = listOf(
                    Ux4gResultDetail(label = "Applicant", value = "Ravi Kumar"),
                    Ux4gResultDetail(label = "Date", value = "12 May 2025"),
                    Ux4gResultDetail(label = "Sanctioned Amount", value = "₹1,20,000", isBold = true)
                ),
                showBottomDivider = false
            )
        }

        // Variant 2: Card Style with Metadata Pills (Under Review)
        Ux4gCard(
            modifier = Modifier.fillMaxWidth(),
            cornerRadius = 12.dp,
            borderColor = Ux4gColors.neutral900.copy(alpha = 0.12f),
            borderWidth = 1.dp
        ) {
            Ux4gResultRow(
                title = "Income Certificate Application",
                metadataSegments = listOf(
                    Ux4gPillSegment(
                        text = "8 days left",
                        leading = {
                            Box(
                                modifier = Modifier
                                    .size(6.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFFFA8C16))
                            )
                        }
                    ),
                    Ux4gPillSegment(
                        text = "Under Review",
                        textColor = Color(0xFFFA8C16)
                    )
                ),
                actionButtonText = "Track Status",
                onActionPressed = {
                    scope.launch {
                        toastHostState.showToast(
                            category = Ux4gToastCategory.Info,
                            title = "Tracking Application",
                            subtitle = "Income Certificate Application"
                        )
                    }
                },
                details = listOf(
                    Ux4gResultDetail(label = "Reference Number", value = "INC-2026-MH-04127"),
                    Ux4gResultDetail(label = "Last Updated Date", value = "10 Apr 2026"),
                    Ux4gResultDetail(label = "Assigned Officer", value = "Rahul Sharma"),
                    Ux4gResultDetail(label = "Department", value = "Revenue Department")
                ),
                showBottomDivider = false
            )
        }

        // Variant 3: Action Required with Icon Button (Upload Docs)
        Ux4gCard(
            modifier = Modifier.fillMaxWidth(),
            cornerRadius = 12.dp,
            borderColor = Ux4gColors.neutral900.copy(alpha = 0.12f),
            borderWidth = 1.dp
        ) {
            Ux4gResultList(
                title = "Driving License Renewal",
                statusTag = "Action Required",
                tagColorScheme = Ux4gTagColor.Warning,
                actionButtonText = "Upload Docs",
                actionButtonIcon = Ux4gIcons.upload,
                onActionPressed = {
                    scope.launch {
                        toastHostState.showToast(
                            category = Ux4gToastCategory.Warning,
                            title = "Upload Docs",
                            subtitle = "Opening document upload dialog..."
                        )
                    }
                },
                details = listOf(
                    Ux4gResultDetail(label = "Applicant", value = "Sunita Sharma"),
                    Ux4gResultDetail(label = "Application ID", value = "DL-99201-DL"),
                    Ux4gResultDetail(label = "Pending Stage", value = "Biometrics Verification")
                ),
                showBottomDivider = false
            )
        }

        // Variant 4: Rejected State with Custom Expanded Grievance Child
        Ux4gCard(
            modifier = Modifier.fillMaxWidth(),
            cornerRadius = 12.dp,
            borderColor = Ux4gColors.neutral900.copy(alpha = 0.12f),
            borderWidth = 1.dp
        ) {
            Ux4gResultRow(
                title = "Ration Card Transfer Request",
                statusTag = "Rejected",
                tagColorScheme = Ux4gTagColor.Error,
                actionButtonText = "Re-apply",
                onActionPressed = {
                    scope.launch {
                        toastHostState.showToast(
                            category = Ux4gToastCategory.Error,
                            title = "Re-apply Initiated",
                            subtitle = "Starting new Ration Card Transfer request..."
                        )
                    }
                },
                details = listOf(
                    Ux4gResultDetail(label = "Rejection Reason", value = "Address Proof Mismatch", isBold = true),
                    Ux4gResultDetail(label = "Applied Date", value = "05 Feb 2026")
                ),
                expandedChild = {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 4.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = "Need Help?",
                            style = defaultTypography.bS_default.copy(
                                color = Color(0xFF9E3A3A)
                            )
                        )
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.clickable {
                                scope.launch {
                                    toastHostState.showToast(
                                        category = Ux4gToastCategory.Info,
                                        title = "Grievance Portal",
                                        subtitle = "Register grievance portal opened."
                                    )
                                }
                            }
                        ) {
                            Text(
                                text = "Register grievance",
                                style = defaultTypography.bM_strong.copy(
                                    fontWeight = FontWeight.SemiBold,
                                    color = Ux4gColors.primary
                                )
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Ux4gIconComponent(
                                icon = Ux4gIcons.arrowRight,
                                contentDescription = null,
                                tint = Ux4gColors.primary,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                },
                showBottomDivider = false
            )
        }

        // ─── 2. Certificate Download Story (Title Trailing Icon & Download CTA) ─
        Text(
            text = "2. Certificate Download (Verified Trailing Icon)",
            style = Ux4gTheme.typography.lM_strong,
            color = Ux4gTheme.colorScheme.onBackground
        )
        Ux4gCard(
            modifier = Modifier.fillMaxWidth(),
            cornerRadius = 12.dp,
            borderColor = Ux4gColors.neutral900.copy(alpha = 0.12f),
            borderWidth = 1.dp
        ) {
            Ux4gResultRow(
                title = "Income Certificate",
                titleTrailing = {
                    Ux4gIconComponent(
                        icon = Ux4gIcons.success_circle,
                        contentDescription = "Verified",
                        tint = Ux4gColors.green600,
                        modifier = Modifier.size(18.dp)
                    )
                },
                actionButtonText = "Download",
                actionButtonIcon = Ux4gIcons.upload,
                onActionPressed = {
                    scope.launch {
                        toastHostState.showToast(
                            category = Ux4gToastCategory.Success,
                            title = "Downloading Certificate",
                            subtitle = "Income Certificate #INC-2026-MH-04127"
                        )
                    }
                },
                details = listOf(
                    Ux4gResultDetail(label = "Issued", value = "05 Apr 2026"),
                    Ux4gResultDetail(label = "Valid till", value = "05 Apr 2027"),
                    Ux4gResultDetail(label = "Reference Number", value = "INC-2026-MH-04127"),
                    Ux4gResultDetail(label = "Last Updated Date", value = "10 Apr 2026"),
                    Ux4gResultDetail(label = "Submitted Date", value = "1 Apr 2026"),
                    Ux4gResultDetail(label = "Assigned Officer", value = "Rahul Sharma"),
                    Ux4gResultDetail(label = "Department", value = "Revenue Department"),
                    Ux4gResultDetail(label = "Documents", value = "ID Proof, Address Proof")
                ),
                showBottomDivider = false
            )
        }

        // ─── 3. Service Apply Story (Custom Inline Metadata & Price) ─────────
        Text(
            text = "3. Service Apply (Custom Inline Fee & Duration Metadata)",
            style = Ux4gTheme.typography.lM_strong,
            color = Ux4gTheme.colorScheme.onBackground
        )
        Ux4gCard(
            modifier = Modifier.fillMaxWidth(),
            cornerRadius = 12.dp,
            borderColor = Ux4gColors.neutral900.copy(alpha = 0.12f),
            borderWidth = 1.dp
        ) {
            Ux4gResultRow(
                title = "Domicile Certificate",
                statusTag = "Paid",
                tagColorScheme = Ux4gTagColor.Neutral,
                customMetadata = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "₹ 120/-",
                            style = defaultTypography.bS_strong.copy(
                                fontWeight = FontWeight.SemiBold,
                                color = Ux4gColors.neutral800
                            )
                        )
                        Box(
                            modifier = Modifier
                                .size(4.dp)
                                .clip(CircleShape)
                                .background(Ux4gColors.neutral900.copy(alpha = 0.38f))
                        )
                        Text(
                            text = "20 mins",
                            style = defaultTypography.bS_default.copy(
                                color = Ux4gColors.neutral900.copy(alpha = 0.6f)
                            )
                        )
                    }
                },
                actionButtonText = "Apply",
                onActionPressed = {
                    scope.launch {
                        toastHostState.showToast(
                            category = Ux4gToastCategory.Info,
                            title = "Service Application",
                            subtitle = "Starting application for Domicile Certificate..."
                        )
                    }
                },
                details = listOf(
                    Ux4gResultDetail(label = "Reference Number", value = "DOM-2026-DL-88319"),
                    Ux4gResultDetail(label = "Processing Time", value = "7 Working Days"),
                    Ux4gResultDetail(label = "Fee Paid", value = "₹ 120 (Online)", isBold = true),
                    Ux4gResultDetail(label = "Required Docs", value = "Aadhaar, Electricity Bill")
                ),
                showBottomDivider = false
            )
        }

        // ─── 4. SLA Escalated Story (Action Leading Icon & Pill Tag Detail) ───
        Text(
            text = "4. SLA Escalated (Action Alert Icon & Overdue Pill Status)",
            style = Ux4gTheme.typography.lM_strong,
            color = Ux4gTheme.colorScheme.onBackground
        )
        Ux4gCard(
            modifier = Modifier.fillMaxWidth(),
            cornerRadius = 12.dp,
            borderColor = Ux4gColors.neutral900.copy(alpha = 0.12f),
            borderWidth = 1.dp
        ) {
            Ux4gResultRow(
                title = "Electricity Connection Grievance",
                statusTag = "Escalated",
                tagColorScheme = Ux4gTagColor.Brand,
                actionLeading = {
                    Ux4gIconComponent(
                        icon = Ux4gIcons.warning,
                        contentDescription = "Escalated",
                        tint = Color(0xFFDC2626),
                        modifier = Modifier.size(16.dp)
                    )
                },
                actionButtonText = "Track",
                onActionPressed = {
                    scope.launch {
                        toastHostState.showToast(
                            category = Ux4gToastCategory.Warning,
                            title = "Escalated Ticket",
                            subtitle = "Ticket #GRV-2026-04127 is escalated to higher authority."
                        )
                    }
                },
                details = listOf(
                    Ux4gResultDetail(label = "Reference Number", value = "GRV-2026-04127"),
                    Ux4gResultDetail(
                        label = "SLA Status",
                        value = "",
                        customValueWidget = {
                            Ux4gUnifiedPillTag(
                                segments = listOf(
                                    Ux4gPillSegment(
                                        text = "3 days overdue",
                                        bold = true,
                                        textColor = Color(0xFFDC2626),
                                        leading = {
                                            Box(
                                                modifier = Modifier
                                                    .size(6.dp)
                                                    .clip(CircleShape)
                                                    .background(Color(0xFFDC2626))
                                            )
                                        }
                                    )
                                ),
                                size = Ux4gTagSize.M
                            )
                        }
                    ),
                    Ux4gResultDetail(label = "Submitted Date", value = "1 Apr 2026"),
                    Ux4gResultDetail(label = "Assigned Officer", value = "Rahul Sharma")
                ),
                showBottomDivider = false
            )
        }

        // ─── 5. Without Background (List Style Separated by Dividers) ────────
        Text(
            text = "5. List Style (Continuous Items Separated by Dividers)",
            style = Ux4gTheme.typography.lM_strong,
            color = Ux4gTheme.colorScheme.onBackground
        )
        Ux4gCard(
            modifier = Modifier.fillMaxWidth(),
            cornerRadius = 12.dp,
            borderColor = Ux4gColors.neutral900.copy(alpha = 0.12f),
            borderWidth = 1.dp
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                Ux4gResultRow(
                    title = "Senior Citizen Pension Scheme",
                    statusTag = "Active",
                    tagColorScheme = Ux4gTagColor.Success,
                    actionButtonText = "Manage",
                    onActionPressed = {},
                    details = listOf(
                        Ux4gResultDetail(label = "Beneficiary", value = "Mohan Lal"),
                        Ux4gResultDetail(label = "Monthly Pension", value = "₹ 2,500 / month", isBold = true)
                    ),
                    showBottomDivider = true
                )
                Ux4gResultRow(
                    title = "Disability Allowance Scheme",
                    statusTag = "Renewal Due",
                    tagColorScheme = Ux4gTagColor.Warning,
                    actionButtonText = "Renew",
                    onActionPressed = {},
                    details = listOf(
                        Ux4gResultDetail(label = "Beneficiary", value = "Kiran Devi"),
                        Ux4gResultDetail(label = "Renewal Deadline", value = "30 Apr 2026", valueColor = Ux4gColors.orange600)
                    ),
                    showBottomDivider = false
                )
            }
        }

        // =========================================================================
        // Biometric Verification Showcase (Matching Flutter Widgetbook 1:1)
        // =========================================================================
        Text(
            text = "Biometric Verification",
            style = Ux4gTheme.typography.hL_default,
            color = Ux4gTheme.colorScheme.onBackground
        )

        // ─── 1. Interactive Flow with Custom Dummy Backend API (Google ML Kit) ─────
        Text(
            text = "1. Full Verification Flow with Custom Dummy API & Google ML Kit",
            style = Ux4gTheme.typography.lM_strong,
            color = Ux4gTheme.colorScheme.onBackground
        )

        var isFlowLaunched by remember { mutableStateOf(false) }
        var isSimulateSuccess by remember { mutableStateOf(true) }
        var flowResultMessage by remember { mutableStateOf<String?>(null) }

        // Custom Dummy Backend API function (demonstrating production integration without MockVerificationService)
        suspend fun dummyUidaiVerificationApi(imageBytes: ByteArray, shouldSucceed: Boolean): BiometricVerificationResult {
            // Simulate network latency to UIDAI verification gateway
            kotlinx.coroutines.delay(2200L)

            return if (shouldSucceed) {
                BiometricVerificationResult(
                    success = true,
                    userId = "GOV-UID-9842",
                    maskedName = "Aarav Sharma",
                    maskedPhone = "XXXX-XXXX-8921",
                    message = "Aadhaar e-KYC Biometric Matched Successfully",
                    confidenceScore = 0.98
                )
            } else {
                BiometricVerificationResult(
                    success = false,
                    message = "Face does not match Aadhaar biometric records",
                    errorCode = "UIDAI_BIOMETRIC_MISMATCH",
                    confidenceScore = 0.31
                )
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(560.dp)
                .clip(RoundedCornerShape(24.dp))
                .border(1.5.dp, Color(0xFFD1D5DB), RoundedCornerShape(24.dp))
                .background(Color(0xFFF8F9FC)),
            contentAlignment = Alignment.Center
        ) {
            if (isFlowLaunched) {
                Ux4gBiometricVerificationFlow(
                    onVerify = { bytes ->
                        // Calling our custom dummy backend API directly (NO MockVerificationService)
                        dummyUidaiVerificationApi(bytes, isSimulateSuccess)
                    },
                    enableBlinkCheck = true,
                    enableLightingCheck = true,
                    enableLiveness = true,
                    maxAttempts = 3,
                    onSuccess = { result ->
                        isFlowLaunched = false
                        flowResultMessage = "✓ Verified: ${result.maskedName ?: "Success"}"
                        scope.launch {
                            toastHostState.showToast(
                                category = Ux4gToastCategory.Success,
                                title = "Biometric Verified",
                                subtitle = "Name: ${result.maskedName ?: "Authorized User"}"
                            )
                        }
                    },
                    onFailure = { reason, msg ->
                        isFlowLaunched = false
                        flowResultMessage = "✗ Failed: ${reason.name}"
                        scope.launch {
                            toastHostState.showToast(
                                category = Ux4gToastCategory.Error,
                                title = "Biometric Failed",
                                subtitle = msg ?: reason.name
                            )
                        }
                    },
                    onDismiss = {
                        isFlowLaunched = false
                    },
                    onAlternateVerify = {
                        isFlowLaunched = false
                        flowResultMessage = "→ Redirected to OTP flow"
                        scope.launch {
                            toastHostState.showToast(
                                category = Ux4gToastCategory.Info,
                                title = "OTP Fallback",
                                subtitle = "Switched to OTP verification"
                            )
                        }
                    }
                )
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size(72.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF6A4EFF).copy(alpha = 0.12f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = androidx.compose.material.icons.Icons.Outlined.FaceRetouchingNatural,
                            contentDescription = null,
                            tint = Color(0xFF6A4EFF),
                            modifier = Modifier.size(36.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    Text(
                        text = "Biometric Verification",
                        style = TextStyle(
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF0F172A)
                        ),
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = if (isSimulateSuccess)
                            "Mock mode: simulates a successful verification flow"
                        else
                            "Mock mode: simulates a failed verification flow",
                        style = TextStyle(fontSize = 13.sp, color = Color(0xFF64748B)),
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Mode Switcher
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        FilterChip(
                            selected = isSimulateSuccess,
                            onClick = { isSimulateSuccess = true },
                            label = { Text("Success Mode") }
                        )
                        FilterChip(
                            selected = !isSimulateSuccess,
                            onClick = { isSimulateSuccess = false },
                            label = { Text("Failure Mode") }
                        )
                    }

                    if (flowResultMessage != null) {
                        Spacer(modifier = Modifier.height(16.dp))
                        val isSuccess = flowResultMessage!!.startsWith("✓")
                        val isOtp = flowResultMessage!!.startsWith("→")
                        val chipBg = if (isSuccess) Color(0xFFDCFCE7) else if (isOtp) Color(0xFFE0F2FE) else Color(0xFFFFE4E6)
                        val chipTextColor = if (isSuccess) Color(0xFF166534) else if (isOtp) Color(0xFF075985) else Color(0xFF991B1B)

                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(chipBg)
                                .padding(horizontal = 14.dp, vertical = 10.dp)
                        ) {
                            Text(
                                text = flowResultMessage!!,
                                style = TextStyle(
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = chipTextColor
                                ),
                                textAlign = TextAlign.Center
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    Button(
                        onClick = {
                            flowResultMessage = null
                            isFlowLaunched = true
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF6A4EFF),
                            contentColor = Color.White
                        )
                    ) {
                        Text(
                            text = "Launch Flow",
                            style = TextStyle(fontWeight = FontWeight.SemiBold, fontSize = 15.sp)
                        )
                    }
                }
            }
        }

        // ─── 2. Standalone Identity Consent Screen ───────────────────────────
        Text(
            text = "2. Standalone Identity Consent Screen",
            style = Ux4gTheme.typography.lM_strong,
            color = Ux4gTheme.colorScheme.onBackground
        )

        Ux4gCard(
            modifier = Modifier.fillMaxWidth(),
            cornerRadius = 16.dp,
            borderColor = Ux4gColors.neutral900.copy(alpha = 0.12f),
            borderWidth = 1.dp
        ) {
            Ux4gIdentityConsentScreen(
                onAllow = {
                    scope.launch {
                        toastHostState.showToast(
                            category = Ux4gToastCategory.Info,
                            title = "Consent Allowed",
                            subtitle = "Proceeding to camera device checks"
                        )
                    }
                },
                onDecline = {
                    scope.launch {
                        toastHostState.showToast(
                            category = Ux4gToastCategory.Warning,
                            title = "Consent Declined",
                            subtitle = "Switching to OTP verification fallback"
                        )
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(380.dp)
            )
        }

        // ─── 3. Standalone Device Check Screen ───────────────────────────────
        Text(
            text = "3. Standalone Device Readiness Check Screen",
            style = Ux4gTheme.typography.lM_strong,
            color = Ux4gTheme.colorScheme.onBackground
        )

        val standaloneDeviceController = remember {
            BiometricController(
                onVerify = { dummyUidaiVerificationApi(it, true) }
            )
        }

        Ux4gCard(
            modifier = Modifier.fillMaxWidth(),
            cornerRadius = 16.dp,
            borderColor = Ux4gColors.neutral900.copy(alpha = 0.12f),
            borderWidth = 1.dp
        ) {
            Ux4gDeviceCheckScreen(
                controller = standaloneDeviceController,
                onProceed = {
                    scope.launch {
                        toastHostState.showToast(
                            category = Ux4gToastCategory.Success,
                            title = "All Systems Ready",
                            subtitle = "Camera capture triggered"
                        )
                    }
                },
                compact = true,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    Ux4gTheme { DesignSystemShowcase() }
}

