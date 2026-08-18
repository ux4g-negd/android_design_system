package com.ux4g.designsystemv1.components

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import coil.compose.AsyncImage
import com.ux4g.designsystemv1.components.avatar_component.Ux4gAvatar
import com.ux4g.designsystemv1.components.avatar_component.Ux4gAvatarSize
import com.ux4g.designsystemv1.components.buttons.Ux4gButton
import com.ux4g.designsystemv1.components.buttons.Ux4gOutlineButton
import com.ux4g.designsystemv1.foundation.Ux4gColors
import com.ux4g.designsystemv1.foundation.Ux4gIcon
import com.ux4g.designsystemv1.foundation.Ux4gIconComponent
import com.ux4g.designsystemv1.foundation.Ux4gIcons
import com.ux4g.designsystemv1.foundation.Ux4gRadius
import com.ux4g.designsystemv1.foundation.Ux4gSpace
import com.ux4g.designsystemv1.foundation.defaultTypography
import com.ux4g.designsystemv1.theme.Ux4gTheme

// ─────────────────────────────────────────────────────────────────────────────
// Enums
// ─────────────────────────────────────────────────────────────────────────────

/** Content alignment of the modal header and body text. */
enum class Ux4gModalAlignment {
    /** Text starts from the left edge (default). */
    LeftAligned,
    /** Text is centered horizontally. */
    Centered
}

/**
 * Leading item shown in the header area.
 *
 * - [None] – nothing
 * - [Icon] – a small icon to the left of the title (inline in header row)
 * - [Avatar] – a circular avatar with initials / image, placed above the title when
 *              [Ux4gModalAlignment.Centered], or inline left when [Ux4gModalAlignment.LeftAligned]
 * - [Image] – a small rectangular image thumbnail inline in the header
 */
enum class Ux4gModalLeadingItem {
    None,
    Icon,
    Avatar,
    Image
}

/**
 * How the header image at the top is displayed.
 *
 * - [FullBleed] – fills the full card width, clipped to top corners
 * - [Padded] – displayed with horizontal/vertical padding and fully rounded corners
 * - [None] – no image shown
 */
enum class Ux4gModalHeaderImage {
    FullBleed,
    Padded,
    None
}

/** Controls how many buttons appear in the footer. */
enum class Ux4gModalFooterButtons {
    OneButton,
    TwoButtons,
    OneButtonWithIcon,
    TwoButtonsWithIcon
}

/** Horizontal alignment of footer buttons. */
enum class Ux4gModalFooterAlign {
    Left,
    Right,
    Center,
    /** Secondary sits at the left edge, primary at the right edge. */
    Split
}

// ─────────────────────────────────────────────────────────────────────────────
// Main Composable
// ─────────────────────────────────────────────────────────────────────────────

