package com.ux4g.designsystemv1.components.biometric

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Shield
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Initial consent screen shown before biometric verification begins.
 * Exact parity with Flutter `IdentityConsentScreen`.
 *
 * Matches the reference design: shield icon, title, description,
 * "Allow and verify" button, "Decline - Verify with OTP instead" button.
 */
@Composable
fun IdentityConsentScreen(
    onAllow: () -> Unit,
    modifier: Modifier = Modifier,
    onDecline: (() -> Unit)? = null
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
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
        ) {
            // Shield Icon
            Box(
                modifier = Modifier
                    .size(72.dp)
                    .clip(CircleShape)
                    .background(colors.primary.copy(alpha = 0.12f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Outlined.Shield,
                    contentDescription = null,
                    tint = colors.primary,
                    modifier = Modifier.size(36.dp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Title
            Text(
                text = "Identity Verification",
                style = theme.typography.headline.copy(
                    color = colors.onSurface
                ),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Description
            Text(
                text = "Your face will be matched against your Aadhaar photo. No biometric data is stored on this device.",
                style = theme.typography.body.copy(
                    color = colors.onSurface.copy(alpha = 0.6f)
                ),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Allow and verify button
            Button(
                onClick = onAllow,
                modifier = Modifier
                    .width(260.dp)
                    .height(48.dp),
                shape = RoundedCornerShape(theme.buttonRadius),
                colors = ButtonDefaults.buttonColors(
                    containerColor = colors.primary,
                    contentColor = Color.White
                ),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp)
            ) {
                Text(
                    text = "Allow and verify",
                    style = theme.typography.button.copy(
                        color = Color.White
                    )
                )
            }

            // Decline button
            if (onDecline != null) {
                Spacer(modifier = Modifier.height(12.dp))
                OutlinedButton(
                    onClick = onDecline,
                    modifier = Modifier
                        .width(260.dp)
                        .height(48.dp),
                    shape = RoundedCornerShape(theme.buttonRadius),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = colors.primary
                    ),
                    border = BorderStroke(1.dp, colors.primary.copy(alpha = 0.4f))
                ) {
                    Text(
                        text = "Decline - Verify with OTP instead",
                        style = theme.typography.button.copy(
                            color = colors.primary,
                            fontSize = 14.sp
                        )
                    )
                }
            }
        }
    }
}
