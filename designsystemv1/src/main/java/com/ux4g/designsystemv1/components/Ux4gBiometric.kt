package com.ux4g.designsystemv1.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.ux4g.designsystemv1.components.biometric.*
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds

/**
 * UX4G Biometric Verification Flow component.
 * Exact parity with Flutter `Ux4gBiometricVerificationFlow` / `BiometricVerificationFlow`.
 */
@Composable
fun Ux4gBiometricVerificationFlow(
    onVerify: suspend (ByteArray) -> BiometricVerificationResult,
    modifier: Modifier = Modifier,
    onSuccess: ((BiometricVerificationResult) -> Unit)? = null,
    onFailure: ((VerificationFailureReason, String?) -> Unit)? = null,
    onDismiss: (() -> Unit)? = null,
    onAlternateVerify: (() -> Unit)? = null,
    enableBlinkCheck: Boolean = true,
    enableLightingCheck: Boolean = true,
    enableLiveness: Boolean = true,
    theme: BiometricThemeData = BiometricThemeData(),
    maxAttempts: Int = 3,
    lockoutDuration: Duration = 30.minutes,
    verificationTimeout: Duration = 30.seconds
) {
    BiometricVerificationFlow(
        onVerify = onVerify,
        modifier = modifier,
        onSuccess = onSuccess,
        onFailure = onFailure,
        onDismiss = onDismiss,
        onAlternateVerify = onAlternateVerify,
        enableBlinkCheck = enableBlinkCheck,
        enableLightingCheck = enableLightingCheck,
        enableLiveness = enableLiveness,
        theme = theme,
        maxAttempts = maxAttempts,
        lockoutDuration = lockoutDuration,
        verificationTimeout = verificationTimeout
    )
}

/**
 * UX4G Identity Consent Screen component.
 */
@Composable
fun Ux4gIdentityConsentScreen(
    onAllow: () -> Unit,
    modifier: Modifier = Modifier,
    onDecline: (() -> Unit)? = null
) {
    IdentityConsentScreen(
        onAllow = onAllow,
        modifier = modifier,
        onDecline = onDecline
    )
}

/**
 * UX4G Device Check Screen component.
 */
@Composable
fun Ux4gDeviceCheckScreen(
    controller: BiometricController,
    onProceed: () -> Unit,
    modifier: Modifier = Modifier,
    compact: Boolean = false
) {
    DeviceCheckScreen(
        controller = controller,
        onProceed = onProceed,
        modifier = modifier,
        compact = compact
    )
}
