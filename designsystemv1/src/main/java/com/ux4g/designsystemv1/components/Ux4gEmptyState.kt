package com.ux4g.designsystemv1.components

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.Inbox
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.RocketLaunch
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.SearchOff
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.ux4g.designsystemv1.components.buttons.Ux4gButton
import com.ux4g.designsystemv1.components.buttons.Ux4gButtonSize
import com.ux4g.designsystemv1.components.buttons.Ux4gButtonVariant
import com.ux4g.designsystemv1.foundation.Ux4gColors
import com.ux4g.designsystemv1.foundation.Ux4gIcon
import com.ux4g.designsystemv1.foundation.Ux4gIconComponent
import com.ux4g.designsystemv1.foundation.defaultTypography

/**
 * Semantic presets for [Ux4gEmptyState].
 *
 * Each preset automatically selects an appropriate default icon matching its meaning,
 * while allowing complete customization of all properties.
 */
enum class Ux4gEmptyStateVariant {
    /** Search returned no results. */
    NoResults,

    /** The list / inbox is empty — no items to show. */
    NoData,

    /** Feature is not yet available. */
    ComingSoon,

    /** Data could not be loaded (network / server error). */
    Error,

    /** Fully custom — no pre-configured values are applied. */
    Custom;

    companion object {
        val noResults = NoResults
        val noData = NoData
        val comingSoon = ComingSoon
        val error = Error
        val custom = Custom
    }
}

/**
 * A flexible empty-state composable following the UX4G design system.
 *
 * Displays an icon, a bold header, secondary text lines (subtitle/description), and an
 * optional action button with a filled tonal style.
 *
 * Matches Flutter's `empty_state.dart` specifications 1:1.
 *
 * @param title Bold header line shown directly below the icon.
 * @param variant Semantic preset controlling default icon selection.
 * @param modifier Optional [Modifier] for this composable.
 * @param icon Override the icon. Falls back to the variant default when null.
 * @param iconContent Optional composable slot for fully custom icon/illustration rendering.
 * @param iconSize Size of the icon. Defaults to 48.dp.
 * @param iconColor Color of the icon. Defaults to [Ux4gColors.primary].
 * @param subtitle Second text line. Optional.
 * @param description Third text line. Optional.
 * @param titleStyle Optional [TextStyle] override for the title.
 * @param bodyStyle Optional [TextStyle] override for the subtitle/description.
 * @param buttonText Label for the action button. If null, button is omitted.
 * @param onButtonPressed Callback invoked when the button is clicked.
 * @param buttonSize Size of the action button. Defaults to [Ux4gButtonSize.Small].
 * @param buttonLeadingIcon Optional leading icon for the action button.
 * @param padding Padding around the whole empty state. Defaults to 24.dp on all sides.
 * @param bodyHorizontalPadding Horizontal padding for subtitle/description for narrower reading width.
 * @param customContent Optional custom content slot below the body text.
 */
