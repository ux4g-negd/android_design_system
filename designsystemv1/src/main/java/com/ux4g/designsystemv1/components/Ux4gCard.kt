package com.ux4g.designsystemv1.components

import android.content.res.Configuration
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.ux4g.designsystemv1.components.avatar_component.Ux4gAvatar
import com.ux4g.designsystemv1.components.avatar_component.Ux4gAvatarSize
import com.ux4g.designsystemv1.components.buttons.Ux4gButton
import com.ux4g.designsystemv1.components.buttons.Ux4gOutlineButton
import com.ux4g.designsystemv1.foundation.Ux4gColors
import com.ux4g.designsystemv1.foundation.Ux4gRadius
import com.ux4g.designsystemv1.foundation.Ux4gSpace
import com.ux4g.designsystemv1.theme.Ux4gTheme

// ─────────────────────────────────────────────────────────────────────────────
// Enums
// ─────────────────────────────────────────────────────────────────────────────

/** Layout direction of the card. */
enum class Ux4gCardDirection { Vertical, Horizontal }

/** Footer button configuration. */
enum class Ux4gCardFooterType {
    None,
    PrimaryOnly,
    SecondaryOnly,
    PrimaryAndSecondary
}

/** Horizontal alignment of footer buttons. */
enum class Ux4gCardFooterAlignment { Left, Centered, Right }

// ─────────────────────────────────────────────────────────────────────────────
// Rich card composable
// ─────────────────────────────────────────────────────────────────────────────

/**
 * Rich, configurable Card component following Ux4G Design System specs.
 *
 * ─── APPEARANCE ───────────────────────────────────────────────────────────────
 * @param modifier         Root modifier.
 * @param cornerRadius     Corner radius of the card (default [Ux4gRadius.Radius12]).
 * @param backgroundColor  Card background colour. Defaults to theme surface.
 * @param borderColor      Optional border colour (transparent = no border).
 * @param borderWidth      Width of the optional border stroke.
 * @param elevation        Drop-shadow elevation (dp).
 * @param isClickable      When true the whole card surface is tappable.
 * @param onClick          Callback for whole-card click when [isClickable]=true.
 *
 * ─── HEADER IMAGE ─────────────────────────────────────────────────────────────
 * @param mediaImageUrl    URL of a header image. When provided the image is displayed
 *                         fullbleed at the top of the card (Vertical) or left side (Horizontal).
 * @param mediaHeight      Height of the media area in Vertical layout (default 180 dp).
 * @param mediaWidth       Width  of the media area in Horizontal layout (default 120 dp).
 * @param mediaLabelText   Optional label badge overlaid top-left on the image.
 *
 * ─── AVATAR ───────────────────────────────────────────────────────────────────
 * @param avatar           Pass any composable here (e.g. `{ Ux4gAvatar(initials = "JD") }`).
 *                         When `null` the avatar slot is completely hidden.
 *
 * ─── HEADER TEXT ──────────────────────────────────────────────────────────────
 * @param title            Primary title.
 * @param subtitle         Secondary subtitle shown below title.
 *
 * ─── STATUS CHIPS (inline tag row below title) ────────────────────────────────
 * @param statusChips      List of label strings. Rendered as a horizontally-scrollable
 *                         chip row directly below the title/subtitle block.
 *
 * ─── BODY ─────────────────────────────────────────────────────────────────────
 * @param bodyText         Plain body / description text (up to 4 lines, ellipsized).
 *
 * ─── BOTTOM CHIPS (tag row below body text) ───────────────────────────────────
 * @param bottomChips      List of label strings. Rendered as a horizontally-scrollable
 *                         chip row below the body text.
 *
 * ─── FOOTER ───────────────────────────────────────────────────────────────────
 * @param footerType       Which buttons to show. Default [Ux4gCardFooterType.None].
 * @param footerAlignment  Alignment of footer buttons.
 * @param primaryButtonText   Label for the filled primary button.
 * @param secondaryButtonText Label for the outline secondary button.
 * @param onPrimaryClick   Callback for primary button.
 * @param onSecondaryClick Callback for secondary button.
 *
 * ─── LAYOUT ───────────────────────────────────────────────────────────────────
 * @param direction        [Ux4gCardDirection.Vertical] (image top, default) or [Horizontal].
 */
