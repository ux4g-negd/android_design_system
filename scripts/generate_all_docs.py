import os

docs_dir = r"c:\Users\Monu\Desktop\prac\openforge\ux4g-android-docs\content\docs\components"

# We define rich, exact component documentation data for each component based on android_design_system Kotlin source code.
REQ = '<span className="text-red-500 font-bold">*</span>'

def create_table(headers, rows):
    res = "| " + " | ".join(headers) + " |\n"
    res += "| " + " | ".join([":---" for _ in headers]) + " |\n"
    for r in rows:
        res += "| " + " | ".join(r) + " |\n"
    return res

components = {}

# 1. app-header
components['app-header'] = {
    'title': 'App Header',
    'desc': 'Configurable top navigation app bar supporting official emblems, logos, title, trailing actions, and profile avatars',
    'component': 'Ux4gAppHeader',
    'pkg': 'com.ux4g.designsystemv1.components.Ux4gAppHeader',
    'preview_code': '''Ux4gAppHeader(
    title = "National Scholarship Portal",
    variant = Ux4gAppHeaderVariant.Light,
    showBackButton = false,
    actions = listOf(
        Ux4gAppHeaderAction(
            icon = Ux4gIcons.Settings,
            onClick = { /* open settings */ }
        )
    ),
    showAvatar = true,
    avatarInitials = "AS"
)''',
    'usage_code': '''import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.ux4g.designsystemv1.components.Ux4gAppHeader
import com.ux4g.designsystemv1.components.Ux4gAppHeaderVariant
import com.ux4g.designsystemv1.components.Ux4gAppHeaderAction
import com.ux4g.designsystemv1.foundation.Ux4gIcons

@Composable
fun AppHeaderExample() {
    Ux4gAppHeader(
        title = "Dashboard",
        variant = Ux4gAppHeaderVariant.Filled,
        showBackButton = true,
        onBackPressed = { /* Navigate back */ },
        actions = listOf(
            Ux4gAppHeaderAction(icon = Ux4gIcons.Settings, onClick = { /* Settings */ })
        )
    )
}''',
    'tables': [
        ('`Ux4gAppHeader` Props', ['Prop', 'Type', 'Description', 'Default'], [
            [f'`title` {REQ}', '`String`', 'Primary header title text.', '`""`'],
            ['`modifier`', '`Modifier`', 'Compose layout modifier applied to header container.', '`Modifier`'],
            ['`variant`', '`Ux4gAppHeaderVariant`', 'Visual style variant: `Light`, `Filled`, or `Outlined`.', '`Outlined`'],
            ['`showBackButton`', '`Boolean`', 'Whether to show back button as leading widget (overrides leading items).', '`false`'],
            ['`onBackPressed`', '`(() -> Unit)?`', 'Callback triggered when back button is tapped.', '`null`'],
            ['`leadingWidgets`', '`List<@Composable () -> Unit>?`', 'List of custom leading widgets (emblems, logos, dividers).', '`null`'],
            ['`leadingContent`', '`(@Composable () -> Unit)?`', 'Single custom leading composable slot.', '`null`'],
            ['`actions`', '`List<Ux4gAppHeaderAction>`', 'List of trailing action icon buttons or custom action widgets.', '`emptyList()`'],
            ['`avatar`', '`(@Composable () -> Unit)?`', 'Custom avatar composable slot at trailing end.', '`null`'],
            ['`avatarSize`', '`Ux4gAvatarSize`', 'Avatar dimension size when `showAvatar = true`.', '`Ux4gAvatarSize.Medium`'],
            ['`showAvatar`', '`Boolean`', 'Whether to render default `Ux4gAvatar` at trailing end.', '`false`'],
            ['`avatarImageUrl`', '`String?`', 'Image URL for default profile avatar.', '`null`'],
            ['`avatarInitials`', '`String?`', 'Initials fallback text when avatar image is absent.', '`null`'],
            ['`onAvatarClick`', '`(() -> Unit)?`', 'Click callback for trailing avatar.', '`null`'],
            ['`titleWidget`', '`(@Composable () -> Unit)?`', 'Custom composable slot for title (overrides `title` string).', '`null`'],
            ['`useSafeArea`', '`Boolean`', 'Applies status bar inset padding automatically.', '`true`'],
            ['`height`', '`Dp`', 'Height dimension of header bar.', '`56.dp`'],
            ['`backgroundColor`', '`Color?`', 'Custom background color override.', '`null`'],
            ['`contentColor`', '`Color?`', 'Custom text and icon color override.', '`null`'],
            ['`borderColor`', '`Color?`', 'Border color for Outlined variant.', '`null`'],
        ]),
        ('`Ux4gAppHeaderAction` Model', ['Prop', 'Type', 'Description', 'Default'], [
            ['`icon`', '`Ux4gIcon?`', 'Vector icon resource for action button.', '`Ux4gIcons.Settings`'],
            ['`onClick`', '`(() -> Unit)?`', 'Click handler callback when action is pressed.', '`null`'],
            ['`tooltip`', '`String?`', 'Accessibility label / tooltip description.', '`null`'],
            ['`customWidget`', '`(@Composable () -> Unit)?`', 'Custom composable slot replacing standard icon button.', '`null`']
        ]),
        ('`Ux4gAppHeaderVariant` Enum', ['Value', 'Description'], [
            ['`Light`', 'Surface background with dark text, emblem, and action icons.'],
            ['`Filled`', 'Primary purple solid background with high-contrast white text and icons.'],
            ['`Outlined`', 'Surface background with subtle bottom border line.']
        ])
    ]
}

# 2. avatar
components['avatar'] = {
    'title': 'Avatar',
    'desc': 'User profile picture, initials fallback, active status indicator, and grouped stacked avatars',
    'component': 'Ux4gAvatar',
    'pkg': 'com.ux4g.designsystemv1.components.avatar_component.Ux4gAvatar',
    'preview_code': '''// Profile Avatar with Initials
Ux4gAvatar(
    name = "Aarav Sharma",
    size = Ux4gAvatarSize.Large,
    status = Ux4gAvatarStatus.Online
)

// Avatar with Image URL
Ux4gAvatar(
    imageUrl = "https://example.com/avatar.jpg",
    name = "Priya Patel",
    size = Ux4gAvatarSize.Medium
)''',
    'usage_code': '''import androidx.compose.runtime.Composable
import com.ux4g.designsystemv1.components.avatar_component.Ux4gAvatar
import com.ux4g.designsystemv1.components.avatar_component.Ux4gAvatarSize
import com.ux4g.designsystemv1.components.avatar_component.Ux4gAvatarStatus
import com.ux4g.designsystemv1.components.avatar_component.Ux4gAvatarGroup

@Composable
fun AvatarExample() {
    Ux4gAvatar(
        name = "Rajesh Kumar",
        size = Ux4gAvatarSize.Large,
        status = Ux4gAvatarStatus.Online,
        onClick = { /* Open citizen profile */ }
    )
}''',
    'tables': [
        ('`Ux4gAvatar` Props', ['Prop', 'Type', 'Description', 'Default'], [
            [f'`name` {REQ}', '`String`', 'Citizen/User name used for computing initials & accessibility content description.', '`—`'],
            ['`imageUrl`', '`String?`', 'Optional remote image URL.', '`null`'],
            ['`size`', '`Ux4gAvatarSize`', 'Avatar dimension: `ExtraSmall`, `Small`, `Medium`, `Large`, `ExtraLarge`.', '`Medium`'],
            ['`status`', '`Ux4gAvatarStatus?`', 'Presence status dot badge: `Online`, `Offline`, `Away`, `Busy`.', '`null`'],
            ['`shape`', '`Shape`', 'Shape applied to the avatar boundary (Circle or Rounded Corner).', '`CircleShape`'],
            ['`onClick`', '`(() -> Unit)?`', 'Optional click listener callback when avatar is tapped.', '`null`'],
            ['`modifier`', '`Modifier`', 'Compose layout modifier.', '`Modifier`'],
        ]),
        ('`Ux4gAvatarGroup` Props', ['Prop', 'Type', 'Description', 'Default'], [
            [f'`avatars` {REQ}', '`List<Ux4gAvatarData>`', 'List of user avatar models to render in an overlapping horizontal stack.', '`—`'],
            ['`maxVisible`', '`Int`', 'Maximum number of avatar circles to display before showing `+N` count pill.', '`4`'],
            ['`size`', '`Ux4gAvatarSize`', 'Uniform dimension size for all avatars in group.', '`Ux4gAvatarSize.Medium`'],
            ['`modifier`', '`Modifier`', 'Compose layout modifier.', '`Modifier`']
        ]),
        ('`Ux4gAvatarSize` Enum', ['Value', 'Dimension', 'Text Style'], [
            ['`ExtraSmall`', '`24.dp`', '`10.sp` bold initials'],
            ['`Small`', '`32.dp`', '`12.sp` bold initials'],
            ['`Medium`', '`40.dp`', '`14.sp` bold initials'],
            ['`Large`', '`48.dp`', '`16.sp` bold initials'],
            ['`ExtraLarge`', '`64.dp`', '`20.sp` bold initials']
        ]),
        ('`Ux4gAvatarStatus` Enum', ['Value', 'Color', 'Description'], [
            ['`Online`', 'Green (`#22C55E`)', 'User currently active / signed in'],
            ['`Offline`', 'Neutral Gray (`#9CA3AF`)', 'User offline'],
            ['`Away`', 'Amber (`#F59E0B`)', 'User temporarily away'],
            ['`Busy`', 'Red (`#EF4444`)', 'User busy / in verification session']
        ])
    ]
}

# 3. badge
components['badge'] = {
    'title': 'Badge',
    'desc': 'Status labels, notification count pills, verification icons, and widget-anchored badges',
    'component': 'Ux4gBadge',
    'pkg': 'com.ux4g.designsystemv1.components.Ux4gBadge',
    'preview_code': '''// Count Badge
Ux4gBadge(type = Ux4gBadgeType.Count(99, Ux4gBadgeLimit.DoubleDigit))

// Label Badge
Ux4gBadge(type = Ux4gBadgeType.Label("NEW"), containerColor = Ux4gColors.primary600)

// Dot Badge
Ux4gBadge(type = Ux4gBadgeType.Dot, containerColor = Ux4gColors.green600)

// Ready to use Verification Badge
Ux4gBadge(type = Ux4gBadgeType.ReadyToUse(Ux4gBadgeResource.Verification))''',
    'usage_code': '''import androidx.compose.runtime.Composable
import com.ux4g.designsystemv1.components.Ux4gBadge
import com.ux4g.designsystemv1.components.Ux4gBadgeType
import com.ux4g.designsystemv1.components.Ux4gBadgeLimit
import com.ux4g.designsystemv1.foundation.Ux4gColors

@Composable
fun BadgeExample() {
    Ux4gBadge(
        type = Ux4gBadgeType.Label("VERIFIED"),
        containerColor = Ux4gColors.green600
    )
}''',
    'tables': [
        ('`Ux4gBadge` Props', ['Prop', 'Type', 'Description', 'Default'], [
            [f'`type` {REQ}', '`Ux4gBadgeType`', 'Type configuration: `Dot`, `Count`, `Label`, `Icon`, or `ReadyToUse`.', '`—`'],
            ['`modifier`', '`Modifier`', 'Compose layout modifier.', '`Modifier`'],
            ['`containerColor`', '`Color`', 'Background fill color of the badge container.', '`Ux4gColors.primary`'],
            ['`contentColor`', '`Color`', 'Color for text or icon inside the badge.', '`Ux4gColors.white`'],
            ['`showBorder`', '`Boolean`', 'Whether to display outer boundary stroke.', '`false`'],
            ['`borderColor`', '`Color`', 'Stroke outline color when `showBorder = true`.', '`Ux4gColors.neutral300`'],
            ['`alignment`', '`Ux4gBadgeAlignment`', 'Corner anchor placement when wrapping child content (`TopEnd`, `TopStart`, `BottomEnd`, `BottomStart`).', '`Ux4gBadgeAlignment.TopEnd`'],
            ['`content`', '`(@Composable () -> Unit)?`', 'Optional child composable to anchor the badge overlay upon.', '`null`']
        ]),
        ('`Ux4gBadgeType` Sealed Class Variants', ['Variant', 'Parameters', 'Description'], [
            ['`Dot`', '`size: Dp = 8.dp`', 'Small circular presence or unread indicator dot.'],
            ['`Count` / `Text`', '`count: Int, limit: Ux4gBadgeLimit`', 'Numeric count badge displaying formatted values (e.g. `9+`, `99+`).'],
            ['`Label`', '`text: String`', 'Capsule pill displaying short capitalized label text.'],
            ['`Icon`', '`iconRes: Int` or `icon: Ux4gIcon`', 'Small icon badge inside rounded container.'],
            ['`ReadyToUse`', '`resource: Ux4gBadgeResource`', 'Pre-styled official badges (`Verification`, `Star`, `Shield`).']
        ])
    ]
}

