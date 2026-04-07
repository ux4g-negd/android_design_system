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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.ux4g.designsystemv1.foundation.Ux4gIconComponent
import com.ux4g.designsystemv1.foundation.Ux4gIcons
import com.ux4g.designsystemv1.foundation.Ux4gPalette
import com.ux4g.designsystemv1.theme.Ux4gTheme
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.setValue
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
            .padding(16.dp),
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
            Ux4gLoader(size = 48.dp, percentage = 90f, color = Ux4gPalette.primary)
            Ux4gLoader(size = 40.dp, percentage = 75f, color = Ux4gPalette.secondary)
            Ux4gLoader(size = 32.dp, percentage = 50f, color = Ux4gPalette.tertiary)
            Ux4gLoader(size = 24.dp, percentage = 25f, color = Ux4gPalette.red)
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
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    Ux4gTheme { DesignSystemShowcase() }
}
