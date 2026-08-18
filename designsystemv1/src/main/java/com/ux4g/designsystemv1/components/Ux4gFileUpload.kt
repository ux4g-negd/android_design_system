package com.ux4g.designsystemv1.components

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.net.Uri
import android.provider.OpenableColumns
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.CloudUpload
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.InsertDriveFile
import androidx.compose.material.icons.filled.PictureAsPdf
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ux4g.designsystemv1.components.buttons.Ux4gButton
import com.ux4g.designsystemv1.components.buttons.Ux4gButtonSize
import com.ux4g.designsystemv1.components.buttons.Ux4gButtonVariant
import com.ux4g.designsystemv1.foundation.Ux4gColors
import com.ux4g.designsystemv1.foundation.Ux4gRadius
import com.ux4g.designsystemv1.foundation.defaultTypography
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

// ─────────────────────────────────────────────────────────────────────────────
// Models & Enums
// ─────────────────────────────────────────────────────────────────────────────

enum class Ux4gUploadStatus {
    Idle,
    Uploading,
    Success,
    Error;

    companion object {
        val idle = Idle
        val uploading = Uploading
        val success = Success
        val error = Error
    }
}
typealias UploadStatus = Ux4gUploadStatus

enum class Ux4gFileUploadBorderStyle {
    Solid,
    Dashed;

    companion object {
        val solid = Solid
        val dashed = Dashed
    }
}

data class Ux4gUploadedFile(
    val id: String,
    val name: String,
    val fileSize: Long,
    val status: Ux4gUploadStatus = Ux4gUploadStatus.Idle,
    val progress: Float = 0f,
    val errorMessage: String? = null,
    val uri: Uri? = null
) {
    val formattedSize: String
        get() {
            if (fileSize < 1024) return "$fileSize B"
            if (fileSize < 1024 * 1024) {
                return String.format("%.2f KB", fileSize / 1024.0)
            }
            return String.format("%.2f MB", fileSize / (1024.0 * 1024.0))
        }
}
typealias UploadedFile = Ux4gUploadedFile

// ─────────────────────────────────────────────────────────────────────────────
// File Upload Component
// ─────────────────────────────────────────────────────────────────────────────

/**
 * A comprehensive File Upload component matching UX4G design system specifications 1:1.
 *
 * Supports file picking, camera scan, progress tracking, file cards with type-based iconography,
 * retry flows, dashed/solid borders, custom error states, and direct UX4G tokens.
 */
