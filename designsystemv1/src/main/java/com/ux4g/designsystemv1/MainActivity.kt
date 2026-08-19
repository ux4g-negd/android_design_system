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
import com.ux4g.designsystemv1.components.Ux4gPaginationIndicator
import com.ux4g.designsystemv1.components.Ux4gPaginationVariant
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
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    Ux4gTheme { DesignSystemShowcase() }
}

