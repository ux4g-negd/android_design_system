package com.ux4g.designsystemv1.components

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ux4g.designsystemv1.components.avatar_component.Ux4gAvatar
import com.ux4g.designsystemv1.components.avatar_component.Ux4gAvatarSize
import com.ux4g.designsystemv1.foundation.Ux4gColors
import com.ux4g.designsystemv1.foundation.Ux4gIcon
import com.ux4g.designsystemv1.foundation.Ux4gIconComponent
import com.ux4g.designsystemv1.foundation.Ux4gIcons
import com.ux4g.designsystemv1.foundation.Ux4gSpace
import com.ux4g.designsystemv1.theme.Ux4gTheme

/**
 * Visual variant for [Ux4gAppHeader].
 */
enum class Ux4gAppHeaderVariant {
    /** Surface background with dark text and icons. */
    Light,

    /** Primary (purple) background with white text and icons. */
    Filled,

    /** Surface background with a subtle bottom border. */
    Outlined
}

/**
 * A single action item in the header's trailing section.
 */
data class Ux4gAppHeaderAction(
    val icon: Ux4gIcon? = Ux4gIcons.Settings,
    val onClick: (() -> Unit)? = null,
    val tooltip: String? = null,
    val customWidget: (@Composable () -> Unit)? = null
)

/**
 * A configurable app bar / header component for the UX4G Design System.
 *
 * Supports:
 * - Leading section: back button, list of custom leading widgets, or custom composable (emblem, logo, etc.)
 * - Title (Text or custom Composable)
 * - Trailing actions: configurable list of icon buttons or custom widgets
 * - Avatar: optional profile avatar at trailing end
 * - 3 visual variants: [Light], [Filled], [Outlined]
 * - Status bar padding support
 *
 * @param title Title text displayed in the header.
 * @param modifier Modifier applied to the outer header container.
 * @param variant Visual variant: [Ux4gAppHeaderVariant.Outlined], [Ux4gAppHeaderVariant.Light], or [Ux4gAppHeaderVariant.Filled].
 * @param showBackButton Whether to show a back button as the leading widget. If true, leading widgets are ignored.
 * @param onBackPressed Callback when back button is clicked.
 * @param leadingWidgets Custom list of leading composables (e.g. Emblem, Divider line, Logo).
 * @param leadingContent Single custom leading composable.
 * @param actions List of trailing action items.
 * @param avatar Optional custom avatar composable at trailing end.
 * @param avatarSize Size of avatar when default [Ux4gAvatar] is rendered via [showAvatar].
 * @param showAvatar Whether to show default [Ux4gAvatar].
 * @param avatarImageUrl Image URL for default avatar.
 * @param avatarInitials Initials for default avatar.
 * @param onAvatarClick Callback when avatar is clicked.
 * @param titleWidget Custom title composable (overrides [title] text).
 * @param titleStyle Title text style override.
 * @param backgroundColor Background color override.
 * @param foregroundColor Text/icon color override.
 * @param borderColor Border color for [Ux4gAppHeaderVariant.Outlined] variant.
 * @param height Height of header content area (default 56.dp).
 * @param horizontalPadding Horizontal content padding (default 12.dp).
 * @param leadingSpacing Spacing between leading items and title.
 * @param actionSpacing Spacing between trailing action items.
 * @param elevation Shadow depth elevation in Dp.
 * @param useSafeArea Whether to apply status bar padding to avoid overlap.
 */
