package com.ux4g.designsystemv1.components.biometric

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.*
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.outlined.ErrorOutline
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Visual state of the oval border on the camera capture screen.
 * Exact parity with Flutter `OvalVisualState`.
 */
enum class OvalVisualState {
    Positioning,
    Aligned,
    Blink,
    Verifying,
    Success,
    Failure
}

/**
 * Full-screen overlay for the camera capture screen matching Flutter `FaceAlignmentOverlay`.
 */
@Composable
fun FaceAlignmentOverlay(
    visualState: OvalVisualState,
    instructionText: String,
    modifier: Modifier = Modifier,
    subText: String? = null,
    instructionIcon: ImageVector? = null,
    instructionIconColor: Color? = null,
    showTopBadge: Boolean = true,
    attemptCount: Int = 0,
    maxAttempts: Int = 2,
    verifiedName: String? = null,
    maskedAadhaar: String? = null,
    failureReason: VerificationFailureReason? = null,
    onContinue: (() -> Unit)? = null,
    onRetry: (() -> Unit)? = null,
    onAlternateVerify: (() -> Unit)? = null
) {
    val theme = BiometricTheme.current
    val colors = theme.colors

    // Pulse animation for positioning state
    val infiniteTransition = rememberInfiniteTransition(label = "pulse_transition")
    val pulseValue by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse"
    )

    // Spinner rotation for verifying state
    val rotationAngle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "spinner"
    )

    val localDensity = androidx.compose.ui.platform.LocalDensity.current

    BoxWithConstraints(
        modifier = modifier.fillMaxSize()
    ) {
        val w = constraints.maxWidth.toFloat()
        val h = constraints.maxHeight.toFloat()

        // Responsive oval calculation to guarantee it never overlaps top badge or bottom card
        val minOvalW = with(localDensity) { 180.dp.toPx() }
        val maxOvalW = with(localDensity) { 280.dp.toPx() }
        val ovalW = (w * 0.65f).coerceIn(minOvalW, maxOvalW)
        val ovalH = (ovalW * 1.35f).coerceAtMost(h * 0.38f)

        val topMargin = with(localDensity) { 70.dp.toPx() }
        val bottomReserved = with(localDensity) { 230.dp.toPx() }
        val idealCenterY = h * 0.36f
        val ovalCenterY = idealCenterY.coerceIn(ovalH / 2f + topMargin, (h - ovalH / 2f - bottomReserved).coerceAtLeast(ovalH / 2f + topMargin))
        val ovalCenter = Offset(w / 2f, ovalCenterY)

        val ovalRect = Rect(
            left = ovalCenter.x - ovalW / 2f,
            top = ovalCenter.y - ovalH / 2f,
            right = ovalCenter.x + ovalW / 2f,
            bottom = ovalCenter.y + ovalH / 2f
        )

        // 1. Dark Cutout Overlay with Oval Window
        Canvas(modifier = Modifier.fillMaxSize()) {
            val path = Path().apply {
                addRect(Rect(0f, 0f, size.width, size.height))
                addOval(ovalRect)
                fillType = PathFillType.EvenOdd
            }
            drawPath(path, color = colors.overlay)

            // Oval Border Drawing
            when (visualState) {
                OvalVisualState.Positioning -> {
                    val borderAlpha = 0.6f + (pulseValue * 0.4f)
                    drawOval(
                        color = colors.ovalGuide.copy(alpha = borderAlpha),
                        topLeft = Offset(ovalRect.left, ovalRect.top),
                        size = Size(ovalRect.width, ovalRect.height),
                        style = Stroke(width = 3.dp.toPx(), cap = StrokeCap.Round)
                    )
                }

                OvalVisualState.Aligned -> {
                    drawOval(
                        color = colors.ovalActive,
                        topLeft = Offset(ovalRect.left, ovalRect.top),
                        size = Size(ovalRect.width, ovalRect.height),
                        style = Stroke(width = 3.5.dp.toPx(), cap = StrokeCap.Round)
                    )
                }

                OvalVisualState.Blink -> {
                    val dashEffect = PathEffect.dashPathEffect(floatArrayOf(24f, 16f), 0f)
                    drawOval(
                        color = colors.ovalBlink,
                        topLeft = Offset(ovalRect.left, ovalRect.top),
                        size = Size(ovalRect.width, ovalRect.height),
                        style = Stroke(width = 3.dp.toPx(), cap = StrokeCap.Round, pathEffect = dashEffect)
                    )
                }

                OvalVisualState.Verifying -> {
                    drawOval(
                        color = colors.ovalGuide.copy(alpha = 0.5f),
                        topLeft = Offset(ovalRect.left, ovalRect.top),
                        size = Size(ovalRect.width, ovalRect.height),
                        style = Stroke(width = 3.dp.toPx(), cap = StrokeCap.Round)
                    )
                }

                OvalVisualState.Success -> {
                    drawOval(
                        color = colors.ovalActive,
                        topLeft = Offset(ovalRect.left, ovalRect.top),
                        size = Size(ovalRect.width, ovalRect.height),
                        style = Stroke(width = 4.dp.toPx(), cap = StrokeCap.Round)
                    )
                }

                OvalVisualState.Failure -> {
                    drawOval(
                        color = colors.ovalError,
                        topLeft = Offset(ovalRect.left, ovalRect.top),
                        size = Size(ovalRect.width, ovalRect.height),
                        style = Stroke(width = 4.dp.toPx(), cap = StrokeCap.Round)
                    )
                }
            }
        }

        // 2. Verifying Spinner Overlay inside Oval
        if (visualState == OvalVisualState.Verifying) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = with(localDensity) { (h - ovalCenterY).toDp() - 20.dp }),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.size(40.dp),
                    strokeWidth = 3.dp,
                    color = colors.primary.copy(alpha = 0.8f)
                )
            }
        }

        // 3. Top-Right Success Check Badge on Oval
        if (visualState == OvalVisualState.Success) {
            val badgeX = (ovalCenter.x + (ovalW / 2f) * 0.707f - with(localDensity) { 18.dp.toPx() })
            val badgeY = (ovalCenter.y - (ovalH / 2f) * 0.707f - with(localDensity) { 18.dp.toPx() })
            val badgeXDp = with(localDensity) { badgeX.toDp() }
            val badgeYDp = with(localDensity) { badgeY.toDp() }
            Box(
                modifier = Modifier
                    .absoluteOffset(
                        x = badgeXDp,
                        y = badgeYDp
                    )
                    .size(38.dp)
                    .clip(CircleShape)
                    .background(colors.success)
                    .border(3.dp, Color.White, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Success",
                    tint = Color.White,
                    modifier = Modifier.size(22.dp)
                )
            }
        }

        // 4. Top-Right Failure X Badge on Oval
        if (visualState == OvalVisualState.Failure) {
            val badgeX = (ovalCenter.x + (ovalW / 2f) * 0.707f - with(localDensity) { 18.dp.toPx() })
            val badgeY = (ovalCenter.y - (ovalH / 2f) * 0.707f - with(localDensity) { 18.dp.toPx() })
            val badgeXDp = with(localDensity) { badgeX.toDp() }
            val badgeYDp = with(localDensity) { badgeY.toDp() }
            Box(
                modifier = Modifier
                    .absoluteOffset(
                        x = badgeXDp,
                        y = badgeYDp
                    )
                    .size(38.dp)
                    .clip(CircleShape)
                    .background(colors.error)
                    .border(3.dp, Color.White, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Failure",
                    tint = Color.White,
                    modifier = Modifier.size(22.dp)
                )
            }
        }

        // 5. Top: "All systems ready" Green Pill Badge (with status bar inset)
        if (showTopBadge) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(top = 12.dp),
                contentAlignment = Alignment.TopCenter
            ) {
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(colors.statusBadgeBg)
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = "All systems ready",
                        style = theme.typography.caption.copy(
                            color = Color.White,
                            fontWeight = FontWeight.SemiBold
                        )
                    )
                }
            }
        }

        // 6. Bottom Area: Instruction Card + Action Buttons (with navigation bar inset)
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(start = 20.dp, end = 20.dp, bottom = 12.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Instruction Card
            InstructionCard(
                icon = instructionIcon,
                iconColor = instructionIconColor,
                text = instructionText,
                subText = subText,
                verifiedName = verifiedName,
                maskedAadhaar = maskedAadhaar,
                visualState = visualState,
                attemptCount = attemptCount,
                maxAttempts = maxAttempts,
                isVerifying = visualState == OvalVisualState.Verifying,
                theme = theme,
                colors = colors
            )

            // Action Buttons
            if (visualState == OvalVisualState.Success) {
                Spacer(modifier = Modifier.height(14.dp))
                Button(
                    onClick = { onContinue?.invoke() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    shape = RoundedCornerShape(theme.buttonRadius),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colors.primary,
                        contentColor = Color.White
                    ),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp)
                ) {
                    Text(
                        text = "Continue",
                        style = theme.typography.button.copy(color = Color.White)
                    )
                }
            }

            if (visualState == OvalVisualState.Failure) {
                Spacer(modifier = Modifier.height(14.dp))
                Button(
                    onClick = { onRetry?.invoke() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    shape = RoundedCornerShape(theme.buttonRadius),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colors.primary,
                        contentColor = Color.White
                    ),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp)
                ) {
                    Text(
                        text = "Try again",
                        style = theme.typography.button.copy(color = Color.White)
                    )
                }

                if (onAlternateVerify != null) {
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedButton(
                        onClick = onAlternateVerify,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp),
                        shape = RoundedCornerShape(theme.buttonRadius),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = colors.primary
                        ),
                        border = BorderStroke(1.dp, colors.primary.copy(alpha = 0.4f))
                    ) {
                        Text(
                            text = "Verify with OTP instead",
                            style = theme.typography.button.copy(color = colors.primary)
                        )
                    }
                }
            }
        }
    }
}