/**
 * A fully-customisable modal dialog matching the UX4G Design System spec.
 *
 * Every section is **optional** – pass `null` / `false` to hide it.
 *
 * @param onDismiss Invoked when the dialog is dismissed or close button clicked.
 * @param headerImageContent Custom composable for the header image.
 * @param headerImageUrl Remote URL for the header image.
 * @param headerImageHeight Height of the header image (default: 160.dp).
 * @param headerImageStyle Style of header image ([FullBleed], [Padded], [None]).
 * @param leadingItem Type of leading element in header ([None], [Icon], [Avatar], [Image]).
 * @param leadingIcon Icon used when [leadingItem] is [Ux4gModalLeadingItem.Icon].
 * @param leadingIconTint Color tint for the leading icon.
 * @param avatarInitials Initials shown inside avatar when [leadingItem] is [Ux4gModalLeadingItem.Avatar].
 * @param avatarImageUrl Remote image URL for avatar.
 * @param avatarIcon Icon for avatar.
 * @param avatarSize Avatar size when centered above header title (default: [Ux4gAvatarSize.L]).
 * @param avatarInlineSize Avatar size when inline beside header title (default: [Ux4gAvatarSize.S]).
 * @param avatarContainerColor Background color for avatar container.
 * @param avatarContentColor Content / text / icon color for avatar.
 * @param leadingImageUrl Remote URL for 40x40 thumbnail when [leadingItem] is [Ux4gModalLeadingItem.Image].
 * @param leadingImageContent Custom composable for thumbnail.
 * @param showHeader Whether to show the header section (default: true).
 * @param headerTitle Title text in header (default: "Header").
 * @param showDescription Whether to show the description line below title.
 * @param descriptionText Description text content.
 * @param showDividers Whether to show top and bottom divider lines (default: true).
 * @param showSubtitle Whether to show the subtitle in body (default: true).
 * @param subtitleText Subtitle text content (default: "Subtitle").
 * @param showBody Whether to show body content / text (default: true).
 * @param bodyText Body text content.
 * @param bodyContent Custom body composable slot (overrides [bodyText]).
 * @param showFooter Whether to show the footer buttons (default: true).
 * @param footerButtons Button configuration ([OneButton], [TwoButtons], [OneButtonWithIcon], [TwoButtonsWithIcon]).
 * @param footerAlign Alignment of footer buttons ([Left], [Right], [Center], [Split]).
 * @param isDestructive Whether the primary action is destructive (error color).
 * @param primaryButtonText Label for primary button.
 * @param secondaryButtonText Label for secondary outline button.
 * @param leadingActionIcon Icon for footer leading action button.
 * @param onPrimaryClick Action when primary button clicked (defaults to [onDismiss]).
 * @param onSecondaryClick Action when secondary button clicked (defaults to [onDismiss]).
 * @param onLeadingIconClick Action when footer leading icon clicked.
 * @param alignment Text & header alignment ([LeftAligned] or [Centered]).
 * @param showCloseButton Whether to display the top-right close button (default: true).
 * @param backgroundColor Dialog card background color (default: white).
 * @param cornerRadius Corner radius of the dialog card (default: 16.dp).
 */
