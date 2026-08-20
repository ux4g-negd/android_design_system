package com.ux4g.designsystemv1.components.biometric

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Device check card matching Flutter `DeviceCheckScreen`.
 *
 * Can be used as a standalone full-screen or as a floating card overlay.
 * Shows "Device Check" title, check rows, helper text, and proceed button.
 */
@Composable
fun DeviceCheckScreen(
    controller: BiometricController,
    onProceed: () -> Unit,
    modifier: Modifier = Modifier,
    compact: Boolean = false
) {
    val theme = BiometricTheme.current
    val colors = theme.colors

    LaunchedEffect(Unit) {
        if (controller.state == BiometricFlowState.Initial || controller.deviceChecks.isEmpty()) {
            controller.startDeviceChecks()
        }
    }

    val checks = controller.deviceChecks
    val allPassed = controller.state == BiometricFlowState.DeviceReady
    val hasFailed = checks.any { it.status == DeviceCheckStatus.Failed }

    // Collect helper texts from failed items
    val helperTexts = checks.filter { it.status == DeviceCheckStatus.Failed && it.helperText != null }
        .mapNotNull { it.helperText }

    val cardContent = @Composable {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(
                    elevation = if (compact) 8.dp else 12.dp,
                    shape = RoundedCornerShape(theme.cardRadius)
                )
                .clip(RoundedCornerShape(theme.cardRadius))
                .background(colors.surface)
                .border(
                    width = 1.dp,
                    color = colors.onSurface.copy(alpha = 0.08f),
                    shape = RoundedCornerShape(theme.cardRadius)
                )
                .padding(if (compact) 16.dp else 20.dp),
            horizontalAlignment = Alignment.Start
        ) {
            // Title
            Text(
                text = "Device Check",
                style = theme.typography.title.copy(
                    color = colors.onSurface,
                    fontWeight = FontWeight.Bold
                )
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Check rows
            checks.forEachIndexed { i, item ->
                DeviceCheckCard(item = item, index = i)
            }

            // Helper text for failures
            if (hasFailed && helperTexts.isNotEmpty()) {
                Spacer(modifier = Modifier.height(4.dp))
                helperTexts.forEach { text ->
                    Text(
                        text = text,
                        style = theme.typography.caption.copy(
                            color = colors.onSurface.copy(alpha = 0.5f),
                            fontSize = 11.sp
                        ),
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }

            // Proceed button (only when all passed)
            if (allPassed) {
                Spacer(modifier = Modifier.height(12.dp))
                Button(
                    onClick = onProceed,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(40.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colors.success,
                        contentColor = Color.White
                    ),
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "All systems ready — proceed to verify",
                            style = theme.typography.caption.copy(
                                color = Color.White,
                                fontWeight = FontWeight.SemiBold
                            ),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }
        }
    }

    if (compact) {
        Box(modifier = modifier) {
            cardContent()
        }
    } else {
        Box(
            modifier = modifier
                .fillMaxSize()
                .background(colors.cardBackground)
                .statusBarsPadding()
                .navigationBarsPadding()
                .padding(theme.screenPadding),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                cardContent()
            }
        }
    }
}
