package com.ux4g.designsystemv1.components.biometric

import androidx.compose.ui.geometry.Rect

/**
 * All possible states of the biometric verification flow.
 * Exact parity with Flutter `BiometricFlowState`.
 */
enum class BiometricFlowState {
    /** Initial state before anything starts (Identity Consent). */
    Initial,

    /** Running device readiness checks. */
    CheckingDevice,

    /** All device checks passed. */
    DeviceReady,

    /** Requesting camera/permissions. */
    RequestingPermission,

    /** Permission denied by user. */
    PermissionDenied,

    /** Camera initialized and streaming. */
    CameraReady,

    /** Actively looking for a face in frame. */
    DetectingFace,

    /** Face detected, guiding alignment into oval. */
    AligningFace,

    /** Checking ambient lighting conditions. */
    CheckingLighting,

    /** Checking liveness (blink / head movement). */
    CheckingLiveness,

    /** Waiting for user to blink. */
    BlinkRequired,

    /** Blink detected, continuing. */
    BlinkDetected,

    /** Capturing the face image. */
    CapturingImage,

    /** Sending image to verification API. */
    ProcessingVerification,

    /** Verification succeeded. */
    VerificationSuccess,

    /** Verification failed. */
    VerificationFailed,

    /** An unrecoverable error occurred. */
    Error
}

/**
 * Result of a single device check.
 */
enum class DeviceCheckStatus {
    Pending,
    Checking,
    Passed,
    Failed
}

/**
 * Individual device check item.
 */
data class DeviceCheckItem(
    val label: String,
    val description: String,
    val status: DeviceCheckStatus = DeviceCheckStatus.Pending,
    val errorMessage: String? = null,
    val passedText: String = "Sufficient",
    val failedText: String = "Not detected",
    val helperText: String? = null
)

/**
 * Face alignment guidance.
 */
enum class FaceAlignmentStatus {
    NoFace,
    TooFar,
    TooClose,
    OffCenter,
    MultipleFaces,
    Aligned
}

/**
 * Lighting condition status.
 */
enum class LightingStatus {
    Unknown,
    TooLow,
    OverExposed,
    Good
}

/**
 * Liveness check step.
 */
enum class LivenessStep {
    WaitingForBlink,
    BlinkDetected,
    HeadMovement,
    Passed,
    Failed
}

/**
 * Result from the verification API (mock or real).
 */
data class BiometricVerificationResult(
    val success: Boolean,
    val userId: String? = null,
    val maskedName: String? = null,
    val maskedPhone: String? = null,
    val message: String? = null,
    val errorCode: String? = null,
    val confidenceScore: Double? = null
)

/**
 * Failure reasons for verification.
 */
enum class VerificationFailureReason {
    LivenessFailed,
    FaceNotDetected,
    FaceNotMatched,
    VerificationTimeout,
    NetworkError,
    ServerError,
    LowConfidence,
    Unknown
}

/**
 * Comprehensive result from face analysis on a single frame.
 */
data class FaceAnalysisResult(
    val faceCount: Int,
    val alignment: FaceAlignmentStatus,
    val lighting: LightingStatus,
    val livenessStep: LivenessStep,
    val leftEyeOpen: Float? = null,
    val rightEyeOpen: Float? = null,
    val smilingProbability: Float? = null,
    val headEulerAngleY: Float? = null,
    val headEulerAngleZ: Float? = null,
    val blinkCount: Int = 0,
    val boundingBox: Rect? = null
) {
    val isFaceAligned: Boolean get() = alignment == FaceAlignmentStatus.Aligned
    val isLightingGood: Boolean get() = lighting == LightingStatus.Good
    val isLivenessPassed: Boolean get() = livenessStep == LivenessStep.Passed
}