# 4. biometric
components['biometric'] = {
    'title': 'Biometric Verification',
    'desc': 'Aadhaar e-KYC face liveness, iris scan, and fingerprint verification flow with camera overlay and device checks',
    'component': 'Ux4gBiometricVerificationFlow',
    'pkg': 'com.ux4g.designsystemv1.components.biometric.Ux4gBiometricVerificationFlow',
    'preview_code': '''Ux4gBiometricVerificationFlow(
    onVerify = { capturedBytes ->
        // Simulate Aadhaar biometric match API
        BiometricVerificationResult(
            success = true,
            userId = "GOV-UID-9842",
            maskedName = "Aarav Sharma"
        )
    },
    enableBlinkCheck = true,
    enableLightingCheck = true,
    enableLiveness = true,
    maxAttempts = 3,
    onSuccess = { result -> /* Handle auth success */ },
    onFailure = { reason, message -> /* Handle auth error */ },
    onDismiss = { /* Dismiss modal */ }
)''',
    'usage_code': '''import androidx.compose.runtime.Composable
import com.ux4g.designsystemv1.components.biometric.Ux4gBiometricVerificationFlow

@Composable
fun BiometricAuthScreen() {
    Ux4gBiometricVerificationFlow(
        onVerify = { bytes ->
            // Authenticate with RD service backend
            verifyWithUidai(bytes)
        },
        onSuccess = { result ->
            navigateToDashboard(result.userId)
        },
        onFailure = { reason, msg ->
            showErrorToast(msg)
        },
        onDismiss = {
            finishActivity()
        }
    )
}''',
    'tables': [
        ('`Ux4gBiometricVerificationFlow` Props', ['Prop', 'Type', 'Description', 'Default'], [
            [f'`onVerify` {REQ}', '`suspend (ByteArray) -> BiometricVerificationResult`', 'Async API callback invoked with captured biometric payload.', '`—`'],
            [f'`onSuccess` {REQ}', '`(BiometricVerificationResult) -> Unit`', 'Callback executed upon successful biometric identity confirmation.', '`—`'],
            [f'`onFailure` {REQ}', '`(BiometricFailureReason, String?) -> Unit`', 'Callback executed when verification fails or max attempts are exceeded.', '`—`'],
            ['`onDismiss`', '`(() -> Unit)?`', 'Callback when citizen closes or cancels the verification flow.', '`null`'],
            ['`onAlternateVerify`', '`(() -> Unit)?`', 'Callback when citizen opts for fallback verification (e.g. OTP).', '`null`'],
            ['`enableBlinkCheck`', '`Boolean`', 'Enforces eye-blink detection for passive liveness verification.', '`true`'],
            ['`enableLightingCheck`', '`Boolean`', 'Validates ambient illumination threshold before capture.', '`true`'],
            ['`enableLiveness`', '`Boolean`', 'Enables full facial liveness anti-spoof analysis.', '`true`'],
            ['`maxAttempts`', '`Int`', 'Maximum permitted capture attempts before lockout.', '`3`'],
            ['`modifier`', '`Modifier`', 'Compose layout modifier.', '`Modifier`']
        ])
    ]
}

# 5. bottom-sheet
components['bottom-sheet'] = {
    'title': 'Bottom Sheet',
    'desc': 'Slide-up modal container for mobile filter sheets, secondary actions, and contextual forms',
    'component': 'Ux4gBottomSheet',
    'pkg': 'com.ux4g.designsystemv1.components.Ux4gBottomSheet',
    'preview_code': '''Ux4gBottomSheet(
    show = showSheet,
    title = "Filter Citizen Services",
    onDismiss = { showSheet = false }
) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text("Select Category", style = Ux4gTheme.typography.tS_strong)
        Spacer(modifier = Modifier.height(8.dp))
        // Sheet content controls
    }
}''',
    'usage_code': '''import androidx.compose.runtime.*
import androidx.compose.material3.Text
import com.ux4g.designsystemv1.components.Ux4gBottomSheet
import com.ux4g.designsystemv1.theme.Ux4gTheme

@Composable
fun FilterSheetExample() {
    var isSheetOpen by remember { mutableStateOf(false) }

    Ux4gBottomSheet(
        show = isSheetOpen,
        title = "Filter Schemes",
        onDismiss = { isSheetOpen = false }
    ) {
        Text("Choose scheme eligibility criteria...")
    }
}''',
    'tables': [
        ('`Ux4gBottomSheet` Props', ['Prop', 'Type', 'Description', 'Default'], [
            [f'`show` {REQ}', '`Boolean`', 'Controls whether the bottom sheet modal is visible.', '`—`'],
            [f'`onDismiss` {REQ}', '`() -> Unit`', 'Callback invoked when backdrop is tapped or sheet is swiped down.', '`—`'],
            [f'`content` {REQ}', '`@Composable () -> Unit`', 'Main composable body content inside the sheet.', '`—`'],
            ['`title`', '`String?`', 'Optional header title text displayed at top of sheet.', '`null`'],
            ['`showCloseButton`', '`Boolean`', 'Whether to render top-right close icon button.', '`true`'],
            ['`showDragHandle`', '`Boolean`', 'Whether to show the top center drag pill handle.', '`true`'],
            ['`modifier`', '`Modifier`', 'Compose layout modifier.', '`Modifier`'],
            ['`containerColor`', '`Color`', 'Surface background color of sheet container.', '`Ux4gTheme.colorScheme.surface`'],
            ['`scrimColor`', '`Color`', 'Backdrop scrim dimmer color.', '`Color.Black.copy(alpha = 0.5f)`']
        ])
    ]
}

# 6. button
components['button'] = {
    'title': 'Button',
    'desc': 'Interactive standard Button and IconButton with solid, secondary, outline, destructive variants and loading states',
    'component': 'Ux4gButton',
    'pkg': 'com.ux4g.designsystemv1.components.buttons.Ux4gButton',
    'preview_code': '''// Primary Action Button
Ux4gButton(
    text = "Submit Application",
    onClick = { /* submit */ },
    variant = Ux4gButtonVariant.Primary
)

// Destructive Outline Button
Ux4gButton(
    text = "Delete Draft",
    onClick = { /* delete */ },
    variant = Ux4gButtonVariant.Destructive
)''',
    'usage_code': '''import androidx.compose.runtime.Composable
import com.ux4g.designsystemv1.components.buttons.Ux4gButton
import com.ux4g.designsystemv1.components.buttons.Ux4gButtonVariant
import com.ux4g.designsystemv1.components.buttons.Ux4gButtonSize

@Composable
fun ButtonDemo() {
    Ux4gButton(
        text = "Proceed to Aadhaar Auth",
        onClick = { /* handle action */ },
        variant = Ux4gButtonVariant.Primary,
        size = Ux4gButtonSize.Large
    )
}''',
    'tables': [
        ('`Ux4gButton` Props', ['Prop', 'Type', 'Description', 'Default'], [
            [f'`text` {REQ}', '`String`', 'Label text displayed inside the button.', '`—`'],
            [f'`onClick` {REQ}', '`() -> Unit`', 'Callback invoked when button is clicked.', '`—`'],
            ['`modifier`', '`Modifier`', 'Compose layout modifier.', '`Modifier`'],
            ['`variant`', '`Ux4gButtonVariant`', 'Visual style: `Primary`, `Secondary`, `Outline`, `Destructive`, `Ghost`.', '`Ux4gButtonVariant.Primary`'],
            ['`size`', '`Ux4gButtonSize`', 'Button dimensions: `Small`, `Medium`, `Large`.', '`Ux4gButtonSize.Medium`'],
            ['`enabled`', '`Boolean`', 'Whether button responds to user touch interactions.', '`true`'],
            ['`isLoading`', '`Boolean`', 'Replaces label with progress spinner and disables interactions.', '`false`'],
            ['`leadingIcon`', '`(@Composable () -> Unit)?`', 'Optional composable icon before the text.', '`null`'],
            ['`trailingIcon`', '`(@Composable () -> Unit)?`', 'Optional composable icon after the text.', '`null`']
        ]),
        ('`Ux4gIconButton` Props', ['Prop', 'Type', 'Description', 'Default'], [
            [f'`icon` {REQ}', '`Ux4gIcon`', 'Vector icon displayed inside circular button.', '`—`'],
            [f'`onClick` {REQ}', '`() -> Unit`', 'Click callback handler.', '`—`'],
            ['`modifier`', '`Modifier`', 'Compose layout modifier.', '`Modifier`'],
            ['`size`', '`Ux4gIconButtonSize`', 'Button size: `Small`, `Medium`, `Large`.', '`Ux4gIconButtonSize.Medium`'],
            ['`style`', '`Ux4gIconButtonStyle`', 'Visual style: `Filled`, `Outlined`, `Ghost`.', '`Ux4gIconButtonStyle.Filled`'],
            ['`enabled`', '`Boolean`', 'Interactive enabled state.', '`true`'],
            ['`contentDescription`', '`String?`', 'Accessibility text description.', '`null`']
        ]),
        ('`Ux4gButtonVariant` Enum', ['Variant', 'Tone & Border', 'Recommended Context'], [
            ['`Primary`', 'Solid Brand Purple (`#4A2BC2`), White text', 'Primary high-priority action per screen (Submit, Proceed, Pay)'],
            ['`Secondary`', 'Amber / Neutral fill, Dark text', 'Alternative secondary actions (Save Draft, View Details)'],
            ['`Outline`', 'Transparent fill with subtle border', 'Low-emphasis complementary actions (Cancel, Back)'],
            ['`Destructive`', 'Red error accent fill', 'Irreversible actions (Delete, Reject, Discard)'],
            ['`Ghost`', 'Flat transparent background', 'Inline text buttons and table action links']
        ])
    ]
}

