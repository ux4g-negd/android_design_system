package com.ux4g.designsystemv1.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ux4g.designsystemv1.foundation.*
import com.ux4g.designsystemv1.theme.Ux4gTheme

// ─────────────────────────────────────────────────────────────────────────────
// TYPES & ENUMS
// ─────────────────────────────────────────────────────────────────────────────

/**
 * Indicator style for active tab item:
 * - [Pill]: Soft rounded purple capsule background behind active tab
 * - [Bar]: Purple horizontal bar indicator at the top of the active tab
 * - [Plain]: Clean active color highlight for icon and text without extra background/bar
 */
enum class Ux4gBottomNavIndicatorVariant {
    Pill,
    Bar,
    Plain
}

/**
 * High-level layout / shape variant preset:
 * - [Labelled]: Standard flat bottom bar with icons and text labels
 * - [TopRounded]: Bottom bar with rounded top-left and top-right corners (20dp)
 * - [IconOnly]: Icons only without text labels
 * - [CentreAction]: Bottom bar with a prominent circular action button in the center
 * - [FloatingPill]: Floating capsule bar with rounded edges detached from screen borders
 */
enum class Ux4gBottomNavVariant {
    Labelled,
    TopRounded,
    IconOnly,
    CentreAction,
    FloatingPill
}

/** Geometric shape of the bottom navigation bar container */
enum class Ux4gBottomNavShape {
    Rectangular,
    TopRounded,
    FloatingPill
}

/**
 * Individual navigation tab item definition.
 *
 * @param key Unique key for the tab item
 * @param label Label text displayed under the icon
 * @param icon Primary icon for inactive state
 * @param activeIcon Optional separate icon to render when tab is active
 * @param badge Optional badge: can be [Int] (e.g. 5), [String] (e.g. "99+"), or [Boolean] (true for dot)
 * @param badgeColor Custom badge background color (defaults to [Ux4gColors.red600])
 * @param badgeTextColor Custom badge text color (defaults to [Ux4gColors.white])
 * @param disabled Whether the tab item is disabled
 * @param tooltip Accessibility / tooltip label
 * @param customIcon Optional custom composable builder for icon rendering
 */
data class Ux4gBottomNavItem(
    val key: String? = null,
    val label: String,
    val icon: Ux4gIcon,
    val activeIcon: Ux4gIcon? = null,
    val badge: Any? = null,
    val badgeColor: Color? = null,
    val badgeTextColor: Color? = null,
    val disabled: Boolean = false,
    val tooltip: String? = null,
    val customIcon: (@Composable (isActive: Boolean, tint: Color) -> Unit)? = null
)

/**
 * Prominent center floating action button configuration (used in [Ux4gBottomNavVariant.CentreAction]).
 *
 * @param icon Icon to render inside center action button (defaults to [Ux4gIcons.Add])
 * @param onPressed Callback triggered when center action button is pressed
 * @param size Button diameter in dp (default: 48.dp)
 * @param backgroundColor Background color (defaults to resolved active primary color)
 * @param iconColor Foreground / icon color (defaults to white)
 * @param elevation Shadow / elevation depth (default: 4.dp)
 * @param tooltip Tooltip / accessibility label
 * @param customContent Optional custom composable inside the button
 */
data class Ux4gBottomNavCenterAction(
    val icon: Ux4gIcon = Ux4gIcons.Add,
    val onPressed: (() -> Unit)? = null,
    val size: Dp = 48.dp,
    val backgroundColor: Color? = null,
    val iconColor: Color? = null,
    val elevation: Dp = 4.dp,
    val tooltip: String? = null,
    val customContent: (@Composable () -> Unit)? = null
)

// ─────────────────────────────────────────────────────────────────────────────
// MAIN COMPONENT
// ─────────────────────────────────────────────────────────────────────────────

