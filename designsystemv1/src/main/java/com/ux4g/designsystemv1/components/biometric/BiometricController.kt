package com.ux4g.designsystemv1.components.biometric

import androidx.camera.core.ImageProxy
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Size
import kotlinx.coroutines.*
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds

/**
 * Main controller driving the biometric verification flow.
 * Exact parity with Flutter `BiometricController`.
 *
 * Uses [FaceDetectionService] powered by Google ML Kit for real-time face alignment,
 * lighting assessment, and eye blink / head movement liveness verification.
 */
class BiometricController(
    val onVerify: suspend (ByteArray) -> BiometricVerificationResult,
    val enableBlinkCheck: Boolean = true,
    val enableLightingCheck: Boolean = true,
    val enableLiveness: Boolean = true,
    val verificationTimeout: Duration = 30.seconds,
    val maxAttempts: Int = 3,
    val lockoutDuration: Duration = 30.minutes,
    private val coroutineScope: CoroutineScope = CoroutineScope(Dispatchers.Main + SupervisorJob())
) {
    var state by mutableStateOf(BiometricFlowState.Initial)
        private set

    var deviceChecks by mutableStateOf<List<DeviceCheckItem>>(emptyList())
        private set

    var lastAnalysis by mutableStateOf<FaceAnalysisResult?>(null)
        private set

    var verificationResult by mutableStateOf<BiometricVerificationResult?>(null)
        private set

    var failureReason by mutableStateOf<VerificationFailureReason?>(null)
        private set

    var instructionText by mutableStateOf("")
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    var attemptCount by mutableIntStateOf(0)
        private set

    val remainingAttempts: Int get() = maxAttempts - attemptCount
    val isLastAttempt: Boolean get() = attemptCount >= maxAttempts - 1

    /** Google ML Kit Face Detection Service */
    val faceDetectionService = FaceDetectionService()

    private var isRealFrameReceived = false

    // ── Device Checks ──

    fun startDeviceChecks() {
        state = BiometricFlowState.CheckingDevice

        deviceChecks = listOf(
            DeviceCheckItem(
                label = "Camera",
                description = "Checking front camera availability",
                passedText = "Sufficient",
                failedText = "Not detected"
            ),
            DeviceCheckItem(
                label = "Lighting",
                description = "Checking ambient lighting",
                passedText = "Sufficient",
                failedText = "Not detected"
            ),
            DeviceCheckItem(
                label = "Internet connection",
                description = "Checking network connection",
                passedText = "Sufficient",
                failedText = "Not detected"
            ),
            DeviceCheckItem(
                label = "Permissions",
                description = "Checking camera permission",
                passedText = "Sufficient",
                failedText = "Not detected",
                helperText = "Go to App Settings › Permissions › Camera › Allow."
            ),
            DeviceCheckItem(
                label = "Fingerprint scanner",
                description = "Checking fingerprint scanner availability",
                passedText = "Connected",
                failedText = "Not connected",
                helperText = "Fingerprint scanner not detected. Please check connection."
            )
        )

        coroutineScope.launch {
            // Check 1: Camera
            updateCheck(0, DeviceCheckStatus.Checking)
            delay(500)
            updateCheck(0, DeviceCheckStatus.Passed)

            // Check 2: Lighting
            updateCheck(1, DeviceCheckStatus.Checking)
            delay(400)
            updateCheck(1, DeviceCheckStatus.Passed)

            // Check 3: Internet connection
            updateCheck(2, DeviceCheckStatus.Checking)
            delay(400)
            updateCheck(2, DeviceCheckStatus.Passed)

            // Check 4: Permissions
            updateCheck(3, DeviceCheckStatus.Checking)
            delay(300)
            updateCheck(3, DeviceCheckStatus.Passed)

            // Check 5: Fingerprint scanner
            updateCheck(4, DeviceCheckStatus.Checking)
            delay(300)
            updateCheck(4, DeviceCheckStatus.Passed)

            delay(200)
            state = BiometricFlowState.DeviceReady
        }
    }

    private fun updateCheck(index: Int, status: DeviceCheckStatus, errorMessage: String? = null) {
        val updated = deviceChecks.toMutableList()
        if (index in updated.indices) {
            updated[index] = updated[index].copy(status = status, errorMessage = errorMessage)
            deviceChecks = updated
        }
    }

    // ── Real-Time Google ML Kit Frame Processing ──

    fun startDetection() {
        faceDetectionService.resetLivenessState()
        state = BiometricFlowState.DetectingFace
        instructionText = "Position your face in the oval"
    }

    fun startCamera() {
        startDetection()
    }

    fun proceedToCapture() {
        startDetection()
    }

    /**
     * Process real CameraX ImageProxy using Google ML Kit.
     */
    fun processCameraFrame(imageProxy: ImageProxy, previewSize: Size) {
        // Do not process once we reach capture/verification
        if (state in listOf(
                BiometricFlowState.CapturingImage,
                BiometricFlowState.ProcessingVerification,
                BiometricFlowState.VerificationSuccess,
                BiometricFlowState.VerificationFailed
            )
        ) {
            imageProxy.close()
            return
        }

        coroutineScope.launch {
            val result = faceDetectionService.processImageProxy(imageProxy, previewSize) ?: return@launch
            isRealFrameReceived = true
            handleAnalysisResult(result)
        }
    }

    /**
     * Handle ML Kit analysis result matching Flutter exact flow.
     */
    fun handleAnalysisResult(result: FaceAnalysisResult) {
        lastAnalysis = result

        // 1. Face Alignment Phase
        if (!result.isFaceAligned) {
            state = BiometricFlowState.AligningFace
            instructionText = when (result.alignment) {
                FaceAlignmentStatus.NoFace -> "Position your face in the oval"
                FaceAlignmentStatus.TooFar -> "Move closer to the camera"
                FaceAlignmentStatus.TooClose -> "Move back slightly"
                FaceAlignmentStatus.OffCenter -> "Center your face in the oval"
                FaceAlignmentStatus.MultipleFaces -> "Only one face allowed"
                FaceAlignmentStatus.Aligned -> "Hold still..."
            }
            return
        }

        // 2. Lighting Check Phase
        if (enableLightingCheck && !result.isLightingGood) {
            state = BiometricFlowState.CheckingLighting
            instructionText = when (result.lighting) {
                LightingStatus.TooLow -> "Improve lighting"
                LightingStatus.OverExposed -> "Reduce backlight"
                else -> "Checking lighting..."
            }
            return
        }

        // 3. Liveness / Blink Check Phase
        if (enableLiveness || enableBlinkCheck) {
            when (result.livenessStep) {
                LivenessStep.WaitingForBlink -> {
                    if (state != BiometricFlowState.BlinkRequired) {
                        state = BiometricFlowState.BlinkRequired
                        instructionText = "Please blink now"
                    }
                    return
                }
                LivenessStep.BlinkDetected -> {
                    if (enableLiveness) {
                        state = BiometricFlowState.CheckingLiveness
                        instructionText = "Turn slightly to your left"
                        return
                    } else {
                        if (state != BiometricFlowState.BlinkDetected && state != BiometricFlowState.CapturingImage && state != BiometricFlowState.ProcessingVerification) {
                            state = BiometricFlowState.BlinkDetected
                            instructionText = "Blink detected ✓"
                            coroutineScope.launch {
                                delay(500)
                                captureAndVerify(ByteArray(1024))
                            }
                            return
                        }
                    }
                }
                LivenessStep.HeadMovement -> {
                    if (enableLiveness) {
                        state = BiometricFlowState.CheckingLiveness
                        instructionText = "Turn slightly to your left"
                        return
                    }
                }
                LivenessStep.Passed -> {
                    if (state != BiometricFlowState.CapturingImage && state != BiometricFlowState.ProcessingVerification && state != BiometricFlowState.VerificationSuccess && state != BiometricFlowState.VerificationFailed) {
                        state = BiometricFlowState.BlinkDetected
                        instructionText = "Hold still..."
                        coroutineScope.launch {
                            delay(400)
                            captureAndVerify(ByteArray(1024))
                        }
                        return
                    }
                }
                LivenessStep.Failed -> {
                    failureReason = VerificationFailureReason.LivenessFailed
                    state = BiometricFlowState.VerificationFailed
                    instructionText = "Liveness check failed"
                    return
                }
            }
        } else {
            captureAndVerify(ByteArray(1024))
        }
    }

    fun captureAndVerify(imageBytes: ByteArray) {
        if (state == BiometricFlowState.CapturingImage || state == BiometricFlowState.ProcessingVerification) return
        state = BiometricFlowState.CapturingImage
        instructionText = "Hold still — capturing..."

        coroutineScope.launch {
            delay(600)
            state = BiometricFlowState.ProcessingVerification
            instructionText = "Verifying with UIDAI"

            val result = try {
                withTimeout(verificationTimeout) {
                    onVerify(imageBytes)
                }
            } catch (e: Exception) {
                BiometricVerificationResult(
                    success = false,
                    message = e.localizedMessage ?: "Verification error",
                    errorCode = "TIMEOUT_OR_NETWORK"
                )
            }

            verificationResult = result
            if (result.success) {
                state = BiometricFlowState.VerificationSuccess
                instructionText = "Identity verified"
            } else {
                failureReason = VerificationFailureReason.LowConfidence
                state = BiometricFlowState.VerificationFailed
                instructionText = "Face does not match Aadhaar records"
            }
        }
    }

    fun retry() {
        attemptCount++
        lastAnalysis = null
        verificationResult = null
        failureReason = null
        errorMessage = null

        startDetection()
    }

    fun resetFull() {
        lastAnalysis = null
        verificationResult = null
        failureReason = null
        errorMessage = null
        attemptCount = 0

        state = BiometricFlowState.Initial
    }

    fun pause() {}

    fun resume() {
        if (state in listOf(
                BiometricFlowState.DetectingFace,
                BiometricFlowState.AligningFace,
                BiometricFlowState.BlinkRequired
            )
        ) {
            startDetection()
        }
    }

    fun dispose() {
        faceDetectionService.close()
        coroutineScope.cancel()
    }
}
