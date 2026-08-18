package com.ux4g.ux4gdesignsystem

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.verticalScroll
import androidx.compose.ui.draw.clip
import java.time.LocalDate
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
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
import com.ux4g.designsystemv1.components.avatar_component.*
import com.ux4g.designsystemv1.components.buttons.Ux4gButton
import com.ux4g.designsystemv1.components.buttons.Ux4gButtonSize
import com.ux4g.designsystemv1.components.buttons.Ux4gButtonVariant
import com.ux4g.designsystemv1.components.buttons.Ux4gIconButton
import com.ux4g.designsystemv1.components.buttons.Ux4gIconButtonSize
import com.ux4g.designsystemv1.components.buttons.Ux4gIconButtonStyle
import com.ux4g.designsystemv1.components.dropdowns.*
import com.ux4g.designsystemv1.components.inputfield.*
import com.ux4g.designsystemv1.components.searchfield.*
import com.ux4g.designsystemv1.components.steppers.StepperOrientation
import com.ux4g.designsystemv1.components.steppers.Ux4gCapsuleStepper
import com.ux4g.designsystemv1.components.steppers.Ux4gCapsuleStepperLayout
import com.ux4g.designsystemv1.components.steppers.Ux4gStepItem
import com.ux4g.designsystemv1.components.steppers.Ux4gStepper
import com.ux4g.designsystemv1.components.tooltip.*
import com.ux4g.designsystemv1.R
import com.ux4g.designsystemv1.foundation.ExperimentalUx4gApi
import com.ux4g.designsystemv1.foundation.Ux4gIconComponent
import com.ux4g.designsystemv1.foundation.Ux4gIcons
import com.ux4g.designsystemv1.foundation.Ux4gColors
import com.ux4g.designsystemv1.foundation.Ux4gRadius
import com.ux4g.designsystemv1.foundation.defaultTypography
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
        var expandedAccordionIndex by remember { mutableStateOf<Int?>(2) }

        Text(
                text = "Ux4g Component Showcase",
                style = Ux4gTheme.typography.hM_strong,
                color = Ux4gTheme.colorScheme.onBackground
        )

        Ux4gCard(modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(text = "Accordion", style = Ux4gTheme.typography.tS_strong)

                Ux4gAccordionGroup(
                    items = listOf(
                        Ux4gAccordionItem(title = "Accordion Item"),
                        Ux4gAccordionItem(title = "Accordion Item"),
                        Ux4gAccordionItem(title = "Accordion Item"),
                        Ux4gAccordionItem(title = "Accordion Item"),
                        Ux4gAccordionItem(title = "Accordion Item", enabled = false)
                    ),
                    expandedIndex = expandedAccordionIndex,
                    onExpandedIndexChange = { expandedAccordionIndex = it }
                ) { index, item ->
                    when (index) {
                        2 -> {
                            Text(
                                text = "Lorem ipsum is a dummy or placeholder text commonly used in graphic design, publishing, and web development. Its purpose is to permit a page layout to be designed, independently of the copy that will subsequently populate it, or to demonstrate various fonts of a typeface without meaningful text that could be distracting.",
                                style = Ux4gTheme.typography.bXS_default,
                                color = Ux4gColors.neutral600
                            )
                        }

                        else -> {
                            Text(
                                text = "Custom content for ${item.title}",
                                style = Ux4gTheme.typography.bXS_default,
                                color = Ux4gColors.neutral600
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Disabled State:",
                    style = Ux4gTheme.typography.bS_strong,
                    color = Ux4gColors.neutral700
                )

                Ux4gAccordion(
                    title = "Accordion Item",
                    enabled = false,
                    iconColor = Ux4gTheme.colorScheme.primary,
                    disabledIconColor = Ux4gTheme.colorScheme.primary
                )
            }
        }

        // App Header Showcase
        Ux4gCard(modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(text = "App Header", style = Ux4gTheme.typography.tS_strong)

                Text(
                    text = "Header with Emblem, Union, Settings & Avatar:",
                    style = Ux4gTheme.typography.bS_strong,
                    color = Ux4gColors.neutral700
                )
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    color = Ux4gTheme.colorScheme.surface,
                    shadowElevation = 1.dp
                ) {
                    Ux4gAppHeader(
                        title = "Title",
                        variant = Ux4gAppHeaderVariant.Light,
                        useSafeArea = false,
                        leadingContent = {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Ux4gIconComponent(
                                    icon = Ux4gIcons.NationalEmblem,
                                    contentDescription = "National Emblem",
                                    modifier = Modifier.size(32.dp),
                                    tint = Color.Unspecified
                                )
                                Box(
                                    modifier = Modifier
                                        .width(1.dp)
                                        .height(24.dp)
                                        .background(Ux4gTheme.colorScheme.onSurface.copy(alpha = 0.12f))
                                )
                                Ux4gIconComponent(
                                    icon = Ux4gIcons.Union,
                                    contentDescription = "Union",
                                    modifier = Modifier.size(32.dp),
                                    tint = Color.Unspecified
                                )
                            }
                        },
                        actions = listOf(
                            Ux4gAppHeaderAction(icon = Ux4gIcons.Settings)
                        ),
                        showAvatar = true,
                        avatarInitials = "JD"
                    )
                }

                Text(
                    text = "Header with Emblem, Union, Settings & Menu:",
                    style = Ux4gTheme.typography.bS_strong,
                    color = Ux4gColors.neutral700
                )
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    color = Ux4gTheme.colorScheme.surface,
                    shadowElevation = 1.dp
                ) {
                    Ux4gAppHeader(
                        title = "Title",
                        variant = Ux4gAppHeaderVariant.Light,
                        useSafeArea = false,
                        leadingContent = {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Ux4gIconComponent(
                                    icon = Ux4gIcons.NationalEmblem,
                                    contentDescription = "National Emblem",
                                    modifier = Modifier.size(32.dp),
                                    tint = Color.Unspecified
                                )
                                Box(
                                    modifier = Modifier
                                        .width(1.dp)
                                        .height(24.dp)
                                        .background(Ux4gTheme.colorScheme.onSurface.copy(alpha = 0.12f))
                                )
                                Ux4gIconComponent(
                                    icon = Ux4gIcons.Union,
                                    contentDescription = "Union",
                                    modifier = Modifier.size(32.dp),
                                    tint = Color.Unspecified
                                )
                            }
                        },
                        actions = listOf(
                            Ux4gAppHeaderAction(icon = Ux4gIcons.Settings),
                            Ux4gAppHeaderAction(icon = Ux4gIcons.Menu)
                        )
                    )
                }

                Text(
                    text = "Filled Variant (Primary with Back Button):",
                    style = Ux4gTheme.typography.bS_strong,
                    color = Ux4gColors.neutral700
                )
                Ux4gAppHeader(
                    title = "Profile Settings",
                    variant = Ux4gAppHeaderVariant.Filled,
                    useSafeArea = false,
                    showBackButton = true,
                    actions = listOf(
                        Ux4gAppHeaderAction(icon = Ux4gIcons.Settings)
                    )
                )
            }
        }

        // Badge Showcase
        Ux4gCard(modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(text = "Badge", style = Ux4gTheme.typography.tS_strong)

                Text(
                    text = "Dot Badges:",
                    style = Ux4gTheme.typography.bS_strong,
                    color = Ux4gColors.neutral700
                )
                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    Ux4gBadge(type = Ux4gBadgeType.Dot, containerColor = Ux4gColors.primary600)
                    Ux4gBadge(type = Ux4gBadgeType.Dot, containerColor = Ux4gColors.neutral300)
                    Ux4gBadge(type = Ux4gBadgeType.Dot, containerColor = Ux4gColors.cyan600)
                    Ux4gBadge(type = Ux4gBadgeType.Dot, containerColor = Ux4gColors.green600)
                    Ux4gBadge(type = Ux4gBadgeType.Dot, containerColor = Ux4gColors.red600)
                    Ux4gBadge(type = Ux4gBadgeType.Dot, containerColor = Ux4gColors.orange600)
                }

                Text(
                    text = "Digit / Count Badges:",
                    style = Ux4gTheme.typography.bS_strong,
                    color = Ux4gColors.neutral700
                )
                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Ux4gBadge(type = Ux4gBadgeType.Count(1), containerColor = Ux4gColors.primary600)
                    Ux4gBadge(type = Ux4gBadgeType.Count(3), containerColor = Ux4gColors.neutral300)
                    Ux4gBadge(
                        type = Ux4gBadgeType.Count(11, Ux4gBadgeLimit.DoubleDigit),
                        containerColor = Ux4gColors.cyan600
                    )
                    Ux4gBadge(
                        type = Ux4gBadgeType.Count(22, Ux4gBadgeLimit.DoubleDigit),
                        containerColor = Ux4gColors.green600
                    )
                    Ux4gBadge(
                        type = Ux4gBadgeType.Count(150, Ux4gBadgeLimit.DoubleDigit),
                        containerColor = Ux4gColors.red600
                    )
                    Ux4gBadge(
                        type = Ux4gBadgeType.Count(12),
                        containerColor = Ux4gColors.neutral300,
                        showBorder = true
                    )
                }

                Text(
                    text = "Label Badges:",
                    style = Ux4gTheme.typography.bS_strong,
                    color = Ux4gColors.neutral700
                )
                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    Ux4gBadge(type = Ux4gBadgeType.Label("NEW"), containerColor = Ux4gColors.primary600)
                    Ux4gBadge(type = Ux4gBadgeType.Label("BETA"), containerColor = Ux4gColors.green600)
                    Ux4gBadge(type = Ux4gBadgeType.Label("DRAFT"), containerColor = Ux4gColors.neutral600)
                    Ux4gBadge(type = Ux4gBadgeType.Label("EXACT"), containerColor = Ux4gColors.orange600)
                }

                Text(
                    text = "Icon Badges & Ready to Use:",
                    style = Ux4gTheme.typography.bS_strong,
                    color = Ux4gColors.neutral700
                )
                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Ux4gBadge(type = Ux4gBadgeType.Icon(Ux4gIcons.Check), containerColor = Ux4gColors.primary600)
                    Ux4gBadge(type = Ux4gBadgeType.Icon(Ux4gIcons.Settings), containerColor = Ux4gColors.secondary600)
                    Ux4gBadge(type = Ux4gBadgeType.Icon(Ux4gIcons.Close), containerColor = Ux4gColors.red600)
                    Ux4gBadge(type = Ux4gBadgeType.ReadyToUse(Ux4gBadgeResource.Verification))
                    Ux4gBadge(type = Ux4gBadgeType.ReadyToUse(Ux4gBadgeResource.Star))
                    Ux4gBadge(type = Ux4gBadgeType.ReadyToUse(Ux4gBadgeResource.Shield), showBorder = true)
                }

                Text(
                    text = "Overlay Anchored on Widgets:",
                    style = Ux4gTheme.typography.bS_strong,
                    color = Ux4gColors.neutral700
                )
                Row(
                    horizontalArrangement = Arrangement.spacedBy(28.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Ux4gBadge(type = Ux4gBadgeType.Count(99)) {
                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .background(Ux4gColors.neutral200, RoundedCornerShape(8.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Ux4gIconComponent(
                                icon = Ux4gIcons.Person,
                                contentDescription = null,
                                tint = Ux4gColors.neutral700
                            )
                        }
                    }

                    Ux4gBadge(
                        type = Ux4gBadgeType.Dot,
                        containerColor = Ux4gColors.red600,
                        alignment = Ux4gBadgeAlignment.TopStart
                    ) {
                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .background(Ux4gColors.neutral200, RoundedCornerShape(8.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Ux4gIconComponent(
                                icon = Ux4gIcons.Settings,
                                contentDescription = null,
                                tint = Ux4gColors.neutral700
                            )
                        }
                    }

                    Ux4gBadge(
                        type = Ux4gBadgeType.Label("NEW"),
                        containerColor = Ux4gColors.green600,
                        alignment = Ux4gBadgeAlignment.TopEnd
                    ) {
                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .background(Ux4gColors.neutral200, RoundedCornerShape(8.dp))
                        )
                    }
                }
            }
        }

        // Link Showcase
        Ux4gCard(modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(text = "Link", style = Ux4gTheme.typography.tS_strong)

                Text(
                    text = "Tap to open the website in your browser:",
                    style = Ux4gTheme.typography.bS_default,
                    color = Ux4gColors.neutral600
                )

                Ux4gLink(url = "https://ux4g.gov.in") {
                    Text(
                        text = "Open Ux4G website",
                        style = Ux4gTheme.typography.lM_strong,
                        color = Ux4gTheme.colorScheme.primary,
                        modifier = Modifier.padding(vertical = 4.dp)
                    )
                }

                Text(
                    text = "The Link component wraps any composable and opens the URL in the device's browser when tapped.",
                    style = Ux4gTheme.typography.bXS_default,
                    color = Ux4gColors.neutral500
                )
            }
        }

        // Badge Showcase
        Ux4gCard(modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(text = "Badges", style = Ux4gTheme.typography.tS_strong)

                // 1. Dot
                Text(text = "Dot", style = Ux4gTheme.typography.lS_strong)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Ux4gBadge(type = Ux4gBadgeType.Dot, containerColor = Ux4gColors.primary)
                    Ux4gBadge(type = Ux4gBadgeType.Dot, containerColor = Ux4gColors.secondary)
                    Ux4gBadge(type = Ux4gBadgeType.Dot, containerColor = Ux4gColors.green)
                    Ux4gBadge(type = Ux4gBadgeType.Dot, containerColor = Ux4gColors.gold)
                    Ux4gBadge(type = Ux4gBadgeType.Dot, containerColor = Ux4gColors.red)
                    Ux4gBadge(type = Ux4gBadgeType.Dot, containerColor = Ux4gColors.blue)
                    Ux4gBadge(type = Ux4gBadgeType.Dot, containerColor = Ux4gColors.cyan)
                    Ux4gBadge(type = Ux4gBadgeType.Dot, containerColor = Ux4gColors.neutral600)
                    Ux4gBadge(
                        type = Ux4gBadgeType.Dot,
                        containerColor = Ux4gColors.neutral300,
                        showBorder = true
                    )
                }

                // 2. Count – Single Digit limit
                Text(text = "Count – SingleDigit (9+)", style = Ux4gTheme.typography.lS_strong)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Ux4gBadge(type = Ux4gBadgeType.Text(3))
                    Ux4gBadge(type = Ux4gBadgeType.Text(9))
                    Ux4gBadge(type = Ux4gBadgeType.Text(10))
                    Ux4gBadge(type = Ux4gBadgeType.Text(32))
                }

                // 3. Count – Double Digit limit
                Text(text = "Count – DoubleDigit (99+)", style = Ux4gTheme.typography.lS_strong)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Ux4gBadge(
                        type = Ux4gBadgeType.Text(99, Ux4gBadgeLimit.DoubleDigit),
                        containerColor = Ux4gColors.secondary
                    )
                    Ux4gBadge(
                        type = Ux4gBadgeType.Text(100, Ux4gBadgeLimit.DoubleDigit),
                        containerColor = Ux4gColors.green
                    )
                    Ux4gBadge(
                        type = Ux4gBadgeType.Text(150, Ux4gBadgeLimit.DoubleDigit),
                        containerColor = Ux4gColors.red
                    )
                }

                // 4. Count – Custom colors
                Text(text = "Count – Colors", style = Ux4gTheme.typography.lS_strong)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Ux4gBadge(type = Ux4gBadgeType.Text(7), containerColor = Ux4gColors.primary)
                    Ux4gBadge(type = Ux4gBadgeType.Text(15), containerColor = Ux4gColors.gold)
                    Ux4gBadge(type = Ux4gBadgeType.Text(42), containerColor = Ux4gColors.neutral600)
                    Ux4gBadge(
                        type = Ux4gBadgeType.Text(8),
                        containerColor = Ux4gColors.neutral300,
                        contentColor = Ux4gColors.neutral900
                    )
                    Ux4gBadge(
                        type = Ux4gBadgeType.Text(21),
                        containerColor = Ux4gColors.red,
                        contentColor = Ux4gColors.white
                    )
                }

                // 5. Label
                Text(text = "Label", style = Ux4gTheme.typography.lS_strong)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Ux4gBadge(
                        type = Ux4gBadgeType.Label("New"),
                        containerColor = Ux4gColors.primary
                    )
                    Ux4gBadge(
                        type = Ux4gBadgeType.Label("Beta"),
                        containerColor = Ux4gColors.secondary
                    )
                    Ux4gBadge(
                        type = Ux4gBadgeType.Label("Live"),
                        containerColor = Ux4gColors.green
                    )
                    Ux4gBadge(
                        type = Ux4gBadgeType.Label("Hot"),
                        containerColor = Ux4gColors.red
                    )
                    Ux4gBadge(
                        type = Ux4gBadgeType.Label("Info"),
                        containerColor = Ux4gColors.blue
                    )
                    Ux4gBadge(
                        type = Ux4gBadgeType.Label("Draft"),
                        containerColor = Ux4gColors.neutral600
                    )
                    Ux4gBadge(
                        type = Ux4gBadgeType.Label("VIP"),
                        containerColor = Ux4gColors.gold,
                        contentColor = Ux4gColors.neutral900
                    )
                }

                // 6. Icon
                Text(text = "Icon", style = Ux4gTheme.typography.lS_strong)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Ux4gBadge(
                        type = Ux4gBadgeType.Icon(R.drawable.ic_tick),
                        containerColor = Ux4gColors.primary
                    )
                    Ux4gBadge(
                        type = Ux4gBadgeType.Icon(R.drawable.ic_tick),
                        containerColor = Ux4gColors.green
                    )
                    Ux4gBadge(
                        type = Ux4gBadgeType.Icon(R.drawable.ic_tick),
                        containerColor = Ux4gColors.red,
                        contentColor = Ux4gColors.white
                    )
                    Ux4gBadge(
                        type = Ux4gBadgeType.Icon(R.drawable.ic_i_desc),
                        containerColor = Ux4gColors.blue
                    )
                    Ux4gBadge(
                        type = Ux4gBadgeType.Icon(R.drawable.ic_warning_triangle),
                        containerColor = Ux4gColors.gold,
                        contentColor = Ux4gColors.neutral900
                    )
                    Ux4gBadge(
                        type = Ux4gBadgeType.Icon(R.drawable.ic_error_circle_red),
                        containerColor = Ux4gColors.neutral300,
                        contentColor = Ux4gColors.red
                    )
                }

                // 7. Ready to use
                Text(text = "Ready To Use", style = Ux4gTheme.typography.lS_strong)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Ux4gBadge(type = Ux4gBadgeType.ReadyToUse(Ux4gBadgeResource.Verification))
                    Ux4gBadge(type = Ux4gBadgeType.ReadyToUse(Ux4gBadgeResource.Star))
                    Ux4gBadge(type = Ux4gBadgeType.ReadyToUse(Ux4gBadgeResource.Shield))
                    Ux4gBadge(
                        type = Ux4gBadgeType.ReadyToUse(Ux4gBadgeResource.Shield),
                        showBorder = true
                    )
                }

                // 8. With border
                Text(text = "With Border", style = Ux4gTheme.typography.lS_strong)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Ux4gBadge(
                        type = Ux4gBadgeType.Text(12),
                        containerColor = Ux4gColors.primary,
                        showBorder = true
                    )
                    Ux4gBadge(
                        type = Ux4gBadgeType.Label("New"),
                        containerColor = Ux4gColors.green,
                        showBorder = true,
                        borderColor = Ux4gColors.green600
                    )
                    Ux4gBadge(
                        type = Ux4gBadgeType.Icon(R.drawable.ic_tick),
                        containerColor = Ux4gColors.secondary,
                        showBorder = true
                    )
                    Ux4gBadge(
                        type = Ux4gBadgeType.Dot,
                        containerColor = Ux4gColors.red,
                        showBorder = true,
                        borderColor = Ux4gColors.red600
                    )
                }

                // 9. Overlay on child
                Text(text = "Overlay on Child", style = Ux4gTheme.typography.lS_strong)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(24.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Ux4gBadge(type = Ux4gBadgeType.Text(99)) {
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .background(
                                    Ux4gColors.neutral300,
                                    RoundedCornerShape(8.dp)
                                )
                        )
                    }
                    Ux4gBadge(
                        type = Ux4gBadgeType.Dot,
                        containerColor = Ux4gColors.red,
                        alignment = Ux4gBadgeAlignment.TopStart
                    ) {
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .background(
                                    Ux4gColors.neutral300,
                                    RoundedCornerShape(8.dp)
                                )
                        )
                    }
                    Ux4gBadge(
                        type = Ux4gBadgeType.Label("New"),
                        containerColor = Ux4gColors.green,
                        alignment = Ux4gBadgeAlignment.BottomEnd,
                        showBorder = true
                    ) {
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .background(
                                    Ux4gColors.neutral300,
                                    RoundedCornerShape(8.dp)
                                )
                        )
                    }
                    Ux4gBadge(
                        type = Ux4gBadgeType.Icon(R.drawable.ic_tick),
                        containerColor = Ux4gColors.primary,
                        alignment = Ux4gBadgeAlignment.TopEnd,
                        showBorder = true
                    ) {
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .background(
                                    Ux4gColors.primary50,
                                    RoundedCornerShape(8.dp)
                                )
                        )
                    }
                }
            }
        }

        // Avatar Variants Showcase (matching requested image)
        Ux4gCard(modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                Text(text = "Avatar Varieties", style = Ux4gTheme.typography.tS_strong)

                val owlUrl = "https://miro.medium.com/v2/resize:fill:72:72/1*NfVk3LQfhRaDFW8qZ4MhCQ.jpeg"

                // Row 1: Base Avatars & Status Avatars
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(24.dp)
                ) {
                    // Base Avatars
                    Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text(text = "Base Avatars", style = Ux4gTheme.typography.lS_strong)
                        Ux4gCard(
                            modifier = Modifier.fillMaxWidth(),
                            backgroundColor = Color.White,
                            borderColor = Ux4gColors.neutral200,
                            borderWidth = 1.dp
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                horizontalArrangement = Arrangement.spacedBy(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Ux4gAvatar(size = Ux4gAvatarSize.M, imageUrl = owlUrl)
                                Ux4gAvatar(size = Ux4gAvatarSize.M, initials = "JD")
                                Ux4gAvatar(size = Ux4gAvatarSize.M, icon = Ux4gIcons.Person)
                            }
                        }
                    }

                    // Status Avatars
                    Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text(text = "Status Avatars", style = Ux4gTheme.typography.lS_strong)
                        Ux4gCard(
                            modifier = Modifier.fillMaxWidth(),
                            backgroundColor = Color.White,
                            borderColor = Ux4gColors.neutral200,
                            borderWidth = 1.dp
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                horizontalArrangement = Arrangement.spacedBy(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Ux4gStatusAvatar(size = Ux4gAvatarSize.M, imageUrl = owlUrl, variant = Ux4gStatusAvatarVariant.Success)
                                Ux4gStatusAvatar(size = Ux4gAvatarSize.M, imageUrl = owlUrl, variant = Ux4gStatusAvatarVariant.Warning)
                            }
                        }
                    }
                }

                // Row 2: Avatar Groups & Profile Avatars
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(24.dp)
                ) {
                    // Avatar Groups
                    Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text(text = "Avatar Groups", style = Ux4gTheme.typography.lS_strong)
                        Ux4gCard(
                            modifier = Modifier.fillMaxWidth(),
                            backgroundColor = Color.White,
                            borderColor = Ux4gColors.neutral200,
                            borderWidth = 1.dp
                        ) {
                            Box(modifier = Modifier.padding(12.dp)) {
                                Ux4gAvatarGroup(
                                    size = Ux4gAvatarSize.M,
                                    maxLimit = 4,
                                    items = listOf(
                                        Ux4gAvatarGroupItem(imageUrl = owlUrl),
                                        Ux4gAvatarGroupItem(imageUrl = owlUrl),
                                        Ux4gAvatarGroupItem(imageUrl = owlUrl),
                                        Ux4gAvatarGroupItem(imageUrl = owlUrl),
                                        Ux4gAvatarGroupItem(imageUrl = owlUrl),
                                        Ux4gAvatarGroupItem(imageUrl = owlUrl)
                                    )
                                )
                            }
                        }
                    }

                    // Profile Avatars
                    Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text(text = "Profile Avatars", style = Ux4gTheme.typography.lS_strong)
                        Ux4gCard(
                            modifier = Modifier.fillMaxWidth(),
                            backgroundColor = Color.White,
                            borderColor = Ux4gColors.neutral200,
                            borderWidth = 1.dp
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                horizontalArrangement = Arrangement.spacedBy(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Ux4gProfileAvatar(
                                    size = Ux4gAvatarSize.M,
                                    imageUrl = owlUrl,
                                    variant = Ux4gProfileAvatarVariant.Badge.Verified
                                )
                            }
                        }
                    }
                }
            }
        }

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
                        backgroundColor = Ux4gColors.secondary,
                        contentColor = Ux4gColors.neutral1000black,
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
            Ux4gLoader(size = 40.dp, percentage = 100f, color = Ux4gColors.primary)
            Ux4gLoader(size = 40.dp, percentage = 60f, color = Ux4gColors.secondary)
            Ux4gLoader(size = 40.dp, percentage = 30f, color = Ux4gColors.red)
            Ux4gLoader(color = Ux4gColors.green, percentage = 75f)
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
        Ux4gCard(
            modifier = Modifier.fillMaxWidth(),
            borderColor = Ux4gColors.neutral300,
            borderWidth = 1.dp
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "7. Selection Dropdowns",
                    style = defaultTypography.hS_strong,
                    color = Ux4gColors.primary
                )

                val mockOptions = remember {
                    listOf(
                        Ux4gDropdownOption("opt_1", "Option A (English)"),
                        Ux4gDropdownOption("opt_2", "Option B (Hindi)"),
                        Ux4gDropdownOption("opt_3", "Option C (Spanish)", leadingIcon = Ux4gIcons.Person),
                        Ux4gDropdownOption("opt_4", "Option D (French)")
                    )
                }
                var singleSelected by remember { mutableStateOf(listOf("opt_1")) }
                var multiSelected by remember { mutableStateOf(listOf("opt_1", "opt_3")) }

                // 7.1 Single & Multi Selection
                Text("7.1 Single & Multi Selection", style = defaultTypography.lS_strong)
                Ux4gSelectionDropdown(
                    options = mockOptions,
                    selectedOptionIds = singleSelected,
                    onSelectionChange = { singleSelected = it },
                    label = "Single Select",
                    isRequired = true,
                    description = "Choose one option from the list",
                    mode = Ux4gDropdownMode.Single
                )
                Ux4gSelectionDropdown(
                    options = mockOptions,
                    selectedOptionIds = multiSelected,
                    onSelectionChange = { multiSelected = it },
                    label = "Multi Select",
                    isRequired = true,
                    description = "Choose multiple options (chips displayed with remove action)",
                    mode = Ux4gDropdownMode.Multi
                )

                Ux4gDivider()

                // 7.2 Dropdown Sizes
                Text("7.2 Dropdown Sizes (Small, Medium, Large)", style = defaultTypography.lS_strong)
                var sizeSelectedS by remember { mutableStateOf(listOf("opt_1")) }
                var sizeSelectedM by remember { mutableStateOf(listOf("opt_2")) }
                var sizeSelectedL by remember { mutableStateOf(listOf("opt_3")) }

                Ux4gSelectionDropdown(
                    options = mockOptions,
                    selectedOptionIds = sizeSelectedS,
                    onSelectionChange = { sizeSelectedS = it },
                    label = "Size Small (32dp)",
                    size = Ux4gDropdownSize.S
                )
                Ux4gSelectionDropdown(
                    options = mockOptions,
                    selectedOptionIds = sizeSelectedM,
                    onSelectionChange = { sizeSelectedM = it },
                    label = "Size Medium (40dp)",
                    size = Ux4gDropdownSize.M
                )
                Ux4gSelectionDropdown(
                    options = mockOptions,
                    selectedOptionIds = sizeSelectedL,
                    onSelectionChange = { sizeSelectedL = it },
                    label = "Size Large (48dp)",
                    size = Ux4gDropdownSize.L
                )

                Ux4gDivider()

                // 7.3 Validation & Status States
                Text("7.3 Validation & Status States", style = defaultTypography.lS_strong)
                Ux4gSelectionDropdown(
                    options = mockOptions,
                    selectedOptionIds = emptyList(),
                    onSelectionChange = {},
                    label = "Error State",
                    isRequired = true,
                    status = Ux4gDropdownStatus.Error,
                    description = "This field is required and cannot be empty"
                )
                Ux4gSelectionDropdown(
                    options = mockOptions,
                    selectedOptionIds = listOf("opt_2"),
                    onSelectionChange = {},
                    label = "Warning State",
                    status = Ux4gDropdownStatus.Warning,
                    description = "Selected language has partial translation coverage"
                )
                Ux4gSelectionDropdown(
                    options = mockOptions,
                    selectedOptionIds = listOf("opt_1"),
                    onSelectionChange = {},
                    label = "Success State",
                    status = Ux4gDropdownStatus.Success,
                    description = "Language preference verified successfully"
                )
                Ux4gSelectionDropdown(
                    options = mockOptions,
                    selectedOptionIds = listOf("opt_1"),
                    onSelectionChange = {},
                    label = "Disabled State",
                    status = Ux4gDropdownStatus.Disabled,
                    description = "Selection is locked by administrator"
                )
            }
        }

        // 7.1. Searchable Dropdown Showcase
        Ux4gCard(
            modifier = Modifier.fillMaxWidth(),
            borderColor = Ux4gColors.neutral300,
            borderWidth = 1.dp
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "7.4 Searchable Selection Dropdowns",
                    style = defaultTypography.hS_strong,
                    color = Ux4gColors.primary
                )

                val stateOptions = remember {
                    listOf(
                        Ux4gDropdownOption("ap", "Andhra Pradesh"),
                        Ux4gDropdownOption("arp", "Arunachal Pradesh"),
                        Ux4gDropdownOption("as", "Assam"),
                        Ux4gDropdownOption("br", "Bihar"),
                        Ux4gDropdownOption("cg", "Chhattisgarh"),
                        Ux4gDropdownOption("ga", "Goa"),
                        Ux4gDropdownOption("gj", "Gujarat"),
                        Ux4gDropdownOption("hp", "Himachal Pradesh"),
                        Ux4gDropdownOption("jk", "Jammu and Kashmir"),
                        Ux4gDropdownOption("ka", "Karnataka"),
                        Ux4gDropdownOption("kl", "Kerala"),
                        Ux4gDropdownOption("mh", "Maharashtra"),
                        Ux4gDropdownOption("mp", "Madhya Pradesh"),
                        Ux4gDropdownOption("tn", "Tamil Nadu"),
                        Ux4gDropdownOption("up", "Uttar Pradesh"),
                        Ux4gDropdownOption("wb", "West Bengal")
                    )
                }
                var searchableSingleSelected by remember { mutableStateOf(listOf("br")) }
                var searchableMultiSelected by remember { mutableStateOf(listOf("arp", "br")) }

                Text("Filter Mode: StartsWithPerTerm", style = defaultTypography.lS_strong)
                Ux4gSelectionDropdown(
                    options = stateOptions,
                    selectedOptionIds = searchableSingleSelected,
                    onSelectionChange = { searchableSingleSelected = it },
                    label = "Single Select - Starts With Word (Per Term)",
                    description = "Typing 'Pra' matches 'Pradesh' anywhere",
                    searchEnabled = true,
                    filterType = Ux4gDropdownFilterType.StartsWithPerTerm,
                    mode = Ux4gDropdownMode.Single
                )

                Text("Filter Mode: Contains", style = defaultTypography.lS_strong)
                Ux4gSelectionDropdown(
                    options = stateOptions,
                    selectedOptionIds = searchableSingleSelected,
                    onSelectionChange = { searchableSingleSelected = it },
                    label = "Single Select - Contains",
                    description = "Typing 'esh' matches 'Pradesh'",
                    searchEnabled = true,
                    filterType = Ux4gDropdownFilterType.Contains,
                    mode = Ux4gDropdownMode.Single
                )

                Text("Filter Mode: StartsWith (Strict)", style = defaultTypography.lS_strong)
                Ux4gSelectionDropdown(
                    options = stateOptions,
                    selectedOptionIds = searchableSingleSelected,
                    onSelectionChange = { searchableSingleSelected = it },
                    label = "Single Select - Starts With",
                    description = "Typing 'And' matches 'Andhra Pradesh'",
                    searchEnabled = true,
                    filterType = Ux4gDropdownFilterType.StartsWith,
                    mode = Ux4gDropdownMode.Single
                )

                Text("Searchable Multi Select", style = defaultTypography.lS_strong)
                Ux4gSelectionDropdown(
                    options = stateOptions,
                    selectedOptionIds = searchableMultiSelected,
                    onSelectionChange = { searchableMultiSelected = it },
                    label = "Multi Select - Searchable",
                    description = "Search and select multiple states",
                    searchEnabled = true,
                    filterType = Ux4gDropdownFilterType.Contains,
                    mode = Ux4gDropdownMode.Multi
                )
            }
        }

        // 7.2. Action Dropdown Showcase
        Ux4gCard(
            modifier = Modifier.fillMaxWidth(),
            borderColor = Ux4gColors.neutral300,
            borderWidth = 1.dp
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "7.5 Action Dropdowns",
                    style = defaultTypography.hS_strong,
                    color = Ux4gColors.primary
                )

                val actionOptions = remember {
                    listOf(
                        Ux4gActionDropdownOption("1", "Account Settings", showTrailingArrow = true),
                        Ux4gActionDropdownOption("2", "Profile & Privacy", showTrailingArrow = true),
                        Ux4gActionDropdownOption("3", "Notification Preferences", showTrailingArrow = true),
                        Ux4gActionDropdownOption("4", "Current Active Plan", showTrailingArrow = false)
                    )
                }

                var selectedActionId by remember { mutableStateOf("4") }

                Text("Icon Button Trigger (More Options)", style = defaultTypography.lS_strong)
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Ux4gActionDropdown(
                        options = actionOptions,
                        selectedOptionId = selectedActionId,
                        onOptionClick = { selectedActionId = it.id }
                    ) { onClick ->
                        Ux4gIconButton(
                            onClick = onClick,
                            type = Ux4gIconButtonStyle.Tonal,
                            size = Ux4gIconButtonSize.M
                        ) {
                            Icon(
                                imageVector = Icons.Default.MoreVert,
                                contentDescription = "Menu Options",
                                modifier = Modifier.size(24.dp),
                                tint = Ux4gColors.neutral900
                            )
                        }
                    }
                    Text(
                        text = "Selected Action ID: $selectedActionId",
                        style = defaultTypography.bS_default,
                        color = Ux4gColors.neutral700
                    )
                }

                Text("Button Trigger (Custom Actions)", style = defaultTypography.lS_strong)
                Ux4gActionDropdown(
                    options = actionOptions,
                    selectedOptionId = selectedActionId,
                    onOptionClick = { selectedActionId = it.id }
                ) { onClick ->
                    Ux4gButton(
                        text = "Open Quick Actions",
                        variant = Ux4gButtonVariant.Outline,
                        onClick = onClick
                    )
                }
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
                            backgroundColor = Ux4gColors.primary,
                            contentColor = Ux4gColors.white
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
                                        backgroundColor = Ux4gColors.primary
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
                        color = Ux4gColors.primary500,
                        thickness = 1.5.dp
                )

                // With label
                Text("Horizontal – With Label", style = Ux4gTheme.typography.lM_default)
                Ux4gDivider(
                        label = {
                            Text(
                                    text = "OR",
                                    style = Ux4gTheme.typography.lS_strong,
                                    color = Ux4gColors.neutral500
                            )
                        }
                )

                // Thick error color with indent
                Text(
                        "Horizontal – Error 2dp, Start Indent 32dp",
                        style = Ux4gTheme.typography.lM_default
                )
                Ux4gDivider(thickness = 2.dp, color = Ux4gColors.red500, startIndent = 32.dp)

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
                            color = Ux4gColors.primary500,
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
                        backgroundColor = Ux4gColors.red500
                ) { Text("Open Destructive Modal (Centered)") }

                Ux4gButton(
                        onClick = { showMinimalModal = true },
                        modifier = Modifier.fillMaxWidth(),
                        backgroundColor = Ux4gColors.neutral700
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
                        backgroundColor = Ux4gColors.primary700
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
                        backgroundColor = Ux4gColors.red700
                ) { Text("Open Icon Modal (No image + Split footer)") }

                if (showIconModal) {
                    Ux4gModal(
                            onDismiss = { showIconModal = false },
                            headerImageStyle = Ux4gModalHeaderImage.None,
                            leadingItem = Ux4gModalLeadingItem.Icon,
                            leadingIcon = Ux4gIcons.info_circle,
                            leadingIconTint = Ux4gColors.red500,
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
                                    containerColor = Ux4gColors.primary100,
                                    contentColor = Ux4gColors.primary700
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
                        borderColor = Ux4gColors.primary,
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
                        backgroundColor = Ux4gColors.primary50,
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
                    backgroundColor = Ux4gColors.neutral800
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
                        backgroundColor = Ux4gColors.green500
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
                        backgroundColor = Ux4gColors.red500
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
                        backgroundColor = Ux4gColors.gold600
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
                        backgroundColor = Ux4gColors.cyan600
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
                        backgroundColor = Ux4gColors.neutral700
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
                    backgroundColor = Ux4gColors.primary700
                ) { Text("Open Full Bottom Sheet (Justified)") }

                Ux4gButton(
                    onClick = { showNonDraggableSheet = true },
                    modifier = Modifier.fillMaxWidth(),
                    backgroundColor = Ux4gColors.neutral700
                ) { Text("Open Non-Draggable Sheet") }

                Ux4gButton(
                    onClick = { showCustomContentSheet = true },
                    modifier = Modifier.fillMaxWidth(),
                    backgroundColor = Ux4gColors.green700
                ) { Text("Open Custom Content Sheet") }

                Ux4gButton(
                    onClick = { showLongContentSheet = true },
                    modifier = Modifier.fillMaxWidth(),
                    backgroundColor = Ux4gColors.cyan700
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
                                color = Ux4gColors.neutral700
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
                                    color = Ux4gColors.neutral500
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

        // 19. Card Component Showcase
        Ux4gCard(modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                Text(text = "Card Component Showcase", style = Ux4gTheme.typography.tS_strong)

                // 19.1 Rich Card Matching User Design
                Text(text = "Rich Card (Full Spec)", style = Ux4gTheme.typography.lS_strong)
                Ux4gCard(
                    mediaImageUrl = "https://picsum.photos/seed/canyon/600/300",
                    mediaHeight = 180.dp,
                    mediaLabelText = "Label",
                    mediaLabelLeading = {
                        androidx.compose.material3.Icon(
                            imageVector = androidx.compose.material.icons.Icons.Filled.Category,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(12.dp)
                        )
                    },
                    mediaTopEndAction = {
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .background(Color(0x66000000)),
                            contentAlignment = Alignment.Center
                        ) {
                            androidx.compose.material3.Icon(
                                imageVector = androidx.compose.material.icons.Icons.Filled.Category,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    },
                    avatar = {
                        Ux4gAvatar(
                            size = Ux4gAvatarSize.M,
                            initials = "JD",
                            containerColor = Ux4gColors.primary100,
                            contentColor = Ux4gColors.primary700
                        )
                    },
                    title = "Title",
                    subtitle = "Subtitle",
                    headerTrailing = {
                        androidx.compose.material3.Icon(
                            imageVector = androidx.compose.material.icons.Icons.Filled.Category,
                            contentDescription = null,
                            tint = Ux4gColors.primary,
                            modifier = Modifier.size(24.dp)
                        )
                    },
                    statusContent = {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            repeat(3) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    androidx.compose.material3.Icon(
                                        imageVector = androidx.compose.material.icons.Icons.Filled.Category,
                                        contentDescription = null,
                                        tint = Ux4gColors.neutral500,
                                        modifier = Modifier.size(14.dp)
                                    )
                                    Text(
                                        text = "Label",
                                        style = defaultTypography.bS_default,
                                        color = Ux4gColors.neutral600
                                    )
                                }
                            }
                        }
                    },
                    body = "Lorem ipsum is a dummy or placeholder text commonly used in graphic design, publishing, and web development.",
                    bottomChipsContent = {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .horizontalScroll(rememberScrollState()),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            repeat(4) {
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(4.dp))
                                        .background(Ux4gColors.neutral100)
                                        .padding(horizontal = 8.dp, vertical = 6.dp)
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                                    ) {
                                        androidx.compose.material3.Icon(
                                            imageVector = androidx.compose.material.icons.Icons.Filled.Category,
                                            contentDescription = null,
                                            tint = Ux4gColors.neutral600,
                                            modifier = Modifier.size(12.dp)
                                        )
                                        Text(
                                            text = "Label",
                                            style = defaultTypography.lS_default,
                                            color = Ux4gColors.neutral700
                                        )
                                    }
                                }
                            }
                        }
                    },
                    footerContent = {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Ux4gButton(
                                onClick = {},
                                modifier = Modifier.weight(1f)
                            ) {
                                androidx.compose.material3.Icon(
                                    imageVector = androidx.compose.material.icons.Icons.Filled.Add,
                                    contentDescription = null,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Button", style = defaultTypography.bM_strong)
                                Spacer(modifier = Modifier.width(4.dp))
                                androidx.compose.material3.Icon(
                                    imageVector = androidx.compose.material.icons.Icons.Filled.ArrowDropDown,
                                    contentDescription = null,
                                    modifier = Modifier.size(18.dp)
                                )
                            }

                            com.ux4g.designsystemv1.components.buttons.Ux4gOutlineButton(
                                onClick = {},
                                modifier = Modifier.weight(1f)
                            ) {
                                androidx.compose.material3.Icon(
                                    imageVector = androidx.compose.material.icons.Icons.Filled.Add,
                                    contentDescription = null,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Button", style = defaultTypography.bM_strong)
                                Spacer(modifier = Modifier.width(4.dp))
                                androidx.compose.material3.Icon(
                                    imageVector = androidx.compose.material.icons.Icons.Filled.ArrowDropDown,
                                    contentDescription = null,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                    }
                )

                Ux4gDivider()

                // 19.2 Horizontal Card Layout
                Text(text = "Horizontal Layout Card", style = Ux4gTheme.typography.lS_strong)
                Ux4gCard(
                    direction = Ux4gCardDirection.Horizontal,
                    borderColor = Ux4gColors.neutral300,
                    borderWidth = 1.dp,
                    mediaImageUrl = "https://picsum.photos/seed/canyon/300/300",
                    title = "Horizontal Card",
                    subtitle = "Image on the left",
                    body = "Cards can display media horizontally for compact list feeds and desktop layouts.",
                    footerType = Ux4gCardFooterType.SecondaryOnly,
                    secondaryButtonText = "Learn More"
                )

                Ux4gDivider()

                // 19.3 Dynamic Contrast / Elevated Dark Card
                Text(text = "Elevated Dark Card (Auto Contrast)", style = defaultTypography.lS_strong)
                Ux4gCard(
                    elevation = 4.dp,
                    backgroundColor = Ux4gColors.primary700,
                    title = "Dark Surface Card",
                    subtitle = "Automated text luminance",
                    body = "All title, subtitle, chip, and body styles automatically calibrate to high-contrast white text.",
                    bottomChips = listOf("Jetpack Compose", "UX4G", "Android"),
                    footerType = Ux4gCardFooterType.PrimaryOnly,
                    footerAlignment = Ux4gCardFooterAlignment.Right,
                    primaryButtonText = "Get Started"
                )
            }

            // 20. Carousel Component Showcase
            Ux4gCard(
                modifier = Modifier.fillMaxWidth(),
                borderColor = Ux4gColors.neutral300,
                borderWidth = 1.dp
            ) {
                Text(
                    text = "20. Carousel (Image / Widget Slider)",
                    style = defaultTypography.hS_strong,
                    color = Ux4gColors.primary
                )

                // 20.1 Hero Banner Carousel (Screenshot 1)
                Text(
                    text = "20.1 Hero Banner Carousel (Gradient & Action Button)",
                    style = defaultTypography.lS_strong
                )

                Ux4gCarousel(
                    height = 240.dp,
                    autoPlay = true,
                    autoPlayIntervalMs = 3500L,
                    showPaginationArrows = true,
                    activePaginationColor = Color(0xFF5E3CEE),
                    inactivePaginationColor = Color(0xFFDCD6FD),
                    paginationArrowBackgroundColor = Color(0xFFDCD6FD),
                    paginationArrowColor = Color(0xFF5E3CEE),
                    items = listOf(
                        {
                            Ux4gHeroCarouselSlide(
                                gradientColors = listOf(Color(0xFF5E3CEE), Color(0xFF7C3AED)),
                                badgeText = "FEATURED",
                                title = "Digital Services Portal 2.0",
                                description = "Seamlessly access government citizen services with single sign-on and instant verification.",
                                buttonText = "Explore Services"
                            )
                        },
                        {
                            Ux4gHeroCarouselSlide(
                                gradientColors = listOf(Color(0xFF0D9488), Color(0xFF0284C7)),
                                badgeText = "NEW UPDATE",
                                title = "Biometric Identity Capture",
                                description = "Fast, secure face verification integration with real-time liveness checks.",
                                buttonText = "View Documentation"
                            )
                        },
                        {
                            Ux4gHeroCarouselSlide(
                                gradientColors = listOf(Color(0xFFD97706), Color(0xFFE11D48)),
                                badgeText = "POPULAR",
                                title = "Unified Design System",
                                description = "Production-ready accessible Android UI components built for digital infrastructure.",
                                buttonText = "Get Started"
                            )
                        }
                    )
                )

                Ux4gDivider()

                // 20.2 Image Gallery Carousel (Screenshot 2)
                Text(
                    text = "20.2 Image Gallery Carousel (Dark Image Overlay)",
                    style = defaultTypography.lS_strong
                )

                Ux4gCarousel(
                    height = 240.dp,
                    autoPlay = true,
                    autoPlayIntervalMs = 4000L,
                    showPaginationArrows = true,
                    activePaginationColor = Color(0xFF5E3CEE),
                    inactivePaginationColor = Color.White.copy(alpha = 0.85f),
                    paginationArrowBackgroundColor = Color(0xFFDCD6FD),
                    paginationArrowColor = Color(0xFF5E3CEE),
                    items = listOf(
                        {
                            Ux4gImageCarouselSlide(
                                imageUrl = "https://images.unsplash.com/photo-1451187580459-43490279c0fa?auto=format&fit=crop&w=800&q=80",
                                badgeText = "NETWORK",
                                title = "Global Cloud Infrastructure",
                                subtitle = "High availability and low latency cross-region deployment."
                            )
                        },
                        {
                            Ux4gImageCarouselSlide(
                                imageUrl = "https://images.unsplash.com/photo-1579546929518-9e396f3cc809?auto=format&fit=crop&w=800&q=80",
                                badgeText = "DESIGN",
                                title = "Modern UI System Architecture",
                                subtitle = "Crafted with accessible design tokens & fluid responsive components."
                            )
                        },
                        {
                            Ux4gImageCarouselSlide(
                                imageUrl = "https://images.unsplash.com/photo-1518770660439-4636190af475?auto=format&fit=crop&w=800&q=80",
                                badgeText = "SECURITY",
                                title = "Cryptographic Hardware Modules",
                                subtitle = "Next-generation secure key storage and identity management."
                            )
                        }
                    )
                )

                Ux4gDivider()

                // 20.3 Card Slider with Capsule Pagination
                Text(
                    text = "20.3 Card Slider with Capsule Pagination",
                    style = defaultTypography.lS_strong
                )

                Ux4gCarousel(
                    height = 160.dp,
                    autoPlay = false,
                    paginationVariant = Ux4gPaginationVariant.Capsule,
                    itemCount = 4
                ) { index ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 8.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Ux4gColors.primary.copy(alpha = 0.1f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text(
                                text = "Slide ${index + 1}",
                                style = defaultTypography.hM_strong,
                                color = Ux4gColors.primary
                            )
                            Text(
                                text = "(Replace with desired banner content)",
                                style = defaultTypography.lS_default,
                                color = Ux4gColors.neutral600
                            )
                        }
                    }
                }
            }

            // 21. Checkbox Component Showcase
            Ux4gCard(
                modifier = Modifier.fillMaxWidth(),
                borderColor = Ux4gColors.neutral300,
                borderWidth = 1.dp
            ) {
                Text(
                    text = "21. Checkbox",
                    style = defaultTypography.hS_strong,
                    color = Ux4gColors.primary
                )

                // 21.1 Sizes
                Text(
                    text = "21.1 Sizes (Small / Medium / Large)",
                    style = defaultTypography.lS_strong
                )

                var sizeSmall by remember { mutableStateOf<Boolean?>(true) }
                Ux4gCheckbox(
                    value = sizeSmall,
                    onValueChange = { sizeSmall = it },
                    label = "Small Checkbox",
                    size = Ux4gCheckboxSize.Small
                )

                var sizeMedium by remember { mutableStateOf<Boolean?>(false) }
                Ux4gCheckbox(
                    value = sizeMedium,
                    onValueChange = { sizeMedium = it },
                    label = "Medium Checkbox",
                    size = Ux4gCheckboxSize.Medium
                )

                var sizeLarge by remember { mutableStateOf<Boolean?>(true) }
                Ux4gCheckbox(
                    value = sizeLarge,
                    onValueChange = { sizeLarge = it },
                    label = "Large Checkbox",
                    size = Ux4gCheckboxSize.Large
                )

                Ux4gDivider()

                // 21.2 Tri-State (On / Off / Indeterminate)
                Text(
                    text = "21.2 Tri-State (On / Off / Indeterminate)",
                    style = defaultTypography.lS_strong
                )

                var triChecked by remember { mutableStateOf<Boolean?>(true) }
                Ux4gCheckbox(
                    value = triChecked,
                    onValueChange = { triChecked = it },
                    label = "Checked (On)"
                )

                var triUnchecked by remember { mutableStateOf<Boolean?>(false) }
                Ux4gCheckbox(
                    value = triUnchecked,
                    onValueChange = { triUnchecked = it },
                    label = "Unchecked (Off)"
                )

                var triIndet by remember { mutableStateOf<Boolean?>(null) }
                Ux4gCheckbox(
                    value = triIndet,
                    onValueChange = { triIndet = it },
                    label = "Indeterminate"
                )

                Ux4gDivider()

                // 21.3 Required with Error
                Text(
                    text = "21.3 Required + Error State",
                    style = defaultTypography.lS_strong
                )

                var reqErr by remember { mutableStateOf<Boolean?>(false) }
                Ux4gCheckbox(
                    value = reqErr,
                    onValueChange = { reqErr = it },
                    label = "Accept Terms & Conditions",
                    isRequired = true,
                    hasError = true,
                    description = "You must accept the terms to proceed",
                    descriptionVariant = Ux4gCheckboxDescriptionVariant.Error
                )

                Ux4gDivider()

                // 21.4 Description Variants (Helper / Error / Warning / Success)
                Text(
                    text = "21.4 Description Variants",
                    style = defaultTypography.lS_strong
                )

                var helperCb by remember { mutableStateOf<Boolean?>(true) }
                Ux4gCheckbox(
                    value = helperCb,
                    onValueChange = { helperCb = it },
                    label = "Helper Description",
                    description = "This provides additional context",
                    descriptionVariant = Ux4gCheckboxDescriptionVariant.Helper
                )

                var errorCb by remember { mutableStateOf<Boolean?>(false) }
                Ux4gCheckbox(
                    value = errorCb,
                    onValueChange = { errorCb = it },
                    label = "Error Description",
                    description = "This field has a validation error",
                    descriptionVariant = Ux4gCheckboxDescriptionVariant.Error
                )

                var warningCb by remember { mutableStateOf<Boolean?>(true) }
                Ux4gCheckbox(
                    value = warningCb,
                    onValueChange = { warningCb = it },
                    label = "Warning Description",
                    description = "Please review before continuing",
                    descriptionVariant = Ux4gCheckboxDescriptionVariant.Warning
                )

                var successCb by remember { mutableStateOf<Boolean?>(true) }
                Ux4gCheckbox(
                    value = successCb,
                    onValueChange = { successCb = it },
                    label = "Success Description",
                    description = "Successfully validated",
                    descriptionVariant = Ux4gCheckboxDescriptionVariant.Success
                )

                Ux4gDivider()

                // 21.5 Disabled States
                Text(
                    text = "21.5 Disabled States",
                    style = defaultTypography.lS_strong
                )

                Ux4gCheckbox(
                    value = false,
                    onValueChange = {},
                    label = "Disabled Unchecked",
                    isRequired = true,
                    enabled = false
                )

                Ux4gCheckbox(
                    value = true,
                    onValueChange = {},
                    label = "Disabled Checked",
                    isRequired = true,
                    enabled = false
                )

                Ux4gCheckbox(
                    value = null,
                    onValueChange = {},
                    label = "Disabled Indeterminate",
                    isRequired = true,
                    enabled = false
                )
            }

            // 22. Chips Component Showcase
            Ux4gCard(
                modifier = Modifier.fillMaxWidth(),
                borderColor = Ux4gColors.neutral300,
                borderWidth = 1.dp
            ) {
                Text(
                    text = "22. Chips",
                    style = defaultTypography.hS_strong,
                    color = Ux4gColors.primary
                )

                // 22.1 Choice Chips
                Text(
                    text = "22.1 Choice Chips (Single / Multi Select, Sizes, Custom Colors)",
                    style = defaultTypography.lS_strong
                )

                var selectedChoice by remember { mutableStateOf("Design") }
                val choiceCategories = listOf("Design", "Development", "Marketing", "Research")

                Text(text = "Single Select Choice Chips (Medium):", style = defaultTypography.lS_default, color = Ux4gColors.neutral600)
                Row(
                    modifier = Modifier.horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    choiceCategories.forEach { category ->
                        Ux4gChoiceChip(
                            text = category,
                            selected = selectedChoice == category,
                            onClick = { selectedChoice = category },
                            size = Ux4gChoiceChipSize.M
                        )
                    }
                }

                var selectedSmallChoice by remember { mutableStateOf("Android") }
                val techOptions = listOf("Android", "Flutter", "Compose", "Kotlin")

                Text(text = "Small Choice Chips with Leading/Trailing Icons:", style = defaultTypography.lS_default, color = Ux4gColors.neutral600)
                Row(
                    modifier = Modifier.horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    techOptions.forEach { tech ->
                        val isSelected = selectedSmallChoice == tech
                        Ux4gChoiceChip(
                            text = tech,
                            selected = isSelected,
                            onClick = { selectedSmallChoice = tech },
                            size = Ux4gChoiceChipSize.S,
                            leadingContent = if (isSelected) {
                                {
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = null,
                                        modifier = Modifier.size(12.dp),
                                        tint = Ux4gColors.neutral50
                                    )
                                }
                            } else null,
                            trailingContent = {
                                Icon(
                                    imageVector = Icons.Default.Star,
                                    contentDescription = null,
                                    modifier = Modifier.size(12.dp),
                                    tint = if (isSelected) Ux4gColors.neutral50 else Ux4gColors.neutral500
                                )
                            }
                        )
                    }
                }

                Ux4gDivider()

                // 22.2 Filter Chips
                Text(
                    text = "22.2 Filter Chips (Multi-select Filtering, 8% Primary Bg on Select)",
                    style = defaultTypography.lS_strong
                )

                var selectedFilters by remember { mutableStateOf(setOf("Trending", "Newest")) }
                val filterOptions = listOf("Trending", "Top Rated", "Newest", "Featured", "Free")

                Row(
                    modifier = Modifier.horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    filterOptions.forEach { filter ->
                        val isSelected = selectedFilters.contains(filter)
                        Ux4gFilterChip(
                            text = filter,
                            selected = isSelected,
                            onClick = {
                                selectedFilters = if (isSelected) {
                                    selectedFilters - filter
                                } else {
                                    selectedFilters + filter
                                }
                            },
                            size = Ux4gFilterChipSize.M,
                            leadingContent = if (isSelected) {
                                {
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = null,
                                        modifier = Modifier.size(16.dp),
                                        tint = Ux4gColors.primary
                                    )
                                }
                            } else null
                        )
                    }
                }

                var smallSelectedFilters by remember { mutableStateOf(setOf("UI/UX")) }
                val smallFilterOptions = listOf("UI/UX", "Backend", "Mobile", "Web", "Cloud")

                Text(text = "Small Filter Chips:", style = defaultTypography.lS_default, color = Ux4gColors.neutral600)
                Row(
                    modifier = Modifier.horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    smallFilterOptions.forEach { filter ->
                        val isSelected = smallSelectedFilters.contains(filter)
                        Ux4gFilterChip(
                            text = filter,
                            selected = isSelected,
                            onClick = {
                                smallSelectedFilters = if (isSelected) {
                                    smallSelectedFilters - filter
                                } else {
                                    smallSelectedFilters + filter
                                }
                            },
                            size = Ux4gFilterChipSize.S
                        )
                    }
                }

                Ux4gDivider()

                // 22.3 Input Chips
                Text(
                    text = "22.3 Input Chips (Dismissible, Sizes: Medium, Small, XS)",
                    style = defaultTypography.lS_strong
                )

                var inputChipsList by remember {
                    mutableStateOf(listOf("John Doe", "Jane Smith", "Alex Rivera", "Emma Watson"))
                }

                Text(text = "Interactive Input Chips (Click 'x' to remove):", style = defaultTypography.lS_default, color = Ux4gColors.neutral600)
                Row(
                    modifier = Modifier.horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    inputChipsList.forEach { user ->
                        Ux4gInputChip(
                            text = user,
                            onDismiss = {
                                inputChipsList = inputChipsList - user
                            },
                            size = Ux4gInputChipSize.M,
                            leadingContent = {
                                Icon(
                                    imageVector = Icons.Default.Person,
                                    contentDescription = null,
                                    modifier = Modifier.size(16.dp),
                                    tint = Ux4gColors.neutral700
                                )
                            }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    modifier = Modifier.horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Ux4gInputChip(text = "Medium (32dp)", onDismiss = {}, size = Ux4gInputChipSize.M)
                    Ux4gInputChip(text = "Small (28dp)", onDismiss = {}, size = Ux4gInputChipSize.S)
                    Ux4gInputChip(text = "XS (20dp)", onDismiss = {}, size = Ux4gInputChipSize.XS)
                }

                Ux4gDivider()

                // 22.4 Input Chip Field
                Text(
                    text = "22.4 Input Chip Field (Interactive Input & Dropdown Add)",
                    style = defaultTypography.lS_strong
                )

                var textInputValue by remember { mutableStateOf("") }
                var tagsList by remember { mutableStateOf(listOf("Kotlin", "Jetpack Compose")) }

                Text(text = "Text Input Mode (Type & press Add/Enter):", style = defaultTypography.lS_default, color = Ux4gColors.neutral600)
                Ux4gInputChipField(
                    value = textInputValue,
                    onValueChange = { textInputValue = it },
                    onAddChip = { newTag ->
                        if (newTag.isNotBlank()) {
                            tagsList = tagsList + newTag
                        }
                    },
                    placeholder = "Add a skill tag...",
                    chips = tagsList.mapIndexed { index, tag ->
                        {
                            Ux4gInputChip(
                                text = tag,
                                onDismiss = {
                                    tagsList = tagsList.filterIndexed { i, _ -> i != index }
                                },
                                size = Ux4gInputChipSize.S
                            )
                        }
                    }
                )

                var dropdownValue by remember { mutableStateOf("") }
                var selectedSkills by remember { mutableStateOf(listOf("Android SDK", "Material Design")) }

                Text(text = "Dropdown Mode (Select option to add chip):", style = defaultTypography.lS_default, color = Ux4gColors.neutral600)
                Ux4gInputChipField(
                    value = dropdownValue,
                    onValueChange = { dropdownValue = it },
                    onAddChip = { newSkill ->
                        if (newSkill.isNotBlank()) {
                            selectedSkills = selectedSkills + newSkill
                        }
                    },
                    isDropdown = true,
                    placeholder = "Select skill to add...",
                    dropdownOptions = listOf("Android SDK", "Material Design", "Coroutines", "Flow", "Ktor", "Hilt"),
                    chips = selectedSkills.mapIndexed { index, skill ->
                        {
                            Ux4gInputChip(
                                text = skill,
                                onDismiss = {
                                    selectedSkills = selectedSkills.filterIndexed { i, _ -> i != index }
                                },
                                size = Ux4gInputChipSize.S
                            )
                        }
                    }
                )

                Ux4gDivider()

                // 22.5 Chip Group Arrangements
                Text(
                    text = "22.5 Chip Group Arrangement (Wrap vs Horizontal)",
                    style = defaultTypography.lS_strong
                )

                val sampleTags = listOf("React", "Vue", "Angular", "Svelte", "Next.js", "Nuxt", "Remix", "Astro", "Gatsby", "Vite")

                Text(text = "Wrap Flow Layout (Multi-line):", style = defaultTypography.lS_default, color = Ux4gColors.neutral600)
                Ux4gChipGroup(
                    chips = sampleTags.map { tag ->
                        {
                            Ux4gFilterChip(
                                text = tag,
                                selected = tag == "React" || tag == "Next.js",
                                onClick = {},
                                size = Ux4gFilterChipSize.S
                            )
                        }
                    },
                    arrangement = Ux4gChipGroupArrangement.Wrap
                )

                Ux4gDivider()

                // 22.6 Disabled States
                Text(
                    text = "22.6 Disabled States (Opacity 50%)",
                    style = defaultTypography.lS_strong
                )

                Row(
                    modifier = Modifier.horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Ux4gChoiceChip(text = "Disabled Selected", selected = true, onClick = {}, enabled = false)
                    Ux4gChoiceChip(text = "Disabled Unselected", selected = false, onClick = {}, enabled = false)
                    Ux4gFilterChip(text = "Disabled Filter", selected = true, onClick = {}, enabled = false)
                    Ux4gInputChip(text = "Disabled Input", onDismiss = {}, enabled = false)
                }
            }

            // 23. Date Picker Component Showcase
            Ux4gCard(
                modifier = Modifier.fillMaxWidth(),
                borderColor = Ux4gColors.neutral300,
                borderWidth = 1.dp
            ) {
                Text(
                    text = "23. Date Picker",
                    style = defaultTypography.hS_strong,
                    color = Ux4gColors.primary
                )

                // 23.1 Single Date Picker Mode
                Text(
                    text = "23.1 Single Date Picker (Interactive Calendar Dialog)",
                    style = defaultTypography.lS_strong
                )

                var singleSelectedDate by remember { mutableStateOf<LocalDate?>(null) }
                Ux4gDatePicker(
                    label = "Date of Birth",
                    isRequired = true,
                    placeholder = "Select birth date",
                    selectedDate = singleSelectedDate,
                    onDateSelected = { singleSelectedDate = it },
                    description = if (singleSelectedDate != null) "Selected: $singleSelectedDate" else "Tap to choose a date from calendar dialog"
                )

                Ux4gDivider()

                // 23.2 Date Range Picker Mode
                Text(
                    text = "23.2 Date Range Picker (Start – End Date Selector)",
                    style = defaultTypography.lS_strong
                )

                var selectedRange by remember { mutableStateOf<Ux4gDateRange?>(null) }
                Ux4gDatePicker(
                    mode = Ux4gDatePickerMode.Range,
                    label = "Travel Duration",
                    isRequired = true,
                    placeholder = "Select date",
                    selectedDateRange = selectedRange,
                    onDateRangeSelected = { selectedRange = it },
                    description = if (selectedRange != null) "From ${selectedRange?.start} to ${selectedRange?.end}" else "Select start and end dates"
                )

                Ux4gDivider()

                // 23.3 Validation & Status States
                Text(
                    text = "23.3 Validation Statuses (Error, Warning, Success)",
                    style = defaultTypography.lS_strong
                )

                var errorDate by remember { mutableStateOf<LocalDate?>(null) }
                Ux4gDatePicker(
                    label = "Appointment Date (Error Status)",
                    isRequired = true,
                    selectedDate = errorDate,
                    onDateSelected = { errorDate = it },
                    status = Ux4gInputFieldStatus.Error,
                    description = "Date is required to book appointment"
                )

                var warningDate by remember { mutableStateOf<LocalDate?>(LocalDate.now().plusDays(2)) }
                Ux4gDatePicker(
                    label = "Event Date (Warning Status)",
                    selectedDate = warningDate,
                    onDateSelected = { warningDate = it },
                    status = Ux4gInputFieldStatus.Warning,
                    description = "High demand expected on this day"
                )

                var successDate by remember { mutableStateOf<LocalDate?>(LocalDate.now()) }
                Ux4gDatePicker(
                    label = "Verification Date (Success Status)",
                    selectedDate = successDate,
                    onDateSelected = { successDate = it },
                    status = Ux4gInputFieldStatus.Success,
                    description = "Date successfully verified and locked"
                )

                Ux4gDivider()

                // 23.4 Disabled State
                Text(
                    text = "23.4 Disabled State",
                    style = defaultTypography.lS_strong
                )

                Ux4gDatePicker(
                    label = "Disabled Date Picker",
                    selectedDate = LocalDate.now(),
                    enabled = false,
                    description = "This field cannot be edited"
                )
            }

            // 24. Divider Component Showcase
            Ux4gCard(
                modifier = Modifier.fillMaxWidth(),
                borderColor = Ux4gColors.neutral300,
                borderWidth = 1.dp
            ) {
                Text(
                    text = "24. Divider",
                    style = defaultTypography.hS_strong,
                    color = Ux4gColors.primary
                )

                // 24.1 Line Styles (Solid, Dashed, Dotted)
                Text(
                    text = "24.1 Line Styles (Solid, Dashed, Dotted)",
                    style = defaultTypography.lS_strong
                )

                Text("Solid Divider (Default):", style = defaultTypography.bS_default, color = Ux4gColors.neutral600)
                Ux4gDivider(style = Ux4gDividerStyle.Solid)

                Text("Dashed Divider (12dp dashes, 8dp gaps):", style = defaultTypography.bS_default, color = Ux4gColors.neutral600)
                Ux4gDivider(style = Ux4gDividerStyle.Dashed)

                Text("Dotted Divider (4dp dots, 4dp gaps):", style = defaultTypography.bS_default, color = Ux4gColors.neutral600)
                Ux4gDivider(style = Ux4gDividerStyle.Dotted)

                Ux4gDivider()

                // 24.2 Divider with Center Labels
                Text(
                    text = "24.2 Dividers with Center Labels",
                    style = defaultTypography.lS_strong
                )

                Text("Horizontal with 'OR' Text Label:", style = defaultTypography.bS_default, color = Ux4gColors.neutral600)
                Ux4gDivider(
                    label = {
                        Text(
                            text = "OR",
                            style = defaultTypography.lS_strong,
                            color = Ux4gColors.neutral600
                        )
                    }
                )

                Text("Dashed with Primary Badge/Tag Label:", style = defaultTypography.bS_default, color = Ux4gColors.neutral600)
                Ux4gDivider(
                    style = Ux4gDividerStyle.Dashed,
                    color = Ux4gColors.primary300,
                    thickness = 1.5.dp,
                    label = {
                        Text(
                            text = "SECTION 24",
                            style = defaultTypography.lS_strong,
                            color = Ux4gColors.primary
                        )
                    }
                )

                Ux4gDivider()

                // 24.3 Custom Thickness, Colors & Indents
                Text(
                    text = "24.3 Custom Thickness, Color & Indentation",
                    style = defaultTypography.lS_strong
                )

                Text("Thick Primary Divider (3dp):", style = defaultTypography.bS_default, color = Ux4gColors.neutral600)
                Ux4gDivider(thickness = 3.dp, color = Ux4gColors.primary)

                Text("Divider with Start & End Indent (40dp):", style = defaultTypography.bS_default, color = Ux4gColors.neutral600)
                Ux4gDivider(startIndent = 40.dp, endIndent = 40.dp, color = Ux4gColors.neutral500)

                Ux4gDivider()

                // 24.4 Vertical Dividers
                Text(
                    text = "24.4 Vertical Dividers in Horizontal Layout",
                    style = defaultTypography.lS_strong
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .background(Ux4gColors.neutral100, RoundedCornerShape(8.dp))
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Option 1", style = defaultTypography.bM_strong)
                    Ux4gDivider(orientation = Ux4gDividerOrientation.Vertical)
                    Text("Option 2", style = defaultTypography.bM_strong)
                    Ux4gDivider(
                        orientation = Ux4gDividerOrientation.Vertical,
                        style = Ux4gDividerStyle.Dashed
                    )
                    Text("Option 3", style = defaultTypography.bM_strong)
                    Ux4gDivider(
                        orientation = Ux4gDividerOrientation.Vertical,
                        style = Ux4gDividerStyle.Dotted,
                        color = Ux4gColors.primary
                    )
                    Text("Option 4", style = defaultTypography.bM_strong)
                }
            }

            // 25. Status Banner Component Showcase
            Ux4gCard(
                modifier = Modifier.fillMaxWidth(),
                borderColor = Ux4gColors.neutral300,
                borderWidth = 1.dp
            ) {
                Text(
                    text = "25. Status Banner",
                    style = defaultTypography.hS_strong,
                    color = Ux4gColors.primary
                )

                // 25.1 Expandable Draft Application Banner (Matching Screenshots)
                Text(
                    text = "25.1 Expandable Draft Application Banner (Collapsible with Actions)",
                    style = defaultTypography.lS_strong
                )

                var isBannerExpanded by remember { mutableStateOf(false) }
                Ux4gStatusBanner(
                    variant = Ux4gBannerVariant.WarningLight,
                    title = "Income Certificate Application",
                    subtitleWidget = {
                        Column {
                            Spacer(modifier = Modifier.height(4.dp))
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(Color(0xFFFFEBD6))
                                    .padding(horizontal = 8.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = "Step 3 of 5 Document Upload",
                                    style = defaultTypography.bXS_default.copy(
                                        color = Color(0xFFA46800),
                                        fontWeight = androidx.compose.ui.text.font.FontWeight.Medium
                                    )
                                )
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Last saved: 10 Apr 2026",
                                style = defaultTypography.bS_default.copy(color = Ux4gColors.neutral700)
                            )
                        }
                    },
                    trailingIcon = {
                        Icon(
                            imageVector = if (isBannerExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                            contentDescription = "Toggle Details",
                            tint = Ux4gColors.neutral900,
                            modifier = Modifier
                                .size(24.dp)
                                .clickable { isBannerExpanded = !isBannerExpanded }
                        )
                    },
                    actions = if (isBannerExpanded) {
                        {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Discard",
                                    style = defaultTypography.bM_strong.copy(color = Ux4gColors.red600),
                                    modifier = Modifier.clickable { /* Handle discard */ }
                                )
                                Ux4gButton(
                                    text = "Resume",
                                    variant = Ux4gButtonVariant.Primary,
                                    size = Ux4gButtonSize.Small,
                                    backgroundColor = Ux4gColors.primary,
                                    contentColor = Ux4gColors.neutral50,
                                    onClick = { /* Handle resume */ }
                                )
                            }
                        }
                    } else null,
                    margin = PaddingValues(0.dp)
                )

                Ux4gDivider()

                // 25.2 Warning Solid Banner
                Text(
                    text = "25.2 Warning Solid Banner",
                    style = defaultTypography.lS_strong
                )

                Ux4gStatusBanner(
                    variant = Ux4gBannerVariant.WarningSolid,
                    title = "Your draft expires tomorrow. Submit today",
                    margin = PaddingValues(0.dp)
                )

                Ux4gDivider()

                // 25.3 Saving State Banner
                Text(
                    text = "25.3 Saving State Banner",
                    style = defaultTypography.lS_strong
                )

                Ux4gStatusBanner(
                    variant = Ux4gBannerVariant.WarningLight,
                    title = "",
                    trailingIcon = {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(16.dp),
                                strokeWidth = 2.dp,
                                color = Ux4gColors.primary
                            )
                            Text(
                                text = "Saving",
                                style = defaultTypography.bM_default.copy(color = Ux4gColors.neutral700)
                            )
                        }
                    },
                    margin = PaddingValues(0.dp)
                )

                Ux4gDivider()

                // 25.4 Success Saved Banner
                Text(
                    text = "25.4 Draft Saved Banner (Leading Check)",
                    style = defaultTypography.lS_strong
                )

                Ux4gStatusBanner(
                    variant = Ux4gBannerVariant.SuccessLight,
                    title = "Draft saved successfully at 3:14 PM",
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = null,
                            tint = Color(0xFF128937),
                            modifier = Modifier.size(20.dp)
                        )
                    },
                    margin = PaddingValues(0.dp)
                )

                Ux4gDivider()

                // 25.5 Expiry Banner with Badge
                Text(
                    text = "25.5 Draft Expiry with Badge",
                    style = defaultTypography.lS_strong
                )

                Ux4gStatusBanner(
                    variant = Ux4gBannerVariant.WarningLight,
                    title = "Your draft expires in 5 days",
                    badge = {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(4.dp))
                                .background(Color(0xFFFFEBD6))
                                .padding(horizontal = 8.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = "16 Apr",
                                style = defaultTypography.bXS_default.copy(
                                    color = Color(0xFFA46800),
                                    fontWeight = androidx.compose.ui.text.font.FontWeight.Medium
                                )
                            )
                        }
                    },
                    margin = PaddingValues(0.dp)
                )

                Ux4gDivider()

                // 25.6 Error / Expired Banner with Action
                Text(
                    text = "25.6 Draft Expired Banner with Action Link",
                    style = defaultTypography.lS_strong
                )

                Ux4gStatusBanner(
                    variant = Ux4gBannerVariant.ErrorLight,
                    title = "Draft expired on 9 April",
                    subtitle = "2026",
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Error,
                            contentDescription = null,
                            tint = Ux4gColors.red600,
                            modifier = Modifier.size(20.dp)
                        )
                    },
                    trailingIcon = {
                        Text(
                            text = "Action",
                            style = defaultTypography.bM_strong.copy(color = Color(0xFFA44030)),
                            modifier = Modifier.clickable { /* Action click */ }
                        )
                    },
                    margin = PaddingValues(0.dp)
                )

                Ux4gDivider()

                // 25.7 Success Light (Right Aligned Timestamp)
                Text(
                    text = "25.7 Saved Timestamp Banner (Right Aligned)",
                    style = defaultTypography.lS_strong
                )

                Ux4gStatusBanner(
                    variant = Ux4gBannerVariant.SuccessLight,
                    title = "",
                    trailingIcon = {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = null,
                                tint = Color(0xFF128937),
                                modifier = Modifier.size(18.dp)
                            )
                            Text(
                                text = "Saved 3:14 PM",
                                style = defaultTypography.bM_strong.copy(color = Color(0xFF128937))
                            )
                        }
                    },
                    margin = PaddingValues(0.dp)
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
