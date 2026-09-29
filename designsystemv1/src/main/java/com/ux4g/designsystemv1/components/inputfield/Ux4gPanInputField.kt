package com.ux4g.designsystemv1.components.inputfield

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

/**
 * A specialized input field for Indian Permanent Account Number (PAN) cards.
 *
 * Features:
 * - Auto-uppercase conversion & alphanumeric filtering (e.g. `ABCDE1234F`).
 * - Real-time regex validation (`^[A-Z]{5}[0-9]{4}[A-Z]{1}$`).
 * - Dynamic status updates (switches to [Ux4gInputFieldStatus.Success] or [Ux4gInputFieldStatus.Error] on 10 characters).
 * - Standardized UX4G tokens, icons, and sizing variants.
 *
 * Matches Flutter's `pan_input_field.dart` 1:1.
 */
@Composable
fun Ux4gPanInputField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    size: Ux4gInputFieldSize = Ux4gInputFieldSize.Medium,
    status: Ux4gInputFieldStatus = Ux4gInputFieldStatus.Default,
    label: String? = "PAN Card Number",
    required: Boolean = false,
    placeholder: String? = "ABCDE1234F",
    caption: String? = "Enter your 10-character PAN number",
    leadingIcon: Any? = null,
    trailingIcon: Any? = null,
    onTrailingIconPressed: (() -> Unit)? = null,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    placeholderStyle: TextStyle? = null,
    style: TextStyle? = null,
    labelStyle: TextStyle? = null,
    captionStyle: TextStyle? = null
) {
    val cleanUpper = value.uppercase().filter { it.isLetterOrDigit() }.take(10)

    var currentStatus = status
    var currentCaption = caption

    if (cleanUpper.length == 10) {
        if (status == Ux4gInputFieldStatus.Default) {
            if (Ux4gPanInputField.validatePan(cleanUpper)) {
                currentStatus = Ux4gInputFieldStatus.Success
                currentCaption = "Valid PAN number"
            } else {
                currentStatus = Ux4gInputFieldStatus.Error
                currentCaption = "Please enter a valid PAN number"
            }
        }
    }

    Ux4gInputField(
        value = value,
        onValueChange = { input ->
            val formatted = Ux4gPanInputField.formatPan(input)
            onValueChange(formatted)
        },
        modifier = modifier,
        size = size,
        type = Ux4gInputFieldType.Text,
        status = currentStatus,
        label = label,
        required = required,
        placeholder = placeholder,
        caption = currentCaption,
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        onTrailingIconPressed = onTrailingIconPressed,
        enabled = enabled,
        readOnly = readOnly,
        maxLength = null, // Formatted and length-capped by formatPan (10 alphanumeric chars)
        keyboardOptions = KeyboardOptions(
            capitalization = KeyboardCapitalization.Characters,
            keyboardType = KeyboardType.Text
        ),
        style = style,
        labelStyle = labelStyle,
        placeholderStyle = placeholderStyle,
        captionStyle = captionStyle
    )
}

/**
 * Companion utilities for PAN validation and formatting.
 */
object Ux4gPanInputField {

    private val PAN_REGEX = Regex("^[A-Z]{5}[0-9]{4}[A-Z]{1}$")

    /**
     * Formats an input string into uppercase alphanumeric characters up to 10 characters.
     */
    fun formatPan(input: String): String {
        return input.uppercase().filter { it.isLetterOrDigit() }.take(10)
    }

    /**
     * Validates a PAN card number according to Indian Income Tax format:
     * 5 Letters (A-Z) + 4 Digits (0-9) + 1 Letter (A-Z).
     * Example: `ABCDE1234F`
     */
    fun validatePan(pan: String): Boolean {
        val clean = pan.uppercase().trim()
        return PAN_REGEX.matches(clean)
    }
}

// ═══════════════════════════════════════════════════════
// Previews
// ═══════════════════════════════════════════════════════

@Preview(name = "PAN Field - Light", showBackground = true, backgroundColor = 0xFFFFFFFF)
@Preview(name = "PAN Field - Dark", uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true, backgroundColor = 0xFF121212)
@Composable
private fun Ux4gPanInputFieldPreview() {
    var pan1 by remember { mutableStateOf("") }
    var pan2 by remember { mutableStateOf("ABCDE1234F") } // Valid PAN
    var pan3 by remember { mutableStateOf("12345ABCDE") } // Invalid PAN

    Column(
        modifier = Modifier.padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Ux4gPanInputField(
            value = pan1,
            onValueChange = { pan1 = it },
            label = "Empty PAN Field",
            leadingIcon = Icons.Default.CreditCard
        )

        Ux4gPanInputField(
            value = pan2,
            onValueChange = { pan2 = it },
            label = "Valid PAN (Auto Success)",
            leadingIcon = Icons.Default.CreditCard,
            trailingIcon = Icons.Default.Close,
            onTrailingIconPressed = { pan2 = "" }
        )

        Ux4gPanInputField(
            value = pan3,
            onValueChange = { pan3 = it },
            label = "Invalid PAN (Auto Error)",
            leadingIcon = Icons.Default.CreditCard,
            trailingIcon = Icons.Default.Close,
            onTrailingIconPressed = { pan3 = "" }
        )
    }
}