@Composable
fun Ux4gFileUpload(
    modifier: Modifier = Modifier,
    files: List<Ux4gUploadedFile> = emptyList(),
    maxFiles: Int = 5,
    maxFileSize: Long = 5L * 1024L * 1024L, // 5MB
    allowedExtensions: List<String> = listOf("jpg", "png", "pdf"),
    borderStyle: Ux4gFileUploadBorderStyle = Ux4gFileUploadBorderStyle.Solid,
    buttonBorderRadius: Dp = 8.dp,
    buttonColor: Color? = null,
    buttonBorderColor: Color? = null,
    errorTitle: String? = null,
    errorText: String? = null,
    errorTitleStyle: TextStyle? = null,
    errorTextStyle: TextStyle? = null,
    onFilesChanged: ((List<Ux4gUploadedFile>) -> Unit)? = null,
    onUpload: (suspend (Ux4gUploadedFile) -> Boolean)? = null,
    onError: ((title: String, message: String) -> Unit)? = null
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    var internalFiles by remember { mutableStateOf(files) }
    var hasGlobalError by remember { mutableStateOf(false) }
    var currentErrorTitle by remember { mutableStateOf<String?>(null) }
    var currentErrorText by remember { mutableStateOf<String?>(null) }

    fun updateFileList(newFiles: List<Ux4gUploadedFile>) {
        internalFiles = newFiles
        onFilesChanged?.invoke(newFiles)
    }

    fun triggerError(title: String, message: String, fileName: String? = null) {
        hasGlobalError = true
        val resolvedTitle = errorTitle ?: title
        var resolvedText = errorText ?: message
        if (fileName != null) {
            resolvedText = resolvedText.replace("[File Name]", fileName)
        }
        currentErrorTitle = resolvedTitle
        currentErrorText = resolvedText
        onError?.invoke(resolvedTitle, resolvedText)
    }

    fun uploadSingleFile(file: Ux4gUploadedFile) {
        scope.launch {
            val fileIndex = internalFiles.indexOfFirst { it.id == file.id }
            if (fileIndex == -1) return@launch

            // Set uploading state
            val uploadingFiles = internalFiles.toMutableList()
            uploadingFiles[fileIndex] = file.copy(status = Ux4gUploadStatus.Uploading, progress = 0f)
            updateFileList(uploadingFiles)

            // Simulate progress animation
            for (step in 1..10) {
                delay(120)
                val currentIdx = internalFiles.indexOfFirst { it.id == file.id }
                if (currentIdx != -1) {
                    val updated = internalFiles.toMutableList()
                    updated[currentIdx] = updated[currentIdx].copy(progress = step / 10f)
                    updateFileList(updated)
                }
            }

            // Execute custom or default upload
            val success = onUpload?.invoke(file) ?: true

            val finalIdx = internalFiles.indexOfFirst { it.id == file.id }
            if (finalIdx != -1) {
                val updated = internalFiles.toMutableList()
                if (success) {
                    updated[finalIdx] = updated[finalIdx].copy(
                        status = Ux4gUploadStatus.Success,
                        progress = 1f,
                        errorMessage = null
                    )
                } else {
                    updated[finalIdx] = updated[finalIdx].copy(
                        status = Ux4gUploadStatus.Error,
                        errorMessage = "Upload failed. Please try again."
                    )
                }
                updateFileList(updated)
            }
        }
    }

    fun processSelectedUri(uri: Uri) {
        hasGlobalError = false
        val fileDetails = getFileDetailsFromUri(context, uri)
        val name = fileDetails.first
        val size = fileDetails.second

        // Size check
        if (size > maxFileSize) {
            val maxMb = maxFileSize / (1024 * 1024)
            triggerError(
                title = "File too large",
                message = "$name exceeds the ${maxMb}MB upload limit. Please compress it or select a smaller file.",
                fileName = name
            )
            return
        }

        // Max files check
        if (internalFiles.size >= maxFiles) {
            triggerError(
                title = "Max files reached",
                message = "You can upload maximum $maxFiles files."
            )
            return
        }

        val newFile = Ux4gUploadedFile(
            id = System.currentTimeMillis().toString(),
            name = name,
            fileSize = size,
            uri = uri
        )

        val newFiles = internalFiles + newFile
        updateFileList(newFiles)
        uploadSingleFile(newFile)
    }

    // Activity Result Launchers
    val filePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetMultipleContents()
    ) { uris: List<Uri> ->
        uris.forEach { uri -> processSelectedUri(uri) }
    }

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview()
    ) { bitmap ->
        if (bitmap != null) {
            hasGlobalError = false
            if (internalFiles.size >= maxFiles) {
                triggerError(
                    title = "Max files reached",
                    message = "You can upload maximum $maxFiles files."
                )
                return@rememberLauncherForActivityResult
            }
            val scannedFile = Ux4gUploadedFile(
                id = System.currentTimeMillis().toString(),
                name = "Scanned_Doc_${System.currentTimeMillis() % 10000}.jpg",
                fileSize = 512 * 1024L // Simulated size
            )
            val newFiles = internalFiles + scannedFile
            updateFileList(newFiles)
            uploadSingleFile(scannedFile)
        }
    }

    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            cameraLauncher.launch(null)
        } else {
            triggerError(
                title = "Camera Permission Required",
                message = "Camera permission is required to scan documents. Please grant camera access in app settings."
            )
        }
    }

    val primary = Ux4gColors.primary
    val errorColor = Ux4gColors.red600
    val onPrimary = Ux4gColors.neutral50
    val onSurface = Ux4gColors.neutral900
    val onBackground = Ux4gColors.neutral900

    val resolvedBorderColor = if (hasGlobalError) errorColor else primary
    val resolvedButtonColor = buttonColor ?: primary
    val resolvedButtonBorderColor = buttonBorderColor ?: primary

    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.Start
    ) {
        // ── Upload Box ────────────────────────────────────────────────────────
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .then(
                    if (borderStyle == Ux4gFileUploadBorderStyle.Dashed) {
                        Modifier.dashedBorder(
                            color = resolvedBorderColor,
                            strokeWidth = 2.dp,
                            borderRadius = 12.dp
                        )
                    } else {
                        Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .border(2.dp, resolvedBorderColor, RoundedCornerShape(12.dp))
                    }
                )
                .background(Ux4gColors.neutral0, RoundedCornerShape(12.dp))
                .padding(24.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Upload Icon
                if (hasGlobalError) {
                    Icon(
                        imageVector = Icons.Default.ErrorOutline,
                        contentDescription = "Error",
                        tint = errorColor,
                        modifier = Modifier.size(32.dp)
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.CloudUpload,
                        contentDescription = "Upload",
                        tint = primary,
                        modifier = Modifier.size(32.dp)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Title
                Text(
                    text = if (hasGlobalError) (currentErrorTitle ?: "Could not scan") else "Upload Documents",
                    style = if (hasGlobalError) {
                        (errorTitleStyle ?: defaultTypography.tS_strong).copy(
                            color = onBackground,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                    } else {
                        defaultTypography.hS_strong.copy(
                            color = onBackground,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold
                        )
                    },
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Description
                Text(
                    text = if (hasGlobalError) {
                        (currentErrorText ?: "File exceeds the upload limit.")
                    } else {
                        val exts = allowedExtensions.joinToString(" ") { it.uppercase() }
                        val maxMb = maxFileSize / (1024 * 1024)
                        "File type: $exts Max size: $maxMb MB"
                    },
                    style = if (hasGlobalError) {
                        (errorTextStyle ?: defaultTypography.bS_default).copy(color = errorColor)
                    } else {
                        defaultTypography.bS_default.copy(color = onSurface.copy(alpha = 0.6f))
                    },
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Action Buttons: Upload & Scan
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedButton(
                        onClick = { filePickerLauncher.launch("*/*") },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(buttonBorderRadius),
                        border = androidx.compose.foundation.BorderStroke(1.5.dp, resolvedButtonBorderColor)
                    ) {
                        Icon(
                            imageVector = Icons.Default.CloudUpload,
                            contentDescription = null,
                            tint = resolvedButtonColor,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Upload",
                            style = defaultTypography.bS_strong.copy(color = resolvedButtonColor)
                        )
                    }

                    ElevatedButton(
                        onClick = {
                            val hasCameraPermission = ContextCompat.checkSelfPermission(
                                context,
                                Manifest.permission.CAMERA
                            ) == PackageManager.PERMISSION_GRANTED

                            if (hasCameraPermission) {
                                cameraLauncher.launch(null)
                            } else {
                                cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
                            }
                        },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(buttonBorderRadius),
                        colors = ButtonDefaults.elevatedButtonColors(
                            containerColor = resolvedButtonColor,
                            contentColor = onPrimary
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Default.CameraAlt,
                            contentDescription = null,
                            tint = onPrimary,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Scan",
                            style = defaultTypography.bS_strong.copy(color = onPrimary)
                        )
                    }
                }
            }
        }

        // ── Uploaded Files List ───────────────────────────────────────────────
        if (internalFiles.isNotEmpty()) {
            Spacer(modifier = Modifier.height(20.dp))

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                internalFiles.forEach { file ->
                    Ux4gFileItemCard(
                        file = file,
                        onRemove = {
                            val remaining = internalFiles.filter { it.id != file.id }
                            updateFileList(remaining)
                        },
                        onRetry = {
                            uploadSingleFile(file)
                        }
                    )
                }
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// File Item Card Composable
// ─────────────────────────────────────────────────────────────────────────────

@Composable
fun Ux4gFileItemCard(
    file: Ux4gUploadedFile,
    onRemove: () -> Unit,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier
) {
    val extension = file.name.substringAfterLast('.', "").lowercase()

    val (iconVector, iconColor, iconBg) = when (extension) {
        "jpg", "jpeg", "png", "gif", "bmp", "webp" -> {
            Triple(Icons.Default.Image, Ux4gColors.blue600, Ux4gColors.blue50)
        }
        "pdf" -> {
            Triple(Icons.Default.PictureAsPdf, Ux4gColors.red600, Ux4gColors.red50)
        }
        "doc", "docx", "txt" -> {
            Triple(Icons.Default.Description, Ux4gColors.primary600, Ux4gColors.primary50)
        }
        else -> {
            Triple(Icons.Default.InsertDriveFile, Ux4gColors.neutral500, Ux4gColors.neutral100)
        }
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(Ux4gColors.neutral0)
            .border(1.dp, Ux4gColors.neutral200, RoundedCornerShape(8.dp))
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 10.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // File Icon Box
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(iconBg),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = iconVector,
                            contentDescription = null,
                            tint = iconColor,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    // Name + Size
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = file.name,
                            style = defaultTypography.tS_strong.copy(
                                color = Ux4gColors.neutral900,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            ),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = file.formattedSize,
                            style = defaultTypography.bS_default.copy(
                                color = Ux4gColors.neutral500,
                                fontSize = 12.sp
                            ),
                            maxLines = 1
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    // Success Indicator
                    if (file.status == Ux4gUploadStatus.Success) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = "Success",
                            tint = Ux4gColors.green600,
                            modifier = Modifier
                                .size(22.dp)
                                .padding(end = 4.dp)
                        )
                    }

                    // Remove Cross
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Remove File",
                        tint = Ux4gColors.neutral500,
                        modifier = Modifier
                            .size(20.dp)
                            .clickable(onClick = onRemove)
                    )
                }
            }

            // Linear Progress Bar while uploading
            if (file.status == Ux4gUploadStatus.Uploading) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 12.dp, end = 12.dp, bottom = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    LinearProgressIndicator(
                        progress = { file.progress },
                        modifier = Modifier
                            .weight(1f)
                            .height(4.dp)
                            .clip(RoundedCornerShape(4.dp)),
                        color = Ux4gColors.primary,
                        trackColor = Ux4gColors.neutral200
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "${(file.progress * 100).toInt()}%",
                        style = defaultTypography.bS_default.copy(
                            color = Ux4gColors.neutral500,
                            fontSize = 12.sp
                        )
                    )
                }
            }

            // Error Message + Retry Action
            if (file.status == Ux4gUploadStatus.Error) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 12.dp, end = 12.dp, bottom = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.ErrorOutline,
                        contentDescription = "Error",
                        tint = Ux4gColors.red600,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = file.errorMessage ?: "Upload failed",
                        style = defaultTypography.bS_default.copy(
                            color = Ux4gColors.red600,
                            fontSize = 12.sp
                        ),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Retry",
                        style = defaultTypography.bS_strong.copy(
                            color = Ux4gColors.primary,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold
                        ),
                        modifier = Modifier.clickable(onClick = onRetry)
                    )
                }
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Dashed Border Modifier Extension
// ─────────────────────────────────────────────────────────────────────────────

