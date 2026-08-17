package com.ux4g.designsystemv1.components

import android.content.res.Configuration
import androidx.annotation.DrawableRes
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ux4g.designsystemv1.R
import com.ux4g.designsystemv1.foundation.Ux4gColors
import com.ux4g.designsystemv1.foundation.Ux4gIcon
import com.ux4g.designsystemv1.foundation.Ux4gIconComponent
import com.ux4g.designsystemv1.theme.Ux4gTheme

/**
 * Badge limit options for count/numeric badges:
 * - [SingleDigit]: caps at "9+" when count > 9
 * - [DoubleDigit]: caps at "99+" when count > 99
 */
enum class Ux4gBadgeLimit {
    SingleDigit, // 9+
    DoubleDigit; // 99+

    companion object {
        val singleDigit = SingleDigit
        val doubleDigit = DoubleDigit
    }
}

/**
 * Position where the badge is anchored relative to its child widget.
 */
enum class Ux4gBadgeAlignment(val alignment: Alignment, val offset: DpOffset) {
    TopEnd(Alignment.TopEnd, DpOffset(4.dp, (-4).dp)),
    TopStart(Alignment.TopStart, DpOffset((-4).dp, (-4).dp)),
    BottomEnd(Alignment.BottomEnd, DpOffset(4.dp, 4.dp)),
    BottomStart(Alignment.BottomStart, DpOffset((-4).dp, 4.dp));

    companion object {
        val topRight = TopEnd
        val topLeft = TopStart
        val bottomRight = BottomEnd
        val bottomLeft = BottomStart
    }
}

/**
 * Predefined badge drawables for the [Ux4gBadgeType.ReadyToUse] badge.
 */
object Ux4gBadgeResource {
    val Verification = R.drawable.ic_verification_badge
    val Star = R.drawable.ic_star_badge
    val Shield = R.drawable.ic_shield_badge
}

/**
 * Badge types for [Ux4gBadge]:
 * - [Dot]: Simple presence indicator (8x8 dp dot).
 * - [Count]: Shows numeric count formatted as single digit (9+) or double digit (99+).
 * - [Label]: Displays short status text pill with 4dp corner radius.
 * - [Icon]: Displays a small icon inside an 18x18 dp circular badge.
 * - [ReadyToUse]: Renders a pre-designed badge asset (e.g. verification, star, shield).
 */
@Immutable
sealed class Ux4gBadgeType {
    object Dot : Ux4gBadgeType()

    data class Count(
        val count: Int,
        val limit: Ux4gBadgeLimit = Ux4gBadgeLimit.SingleDigit
    ) : Ux4gBadgeType()

    // Alias for Text
    data class Text(
        val count: Int,
        val limit: Ux4gBadgeLimit = Ux4gBadgeLimit.SingleDigit
    ) : Ux4gBadgeType()

    data class Label(val text: String) : Ux4gBadgeType()

    data class Icon(val icon: Ux4gIcon) : Ux4gBadgeType() {
        constructor(@DrawableRes resId: Int) : this(Ux4gIcon.Drawable(resId))
        constructor(imageVector: ImageVector) : this(Ux4gIcon.Vector(imageVector))
    }

    data class ReadyToUse(@DrawableRes val resId: Int) : Ux4gBadgeType()
}

/**
 * A versatile Badge component for the UX4G Design System.
 *
 * Mirrors the Flutter `Ux4gBadge` component: when [content] is provided, the badge is overlaid on
 * its corner at [alignment]; otherwise the badge is rendered standalone.
 *
 * @param type The type of badge ([Ux4gBadgeType.Dot], [Ux4gBadgeType.Count], [Ux4gBadgeType.Label], [Ux4gBadgeType.Icon], or [Ux4gBadgeType.ReadyToUse]).
 * @param modifier Modifier applied to the outer badge container (or overlay wrapper).
 * @param containerColor Background color of the badge. Defaults to theme's primary color.
 * @param contentColor Text/icon color. Resolved automatically using design tokens and brightness fallback when null.
 * @param alignment Position where badge overlays [content] (default [Ux4gBadgeAlignment.TopEnd]).
 * @param showBorder Whether to draw an outer border stroke (1.5dp).
 * @param borderColor Color of the outer border. Defaults to surface color.
 * @param content Optional child composable that the badge will anchor onto.
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
                .offset(x = alignment.offset.x, y = alignment.offset.y),
            containerColor = containerColor,
            contentColor = contentColor,
            showBorder = showBorder,
            borderColor = borderColor,
        )
    }
}

/**
 * Convenience composable for Dot Badge.
 */
