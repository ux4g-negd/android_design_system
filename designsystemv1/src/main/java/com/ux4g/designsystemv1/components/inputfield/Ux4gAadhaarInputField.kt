package com.ux4g.designsystemv1.components.inputfield

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

/**
 * Visual transformation that masks Aadhaar number digits with a mask character ('X' or '•').
 * Spaces and formatting separators are preserved as-is.
 */
class AadhaarMaskVisualTransformation(
    private val maskChar: Char = 'X'
) : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val transformed = buildString {
            for (char in text.text) {
                if (char.isDigit()) {
                    append(maskChar)
                } else {
                    append(char)
                }
            }
        }
        return TransformedText(
            text = AnnotatedString(transformed),
            offsetMapping = OffsetMapping.Identity
        )
    }
}

/**
 * A specialized input field for Indian Aadhaar numbers (12 digits).
 *
 * Features:
 * - Auto-formatting: formats raw digits as `XXXX XXXX XXXX` (space every 4 digits).
 * - Numeric-only input filter.
 * - Masking toggle: show/hide Aadhaar digits with Material Outlined Eye icons (`Visibility` / `VisibilityOff`).
 * - Built-in validation using the official **Verhoeff algorithm**.
 * - Dynamic status updates (switches to [Ux4gInputFieldStatus.Success] or [Ux4gInputFieldStatus.Error] on complete entry).
 *
 * Matches Flutter's `aadhaar_input_field.dart` 1:1.
 */
@Composable
fun Ux4gAadhaarInputField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    size: Ux4gInputFieldSize = Ux4gInputFieldSize.Medium,
    status: Ux4gInputFieldStatus = Ux4gInputFieldStatus.Default,
    label: String? = "Aadhaar Number",
    required: Boolean = false,
    placeholder: String? = "XXXX XXXX XXXX",
    caption: String? = "Enter your 12-digit Aadhaar number",
    leadingIcon: Any? = null,
    trailingIcon: Any? = null,
    onTrailingIconPressed: (() -> Unit)? = null,
    showMaskToggle: Boolean = false,
    isMasked: Boolean? = null,
    onMaskChange: ((Boolean) -> Unit)? = null,
    maskChar: Char = 'X',
    enabled: Boolean = true,
    readOnly: Boolean = false,
    placeholderStyle: TextStyle? = null,
    style: TextStyle? = null,
    labelStyle: TextStyle? = null,
    captionStyle: TextStyle? = null
) {
    val cleanValue = value.filter { it.isDigit() }

    var currentStatus = status
    var currentCaption = caption

    if (cleanValue.length == 12) {
        if (status == Ux4gInputFieldStatus.Default) {
            if (Ux4gAadhaarInputField.validateAadhaar(cleanValue)) {
                currentStatus = Ux4gInputFieldStatus.Success
                currentCaption = "Valid Aadhaar number"
            } else {
                currentStatus = Ux4gInputFieldStatus.Error
                currentCaption = "Please enter a valid Aadhaar number"
            }
        }
    }

    var internalMasked by remember { mutableStateOf(true) }
    val effectiveMasked = isMasked ?: internalMasked

    val effectiveVisualTransformation = if (showMaskToggle && effectiveMasked) {
        AadhaarMaskVisualTransformation(maskChar = maskChar)
    } else {
        VisualTransformation.None
    }

    val effectiveTrailingIcon = if (trailingIcon != null) {
        trailingIcon
    } else if (showMaskToggle) {
        if (effectiveMasked) Icons.Outlined.VisibilityOff else Icons.Outlined.Visibility
    } else {
        null
    }

    val effectiveOnTrailingIconPressed: (() -> Unit)? = if (onTrailingIconPressed != null) {
        onTrailingIconPressed
    } else if (showMaskToggle && trailingIcon == null) {
        {
            val newMaskState = !effectiveMasked
            internalMasked = newMaskState
            onMaskChange?.invoke(newMaskState)
            Unit
        }
    } else {
        null
    }

    Ux4gInputField(
        value = value,
        onValueChange = { input ->
            val formatted = Ux4gAadhaarInputField.formatAadhaar(input)
            onValueChange(formatted)
        },
        modifier = modifier,
        size = size,
        type = Ux4gInputFieldType.Number,
        status = currentStatus,
        label = label,
        required = required,
        placeholder = placeholder,
        caption = currentCaption,
        leadingIcon = leadingIcon,
        trailingIcon = effectiveTrailingIcon,
        onTrailingIconPressed = effectiveOnTrailingIconPressed,
        enabled = enabled,
        readOnly = readOnly,
        maxLength = null, // Formatted and length-capped by formatAadhaar (14 chars: 12 digits + 2 spaces)
        visualTransformation = effectiveVisualTransformation,
        style = style,
        labelStyle = labelStyle,
        placeholderStyle = placeholderStyle,
        captionStyle = captionStyle
    )
}

