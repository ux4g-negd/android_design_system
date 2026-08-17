package com.ux4g.designsystemv1.components.avatar_component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.ux4g.designsystemv1.foundation.Ux4gIcon
import com.ux4g.designsystemv1.foundation.Ux4gIcons
import com.ux4g.designsystemv1.foundation.Ux4gColors
import com.ux4g.designsystemv1.theme.Ux4gTheme

/** Variants for Ux4gStatusAvatar indicator. */
sealed class Ux4gStatusAvatarVariant {
    abstract val color: Color

    data object Online : Ux4gStatusAvatarVariant() {
        override val color: Color get() = Ux4gColors.green500
    }
    data object Offline : Ux4gStatusAvatarVariant() {
        override val color: Color get() = Ux4gColors.neutral900.copy(alpha = 0.5f)
    }
    data object Busy : Ux4gStatusAvatarVariant() {
        override val color: Color get() = Ux4gColors.red600
    }
    data object Success : Ux4gStatusAvatarVariant() {
        override val color: Color get() = Ux4gColors.green500
    }
    data object Error : Ux4gStatusAvatarVariant() {
        override val color: Color get() = Ux4gColors.red600
    }
    data object Warning : Ux4gStatusAvatarVariant() {
        override val color: Color get() = Ux4gColors.orange500
    }
    class Custom(val customColor: Color) : Ux4gStatusAvatarVariant() {
        override val color: Color get() = customColor
    }
}

/** Extension to get the proper size for the status indicator based on Avatar size. */
val Ux4gAvatarSize.statusIndicatorSize: Dp
    get() =
            when (this) {
                Ux4gAvatarSize.XS -> 10.dp
                Ux4gAvatarSize.S -> 12.dp
                Ux4gAvatarSize.M -> 14.dp
                Ux4gAvatarSize.L -> 16.dp
                Ux4gAvatarSize.XL -> 20.dp
                Ux4gAvatarSize.XXL -> 24.dp
                Ux4gAvatarSize.XXXL -> 34.dp
            }

/**
 * A Status Avatar component built on top of [Ux4gAvatar] that adds support for an online/offline
 * status indicator positioned at the bottom right.
 *
 * @param modifier The modifier to be applied to the avatar.
 * @param size The size of the avatar (XS, S, M, L, XL, XXL, XXXL).
 * @param imageUrl Allows overriding the image. Can be a URL, resource ID, or file.
 * @param initials The initials to display (max 2 characters).
 * @param avatarIcon The fallback icon to show when no image or initials are set.
 * @param variant The status (e.g. Online, Offline) to overlay on the avatar.
 * @param statusSize Optional custom size for the status indicator to override the default.
 */
@Composable
fun Ux4gStatusAvatar(
        modifier: Modifier = Modifier,
        size: Ux4gAvatarSize = Ux4gAvatarSize.M,
        imageUrl: Any? = null,
        initials: String? = null,
        avatarIcon: Ux4gIcon? = Ux4gIcons.Person,
        variant: Ux4gStatusAvatarVariant = Ux4gStatusAvatarVariant.Online,
        statusSize: Dp? = null
) {
    Box(modifier = modifier) {
        // Base Avatar
        Ux4gAvatar(
                size = size,
                imageUrl = imageUrl,
                initials = initials,
                icon = avatarIcon,
                modifier = Modifier
        )

        val indicatorSize = statusSize ?: size.statusIndicatorSize

        Box(modifier = Modifier.align(Alignment.BottomEnd).size(indicatorSize)) {
            Spacer(
                    modifier =
                            Modifier.matchParentSize()
                                    .background(Ux4gTheme.colorScheme.surface, CircleShape)
                                    .padding(2.dp)
                                    .background(variant.color, CircleShape)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun Ux4gStatusAvatarPreview() {
    Ux4gTheme {
        Row(modifier = Modifier.padding(16.dp)) {
            // Online Status Variant
            Ux4gStatusAvatar(
                    initials = "MS",
                    size = Ux4gAvatarSize.L,
                    variant = Ux4gStatusAvatarVariant.Online
            )
            Spacer(modifier = Modifier.width(16.dp))

            // Warning Status Variant
            Ux4gStatusAvatar(
                    size = Ux4gAvatarSize.L,
                    variant = Ux4gStatusAvatarVariant.Warning,
                    imageUrl =
                            "https://miro.medium.com/v2/resize:fill:72:72/1*NfVk3LQfhRaDFW8qZ4MhCQ.jpeg"
            )

            Spacer(modifier = Modifier.width(16.dp))

            // Error Status Variant
            Ux4gStatusAvatar(size = Ux4gAvatarSize.L, variant = Ux4gStatusAvatarVariant.Error)

            Spacer(modifier = Modifier.width(16.dp))

            // Custom Status Variant
            Ux4gStatusAvatar(
                    size = Ux4gAvatarSize.L,
                    variant = Ux4gStatusAvatarVariant.Custom(Color.Magenta)
            )
        }
    }
}
