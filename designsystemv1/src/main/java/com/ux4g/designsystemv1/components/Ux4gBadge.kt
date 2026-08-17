package com.ux4g.designsystemv1.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ux4g.designsystemv1.R
import com.ux4g.designsystemv1.foundation.Ux4gColors
import com.ux4g.designsystemv1.theme.Ux4gTheme

/** Badge Limit options for text badges */
enum class Ux4gBadgeLimit {
    SingleDigit, // 9+
    DoubleDigit // 99+
}

/** Where the badge is anchored relative to its [Ux4gBadge] child. */
enum class Ux4gBadgeAlignment(val alignment: Alignment, val offset: DpOffset) {
    TopEnd(Alignment.TopEnd, DpOffset(4.dp, -4.dp)),
    TopStart(Alignment.TopStart, DpOffset(-4.dp, -4.dp)),
    BottomEnd(Alignment.BottomEnd, DpOffset(4.dp, 4.dp)),
    BottomStart(Alignment.BottomStart, DpOffset(-4.dp, 4.dp)),
}

/** Predefined badge resources for the ReadyToUse type */
object Ux4gBadgeResource {
    val Verification = R.drawable.ic_verification_badge
    val Star = R.drawable.ic_star_badge
    val Shield = R.drawable.ic_shield_badge
}

/** Badge Type options */
@Immutable
sealed class Ux4gBadgeType {
    object Dot : Ux4gBadgeType()
    data class Text(val count: Int, val limit: Ux4gBadgeLimit = Ux4gBadgeLimit.SingleDigit) :
            Ux4gBadgeType()
    data class Label(val text: String) : Ux4gBadgeType()
    data class Icon(val iconRes: Int) : Ux4gBadgeType() // Icons formatted as badges (e.g. tick)
    data class ReadyToUse(val resId: Int) : Ux4gBadgeType() // Standalone pre-designed badges
}

/**
 * A versatile Badge component for the Ux4G Design System.
 *
 * Mirrors the Flutter `Ux4gBadge` component: when [content] is provided, the badge is overlaid on
 * its corner at [alignment]; otherwise the badge is rendered standalone.
 *
 * @param type The type of badge (Dot, Text, Label, Icon, or ReadyToUse).
 * @param modifier Modifier to be applied to the badge (or its wrapper when [content] is set).
 * @param containerColor The background color of the badge. Defaults to the theme primary color
 * (ignored for ReadyToUse).
 * @param contentColor The tint color for Text/Label/Icon content. When null it is resolved from
 * [containerColor] using the theme's semantic on-colors, falling back to a luminance check.
 * @param alignment The corner on which the badge is overlaid when [content] is set.
 * @param showBorder Whether to draw a 1.5dp border around the badge.
 * @param borderColor The border color, defaults to the theme surface color.
 * @param content The child widget the badge overlays. When null the badge renders standalone.
 */
@Composable
fun Ux4gBadge(
    type: Ux4gBadgeType,
    modifier: Modifier = Modifier,
    containerColor: Color? = null,
    contentColor: Color? = null,
    alignment: Ux4gBadgeAlignment = Ux4gBadgeAlignment.TopEnd,
    showBorder: Boolean = false,
    borderColor: Color? = null,
    content: (@Composable () -> Unit)? = null,
) {
    if (content == null) {
        Ux4gBadgeContent(
            type = type,
            modifier = modifier,
            containerColor = containerColor,
            contentColor = contentColor,
            showBorder = showBorder,
            borderColor = borderColor,
        )
        return
    }

    Box(modifier = modifier) {
        content()
        Ux4gBadgeContent(
            type = type,
            modifier = Modifier
                .align(alignment.alignment)
                .offset(alignment.offset.x, alignment.offset.y),
            containerColor = containerColor,
            contentColor = contentColor,
            showBorder = showBorder,
            borderColor = borderColor,
        )
    }
}

