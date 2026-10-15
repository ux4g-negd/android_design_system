package com.ux4g.designsystemv1.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.outlined.ChatBubbleOutline
import androidx.compose.material.icons.outlined.Phone
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ux4g.designsystemv1.foundation.*
import com.ux4g.designsystemv1.theme.Ux4gTheme

// ─────────────────────────────────────────────────────────────────────────────
// ENUMS & MODELS
// ─────────────────────────────────────────────────────────────────────────────

/**
 * Layout variant for [Ux4gFab].
 * Matches Flutter UX4G `Ux4gFabVariant`.
 */
enum class Ux4gFabVariant {
    /** Standard circular floating action button with icon only. */
    Icon,

    /** Extended pill-shaped floating action button with label (and optional icon). */
    Labelled,

    /** Speed-dial menu where action items are joined inside a single vertical capsule. */
    MenuJoined,

    /** Speed-dial menu where action items are individual spaced circular buttons. */
    MenuSpaced,

    /** Speed-dial menu where action items are individual pill buttons with icons and labels. */
    MenuLabelled
}

/**
 * Color theme preset for [Ux4gFab].
 * Matches Flutter UX4G `Ux4gFabColorTheme`.
 */
enum class Ux4gFabColorTheme {
    /** Filled brand primary background (purple) with white content. */
    Brand,

    /** Elevated white surface background with primary or dark content and subtle border. */
    Surface
}

/**
 * Screen position presets for [Ux4gFab].
 * Matches Flutter UX4G `Ux4gFabPosition`.
 */
enum class Ux4gFabPosition {
    /** Flow inline without absolute positioning. */
    None,

    /** Positioned at the bottom right corner of parent. */
    BottomRight,

    /** Positioned at the bottom left corner of parent. */
    BottomLeft,

    /** Positioned at the bottom center of parent. */
    BottomCenter,

    /** Positioned at the top right corner of parent. */
    TopRight,

    /** Positioned at the top left corner of parent. */
    TopLeft
}

/**
 * Sizing presets for [Ux4gFab].
 * Matches Flutter UX4G `Ux4gFabSize`.
 */
enum class Ux4gFabSize {
    /** Small FAB: 44x44 circular button. */
    Small,

    /** Medium FAB: 56x56 circular button (standard). */
    Medium,

    /** Large FAB: 64x64 circular button. */
    Large
}

/**
 * Model representing an individual action item in a speed-dial menu.
 * Matches Flutter UX4G `Ux4gFabActionItem`.
 */
data class Ux4gFabActionItem(
    val id: String,
    val label: String? = null,
    val icon: Ux4gIcon? = null,
    val iconVector: ImageVector? = null,
    val iconComposable: (@Composable () -> Unit)? = null,
    val onClick: (() -> Unit)? = null,
    val backgroundColor: Color? = null,
    val contentColor: Color? = null,
    val disabled: Boolean = false,
    val tooltip: String? = null
)

/**
 * Default specifications and helper presets for [Ux4gFab].
 */
object Ux4gFabDefaults {
    /**
     * Default speed-dial actions matching Flutter UX4G specifications.
     */
    val defaultActions: List<Ux4gFabActionItem> = listOf(
        Ux4gFabActionItem(
            id = "add",
            label = "Add",
            iconVector = Icons.Filled.Add
        ),
        Ux4gFabActionItem(
            id = "message",
            label = "Message",
            iconVector = Icons.Outlined.ChatBubbleOutline
        ),
        Ux4gFabActionItem(
            id = "share",
            label = "Share",
            iconVector = Icons.Outlined.Share
        ),
        Ux4gFabActionItem(
            id = "call",
            label = "Call",
            iconVector = Icons.Outlined.Phone
        )
    )

    fun diameter(size: Ux4gFabSize): Dp = when (size) {
        Ux4gFabSize.Small -> 44.dp
        Ux4gFabSize.Medium -> 56.dp
        Ux4gFabSize.Large -> 64.dp
    }

    fun iconSize(size: Ux4gFabSize): Dp = when (size) {
        Ux4gFabSize.Small -> 20.dp
        Ux4gFabSize.Medium -> 24.dp
        Ux4gFabSize.Large -> 28.dp
    }