@Composable
fun Ux4gModal(
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,

    // ── Header Image ──────────────────────────────────────────────────────────
    headerImageUrl: String? = null,
    headerImageContent: (@Composable () -> Unit)? = null,
    headerImageHeight: Dp = 160.dp,
    headerImageStyle: Ux4gModalHeaderImage = Ux4gModalHeaderImage.FullBleed,

    // ── Leading Items ─────────────────────────────────────────────────────────
    leadingItem: Ux4gModalLeadingItem = Ux4gModalLeadingItem.None,
    leadingIcon: Ux4gIcon = Ux4gIcons.info_circle,
    leadingIconTint: Color? = null,
    avatarInitials: String? = null,
    avatarImageUrl: Any? = null,
    avatarIcon: Ux4gIcon? = null,
    avatarSize: Ux4gAvatarSize = Ux4gAvatarSize.L,
    avatarInlineSize: Ux4gAvatarSize = Ux4gAvatarSize.S,
    avatarContainerColor: Color? = null,
    avatarContentColor: Color? = null,
    leadingImageUrl: String? = null,
    leadingImageContent: (@Composable () -> Unit)? = null,

    // ── Header ────────────────────────────────────────────────────────────────
    showHeader: Boolean = true,
    headerTitle: String = "Header",

    // ── Description ───────────────────────────────────────────────────────────
    showDescription: Boolean = false,
    descriptionText: String = "Write description here",

    // ── Dividers ──────────────────────────────────────────────────────────────
    showDividers: Boolean = true,

    // ── Body ──────────────────────────────────────────────────────────────────
    showSubtitle: Boolean = true,
    subtitleText: String = "Subtitle",
    showBody: Boolean = true,
    bodyText: String =
        "A modal is a design element that pops up over the main content of a webpage. " +
        "It demands the user's attention by temporarily disabling interaction with the " +
        "rest of the page until the user addresses the content within the modal.",
    bodyContent: (@Composable () -> Unit)? = null,

    // ── Footer ────────────────────────────────────────────────────────────────
    showFooter: Boolean = true,
    footerButtons: Ux4gModalFooterButtons = Ux4gModalFooterButtons.TwoButtons,
    footerAlign: Ux4gModalFooterAlign = Ux4gModalFooterAlign.Right,
    isDestructive: Boolean = false,
    primaryButtonText: String = "Button",
    secondaryButtonText: String = "Button",
    leadingActionIcon: Ux4gIcon = Ux4gIcons.info_circle,
    onPrimaryClick: (() -> Unit)? = null,
    onSecondaryClick: (() -> Unit)? = null,
    onLeadingIconClick: (() -> Unit)? = null,

    // ── Appearance ────────────────────────────────────────────────────────────
    alignment: Ux4gModalAlignment = Ux4gModalAlignment.LeftAligned,
    showCloseButton: Boolean = true,
    backgroundColor: Color? = null,
    cornerRadius: Dp = Ux4gRadius.Radius16
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            dismissOnBackPress = true,
            dismissOnClickOutside = true
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 24.dp),
            contentAlignment = Alignment.Center
        ) {
            Ux4gModalContent(
                modifier = modifier,
                onDismiss = onDismiss,
                headerImageUrl = headerImageUrl,
                headerImageContent = headerImageContent,
                headerImageHeight = headerImageHeight,
                headerImageStyle = headerImageStyle,
                leadingItem = leadingItem,
                leadingIcon = leadingIcon,
                leadingIconTint = leadingIconTint,
                avatarInitials = avatarInitials,
                avatarImageUrl = avatarImageUrl,
                avatarIcon = avatarIcon,
                avatarSize = avatarSize,
                avatarInlineSize = avatarInlineSize,
                avatarContainerColor = avatarContainerColor,
                avatarContentColor = avatarContentColor,
                leadingImageUrl = leadingImageUrl,
                leadingImageContent = leadingImageContent,
                showHeader = showHeader,
                headerTitle = headerTitle,
                showDescription = showDescription,
                descriptionText = descriptionText,
                showDividers = showDividers,
                showSubtitle = showSubtitle,
                subtitleText = subtitleText,
                showBody = showBody,
                bodyText = bodyText,
                bodyContent = bodyContent,
                showFooter = showFooter,
                footerButtons = footerButtons,
                footerAlign = footerAlign,
                isDestructive = isDestructive,
                primaryButtonText = primaryButtonText,
                secondaryButtonText = secondaryButtonText,
                leadingActionIcon = leadingActionIcon,
                onPrimaryClick = onPrimaryClick ?: onDismiss,
                onSecondaryClick = onSecondaryClick ?: onDismiss,
                onLeadingIconClick = onLeadingIconClick ?: {},
                alignment = alignment,
                showCloseButton = showCloseButton,
                backgroundColor = backgroundColor ?: Ux4gColors.white,
                cornerRadius = cornerRadius
            )
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Card Content (exposed for previews / embedding directly without Dialog window)
// ─────────────────────────────────────────────────────────────────────────────

@Composable
fun Ux4gModalContent(
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    headerImageUrl: String? = null,
    headerImageContent: (@Composable () -> Unit)? = null,
    headerImageHeight: Dp = 160.dp,
    headerImageStyle: Ux4gModalHeaderImage = Ux4gModalHeaderImage.FullBleed,
    leadingItem: Ux4gModalLeadingItem = Ux4gModalLeadingItem.None,
    leadingIcon: Ux4gIcon = Ux4gIcons.info_circle,
    leadingIconTint: Color? = null,
    avatarInitials: String? = null,
    avatarImageUrl: Any? = null,
    avatarIcon: Ux4gIcon? = null,
    avatarSize: Ux4gAvatarSize = Ux4gAvatarSize.L,
    avatarInlineSize: Ux4gAvatarSize = Ux4gAvatarSize.S,
    avatarContainerColor: Color? = null,
    avatarContentColor: Color? = null,
    leadingImageUrl: String? = null,
    leadingImageContent: (@Composable () -> Unit)? = null,
    showHeader: Boolean = true,
    headerTitle: String = "Header",
    showDescription: Boolean = false,
    descriptionText: String = "Write description here",
    showDividers: Boolean = true,
    showSubtitle: Boolean = true,
    subtitleText: String = "Subtitle",
    showBody: Boolean = true,
    bodyText: String =
        "A modal is a design element that pops up over the main content of a webpage. " +
        "It demands the user's attention by temporarily disabling interaction with the " +
        "rest of the page until the user addresses the content within the modal.",
    bodyContent: (@Composable () -> Unit)? = null,
    showFooter: Boolean = true,
    footerButtons: Ux4gModalFooterButtons = Ux4gModalFooterButtons.TwoButtons,
    footerAlign: Ux4gModalFooterAlign = Ux4gModalFooterAlign.Right,
    isDestructive: Boolean = false,
    primaryButtonText: String = "Button",
    secondaryButtonText: String = "Button",
    leadingActionIcon: Ux4gIcon = Ux4gIcons.info_circle,
    onPrimaryClick: () -> Unit = onDismiss,
    onSecondaryClick: () -> Unit = onDismiss,
    onLeadingIconClick: () -> Unit = {},
    alignment: Ux4gModalAlignment = Ux4gModalAlignment.LeftAligned,
    showCloseButton: Boolean = true,
    backgroundColor: Color = Ux4gColors.white,
    cornerRadius: Dp = Ux4gRadius.Radius16
) {
    val onSurface = Ux4gColors.neutral900
    val primary = Ux4gColors.primary
    val error = Ux4gColors.red600
    val primaryColor = if (isDestructive) error else primary

    val resolvedAvatarContainerColor =
        avatarContainerColor ?: primary.copy(alpha = 0.1f)
    val resolvedAvatarContentColor =
        avatarContentColor ?: primary

    val isCentered = alignment == Ux4gModalAlignment.Centered
    val textAlign = if (isCentered) TextAlign.Center else TextAlign.Start
    val horizAlign = if (isCentered) Alignment.CenterHorizontally else Alignment.Start

    val hasImage =
        headerImageStyle != Ux4gModalHeaderImage.None &&
        (headerImageContent != null || headerImageUrl != null)

    val showAvatarAbove = leadingItem == Ux4gModalLeadingItem.Avatar && isCentered
    val showAvatarInline = leadingItem == Ux4gModalLeadingItem.Avatar && !isCentered

    val shape = RoundedCornerShape(cornerRadius)

    Surface(
        modifier = modifier
            .widthIn(max = 560.dp)
            .fillMaxWidth()
            .wrapContentHeight(),
        shape = shape,
        color = backgroundColor,
        shadowElevation = 8.dp
    ) {
        Box {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                // ── Header Image ─────────────────────────────────────────────
                when {
                    hasImage && headerImageStyle == Ux4gModalHeaderImage.FullBleed -> {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(headerImageHeight)
                                .clip(
                                    RoundedCornerShape(
                                        topStart = cornerRadius,
                                        topEnd = cornerRadius
                                    )
                                )
                        ) {
                            HeaderImageSlot(headerImageContent, headerImageUrl)
                        }
                    }
                    hasImage && headerImageStyle == Ux4gModalHeaderImage.Padded -> {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(Ux4gSpace.Space12)
                                .height(headerImageHeight)
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .clip(RoundedCornerShape(Ux4gRadius.Radius12))
                            ) {
                                HeaderImageSlot(headerImageContent, headerImageUrl)
                            }

                            if (showCloseButton) {
                                Box(
                                    modifier = Modifier
                                        .align(Alignment.TopEnd)
                                        .padding(Ux4gSpace.Space16)
                                        .size(32.dp)
                                        .clip(CircleShape)
                                        .background(onSurface.copy(alpha = 0.65f)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    IconButton(
                                        onClick = onDismiss,
                                        modifier = Modifier.size(32.dp)
                                    ) {
                                        Ux4gIconComponent(
                                            icon = Ux4gIcons.Close,
                                            contentDescription = "Close modal",
                                            modifier = Modifier.size(16.dp),
                                            tint = Ux4gColors.white
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                // ── Avatar centred BELOW image / at top ──────────────────────
                if (showAvatarAbove && showHeader) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = Ux4gSpace.Space16),
                        contentAlignment = Alignment.Center
                    ) {
                        Ux4gAvatar(
                            initials = avatarInitials,
                            imageUrl = avatarImageUrl,
                            icon = avatarIcon,
                            size = avatarSize,
                            containerColor = resolvedAvatarContainerColor,
                            contentColor = resolvedAvatarContentColor,
                            iconColor = resolvedAvatarContentColor
                        )
                    }
                }

                // ── Header Section ───────────────────────────────────────────
                if (showHeader) {
                    val headerTopPadding = if (showAvatarAbove) Ux4gSpace.Space8 else Ux4gSpace.Space16
                    val headerEndPadding = if (showCloseButton && headerImageStyle != Ux4gModalHeaderImage.Padded && !hasImage) {
                        Ux4gSpace.Space16 + 36.dp
                    } else {
                        Ux4gSpace.Space16
                    }

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                start = Ux4gSpace.Space16,
                                end = headerEndPadding,
                                top = headerTopPadding,
                                bottom = Ux4gSpace.Space16
                            ),
                        horizontalAlignment = horizAlign
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = if (isCentered) Arrangement.Center else Arrangement.Start,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            if (leadingItem == Ux4gModalLeadingItem.Icon) {
                                Ux4gIconComponent(
                                    icon = leadingIcon,
                                    contentDescription = "Leading icon",
                                    modifier = Modifier
                                        .padding(end = Ux4gSpace.Space8)
                                        .size(24.dp),
                                    tint = leadingIconTint ?: onSurface
                                )
                            }

                            if (showAvatarInline) {
                                Ux4gAvatar(
                                    initials = avatarInitials,
                                    imageUrl = avatarImageUrl,
                                    icon = avatarIcon,
                                    size = avatarInlineSize,
                                    containerColor = resolvedAvatarContainerColor,
                                    contentColor = resolvedAvatarContentColor,
                                    iconColor = resolvedAvatarContentColor,
                                    modifier = Modifier.padding(end = Ux4gSpace.Space8)
                                )
                            }

                            if (leadingItem == Ux4gModalLeadingItem.Image &&
                                (leadingImageContent != null || leadingImageUrl != null)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .padding(end = Ux4gSpace.Space8)
                                        .size(40.dp)
                                        .clip(RoundedCornerShape(Ux4gRadius.Radius8))
                                ) {
                                    when {
                                        leadingImageContent != null -> leadingImageContent()
                                        leadingImageUrl != null -> AsyncImage(
                                            model = leadingImageUrl,
                                            contentDescription = "Leading image",
                                            contentScale = ContentScale.Crop,
                                            modifier = Modifier.fillMaxSize()
                                        )
                                        else -> Box(
                                            modifier = Modifier
                                                .fillMaxSize()
                                                .background(Ux4gColors.neutral200)
                                        )
                                    }
                                }
                            }

                            Text(
                                text = headerTitle,
                                style = defaultTypography.tS_strong,
                                color = onSurface,
                                textAlign = textAlign
                            )
                        }

                        if (showDescription) {
                            Spacer(Modifier.height(Ux4gSpace.Space4))
                            Text(
                                text = descriptionText,
                                style = defaultTypography.bS_default,
                                color = onSurface.copy(alpha = 0.5f),
                                textAlign = textAlign
                            )
                        }
                    }

                    if (showDividers) {
                        Ux4gDivider(color = onSurface.copy(alpha = 0.2f))
                    }
                }

                // ── Body Section ─────────────────────────────────────────────
                if (showSubtitle || showBody || bodyContent != null) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(Ux4gSpace.Space16),
                        horizontalAlignment = horizAlign,
                        verticalArrangement = Arrangement.spacedBy(Ux4gSpace.Space8)
                    ) {
                        if (showSubtitle) {
                            Text(
                                text = subtitleText,
                                style = defaultTypography.tS_strong,
                                color = onSurface,
                                textAlign = textAlign
                            )
                        }
                        if (showBody) {
                            if (bodyContent != null) {
                                bodyContent()
                            } else {
                                Text(
                                    text = bodyText,
                                    style = defaultTypography.bM_default,
                                    color = onSurface.copy(alpha = 0.6f),
                                    textAlign = textAlign
                                )
                            }
                        }
                    }
                }

                // ── Footer Section ───────────────────────────────────────────
                if (showFooter) {
                    if (showDividers) {
                        Ux4gDivider(color = onSurface.copy(alpha = 0.2f))
                    }
                    Ux4gModalFooter(
                        footerButtons = footerButtons,
                        footerAlign = footerAlign,
                        isDestructive = isDestructive,
                        primaryColor = primaryColor,
                        primaryButtonText = primaryButtonText,
                        secondaryButtonText = secondaryButtonText,
                        leadingActionIcon = leadingActionIcon,
                        onPrimaryClick = onPrimaryClick,
                        onSecondaryClick = onSecondaryClick,
                        onLeadingIconClick = onLeadingIconClick
                    )
                }
            }

            // Top-right close button (for full-bleed or no-image cards)
            if (showCloseButton && headerImageStyle != Ux4gModalHeaderImage.Padded) {
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(Ux4gSpace.Space16)
                ) {
                    if (hasImage) {
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .background(onSurface.copy(alpha = 0.65f)),
                            contentAlignment = Alignment.Center
                        ) {
                            IconButton(
                                onClick = onDismiss,
                                modifier = Modifier.size(32.dp)
                            ) {
                                Ux4gIconComponent(
                                    icon = Ux4gIcons.Close,
                                    contentDescription = "Close modal",
                                    modifier = Modifier.size(16.dp),
                                    tint = Ux4gColors.white
                                )
                            }
                        }
                    } else {
                        IconButton(
                            onClick = onDismiss,
                            modifier = Modifier.size(32.dp)
                        ) {
                            Ux4gIconComponent(
                                icon = Ux4gIcons.Close,
                                contentDescription = "Close modal",
                                modifier = Modifier.size(20.dp),
                                tint = onSurface
                            )
                        }
                    }
                }
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Private helpers
// ─────────────────────────────────────────────────────────────────────────────

