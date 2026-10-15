package com.ux4g.designsystemv1.components

import android.content.res.Configuration
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.ux4g.designsystemv1.components.buttons.Ux4gButton
import com.ux4g.designsystemv1.components.buttons.Ux4gButtonSize
import com.ux4g.designsystemv1.components.buttons.Ux4gButtonVariant
import com.ux4g.designsystemv1.foundation.*
import com.ux4g.designsystemv1.theme.Ux4gTheme
import kotlin.math.min

// ─────────────────────────────────────────────────────────────────────────────
// ENUMS & MODELS
// ─────────────────────────────────────────────────────────────────────────────

/** Position of the side menu / drawer: Left or Right. */
enum class Ux4gSideMenuPosition { Left, Right }

/** Header style preset rendered at the top of the side menu. */
enum class Ux4gSideMenuHeaderType {
    /** Standard government/department header: Emblem/logo, Title, Subtitle, and Close button. */
    Standard,

    /** Compact horizontal user profile header: Initials avatar, Name, Subtitle, and Close button. */
    CompactUser,

    /** Prominent centered profile header: Large avatar, Name, Role, Metadata, "View profile" button, and Close button. */
    ProminentUser,

    /** Plain title/subtitle header (e.g. for Department Switcher). */
    Plain,

    /** Custom header composable provided by developer. */
    Custom
}

/**
 * Model representing a single item (or parent with nested children) in [Ux4gSideMenu].
 */
data class Ux4gSideMenuItem(
    val key: String,
    val label: String,
    val icon: Ux4gIcon? = null,
    val iconVector: ImageVector? = null,
    val iconComposable: (@Composable () -> Unit)? = null,
    val subtitle: String? = null,
    val badge: Any? = null,
    val badgeColor: Color? = null,
    val badgeTextColor: Color? = null,
    val tag: String? = null,
    val tagColor: Color? = null,
    val tagTextColor: Color? = null,
    val avatarInitials: String? = null,
    val avatarColor: Color? = null,
    val avatarTextColor: Color? = null,
    val trailing: (@Composable () -> Unit)? = null,
    val disabled: Boolean = false,
    val onTap: (() -> Unit)? = null,
    val children: List<Ux4gSideMenuItem>? = null,
    val defaultExpanded: Boolean = false,
    val isCard: Boolean = false,
    val semanticLabel: String? = null,
    val customContent: (@Composable () -> Unit)? = null
)

/**
 * Model representing a group/section of items in [Ux4gSideMenu].
 */
data class Ux4gSideMenuSection(
    val key: String? = null,
    val title: String? = null,
    val items: List<Ux4gSideMenuItem>,
    val actionText: String? = null,
    val onActionTap: (() -> Unit)? = null,
    val customHeader: (@Composable () -> Unit)? = null,
    val customFooter: (@Composable () -> Unit)? = null
)

/**
 * Model representing an action-required callout card inside the side menu.
 */
data class Ux4gSideMenuActionCard(
    val tag: String = "ACTION REQUIRED",
    val title: String,
    val description: String,
    val buttonText: String = "Verify now",
    val onButtonPressed: (() -> Unit)? = null,
    val backgroundColor: Color? = null,
    val borderColor: Color? = null,
    val customContent: (@Composable () -> Unit)? = null
)

/**
 * Model representing the bottom branding block (e.g. Emblem + Department Name + Division).
 */
data class Ux4gSideMenuBottomBranding(
    val logo: (@Composable () -> Unit)? = null,
    val title: String,
    val subtitle: String? = null,
    val onTap: (() -> Unit)? = null
)

// ─────────────────────────────────────────────────────────────────────────────
// MAIN COMPOSABLE
// ─────────────────────────────────────────────────────────────────────────────

/**
 * **Ux4gSideMenu** (also aliased as **Ux4gDrawer**)
 *
 * Fully configurable, accessible, and responsive navigation drawer / side menu component
 * built strictly to UX4G Design System guidelines.
 *
 * ### Features:
 * - 6 Rich Visual Presets & custom configurations:
 *   1. Standard Government Navigation (Emblem, Search, Accordion Tree with connector line, Badges, Footer Sign out)
 *   2. Officer Inbox (User header, Mailbox, Folders, Action link, Inbox settings, Bottom branding)
 *   3. Citizen Profile Menu (Prominent centered avatar, Role, Status metadata, Outlined action button, App sections)
 *   4. Department Switcher (Department card tiles with avatar initials, checkmark, and pending tags)
 *   5. Action Required Mode (Action Required card with primary CTA button)
 *   6. Citizen Simple Navigation
 * - Collapsible Sub-menu Navigation Tree with vertical connecting line
 * - Integrated Search bar with real-time auto-filtering across parents and children
 * - Full Dark Mode & Light Mode support via UX4G Theme
 */