@Composable
fun Ux4gEmptyState(
    title: String,
    modifier: Modifier = Modifier,
    variant: Ux4gEmptyStateVariant = Ux4gEmptyStateVariant.Custom,
    icon: Any? = null,
    iconContent: (@Composable () -> Unit)? = null,
    iconSize: Dp = 48.dp,
    iconColor: Color? = null,
    subtitle: String? = null,
    description: String? = null,
    titleStyle: TextStyle? = null,
    bodyStyle: TextStyle? = null,
    buttonText: String? = null,
    onButtonPressed: (() -> Unit)? = null,
    buttonSize: Ux4gButtonSize = Ux4gButtonSize.Small,
    buttonLeadingIcon: Any? = null,
    padding: PaddingValues = PaddingValues(24.dp),
    bodyHorizontalPadding: Dp = 24.dp,
    customContent: (@Composable () -> Unit)? = null
) {
    val defaultIconVector = when (variant) {
        Ux4gEmptyStateVariant.NoResults -> Icons.Default.SearchOff
        Ux4gEmptyStateVariant.NoData -> Icons.Default.Inbox
        Ux4gEmptyStateVariant.ComingSoon -> Icons.Default.RocketLaunch
        Ux4gEmptyStateVariant.Error -> Icons.Default.ErrorOutline
        Ux4gEmptyStateVariant.Custom -> Icons.Default.Info
    }

    val resolvedIconColor = iconColor ?: Ux4gColors.primary
    val resolvedTitleStyle = (titleStyle ?: defaultTypography.hXXS_strong).copy(
        color = titleStyle?.color ?: Ux4gColors.neutral900
    )
    val resolvedBodyStyle = (bodyStyle ?: defaultTypography.bS_default).copy(
        color = bodyStyle?.color ?: Ux4gColors.neutral900.copy(alpha = 0.6f)
    )

    // Tonal button: filled light-primary background (12% alpha), primary foreground
    val buttonBg = Ux4gColors.primary.copy(alpha = 0.12f)
    val buttonFg = Ux4gColors.primary

    val bodyText = listOfNotNull(subtitle, description).joinToString(" ").takeIf { it.isNotEmpty() }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(padding),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // ── Icon / Illustration Slot ──────────────────────────────────────────
        if (iconContent != null) {
            iconContent()
        } else {
            val targetIcon = icon ?: defaultIconVector
            when (targetIcon) {
                is ImageVector -> {
                    Icon(
                        imageVector = targetIcon,
                        contentDescription = null,
                        tint = resolvedIconColor,
                        modifier = Modifier.size(iconSize)
                    )
                }
                is Ux4gIcon -> {
                    Ux4gIconComponent(
                        icon = targetIcon,
                        contentDescription = null,
                        tint = resolvedIconColor,
                        modifier = Modifier.size(iconSize)
                    )
                }
                is Int -> {
                    Icon(
                        painter = painterResource(id = targetIcon),
                        contentDescription = null,
                        tint = resolvedIconColor,
                        modifier = Modifier.size(iconSize)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // ── Bold Title Header ────────────────────────────────────────────────
        Text(
            text = title,
            style = resolvedTitleStyle,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = bodyHorizontalPadding)
        )

        // ── Subtitle / Description Body ──────────────────────────────────────
        if (bodyText != null) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = bodyText,
                style = resolvedBodyStyle,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = bodyHorizontalPadding)
            )
        }

        // ── Custom Content Slot ──────────────────────────────────────────────
        if (customContent != null) {
            Spacer(modifier = Modifier.height(16.dp))
            customContent()
        }

        // ── Action Button (Filled Tonal) ─────────────────────────────────────
        if (buttonText != null) {
            Spacer(modifier = Modifier.height(16.dp))
            Ux4gButton(
                text = buttonText,
                onClick = onButtonPressed ?: {},
                variant = Ux4gButtonVariant.Primary,
                backgroundColor = buttonBg,
                contentColor = buttonFg,
                size = buttonSize
            )
        }
    }
}

// ═══════════════════════════════════════════════════════
// Previews
// ═══════════════════════════════════════════════════════

@Preview(name = "Empty State - Light", showBackground = true, backgroundColor = 0xFFFFFFFF)
@Preview(name = "Empty State - Dark", uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true, backgroundColor = 0xFF121212)
@Composable
private fun Ux4gEmptyStatePreview() {
    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        // No Results
        Ux4gEmptyState(
            variant = Ux4gEmptyStateVariant.NoResults,
            title = "No results found",
            subtitle = "Did you mean: Driving License, Ration Card",
            buttonText = "Clear all filters",
            onButtonPressed = {}
        )

        // No Data
        Ux4gEmptyState(
            variant = Ux4gEmptyStateVariant.NoData,
            title = "No items in your inbox",
            subtitle = "When you receive messages or notifications, they will appear here.",
            buttonText = "Refresh Inbox",
            onButtonPressed = {}
        )

        // Coming Soon
        Ux4gEmptyState(
            variant = Ux4gEmptyStateVariant.ComingSoon,
            title = "Feature Coming Soon",
            subtitle = "We are working hard to bring this feature to you. Stay tuned!",
            buttonText = "Notify Me",
            onButtonPressed = {}
        )

        // Error State
        Ux4gEmptyState(
            variant = Ux4gEmptyStateVariant.Error,
            title = "Unable to load data",
            subtitle = "Please check your network connection and try again.",
            buttonText = "Try Again",
            onButtonPressed = {}
        )
    }
}