private fun Modifier.dashedBorder(
    color: Color,
    strokeWidth: Dp = 2.dp,
    borderRadius: Dp = 12.dp,
    dashWidth: Float = 16f,
    dashSpace: Float = 10f
): Modifier = this.drawWithContent {
    drawContent()
    val stroke = Stroke(
        width = strokeWidth.toPx(),
        pathEffect = PathEffect.dashPathEffect(floatArrayOf(dashWidth, dashSpace), 0f)
    )
    val r = borderRadius.toPx()
    val path = Path().apply {
        addRoundRect(
            androidx.compose.ui.geometry.RoundRect(
                left = strokeWidth.toPx() / 2,
                top = strokeWidth.toPx() / 2,
                right = size.width - strokeWidth.toPx() / 2,
                bottom = size.height - strokeWidth.toPx() / 2,
                radiusX = r,
                radiusY = r
            )
        )
    }
    drawPath(path = path, color = color, style = stroke)
}

// Helper to query file metadata from ContentResolver
private fun getFileDetailsFromUri(context: Context, uri: Uri): Pair<String, Long> {
    var name = "uploaded_file"
    var size = 0L
    try {
        context.contentResolver.query(uri, null, null, null, null)?.use { cursor ->
            val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            val sizeIndex = cursor.getColumnIndex(OpenableColumns.SIZE)
            if (cursor.moveToFirst()) {
                if (nameIndex != -1) name = cursor.getString(nameIndex) ?: name
                if (sizeIndex != -1) size = cursor.getLong(sizeIndex)
            }
        }
    } catch (_: Exception) {}
    return Pair(name, size)
}