@Composable
fun Ux4gAppHeader(
    modifier: Modifier = Modifier,
    title: String = "Title",
    variant: Ux4gAppHeaderVariant = Ux4gAppHeaderVariant.Outlined,
    showBackButton: Boolean = false,
    onBackPressed: (() -> Unit)? = null,
    leadingWidgets: List<@Composable () -> Unit>? = null,
    leadingContent: (@Composable () -> Unit)? = null,
    actions: List<Ux4gAppHeaderAction>? = null,
    avatar: (@Composable () -> Unit)? = null,
    avatarSize: Ux4gAvatarSize = Ux4gAvatarSize.S,
    showAvatar: Boolean = false,
    avatarImageUrl: Any? = null,
    avatarInitials: String? = null,
    onAvatarClick: (() -> Unit)? = null,
    titleWidget: (@Composable () -> Unit)? = null,
    titleStyle: TextStyle? = null,
    backgroundColor: Color? = null,
    foregroundColor: Color? = null,
    borderColor: Color? = null,
    height: Dp = 56.dp,
    horizontalPadding: Dp = Ux4gSpace.Space12,
    leadingSpacing: Dp = Ux4gSpace.Space8,
    actionSpacing: Dp = Ux4gSpace.Space4,
    elevation: Dp = 0.dp,
    useSafeArea: Boolean = true
) {
    val resolvedBackgroundColor = backgroundColor ?: when (variant) {
        Ux4gAppHeaderVariant.Light,
        Ux4gAppHeaderVariant.Outlined -> Ux4gTheme.colorScheme.surface
        Ux4gAppHeaderVariant.Filled -> Ux4gTheme.colorScheme.primary
    }

    val resolvedForegroundColor = foregroundColor ?: when (variant) {
        Ux4gAppHeaderVariant.Light,
        Ux4gAppHeaderVariant.Outlined -> Ux4gTheme.colorScheme.onSurface
        Ux4gAppHeaderVariant.Filled -> Ux4gTheme.colorScheme.onPrimary
    }

    val resolvedBorderColor = borderColor ?: Ux4gTheme.colorScheme.onSurface.copy(alpha = 0.12f)

    val resolvedTitleStyle = titleStyle ?: Ux4gTheme.typography.tS_strong.copy(
        color = resolvedForegroundColor,
        fontSize = 16.sp,
        fontWeight = FontWeight.SemiBold
    )

    val borderModifier = if (variant == Ux4gAppHeaderVariant.Outlined) {
        Modifier.drawBehind {
            val strokeWidth = 1.dp.toPx()
            drawLine(
                color = resolvedBorderColor,
                start = Offset(0f, size.height - strokeWidth / 2),
                end = Offset(size.width, size.height - strokeWidth / 2),
                strokeWidth = strokeWidth
            )
        }
    } else Modifier

    val elevationModifier = if (elevation > 0.dp) {
        Modifier.shadow(elevation)
    } else Modifier

    val safeAreaModifier = if (useSafeArea) Modifier.statusBarsPadding() else Modifier

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .then(elevationModifier)
            .then(borderModifier),
        color = resolvedBackgroundColor
    ) {
        Box(modifier = safeAreaModifier) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(height)
                    .padding(horizontal = horizontalPadding),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Leading Section
                if (showBackButton) {
                    HeaderIconButton(
                        icon = Ux4gIcon.Vector(Icons.AutoMirrored.Filled.ArrowBack),
                        color = resolvedForegroundColor,
                        onClick = onBackPressed,
                        contentDescription = "Back"
                    )
                    Spacer(modifier = Modifier.width(leadingSpacing))
                } else if (!leadingWidgets.isNullOrEmpty()) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(leadingSpacing)
                    ) {
                        leadingWidgets.forEach { widget ->
                            CompositionLocalProvider(LocalContentColor provides resolvedForegroundColor) {
                                widget()
                            }
                        }
                    }
                    Spacer(modifier = Modifier.width(leadingSpacing))
                } else if (leadingContent != null) {
                    CompositionLocalProvider(LocalContentColor provides resolvedForegroundColor) {
                        leadingContent()
                    }
                    Spacer(modifier = Modifier.width(leadingSpacing))
                }

                // Title Section
                Box(
                    modifier = Modifier.weight(1f),
                    contentAlignment = Alignment.CenterStart
                ) {
                    if (titleWidget != null) {
                        CompositionLocalProvider(LocalContentColor provides resolvedForegroundColor) {
                            titleWidget()
                        }
                    } else {
                        Text(
                            text = title,
                            style = resolvedTitleStyle,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }

                // Trailing Section
                val hasActions = !actions.isNullOrEmpty()
                val hasAvatar = avatar != null || showAvatar

                if (hasActions || hasAvatar) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(actionSpacing)
                    ) {
                        actions?.forEach { action ->
                            if (action.customWidget != null) {
                                CompositionLocalProvider(LocalContentColor provides resolvedForegroundColor) {
                                    action.customWidget.invoke()
                                }
                            } else if (action.icon != null) {
                                HeaderIconButton(
                                    icon = action.icon,
                                    color = resolvedForegroundColor,
                                    onClick = action.onClick,
                                    contentDescription = action.tooltip
                                )
                            }
                        }

                        if (avatar != null) {
                            Box(
                                modifier = Modifier.let { m ->
                                    if (onAvatarClick != null) m.clickable(onClick = onAvatarClick) else m
                                }
                            ) {
                                avatar()
                            }
                        } else if (showAvatar) {
                            Ux4gAvatar(
                                size = avatarSize,
                                imageUrl = avatarImageUrl,
                                initials = avatarInitials,
                                modifier = Modifier.let { m ->
                                    if (onAvatarClick != null) m.clickable(onClick = onAvatarClick) else m
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun HeaderIconButton(
    icon: Ux4gIcon,
    color: Color,
    onClick: (() -> Unit)?,
    contentDescription: String? = null,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .size(40.dp)
            .clip(CircleShape)
            .clickable(
                enabled = onClick != null,
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) { onClick?.invoke() },
        contentAlignment = Alignment.Center
    ) {
        Ux4gIconComponent(
            icon = icon,
            contentDescription = contentDescription,
            modifier = Modifier.size(24.dp),
            tint = color
        )
    }
}

@Preview(name = "AppHeader Showcase", showBackground = true)
@Preview(
    name = "AppHeader Dark Showcase",
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true
)
@Composable
private fun Ux4gAppHeaderPreview() {
    Ux4gTheme {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // 1. Emblem + Divider + Union + "Title" + Settings + Avatar
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
                    leadingWidgets = listOf(
                        {
                            Ux4gIconComponent(
                                icon = Ux4gIcons.NationalEmblem,
                                contentDescription = "National Emblem",
                                modifier = Modifier.size(32.dp),
                                tint = Color.Unspecified
                            )
                        },
                        {
                            Box(
                                modifier = Modifier
                                    .width(1.dp)
                                    .height(24.dp)
                                    .background(Ux4gTheme.colorScheme.onSurface.copy(alpha = 0.12f))
                            )
                        },
                        {
                            Ux4gIconComponent(
                                icon = Ux4gIcons.Union,
                                contentDescription = "Union",
                                modifier = Modifier.size(32.dp),
                                tint = Color.Unspecified
                            )
                        }
                    ),
                    actions = listOf(
                        Ux4gAppHeaderAction(icon = Ux4gIcons.Settings)
                    ),
                    showAvatar = true,
                    avatarInitials = "JD"
                )
            }

            // 2. Emblem + Divider + Union + "Title" + Settings + Hamburger Menu
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
                    leadingWidgets = listOf(
                        {
                            Ux4gIconComponent(
                                icon = Ux4gIcons.NationalEmblem,
                                contentDescription = "National Emblem",
                                modifier = Modifier.size(32.dp),
                                tint = Color.Unspecified
                            )
                        },
                        {
                            Box(
                                modifier = Modifier
                                    .width(1.dp)
                                    .height(24.dp)
                                    .background(Ux4gTheme.colorScheme.onSurface.copy(alpha = 0.12f))
                            )
                        },
                        {
                            Ux4gIconComponent(
                                icon = Ux4gIcons.Union,
                                contentDescription = "Union",
                                modifier = Modifier.size(32.dp),
                                tint = Color.Unspecified
                            )
                        }
                    ),
                    actions = listOf(
                        Ux4gAppHeaderAction(icon = Ux4gIcons.Settings),
                        Ux4gAppHeaderAction(icon = Ux4gIcons.Menu)
                    )
                )
            }

            // 3. Filled (Primary Purple) Variant with Back Button
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
}