@Composable
fun Ux4gBadgeDot(
    modifier: Modifier = Modifier,
    containerColor: Color? = null,
    alignment: Ux4gBadgeAlignment = Ux4gBadgeAlignment.TopEnd,
    showBorder: Boolean = false,
    borderColor: Color? = null,
    content: (@Composable () -> Unit)? = null,
) = Ux4gBadge(
    type = Ux4gBadgeType.Dot,
    modifier = modifier,
    containerColor = containerColor,
    alignment = alignment,
    showBorder = showBorder,
    borderColor = borderColor,
    content = content
)

/**
 * Convenience composable for Count Badge.
 */
@Composable
fun Ux4gBadgeCount(
    count: Int,
    modifier: Modifier = Modifier,
    limit: Ux4gBadgeLimit = Ux4gBadgeLimit.SingleDigit,
    containerColor: Color? = null,
    contentColor: Color? = null,
    alignment: Ux4gBadgeAlignment = Ux4gBadgeAlignment.TopEnd,
    showBorder: Boolean = false,
    borderColor: Color? = null,
    content: (@Composable () -> Unit)? = null,
) = Ux4gBadge(
    type = Ux4gBadgeType.Count(count = count, limit = limit),
    modifier = modifier,
    containerColor = containerColor,
    contentColor = contentColor,
    alignment = alignment,
    showBorder = showBorder,
    borderColor = borderColor,
    content = content
)

/**
 * Convenience composable for Label Badge.
 */
@Composable
fun Ux4gBadgeLabel(
    label: String,
    modifier: Modifier = Modifier,
    containerColor: Color? = null,
    contentColor: Color? = null,
    alignment: Ux4gBadgeAlignment = Ux4gBadgeAlignment.TopEnd,
    showBorder: Boolean = false,
    borderColor: Color? = null,
    content: (@Composable () -> Unit)? = null,
) = Ux4gBadge(
    type = Ux4gBadgeType.Label(label),
    modifier = modifier,
    containerColor = containerColor,
    contentColor = contentColor,
    alignment = alignment,
    showBorder = showBorder,
    borderColor = borderColor,
    content = content
)

/**
 * Convenience composable for Icon Badge.
 */
@Composable
fun Ux4gBadgeIcon(
    icon: Ux4gIcon,
    modifier: Modifier = Modifier,
    containerColor: Color? = null,
    contentColor: Color? = null,
    alignment: Ux4gBadgeAlignment = Ux4gBadgeAlignment.TopEnd,
    showBorder: Boolean = false,
    borderColor: Color? = null,
    content: (@Composable () -> Unit)? = null,
) = Ux4gBadge(
    type = Ux4gBadgeType.Icon(icon),
    modifier = modifier,
    containerColor = containerColor,
    contentColor = contentColor,
    alignment = alignment,
    showBorder = showBorder,
    borderColor = borderColor,
    content = content
)

/**
 * Convenience composable for ReadyToUse Badge.
 */
