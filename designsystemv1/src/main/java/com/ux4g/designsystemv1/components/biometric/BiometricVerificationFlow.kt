package com.ux4g.designsystemv1.components.biometric

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds

/**
 * Main entry-point composable for the biometric verification flow.
 * Exact parity with Flutter `BiometricVerificationFlow`.
 *
 * Manages the complete lifecycle: Consent -> Device checks -> Camera -> Face detection ->
 * Liveness -> Verification -> Success/Failure.
 */
private enum class BiometricScreenPhase {
    Consent,
    DeviceCheck,
    Camera
}

@Composable
fun BiometricVerificationFlow(
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
    val controller = remember {
        BiometricController(
            onVerify = onVerify,
            enableBlinkCheck = enableBlinkCheck,
            enableLightingCheck = enableLightingCheck,
            enableLiveness = enableLiveness,
            verificationTimeout = verificationTimeout,
            maxAttempts = maxAttempts,
            lockoutDuration = lockoutDuration
        )
    }

    DisposableEffect(Unit) {
        onDispose {
            controller.dispose()
        }
    }

    LaunchedEffect(controller.state) {
        if (controller.state == BiometricFlowState.VerificationFailed) {
            onFailure?.invoke(
                controller.failureReason ?: VerificationFailureReason.Unknown,
                controller.errorMessage
            )
        }
    }

    val currentScreenPhase = when (controller.state) {
        BiometricFlowState.Initial -> BiometricScreenPhase.Consent
        BiometricFlowState.CheckingDevice -> BiometricScreenPhase.DeviceCheck
        else -> BiometricScreenPhase.Camera
    }

    BiometricThemeProvider(data = theme) {
        Box(modifier = modifier.fillMaxSize()) {
            AnimatedContent(
                targetState = currentScreenPhase,
                transitionSpec = {
                    fadeIn(animationSpec = tween(theme.animationDurationMs)) togetherWith
                            fadeOut(animationSpec = tween(theme.animationDurationMs))
                },
                label = "biometric_screen_transition"
            ) { phase ->
                when (phase) {
                    BiometricScreenPhase.Consent -> {
                        IdentityConsentScreen(
                            onAllow = { controller.startDeviceChecks() },
                            onDecline = onAlternateVerify
                        )
                    }

                    BiometricScreenPhase.DeviceCheck -> {
                        DeviceCheckScreen(
                            controller = controller,
                            onProceed = { controller.startDetection() }
                        )
                    }

                    BiometricScreenPhase.Camera -> {
                        CameraCaptureScreen(
                            controller = controller,
                            onAlternateVerify = onAlternateVerify,
                            onDismiss = onDismiss,
                            onSuccess = onSuccess
                        )
                    }
                }
            }
        }
    }
}