/**
 * Bottom Instruction Card matching Flutter `_InstructionCard`.
 */
@Composable
private fun InstructionCard(
    icon: ImageVector?,
    iconColor: Color?,
    text: String,
    subText: String?,
    verifiedName: String?,
    maskedAadhaar: String?,
    visualState: OvalVisualState,
    attemptCount: Int,
    maxAttempts: Int,
    isVerifying: Boolean,
    theme: BiometricThemeData,
    colors: BiometricColors
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(12.dp, RoundedCornerShape(16.dp))
            .clip(RoundedCornerShape(16.dp))
            .background(colors.instructionCardBg)
            .padding(horizontal = 20.dp, vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Icon
        if (isVerifying) {
            CircularProgressIndicator(
                modifier = Modifier.size(28.dp),
                strokeWidth = 2.5.dp,
                color = colors.primary
            )
        } else if (icon != null) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = iconColor ?: colors.onSurface.copy(alpha = 0.5f),
                modifier = Modifier.size(28.dp)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Main Text
        Text(
            text = text,
            style = theme.typography.label.copy(
                color = colors.onSurface.copy(alpha = 0.9f),
                fontWeight = FontWeight.SemiBold
            ),
            textAlign = TextAlign.Center
        )

        // Sub Text
        if (subText != null) {
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = subText,
                style = theme.typography.caption.copy(
                    color = colors.onSurface.copy(alpha = 0.5f)
                ),
                textAlign = TextAlign.Center
            )
        }

        // Attempt Warning (shown on failure)
        if (visualState == OvalVisualState.Failure) {
            Spacer(modifier = Modifier.height(12.dp))
            AttemptWarning(
                attemptCount = attemptCount,
                maxAttempts = maxAttempts,
                theme = theme,
                colors = colors
            )
        }

        // Verified Name + Masked Aadhaar
        if (visualState == OvalVisualState.Success && verifiedName != null) {
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = verifiedName,
                style = theme.typography.label.copy(
                    color = colors.onSurface,
                    fontWeight = FontWeight.SemiBold
                ),
                textAlign = TextAlign.Center
            )
            if (maskedAadhaar != null) {
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = maskedAadhaar,
                    style = theme.typography.caption.copy(
                        color = colors.onSurface.copy(alpha = 0.5f),
                        letterSpacing = 1.5.sp
                    ),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

/**
 * Attempt Warning Card matching Flutter `_AttemptWarning` and reference UI.
 */
@Composable
private fun AttemptWarning(
    attemptCount: Int,
    maxAttempts: Int,
    theme: BiometricThemeData,
    colors: BiometricColors
) {
    val displayAttempt = (attemptCount + 1).coerceAtMost(maxAttempts)
    val isLast = displayAttempt >= maxAttempts || attemptCount >= maxAttempts - 1
    val bg = if (isLast) colors.attemptLastBg else colors.attemptWarningBg
    val border = if (isLast) colors.attemptLastBorder else colors.attemptWarningBorder
    val iconColor = if (isLast) colors.error else colors.warning

    val title = if (isLast) "Last attempt" else "Attempt $displayAttempt of $maxAttempts"
    val remaining = (maxAttempts - displayAttempt).coerceAtLeast(1)
    val subtitle = if (isLast) "Account locks for 30 minutes if this fails" else "$remaining more before lockout."

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(bg)
            .border(1.dp, border, RoundedCornerShape(10.dp))
            .padding(horizontal = 14.dp, vertical = 10.dp),
        verticalAlignment = Alignment.Top
    ) {
        Icon(
            imageVector = Icons.Outlined.ErrorOutline,
            contentDescription = null,
            tint = iconColor,
            modifier = Modifier.size(18.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = title,
                style = theme.typography.caption.copy(
                    color = colors.onSurface.copy(alpha = 0.85f),
                    fontWeight = FontWeight.SemiBold
                )
            )
            Text(
                text = subtitle,
                style = theme.typography.caption.copy(
                    color = colors.onSurface.copy(alpha = 0.6f),
                    fontSize = 11.sp
                )
            )
        }
    }
}