# 7. card
components['card'] = {
    'title': 'Card',
    'desc': 'Surface containers for citizen services, scheme benefits, statistics, and interactive items',
    'component': 'Ux4gCard',
    'pkg': 'com.ux4g.designsystemv1.components.Ux4gCard',
    'preview_code': '''Ux4gCard(
    modifier = Modifier.fillMaxWidth(),
    variant = Ux4gCardVariant.Elevated,
    onClick = { /* Open scheme */ }
) {
    Column(modifier = Modifier.padding(16.dp)) {
        Ux4gBadge(type = Ux4gBadgeType.Label("DBT Enabled"), containerColor = Ux4gColors.green600)
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = "PM Kisan Samman Nidhi", style = Ux4gTheme.typography.tM_strong)
        Text(text = "Direct income support of ₹6,000 per year in 3 equal installments.", style = Ux4gTheme.typography.bS_default)
    }
}''',
    'usage_code': '''import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.ux4g.designsystemv1.components.Ux4gCard
import com.ux4g.designsystemv1.components.Ux4gCardVariant

@Composable
fun CardExample() {
    Ux4gCard(
        variant = Ux4gCardVariant.Elevated,
        onClick = { /* card tapped */ }
    ) {
        // Child composables inside card
    }
}''',
    'tables': [
        ('`Ux4gCard` Props', ['Prop', 'Type', 'Description', 'Default'], [
            [f'`content` {REQ}', '`@Composable () -> Unit`', 'Child composable content inside the card container.', '`—`'],
            ['`modifier`', '`Modifier`', 'Compose layout modifier.', '`Modifier`'],
            ['`variant`', '`Ux4gCardVariant`', 'Elevation style: `Elevated`, `Outlined`, `Filled`.', '`Ux4gCardVariant.Elevated`'],
            ['`shape`', '`Shape`', 'Corner shape applied to card boundary.', '`RoundedCornerShape(8.dp)`'],
            ['`backgroundColor`', '`Color?`', 'Container background color (defaults to surface).', '`null`'],
            ['`borderColor`', '`Color?`', 'Border stroke color for outlined variant.', '`null`'],
            ['`elevation`', '`Dp`', 'Shadow elevation depth for elevated variant.', '`2.dp`'],
            ['`onClick`', '`(() -> Unit)?`', 'Optional click listener turning card into interactive touch target.', '`null`'],
            ['`enabled`', '`Boolean`', 'Whether click interaction is enabled.', '`true`']
        ]),
        ('`Ux4gCardVariant` Enum', ['Variant', 'Visual Description'], [
            ['`Elevated`', 'Surface fill with soft drop-shadow elevation.'],
            ['`Outlined`', 'Flat surface fill with 1dp border stroke outline.'],
            ['`Filled`', 'Neutral container fill without shadow or border.']
        ])
    ]
}

# 8. carousel
components['carousel'] = {
    'title': 'Carousel',
    'desc': 'Horizontal paging banner with auto-scroll and indicators for announcements and flagship schemes',
    'component': 'Ux4gCarousel',
    'pkg': 'com.ux4g.designsystemv1.components.Ux4gCarousel',
    'preview_code': '''val banners = listOf(
    CarouselItem(title = "Digital India Week 2024", description = "Empowering citizens with digital governance"),
    CarouselItem(title = "Viksit Bharat Sankalp", description = "Reaching the unreached with government benefits")
)

Ux4gCarousel(
    items = banners,
    autoScroll = true,
    intervalMs = 4000L,
    onItemClick = { item -> /* open detail */ }
)''',
    'usage_code': '''import androidx.compose.runtime.Composable
import com.ux4g.designsystemv1.components.Ux4gCarousel

@Composable
fun CarouselExample() {
    Ux4gCarousel(
        items = listOf("Banner 1", "Banner 2", "Banner 3"),
        autoScroll = true,
        intervalMs = 4000L
    ) { index, item ->
        // Render each slide
    }
}''',
    'tables': [
        ('`Ux4gCarousel` Props', ['Prop', 'Type', 'Description', 'Default'], [
            [f'`items` {REQ}', '`List<T>`', 'List of items to render across carousel slides.', '`—`'],
            [f'`content` {REQ}', '`@Composable (index: Int, item: T) -> Unit`', 'Composable lambda rendering slide content.', '`—`'],
            ['`modifier`', '`Modifier`', 'Compose layout modifier.', '`Modifier`'],
            ['`autoScroll`', '`Boolean`', 'Whether slides advance automatically on a timer.', '`true`'],
            ['`intervalMs`', '`Long`', 'Duration in milliseconds per slide before transitioning.', '`4000L`'],
            ['`showIndicator`', '`Boolean`', 'Whether to render page dots indicator at bottom.', '`true`'],
            ['`onItemClick`', '`((T) -> Unit)?`', 'Optional click listener callback when slide is tapped.', '`null`']
        ])
    ]
}

# 9. checkbox
components['checkbox'] = {
    'title': 'Checkbox',
    'desc': 'Multi-selection controls with custom states, tri-state support, and citizen declaration labels',
    'component': 'Ux4gCheckbox',
    'pkg': 'com.ux4g.designsystemv1.components.Ux4gCheckbox',
    'preview_code': '''Ux4gCheckbox(
    checked = isAgreed,
    onCheckedChange = { isAgreed = it },
    label = "I declare that all submitted information is accurate and verified as per Aadhaar."
)''',
    'usage_code': '''import androidx.compose.runtime.*
import com.ux4g.designsystemv1.components.Ux4gCheckbox

@Composable
fun CheckboxExample() {
    var checked by remember { mutableStateOf(false) }

    Ux4gCheckbox(
        checked = checked,
        onCheckedChange = { checked = it },
        label = "Agree to Terms and Conditions",
        enabled = true
    )
}''',
    'tables': [
        ('`Ux4gCheckbox` Props', ['Prop', 'Type', 'Description', 'Default'], [
            [f'`checked` {REQ}', '`Boolean`', 'Current boolean checked state.', '`—`'],
            [f'`onCheckedChange` {REQ}', '`((Boolean) -> Unit)?`', 'Callback triggered when checkbox is toggled.', '`—`'],
            ['`modifier`', '`Modifier`', 'Compose layout modifier.', '`Modifier`'],
            ['`label`', '`String?`', 'Accompanying text label rendered adjacent to checkbox.', '`null`'],
            ['`enabled`', '`Boolean`', 'Whether control responds to user touch interaction.', '`true`'],
            ['`isError`', '`Boolean`', 'Renders red error outline when mandatory field is unchecked.', '`false`'],
            ['`checkedColor`', '`Color?`', 'Background fill color when checked.', '`null`'],
            ['`uncheckedColor`', '`Color?`', 'Border outline color when unchecked.', '`null`'],
            ['`checkmarkColor`', '`Color?`', 'Checkmark icon tick color.', '`null`']
        ])
    ]
}

# 10. chips
components['chips'] = {
    'title': 'Chips',
    'desc': 'Filter, choice, and input chips for categorical selection, scheme tagging, and quick actions',
    'component': 'Ux4gChip',
    'pkg': 'com.ux4g.designsystemv1.components.Ux4gChip',
    'preview_code': '''// Filter Chip
Ux4gFilterChip(
    selected = isSelected,
    onClick = { isSelected = !isSelected },
    label = "Agriculture & Farming"
)

// Action Chip
Ux4gActionChip(
    label = "Download Certificate",
    onClick = { /* download action */ }
)''',
    'usage_code': '''import androidx.compose.runtime.*
import com.ux4g.designsystemv1.components.Ux4gFilterChip
import com.ux4g.designsystemv1.components.Ux4gActionChip

@Composable
fun ChipGroupExample() {
    var selected by remember { mutableStateOf(true) }

    Ux4gFilterChip(
        label = "Scholarships",
        selected = selected,
        onClick = { selected = !selected }
    )
}''',
    'tables': [
        ('`Ux4gFilterChip` Props', ['Prop', 'Type', 'Description', 'Default'], [
            [f'`label` {REQ}', '`String`', 'Text displayed inside the chip.', '`—`'],
            [f'`selected` {REQ}', '`Boolean`', 'Current selected state of the filter chip.', '`false`'],
            [f'`onClick` {REQ}', '`() -> Unit`', 'Callback invoked when chip is tapped.', '`—`'],
            ['`modifier`', '`Modifier`', 'Compose layout modifier.', '`Modifier`'],
            ['`enabled`', '`Boolean`', 'Interactive enabled state.', '`true`'],
            ['`leadingIcon`', '`(@Composable () -> Unit)?`', 'Optional leading icon composable slot.', '`null`'],
            ['`trailingIcon`', '`(@Composable () -> Unit)?`', 'Optional trailing icon (e.g. close/clear cross).', '`null`']
        ]),
        ('`Ux4gActionChip` Props', ['Prop', 'Type', 'Description', 'Default'], [
            [f'`label` {REQ}', '`String`', 'Action button label text.', '`—`'],
            [f'`onClick` {REQ}', '`() -> Unit`', 'Action callback handler.', '`—`'],
            ['`modifier`', '`Modifier`', 'Compose layout modifier.', '`Modifier`'],
            ['`leadingIcon`', '`(@Composable () -> Unit)?`', 'Optional leading icon composable slot.', '`null`'],
            ['`enabled`', '`Boolean`', 'Interactive enabled state.', '`true`']
        ])
    ]
}

# 11. date-picker
components['date-picker'] = {
    'title': 'Date Picker',
    'desc': 'Calendar modal and text field date selector supporting DOB, ranges, and maximum date limits',
    'component': 'Ux4gDatePicker',
    'pkg': 'com.ux4g.designsystemv1.components.Ux4gDatePicker',
    'preview_code': '''Ux4gDatePicker(
    selectedDate = selectedDate,
    onDateSelected = { date -> selectedDate = date },
    label = "Date of Birth (as per Aadhaar)",
    placeholder = "DD/MM/YYYY",
    maxDate = LocalDate.now()
)''',
    'usage_code': '''import androidx.compose.runtime.*
import java.time.LocalDate
import com.ux4g.designsystemv1.components.Ux4gDatePicker

@Composable
fun DatePickerExample() {
    var dob by remember { mutableStateOf<LocalDate?>(null) }

    Ux4gDatePicker(
        label = "Date of Birth",
        selectedDate = dob,
        onDateSelected = { dob = it },
        maxDate = LocalDate.now()
    )
}''',
    'tables': [
        ('`Ux4gDatePicker` Props', ['Prop', 'Type', 'Description', 'Default'], [
            [f'`label` {REQ}', '`String`', 'Header title above the date input field.', '`—`'],
            [f'`onDateSelected` {REQ}', '`(LocalDate) -> Unit`', 'Callback providing newly selected `LocalDate`.', '`—`'],
            ['`selectedDate`', '`LocalDate?`', 'Currently selected calendar date.', '`null`'],
            ['`placeholder`', '`String`', 'Hint text displayed when no date is picked.', '`"DD/MM/YYYY"`'],
            ['`minDate`', '`LocalDate?`', 'Earliest selectable date bound.', '`null`'],
            ['`maxDate`', '`LocalDate?`', 'Latest selectable date bound (e.g. `LocalDate.now()`).', '`null`'],
            ['`isError`', '`Boolean`', 'Highlights field in red validation error style.', '`false`'],
            ['`errorMessage`', '`String?`', 'Validation error message below input.', '`null`'],
            ['`enabled`', '`Boolean`', 'Whether date picker is interactive.', '`true`'],
            ['`modifier`', '`Modifier`', 'Compose layout modifier.', '`Modifier`']
        ])
    ]
}