@Composable
private fun Ux4gBadgeContent(
    type: Ux4gBadgeType,
    modifier: Modifier,
    containerColor: Color?,
    contentColor: Color?,
    showBorder: Boolean,
    borderColor: Color?,
) {
    if (type is Ux4gBadgeType.ReadyToUse) {
        Ux4gReadyToUseBadge(
            type = type,
            modifier = modifier,
            showBorder = showBorder,
            borderColor = borderColor,
        )
        return
    }

    val themeColors = Ux4gTheme.colorScheme
    val resolvedBg = containerColor ?: themeColors.primary
    val resolvedContentColor = resolveContentColor(resolvedBg, contentColor)

    when (type) {
        is Ux4gBadgeType.Dot -> Box(
            modifier = modifier
                .size(8.dp)
                .background(resolvedBg, CircleShape)
                .borderIf(showBorder, borderColor ?: themeColors.surface, CircleShape),
        )

        is Ux4gBadgeType.Text -> {
            val badgeText = when (type.limit) {
                Ux4gBadgeLimit.SingleDigit -> if (type.count > 9) "9+" else type.count.toString()
                Ux4gBadgeLimit.DoubleDigit -> if (type.count > 99) "99+" else type.count.toString()
            }
            val isSingleChar = badgeText.length == 1
            val shape = if (isSingleChar) CircleShape else RoundedCornerShape(percent = 50)
            Box(
                modifier = modifier
                    .sizeIn(minWidth = 18.dp, minHeight = 18.dp)
                    .background(resolvedBg, shape)
                    .borderIf(showBorder, borderColor ?: themeColors.surface, shape)
                    .padding(horizontal = if (isSingleChar) 0.dp else 6.dp),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = badgeText,
                    color = resolvedContentColor,
                    style = Ux4gTheme.typography.lS_strong.copy(
                        fontSize = 10.sp,
                        lineHeight = 10.sp,
                    ),
                )
            }
        }

        is Ux4gBadgeType.Label -> {
            val shape = RoundedCornerShape(4.dp)
            Box(
                modifier = modifier
                    .background(resolvedBg, shape)
                    .borderIf(showBorder, borderColor ?: themeColors.surface, shape)
                    .padding(horizontal = 8.dp, vertical = 4.dp),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = type.text,
                    color = resolvedContentColor,
                    style = Ux4gTheme.typography.lS_strong.copy(lineHeight = 11.sp),
                )
            }
        }

        is Ux4gBadgeType.Icon -> Box(
            modifier = modifier
                .size(18.dp)
                .background(resolvedBg, CircleShape)
                .borderIf(showBorder, borderColor ?: themeColors.surface, CircleShape),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                painter = painterResource(id = type.iconRes),
                contentDescription = null,
                tint = resolvedContentColor,
                modifier = Modifier.size(12.dp),
            )
        }

        is Ux4gBadgeType.ReadyToUse -> Unit // Handled early
    }
}

@Composable
private fun Ux4gReadyToUseBadge(
    type: Ux4gBadgeType.ReadyToUse,
    modifier: Modifier,
    showBorder: Boolean,
    borderColor: Color?,
) {
    val image: @Composable () -> Unit = {
        Image(
            painter = painterResource(id = type.resId),
            contentDescription = null,
            modifier = Modifier.size(18.dp),
        )
    }

    if (showBorder) {
        Box(
            modifier = modifier
                .size(20.dp)
                .border(1.5.dp, borderColor ?: Ux4gTheme.colorScheme.surface, CircleShape),
            contentAlignment = Alignment.Center,
        ) { image() }
    } else {
        Box(modifier = modifier) { image() }
    }
}

/**
 * Resolves the content color the same way the Flutter `Ux4gBadge` does: explicit [contentColor]
 * wins, otherwise theme semantic on-colors are used for known backgrounds, with a luminance-based
 * fallback (dark background -> white, light background -> neutral900).
 */
@Composable
private fun resolveContentColor(resolvedBg: Color, contentColor: Color?): Color {
    if (contentColor != null) return contentColor
    val colors = Ux4gTheme.colorScheme
    return when (resolvedBg) {
        Ux4gColors.neutral600 -> Ux4gColors.white
        Ux4gColors.neutral300 -> Ux4gColors.neutral900
        Ux4gColors.orange600 -> Ux4gColors.white
        colors.primary -> colors.onPrimary
        colors.secondary -> colors.onSecondary
        colors.success -> colors.onSuccess
        colors.error -> colors.onError
        colors.warning -> colors.onWarning
        colors.info -> colors.onInfo
        else -> if (isLightColor(resolvedBg)) Ux4gColors.neutral900 else Ux4gColors.white
    }
}

/** Mirrors Flutter's `ThemeData.estimateBrightnessForColor`. */
private fun isLightColor(color: Color): Boolean =
    (0.299 * color.red + 0.587 * color.green + 0.114 * color.blue) >= 0.5

private fun Modifier.borderIf(show: Boolean, color: Color, shape: Shape): Modifier =
    if (show) border(1.5.dp, color, shape) else this

