package com.ux4g.designsystemv1.components.biometric

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.view.PreviewView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.filled.PanTool
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.LocalLifecycleOwner

/**
 * Camera preview screen handling all phases from device readiness through success/failure.
 * Exact parity with Flutter `CameraCaptureScreen`.
 *
 * Renders the live CameraX viewfinder stream and feeds frames directly to Google ML Kit.
 */
@Composable
fun CameraCaptureScreen(
    controller: BiometricController,
    modifier: Modifier = Modifier,
    onAlternateVerify: (() -> Unit)? = null,
    onDismiss: (() -> Unit)? = null,
    onSuccess: ((BiometricVerificationResult) -> Unit)? = null
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val theme = BiometricTheme.current
    val colors = theme.colors
    val state = controller.state

    var hasCameraPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
        )
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        hasCameraPermission = granted
        if (granted) {
            controller.startCamera()
        }
    }

    LaunchedEffect(Unit) {
        if (!hasCameraPermission) {
            permissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }

    if (!hasCameraPermission) {
        // Permission Request / Denied Screen matching Flutter _PermissionScreen
        PermissionScreen(
            onRequestPermission = {
                permissionLauncher.launch(Manifest.permission.CAMERA)
            }
        )
        return
    }

    val isDeviceCheckPhase = state == BiometricFlowState.DeviceReady || state == BiometricFlowState.CheckingDevice
    val isActiveCapture = isActiveCapture(state)
    val isTerminal = state == BiometricFlowState.VerificationSuccess || state == BiometricFlowState.VerificationFailed

    val cameraService = remember { CameraService(context) }

    DisposableEffect(Unit) {
        onDispose {
            cameraService.dispose()
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        // 1. Live CameraX Preview Viewfinder
        AndroidView(
            factory = { ctx ->
                PreviewView(ctx).apply {
                    scaleType = PreviewView.ScaleType.FILL_CENTER
                    implementationMode = PreviewView.ImplementationMode.COMPATIBLE

                    cameraService.bindPreview(
                        lifecycleOwner = lifecycleOwner,
                        previewView = this,
                        analyzer = { imageProxy ->
                            val previewSize = Size(
                                width.toFloat().takeIf { it > 0 } ?: 720f,
                                height.toFloat().takeIf { it > 0 } ?: 1280f
                            )
                            controller.processCameraFrame(imageProxy, previewSize)
                        }
                    )
                }
            },
            modifier = Modifier.fillMaxSize()
        )

        // Phase 1: Device Check Overlay matching Flutter _DeviceCheckOverlay
        if (isDeviceCheckPhase) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.35f))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .statusBarsPadding()
                        .navigationBarsPadding()
                        .padding(horizontal = 20.dp)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    // Device check card (floating, compact) at top
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp, bottom = 16.dp)
                    ) {
                        DeviceCheckScreen(
                            controller = controller,
                            onProceed = { controller.startDetection() },
                            compact = true
                        )
                    }

                    // Start Face Verification button at bottom
                    if (state == BiometricFlowState.DeviceReady) {
                        Button(
                            onClick = { controller.startDetection() },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 16.dp)
                                .height(52.dp),
                            shape = RoundedCornerShape(theme.buttonRadius),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = colors.primary,
                                contentColor = Color.White
                            ),
                            elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp)
                        ) {
                            Text(
                                text = "Start Face Verification",
                                style = theme.typography.button.copy(color = Color.White)
                            )
                        }
                    }
                }
            }
        }

        // Phase 2-5: Face Alignment Overlay
        if (isActiveCapture || isTerminal) {
            FaceAlignmentOverlay(
                visualState = mapVisualState(state, controller),
                instructionText = instructionText(state, controller),
                subText = subText(state, controller),
                instructionIcon = instructionIcon(state, controller),
                instructionIconColor = instructionIconColor(state, colors, controller),
                showTopBadge = true,
                attemptCount = controller.attemptCount,
                maxAttempts = controller.maxAttempts,
                verifiedName = controller.verificationResult?.maskedName,
                maskedAadhaar = controller.verificationResult?.maskedPhone,
                failureReason = controller.failureReason,
                onContinue = {
                    controller.verificationResult?.let { result ->
                        onSuccess?.invoke(result)
                    }
                },
                onRetry = { controller.retry() },
                onAlternateVerify = onAlternateVerify
            )
        }
    }
}

/**
 * Permission Screen matching Flutter `_PermissionScreen`.
 */
