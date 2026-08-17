package com.ux4g.designsystemv1.components.avatar_component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.compose.foundation.isSystemInDarkTheme
import com.ux4g.designsystemv1.foundation.Ux4gColors
import com.ux4g.designsystemv1.foundation.Ux4gIcon
import com.ux4g.designsystemv1.foundation.Ux4gIcons
import com.ux4g.designsystemv1.theme.Ux4gTheme

/** Represents a single avatar item within the Avatar Group. */
data class Ux4gAvatarGroupItem(
        val imageUrl: Any? = null,
        val initials: String? = null,
        val icon: Ux4gIcon? = Ux4gIcons.Person,
        val onClick: (() -> Unit)? = null
)

/** Extension to get overlapping spacing for avatars based on their size */
val Ux4gAvatarSize.overlapSpacing: Dp
    get() = -(this.size * 0.25f)

/** Extension to get separated spacing for avatars based on their size */
val Ux4gAvatarSize.groupSpacing: Dp
    get() = 8.dp

/**
 * A component that displays a group of Avatars with overlap/spacing and a remaining items count
 * limit. The right-most avatar is stacked on top of preceding avatars.
 *
 * @param modifier The modifier to be applied to the group layout.
 * @param items The ordered list of avatar images, initials, or icons to display.
 * @param size The size of the avatars in the group.
 * @param maxLimit The maximum number of avatars to display. If the total exceeds this number, the
 *                 last avatar is replaced with a "+X" placeholder showing the remaining count. If null, no limit is applied.
 * @param collapsed Whether the avatars should be overlapped (true) with cutouts, or spaced out (false).
 * @param borderColor The color of the stroke border around each avatar (creates the cutout effect
 *                    when collapsed). Defaults to theme surface color.
 * @param borderWidth The width of the stroke border around avatars.
 * @param onRemainingClick Callback when the remaining "+X" counter is clicked.
 */
@Composable
fun Ux4gAvatarGroup(
        modifier: Modifier = Modifier,
        items: List<Ux4gAvatarGroupItem>,
        size: Ux4gAvatarSize = Ux4gAvatarSize.M,
        maxLimit: Int? = null,
        collapsed: Boolean = true,
        borderColor: Color? = null,
        borderWidth: Dp = 2.dp,
        onRemainingClick: (() -> Unit)? = null
) {
    if (items.isEmpty()) return

    val resolvedBorderColor = borderColor ?: Ux4gTheme.colorScheme.surface
    val isDark = androidx.compose.foundation.isSystemInDarkTheme()
    val overflowBgColor = if (isDark) Ux4gColors.primary900 else Ux4gColors.primary50
    val overflowTextColor = if (isDark) Ux4gColors.primary300 else Ux4gColors.primary900

    // Limit calculation
    // e.g. If limit is 3, and list is 4 -> we show 2 avatars, and the 3rd is the "+2" placeholder
    val actualLimit = maxLimit ?: items.size
    val exceedsLimit = items.size > actualLimit
    val visibleItems = if (exceedsLimit) items.take(actualLimit - 1) else items
    val remainingCount = if (exceedsLimit) items.size - (actualLimit - 1) else 0

    val spacing = if (collapsed) size.overlapSpacing else size.groupSpacing

    Row(
            modifier = modifier,
            horizontalArrangement = Arrangement.spacedBy(spacing),
            verticalAlignment = Alignment.CenterVertically
    ) {
        visibleItems.forEachIndexed { index, item ->
            Ux4gAvatar(
                    size = size,
                    imageUrl = item.imageUrl,
                    initials = item.initials,
                    icon = item.icon,
                    modifier =
                            Modifier
                                    // Rightmost avatar is stacked on top of previous items
                                    .zIndex((index + 1).toFloat())
                                    .let {
                                        if (collapsed)
                                                it.background(resolvedBorderColor, CircleShape)
                                                        .border(
                                                                borderWidth,
                                                                resolvedBorderColor,
                                                                CircleShape
                                                        )
                                        else it
                                    }
                                    .clip(CircleShape)
                                    .let { m ->
                                        item.onClick?.let { m.clickable(onClick = it) } ?: m
                                    }
            )
        }

        if (remainingCount > 0) {
            Ux4gAvatar(
                    size = size,
                    initials = "+$remainingCount",
                    containerColor = overflowBgColor,
                    contentColor = overflowTextColor,
                    modifier =
                            Modifier
                                    // Rightmost "+X" placeholder is stacked on top of all preceding avatars
                                    .zIndex((visibleItems.size + 1).toFloat())
                                    .let {
                                        if (collapsed)
                                                it.background(resolvedBorderColor, CircleShape)
                                                        .border(
                                                                borderWidth,
                                                                resolvedBorderColor,
                                                                CircleShape
                                                        )
                                        else it
                                    }
                                    .clip(CircleShape)
                                    .let { m ->
                                        onRemainingClick?.let { m.clickable(onClick = it) } ?: m
                                    }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun Ux4gAvatarGroupPreview() {
    val items =
            listOf(
                    Ux4gAvatarGroupItem(initials = "JD"),
                    Ux4gAvatarGroupItem(initials = "AS"),
                    Ux4gAvatarGroupItem(initials = "MK"),
                    Ux4gAvatarGroupItem(initials = "RJ"),
                    Ux4gAvatarGroupItem(initials = "LS"),
                    Ux4gAvatarGroupItem(initials = "EM")
            )

    Ux4gTheme {
        Column(modifier = Modifier.padding(16.dp)) {
            // Collapsed true, no limit given (shows all overlapping with border)
            Ux4gAvatarGroup(items = items, size = Ux4gAvatarSize.S, collapsed = true)

            Spacer(modifier = Modifier.height(32.dp))

            // Collapsed false, no limit given (shows all separated without border)
            Ux4gAvatarGroup(items = items, size = Ux4gAvatarSize.S, collapsed = false)

            Spacer(modifier = Modifier.height(32.dp))

            // Collapsed true, limit 4 (shows 3 actual avatars + 1 remaining counter -> "+3")
            Ux4gAvatarGroup(items = items, size = Ux4gAvatarSize.S, maxLimit = 4, collapsed = true)
        }
    }
}