@Composable
fun Ux4gSideMenu(
    modifier: Modifier = Modifier,
    onClose: (() -> Unit)? = null,
    position: Ux4gSideMenuPosition = Ux4gSideMenuPosition.Left,
    width: Dp? = null,
    // ── Header Configuration ──
    headerType: Ux4gSideMenuHeaderType = Ux4gSideMenuHeaderType.Standard,
    title: String = "Revenue Department",
    subtitle: String? = "Government of India",
    logo: (@Composable () -> Unit)? = null,
    avatarInitials: String? = null,
    avatarColor: Color? = null,
    avatarTextColor: Color? = null,
    userMetaText: String? = null,
    userActionButtonText: String? = "View profile",
    onUserActionButtonPressed: (() -> Unit)? = null,
    showCloseButton: Boolean = true,
    customHeader: (@Composable () -> Unit)? = null,
    showHeaderDivider: Boolean = true,
    // ── Search Configuration ──
    showSearch: Boolean = true,
    searchPlaceholder: String = "Search for...",
    searchQuery: String? = null,
    onSearchChanged: ((String) -> Unit)? = null,
    autoFilterOnSearch: Boolean = true,
    // ── Sections & Navigation Items ──
    sections: List<Ux4gSideMenuSection>? = null,
    items: List<Ux4gSideMenuItem>? = null,
    selectedKey: String? = null,
    defaultSelectedKey: String? = "dashboard",
    onItemSelected: ((Ux4gSideMenuItem) -> Unit)? = null,
    // ── Action Banner / Card ──
    actionCard: Ux4gSideMenuActionCard? = null,
    // ── Footer Configuration ──
    footerItems: List<Ux4gSideMenuItem>? = null,
    showSignOut: Boolean = true,
    signOutLabel: String = "Sign out",
    onSignOut: (() -> Unit)? = null,
    bottomBranding: Ux4gSideMenuBottomBranding? = null,
    customFooter: (@Composable () -> Unit)? = null,
    showFooterDivider: Boolean = true,
    // ── Appearance & Theme ──
    backgroundColor: Color? = null,
    activeItemBackgroundColor: Color? = null,
    activeItemColor: Color? = null,
    inactiveItemColor: Color? = null,
    borderColor: Color? = null,
    elevation: Dp = 16.dp,
    contentPadding: PaddingValues = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
) {
    val isDark = isSystemInDarkTheme()
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val resolvedWidth = width ?: (screenWidth * 0.82f).coerceAtMost(340.dp)

    // Resolved colors matching Flutter UX4G design tokens
    val resolvedBg = backgroundColor ?: (if (isDark) Ux4gColors.neutral900 else Ux4gTheme.colorScheme.surface)
    val resolvedBorder = borderColor ?: (if (isDark) Ux4gColors.neutral700 else Ux4gColors.neutral200)
    val resolvedActiveBg = activeItemBackgroundColor ?: (if (isDark) Ux4gColors.primary900.copy(alpha = 0.35f) else Ux4gColors.neutral100)
    val resolvedActiveFg = activeItemColor ?: (if (isDark) Ux4gColors.primary300 else Ux4gColors.neutral1000black)
    val resolvedInactiveFg = inactiveItemColor ?: (if (isDark) Ux4gColors.neutral400 else Ux4gColors.neutral800)
    val resolvedSectionTitleColor = if (isDark) Ux4gColors.neutral500 else Ux4gColors.neutral600

    var internalSearchQuery by remember(searchQuery) { mutableStateOf(searchQuery ?: "") }
    var internalSelectedKey by remember(selectedKey) { mutableStateOf(selectedKey ?: defaultSelectedKey ?: "dashboard") }
    val expandedKeys = remember { mutableStateMapOf<String, Boolean>() }

    // Resolve active sections (defaults if none provided)
    val resolvedSections = remember(sections, items) {
        when {
            sections != null && sections.isNotEmpty() -> sections
            items != null && items.isNotEmpty() -> listOf(Ux4gSideMenuSection(items = items))
            else -> defaultSections()
        }
    }

    // Initialize default expanded state for accordion parents
    LaunchedEffect(resolvedSections) {
        resolvedSections.forEach { section ->
            section.items.forEach { item ->
                if (!item.children.isNullOrEmpty()) {
                    if (item.defaultExpanded || item.key == "applications") {
                        expandedKeys[item.key] = true
                    }
                }
            }
        }
    }

    // Filter items based on real-time search
    val effectiveSections = remember(resolvedSections, internalSearchQuery, autoFilterOnSearch) {
        if (!autoFilterOnSearch || internalSearchQuery.trim().isEmpty()) {
            resolvedSections
        } else {
            val q = internalSearchQuery.lowercase().trim()
            resolvedSections.mapNotNull { section ->
                val matchingItems = section.items.mapNotNull { item ->
                    val matchesParent = item.label.lowercase().contains(q) || (item.subtitle?.lowercase()?.contains(q) == true)
                    val matchingChildren = item.children?.filter { child ->
                        child.label.lowercase().contains(q) || (child.subtitle?.lowercase()?.contains(q) == true)
                    }
                    when {
                        matchesParent -> item
                        !matchingChildren.isNullOrEmpty() -> item.copy(children = matchingChildren)
                        else -> null
                    }
                }
                if (matchingItems.isNotEmpty()) section.copy(items = matchingItems) else null
            }
        }
    }

    // Drawer Surface Container
    Column(
        modifier = modifier
            .width(resolvedWidth)
            .fillMaxHeight()
            .shadow(elevation)
            .background(resolvedBg)
            .border(
                width = 0.5.dp,
                color = resolvedBorder,
                shape = RoundedCornerShape(0.dp)
            )
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        // ── 1. HEADER ────────────────────────────────────────────────────────
        if (customHeader != null) {
            customHeader()
        } else {
            Column(modifier = Modifier.fillMaxWidth()) {
                when (headerType) {
                    Ux4gSideMenuHeaderType.CompactUser -> {
                        CompactUserHeader(
                            title = title,
                            subtitle = subtitle,
                            avatarInitials = avatarInitials,
                            avatarColor = avatarColor,
                            avatarTextColor = avatarTextColor,
                            activeFg = resolvedActiveFg,
                            subtitleColor = resolvedSectionTitleColor,
                            showCloseButton = showCloseButton,
                            onClose = onClose,
                            isDark = isDark
                        )
                    }
                    Ux4gSideMenuHeaderType.ProminentUser -> {
                        ProminentUserHeader(
                            title = title,
                            subtitle = subtitle,
                            avatarInitials = avatarInitials,
                            avatarColor = avatarColor,
                            avatarTextColor = avatarTextColor,
                            userMetaText = userMetaText,
                            userActionButtonText = userActionButtonText,
                            onUserActionButtonPressed = onUserActionButtonPressed,
                            activeFg = resolvedActiveFg,
                            subtitleColor = resolvedSectionTitleColor,
                            showCloseButton = showCloseButton,
                            onClose = onClose,
                            isDark = isDark
                        )
                    }
                    Ux4gSideMenuHeaderType.Plain -> {
                        PlainHeader(
                            title = title,
                            subtitle = subtitle,
                            activeFg = resolvedActiveFg,
                            subtitleColor = resolvedSectionTitleColor,
                            showCloseButton = showCloseButton,
                            onClose = onClose
                        )
                    }
                    Ux4gSideMenuHeaderType.Standard,
                    Ux4gSideMenuHeaderType.Custom -> {
                        StandardGovHeader(
                            title = title,
                            subtitle = subtitle,
                            logo = logo,
                            activeFg = resolvedActiveFg,
                            subtitleColor = resolvedSectionTitleColor,
                            showCloseButton = showCloseButton,
                            onClose = onClose,
                            isDark = isDark
                        )
                    }
                }

                if (showHeaderDivider) {
                    Ux4gDivider(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        color = resolvedBorder
                    )
                }
            }
        }

        // ── 2. SEARCH BAR ────────────────────────────────────────────────────
        if (showSearch) {
            SideMenuSearchBar(
                query = internalSearchQuery,
                onQueryChange = { newQuery ->
                    internalSearchQuery = newQuery
                    onSearchChanged?.invoke(newQuery)
                },
                placeholder = searchPlaceholder,
                borderColor = resolvedBorder,
                isDark = isDark
            )
        }

        // ── 3. SCROLLABLE BODY ───────────────────────────────────────────────
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(contentPadding)
        ) {
            if (effectiveSections.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No matching options found",
                        style = Ux4gTheme.typography.bS_default,
                        color = resolvedSectionTitleColor
                    )
                }
            } else {
                effectiveSections.forEach { section ->
                    SideMenuSectionBlock(
                        section = section,
                        selectedKey = selectedKey ?: internalSelectedKey,
                        expandedKeys = expandedKeys,
                        activeBg = resolvedActiveBg,
                        activeFg = resolvedActiveFg,
                        inactiveFg = resolvedInactiveFg,
                        sectionTitleColor = resolvedSectionTitleColor,
                        borderColor = resolvedBorder,
                        isDark = isDark,
                        onItemClick = { clickedItem ->
                            if (!clickedItem.disabled) {
                                if (!clickedItem.children.isNullOrEmpty()) {
                                    expandedKeys[clickedItem.key] = !(expandedKeys[clickedItem.key] ?: false)
                                } else {
                                    internalSelectedKey = clickedItem.key
                                    clickedItem.onTap?.invoke()
                                    onItemSelected?.invoke(clickedItem)
                                }
                            }
                        }
                    )
                }
            }

            // Optional Action Required Callout Card
            if (actionCard != null) {
                Spacer(modifier = Modifier.height(12.dp))
                ActionRequiredCard(card = actionCard, isDark = isDark)
            }
        }

        // ── 4. FOOTER / SIGN OUT / BRANDING ──────────────────────────────────
        if (customFooter != null) {
            customFooter()
        } else {
            val hasFooterItems = !footerItems.isNullOrEmpty()
            val hasSignOut = showSignOut
            val hasBranding = bottomBranding != null

            if (hasFooterItems || hasSignOut || hasBranding) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    if (showFooterDivider) {
                        Ux4gDivider(
                            modifier = Modifier.padding(horizontal = 16.dp),
                            color = resolvedBorder
                        )
                    }

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        // Extra Footer Items
                        footerItems?.forEach { item ->
                            SideMenuItemTile(
                                item = item,
                                isSelected = (selectedKey ?: internalSelectedKey) == item.key,
                                activeBg = resolvedActiveBg,
                                activeFg = resolvedActiveFg,
                                inactiveFg = resolvedInactiveFg,
                                isDark = isDark,
                                onClick = {
                                    if (!item.disabled) {
                                        internalSelectedKey = item.key
                                        item.onTap?.invoke()
                                        onItemSelected?.invoke(item)
                                    }
                                }
                            )
                        }

                        // Default Sign out button
                        if (hasSignOut) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(42.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .clickable {
                                        onSignOut?.invoke() ?: onClose?.invoke()
                                    }
                                    .padding(horizontal = 10.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.Logout,
                                    contentDescription = "Sign out",
                                    tint = resolvedInactiveFg,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                Text(
                                    text = signOutLabel,
                                    style = TextStyle(
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = resolvedInactiveFg
                                    )
                                )
                            }
                        }

                        // Bottom Branding
                        if (bottomBranding != null) {
                            Spacer(modifier = Modifier.height(6.dp))
                            BottomBrandingBlock(
                                branding = bottomBranding,
                                activeFg = resolvedActiveFg,
                                subtitleColor = resolvedSectionTitleColor
                            )
                        }
                    }
                }
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// ANIMATED MODAL DRAWER LAUNCHER
// ─────────────────────────────────────────────────────────────────────────────

