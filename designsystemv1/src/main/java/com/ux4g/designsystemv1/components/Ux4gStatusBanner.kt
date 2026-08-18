package com.ux4g.designsystemv1.components

import android.content.res.Configuration
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.ux4g.designsystemv1.components.buttons.Ux4gButton
import com.ux4g.designsystemv1.components.buttons.Ux4gButtonSize
import com.ux4g.designsystemv1.components.buttons.Ux4gButtonVariant
import com.ux4g.designsystemv1.foundation.Ux4gColors
import com.ux4g.designsystemv1.foundation.defaultTypography
import kotlinx.coroutines.delay

/**
 * Visual variants for the status banner component.
 */
enum class Ux4gBannerVariant {
    WarningLight,
    WarningSolid,
    ErrorLight,
    SuccessLight,
    SavingLight,
    InfoLight,
    NeutralLight,
    PrimaryLight
}

/**
 * A highly customizable Status Banner component supporting multiple severity variants,
 * leading and trailing icons, badges, titles, subtitles, actions, and dismiss behavior.
 *
 * Matches UX4G Flutter design specifications 1:1.
 *
 * @param variant Visual variant of the banner (e.g. WarningLight, ErrorLight, SuccessLight).
 * @param title Header text of the banner.
 * @param modifier Standard Compose modifier.
 * @param subtitle Optional body subtitle text.
 * @param subtitleWidget Optional custom subtitle composable.
 * @param titleStyle Optional text style override for the title.
 * @param subtitleStyle Optional text style override for the subtitle.
 * @param badge Optional badge composable placed next to the title.
 * @param leadingIcon Optional leading icon/graphic composable.
 * @param trailingIcon Optional trailing icon/graphic composable.
 * @param actions Optional list of action buttons or widgets rendered below.
 * @param onDismiss Optional callback invoked when dismiss icon is clicked.
 * @param backgroundColor Optional background color override.
 * @param borderColor Optional border color override.
 * @param actionsAlignment Alignment of action widgets. Defaults to [Arrangement.Start].
 * @param width Optional custom width.
 * @param height Optional custom height (default minHeight = 48.dp).
 * @param margin Outer padding/margin around the banner. Defaults to 16.dp horizontal, 8.dp vertical.
 * @param padding Inner padding inside the banner card. Defaults to 16.dp horizontal, 12.dp vertical.
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun Ux4gStatusBanner(
    variant: Ux4gBannerVariant,
    title: String,
    modifier: Modifier = Modifier,
    subtitle: String? = null,
    subtitleWidget: (@Composable () -> Unit)? = null,
    titleStyle: TextStyle? = null,
    subtitleStyle: TextStyle? = null,
    badge: (@Composable () -> Unit)? = null,
    leadingIcon: (@Composable () -> Unit)? = null,
    trailingIcon: (@Composable () -> Unit)? = null,
    actions: (@Composable () -> Unit)? = null,
    onDismiss: (() -> Unit)? = null,
    backgroundColor: Color? = null,
    borderColor: Color? = null,
    actionsAlignment: Arrangement.Horizontal = Arrangement.Start,
    width: Dp? = null,
    height: Dp? = null,
    margin: PaddingValues = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
    padding: PaddingValues = PaddingValues(horizontal = 16.dp, vertical = 12.dp)
) {
    val surface = Ux4gColors.neutral0
    val onSurface = Ux4gColors.neutral900
    val warning = Ux4gColors.orange600
    val onWarning = Ux4gColors.neutral0
    val error = Ux4gColors.red600
    val success = Ux4gColors.green600
    val primary = Ux4gColors.primary
    val info = Ux4gColors.blue600

    val bMStrong = defaultTypography.bM_strong
    val bMDefault = defaultTypography.bM_default

    // Determine colors based on variant
    val defaultBgColor: Color
    val defaultBorderColor: Color
    val textColor: Color
    val subtitleColor: Color

    when (variant) {
        Ux4gBannerVariant.WarningLight -> {
            defaultBgColor = lerp(surface, warning, 0.12f)
            defaultBorderColor = warning.copy(alpha = 0.3f)
            textColor = onSurface
            subtitleColor = onSurface.copy(alpha = 0.6f)
        }
        Ux4gBannerVariant.WarningSolid -> {
            defaultBgColor = warning
            defaultBorderColor = warning
            textColor = onWarning
            subtitleColor = onWarning.copy(alpha = 0.7f)
        }
        Ux4gBannerVariant.ErrorLight -> {
            defaultBgColor = lerp(surface, error, 0.12f)
            defaultBorderColor = error.copy(alpha = 0.3f)
            textColor = onSurface
            subtitleColor = onSurface.copy(alpha = 0.6f)
        }
        Ux4gBannerVariant.SuccessLight -> {
            defaultBgColor = lerp(surface, success, 0.12f)
            defaultBorderColor = success.copy(alpha = 0.3f)
            textColor = onSurface
            subtitleColor = onSurface.copy(alpha = 0.6f)
        }
        Ux4gBannerVariant.SavingLight,
        Ux4gBannerVariant.PrimaryLight -> {
            defaultBgColor = lerp(surface, primary, 0.12f)
            defaultBorderColor = primary.copy(alpha = 0.3f)
            textColor = onSurface
            subtitleColor = onSurface.copy(alpha = 0.6f)
        }
        Ux4gBannerVariant.InfoLight -> {
            defaultBgColor = lerp(surface, info, 0.12f)
            defaultBorderColor = info.copy(alpha = 0.3f)
            textColor = onSurface
            subtitleColor = onSurface.copy(alpha = 0.6f)
        }
        Ux4gBannerVariant.NeutralLight -> {
            defaultBgColor = onSurface.copy(alpha = 0.05f)
            defaultBorderColor = onSurface.copy(alpha = 0.15f)
            textColor = onSurface
            subtitleColor = onSurface.copy(alpha = 0.6f)
        }
    }

    val resolvedBgColor = backgroundColor ?: defaultBgColor
    val resolvedBorderColor = borderColor ?: defaultBorderColor

    val sizeModifier = Modifier
        .then(if (width != null) Modifier.width(width) else Modifier.fillMaxWidth())
        .then(if (height != null) Modifier.height(height) else Modifier.defaultMinSize(minHeight = 48.dp))

    val hasSubtitle = subtitle != null || subtitleWidget != null
    val topRowCrossAlignment = if (hasSubtitle) Alignment.Top else Alignment.CenterVertically

    val resolvedTrailingIcon: (@Composable () -> Unit)? = when {
        trailingIcon != null -> trailingIcon
        onDismiss != null -> {
            {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Dismiss Banner",
                    tint = textColor.copy(alpha = 0.7f),
                    modifier = Modifier
                        .size(20.dp)
                        .clickable(onClick = onDismiss)
                )
            }
        }
        else -> null
    }

    Box(
        modifier = modifier
            .padding(margin)
            .then(sizeModifier)
            .shadow(
                elevation = 2.dp,
                shape = RoundedCornerShape(8.dp),
                spotColor = onSurface.copy(alpha = 0.05f)
            )
            .clip(RoundedCornerShape(8.dp))
            .background(resolvedBgColor)
            .border(1.dp, resolvedBorderColor, RoundedCornerShape(8.dp))
            .padding(padding)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.Center
        ) {
            // Header Content
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = topRowCrossAlignment,
                horizontalArrangement = Arrangement.Start
            ) {
                if (leadingIcon != null) {
                    leadingIcon()
                    Spacer(modifier = Modifier.width(16.dp))
                }

                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.Center
                ) {
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(
                            text = title,
                            style = (titleStyle ?: bMStrong).copy(
                                color = titleStyle?.color ?: textColor
                            )
                        )
                        if (badge != null) {
                            badge()
                        }
                    }

                    if (subtitleWidget != null) {
                        Spacer(modifier = Modifier.height(4.dp))
                        subtitleWidget()
                    } else if (subtitle != null) {
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = subtitle,
                            style = (subtitleStyle ?: bMDefault).copy(
                                color = subtitleStyle?.color ?: subtitleColor
                            )
                        )
                    }
                }

                if (resolvedTrailingIcon != null) {
                    Spacer(modifier = Modifier.width(16.dp))
                    resolvedTrailingIcon()
                }
            }

            // Action Buttons
            if (actions != null) {
                Spacer(modifier = Modifier.height(12.dp))
                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = actionsAlignment,
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    actions()
                }
            }
        }
    }
}

// ═══════════════════════════════════════════════════════
// Banner Top Overlay Manager / Host
// ═══════════════════════════════════════════════════════

/**
 * Represents the data for showing a banner via the banner host/manager.
 */