// ═══════════════════════════════════════════════════════
// Previews
// ═══════════════════════════════════════════════════════

@Preview(name = "File Upload - Light", showBackground = true, backgroundColor = 0xFFFFFFFF)
@Preview(name = "File Upload - Dark", uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true, backgroundColor = 0xFF121212)
@Composable
private fun Ux4gFileUploadPreview() {
    val sampleFiles = listOf(
        Ux4gUploadedFile(
            id = "1",
            name = "Income_Certificate_2026.pdf",
            fileSize = 1_450_000,
            status = Ux4gUploadStatus.Success
        ),
        Ux4gUploadedFile(
            id = "2",
            name = "Identity_Proof_Card.png",
            fileSize = 850_000,
            status = Ux4gUploadStatus.Uploading,
            progress = 0.65f
        ),
        Ux4gUploadedFile(
            id = "3",
            name = "Electricity_Bill_March.pdf",
            fileSize = 4_800_000,
            status = Ux4gUploadStatus.Error,
            errorMessage = "Server timeout. Please try again."
        )
    )

    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        Text("Solid Border with Uploaded Files", style = defaultTypography.lS_strong)
        Ux4gFileUpload(
            files = sampleFiles,
            borderStyle = Ux4gFileUploadBorderStyle.Solid
        )

        Text("Dashed Border Style", style = defaultTypography.lS_strong)
        Ux4gFileUpload(
            files = emptyList(),
            borderStyle = Ux4gFileUploadBorderStyle.Dashed
        )
    }
}