/**
 * Animated slide-out Modal Side Menu with dark backdrop scrim.
 * Matches Flutter's `Ux4gSideMenu.show(context, ...)` behavior.
 */
@Composable
fun Ux4gSideMenuModal(
    isOpen: Boolean,
    onDismissRequest: () -> Unit,
    position: Ux4gSideMenuPosition = Ux4gSideMenuPosition.Left,
    width: Dp? = null,
    // Header
    headerType: Ux4gSideMenuHeaderType = Ux4gSideMenuHeaderType.Standard,
    title: String = "Revenue Department",
    subtitle: String? = "Government of India",
    logo: (@Composable () -> Unit)? = null,
    avatarInitials: String? = null,
    avatarColor: Color? = null,
    avatarTextColor: Color? = null,
    userMetaText: String? = null,
    userActionButtonText: String? = "View profile",
    onUserActionButtonPressed: (() -> Unit)? = null,
    showCloseButton: Boolean = true,
    customHeader: (@Composable () -> Unit)? = null,
    showHeaderDivider: Boolean = true,
    // Search
    showSearch: Boolean = true,
    searchPlaceholder: String = "Search for...",
    searchQuery: String? = null,
    onSearchChanged: ((String) -> Unit)? = null,
    autoFilterOnSearch: Boolean = true,
    // Items
    sections: List<Ux4gSideMenuSection>? = null,
    items: List<Ux4gSideMenuItem>? = null,
    selectedKey: String? = null,
    defaultSelectedKey: String? = "dashboard",
    onItemSelected: ((Ux4gSideMenuItem) -> Unit)? = null,
    // Cards & Footer
    actionCard: Ux4gSideMenuActionCard? = null,
    footerItems: List<Ux4gSideMenuItem>? = null,
    showSignOut: Boolean = true,
    signOutLabel: String = "Sign out",
    onSignOut: (() -> Unit)? = null,
    bottomBranding: Ux4gSideMenuBottomBranding? = null,
    customFooter: (@Composable () -> Unit)? = null,
    showFooterDivider: Boolean = true,
    // Theme
    backgroundColor: Color? = null,
    scrimColor: Color = Color(0x80000000)
) {
    if (isOpen) {
        Dialog(
            onDismissRequest = onDismissRequest,
            properties = DialogProperties(
                usePlatformDefaultWidth = false,
                decorFitsSystemWindows = false
            )
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(scrimColor)
                    .clickable(onClick = onDismissRequest)
            ) {
                val slideOffset = if (position == Ux4gSideMenuPosition.Left) -1 else 1

                AnimatedVisibility(
                    visible = true,
                    enter = slideInHorizontally(
                        initialOffsetX = { fullWidth -> fullWidth * slideOffset },
                        animationSpec = tween(durationMillis = 280)
                    ) + fadeIn(animationSpec = tween(280)),
                    exit = slideOutHorizontally(
                        targetOffsetX = { fullWidth -> fullWidth * slideOffset },
                        animationSpec = tween(durationMillis = 280)
                    ) + fadeOut(animationSpec = tween(280)),
                    modifier = Modifier.align(
                        if (position == Ux4gSideMenuPosition.Left) Alignment.CenterStart else Alignment.CenterEnd
                    )
                ) {
                    Box(modifier = Modifier.clickable(enabled = false) { /* Absorb clicks inside drawer */ }) {
                        Ux4gSideMenu(
                            onClose = onDismissRequest,
                            position = position,
                            width = width,
                            headerType = headerType,
                            title = title,
                            subtitle = subtitle,
                            logo = logo,
                            avatarInitials = avatarInitials,
                            avatarColor = avatarColor,
                            avatarTextColor = avatarTextColor,
                            userMetaText = userMetaText,
                            userActionButtonText = userActionButtonText,
                            onUserActionButtonPressed = onUserActionButtonPressed,
                            showCloseButton = showCloseButton,
                            customHeader = customHeader,
                            showHeaderDivider = showHeaderDivider,
                            showSearch = showSearch,
                            searchPlaceholder = searchPlaceholder,
                            searchQuery = searchQuery,
                            onSearchChanged = onSearchChanged,
                            autoFilterOnSearch = autoFilterOnSearch,
                            sections = sections,
                            items = items,
                            selectedKey = selectedKey,
                            defaultSelectedKey = defaultSelectedKey,
                            onItemSelected = { item ->
                                onItemSelected?.invoke(item)
                                onDismissRequest()
                            },
                            actionCard = actionCard,
                            footerItems = footerItems,
                            showSignOut = showSignOut,
                            signOutLabel = signOutLabel,
                            onSignOut = {
                                onSignOut?.invoke()
                                onDismissRequest()
                            },
                            bottomBranding = bottomBranding,
                            customFooter = customFooter,
                            showFooterDivider = showFooterDivider,
                            backgroundColor = backgroundColor
                        )
                    }
                }
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// HEADER COMPOSABLES
// ─────────────────────────────────────────────────────────────────────────────

@Composable
private fun StandardGovHeader(
    title: String,
    subtitle: String?,
    logo: (@Composable () -> Unit)?,
    activeFg: Color,
    subtitleColor: Color,
    showCloseButton: Boolean,
    onClose: (() -> Unit)?,
    isDark: Boolean
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, top = 14.dp, end = 12.dp, bottom = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Logo / Emblem
        if (logo != null) {
            Box(modifier = Modifier.size(32.dp), contentAlignment = Alignment.Center) {
                logo()
            }
        } else {
            Ux4gIconComponent(
                icon = Ux4gIcons.NationalEmblem,
                contentDescription = "National Emblem",
                modifier = Modifier.size(32.dp),
                tint = activeFg
            )
        }
        Spacer(modifier = Modifier.width(12.dp))

        // Titles
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = TextStyle(
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = activeFg,
                    letterSpacing = (-0.2).sp
                )
            )
            if (subtitle != null) {
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = subtitle,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = TextStyle(
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Normal,
                        color = subtitleColor
                    )
                )
            }
        }

        // Close button
        if (showCloseButton && onClose != null) {
            IconButton(
                onClick = onClose,
                modifier = Modifier.size(32.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.Close,
                    contentDescription = "Close drawer",
                    tint = activeFg,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

@Composable
private fun CompactUserHeader(
    title: String,
    subtitle: String?,
    avatarInitials: String?,
    avatarColor: Color?,
    avatarTextColor: Color?,
    activeFg: Color,
    subtitleColor: Color,
    showCloseButton: Boolean,
    onClose: (() -> Unit)?,
    isDark: Boolean
) {
    val initials = avatarInitials ?: title.split(" ")
        .mapNotNull { it.firstOrNull()?.toString() }
        .take(2)
        .joinToString("")
        .ifEmpty { "U" }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, top = 14.dp, end = 12.dp, bottom = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Initials Avatar
        Box(
            modifier = Modifier
                .size(38.dp)
                .clip(CircleShape)
                .background(avatarColor ?: if (isDark) Ux4gColors.primary900 else Ux4gColors.primary100),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = initials,
                style = TextStyle(
                    color = avatarTextColor ?: if (isDark) Ux4gColors.primary300 else Ux4gColors.primary700,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
            )
        }
        Spacer(modifier = Modifier.width(12.dp))

        // Name and Email / Role
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = TextStyle(
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = activeFg
                )
            )
            if (subtitle != null) {
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = subtitle,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = TextStyle(
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Normal,
                        color = subtitleColor
                    )
                )
            }
        }

        if (showCloseButton && onClose != null) {
            IconButton(onClick = onClose, modifier = Modifier.size(32.dp)) {
                Icon(
                    imageVector = Icons.Filled.Close,
                    contentDescription = "Close",
                    tint = activeFg,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

@Composable
private fun ProminentUserHeader(
    title: String,
    subtitle: String?,
    avatarInitials: String?,
    avatarColor: Color?,
    avatarTextColor: Color?,
    userMetaText: String?,
    userActionButtonText: String?,
    onUserActionButtonPressed: (() -> Unit)?,
    activeFg: Color,
    subtitleColor: Color,
    showCloseButton: Boolean,
    onClose: (() -> Unit)?,
    isDark: Boolean
) {
    val initials = avatarInitials ?: title.split(" ")
        .mapNotNull { it.firstOrNull()?.toString() }
        .take(2)
        .joinToString("")
        .ifEmpty { "U" }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, top = 16.dp, end = 16.dp, bottom = 14.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Top Close Button row
        if (showCloseButton && onClose != null) {
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.TopEnd) {
                IconButton(onClick = onClose, modifier = Modifier.size(28.dp)) {
                    Icon(
                        imageVector = Icons.Filled.Close,
                        contentDescription = "Close",
                        tint = activeFg,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }

        // Large Centered Avatar
        Box(
            modifier = Modifier
                .size(56.dp)
                .clip(CircleShape)
                .background(avatarColor ?: if (isDark) Ux4gColors.primary900 else Ux4gColors.primary200),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = initials,
                style = TextStyle(
                    color = avatarTextColor ?: if (isDark) Ux4gColors.primary300 else Ux4gColors.primary800,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            )
        }
        Spacer(modifier = Modifier.height(10.dp))

        // Name
        Text(
            text = title,
            textAlign = TextAlign.Center,
            style = TextStyle(
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = activeFg
            )
        )

        // Subtitle / Role (e.g. "Citizen")
        if (subtitle != null) {
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = subtitle,
                textAlign = TextAlign.Center,
                style = TextStyle(
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Normal,
                    color = subtitleColor
                )
            )
        }

        // User Metadata
        if (userMetaText != null) {
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = userMetaText,
                textAlign = TextAlign.Center,
                style = TextStyle(
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Normal,
                    color = subtitleColor
                )
            )
        }

        // Outlined Action Button (e.g. "View profile")
        if (userActionButtonText != null) {
            Spacer(modifier = Modifier.height(12.dp))
            OutlinedButton(
                onClick = { onUserActionButtonPressed?.invoke() ?: onClose?.invoke() },
                shape = RoundedCornerShape(8.dp),
                border = androidx.compose.foundation.BorderStroke(
                    width = 1.dp,
                    color = if (isDark) Ux4gColors.neutral700 else Ux4gColors.neutral300
                ),
                contentPadding = PaddingValues(horizontal = 20.dp, vertical = 6.dp),
                modifier = Modifier.height(36.dp)
            ) {
                Text(
                    text = userActionButtonText,
                    style = TextStyle(
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = activeFg
                    )
                )
            }
        }
    }
}

@Composable
private fun PlainHeader(
    title: String,
    subtitle: String?,
    activeFg: Color,
    subtitleColor: Color,
    showCloseButton: Boolean,
    onClose: (() -> Unit)?
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, top = 16.dp, end = 12.dp, bottom = 12.dp),
        verticalAlignment = Alignment.Top
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = TextStyle(
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = activeFg
                )
            )
            if (subtitle != null) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = subtitle,
                    style = TextStyle(
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Normal,
                        color = subtitleColor
                    )
                )
            }
        }
        if (showCloseButton && onClose != null) {
            IconButton(onClick = onClose, modifier = Modifier.size(32.dp)) {
                Icon(
                    imageVector = Icons.Filled.Close,
                    contentDescription = "Close",
                    tint = activeFg,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// SEARCH BAR
// ─────────────────────────────────────────────────────────────────────────────

@Composable
private fun SideMenuSearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    placeholder: String,
    borderColor: Color,
    isDark: Boolean
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, top = 12.dp, end = 16.dp, bottom = 8.dp)
            .height(38.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(if (isDark) Ux4gColors.neutral800 else Ux4gColors.white)
            .border(1.dp, borderColor, RoundedCornerShape(8.dp))
            .padding(horizontal = 10.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Filled.Search,
                contentDescription = "Search",
                tint = if (isDark) Ux4gColors.neutral400 else Ux4gColors.neutral500,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))

            Box(modifier = Modifier.weight(1f)) {
                if (query.isEmpty()) {
                    Text(
                        text = placeholder,
                        style = TextStyle(
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Normal,
                            color = if (isDark) Ux4gColors.neutral400 else Ux4gColors.neutral500
                        )
                    )
                }
                BasicTextField(
                    value = query,
                    onValueChange = onQueryChange,
                    singleLine = true,
                    textStyle = TextStyle(
                        fontSize = 13.sp,
                        color = if (isDark) Ux4gColors.white else Ux4gColors.neutral1000black
                    ),
                    cursorBrush = SolidColor(if (isDark) Ux4gColors.primary300 else Ux4gColors.primary600),
                    modifier = Modifier.fillMaxWidth()
                )
            }

            if (query.isNotEmpty()) {
                Icon(
                    imageVector = Icons.Filled.Close,
                    contentDescription = "Clear search",
                    tint = if (isDark) Ux4gColors.neutral400 else Ux4gColors.neutral500,
                    modifier = Modifier
                        .size(16.dp)
                        .clip(CircleShape)
                        .clickable { onQueryChange("") }
                )
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// SECTION & MENU ITEMS
// ─────────────────────────────────────────────────────────────────────────────

@Composable
private fun SideMenuSectionBlock(
    section: Ux4gSideMenuSection,
    selectedKey: String,
    expandedKeys: Map<String, Boolean>,
    activeBg: Color,
    activeFg: Color,
    inactiveFg: Color,
    sectionTitleColor: Color,
    borderColor: Color,
    isDark: Boolean,
    onItemClick: (Ux4gSideMenuItem) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 14.dp)
    ) {
        // Section Title
        if (section.title != null) {
            Text(
                text = section.title,
                style = TextStyle(
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = sectionTitleColor,
                    letterSpacing = 0.8.sp
                ),
                modifier = Modifier.padding(start = 4.dp, bottom = 6.dp, top = 4.dp)
            )
        }

        section.customHeader?.invoke()

        // Section Items
        section.items.forEach { item ->
            SideMenuItemTile(
                item = item,
                isSelected = selectedKey == item.key,
                isExpanded = expandedKeys[item.key] ?: false,
                activeBg = activeBg,
                activeFg = activeFg,
                inactiveFg = inactiveFg,
                isDark = isDark,
                onClick = { onItemClick(item) }
            )

            // Sub-item Tree with Vertical Connector Line
            if (!item.children.isNullOrEmpty() && (expandedKeys[item.key] == true)) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 20.dp, top = 2.dp)
                ) {
                    // Vertical continuous guide line
                    Box(
                        modifier = Modifier
                            .padding(top = 4.dp, bottom = 8.dp)
                            .width(1.5.dp)
                            .fillMaxHeight()
                            .background(borderColor, RoundedCornerShape(1.dp))
                    )

                    // Indented sub-items
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 12.dp)
                    ) {
                        item.children.forEach { child ->
                            SideMenuItemTile(
                                item = child,
                                isSelected = selectedKey == child.key,
                                isSubItem = true,
                                activeBg = activeBg,
                                activeFg = activeFg,
                                inactiveFg = inactiveFg,
                                isDark = isDark,
                                onClick = { onItemClick(child) }
                            )
                        }
                    }
                }
            }
        }

        // Section Action Link (e.g. "Customize inbox")
        if (section.actionText != null) {
            Text(
                text = section.actionText,
                style = TextStyle(
                    color = if (isDark) Ux4gColors.primary300 else Ux4gColors.primary600,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold
                ),
                modifier = Modifier
                    .clip(RoundedCornerShape(4.dp))
                    .clickable { section.onActionTap?.invoke() }
                    .padding(start = 10.dp, top = 8.dp)
            )
        }

        section.customFooter?.invoke()
    }
}

