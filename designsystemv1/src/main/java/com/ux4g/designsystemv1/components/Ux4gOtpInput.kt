package com.ux4g.designsystemv1.components

import android.content.res.Configuration
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.ux4g.designsystemv1.foundation.Ux4gBorderWidth
import com.ux4g.designsystemv1.foundation.Ux4gColors
import com.ux4g.designsystemv1.foundation.Ux4gIcon
import com.ux4g.designsystemv1.foundation.Ux4gIconComponent
import com.ux4g.designsystemv1.foundation.Ux4gRadius
import com.ux4g.designsystemv1.theme.Ux4gTheme
import kotlinx.coroutines.delay

// ─── Enums ────────────────────────────────────────────────────────────────────

/**
 * Status of an individual OTP digit box.
 */
enum class Ux4gOtpBoxStatus {
    Default,
    Focused,
    Error,
    Warning,
    Success,
    Disabled;

    companion object {
        val defaultStatus = Default
        val focused = Focused
        val error = Error
        val warning = Warning
        val success = Success
        val disabled = Disabled
    }
}

/**
 * Overall validation or interaction status of the OTP input component.
 */
enum class Ux4gOtpInputStatus {
    Default,
    Error,
    Warning,
    Success,
    Locked;

    companion object {
        val defaultStatus = Default
        val error = Error
        val warning = Warning
        val success = Success
        val locked = Locked
    }
}

/**
 * Variants for the helper caption section rendered below the OTP boxes.
 */
enum class Ux4gOtpCaptionVariant {
    /** e.g. "Didn't receive OTP? Resend in 00:17" */
    ResendTimer,

    /** e.g. "Didn't receive OTP? [Resend OTP]" */
    ResendAction,

    /** e.g. "🔴 Attempt 2 of 3   Resend OTP in 00:17" */
    AttemptWithTimer,

    /** e.g. "🔒 Locked for 28:43   Resend OTP" */
    Locked,

    /** e.g. "✅ Verification successful" */
    Success,

    /** e.g. "⚠️ Warning message" */
    Warning,

    /** Custom plain caption */
    Plain;

    companion object {
        val resendTimer = ResendTimer
        val resendAction = ResendAction
        val attemptWithTimer = AttemptWithTimer
        val locked = Locked
        val success = Success
        val warning = Warning
        val plain = Plain
    }
}

// ─── Blinking Cursor ─────────────────────────────────────────────────────────

@Composable
private fun Ux4gOtpCursor(
    color: Color,
    modifier: Modifier = Modifier
) {
    val transition = rememberInfiniteTransition(label = "cursor_fade")
    val alpha by transition.animateFloat(
        initialValue = 1f,
        targetValue = 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 600),
            repeatMode = RepeatMode.Reverse
        ),
        label = "cursor_alpha"
    )

    Box(
        modifier = modifier
            .width(1.5.dp)
            .height(20.dp)
            .alpha(alpha)
            .background(color)
    )
}

// ─── Ux4gOtpBox (Single Digit Box) ──────────────────────────────────────────

/**
 * A single OTP digit box — used standalone or internally by [Ux4gOtpInput].
 *
 * @param value Character/digit displayed inside the box.
 * @param status Visual state of the box ([Ux4gOtpBoxStatus]).
 * @param size Box dimensions (width and height). Defaults to 48.dp.
 * @param obscure Obscures the character with a bullet "•" (password mode).
 * @param modifier Optional modifier for the box.
 */
