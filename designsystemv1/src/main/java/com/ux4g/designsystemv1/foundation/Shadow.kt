package com.ux4g.designsystemv1.foundation

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/** Platform-neutral shadow values matching the Flutter BoxShadow tokens. */
data class Ux4gBoxShadow(
        val color: Color,
        val offsetX: Dp,
        val offsetY: Dp,
        val blurRadius: Dp,
        val spreadRadius: Dp = 0.dp
)

object Ux4gShadow {
    val shadow0: List<Ux4gBoxShadow> = emptyList()
    val flat: List<Ux4gBoxShadow> = shadow0

    val shadow1 = listOf(
            Ux4gBoxShadow(Color(0x0A000000), 0.dp, 1.dp, 2.dp),
            Ux4gBoxShadow(Color(0x0A000000), 0.dp, 1.dp, 2.dp)
    )
    val subtle: List<Ux4gBoxShadow> = shadow1

    val shadow2 = listOf(
            Ux4gBoxShadow(Color(0x0D000000), 0.dp, 1.dp, 2.dp),
            Ux4gBoxShadow(Color(0x14000000), 0.dp, 4.dp, 8.dp)
    )
    val floating: List<Ux4gBoxShadow> = shadow2

    val shadow3 = listOf(
            Ux4gBoxShadow(Color(0x14000000), 0.dp, 4.dp, 8.dp),
            Ux4gBoxShadow(Color(0x1A000000), 0.dp, 0.dp, 16.dp)
    )
    val prominent: List<Ux4gBoxShadow> = shadow3

    val shadow4 = listOf(
            Ux4gBoxShadow(Color(0x1F000000), 0.dp, 8.dp, 16.dp),
            Ux4gBoxShadow(Color(0x26000000), 0.dp, 16.dp, 32.dp)
    )
    val highest: List<Ux4gBoxShadow> = shadow4

    fun get(level: Int, isDark: Boolean = false): List<Ux4gBoxShadow> {
        if (!isDark) {
            return when (level) {
                1 -> shadow1
                2 -> shadow2
                3 -> shadow3
                4 -> shadow4
                else -> shadow0
            }
        }

        return when (level) {
            1 -> listOf(
                    Ux4gBoxShadow(Color(0x33000000), 0.dp, 1.dp, 2.dp),
                    Ux4gBoxShadow(Color(0x26000000), 0.dp, 1.dp, 2.dp)
            )
            2 -> listOf(
                    Ux4gBoxShadow(Color(0x3D000000), 0.dp, 1.dp, 2.dp),
                    Ux4gBoxShadow(Color(0x4D000000), 0.dp, 4.dp, 8.dp)
            )
            3 -> listOf(
                    Ux4gBoxShadow(Color(0x4D000000), 0.dp, 4.dp, 8.dp),
                    Ux4gBoxShadow(Color(0x66000000), 0.dp, 0.dp, 16.dp)
            )
            4 -> listOf(
                    Ux4gBoxShadow(Color(0x66000000), 0.dp, 8.dp, 16.dp),
                    Ux4gBoxShadow(Color(0x80000000), 0.dp, 16.dp, 32.dp)
            )
            else -> shadow0
        }
    }
}