# 12. divider
components['divider'] = {
    'title': 'Divider',
    'desc': 'Horizontal visual separator lines and labeled partition bars for dividing form sections',
    'component': 'Ux4gDivider',
    'pkg': 'com.ux4g.designsystemv1.components.Ux4gDivider',
    'preview_code': '''// Standard Divider
Ux4gDivider()

// Divider with Middle Label
Ux4gDividerWithLabel(label = "OR VERIFY WITH")''',
    'usage_code': '''import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import com.ux4g.designsystemv1.components.Ux4gDivider
import com.ux4g.designsystemv1.components.Ux4gDividerWithLabel

@Composable
fun DividerExample() {
    Ux4gDivider(thickness = 1.dp)
    Ux4gDividerWithLabel(label = "OR")
}''',
    'tables': [
        ('`Ux4gDivider` Props', ['Prop', 'Type', 'Description', 'Default'], [
            ['`modifier`', '`Modifier`', 'Compose layout modifier.', '`Modifier`'],
            ['`thickness`', '`Dp`', 'Stroke thickness height of divider line.', '`1.dp`'],
            ['`color`', '`Color`', 'Divider line stroke color token.', '`Ux4gTheme.colorScheme.onSurface.copy(alpha = 0.12f)`']
        ]),
        ('`Ux4gDividerWithLabel` Props', ['Prop', 'Type', 'Description', 'Default'], [
            [f'`label` {REQ}', '`String`', 'Middle partition label text.', '`—`'],
            ['`modifier`', '`Modifier`', 'Compose layout modifier.', '`Modifier`'],
            ['`color`', '`Color`', 'Line color token.', '`Ux4gTheme.colorScheme.onSurface.copy(alpha = 0.12f)`']
        ])
    ]
}

# 13. dropdown
components['dropdown'] = {
    'title': 'Dropdown',
    'desc': 'Selection and action dropdown menus for States, Districts, categories, and contextual options',
    'component': 'Ux4gSelectionDropdown',
    'pkg': 'com.ux4g.designsystemv1.components.dropdowns.Ux4gSelectionDropdown',
    'preview_code': '''val states = listOf("Andhra Pradesh", "Delhi", "Maharashtra", "Tamil Nadu", "Uttar Pradesh")

Ux4gSelectionDropdown(
    label = "Select State / UT",
    items = states,
    selectedItem = selectedState,
    onItemSelected = { selectedState = it },
    isSearchable = true
)''',
    'usage_code': '''import androidx.compose.runtime.*
import com.ux4g.designsystemv1.components.dropdowns.Ux4gSelectionDropdown

@Composable
fun DropdownExample() {
    var selected by remember { mutableStateOf<String?>(null) }
    val districts = listOf("Central Delhi", "New Delhi", "North Delhi", "South Delhi")

    Ux4gSelectionDropdown(
        label = "District",
        items = districts,
        selectedItem = selected,
        onItemSelected = { selected = it },
        isSearchable = true
    )
}''',
    'tables': [
        ('`Ux4gSelectionDropdown` Props', ['Prop', 'Type', 'Description', 'Default'], [
            [f'`label` {REQ}', '`String`', 'Header title displayed above dropdown field.', '`—`'],
            [f'`items` {REQ}', '`List<T>`', 'List of selectable items.', '`—`'],
            [f'`onItemSelected` {REQ}', '`(T) -> Unit`', 'Callback invoked when user picks an option.', '`—`'],
            ['`selectedItem`', '`T?`', 'Currently chosen item model.', '`null`'],
            ['`placeholder`', '`String`', 'Placeholder text when no item is selected.', '`"Select option"`'],
            ['`isSearchable`', '`Boolean`', 'Enables type-ahead search filter inside popup list.', '`false`'],
            ['`enabled`', '`Boolean`', 'Interactive enabled state.', '`true`'],
            ['`isError`', '`Boolean`', 'Shows red error validation styling.', '`false`'],
            ['`errorMessage`', '`String?`', 'Validation error message below input.', '`null`'],
            ['`modifier`', '`Modifier`', 'Compose layout modifier.', '`Modifier`']
        ])
    ]
}

# 14. empty-state
components['empty-state'] = {
    'title': 'Empty State',
    'desc': 'Informative fallback view for zero search results, empty inboxes, and fresh accounts',
    'component': 'Ux4gEmptyState',
    'pkg': 'com.ux4g.designsystemv1.components.Ux4gEmptyState',
    'preview_code': '''Ux4gEmptyState(
    icon = Ux4gIcons.Folder,
    title = "No Applications Found",
    description = "You have not submitted any scheme applications yet. Start a new application to track status here.",
    actionLabel = "Explore Schemes",
    onActionClick = { /* navigate to schemes */ }
)''',
    'usage_code': '''import androidx.compose.runtime.Composable
import com.ux4g.designsystemv1.components.Ux4gEmptyState
import com.ux4g.designsystemv1.foundation.Ux4gIcons

@Composable
fun EmptyInboxExample() {
    Ux4gEmptyState(
        icon = Ux4gIcons.Inbox,
        title = "No Pending Notifications",
        description = "All your citizen service alerts will appear here.",
        actionLabel = "Refresh",
        onActionClick = { /* refresh */ }
    )
}''',
    'tables': [
        ('`Ux4gEmptyState` Props', ['Prop', 'Type', 'Description', 'Default'], [
            [f'`icon` {REQ}', '`Ux4gIcon`', 'Hero vector icon displayed at the top.', '`—`'],
            [f'`title` {REQ}', '`String`', 'Bold primary headline message.', '`—`'],
            [f'`description` {REQ}', '`String`', 'Explanatory subtitle guiding the citizen.', '`—`'],
            ['`modifier`', '`Modifier`', 'Compose layout modifier.', '`Modifier`'],
            ['`actionLabel`', '`String?`', 'Optional call-to-action button text.', '`null`'],
            ['`onActionClick`', '`(() -> Unit)?`', 'Callback when action button is tapped.', '`null`'],
            ['`iconTint`', '`Color?`', 'Custom color tint for hero icon.', '`null`']
        ])
    ]
}

# 15. feedback-form
components['feedback-form'] = {
    'title': 'Feedback Form',
    'desc': 'Citizen satisfaction ratings including 5-Star Ratings, CSAT (emojis), and NPS widgets',
    'component': 'Ux4gFeedbackFormStar',
    'pkg': 'com.ux4g.designsystemv1.components.feedback.Ux4gFeedbackFormStar',
    'preview_code': '''Ux4gFeedbackFormStar(
    title = "How was your service experience?",
    subtitle = "Your feedback helps improve public digital delivery",
    onRatingSubmit = { rating, remarks ->
        /* Send telemetry */
    }
)''',
    'usage_code': '''import androidx.compose.runtime.Composable
import com.ux4g.designsystemv1.components.feedback.Ux4gFeedbackFormStar
import com.ux4g.designsystemv1.components.feedback.Ux4gFeedbackFormCsat

@Composable
fun FeedbackDemo() {
    Ux4gFeedbackFormStar(
        title = "Rate this service",
        onRatingSubmit = { stars, comment ->
            // Process feedback
        }
    )
}''',
    'tables': [
        ('`Ux4gFeedbackFormStar` Props', ['Prop', 'Type', 'Description', 'Default'], [
            [f'`title` {REQ}', '`String`', 'Headline prompt asking citizen for review.', '`—`'],
            [f'`onRatingSubmit` {REQ}', '`(Int, String) -> Unit`', 'Callback providing rating score (1-5) and remarks.', '`—`'],
            ['`subtitle`', '`String?`', 'Optional secondary instruction message.', '`null`'],
            ['`showCommentField`', '`Boolean`', 'Whether to render multiline remarks text area.', '`true`'],
            ['`modifier`', '`Modifier`', 'Compose layout modifier.', '`Modifier`']
        ]),
        ('`Ux4gFeedbackFormCsat` Props', ['Prop', 'Type', 'Description', 'Default'], [
            [f'`title` {REQ}', '`String`', 'Question title prompt.', '`—`'],
            [f'`onCsatSubmit` {REQ}', '`(Int, String) -> Unit`', 'Callback with CSAT emoji index (1-5) and feedback.', '`—`'],
            ['`modifier`', '`Modifier`', 'Compose layout modifier.', '`Modifier`']
        ])
    ]
}

# 16. file-upload
components['file-upload'] = {
    'title': 'File Upload',
    'desc': 'Document attachment drag/picker zone with file extension checking, max file size enforcement, and upload progress',
    'component': 'Ux4gFileUpload',
    'pkg': 'com.ux4g.designsystemv1.components.Ux4gFileUpload',
    'preview_code': '''Ux4gFileUpload(
    label = "Upload Income Certificate",
    allowedExtensions = listOf("pdf", "jpg", "png"),
    maxSizeMb = 5,
    onFileSelected = { uri -> /* Upload document */ }
)''',
    'usage_code': '''import androidx.compose.runtime.Composable
import com.ux4g.designsystemv1.components.Ux4gFileUpload

@Composable
fun DocumentUploadExample() {
    Ux4gFileUpload(
        label = "Proof of Residence (PDF / JPG up to 5MB)",
        allowedExtensions = listOf("pdf", "jpg", "png"),
        maxSizeMb = 5,
        onFileSelected = { fileUri ->
            // Upload to server
        }
    )
}''',
    'tables': [
        ('`Ux4gFileUpload` Props', ['Prop', 'Type', 'Description', 'Default'], [
            [f'`label` {REQ}', '`String`', 'Attachment title prompt.', '`—`'],
            [f'`onFileSelected` {REQ}', '`(Uri) -> Unit`', 'Callback returning picked document Uri.', '`—`'],
            ['`allowedExtensions`', '`List<String>`', 'Permitted file extensions.', '`listOf("pdf", "jpg", "png")`'],
            ['`maxSizeMb`', '`Int`', 'Maximum permissible file size in Megabytes.', '`5`'],
            ['`enabled`', '`Boolean`', 'Interactive enabled state.', '`true`'],
            ['`isError`', '`Boolean`', 'Error state highlighting.', '`false`'],
            ['`errorMessage`', '`String?`', 'Error helper message.', '`null`'],
            ['`modifier`', '`Modifier`', 'Compose layout modifier.', '`Modifier`']
        ])
    ]
}