@Composable
fun PermissionScreen(
    onRequestPermission: () -> Unit,
    modifier: Modifier = Modifier
) {
    val theme = BiometricTheme.current
    val colors = theme.colors

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(colors.surface)
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(theme.screenPadding),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.verticalScroll(rememberScrollState())
        ) {
            Text(
                text = "Allow camera access to continue.",
                style = theme.typography.title.copy(
                    color = colors.onSurface,
                    fontWeight = FontWeight.Bold
                ),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Camera access is required for real-time face alignment and eye blink liveness verification.",
                style = theme.typography.body.copy(
                    color = colors.onSurface.copy(alpha = 0.6f)
                ),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = onRequestPermission,
                colors = ButtonDefaults.buttonColors(
                    containerColor = colors.primary,
                    contentColor = Color.White
                )
            ) {
                Text(text = "Allow Camera")
            }
        }
    }
}

private fun isActiveCapture(state: BiometricFlowState): Boolean {
    return state in listOf(
        BiometricFlowState.CameraReady,
        BiometricFlowState.DetectingFace,
        BiometricFlowState.AligningFace,
        BiometricFlowState.CheckingLighting,
        BiometricFlowState.CheckingLiveness,
        BiometricFlowState.BlinkRequired,
        BiometricFlowState.BlinkDetected,
        BiometricFlowState.CapturingImage,
        BiometricFlowState.ProcessingVerification
    )
}

/**
 * Map BiometricFlowState to OvalVisualState.
 * Exact parity with Flutter `_mapVisualState`.
 */
private fun mapVisualState(state: BiometricFlowState, ctrl: BiometricController): OvalVisualState {
    return when (state) {
        BiometricFlowState.CameraReady,
        BiometricFlowState.DetectingFace,
        BiometricFlowState.CheckingLighting -> OvalVisualState.Positioning

        BiometricFlowState.AligningFace -> alignmentVisual(ctrl.lastAnalysis?.alignment)

        BiometricFlowState.BlinkDetected,
        BiometricFlowState.CheckingLiveness,
        BiometricFlowState.CapturingImage -> OvalVisualState.Aligned

        BiometricFlowState.BlinkRequired -> OvalVisualState.Blink
        BiometricFlowState.ProcessingVerification -> OvalVisualState.Verifying
        BiometricFlowState.VerificationSuccess -> OvalVisualState.Success
        BiometricFlowState.VerificationFailed -> OvalVisualState.Failure
        else -> OvalVisualState.Positioning
    }
}

private fun alignmentVisual(alignment: FaceAlignmentStatus?): OvalVisualState {
    return if (alignment == FaceAlignmentStatus.Aligned) OvalVisualState.Aligned
    else OvalVisualState.Positioning
}

/**
 * Map BiometricFlowState to instruction text.
 * Exact parity with Flutter `_instructionText`.
 */
private fun instructionText(state: BiometricFlowState, ctrl: BiometricController): String {
    return when (state) {
        BiometricFlowState.CameraReady,
        BiometricFlowState.DetectingFace -> "Position your face in the oval"
        BiometricFlowState.AligningFace -> alignmentInstruction(ctrl)
        BiometricFlowState.CheckingLighting -> lightingInstruction(ctrl)
        BiometricFlowState.BlinkRequired -> "Please blink now"
        BiometricFlowState.BlinkDetected -> "Hold still..."
        BiometricFlowState.CheckingLiveness -> "Turn slightly to your left"
        BiometricFlowState.CapturingImage -> "Hold still..."
        BiometricFlowState.ProcessingVerification -> "Verifying with UIDAI"
        BiometricFlowState.VerificationSuccess -> "Identity verified"
        BiometricFlowState.VerificationFailed -> failureTitle(ctrl.failureReason)
        else -> "Position your face in the oval"
    }
}

private fun alignmentInstruction(ctrl: BiometricController): String {
    return when (ctrl.lastAnalysis?.alignment) {
        FaceAlignmentStatus.NoFace -> "Position your face in the oval"
        FaceAlignmentStatus.TooFar -> "Move closer to the camera"
        FaceAlignmentStatus.TooClose -> "Move back slightly"
        FaceAlignmentStatus.OffCenter -> "Position your face in the oval"
        FaceAlignmentStatus.MultipleFaces -> "Only one face allowed"
        FaceAlignmentStatus.Aligned -> "Hold still..."
        null -> "Position your face in the oval"
    }
}

private fun lightingInstruction(ctrl: BiometricController): String {
    return when (ctrl.lastAnalysis?.lighting) {
        LightingStatus.TooLow -> "Move to a brighter area"
        LightingStatus.OverExposed -> "Reduce backlight"
        else -> "Checking lighting..."
    }
}

