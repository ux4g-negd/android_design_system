package com.ux4g.designsystemv1.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ux4g.designsystemv1.R
import com.ux4g.designsystemv1.theme.Ux4gTheme

/** Badge Limit options for text badges */
enum class Ux4gBadgeLimit {
        SingleDigit, // 9+
        DoubleDigit // 99+
}

/** Predefined badge resources for the READY_TO_USE type */
object Ux4gBadgeResource {
        val Verification = R.drawable.ic_verification_badge
        val Star = R.drawable.ic_star_badge
        val Shield = R.drawable.ic_shield_badge
}

/** Badge Type options */
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
 * @param type The type of badge (Dot, Text, Icon, or ReadyToUse).
 * @param modifier Modifier to be applied to the badge.
 * @param containerColor The background color of the badge (Ignored for ReadyToUse).
 * @param contentColor The tint color (For Text/Icon). For ReadyToUse, it will tint the image if
 * provided.
 */
@Composable
fun Ux4gBadge(
        type: Ux4gBadgeType,
        modifier: Modifier = Modifier,
        containerColor: Color = Ux4gTheme.colorScheme.primary,
        contentColor: Color = Color.Unspecified
) {
        if (type is Ux4gBadgeType.ReadyToUse) {
                androidx.compose.foundation.Image(
                        painter = painterResource(id = type.resId),
                        contentDescription = null,
                        modifier = modifier.size(24.dp), // Default size for ready-to-use badges
                        colorFilter =
                                if (contentColor != Color.Unspecified)
                                        androidx.compose.ui.graphics.ColorFilter.tint(contentColor)
                                else null
                )
                return
        }

        // Resolve the actual color to use for content (Standard Badges)
        val resolvedContentColor =
                if (contentColor != Color.Unspecified) {
                        contentColor
                } else {
                        when (type) {
                                is Ux4gBadgeType.Text, is Ux4gBadgeType.Label -> Color.White
                                else -> Color.Unspecified
                        }
                }

        Box(
                modifier =
                        modifier.background(containerColor, CircleShape)
                                .then(
                                        when (type) {
                                                is Ux4gBadgeType.Dot -> Modifier.size(8.dp)
                                                is Ux4gBadgeType.Text ->
                                                        Modifier.sizeIn(
                                                                        minWidth = 18.dp,
                                                                        minHeight = 18.dp
                                                                )
                                                                .padding(
                                                                        horizontal = 4.dp,
                                                                        vertical = 2.dp
                                                                )
                                                is Ux4gBadgeType.Label ->
                                                        Modifier.sizeIn(minHeight = 18.dp)
                                                                .padding(horizontal = 6.dp, vertical = 2.dp)
                                                is Ux4gBadgeType.Icon -> Modifier.size(18.dp)
                                                is Ux4gBadgeType.ReadyToUse ->
                                                        Modifier // Handled early
                                        }
                                ),
                contentAlignment = Alignment.Center
        ) {
                when (type) {
                        is Ux4gBadgeType.Dot -> {
                                /* No content */
                        }
                        is Ux4gBadgeType.Text -> {
                                val badgeText =
                                        when (type.limit) {
                                                Ux4gBadgeLimit.SingleDigit ->
                                                        if (type.count > 9) "9+"
                                                        else type.count.toString()
                                                Ux4gBadgeLimit.DoubleDigit ->
                                                        if (type.count > 99) "99+"
                                                        else type.count.toString()
                                        }
                                Text(
                                        text = badgeText,
                                        color = resolvedContentColor,
                                        style =
                                                Ux4gTheme.typography.lS_strong.copy(
                                                        fontSize = 10.sp,
                                                        lineHeight = 10.sp
                                                )
                                )
                        }
                        is Ux4gBadgeType.Label -> {
                                Text(
                                        text = type.text,
                                        color = resolvedContentColor,
                                        style =
                                                Ux4gTheme.typography.lS_strong.copy(
                                                        fontSize = 10.sp,
                                                        lineHeight = 10.sp
                                                )
                                )
                        }
                        is Ux4gBadgeType.Icon -> {
                                Icon(
                                        painter = painterResource(id = type.iconRes),
                                        contentDescription = null,
                                        tint = resolvedContentColor,
                                        modifier = Modifier.size(12.dp)
                                )
                        }
                        is Ux4gBadgeType.ReadyToUse -> {
                                // Handled early
                        }
                }
        }
}

