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
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Category
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.ux4g.designsystemv1.components.avatar_component.Ux4gAvatar
import com.ux4g.designsystemv1.components.avatar_component.Ux4gAvatarSize
import com.ux4g.designsystemv1.components.buttons.Ux4gButton
import com.ux4g.designsystemv1.components.buttons.Ux4gOutlineButton
import com.ux4g.designsystemv1.foundation.Ux4gColors
import com.ux4g.designsystemv1.foundation.Ux4gRadius
import com.ux4g.designsystemv1.foundation.Ux4gSpace
import com.ux4g.designsystemv1.foundation.defaultTypography
import com.ux4g.designsystemv1.theme.Ux4gTheme

/**
 * Layout direction of the card.
 */
enum class Ux4gCardDirection {
    Vertical,
    Horizontal;

    companion object {
        val vertical = Vertical
        val horizontal = Horizontal
    }
}

/**
 * Footer button configuration.
 */
enum class Ux4gCardFooterType {
    None,
    PrimaryOnly,
    SecondaryOnly,
    PrimaryAndSecondary;

    companion object {
        val none = None
        val primaryOnly = PrimaryOnly
        val secondaryOnly = SecondaryOnly
        val primaryAndSecondary = PrimaryAndSecondary
    }
}

/**
 * Horizontal alignment of footer buttons.
 */
enum class Ux4gCardFooterAlignment {
    Left,
    Centered,
    Right;

    companion object {
        val left = Left
        val centered = Centered
        val right = Right
    }
}

/**
 * Rich, configurable Card component following the UX4G Design System specs.
 *
 * Mirrors the Flutter `Ux4gCard` component:
 * - Direct layout orientation: [Ux4gCardDirection.Vertical] or [Ux4gCardDirection.Horizontal]
 * - Full-bleed media banner with optional label badge & top-end action
 * - Header with Avatar slot, Title, Subtitle, and Trailing action slot
 * - Horizontally-scrollable status chips row (separated by dividers or custom icon chips)
 * - Multiline body description
 * - Horizontally-scrollable bottom chips row (pill badges or tonal tags)
 * - Flexible footer buttons (Primary / Secondary / Both / Custom) with configurable alignment
 * - Dynamic content color calculation based on background luminance
 *
 * @param modifier Root modifier.
 * @param cornerRadius Corner radius of the card (default [Ux4gRadius.Radius12] = 12.dp).
 * @param backgroundColor Card background color. Defaults to theme's surface color.
 * @param borderColor Optional border color (default [Color.Transparent]).
 * @param borderWidth Width of the optional border stroke (default 0.dp).
 * @param elevation Drop-shadow elevation in Dp (default 0.dp).
 * @param isClickable When true, clicking anywhere on the card invokes [onPressed] / [onClick].
 * @param onPressed Callback when card is clicked.
 * @param onClick Alias for [onPressed].
 * @param direction [Ux4gCardDirection.Vertical] or [Ux4gCardDirection.Horizontal].
 * @param mediaImageUrl URL of media image. Displayed full-bleed on top (Vertical) or left side (Horizontal).
 * @param mediaHeight Height of media image in Vertical layout (default 180.dp).
 * @param mediaWidth Width of media image in Horizontal layout (default 120.dp).
 * @param mediaLabelText Optional label badge overlaid on the media image (top-left).
 * @param mediaLabelLeading Optional leading icon/widget inside the media label badge.
 * @param mediaTopEndAction Optional action button/badge on top-right of media.
 * @param avatar Custom avatar composable (e.g. `{ Ux4gAvatar(...) }`).
 * @param title Primary title text.
 * @param subtitle Secondary subtitle text.
 * @param headerTrailing Optional trailing composable at the right side of header row.
 * @param statusChips Inline metadata tags rendered with divider separators.
 * @param statusContent Custom composable for the status chips row.
 * @param body Main description text.
 * @param bodyText Alias for [body].
 * @param bottomChips Rounded pill tag list below body text.
 * @param bottomChipsContent Custom composable for the bottom chips row.
 * @param footerType Footer button layout: [Ux4gCardFooterType.None], [Ux4gCardFooterType.PrimaryOnly], [Ux4gCardFooterType.SecondaryOnly], or [Ux4gCardFooterType.PrimaryAndSecondary].
 * @param footerAlignment Footer buttons alignment: [Ux4gCardFooterAlignment.Left], [Ux4gCardFooterAlignment.Centered], or [Ux4gCardFooterAlignment.Right].
 * @param primaryButtonText Text for the filled primary button (default "Confirm").
 * @param secondaryButtonText Text for the outlined secondary button (default "Cancel").
 * @param onPrimaryClick Callback for the primary button.
 * @param onSecondaryClick Callback for the secondary button.
 * @param footerContent Custom footer composable block.
 * @param content Optional custom composable block (trailing lambda) replacing the default rich card layout.
 */