@Composable
fun Ux4gBadgeReadyToUse(
    @DrawableRes resId: Int,
    modifier: Modifier = Modifier,
    alignment: Ux4gBadgeAlignment = Ux4gBadgeAlignment.TopEnd,
    showBorder: Boolean = false,
    borderColor: Color? = null,
    content: (@Composable () -> Unit)? = null,
) = Ux4gBadge(
    type = Ux4gBadgeType.ReadyToUse(resId),
    modifier = modifier,
    alignment = alignment,
    showBorder = showBorder,
    borderColor = borderColor,
    content = content
)

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
    val resolvedBorderColor = borderColor ?: themeColors.surface

    when (type) {
        is Ux4gBadgeType.Dot -> {
            Box(
                modifier = modifier
                    .size(8.dp)
                    .background(resolvedBg, CircleShape)
                    .borderIf(showBorder, resolvedBorderColor, CircleShape),
            )
        }

        is Ux4gBadgeType.Count,
        is Ux4gBadgeType.Text -> {
            val count = if (type is Ux4gBadgeType.Count) type.count else (type as Ux4gBadgeType.Text).count
            val limit = if (type is Ux4gBadgeType.Count) type.limit else (type as Ux4gBadgeType.Text).limit

            val badgeText = when (limit) {
                Ux4gBadgeLimit.SingleDigit -> if (count > 9) "9+" else count.toString()
                Ux4gBadgeLimit.DoubleDigit -> if (count > 99) "99+" else count.toString()
            }
            val isSingleChar = badgeText.length == 1
            val shape = if (isSingleChar) CircleShape else RoundedCornerShape(100.dp)

            Box(
                modifier = modifier
                    .sizeIn(minWidth = 18.dp, minHeight = 18.dp)
                    .background(resolvedBg, shape)
                    .borderIf(showBorder, resolvedBorderColor, shape)
                    .padding(horizontal = if (isSingleChar) 0.dp else 6.dp),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = badgeText,
                    color = resolvedContentColor,
                    textAlign = TextAlign.Center,
                    style = Ux4gTheme.typography.lS_strong.copy(
                        fontSize = 10.sp,
                        lineHeight = 10.sp,
                        fontWeight = FontWeight.Bold
                    ),
                )
            }
        }

        is Ux4gBadgeType.Label -> {
            val shape = RoundedCornerShape(4.dp)
            Box(
                modifier = modifier
                    .background(resolvedBg, shape)
                    .borderIf(showBorder, resolvedBorderColor, shape)
                    .padding(horizontal = 8.dp, vertical = 4.dp),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = type.text,
                    color = resolvedContentColor,
                    style = Ux4gTheme.typography.lS_strong.copy(
                        lineHeight = 11.sp,
                        fontWeight = FontWeight.Bold
                    ),
                )
            }
        }

        is Ux4gBadgeType.Icon -> {
            Box(
                modifier = modifier
                    .size(18.dp)
                    .background(resolvedBg, CircleShape)
                    .borderIf(showBorder, resolvedBorderColor, CircleShape),
                contentAlignment = Alignment.Center,
            ) {
                Ux4gIconComponent(
                    icon = type.icon,
                    contentDescription = null,
                    modifier = Modifier.size(12.dp),
                    tint = resolvedContentColor
                )
            }
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
        ) {
            image()
        }
    } else {
        Box(modifier = modifier) {
            image()
        }
    }
}

/**
 * Resolves content color identically to Flutter `Ux4gBadge`:
 * Explicit [contentColor] wins, followed by design system color mappings, and luminance fallback.
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

/**
 * Determines whether a color is light using standard luminance calculation.
 */
private fun isLightColor(color: Color): Boolean =
    (0.299 * color.red + 0.587 * color.green + 0.114 * color.blue) >= 0.5

private fun Modifier.borderIf(show: Boolean, color: Color, shape: Shape, width: Dp = 1.5.dp): Modifier =
    if (show) border(width, color, shape) else this

