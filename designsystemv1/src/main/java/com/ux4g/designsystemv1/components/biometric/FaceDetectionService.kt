package com.ux4g.designsystemv1.components.biometric

import android.graphics.Bitmap
import androidx.annotation.OptIn
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageProxy
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import com.google.android.gms.tasks.Task
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.Face
import com.google.mlkit.vision.face.FaceDetection
import com.google.mlkit.vision.face.FaceDetector
import com.google.mlkit.vision.face.FaceDetectorOptions
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.math.abs

/**
 * Service for Google ML Kit face detection, alignment, lighting, and liveness analysis.
 * Exact parity with Flutter `FaceDetectionService`.
 */
class FaceDetectionService {
    private val detector: FaceDetector

    private var isBusy = false
    private var isDisposed = false
    private var lastProcessedTime = 0L

    companion object {
        private const val THROTTLE_MS = 200L
        private const val EYE_CLOSED_THRESHOLD = 0.3f
        private const val HEAD_MOVEMENT_THRESHOLD = 8.0f
    }

    // Blink detection state
    private var leftEyeWasClosed = false
    private var rightEyeWasClosed = false
    var blinkCount = 0
        private set

    // Head movement tracking
    private var previousHeadY: Float? = null
    private var headMovementCount = 0

    init {
        val options = FaceDetectorOptions.Builder()
            .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_FAST)
            .setClassificationMode(FaceDetectorOptions.CLASSIFICATION_MODE_ALL)
            .setLandmarkMode(FaceDetectorOptions.LANDMARK_MODE_ALL)
            .setContourMode(FaceDetectorOptions.CONTOUR_MODE_NONE)
            .setMinFaceSize(0.25f)
            .enableTracking()
            .build()

