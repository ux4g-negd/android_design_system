package com.ux4g.designsystemv1.components.biometric

import kotlinx.coroutines.delay

/**
 * Mock verification service for testing only.
 * Exact parity with Flutter `MockVerificationService`.
 */
class MockVerificationService(
    val delayMs: Long = 3000L,
    val simulateSuccess: Boolean = true
) {
    /**
     * Simulate a verification API call.
     */
    suspend fun verify(imageBytes: ByteArray): BiometricVerificationResult {
        delay(delayMs)

        return if (simulateSuccess) {
            BiometricVerificationResult(
                success = true,
                userId = "USR-2024-001",
                maskedName = "Mo***ar",
                maskedPhone = "****1234",
                message = "Identity verified successfully",
                confidenceScore = 0.97
            )
        } else {
            BiometricVerificationResult(
                success = false,
                message = "Face verification failed",
                errorCode = "VERIFICATION_FAILED",
                confidenceScore = 0.32
            )
        }
    }
}
