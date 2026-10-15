package com.ux4g.ux4gdesignsystem

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
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
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontWeight
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
import com.ux4g.designsystemv1.components.steppers.*
import com.ux4g.designsystemv1.components.timeslot.*
import com.ux4g.designsystemv1.components.biometric.*
import com.ux4g.designsystemv1.components.tooltip.*
import com.ux4g.designsystemv1.components.feedback.*
import com.ux4g.designsystemv1.components.textarea.*
import com.ux4g.designsystemv1.components.progress.*
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
            Ux4GDesignSystemTheme {
                val toastHostState = remember { Ux4gToastHostState() }
                var toastAtTop by remember { mutableStateOf(true) }
                var isFullscreenBiometricOpen by remember { mutableStateOf(false) }
                var isFullscreenBottomNavOpen by remember { mutableStateOf(false) }
                val scope = rememberCoroutineScope()

                if (isFullscreenBiometricOpen) {
                    // Fullscreen screen containing ONLY the Biometric Verification component with full width & height
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.Black)
                    ) {
                        Ux4gBiometricVerificationFlow(
                            onVerify = { bytes ->
                                // Custom Dummy UIDAI verification endpoint simulation
                                kotlinx.coroutines.delay(2200L)
                                BiometricVerificationResult(
                                    success = true,
                                    userId = "GOV-UID-9842",
                                    maskedName = "Aarav Sharma",
                                    maskedPhone = "XXXX-XXXX-8921",
                                    message = "Aadhaar e-KYC Biometric Matched Successfully",
                                    confidenceScore = 0.98
                                )
                            },
                            enableBlinkCheck = true,
                            enableLightingCheck = true,
                            enableLiveness = true,
                            maxAttempts = 3,
                            onSuccess = { result ->
                                scope.launch {
                                    toastHostState.showToast(
                                        category = Ux4gToastCategory.Success,
                                        title = "Biometric Verified",
                                        subtitle = "Welcome, ${result.maskedName ?: "Authorized User"}"
                                    )
                                }
                                isFullscreenBiometricOpen = false
                            },
                            onFailure = { reason, msg ->
                                scope.launch {
                                    toastHostState.showToast(
                                        category = Ux4gToastCategory.Error,
                                        title = "Biometric Verification Failed",
                                        subtitle = msg ?: reason.name
                                    )
                                }
                            },
                            onDismiss = {
                                isFullscreenBiometricOpen = false
                            },
                            onAlternateVerify = {
                                scope.launch {
                                    toastHostState.showToast(
                                        category = Ux4gToastCategory.Info,
                                        title = "OTP Fallback",
                                        subtitle = "Switched to OTP verification"
                                    )
                                }
                                isFullscreenBiometricOpen = false
                            }
                        )

                        // Floating Close button in top-right
                        IconButton(
                            onClick = { isFullscreenBiometricOpen = false },
                            modifier = Modifier
                                .statusBarsPadding()
                                .align(Alignment.TopEnd)
                                .padding(16.dp)
                                .size(44.dp)
                                .clip(CircleShape)
                                .background(Color.Black.copy(alpha = 0.6f))
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Close",
                                tint = Color.White,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }
                } else if (isFullscreenBottomNavOpen) {
                    LiveBottomNavScreen(
                        onDismiss = { isFullscreenBottomNavOpen = false },
                        toastHostState = toastHostState
                    )
                } else {
                    Scaffold(
                        modifier = Modifier.fillMaxSize(),
                        containerColor = Ux4gTheme.colorScheme.background
                    ) { innerPadding ->
                        Box(modifier = Modifier.fillMaxSize()) {
                            MainContent(
                                modifier = Modifier.padding(innerPadding),
                                toastHostState = toastHostState,
                                onToggleToastPosition = { toastAtTop = !toastAtTop },
                                isToastAtTop = toastAtTop,
                                onLaunchFullScreenBiometric = { isFullscreenBiometricOpen = true },
                                onLaunchFullScreenBottomNav = { isFullscreenBottomNavOpen = true }
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
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalUx4gApi::class)
@Composable
fun MainContent(
    modifier: Modifier = Modifier,
    toastHostState: Ux4gToastHostState,
    onToggleToastPosition: () -> Unit,
    isToastAtTop: Boolean,
    onLaunchFullScreenBiometric: () -> Unit = {},
    onLaunchFullScreenBottomNav: () -> Unit = {}
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
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Pagination Dotted (UX4G 1:1 Parity)",
                    style = Ux4gTheme.typography.tS_strong,
                    modifier = Modifier.align(Alignment.Start)
                )

                var pageIndex by remember { mutableStateOf(1) }
                var pageIndexRightArrows by remember { mutableStateOf(2) }

                Text(text = "Default", style = Ux4gTheme.typography.lM_strong, modifier = Modifier.align(Alignment.Start))
                Ux4gPaginationDotted(
                    totalPageCount = 7,
                    currentPageIndex = pageIndex,
                    onPageChange = { pageIndex = it },
                    variant = Ux4gPaginationVariant.Default,
                    size = Ux4gPaginationSize.Small
                )

                Text(text = "Capsule", style = Ux4gTheme.typography.lM_strong, modifier = Modifier.align(Alignment.Start))
                Ux4gPaginationDotted(
                    totalPageCount = 7,
                    currentPageIndex = pageIndex,
                    onPageChange = { pageIndex = it },
                    variant = Ux4gPaginationVariant.Capsule,
                    size = Ux4gPaginationSize.Small
                )

                Text(text = "Dots Only (No Arrows)", style = Ux4gTheme.typography.lM_strong, modifier = Modifier.align(Alignment.Start))
                Ux4gPaginationDotted(
                    totalPageCount = 7,
                    currentPageIndex = pageIndex,
                    onPageChange = { pageIndex = it },
                    showArrows = false,
                    variant = Ux4gPaginationVariant.Default
                )

                Text(text = "Right Aligned Arrows", style = Ux4gTheme.typography.lM_strong, modifier = Modifier.align(Alignment.Start))
                Ux4gPaginationDotted(
                    totalPageCount = 7,
                    currentPageIndex = pageIndexRightArrows,
                    onPageChange = { pageIndexRightArrows = it },
                    arrowsOnRight = true,
                    variant = Ux4gPaginationVariant.Default,
                    size = Ux4gPaginationSize.Medium
                )

                Text(text = "Right Aligned Arrows + Capsule", style = Ux4gTheme.typography.lM_strong, modifier = Modifier.align(Alignment.Start))
                Ux4gPaginationDotted(
                    totalPageCount = 7,
                    currentPageIndex = pageIndexRightArrows,
                    onPageChange = { pageIndexRightArrows = it },
                    arrowsOnRight = true,
                    variant = Ux4gPaginationVariant.Capsule,
                    size = Ux4gPaginationSize.Medium
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

        // 18. Bottom Navigation Bar Showcase (Official 4 Core Variants & Indicators Matrix)
        Ux4gCard(modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Header
                Text(
                    text = "Bottom Navigation Bar",
                    style = Ux4gTheme.typography.tS_strong
                )
                Text(
                    text = "Official UX4G Bottom Navigation Bar matrix showcasing all 5 layout variants across all 3 indicator types (Pill, Bar, Plain).",
                    style = Ux4gTheme.typography.bS_default,
                    color = Ux4gColors.neutral600
                )

                Button(
                    onClick = onLaunchFullScreenBottomNav,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(46.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Ux4gTheme.colorScheme.primary)
                ) {
                    Icon(imageVector = Icons.Default.OpenInNew, contentDescription = null, tint = Color.White)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Open Live Page (Anchored at Screen Bottom)",
                        style = TextStyle(fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                    )
                }

                // Indicator Selector Tabs (Pill, Bar, Plain, All Matrix)
                var selectedIndicatorTab by remember { mutableStateOf(0) }
                val indicatorNames = listOf("Pill", "Bar", "Plain", "All Matrix (Grid)")

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    indicatorNames.forEachIndexed { idx, name ->
                        val isTabActive = selectedIndicatorTab == idx
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(
                                    if (isTabActive) Ux4gTheme.colorScheme.primary else Ux4gColors.neutral100
                                )
                                .clickable { selectedIndicatorTab = idx }
                                .padding(horizontal = 14.dp, vertical = 8.dp)
                        ) {
                            Text(
                                text = name,
                                style = Ux4gTheme.typography.lS_strong.copy(
                                    fontWeight = if (isTabActive) FontWeight.Bold else FontWeight.Medium
                                ),
                                color = if (isTabActive) Color.White else Ux4gColors.neutral800
                            )
                        }
                    }
                }

                Ux4gDivider()

                // Standard navigation items matching official UX4G Flutter showcase
                val standardItems = listOf(
                    Ux4gBottomNavItem(key = "home", label = "Home", icon = Ux4gIcons.Home),
                    Ux4gBottomNavItem(key = "services", label = "Services", icon = Ux4gIcons.Menu),
                    Ux4gBottomNavItem(key = "status", label = "Status", icon = Ux4gIcons.Check),
                    Ux4gBottomNavItem(key = "alerts", label = "Alerts", icon = Ux4gIcons.info_circle, badge = 3),
                    Ux4gBottomNavItem(key = "profile", label = "Profile", icon = Ux4gIcons.Person)
                )

                val centreActionItems = listOf(
                    Ux4gBottomNavItem(key = "home", label = "Home", icon = Ux4gIcons.Home),
                    Ux4gBottomNavItem(key = "services", label = "Services", icon = Ux4gIcons.Menu),
                    Ux4gBottomNavItem(key = "status", label = "Status", icon = Ux4gIcons.Check),
                    Ux4gBottomNavItem(key = "alerts", label = "Alerts", icon = Ux4gIcons.info_circle)
                )

                if (selectedIndicatorTab < 3) {
                    val currentIndicator = when (selectedIndicatorTab) {
                        0 -> Ux4gBottomNavIndicatorVariant.Pill
                        1 -> Ux4gBottomNavIndicatorVariant.Bar
                        else -> Ux4gBottomNavIndicatorVariant.Plain
                    }
                    val currentIndicatorName = indicatorNames[selectedIndicatorTab]

                    Text(
                        text = "Viewing: $currentIndicatorName Indicator",
                        style = Ux4gTheme.typography.lM_strong,
                        color = Ux4gTheme.colorScheme.primary
                    )

                    // 1. Labelled
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text(text = "1. Labelled", style = Ux4gTheme.typography.lS_strong, color = Ux4gColors.neutral800)
                        var tabIndex1 by remember { mutableStateOf(0) }
                        Ux4gBottomNavigationBar(
                            items = standardItems,
                            selectedIndex = tabIndex1,
                            onTabChange = { idx, _ -> tabIndex1 = idx },
                            variant = Ux4gBottomNavVariant.Labelled,
                            indicatorVariant = currentIndicator,
                            useSafeArea = false
                        )
                    }

                    // 2. Top-rounded
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text(text = "2. Top-rounded", style = Ux4gTheme.typography.lS_strong, color = Ux4gColors.neutral800)
                        var tabIndex2 by remember { mutableStateOf(0) }
                        Ux4gBottomNavigationBar(
                            items = standardItems,
                            selectedIndex = tabIndex2,
                            onTabChange = { idx, _ -> tabIndex2 = idx },
                            variant = Ux4gBottomNavVariant.TopRounded,
                            indicatorVariant = currentIndicator,
                            useSafeArea = false
                        )
                    }

                    // 3. Icon only
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text(text = "3. Icon only", style = Ux4gTheme.typography.lS_strong, color = Ux4gColors.neutral800)
                        var tabIndex3 by remember { mutableStateOf(0) }
                        Ux4gBottomNavigationBar(
                            items = standardItems,
                            selectedIndex = tabIndex3,
                            onTabChange = { idx, _ -> tabIndex3 = idx },
                            variant = Ux4gBottomNavVariant.IconOnly,
                            indicatorVariant = currentIndicator,
                            showLabels = false,
                            useSafeArea = false
                        )
                    }

                    // 4. Centre action
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text(text = "4. Centre action", style = Ux4gTheme.typography.lS_strong, color = Ux4gColors.neutral800)
                        var tabIndex4 by remember { mutableStateOf(0) }
                        Ux4gBottomNavigationBar(
                            items = centreActionItems,
                            selectedIndex = tabIndex4,
                            onTabChange = { idx, _ -> tabIndex4 = idx },
                            variant = Ux4gBottomNavVariant.CentreAction,
                            indicatorVariant = currentIndicator,
                            centerAction = Ux4gBottomNavCenterAction(
                                icon = Ux4gIcons.Add,
                                onPressed = {
                                    scope.launch {
                                        toastHostState.showToast(
                                            category = Ux4gToastCategory.Info,
                                            title = "Center Action Clicked",
                                            subtitle = "Prominent (+) action button tapped!"
                                        )
                                    }
                                }
                            ),
                            useSafeArea = false
                        )
                    }

                    // 5. Pill (floating)
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text(text = "5. Pill (floating)", style = Ux4gTheme.typography.lS_strong, color = Ux4gColors.neutral800)
                        var tabIndex5 by remember { mutableStateOf(0) }
                        Ux4gBottomNavigationBar(
                            items = standardItems,
                            selectedIndex = tabIndex5,
                            onTabChange = { idx, _ -> tabIndex5 = idx },
                            variant = Ux4gBottomNavVariant.FloatingPill,
                            indicatorVariant = currentIndicator,
                            useSafeArea = false
                        )
                    }
                } else {
                    // Full 5x3 Matrix View with horizontal scroll
                    Text(
                        text = "Complete 5 × 3 Matrix (Scroll horizontally to compare columns):",
                        style = Ux4gTheme.typography.lM_strong,
                        color = Ux4gTheme.colorScheme.primary
                    )

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(20.dp)
                    ) {
                        listOf(
                            Triple("Pill", Ux4gBottomNavIndicatorVariant.Pill, Ux4gTheme.colorScheme.primary),
                            Triple("Bar", Ux4gBottomNavIndicatorVariant.Bar, Ux4gColors.primary600),
                            Triple("Plain", Ux4gBottomNavIndicatorVariant.Plain, Ux4gColors.neutral600)
                        ).forEach { (colName, indicatorVariant, accentColor) ->
                            Column(
                                modifier = Modifier
                                    .width(360.dp)
                                    .background(Ux4gColors.neutral50, RoundedCornerShape(12.dp))
                                    .border(1.dp, Ux4gColors.neutral200, RoundedCornerShape(12.dp))
                                    .padding(12.dp),
                                verticalArrangement = Arrangement.spacedBy(16.dp)
                            ) {
                                // Column Header with top purple accent line
                                Column(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .width(100.dp)
                                            .height(3.dp)
                                            .background(accentColor, RoundedCornerShape(2.dp))
                                    )
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text(
                                        text = colName,
                                        style = Ux4gTheme.typography.tS_strong,
                                        color = Ux4gColors.neutral900
                                    )
                                }

                                // 1. Labelled
                                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                    Text(text = "Labelled", style = Ux4gTheme.typography.lS_strong, color = Ux4gColors.neutral700)
                                    var idx1 by remember { mutableStateOf(0) }
                                    Ux4gBottomNavigationBar(
                                        items = standardItems,
                                        selectedIndex = idx1,
                                        onTabChange = { i, _ -> idx1 = i },
                                        variant = Ux4gBottomNavVariant.Labelled,
                                        indicatorVariant = indicatorVariant,
                                        useSafeArea = false
                                    )
                                }

                                // 2. Top-rounded
                                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                    Text(text = "Top-rounded", style = Ux4gTheme.typography.lS_strong, color = Ux4gColors.neutral700)
                                    var idx2 by remember { mutableStateOf(0) }
                                    Ux4gBottomNavigationBar(
                                        items = standardItems,
                                        selectedIndex = idx2,
                                        onTabChange = { i, _ -> idx2 = i },
                                        variant = Ux4gBottomNavVariant.TopRounded,
                                        indicatorVariant = indicatorVariant,
                                        useSafeArea = false
                                    )
                                }

                                // 3. Icon only
                                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                    Text(text = "Icon only", style = Ux4gTheme.typography.lS_strong, color = Ux4gColors.neutral700)
                                    var idx3 by remember { mutableStateOf(0) }
                                    Ux4gBottomNavigationBar(
                                        items = standardItems,
                                        selectedIndex = idx3,
                                        onTabChange = { i, _ -> idx3 = i },
                                        variant = Ux4gBottomNavVariant.IconOnly,
                                        indicatorVariant = indicatorVariant,
                                        showLabels = false,
                                        useSafeArea = false
                                    )
                                }

                                // 4. Centre action
                                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                    Text(text = "Centre action", style = Ux4gTheme.typography.lS_strong, color = Ux4gColors.neutral700)
                                    var idx4 by remember { mutableStateOf(0) }
                                    Ux4gBottomNavigationBar(
                                        items = centreActionItems,
                                        selectedIndex = idx4,
                                        onTabChange = { i, _ -> idx4 = i },
                                        variant = Ux4gBottomNavVariant.CentreAction,
                                        indicatorVariant = indicatorVariant,
                                        centerAction = Ux4gBottomNavCenterAction(
                                            icon = Ux4gIcons.Add,
                                            onPressed = {
                                                scope.launch {
                                                    toastHostState.showToast(
                                                        category = Ux4gToastCategory.Info,
                                                        title = "Center Action",
                                                        subtitle = "$colName (+) FAB clicked"
                                                    )
                                                }
                                            }
                                        ),
                                        useSafeArea = false
                                    )
                                }

                                // 5. Pill (floating)
                                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                    Text(text = "Pill (floating)", style = Ux4gTheme.typography.lS_strong, color = Ux4gColors.neutral700)
                                    var idx5 by remember { mutableStateOf(0) }
                                    Ux4gBottomNavigationBar(
                                        items = standardItems,
                                        selectedIndex = idx5,
                                        onTabChange = { i, _ -> idx5 = i },
                                        variant = Ux4gBottomNavVariant.FloatingPill,
                                        indicatorVariant = indicatorVariant,
                                        useSafeArea = false
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // 19. Capsule Stepper Showcase
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

            // 26. Empty State Component Showcase
            Ux4gCard(
                modifier = Modifier.fillMaxWidth(),
                borderColor = Ux4gColors.neutral300,
                borderWidth = 1.dp
            ) {
                Text(
                    text = "26. Empty State",
                    style = defaultTypography.hS_strong,
                    color = Ux4gColors.primary
                )

                // 26.1 No Results State
                Text(
                    text = "26.1 No Results Found (Search Filter)",
                    style = defaultTypography.lS_strong
                )
                Ux4gCard(
                    modifier = Modifier.fillMaxWidth(),
                    borderColor = Ux4gColors.neutral200,
                    borderWidth = 1.dp
                ) {
                    Ux4gEmptyState(
                        variant = Ux4gEmptyStateVariant.NoResults,
                        title = "No results found",
                        subtitle = "Did you mean: Driving License, Ration Card",
                        buttonText = "Clear all filters",
                        onButtonPressed = {}
                    )
                }

                Ux4gDivider()

                // 26.2 No Data State
                Text(
                    text = "26.2 No Data / Empty Inbox",
                    style = defaultTypography.lS_strong
                )
                Ux4gCard(
                    modifier = Modifier.fillMaxWidth(),
                    borderColor = Ux4gColors.neutral200,
                    borderWidth = 1.dp
                ) {
                    Ux4gEmptyState(
                        variant = Ux4gEmptyStateVariant.NoData,
                        title = "No items in your inbox",
                        subtitle = "When you receive messages or official documents, they will appear here.",
                        buttonText = "Refresh Inbox",
                        onButtonPressed = {}
                    )
                }

                Ux4gDivider()

                // 26.3 Coming Soon State
                Text(
                    text = "26.3 Feature Coming Soon",
                    style = defaultTypography.lS_strong
                )
                Ux4gCard(
                    modifier = Modifier.fillMaxWidth(),
                    borderColor = Ux4gColors.neutral200,
                    borderWidth = 1.dp
                ) {
                    Ux4gEmptyState(
                        variant = Ux4gEmptyStateVariant.ComingSoon,
                        title = "Feature Coming Soon",
                        subtitle = "We are working hard to bring digital verification services to you. Stay tuned!",
                        buttonText = "Notify Me",
                        onButtonPressed = {}
                    )
                }

                Ux4gDivider()

                // 26.4 Error State
                Text(
                    text = "26.4 Error / Loading Failed",
                    style = defaultTypography.lS_strong
                )
                Ux4gCard(
                    modifier = Modifier.fillMaxWidth(),
                    borderColor = Ux4gColors.neutral200,
                    borderWidth = 1.dp
                ) {
                    Ux4gEmptyState(
                        variant = Ux4gEmptyStateVariant.Error,
                        title = "Unable to load data",
                        subtitle = "Please check your network connection and try again.",
                        iconColor = Ux4gColors.red600,
                        buttonText = "Try Again",
                        onButtonPressed = {}
                    )
                }
            }

            // 27. File Upload Component Showcase
            Ux4gCard(
                modifier = Modifier.fillMaxWidth(),
                borderColor = Ux4gColors.neutral300,
                borderWidth = 1.dp
            ) {
                Text(
                    text = "27. File Upload",
                    style = defaultTypography.hS_strong,
                    color = Ux4gColors.primary
                )

                // 27.1 Interactive Solid Border Upload
                Text(
                    text = "27.1 Interactive Upload (Solid Border & File States)",
                    style = defaultTypography.lS_strong
                )

                var demoFiles by remember {
                    mutableStateOf(
                        listOf(
                            Ux4gUploadedFile(
                                id = "demo_1",
                                name = "Income_Certificate_2026.pdf",
                                fileSize = 1_450_000,
                                status = Ux4gUploadStatus.Success
                            ),
                            Ux4gUploadedFile(
                                id = "demo_2",
                                name = "Passport_Photo_Scan.png",
                                fileSize = 820_000,
                                status = Ux4gUploadStatus.Uploading,
                                progress = 0.70f
                            ),
                            Ux4gUploadedFile(
                                id = "demo_3",
                                name = "Electricity_Bill_March.pdf",
                                fileSize = 3_200_000,
                                status = Ux4gUploadStatus.Error,
                                errorMessage = "Server timeout. Please try again."
                            )
                        )
                    )
                }

                Ux4gFileUpload(
                    files = demoFiles,
                    borderStyle = Ux4gFileUploadBorderStyle.Solid,
                    onFilesChanged = { updatedList ->
                        demoFiles = updatedList
                    }
                )

                Ux4gDivider()

                // 27.2 Dashed Border Dropzone
                Text(
                    text = "27.2 Dashed Border Dropzone Style",
                    style = defaultTypography.lS_strong
                )

                var dashedFiles by remember { mutableStateOf<List<Ux4gUploadedFile>>(emptyList()) }
                Ux4gFileUpload(
                    files = dashedFiles,
                    borderStyle = Ux4gFileUploadBorderStyle.Dashed,
                    onFilesChanged = { updatedList ->
                        dashedFiles = updatedList
                    }
                )

                Ux4gDivider()

                // 27.3 Individual File Item Cards
                Text(
                    text = "27.3 File Item Cards by File Type & Status",
                    style = defaultTypography.lS_strong
                )

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Ux4gFileItemCard(
                        file = Ux4gUploadedFile(
                            id = "card_1",
                            name = "Aadhaar_Card_Front.jpg",
                            fileSize = 650_000,
                            status = Ux4gUploadStatus.Success
                        ),
                        onRemove = {},
                        onRetry = {}
                    )

                    Ux4gFileItemCard(
                        file = Ux4gUploadedFile(
                            id = "card_2",
                            name = "Salary_Slip_February.pdf",
                            fileSize = 2_150_000,
                            status = Ux4gUploadStatus.Uploading,
                            progress = 0.45f
                        ),
                        onRemove = {},
                        onRetry = {}
                    )

                    Ux4gFileItemCard(
                        file = Ux4gUploadedFile(
                            id = "card_3",
                            name = "Affidavit_Draft.docx",
                            fileSize = 120_000,
                            status = Ux4gUploadStatus.Error,
                            errorMessage = "Invalid document format."
                        ),
                        onRemove = {},
                        onRetry = {}
                    )
                }
            }

            // 28. Input Field Component Showcase
            Ux4gCard(
                modifier = Modifier.fillMaxWidth(),
                borderColor = Ux4gColors.neutral300,
                borderWidth = 1.dp
            ) {
                Text(
                    text = "28. Input Field",
                    style = defaultTypography.hS_strong,
                    color = Ux4gColors.primary
                )

                // 28.1 Sizes
                Text(
                    text = "28.1 Input Field Sizes (XL, Large, Medium, Small)",
                    style = defaultTypography.lS_strong
                )

                var textXl by remember { mutableStateOf("") }
                var textL by remember { mutableStateOf("") }
                var textM by remember { mutableStateOf("") }
                var textS by remember { mutableStateOf("") }

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Ux4gInputField(
                        value = textXl,
                        onValueChange = { textXl = it },
                        label = "XL Size (56dp)",
                        placeholder = "Enter value...",
                        caption = "Extra large height input field",
                        size = Ux4gInputFieldSize.XL,
                        leadingIcon = Icons.Default.Person,
                        trailingIcon = Icons.Default.Close,
                        onTrailingIconPressed = { textXl = "" }
                    )

                    Ux4gInputField(
                        value = textL,
                        onValueChange = { textL = it },
                        label = "Large Size (48dp)",
                        placeholder = "Enter value...",
                        caption = "Large height input field",
                        size = Ux4gInputFieldSize.Large,
                        leadingIcon = Icons.Default.Person,
                        trailingIcon = Icons.Default.Close,
                        onTrailingIconPressed = { textL = "" }
                    )

                    Ux4gInputField(
                        value = textM,
                        onValueChange = { textM = it },
                        label = "Medium Size (40dp) [Default]",
                        placeholder = "Enter value...",
                        caption = "Standard input field",
                        size = Ux4gInputFieldSize.Medium,
                        leadingIcon = Icons.Default.Person,
                        trailingIcon = Icons.Default.Close,
                        onTrailingIconPressed = { textM = "" }
                    )

                    Ux4gInputField(
                        value = textS,
                        onValueChange = { textS = it },
                        label = "Small Size (32dp)",
                        placeholder = "Enter value...",
                        caption = "Compact input field",
                        size = Ux4gInputFieldSize.Small,
                        leadingIcon = Icons.Default.Person,
                        trailingIcon = Icons.Default.Close,
                        onTrailingIconPressed = { textS = "" }
                    )
                }

                Ux4gDivider()

                // 28.2 Types & Password Visibility Toggle
                Text(
                    text = "28.2 Input Field Types & Password Toggle",
                    style = defaultTypography.lS_strong
                )

                var textVal by remember { mutableStateOf("Monu Kumar") }
                var passVal by remember { mutableStateOf("MySecurePass123") }
                var numVal by remember { mutableStateOf("9876543210") }
                var emailVal by remember { mutableStateOf("monu@openforge.gov.in") }

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Ux4gInputField(
                        value = textVal,
                        onValueChange = { textVal = it },
                        label = "Full Name (Text Type)",
                        type = Ux4gInputFieldType.Text,
                        placeholder = "Enter full name",
                        leadingIcon = Icons.Default.Person
                    )

                    Ux4gInputField(
                        value = passVal,
                        onValueChange = { passVal = it },
                        label = "Password (Password Type)",
                        type = Ux4gInputFieldType.Password,
                        placeholder = "Enter password",
                        caption = "Click the eye icon to toggle visibility",
                        leadingIcon = Icons.Default.Lock
                    )

                    Ux4gInputField(
                        value = numVal,
                        onValueChange = { numVal = it },
                        label = "Mobile Number (Number Type)",
                        type = Ux4gInputFieldType.Number,
                        placeholder = "10 digit mobile",
                        prefixText = "+91"
                    )

                    Ux4gInputField(
                        value = emailVal,
                        onValueChange = { emailVal = it },
                        label = "Official Email (Email Type)",
                        type = Ux4gInputFieldType.Email,
                        placeholder = "user@domain.gov.in",
                        leadingIcon = Icons.Default.Email
                    )
                }

                Ux4gDivider()

                // 28.3 Validation & Status States
                Text(
                    text = "28.3 Validation & Status States",
                    style = defaultTypography.lS_strong
                )

                var defaultStatusText by remember { mutableStateOf("Active user input") }
                var errorStatusText by remember { mutableStateOf("invalid-email-format") }
                var warningStatusText by remember { mutableStateOf("weakpass") }
                var successStatusText by remember { mutableStateOf("valid_user_99") }

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Ux4gInputField(
                        value = defaultStatusText,
                        onValueChange = { defaultStatusText = it },
                        label = "Default State",
                        status = Ux4gInputFieldStatus.Default,
                        caption = "Standard input field with focus state"
                    )

                    Ux4gInputField(
                        value = errorStatusText,
                        onValueChange = { errorStatusText = it },
                        label = "Error State",
                        required = true,
                        status = Ux4gInputFieldStatus.Error,
                        caption = "Please enter a valid email address format."
                    )

                    Ux4gInputField(
                        value = warningStatusText,
                        onValueChange = { warningStatusText = it },
                        label = "Warning State",
                        status = Ux4gInputFieldStatus.Warning,
                        caption = "Password strength is weak (add numbers & symbols)."
                    )

                    Ux4gInputField(
                        value = successStatusText,
                        onValueChange = { successStatusText = it },
                        label = "Success State",
                        status = Ux4gInputFieldStatus.Success,
                        caption = "Username is available and verified."
                    )

                    Ux4gInputField(
                        value = "Disabled content",
                        onValueChange = {},
                        label = "Disabled / Read-Only State",
                        enabled = false,
                        caption = "This field cannot be edited by the user."
                    )
                }

                Ux4gDivider()

                // 28.4 Affixes & Labels
                Text(
                    text = "28.4 Prefix, Postfix, Required & Helper Labels",
                    style = defaultTypography.lS_strong
                )

                var currencyVal by remember { mutableStateOf("25000.00") }
                var websiteVal by remember { mutableStateOf("myportal") }
                var weightVal by remember { mutableStateOf("72.5") }

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Ux4gInputField(
                        value = currencyVal,
                        onValueChange = { currencyVal = it },
                        label = "Annual Income",
                        required = true,
                        prefixText = "₹",
                        postfixText = "INR",
                        caption = "Gross income before deductions"
                    )

                    Ux4gInputField(
                        value = websiteVal,
                        onValueChange = { websiteVal = it },
                        label = "Department Domain",
                        trailingIconLabel = Icons.Default.Info,
                        prefixText = "https://",
                        postfixText = ".gov.in",
                        caption = "Custom public portal endpoint"
                    )

                    Ux4gInputField(
                        value = weightVal,
                        onValueChange = { weightVal = it },
                        label = "Package Weight",
                        postfixText = "kg",
                        caption = "Total consignment net weight"
                    )
                }
            }

            // 29. Aadhaar Input Field Component Showcase
            Ux4gCard(
                modifier = Modifier.fillMaxWidth(),
                borderColor = Ux4gColors.neutral300,
                borderWidth = 1.dp
            ) {
                Text(
                    text = "29. Aadhaar Input Field",
                    style = defaultTypography.hS_strong,
                    color = Ux4gColors.primary
                )

                // 29.1 Live Interactive Validation
                Text(
                    text = "29.1 Live Auto-Formatting & Verhoeff Validation",
                    style = defaultTypography.lS_strong
                )

                var liveAadhaar by remember { mutableStateOf("") }

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Ux4gAadhaarInputField(
                        value = liveAadhaar,
                        onValueChange = { liveAadhaar = it },
                        label = "Applicant Aadhaar Number",
                        required = true,
                        leadingIcon = Icons.Default.Badge,
                        trailingIcon = if (liveAadhaar.isNotEmpty()) Icons.Default.Close else null,
                        onTrailingIconPressed = { liveAadhaar = "" }
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Ux4gButton(
                            text = "Fill Valid Aadhaar",
                            variant = Ux4gButtonVariant.Outline,
                            size = Ux4gButtonSize.Small,
                            onClick = { liveAadhaar = "9999 4105 7058" }
                        )
                        Ux4gButton(
                            text = "Fill Invalid Aadhaar",
                            variant = Ux4gButtonVariant.Outline,
                            size = Ux4gButtonSize.Small,
                            onClick = { liveAadhaar = "2345 6789 0123" }
                        )
                    }
                }

                Ux4gDivider()

                // 29.2 Validation States Comparison
                Text(
                    text = "29.2 Validation States Comparison",
                    style = defaultTypography.lS_strong
                )

                var validAadhaar by remember { mutableStateOf("9999 4105 7058") }
                var invalidAadhaar by remember { mutableStateOf("4123 4567 8901") }

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Ux4gAadhaarInputField(
                        value = validAadhaar,
                        onValueChange = { validAadhaar = it },
                        label = "Valid Aadhaar (Auto-Detected Success)",
                        leadingIcon = Icons.Default.Badge
                    )

                    Ux4gAadhaarInputField(
                        value = invalidAadhaar,
                        onValueChange = { invalidAadhaar = it },
                        label = "Invalid Aadhaar (Auto-Detected Error)",
                        leadingIcon = Icons.Default.Badge
                    )
                }

                Ux4gDivider()

                // 29.3 Sizes
                Text(
                    text = "29.3 Aadhaar Field Sizes (XL, Large, Medium, Small)",
                    style = defaultTypography.lS_strong
                )

                var sizeXl by remember { mutableStateOf("9999 4105 7058") }
                var sizeL by remember { mutableStateOf("9999 4105 7058") }
                var sizeM by remember { mutableStateOf("9999 4105 7058") }
                var sizeS by remember { mutableStateOf("9999 4105 7058") }

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Ux4gAadhaarInputField(
                        value = sizeXl,
                        onValueChange = { sizeXl = it },
                        size = Ux4gInputFieldSize.XL,
                        label = "XL Size (56dp)",
                        leadingIcon = Icons.Default.Badge
                    )

                    Ux4gAadhaarInputField(
                        value = sizeL,
                        onValueChange = { sizeL = it },
                        size = Ux4gInputFieldSize.Large,
                        label = "Large Size (48dp)",
                        leadingIcon = Icons.Default.Badge
                    )

                    Ux4gAadhaarInputField(
                        value = sizeM,
                        onValueChange = { sizeM = it },
                        size = Ux4gInputFieldSize.Medium,
                        label = "Medium Size (40dp) [Default]",
                        leadingIcon = Icons.Default.Badge
                    )

                    Ux4gAadhaarInputField(
                        value = sizeS,
                        onValueChange = { sizeS = it },
                        size = Ux4gInputFieldSize.Small,
                        label = "Small Size (32dp)",
                        leadingIcon = Icons.Default.Badge
                    )
                }
            }

            // 30. PAN Input Field Component Showcase
            Ux4gCard(
                modifier = Modifier.fillMaxWidth(),
                borderColor = Ux4gColors.neutral300,
                borderWidth = 1.dp
            ) {
                Text(
                    text = "30. PAN Input Field",
                    style = defaultTypography.hS_strong,
                    color = Ux4gColors.primary
                )

                // 30.1 Live Interactive Validation
                Text(
                    text = "30.1 Live Auto-Uppercase & Regex Validation (ABCDE1234F)",
                    style = defaultTypography.lS_strong
                )

                var livePan by remember { mutableStateOf("") }

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Ux4gPanInputField(
                        value = livePan,
                        onValueChange = { livePan = it },
                        label = "Taxpayer PAN Number",
                        required = true,
                        leadingIcon = Icons.Default.CreditCard,
                        trailingIcon = if (livePan.isNotEmpty()) Icons.Default.Close else null,
                        onTrailingIconPressed = { livePan = "" }
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Ux4gButton(
                            text = "Fill Valid PAN",
                            variant = Ux4gButtonVariant.Outline,
                            size = Ux4gButtonSize.Small,
                            onClick = { livePan = "ABCDE1234F" }
                        )
                        Ux4gButton(
                            text = "Fill Invalid PAN",
                            variant = Ux4gButtonVariant.Outline,
                            size = Ux4gButtonSize.Small,
                            onClick = { livePan = "12345ABCDE" }
                        )
                    }
                }

                Ux4gDivider()

                // 30.2 Validation States Comparison
                Text(
                    text = "30.2 Validation States Comparison",
                    style = defaultTypography.lS_strong
                )

                var validPan by remember { mutableStateOf("ABCDE1234F") }
                var invalidPan by remember { mutableStateOf("ABCD12345F") }

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Ux4gPanInputField(
                        value = validPan,
                        onValueChange = { validPan = it },
                        label = "Valid PAN (Auto-Detected Success)",
                        leadingIcon = Icons.Default.CreditCard
                    )

                    Ux4gPanInputField(
                        value = invalidPan,
                        onValueChange = { invalidPan = it },
                        label = "Invalid PAN (Auto-Detected Error)",
                        leadingIcon = Icons.Default.CreditCard
                    )
                }

                Ux4gDivider()

                // 30.3 Sizes
                Text(
                    text = "30.3 PAN Field Sizes (XL, Large, Medium, Small)",
                    style = defaultTypography.lS_strong
                )

                var sizePanXl by remember { mutableStateOf("ABCDE1234F") }
                var sizePanL by remember { mutableStateOf("ABCDE1234F") }
                var sizePanM by remember { mutableStateOf("ABCDE1234F") }
                var sizePanS by remember { mutableStateOf("ABCDE1234F") }

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Ux4gPanInputField(
                        value = sizePanXl,
                        onValueChange = { sizePanXl = it },
                        size = Ux4gInputFieldSize.XL,
                        label = "XL Size (56dp)",
                        leadingIcon = Icons.Default.CreditCard
                    )

                    Ux4gPanInputField(
                        value = sizePanL,
                        onValueChange = { sizePanL = it },
                        size = Ux4gInputFieldSize.Large,
                        label = "Large Size (48dp)",
                        leadingIcon = Icons.Default.CreditCard
                    )

                    Ux4gPanInputField(
                        value = sizePanM,
                        onValueChange = { sizePanM = it },
                        size = Ux4gInputFieldSize.Medium,
                        label = "Medium Size (40dp) [Default]",
                        leadingIcon = Icons.Default.CreditCard
                    )

                    Ux4gPanInputField(
                        value = sizePanS,
                        onValueChange = { sizePanS = it },
                        size = Ux4gInputFieldSize.Small,
                        label = "Small Size (32dp)",
                        leadingIcon = Icons.Default.CreditCard
                    )
                }
            }

            Ux4gCard(modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    Text(
                        text = "OTP Input (UX4G 1:1 Complete Feature Parity)",
                        style = Ux4gTheme.typography.tS_strong
                    )

                    // ══════════════════════════════════════════════════════════════════
                    // 🌟 FULL INTERACTIVE TESTING PLAYGROUND
                    // ══════════════════════════════════════════════════════════════════
                    Ux4gCard(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Ux4gTheme.colorScheme.surface)
                                .padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(14.dp)
                        ) {
                            Text(
                                text = "🧪 Interactive Test Playground",
                                style = Ux4gTheme.typography.hS_strong,
                                color = Ux4gTheme.colorScheme.primary
                            )
                            Text(
                                text = "Test typing, backspace, per-box tap, paste, auto-complete, timer, obscure, and validation states.",
                                style = Ux4gTheme.typography.bXS_default,
                                color = Ux4gTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                            )

                            // Interactive State
                            var testOtp by remember { mutableStateOf("") }
                            var testLength by remember { mutableStateOf(6) }
                            var testObscure by remember { mutableStateOf(false) }
                            var testShowSeparator by remember { mutableStateOf(true) }
                            var testStatus by remember { mutableStateOf(Ux4gOtpInputStatus.Default) }
                            var testEnabled by remember { mutableStateOf(true) }
                            var testCountdownSeconds by remember { mutableStateOf<Int?>(30) }
                            var testAttemptsLeft by remember { mutableStateOf(3) }
                            var lastEventLog by remember { mutableStateOf("Ready for testing") }

                            // Live Status Display Box
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(Ux4gRadius.Radius8))
                                    .background(Ux4gTheme.colorScheme.onSurface.copy(alpha = 0.04f))
                                    .border(
                                        width = 1.dp,
                                        color = Ux4gTheme.colorScheme.onSurface.copy(alpha = 0.15f),
                                        shape = RoundedCornerShape(Ux4gRadius.Radius8)
                                    )
                                    .padding(12.dp)
                            ) {
                                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text(
                                            text = "Value: \"$testOtp\" (${testOtp.length}/$testLength)",
                                            style = Ux4gTheme.typography.bS_strong,
                                            color = Ux4gTheme.colorScheme.onSurface
                                        )
                                        Text(
                                            text = if (testOtp.length == testLength) "Completed ✅" else "Pending...",
                                            style = Ux4gTheme.typography.bXS_strong,
                                            color = if (testOtp.length == testLength) Ux4gColors.green600 else Ux4gColors.orange600
                                        )
                                    }
                                    Text(
                                        text = "Event Log: $lastEventLog",
                                        style = Ux4gTheme.typography.bXS_default,
                                        color = Ux4gTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                                    )
                                }
                            }

                            // ── The Interactive Component ────────────────────
                            Ux4gOtpInput(
                                value = testOtp,
                                onValueChange = { newCode ->
                                    testOtp = newCode
                                    lastEventLog = "Typed: \"$newCode\""
                                    if (testStatus == Ux4gOtpInputStatus.Error && newCode.isNotEmpty()) {
                                        testStatus = Ux4gOtpInputStatus.Default
                                    }
                                },
                                length = testLength,
                                obscure = testObscure,
                                showSeparator = testShowSeparator,
                                status = testStatus,
                                enabled = testEnabled,
                                label = "Verification Code",
                                required = true,
                                labelTrailingIcon = Icons.Outlined.Info,
                                captionVariant = when (testStatus) {
                                    Ux4gOtpInputStatus.Success -> Ux4gOtpCaptionVariant.Success
                                    Ux4gOtpInputStatus.Error -> Ux4gOtpCaptionVariant.AttemptWithTimer
                                    Ux4gOtpInputStatus.Locked -> Ux4gOtpCaptionVariant.Locked
                                    Ux4gOtpInputStatus.Warning -> Ux4gOtpCaptionVariant.Warning
                                    Ux4gOtpInputStatus.Default -> Ux4gOtpCaptionVariant.ResendTimer
                                },
                                captionLeadingText = when (testStatus) {
                                    Ux4gOtpInputStatus.Error -> "Attempt ${4 - testAttemptsLeft} of 3"
                                    Ux4gOtpInputStatus.Locked -> "Locked for 28:43"
                                    else -> "Didn't receive OTP?"
                                },
                                captionTrailingText = when (testStatus) {
                                    Ux4gOtpInputStatus.Error -> "Resend in 00:30"
                                    Ux4gOtpInputStatus.Locked -> "Resend OTP"
                                    else -> "Resend in"
                                },
                                captionText = when (testStatus) {
                                    Ux4gOtpInputStatus.Success -> "OTP Verification successful!"
                                    Ux4gOtpInputStatus.Warning -> "Warning: Code expires in 60s"
                                    else -> null
                                },
                                autoCountdownSeconds = if (testStatus == Ux4gOtpInputStatus.Default) testCountdownSeconds else null,
                                onCompleted = { fullOtp ->
                                    lastEventLog = "onCompleted fired with: \"$fullOtp\""
                                    scope.launch {
                                        toastHostState.showToast(category = Ux4gToastCategory.Success, title = "OTP Completed", subtitle = fullOtp)
                                    }
                                },
                                onCountdownComplete = {
                                    lastEventLog = "Countdown finished! Ready to resend."
                                },
                                onCaptionTrailingTap = {
                                    lastEventLog = "Resend OTP tapped! Restarting 30s timer."
                                    testCountdownSeconds = 30
                                    scope.launch {
                                        toastHostState.showToast(category = Ux4gToastCategory.Success, title = "New OTP Sent", subtitle = "Sent new code to your device")
                                    }
                                }
                            )

                            // ── Controls: Length & Obscure ───────────────────
                            Text(text = "Configuration Controls:", style = Ux4gTheme.typography.bS_strong)
                            FlowRow(
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Ux4gButton(
                                    text = if (testLength == 6) "Length: 6 Digits (Active)" else "Switch to 6 Digits",
                                    variant = if (testLength == 6) Ux4gButtonVariant.Primary else Ux4gButtonVariant.Outline,
                                    size = Ux4gButtonSize.Small,
                                    onClick = {
                                        testLength = 6
                                        testOtp = testOtp.take(6)
                                        lastEventLog = "Length set to 6"
                                    }
                                )

                                Ux4gButton(
                                    text = if (testLength == 4) "Length: 4 Digits (Active)" else "Switch to 4 Digits",
                                    variant = if (testLength == 4) Ux4gButtonVariant.Primary else Ux4gButtonVariant.Outline,
                                    size = Ux4gButtonSize.Small,
                                    onClick = {
                                        testLength = 4
                                        testOtp = testOtp.take(4)
                                        lastEventLog = "Length set to 4"
                                    }
                                )

                                Ux4gButton(
                                    text = if (testObscure) "Masked: ON (•)" else "Masked: OFF",
                                    variant = if (testObscure) Ux4gButtonVariant.Secondary else Ux4gButtonVariant.Outline,
                                    size = Ux4gButtonSize.Small,
                                    onClick = {
                                        testObscure = !testObscure
                                        lastEventLog = "Obscure toggled to $testObscure"
                                    }
                                )

                                Ux4gButton(
                                    text = if (testShowSeparator) "Separator: ON (–)" else "Separator: OFF",
                                    variant = if (testShowSeparator) Ux4gButtonVariant.Secondary else Ux4gButtonVariant.Outline,
                                    size = Ux4gButtonSize.Small,
                                    onClick = {
                                        testShowSeparator = !testShowSeparator
                                        lastEventLog = "Separator toggled to $testShowSeparator"
                                    }
                                )

                                Ux4gButton(
                                    text = if (testEnabled) "Enabled" else "Disabled",
                                    variant = if (testEnabled) Ux4gButtonVariant.Secondary else Ux4gButtonVariant.Outline,
                                    size = Ux4gButtonSize.Small,
                                    onClick = {
                                        testEnabled = !testEnabled
                                        lastEventLog = "Enabled toggled to $testEnabled"
                                    }
                                )
                            }

                            // ── Controls: Status Switches ────────────────────
                            Text(text = "Simulate Status:", style = Ux4gTheme.typography.bS_strong)
                            FlowRow(
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Ux4gButton(
                                    text = "Default",
                                    variant = if (testStatus == Ux4gOtpInputStatus.Default) Ux4gButtonVariant.Primary else Ux4gButtonVariant.Outline,
                                    size = Ux4gButtonSize.Small,
                                    onClick = { testStatus = Ux4gOtpInputStatus.Default }
                                )
                                Ux4gButton(
                                    text = "Success",
                                    variant = if (testStatus == Ux4gOtpInputStatus.Success) Ux4gButtonVariant.Primary else Ux4gButtonVariant.Outline,
                                    size = Ux4gButtonSize.Small,
                                    onClick = { testStatus = Ux4gOtpInputStatus.Success }
                                )
                                Ux4gButton(
                                    text = "Error",
                                    variant = if (testStatus == Ux4gOtpInputStatus.Error) Ux4gButtonVariant.Secondary else Ux4gButtonVariant.Outline,
                                    size = Ux4gButtonSize.Small,
                                    onClick = { testStatus = Ux4gOtpInputStatus.Error }
                                )
                                Ux4gButton(
                                    text = "Warning",
                                    variant = if (testStatus == Ux4gOtpInputStatus.Warning) Ux4gButtonVariant.Secondary else Ux4gButtonVariant.Outline,
                                    size = Ux4gButtonSize.Small,
                                    onClick = { testStatus = Ux4gOtpInputStatus.Warning }
                                )
                                Ux4gButton(
                                    text = "Locked",
                                    variant = if (testStatus == Ux4gOtpInputStatus.Locked) Ux4gButtonVariant.Secondary else Ux4gButtonVariant.Outline,
                                    size = Ux4gButtonSize.Small,
                                    onClick = { testStatus = Ux4gOtpInputStatus.Locked }
                                )
                            }

                            // ── Action Buttons ───────────────────────────────
                            Text(text = "Quick Actions:", style = Ux4gTheme.typography.bS_strong)
                            FlowRow(
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Ux4gButton(
                                    text = "Verify (Correct: 123456)",
                                    variant = Ux4gButtonVariant.Primary,
                                    size = Ux4gButtonSize.Small,
                                    onClick = {
                                        val correct = if (testLength == 4) "1234" else "123456"
                                        if (testOtp == correct) {
                                            testStatus = Ux4gOtpInputStatus.Success
                                            lastEventLog = "Verification successful! Code matches $correct"
                                            scope.launch {
                                                toastHostState.showToast(category = Ux4gToastCategory.Success, title = "Verified", subtitle = "Code verified successfully!")
                                            }
                                        } else {
                                            val remaining = (testAttemptsLeft - 1).coerceAtLeast(0)
                                            testAttemptsLeft = remaining
                                            if (remaining == 0) {
                                                testStatus = Ux4gOtpInputStatus.Locked
                                                lastEventLog = "Max attempts reached! Account locked."
                                                scope.launch {
                                                    toastHostState.showToast(category = Ux4gToastCategory.Error, title = "Account Locked", subtitle = "Locked due to 3 failed attempts")
                                                }
                                            } else {
                                                testStatus = Ux4gOtpInputStatus.Error
                                                lastEventLog = "Incorrect code! Attempts left: $remaining"
                                                scope.launch {
                                                    toastHostState.showToast(category = Ux4gToastCategory.Error, title = "Incorrect Code", subtitle = "$remaining attempts remaining")
                                                }
                                            }
                                        }
                                    }
                                )

                                Ux4gButton(
                                    text = "Paste \"${if (testLength == 4) "5821" else "492815"}\"",
                                    variant = Ux4gButtonVariant.Outline,
                                    size = Ux4gButtonSize.Small,
                                    onClick = {
                                        val pasteCode = if (testLength == 4) "5821" else "492815"
                                        testOtp = pasteCode
                                        lastEventLog = "Pasted SMS OTP: $pasteCode"
                                        scope.launch {
                                            toastHostState.showToast(category = Ux4gToastCategory.Info, title = "Pasted SMS OTP", subtitle = pasteCode)
                                        }
                                    }
                                )

                                Ux4gButton(
                                    text = "Clear",
                                    variant = Ux4gButtonVariant.Outline,
                                    size = Ux4gButtonSize.Small,
                                    onClick = {
                                        testOtp = ""
                                        testStatus = Ux4gOtpInputStatus.Default
                                        lastEventLog = "OTP cleared"
                                    }
                                )
                            }
                        }
                    }

                    // ══════════════════════════════════════════════════════════════════
                    // 📋 STATIC VARIANT SHOWCASE
                    // ══════════════════════════════════════════════════════════════════
                    Text(text = "Visual Variants & States", style = Ux4gTheme.typography.hXS_strong)

                    var staticOtp1 by remember { mutableStateOf("123") }
                    var staticOtp2 by remember { mutableStateOf("123456") }
                    var staticOtp3 by remember { mutableStateOf("987") }
                    var staticOtp4 by remember { mutableStateOf("") }
                    var staticOtp5 by remember { mutableStateOf("5678") }
                    var staticOtp6 by remember { mutableStateOf("12") }

                    // 1. Standard 6-Digit with Timer
                    Text(text = "1. Standard 6-Digit with Auto Countdown", style = Ux4gTheme.typography.lM_strong)
                    Ux4gOtpInput(
                        value = staticOtp1,
                        onValueChange = { staticOtp1 = it },
                        label = "Enter Verification Code",
                        required = true,
                        labelTrailingIcon = Icons.Outlined.Info,
                        captionVariant = Ux4gOtpCaptionVariant.ResendTimer,
                        autoCountdownSeconds = 30
                    )

                    // 2. Success State
                    Text(text = "2. Success State", style = Ux4gTheme.typography.lM_strong)
                    Ux4gOtpInput(
                        value = staticOtp2,
                        onValueChange = { staticOtp2 = it },
                        status = Ux4gOtpInputStatus.Success,
                        label = "Verified OTP",
                        captionVariant = Ux4gOtpCaptionVariant.Success,
                        captionText = "Verification successful"
                    )

                    // 3. Error with Attempt Counter
                    Text(text = "3. Error State with Attempt Counter", style = Ux4gTheme.typography.lM_strong)
                    Ux4gOtpInput(
                        value = staticOtp3,
                        onValueChange = { staticOtp3 = it },
                        status = Ux4gOtpInputStatus.Error,
                        label = "Invalid OTP",
                        captionVariant = Ux4gOtpCaptionVariant.AttemptWithTimer,
                        captionLeadingText = "Attempt 2 of 3",
                        captionTrailingText = "Resend in 00:45"
                    )

                    // 4. Locked State
                    Text(text = "4. Locked State", style = Ux4gTheme.typography.lM_strong)
                    Ux4gOtpInput(
                        value = staticOtp4,
                        onValueChange = { staticOtp4 = it },
                        status = Ux4gOtpInputStatus.Locked,
                        label = "Account Temporarily Locked",
                        captionVariant = Ux4gOtpCaptionVariant.Locked,
                        captionLeadingText = "Locked for 28:43",
                        captionTrailingText = "Resend OTP",
                        onCaptionTrailingTap = {}
                    )

                    // 5. Password / Obscured Mode
                    Text(text = "5. Obscure Mode (4-Digit PIN)", style = Ux4gTheme.typography.lM_strong)
                    Ux4gOtpInput(
                        value = staticOtp5,
                        onValueChange = { staticOtp5 = it },
                        length = 4,
                        obscure = true,
                        label = "Secure Transaction PIN",
                        captionVariant = Ux4gOtpCaptionVariant.Plain,
                        captionText = "PIN is encrypted and securely sent"
                    )

                    // 6. Warning State
                    Text(text = "6. Warning State", style = Ux4gTheme.typography.lM_strong)
                    Ux4gOtpInput(
                        value = staticOtp6,
                        onValueChange = { staticOtp6 = it },
                        status = Ux4gOtpInputStatus.Warning,
                        label = "OTP Expiring Soon",
                        captionVariant = Ux4gOtpCaptionVariant.Warning,
                        captionText = "Code will expire in less than 1 minute"
                    )
                }
            }

            // ══════════════════════════════════════════════════════════════════
            // 27. JOURNEY TIMELINE COMPONENT SHOWCASE
            // ══════════════════════════════════════════════════════════════════
            Ux4gCard(modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    Text(
                        text = "27. Journey Timeline (UX4G 1:1 Complete Feature Parity)",
                        style = Ux4gTheme.typography.tS_strong
                    )

                    // ──────────────────────────────────────────────────────────
                    // 🌟 INTERACTIVE TESTING PLAYGROUND
                    // ──────────────────────────────────────────────────────────
                    Ux4gCard(modifier = Modifier.fillMaxWidth()) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Ux4gTheme.colorScheme.surface)
                                .padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(14.dp)
                        ) {
                            Text(
                                text = "🧪 Interactive Journey Playground",
                                style = Ux4gTheme.typography.hS_strong,
                                color = Ux4gTheme.colorScheme.primary
                            )
                            Text(
                                text = "Dynamically control the active step, toggle orientation, step numbering, and test real-time timeline state transitions.",
                                style = Ux4gTheme.typography.bXS_default,
                                color = Ux4gTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                            )

                            // Interactive State
                            var currentJourneyStep by remember { mutableStateOf(1) }
                            var journeyOrientation by remember { mutableStateOf(Ux4gJourneyOrientation.Vertical) }
                            var useStepNumbers by remember { mutableStateOf(false) }

                            // Live Status Badge
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(Ux4gColors.primary50)
                                    .padding(horizontal = 12.dp, vertical = 8.dp)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "Current Step Index: $currentJourneyStep (Step ${currentJourneyStep + 1} of 4)",
                                        style = Ux4gTheme.typography.lS_strong,
                                        color = Ux4gColors.primary700
                                    )
                                    Text(
                                        text = "Orientation: ${journeyOrientation.name}",
                                        style = Ux4gTheme.typography.lS_default,
                                        color = Ux4gColors.primary600
                                    )
                                }
                            }

                            // Interactive Controls
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Ux4gButton(
                                    text = "◀ Previous",
                                    variant = Ux4gButtonVariant.Outline,
                                    size = Ux4gButtonSize.Small,
                                    enabled = currentJourneyStep > 0,
                                    onClick = { if (currentJourneyStep > 0) currentJourneyStep-- }
                                )

                                Ux4gButton(
                                    text = "Next ▶",
                                    variant = Ux4gButtonVariant.Primary,
                                    size = Ux4gButtonSize.Small,
                                    enabled = currentJourneyStep < 3,
                                    onClick = { if (currentJourneyStep < 3) currentJourneyStep++ }
                                )

                                Ux4gButton(
                                    text = "Reset",
                                    variant = Ux4gButtonVariant.Text,
                                    size = Ux4gButtonSize.Small,
                                    onClick = { currentJourneyStep = 0 }
                                )
                            }

                            // Toggle Controls
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Ux4gButton(
                                    text = if (journeyOrientation == Ux4gJourneyOrientation.Vertical) "Orientation: Vertical" else "Orientation: Horizontal",
                                    variant = Ux4gButtonVariant.Outline,
                                    size = Ux4gButtonSize.Small,
                                    onClick = {
                                        journeyOrientation = if (journeyOrientation == Ux4gJourneyOrientation.Vertical) {
                                            Ux4gJourneyOrientation.Horizontal
                                        } else {
                                            Ux4gJourneyOrientation.Vertical
                                        }
                                    }
                                )

                                Ux4gButton(
                                    text = if (useStepNumbers) "Numbers: ON" else "Numbers: OFF",
                                    variant = if (useStepNumbers) Ux4gButtonVariant.Secondary else Ux4gButtonVariant.Outline,
                                    size = Ux4gButtonSize.Small,
                                    onClick = { useStepNumbers = !useStepNumbers }
                                )
                            }

                            Spacer(modifier = Modifier.height(4.dp))

                            // Interactive Timeline Component
                            Ux4gJourneyTimeline(
                                currentStep = currentJourneyStep,
                                orientation = journeyOrientation,
                                header = Ux4gJourneyHeader(
                                    title = "Passport Application Lifecycle",
                                    description = "Real-time verification and dispatch pipeline",
                                    icon = Ux4gIcons.info
                                ),
                                steps = listOf(
                                    Ux4gJourneyStep(
                                        title = "Online Application Submitted",
                                        date = "10 Aug, 09:30 AM",
                                        tag = "ARN: 2498214",
                                        helpingText = "Application details and fee payment confirmed.",
                                        stepNumber = if (useStepNumbers) "1" else null
                                    ),
                                    Ux4gJourneyStep(
                                        title = "PSK Appointment & Biometrics",
                                        date = "12 Aug, 11:00 AM",
                                        tag = "Slot Confirmed",
                                        helpingText = "Biometric scanning and document verification completed.",
                                        stepNumber = if (useStepNumbers) "2" else null,
                                        status = Ux4gJourneyStepStatus(
                                            text = "Verification Completed",
                                            badgeText = "Verified",
                                            badgePosition = Ux4gJourneyBadgePosition.Bottom
                                        )
                                    ),
                                    Ux4gJourneyStep(
                                        title = "Police Verification",
                                        date = "15 Aug, 03:00 PM",
                                        tag = "In Progress",
                                        helpingText = "Local police station inquiry and address verification.",
                                        stepNumber = if (useStepNumbers) "3" else null,
                                        status = Ux4gJourneyStepStatus(
                                            text = "Pending Report",
                                            badgeText = "Active",
                                            badgePosition = Ux4gJourneyBadgePosition.TopRight
                                        )
                                    ),
                                    Ux4gJourneyStep(
                                        title = "Passport Printing & Speed Post Dispatch",
                                        date = "Estimated: 20 Aug",
                                        tag = "Final Stage",
                                        helpingText = "Passport will be printed and dispatched via Speed Post.",
                                        stepNumber = if (useStepNumbers) "4" else null
                                    )
                                )
                            )
                        }
                    }

                    // ──────────────────────────────────────────────────────────
                    // 📋 STATIC VARIANT SHOWCASES
                    // ──────────────────────────────────────────────────────────
                    Text(text = "Visual Variants & Orientations", style = Ux4gTheme.typography.hXS_strong)

                    // 1. Standard Vertical with Rich Metadata & Pill Status
                    Text(text = "1. Vertical Journey with Unified Pill Tags & Badges", style = Ux4gTheme.typography.lM_strong)
                    Ux4gJourneyTimeline(
                        header = Ux4gJourneyHeader(
                            title = "Scholarship Disbursal Journey",
                            description = "Academic Year 2026-27 Merit Grant",
                            icon = Ux4gIcons.Home
                        ),
                        steps = listOf(
                            Ux4gJourneyStep(
                                state = Ux4gJourneyStepState.Completed,
                                date = "01 Jul 2026",
                                tag = "Stage 1",
                                title = "Student Application",
                                helpingText = "All academic transcripts verified by University.",
                                icon = Ux4gIcons.Check
                            ),
                            Ux4gJourneyStep(
                                state = Ux4gJourneyStepState.Completed,
                                date = "15 Jul 2026",
                                tag = "Stage 2",
                                title = "Nodal Officer Verification",
                                helpingText = "Income and caste certificates approved.",
                                icon = Ux4gIcons.Check
                            ),
                            Ux4gJourneyStep(
                                state = Ux4gJourneyStepState.Current,
                                date = "01 Aug 2026",
                                title = "State Board Sanction",
                                helpingText = "Fund allocation order under processing by Finance Dept.",
                                status = Ux4gJourneyStepStatus(
                                    text = "3 days remaining",
                                    badgeText = "Sanction In-Progress",
                                    badgePosition = Ux4gJourneyBadgePosition.Bottom
                                )
                            ),
                            Ux4gJourneyStep(
                                state = Ux4gJourneyStepState.Upcoming,
                                date = "15 Aug 2026",
                                title = "Direct DBT Bank Transfer",
                                helpingText = "Amount will be credited directly to Aadhaar-seeded bank account.",
                                status = Ux4gJourneyStepStatus(
                                    text = "Awaiting Release",
                                    badgeText = "Pending",
                                    badgePosition = Ux4gJourneyBadgePosition.TopRight
                                )
                            )
                        )
                    )

                    // 2. Horizontal Stepper Timeline
                    Text(text = "2. Horizontal Timeline with Dynamic Active Step Card", style = Ux4gTheme.typography.lM_strong)
                    Ux4gJourneyTimeline(
                        orientation = Ux4gJourneyOrientation.Horizontal,
                        currentStep = 2,
                        header = Ux4gJourneyHeader(
                            title = "Order Delivery Status",
                            description = "Package tracking #IN-89324-912"
                        ),
                        steps = listOf(
                            Ux4gJourneyStep(
                                title = "Order Placed",
                                date = "10 Aug"
                            ),
                            Ux4gJourneyStep(
                                title = "Dispatched from Hub",
                                date = "12 Aug"
                            ),
                            Ux4gJourneyStep(
                                title = "Out for Delivery",
                                date = "Today, 10:00 AM",
                                helpingText = "Delivery agent Rajesh is on the way with your package.",
                                status = Ux4gJourneyStepStatus(
                                    text = "Arriving by 5:00 PM",
                                    badgeText = "On Track",
                                    badgePosition = Ux4gJourneyBadgePosition.Bottom
                                )
                            ),
                            Ux4gJourneyStep(
                                title = "Delivered",
                                date = "Pending"
                            )
                        )
                    )

                    // 3. Step Number Indicators
                    Text(text = "3. Step Number Indicators (Numeric Stepper)", style = Ux4gTheme.typography.lM_strong)
                    Ux4gJourneyTimeline(
                        currentStep = 1,
                        steps = listOf(
                            Ux4gJourneyStep(
                                title = "Fill Personal Information",
                                date = "Step 1",
                                stepNumber = "1",
                                helpingText = "Name, date of birth, address."
                            ),
                            Ux4gJourneyStep(
                                title = "Upload Supporting Documents",
                                date = "Step 2",
                                stepNumber = "2",
                                helpingText = "Proof of address, photo ID, salary slips.",
                                status = Ux4gJourneyStepStatus(
                                    text = "2 Documents Pending",
                                    badgeText = "Action Required",
                                    badgePosition = Ux4gJourneyBadgePosition.Bottom
                                )
                            ),
                            Ux4gJourneyStep(
                                title = "Review & Submit",
                                date = "Step 3",
                                stepNumber = "3",
                                helpingText = "Confirm all entered details before payment."
                            )
                        )
                    )

                    // 4. Custom Styling & Custom Content Slot
                    Text(text = "4. Custom Card Colors, Borders, and Embedded Content", style = Ux4gTheme.typography.lM_strong)
                    Ux4gJourneyTimeline(
                        steps = listOf(
                            Ux4gJourneyStep(
                                state = Ux4gJourneyStepState.Completed,
                                title = "Custom Card Styling Example",
                                date = "Special Step",
                                cardColor = Color(0xFFF0FDF4),
                                cardBorderColor = Color(0xFF86EFAC),
                                titleColor = Color(0xFF166534),
                                dateColor = Color(0xFF15803D),
                                helpingText = "This card uses custom background and border color overrides.",
                                customContent = {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clip(RoundedCornerShape(6.dp))
                                            .background(Color(0xFFDCFCE7))
                                            .padding(8.dp)
                                    ) {
                                        Text(
                                            text = "✓ Embedded custom content composable",
                                            style = Ux4gTheme.typography.bXS_strong,
                                            color = Color(0xFF166534)
                                        )
                                    }
                                }
                            ),
                            Ux4gJourneyStep(
                                state = Ux4gJourneyStepState.Current,
                                title = "Standard Theme Step",
                                date = "Normal Step",
                                helpingText = "This card uses the standard design system theme styling."
                            )
                        )
                    )
                }
            }

            // ══════════════════════════════════════════════════════════════════
            // 28. MODAL DIALOG COMPONENT SHOWCASE
            // ══════════════════════════════════════════════════════════════════
            Ux4gCard(modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = "Modal Dialog Showcase",
                        style = Ux4gTheme.typography.hM_strong,
                        color = Ux4gTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "Dialog modal matching UX4G Flutter spec with multiple header image styles, alignment, avatars, leading icons, destructive states, and footer configurations.",
                        style = Ux4gTheme.typography.bS_default,
                        color = Ux4gTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )

                    var activeModalType by remember { mutableStateOf<String?>(null) }

                    val gradientHeader: @Composable () -> Unit = {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
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
                    }

                    // Interactive Launch Buttons
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Ux4gButton(
                            onClick = { activeModalType = "default" },
                            modifier = Modifier.fillMaxWidth(),
                            text = "Open Default Modal (2 buttons, Left)"
                        )

                        Ux4gButton(
                            onClick = { activeModalType = "destructive" },
                            modifier = Modifier.fillMaxWidth(),
                            text = "Open Destructive Modal (Centered)",
                            backgroundColor = Ux4gColors.red500,
                            contentColor = Ux4gColors.white
                        )

                        Ux4gButton(
                            onClick = { activeModalType = "minimal" },
                            modifier = Modifier.fillMaxWidth(),
                            text = "Open Minimal Confirm Modal",
                            backgroundColor = Ux4gColors.neutral800,
                            contentColor = Ux4gColors.white
                        )

                        Ux4gButton(
                            onClick = { activeModalType = "avatar" },
                            modifier = Modifier.fillMaxWidth(),
                            text = "Avatar Modal (Centered + Padded)",
                            backgroundColor = Ux4gColors.primary700,
                            contentColor = Ux4gColors.white
                        )

                        Ux4gButton(
                            onClick = { activeModalType = "icon" },
                            modifier = Modifier.fillMaxWidth(),
                            text = "Icon Modal (No image + Split)",
                            backgroundColor = Ux4gColors.red700,
                            contentColor = Ux4gColors.white
                        )
                    }

                    // Static Inline Preview Card
                    Text(
                        text = "Static In-Page Preview (No Overlay)",
                        style = Ux4gTheme.typography.lM_strong,
                        color = Ux4gTheme.colorScheme.onSurface
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(Ux4gRadius.Radius16))
                            .background(Ux4gColors.neutral900.copy(alpha = 0.05f))
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Ux4gModalContent(
                            onDismiss = {},
                            modifier = Modifier.fillMaxWidth(),
                            headerImageContent = gradientHeader,
                            leadingItem = Ux4gModalLeadingItem.Avatar,
                            avatarInitials = "JD",
                            alignment = Ux4gModalAlignment.Centered,
                            showHeader = true,
                            headerTitle = "Live Preview Header",
                            showDescription = true,
                            descriptionText = "This card renders inline using Ux4gModalContent.",
                            showSubtitle = true,
                            showBody = true,
                            showFooter = true,
                            footerButtons = Ux4gModalFooterButtons.TwoButtons,
                            footerAlign = Ux4gModalFooterAlign.Center,
                            isDestructive = false
                        )
                    }

                    // Active Modal Dialogs
                    when (activeModalType) {
                        "default" -> {
                            Ux4gModal(
                                onDismiss = { activeModalType = null },
                                headerImageContent = gradientHeader,
                                showHeader = true,
                                headerTitle = "Header",
                                showSubtitle = true,
                                showBody = true,
                                showFooter = true,
                                footerButtons = Ux4gModalFooterButtons.TwoButtons,
                                footerAlign = Ux4gModalFooterAlign.Right,
                                isDestructive = false,
                                onPrimaryClick = { activeModalType = null },
                                onSecondaryClick = { activeModalType = null }
                            )
                        }
                        "destructive" -> {
                            Ux4gModal(
                                onDismiss = { activeModalType = null },
                                headerImageContent = gradientHeader,
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
                                onPrimaryClick = { activeModalType = null },
                                onSecondaryClick = { activeModalType = null }
                            )
                        }
                        "minimal" -> {
                            Ux4gModal(
                                onDismiss = { activeModalType = null },
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
                                onPrimaryClick = { activeModalType = null },
                                onSecondaryClick = { activeModalType = null }
                            )
                        }
                        "avatar" -> {
                            Ux4gModal(
                                onDismiss = { activeModalType = null },
                                headerImageContent = gradientHeader,
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
                                onPrimaryClick = { activeModalType = null }
                            )
                        }
                        "icon" -> {
                            Ux4gModal(
                                onDismiss = { activeModalType = null },
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
                                onPrimaryClick = { activeModalType = null },
                                onSecondaryClick = { activeModalType = null }
                            )
                        }
                    }
                }
            }

            // ══════════════════════════════════════════════════════════════════
            // 29. TOOLTIP COMPONENT SHOWCASE
            // ══════════════════════════════════════════════════════════════════
            Ux4gCard(modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = "29. Tooltips (UX4G 1:1 Complete Parity)",
                        style = Ux4gTheme.typography.tS_strong
                    )
                    Text(
                        text = "Floating contextual tooltips with directional arrows, 12 placement orientations, rich headers, icons, action buttons, and interactive triggers.",
                        style = Ux4gTheme.typography.bS_default,
                        color = Ux4gTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )

                    // 1. Directional Placements
                    Text(text = "Top Placements (Arrow Down)", style = Ux4gTheme.typography.lM_strong)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Ux4gTooltipBubble(text = "TopStart", icon = Ux4gIcons.info_circle, placement = Ux4gTooltipPlacement.TopStart)
                        Ux4gTooltipBubble(text = "Top", icon = Ux4gIcons.info_circle, placement = Ux4gTooltipPlacement.Top)
                        Ux4gTooltipBubble(text = "TopEnd", icon = Ux4gIcons.info_circle, placement = Ux4gTooltipPlacement.TopEnd)
                    }

                    Text(text = "Bottom Placements (Arrow Up)", style = Ux4gTheme.typography.lM_strong)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Ux4gTooltipBubble(text = "BottomStart", icon = Ux4gIcons.info_circle, placement = Ux4gTooltipPlacement.BottomStart)
                        Ux4gTooltipBubble(text = "Bottom", icon = Ux4gIcons.info_circle, placement = Ux4gTooltipPlacement.Bottom)
                        Ux4gTooltipBubble(text = "BottomEnd", icon = Ux4gIcons.info_circle, placement = Ux4gTooltipPlacement.BottomEnd)
                    }

                    Text(text = "Side Placements (Left & Right)", style = Ux4gTheme.typography.lM_strong)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Ux4gTooltipBubble(text = "Left", icon = Ux4gIcons.info_circle, placement = Ux4gTooltipPlacement.Left)
                        Ux4gTooltipBubble(text = "Right", icon = Ux4gIcons.info_circle, placement = Ux4gTooltipPlacement.Right)
                    }

                    // 2. Rich Tooltip Bubble
                    Text(text = "Rich Tooltip Bubble", style = Ux4gTheme.typography.lM_strong)
                    Ux4gRichTooltipBubble(
                        title = "Tooltip Title",
                        text = "This is a rich tooltip bubble with a title, an icon, and body text.",
                        icon = Ux4gIcons.info_circle,
                        placement = Ux4gTooltipPlacement.Top,
                        action = {
                            Text(
                                text = "Action",
                                color = Ux4gColors.primary,
                                style = Ux4gTheme.typography.lS_strong,
                                modifier = Modifier.clickable { }
                            )
                        }
                    )

                    // 3. Interactive Floating Tooltip Boxes
                    Text(text = "Interactive Tooltip Triggers (Tap or Long-Press)", style = Ux4gTheme.typography.lM_strong)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Ux4gTooltip(
                            tooltipText = "Standard tooltip text!",
                            icon = Ux4gIcons.info_circle,
                            placement = Ux4gTooltipPlacement.Top
                        ) {
                            Ux4gButton(
                                text = "Tap Tooltip",
                                variant = Ux4gButtonVariant.Outline,
                                size = Ux4gButtonSize.Small,
                                onClick = {}
                            )
                        }

                        Ux4gRichTooltip(
                            title = "Rich Popup",
                            tooltipText = "Tap anywhere outside to dismiss.",
                            icon = Ux4gIcons.info_circle,
                            placement = Ux4gTooltipPlacement.Bottom,
                            action = {
                                Text(
                                    text = "Learn More",
                                    color = Ux4gColors.primary,
                                    style = Ux4gTheme.typography.lS_strong
                                )
                            }
                        ) {
                            Ux4gButton(
                                text = "Rich Tooltip",
                                variant = Ux4gButtonVariant.Primary,
                                size = Ux4gButtonSize.Small,
                                onClick = {}
                            )
                        }
                    }
                }
            }

            // ══════════════════════════════════════════════════════════════════
            // 30. FEEDBACK FORMS SHOWCASE (Star, CSAT, NPS)
            // ══════════════════════════════════════════════════════════════════
            Ux4gCard(modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    Text(
                        text = "30. Feedback Forms (Star, CSAT & NPS)",
                        style = Ux4gTheme.typography.tS_strong
                    )
                    Text(
                        text = "Interactive feedback components with exact Flutter UI parity, dynamic color thresholds, conditional text areas, chip selectors, and success confirmation views.",
                        style = Ux4gTheme.typography.bS_default,
                        color = Ux4gTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )

                    // 1. Star Feedback Form
                    Text(text = "1. Star Rating Feedback Form", style = Ux4gTheme.typography.lM_strong)
                    Ux4gFeedbackFormStar(
                        improvementOptions = listOf(
                            "Content accuracy",
                            "Visual design",
                            "Performance",
                            "Navigation"
                        ),
                        onSubmit = { rating, chips, comment ->
                            println("Star Feedback Submitted: Rating: $rating, Chips: $chips, Comment: $comment")
                        },
                        onSkip = {
                            println("Star Feedback Skipped")
                        }
                    )

                    // 2. CSAT Face Feedback Form
                    Text(text = "2. CSAT Face Rating Feedback Form", style = Ux4gTheme.typography.lM_strong)
                    Ux4gFeedbackFormCsat(
                        onSubmit = { score, comment ->
                            println("CSAT Submitted: Score: $score, Comment: $comment")
                        },
                        onSkip = {
                            println("CSAT Skipped")
                        }
                    )

                    // 3. NPS 0-10 Feedback Form
                    Text(text = "3. NPS Score Feedback Form (0-10)", style = Ux4gTheme.typography.lM_strong)
                    Ux4gFeedbackFormNps(
                        onSubmit = { score, comment ->
                            println("NPS Submitted: Score: $score, Comment: $comment")
                        },
                        onSkip = {
                            println("NPS Skipped")
                        }
                    )
                }
            }

            // ══════════════════════════════════════════════════════════════════
            // 31. TEXT AREA COMPONENT SHOWCASE
            // ══════════════════════════════════════════════════════════════════
            Ux4gCard(modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = "31. Text Area (UX4G 1:1 Parity)",
                        style = Ux4gTheme.typography.tS_strong
                    )
                    Text(
                        text = "Multi-line text area with exact typography tokens, custom diagonal resize grip canvas, live character counting, and status indicators.",
                        style = Ux4gTheme.typography.bS_default,
                        color = Ux4gTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )

                    var sampleText1 by remember { mutableStateOf("") }
                    var sampleText2 by remember { mutableStateOf("Valid input text") }

                    Ux4gTextArea(
                        value = sampleText1,
                        onValueChange = { sampleText1 = it },
                        label = "Feedback Comment",
                        required = true,
                        placeholder = "Tell us what you think...",
                        caption = "Maximum 200 characters allowed.",
                        maxLength = 200
                    )

                    Ux4gTextArea(
                        value = sampleText2,
                        onValueChange = { sampleText2 = it },
                        label = "Success State",
                        status = Ux4gTextAreaStatus.Success,
                        caption = "Comment validated successfully.",
                        maxLength = 100
                    )
                }
            }

            // ══════════════════════════════════════════════════════════════════
            // 32. LINEAR PROGRESS BAR SHOWCASE
            // ══════════════════════════════════════════════════════════════════
            Ux4gCard(modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = "32. Linear Progress Bar (UX4G 1:1 Parity)",
                        style = Ux4gTheme.typography.tS_strong
                    )
                    Text(
                        text = "Linear progress indicator matching Flutter linear_progress.dart & linear_progress_bar.dart with gradient fills, labels, hint text, inside/outside percentages, sharp/rounded shapes, and smooth animations.",
                        style = Ux4gTheme.typography.bS_default,
                        color = Ux4gTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )

                    var animatedVal by remember { mutableStateOf(0.4f) }

                    // 1. Basic Progress Bar
                    Text(text = "1. Basic Progress Bar (Default 8dp & Gradient)", style = Ux4gTheme.typography.lM_strong)
                    Ux4gLinearProgressBar(value = 0.4f)

                    // 2. With Label, Hint & Outside Percentage
                    Text(text = "2. With Label, Hint & Outside Percentage", style = Ux4gTheme.typography.lM_strong)
                    Ux4gLinearProgressBar(
                        value = 0.75f,
                        label = "Uploading documents",
                        hint = "3 of 4 files",
                        showPercentage = true,
                        labelPosition = Ux4gProgressLabelPosition.Outside
                    )

                    // 3. With Icon & Inside Percentage
                    Text(text = "3. With Icon & Inside Percentage (18dp height)", style = Ux4gTheme.typography.lM_strong)
                    Ux4gLinearProgressBar(
                        value = 0.65f,
                        icon = Ux4gIcons.info_circle,
                        label = "Application Verification",
                        height = 18.dp,
                        showPercentage = true,
                        labelPosition = Ux4gProgressLabelPosition.Inside
                    )

                    // 4. Custom Solid Color & Sharp Shape
                    Text(text = "4. Solid Color & Sharp Shape", style = Ux4gTheme.typography.lM_strong)
                    Ux4gLinearProgressBar(
                        value = 0.5f,
                        shape = Ux4gProgressShape.Sharp,
                        height = 10.dp,
                        color = Ux4gColors.green600,
                        showPercentage = true
                    )

                    // 5. Interactive Animated Progress Bar
                    Text(text = "5. Animated Linear Progress Bar", style = Ux4gTheme.typography.lM_strong)
                    Ux4gAnimatedLinearProgressBar(
                        value = animatedVal,
                        label = "Dynamic Task Progress",
                        hint = "${(animatedVal * 100).toInt()}%",
                        showPercentage = true,
                        labelPosition = Ux4gProgressLabelPosition.Outside
                    )
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Ux4gButton(
                            text = "25%",
                            size = Ux4gButtonSize.Small,
                            variant = Ux4gButtonVariant.Outline,
                            onClick = { animatedVal = 0.25f }
                        )
                        Ux4gButton(
                            text = "50%",
                            size = Ux4gButtonSize.Small,
                            variant = Ux4gButtonVariant.Outline,
                            onClick = { animatedVal = 0.5f }
                        )
                        Ux4gButton(
                            text = "75%",
                            size = Ux4gButtonSize.Small,
                            variant = Ux4gButtonVariant.Outline,
                            onClick = { animatedVal = 0.75f }
                        )
                        Ux4gButton(
                            text = "100%",
                            size = Ux4gButtonSize.Small,
                            variant = Ux4gButtonVariant.Primary,
                            onClick = { animatedVal = 1.0f }
                        )
                    }
                }
            }

            // ══════════════════════════════════════════════════════════════════
            // 33. HALF CIRCLE (GAUGE) PROGRESS SHOWCASE
            // ══════════════════════════════════════════════════════════════════
            Ux4gCard(modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    Text(
                        text = "33. Half Circle (Gauge) Progress (UX4G 1:1 Parity)",
                        style = Ux4gTheme.typography.tS_strong
                    )
                    Text(
                        text = "A half-circle progress indicator (gauge / semicircle) matching Flutter half_circle_progress.dart. Features 180° clockwise sweep, automatic typography and stroke scaling, inside/outside description placement, scale endpoint labels (0% - 100%), gradient & solid color fills, and smooth animations.",
                        style = Ux4gTheme.typography.bS_default,
                        color = Ux4gTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )

                    var halfCircleVal by remember { mutableStateOf(0.65f) }

                    // 1. Size Variants (Without Scale)
                    Text(text = "1. Predefined Sizes (S, M, L)", style = Ux4gTheme.typography.lM_strong)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround,
                        verticalAlignment = Alignment.Bottom
                    ) {
                        Ux4gHalfCircleProgress(
                            value = 0.5f,
                            size = Ux4gHalfCircleProgressSize.S,
                            description = "Description"
                        )
                        Ux4gHalfCircleProgress(
                            value = 0.5f,
                            size = Ux4gHalfCircleProgressSize.M,
                            description = "Description"
                        )
                        Ux4gHalfCircleProgress(
                            value = 0.5f,
                            size = Ux4gHalfCircleProgressSize.L,
                            description = "Description"
                        )
                    }

                    // 2. XL Size Variant
                    Text(text = "2. XL Size Variant (280dp)", style = Ux4gTheme.typography.lM_strong)
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Ux4gHalfCircleProgress(
                            value = 0.72f,
                            size = Ux4gHalfCircleProgressSize.XL,
                            description = "Server Capacity"
                        )
                    }

                    // 3. With Scale Labels (0% & 100%)
                    Text(text = "3. With Scale Labels (0% and 100%)", style = Ux4gTheme.typography.lM_strong)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround,
                        verticalAlignment = Alignment.Bottom
                    ) {
                        Ux4gHalfCircleProgress(
                            value = 0.5f,
                            size = Ux4gHalfCircleProgressSize.S,
                            description = "Description",
                            showScale = true
                        )
                        Ux4gHalfCircleProgress(
                            value = 0.75f,
                            size = Ux4gHalfCircleProgressSize.M,
                            description = "Description",
                            showScale = true
                        )
                        Ux4gHalfCircleProgress(
                            value = 0.85f,
                            size = Ux4gHalfCircleProgressSize.L,
                            description = "Performance",
                            showScale = true
                        )
                    }

                    // 4. Custom Solid Color & Custom Gradient
                    Text(text = "4. Custom Solid Color & Custom Multi-Color Gradient", style = Ux4gTheme.typography.lM_strong)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround,
                        verticalAlignment = Alignment.Bottom
                    ) {
                        Ux4gHalfCircleProgress(
                            value = 0.90f,
                            size = Ux4gHalfCircleProgressSize.M,
                            progressColor = Ux4gColors.green600,
                            description = "Success Rate",
                            showScale = true
                        )
                        Ux4gHalfCircleProgress(
                            value = 0.65f,
                            size = Ux4gHalfCircleProgressSize.M,
                            gradientColors = listOf(Ux4gColors.orange600, Ux4gColors.yellow500),
                            description = "Battery Level",
                            showScale = true
                        )
                    }

                    // 5. Interactive Animated Half Circle Progress
                    Text(text = "5. Animated Half Circle Progress", style = Ux4gTheme.typography.lM_strong)
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Ux4gAnimatedHalfCircleProgress(
                            value = halfCircleVal,
                            size = Ux4gHalfCircleProgressSize.L,
                            description = "Live Monitoring",
                            showScale = true
                        )
                    }
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Ux4gButton(
                            text = "25%",
                            size = Ux4gButtonSize.Small,
                            variant = Ux4gButtonVariant.Outline,
                            onClick = { halfCircleVal = 0.25f }
                        )
                        Ux4gButton(
                            text = "50%",
                            size = Ux4gButtonSize.Small,
                            variant = Ux4gButtonVariant.Outline,
                            onClick = { halfCircleVal = 0.5f }
                        )
                        Ux4gButton(
                            text = "75%",
                            size = Ux4gButtonSize.Small,
                            variant = Ux4gButtonVariant.Outline,
                            onClick = { halfCircleVal = 0.75f }
                        )
                        Ux4gButton(
                            text = "100%",
                            size = Ux4gButtonSize.Small,
                            variant = Ux4gButtonVariant.Primary,
                            onClick = { halfCircleVal = 1.0f }
                        )
                    }
                }
            }
        }

        // Circular Progress Showcase
        Ux4gCard(modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                Text(
                    text = "34. Circular Progress Indicator (UX4G 1:1 Parity)",
                    style = Ux4gTheme.typography.tS_strong
                )
                Text(
                    text = "A full-circle progress ring component matching Flutter circular_progress.dart 1:1. Features 7 standard sizes (XS to XXXL), auto-scaling stroke widths, sweep gradients, round/butt stroke caps, center text & descriptions, metadata labels, background fills, and smooth animated transitions.",
                    style = Ux4gTheme.typography.bS_default,
                    color = Ux4gTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )

                var circularProgressVal by remember { mutableStateOf(0.70f) }

                // Showcase matching Image 1: Cloud Storage Card
                Text(text = "1. Main Card (Matching Image 1: 75% Cloud Storage)", style = Ux4gTheme.typography.lM_strong)
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Ux4gCircularProgress(
                        value = 0.75f,
                        size = Ux4gCircularProgressSize.XXXL,
                        centerValueText = "75%",
                        centerDescription = "Used",
                        label = "Cloud Storage",
                        description = "18 GB of 24 GB",
                    )
                }

                // Showcase matching Image 2: Size Variants with Text & Labels
                Text(text = "2. Size Variants & Labels (Matching Image 2)", style = Ux4gTheme.typography.lM_strong)
                Row(
                    modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(24.dp),
                    verticalAlignment = Alignment.Bottom
                ) {
                    // Small (28dp)
                    Ux4gCircularProgress(
                        value = 0.20f,
                        size = Ux4gCircularProgressSize.S,
                        label = "Small"
                    )
                    // Medium w/ Text (36dp)
                    Ux4gCircularProgress(
                        value = 0.65f,
                        size = Ux4gCircularProgressSize.M,
                        centerValueText = "65%",
                        label = "Medium w/ Text"
                    )
                    // Large w/ Label (48dp)
                    Ux4gCircularProgress(
                        value = 0.75f,
                        size = Ux4gCircularProgressSize.L,
                        label = "Large w/ Label",
                        description = "Loading"
                    )
                    // Extra Large (88dp)
                    Ux4gCircularProgress(
                        value = 0.90f,
                        size = Ux4gCircularProgressSize.XL,
                        centerValueText = "90%",
                        label = "Extra Large",
                        description = "Almost Done"
                    )
                }

                // Showcase matching Image 3: All 7 Predefined Sizes at 50%
                Text(text = "3. All 7 Sizes at 50% (Matching Image 3)", style = Ux4gTheme.typography.lM_strong)
                // Row A: XS, S, M, L, XL
                Row(
                    modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.Bottom
                ) {
                    Ux4gCircularProgress(
                        value = 0.5f,
                        size = Ux4gCircularProgressSize.XS,
                        centerValueText = "50%",
                        description = "Description"
                    )
                    Ux4gCircularProgress(
                        value = 0.5f,
                        size = Ux4gCircularProgressSize.S,
                        centerValueText = "50%",
                        description = "Description"
                    )
                    Ux4gCircularProgress(
                        value = 0.5f,
                        size = Ux4gCircularProgressSize.M,
                        centerValueText = "50%",
                        description = "Description"
                    )
                    Ux4gCircularProgress(
                        value = 0.5f,
                        size = Ux4gCircularProgressSize.L,
                        centerValueText = "50%",
                        description = "Description"
                    )
                    Ux4gCircularProgress(
                        value = 0.5f,
                        size = Ux4gCircularProgressSize.XL,
                        centerValueText = "50%",
                        description = "Description"
                    )
                }
                // Row B: XXL, XXXL
                Row(
                    modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(24.dp),
                    verticalAlignment = Alignment.Bottom
                ) {
                    Ux4gCircularProgress(
                        value = 0.5f,
                        size = Ux4gCircularProgressSize.XXL,
                        centerValueText = "50%",
                        description = "Description"
                    )
                    Ux4gCircularProgress(
                        value = 0.5f,
                        size = Ux4gCircularProgressSize.XXXL,
                        centerValueText = "50%",
                        description = "Description"
                    )
                }

                // Showcase matching Image 4: Status Cards with "8 days left" & Footer Badges (Purple, Orange, Red, Green)
                Text(text = "4. Status Variant Cards (Purple, Orange, Red, Green with Footer Tags)", style = Ux4gTheme.typography.lM_strong)
                Row(
                    modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(24.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    // Purple / Brand Variant
                    Ux4gCircularProgress(
                        value = 0.65f,
                        size = Ux4gCircularProgressSize.XL,
                        centerValueText = "8",
                        centerDescription = "days left",
                        label = "Label",
                        description = "Description",
                        footer = {
                            Box(
                                modifier = Modifier
                                    .background(Ux4gColors.primary50, RoundedCornerShape(4.dp))
                                    .padding(horizontal = 14.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = "Label",
                                    style = Ux4gTheme.typography.lS_strong,
                                    color = Ux4gColors.primary600
                                )
                            }
                        }
                    )

                    // Orange / Warning Variant
                    Ux4gCircularProgress(
                        value = 0.65f,
                        size = Ux4gCircularProgressSize.XL,
                        progressGradientColors = listOf(Ux4gColors.orange200, Ux4gColors.orange500),
                        centerValueText = "8",
                        centerDescription = "days left",
                        label = "Label",
                        description = "Description",
                        footer = {
                            Box(
                                modifier = Modifier
                                    .background(Ux4gColors.orange50, RoundedCornerShape(4.dp))
                                    .padding(horizontal = 14.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = "Label",
                                    style = Ux4gTheme.typography.lS_strong,
                                    color = Ux4gColors.orange500
                                )
                            }
                        }
                    )

                    // Red / Danger Variant (with reddish description)
                    Ux4gCircularProgress(
                        value = 0.65f,
                        size = Ux4gCircularProgressSize.XL,
                        progressGradientColors = listOf(Ux4gColors.red200, Ux4gColors.red600),
                        centerValueText = "8",
                        centerDescription = "days left",
                        label = "Label",
                        description = "Description",
                        descriptionStyle = Ux4gTheme.typography.bS_default.copy(color = Ux4gColors.red700),
                        footer = {
                            Box(
                                modifier = Modifier
                                    .background(Ux4gColors.red50, RoundedCornerShape(4.dp))
                                    .padding(horizontal = 14.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = "Label",
                                    style = Ux4gTheme.typography.lS_strong,
                                    color = Ux4gColors.red500
                                )
                            }
                        }
                    )

                    // Green / Success Variant
                    Ux4gCircularProgress(
                        value = 0.65f,
                        size = Ux4gCircularProgressSize.XL,
                        progressGradientColors = listOf(Ux4gColors.green200, Ux4gColors.green600),
                        centerValueText = "8",
                        centerDescription = "days left",
                        label = "Label",
                        description = "Description",
                        footer = {
                            Box(
                                modifier = Modifier
                                    .background(Ux4gColors.green50, RoundedCornerShape(4.dp))
                                    .padding(horizontal = 14.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = "Label",
                                    style = Ux4gTheme.typography.lS_strong,
                                    color = Ux4gColors.green600
                                )
                            }
                        }
                    )
                }

                // 5. Custom Colors, Gradients & Background Fills
                Text(text = "5. Custom Colors, Gradients & Round Caps", style = Ux4gTheme.typography.lM_strong)
                Row(
                    modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(20.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    // Solid Color
                    Ux4gCircularProgress(
                        value = 0.80f,
                        size = Ux4gCircularProgressSize.XL,
                        progressColor = Ux4gColors.green600,
                        centerValueText = "80%",
                        label = "Success Rate"
                    )
                    // Multi-color Sweep Gradient
                    Ux4gCircularProgress(
                        value = 0.60f,
                        size = Ux4gCircularProgressSize.XL,
                        progressGradientColors = listOf(Ux4gColors.orange600, Ux4gColors.yellow500),
                        centerValueText = "60%",
                        label = "Battery"
                    )
                    // Round Cap
                    Ux4gCircularProgress(
                        value = 0.70f,
                        size = Ux4gCircularProgressSize.XL,
                        strokeCap = StrokeCap.Round,
                        progressColor = Ux4gColors.cyan600,
                        centerValueText = "70%",
                        label = "Round Cap"
                    )
                    // Dark Background Fill
                    Ux4gCircularProgress(
                        value = 0.50f,
                        size = Ux4gCircularProgressSize.XL,
                        backgroundColor = Ux4gColors.primary900,
                        centerValueText = "50%",
                        label = "With Fill"
                    )
                }

                // 6. Interactive Animated Circular Progress
                Text(text = "6. Animated Circular Progress", style = Ux4gTheme.typography.lM_strong)
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Ux4gAnimatedCircularProgress(
                        value = circularProgressVal,
                        size = Ux4gCircularProgressSize.XXL,
                        centerValueText = "${(circularProgressVal * 100).toInt()}%",
                        centerDescription = "Progress",
                        label = "Live Sync",
                        description = "Smooth 700ms animation"
                    )
                }
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Ux4gButton(
                        text = "25%",
                        size = Ux4gButtonSize.Small,
                        variant = Ux4gButtonVariant.Outline,
                        onClick = { circularProgressVal = 0.25f }
                    )
                    Ux4gButton(
                        text = "50%",
                        size = Ux4gButtonSize.Small,
                        variant = Ux4gButtonVariant.Outline,
                        onClick = { circularProgressVal = 0.5f }
                    )
                    Ux4gButton(
                        text = "75%",
                        size = Ux4gButtonSize.Small,
                        variant = Ux4gButtonVariant.Outline,
                        onClick = { circularProgressVal = 0.75f }
                    )
                    Ux4gButton(
                        text = "100%",
                        size = Ux4gButtonSize.Small,
                        variant = Ux4gButtonVariant.Primary,
                        onClick = { circularProgressVal = 1.0f }
                    )
                }
            }
        }

        // Radio Button Showcase
        Ux4gCard(modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                Text(
                    text = "35. Radio Button (UX4G 1:1 Parity)",
                    style = Ux4gTheme.typography.tS_strong
                )
                Text(
                    text = "Radio button component matching Flutter radioButton.dart 1:1. Features Small (16dp), Medium (20dp), and Large (24dp) sizes, donut selected ring rendering, helper/error/warning/success description variants with icons, independent status ring colors, required asterisks, trailing icons, and disabled states.",
                    style = Ux4gTheme.typography.bS_default,
                    color = Ux4gTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )

                var selectedOption by remember { mutableStateOf("option1") }

                // 1. Standard Sizes (Small, Medium, Large)
                Text(text = "1. Predefined Sizes in a Radio Group", style = Ux4gTheme.typography.lM_strong)
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Ux4gRadioButton(
                        value = "option1",
                        groupValue = selectedOption,
                        onValueChange = { selectedOption = it },
                        label = "Small (16dp)",
                        size = Ux4gRadioButtonSize.Small,
                        description = "Small radio with helper text",
                        descriptionVariant = Ux4gRadioButtonDescriptionVariant.Helper
                    )
                    Ux4gRadioButton(
                        value = "option2",
                        groupValue = selectedOption,
                        onValueChange = { selectedOption = it },
                        label = "Medium (20dp) - Default",
                        size = Ux4gRadioButtonSize.Medium,
                        description = "Standard size for forms and selection lists",
                        descriptionVariant = Ux4gRadioButtonDescriptionVariant.Helper
                    )
                    Ux4gRadioButton(
                        value = "option3",
                        groupValue = selectedOption,
                        onValueChange = { selectedOption = it },
                        label = "Large (24dp)",
                        size = Ux4gRadioButtonSize.Large,
                        description = "Large size for prominent selections",
                        descriptionVariant = Ux4gRadioButtonDescriptionVariant.Helper
                    )
                }

                // 2. Validation & Description Variants with Icons
                Text(text = "2. Description & Validation Variants", style = Ux4gTheme.typography.lM_strong)
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    var validationChoice by remember { mutableStateOf("success") }

                    Ux4gRadioButton(
                        value = "helper",
                        groupValue = validationChoice,
                        onValueChange = { validationChoice = it },
                        label = "Helper Variant",
                        size = Ux4gRadioButtonSize.Medium,
                        description = "General information / helper message",
                        descriptionVariant = Ux4gRadioButtonDescriptionVariant.Helper
                    )
                    Ux4gRadioButton(
                        value = "success",
                        groupValue = validationChoice,
                        onValueChange = { validationChoice = it },
                        label = "Success Variant",
                        size = Ux4gRadioButtonSize.Medium,
                        description = "Selection validated successfully",
                        descriptionVariant = Ux4gRadioButtonDescriptionVariant.Success
                    )
                    Ux4gRadioButton(
                        value = "warning",
                        groupValue = validationChoice,
                        onValueChange = { validationChoice = it },
                        label = "Warning Variant",
                        size = Ux4gRadioButtonSize.Medium,
                        description = "This selection may incur extra charges",
                        descriptionVariant = Ux4gRadioButtonDescriptionVariant.Warning
                    )
                    Ux4gRadioButton(
                        value = "error",
                        groupValue = validationChoice,
                        onValueChange = { validationChoice = it },
                        label = "Error Variant",
                        size = Ux4gRadioButtonSize.Medium,
                        isRequired = true,
                        description = "Please select a valid option to proceed",
                        descriptionVariant = Ux4gRadioButtonDescriptionVariant.Error
                    )
                }

                // 3. Ring Status Variants (Independent of selection)
                Text(text = "3. Ring Status States (Error, Warning, Success)", style = Ux4gTheme.typography.lM_strong)
                Row(
                    modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    var statusChoice by remember { mutableStateOf("err") }

                    Ux4gRadioButton(
                        value = "err",
                        groupValue = statusChoice,
                        onValueChange = { statusChoice = it },
                        label = "Error Ring",
                        status = Ux4gRadioButtonStatus.Error,
                        size = Ux4gRadioButtonSize.Medium
                    )
                    Ux4gRadioButton(
                        value = "warn",
                        groupValue = statusChoice,
                        onValueChange = { statusChoice = it },
                        label = "Warning Ring",
                        status = Ux4gRadioButtonStatus.Warning,
                        size = Ux4gRadioButtonSize.Medium
                    )
                    Ux4gRadioButton(
                        value = "succ",
                        groupValue = statusChoice,
                        onValueChange = { statusChoice = it },
                        label = "Success Ring",
                        status = Ux4gRadioButtonStatus.Success,
                        size = Ux4gRadioButtonSize.Medium
                    )
                }

                // 4. Required & Trailing Icons
                Text(text = "4. Required Field (*) & Trailing Icons", style = Ux4gTheme.typography.lM_strong)
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    var iconChoice by remember { mutableStateOf("plan1") }

                    Ux4gRadioButton(
                        value = "plan1",
                        groupValue = iconChoice,
                        onValueChange = { iconChoice = it },
                        label = "Premium Enterprise Plan",
                        isRequired = true,
                        size = Ux4gRadioButtonSize.Medium,
                        trailingIcon = Ux4gIcons.Settings,
                        description = "Includes 24/7 dedicated support",
                        descriptionVariant = Ux4gRadioButtonDescriptionVariant.Helper
                    )
                    Ux4gRadioButton(
                        value = "plan2",
                        groupValue = iconChoice,
                        onValueChange = { iconChoice = it },
                        label = "Standard Business Plan",
                        isRequired = true,
                        size = Ux4gRadioButtonSize.Medium,
                        description = "For teams up to 50 members",
                        descriptionVariant = Ux4gRadioButtonDescriptionVariant.Helper
                    )
                }

                // 5. Custom Brand Colors
                Text(text = "5. Custom Brand Colors", style = Ux4gTheme.typography.lM_strong)
                Row(
                    modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    var colorOption by remember { mutableStateOf("green") }

                    Ux4gRadioButton(
                        value = "green",
                        groupValue = colorOption,
                        onValueChange = { colorOption = it },
                        label = "Emerald",
                        color = Ux4gColors.green600,
                        size = Ux4gRadioButtonSize.Medium
                    )
                    Ux4gRadioButton(
                        value = "orange",
                        groupValue = colorOption,
                        onValueChange = { colorOption = it },
                        label = "Amber",
                        color = Ux4gColors.orange600,
                        size = Ux4gRadioButtonSize.Medium
                    )
                    Ux4gRadioButton(
                        value = "cyan",
                        groupValue = colorOption,
                        onValueChange = { colorOption = it },
                        label = "Cyan",
                        color = Ux4gColors.cyan600,
                        size = Ux4gRadioButtonSize.Medium
                    )
                }

                // 6. Disabled States
                Text(text = "6. Disabled States", style = Ux4gTheme.typography.lM_strong)
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Ux4gRadioButton(
                        selected = true,
                        onClick = {},
                        enabled = false,
                        label = "Disabled Selected",
                        size = Ux4gRadioButtonSize.Medium,
                        description = "This option is disabled and cannot be changed",
                        descriptionVariant = Ux4gRadioButtonDescriptionVariant.Helper
                    )
                    Ux4gRadioButton(
                        selected = false,
                        onClick = {},
                        enabled = false,
                        label = "Disabled Unselected",
                        size = Ux4gRadioButtonSize.Medium,
                        description = "This option is currently unavailable",
                        descriptionVariant = Ux4gRadioButtonDescriptionVariant.Helper
                    )
                }
            }
        }

        // Search Field Showcase
        Ux4gCard(modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(text = "Search Field", style = Ux4gTheme.typography.tS_strong)

                var basicSearchValue by remember { mutableStateOf("") }
                var submitSearchValue by remember { mutableStateOf("Android Compose") }
                var tonalSubmitSearchValue by remember { mutableStateOf("Design System") }
                var autocompleteValue by remember { mutableStateOf("Ban") }
                var selectedFilterType by remember { mutableStateOf(Ux4gSearchFilterType.StartsWith) }

                val suggestions = remember {
                    listOf(
                        "Apple",
                        "Banana",
                        "Bandung",
                        "Blueberry",
                        "Cherry",
                        "Date",
                        "Elderberry",
                        "Fig",
                        "Grape"
                    )
                }

                // 1. Basic Search
                Text(text = "1. Basic Search", style = Ux4gTheme.typography.lM_strong)
                Ux4gSearchField(
                    value = basicSearchValue,
                    onValueChange = { basicSearchValue = it },
                    label = "Basic Search",
                    placeholder = "Search anything...",
                    showVoiceIcon = true,
                    showClearIcon = true,
                    onVoiceClick = {
                        scope.launch {
                            toastHostState.showToast(
                                category = Ux4gToastCategory.Info,
                                title = "Voice Search",
                                subtitle = "Listening for audio input..."
                            )
                        }
                    }
                )

                // 2. Search With Submit (Filled & Tonal)
                Text(text = "2. Search with Submit Button", style = Ux4gTheme.typography.lM_strong)
                Ux4gSearchField(
                    value = submitSearchValue,
                    onValueChange = { submitSearchValue = it },
                    variant = Ux4gSearchFieldVariant.SearchWithSubmit,
                    buttonStyle = Ux4gSearchFieldButtonStyle.Filled,
                    label = "Filled Submit Button",
                    placeholder = "Search and submit...",
                    onSubmitClick = { query ->
                        scope.launch {
                            toastHostState.showToast(
                                category = Ux4gToastCategory.Success,
                                title = "Search Submitted",
                                subtitle = "Query: '$query'"
                            )
                        }
                    }
                )

                Ux4gSearchField(
                    value = tonalSubmitSearchValue,
                    onValueChange = { tonalSubmitSearchValue = it },
                    variant = Ux4gSearchFieldVariant.SearchWithSubmit,
                    buttonStyle = Ux4gSearchFieldButtonStyle.Tonal,
                    label = "Tonal Submit Button",
                    placeholder = "Search with tonal button...",
                    onSubmitClick = { query ->
                        scope.launch {
                            toastHostState.showToast(
                                category = Ux4gToastCategory.Info,
                                title = "Tonal Search",
                                subtitle = "Query: '$query'"
                            )
                        }
                    }
                )

                // 3. Autocomplete with Live Filtering
                Text(text = "3. Autocomplete & Suggestions", style = Ux4gTheme.typography.lM_strong)
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    listOf(
                        Ux4gSearchFilterType.Contains,
                        Ux4gSearchFilterType.StartsWith,
                        Ux4gSearchFilterType.StartsWithPerTerm
                    ).forEach { filter ->
                        Ux4gButton(
                            onClick = { selectedFilterType = filter },
                            variant = if (selectedFilterType == filter) Ux4gButtonVariant.Primary else Ux4gButtonVariant.Outline,
                            size = Ux4gButtonSize.Small
                        ) {
                            Text(filter.name)
                        }
                    }
                }

                Ux4gSearchField(
                    value = autocompleteValue,
                    onValueChange = { autocompleteValue = it },
                    variant = Ux4gSearchFieldVariant.Autocomplete,
                    filterType = selectedFilterType,
                    label = "Autocomplete Fruit Search (${selectedFilterType.name})",
                    placeholder = "Type to search fruit...",
                    options = suggestions,
                    onOptionSelected = { selected ->
                        autocompleteValue = selected
                        scope.launch {
                            toastHostState.showToast(
                                category = Ux4gToastCategory.Success,
                                title = "Option Selected",
                                subtitle = "Selected: '$selected'"
                            )
                        }
                    }
                )

                // 4. Sizes
                Text(text = "4. Sizes", style = Ux4gTheme.typography.lM_strong)
                var sizeText by remember { mutableStateOf("Query") }
                Ux4gSearchField(
                    value = sizeText,
                    onValueChange = { sizeText = it },
                    label = "Size XL (56dp)",
                    size = Ux4gSearchFieldSize.XL,
                    variant = Ux4gSearchFieldVariant.SearchWithSubmit
                )
                Ux4gSearchField(
                    value = sizeText,
                    onValueChange = { sizeText = it },
                    label = "Size Large (48dp)",
                    size = Ux4gSearchFieldSize.Large,
                    variant = Ux4gSearchFieldVariant.SearchWithSubmit
                )
                Ux4gSearchField(
                    value = sizeText,
                    onValueChange = { sizeText = it },
                    label = "Size Medium (40dp)",
                    size = Ux4gSearchFieldSize.Medium,
                    variant = Ux4gSearchFieldVariant.SearchWithSubmit
                )
                Ux4gSearchField(
                    value = sizeText,
                    onValueChange = { sizeText = it },
                    label = "Size Small (32dp)",
                    size = Ux4gSearchFieldSize.Small,
                    variant = Ux4gSearchFieldVariant.SearchWithSubmit
                )

                // 5. Statuses & Captions
                Text(text = "5. Statuses & Captions", style = Ux4gTheme.typography.lM_strong)
                Ux4gSearchField(
                    value = "Successful search",
                    onValueChange = {},
                    status = Ux4gSearchFieldStatus.Success,
                    label = "Success Status",
                    caption = "Verified query found matching results."
                )
                Ux4gSearchField(
                    value = "Slow connection search",
                    onValueChange = {},
                    status = Ux4gSearchFieldStatus.Warning,
                    label = "Warning Status",
                    caption = "Network latency detected."
                )
                Ux4gSearchField(
                    value = "Invalid search",
                    onValueChange = {},
                    status = Ux4gSearchFieldStatus.Error,
                    label = "Error Status",
                    caption = "Invalid search terms provided."
                )

                // 6. Loading & Disabled States
                Text(text = "6. Loading & Disabled States", style = Ux4gTheme.typography.lM_strong)
                Ux4gSearchField(
                    value = "Loading results...",
                    onValueChange = {},
                    label = "Loading State",
                    isLoading = true
                )
                Ux4gSearchField(
                    value = "Disabled search",
                    onValueChange = {},
                    label = "Disabled State",
                    caption = "Search input is disabled.",
                    enabled = false
                )
            }
        }

        // ==========================================
        // Switch / Toggle Showcase
        // ==========================================
        Ux4gCard(modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "Switch / Toggle Component",
                    style = Ux4gTheme.typography.tS_strong,
                    color = Ux4gTheme.colorScheme.onSurface
                )
                Text(
                    text = "Custom switch controls with sizes, labels, icons, bothSides layout, and states.",
                    style = Ux4gTheme.typography.bS_default,
                    color = Ux4gColors.neutral600
                )

                // 1. Interactive Demo
                Text(text = "1. Interactive Demo", style = Ux4gTheme.typography.lM_strong)
                var demoSwitch by remember { mutableStateOf(true) }
                Ux4gSwitch(
                    checked = demoSwitch,
                    onCheckedChange = {
                        demoSwitch = it
                        scope.launch {
                            toastHostState.showToast(
                                category = if (it) Ux4gToastCategory.Success else Ux4gToastCategory.Info,
                                title = "Switch Toggled",
                                subtitle = if (it) "Enabled" else "Disabled"
                            )
                        }
                    },
                    label = "Push Notifications",
                    description = if (demoSwitch) "Notifications are active" else "Notifications are muted",
                    size = Ux4gSwitchSize.M,
                    labelPosition = Ux4gSwitchLabelPosition.Right
                )

                // 2. Sizes (S, M, L)
                Text(text = "2. Sizes (S, M, L)", style = Ux4gTheme.typography.lM_strong)
                var sizeS by remember { mutableStateOf(true) }
                var sizeM by remember { mutableStateOf(true) }
                var sizeL by remember { mutableStateOf(true) }

                Ux4gSwitch(
                    checked = sizeS,
                    onCheckedChange = { sizeS = it },
                    label = "Small (S) - 32x18",
                    description = "Compact size for dense interfaces",
                    size = Ux4gSwitchSize.S,
                    labelPosition = Ux4gSwitchLabelPosition.Right
                )

                Ux4gSwitch(
                    checked = sizeM,
                    onCheckedChange = { sizeM = it },
                    label = "Medium (M) - 40x22",
                    description = "Standard default size switch",
                    size = Ux4gSwitchSize.M,
                    labelPosition = Ux4gSwitchLabelPosition.Right
                )

                Ux4gSwitch(
                    checked = sizeL,
                    onCheckedChange = { sizeL = it },
                    label = "Large (L) - 48x28",
                    description = "Prominent touch-friendly switch",
                    size = Ux4gSwitchSize.L,
                    labelPosition = Ux4gSwitchLabelPosition.Right
                )

                // 3. Label Positions & BothSides
                Text(text = "3. Label Positions & BothSides Layout", style = Ux4gTheme.typography.lM_strong)
                var posLeft by remember { mutableStateOf(true) }
                var posBothSides by remember { mutableStateOf(true) }

                Ux4gSwitch(
                    checked = posLeft,
                    onCheckedChange = { posLeft = it },
                    label = "Label on Left Position",
                    description = "Switch control placed on the right",
                    labelPosition = Ux4gSwitchLabelPosition.Left
                )

                Ux4gSwitch(
                    checked = posBothSides,
                    onCheckedChange = { posBothSides = it },
                    label = "Monthly",
                    isRequired = true,
                    secondaryLabel = "Annual (Save 20%)",
                    isSecondaryRequired = false,
                    labelPosition = Ux4gSwitchLabelPosition.BothSides
                )

                // 4. Required Asterisk & Trailing Icons
                Text(text = "4. Required Asterisk & Trailing Icons", style = Ux4gTheme.typography.lM_strong)
                var reqWithIcon by remember { mutableStateOf(true) }
                Ux4gSwitch(
                    checked = reqWithIcon,
                    onCheckedChange = { reqWithIcon = it },
                    label = "Analytics Tracking",
                    isRequired = true,
                    trailingIcon = Ux4gIcons.info_circle,
                    description = "Help improve app experience with anonymous usage data",
                    labelPosition = Ux4gSwitchLabelPosition.Right
                )

                // 5. Disabled States
                Text(text = "5. Disabled States", style = Ux4gTheme.typography.lM_strong)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(24.dp)
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

        // ==========================================
        // Status Pipeline Showcase
        // ==========================================
        Ux4gCard(modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "Status Pipeline Component",
                    style = Ux4gTheme.typography.tS_strong,
                    color = Ux4gTheme.colorScheme.onSurface
                )
                Text(
                    text = "A status pipeline displaying process steps connected by lines in vertical and horizontal orientations.",
                    style = Ux4gTheme.typography.bS_default,
                    color = Ux4gColors.neutral600
                )

                // 1. Interactive Vertical Pipeline
                Text(text = "1. Vertical Pipeline (Interactive)", style = Ux4gTheme.typography.lM_strong)
                var currentPipelineStep by remember { mutableStateOf(1) }
                Ux4gStatusPipeline(
                    orientation = Ux4gPipelineOrientation.Vertical,
                    size = Ux4gPipelineSize.M,
                    currentStep = currentPipelineStep,
                    steps = listOf(
                        Ux4gPipelineStep(label = "Application Submitted", description = "5 Apr, 10:30 AM"),
                        Ux4gPipelineStep(label = "Document Verification", description = "In progress"),
                        Ux4gPipelineStep(label = "Field Inspection", description = "Est. 10 Apr"),
                        Ux4gPipelineStep(label = "Final Decision", description = "Est. 18 Apr"),
                        Ux4gPipelineStep(label = "Certificate Issued", description = "Est. 20 Apr")
                    )
                )

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Ux4gButton(
                        text = "Prev Step",
                        onClick = { if (currentPipelineStep > 0) currentPipelineStep = currentPipelineStep - 1 },
                        size = Ux4gButtonSize.Small,
                        variant = Ux4gButtonVariant.Secondary
                    )
                    Ux4gButton(
                        text = "Next Step",
                        onClick = { if (currentPipelineStep < 5) currentPipelineStep = currentPipelineStep + 1 },
                        size = Ux4gButtonSize.Small,
                        variant = Ux4gButtonVariant.Primary
                    )
                }

                // 2. Horizontal Pipeline
                Text(text = "2. Horizontal Pipeline", style = Ux4gTheme.typography.lM_strong)
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
                Text(text = "3. Error & Warning States", style = Ux4gTheme.typography.lM_strong)
                Ux4gStatusPipeline(
                    orientation = Ux4gPipelineOrientation.Vertical,
                    size = Ux4gPipelineSize.M,
                    currentStep = -1,
                    steps = listOf(
                        Ux4gPipelineStep(
                            label = "KYC Uploaded",
                            description = "Approved successfully",
                            state = Ux4gPipelineStepState.Completed
                        ),
                        Ux4gPipelineStep(
                            label = "Address Verification",
                            description = "Warning: Incomplete address proof",
                            state = Ux4gPipelineStepState.Warning
                        ),
                        Ux4gPipelineStep(
                            label = "Bank Details",
                            description = "Failed: Invalid IFSC Code",
                            state = Ux4gPipelineStepState.Error
                        ),
                        Ux4gPipelineStep(
                            label = "Final Activation",
                            description = "Waiting for document resolution",
                            state = Ux4gPipelineStepState.Upcoming
                        )
                    )
                )
            }
        }

        // ==========================================
        // Time Picker Showcase
        // ==========================================
        Ux4gCard(modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "Time Picker Component",
                    style = Ux4gTheme.typography.tS_strong,
                    color = Ux4gTheme.colorScheme.onSurface
                )
                Text(
                    text = "A custom Time Picker supporting 12-hour AM/PM format, smooth scroll wheels, minute intervals, and custom triggers.",
                    style = Ux4gTheme.typography.bS_default,
                    color = Ux4gColors.neutral600
                )

                val coroutineScope = rememberCoroutineScope()
                var appTime1 by remember { mutableStateOf<Ux4gTimeOfDay?>(Ux4gTimeOfDay(9, 30)) }
                var appTime2 by remember { mutableStateOf<Ux4gTimeOfDay?>(null) }

                // 1. Standard Time Picker
                Ux4gTimePicker(
                    selectedTime = appTime1,
                    onTimeSelected = {
                        appTime1 = it
                        coroutineScope.launch {
                            toastHostState.showToast(
                                category = Ux4gToastCategory.Success,
                                title = "Selected Time",
                                subtitle = it.format()
                            )
                        }
                    },
                    label = "Meeting Start Time",
                    description = "Interactive 12-hour AM/PM picker",
                    isRequired = true
                )

                // 2. Minute Interval (15-min) Time Picker
                Ux4gTimePicker(
                    selectedTime = appTime2,
                    onTimeSelected = {
                        appTime2 = it
                        coroutineScope.launch {
                            toastHostState.showToast(
                                category = Ux4gToastCategory.Info,
                                title = "Reminder Time Set",
                                subtitle = it.format()
                            )
                        }
                    },
                    label = "Slot Booking (15-min Intervals)",
                    minuteInterval = 15,
                    placeholder = "Select slot time"
                )

                // 3. Disabled State
                Ux4gTimePicker(
                    selectedTime = Ux4gTimeOfDay(14, 0),
                    onTimeSelected = {},
                    enabled = false,
                    label = "Disabled Time Field",
                    description = "Cannot be modified"
                )
            }
        }

        // ==========================================
        // Stepper & Compact Stepper Showcase
        // ==========================================
        Ux4gCard(modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                Text(
                    text = "Stepper & Compact Stepper Components",
                    style = Ux4gTheme.typography.tS_strong,
                    color = Ux4gTheme.colorScheme.onSurface
                )
                Text(
                    text = "Interactive progress steppers in Horizontal (Center & Bottom line placement), Vertical, and Compact/Capsule 5-layout variants.",
                    style = Ux4gTheme.typography.bS_default,
                    color = Ux4gColors.neutral600
                )

                var currentStep by remember { mutableStateOf(2) }
                val totalSteps = 4

                val stepItems = listOf(
                    Ux4gStepItem(
                        title = "Account Details",
                        description = "Create login credentials",
                        statusLabel = "Completed"
                    ),
                    Ux4gStepItem(
                        title = "Personal Info",
                        description = "Enter full name & email",
                        statusLabel = "In progress"
                    ),
                    Ux4gStepItem(
                        title = "Verification",
                        description = "Verify mobile OTP",
                        statusLabel = "Pending"
                    ),
                    Ux4gStepItem(
                        title = "Payment",
                        description = "Select plan & pay",
                        statusLabel = "Pending"
                    )
                )

                // Interactive Controls
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Current Step: $currentStep of $totalSteps",
                        style = Ux4gTheme.typography.lM_strong,
                        color = Ux4gColors.primary
                    )
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Ux4gButton(
                            text = "Previous",
                            variant = Ux4gButtonVariant.Outline,
                            size = Ux4gButtonSize.Small,
                            enabled = currentStep > 1,
                            onClick = { if (currentStep > 1) currentStep-- }
                        )
                        Ux4gButton(
                            text = "Next",
                            variant = Ux4gButtonVariant.Primary,
                            size = Ux4gButtonSize.Small,
                            enabled = currentStep < totalSteps,
                            onClick = { if (currentStep < totalSteps) currentStep++ }
                        )
                    }
                }

                // 1. Horizontal Stepper - Center Line Placement (Solid)
                Text(
                    text = "1. Horizontal Stepper (Center Line - Solid)",
                    style = Ux4gTheme.typography.lS_strong,
                    color = Ux4gTheme.colorScheme.onSurface
                )
                Ux4gStepper(
                    totalSteps = totalSteps,
                    currentStep = currentStep,
                    steps = stepItems,
                    lineStyle = StepperLineStyle.Solid,
                    linePlacement = StepperLinePlacement.Center
                )

                // 2. Horizontal Stepper - Center Line Placement (Dashed)
                Text(
                    text = "2. Horizontal Stepper (Center Line - Dashed)",
                    style = Ux4gTheme.typography.lS_strong,
                    color = Ux4gTheme.colorScheme.onSurface
                )
                Ux4gStepper(
                    totalSteps = totalSteps,
                    currentStep = currentStep,
                    steps = stepItems,
                    lineStyle = StepperLineStyle.Dashed,
                    linePlacement = StepperLinePlacement.Center
                )

                // 3. Horizontal Stepper - Bottom Line Placement with Active Step Background
                Text(
                    text = "3. Horizontal Stepper (Bottom Line & Active Highlight)",
                    style = Ux4gTheme.typography.lS_strong,
                    color = Ux4gTheme.colorScheme.onSurface
                )
                Ux4gStepper(
                    totalSteps = totalSteps,
                    currentStep = currentStep,
                    steps = stepItems,
                    linePlacement = StepperLinePlacement.Bottom,
                    activeStepBackground = true
                )

                // 4. Horizontal Stepper with Edge Label Alignment
                Text(
                    text = "4. Horizontal Stepper (Edge Label Alignment)",
                    style = Ux4gTheme.typography.lS_strong,
                    color = Ux4gTheme.colorScheme.onSurface
                )
                Ux4gStepper(
                    totalSteps = totalSteps,
                    currentStep = currentStep,
                    steps = stepItems,
                    edgeLabelAlignment = true
                )

                // 5. Vertical Stepper (With error & status handling)
                val verticalSteps = listOf(
                    Ux4gStepItem(
                        title = "Document Upload",
                        description = "Uploaded Passport & Address proof",
                        statusLabel = "Completed"
                    ),
                    Ux4gStepItem(
                        title = "Identity Verification",
                        description = "Facial match verification in progress",
                        statusLabel = "In progress"
                    ),
                    Ux4gStepItem(
                        title = "Bank Account Linking",
                        description = "Invalid IFSC Code provided",
                        statusLabel = "Error",
                        isError = true
                    ),
                    Ux4gStepItem(
                        title = "Final Account Activation",
                        description = "Receive welcome kit and credentials",
                        statusLabel = "Upcoming"
                    )
                )

                Text(
                    text = "5. Vertical Stepper (With States & Description Alignment)",
                    style = Ux4gTheme.typography.lS_strong,
                    color = Ux4gTheme.colorScheme.onSurface
                )
                Ux4gStepper(
                    totalSteps = verticalSteps.size,
                    currentStep = currentStep,
                    orientation = StepperOrientation.Vertical,
                    steps = verticalSteps,
                    alignIconWithDescription = true
                )

                // 6. Compact / Capsule Steppers (5 Layout Variants)
                Text(
                    text = "6. Compact / Capsule Stepper (5 Layout Variants)",
                    style = Ux4gTheme.typography.tS_strong,
                    color = Ux4gTheme.colorScheme.onSurface
                )

                val activeStepItem = stepItems.getOrNull(currentStep - 1)

                // Variant A: Linear Layout
                Text(
                    text = "Variant A: Linear Layout",
                    style = Ux4gTheme.typography.lS_strong,
                    color = Ux4gTheme.colorScheme.onSurface
                )
                Ux4gCompactStepper(
                    totalSteps = totalSteps,
                    currentStep = currentStep,
                    stepLabel = activeStepItem?.title ?: "Step $currentStep",
                    description = activeStepItem?.description,
                    onPrevious = { if (currentStep > 1) currentStep-- },
                    onNext = { if (currentStep < totalSteps) currentStep++ },
                    layout = Ux4gCompactStepperLayout.Linear
                )

                // Variant B: Right-Aligned Layout
                Text(
                    text = "Variant B: Right-Aligned Layout",
                    style = Ux4gTheme.typography.lS_strong,
                    color = Ux4gTheme.colorScheme.onSurface
                )
                Ux4gCompactStepper(
                    totalSteps = totalSteps,
                    currentStep = currentStep,
                    stepLabel = activeStepItem?.title ?: "Step $currentStep",
                    description = activeStepItem?.description,
                    onPrevious = { if (currentStep > 1) currentStep-- },
                    onNext = { if (currentStep < totalSteps) currentStep++ },
                    layout = Ux4gCompactStepperLayout.RightAligned
                )

                // Variant C: Centered Layout
                Text(
                    text = "Variant C: Centered Layout",
                    style = Ux4gTheme.typography.lS_strong,
                    color = Ux4gTheme.colorScheme.onSurface
                )
                Ux4gCompactStepper(
                    totalSteps = totalSteps,
                    currentStep = currentStep,
                    stepLabel = activeStepItem?.title ?: "Step $currentStep",
                    description = activeStepItem?.description,
                    onPrevious = { if (currentStep > 1) currentStep-- },
                    onNext = { if (currentStep < totalSteps) currentStep++ },
                    layout = Ux4gCompactStepperLayout.Centered
                )

                // Variant D: Centered-Between Layout
                Text(
                    text = "Variant D: Centered-Between Layout",
                    style = Ux4gTheme.typography.lS_strong,
                    color = Ux4gTheme.colorScheme.onSurface
                )
                Ux4gCompactStepper(
                    totalSteps = totalSteps,
                    currentStep = currentStep,
                    stepLabel = activeStepItem?.title ?: "Step $currentStep",
                    description = activeStepItem?.description,
                    onPrevious = { if (currentStep > 1) currentStep-- },
                    onNext = { if (currentStep < totalSteps) currentStep++ },
                    layout = Ux4gCompactStepperLayout.CenteredBetween
                )

                // Variant E: Split Layout
                Text(
                    text = "Variant E: Split Layout",
                    style = Ux4gTheme.typography.lS_strong,
                    color = Ux4gTheme.colorScheme.onSurface
                )
                Ux4gCompactStepper(
                    totalSteps = totalSteps,
                    currentStep = currentStep,
                    stepLabel = activeStepItem?.title ?: "Step $currentStep",
                    description = activeStepItem?.description,
                    onPrevious = { if (currentStep > 1) currentStep-- },
                    onNext = { if (currentStep < totalSteps) currentStep++ },
                    layout = Ux4gCompactStepperLayout.Split
                )
            }
        }

        // ==========================================
        // Timeslot & Slot Grid Showcase
        // ==========================================
        Ux4gCard(modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "Timeslot & Slot Grid Component",
                    style = Ux4gTheme.typography.tS_strong,
                    color = Ux4gTheme.colorScheme.onSurface
                )
                Text(
                    text = "Interactive calendar slot grid supporting public holidays, weekly-off rules, no-slot states, and modal time slot picker sheet in both Compact (Grid) and Expanded (List) view modes.",
                    style = Ux4gTheme.typography.bS_default,
                    color = Ux4gColors.neutral600
                )

                // 1. Variant A: Compact View Mode (2-Column Grid Sheet)
                Text(
                    text = "Variant A: Compact View Mode (2-Column Grid Sheet)",
                    style = Ux4gTheme.typography.lS_strong,
                    color = Ux4gTheme.colorScheme.onSurface
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
                                title = "Compact Slot Confirmed",
                                subtitle = "$date at ${slot.time} (${slot.slotCount} slots left)"
                            )
                        }
                    }
                )

                // 2. Variant B: Expanded View Mode (1-Column Vertical List Sheet)
                Text(
                    text = "Variant B: Expanded View Mode (1-Column Vertical List Sheet)",
                    style = Ux4gTheme.typography.lS_strong,
                    color = Ux4gTheme.colorScheme.onSurface
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
                                title = "Expanded Slot Confirmed",
                                subtitle = "$date at ${slot.time} (${slot.slotCount} slots left)"
                            )
                        }
                    }
                )
            }
        }

        // ==========================================
        // Result List & Result Row Showcase
        // ==========================================
        Ux4gCard(modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "Result List & Result Row Component",
                    style = Ux4gTheme.typography.tS_strong,
                    color = Ux4gTheme.colorScheme.onSurface
                )
                Text(
                    text = "Configurable expandable list items for search results, application tracking, status tags, pill metadata, custom actions, and multi-column detail grids.",
                    style = Ux4gTheme.typography.bS_default,
                    color = Ux4gColors.neutral600
                )

                // ─── 1. Visual Showcase (4 Core Variants) ───────────────────
                Text(
                    text = "1. Visual Showcase (Core Variants)",
                    style = defaultTypography.lM_strong,
                    color = Ux4gColors.neutral800
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

                // ─── 2. Certificate Download Story ──────────────────────────
                Text(
                    text = "2. Certificate Download (Verified Trailing Icon)",
                    style = defaultTypography.lM_strong,
                    color = Ux4gColors.neutral800
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

                // ─── 3. Service Apply Story ─────────────────────────────────
                Text(
                    text = "3. Service Apply (Custom Inline Fee & Duration)",
                    style = defaultTypography.lM_strong,
                    color = Ux4gColors.neutral800
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

                // ─── 4. SLA Escalated Story ─────────────────────────────────
                Text(
                    text = "4. SLA Escalated (Action Alert Icon & Overdue Pill)",
                    style = defaultTypography.lM_strong,
                    color = Ux4gColors.neutral800
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

                // ─── 5. List Style (Dividers) ───────────────────────────────
                Text(
                    text = "5. List Style (Continuous Items with Dividers)",
                    style = defaultTypography.lM_strong,
                    color = Ux4gColors.neutral800
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
                    style = defaultTypography.hL_default,
                    color = Ux4gColors.neutral800
                )

                // ─── 0. Fullscreen Biometric Verification Hero Launcher ──────────────
                Text(
                    text = "1. Fullscreen Biometric Verification (Full Width & Height Screen)",
                    style = defaultTypography.lM_strong,
                    color = Ux4gColors.neutral800
                )

                Ux4gCard(
                    modifier = Modifier.fillMaxWidth(),
                    cornerRadius = 16.dp,
                    borderColor = Color(0xFF6A4EFF).copy(alpha = 0.3f),
                    borderWidth = 1.5.dp
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
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

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = "Full-Screen Biometric Verification",
                            style = TextStyle(
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF0F172A)
                            ),
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "Opens the biometric verification flow in a dedicated full-screen view (100% width and height) with live CameraX viewfinder, Google ML Kit face alignment, eye-blink verification, and UIDAI verification.",
                            style = TextStyle(fontSize = 13.sp, color = Color(0xFF64748B)),
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(20.dp))

                        Button(
                            onClick = { onLaunchFullScreenBiometric() },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF6A4EFF),
                                contentColor = Color.White
                            )
                        ) {
                            Icon(
                                imageVector = androidx.compose.material.icons.Icons.Default.Fullscreen,
                                contentDescription = null,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Launch Full-Screen Biometric Flow",
                                style = TextStyle(fontWeight = FontWeight.SemiBold, fontSize = 15.sp)
                            )
                        }
                    }
                }

                // ─── 1. Interactive Flow with Custom Dummy Backend API (Google ML Kit) ─────
                Text(
                    text = "2. Inline Verification Flow Simulator with Custom Dummy API",
                    style = defaultTypography.lM_strong,
                    color = Ux4gColors.neutral800
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

                // ─── 2. Standalone Identity Consent Screen ───────────────────
                Text(
                    text = "2. Standalone Identity Consent Screen",
                    style = defaultTypography.lM_strong,
                    color = Ux4gColors.neutral800
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

                // ─── 3. Standalone Device Check Screen ───────────────────────
                Text(
                    text = "3. Standalone Device Readiness Check Screen",
                    style = defaultTypography.lM_strong,
                    color = Ux4gColors.neutral800
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

                // ─── 4. Custom Themed Biometric Flow ─────────────────────────
                Text(
                    text = "4. Custom Themed Verification Flow",
                    style = defaultTypography.lM_strong,
                    color = Ux4gColors.neutral800
                )

                var isCustomThemeLaunched by remember { mutableStateOf(false) }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(560.dp)
                        .clip(RoundedCornerShape(24.dp))
                        .border(1.5.dp, Color(0xFF0057FF).copy(alpha = 0.4f), RoundedCornerShape(24.dp))
                        .background(Color(0xFFF0F4FF)),
                    contentAlignment = Alignment.Center
                ) {
                    if (isCustomThemeLaunched) {
                        Ux4gBiometricVerificationFlow(
                            onVerify = { bytes ->
                                dummyUidaiVerificationApi(bytes, true)
                            },
                            theme = BiometricThemeData(
                                colors = BiometricColors(
                                    primary = Color(0xFF0057FF),
                                    success = Color(0xFF00C48C),
                                    ovalActive = Color(0xFF00C48C),
                                    ovalBlink = Color(0xFF0057FF)
                                ),
                                cardRadius = 20.dp,
                                buttonRadius = 8.dp
                            ),
                            onSuccess = { result ->
                                isCustomThemeLaunched = false
                                scope.launch {
                                    toastHostState.showToast(
                                        category = Ux4gToastCategory.Success,
                                        title = "Custom Theme Verified",
                                        subtitle = "Welcome, ${result.maskedName}"
                                    )
                                }
                            },
                            onFailure = { reason, _ ->
                                isCustomThemeLaunched = false
                            },
                            onDismiss = {
                                isCustomThemeLaunched = false
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
                                    .background(Color(0xFF0057FF).copy(alpha = 0.12f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = androidx.compose.material.icons.Icons.Outlined.Shield,
                                    contentDescription = null,
                                    tint = Color(0xFF0057FF),
                                    modifier = Modifier.size(36.dp)
                                )
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            Text(
                                text = "Custom Themed Flow",
                                style = TextStyle(
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF0F172A)
                                ),
                                textAlign = TextAlign.Center
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = "Uses customized blue/teal theme colors, custom 20dp card radius, and 8dp button radius.",
                                style = TextStyle(fontSize = 13.sp, color = Color(0xFF64748B)),
                                textAlign = TextAlign.Center
                            )

                            Spacer(modifier = Modifier.height(24.dp))

                            Button(
                                onClick = { isCustomThemeLaunched = true },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(48.dp),
                                shape = RoundedCornerShape(8.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFF0057FF),
                                    contentColor = Color.White
                                )
                            ) {
                                Text(
                                    text = "Launch Custom Themed Flow",
                                    style = TextStyle(fontWeight = FontWeight.SemiBold, fontSize = 15.sp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}




@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LiveBottomNavScreen(
    onDismiss: () -> Unit,
    toastHostState: Ux4gToastHostState
) {
    val scope = rememberCoroutineScope()
    var selectedVariant by remember { mutableStateOf(0) }
    var selectedTab by remember { mutableStateOf(0) }

    val standardItems = listOf(
        Ux4gBottomNavItem(key = "home", label = "Home", icon = Ux4gIcons.Home),
        Ux4gBottomNavItem(key = "services", label = "Services", icon = Ux4gIcons.Menu),
        Ux4gBottomNavItem(key = "status", label = "Status", icon = Ux4gIcons.Check),
        Ux4gBottomNavItem(key = "alerts", label = "Alerts", icon = Ux4gIcons.info_circle, badge = 3),
        Ux4gBottomNavItem(key = "profile", label = "Profile", icon = Ux4gIcons.Person)
    )

    val centreActionItems = listOf(
        Ux4gBottomNavItem(key = "home", label = "Home", icon = Ux4gIcons.Home),
        Ux4gBottomNavItem(key = "services", label = "Services", icon = Ux4gIcons.Menu),
        Ux4gBottomNavItem(key = "status", label = "Status", icon = Ux4gIcons.Check),
        Ux4gBottomNavItem(key = "alerts", label = "Alerts", icon = Ux4gIcons.info_circle)
    )

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = Ux4gColors.neutral50,
        topBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Ux4gTheme.colorScheme.surface)
                    .statusBarsPadding()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = onDismiss) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = Ux4gColors.neutral900
                        )
                    }
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "UX4G Citizen Portal",
                            style = Ux4gTheme.typography.tS_strong,
                            color = Ux4gColors.neutral900
                        )
                        Text(
                            text = "Bottom Navigation Anchored at Screen Bottom",
                            style = Ux4gTheme.typography.bXS_default,
                            color = Ux4gColors.neutral600
                        )
                    }
                }

                // Interactive variant selector
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState())
                        .padding(horizontal = 12.dp, vertical = 6.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    val variantTitles = listOf(
                        "1. Pill (Labelled)",
                        "2. Bar (Top-Rounded)",
                        "3. Plain (Icon Only)",
                        "4. Center Action (FAB)",
                        "5. Floating Pill"
                    )
                    variantTitles.forEachIndexed { idx, title ->
                        val isSelected = selectedVariant == idx
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(20.dp))
                                .background(
                                    if (isSelected) Ux4gTheme.colorScheme.primary else Ux4gColors.neutral200
                                )
                                .clickable {
                                    selectedVariant = idx
                                    selectedTab = 0
                                }
                                .padding(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = title,
                                style = TextStyle(
                                    fontSize = 12.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                    color = if (isSelected) Color.White else Ux4gColors.neutral800
                                )
                            )
                        }
                    }
                }
                Ux4gDivider()
            }
        },
        bottomBar = {
            when (selectedVariant) {
                0 -> {
                    Ux4gBottomNavigationBar(
                        items = standardItems,
                        selectedIndex = selectedTab,
                        onTabChange = { idx, _ -> selectedTab = idx },
                        indicatorVariant = Ux4gBottomNavIndicatorVariant.Pill,
                        variant = Ux4gBottomNavVariant.Labelled,
                        useSafeArea = true
                    )
                }
                1 -> {
                    Ux4gBottomNavigationBar(
                        items = standardItems,
                        selectedIndex = selectedTab,
                        onTabChange = { idx, _ -> selectedTab = idx },
                        indicatorVariant = Ux4gBottomNavIndicatorVariant.Bar,
                        variant = Ux4gBottomNavVariant.TopRounded,
                        useSafeArea = true
                    )
                }
                2 -> {
                    Ux4gBottomNavigationBar(
                        items = standardItems,
                        selectedIndex = selectedTab,
                        onTabChange = { idx, _ -> selectedTab = idx },
                        indicatorVariant = Ux4gBottomNavIndicatorVariant.Plain,
                        variant = Ux4gBottomNavVariant.IconOnly,
                        showLabels = false,
                        useSafeArea = true
                    )
                }
                3 -> {
                    Ux4gBottomNavigationBar(
                        items = centreActionItems,
                        selectedIndex = selectedTab,
                        onTabChange = { idx, _ -> selectedTab = idx },
                        variant = Ux4gBottomNavVariant.CentreAction,
                        indicatorVariant = Ux4gBottomNavIndicatorVariant.Pill,
                        centerAction = Ux4gBottomNavCenterAction(
                            icon = Ux4gIcons.Add,
                            onPressed = {
                                scope.launch {
                                    toastHostState.showToast(
                                        category = Ux4gToastCategory.Success,
                                        title = "Quick Action Triggered",
                                        subtitle = "New citizen service request modal initiated!"
                                    )
                                }
                            }
                        ),
                        useSafeArea = true
                    )
                }
                else -> {
                    Ux4gFloatingBottomNavBar(
                        items = standardItems,
                        selectedIndex = selectedTab,
                        onTabChange = { idx, _ -> selectedTab = idx },
                        indicatorVariant = Ux4gBottomNavIndicatorVariant.Pill,
                        useSafeArea = true
                    )
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            when (selectedTab) {
                0 -> {
                    Ux4gCard(modifier = Modifier.fillMaxWidth()) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text("Namaste, Aarav Sharma", style = Ux4gTheme.typography.tS_strong)
                            Text(
                                "Digital India Citizen Portal — Aadhaar Verified",
                                style = Ux4gTheme.typography.bXS_default,
                                color = Ux4gColors.neutral600
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                "This is a full live page where the Bottom Navigation Bar is practically anchored at the screen bottom with system safe insets. Tap any tab below or switch variants above to test smooth transitions without any black flash!",
                                style = Ux4gTheme.typography.bS_default,
                                color = Ux4gColors.neutral800
                            )
                        }
                    }

                    Text("Featured Public Services", style = Ux4gTheme.typography.lM_strong)
                    listOf(
                        "Income & Domicile Certificate",
                        "Electricity & Water Bill Payment",
                        "Public Grievance Redressal (CPGRAMS)"
                    ).forEach { title ->
                        Ux4gCard(modifier = Modifier.fillMaxWidth()) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(14.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(36.dp)
                                        .clip(CircleShape)
                                        .background(Ux4gTheme.colorScheme.primary.copy(alpha = 0.12f)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Ux4gIconComponent(icon = Ux4gIcons.Check, contentDescription = null, tint = Ux4gTheme.colorScheme.primary)
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                                Text(text = title, style = Ux4gTheme.typography.lS_strong, modifier = Modifier.weight(1f))
                                Ux4gIconComponent(icon = Ux4gIcons.arrowRight, contentDescription = null, tint = Ux4gColors.neutral400)
                            }
                        }
                    }
                }
                1 -> {
                    Ux4gCard(modifier = Modifier.fillMaxWidth()) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Text("Services Directory", style = Ux4gTheme.typography.tS_strong)
                            Text("Explore state and central citizen services.", style = Ux4gTheme.typography.bS_default, color = Ux4gColors.neutral600)
                            listOf(
                                "Identity & Verification (DigiLocker)",
                                "Revenue & Land Registration",
                                "Education Scholarships",
                                "Healthcare & Medical Insurance"
                            ).forEach { s ->
                                Row(
                                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Ux4gIconComponent(icon = Ux4gIcons.Check, contentDescription = null, tint = Ux4gColors.green600)
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Text(s, style = Ux4gTheme.typography.bS_default)
                                }
                                Ux4gDivider()
                            }
                        }
                    }
                }
                2 -> {
                    Ux4gCard(modifier = Modifier.fillMaxWidth()) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Text("Track Status", style = Ux4gTheme.typography.tS_strong)
                            Text("Real-time updates on active applications.", style = Ux4gTheme.typography.bS_default, color = Ux4gColors.neutral600)
                            Ux4gStatusBanner(
                                variant = Ux4gBannerVariant.SuccessLight,
                                title = "Application Approved",
                                subtitle = "Application #GOV-9842 verified and approved."
                            )
                        }
                    }
                }
                3 -> {
                    Ux4gCard(modifier = Modifier.fillMaxWidth()) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Text("Alerts & Notifications", style = Ux4gTheme.typography.tS_strong)
                            listOf(
                                "DigiLocker document fetched successfully (10m ago)",
                                "Aadhaar authentication request approved (1h ago)",
                                "Portal maintenance scheduled for Sunday 2 AM"
                            ).forEach { msg ->
                                Row(
                                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Ux4gIconComponent(icon = Ux4gIcons.info_circle, contentDescription = null, tint = Ux4gColors.red600)
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Text(msg, style = Ux4gTheme.typography.bS_default)
                                }
                                Ux4gDivider()
                            }
                        }
                    }
                }
                else -> {
                    Ux4gCard(modifier = Modifier.fillMaxWidth()) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Text("Citizen Profile", style = Ux4gTheme.typography.tS_strong)
                            Text("Aarav Sharma", style = Ux4gTheme.typography.lS_strong)
                            Text("Phone: +91 98765-43210", style = Ux4gTheme.typography.bS_default, color = Ux4gColors.neutral600)
                            Spacer(modifier = Modifier.height(12.dp))
                            Button(
                                onClick = onDismiss,
                                modifier = Modifier.fillMaxWidth(),
                                colors = ButtonDefaults.buttonColors(containerColor = Ux4gTheme.colorScheme.primary)
                            ) {
                                Text("Back to Component Showcase")
                            }
                        }
                    }
                }
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