@Preview(showBackground = true)
@Composable
private fun Ux4gBadgePreview() {
        Ux4gTheme {
                Row(
                        modifier = Modifier.padding(24.dp),
                        horizontalArrangement = Arrangement.spacedBy(48.dp)
                ) {
                        // Badge Digit Section
                        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                                Text("Badge digit", style = Ux4gTheme.typography.hM_strong)
                                Box(
                                        modifier =
                                                Modifier.background(
                                                                Color(0xFF6B6B6B),
                                                                shape =
                                                                        androidx.compose.foundation
                                                                                .shape
                                                                                .RoundedCornerShape(
                                                                                        8.dp
                                                                                )
                                                        )
                                                        .padding(16.dp)
                                ) {
                                        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                                                Ux4gBadge(
                                                        type = Ux4gBadgeType.Text(3),
                                                        containerColor = Color.Transparent,
                                                        contentColor = Color.White
                                                )
                                                Ux4gBadge(
                                                        type = Ux4gBadgeType.Text(32),
                                                        containerColor = Color.Transparent,
                                                        contentColor = Color.White
                                                )
                                                Ux4gBadge(
                                                        type =
                                                                Ux4gBadgeType.Text(
                                                                        150,
                                                                        Ux4gBadgeLimit.DoubleDigit
                                                                ),
                                                        containerColor = Color.Transparent,
                                                        contentColor = Color.White
                                                )
                                        }
                                }
                        }

                        // Badges Section
                        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                                Text("Badges", style = Ux4gTheme.typography.hM_strong)
                                Box(modifier = Modifier.background(Color.White).padding(16.dp)) {
                                        Column(verticalArrangement = Arrangement.spacedBy(20.dp)) {
                                                // Dot row
                                                Row(
                                                        horizontalArrangement =
                                                                Arrangement.spacedBy(24.dp)
                                                ) {
                                                        Ux4gBadge(
                                                                type = Ux4gBadgeType.Dot,
                                                                containerColor = Color(0xFF4A3AFF)
                                                        )
                                                        Ux4gBadge(
                                                                type = Ux4gBadgeType.Dot,
                                                                containerColor = Color(0xFF27AE60)
                                                        )
                                                        Ux4gBadge(
                                                                type = Ux4gBadgeType.Dot,
                                                                containerColor = Color(0xFFF2994A)
                                                        )
                                                        Ux4gBadge(
                                                                type = Ux4gBadgeType.Dot,
                                                                containerColor = Color(0xFFEB5757)
                                                        )
                                                        Ux4gBadge(
                                                                type = Ux4gBadgeType.Dot,
                                                                containerColor = Color(0xFFBDBDBD)
                                                        )
                                                        Ux4gBadge(
                                                                type = Ux4gBadgeType.Dot,
                                                                containerColor = Color(0xFF2D9CDB)
                                                        )
                                                }

                                                // Tick row
                                                Row(
                                                        horizontalArrangement =
                                                                Arrangement.spacedBy(24.dp)
                                                ) {
                                                        Ux4gBadge(
                                                                type =
                                                                        Ux4gBadgeType.Icon(
                                                                                R.drawable.ic_tick
                                                                        ),
                                                                containerColor = Color(0xFF4A3AFF)
                                                        )
                                                        Ux4gBadge(
                                                                type =
                                                                        Ux4gBadgeType.Icon(
                                                                                R.drawable.ic_tick
                                                                        ),
                                                                containerColor = Color(0xFF27AE60)
                                                        )
                                                        Ux4gBadge(
                                                                type =
                                                                        Ux4gBadgeType.Icon(
                                                                                R.drawable.ic_tick
                                                                        ),
                                                                containerColor = Color(0xFFF2994A)
                                                        )
                                                        Ux4gBadge(
                                                                type =
                                                                        Ux4gBadgeType.Icon(
                                                                                R.drawable.ic_tick
                                                                        ),
                                                                containerColor = Color(0xFFEB5757)
                                                        )
                                                        Ux4gBadge(
                                                                type =
                                                                        Ux4gBadgeType.Icon(
                                                                                R.drawable.ic_tick
                                                                        ),
                                                                containerColor = Color(0xFFBDBDBD)
                                                        )
                                                        Ux4gBadge(
                                                                type =
                                                                        Ux4gBadgeType.Icon(
                                                                                R.drawable.ic_tick
                                                                        ),
                                                                containerColor = Color(0xFF2D9CDB)
                                                        )
                                                }

                                                // Number row
                                                Row(
                                                        horizontalArrangement =
                                                                Arrangement.spacedBy(24.dp)
                                                ) {
                                                        Ux4gBadge(
                                                                type = Ux4gBadgeType.Text(3),
                                                                containerColor = Color(0xFF4A3AFF)
                                                        )
                                                        Ux4gBadge(
                                                                type = Ux4gBadgeType.Text(10),
                                                                containerColor = Color(0xFF27AE60)
                                                        )
                                                        Ux4gBadge(
                                                                type =
                                                                        Ux4gBadgeType.Text(
                                                                                100,
                                                                                Ux4gBadgeLimit
                                                                                        .DoubleDigit
                                                                        ),
                                                                containerColor = Color(0xFFF2994A)
                                                        )
                                                }

                                                // Misc row (Special Badges)
                                                Row(
                                                        horizontalArrangement =
                                                                Arrangement.spacedBy(24.dp),
                                                        verticalAlignment =
                                                                Alignment.CenterVertically
                                                ) {
                                                        // 1. Using OUR predefined resource
                                                        Ux4gBadge(
                                                                type =
                                                                        Ux4gBadgeType.ReadyToUse(
                                                                                Ux4gBadgeResource
                                                                                .Verification
                                                                        )
                                                        )

                                                        // 2. Using THEIR own custom resource
                                                        Ux4gBadge(
                                                                type =
                                                                        Ux4gBadgeType.ReadyToUse(
                                                                                R.drawable
                                                                                        .ic_star_badge
                                                                        )
                                                        )

                                                        // 3. Custom Shield with color
                                                        Ux4gBadge(
                                                                type =
                                                                        Ux4gBadgeType.ReadyToUse(
                                                                                Ux4gBadgeResource
                                                                                        .Shield
                                                                        ),
                                                        )
                                                }
                                        }
                                }
                        }
                }
        }
}
