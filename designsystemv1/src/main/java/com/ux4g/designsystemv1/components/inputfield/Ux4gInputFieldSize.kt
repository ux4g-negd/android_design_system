package com.ux4g.designsystemv1.components.inputfield

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Defines the size variants for the Ux4gInputField component.
 * @param height The exact height of the input field container.
 */
enum class Ux4gInputFieldSize(val height: Dp) {
    Small(32.dp),
    Medium(40.dp),
    Large(48.dp),
    XL(56.dp);

    companion object {
        val small = Small
        val medium = Medium
        val large = Large
        val xl = XL
    }
}
