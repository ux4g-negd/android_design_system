package com.ux4g.designsystemv1.components.avatar_component

import androidx.annotation.DrawableRes
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.ux4g.designsystemv1.R
import com.ux4g.designsystemv1.foundation.Ux4gIcon
import com.ux4g.designsystemv1.foundation.Ux4gIconComponent
import com.ux4g.designsystemv1.foundation.Ux4gIcons
import com.ux4g.designsystemv1.theme.Ux4gTheme

/** Variants for Ux4gProfileAvatar indicator. */
sealed class Ux4gProfileAvatarVariant {
    sealed class Badge(@DrawableRes val iconRes: Int) : Ux4gProfileAvatarVariant() {
        data object Verified : Badge(R.drawable.ic_verification_badge)
        data object Star : Badge(R.drawable.ic_star_badge)
        data object Admin : Badge(R.drawable.ic_shield_badge)
        class Custom(@DrawableRes iconRes: Int) : Badge(iconRes)
    }

    sealed class Action(
            val icon: Ux4gIcon,
            val containerColor: Color? = null,
            val iconColor: Color = Color.White
    ) : Ux4gProfileAvatarVariant() {
        data object Edit : Action(Ux4gIcons.edit_badge)
        data object Camera : Action(Ux4gIcons.PhotoCamera)
        data object Remove : Action(Ux4gIcons.Remove)
        class Custom(icon: Ux4gIcon, containerColor: Color, iconColor: Color = Color.White) :
                Action(icon, containerColor, iconColor)
    }
}

/** Extension to get the proper size for the badge/action indicator based on Avatar size. */
val Ux4gAvatarSize.indicatorSize: Dp
    get() =
            when (this) {
                Ux4gAvatarSize.XS -> 12.dp
                Ux4gAvatarSize.S -> 14.dp
                Ux4gAvatarSize.M -> 16.dp
                Ux4gAvatarSize.L -> 18.dp
                Ux4gAvatarSize.XL -> 24.dp
                Ux4gAvatarSize.XXL -> 30.dp
                Ux4gAvatarSize.XXXL -> 42.dp
            }

/**
 * A Profile Avatar component built on top of [Ux4gAvatar] that adds support for Badge or Action
 * indicators positioned at the bottom right.
 *
 * @param modifier The modifier to be applied to the avatar.
 * @param size The size of the avatar (XS, S, M, L, XL, XXL, XXXL).
 * @param imageUrl Allows overriding the image. Can be a URL, resource ID, or file.
 * @param initials The initials to display (max 2 characters).
 * @param avatarIcon The fallback icon to show when no image or initials are set.
 * @param variant The Badge or Action to overlay on the avatar.
 * @param badgeSize Optional custom size for the badge/action indicator to override the default.
 * @param onVariantClick Optional callback triggered when the variant indicator is clicked.
 */
@Composable
fun Ux4gProfileAvatar(
        modifier: Modifier = Modifier,
        size: Ux4gAvatarSize = Ux4gAvatarSize.M,
        imageUrl: Any? = null,
        initials: String? = null,
        avatarIcon: Ux4gIcon? = Ux4gIcons.Person,
        variant: Ux4gProfileAvatarVariant? = null,
        badgeSize: Dp? = null,
        onVariantClick: (() -> Unit)? = null
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

        // Overlay Indicator (Badge or Action)
        if (variant != null) {
            val indicatorSize = badgeSize ?: size.indicatorSize

            Box(
                    modifier =
                            Modifier.align(Alignment.BottomEnd).size(indicatorSize).let { m ->
                                onVariantClick?.let { onClick ->
                                    m.clip(CircleShape).clickable(onClick = onClick)
                                }
                                        ?: m
                            }
            ) {
                when (variant) {
                    is Ux4gProfileAvatarVariant.Badge -> {
                        Ux4gIconComponent(
                                icon = Ux4gIcon.Drawable(variant.iconRes),
                                contentDescription = "Profile Badge",
                                modifier = Modifier.matchParentSize(),
                                tint = Color.Unspecified
                        )
                    }
                    is Ux4gProfileAvatarVariant.Action -> {
                        // All newly provided Action icons (Camera, Edit, Remove) are Drawables
                        // containing their own colors and borders.
                        Ux4gIconComponent(
                                icon = variant.icon,
                                contentDescription = "Profile Action",
                                modifier = Modifier.matchParentSize(),
                                tint =
                                        if (variant.icon is Ux4gIcon.Drawable) Color.Unspecified
                                        else variant.iconColor
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun Ux4gProfileAvatarPreview() {
    Ux4gTheme {
        Row(modifier = Modifier.padding(16.dp)) {
            // Edit Action Variant
            Ux4gProfileAvatar(
                    initials = "MS",
                    size = Ux4gAvatarSize.L,
                    variant = Ux4gProfileAvatarVariant.Action.Remove
            )
            Spacer(modifier = Modifier.width(16.dp))

            // Verified Badge Variant
            Ux4gProfileAvatar(
                    size = Ux4gAvatarSize.L,
                    variant = Ux4gProfileAvatarVariant.Badge.Verified
            )

            Spacer(modifier = Modifier.width(16.dp))

            Ux4gProfileAvatar(
                    size = Ux4gAvatarSize.L,
                    variant = Ux4gProfileAvatarVariant.Action.Edit,
                    imageUrl =
                            "https://miro.medium.com/v2/resize:fill:72:72/1*NfVk3LQfhRaDFW8qZ4MhCQ.jpeg"
            )
        }
    }
}
