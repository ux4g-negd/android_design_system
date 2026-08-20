package com.ux4g.designsystemv1.components.biometric

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material.icons.outlined.CameraAlt
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.ErrorOutline
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.WbSunny
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

/**
 * A single device check row matching Flutter `DeviceCheckCard`.
 *
 * Layout: icon | label | spacer | status_icon + status_text
 */
@Composable
fun DeviceCheckCard(
    item: DeviceCheckItem,
    index: Int,
    modifier: Modifier = Modifier
) {
    val theme = BiometricTheme.current
    val colors = theme.colors

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Left Icon
        Icon(
            imageVector = iconForLabel(item.label),
            contentDescription = null,
            tint = colors.onSurface.copy(alpha = 0.45f),
            modifier = Modifier.size(18.dp)
        )

        Spacer(modifier = Modifier.width(10.dp))

        // Label
        Text(
            text = item.label,
            style = theme.typography.body.copy(
                color = colors.onSurface.copy(alpha = 0.8f)
            ),
            modifier = Modifier.weight(1f)
        )

        // Status Indicator
        when (item.status) {
            DeviceCheckStatus.Pending -> {
                Text(
                    text = "—",
                    style = theme.typography.caption.copy(
                        color = colors.onSurface.copy(alpha = 0.3f)
                    )
                )
            }

            DeviceCheckStatus.Checking -> {
                CircularProgressIndicator(
                    modifier = Modifier.size(16.dp),
                    strokeWidth = 2.dp,
                    color = colors.primary
                )
            }

            DeviceCheckStatus.Passed -> {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Passed",
                        tint = colors.success,
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = item.passedText,
                        style = theme.typography.caption.copy(
                            color = colors.success,
                            fontWeight = FontWeight.Medium
                        ),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            DeviceCheckStatus.Failed -> {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.ErrorOutline,
                        contentDescription = "Failed",
                        tint = colors.error,
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = item.failedText,
                        style = theme.typography.caption.copy(
                            color = colors.error,
                            fontWeight = FontWeight.Medium
                        ),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}

private fun iconForLabel(label: String): ImageVector {
    val lower = label.lowercase()
    return when {
        lower.contains("camera") -> Icons.Outlined.CameraAlt
        lower.contains("lighting") || lower.contains("light") -> Icons.Outlined.WbSunny
        lower.contains("uidai") || lower.contains("connection") || lower.contains("internet") -> Icons.Default.Wifi
        lower.contains("permission") -> Icons.Outlined.Lock
        lower.contains("fingerprint") -> Icons.Default.Fingerprint
        lower.contains("scanner") || lower.contains("driver") -> Icons.Outlined.Settings
        lower.contains("sdk") -> Icons.Default.Code
        else -> Icons.Outlined.CheckCircle
    }
}