@Composable
private fun SideMenuItemTile(
    item: Ux4gSideMenuItem,
    isSelected: Boolean,
    isSubItem: Boolean = false,
    isExpanded: Boolean = false,
    activeBg: Color,
    activeFg: Color,
    inactiveFg: Color,
    isDark: Boolean,
    onClick: () -> Unit
) {
    if (item.customContent != null) {
        item.customContent.invoke()
        return
    }

    // Card style tile (used in Department Switcher)
    if (item.isCard) {
        DepartmentCardTile(
            item = item,
            isSelected = isSelected,
            activeFg = activeFg,
            isDark = isDark,
            onClick = onClick
        )
        return
    }

    val fgColor = if (isSelected) activeFg else inactiveFg
    val tileHeight = if (isSubItem) 38.dp else 42.dp

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(tileHeight)
            .clip(RoundedCornerShape(8.dp))
            .background(if (isSelected) activeBg else Color.Transparent)
            .clickable(enabled = !item.disabled, onClick = onClick)
            .padding(horizontal = if (isSubItem) 8.dp else 10.dp)
            .semantics { role = Role.Tab },
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Leading Icon
        if (item.iconComposable != null) {
            Box(
                modifier = Modifier
                    .width(24.dp)
                    .size(if (isSubItem) 17.dp else 19.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                item.iconComposable.invoke()
            }
            Spacer(modifier = Modifier.width(8.dp))
        } else if (item.icon != null) {
            Box(modifier = Modifier.width(24.dp), contentAlignment = Alignment.CenterStart) {
                Ux4gIconComponent(
                    icon = item.icon,
                    contentDescription = item.label,
                    modifier = Modifier.size(if (isSubItem) 17.dp else 19.dp),
                    tint = fgColor
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
        } else if (item.iconVector != null) {
            Box(modifier = Modifier.width(24.dp), contentAlignment = Alignment.CenterStart) {
                Icon(
                    imageVector = item.iconVector,
                    contentDescription = item.label,
                    modifier = Modifier.size(if (isSubItem) 17.dp else 19.dp),
                    tint = fgColor
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
        }

        // Label
        Text(
            text = item.label,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            style = TextStyle(
                fontSize = if (isSubItem) 13.sp else 14.sp,
                fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Medium,
                color = fgColor
            ),
            modifier = Modifier.weight(1f)
        )

        // Badge counter (e.g. 3, 9, 8)
        if (item.badge != null) {
            Spacer(modifier = Modifier.width(8.dp))
            SideMenuBadgePill(
                badge = item.badge,
                badgeColor = item.badgeColor,
                textColor = item.badgeTextColor
            )
        }

        // Trailing custom widget
        if (item.trailing != null) {
            Spacer(modifier = Modifier.width(6.dp))
            item.trailing.invoke()
        }

        // Accordion Expand/Collapse Chevron
        if (!item.children.isNullOrEmpty()) {
            Spacer(modifier = Modifier.width(4.dp))
            Icon(
                imageVector = if (isExpanded) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown,
                contentDescription = if (isExpanded) "Collapse" else "Expand",
                tint = inactiveFg,
                modifier = Modifier.size(18.dp)
            )
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// DEPARTMENT CARD TILE (Department Switcher Variant)
// ─────────────────────────────────────────────────────────────────────────────

@Composable
private fun DepartmentCardTile(
    item: Ux4gSideMenuItem,
    isSelected: Boolean,
    activeFg: Color,
    isDark: Boolean,
    onClick: () -> Unit
) {
    val cardBg = if (isSelected) {
        if (isDark) Ux4gColors.neutral800 else Ux4gColors.neutral100
    } else {
        Color.Transparent
    }
    val cardBorder = if (isSelected) {
        if (isDark) Ux4gColors.primary400 else Ux4gColors.neutral300
    } else {
        Color.Transparent
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(cardBg)
            .border(1.dp, cardBorder, RoundedCornerShape(8.dp))
            .clickable(enabled = !item.disabled, onClick = onClick)
            .padding(10.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Avatar Initials Badge
            if (item.avatarInitials != null) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(item.avatarColor ?: if (isDark) Ux4gColors.primary900 else Ux4gColors.primary200),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = item.avatarInitials,
                        style = TextStyle(
                            color = item.avatarTextColor ?: if (isDark) Ux4gColors.primary300 else Ux4gColors.primary800,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
            }

            // Titles & Tag
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = item.label,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = TextStyle(
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = activeFg
                    )
                )

                if (item.subtitle != null) {
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = item.subtitle,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        style = TextStyle(
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Normal,
                            color = if (isDark) Ux4gColors.neutral400 else Ux4gColors.neutral600
                        )
                    )
                }

                if (item.tag != null) {
                    Spacer(modifier = Modifier.height(6.dp))
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(100.dp))
                            .background(
                                item.tagColor ?: if (isDark) Ux4gColors.secondary900 else Color(0xFFFFF0D9)
                            )
                            .padding(horizontal = 8.dp, vertical = 3.dp)
                    ) {
                        Text(
                            text = item.tag,
                            style = TextStyle(
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = item.tagTextColor ?: if (isDark) Ux4gColors.secondary300 else Color(0xFF9A5B00)
                            )
                        )
                    }
                }
            }

            // Selected Checkmark
            if (isSelected) {
                Icon(
                    imageVector = Icons.Filled.Check,
                    contentDescription = "Selected",
                    tint = activeFg,
                    modifier = Modifier.size(20.dp)
                )
            } else if (item.trailing != null) {
                item.trailing.invoke()
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// ACTION REQUIRED CARD CALLOUT
// ─────────────────────────────────────────────────────────────────────────────

@Composable
private fun ActionRequiredCard(
    card: Ux4gSideMenuActionCard,
    isDark: Boolean
) {
    if (card.customContent != null) {
        card.customContent.invoke()
        return
    }

    val cardBg = card.backgroundColor ?: (if (isDark) Ux4gColors.neutral800 else Color(0xFFFFF3D9))
    val cardBorder = card.borderColor ?: (if (isDark) Ux4gColors.neutral700 else Color(0xFFFFE3A8))

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(cardBg)
            .border(1.dp, cardBorder, RoundedCornerShape(8.dp))
            .padding(14.dp)
    ) {
        // Tag / Category
        Text(
            text = card.tag,
            style = TextStyle(
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = if (isDark) Ux4gColors.neutral400 else Ux4gColors.neutral700,
                letterSpacing = 0.8.sp
            )
        )
        Spacer(modifier = Modifier.height(6.dp))

        // Title
        Text(
            text = card.title,
            style = TextStyle(
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = if (isDark) Ux4gColors.white else Ux4gColors.neutral1000black
            )
        )
        Spacer(modifier = Modifier.height(4.dp))

        // Description
        Text(
            text = card.description,
            style = TextStyle(
                fontSize = 12.sp,
                fontWeight = FontWeight.Normal,
                color = if (isDark) Ux4gColors.neutral300 else Ux4gColors.neutral800,
                lineHeight = 16.sp
            )
        )
        Spacer(modifier = Modifier.height(12.dp))

        // Action Button
        Ux4gButton(
            onClick = { card.onButtonPressed?.invoke() },
            text = card.buttonText,
            size = Ux4gButtonSize.Small,
            backgroundColor = if (isDark) Ux4gColors.primary500 else Ux4gColors.primary600,
            contentColor = Ux4gColors.white
        )
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// BADGES & BRANDING HELPERS
// ─────────────────────────────────────────────────────────────────────────────

@Composable
private fun SideMenuBadgePill(
    badge: Any,
    badgeColor: Color?,
    textColor: Color?
) {
    Box(
        modifier = Modifier
            .height(20.dp)
            .widthIn(min = 20.dp)
            .clip(RoundedCornerShape(100.dp))
            .background(badgeColor ?: Ux4gColors.primary600)
            .padding(horizontal = 6.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = badge.toString(),
            style = TextStyle(
                color = textColor ?: Ux4gColors.white,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold
            )
        )
    }
}

@Composable
private fun BottomBrandingBlock(
    branding: Ux4gSideMenuBottomBranding,
    activeFg: Color,
    subtitleColor: Color
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .clickable(enabled = branding.onTap != null) { branding.onTap?.invoke() }
            .padding(vertical = 6.dp, horizontal = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (branding.logo != null) {
            Box(modifier = Modifier.size(26.dp), contentAlignment = Alignment.Center) {
                branding.logo.invoke()
            }
        } else {
            Ux4gIconComponent(
                icon = Ux4gIcons.NationalEmblem,
                contentDescription = "Emblem",
                modifier = Modifier.size(26.dp),
                tint = activeFg
            )
        }
        Spacer(modifier = Modifier.width(10.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = branding.title,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = TextStyle(
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = activeFg
                )
            )
            if (branding.subtitle != null) {
                Spacer(modifier = Modifier.height(1.dp))
                Text(
                    text = branding.subtitle,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = TextStyle(
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Normal,
                        color = subtitleColor
                    )
                )
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// DEFAULT SECTIONS HELPER
// ─────────────────────────────────────────────────────────────────────────────

private fun defaultSections(): List<Ux4gSideMenuSection> = listOf(
    Ux4gSideMenuSection(
        key = "services",
        title = "SERVICES",
        items = listOf(
            Ux4gSideMenuItem(
                key = "dashboard",
                label = "Dashboard",
                iconVector = Icons.Filled.GridView
            ),
            Ux4gSideMenuItem(
                key = "applications",
                label = "Applications",
                iconVector = Icons.Outlined.Assignment,
                defaultExpanded = true,
                children = listOf(
                    Ux4gSideMenuItem(
                        key = "applications-in-progress",
                        label = "In progress",
                        iconVector = Icons.Outlined.Assignment
                    ),
                    Ux4gSideMenuItem(
                        key = "applications-approved",
                        label = "Approved",
                        iconVector = Icons.Outlined.Assignment
                    )
                )
            ),
            Ux4gSideMenuItem(
                key = "notifications",
                label = "Notifications",
                iconVector = Icons.Outlined.Notifications,
                badge = 3,
                badgeColor = Ux4gColors.primary600
            )
        )
    ),
    Ux4gSideMenuSection(
        key = "account",
        title = "ACCOUNT",
        items = listOf(
            Ux4gSideMenuItem(
                key = "my-account",
                label = "My account",
                iconVector = Icons.Outlined.AccountCircle
            ),
            Ux4gSideMenuItem(
                key = "help-support",
                label = "Help & support",
                iconVector = Icons.Outlined.HelpOutline
            )
        )
    )
)

// ─────────────────────────────────────────────────────────────────────────────
// READY-TO-USE PRESETS / TEMPLATES (Matching Flutter Ux4gSideMenuPresets)
// ─────────────────────────────────────────────────────────────────────────────

/**
 * Ready-to-use template builders corresponding to standard UX4G application patterns.
 */
object Ux4gSideMenuPresets {
    /** 1. Standard Government Navigation */
    @Composable
    fun Standard(
        onClose: (() -> Unit)? = null,
        onItemSelected: ((Ux4gSideMenuItem) -> Unit)? = null,
        onSignOut: (() -> Unit)? = null,
        selectedKey: String? = null
    ) {
        Ux4gSideMenu(
            title = "Revenue Department",
            subtitle = "Government of India",
            showSearch = true,
            selectedKey = selectedKey,
            onClose = onClose,
            onItemSelected = onItemSelected,
            onSignOut = onSignOut,
            sections = defaultSections()
        )
    }

    /** 2. Officer Inbox Navigation */
    @Composable
    fun OfficerInbox(
        name: String = "Ramesh Kumar",
        email: String = "ramesh.kumar@gov.in",
        division: String = "Kanpur division",
        onClose: (() -> Unit)? = null,
        onItemSelected: ((Ux4gSideMenuItem) -> Unit)? = null,
        onCustomizeInbox: (() -> Unit)? = null,
        onSignOut: (() -> Unit)? = null,
        selectedKey: String = "inbox"
    ) {
        Ux4gSideMenu(
            headerType = Ux4gSideMenuHeaderType.CompactUser,
            title = name,
            subtitle = email,
            showSearch = false,
            selectedKey = selectedKey,
            onClose = onClose,
            onItemSelected = onItemSelected,
            onSignOut = onSignOut,
            sections = listOf(
                Ux4gSideMenuSection(
                    key = "mailbox",
                    title = "MAILBOX",
                    items = listOf(
                        Ux4gSideMenuItem(
                            key = "inbox",
                            label = "Inbox",
                            iconVector = Icons.Outlined.Email,
                            badge = 9,
                            badgeColor = Ux4gColors.primary600
                        ),
                        Ux4gSideMenuItem(
                            key = "assigned-to-me",
                            label = "Assigned to me",
                            iconVector = Icons.Outlined.AssignmentInd
                        )
                    )
                ),
                Ux4gSideMenuSection(
                    key = "folders",
                    title = "FOLDERS",
                    actionText = "Customize inbox",
                    onActionTap = onCustomizeInbox,
                    items = listOf(
                        Ux4gSideMenuItem(key = "sent", label = "Sent", iconVector = Icons.AutoMirrored.Filled.ArrowForward),
                        Ux4gSideMenuItem(key = "drafts", label = "Drafts", iconVector = Icons.Outlined.EditNote),
                        Ux4gSideMenuItem(key = "archive", label = "Archive", iconVector = Icons.Outlined.Layers),
                        Ux4gSideMenuItem(key = "rejected-applications", label = "Rejected applications", iconVector = Icons.Outlined.ErrorOutline)
                    )
                )
            ),
            footerItems = listOf(
                Ux4gSideMenuItem(key = "inbox-settings", label = "Inbox settings", iconVector = Icons.Filled.MoreVert)
            ),
            showSignOut = false,
            bottomBranding = Ux4gSideMenuBottomBranding(
                title = "Revenue Department",
                subtitle = division
            )
        )
    }

    /** 3. Citizen Profile Menu */
    @Composable
    fun CitizenProfile(
        name: String = "Ramesh Kumar",
        role: String = "Citizen",
        metaText: String = "12 applications · 3 actions required",
        onViewProfile: (() -> Unit)? = null,
        onClose: (() -> Unit)? = null,
        onItemSelected: ((Ux4gSideMenuItem) -> Unit)? = null,
        onSignOut: (() -> Unit)? = null
    ) {
        Ux4gSideMenu(
            headerType = Ux4gSideMenuHeaderType.ProminentUser,
            title = name,
            subtitle = role,
            userMetaText = metaText,
            userActionButtonText = "View profile",
            onUserActionButtonPressed = onViewProfile,
            showSearch = false,
            onClose = onClose,
            onItemSelected = onItemSelected,
            onSignOut = onSignOut,
            sections = listOf(
                Ux4gSideMenuSection(
                    key = "account",
                    title = "ACCOUNT",
                    items = listOf(
                        Ux4gSideMenuItem(key = "profile-settings", label = "Profile & settings", iconVector = Icons.Outlined.AccountCircle),
                        Ux4gSideMenuItem(key = "saved-items", label = "Saved items", iconVector = Icons.Outlined.Layers)
                    )
                ),
                Ux4gSideMenuSection(
                    key = "applications",
                    title = "APPLICATIONS",
                    items = listOf(
                        Ux4gSideMenuItem(key = "my-applications", label = "My applications", iconVector = Icons.Outlined.Assignment),
                        Ux4gSideMenuItem(key = "track-application", label = "Track an application", iconVector = Icons.Outlined.LocationOn)
                    )
                ),
                Ux4gSideMenuSection(
                    key = "support",
                    title = "SUPPORT",
                    items = listOf(
                        Ux4gSideMenuItem(key = "help-support", label = "Help & support", iconVector = Icons.Outlined.HelpOutline)
                    )
                )
            ),
            showSignOut = true
        )
    }

    /** 4. Department Switcher Menu */
    @Composable
    fun DepartmentSwitcher(
        title: String = "Departments",
        subtitle: String = "Choose a department to work in.",
        selectedDepartmentKey: String = "dept-revenue",
        onClose: (() -> Unit)? = null,
        onSelectDepartment: ((Ux4gSideMenuItem) -> Unit)? = null,
        onRequestAccess: (() -> Unit)? = null,
        onSettings: (() -> Unit)? = null,
        onHelp: (() -> Unit)? = null
    ) {
        Ux4gSideMenu(
            headerType = Ux4gSideMenuHeaderType.Plain,
            title = title,
            subtitle = subtitle,
            showSearch = false,
            selectedKey = selectedDepartmentKey,
            onClose = onClose,
            onItemSelected = onSelectDepartment,
            sections = listOf(
                Ux4gSideMenuSection(
                    items = listOf(
                        Ux4gSideMenuItem(
                            key = "dept-revenue",
                            label = "Revenue Department",
                            subtitle = "Current department",
                            avatarInitials = "RD",
                            isCard = true
                        ),
                        Ux4gSideMenuItem(
                            key = "dept-transport",
                            label = "Transport Department",
                            subtitle = "transport.up.gov.in",
                            avatarInitials = "TD",
                            tag = "3 applications pending",
                            isCard = true
                        )
                    )
                )
            ),
            footerItems = listOf(
                Ux4gSideMenuItem(key = "request-access", label = "Request department access", iconVector = Icons.Filled.Add, onTap = onRequestAccess),
                Ux4gSideMenuItem(key = "settings", label = "Settings", iconVector = Icons.Filled.MoreVert, onTap = onSettings),
                Ux4gSideMenuItem(key = "help", label = "Help & support", iconVector = Icons.Outlined.HelpOutline, onTap = onHelp)
            ),
            showSignOut = false
        )
    }

    /** 5. Action Required & Workflows Menu */
    @Composable
    fun ActionRequired(
        title: String = "Revenue Department",
        subtitle: String = "Kanpur division",
        onClose: (() -> Unit)? = null,
        onVerifyAadhaar: (() -> Unit)? = null,
        onItemSelected: ((Ux4gSideMenuItem) -> Unit)? = null
    ) {
        Ux4gSideMenu(
            title = title,
            subtitle = subtitle,
            showSearch = false,
            onClose = onClose,
            onItemSelected = onItemSelected,
            showSignOut = false,
            sections = listOf(
                Ux4gSideMenuSection(
                    key = "services",
                    title = "SERVICES",
                    items = listOf(
                        Ux4gSideMenuItem(key = "dashboard", label = "Dashboard", iconVector = Icons.Filled.GridView),
                        Ux4gSideMenuItem(
                            key = "applications",
                            label = "Applications",
                            iconVector = Icons.Outlined.Assignment,
                            defaultExpanded = true,
                            children = listOf(
                                Ux4gSideMenuItem(key = "new-applications", label = "New applications"),
                                Ux4gSideMenuItem(key = "under-review", label = "Under review"),
                                Ux4gSideMenuItem(key = "approved-applications", label = "Approved applications")
                            )
                        ),
                        Ux4gSideMenuItem(
                            key = "inbox",
                            label = "Inbox",
                            iconVector = Icons.Outlined.Email,
                            badge = 8,
                            badgeColor = Ux4gColors.primary600
                        )
                    )
                ),
                Ux4gSideMenuSection(
                    key = "workspace",
                    title = "WORKSPACE",
                    items = listOf(
                        Ux4gSideMenuItem(key = "priority-applications", label = "Priority applications", iconVector = Icons.Filled.PriorityHigh),
                        Ux4gSideMenuItem(key = "archive", label = "Archive", iconVector = Icons.Outlined.Layers)
                    )
                )
            ),
            actionCard = Ux4gSideMenuActionCard(
                title = "Verify your Aadhaar",
                description = "Complete identity verification to continue.",
                buttonText = "Verify now",
                onButtonPressed = onVerifyAadhaar
            )
        )
    }

    /** 6. Citizen Simple Navigation */
    @Composable
    fun CitizenSimple(
        name: String = "Ramesh Kumar",
        role: String = "Citizen",
        onClose: (() -> Unit)? = null,
        onItemSelected: ((Ux4gSideMenuItem) -> Unit)? = null,
        onSignOut: (() -> Unit)? = null
    ) {
        Ux4gSideMenu(
            headerType = Ux4gSideMenuHeaderType.CompactUser,
            title = name,
            subtitle = role,
            showSearch = false,
            onClose = onClose,
            onItemSelected = onItemSelected,
            onSignOut = onSignOut,
            sections = listOf(
                Ux4gSideMenuSection(
                    items = listOf(
                        Ux4gSideMenuItem(key = "my-applications", label = "My applications", iconVector = Icons.Outlined.Assignment),
                        Ux4gSideMenuItem(key = "my-documents", label = "My documents", iconVector = Icons.Outlined.Layers),
                        Ux4gSideMenuItem(key = "notifications", label = "Notifications", iconVector = Icons.Outlined.Notifications, badge = 3, badgeColor = Ux4gColors.primary600)
                    )
                ),
                Ux4gSideMenuSection(
                    items = listOf(
                        Ux4gSideMenuItem(key = "profile-settings", label = "Profile & settings", iconVector = Icons.Outlined.AccountCircle),
                        Ux4gSideMenuItem(key = "help-support", label = "Help & support", iconVector = Icons.Outlined.HelpOutline)
                    )
                )
            ),
            showSignOut = true,
            bottomBranding = Ux4gSideMenuBottomBranding(
                title = "Revenue Department",
                subtitle = "Government of India"
            )
        )
    }
}

/**
 * [Ux4gDrawer] is an alias composable for [Ux4gSideMenu] matching Flutter design system.
 */
@Composable
fun Ux4gDrawer(
    modifier: Modifier = Modifier,
    onClose: (() -> Unit)? = null,
    position: Ux4gSideMenuPosition = Ux4gSideMenuPosition.Left,
    width: Dp? = null,
    headerType: Ux4gSideMenuHeaderType = Ux4gSideMenuHeaderType.Standard,
    title: String = "Revenue Department",
    subtitle: String? = "Government of India",
    logo: (@Composable () -> Unit)? = null,
    avatarInitials: String? = null,
    avatarColor: Color? = null,
    avatarTextColor: Color? = null,
    userMetaText: String? = null,
    userActionButtonText: String? = "View profile",
    onUserActionButtonPressed: (() -> Unit)? = null,
    showCloseButton: Boolean = true,
    customHeader: (@Composable () -> Unit)? = null,
    showHeaderDivider: Boolean = true,
    showSearch: Boolean = true,
    searchPlaceholder: String = "Search for...",
    searchQuery: String? = null,
    onSearchChanged: ((String) -> Unit)? = null,
    autoFilterOnSearch: Boolean = true,
    sections: List<Ux4gSideMenuSection>? = null,
    items: List<Ux4gSideMenuItem>? = null,
    selectedKey: String? = null,
    defaultSelectedKey: String? = "dashboard",
    onItemSelected: ((Ux4gSideMenuItem) -> Unit)? = null,
    actionCard: Ux4gSideMenuActionCard? = null,
    footerItems: List<Ux4gSideMenuItem>? = null,
    showSignOut: Boolean = true,
    signOutLabel: String = "Sign out",
    onSignOut: (() -> Unit)? = null,
    bottomBranding: Ux4gSideMenuBottomBranding? = null,
    customFooter: (@Composable () -> Unit)? = null,
    showFooterDivider: Boolean = true,
    backgroundColor: Color? = null,
    activeItemBackgroundColor: Color? = null,
    activeItemColor: Color? = null,
    inactiveItemColor: Color? = null,
    borderColor: Color? = null,
    elevation: Dp = 16.dp,
    contentPadding: PaddingValues = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
) {
    Ux4gSideMenu(
        modifier = modifier,
        onClose = onClose,
        position = position,
        width = width,
        headerType = headerType,
        title = title,
        subtitle = subtitle,
        logo = logo,
        avatarInitials = avatarInitials,
        avatarColor = avatarColor,
        avatarTextColor = avatarTextColor,
        userMetaText = userMetaText,
        userActionButtonText = userActionButtonText,
        onUserActionButtonPressed = onUserActionButtonPressed,
        showCloseButton = showCloseButton,
        customHeader = customHeader,
        showHeaderDivider = showHeaderDivider,
        showSearch = showSearch,
        searchPlaceholder = searchPlaceholder,
        searchQuery = searchQuery,
        onSearchChanged = onSearchChanged,
        autoFilterOnSearch = autoFilterOnSearch,
        sections = sections,
        items = items,
        selectedKey = selectedKey,
        defaultSelectedKey = defaultSelectedKey,
        onItemSelected = onItemSelected,
        actionCard = actionCard,
        footerItems = footerItems,
        showSignOut = showSignOut,
        signOutLabel = signOutLabel,
        onSignOut = onSignOut,
        bottomBranding = bottomBranding,
        customFooter = customFooter,
        showFooterDivider = showFooterDivider,
        backgroundColor = backgroundColor,
        activeItemBackgroundColor = activeItemBackgroundColor,
        activeItemColor = activeItemColor,
        inactiveItemColor = inactiveItemColor,
        borderColor = borderColor,
        elevation = elevation,
        contentPadding = contentPadding
    )
}

// ─────────────────────────────────────────────────────────────────────────────
// PREVIEWS
// ─────────────────────────────────────────────────────────────────────────────

@Preview(name = "Standard Navigation", showBackground = true, widthDp = 340, heightDp = 700)
@Composable
private fun Ux4gSideMenuStandardPreview() {
    Ux4gTheme {
        Ux4gSideMenuPresets.Standard()
    }
}

@Preview(name = "Officer Inbox", showBackground = true, widthDp = 340, heightDp = 700)
@Composable
private fun Ux4gSideMenuOfficerInboxPreview() {
    Ux4gTheme {
        Ux4gSideMenuPresets.OfficerInbox()
    }
}

@Preview(name = "Citizen Profile", showBackground = true, widthDp = 340, heightDp = 700)
@Composable
private fun Ux4gSideMenuCitizenProfilePreview() {
    Ux4gTheme {
        Ux4gSideMenuPresets.CitizenProfile()
    }
}

@Preview(name = "Department Switcher", showBackground = true, widthDp = 340, heightDp = 700)
@Composable
private fun Ux4gSideMenuDepartmentSwitcherPreview() {
    Ux4gTheme {
        Ux4gSideMenuPresets.DepartmentSwitcher()
    }
}

@Preview(name = "Action Required Banner", showBackground = true, widthDp = 340, heightDp = 700)
@Composable
private fun Ux4gSideMenuActionRequiredPreview() {
    Ux4gTheme {
        Ux4gSideMenuPresets.ActionRequired()
    }
}

@Preview(name = "Dark Mode", uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true, widthDp = 340, heightDp = 700)
@Composable
private fun Ux4gSideMenuDarkPreview() {
    Ux4gTheme {
        Ux4gSideMenuPresets.Standard()
    }
}
