package com.ux4g.designsystemv1.components.avatar_component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.isSystemInDarkTheme
import com.ux4g.designsystemv1.foundation.Ux4gColors
import com.ux4g.designsystemv1.foundation.Ux4gIcon
import com.ux4g.designsystemv1.foundation.Ux4gIconComponent
import com.ux4g.designsystemv1.foundation.Ux4gIcons
import com.ux4g.designsystemv1.theme.Ux4gTheme

/** Sizes available for Ux4gAvatar. */
enum class Ux4gAvatarSize(
        val size: Dp,
        val textStyle: @Composable () -> TextStyle,
        val iconSize: Dp
) {
        XS(
                size = 24.dp,
                textStyle = {
                        Ux4gTheme.typography.lS_default.copy(
                                fontWeight = FontWeight.Medium,
                                fontSize = 10.sp
                        )
                },
                iconSize = 14.dp
        ),
        S(
                size = 32.dp,
                textStyle = {
                        Ux4gTheme.typography.bM_default.copy(
                                fontWeight = FontWeight.Medium,
                                fontSize = 14.sp
                        )
                },
                iconSize = 16.dp
        ),
        M(
                size = 40.dp,
                textStyle = {
                        Ux4gTheme.typography.bL_default.copy(
                                fontWeight = FontWeight.Medium,
                                fontSize = 16.sp
                        )
                },
                iconSize = 20.dp
        ),
        L(
                size = 48.dp,
                textStyle = {
                        Ux4gTheme.typography.hM_default.copy(
                                fontWeight = FontWeight.Medium,
                                fontSize = 20.sp
                        )
                },
                iconSize = 24.dp
        ),
        XL(
                size = 64.dp,
                textStyle = {
                        Ux4gTheme.typography.hL_default.copy(
                                fontWeight = FontWeight.Medium,
                                fontSize = 24.sp
                        )
                },
                iconSize = 32.dp
        ),
        XXL(
                size = 80.dp,
                textStyle = {
                        Ux4gTheme.typography.hL_strong.copy(
                                fontWeight = FontWeight.Medium,
                                fontSize = 32.sp
                        )
                },
                iconSize = 40.dp
        ),
        XXXL(
                size = 120.dp,
                textStyle = {
                        Ux4gTheme.typography.hL_strong.copy(
                                fontWeight = FontWeight.Medium,
                                fontSize = 48.sp
                        )
                },
                iconSize = 60.dp
        )
}

/**
 * A highly customizable Avatar component for the Ux4g Design System.
 *
 * @param modifier The modifier to be applied to the avatar.
 * @param size The size of the avatar (XS, S, M, L, XL, XXL, XXXL).
 * @param shape The shape of the avatar's background (default is CircleShape).
 * @param imageUrl Allows overriding the image. Can be a URL, resource ID, or file.
 * @param initials The initials to display (max 2 characters). Fallbacks to this if no image
 * provided.
 * @param icon The fallback icon to show when no image or initials are set.
 * @param containerColor Background color for the avatar.
 * @param contentColor Color applied to initials text.
 * @param iconColor Color applied to the icon.
 */
@Composable
fun Ux4gAvatar(
        modifier: Modifier = Modifier,
        size: Ux4gAvatarSize = Ux4gAvatarSize.M,
        shape: Shape = CircleShape,
        imageUrl: Any? = null,
        initials: String? = null,
        icon: Ux4gIcon? = Ux4gIcons.Person,
        containerColor: Color? = null,
        contentColor: Color? = null,
        iconColor: Color? = null
) {
        val isDark = androidx.compose.foundation.isSystemInDarkTheme()
        val resolvedPrimary = Ux4gTheme.colorScheme.primary
        val finalContainerColor =
                containerColor
                        ?: if (isDark) Ux4gColors.primary900
                        else resolvedPrimary.copy(alpha = 0.1f)
        val finalContentColor = contentColor ?: resolvedPrimary
        val finalIconColor = iconColor ?: resolvedPrimary

        Box(
                modifier = modifier.size(size.size).clip(shape).background(finalContainerColor),
                contentAlignment = Alignment.Center
        ) {
                if (imageUrl != null) {
                        coil.compose.SubcomposeAsyncImage(
                                model = imageUrl,
                                contentDescription = "Avatar Image",
                                modifier = Modifier.size(size.size),
                                contentScale = ContentScale.Crop,
                                loading = {
                                        Box(
                                                contentAlignment = Alignment.Center,
                                                modifier = Modifier.fillMaxSize()
                                        ) {
                                                Ux4gIconComponent(
                                                        icon = Ux4gIcons.Person,
                                                        contentDescription = "Avatar Loading Icon",
                                                        modifier = Modifier.size(size.iconSize),
                                                        tint = finalIconColor
                                                )
                                        }
                                },
                                error = {
                                        Box(
                                                contentAlignment = Alignment.Center,
                                                modifier = Modifier.fillMaxSize()
                                        ) {
                                                if (!initials.isNullOrBlank()) {
                                                        Text(
                                                                text = initials.take(2).uppercase(),
                                                                style = size.textStyle(),
                                                                color = finalContentColor
                                                        )
                                                } else if (icon != null) {
                                                        Ux4gIconComponent(
                                                                icon = icon,
                                                                contentDescription = "Avatar Icon",
                                                                modifier =
                                                                        Modifier.size(
                                                                                size.iconSize
                                                                        ),
                                                                tint = finalIconColor
                                                        )
                                                } else {
                                                        Ux4gIconComponent(
                                                                icon = Ux4gIcons.Person,
                                                                contentDescription =
                                                                        "Avatar Default Icon",
                                                                modifier =
                                                                        Modifier.size(
                                                                                size.iconSize
                                                                        ),
                                                                tint = finalIconColor
                                                        )
                                                }
                                        }
                                }
                        )
                } else if (!initials.isNullOrBlank()) {
                        Text(
                                text = initials.take(2).uppercase(),
                                style = size.textStyle(),
                                color = finalContentColor
                        )
                } else if (icon != null) {
                        Ux4gIconComponent(
                                icon = icon,
                                contentDescription = "Avatar Icon",
                                modifier = Modifier.size(size.iconSize),
                                tint = finalIconColor
                        )
                } else {
                        // Fallback completely
                        Ux4gIconComponent(
                                icon = Ux4gIcons.Person,
                                contentDescription = "Avatar Default Icon",
                                modifier = Modifier.size(size.iconSize),
                                tint = finalIconColor
                        )
                }
        }
}

@Preview(showBackground = true)
@Composable
private fun Ux4gAvatarPreview() {
        Ux4gTheme {
                Row(modifier = Modifier.padding(16.dp)) {
                        // Initials Avatar
                        Ux4gAvatar(initials = "MS", size = Ux4gAvatarSize.M)
                        Spacer(modifier = Modifier.width(16.dp))

                        // Icon Avatar
                        Ux4gAvatar(size = Ux4gAvatarSize.M)
                }
        }
}