private fun failureTitle(reason: VerificationFailureReason?): String {
    return when (reason) {
        VerificationFailureReason.LivenessFailed -> "Liveness check failed"
        VerificationFailureReason.FaceNotDetected -> "Face not detected"
        VerificationFailureReason.FaceNotMatched -> "Face does not match Aadhaar records"
        VerificationFailureReason.VerificationTimeout -> "Verification timed out"
        VerificationFailureReason.NetworkError -> "Cannot connect to UIDAI"
        VerificationFailureReason.ServerError -> "Server error"
        VerificationFailureReason.LowConfidence -> "Face does not match Aadhaar records"
        else -> "Verification failed"
    }
}

private fun subText(state: BiometricFlowState, ctrl: BiometricController): String? {
    return when (state) {
        BiometricFlowState.ProcessingVerification -> "Do not move"
        BiometricFlowState.VerificationFailed -> failureSubText(ctrl.failureReason)
        else -> null
    }
}

private fun failureSubText(reason: VerificationFailureReason?): String {
    return when (reason) {
        VerificationFailureReason.LivenessFailed -> "Please try again"
        VerificationFailureReason.FaceNotDetected -> "Please try again"
        VerificationFailureReason.FaceNotMatched -> "Please try again"
        VerificationFailureReason.LowConfidence -> "Please try again"
        VerificationFailureReason.VerificationTimeout -> "Try again"
        VerificationFailureReason.NetworkError -> "Check your connection"
        VerificationFailureReason.ServerError -> "Please try again later"
        else -> "Please try again"
    }
}

/**
 * Map BiometricFlowState to instruction icon.
 * Exact parity with Flutter `_instructionIcon`.
 */
private fun instructionIcon(state: BiometricFlowState, ctrl: BiometricController): ImageVector? {
    return when (state) {
        BiometricFlowState.CameraReady,
        BiometricFlowState.DetectingFace -> Icons.Outlined.FaceRetouchingNatural
        BiometricFlowState.AligningFace -> alignmentIcon(ctrl.lastAnalysis?.alignment)
        BiometricFlowState.CheckingLighting -> lightingIcon(ctrl.lastAnalysis?.lighting)
        BiometricFlowState.BlinkRequired -> Icons.Outlined.Visibility
        BiometricFlowState.BlinkDetected,
        BiometricFlowState.CapturingImage -> Icons.Outlined.PanTool
        BiometricFlowState.CheckingLiveness -> Icons.AutoMirrored.Outlined.ArrowBack
        BiometricFlowState.ProcessingVerification -> null // spinner
        BiometricFlowState.VerificationSuccess -> Icons.Outlined.CheckCircle
        BiometricFlowState.VerificationFailed -> failureIcon(ctrl.failureReason)
        else -> Icons.Outlined.FaceRetouchingNatural
    }
}

private fun alignmentIcon(alignment: FaceAlignmentStatus?): ImageVector {
    return when (alignment) {
        FaceAlignmentStatus.TooFar -> Icons.Outlined.ExpandMore
        FaceAlignmentStatus.TooClose -> Icons.Outlined.ExpandLess
        else -> Icons.Outlined.FaceRetouchingNatural
    }
}

private fun lightingIcon(lighting: LightingStatus?): ImageVector {
    return Icons.Outlined.WbSunny
}

private fun failureIcon(reason: VerificationFailureReason?): ImageVector {
    return when (reason) {
        VerificationFailureReason.LivenessFailed -> Icons.Outlined.WarningAmber
        VerificationFailureReason.FaceNotMatched -> Icons.Outlined.ErrorOutline
        VerificationFailureReason.VerificationTimeout -> Icons.Outlined.TimerOff
        VerificationFailureReason.NetworkError -> Icons.Outlined.WifiOff
        else -> Icons.Outlined.WarningAmber
    }
}

/**
 * Map BiometricFlowState to instruction icon color.
 * Exact parity with Flutter `_instructionIconColor`.
 */
private fun instructionIconColor(
    state: BiometricFlowState,
    colors: BiometricColors,
    ctrl: BiometricController
): Color? {
    return when (state) {
        BiometricFlowState.BlinkRequired -> colors.ovalBlink
        BiometricFlowState.BlinkDetected,
        BiometricFlowState.CapturingImage -> colors.warning
        BiometricFlowState.CheckingLighting -> colors.warning
        BiometricFlowState.AligningFace -> alignmentIconColor(ctrl.lastAnalysis?.alignment, colors)
        BiometricFlowState.VerificationSuccess -> colors.success
        BiometricFlowState.VerificationFailed -> failureIconColor(ctrl.failureReason, colors)
        else -> null
    }
}

private fun alignmentIconColor(alignment: FaceAlignmentStatus?, colors: BiometricColors): Color? {
    return when (alignment) {
        FaceAlignmentStatus.TooFar,
        FaceAlignmentStatus.TooClose -> colors.warning
        else -> null
    }
}

private fun failureIconColor(reason: VerificationFailureReason?, colors: BiometricColors): Color {
    return colors.warning
}