@Preview(name = "Badge Showcase", showBackground = true)
@Preview(
    name = "Badge Showcase Dark",
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true
)
@Composable
private fun Ux4gBadgePreview() {
    Ux4gTheme {
        Column(
            modifier = Modifier.padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp),
        ) {
            // Dot Badges
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                Ux4gBadge(type = Ux4gBadgeType.Dot, containerColor = Ux4gColors.primary600)
                Ux4gBadge(type = Ux4gBadgeType.Dot, containerColor = Ux4gColors.neutral300)
                Ux4gBadge(type = Ux4gBadgeType.Dot, containerColor = Ux4gColors.cyan600)
                Ux4gBadge(type = Ux4gBadgeType.Dot, containerColor = Ux4gColors.green600)
                Ux4gBadge(type = Ux4gBadgeType.Dot, containerColor = Ux4gColors.red600)
                Ux4gBadge(type = Ux4gBadgeType.Dot, containerColor = Ux4gColors.orange600)
            }

            // Digit / Count Badges
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Ux4gBadge(type = Ux4gBadgeType.Count(1), containerColor = Ux4gColors.primary600)
                Ux4gBadge(type = Ux4gBadgeType.Count(3), containerColor = Ux4gColors.neutral300)
                Ux4gBadge(
                    type = Ux4gBadgeType.Count(11, Ux4gBadgeLimit.DoubleDigit),
                    containerColor = Ux4gColors.cyan600,
                )
                Ux4gBadge(
                    type = Ux4gBadgeType.Count(22, Ux4gBadgeLimit.DoubleDigit),
                    containerColor = Ux4gColors.green600,
                )
                Ux4gBadge(
                    type = Ux4gBadgeType.Count(150, Ux4gBadgeLimit.DoubleDigit),
                    containerColor = Ux4gColors.red600,
                )
                Ux4gBadge(
                    type = Ux4gBadgeType.Count(12),
                    containerColor = Ux4gColors.neutral300,
                    showBorder = true,
                )
            }

            // Label Badges
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                Ux4gBadge(type = Ux4gBadgeType.Label("NEW"), containerColor = Ux4gColors.primary600)
                Ux4gBadge(type = Ux4gBadgeType.Label("BETA"), containerColor = Ux4gColors.green600)
                Ux4gBadge(
                    type = Ux4gBadgeType.Label("DRAFT"),
                    containerColor = Ux4gColors.neutral600,
                )
                Ux4gBadge(
                    type = Ux4gBadgeType.Label("EXACT"),
                    containerColor = Ux4gColors.orange600,
                )
            }

            // Icon Badges
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                Ux4gBadge(
                    type = Ux4gBadgeType.Icon(Icons.Filled.Check),
                    containerColor = Ux4gColors.primary600,
                )
                Ux4gBadge(
                    type = Ux4gBadgeType.Icon(Icons.Filled.Notifications),
                    containerColor = Ux4gColors.secondary600,
                )
                Ux4gBadge(
                    type = Ux4gBadgeType.Icon(Icons.Filled.Star),
                    containerColor = Ux4gColors.green600,
                )
                Ux4gBadge(
                    type = Ux4gBadgeType.Icon(Icons.Filled.Warning),
                    containerColor = Ux4gColors.orange600,
                )
                Ux4gBadge(
                    type = Ux4gBadgeType.Icon(Icons.Filled.Close),
                    containerColor = Ux4gColors.red600,
                )
                Ux4gBadge(
                    type = Ux4gBadgeType.Icon(Icons.Outlined.Info),
                    containerColor = Ux4gColors.cyan600,
                )
            }

            // Ready To Use Badges
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
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
            Ux4gBadge(type = Ux4gBadgeType.Count(99)) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .background(Ux4gColors.neutral300, RoundedCornerShape(8.dp)),
                )
            }
            Ux4gBadge(
                type = Ux4gBadgeType.Dot,
                containerColor = Ux4gColors.red600,
                alignment = Ux4gBadgeAlignment.TopStart,
            ) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .background(Ux4gColors.neutral300, RoundedCornerShape(8.dp)),
                )
            }
            Ux4gBadge(
                type = Ux4gBadgeType.Label("NEW"),
                containerColor = Ux4gColors.green600,
                alignment = Ux4gBadgeAlignment.TopEnd,
            ) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .background(Ux4gColors.neutral300, RoundedCornerShape(8.dp)),
                )
            }
            Ux4gBadge(
                type = Ux4gBadgeType.Icon(Icons.Filled.Person),
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
