package com.ux4g.ux4gdesignsystem

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.state.ToggleableState
import kotlinx.coroutines.launch
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ux4g.designsystemv1.components.*
import com.ux4g.designsystemv1.components.avatar_component.Ux4gAvatar
import com.ux4g.designsystemv1.components.avatar_component.Ux4gAvatarSize
import com.ux4g.designsystemv1.components.avatar_component.Ux4gProfileAvatar
import com.ux4g.designsystemv1.components.avatar_component.Ux4gProfileAvatarVariant
import com.ux4g.designsystemv1.components.buttons.Ux4gButton
import com.ux4g.designsystemv1.components.dropdowns.Ux4gDropdownOption
import com.ux4g.designsystemv1.components.dropdowns.Ux4gSelectionDropdown
import com.ux4g.designsystemv1.components.dropdowns.Ux4gSelectionDropdownMode
import com.ux4g.designsystemv1.components.dropdowns.Ux4gDropdownFilterType
import com.ux4g.designsystemv1.components.inputfield.*
import com.ux4g.designsystemv1.components.searchfield.*
import com.ux4g.designsystemv1.components.steppers.StepperOrientation
import com.ux4g.designsystemv1.components.steppers.Ux4gCapsuleStepper
import com.ux4g.designsystemv1.components.steppers.Ux4gCapsuleStepperLayout
import com.ux4g.designsystemv1.components.steppers.Ux4gStepItem
import com.ux4g.designsystemv1.components.steppers.Ux4gStepper
import com.ux4g.designsystemv1.components.tooltip.*
import com.ux4g.designsystemv1.foundation.ExperimentalUx4gApi
import com.ux4g.designsystemv1.foundation.Ux4gIconComponent
import com.ux4g.designsystemv1.foundation.Ux4gIcons
import com.ux4g.designsystemv1.foundation.Ux4gPalette
import com.ux4g.designsystemv1.foundation.Ux4gRadius
import com.ux4g.designsystemv1.theme.Ux4gTheme
import com.ux4g.ux4gdesignsystem.ui.theme.Ux4GDesignSystemTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Ux4GDesignSystemTheme() {
                val toastHostState = remember { Ux4gToastHostState() }
                var toastAtTop by remember { mutableStateOf(true) }

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    containerColor = Ux4gTheme.colorScheme.background
                ) { innerPadding ->
                    Box(modifier = Modifier.fillMaxSize()) {
                        MainContent(
                            modifier = Modifier.padding(innerPadding),
                            toastHostState = toastHostState,
                            onToggleToastPosition = { toastAtTop = !toastAtTop },
                            isToastAtTop = toastAtTop
                        )

                        Ux4gToastHost(
                            hostState = toastHostState,
                            modifier = Modifier
                                .align(if (toastAtTop) Alignment.TopCenter else Alignment.BottomCenter)
                                .then(if (toastAtTop) Modifier.statusBarsPadding() else Modifier.navigationBarsPadding()),
                            isBottom = !toastAtTop
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalUx4gApi::class)
@Composable
fun MainContent(
    modifier: Modifier = Modifier,
    toastHostState: Ux4gToastHostState,
    onToggleToastPosition: () -> Unit,
    isToastAtTop: Boolean
) {
    val scope = rememberCoroutineScope()
    var selectedRadio by remember { mutableStateOf(1) }

    Column(
            modifier = modifier.fillMaxSize().padding(16.dp).verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        Text(
                text = "Ux4g Component Showcase",
                style = Ux4gTheme.typography.hM_strong,
                color = Ux4gTheme.colorScheme.onBackground
        )

        Ux4gProfileAvatar(
                size = Ux4gAvatarSize.XL,
                variant = Ux4gProfileAvatarVariant.Action.Edit,
                imageUrl =
                        "https://images.unsplash.com/photo-1494790108377-be9c29b29330?w=1200&auto=format&fit=crop&q=60"
        )

        // 1. Default Primary Button
        Ux4gCard(modifier = Modifier.fillMaxWidth()) {
            Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(text = "Primary Button", style = Ux4gTheme.typography.tS_strong)
                Ux4gButton(onClick = {}, modifier = Modifier.fillMaxWidth()) {
                    Ux4gIconComponent(
                            icon = Ux4gIcons.Check,
                            contentDescription = null,
                            modifier = Modifier.padding(end = 8.dp)
                    )
                    Text("Confirm Changes")
                }
            }
        }

        // 2. Outlined Button
        Ux4gCard(modifier = Modifier.fillMaxWidth()) {
            Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(text = "Outlined Button", style = Ux4gTheme.typography.tS_strong)
                Ux4gButton(
                        onClick = {},
                        modifier = Modifier.fillMaxWidth(),
                        backgroundColor = Color.Transparent,
                        contentColor = Ux4gTheme.colorScheme.primary,
                        borderWidth = 2.dp,
                        borderColor = Ux4gTheme.colorScheme.primary
                ) { Text("Outlined Action") }
            }
        }

        // 3. Custom Color & Radius
        Ux4gCard(modifier = Modifier.fillMaxWidth()) {
            Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(text = "Custom Style (Secondary)", style = Ux4gTheme.typography.tS_strong)
                Ux4gButton(
                        onClick = {},
                        modifier = Modifier.fillMaxWidth(),
                        backgroundColor = Ux4gPalette.secondary,
                        contentColor = Ux4gPalette.neutral1000black,
                        borderRadius = 24.dp,
                        elevation = 8.dp
                ) { Text("Secondary Pill Button") }
            }
        }

        // 4. Loading State
        Ux4gCard(modifier = Modifier.fillMaxWidth()) {
            Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(text = "Loading State", style = Ux4gTheme.typography.tS_strong)
                Ux4gButton(onClick = {}, modifier = Modifier.fillMaxWidth(), isLoading = true) {
                    Text("Click Me")
                }
            }
        }

        // 5. Disabled State
        Ux4gCard(modifier = Modifier.fillMaxWidth()) {
            Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(text = "Disabled State", style = Ux4gTheme.typography.tS_strong)
                Ux4gButton(onClick = {}, modifier = Modifier.fillMaxWidth(), enabled = false) {
                    Text("Disabled Button")
                }
            }
        }

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
            Ux4gLoader(size = 40.dp, percentage = 100f, color = Ux4gPalette.primary)
            Ux4gLoader(size = 40.dp, percentage = 60f, color = Ux4gPalette.secondary)
            Ux4gLoader(size = 40.dp, percentage = 30f, color = Ux4gPalette.red)
            Ux4gLoader(color = Ux4gPalette.green, percentage = 75f)
        }

        // 6. Radio Buttons
        Ux4gCard(modifier = Modifier.fillMaxWidth()) {
            Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(text = "Radio Buttons", style = Ux4gTheme.typography.tS_strong)
                Ux4gRadioButton(
                        selected = selectedRadio == 1,
                        onClick = { selectedRadio = 1 },
                        label = "Option 1 (Medium)",
                        size = Ux4gRadioButtonSize.Medium
                )
                Ux4gRadioButton(
                        selected = selectedRadio == 2,
                        onClick = { selectedRadio = 2 },
                        label = "Option 2 (Large with Desc)",
                        size = Ux4gRadioButtonSize.Large,
                        description = "Helper text for option 2"
                )
                Ux4gRadioButton(
                        selected = selectedRadio == 3,
                        onClick = { selectedRadio = 3 },
                        label = "Option 3 (Disabled)",
                        size = Ux4gRadioButtonSize.Medium,
                        enabled = false
                )
            }
        }

        // 7. Dropdown Showcase
        Ux4gCard(modifier = Modifier.fillMaxWidth()) {
            Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(text = "Selection Dropdowns", style = Ux4gTheme.typography.tS_strong)
                val mockOptions = remember {
                    listOf(
                            Ux4gDropdownOption("opt_1", "Option A"),
                            Ux4gDropdownOption("opt_2", "Option B"),
                            Ux4gDropdownOption("opt_3", "Option C", leadingIcon = Ux4gIcons.Person)
                    )
                }
                var singleSelected by remember { mutableStateOf(listOf("opt_1")) }
                var multiSelected by remember { mutableStateOf(listOf("opt_1", "opt_3")) }

                Ux4gSelectionDropdown(
                        options = mockOptions,
                        selectedOptionIds = singleSelected,
                        onSelectionChange = { singleSelected = it },
                        label = "Single Select",
                        description = "Choose one option",
                        mode = Ux4gSelectionDropdownMode.SingleSelect
                )
                Ux4gSelectionDropdown(
                        options = mockOptions,
                        selectedOptionIds = multiSelected,
                        onSelectionChange = { multiSelected = it },
                        label = "Multi Select",
                        description = "Choose multiple options",
                        mode = Ux4gSelectionDropdownMode.MultiSelect
                )
            }
        }

        // 7.1. Searchable Dropdown Showcase
        Ux4gCard(modifier = Modifier.fillMaxWidth()) {
            Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(text = "Searchable Selection Dropdowns", style = Ux4gTheme.typography.tS_strong)
                val stateOptions = remember {
                    listOf(
                        Ux4gDropdownOption("ap", "Andhra Pradesh"),
                        Ux4gDropdownOption("arp", "Arunachal Pradesh"),
                        Ux4gDropdownOption("as", "Assam"),
                        Ux4gDropdownOption("br", "Bihar"),
                        Ux4gDropdownOption("cg", "Chhattisgarh"),
                        Ux4gDropdownOption("ga", "Goa"),
                        Ux4gDropdownOption("gj", "Gujarat"),
                    )
                }
                var searchableSingleSelected by remember { mutableStateOf(listOf("br")) }
                var searchableMultiSelected by remember { mutableStateOf(listOf("arp", "br")) }

                Text("Filter Mode: StartsWithPerTerm", style = Ux4gTheme.typography.lS_strong)
                Ux4gSelectionDropdown(
                    options = stateOptions,
                    selectedOptionIds = searchableSingleSelected,
                    onSelectionChange = { searchableSingleSelected = it },
                    label = "Single Select - Starts With Word",
                    description = "Typing 'Pra' matches 'Pradesh'",
                    searchEnabled = true,
                    filterType = Ux4gDropdownFilterType.StartsWithPerTerm,
                    mode = Ux4gSelectionDropdownMode.SingleSelect
                )

                Text("Filter Mode: Contains", style = Ux4gTheme.typography.lS_strong)
                Ux4gSelectionDropdown(
                    options = stateOptions,
                    selectedOptionIds = searchableSingleSelected,
                    onSelectionChange = { searchableSingleSelected = it },
                    label = "Single Select - Contains",
                    description = "Typing 'esh' matches 'Pradesh'",
                    searchEnabled = true,
                    filterType = Ux4gDropdownFilterType.Contains,
                    mode = Ux4gSelectionDropdownMode.SingleSelect
                )

                Text("Filter Mode: StartsWith", style = Ux4gTheme.typography.lS_strong)
                Ux4gSelectionDropdown(
                    options = stateOptions,
                    selectedOptionIds = searchableSingleSelected,
                    onSelectionChange = { searchableSingleSelected = it },
                    label = "Single Select - Starts With (Strict)",
                    description = "Typing 'And' matches 'Andhra Pradesh'",
                    searchEnabled = true,
                    filterType = Ux4gDropdownFilterType.StartsWith,
                    mode = Ux4gSelectionDropdownMode.SingleSelect
                )

                Text("Multi Select Examples", style = Ux4gTheme.typography.lS_strong)
                Ux4gSelectionDropdown(
                    options = stateOptions,
                    selectedOptionIds = searchableMultiSelected,
                    onSelectionChange = { searchableMultiSelected = it },
                    label = "Multi Select - Contains",
                    description = "Choose multiple states",
                    searchEnabled = true,
                    filterType = Ux4gDropdownFilterType.Contains,
                    mode = Ux4gSelectionDropdownMode.MultiSelect
                )
            }
        }

        // 8. Input Field Showcase
        Ux4gCard(modifier = Modifier.fillMaxWidth()) {
            Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(text = "Input Fields", style = Ux4gTheme.typography.tS_strong)
                var textVal by remember { mutableStateOf("") }
                var passVal by remember { mutableStateOf("") }

                Ux4gInputField(
                        value = textVal,
                        onValueChange = { textVal = it },
                        label = "Default Input",
                        placeholder = "Placeholder",
                        caption = "Helper text",
                        required = true,
                        size = Ux4gInputFieldSize.Medium,
                        status = Ux4gInputFieldStatus.Default
                )
                Ux4gInputField(
                        value = textVal,
                        onValueChange = { textVal = it },
                        label = "Error Input",
                        placeholder = "Placeholder",
                        caption = "Error subtitle",
                        status = Ux4gInputFieldStatus.Error,
                        trailingIcon = Ux4gIcons.Close
                )
                Ux4gInputField(
                        value = passVal,
                        onValueChange = { passVal = it },
                        label = "Password",
                        placeholder = "Enter your password",
                        type = Ux4gInputFieldType.Password,
                        showPasswordToggle = true
                )
                Ux4gInputField(
                        value = textVal,
                        onValueChange = { textVal = it },
                        label = "Amount",
                        placeholder = "100",
                        prefixText = "$",
                        postfixText = ".00"
                )
                Ux4gInputField(
                        value = textVal,
                        onValueChange = { textVal = it },
                        label = "Search",
                        placeholder = "Search...",
                        leadingIcon = Ux4gIcons.Person
                )
            }
        }

        // 9. Search Field Autocomplete
        Ux4gCard(modifier = Modifier.fillMaxWidth()) {
            Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(text = "Search Field (Autocomplete)", style = Ux4gTheme.typography.tS_strong)
                var autocompleteVal by remember { mutableStateOf("m") }
                val dummyData =
                        listOf("macbook", "mobile", "monitor", "mouse", "camera", "keyboard")
                val filteredOptions =
                        dummyData.filter { it.contains(autocompleteVal, ignoreCase = true) }
                val isMockLoading = autocompleteVal == "mm"

                Ux4gSearchField(
                        value = autocompleteVal,
                        onValueChange = { autocompleteVal = it },
                        variant = Ux4gSearchFieldVariant.Autocomplete,
                        label = "Search components",
                        placeholder = "Search for...",
                        showVoiceIcon = true,
                        isLoading = isMockLoading,
                        options = if (isMockLoading) emptyList() else filteredOptions,
                        onOptionSelected = { selected -> autocompleteVal = selected }
                )
            }
        }

        // 10. Tooltip Showcase
        Ux4gCard(modifier = Modifier.fillMaxWidth()) {
            Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(text = "Tooltips (Long-press to show)", style = Ux4gTheme.typography.tS_strong)

                // Plain tooltips – appear on long-press as floating popup above UI
                Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Ux4gTooltip(tooltipText = "Top tooltip", placement = Ux4gTooltipPlacement.Top) {
                        Ux4gButton(onClick = {}) { Text("Top") }
                    }
                    Ux4gTooltip(
                            tooltipText = "Bottom tooltip",
                            placement = Ux4gTooltipPlacement.Bottom
                    ) { Ux4gButton(onClick = {}) { Text("Bottom") } }
                    Ux4gTooltip(
                            tooltipText = "With Icon",
                            placement = Ux4gTooltipPlacement.Right,
                            icon = Ux4gIcons.info_circle
                    ) { Ux4gButton(onClick = {}) { Text("Right") } }
                }

                Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                ) {
                    Ux4gTooltip(
                            tooltipText = "Custom Color",
                            placement = Ux4gTooltipPlacement.Left,
                            backgroundColor = Ux4gPalette.primary,
                            contentColor = Ux4gPalette.white
                    ) { Ux4gButton(onClick = {}) { Text("Left") } }
                    Ux4gTooltip(
                            tooltipText = "Top Start",
                            placement = Ux4gTooltipPlacement.TopStart,
                            icon = Ux4gIcons.success_circle
                    ) {
                        Ux4gIconComponent(
                                icon = Ux4gIcons.info_circle,
                                contentDescription = "Info",
                                modifier = Modifier.size(24.dp),
                                tint = Ux4gTheme.colorScheme.onBackground
                        )
                    }
                }

                // Rich Tooltip – persistent, dismisses on tap outside
                Text(text = "Rich Tooltips (Long-press)", style = Ux4gTheme.typography.tS_strong)
                Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Ux4gRichTooltip(
                            tooltipText =
                                    "This is a rich tooltip with a title and an action button. Tap outside or the action to dismiss.",
                            title = "Rich Tooltip",
                            icon = Ux4gIcons.info_circle,
                            placement = Ux4gTooltipPlacement.Top,
                            action = {
                                Ux4gButton(
                                        onClick = {},
                                        borderRadius = 4.dp,
                                        backgroundColor = Ux4gPalette.primary
                                ) { Text("Action", style = Ux4gTheme.typography.lS_strong) }
                            }
                    ) { Ux4gButton(onClick = {}) { Text("Long Press Me") } }
                }
            }
        }

        // 11. Divider Showcase
        Ux4gCard(modifier = Modifier.fillMaxWidth()) {
            Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(text = "Dividers", style = Ux4gTheme.typography.tS_strong)

                // Default horizontal solid
                Text("Horizontal – Default (Solid)", style = Ux4gTheme.typography.lM_default)
                Ux4gDivider()

                // Dashed
                Text("Horizontal – Dashed", style = Ux4gTheme.typography.lM_default)
                Ux4gDivider(style = Ux4gDividerStyle.Dashed)

                // Dotted in primary color
                Text("Horizontal – Dotted, Primary", style = Ux4gTheme.typography.lM_default)
                Ux4gDivider(
                        style = Ux4gDividerStyle.Dotted,
                        color = Ux4gPalette.primary500,
                        thickness = 1.5.dp
                )

                // With label
                Text("Horizontal – With Label", style = Ux4gTheme.typography.lM_default)
                Ux4gDivider(
                        label = {
                            Text(
                                    text = "OR",
                                    style = Ux4gTheme.typography.lS_strong,
                                    color = Ux4gPalette.neutral500
                            )
                        }
                )

                // Thick error color with indent
                Text(
                        "Horizontal – Error 2dp, Start Indent 32dp",
                        style = Ux4gTheme.typography.lM_default
                )
                Ux4gDivider(thickness = 2.dp, color = Ux4gPalette.red500, startIndent = 32.dp)

                // Vertical dividers
                Text("Vertical – Solid / Dashed / Primary", style = Ux4gTheme.typography.lM_default)
                Row(
                        modifier = Modifier.fillMaxWidth().height(56.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Left", style = Ux4gTheme.typography.bM_default)
                    Ux4gDivider(orientation = Ux4gDividerOrientation.Vertical)
                    Text("Mid", style = Ux4gTheme.typography.bM_default)
                    Ux4gDivider(
                            orientation = Ux4gDividerOrientation.Vertical,
                            style = Ux4gDividerStyle.Dashed
                    )
                    Text("Right", style = Ux4gTheme.typography.bM_default)
                    Ux4gDivider(
                            orientation = Ux4gDividerOrientation.Vertical,
                            color = Ux4gPalette.primary500,
                            thickness = 2.dp
                    )
                    Text("End", style = Ux4gTheme.typography.bM_default)
                }
            }
        }

        // 12. Modal Showcase
        Ux4gCard(modifier = Modifier.fillMaxWidth()) {
            Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(text = "Modals", style = Ux4gTheme.typography.tS_strong)

                var showDefaultModal by remember { mutableStateOf(false) }
                var showDestructiveModal by remember { mutableStateOf(false) }
                var showMinimalModal by remember { mutableStateOf(false) }
                var showAvatarModal by remember { mutableStateOf(false) }
                var showIconModal by remember { mutableStateOf(false) }

                Ux4gButton(
                        onClick = { showDefaultModal = true },
                        modifier = Modifier.fillMaxWidth()
                ) { Text("Open Default Modal (2 buttons, Left)") }

                Ux4gButton(
                        onClick = { showDestructiveModal = true },
                        modifier = Modifier.fillMaxWidth(),
                        backgroundColor = Ux4gPalette.red500
                ) { Text("Open Destructive Modal (Centered)") }

                Ux4gButton(
                        onClick = { showMinimalModal = true },
                        modifier = Modifier.fillMaxWidth(),
                        backgroundColor = Ux4gPalette.neutral700
                ) { Text("Open Minimal Confirm Modal") }

                if (showDefaultModal) {
                    Ux4gModal(
                            onDismiss = { showDefaultModal = false },
                            headerImageContent = {
                                Box(
                                        modifier =
                                                Modifier.fillMaxSize()
                                                        .background(
                                                                Brush.linearGradient(
                                                                        listOf(
                                                                                Color(0xFF9C6FE0),
                                                                                Color(0xFFE07BAD),
                                                                                Color(0xFF7B9FE0)
                                                                        )
                                                                )
                                                        )
                                )
                            },
                            showHeader = true,
                            headerTitle = "Header",
                            showSubtitle = true,
                            showBody = true,
                            showFooter = true,
                            footerButtons = Ux4gModalFooterButtons.TwoButtons,
                            footerAlign = Ux4gModalFooterAlign.Right,
                            isDestructive = false,
                            onPrimaryClick = { showDefaultModal = false },
                            onSecondaryClick = { showDefaultModal = false }
                    )
                }

                if (showDestructiveModal) {
                    Ux4gModal(
                            onDismiss = { showDestructiveModal = false },
                            headerImageContent = {
                                Box(
                                        modifier =
                                                Modifier.fillMaxSize()
                                                        .background(
                                                                Brush.linearGradient(
                                                                        listOf(
                                                                                Color(0xFF9C6FE0),
                                                                                Color(0xFFE07BAD)
                                                                        )
                                                                )
                                                        )
                                )
                            },
                            showHeader = true,
                            headerTitle = "Warning",
                            showDescription = true,
                            descriptionText = "This action cannot be undone.",
                            alignment = Ux4gModalAlignment.Centered,
                            showSubtitle = true,
                            subtitleText = "Delete Item?",
                            showBody = true,
                            bodyText = "Are you sure you want to permanently delete this item?",
                            showFooter = true,
                            footerButtons = Ux4gModalFooterButtons.TwoButtons,
                            footerAlign = Ux4gModalFooterAlign.Center,
                            isDestructive = true,
                            primaryButtonText = "Delete",
                            secondaryButtonText = "Cancel",
                            onPrimaryClick = { showDestructiveModal = false },
                            onSecondaryClick = { showDestructiveModal = false }
                    )
                }

                if (showMinimalModal) {
                    Ux4gModal(
                            onDismiss = { showMinimalModal = false },
                            headerImageStyle = Ux4gModalHeaderImage.None,
                            showHeader = false,
                            showDividers = false,
                            showSubtitle = true,
                            subtitleText = "Confirm Action",
                            showBody = true,
                            bodyText = "This will permanently remove the selected items. Continue?",
                            showFooter = true,
                            footerButtons = Ux4gModalFooterButtons.TwoButtons,
                            footerAlign = Ux4gModalFooterAlign.Split,
                            isDestructive = true,
                            primaryButtonText = "Confirm",
                            secondaryButtonText = "Cancel",
                            showCloseButton = false,
                            onPrimaryClick = { showMinimalModal = false },
                            onSecondaryClick = { showMinimalModal = false }
                    )
                }

                // New button: Avatar modal (centered, padded image)
                Ux4gButton(
                        onClick = { showAvatarModal = true },
                        modifier = Modifier.fillMaxWidth(),
                        backgroundColor = Ux4gPalette.primary700
                ) { Text("Open Avatar Modal (Centered + Padded image)") }

                if (showAvatarModal) {
                    Ux4gModal(
                            onDismiss = { showAvatarModal = false },
                            headerImageContent = {
                                Box(
                                        modifier =
                                                Modifier.fillMaxSize()
                                                        .background(
                                                                Brush.linearGradient(
                                                                        listOf(
                                                                                Color(0xFF9C6FE0),
                                                                                Color(0xFFE07BAD)
                                                                        )
                                                                )
                                                        )
                                )
                            },
                            headerImageStyle = Ux4gModalHeaderImage.Padded,
                            leadingItem = Ux4gModalLeadingItem.Avatar,
                            avatarInitials = "JD",
                            alignment = Ux4gModalAlignment.Centered,
                            showHeader = true,
                            headerTitle = "Header",
                            showDescription = true,
                            descriptionText = "Write description here",
                            showSubtitle = true,
                            showBody = true,
                            showFooter = true,
                            footerButtons = Ux4gModalFooterButtons.OneButton,
                            footerAlign = Ux4gModalFooterAlign.Center,
                            isDestructive = true,
                            onPrimaryClick = { showAvatarModal = false }
                    )
                }

                // New button: No-image + Icon leading item + Destructive + Split footer
                Ux4gButton(
                        onClick = { showIconModal = true },
                        modifier = Modifier.fillMaxWidth(),
                        backgroundColor = Ux4gPalette.red700
                ) { Text("Open Icon Modal (No image + Split footer)") }

                if (showIconModal) {
                    Ux4gModal(
                            onDismiss = { showIconModal = false },
                            headerImageStyle = Ux4gModalHeaderImage.None,
                            leadingItem = Ux4gModalLeadingItem.Icon,
                            leadingIcon = Ux4gIcons.info_circle,
                            leadingIconTint = Ux4gPalette.red500,
                            alignment = Ux4gModalAlignment.LeftAligned,
                            showHeader = true,
                            headerTitle = "Header",
                            showDescription = true,
                            descriptionText = "Write description here",
                            showSubtitle = true,
                            showBody = true,
                            showFooter = true,
                            footerButtons = Ux4gModalFooterButtons.TwoButtons,
                            footerAlign = Ux4gModalFooterAlign.Split,
                            isDestructive = true,
                            primaryButtonText = "Delete",
                            secondaryButtonText = "Cancel",
                            onPrimaryClick = { showIconModal = false },
                            onSecondaryClick = { showIconModal = false }
                    )
                }

                // ── Card Showcase ──
                Spacer(modifier = Modifier.height(32.dp))
                Text(text = "Card Component", style = Ux4gTheme.typography.tS_strong)
                Spacer(modifier = Modifier.height(16.dp))

                // 1. Full card — image, avatar, status chips, body, bottom chips, footer
                Ux4gCard(
                        modifier = Modifier.fillMaxWidth(),
                        mediaImageUrl = "https://picsum.photos/seed/card1/600/300",
                        mediaLabelText = "Label",
                        avatar = {
                            Ux4gAvatar(
                                    size = Ux4gAvatarSize.S,
                                    initials = "JD",
                                    containerColor = Ux4gPalette.primary100,
                                    contentColor = Ux4gPalette.primary700
                            )
                        },
                        title = "Title",
                        subtitle = "Subtitle",
                        statusChips = listOf("Status A", "Pending", "Approved"),
                        bodyText =
                                "Lorem ipsum is a dummy or placeholder text commonly used in graphic design, publishing, and web development.",
                        bottomChips = listOf("Label", "Label", "Label", "Label"),
                        footerType = Ux4gCardFooterType.PrimaryAndSecondary,
                        footerAlignment = Ux4gCardFooterAlignment.Centered,
                        primaryButtonText = "Confirm",
                        secondaryButtonText = "Cancel"
                )

                Spacer(modifier = Modifier.height(16.dp))

                // 2. Outlined border card — no avatar, secondary only
                Ux4gCard(
                        modifier = Modifier.fillMaxWidth(),
                        mediaImageUrl = "https://picsum.photos/seed/card2/600/300",
                        mediaLabelText = "New",
                        borderColor = Ux4gPalette.primary,
                        borderWidth = 1.dp,
                        title = "Outlined Card",
                        subtitle = "No avatar shown here",
                        statusChips = listOf("Category", "Design"),
                        bodyText =
                                "This card has a primary coloured border and only a secondary footer button.",
                        bottomChips = listOf("Kotlin", "Compose", "Ux4G"),
                        footerType = Ux4gCardFooterType.SecondaryOnly,
                        footerAlignment = Ux4gCardFooterAlignment.Left,
                        secondaryButtonText = "Learn More"
                )

                Spacer(modifier = Modifier.height(16.dp))

                // 3. Elevated card with custom background — no image
                Ux4gCard(
                        modifier = Modifier.fillMaxWidth(),
                        elevation = 4.dp,
                        backgroundColor = Ux4gPalette.primary50,
                        cornerRadius = Ux4gRadius.Radius16,
                        title = "Elevated Card",
                        bodyText =
                                "This card has a tinted background and elevated shadow, no image required.",
                        bottomChips = listOf("Jetpack", "Material3", "API 26+"),
                        footerType = Ux4gCardFooterType.PrimaryOnly,
                        footerAlignment = Ux4gCardFooterAlignment.Right,
                        primaryButtonText = "Get Started"
                )
            }
        }

        // 13. Slider Showcase
        Ux4gCard(modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                Text(text = "Sliders", style = Ux4gTheme.typography.tS_strong)
                
                // State 1: Basic Single (Small)
                var s1 by remember { mutableStateOf(50f) }
                Ux4gSlider(
                    value = s1,
                    onValueChange = { s1 = it },
                    label = "Basic Single Slider (Small)",
                    showMarksAndValues = true,
                    steps = 9
                )

                // State 2: Range (Medium) + Custom Labels
                var s2 by remember { mutableStateOf(20f..70f) }
                Ux4gRangeSlider(
                    value = s2,
                    onValueChange = { s2 = it },
                    label = "Basic Range Slider (Medium)",
                    size = Ux4gSliderSize.Medium,
                    showValueLabels = true,
                    showMarksAndValues = true,
                    steps = 9
                )

                // State 3: With Indicator (Slide to see)
                var s3 by remember { mutableStateOf(40f) }
                Ux4gSlider(
                    value = s3,
                    onValueChange = { s3 = it },
                    label = "Slider with Value Indicator (Slide thumb)",
                    showIndicator = true,
                    showMarksAndValues = true,
                    steps = 4
                )

                // State 4: With Input Fields
                var s4 by remember { mutableStateOf(30f..80f) }
                Ux4gRangeSlider(
                    value = s4,
                    onValueChange = { s4 = it },
                    label = "With Editable Input Fields",
                    showInputFields = true,
                    showMarksAndValues = true,
                    steps = 5
                )

                // State 5: Disabled State
                Ux4gSlider(
                    value = 60f,
                    onValueChange = { },
                    label = "Disabled State",
                    enabled = false,
                    showMarksAndValues = true,
                    steps = 9,
                    caption = "Component is disabled"
                )

                // State 6: Status Variations
                Ux4gSlider(
                    value = 15f,
                    onValueChange = { },
                    label = "Slider Error state",
                    caption = "Value is out of budget!",
                    captionVariant = Ux4gSliderCaptionVariant.Error,
                    showMarksAndValues = true,
                    steps = 2
                )
                Ux4gSlider(
                    value = 85f,
                    onValueChange = { },
                    label = "Slider Warning state",
                    caption = "High priority task",
                    captionVariant = Ux4gSliderCaptionVariant.Warning,
                    showMarksAndValues = true,
                    steps = 2
                )
                Ux4gSlider(
                    value = 100f,
                    onValueChange = { },
                    label = "Slider Success state",
                    caption = "Target achieved!",
                    captionVariant = Ux4gSliderCaptionVariant.Success,
                    showMarksAndValues = true,
                    steps = 2
                )
            }
        }

        // 14. Interactive Toast Showcase
        Ux4gCard(modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(text = "Interactive Toasts", style = Ux4gTheme.typography.tS_strong)
                Text(
                    text = "Position: ${if (isToastAtTop) "Top" else "Bottom"}",
                    style = Ux4gTheme.typography.bS_default
                )

                Ux4gButton(
                    onClick = onToggleToastPosition,
                    modifier = Modifier.fillMaxWidth(),
                    backgroundColor = Ux4gPalette.neutral800
                ) {
                    Text("Toggle Toast Position")
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Ux4gButton(
                        onClick = {
                            scope.launch {
                                toastHostState.showToast(
                                    category = Ux4gToastCategory.Success,
                                    title = "Sucess!",
                                    subtitle = "Operation completed successfully.",
                                    autoClose = true,
                                    durationMillis = 2000
                                )
                            }
                        },
                        modifier = Modifier.weight(1f),
                        backgroundColor = Ux4gPalette.green500
                    ) { Text("Success") }

                    Ux4gButton(
                        onClick = {
                            scope.launch {
                                toastHostState.showToast(
                                    category = Ux4gToastCategory.Error,
                                    title = "Error",
                                    subtitle = "Something went wrong.",
                                    autoClose = true,
                                    durationMillis = 2000
                                )
                            }
                        },
                        modifier = Modifier.weight(1f),
                        backgroundColor = Ux4gPalette.red500
                    ) { Text("Error") }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Ux4gButton(
                        onClick = {
                            scope.launch {
                                toastHostState.showToast(
                                    category = Ux4gToastCategory.Warning,
                                    title = "Warning",
                                    subtitle = "Please check your input.",
                                    autoClose = true,
                                    durationMillis = 2000
                                )
                            }
                        },
                        modifier = Modifier.weight(1f),
                        backgroundColor = Ux4gPalette.gold600
                    ) { Text("Warning") }

                    Ux4gButton(
                        onClick = {
                            scope.launch {
                                toastHostState.showToast(
                                    category = Ux4gToastCategory.Info,
                                    title = "Info",
                                    subtitle = "New update available.",
                                    autoClose = true,
                                    durationMillis = 2000
                                )
                            }
                        },
                        modifier = Modifier.weight(1f),
                        backgroundColor = Ux4gPalette.cyan600
                    ) { Text("Info") }
                }
            }
        }

        // 14. Static Toast Styles (for reference)
        Ux4gCard(modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(text = "Toast Varieties", style = Ux4gTheme.typography.tS_strong)

                Text(text = "Tablet/Desktop (Full Layout)", style = Ux4gTheme.typography.lS_strong)
                Ux4gToast(
                    category = Ux4gToastCategory.Info,
                    title = "Info",
                    subtitle = "Write your information text here",
                    actionText = "Action"
                )
                Ux4gToast(
                    category = Ux4gToastCategory.Success,
                    title = "Success",
                    subtitle = "Write your success text here",
                    actionText = "Action"
                )
                Ux4gToast(
                    category = Ux4gToastCategory.Warning,
                    title = "Warning",
                    subtitle = "Write your warning text here",
                    actionText = "Action"
                )
                Ux4gToast(
                    category = Ux4gToastCategory.Error,
                    title = "Error",
                    subtitle = "Write your error text here",
                    actionText = "Action"
                )

                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "Mobile (Stacked Layout)", style = Ux4gTheme.typography.lS_strong)
                Ux4gToast(
                    category = Ux4gToastCategory.Info,
                    title = "Info",
                    subtitle = "Write your information text here",
                    actionText = "Action",
                    layout = Ux4gToastLayout.Stacked
                )
                Ux4gToast(
                    category = Ux4gToastCategory.Warning,
                    title = "Warning",
                    subtitle = "Write your warning text here",
                    actionText = "Action",
                    layout = Ux4gToastLayout.Stacked
                )
            }
        }

        // 15. Stepper Showcase
        Ux4gCard(modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                Text(text = "Stepper Component", style = Ux4gTheme.typography.tS_strong)

                var currentStep by remember { mutableStateOf(2) }

                Text(text = "Horizontal Stepper", style = Ux4gTheme.typography.lM_strong)
                Ux4gStepper(
                    totalSteps = 4,
                    currentStep = currentStep,
                    orientation = StepperOrientation.Horizontal,
                    steps = listOf(
                        Ux4gStepItem("Label", "Write description here"),
                        Ux4gStepItem("Label", "Write description here"),
                        Ux4gStepItem("Label", "Write description here"),
                        Ux4gStepItem("Label", "Write description here")
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(text = "Vertical Stepper", style = Ux4gTheme.typography.lM_strong)
                Ux4gStepper(
                    totalSteps = 4,
                    currentStep = currentStep,
                    orientation = StepperOrientation.Vertical,
                    steps = listOf(
                        Ux4gStepItem("Label", "Write description here"),
                        Ux4gStepItem("Label", "Write description here"),
                        Ux4gStepItem("Label", "Write description here"),
                        Ux4gStepItem("Label", "Write description here")
                    )
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Ux4gButton(
                        onClick = { if (currentStep > 1) currentStep-- },
                        modifier = Modifier.weight(1f),
                        enabled = currentStep > 1,
                        backgroundColor = Ux4gPalette.neutral700
                    ) {
                        Text("Previous")
                    }
                    Ux4gButton(
                        onClick = { if (currentStep < 4) currentStep++ },
                        modifier = Modifier.weight(1f),
                        enabled = currentStep < 4
                    ) {
                        Text("Next")
                    }
                }

                Ux4gDivider()

                Text(text = "Stepper with Error State", style = Ux4gTheme.typography.lM_strong)
                Ux4gStepper(
                    totalSteps = 4,
                    currentStep = 3,
                    orientation = StepperOrientation.Horizontal,
                    steps = listOf(
                        Ux4gStepItem("Account", "Completed"),
                        Ux4gStepItem("Profile", "Completed"),
                        Ux4gStepItem("Payment", "Transaction failed", isError = true),
                        Ux4gStepItem("Done", "Pending")
                    )
                )
            }
        }

        // 16. Pagination Showcase
        Ux4gCard(modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Pagination Indicators",
                    style = Ux4gTheme.typography.tS_strong,
                    modifier = Modifier.align(Alignment.Start)
                )

                var pageIndex by remember { mutableStateOf(1) }

                Text(text = "Default", style = Ux4gTheme.typography.lM_strong, modifier = Modifier.align(Alignment.Start))
                Ux4gPaginationIndicator(
                    totalPageCount = 7,
                    currentPageIndex = pageIndex,
                    onPageChange = { pageIndex = it }
                )

                Text(text = "Capsule", style = Ux4gTheme.typography.lM_strong, modifier = Modifier.align(Alignment.Start))
                Ux4gPaginationIndicator(
                    totalPageCount = 7,
                    currentPageIndex = pageIndex,
                    onPageChange = { pageIndex = it },
                    variant = Ux4gPaginationVariant.Capsule
                )

                Text(text = "Dots Only", style = Ux4gTheme.typography.lM_strong, modifier = Modifier.align(Alignment.Start))
                Ux4gPaginationIndicator(
                    totalPageCount = 7,
                    currentPageIndex = pageIndex,
                    onPageChange = { pageIndex = it },
                    showArrows = false
                )
            }
        }

        // 17. Bottom Sheet Showcase
        Ux4gCard(modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(text = "Bottom Sheets", style = Ux4gTheme.typography.tS_strong)

                var showBasicSheet by remember { mutableStateOf(false) }
                var showFullSheet by remember { mutableStateOf(false) }
                var showNonDraggableSheet by remember { mutableStateOf(false) }
                var showCustomContentSheet by remember { mutableStateOf(false) }
                var showLongContentSheet by remember { mutableStateOf(false) }

                Ux4gButton(
                    onClick = { showBasicSheet = true },
                    modifier = Modifier.fillMaxWidth()
                ) { Text("Open Basic Bottom Sheet") }

                Ux4gButton(
                    onClick = { showFullSheet = true },
                    modifier = Modifier.fillMaxWidth(),
                    backgroundColor = Ux4gPalette.primary700
                ) { Text("Open Full Bottom Sheet (Justified)") }

                Ux4gButton(
                    onClick = { showNonDraggableSheet = true },
                    modifier = Modifier.fillMaxWidth(),
                    backgroundColor = Ux4gPalette.neutral700
                ) { Text("Open Non-Draggable Sheet") }

                Ux4gButton(
                    onClick = { showCustomContentSheet = true },
                    modifier = Modifier.fillMaxWidth(),
                    backgroundColor = Ux4gPalette.green700
                ) { Text("Open Custom Content Sheet") }

                Ux4gButton(
                    onClick = { showLongContentSheet = true },
                    modifier = Modifier.fillMaxWidth(),
                    backgroundColor = Ux4gPalette.cyan700
                ) { Text("Open Long Content Sheet") }

                if (showBasicSheet) {
                    Ux4gBottomSheet(
                        onDismissRequest = { showBasicSheet = false },
                        headerTitle = "Simple Sheet",
                        content = {
                            Text("This is a simple bottom sheet with just a title and body content. It automatically adjusts its height to fit the content.")
                        }
                    )
                }

                if (showFullSheet) {
                    Ux4gBottomSheet(
                        onDismissRequest = { showFullSheet = false },
                        headerTitle = "Header",
                        headerSubtleText = "Subtle text",
                        headerIcon = Ux4gIcons.info_circle,
                        description = "Write description here",
                        footerType = Ux4gCardFooterType.PrimaryAndSecondary,
                        footerAlignment = Ux4gBottomSheetFooterAlignment.Justified,
                        primaryButtonText = "Action",
                        secondaryButtonText = "Cancel",
                        onPrimaryClick = { showFullSheet = false },
                        onSecondaryClick = { showFullSheet = false },
                        content = {
                            Text(
                                text = "A Drawer is a panel that slides out from the edge of the screen to show extra information or options. It follows the same footer structure as the Card component.",
                                style = Ux4gTheme.typography.bM_default,
                                color = Ux4gPalette.neutral700
                            )
                        }
                    )
                }

                if (showNonDraggableSheet) {
                    Ux4gBottomSheet(
                        onDismissRequest = { showNonDraggableSheet = false },
                        headerTitle = "Non-Draggable Sheet",
                        isDraggable = false,
                        description = "The drag handle is hidden and swipe gestures are handled by the system (usually minimal interaction).",
                        content = {
                            Text("This sheet has the 'isDraggable' property set to false.")
                        }
                    )
                }

                if (showCustomContentSheet) {
                    Ux4gBottomSheet(
                        onDismissRequest = { showCustomContentSheet = false },
                        headerTitle = "Complex Settings",
                        description = "You can add any custom UI inside this central area.",
                        footerType = Ux4gCardFooterType.PrimaryOnly,
                        primaryButtonText = "Apply Settings",
                        onPrimaryClick = { showCustomContentSheet = false },
                        content = {
                            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                                Text("Pick your notification types:", style = Ux4gTheme.typography.tS_strong)
                                
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    var checked1 by remember { mutableStateOf(ToggleableState.On) }
                                    var checked2 by remember { mutableStateOf(ToggleableState.Off) }
                                    
                                    Ux4gCheckbox(
                                        state = checked1,
                                        onClick = { 
                                            checked1 = if (checked1 == ToggleableState.On) ToggleableState.Off else ToggleableState.On 
                                        },
                                        label = "Email"
                                    )
                                    Ux4gCheckbox(
                                        state = checked2,
                                        onClick = { 
                                            checked2 = if (checked2 == ToggleableState.On) ToggleableState.Off else ToggleableState.On 
                                        },
                                        label = "Push"
                                    )
                                }
                                
                                Ux4gDivider()
                                
                                Text("Adjust Volume:", style = Ux4gTheme.typography.tS_strong)
                                var volume by remember { mutableStateOf(75f) }
                                Ux4gSlider(
                                    value = volume,
                                    onValueChange = { volume = it },
                                    showMarksAndValues = true,
                                    steps = 4
                                )
                                
                                Text(
                                    "Notice how the sheet automatically expands to fit these custom components.",
                                    style = Ux4gTheme.typography.bS_default,
                                    color = Ux4gPalette.neutral500
                                )
                            }
                        }
                    )
                }

                if (showLongContentSheet) {
                    Ux4gBottomSheet(
                        onDismissRequest = { showLongContentSheet = false },
                        headerTitle = "Scrollable Content",
                        description = "The header and footer stay fixed. Only this middle part scrolls.",
                        footerType = Ux4gCardFooterType.PrimaryAndSecondary,
                        footerAlignment = Ux4gBottomSheetFooterAlignment.Justified,
                        primaryButtonText = "Got it",
                        secondaryButtonText = "Cancel",
                        onPrimaryClick = { showLongContentSheet = false },
                        onSecondaryClick = { showLongContentSheet = false },
                        content = {
                            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                                repeat(20) { index ->
                                    Text(
                                        "Item #$index: This long text demonstrates that even when the content grows beyond the screen height, only the inner body remains scrollable while the top header and bottom buttons stay pinned.",
                                        style = Ux4gTheme.typography.bM_default
                                    )
                                    if (index < 19) Ux4gDivider()
                                }
                            }
                        }
                    )
                }
            }
        }

        // 18. Capsule Stepper Showcase
        Ux4gCard(modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                Text(text = "Capsule (Compact) Stepper", style = Ux4gTheme.typography.tS_strong)
                
                var capsuleStep by remember { mutableStateOf(1) }
                val totalSteps = 12

                Text("Layout: Linear (Default)", style = Ux4gTheme.typography.lS_strong)
                Ux4gCapsuleStepper(
                    totalSteps = totalSteps,
                    currentStep = capsuleStep,
                    stepLabel = "Account Setup",
                    description = "Enter your personal details to continue.",
                    layout = Ux4gCapsuleStepperLayout.Linear,
                    onNext = { if (capsuleStep < totalSteps) capsuleStep++ },
                    onPrevious = { if (capsuleStep > 1) capsuleStep-- }
                )

                Ux4gDivider()

                Text("Layout: Right Aligned", style = Ux4gTheme.typography.lS_strong)
                Ux4gCapsuleStepper(
                    totalSteps = totalSteps,
                    currentStep = capsuleStep,
                    stepLabel = "Finalize Profile",
                    description = "Uploading your bio and other settings.",
                    layout = Ux4gCapsuleStepperLayout.RightAligned,
                    onNext = { if (capsuleStep < totalSteps) capsuleStep++ },
                    onPrevious = { if (capsuleStep > 1) capsuleStep-- }
                )

                Ux4gDivider()

                Text("Layout: Centered", style = Ux4gTheme.typography.lS_strong)
                Ux4gCapsuleStepper(
                    totalSteps = totalSteps,
                    currentStep = capsuleStep,
                    stepLabel = "Payment Methods",
                    description = "Review your cards and billing address.",
                    layout = Ux4gCapsuleStepperLayout.Centered,
                    onNext = { if (capsuleStep < totalSteps) capsuleStep++ },
                    onPrevious = { if (capsuleStep > 1) capsuleStep-- }
                )

                Ux4gDivider()

                Text("Layout: Split", style = Ux4gTheme.typography.lS_strong)
                Ux4gCapsuleStepper(
                    totalSteps = totalSteps,
                    currentStep = capsuleStep,
                    stepLabel = "Confirmation",
                    description = "Almost done. Just one more click to finish your setup and start using the platform.",
                    layout = Ux4gCapsuleStepperLayout.Split,
                    onNext = { if (capsuleStep < totalSteps) capsuleStep++ },
                    onPrevious = { if (capsuleStep > 1) capsuleStep-- }
                )
            }
        }
    }
}


@Preview(name = "Light", showBackground = false, showSystemUi = true)
@Preview(name = "Dark", uiMode = Configuration.UI_MODE_NIGHT_YES)
@OptIn(ExperimentalMaterial3Api::class, ExperimentalUx4gApi::class)
@Composable
fun GreetingPreview() {
    Ux4GDesignSystemTheme { 
        MainContent(
            modifier = Modifier.padding(10.dp),
            toastHostState = remember { Ux4gToastHostState() },
            onToggleToastPosition = {},
            isToastAtTop = true
        ) 
    }
}