/**
 * **Ux4gBottomNavigationBar**
 *
 * Fully accessible, responsive Bottom Navigation Bar built strictly to UX4G Design System guidelines:
 * - **Indicator Variants**: `Pill` (soft capsule background), `Bar` (top purple indicator bar), `Plain` (clean icon/text tint)
 * - **Shape & Layout Variants**: `Labelled`, `TopRounded`, `IconOnly`, `CentreAction`, `FloatingPill`
 * - **Badge Support**: Numeric badges (e.g. 5), text badges ("99+"), and notification dot indicators
 * - **Prominent Center Action Button**: High-visibility circular action button (FAB) for primary actions
 * - **Full Dark & Light Mode Support** with high-contrast active states
 */
@Composable
fun Ux4gBottomNavigationBar(
    items: List<Ux4gBottomNavItem>,
    modifier: Modifier = Modifier,
    selectedIndex: Int? = null,
    defaultSelectedIndex: Int = 0,
    onTabChange: ((index: Int, item: Ux4gBottomNavItem) -> Unit)? = null,
    indicatorVariant: Ux4gBottomNavIndicatorVariant = Ux4gBottomNavIndicatorVariant.Pill,
    variant: Ux4gBottomNavVariant = Ux4gBottomNavVariant.Labelled,
    shape: Ux4gBottomNavShape? = null,
    showLabels: Boolean? = null,
    centerAction: Ux4gBottomNavCenterAction? = null,
    activeColor: Color? = null,
    inactiveColor: Color? = null,
    pillBackgroundColor: Color? = null,
    barIndicatorColor: Color? = null,
    barIndicatorHeight: Dp = 3.dp,
    barIndicatorWidth: Dp = 32.dp,
    backgroundColor: Color? = null,
    borderColor: Color? = null,
    elevation: Dp = 4.dp,
    useSafeArea: Boolean = true,
    height: Dp? = null,
    paddingTop: Dp? = null,
    paddingBottom: Dp? = null,
    paddingHorizontal: Dp? = null,
    labelStyle: TextStyle? = null,
    activeLabelStyle: TextStyle? = null,
    semanticLabel: String? = null
) {
    var internalIndex by remember { mutableIntStateOf(defaultSelectedIndex) }
    val currentIndex = selectedIndex ?: internalIndex

    val isDark = isSystemInDarkTheme()

    // Resolved colors with Dark / Light Mode fallbacks
    val resolvedActiveColor = activeColor
        ?: if (isDark) Ux4gColors.primary300 else Ux4gTheme.colorScheme.primary
    val resolvedInactiveColor = inactiveColor
        ?: if (isDark) Ux4gColors.neutral400 else Ux4gColors.neutral600
    val resolvedPillBg = pillBackgroundColor
        ?: if (isDark) Ux4gColors.primary400.copy(alpha = 0.18f) else Ux4gColors.primary50
    val resolvedBarColor = barIndicatorColor ?: resolvedActiveColor
    val resolvedBgColor = backgroundColor
        ?: if (isDark) Ux4gColors.neutral900 else Ux4gTheme.colorScheme.surface
    val resolvedBorderColor = borderColor
        ?: if (isDark) Ux4gColors.neutral700 else Ux4gColors.neutral200

    val resolvedShape = shape ?: when (variant) {
        Ux4gBottomNavVariant.TopRounded -> Ux4gBottomNavShape.TopRounded
        Ux4gBottomNavVariant.FloatingPill -> Ux4gBottomNavShape.FloatingPill
        else -> Ux4gBottomNavShape.Rectangular
    }

    val isFloating = resolvedShape == Ux4gBottomNavShape.FloatingPill
    val isTopRounded = resolvedShape == Ux4gBottomNavShape.TopRounded
    val isIconOnly = variant == Ux4gBottomNavVariant.IconOnly || showLabels == false
    val hasCenterAction = variant == Ux4gBottomNavVariant.CentreAction

    val resolvedPadTop = paddingTop ?: if (isFloating) 4.dp else 8.dp
    val resolvedPadBottom = paddingBottom ?: if (isFloating) 4.dp else 12.dp
    val resolvedPadHoriz = paddingHorizontal ?: if (isFloating) 8.dp else 4.dp
    val resolvedContentHeight = height ?: if (isIconOnly) 50.dp else 58.dp
    val totalNavHeight = resolvedContentHeight + resolvedPadTop + resolvedPadBottom

    val handleTabPress: (Int, Ux4gBottomNavItem) -> Unit = { index, item ->
        if (!item.disabled) {
            if (selectedIndex == null) {
                internalIndex = index
            }
            onTabChange?.invoke(index, item)
        }
    }

    // Outer layout with SafeArea and Floating positioning
    val safeAreaModifier = if (useSafeArea && !isFloating) {
        Modifier.navigationBarsPadding()
    } else {
        Modifier
    }

    val rootContainerModifier = if (isFloating) {
        modifier
            .fillMaxWidth()
            .then(if (useSafeArea) Modifier.navigationBarsPadding() else Modifier)
            .padding(start = 12.dp, end = 12.dp, top = 4.dp, bottom = 16.dp)
            .wrapContentWidth(Alignment.CenterHorizontally)
            .widthIn(max = 520.dp)
    } else {
        modifier
            .fillMaxWidth()
            .then(safeAreaModifier)
    }

    // Container shape & border
    val containerShape: Shape = when (resolvedShape) {
        Ux4gBottomNavShape.FloatingPill -> RoundedCornerShape(999.dp)
        Ux4gBottomNavShape.TopRounded -> RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
        Ux4gBottomNavShape.Rectangular -> RoundedCornerShape(0.dp)
    }

    Surface(
        modifier = rootContainerModifier
            .semantics {
                contentDescription = semanticLabel ?: "Bottom navigation bar"
            },
        shape = containerShape,
        color = resolvedBgColor,
        shadowElevation = elevation,
        border = when (resolvedShape) {
            Ux4gBottomNavShape.FloatingPill -> androidx.compose.foundation.BorderStroke(1.dp, resolvedBorderColor)
            Ux4gBottomNavShape.TopRounded -> androidx.compose.foundation.BorderStroke(1.dp, resolvedBorderColor)
            Ux4gBottomNavShape.Rectangular -> androidx.compose.foundation.BorderStroke(1.dp, resolvedBorderColor)
        }
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(totalNavHeight)
                .padding(horizontal = resolvedPadHoriz)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize(),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (!hasCenterAction) {
                    items.forEachIndexed { index, item ->
                        BottomNavTabItem(
                            item = item,
                            index = index,
                            isActive = currentIndex == index,
                            indicatorVariant = indicatorVariant,
                            isFloating = isFloating,
                            isIconOnly = isIconOnly,
                            resolvedActiveColor = resolvedActiveColor,
                            resolvedInactiveColor = resolvedInactiveColor,
                            resolvedPillBg = resolvedPillBg,
                            resolvedBarColor = resolvedBarColor,
                            barIndicatorWidth = barIndicatorWidth,
                            barIndicatorHeight = barIndicatorHeight,
                            contentHeight = resolvedContentHeight,
                            labelStyle = labelStyle,
                            activeLabelStyle = activeLabelStyle,
                            onTabClick = { handleTabPress(index, item) },
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight()
                        )
                    }
                } else {
                    val mid = items.size / 2
                    val leftItems = items.subList(0, mid)
                    val rightItems = items.subList(mid, items.size)

                    leftItems.forEachIndexed { i, item ->
                        BottomNavTabItem(
                            item = item,
                            index = i,
                            isActive = currentIndex == i,
                            indicatorVariant = indicatorVariant,
                            isFloating = isFloating,
                            isIconOnly = isIconOnly,
                            resolvedActiveColor = resolvedActiveColor,
                            resolvedInactiveColor = resolvedInactiveColor,
                            resolvedPillBg = resolvedPillBg,
                            resolvedBarColor = resolvedBarColor,
                            barIndicatorWidth = barIndicatorWidth,
                            barIndicatorHeight = barIndicatorHeight,
                            contentHeight = resolvedContentHeight,
                            labelStyle = labelStyle,
                            activeLabelStyle = activeLabelStyle,
                            onTabClick = { handleTabPress(i, item) },
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight()
                                .padding(end = if (i == leftItems.lastIndex) 2.dp else 0.dp)
                        )
                    }

                    // Prominent Center Action Button
                    CenterActionButton(
                        action = centerAction ?: Ux4gBottomNavCenterAction(),
                        resolvedActiveColor = resolvedActiveColor
                    )

                    rightItems.forEachIndexed { i, item ->
                        val actualIndex = mid + i
                        BottomNavTabItem(
                            item = item,
                            index = actualIndex,
                            isActive = currentIndex == actualIndex,
                            indicatorVariant = indicatorVariant,
                            isFloating = isFloating,
                            isIconOnly = isIconOnly,
                            resolvedActiveColor = resolvedActiveColor,
                            resolvedInactiveColor = resolvedInactiveColor,
                            resolvedPillBg = resolvedPillBg,
                            resolvedBarColor = resolvedBarColor,
                            barIndicatorWidth = barIndicatorWidth,
                            barIndicatorHeight = barIndicatorHeight,
                            contentHeight = resolvedContentHeight,
                            labelStyle = labelStyle,
                            activeLabelStyle = activeLabelStyle,
                            onTabClick = { handleTabPress(actualIndex, item) },
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight()
                                .padding(start = if (i == 0) 2.dp else 0.dp)
                        )
                    }
                }
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// TAB ITEM SUBCOMPOSABLE
// ─────────────────────────────────────────────────────────────────────────────

@Composable
private fun BottomNavTabItem(
    item: Ux4gBottomNavItem,
    index: Int,
    isActive: Boolean,
    indicatorVariant: Ux4gBottomNavIndicatorVariant,
    isFloating: Boolean,
    isIconOnly: Boolean,
    resolvedActiveColor: Color,
    resolvedInactiveColor: Color,
    resolvedPillBg: Color,
    resolvedBarColor: Color,
    barIndicatorWidth: Dp,
    barIndicatorHeight: Dp,
    contentHeight: Dp,
    labelStyle: TextStyle?,
    activeLabelStyle: TextStyle?,
    onTabClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val fgColor = if (isActive) resolvedActiveColor else resolvedInactiveColor
    val isPill = indicatorVariant == Ux4gBottomNavIndicatorVariant.Pill && isActive
    val isBar = indicatorVariant == Ux4gBottomNavIndicatorVariant.Bar && isActive

    val animatedPillColor by animateColorAsState(
        targetValue = if (isPill) resolvedPillBg else resolvedPillBg.copy(alpha = 0f),
        animationSpec = tween(durationMillis = 180, easing = FastOutSlowInEasing),
        label = "pill_background"
    )

    val pillShape = RoundedCornerShape(if (isFloating) 999.dp else 8.dp)

    Box(
        modifier = modifier
            .fillMaxHeight(),
        contentAlignment = Alignment.Center
    ) {
        // ── 1. Top Bar Indicator (pinned to top edge) ────────────────────────
        if (isBar) {
            val barWidth = if (isFloating) {
                if (barIndicatorWidth > 28.dp) 28.dp else barIndicatorWidth
            } else {
                barIndicatorWidth
            }

            Box(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .width(barWidth)
                    .height(barIndicatorHeight)
                    .background(
                        color = resolvedBarColor,
                        shape = RoundedCornerShape(bottomStart = 2.dp, bottomEnd = 2.dp)
                    )
            )
        }

        // ── 2. Tab Content Box (Icon + Label, with Pill BG matching Flutter) ─
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(contentHeight)
                .padding(horizontal = 2.dp, vertical = 2.dp)
                .clip(pillShape)
                .background(color = animatedPillColor, shape = pillShape)
                .then(
                    if (!item.disabled) {
                        Modifier.clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null, // Matches Flutter splashColor = Colors.transparent
                            onClick = onTabClick
                        )
                    } else {
                        Modifier
                    }
                )
                .alpha(if (item.disabled) 0.4f else 1f)
                .semantics {
                    role = Role.Tab
                    selected = isActive
                    contentDescription = item.tooltip ?: item.label
                },
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(vertical = 2.dp, horizontal = 2.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                // Icon + Badge Box (24x24 matching UX4G icon container)
                Box(
                    modifier = Modifier.size(24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    // Render Icon
                    if (item.customIcon != null) {
                        item.customIcon.invoke(isActive, fgColor)
                    } else {
                        val currentIcon = if (isActive && item.activeIcon != null) item.activeIcon else item.icon
                        Ux4gIconComponent(
                            icon = currentIcon,
                            contentDescription = null,
                            modifier = Modifier.size(24.dp),
                            tint = fgColor
                        )
                    }

                    // Render Badge if present
                    if (item.badge != null) {
                        RenderBadge(
                            badge = item.badge,
                            badgeColor = item.badgeColor ?: Ux4gColors.red600,
                            badgeTextColor = item.badgeTextColor ?: Ux4gColors.white
                        )
                    }
                }

                // Text Label
                if (!isIconOnly) {
                    Spacer(modifier = Modifier.height(3.dp))
                    val resolvedTextStyle = if (isActive) {
                        activeLabelStyle ?: Ux4gTheme.typography.lS_strong.copy(
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    } else {
                        labelStyle ?: Ux4gTheme.typography.lS_default.copy(
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }

                    Text(
                        text = item.label,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        textAlign = TextAlign.Center,
                        style = resolvedTextStyle.copy(color = fgColor)
                    )
                }
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// BADGE SUBCOMPOSABLE
// ─────────────────────────────────────────────────────────────────────────────

@Composable
private fun BoxScope.RenderBadge(
    badge: Any,
    badgeColor: Color,
    badgeTextColor: Color
) {
    if (badge is Boolean && badge) {
        // Red dot indicator (8x8)
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .offset(x = 2.dp, y = (-2).dp)
                .size(8.dp)
                .background(badgeColor, CircleShape)
        )
    } else {
        // Numeric or text badge (e.g. 5, "99+")
        val badgeText = badge.toString()
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .offset(x = 8.dp, y = (-4).dp)
                .defaultMinSize(minWidth = 16.dp, minHeight = 16.dp)
                .background(badgeColor, RoundedCornerShape(8.dp))
                .padding(horizontal = 4.dp, vertical = 1.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = badgeText,
                color = badgeTextColor,
                fontSize = 9.sp,
                fontWeight = FontWeight.Bold,
                lineHeight = 9.sp,
                textAlign = TextAlign.Center
            )
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// CENTER ACTION BUTTON
// ─────────────────────────────────────────────────────────────────────────────

@Composable
private fun CenterActionButton(
    action: Ux4gBottomNavCenterAction,
    resolvedActiveColor: Color
) {
    val bg = action.backgroundColor ?: resolvedActiveColor
    val fg = action.iconColor ?: Ux4gColors.white

    Box(
        modifier = Modifier
            .padding(horizontal = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Surface(
            onClick = { action.onPressed?.invoke() },
            shape = CircleShape,
            color = bg,
            shadowElevation = action.elevation,
            modifier = Modifier
                .size(action.size)
                .semantics {
                    role = Role.Button
                    contentDescription = action.tooltip ?: "Center action"
                }
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                if (action.customContent != null) {
                    action.customContent.invoke()
                } else {
                    Ux4gIconComponent(
                        icon = action.icon,
                        contentDescription = null,
                        modifier = Modifier.size(24.dp),
                        tint = fg
                    )
                }
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// CONVENIENCE PRESET COMPOSABLES
// ─────────────────────────────────────────────────────────────────────────────

/**
 * Bottom Navigation Bar with Soft Pill indicator around active tab.
 */
@Composable
fun Ux4gPillBottomNavBar(
    items: List<Ux4gBottomNavItem>,
    modifier: Modifier = Modifier,
    selectedIndex: Int? = null,
    defaultSelectedIndex: Int = 0,
    onTabChange: ((index: Int, item: Ux4gBottomNavItem) -> Unit)? = null,
    variant: Ux4gBottomNavVariant = Ux4gBottomNavVariant.Labelled,
    shape: Ux4gBottomNavShape? = null,
    showLabels: Boolean? = null,
    centerAction: Ux4gBottomNavCenterAction? = null,
    activeColor: Color? = null,
    inactiveColor: Color? = null,
    pillBackgroundColor: Color? = null,
    backgroundColor: Color? = null,
    borderColor: Color? = null,
    elevation: Dp = 4.dp,
    useSafeArea: Boolean = true,
    height: Dp? = null
) {
    Ux4gBottomNavigationBar(
        items = items,
        modifier = modifier,
        selectedIndex = selectedIndex,
        defaultSelectedIndex = defaultSelectedIndex,
        onTabChange = onTabChange,
        indicatorVariant = Ux4gBottomNavIndicatorVariant.Pill,
        variant = variant,
        shape = shape,
        showLabels = showLabels,
        centerAction = centerAction,
        activeColor = activeColor,
        inactiveColor = inactiveColor,
        pillBackgroundColor = pillBackgroundColor,
        backgroundColor = backgroundColor,
        borderColor = borderColor,
        elevation = elevation,
        useSafeArea = useSafeArea,
        height = height
    )
}

/**
 * Bottom Navigation Bar with Top Bar indicator above active tab.
 */
@Composable
fun Ux4gBarBottomNavBar(
    items: List<Ux4gBottomNavItem>,
    modifier: Modifier = Modifier,
    selectedIndex: Int? = null,
    defaultSelectedIndex: Int = 0,
    onTabChange: ((index: Int, item: Ux4gBottomNavItem) -> Unit)? = null,
    variant: Ux4gBottomNavVariant = Ux4gBottomNavVariant.Labelled,
    shape: Ux4gBottomNavShape? = null,
    showLabels: Boolean? = null,
    centerAction: Ux4gBottomNavCenterAction? = null,
    activeColor: Color? = null,
    inactiveColor: Color? = null,
    barIndicatorColor: Color? = null,
    barIndicatorHeight: Dp = 3.dp,
    barIndicatorWidth: Dp = 32.dp,
    backgroundColor: Color? = null,
    borderColor: Color? = null,
    elevation: Dp = 4.dp,
    useSafeArea: Boolean = true,
    height: Dp? = null
) {
    Ux4gBottomNavigationBar(
        items = items,
        modifier = modifier,
        selectedIndex = selectedIndex,
        defaultSelectedIndex = defaultSelectedIndex,
        onTabChange = onTabChange,
        indicatorVariant = Ux4gBottomNavIndicatorVariant.Bar,
        variant = variant,
        shape = shape,
        showLabels = showLabels,
        centerAction = centerAction,
        activeColor = activeColor,
        inactiveColor = inactiveColor,
        barIndicatorColor = barIndicatorColor,
        barIndicatorHeight = barIndicatorHeight,
        barIndicatorWidth = barIndicatorWidth,
        backgroundColor = backgroundColor,
        borderColor = borderColor,
        elevation = elevation,
        useSafeArea = useSafeArea,
        height = height
    )
}

/**
 * Bottom Navigation Bar with Plain clean active highlight (no pill, no bar).
 */
@Composable
fun Ux4gPlainBottomNavBar(
    items: List<Ux4gBottomNavItem>,
    modifier: Modifier = Modifier,
    selectedIndex: Int? = null,
    defaultSelectedIndex: Int = 0,
    onTabChange: ((index: Int, item: Ux4gBottomNavItem) -> Unit)? = null,
    variant: Ux4gBottomNavVariant = Ux4gBottomNavVariant.Labelled,
    shape: Ux4gBottomNavShape? = null,
    showLabels: Boolean? = null,
    centerAction: Ux4gBottomNavCenterAction? = null,
    activeColor: Color? = null,
    inactiveColor: Color? = null,
    backgroundColor: Color? = null,
    borderColor: Color? = null,
    elevation: Dp = 4.dp,
    useSafeArea: Boolean = true,
    height: Dp? = null
) {
    Ux4gBottomNavigationBar(
        items = items,
        modifier = modifier,
        selectedIndex = selectedIndex,
        defaultSelectedIndex = defaultSelectedIndex,
        onTabChange = onTabChange,
        indicatorVariant = Ux4gBottomNavIndicatorVariant.Plain,
        variant = variant,
        shape = shape,
        showLabels = showLabels,
        centerAction = centerAction,
        activeColor = activeColor,
        inactiveColor = inactiveColor,
        backgroundColor = backgroundColor,
        borderColor = borderColor,
        elevation = elevation,
        useSafeArea = useSafeArea,
        height = height
    )
}

/**
 * Floating Capsule Bottom Navigation Bar detached from screen borders.
 */
@Composable
fun Ux4gFloatingBottomNavBar(
    items: List<Ux4gBottomNavItem>,
    modifier: Modifier = Modifier,
    selectedIndex: Int? = null,
    defaultSelectedIndex: Int = 0,
    onTabChange: ((index: Int, item: Ux4gBottomNavItem) -> Unit)? = null,
    indicatorVariant: Ux4gBottomNavIndicatorVariant = Ux4gBottomNavIndicatorVariant.Pill,
    showLabels: Boolean? = null,
    centerAction: Ux4gBottomNavCenterAction? = null,
    activeColor: Color? = null,
    inactiveColor: Color? = null,
    pillBackgroundColor: Color? = null,
    barIndicatorColor: Color? = null,
    backgroundColor: Color? = null,
    borderColor: Color? = null,
    elevation: Dp = 4.dp,
    useSafeArea: Boolean = true,
    height: Dp? = null
) {
    Ux4gBottomNavigationBar(
        items = items,
        modifier = modifier,
        selectedIndex = selectedIndex,
        defaultSelectedIndex = defaultSelectedIndex,
        onTabChange = onTabChange,
        indicatorVariant = indicatorVariant,
        variant = Ux4gBottomNavVariant.FloatingPill,
        shape = Ux4gBottomNavShape.FloatingPill,
        showLabels = showLabels,
        centerAction = centerAction,
        activeColor = activeColor,
        inactiveColor = inactiveColor,
        pillBackgroundColor = pillBackgroundColor,
        barIndicatorColor = barIndicatorColor,
        backgroundColor = backgroundColor,
        borderColor = borderColor,
        elevation = elevation,
        useSafeArea = useSafeArea,
        height = height
    )
}

/**
 * Bottom Navigation Bar with Prominent Center Action '+' button.
 */
@Composable
fun Ux4gCenterActionBottomNavBar(
    items: List<Ux4gBottomNavItem>,
    modifier: Modifier = Modifier,
    selectedIndex: Int? = null,
    defaultSelectedIndex: Int = 0,
    onTabChange: ((index: Int, item: Ux4gBottomNavItem) -> Unit)? = null,
    indicatorVariant: Ux4gBottomNavIndicatorVariant = Ux4gBottomNavIndicatorVariant.Pill,
    shape: Ux4gBottomNavShape? = null,
    showLabels: Boolean? = null,
    centerAction: Ux4gBottomNavCenterAction? = null,
    activeColor: Color? = null,
    inactiveColor: Color? = null,
    pillBackgroundColor: Color? = null,
    barIndicatorColor: Color? = null,
    backgroundColor: Color? = null,
    borderColor: Color? = null,
    elevation: Dp = 4.dp,
    useSafeArea: Boolean = true,
    height: Dp? = null
) {
    Ux4gBottomNavigationBar(
        items = items,
        modifier = modifier,
        selectedIndex = selectedIndex,
        defaultSelectedIndex = defaultSelectedIndex,
        onTabChange = onTabChange,
        indicatorVariant = indicatorVariant,
        variant = Ux4gBottomNavVariant.CentreAction,
        shape = shape,
        showLabels = showLabels,
        centerAction = centerAction ?: Ux4gBottomNavCenterAction(),
        activeColor = activeColor,
        inactiveColor = inactiveColor,
        pillBackgroundColor = pillBackgroundColor,
        barIndicatorColor = barIndicatorColor,
        backgroundColor = backgroundColor,
        borderColor = borderColor,
        elevation = elevation,
        useSafeArea = useSafeArea,
        height = height
    )
}

// ─────────────────────────────────────────────────────────────────────────────
// PREVIEWS
// ─────────────────────────────────────────────────────────────────────────────

@Preview(name = "Ux4g BottomNav - Pill Light", showBackground = true)
@Composable
private fun PreviewBottomNavPill() {
    Ux4gTheme {
        val sampleItems = listOf(
            Ux4gBottomNavItem(label = "Home", icon = Ux4gIcons.Home),
            Ux4gBottomNavItem(label = "Search", icon = Ux4gIcons.Search, badge = 3),
            Ux4gBottomNavItem(label = "Upload", icon = Ux4gIcons.upload),
            Ux4gBottomNavItem(label = "Profile", icon = Ux4gIcons.Person, badge = true)
        )
        Ux4gBottomNavigationBar(
            items = sampleItems,
            defaultSelectedIndex = 0,
            indicatorVariant = Ux4gBottomNavIndicatorVariant.Pill,
            variant = Ux4gBottomNavVariant.Labelled,
            useSafeArea = false
        )
    }
}

@Preview(name = "Ux4g BottomNav - Floating Pill", showBackground = true)
@Composable
private fun PreviewBottomNavFloating() {
    Ux4gTheme {
        val sampleItems = listOf(
            Ux4gBottomNavItem(label = "Home", icon = Ux4gIcons.Home),
            Ux4gBottomNavItem(label = "Search", icon = Ux4gIcons.Search),
            Ux4gBottomNavItem(label = "Upload", icon = Ux4gIcons.upload, badge = "99+"),
            Ux4gBottomNavItem(label = "Profile", icon = Ux4gIcons.Person)
        )
        Ux4gFloatingBottomNavBar(
            items = sampleItems,
            defaultSelectedIndex = 1,
            useSafeArea = false
        )
    }
}

@Preview(name = "Ux4g BottomNav - Center Action", showBackground = true)
@Composable
private fun PreviewBottomNavCenterAction() {
    Ux4gTheme {
        val sampleItems = listOf(
            Ux4gBottomNavItem(label = "Home", icon = Ux4gIcons.Home),
            Ux4gBottomNavItem(label = "Search", icon = Ux4gIcons.Search),
            Ux4gBottomNavItem(label = "Favorites", icon = Ux4gIcons.Star),
            Ux4gBottomNavItem(label = "Settings", icon = Ux4gIcons.Settings)
        )
        Ux4gCenterActionBottomNavBar(
            items = sampleItems,
            defaultSelectedIndex = 0,
            useSafeArea = false
        )
    }
}