@Composable
fun Ux4gOtpBox(
    value: String = "",
    status: Ux4gOtpBoxStatus = Ux4gOtpBoxStatus.Default,
    size: Dp = 48.dp,
    obscure: Boolean = false,
    modifier: Modifier = Modifier
) {
    val primary = Ux4gTheme.colorScheme.primary
    val onSurface = Ux4gTheme.colorScheme.onSurface
    val error = Ux4gColors.red600
    val warning = Ux4gColors.orange600
    val success = Ux4gColors.green600
    val surface = Ux4gTheme.colorScheme.surface

    val hsStrong = Ux4gTheme.typography.hS_strong
    val bmDefault = Ux4gTheme.typography.bM_default

    val borderColor = when (status) {
        Ux4gOtpBoxStatus.Focused -> primary
        Ux4gOtpBoxStatus.Error -> error
        Ux4gOtpBoxStatus.Warning -> warning
        Ux4gOtpBoxStatus.Success -> success
        Ux4gOtpBoxStatus.Disabled -> onSurface.copy(alpha = 0.2f)
        Ux4gOtpBoxStatus.Default -> onSurface.copy(alpha = 0.25f)
    }

    val bgColor = if (status == Ux4gOtpBoxStatus.Disabled) {
        onSurface.copy(alpha = 0.05f)
    } else {
        surface
    }

    val borderWidth = if (status == Ux4gOtpBoxStatus.Focused) {
        Ux4gBorderWidth.Thick
    } else {
        Ux4gBorderWidth.Thin
    }

    val textColor = if (status == Ux4gOtpBoxStatus.Disabled) {
        onSurface.copy(alpha = 0.35f)
    } else {
        onSurface
    }

    Box(
        modifier = modifier
            .size(size)
            .clip(RoundedCornerShape(Ux4gRadius.Radius8))
            .background(bgColor)
            .border(
                width = borderWidth,
                color = borderColor,
                shape = RoundedCornerShape(Ux4gRadius.Radius8)
            ),
        contentAlignment = Alignment.Center
    ) {
        if (value.isNotEmpty()) {
            Text(
                text = if (obscure) "•" else value,
                style = hsStrong.copy(
                    color = textColor,
                    textAlign = TextAlign.Center
                )
            )
        } else if (status == Ux4gOtpBoxStatus.Focused) {
            Ux4gOtpCursor(color = primary)
        } else {
            Text(
                text = "—",
                style = bmDefault.copy(
                    color = onSurface.copy(alpha = 0.3f),
                    textAlign = TextAlign.Center
                )
            )
        }
    }
}

// ─── Ux4gOtpCaption ──────────────────────────────────────────────────────────