/**
 * Companion utilities for Aadhaar validation and formatting.
 */
object Ux4gAadhaarInputField {

    /**
     * Formats an input string into `XXXX XXXX XXXX` structure.
     */
    fun formatAadhaar(input: String): String {
        val digits = input.filter { it.isDigit() }.take(12)
        val sb = StringBuilder()
        for (i in digits.indices) {
            sb.append(digits[i])
            if ((i + 1) % 4 == 0 && (i + 1) != digits.length && (i + 1) < 12) {
                sb.append(' ')
            }
        }
        return sb.toString()
    }

    /**
     * Validates a 12-digit Aadhaar number using the Verhoeff algorithm.
     *
     * Rules:
     * 1. Must be exactly 12 digits.
     * 2. Cannot start with '0' or '1'.
     * 3. Must satisfy the Verhoeff checksum.
     */
    fun validateAadhaar(aadhaar: String): Boolean {
        val clean = aadhaar.filter { it.isDigit() }
        if (clean.length != 12) return false
        if (clean.startsWith('0') || clean.startsWith('1')) return false
        return VerhoeffAlgorithm.validate(clean)
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Verhoeff Algorithm Implementation
// ─────────────────────────────────────────────────────────────────────────────

private object VerhoeffAlgorithm {
    private val d = arrayOf(
        intArrayOf(0, 1, 2, 3, 4, 5, 6, 7, 8, 9),
        intArrayOf(1, 2, 3, 4, 0, 6, 7, 8, 9, 5),
        intArrayOf(2, 3, 4, 0, 1, 7, 8, 9, 5, 6),
        intArrayOf(3, 4, 0, 1, 2, 8, 9, 5, 6, 7),
        intArrayOf(4, 0, 1, 2, 3, 9, 5, 6, 7, 8),
        intArrayOf(5, 9, 8, 7, 6, 0, 4, 3, 2, 1),
        intArrayOf(6, 5, 9, 8, 7, 1, 0, 4, 3, 2),
        intArrayOf(7, 6, 5, 9, 8, 2, 1, 0, 4, 3),
        intArrayOf(8, 7, 6, 5, 9, 3, 2, 1, 0, 4),
        intArrayOf(9, 8, 7, 6, 5, 4, 3, 2, 1, 0)
    )

    private val p = arrayOf(
        intArrayOf(0, 1, 2, 3, 4, 5, 6, 7, 8, 9),
        intArrayOf(1, 5, 7, 6, 2, 8, 3, 0, 9, 4),
        intArrayOf(5, 8, 0, 3, 7, 9, 6, 1, 4, 2),
        intArrayOf(8, 9, 1, 6, 0, 4, 3, 5, 2, 7),
        intArrayOf(9, 4, 5, 3, 1, 2, 6, 8, 7, 0),
        intArrayOf(4, 2, 8, 6, 5, 7, 3, 9, 0, 1),
        intArrayOf(2, 7, 9, 3, 8, 0, 6, 4, 1, 5),
        intArrayOf(7, 0, 4, 6, 9, 1, 3, 2, 5, 8)
    )

    fun validate(num: String): Boolean {
        var c = 0
        val reversedArray = num.reversed().map { it.digitToInt() }

        for (i in reversedArray.indices) {
            c = d[c][p[i % 8][reversedArray[i]]]
        }

        return c == 0
    }
}

// ═══════════════════════════════════════════════════════
// Previews
// ═══════════════════════════════════════════════════════

@Preview(name = "Aadhaar Field - Light", showBackground = true, backgroundColor = 0xFFFFFFFF)
@Preview(name = "Aadhaar Field - Dark", uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true, backgroundColor = 0xFF121212)
@Composable
private fun Ux4gAadhaarInputFieldPreview() {
    var aadhaar1 by remember { mutableStateOf("") }
    var aadhaar2 by remember { mutableStateOf("9999 4105 7058") } // Valid Verhoeff checksum Aadhaar
    var aadhaar3 by remember { mutableStateOf("2345 6789 0123") } // Invalid checksum

    Column(
        modifier = Modifier.padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Ux4gAadhaarInputField(
            value = aadhaar1,
            onValueChange = { aadhaar1 = it },
            label = "Empty Aadhaar Field",
            leadingIcon = Icons.Default.Badge
        )

        Ux4gAadhaarInputField(
            value = aadhaar2,
            onValueChange = { aadhaar2 = it },
            label = "Valid Aadhaar Number (Auto Success)",
            leadingIcon = Icons.Default.Badge,
            trailingIcon = Icons.Default.Close,
            onTrailingIconPressed = { aadhaar2 = "" }
        )

        var aadhaar4 by remember { mutableStateOf("9999 4105 7058") }
        Ux4gAadhaarInputField(
            value = aadhaar4,
            onValueChange = { aadhaar4 = it },
            label = "Label",
            caption = "Description",
            showMaskToggle = true
        )
    }
}