@Preview(name = "Badge", showBackground = true)
@Composable
private fun Ux4gBadgePreview() {
    Ux4gTheme {
        Column(
            modifier = Modifier.padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp),
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(24.dp)) {
                Ux4gBadge(type = Ux4gBadgeType.Dot, containerColor = Ux4gColors.primary)
                Ux4gBadge(type = Ux4gBadgeType.Dot, containerColor = Ux4gColors.green)
                Ux4gBadge(type = Ux4gBadgeType.Dot, containerColor = Ux4gColors.gold)
                Ux4gBadge(type = Ux4gBadgeType.Dot, containerColor = Ux4gColors.red)
                Ux4gBadge(type = Ux4gBadgeType.Dot, containerColor = Ux4gColors.neutral300)
                Ux4gBadge(type = Ux4gBadgeType.Dot, containerColor = Ux4gColors.blue)
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(24.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Ux4gBadge(type = Ux4gBadgeType.Text(3))
                Ux4gBadge(type = Ux4gBadgeType.Text(32))
                Ux4gBadge(
                    type = Ux4gBadgeType.Text(150, Ux4gBadgeLimit.DoubleDigit),
                    containerColor = Ux4gColors.secondary,
                )
                Ux4gBadge(
                    type = Ux4gBadgeType.Text(12),
                    containerColor = Ux4gColors.neutral300,
                    showBorder = true,
                )
            }

            Row(horizontalArrangement = Arrangement.spacedBy(24.dp)) {
                Ux4gBadge(type = Ux4gBadgeType.Label("New"), containerColor = Ux4gColors.primary)
                Ux4gBadge(type = Ux4gBadgeType.Label("Beta"), containerColor = Ux4gColors.green)
                Ux4gBadge(
                    type = Ux4gBadgeType.Label("Draft"),
                    containerColor = Ux4gColors.neutral600,
                )
            }

            Row(horizontalArrangement = Arrangement.spacedBy(24.dp)) {
                Ux4gBadge(
                    type = Ux4gBadgeType.Icon(R.drawable.ic_tick),
                    containerColor = Ux4gColors.primary,
                )
                Ux4gBadge(
                    type = Ux4gBadgeType.Icon(R.drawable.ic_tick),
                    containerColor = Ux4gColors.green,
                )
                Ux4gBadge(
                    type = Ux4gBadgeType.Icon(R.drawable.ic_tick),
                    containerColor = Ux4gColors.red,
                    contentColor = Ux4gColors.white,
                )
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(24.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Ux4gBadge(type = Ux4gBadgeType.ReadyToUse(Ux4gBadgeResource.Verification))
                Ux4gBadge(type = Ux4gBadgeType.ReadyToUse(Ux4gBadgeResource.Star))
                Ux4gBadge(type = Ux4gBadgeType.ReadyToUse(Ux4gBadgeResource.Shield))
                Ux4gBadge(
                    type = Ux4gBadgeType.ReadyToUse(Ux4gBadgeResource.Shield),
                    showBorder = true,
                )
            }
        }
    }
}

@Preview(name = "Badge Overlay", showBackground = true)
@Composable
private fun Ux4gBadgeOverlayPreview() {
    Ux4gTheme {
        Row(
            modifier = Modifier.padding(24.dp),
            horizontalArrangement = Arrangement.spacedBy(32.dp),
        ) {
            Ux4gBadge(type = Ux4gBadgeType.Text(99)) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .background(Ux4gColors.neutral300, RoundedCornerShape(8.dp)),
                )
            }
            Ux4gBadge(
                type = Ux4gBadgeType.Dot,
                containerColor = Ux4gColors.red,
                alignment = Ux4gBadgeAlignment.TopStart,
            ) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .background(Ux4gColors.neutral300, RoundedCornerShape(8.dp)),
                )
            }
            Ux4gBadge(
                type = Ux4gBadgeType.Label("New"),
                containerColor = Ux4gColors.green,
                alignment = Ux4gBadgeAlignment.TopEnd,
            ) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .background(Ux4gColors.neutral300, RoundedCornerShape(8.dp)),
                )
            }
            Ux4gBadge(
                type = Ux4gBadgeType.Dot,
                containerColor = Ux4gColors.blue,
                alignment = Ux4gBadgeAlignment.BottomStart,
            ) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .background(Ux4gColors.neutral300, RoundedCornerShape(8.dp)),
                )
            }
        }
    }
}