@Composable
fun Ux4gCard(
    modifier: Modifier = Modifier,
    // Appearance
    cornerRadius: Dp = Ux4gRadius.Radius12,
    backgroundColor: Color? = null,
    borderColor: Color = Color.Transparent,
    borderWidth: Dp = 0.dp,
    elevation: Dp = 0.dp,
    isClickable: Boolean = false,
    onPressed: (() -> Unit)? = null,
    onClick: (() -> Unit)? = null,
    // Layout
    direction: Ux4gCardDirection = Ux4gCardDirection.Vertical,
    // Media Header
    mediaImageUrl: String? = null,
    mediaHeight: Dp = 180.dp,
    mediaWidth: Dp = 120.dp,
    mediaLabelText: String? = null,
    mediaLabelLeading: (@Composable () -> Unit)? = null,
    mediaTopEndAction: (@Composable () -> Unit)? = null,
    // Header Content
    avatar: (@Composable () -> Unit)? = null,
    title: String? = null,
    subtitle: String? = null,
    headerTrailing: (@Composable () -> Unit)? = null,
    // Status Chips
    statusChips: List<String> = emptyList(),
    statusContent: (@Composable () -> Unit)? = null,
    // Body Text
    body: String? = null,
    bodyText: String? = null,
    // Bottom Chips
    bottomChips: List<String> = emptyList(),
    bottomChipsContent: (@Composable () -> Unit)? = null,
    // Footer
    footerType: Ux4gCardFooterType = Ux4gCardFooterType.None,
    footerAlignment: Ux4gCardFooterAlignment = Ux4gCardFooterAlignment.Left,
    primaryButtonText: String = "Confirm",
    secondaryButtonText: String = "Cancel",
    onPrimaryClick: (() -> Unit)? = null,
    onSecondaryClick: (() -> Unit)? = null,
    footerContent: (@Composable () -> Unit)? = null,
    content: (@Composable ColumnScope.() -> Unit)? = null
) {
    val shape = RoundedCornerShape(cornerRadius)
    val resolvedBg = backgroundColor ?: Ux4gTheme.colorScheme.surface
    val hasBorder = borderWidth > 0.dp && borderColor != Color.Transparent
    val borderStroke = if (hasBorder) BorderStroke(borderWidth, borderColor) else null

    // Determine content color from background luminance matching Flutter
    val isDark = !isLightColor(resolvedBg)
    val contentColor = if (isDark) Ux4gColors.white else Ux4gTheme.colorScheme.onSurface

    val effectiveOnClick = onClick ?: onPressed
    val clickModifier = if ((isClickable || effectiveOnClick != null) && effectiveOnClick != null) {
        Modifier.clickable(onClick = effectiveOnClick)
    } else Modifier

    val effectiveBody = body ?: bodyText

    Card(
        modifier = modifier.then(clickModifier),
        shape = shape,
        colors = CardDefaults.cardColors(
            containerColor = resolvedBg,
            contentColor = contentColor
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = elevation),
        border = borderStroke
    ) {
        if (content != null) {
            Column(modifier = Modifier.fillMaxWidth()) {
                content()
            }
        } else {
            when (direction) {
                Ux4gCardDirection.Vertical -> CardVerticalLayout(
                    mediaImageUrl = mediaImageUrl,
                    mediaHeight = mediaHeight,
                    mediaLabelText = mediaLabelText,
                    mediaLabelLeading = mediaLabelLeading,
                    mediaTopEndAction = mediaTopEndAction,
                    avatar = avatar,
                    title = title,
                    subtitle = subtitle,
                    headerTrailing = headerTrailing,
                    statusChips = statusChips,
                    statusContent = statusContent,
                    body = effectiveBody,
                    bottomChips = bottomChips,
                    bottomChipsContent = bottomChipsContent,
                    footerType = footerType,
                    footerAlignment = footerAlignment,
                    primaryButtonText = primaryButtonText,
                    secondaryButtonText = secondaryButtonText,
                    onPrimaryClick = onPrimaryClick,
                    onSecondaryClick = onSecondaryClick,
                    footerContent = footerContent,
                    contentColor = contentColor,
                    surfaceColor = resolvedBg
                )

                Ux4gCardDirection.Horizontal -> CardHorizontalLayout(
                    mediaImageUrl = mediaImageUrl,
                    mediaWidth = mediaWidth,
                    mediaLabelText = mediaLabelText,
                    mediaLabelLeading = mediaLabelLeading,
                    mediaTopEndAction = mediaTopEndAction,
                    avatar = avatar,
                    title = title,
                    subtitle = subtitle,
                    headerTrailing = headerTrailing,
                    statusChips = statusChips,
                    statusContent = statusContent,
                    body = effectiveBody,
                    bottomChips = bottomChips,
                    bottomChipsContent = bottomChipsContent,
                    footerType = footerType,
                    footerAlignment = footerAlignment,
                    primaryButtonText = primaryButtonText,
                    secondaryButtonText = secondaryButtonText,
                    onPrimaryClick = onPrimaryClick,
                    onSecondaryClick = onSecondaryClick,
                    footerContent = footerContent,
                    contentColor = contentColor,
                    surfaceColor = resolvedBg
                )
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Vertical Layout (Media on top)
// ─────────────────────────────────────────────────────────────────────────────

@Composable
private fun CardVerticalLayout(
    mediaImageUrl: String?,
    mediaHeight: Dp,
    mediaLabelText: String?,
    mediaLabelLeading: (@Composable () -> Unit)?,
    mediaTopEndAction: (@Composable () -> Unit)?,
    avatar: (@Composable () -> Unit)?,
    title: String?,
    subtitle: String?,
    headerTrailing: (@Composable () -> Unit)?,
    statusChips: List<String>,
    statusContent: (@Composable () -> Unit)?,
    body: String?,
    bottomChips: List<String>,
    bottomChipsContent: (@Composable () -> Unit)?,
    footerType: Ux4gCardFooterType,
    footerAlignment: Ux4gCardFooterAlignment,
    primaryButtonText: String,
    secondaryButtonText: String,
    onPrimaryClick: (() -> Unit)?,
    onSecondaryClick: (() -> Unit)?,
    footerContent: (@Composable () -> Unit)?,
    contentColor: Color,
    surfaceColor: Color
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        if (mediaImageUrl != null) {
            CardMediaImage(
                imageUrl = mediaImageUrl,
                labelText = mediaLabelText,
                labelLeading = mediaLabelLeading,
                topEndAction = mediaTopEndAction,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(mediaHeight),
                badgeContainerColor = contentColor,
                badgeContentColor = surfaceColor
            )
        }
        Column(
            modifier = Modifier.padding(
                start = Ux4gSpace.Space20,
                top = Ux4gSpace.Space20,
                end = Ux4gSpace.Space20,
                bottom = Ux4gSpace.Space24
            )
        ) {
            CardContentBlock(
                avatar = avatar,
                title = title,
                subtitle = subtitle,
                headerTrailing = headerTrailing,
                statusChips = statusChips,
                statusContent = statusContent,
                body = body,
                bottomChips = bottomChips,
                bottomChipsContent = bottomChipsContent,
                footerType = footerType,
                footerAlignment = footerAlignment,
                primaryButtonText = primaryButtonText,
                secondaryButtonText = secondaryButtonText,
                onPrimaryClick = onPrimaryClick,
                onSecondaryClick = onSecondaryClick,
                footerContent = footerContent,
                contentColor = contentColor
            )
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Horizontal Layout (Media on left)
// ─────────────────────────────────────────────────────────────────────────────

@Composable
private fun CardHorizontalLayout(
    mediaImageUrl: String?,
    mediaWidth: Dp,
    mediaLabelText: String?,
    mediaLabelLeading: (@Composable () -> Unit)?,
    mediaTopEndAction: (@Composable () -> Unit)?,
    avatar: (@Composable () -> Unit)?,
    title: String?,
    subtitle: String?,
    headerTrailing: (@Composable () -> Unit)?,
    statusChips: List<String>,
    statusContent: (@Composable () -> Unit)?,
    body: String?,
    bottomChips: List<String>,
    bottomChipsContent: (@Composable () -> Unit)?,
    footerType: Ux4gCardFooterType,
    footerAlignment: Ux4gCardFooterAlignment,
    primaryButtonText: String,
    secondaryButtonText: String,
    onPrimaryClick: (() -> Unit)?,
    onSecondaryClick: (() -> Unit)?,
    footerContent: (@Composable () -> Unit)?,
    contentColor: Color,
    surfaceColor: Color
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
    ) {
        if (mediaImageUrl != null) {
            CardMediaImage(
                imageUrl = mediaImageUrl,
                labelText = mediaLabelText,
                labelLeading = mediaLabelLeading,
                topEndAction = mediaTopEndAction,
                modifier = Modifier
                    .width(mediaWidth)
                    .fillMaxHeight(),
                badgeContainerColor = contentColor,
                badgeContentColor = surfaceColor
            )
        }
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(
                    start = Ux4gSpace.Space20,
                    top = Ux4gSpace.Space20,
                    end = Ux4gSpace.Space20,
                    bottom = Ux4gSpace.Space24
                )
        ) {
            CardContentBlock(
                avatar = avatar,
                title = title,
                subtitle = subtitle,
                headerTrailing = headerTrailing,
                statusChips = statusChips,
                statusContent = statusContent,
                body = body,
                bottomChips = bottomChips,
                bottomChipsContent = bottomChipsContent,
                footerType = footerType,
                footerAlignment = footerAlignment,
                primaryButtonText = primaryButtonText,
                secondaryButtonText = secondaryButtonText,
                onPrimaryClick = onPrimaryClick,
                onSecondaryClick = onSecondaryClick,
                footerContent = footerContent,
                contentColor = contentColor
            )
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Media Banner
// ─────────────────────────────────────────────────────────────────────────────

@Composable
private fun CardMediaImage(
    imageUrl: String,
    labelText: String?,
    labelLeading: (@Composable () -> Unit)? = null,
    topEndAction: (@Composable () -> Unit)? = null,
    modifier: Modifier = Modifier,
    badgeContainerColor: Color = Ux4gColors.neutral900,
    badgeContentColor: Color = Ux4gColors.white
) {
    Box(modifier = modifier) {
        AsyncImage(
            model = imageUrl,
            contentDescription = "Card Media",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
        // Top-Start Label Badge
        if (labelText != null || labelLeading != null) {
            Box(
                modifier = Modifier
                    .padding(Ux4gSpace.Space8)
                    .align(Alignment.TopStart)
            ) {
                if (labelLeading != null && labelText != null) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(badgeContainerColor)
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            labelLeading()
                            Text(
                                text = labelText,
                                style = defaultTypography.lS_strong.copy(
                                    color = badgeContentColor,
                                    fontSize = 10.sp
                                )
                            )
                        }
                    }
                } else if (labelText != null) {
                    Ux4gBadge(
                        type = Ux4gBadgeType.Label(labelText),
                        containerColor = badgeContainerColor,
                        contentColor = badgeContentColor
                    )
                }
            }
        }

        // Top-End Action
        if (topEndAction != null) {
            Box(
                modifier = Modifier
                    .padding(Ux4gSpace.Space8)
                    .align(Alignment.TopEnd)
            ) {
                topEndAction()
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Content Block
// ─────────────────────────────────────────────────────────────────────────────

@Composable
private fun ColumnScope.CardContentBlock(
    avatar: (@Composable () -> Unit)?,
    title: String?,
    subtitle: String?,
    headerTrailing: (@Composable () -> Unit)?,
    statusChips: List<String>,
    statusContent: (@Composable () -> Unit)?,
    body: String?,
    bottomChips: List<String>,
    bottomChipsContent: (@Composable () -> Unit)?,
    footerType: Ux4gCardFooterType,
    footerAlignment: Ux4gCardFooterAlignment,
    primaryButtonText: String,
    secondaryButtonText: String,
    onPrimaryClick: (() -> Unit)?,
    onSecondaryClick: (() -> Unit)?,
    footerContent: (@Composable () -> Unit)?,
    contentColor: Color
) {
    val hasHeader = avatar != null || title != null || subtitle != null || headerTrailing != null
    if (hasHeader) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(Ux4gSpace.Space12),
            modifier = Modifier.fillMaxWidth()
        ) {
            if (avatar != null) {
                avatar()
            }
            Column(modifier = Modifier.weight(1f)) {
                if (title != null) {
                    Text(
                        text = title,
                        style = defaultTypography.tS_strong.copy(color = contentColor),
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                if (subtitle != null) {
                    Text(
                        text = subtitle,
                        style = defaultTypography.bS_default.copy(
                            color = contentColor.copy(alpha = 0.5f)
                        ),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
            if (headerTrailing != null) {
                headerTrailing()
            }
        }
    }

    if (statusContent != null) {
        Spacer(modifier = Modifier.height(Ux4gSpace.Space8))
        statusContent()
    } else if (statusChips.isNotEmpty()) {
        Spacer(modifier = Modifier.height(Ux4gSpace.Space8))
        CardChipRow(chips = statusChips, pillStyle = false, contentColor = contentColor)
    }

    if (body != null) {
        Spacer(modifier = Modifier.height(Ux4gSpace.Space12))
        Text(
            text = body,
            style = defaultTypography.bM_default.copy(
                color = contentColor.copy(alpha = 0.7f)
            ),
            maxLines = 4,
            overflow = TextOverflow.Ellipsis
        )
    }

    if (bottomChipsContent != null) {
        Spacer(modifier = Modifier.height(Ux4gSpace.Space12))
        bottomChipsContent()
    } else if (bottomChips.isNotEmpty()) {
        Spacer(modifier = Modifier.height(Ux4gSpace.Space12))
        CardChipRow(chips = bottomChips, pillStyle = true, contentColor = contentColor)
    }

    if (footerContent != null) {
        Spacer(modifier = Modifier.height(Ux4gSpace.Space20))
        footerContent()
    } else if (footerType != Ux4gCardFooterType.None) {
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
// Chip Row
// ─────────────────────────────────────────────────────────────────────────────

@Composable
private fun CardChipRow(
    chips: List<String>,
    pillStyle: Boolean,
    contentColor: Color
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
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(Ux4gRadius.Radius999))
                        .border(
                            width = 1.dp,
                            color = contentColor.copy(alpha = 0.3f),
                            shape = RoundedCornerShape(Ux4gRadius.Radius999)
                        )
                        .padding(
                            horizontal = Ux4gSpace.Space12,
                            vertical = Ux4gSpace.Space4
                        )
                ) {
                    Text(
                        text = chip,
                        style = defaultTypography.lS_default.copy(
                            color = contentColor.copy(alpha = 0.7f)
                        )
                    )
                }
            } else {
                if (index > 0) {
                    Text(
                        text = "│",
                        style = defaultTypography.bS_default.copy(
                            color = contentColor.copy(alpha = 0.3f)
                        )
                    )
                }
                Text(
                    text = chip,
                    style = defaultTypography.lS_default.copy(
                        color = contentColor.copy(alpha = 0.6f)
                    )
                )
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Footer Row
// ─────────────────────────────────────────────────────────────────────────────

@Composable
private fun CardFooterRow(
    footerType: Ux4gCardFooterType,
    footerAlignment: Ux4gCardFooterAlignment,
    primaryButtonText: String,
    secondaryButtonText: String,
    onPrimaryClick: (() -> Unit)?,
    onSecondaryClick: (() -> Unit)?
) {
    val arrangement = when (footerAlignment) {
        Ux4gCardFooterAlignment.Left -> Arrangement.Start
        Ux4gCardFooterAlignment.Centered -> Arrangement.Center
        Ux4gCardFooterAlignment.Right -> Arrangement.End
    }

    val fillWidth = footerAlignment == Ux4gCardFooterAlignment.Centered &&
            footerType != Ux4gCardFooterType.PrimaryAndSecondary

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = arrangement,
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (footerType == Ux4gCardFooterType.PrimaryAndSecondary ||
            footerType == Ux4gCardFooterType.PrimaryOnly
        ) {
            Ux4gButton(
                onClick = onPrimaryClick ?: {},
                modifier = if (fillWidth) Modifier.fillMaxWidth() else Modifier,
                contentPadding = PaddingValues(
                    horizontal = Ux4gSpace.Space20,
                    vertical = Ux4gSpace.Space12
                )
            ) {
                Text(primaryButtonText)
            }
        }

        if (footerType == Ux4gCardFooterType.PrimaryAndSecondary) {
            Spacer(modifier = Modifier.width(Ux4gSpace.Space12))
        }

        if (footerType == Ux4gCardFooterType.PrimaryAndSecondary ||
            footerType == Ux4gCardFooterType.SecondaryOnly
        ) {
            Ux4gOutlineButton(
                onClick = onSecondaryClick ?: {},
                modifier = if (fillWidth) Modifier.fillMaxWidth() else Modifier,
                contentPadding = PaddingValues(
                    horizontal = Ux4gSpace.Space20,
                    vertical = Ux4gSpace.Space12
                )
            ) {
                Text(secondaryButtonText)
            }
        }
    }
}

private fun isLightColor(color: Color): Boolean =
    (0.299 * color.red + 0.587 * color.green + 0.114 * color.blue) >= 0.5

// ─────────────────────────────────────────────────────────────────────────────
// Previews
// ─────────────────────────────────────────────────────────────────────────────

@Preview(name = "Card Showcase", showBackground = true)
@Preview(
    name = "Card Showcase Dark",
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true
)
@Composable
private fun Ux4gCardPreview() {
    Ux4gTheme {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Ux4gTheme.colorScheme.onSurface.copy(alpha = 0.05f))
                .padding(Ux4gSpace.Space24),
            verticalArrangement = Arrangement.spacedBy(Ux4gSpace.Space24)
        ) {
            // Exact UI as Requested in Showcase
            Ux4gCard(
                mediaImageUrl = "https://picsum.photos/seed/canyon/600/300",
                mediaHeight = 180.dp,
                mediaLabelText = "Label",
                mediaLabelLeading = {
                    Icon(
                        imageVector = Icons.Filled.Category,
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
                        Icon(
                            imageVector = Icons.Filled.Category,
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
                    Icon(
                        imageVector = Icons.Filled.Category,
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
                                Icon(
                                    imageVector = Icons.Filled.Category,
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
                                    Icon(
                                        imageVector = Icons.Filled.Category,
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
                            Icon(
                                imageVector = Icons.Filled.Add,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Button", style = defaultTypography.bM_strong)
                            Spacer(modifier = Modifier.width(4.dp))
                            Icon(
                                imageVector = Icons.Filled.ArrowDropDown,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                        }

                        Ux4gOutlineButton(
                            onClick = {},
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Add,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Button", style = defaultTypography.bM_strong)
                            Spacer(modifier = Modifier.width(4.dp))
                            Icon(
                                imageVector = Icons.Filled.ArrowDropDown,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }
            )

            // Horizontal Card Example
            Ux4gCard(
                direction = Ux4gCardDirection.Horizontal,
                borderColor = Ux4gColors.neutral300,
                borderWidth = 1.dp,
                mediaImageUrl = "https://picsum.photos/seed/card2/300/300",
                title = "Horizontal Card",
                subtitle = "Image on left side",
                body = "This card uses horizontal layout orientation with media on the left.",
                footerType = Ux4gCardFooterType.SecondaryOnly,
                secondaryButtonText = "Learn More"
            )
        }
    }
}