@Immutable
data class Ux4gBannerData(
    val variant: Ux4gBannerVariant,
    val title: String,
    val subtitle: String? = null,
    val leadingIcon: (@Composable () -> Unit)? = null,
    val actions: (@Composable () -> Unit)? = null,
    val durationMs: Long = 4000L,
    val autoDismiss: Boolean = true,
    val onDismiss: (() -> Unit)? = null
)

/**
 * Controller state to trigger top animated status banners across the application.
 */
class Ux4gBannerHostState {
    var currentBanner by mutableStateOf<Ux4gBannerData?>(null)
        private set

    fun show(banner: Ux4gBannerData) {
        currentBanner = banner
    }

    fun hide() {
        currentBanner?.onDismiss?.invoke()
        currentBanner = null
    }
}

@Composable
fun rememberUx4gBannerHostState(): Ux4gBannerHostState {
    return remember { Ux4gBannerHostState() }
}

/**
 * A host composable that displays top floating animated banners.
 */
@Composable
fun Ux4gBannerHost(
    hostState: Ux4gBannerHostState,
    modifier: Modifier = Modifier
) {
    val banner = hostState.currentBanner

    LaunchedEffect(banner) {
        if (banner != null && banner.autoDismiss) {
            delay(banner.durationMs)
            hostState.hide()
        }
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 8.dp),
        contentAlignment = Alignment.TopCenter
    ) {
        AnimatedVisibility(
            visible = banner != null,
            enter = slideInVertically(
                initialOffsetY = { -it },
                animationSpec = tween(durationMillis = 350)
            ) + fadeIn(),
            exit = slideOutVertically(
                targetOffsetY = { -it },
                animationSpec = tween(durationMillis = 250)
            ) + fadeOut()
        ) {
            if (banner != null) {
                Ux4gStatusBanner(
                    variant = banner.variant,
                    title = banner.title,
                    subtitle = banner.subtitle,
                    leadingIcon = banner.leadingIcon,
                    actions = banner.actions,
                    onDismiss = { hostState.hide() }
                )
            }
        }
    }
}