# 17. input-field
components['input-field'] = {
    'title': 'Input Field',
    'desc': 'Standard Text Input, Aadhaar Number Input, and PAN Input with status states, masking, and validation',
    'component': 'Ux4gInputField',
    'pkg': 'com.ux4g.designsystemv1.components.inputfield.Ux4gInputField',
    'preview_code': '''// Standard Text Input
Ux4gInputField(
    value = fullName,
    onValueChange = { fullName = it },
    label = "Full Name (as per Aadhaar)",
    placeholder = "e.g. Ramesh Kumar"
)

// Aadhaar 12-Digit Auto-Formatting Input
Ux4gAadhaarInputField(
    value = aadhaarNumber,
    onValueChange = { aadhaarNumber = it },
    label = "Aadhaar Number"
)''',
    'usage_code': '''import androidx.compose.runtime.*
import com.ux4g.designsystemv1.components.inputfield.Ux4gInputField
import com.ux4g.designsystemv1.components.inputfield.Ux4gInputFieldStatus
import com.ux4g.designsystemv1.components.inputfield.Ux4gInputFieldSize

@Composable
fun InputDemo() {
    var name by remember { mutableStateOf("") }

    Ux4gInputField(
        value = name,
        onValueChange = { name = it },
        label = "Full Name",
        placeholder = "Enter your full name",
        size = Ux4gInputFieldSize.Medium,
        status = Ux4gInputFieldStatus.Default
    )
}''',
    'tables': [
        ('`Ux4gInputField` Props', ['Prop', 'Type', 'Description', 'Default'], [
            [f'`value` {REQ}', '`String`', 'Current text input value.', '`—`'],
            [f'`onValueChange` {REQ}', '`(String) -> Unit`', 'Callback invoked when input text changes.', '`—`'],
            ['`label`', '`String?`', 'Header title label above input.', '`null`'],
            ['`placeholder`', '`String`', 'Placeholder text shown when field is empty.', '`""`'],
            ['`modifier`', '`Modifier`', 'Compose layout modifier.', '`Modifier`'],
            ['`size`', '`Ux4gInputFieldSize`', 'Input size: `Small`, `Medium`, `Large`.', '`Ux4gInputFieldSize.Medium`'],
            ['`status`', '`Ux4gInputFieldStatus`', 'Field state: `Default`, `Success`, `Warning`, `Error`, `Disabled`.', '`Ux4gInputFieldStatus.Default`'],
            ['`type`', '`Ux4gInputFieldType`', 'Input type: `Text`, `Password`, `Number`, `Email`, `Phone`.', '`Ux4gInputFieldType.Text`'],
            ['`helperText`', '`String?`', 'Secondary guidance or error text below field.', '`null`'],
            ['`leadingIcon`', '`(@Composable () -> Unit)?`', 'Optional composable icon at start.', '`null`'],
            ['`trailingIcon`', '`(@Composable () -> Unit)?`', 'Optional composable icon at end (e.g. password toggle).', '`null`'],
            ['`enabled`', '`Boolean`', 'Interactive enabled state.', '`true`'],
            ['`readOnly`', '`Boolean`', 'Whether field is read-only non-editable.', '`false`']
        ]),
        ('`Ux4gAadhaarInputField` Props', ['Prop', 'Type', 'Description', 'Default'], [
            [f'`value` {REQ}', '`String`', '12-digit Aadhaar number string.', '`—`'],
            [f'`onValueChange` {REQ}', '`(String) -> Unit`', 'Change listener formatting in `XXXX XXXX XXXX` chunks.', '`—`'],
            ['`label`', '`String?`', 'Header label.', '`"Aadhaar Number"`'],
            ['`mask`', '`Boolean`', 'Whether to mask first 8 digits for citizen privacy.', '`false`']
        ])
    ]
}

# 18. journey-timeline
components['journey-timeline'] = {
    'title': 'Journey Timeline',
    'desc': 'Multi-stage application status tracker showing Submitted, Verified, Approved, Disbursed checkpoints',
    'component': 'Ux4gJourneyTimeline',
    'pkg': 'com.ux4g.designsystemv1.components.Ux4gJourneyTimeline',
    'preview_code': '''val steps = listOf(
    JourneyTimelineStep(title = "Application Submitted", date = "12 Aug 2024", state = StepState.Completed),
    JourneyTimelineStep(title = "Field Officer Verification", date = "15 Aug 2024", state = StepState.Completed),
    JourneyTimelineStep(title = "Sanction Order Issued", date = "In Progress", state = StepState.Current),
    JourneyTimelineStep(title = "Direct Bank Transfer", date = "Pending", state = StepState.Upcoming)
)

Ux4gJourneyTimeline(steps = steps)''',
    'usage_code': '''import androidx.compose.runtime.Composable
import com.ux4g.designsystemv1.components.Ux4gJourneyTimeline
import com.ux4g.designsystemv1.components.JourneyTimelineStep

@Composable
fun ApplicationTrackingExample(steps: List<JourneyTimelineStep>) {
    Ux4gJourneyTimeline(steps = steps)
}''',
    'tables': [
        ('`Ux4gJourneyTimeline` Props', ['Prop', 'Type', 'Description', 'Default'], [
            [f'`steps` {REQ}', '`List<JourneyTimelineStep>`', 'Ordered list of lifecycle milestone checkpoint items.', '`—`'],
            ['`modifier`', '`Modifier`', 'Compose layout modifier.', '`Modifier`'],
            ['`orientation`', '`TimelineOrientation`', 'Layout orientation (`Vertical` or `Horizontal`).', '`Vertical`']
        ]),
        ('`JourneyTimelineStep` Model', ['Prop', 'Type', 'Description', 'Default'], [
            [f'`title` {REQ}', '`String`', 'Checkpoint title heading.', '`—`'],
            ['`subtitle`', '`String?`', 'Description or department officer remarks.', '`null`'],
            ['`date`', '`String?`', 'Timestamp string (e.g. `12 Aug 2024, 03:45 PM`).', '`null`'],
            ['`state`', '`StepState`', 'Progress state: `Completed`, `Current`, `Upcoming`, `Error`.', '`StepState.Upcoming`']
        ])
    ]
}

# 19. link
components['link'] = {
    'title': 'Link',
    'desc': 'Interactive anchor hyperlinks for portal terms, privacy policy, and external government portals',
    'component': 'Ux4gLink',
    'pkg': 'com.ux4g.designsystemv1.components.Ux4gLink',
    'preview_code': '''Ux4gLink(url = "https://india.gov.in") {
    Text(
        text = "Visit National Portal of India",
        style = Ux4gTheme.typography.lM_strong,
        color = Ux4gTheme.colorScheme.primary
    )
}''',
    'usage_code': '''import androidx.compose.runtime.Composable
import androidx.compose.material3.Text
import com.ux4g.designsystemv1.components.Ux4gLink
import com.ux4g.designsystemv1.theme.Ux4gTheme

@Composable
fun LinkDemo() {
    Ux4gLink(url = "https://ux4g.gov.in") {
        Text("Learn more about UX4G Design Guidelines", color = Ux4gTheme.colorScheme.primary)
    }
}''',
    'tables': [
        ('`Ux4gLink` Props', ['Prop', 'Type', 'Description', 'Default'], [
            [f'`url` {REQ}', '`String`', 'Destination web URL to open in system browser.', '`—`'],
            [f'`content` {REQ}', '`@Composable () -> Unit`', 'Child composable content inside clickable hyperlink container.', '`—`'],
            ['`modifier`', '`Modifier`', 'Compose layout modifier.', '`Modifier`'],
            ['`enabled`', '`Boolean`', 'Whether link is clickable.', '`true`']
        ])
    ]
}

# 20. loader
components['loader'] = {
    'title': 'Loader & Spinner',
    'desc': 'Animated circular spinner and linear indeterminate progress bar for API loading states',
    'component': 'Ux4gLoader',
    'pkg': 'com.ux4g.designsystemv1.components.Ux4gLoader',
    'preview_code': '''// Circular Spinner
Ux4gLoader(
    size = Ux4gLoaderSize.Medium,
    color = Ux4gColors.primary
)''',
    'usage_code': '''import androidx.compose.runtime.Composable
import com.ux4g.designsystemv1.components.Ux4gLoader
import com.ux4g.designsystemv1.components.Ux4gLoaderSize

@Composable
fun LoaderExample() {
    Ux4gLoader(size = Ux4gLoaderSize.Medium)
}''',
    'tables': [
        ('`Ux4gLoader` Props', ['Prop', 'Type', 'Description', 'Default'], [
            ['`modifier`', '`Modifier`', 'Compose layout modifier.', '`Modifier`'],
            ['`size`', '`Ux4gLoaderSize`', 'Spinner dimensions: `Small` (16dp), `Medium` (24dp), `Large` (40dp).', '`Ux4gLoaderSize.Medium`'],
            ['`color`', '`Color`', 'Spinner stroke color.', '`Ux4gTheme.colorScheme.primary`'],
            ['`strokeWidth`', '`Dp?`', 'Optional custom stroke thickness.', '`null`']
        ])
    ]
}

# 21. modal
components['modal'] = {
    'title': 'Modal Dialog',
    'desc': 'Focus-trapping popup dialog for confirming destructive actions, service submissions, or alerts',
    'component': 'Ux4gModal',
    'pkg': 'com.ux4g.designsystemv1.components.Ux4gModal',
    'preview_code': '''Ux4gModal(
    show = showDialog,
    title = "Confirm Final Submission",
    message = "Are you sure you want to submit your application? Once submitted, form details cannot be modified.",
    confirmText = "Yes, Submit Application",
    dismissText = "Review Again",
    onConfirm = { /* Submit API */ },
    onDismiss = { showDialog = false }
)''',
    'usage_code': '''import androidx.compose.runtime.*
import com.ux4g.designsystemv1.components.Ux4gModal

@Composable
fun ConfirmationDialogExample() {
    var open by remember { mutableStateOf(false) }

    Ux4gModal(
        show = open,
        title = "Confirm Action",
        message = "Do you want to proceed with Aadhaar authentication?",
        confirmText = "Proceed",
        dismissText = "Cancel",
        onConfirm = { open = false },
        onDismiss = { open = false }
    )
}''',
    'tables': [
        ('`Ux4gModal` Props', ['Prop', 'Type', 'Description', 'Default'], [
            [f'`show` {REQ}', '`Boolean`', 'Controls whether the modal dialog is visible.', '`—`'],
            [f'`title` {REQ}', '`String`', 'Dialog headline title.', '`—`'],
            [f'`message` {REQ}', '`String`', 'Dialog explanation body copy.', '`—`'],
            [f'`onConfirm` {REQ}', '`() -> Unit`', 'Primary action button click callback.', '`—`'],
            [f'`onDismiss` {REQ}', '`() -> Unit`', 'Dismiss / cancel button click callback.', '`—`'],
            ['`confirmText`', '`String`', 'Primary confirm button label.', '`"Confirm"`'],
            ['`dismissText`', '`String`', 'Dismiss button label.', '`"Cancel"`'],
            ['`isDestructive`', '`Boolean`', 'Styles confirm button in red destructive tone.', '`false`'],
            ['`icon`', '`Ux4gIcon?`', 'Optional top center status visual icon.', '`null`']
        ])
    ]
}

# 22. otp-input
components['otp-input'] = {
    'title': 'OTP Input',
    'desc': 'Auto-advancing 4/6-digit verification code input boxes with timer, paste handling, and resend action',
    'component': 'Ux4gOtpInput',
    'pkg': 'com.ux4g.designsystemv1.components.Ux4gOtpInput',
    'preview_code': '''Ux4gOtpInput(
    length = 6,
    otpValue = otp,
    onOtpChange = { otp = it },
    onComplete = { code -> /* Trigger OTP verify API */ },
    onResendClick = { /* Send new SMS OTP */ }
)''',
    'usage_code': '''import androidx.compose.runtime.*
import com.ux4g.designsystemv1.components.Ux4gOtpInput

@Composable
fun OtpVerificationScreen() {
    var otp by remember { mutableStateOf("") }

    Ux4gOtpInput(
        length = 6,
        otpValue = otp,
        onOtpChange = { otp = it },
        onComplete = { fullOtp ->
            verifyOtpApi(fullOtp)
        }
    )
}''',
    'tables': [
        ('`Ux4gOtpInput` Props', ['Prop', 'Type', 'Description', 'Default'], [
            [f'`otpValue` {REQ}', '`String`', 'Current OTP string value.', '`—`'],
            [f'`onOtpChange` {REQ}', '`(String) -> Unit`', 'Callback invoked on each digit change.', '`—`'],
            ['`length`', '`Int`', 'Number of OTP digit boxes (typically 4 or 6).', '`6`'],
            ['`onComplete`', '`((String) -> Unit)?`', 'Fires automatically when citizen enters all digits.', '`null`'],
            ['`onResendClick`', '`(() -> Unit)?`', 'Resend OTP action trigger callback.', '`null`'],
            ['`isError`', '`Boolean`', 'Highlights digit boxes in error red outline.', '`false`'],
            ['`errorMessage`', '`String?`', 'Error description text below boxes.', '`null`'],
            ['`enabled`', '`Boolean`', 'Interactive enabled state.', '`true`'],
            ['`modifier`', '`Modifier`', 'Compose layout modifier.', '`Modifier`']
        ])
    ]
}