@Composable
private fun HeaderImageSlot(
    headerImageContent: (@Composable () -> Unit)?,
    headerImageUrl: String?
) {
    when {
        headerImageContent != null -> headerImageContent()
        headerImageUrl != null -> AsyncImage(
            model = headerImageUrl,
            contentDescription = "Modal header image",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
    }
}

@Composable
private fun Ux4gModalFooter(
    footerButtons: Ux4gModalFooterButtons,
    footerAlign: Ux4gModalFooterAlign,
    isDestructive: Boolean,
    primaryColor: Color,
    primaryButtonText: String,
    secondaryButtonText: String,
    leadingActionIcon: Ux4gIcon,
    onPrimaryClick: () -> Unit,
    onSecondaryClick: () -> Unit,
    onLeadingIconClick: () -> Unit
) {
    val primary = Ux4gColors.primary
    val onPrimary = Ux4gColors.white
    val onError = Ux4gColors.white

    val buttonPadding = PaddingValues(horizontal = Ux4gSpace.Space16, vertical = Ux4gSpace.Space12)

    val hasLeadingIcon =
        footerButtons == Ux4gModalFooterButtons.OneButtonWithIcon ||
        footerButtons == Ux4gModalFooterButtons.TwoButtonsWithIcon
    val hasSecondary =
        footerButtons == Ux4gModalFooterButtons.TwoButtons ||
        footerButtons == Ux4gModalFooterButtons.TwoButtonsWithIcon

    val rowArrangement: Arrangement.Horizontal = when (footerAlign) {
        Ux4gModalFooterAlign.Left -> Arrangement.Start
        Ux4gModalFooterAlign.Right -> Arrangement.End
        Ux4gModalFooterAlign.Center -> Arrangement.Center
        Ux4gModalFooterAlign.Split -> Arrangement.SpaceBetween
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = Ux4gSpace.Space16, vertical = Ux4gSpace.Space12),
        horizontalArrangement = rowArrangement,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Leading icon button
        if (hasLeadingIcon) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(primary.copy(alpha = 0.08f)),
                contentAlignment = Alignment.Center
            ) {
                IconButton(
                    onClick = onLeadingIconClick,
                    modifier = Modifier.size(40.dp)
                ) {
                    Ux4gIconComponent(
                        icon = leadingActionIcon,
                        contentDescription = "Leading footer action",
                        modifier = Modifier.size(20.dp),
                        tint = primary
                    )
                }
            }
            if (footerAlign != Ux4gModalFooterAlign.Split) {
                Spacer(Modifier.width(Ux4gSpace.Space8))
            }
        }

        // Secondary (outline) button
        if (hasSecondary) {
            if (footerAlign == Ux4gModalFooterAlign.Split) {
                Box(modifier = Modifier.weight(1f)) {
                    Ux4gOutlineButton(
                        onClick = onSecondaryClick,
                        modifier = Modifier.fillMaxWidth(),
                        text = secondaryButtonText,
                        color = primary,
                        borderRadius = Ux4gRadius.Radius8,
                        contentPadding = buttonPadding
                    )
                }
                Spacer(Modifier.width(Ux4gSpace.Space8))
            } else {
                Ux4gOutlineButton(
                    onClick = onSecondaryClick,
                    text = secondaryButtonText,
                    color = primary,
                    borderRadius = Ux4gRadius.Radius8,
                    contentPadding = buttonPadding
                )
                Spacer(Modifier.width(Ux4gSpace.Space8))
            }
        }

        // Primary (filled) button
        if (footerAlign == Ux4gModalFooterAlign.Split && (hasSecondary || hasLeadingIcon)) {
            Box(modifier = Modifier.weight(1f)) {
                Ux4gButton(
                    onClick = onPrimaryClick,
                    modifier = Modifier.fillMaxWidth(),
                    text = primaryButtonText,
                    backgroundColor = primaryColor,
                    contentColor = if (isDestructive) onError else onPrimary,
                    borderRadius = Ux4gRadius.Radius8,
                    contentPadding = buttonPadding
                )
            }
        } else {
            Ux4gButton(
                onClick = onPrimaryClick,
                text = primaryButtonText,
                backgroundColor = primaryColor,
                contentColor = if (isDestructive) onError else onPrimary,
                borderRadius = Ux4gRadius.Radius8,
                contentPadding = buttonPadding
            )
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Previews
// ─────────────────────────────────────────────────────────────────────────────

private val gradientPurplePink: @Composable () -> Unit = {
    Box(
        Modifier
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

@Preview(name = "Left + Avatar inline + FullBleed image", showBackground = true)
@Composable
private fun PreviewLeftAvatarInline() {
    Ux4gTheme {
        Box(
            Modifier
                .fillMaxWidth()
                .background(Ux4gColors.neutral900.copy(alpha = 0.1f))
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Ux4gModalContent(
                onDismiss = {},
                modifier = Modifier.fillMaxWidth(),
                headerImageContent = gradientPurplePink,
                leadingItem = Ux4gModalLeadingItem.Avatar,
                avatarInitials = "JD",
                alignment = Ux4gModalAlignment.LeftAligned,
                showHeader = true,
                headerTitle = "Header",
                showDescription = true,
                descriptionText = "Write description here",
                showSubtitle = true,
                showBody = true,
                showFooter = true,
                footerButtons = Ux4gModalFooterButtons.TwoButtons,
                footerAlign = Ux4gModalFooterAlign.Right,
                isDestructive = true
            )
        }
    }
}

@Preview(name = "Centered + Avatar above + FullBleed image", showBackground = true)
@Composable
private fun PreviewCenteredAvatarAbove() {
    Ux4gTheme {
        Box(
            Modifier
                .fillMaxWidth()
                .background(Ux4gColors.neutral900.copy(alpha = 0.1f))
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Ux4gModalContent(
                onDismiss = {},
                modifier = Modifier.fillMaxWidth(),
                headerImageContent = gradientPurplePink,
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
                isDestructive = true
            )
        }
    }
}

@Preview(name = "Centered + Avatar above + Padded image", showBackground = true)
@Composable
private fun PreviewCenteredAvatarPadded() {
    Ux4gTheme {
        Box(
            Modifier
                .fillMaxWidth()
                .background(Ux4gColors.neutral900.copy(alpha = 0.1f))
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Ux4gModalContent(
                onDismiss = {},
                modifier = Modifier.fillMaxWidth(),
                headerImageContent = gradientPurplePink,
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
                isDestructive = true
            )
        }
    }
}

@Preview(name = "No image + Icon leading + Destructive + Split footer", showBackground = true)
@Composable
private fun PreviewNoImageIconLeading() {
    Ux4gTheme {
        Box(
            Modifier
                .fillMaxWidth()
                .background(Ux4gColors.neutral900.copy(alpha = 0.1f))
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Ux4gModalContent(
                onDismiss = {},
                modifier = Modifier.fillMaxWidth(),
                headerImageStyle = Ux4gModalHeaderImage.None,
                leadingItem = Ux4gModalLeadingItem.Icon,
                leadingIcon = Ux4gIcons.info_circle,
                leadingIconTint = Ux4gColors.red600,
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
                isDestructive = true
            )
        }
    }
}

@Preview(name = "Dark – Default", uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
private fun PreviewDark() {
    Ux4gTheme {
        Box(
            Modifier
                .fillMaxWidth()
                .background(Ux4gColors.neutral900)
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Ux4gModalContent(
                onDismiss = {},
                modifier = Modifier.fillMaxWidth(),
                headerImageContent = gradientPurplePink,
                showHeader = true,
                headerTitle = "Header",
                showSubtitle = true,
                showBody = true,
                showFooter = true,
                footerButtons = Ux4gModalFooterButtons.TwoButtons,
                footerAlign = Ux4gModalFooterAlign.Right
            )
        }
    }
}