// ═══════════════════════════════════════════════════════
// Previews
// ═══════════════════════════════════════════════════════

@Preview(name = "Status Banner - Light", showBackground = true, backgroundColor = 0xFFFFFFFF)
@Preview(name = "Status Banner - Dark", uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true, backgroundColor = 0xFF121212)
@Composable
private fun Ux4gStatusBannerPreview() {
    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Warning Light
        Ux4gStatusBanner(
            variant = Ux4gBannerVariant.WarningLight,
            title = "Warning: Action Required",
            subtitle = "Your subscription expires in 3 days. Please renew to keep services active.",
            leadingIcon = {
                Icon(Icons.Default.Warning, contentDescription = null, tint = Ux4gColors.orange600)
            },
            onDismiss = {}
        )

        // Warning Solid
        Ux4gStatusBanner(
            variant = Ux4gBannerVariant.WarningSolid,
            title = "System Maintenance Scheduled",
            subtitle = "The platform will undergo scheduled updates tonight from 2 AM to 4 AM UTC.",
            leadingIcon = {
                Icon(Icons.Default.Warning, contentDescription = null, tint = Ux4gColors.neutral0)
            },
            onDismiss = {}
        )

        // Error Light
        Ux4gStatusBanner(
            variant = Ux4gBannerVariant.ErrorLight,
            title = "Payment Failed",
            subtitle = "We could not process your latest invoice. Please update your payment method.",
            leadingIcon = {
                Icon(Icons.Default.Error, contentDescription = null, tint = Ux4gColors.red600)
            },
            actions = {
                Ux4gButton(
                    text = "Retry Payment",
                    size = Ux4gButtonSize.Small,
                    variant = Ux4gButtonVariant.Primary,
                    onClick = {}
                )
                Spacer(modifier = Modifier.width(8.dp))
                Ux4gButton(
                    text = "Update Card",
                    size = Ux4gButtonSize.Small,
                    variant = Ux4gButtonVariant.Outline,
                    onClick = {}
                )
            },
            onDismiss = {}
        )

        // Success Light
        Ux4gStatusBanner(
            variant = Ux4gBannerVariant.SuccessLight,
            title = "Deployment Successful",
            subtitle = "Build #4289 has been pushed to production successfully.",
            leadingIcon = {
                Icon(Icons.Default.CheckCircle, contentDescription = null, tint = Ux4gColors.green600)
            },
            onDismiss = {}
        )

        // Primary / Saving Light
        Ux4gStatusBanner(
            variant = Ux4gBannerVariant.PrimaryLight,
            title = "Changes Saved",
            subtitle = "Your profile information and preferences have been synchronized.",
            leadingIcon = {
                Icon(Icons.Default.Info, contentDescription = null, tint = Ux4gColors.primary)
            },
            onDismiss = {}
        )

        // Info Light
        Ux4gStatusBanner(
            variant = Ux4gBannerVariant.InfoLight,
            title = "New Features Available",
            subtitle = "Check out the updated component guidelines in the documentation portal.",
            leadingIcon = {
                Icon(Icons.Default.Info, contentDescription = null, tint = Ux4gColors.blue600)
            },
            onDismiss = {}
        )

        // Neutral Light
        Ux4gStatusBanner(
            variant = Ux4gBannerVariant.NeutralLight,
            title = "Cookie Preferences",
            subtitle = "We use cookies to improve your browsing experience.",
            onDismiss = {}
        )
    }
}