@Composable
fun Ux4gCard(
    modifier: Modifier = Modifier,
    // ── Appearance ──────────────────────────────────────────────────────────
    cornerRadius: Dp = Ux4gRadius.Radius12,
    backgroundColor: Color? = null,
    borderColor: Color = Color.Transparent,
    borderWidth: Dp = 0.dp,
    elevation: Dp = 0.dp,
    isClickable: Boolean = false,
    onClick: () -> Unit = {},
    // ── Direction ───────────────────────────────────────────────────────────
    direction: Ux4gCardDirection = Ux4gCardDirection.Vertical,
    // ── Header image ────────────────────────────────────────────────────────
    mediaImageUrl: String? = null,
    mediaHeight: Dp = 180.dp,
    mediaWidth: Dp = 120.dp,
    mediaLabelText: String? = null,
    // ── Avatar ──────────────────────────────────────────────────────────────
    avatar: (@Composable () -> Unit)? = null,
    // ── Header text ─────────────────────────────────────────────────────────
    title: String? = null,
    subtitle: String? = null,
    // ── Status chips ────────────────────────────────────────────────────────
    statusChips: List<String> = emptyList(),
    // ── Body ────────────────────────────────────────────────────────────────
    bodyText: String? = null,
    // ── Bottom chips ────────────────────────────────────────────────────────
    bottomChips: List<String> = emptyList(),
    // ── Footer ──────────────────────────────────────────────────────────────
    footerType: Ux4gCardFooterType = Ux4gCardFooterType.None,
    footerAlignment: Ux4gCardFooterAlignment = Ux4gCardFooterAlignment.Left,
    onPrimaryClick: () -> Unit = {},
    onSecondaryClick: () -> Unit = {},
    primaryButtonText: String = "Confirm",
    secondaryButtonText: String = "Cancel"
) {
    val shape = RoundedCornerShape(cornerRadius)
    val resolvedBg = backgroundColor ?: Ux4gTheme.colorScheme.surface

    val hasBorder = borderWidth > 0.dp && borderColor != Color.Transparent
    val borderStroke = if (hasBorder) BorderStroke(borderWidth, borderColor) else null

    val hasMedia = mediaImageUrl != null

    // Click modifier only when requested
    val clickModifier = if (isClickable) Modifier.clickable(onClick = onClick) else Modifier

    Card(
        modifier = modifier.then(clickModifier),
        shape = shape,
        colors = CardDefaults.cardColors(
            containerColor = resolvedBg,
            contentColor = Ux4gTheme.colorScheme.onSurface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = elevation),
        border = borderStroke
    ) {
        when (direction) {
            Ux4gCardDirection.Horizontal -> CardHorizontalLayout(
                hasMedia = hasMedia,
                mediaImageUrl = mediaImageUrl,
                mediaWidth = mediaWidth,
                mediaLabelText = mediaLabelText,
                avatar = avatar,
                title = title,
                subtitle = subtitle,
                statusChips = statusChips,
                bodyText = bodyText,
                bottomChips = bottomChips,
                footerType = footerType,
                footerAlignment = footerAlignment,
                primaryButtonText = primaryButtonText,
                secondaryButtonText = secondaryButtonText,
                onPrimaryClick = onPrimaryClick,
                onSecondaryClick = onSecondaryClick
            )
            Ux4gCardDirection.Vertical -> CardVerticalLayout(
                hasMedia = hasMedia,
                mediaImageUrl = mediaImageUrl,
                mediaHeight = mediaHeight,
                mediaLabelText = mediaLabelText,
                avatar = avatar,
                title = title,
                subtitle = subtitle,
                statusChips = statusChips,
                bodyText = bodyText,
                bottomChips = bottomChips,
                footerType = footerType,
                footerAlignment = footerAlignment,
                primaryButtonText = primaryButtonText,
                secondaryButtonText = secondaryButtonText,
                onPrimaryClick = onPrimaryClick,
                onSecondaryClick = onSecondaryClick
            )
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Vertical layout (image on top)
// ─────────────────────────────────────────────────────────────────────────────

@Composable
private fun CardVerticalLayout(
    hasMedia: Boolean,
    mediaImageUrl: String?,
    mediaHeight: Dp,
    mediaLabelText: String?,
    avatar: (@Composable () -> Unit)?,
    title: String?,
    subtitle: String?,
    statusChips: List<String>,
    bodyText: String?,
    bottomChips: List<String>,
    footerType: Ux4gCardFooterType,
    footerAlignment: Ux4gCardFooterAlignment,
    primaryButtonText: String,
    secondaryButtonText: String,
    onPrimaryClick: () -> Unit,
    onSecondaryClick: () -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        // Media area
        if (hasMedia) {
            CardMediaImage(
                imageUrl = mediaImageUrl,
                labelText = mediaLabelText,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(mediaHeight)
            )
        }
        // Content
        Column(modifier = Modifier.padding(Ux4gSpace.Space16)) {
            CardContentBlock(
                avatar = avatar,
                title = title,
                subtitle = subtitle,
                statusChips = statusChips,
                bodyText = bodyText,
                bottomChips = bottomChips,
                footerType = footerType,
                footerAlignment = footerAlignment,
                primaryButtonText = primaryButtonText,
                secondaryButtonText = secondaryButtonText,
                onPrimaryClick = onPrimaryClick,
                onSecondaryClick = onSecondaryClick
            )
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Horizontal layout (image on the left)
// ─────────────────────────────────────────────────────────────────────────────

@Composable
private fun CardHorizontalLayout(
    hasMedia: Boolean,
    mediaImageUrl: String?,
    mediaWidth: Dp,
    mediaLabelText: String?,
    avatar: (@Composable () -> Unit)?,
    title: String?,
    subtitle: String?,
    statusChips: List<String>,
    bodyText: String?,
    bottomChips: List<String>,
    footerType: Ux4gCardFooterType,
    footerAlignment: Ux4gCardFooterAlignment,
    primaryButtonText: String,
    secondaryButtonText: String,
    onPrimaryClick: () -> Unit,
    onSecondaryClick: () -> Unit
) {
    Row(modifier = Modifier.fillMaxWidth()) {
        if (hasMedia) {
            CardMediaImage(
                imageUrl = mediaImageUrl,
                labelText = mediaLabelText,
                modifier = Modifier
                    .width(mediaWidth)
                    .fillMaxSize()
            )
        }
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(Ux4gSpace.Space16)
        ) {
            CardContentBlock(
                avatar = avatar,
                title = title,
                subtitle = subtitle,
                statusChips = statusChips,
                bodyText = bodyText,
                bottomChips = bottomChips,
                footerType = footerType,
                footerAlignment = footerAlignment,
                primaryButtonText = primaryButtonText,
                secondaryButtonText = secondaryButtonText,
                onPrimaryClick = onPrimaryClick,
                onSecondaryClick = onSecondaryClick
            )
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Header image with optional label badge
// ─────────────────────────────────────────────────────────────────────────────

@Composable
private fun CardMediaImage(
    imageUrl: String?,
    labelText: String?,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier) {
        if (imageUrl != null) {
            AsyncImage(
                model = imageUrl,
                contentDescription = "Card Image",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }
        // Label badge overlay — top-left
        if (labelText != null) {
            Box(
                modifier = Modifier
                    .padding(Ux4gSpace.Space8)
                    .align(Alignment.TopStart)
            ) {
                Ux4gBadge(
                    type = Ux4gBadgeType.Label(labelText),
                    containerColor = Ux4gTheme.colorScheme.onSurface,
                    contentColor = Ux4gColors.white
                )
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Unified content block (avatar + title + chips + body + footer)
// ─────────────────────────────────────────────────────────────────────────────

@Composable
private fun ColumnScope.CardContentBlock(
    avatar: (@Composable () -> Unit)?,
    title: String?,
    subtitle: String?,
    statusChips: List<String>,
    bodyText: String?,
    bottomChips: List<String>,
    footerType: Ux4gCardFooterType,
    footerAlignment: Ux4gCardFooterAlignment,
    primaryButtonText: String,
    secondaryButtonText: String,
    onPrimaryClick: () -> Unit,
    onSecondaryClick: () -> Unit
) {
    // ── Avatar + Title/Subtitle row ────────────────────────────────────────
    val hasHeader = avatar != null || title != null || subtitle != null
    if (hasHeader) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(Ux4gSpace.Space12),
            modifier = Modifier.fillMaxWidth()
        ) {
            // Avatar — only rendered when caller passes one
            if (avatar != null) {
                avatar()
            }
            // Title + Subtitle
            Column(modifier = Modifier.weight(1f)) {
                if (title != null) {
                    Text(
                        text = title,
                        style = Ux4gTheme.typography.tS_strong,
                        color = Ux4gTheme.colorScheme.onSurface,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                if (subtitle != null) {
                    Text(
                        text = subtitle,
                        style = Ux4gTheme.typography.bS_default,
                        color = Ux4gTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }

    // ── Status chips — horizontal scroll, separator-separated ─────────────
    if (statusChips.isNotEmpty()) {
        Spacer(modifier = Modifier.height(Ux4gSpace.Space8))
        CardChipRow(chips = statusChips, pillStyle = false)
    }

    // ── Body text ──────────────────────────────────────────────────────────
    if (bodyText != null) {
        Spacer(modifier = Modifier.height(Ux4gSpace.Space12))
        Text(
            text = bodyText,
            style = Ux4gTheme.typography.bM_default,
            color = Ux4gTheme.colorScheme.onSurface.copy(alpha = 0.7f),
            maxLines = 4,
            overflow = TextOverflow.Ellipsis
        )
    }

    // ── Bottom chips — horizontal scroll, pill-style ───────────────────────
    if (bottomChips.isNotEmpty()) {
        Spacer(modifier = Modifier.height(Ux4gSpace.Space12))
        CardChipRow(chips = bottomChips, pillStyle = true)
    }

    // ── Footer ─────────────────────────────────────────────────────────────
    if (footerType != Ux4gCardFooterType.None) {
        Spacer(modifier = Modifier.height(Ux4gSpace.Space20))
        CardFooterRow(
            footerType = footerType,
            footerAlignment = footerAlignment,
            primaryButtonText = primaryButtonText,
            secondaryButtonText = secondaryButtonText,
            onPrimaryClick = onPrimaryClick,
            onSecondaryClick = onSecondaryClick
        )
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Chip row — horizontal scrollable
// ─────────────────────────────────────────────────────────────────────────────

/**
 * Renders [chips] as a single horizontally-scrollable row.
 *
 * @param pillStyle When `true` chips are rendered as rounded pill badges (bottom chips).
 *                  When `false` they appear as plain inline labels separated by "│" (status chips).
 */
@Composable
private fun CardChipRow(
    chips: List<String>,
    pillStyle: Boolean
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(Ux4gSpace.Space6),
        verticalAlignment = Alignment.CenterVertically
    ) {
        chips.forEachIndexed { index, chip ->
            if (pillStyle) {
                // Pill chip (bottom tags style)
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(Ux4gRadius.Radius999))
                        .border(
                            1.dp,
                            Ux4gTheme.colorScheme.onSurface.copy(alpha = 0.3f),
                            RoundedCornerShape(Ux4gRadius.Radius999)
                        )
                        .padding(
                            horizontal = Ux4gSpace.Space12,
                            vertical = Ux4gSpace.Space4
                        )
                ) {
                    Text(
                        text = chip,
                        style = Ux4gTheme.typography.lS_default,
                        color = Ux4gTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                }
            } else {
                // Inline status label with separator
                if (index > 0) {
                    Text(
                        text = "│",
                        color = Ux4gTheme.colorScheme.onSurface.copy(alpha = 0.3f),
                        style = Ux4gTheme.typography.bS_default
                    )
                }
                Text(
                    text = chip,
                    style = Ux4gTheme.typography.lS_default,
                    color = Ux4gTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Footer row
// ─────────────────────────────────────────────────────────────────────────────

@Composable
private fun CardFooterRow(
    footerType: Ux4gCardFooterType,
    footerAlignment: Ux4gCardFooterAlignment,
    primaryButtonText: String,
    secondaryButtonText: String,
    onPrimaryClick: () -> Unit,
    onSecondaryClick: () -> Unit
) {
    val arrangement = when (footerAlignment) {
        Ux4gCardFooterAlignment.Left -> Arrangement.Start
        Ux4gCardFooterAlignment.Centered -> Arrangement.Center
        Ux4gCardFooterAlignment.Right -> Arrangement.End
    }

    val fillWidth =
        footerAlignment == Ux4gCardFooterAlignment.Centered &&
                footerType != Ux4gCardFooterType.PrimaryAndSecondary

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = arrangement,
        verticalAlignment = Alignment.CenterVertically
    ) {
        when (footerType) {
            Ux4gCardFooterType.PrimaryAndSecondary -> {
                Ux4gButton(
                    onClick = onPrimaryClick,
                    contentPadding = PaddingValues(
                        horizontal = Ux4gSpace.Space20,
                        vertical = Ux4gSpace.Space12
                    )
                ) { Text(primaryButtonText) }
                Spacer(modifier = Modifier.width(Ux4gSpace.Space12))
                Ux4gOutlineButton(
                    onClick = onSecondaryClick,
                    contentPadding = PaddingValues(
                        horizontal = Ux4gSpace.Space20,
                        vertical = Ux4gSpace.Space12
                    )
                ) { Text(secondaryButtonText) }
            }
            Ux4gCardFooterType.PrimaryOnly -> {
                Ux4gButton(
                    onClick = onPrimaryClick,
                    modifier = if (fillWidth) Modifier.fillMaxWidth() else Modifier,
                    contentPadding = PaddingValues(
                        horizontal = Ux4gSpace.Space20,
                        vertical = Ux4gSpace.Space12
                    )
                ) { Text(primaryButtonText) }
            }
            Ux4gCardFooterType.SecondaryOnly -> {
                Ux4gOutlineButton(
                    onClick = onSecondaryClick,
                    modifier = if (fillWidth) Modifier.fillMaxWidth() else Modifier,
                    contentPadding = PaddingValues(
                        horizontal = Ux4gSpace.Space20,
                        vertical = Ux4gSpace.Space12
                    )
                ) { Text(secondaryButtonText) }
            }
            Ux4gCardFooterType.None -> Unit
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Simple container overload — backward compatible trailing-lambda pattern
// ─────────────────────────────────────────────────────────────────────────────

/**
 * Simple container overload of [Ux4gCard].
 *
 * Use this when you want a basic styled card surface with free-form [content] inside.
 * This preserves backward-compatibility with all existing usages throughout the app.
 *
 * ```kotlin
 * Ux4gCard(modifier = Modifier.fillMaxWidth()) {
 *     Column(modifier = Modifier.padding(16.dp)) {
 *         Text("Hello World")
 *     }
 * }
 * ```
 */
@Composable
fun Ux4gCard(
    modifier: Modifier = Modifier,
    cornerRadius: Dp = Ux4gRadius.Radius12,
    backgroundColor: Color? = null,
    borderColor: Color = Color.Transparent,
    borderWidth: Dp = 0.dp,
    elevation: Dp = 0.dp,
    onClick: (() -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit
) {
    val shape = RoundedCornerShape(cornerRadius)
    val resolvedBg = backgroundColor ?: Ux4gTheme.colorScheme.surface
    val hasBorder = borderWidth > 0.dp && borderColor != Color.Transparent
    val borderStroke = if (hasBorder) BorderStroke(borderWidth, borderColor) else null

    Card(
        modifier = modifier.then(
            if (onClick != null) Modifier.clickable(onClick = onClick) else Modifier
        ),
        shape = shape,
        colors = CardDefaults.cardColors(
            containerColor = resolvedBg,
            contentColor = Ux4gTheme.colorScheme.onSurface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = elevation),
        border = borderStroke
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            content()
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Previews
// ─────────────────────────────────────────────────────────────────────────────

@Preview(name = "Light", showBackground = true)
@Preview(name = "Dark", uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
private fun Ux4gCardPreview() {
    Ux4gTheme {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Ux4gTheme.colorScheme.onSurface.copy(alpha = 0.1f))
                .padding(Ux4gSpace.Space24),
            verticalArrangement = Arrangement.spacedBy(Ux4gSpace.Space24)
        ) {

            // ── Full card with image, avatar, chips, footer ──
            Ux4gCard(
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
                statusChips = listOf("Status A", "Pending", "Active"),
                bodyText = "Lorem ipsum is a dummy or placeholder text commonly used in graphic design, publishing, and web development.",
                bottomChips = listOf("Label", "Label", "Label", "Label"),
                footerType = Ux4gCardFooterType.PrimaryAndSecondary,
                footerAlignment = Ux4gCardFooterAlignment.Centered,
                primaryButtonText = "Button",
                secondaryButtonText = "Button"
            )

            // ── Card with outlined border, no image, no avatar ──
            Ux4gCard(
                borderColor = Ux4gColors.primary,
                borderWidth = 1.dp,
                title = "Outlined Card",
                subtitle = "No image, no avatar",
                bodyText = "This card has a primary-coloured border and no media or avatar.",
                statusChips = listOf("Tag 1", "Tag 2"),
                footerType = Ux4gCardFooterType.SecondaryOnly,
                footerAlignment = Ux4gCardFooterAlignment.Left,
                secondaryButtonText = "Learn More"
            )

            // ── Elevated card, primary only, right-aligned footer ──
            Ux4gCard(
                elevation = 4.dp,
                backgroundColor = Ux4gColors.primary50,
                title = "Elevated Card",
                bodyText = "This card has a custom background color and elevated shadow.",
                bottomChips = listOf("Kotlin", "Compose", "Ux4G"),
                footerType = Ux4gCardFooterType.PrimaryOnly,
                footerAlignment = Ux4gCardFooterAlignment.Right,
                primaryButtonText = "Get Started"
            )
        }
    }
}