    fun fontSize(size: Ux4gFabSize): TextUnit = when (size) {
        Ux4gFabSize.Small -> 13.sp
        Ux4gFabSize.Medium -> 15.sp
        Ux4gFabSize.Large -> 16.sp
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// COMPONENT IMPLEMENTATION
// ─────────────────────────────────────────────────────────────────────────────

/**
 * **Ux4gFab**
 *
 * UX4G Floating Action Button (FAB) and Speed-Dial component built strictly to UX4G Design System guidelines:
 * - **5 Core Variants**:
 *   - `Icon`: Standard circular floating action button with icon only.
 *   - `Labelled`: Extended pill-shaped FAB with label (and optional icon).
 *   - `MenuJoined`: Speed-dial menu where action items are joined in a single vertical capsule.
 *   - `MenuSpaced`: Speed-dial menu where action items are separate spaced circular buttons.
 *   - `MenuLabelled`: Speed-dial menu where action items are individual pill buttons with icons and labels.
 * - **2 Color Themes**:
 *   - `Brand`: Filled primary purple (`Ux4gColors.primary600`) with white foreground.
 *   - `Surface`: Elevated white surface (`Ux4gColors.neutral0`) with primary content & subtle border (`0xFFE2E8F0`).
 * - **3 Sizing Options**: `Small` (44dp), `Medium` (56dp standard), `Large` (64dp).
 * - **Full Speed-Dial Interactivity**: Smooth scale + fade + slide animations, rotation/crossfade trigger icon,
 *   optional backdrop scrim, and controlled or uncontrolled open state.
 * - **Positioning**: Seamless inline flow (`Ux4gFabPosition.None`) or anchored overlay (`BottomRight`, `BottomLeft`, etc.).
 */
@Composable
fun Ux4gFab(
    modifier: Modifier = Modifier,
    variant: Ux4gFabVariant = Ux4gFabVariant.Icon,
    colorTheme: Ux4gFabColorTheme = Ux4gFabColorTheme.Brand,
    label: String = "Button",
    showIcon: Boolean = true,
    icon: Ux4gIcon? = null,
    iconVector: ImageVector? = Icons.Filled.Add,
    iconComposable: (@Composable () -> Unit)? = null,
    activeIcon: Ux4gIcon? = null,
    activeIconVector: ImageVector? = Icons.Filled.Close,
    activeIconComposable: (@Composable () -> Unit)? = null,
    actions: List<Ux4gFabActionItem> = Ux4gFabDefaults.defaultActions,
    isOpen: Boolean? = null,
    defaultOpen: Boolean = false,
    onToggle: ((Boolean) -> Unit)? = null,
    onClick: (() -> Unit)? = null,
    position: Ux4gFabPosition = Ux4gFabPosition.None,
    alignment: Alignment.Horizontal? = null,
    offset: PaddingValues = PaddingValues(24.dp),
    showBackdrop: Boolean = false,
    onBackdropClick: (() -> Unit)? = null,
    disabled: Boolean = false,
    size: Ux4gFabSize = Ux4gFabSize.Medium,
    backgroundColor: Color? = null,
    contentColor: Color? = null,
    elevation: Dp? = null,
    animated: Boolean = true,
    labelStyle: TextStyle? = null,
    tooltip: String? = null,
    contentDescription: String? = null
) {
    // Controlled vs uncontrolled open state
    var internalOpen by remember { mutableStateOf(defaultOpen) }
    val isControlled = isOpen != null
    val isMenuOpen = if (isControlled) isOpen!! else internalOpen

    val isMenuVariant = variant == Ux4gFabVariant.MenuJoined ||
            variant == Ux4gFabVariant.MenuSpaced ||
            variant == Ux4gFabVariant.MenuLabelled

    val effectiveAlignment: Alignment.Horizontal = alignment ?: when (position) {
        Ux4gFabPosition.BottomLeft, Ux4gFabPosition.TopLeft -> Alignment.Start
        Ux4gFabPosition.BottomCenter -> Alignment.CenterHorizontally
        Ux4gFabPosition.BottomRight, Ux4gFabPosition.TopRight -> Alignment.End
        Ux4gFabPosition.None -> if (variant == Ux4gFabVariant.MenuLabelled) Alignment.End else Alignment.CenterHorizontally
    }

    val toggleMenu: () -> Unit = {
        if (!disabled) {
            val next = !isMenuOpen
            if (!isControlled) {
                internalOpen = next
            }
            onToggle?.invoke(next)
        }
    }

    val handleMainPress: () -> Unit = {
        if (!disabled) {
            if (isMenuVariant) {
                toggleMenu()
            }
            onClick?.invoke()
        }
    }

    val handleBackdropPress: () -> Unit = {
        if (onBackdropClick != null) {
            onBackdropClick.invoke()
        } else {
            if (!isControlled) {
                internalOpen = false
            }
            onToggle?.invoke(false)
        }
    }

    // Core content column (Speed dial items + Main FAB)
    val fabContent = @Composable {
        Column(
            modifier = Modifier.wrapContentSize(),
            horizontalAlignment = effectiveAlignment,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            if (isMenuVariant) {
                val enterAnim = if (animated) {
                    fadeIn(animationSpec = tween(220, easing = FastOutSlowInEasing)) +
                            scaleIn(
                                initialScale = 0.8f,
                                transformOrigin = when (effectiveAlignment) {
                                    Alignment.Start -> TransformOrigin(0f, 1f)
                                    Alignment.End -> TransformOrigin(1f, 1f)
                                    else -> TransformOrigin(0.5f, 1f)
                                },
                                animationSpec = tween(220, easing = FastOutSlowInEasing)
                            ) +
                            slideInVertically(
                                initialOffsetY = { (it * 0.15f).toInt() },
                                animationSpec = tween(220, easing = FastOutSlowInEasing)
                            )
                } else {
                    fadeIn(animationSpec = tween(0))
                }

                val exitAnim = if (animated) {
                    fadeOut(animationSpec = tween(180, easing = FastOutSlowInEasing)) +
                            scaleOut(
                                targetScale = 0.8f,
                                transformOrigin = when (effectiveAlignment) {
                                    Alignment.Start -> TransformOrigin(0f, 1f)
                                    Alignment.End -> TransformOrigin(1f, 1f)
                                    else -> TransformOrigin(0.5f, 1f)
                                },
                                animationSpec = tween(180, easing = FastOutSlowInEasing)
                            ) +
                            slideOutVertically(
                                targetOffsetY = { (it * 0.15f).toInt() },
                                animationSpec = tween(180, easing = FastOutSlowInEasing)
                            )
                } else {
                    fadeOut(animationSpec = tween(0))
                }

                AnimatedVisibility(
                    visible = isMenuOpen,
                    enter = enterAnim,
                    exit = exitAnim
                ) {
                    when (variant) {
                        Ux4gFabVariant.MenuJoined -> {
                            Ux4gFabJoinedMenu(
                                actions = actions,
                                onActionClick = { item ->
                                    item.onClick?.invoke()
                                    if (!isControlled) internalOpen = false
                                    onToggle?.invoke(false)
                                }
                            )
                        }
                        Ux4gFabVariant.MenuSpaced -> {
                            Ux4gFabSpacedMenu(
                                actions = actions,
                                colorTheme = colorTheme,
                                alignment = effectiveAlignment,
                                onActionClick = { item ->
                                    item.onClick?.invoke()
                                    if (!isControlled) internalOpen = false
                                    onToggle?.invoke(false)
                                }
                            )
                        }
                        Ux4gFabVariant.MenuLabelled -> {
                            Ux4gFabLabelledMenu(
                                actions = actions,
                                colorTheme = colorTheme,
                                alignment = effectiveAlignment,
                                onActionClick = { item ->
                                    item.onClick?.invoke()
                                    if (!isControlled) internalOpen = false
                                    onToggle?.invoke(false)
                                }
                            )
                        }
                        else -> Unit
                    }
                }
            }

            // Main Trigger Button
            Ux4gFabMainButton(
                variant = variant,
                colorTheme = colorTheme,
                size = size,
                label = label,
                showIcon = showIcon,
                icon = icon,
                iconVector = iconVector,
                iconComposable = iconComposable,
                activeIcon = activeIcon,
                activeIconVector = activeIconVector,
                activeIconComposable = activeIconComposable,
                isMenuOpen = isMenuOpen && isMenuVariant,
                disabled = disabled,
                backgroundColor = backgroundColor,
                contentColor = contentColor,
                elevation = elevation,
                animated = animated,
                labelStyle = labelStyle,
                tooltip = tooltip,
                contentDescription = contentDescription,
                onClick = handleMainPress
            )
        }
    }

    // Positioning and Scrim Layout
    if (position == Ux4gFabPosition.None) {
        Box(modifier = modifier.wrapContentSize()) {
            fabContent()
        }
    } else {
        val overlayAlignment = when (position) {
            Ux4gFabPosition.BottomRight -> Alignment.BottomEnd
            Ux4gFabPosition.BottomLeft -> Alignment.BottomStart
            Ux4gFabPosition.BottomCenter -> Alignment.BottomCenter
            Ux4gFabPosition.TopRight -> Alignment.TopEnd
            Ux4gFabPosition.TopLeft -> Alignment.TopStart
            Ux4gFabPosition.None -> Alignment.BottomEnd
        }

        Box(modifier = modifier.fillMaxSize()) {
            if (showBackdrop && isMenuVariant && isMenuOpen) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.35f))
                        .clickable(
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() },
                            onClick = handleBackdropPress
                        )
                )
            }

            Box(
                modifier = Modifier
                    .align(overlayAlignment)
                    .padding(offset)
            ) {
                fabContent()
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// MAIN BUTTON
// ─────────────────────────────────────────────────────────────────────────────

@Composable
private fun Ux4gFabMainButton(
    variant: Ux4gFabVariant,
    colorTheme: Ux4gFabColorTheme,
    size: Ux4gFabSize,
    label: String,
    showIcon: Boolean,
    icon: Ux4gIcon?,
    iconVector: ImageVector?,
    iconComposable: (@Composable () -> Unit)?,
    activeIcon: Ux4gIcon?,
    activeIconVector: ImageVector?,
    activeIconComposable: (@Composable () -> Unit)?,
    isMenuOpen: Boolean,
    disabled: Boolean,
    backgroundColor: Color?,
    contentColor: Color?,
    elevation: Dp?,
    animated: Boolean,
    labelStyle: TextStyle?,
    tooltip: String?,
    contentDescription: String?,
    onClick: () -> Unit
) {
    val isBrand = colorTheme == Ux4gFabColorTheme.Brand
    val isLabelled = variant == Ux4gFabVariant.Labelled

    val defaultBg = if (isBrand) Ux4gColors.primary600 else Ux4gColors.neutral0
    val defaultFg = if (isBrand) Color.White else Ux4gColors.primary600

    val mainBg = backgroundColor ?: defaultBg
    val mainFg = contentColor ?: defaultFg

    val diameter = Ux4gFabDefaults.diameter(size)
    val iconSize = Ux4gFabDefaults.iconSize(size)
    val fontSize = Ux4gFabDefaults.fontSize(size)

    val btnHeight = if (isLabelled) diameter * 0.86f else diameter
    val cornerRadius = btnHeight / 2
    val buttonShape = RoundedCornerShape(cornerRadius)

    val elevationVal = elevation ?: if (isBrand) 4.dp else 3.dp
    val shadowColor = if (isBrand) Ux4gColors.primary600.copy(alpha = 0.35f) else Color.Black.copy(alpha = 0.12f)

    // Rotation animation for active speed-dial icon
    val rotation by animateFloatAsState(
        targetValue = if (animated && isMenuOpen) 45f else 0f,
        animationSpec = tween(durationMillis = 200, easing = FastOutSlowInEasing),
        label = "fab_rotation"
    )

    Surface(
        modifier = Modifier
            .alpha(if (disabled) 0.45f else 1.0f)
            .shadow(
                elevation = elevationVal,
                shape = buttonShape,
                spotColor = shadowColor,
                ambientColor = shadowColor
            )
            .clip(buttonShape)
            .then(
                if (!isBrand) {
                    Modifier.border(width = 1.dp, color = Color(0xFFE2E8F0), shape = buttonShape)
                } else Modifier
            )
            .clickable(
                enabled = !disabled,
                role = Role.Button,
                onClick = onClick
            )
            .semantics {
                this.role = Role.Button
                this.contentDescription = contentDescription ?: tooltip ?: if (label.isNotEmpty()) label else "Floating action button"
            },
        shape = buttonShape,
        color = mainBg,
        contentColor = mainFg
    ) {
        if (isLabelled) {
            Row(
                modifier = Modifier
                    .height(btnHeight)
                    .padding(horizontal = 22.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                if (showIcon) {
                    Ux4gFabIconContent(
                        icon = if (isMenuOpen) (activeIcon ?: icon) else icon,
                        iconVector = if (isMenuOpen) (activeIconVector ?: iconVector) else iconVector,
                        iconComposable = if (isMenuOpen) (activeIconComposable ?: iconComposable) else iconComposable,
                        tint = mainFg,
                        size = iconSize,
                        modifier = Modifier.rotate(if (isMenuOpen && activeIconVector == null && activeIcon == null) rotation else 0f)
                    )
                    if (label.isNotEmpty()) {
                        Spacer(modifier = Modifier.width(8.dp))
                    }
                }
                if (label.isNotEmpty()) {
                    Text(
                        text = label,
                        style = labelStyle ?: TextStyle(
                            fontSize = fontSize,
                            fontWeight = FontWeight.SemiBold,
                            color = mainFg
                        )
                    )
                }
            }
        } else {
            Box(
                modifier = Modifier
                    .size(diameter)
                    .graphicsLayer {
                        if (animated && isMenuOpen && activeIconVector == null && activeIcon == null && activeIconComposable == null) {
                            rotationZ = rotation
                        }
                    },
                contentAlignment = Alignment.Center
            ) {
                if (isMenuOpen) {
                    Ux4gFabIconContent(
                        icon = activeIcon ?: icon,
                        iconVector = activeIconVector ?: iconVector,
                        iconComposable = activeIconComposable ?: iconComposable,
                        tint = mainFg,
                        size = iconSize
                    )
                } else {
                    Ux4gFabIconContent(
                        icon = icon,
                        iconVector = iconVector,
                        iconComposable = iconComposable,
                        tint = mainFg,
                        size = iconSize
                    )
                }
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// SPEED-DIAL MENUS
// ─────────────────────────────────────────────────────────────────────────────

/**
 * Joined capsule speed-dial menu variant (`Ux4gFabVariant.MenuJoined`).
 * Vertical white/surface capsule containing circular action buttons.
 */
@Composable
private fun Ux4gFabJoinedMenu(
    actions: List<Ux4gFabActionItem>,
    onActionClick: (Ux4gFabActionItem) -> Unit
) {
    val borderColor = Color(0xFFE2E8F0)
    val capsuleShape = RoundedCornerShape(24.dp)

    Surface(
        shape = capsuleShape,
        color = Ux4gColors.neutral0,
        tonalElevation = 0.dp,
        shadowElevation = 4.dp,
        modifier = Modifier
            .border(width = 1.dp, color = borderColor, shape = capsuleShape)
            .shadow(elevation = 4.dp, shape = capsuleShape, spotColor = Color.Black.copy(alpha = 0.10f))
    ) {
        Column(
            modifier = Modifier.padding(vertical = 6.dp, horizontal = 4.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            actions.forEach { item ->
                val iconColor = item.contentColor ?: Color(0xFF262626)

                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .padding(2.dp)
                        .alpha(if (item.disabled) 0.45f else 1.0f)
                        .clip(CircleShape)
                        .clickable(
                            enabled = !item.disabled,
                            role = Role.Button,
                            onClick = { onActionClick(item) }
                        )
                        .semantics {
                            role = Role.Button
                            contentDescription = item.tooltip ?: item.label ?: item.id
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Ux4gFabIconContent(
                        icon = item.icon,
                        iconVector = item.iconVector ?: Icons.Filled.Add,
                        iconComposable = item.iconComposable,
                        tint = iconColor,
                        size = 20.dp
                    )
                }
            }
        }
    }
}

/**
 * Spaced circular buttons speed-dial menu variant (`Ux4gFabVariant.MenuSpaced`).
 * Column of individual 44x44dp circular buttons.
 */
@Composable
private fun Ux4gFabSpacedMenu(
    actions: List<Ux4gFabActionItem>,
    colorTheme: Ux4gFabColorTheme,
    alignment: Alignment.Horizontal,
    onActionClick: (Ux4gFabActionItem) -> Unit
) {
    val isBrand = colorTheme == Ux4gFabColorTheme.Brand
    val borderColor = Color(0xFFE2E8F0)

    Column(
        horizontalAlignment = alignment,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        actions.forEach { item ->
            val itemBg = item.backgroundColor ?: Ux4gColors.neutral0
            val itemFg = item.contentColor ?: (if (isBrand) Color(0xFF262626) else Ux4gColors.primary600)

            Surface(
                modifier = Modifier
                    .size(44.dp)
                    .alpha(if (item.disabled) 0.45f else 1.0f)
                    .shadow(elevation = 3.dp, shape = CircleShape, spotColor = Color.Black.copy(alpha = 0.12f))
                    .clip(CircleShape)
                    .border(width = 1.dp, color = borderColor, shape = CircleShape)
                    .clickable(
                        enabled = !item.disabled,
                        role = Role.Button,
                        onClick = { onActionClick(item) }
                    )
                    .semantics {
                        role = Role.Button
                        contentDescription = item.tooltip ?: item.label ?: item.id
                    },
                shape = CircleShape,
                color = itemBg
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Ux4gFabIconContent(
                        icon = item.icon,
                        iconVector = item.iconVector ?: Icons.Filled.Add,
                        iconComposable = item.iconComposable,
                        tint = itemFg,
                        size = 20.dp
                    )
                }
            }
        }
    }
}

/**
 * Labelled pill buttons speed-dial menu variant (`Ux4gFabVariant.MenuLabelled`).
 * Column of individual pill buttons with icon and text label.
 */
@Composable
private fun Ux4gFabLabelledMenu(
    actions: List<Ux4gFabActionItem>,
    colorTheme: Ux4gFabColorTheme,
    alignment: Alignment.Horizontal,
    onActionClick: (Ux4gFabActionItem) -> Unit
) {
    val isBrand = colorTheme == Ux4gFabColorTheme.Brand
    val borderColor = Color(0xFFE2E8F0)
    val pillShape = RoundedCornerShape(999.dp)

    Column(
        horizontalAlignment = alignment,
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        actions.forEach { item ->
            val itemBg = item.backgroundColor ?: Ux4gColors.neutral0
            val itemFg = item.contentColor ?: (if (isBrand) Ux4gColors.primary600 else Color(0xFF171717))

            Surface(
                modifier = Modifier
                    .alpha(if (item.disabled) 0.45f else 1.0f)
                    .shadow(elevation = 2.5.dp, shape = pillShape, spotColor = Color.Black.copy(alpha = 0.10f))
                    .clip(pillShape)
                    .border(width = 1.dp, color = borderColor, shape = pillShape)
                    .clickable(
                        enabled = !item.disabled,
                        role = Role.Button,
                        onClick = { onActionClick(item) }
                    )
                    .semantics {
                        role = Role.Button
                        contentDescription = item.tooltip ?: item.label ?: item.id
                    },
                shape = pillShape,
                color = itemBg
            ) {
                Row(
                    modifier = Modifier.padding(vertical = 9.dp, horizontal = 18.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    if (item.iconComposable != null || item.icon != null || item.iconVector != null) {
                        Ux4gFabIconContent(
                            icon = item.icon,
                            iconVector = item.iconVector,
                            iconComposable = item.iconComposable,
                            tint = itemFg,
                            size = 18.dp
                        )
                        if (!item.label.isNullOrEmpty()) {
                            Spacer(modifier = Modifier.width(8.dp))
                        }
                    }
                    if (!item.label.isNullOrEmpty()) {
                        Text(
                            text = item.label,
                            style = TextStyle(
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium,
                                color = itemFg
                            )
                        )
                    }
                }
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// ICON HELPER
// ─────────────────────────────────────────────────────────────────────────────

@Composable
private fun Ux4gFabIconContent(
    icon: Ux4gIcon?,
    iconVector: ImageVector?,
    iconComposable: (@Composable () -> Unit)?,
    tint: Color,
    size: Dp,
    modifier: Modifier = Modifier
) {
    if (iconComposable != null) {
        Box(modifier = modifier.size(size), contentAlignment = Alignment.Center) {
            iconComposable()
        }
    } else if (icon != null) {
        Ux4gIconComponent(
            icon = icon,
            contentDescription = null,
            modifier = modifier.size(size),
            tint = tint
        )
    } else if (iconVector != null) {
        Icon(
            imageVector = iconVector,
            contentDescription = null,
            modifier = modifier.size(size),
            tint = tint
        )
    } else {
        Icon(
            imageVector = Icons.Filled.Add,
            contentDescription = null,
            modifier = modifier.size(size),
            tint = tint
        )
    }
}