        detector = FaceDetection.getClient(options)
    }

    /**
     * Reset blink and liveness state.
     */
    fun resetLivenessState() {
        blinkCount = 0
        leftEyeWasClosed = false
        rightEyeWasClosed = false
        previousHeadY = null
        headMovementCount = 0
    }

    /**
     * Process an ImageProxy from CameraX analyzer.
     */
    @OptIn(ExperimentalGetImage::class)
    suspend fun processImageProxy(imageProxy: ImageProxy, previewSize: Size): FaceAnalysisResult? {
        if (isDisposed || isBusy) {
            imageProxy.close()
            return null
        }

        val now = System.currentTimeMillis()
        if (now - lastProcessedTime < THROTTLE_MS) {
            imageProxy.close()
            return null
        }

        val mediaImage = imageProxy.image
        if (mediaImage == null) {
            imageProxy.close()
            return null
        }

        isBusy = true
        lastProcessedTime = now

        return try {
            val rotationDegrees = imageProxy.imageInfo.rotationDegrees
            val isRotated = rotationDegrees == 90 || rotationDegrees == 270
            val frameWidth = if (isRotated) imageProxy.height.toFloat() else imageProxy.width.toFloat()
            val frameHeight = if (isRotated) imageProxy.width.toFloat() else imageProxy.height.toFloat()

            val inputImage = InputImage.fromMediaImage(mediaImage, rotationDegrees)
            val faces = detector.process(inputImage).awaitTask()
            analyze(faces, Size(frameWidth, frameHeight))
        } catch (e: Exception) {
            null
        } finally {
            isBusy = false
            imageProxy.close()
        }
    }

    /**
     * Process a Bitmap frame.
     */
    suspend fun processBitmap(bitmap: Bitmap, rotationDegrees: Int, previewSize: Size): FaceAnalysisResult? {
        if (isDisposed || isBusy) return null

        val now = System.currentTimeMillis()
        if (now - lastProcessedTime < THROTTLE_MS) return null

        isBusy = true
        lastProcessedTime = now

        return try {
            val inputImage = InputImage.fromBitmap(bitmap, rotationDegrees)
            val faces = detector.process(inputImage).awaitTask()
            analyze(faces, previewSize)
        } catch (e: Exception) {
            null
        } finally {
            isBusy = false
        }
    }

    /**
     * Analyze detected faces and return comprehensive result.
     */
    fun analyze(faces: List<Face>, previewSize: Size): FaceAnalysisResult {
        if (faces.isEmpty()) {
            return FaceAnalysisResult(
                faceCount = 0,
                alignment = FaceAlignmentStatus.NoFace,
                lighting = LightingStatus.Unknown,
                livenessStep = LivenessStep.WaitingForBlink
            )
        }

        if (faces.size > 1) {
            return FaceAnalysisResult(
                faceCount = faces.size,
                alignment = FaceAlignmentStatus.MultipleFaces,
                lighting = LightingStatus.Unknown,
                livenessStep = LivenessStep.WaitingForBlink
            )
        }

        val face = faces.first()
        val bbox = face.boundingBox

        val composeRect = Rect(
            left = bbox.left.toFloat(),
            top = bbox.top.toFloat(),
            right = bbox.right.toFloat(),
            bottom = bbox.bottom.toFloat()
        )

        // 1. Alignment
        val alignment = checkAlignment(composeRect, previewSize)

        // 2. Lighting
        val lighting = checkLighting(face)

        // 3. Blink Detection
        detectBlink(face)

        // 4. Head Movement
        detectHeadMovement(face)

        // 5. Liveness
        val livenessStep = evaluateLiveness()

        return FaceAnalysisResult(
            faceCount = 1,
            alignment = alignment,
            lighting = lighting,
            livenessStep = livenessStep,
            leftEyeOpen = face.leftEyeOpenProbability,
            rightEyeOpen = face.rightEyeOpenProbability,
            smilingProbability = face.smilingProbability,
            headEulerAngleY = face.headEulerAngleY,
            headEulerAngleZ = face.headEulerAngleZ,
            blinkCount = blinkCount,
            boundingBox = composeRect
        )
    }

    private fun checkAlignment(bbox: Rect, preview: Size): FaceAlignmentStatus {
        if (preview.width <= 0 || preview.height <= 0) return FaceAlignmentStatus.Aligned

        val centerX = preview.width / 2f
        val centerY = preview.height / 2f
        val faceCenterX = bbox.center.x
        val faceCenterY = bbox.center.y

        val faceArea = bbox.width * bbox.height
        val previewArea = preview.width * preview.height
        val faceRatio = faceArea / previewArea

        // Too far: face is less than 8% of frame
        if (faceRatio < 0.08f) return FaceAlignmentStatus.TooFar

        // Too close: face is more than 45% of frame
        if (faceRatio > 0.45f) return FaceAlignmentStatus.TooClose

        // Off center: face center is more than 20% away from preview center
        val dx = abs(faceCenterX - centerX) / preview.width
        val dy = abs(faceCenterY - centerY) / preview.height
        if (dx > 0.20f || dy > 0.20f) return FaceAlignmentStatus.OffCenter

        return FaceAlignmentStatus.Aligned
    }

    private fun checkLighting(face: Face): LightingStatus {
        val leftEye = face.leftEyeOpenProbability
        val rightEye = face.rightEyeOpenProbability
        if (leftEye == null || rightEye == null) return LightingStatus.Unknown

        return LightingStatus.Good
    }

    private fun detectBlink(face: Face) {
        val leftEye = face.leftEyeOpenProbability
        val rightEye = face.rightEyeOpenProbability
        if (leftEye == null || rightEye == null) return

        val leftClosed = leftEye < EYE_CLOSED_THRESHOLD
        val rightClosed = rightEye < EYE_CLOSED_THRESHOLD

        if (leftClosed && rightClosed) {
            leftEyeWasClosed = true
            rightEyeWasClosed = true
        }

        if (leftEyeWasClosed && rightEyeWasClosed && !leftClosed && !rightClosed) {
            blinkCount++
            leftEyeWasClosed = false
            rightEyeWasClosed = false
        }
    }

    private fun detectHeadMovement(face: Face) {
        val headY = face.headEulerAngleY ?: return
        val prevY = previousHeadY
        if (prevY != null) {
            val diff = abs(headY - prevY)
            // Trigger head turn when either angle difference is significant or absolute yaw angle is turned left/right
            if (diff >= HEAD_MOVEMENT_THRESHOLD || abs(headY) >= 10.0f) {
                headMovementCount++
            }
        }
        previousHeadY = headY
    }

    private fun evaluateLiveness(): LivenessStep {
        if (blinkCount >= 1 && headMovementCount >= 1) {
            return LivenessStep.Passed
        }
        if (blinkCount >= 1) {
            return LivenessStep.HeadMovement
        }
        return LivenessStep.WaitingForBlink
    }

    fun close() {
        if (isDisposed) return
        isDisposed = true
        try {
            detector.close()
        } catch (_: Exception) {}
    }
}

/**
 * Extension function to await a Google Task with coroutine suspension.
 */
private suspend fun <T> Task<T>.awaitTask(): T = suspendCancellableCoroutine { continuation ->
    addOnSuccessListener { result ->
        continuation.resume(result)
    }
    addOnFailureListener { exception ->
        continuation.resumeWithException(exception)
    }
    addOnCanceledListener {
        continuation.cancel()
    }
}