# 23. pagination
components['pagination'] = {
    'title': 'Pagination',
    'desc': 'Page number navigation, next/prev triggers, and dot indicators for large result sets',
    'component': 'Ux4gPaginationIndicator',
    'pkg': 'com.ux4g.designsystemv1.components.Ux4gPaginationIndicator',
    'preview_code': '''Ux4gPaginationIndicator(
    currentPage = page,
    totalPages = 12,
    onPageChange = { newPage -> page = newPage }
)''',
    'usage_code': '''import androidx.compose.runtime.*
import com.ux4g.designsystemv1.components.Ux4gPaginationIndicator

@Composable
fun PaginationExample() {
    var page by remember { mutableStateOf(1) }

    Ux4gPaginationIndicator(
        currentPage = page,
        totalPages = 10,
        onPageChange = { page = it }
    )
}''',
    'tables': [
        ('`Ux4gPaginationIndicator` Props', ['Prop', 'Type', 'Description', 'Default'], [
            [f'`currentPage` {REQ}', '`Int`', 'Active 1-indexed page pointer.', '`—`'],
            [f'`totalPages` {REQ}', '`Int`', 'Total number of pages available.', '`—`'],
            [f'`onPageChange` {REQ}', '`(Int) -> Unit`', 'Callback triggered when page button is clicked.', '`—`'],
            ['`modifier`', '`Modifier`', 'Compose layout modifier.', '`Modifier`'],
            ['`maxVisiblePages`', '`Int`', 'Maximum visible number buttons before ellipsis.', '`5`']
        ])
    ]
}

# 24. progress-indicator
components['progress-indicator'] = {
    'title': 'Progress Indicator',
    'desc': 'Linear progress bar, circular progress, and half-circle gauges for profile completion and workflow tracking',
    'component': 'Ux4gLinearProgressBar',
    'pkg': 'com.ux4g.designsystemv1.components.progress.Ux4gLinearProgressBar',
    'preview_code': '''// Linear Progress
Ux4gLinearProgressBar(
    progress = 0.75f,
    label = "Profile Completion: 75%"
)

// Circular Progress Gauge
Ux4gCircularProgress(
    progress = 0.80f,
    size = 64.dp
)''',
    'usage_code': '''import androidx.compose.runtime.Composable
import com.ux4g.designsystemv1.components.progress.Ux4gLinearProgressBar
import com.ux4g.designsystemv1.components.progress.Ux4gCircularProgress

@Composable
fun ProgressDemo() {
    Ux4gLinearProgressBar(progress = 0.6f)
}''',
    'tables': [
        ('`Ux4gLinearProgressBar` Props', ['Prop', 'Type', 'Description', 'Default'], [
            [f'`progress` {REQ}', '`Float`', 'Determinate progress float value between `0.0f` and `1.0f`.', '`—`'],
            ['`modifier`', '`Modifier`', 'Compose layout modifier.', '`Modifier`'],
            ['`label`', '`String?`', 'Optional label above progress bar.', '`null`'],
            ['`color`', '`Color`', 'Progress fill bar color.', '`Ux4gTheme.colorScheme.primary`'],
            ['`trackColor`', '`Color`', 'Background track rail color.', '`Ux4gTheme.colorScheme.onSurface.copy(alpha = 0.12f)`']
        ]),
        ('`Ux4gCircularProgress` Props', ['Prop', 'Type', 'Description', 'Default'], [
            [f'`progress` {REQ}', '`Float`', 'Progress float value from `0.0f` to `1.0f`.', '`—`'],
            ['`size`', '`Dp`', 'Diameter dimension of circle.', '`48.dp`'],
            ['`strokeWidth`', '`Dp`', 'Thickness of gauge arc.', '`4.dp`'],
            ['`color`', '`Color`', 'Progress arc color.', '`Ux4gTheme.colorScheme.primary`']
        ])
    ]
}

# 25. radio-button
components['radio-button'] = {
    'title': 'Radio Button',
    'desc': 'Single-selection radio group for gender selection, payment modes, and mutually exclusive options',
    'component': 'Ux4gRadioButton',
    'pkg': 'com.ux4g.designsystemv1.components.Ux4gRadioButton',
    'preview_code': '''val options = listOf("Male", "Female", "Transgender")

Ux4gRadioGroup(
    options = options,
    selectedOption = selectedGender,
    onOptionSelected = { selectedGender = it },
    label = "Gender (as per Aadhaar)"
)''',
    'usage_code': '''import androidx.compose.runtime.*
import com.ux4g.designsystemv1.components.Ux4gRadioButton
import com.ux4g.designsystemv1.components.Ux4gRadioGroup

@Composable
fun RadioGroupExample() {
    var selected by remember { mutableStateOf("Online") }

    Ux4gRadioGroup(
        options = listOf("Online Payment", "Demand Draft", "Challan"),
        selectedOption = selected,
        onOptionSelected = { selected = it }
    )
}''',
    'tables': [
        ('`Ux4gRadioButton` Props', ['Prop', 'Type', 'Description', 'Default'], [
            [f'`selected` {REQ}', '`Boolean`', 'Whether this radio button is selected.', '`—`'],
            [f'`onClick` {REQ}', '`(() -> Unit)?`', 'Callback invoked when radio is clicked.', '`—`'],
            ['`label`', '`String?`', 'Accompanying text label.', '`null`'],
            ['`enabled`', '`Boolean`', 'Interactive enabled state.', '`true`'],
            ['`modifier`', '`Modifier`', 'Compose layout modifier.', '`Modifier`'],
            ['`selectedColor`', '`Color?`', 'Inner dot and ring color when selected.', '`null`'],
            ['`unselectedColor`', '`Color?`', 'Outer ring color when unselected.', '`null`']
        ]),
        ('`Ux4gRadioGroup` Props', ['Prop', 'Type', 'Description', 'Default'], [
            [f'`options` {REQ}', '`List<T>`', 'List of selectable option models.', '`—`'],
            [f'`selectedOption` {REQ}', '`T?`', 'Currently chosen active option.', '`—`'],
            [f'`onOptionSelected` {REQ}', '`(T) -> Unit`', 'Selection change event callback.', '`—`'],
            ['`label`', '`String?`', 'Group header label text.', '`null`'],
            ['`modifier`', '`Modifier`', 'Compose layout modifier.', '`Modifier`']
        ])
    ]
}

# 26. result-list
components['result-list'] = {
    'title': 'Result List',
    'desc': 'High-density search results, scheme cards, and grievance list rows with thumbnail, metadata, and status badges',
    'component': 'Ux4gResultList',
    'pkg': 'com.ux4g.designsystemv1.components.Ux4gResultList',
    'preview_code': '''Ux4gResultRow(
    title = "Pradhan Mantri Awas Yojana (Urban)",
    subtitle = "Ministry of Housing & Urban Affairs",
    badgeText = "Subsidy upto ₹2.67 Lakh",
    onClick = { /* navigate to scheme detail */ }
)''',
    'usage_code': '''import androidx.compose.runtime.Composable
import com.ux4g.designsystemv1.components.Ux4gResultRow

@Composable
fun ResultListingDemo() {
    Ux4gResultRow(
        title = "National Social Assistance Programme",
        subtitle = "Ministry of Rural Development",
        badgeText = "Direct Pension",
        onClick = { /* View details */ }
    )
}''',
    'tables': [
        ('`Ux4gResultRow` Props', ['Prop', 'Type', 'Description', 'Default'], [
            [f'`title` {REQ}', '`String`', 'Primary heading title.', '`—`'],
            [f'`subtitle` {REQ}', '`String`', 'Secondary department or category subtitle.', '`—`'],
            ['`badgeText`', '`String?`', 'Optional highlight tag text.', '`null`'],
            ['`leadingIcon`', '`(@Composable () -> Unit)?`', 'Optional leading icon or thumbnail.', '`null`'],
            ['`onClick`', '`(() -> Unit)?`', 'Row click listener callback.', '`null`'],
            ['`modifier`', '`Modifier`', 'Compose layout modifier.', '`Modifier`']
        ])
    ]
}

# 27. search-field
components['search-field'] = {
    'title': 'Search Field',
    'desc': 'Universal search bar with search button, voice search trigger, clear cross, and filter dropdown integration',
    'component': 'Ux4gSearchField',
    'pkg': 'com.ux4g.designsystemv1.components.searchfield.Ux4gSearchField',
    'preview_code': '''Ux4gSearchField(
    query = searchQuery,
    onQueryChange = { searchQuery = it },
    placeholder = "Search schemes, certificates, or services...",
    onSearch = { query -> /* Fetch search API */ },
    onVoiceClick = { /* Launch voice recognition */ }
)''',
    'usage_code': '''import androidx.compose.runtime.*
import com.ux4g.designsystemv1.components.searchfield.Ux4gSearchField
import com.ux4g.designsystemv1.components.searchfield.Ux4gSearchFieldSize

@Composable
fun SearchDemo() {
    var query by remember { mutableStateOf("") }

    Ux4gSearchField(
        query = query,
        onQueryChange = { query = it },
        onSearch = { performSearch(it) },
        placeholder = "Search government services..."
    )
}''',
    'tables': [
        ('`Ux4gSearchField` Props', ['Prop', 'Type', 'Description', 'Default'], [
            [f'`query` {REQ}', '`String`', 'Current search query string.', '`—`'],
            [f'`onQueryChange` {REQ}', '`(String) -> Unit`', 'Callback invoked as citizen types into search bar.', '`—`'],
            ['`onSearch`', '`((String) -> Unit)?`', 'Fires when citizen presses Search action button or keyboard enter.', '`null`'],
            ['`placeholder`', '`String`', 'Placeholder text when field is empty.', '`"Search..."`'],
            ['`size`', '`Ux4gSearchFieldSize`', 'Size: `Small`, `Medium`, `Large`.', '`Ux4gSearchFieldSize.Medium`'],
            ['`onVoiceClick`', '`(() -> Unit)?`', 'Optional voice microphone trigger callback.', '`null`'],
            ['`onClearClick`', '`(() -> Unit)?`', 'Callback when clear cross icon is pressed.', '`null`'],
            ['`enabled`', '`Boolean`', 'Interactive enabled state.', '`true`'],
            ['`modifier`', '`Modifier`', 'Compose layout modifier.', '`Modifier`']
        ])
    ]
}