/**
 * Caption section that renders below the OTP field.
 * Wraps all message variants (timer, resend action, attempt counter, locked, success, warning, plain).
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun Ux4gOtpCaption(
    variant: Ux4gOtpCaptionVariant,
    modifier: Modifier = Modifier,
    caption: String? = null,
    leadingText: String? = null,
    trailingText: String? = null,
    onTrailingTap: (() -> Unit)? = null,
    trailingIsAction: Boolean = false
) {
    val onSurface = Ux4gTheme.colorScheme.onSurface
    val primary = Ux4gTheme.colorScheme.primary
    val error = Ux4gColors.red600
    val success = Ux4gColors.green600
    val warning = Ux4gColors.orange600

    val bxsDefault = Ux4gTheme.typography.bXS_default
    val bxsStrong = Ux4gTheme.typography.bXS_strong

    when (variant) {
        Ux4gOtpCaptionVariant.ResendTimer -> {
            FlowRow(
                modifier = modifier,
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = leadingText ?: "Didn't receive OTP?",
                    style = bxsDefault.copy(color = onSurface.copy(alpha = 0.6f))
                )
                if (!trailingText.isNullOrEmpty()) {
                    Text(
                        text = trailingText,
                        style = bxsDefault.copy(color = onSurface.copy(alpha = 0.6f))
                    )
                }
            }
        }

        Ux4gOtpCaptionVariant.ResendAction -> {
            FlowRow(
                modifier = modifier,
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = leadingText ?: "Didn't receive OTP?",
                    style = bxsDefault.copy(color = onSurface.copy(alpha = 0.6f))
                )
                Text(
                    text = trailingText ?: "Resend OTP",
                    style = bxsStrong.copy(color = primary),
                    modifier = Modifier.clickable(
                        enabled = onTrailingTap != null,
                        onClick = { onTrailingTap?.invoke() }
                    )
                )
            }
        }

        Ux4gOtpCaptionVariant.AttemptWithTimer -> {
            Row(
                modifier = modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Error,
                        contentDescription = "Attempt Error",
                        tint = error,
                        modifier = Modifier.size(14.dp)
                    )
                    Text(
                        text = leadingText ?: "",
                        style = bxsStrong.copy(color = error)
                    )
                }
                if (!trailingText.isNullOrEmpty()) {
                    Text(
                        text = trailingText,
                        style = bxsDefault.copy(color = onSurface.copy(alpha = 0.6f))
                    )
                }
            }
        }

        Ux4gOtpCaptionVariant.Locked -> {
            Row(
                modifier = modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Lock,
                        contentDescription = "Locked",
                        tint = onSurface.copy(alpha = 0.6f),
                        modifier = Modifier.size(14.dp)
                    )
                    Text(
                        text = leadingText ?: "",
                        style = bxsDefault.copy(color = onSurface.copy(alpha = 0.6f))
                    )
                }
                Text(
                    text = trailingText ?: "Resend OTP",
                    style = bxsDefault.copy(color = onSurface.copy(alpha = 0.6f)),
                    modifier = Modifier.clickable(
                        enabled = onTrailingTap != null,
                        onClick = { onTrailingTap?.invoke() }
                    )
                )
            }
        }

        Ux4gOtpCaptionVariant.Success -> {
            Row(
                modifier = modifier,
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.CheckCircle,
                    contentDescription = "Success",
                    tint = success,
                    modifier = Modifier.size(14.dp)
                )
                Text(
                    text = caption ?: "Verification successful",
                    style = bxsStrong.copy(color = success)
                )
            }
        }

        Ux4gOtpCaptionVariant.Warning -> {
            Row(
                modifier = modifier,
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.Warning,
                    contentDescription = "Warning",
                    tint = warning,
                    modifier = Modifier.size(14.dp)
                )
                Text(
                    text = caption ?: "Warning message",
                    style = bxsDefault.copy(color = warning)
                )
            }
        }

        Ux4gOtpCaptionVariant.Plain -> {
            Text(
                text = caption ?: "",
                style = bxsDefault.copy(color = onSurface.copy(alpha = 0.6f)),
                modifier = modifier
            )
        }
    }
}

// ─── Ux4gOtpInput (Complete Component) ──────────────────────────────────────

private const val ZERO_WIDTH_SPACE = "\u200B"

/**
 * A comprehensive OTP Input component matching UX4G Flutter specifications 1:1.
 *
 * Features:
 * - Per-box editing: tap anywhere in the middle to edit or remove specific digits.
 * - Backspace auto-step-back: deleting a digit immediately focuses the previous box.
 * - 100% exact colors matching Flutter (Green success, Red error, Orange warning, Primary focus).
 * - Password obscuring support ("•").
 * - Status border colors: Default, Focused, Error, Warning, Success, Disabled/Locked.
 * - Label with optional required asterisk and trailing icon.
 * - Auto-countdown timer (mm:ss) with automatic transition to "Resend OTP" action.
 * - Comprehensive caption variants (resend timer, resend link, attempt with timer, locked, success, warning, plain).
 * - Full paste & SMS autofill support.
 *
 * @param value Current OTP text.
 * @param onValueChange Callback invoked when the OTP text changes.
 * @param modifier Modifier applied to the outer container.
 * @param length Number of digits/boxes (defaults to 6).
 * @param onCompleted Callback invoked when all digit boxes are filled.
 * @param label Label rendered above the boxes.
 * @param required Whether to display a red required asterisk '*' next to the label.
 * @param labelTrailingIcon Optional icon placed next to the label.
 * @param status Component status driving border colors and caption styling ([Ux4gOtpInputStatus]).
 * @param obscure Obscures digits with bullets (password mode).
 * @param boxSize Dimension of each digit box (width & height). Defaults to 48.dp.
 * @param gap Spacing between adjacent boxes. Defaults to 8.dp.
 * @param showSeparator Whether to show a separator dash between digit groups (e.g. in 6-digit OTP).
 * @param keyboardType Keyboard type (defaults to [KeyboardType.Number]).
 * @param captionVariant Variant for the bottom caption ([Ux4gOtpCaptionVariant]).
 * @param captionLeadingText Leading label for caption (e.g. "Didn't receive OTP?" or "Attempt 2 of 3").
 * @param captionTrailingText Trailing label for caption (e.g. "Resend in" or "Resend OTP").
 * @param onCaptionTrailingTap Callback when trailing caption action is tapped.
 * @param captionText Caption message for plain, success, or warning variants.
 * @param autoCountdownSeconds If specified, runs a 1-second countdown and formats remaining time as mm:ss.
 * @param onCountdownComplete Callback invoked when the countdown finishes.
 * @param enabled Whether the input is enabled.
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun Ux4gOtpInput(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    length: Int = 6,
    onCompleted: ((String) -> Unit)? = null,
    label: String? = null,
    required: Boolean = false,
    labelTrailingIcon: Any? = null,
    status: Ux4gOtpInputStatus = Ux4gOtpInputStatus.Default,
    obscure: Boolean = false,
    boxSize: Dp = 48.dp,
    gap: Dp = 8.dp,
    showSeparator: Boolean = false,
    keyboardType: KeyboardType = KeyboardType.Number,
    captionVariant: Ux4gOtpCaptionVariant? = null,
    captionLeadingText: String? = null,
    captionTrailingText: String? = null,
    onCaptionTrailingTap: (() -> Unit)? = null,
    captionText: String? = null,
    autoCountdownSeconds: Int? = null,
    onCountdownComplete: (() -> Unit)? = null,
    enabled: Boolean = true
) {
    val onSurface = Ux4gTheme.colorScheme.onSurface
    val errorColor = Ux4gColors.red600

    val bsStrong = Ux4gTheme.typography.bS_strong
    val bmDefault = Ux4gTheme.typography.bM_default

    val focusManager = LocalFocusManager.current
    val focusRequesters = remember(length) { List(length) { FocusRequester() } }
    var focusedIndex by remember { mutableIntStateOf(-1) }
    var justClearedIndex by remember { mutableIntStateOf(-1) }

    // ── Internal per-box digits state matching Flutter's _isInternalUpdate ──
    // Flutter uses `_isInternalUpdate` flag to prevent re-syncing controllers
    // when the value change originated from user interaction inside the component.
    // Without this, removing a middle digit (e.g. box 3 in "123456") would:
    //   digits = ["1","2","","4","5","6"] → joinToString = "12456"
    //   → parent sets value = "12456" → splitValue("12456") = ["1","2","4","5","6",""]
    //   → digits left-shift! ❌
    // With lastReportedValue, we skip re-sync when value matches what we just sent.

    fun splitValue(v: String): List<String> {
        val list = MutableList(length) { "" }
        for (i in 0 until minOf(v.length, length)) {
            list[i] = v[i].toString()
        }
        return list
    }

    var digits by remember(length) {
        mutableStateOf(splitValue(value))
    }

    // Track what we last reported to the parent to distinguish internal vs external changes
    var lastReportedValue by remember { mutableStateOf(value) }

    // Only sync from parent when the value change is genuinely external
    // (e.g. parent programmatically set a new value, clear, or paste from outside)
    LaunchedEffect(value, length) {
        if (value != lastReportedValue) {
            digits = splitValue(value)
            lastReportedValue = value
        }
    }

    // ── Countdown Timer Logic ────────────────────────────────────────────────
    var remainingSeconds by remember(autoCountdownSeconds) {
        mutableIntStateOf(autoCountdownSeconds ?: 0)
    }
    var countdownExpired by remember(autoCountdownSeconds) {
        mutableStateOf(autoCountdownSeconds == null || autoCountdownSeconds <= 0)
    }

    LaunchedEffect(autoCountdownSeconds) {
        if (autoCountdownSeconds != null && autoCountdownSeconds > 0) {
            remainingSeconds = autoCountdownSeconds
            countdownExpired = false
            while (remainingSeconds > 0) {
                delay(1000L)
                remainingSeconds -= 1
            }
            countdownExpired = true
            onCountdownComplete?.invoke()
        }
    }

    fun formattedRemaining(): String {
        val m = remainingSeconds / 60
        val s = remainingSeconds % 60
        return String.format("%02d:%02d", m, s)
    }

    // ── Resolve Effective Caption ────────────────────────────────────────────
    var effectiveVariant = captionVariant
    var effectiveTrailing = captionTrailingText

    if (autoCountdownSeconds != null) {
        if (countdownExpired) {
            effectiveVariant = Ux4gOtpCaptionVariant.ResendAction
            effectiveTrailing = captionTrailingText ?: "Resend OTP"
        } else {
            val prefix = captionTrailingText ?: "Resend in"
            effectiveTrailing = "$prefix ${formattedRemaining()}"
        }
    }

    // ── Determine Status per Box ─────────────────────────────────────────────
    fun resolveBoxStatus(index: Int): Ux4gOtpBoxStatus {
        if (!enabled || status == Ux4gOtpInputStatus.Locked) return Ux4gOtpBoxStatus.Disabled
        if (focusedIndex == index) return Ux4gOtpBoxStatus.Focused
        return when (status) {
            Ux4gOtpInputStatus.Error -> Ux4gOtpBoxStatus.Error
            Ux4gOtpInputStatus.Warning -> Ux4gOtpBoxStatus.Warning
            Ux4gOtpInputStatus.Success -> Ux4gOtpBoxStatus.Success
            Ux4gOtpInputStatus.Locked -> Ux4gOtpBoxStatus.Disabled
            Ux4gOtpInputStatus.Default -> Ux4gOtpBoxStatus.Default
        }
    }

    // ── Per-Box Interaction Handlers (1:1 with Flutter) ──────────────────────
    fun handleInput(index: Int, text: String) {
        if (!enabled || status == Ux4gOtpInputStatus.Locked) return

        // Clean out zero-width space anchor
        val rawInput = text.replace(ZERO_WIDTH_SPACE, "")

        if (rawInput.length > 2) {
            // Multi-character Paste (3+ chars, e.g. from SMS autofill or Clipboard)
            // When rawInput.length == 2, it's a single-digit override on a filled box
            // (old "3" + new "5" = "35"), NOT a paste — handled below via last char.
            val rawDigits = if (keyboardType == KeyboardType.Number) {
                rawInput.filter { it.isDigit() }
            } else {
                rawInput
            }
            val newDigits = MutableList(length) { "" }
            for (i in 0 until length) {
                if (i < rawDigits.length) {
                    newDigits[i] = rawDigits[i].toString()
                }
            }
            digits = newDigits
            val full = newDigits.joinToString("")
            lastReportedValue = full
            onValueChange(full)
            if (full.length == length) {
                focusManager.clearFocus()
                onCompleted?.invoke(full)
            } else {
                val nextEmpty = newDigits.indexOfFirst { it.isEmpty() }
                if (nextEmpty != -1) {
                    focusRequesters[nextEmpty].requestFocus()
                }
            }
            return
        }

        if (rawInput.isEmpty()) {
            // ── Backspace Logic (matching Flutter's _onDigitChanged + _onKeyEvent) ──
            // Flutter behavior:
            //   1st backspace on filled box → clear digit, stay focused (DON'T move)
            //   2nd backspace on just-cleared box → move to previous (DON'T clear previous)
            //   Backspace on already-empty box → move to previous AND clear previous
            val hadContent = digits[index].isNotEmpty()

            if (hadContent) {
                // Step 1: Box has content → clear it, stay focused here
                val newDigits = digits.toMutableList()
                newDigits[index] = ""
                digits = newDigits
                justClearedIndex = index
                val joinedVal = newDigits.joinToString("")
                lastReportedValue = joinedVal
                onValueChange(joinedVal)
                // DON'T move focus — stay on this box (Flutter: _onDigitChanged sets
                // _justClearedIndex and does NOT call requestFocus)
            } else if (justClearedIndex == index) {
                // Step 2: Box was JUST cleared by us → only move focus, don't clear previous
                // (Flutter: _onKeyEvent branch `if (_justClearedIndex == index)`)
                justClearedIndex = -1
                if (index > 0) {
                    focusRequesters[index - 1].requestFocus()
                }
            } else if (index > 0) {
                // Step 3: Box was already empty (not just cleared) → move AND clear previous
                // (Flutter: _onKeyEvent branch `if (_controllers[index].text.isEmpty)`)
                val newDigits = digits.toMutableList()
                newDigits[index - 1] = ""
                digits = newDigits
                justClearedIndex = index - 1
                val joinedVal = newDigits.joinToString("")
                lastReportedValue = joinedVal
                onValueChange(joinedVal)
                focusRequesters[index - 1].requestFocus()
            }
            return
        }

        // Single digit typed / replaced
        val lastChar = rawInput.last().toString()
        val sanitized = if (keyboardType == KeyboardType.Number) {
            if (lastChar.first().isDigit()) lastChar else ""
        } else {
            lastChar
        }

        // Reset justClearedIndex on digit entry (Flutter: _justClearedIndex = -1)
        justClearedIndex = -1

        val newDigits = digits.toMutableList()
        newDigits[index] = sanitized
        digits = newDigits

        val full = newDigits.joinToString("")
        lastReportedValue = full
        onValueChange(full)

        if (sanitized.isNotEmpty()) {
            if (index < length - 1) {
                focusRequesters[index + 1].requestFocus()
            } else if (index == length - 1) {
                focusManager.clearFocus()
                if (full.length == length) {
                    onCompleted?.invoke(full)
                }
            }
        }
    }

    fun handleHardwareKey(index: Int, keyEvent: androidx.compose.ui.input.key.KeyEvent): Boolean {
        if (!enabled || status == Ux4gOtpInputStatus.Locked) return false
        if (keyEvent.type == KeyEventType.KeyDown && keyEvent.key == Key.Backspace) {
            // Hardware keyboard backspace — same 3-step logic as handleInput
            val hadContent = digits[index].isNotEmpty()

            if (hadContent) {
                val newDigits = digits.toMutableList()
                newDigits[index] = ""
                digits = newDigits
                justClearedIndex = index
                val joinedVal = newDigits.joinToString("")
                lastReportedValue = joinedVal
                onValueChange(joinedVal)
            } else if (justClearedIndex == index) {
                justClearedIndex = -1
                if (index > 0) {
                    focusRequesters[index - 1].requestFocus()
                }
            } else if (index > 0) {
                val newDigits = digits.toMutableList()
                newDigits[index - 1] = ""
                digits = newDigits
                justClearedIndex = index - 1
                val joinedVal = newDigits.joinToString("")
                lastReportedValue = joinedVal
                onValueChange(joinedVal)
                focusRequesters[index - 1].requestFocus()
            }
            return true
        }
        return false
    }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(0.dp)
    ) {
        // ── Label Section ────────────────────────────────────────────────────
        if (label != null) {
            FlowRow(
                modifier = Modifier.padding(bottom = 10.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = label,
                    style = bsStrong.copy(
                        color = if (enabled) onSurface else onSurface.copy(alpha = 0.4f)
                    )
                )
                if (required) {
                    Text(
                        text = "*",
                        style = bsStrong.copy(color = errorColor)
                    )
                }
                if (labelTrailingIcon != null) {
                    when (labelTrailingIcon) {
                        is ImageVector -> {
                            Icon(
                                imageVector = labelTrailingIcon,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp),
                                tint = onSurface.copy(alpha = 0.5f)
                            )
                        }
                        is Ux4gIcon -> {
                            Ux4gIconComponent(
                                icon = labelTrailingIcon,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp),
                                tint = onSurface.copy(alpha = 0.5f)
                            )
                        }
                        else -> {}
                    }
                }
            }
        }

        // ── Per-Box Interactive OTP Digit Boxes ──────────────────────────────
        Row(
            horizontalArrangement = Arrangement.spacedBy(gap),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val halfLength = length / 2
            for (i in 0 until length) {
                if (showSeparator && length > 3 && i == halfLength) {
                    Text(
                        text = "–",
                        style = bmDefault.copy(
                            color = onSurface.copy(alpha = 0.5f),
                            fontWeight = FontWeight.Bold
                        ),
                        modifier = Modifier.padding(horizontal = 2.dp)
                    )
                }

                val char = digits.getOrNull(i) ?: ""
                val boxStatus = resolveBoxStatus(i)

                Box(
                    modifier = Modifier.size(boxSize),
                    contentAlignment = Alignment.Center
                ) {
                    // Visual OTP Box (Render exact UI, Border & Colors)
                    Ux4gOtpBox(
                        value = char,
                        status = boxStatus,
                        size = boxSize,
                        obscure = obscure
                    )

                    // Overlay Interactive Transparent BasicTextField with Backspace Anchor
                    if (enabled && status != Ux4gOtpInputStatus.Locked) {
                        val textWithAnchor = ZERO_WIDTH_SPACE + char
                        BasicTextField(
                            value = TextFieldValue(
                                text = textWithAnchor,
                                selection = TextRange(textWithAnchor.length)
                            ),
                            onValueChange = { tfv ->
                                handleInput(i, tfv.text)
                            },
                            modifier = Modifier
                                .matchParentSize()
                                .alpha(0f)
                                .focusRequester(focusRequesters[i])
                                .onFocusChanged { focusState ->
                                    if (focusState.isFocused) {
                                        focusedIndex = i
                                    } else if (focusedIndex == i) {
                                        focusedIndex = -1
                                    }
                                }
                                .onKeyEvent { keyEvent ->
                                    handleHardwareKey(i, keyEvent)
                                },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(
                                keyboardType = keyboardType,
                                imeAction = if (i == length - 1) ImeAction.Done else ImeAction.Next
                            ),
                            keyboardActions = KeyboardActions(
                                onNext = {
                                    if (i < length - 1) focusRequesters[i + 1].requestFocus()
                                },
                                onDone = {
                                    focusManager.clearFocus()
                                }
                            ),
                            cursorBrush = SolidColor(Color.Transparent)
                        )
                    }
                }
            }
        }

        // ── Caption Section ──────────────────────────────────────────────────
        if (effectiveVariant != null) {
            Spacer(modifier = Modifier.height(8.dp))
            Ux4gOtpCaption(
                variant = effectiveVariant,
                caption = captionText,
                leadingText = captionLeadingText,
                trailingText = effectiveTrailing,
                onTrailingTap = onCaptionTrailingTap
            )
        }
    }
}

// ─── Previews ────────────────────────────────────────────────────────────────

@Preview(name = "Light Mode", showBackground = true)
@Preview(name = "Dark Mode", uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
private fun Ux4gOtpInputPreview() {
    Ux4gTheme {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            var otp1 by remember { mutableStateOf("123") }
            Ux4gOtpInput(
                value = otp1,
                onValueChange = { otp1 = it },
                label = "Enter OTP",
                required = true,
                labelTrailingIcon = Icons.Outlined.Info,
                captionVariant = Ux4gOtpCaptionVariant.ResendTimer,
                autoCountdownSeconds = 30
            )

            var otp2 by remember { mutableStateOf("123456") }
            Ux4gOtpInput(
                value = otp2,
                onValueChange = { otp2 = it },
                label = "Success Verification",
                status = Ux4gOtpInputStatus.Success,
                captionVariant = Ux4gOtpCaptionVariant.Success,
                captionText = "Verification successful"
            )

            var otp3 by remember { mutableStateOf("987") }
            Ux4gOtpInput(
                value = otp3,
                onValueChange = { otp3 = it },
                label = "Error with Attempts",
                status = Ux4gOtpInputStatus.Error,
                captionVariant = Ux4gOtpCaptionVariant.AttemptWithTimer,
                captionLeadingText = "Attempt 2 of 3",
                captionTrailingText = "Resend in 00:45"
            )

            var otp4 by remember { mutableStateOf("") }
            Ux4gOtpInput(
                value = otp4,
                onValueChange = { otp4 = it },
                label = "Locked State",
                status = Ux4gOtpInputStatus.Locked,
                captionVariant = Ux4gOtpCaptionVariant.Locked,
                captionLeadingText = "Locked for 28:43",
                captionTrailingText = "Resend OTP"
            )
        }
    }
}
