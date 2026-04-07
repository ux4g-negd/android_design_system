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
    get() =
            when (this) {
                Ux4gAvatarSize.XS -> (-4).dp
                Ux4gAvatarSize.S -> (-8).dp
                Ux4gAvatarSize.M -> (-10).dp
                Ux4gAvatarSize.L -> (-12).dp
                Ux4gAvatarSize.XL -> (-16).dp
                Ux4gAvatarSize.XXL -> (-20).dp
                Ux4gAvatarSize.XXXL -> (-26).dp
            }

/** Extension to get separated spacing for avatars based on their size */
val Ux4gAvatarSize.groupSpacing: Dp
    get() =
            when (this) {
                Ux4gAvatarSize.XS -> 4.dp
                Ux4gAvatarSize.S -> 6.dp
                Ux4gAvatarSize.M -> 8.dp
                Ux4gAvatarSize.L -> 10.dp
                Ux4gAvatarSize.XL -> 12.dp
                Ux4gAvatarSize.XXL -> 16.dp
                Ux4gAvatarSize.XXXL -> 24.dp
            }

/**
 * A component that displays a group of Avatars with overlap/spacing and a remaining items count
 * limit.
 *
 * @param modifier The modifier to be applied to the group layout.
 * @param items The ordered list of avatar images, initials, or icons to display.
 * @param size The size of the avatars in the group.
 * @param maxLimit The maximum number of avatars to display. If the total exceeds this number, the
 * last
 * ```
 *                 avatar is replaced with a "+X" placeholder showing the remaining count. If null, no limit is applied.
 * @param collapsed
 * ```
 * Whether the avatars should be overlapped (true) with cutouts, or spaced out (false).
 * @param borderColor The color of the stroke border around each avatar (creates the cutout effect
 * when collapsed).
 * @param borderWidth The width of the stroke border around avatars.
 */
@Composable
fun Ux4gAvatarGroup(
        modifier: Modifier = Modifier,
        items: List<Ux4gAvatarGroupItem>,
        size: Ux4gAvatarSize = Ux4gAvatarSize.M,
        maxLimit: Int? = null,
        collapsed: Boolean = true,
        borderColor: Color = Color.White,
        borderWidth: Dp = 2.dp,
        onRemainingClick: (() -> Unit)? = null
) {
    if (items.isEmpty()) return

    // Limit calculation
    // e.g. If limit is 3, and list is 4 -> we show 2 avatars, and the 3rd is the "+2" placeholder
    val actualLimit = maxLimit ?: Int.MAX_VALUE
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
                                    // Make sure first item is drawn on top, going down
                                    // left-to-right
                                    .zIndex((visibleItems.size - index).toFloat() + 1f)
                                    .let {
                                        if (collapsed)
                                                it.background(borderColor, CircleShape)
                                                        .border(
                                                                borderWidth,
                                                                borderColor,
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
                    modifier =
                            Modifier
                                    // Drawn below all avatars in the group
                                    .zIndex(0f)
                                    .let {
                                        if (collapsed)
                                                it.background(borderColor, CircleShape)
                                                        .border(
                                                                borderWidth,
                                                                borderColor,
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