# 28. slider
components['slider'] = {
    'title': 'Slider',
    'desc': 'Continuous and discrete range sliders for loan amounts, tenure months, and value thresholds',
    'component': 'Ux4gSlider',
    'pkg': 'com.ux4g.designsystemv1.components.Ux4gSlider',
    'preview_code': '''Ux4gSlider(
    value = loanAmount,
    onValueChange = { loanAmount = it },
    valueRange = 10000f..500000f,
    steps = 49,
    label = "Loan Amount: ₹${loanAmount.toInt()}"
)''',
    'usage_code': '''import androidx.compose.runtime.*
import com.ux4g.designsystemv1.components.Ux4gSlider

@Composable
fun LoanSliderExample() {
    var value by remember { mutableStateOf(50000f) }

    Ux4gSlider(
        value = value,
        onValueChange = { value = it },
        valueRange = 10000f..200000f,
        label = "Requested Amount"
    )
}''',
    'tables': [
        ('`Ux4gSlider` Props', ['Prop', 'Type', 'Description', 'Default'], [
            [f'`value` {REQ}', '`Float`', 'Current slider float value.', '`—`'],
            [f'`onValueChange` {REQ}', '`(Float) -> Unit`', 'Callback invoked when slider thumb is dragged.', '`—`'],
            ['`modifier`', '`Modifier`', 'Compose layout modifier.', '`Modifier`'],
            ['`valueRange`', '`ClosedFloatingPointRange<Float>`', 'Permitted minimum and maximum bounds.', '`0f..1f`'],
            ['`steps`', '`Int`', 'Number of discrete snap notches between bounds.', '`0`'],
            ['`label`', '`String?`', 'Value summary label above track.', '`null`'],
            ['`enabled`', '`Boolean`', 'Interactive enabled state.', '`true`'],
            ['`thumbColor`', '`Color?`', 'Drag handle thumb color.', '`null`'],
            ['`activeTrackColor`', '`Color?`', 'Progressed track highlight color.', '`null`']
        ])
    ]
}

# 29. status-banner
components['status-banner'] = {
    'title': 'Status Banner',
    'desc': 'Prominent announcement alert bar for draft state, KYC pending, or system maintenance alerts',
    'component': 'Ux4gStatusBanner',
    'pkg': 'com.ux4g.designsystemv1.components.Ux4gStatusBanner',
    'preview_code': '''Ux4gStatusBanner(
    title = "Application in Draft State",
    message = "Your scholarship application is saved as a draft. Please submit before 30 Sep.",
    variant = Ux4gBannerVariant.Warning,
    actionLabel = "Resume Form",
    onActionClick = { /* continue application */ }
)''',
    'usage_code': '''import androidx.compose.runtime.Composable
import com.ux4g.designsystemv1.components.Ux4gStatusBanner
import com.ux4g.designsystemv1.components.Ux4gBannerVariant

@Composable
fun BannerDemo() {
    Ux4gStatusBanner(
        title = "Aadhaar e-KYC Pending",
        message = "Complete your biometrics to activate DBT payment disbursements.",
        variant = Ux4gBannerVariant.Warning
    )
}''',
    'tables': [
        ('`Ux4gStatusBanner` Props', ['Prop', 'Type', 'Description', 'Default'], [
            [f'`title` {REQ}', '`String`', 'Banner headline message.', '`—`'],
            [f'`message` {REQ}', '`String`', 'Detailed explanation text.', '`—`'],
            ['`modifier`', '`Modifier`', 'Compose layout modifier.', '`Modifier`'],
            ['`variant`', '`Ux4gBannerVariant`', 'Status tone: `Info`, `Success`, `Warning`, `Error`.', '`Ux4gBannerVariant.Info`'],
            ['`actionLabel`', '`String?`', 'Optional call-to-action button label.', '`null`'],
            ['`onActionClick`', '`(() -> Unit)?`', 'Callback when action is clicked.', '`null`'],
            ['`onDismiss`', '`(() -> Unit)?`', 'Callback when close cross is tapped.', '`null`']
        ]),
        ('`Ux4gBannerVariant` Enum', ['Variant', 'Tone Color', 'Use Case'], [
            ['`Info`', 'Blue (`#3B82F6`)', 'General informational guidance and system notices'],
            ['`Success`', 'Green (`#22C55E`)', 'Positive confirmations and verified milestone alerts'],
            ['`Warning`', 'Amber (`#F59E0B`)', 'Action required, expiring deadlines, and pending draft states'],
            ['`Error`', 'Red (`#EF4444`)', 'Rejection notices, critical system errors, and authentication failures']
        ])
    ]
}

# 30. status-pipeline
components['status-pipeline'] = {
    'title': 'Status Pipeline',
    'desc': 'Visual horizontal progress pipeline tracking approval checkpoints across departments',
    'component': 'Ux4gStatusPipeline',
    'pkg': 'com.ux4g.designsystemv1.components.Ux4gStatusPipeline',
    'preview_code': '''Ux4gStatusPipeline(
    stages = listOf("Submitted", "Tehsildar Review", "SDM Approval", "Certificate Issued"),
    currentStageIndex = 2
)''',
    'usage_code': '''import androidx.compose.runtime.Composable
import com.ux4g.designsystemv1.components.Ux4gStatusPipeline

@Composable
fun PipelineExample() {
    Ux4gStatusPipeline(
        stages = listOf("Applied", "Verified", "Sanctioned", "Disbursed"),
        currentStageIndex = 1
    )
}''',
    'tables': [
        ('`Ux4gStatusPipeline` Props', ['Prop', 'Type', 'Description', 'Default'], [
            [f'`stages` {REQ}', '`List<String>`', 'List of pipeline approval department stage names.', '`—`'],
            [f'`currentStageIndex` {REQ}', '`Int`', 'Active 0-indexed progress checkpoint pointer.', '`—`'],
            ['`modifier`', '`Modifier`', 'Compose layout modifier.', '`Modifier`']
        ])
    ]
}

# 31. stepper
components['stepper'] = {
    'title': 'Stepper',
    'desc': 'Multi-step form wizard navigation header and capsule stepper for multi-page citizen workflows',
    'component': 'Ux4gStepper',
    'pkg': 'com.ux4g.designsystemv1.components.steppers.Ux4gStepper',
    'preview_code': '''val steps = listOf("Personal Details", "Address Proof", "Income Details", "Bank Account")

Ux4gStepper(
    steps = steps,
    currentStep = currentStepIndex,
    onStepClick = { index -> currentStepIndex = index }
)''',
    'usage_code': '''import androidx.compose.runtime.*
import com.ux4g.designsystemv1.components.steppers.Ux4gStepper

@Composable
fun FormWizardExample() {
    var step by remember { mutableStateOf(0) }

    Ux4gStepper(
        steps = listOf("Identity", "Documents", "Review"),
        currentStep = step,
        onStepClick = { step = it }
    )
}''',
    'tables': [
        ('`Ux4gStepper` Props', ['Prop', 'Type', 'Description', 'Default'], [
            [f'`steps` {REQ}', '`List<String>`', 'List of step title labels in the wizard.', '`—`'],
            [f'`currentStep` {REQ}', '`Int`', 'Active 0-indexed step number.', '`—`'],
            ['`onStepClick`', '`((Int) -> Unit)?`', 'Optional step jump callback handler.', '`null`'],
            ['`modifier`', '`Modifier`', 'Compose layout modifier.', '`Modifier`']
        ]),
        ('`Ux4gCapsuleStepper` Props', ['Prop', 'Type', 'Description', 'Default'], [
            [f'`totalSteps` {REQ}', '`Int`', 'Total number of steps in the sequence.', '`—`'],
            [f'`currentStep` {REQ}', '`Int`', 'Active step index.', '`—`'],
            ['`modifier`', '`Modifier`', 'Compose layout modifier.', '`Modifier`']
        ])
    ]
}

# 32. tag
components['tag'] = {
    'title': 'Tag',
    'desc': 'Small metadata pills for document categories, government schemes, and priority flags',
    'component': 'Ux4gTag',
    'pkg': 'com.ux4g.designsystemv1.components.Ux4gTag',
    'preview_code': '''Ux4gTag(text = "Central Sector Scheme", variant = Ux4gTagVariant.Primary)
Ux4gTag(text = "High Priority", variant = Ux4gTagVariant.Error)
Ux4gTag(text = "Offline Verification", variant = Ux4gTagVariant.Neutral)''',
    'usage_code': '''import androidx.compose.runtime.Composable
import com.ux4g.designsystemv1.components.Ux4gTag
import com.ux4g.designsystemv1.components.Ux4gTagVariant

@Composable
fun TagDemo() {
    Ux4gTag(text = "DBT Active", variant = Ux4gTagVariant.Success)
}''',
    'tables': [
        ('`Ux4gTag` Props', ['Prop', 'Type', 'Description', 'Default'], [
            [f'`text` {REQ}', '`String`', 'Label text rendered inside the tag pill.', '`—`'],
            ['`variant`', '`Ux4gTagVariant`', 'Color tone: `Primary`, `Neutral`, `Success`, `Warning`, `Error`.', '`Ux4gTagVariant.Primary`'],
            ['`modifier`', '`Modifier`', 'Compose layout modifier.', '`Modifier`'],
            ['`leadingIcon`', '`(@Composable () -> Unit)?`', 'Optional leading icon composable slot.', '`null`']
        ])
    ]
}

# 33. text-area
components['text-area'] = {
    'title': 'Text Area',
    'desc': 'Multi-line expanded text box for grievance descriptions, remarks, and residential addresses',
    'component': 'Ux4gTextArea',
    'pkg': 'com.ux4g.designsystemv1.components.textarea.Ux4gTextArea',
    'preview_code': '''Ux4gTextArea(
    value = grievanceDetails,
    onValueChange = { grievanceDetails = it },
    label = "Grievance Description",
    placeholder = "Please provide detailed description of the issue faced (min 50 characters)...",
    maxCharacters = 1000,
    minLines = 4
)''',
    'usage_code': '''import androidx.compose.runtime.*
import com.ux4g.designsystemv1.components.textarea.Ux4gTextArea

@Composable
fun RemarksExample() {
    var remarks by remember { mutableStateOf("") }

    Ux4gTextArea(
        value = remarks,
        onValueChange = { remarks = it },
        label = "Officer Remarks",
        placeholder = "Enter remarks...",
        minLines = 3
    )
}''',
    'tables': [
        ('`Ux4gTextArea` Props', ['Prop', 'Type', 'Description', 'Default'], [
            [f'`value` {REQ}', '`String`', 'Multi-line text content string.', '`—`'],
            [f'`onValueChange` {REQ}', '`(String) -> Unit`', 'Text change event callback.', '`—`'],
            ['`label`', '`String?`', 'Header title above text area.', '`null`'],
            ['`placeholder`', '`String`', 'Placeholder text.', '`""`'],
            ['`maxCharacters`', '`Int?`', 'Character limit counter displayed at bottom-right.', '`null`'],
            ['`minLines`', '`Int`', 'Minimum initial height in text lines.', '`4`'],
            ['`maxLines`', '`Int`', 'Maximum expansion limit before internal scrolling.', '`8`'],
            ['`isError`', '`Boolean`', 'Validation error state styling.', '`false`'],
            ['`errorMessage`', '`String?`', 'Validation error message below input.', '`null`'],
            ['`enabled`', '`Boolean`', 'Interactive enabled state.', '`true`'],
            ['`modifier`', '`Modifier`', 'Compose layout modifier.', '`Modifier`']
        ])
    ]
}

# 34. time-picker
components['time-picker'] = {
    'title': 'Time Picker',
    'desc': 'Analog clock and digital hour/minute selection dialog for appointment scheduling',
    'component': 'Ux4gTimePicker',
    'pkg': 'com.ux4g.designsystemv1.components.Ux4gTimePicker',
    'preview_code': '''Ux4gTimePicker(
    selectedTime = appointmentTime,
    onTimeSelected = { time -> appointmentTime = time },
    label = "Appointment Slot Time",
    is24Hour = false
)''',
    'usage_code': '''import androidx.compose.runtime.*
import java.time.LocalTime
import com.ux4g.designsystemv1.components.Ux4gTimePicker

@Composable
fun TimePickerExample() {
    var time by remember { mutableStateOf<LocalTime?>(null) }

    Ux4gTimePicker(
        label = "Preferred Slot",
        selectedTime = time,
        onTimeSelected = { time = it }
    )
}''',
    'tables': [
        ('`Ux4gTimePicker` Props', ['Prop', 'Type', 'Description', 'Default'], [
            [f'`label` {REQ}', '`String`', 'Field title text.', '`—`'],
            [f'`onTimeSelected` {REQ}', '`(LocalTime) -> Unit`', 'Callback providing chosen `LocalTime`.', '`—`'],
            ['`selectedTime`', '`LocalTime?`', 'Currently selected time value.', '`null`'],
            ['`is24Hour`', '`Boolean`', 'Toggles between 12-hour AM/PM vs 24-hour military clock.', '`false`'],
            ['`enabled`', '`Boolean`', 'Interactive enabled state.', '`true`'],
            ['`modifier`', '`Modifier`', 'Compose layout modifier.', '`Modifier`']
        ])
    ]
}

# 35. time-slot
components['time-slot'] = {
    'title': 'Time Slot',
    'desc': 'Available and booked appointment grid chips for RTO, Passport Seva, and Sub-Registrar slots',
    'component': 'Ux4gTimeSlotGrid',
    'pkg': 'com.ux4g.designsystemv1.components.timeslot.Ux4gTimeSlotGrid',
    'preview_code': '''val slots = listOf(
    TimeSlot(time = "09:30 AM", isAvailable = true),
    TimeSlot(time = "10:00 AM", isAvailable = false),
    TimeSlot(time = "10:30 AM", isAvailable = true)
)

Ux4gTimeSlotGrid(
    slots = slots,
    selectedSlot = selectedSlot,
    onSlotSelected = { slot -> selectedSlot = slot }
)''',
    'usage_code': '''import androidx.compose.runtime.*
import com.ux4g.designsystemv1.components.timeslot.Ux4gTimeSlotGrid
import com.ux4g.designsystemv1.components.timeslot.TimeSlot

@Composable
fun AppointmentGridDemo() {
    var selected by remember { mutableStateOf<TimeSlot?>(null) }

    Ux4gTimeSlotGrid(
        slots = listOf(
            TimeSlot("10:00 AM", isAvailable = true),
            TimeSlot("11:00 AM", isAvailable = true)
        ),
        selectedSlot = selected,
        onSlotSelected = { selected = it }
    )
}''',
    'tables': [
        ('`Ux4gTimeSlotGrid` Props', ['Prop', 'Type', 'Description', 'Default'], [
            [f'`slots` {REQ}', '`List<TimeSlot>`', 'List of available and booked appointment slot objects.', '`—`'],
            [f'`onSlotSelected` {REQ}', '`(TimeSlot) -> Unit`', 'Callback invoked when citizen picks a slot.', '`—`'],
            ['`selectedSlot`', '`TimeSlot?`', 'Currently selected appointment slot.', '`null`'],
            ['`columns`', '`Int`', 'Number of columns in the slot grid.', '`3`'],
            ['`modifier`', '`Modifier`', 'Compose layout modifier.', '`Modifier`']
        ]),
        ('`TimeSlot` Model', ['Prop', 'Type', 'Description', 'Default'], [
            [f'`time` {REQ}', '`String`', 'Slot time label string (e.g. `09:30 AM`).', '`—`'],
            ['`isAvailable`', '`Boolean`', 'Whether the slot is open for booking or disabled/booked.', '`true`'],
            ['`badge`', '`String?`', 'Optional badge text (e.g. `Fast Filling`, `Premium`).', '`null`']
        ])
    ]
}

# 36. toast
components['toast'] = {
    'title': 'Toast & Snackbars',
    'desc': 'Transient feedback messages with category icons, title, subtitle, and top/bottom positioning',
    'component': 'Ux4gToast',
    'pkg': 'com.ux4g.designsystemv1.components.Ux4gToast',
    'preview_code': '''// Show Toast Notification
val toastHostState = remember { Ux4gToastHostState() }

toastHostState.showToast(
    category = Ux4gToastCategory.Success,
    title = "Application Submitted Successfully",
    subtitle = "Reference ID: GOV-2024-89412 has been sent via SMS."
)''',
    'usage_code': '''import androidx.compose.runtime.*
import com.ux4g.designsystemv1.components.Ux4gToastHost
import com.ux4g.designsystemv1.components.Ux4gToastHostState
import com.ux4g.designsystemv1.components.Ux4gToastCategory

@Composable
fun ToastDemo() {
    val toastState = remember { Ux4gToastHostState() }
    Ux4gToastHost(hostState = toastState)
}''',
    'tables': [
        ('`Ux4gToastHostState.showToast` Parameters', ['Prop', 'Type', 'Description', 'Default'], [
            [f'`title` {REQ}', '`String`', 'Primary notification bold heading text.', '`—`'],
            ['`subtitle`', '`String?`', 'Secondary description line.', '`null`'],
            ['`category`', '`Ux4gToastCategory`', 'Category tone: `Success`, `Error`, `Warning`, `Info`.', '`Ux4gToastCategory.Info`'],
            ['`duration`', '`Ux4gToastDuration`', 'Display duration: `Short` (3s), `Long` (5s), `Indefinite`.', '`Ux4gToastDuration.Short`']
        ]),
        ('`Ux4gToastCategory` Enum', ['Category', 'Color Token', 'Icon Description'], [
            ['`Success`', 'Green (`#22C55E`)', 'Checkmark in circle — for completed tasks & submissions'],
            ['`Error`', 'Red (`#EF4444`)', 'Cross in circle — for API failure & auth errors'],
            ['`Warning`', 'Amber (`#F59E0B`)', 'Alert triangle — for warnings & connectivity interruptions'],
            ['`Info`', 'Blue (`#3B82F6`)', 'Info circle — for background sync updates & notifications']
        ])
    ]
}

# 37. toggle
components['toggle'] = {
    'title': 'Toggle Switch',
    'desc': 'Binary on/off switch for SMS alerts, biometric login, and system preference controls',
    'component': 'Ux4gToggle',
    'pkg': 'com.ux4g.designsystemv1.components.Ux4gToggle',
    'preview_code': '''Ux4gToggle(
    checked = isSmsAlertsEnabled,
    onCheckedChange = { isSmsAlertsEnabled = it },
    label = "Receive SMS status updates on registered mobile number"
)''',
    'usage_code': '''import androidx.compose.runtime.*
import com.ux4g.designsystemv1.components.Ux4gToggle

@Composable
fun ToggleDemo() {
    var enabled by remember { mutableStateOf(true) }

    Ux4gToggle(
        checked = enabled,
        onCheckedChange = { enabled = it },
        label = "Enable Biometric Login"
    )
}''',
    'tables': [
        ('`Ux4gToggle` Props', ['Prop', 'Type', 'Description', 'Default'], [
            [f'`checked` {REQ}', '`Boolean`', 'Current boolean switch state.', '`—`'],
            [f'`onCheckedChange` {REQ}', '`(Boolean) -> Unit`', 'Callback triggered when switch is toggled.', '`—`'],
            ['`label`', '`String?`', 'Accompanying text label adjacent to switch.', '`null`'],
            ['`enabled`', '`Boolean`', 'Whether toggle is interactive or grayed out.', '`true`'],
            ['`modifier`', '`Modifier`', 'Compose layout modifier.', '`Modifier`'],
            ['`checkedThumbColor`', '`Color?`', 'Thumb ball color when active.', '`null`'],
            ['`checkedTrackColor`', '`Color?`', 'Track bar background color when active.', '`null`']
        ])
    ]
}

# 38. tooltip
components['tooltip'] = {
    'title': 'Tooltip',
    'desc': 'Contextual helper bubble explaining technical terms, mandatory requirements, and field guidance tips',
    'component': 'Ux4gTooltip',
    'pkg': 'com.ux4g.designsystemv1.components.tooltip.Ux4gTooltip',
    'preview_code': '''Ux4gTooltip(
    tooltipText = "Enter the 12-digit number printed on your Aadhaar card or e-Aadhaar PDF."
) {
    Icon(
        imageVector = Icons.Outlined.Info,
        contentDescription = "Help information",
        tint = Ux4gTheme.colorScheme.primary
    )
}''',
    'usage_code': '''import androidx.compose.runtime.Composable
import androidx.compose.material3.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import com.ux4g.designsystemv1.components.tooltip.Ux4gTooltip
import com.ux4g.designsystemv1.theme.Ux4gTheme

@Composable
fun TooltipExample() {
    Ux4gTooltip(tooltipText = "Mandatory document for direct benefit bank transfer") {
        Icon(Icons.Outlined.Info, contentDescription = "Help", tint = Ux4gTheme.colorScheme.primary)
    }
}''',
    'tables': [
        ('`Ux4gTooltip` Props', ['Prop', 'Type', 'Description', 'Default'], [
            [f'`tooltipText` {REQ}', '`String`', 'Guidance explanation text displayed inside the tooltip bubble.', '`—`'],
            [f'`content` {REQ}', '`@Composable () -> Unit`', 'Anchor composable trigger that opens the tooltip on tap or hover.', '`—`'],
            ['`modifier`', '`Modifier`', 'Compose layout modifier.', '`Modifier`'],
            ['`backgroundColor`', '`Color`', 'Tooltip bubble background container color.', '`Ux4gTheme.colorScheme.onSurface`'],
            ['`textColor`', '`Color`', 'Tooltip text font color.', '`Ux4gTheme.colorScheme.surface`']
        ])
    ]
}

for name, data in components.items():
    file_path = os.path.join(docs_dir, f"{name}.mdx")
    
    sections = []
    for title, headers, rows in data['tables']:
        table_md = create_table(headers, rows)
        if len(data['tables']) > 1:
            sections.append(f"### {title}\n\n{table_md}")
        else:
            sections.append(table_md)
            
    tables_content = "\n\n---\n\n".join(sections)
    
    content = f"""---
title: {data['title']}
description: {data['desc']}
---

# {data['title']} (`{data['component']}`)

{data['desc']}.

Import from `{data['pkg']}`.

---

<h2><MIcon name="smartphone" /> Interactive Preview</h2>

<ComponentPreview
  title="{data['component']}"
  category="Jetpack Compose"
  code={{`{data['preview_code']}`}}
>
  <div className="flex flex-col items-center justify-center py-2">
    <img
      src="/images/components/{name}_preview.png"
      alt="UX4G {data['title']} Preview"
      className="max-w-full rounded-xl border border-border/60 shadow-sm"
    />
  </div>
</ComponentPreview>

---

<h2><MIcon name="code" /> Kotlin Usage</h2>

```kotlin title="Compose Example"
{data['usage_code']}
```

---

<h2><MIcon name="settings" /> Parameters</h2>

{tables_content}
"""
    with open(file_path, "w", encoding="utf-8") as f:
        f.write(content)
    print(f"Updated {name}.mdx successfully")

print("All components updated successfully!")
