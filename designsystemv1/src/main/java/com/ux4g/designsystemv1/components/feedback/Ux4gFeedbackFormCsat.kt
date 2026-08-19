package com.ux4g.designsystemv1.components.feedback

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ux4g.designsystemv1.components.buttons.Ux4gButton
import com.ux4g.designsystemv1.components.buttons.Ux4gButtonVariant
import com.ux4g.designsystemv1.components.textarea.Ux4gTextArea
import com.ux4g.designsystemv1.components.textarea.Ux4gTextAreaMinHeight
import com.ux4g.designsystemv1.foundation.Ux4gColors
import com.ux4g.designsystemv1.foundation.Ux4gIcon
import com.ux4g.designsystemv1.foundation.Ux4gIconComponent
import com.ux4g.designsystemv1.foundation.Ux4gIcons
import com.ux4g.designsystemv1.foundation.defaultTypography
import com.ux4g.designsystemv1.theme.Ux4gTheme

/**
 * CSAT Face Rating Feedback Form matching Flutter's `Ux4gFeedbackFormCsat`.
 *
 * @param onSubmit Callback when score (1 to 5) and comment are submitted.
 * @param minWords Minimum required word count for comment (default: 0).
 * @param maxLength Maximum comment character length (default: 200).
 * @param onSkip Optional callback when skip is tapped.
 * @param onCloseSuccess Callback when success screen is closed.
 * @param title Form title (default: "How do you feel about this service?").
 * @param badLabel Left side label under face ratings (default: "← Bad").
 * @param goodLabel Right side label under face ratings (default: "Good →").
 * @param commentPlaceholder Text area placeholder text.
 * @param submitButtonText Text on submit button (default: "Submit").
 * @param skipButtonText Text on skip button (default: "Skip").
 * @param successTitle Title on success screen.
 * @param successMessage Message on success screen.
 * @param ratingFaces List of 5 face icons from saddest to happiest.
 * @param successIcon Icon on success confirmation (default: [Ux4gIcons.ThumbUp]).
 * @param selectedBackgroundColors Background colors for selected faces (0 to 4).
 * @param selectedIconColors Icon tint colors for selected faces (0 to 4).
 * @param unselectedBackgroundColor Background for unselected faces.
 * @param unselectedIconColor Icon tint for unselected faces.
 */
@Composable
fun Ux4gFeedbackFormCsat(
    onSubmit: (score: Int, comment: String) -> Unit,
    modifier: Modifier = Modifier,
    minWords: Int = 0,
    maxLength: Int = 200,
    onSkip: (() -> Unit)? = null,
    onCloseSuccess: (() -> Unit)? = null,
    title: String = "How do you feel about this service?",
    badLabel: String = "← Bad",
    goodLabel: String = "Good →",
    commentPlaceholder: String = "Please tell us how can we improve",
    submitButtonText: String = "Submit",
    skipButtonText: String = "Skip",
    successTitle: String = "Feedback submitted",
    successMessage: String = "Thank you for your feedback. This helps improve government services.",
    ratingFaces: List<Ux4gIcon> = listOf(
        Ux4gIcons.ic_face_0,
        Ux4gIcons.ic_face_1,
        Ux4gIcons.ic_face_2,
        Ux4gIcons.ic_face_3,
        Ux4gIcons.ic_face_4
    ),
    titleStyle: TextStyle? = null,
    successTitleStyle: TextStyle? = null,
    successMessageStyle: TextStyle? = null,
    successIcon: Ux4gIcon = Ux4gIcons.ThumbUp,
    successIconColor: Color? = null,
    successBackgroundColor: Color? = null,
    selectedBackgroundColors: List<Color> = listOf(
        Ux4gColors.red100,
        Ux4gColors.red50,
        Ux4gColors.orange50,
        Ux4gColors.green50,
        Ux4gColors.green100
    ),
    selectedIconColors: List<Color> = listOf(
        Ux4gColors.red600,
        Ux4gColors.red600,
        Ux4gColors.orange600,
        Ux4gColors.green600,
        Ux4gColors.green600
    ),
    unselectedBackgroundColor: Color = Ux4gColors.neutral100,
    unselectedIconColor: Color = Ux4gColors.neutral500
) {
    var selectedScore by remember { mutableStateOf<Int?>(null) } // 0 to 4
    var comment by remember { mutableStateOf("") }
    var isSubmitted by remember { mutableStateOf(false) }

    fun getWordCount(text: String): Int {
        val trimmed = text.trim()
        if (trimmed.isEmpty()) return 0
        return trimmed.split(Regex("\\s+")).size
    }

    val isFormValid = selectedScore != null &&
            comment.trim().isNotEmpty() &&
            (minWords <= 0 || getWordCount(comment) >= minWords)

    fun handleSubmit() {
        val score = selectedScore ?: return
        if (isFormValid) {
            onSubmit(score + 1, comment)
            isSubmitted = true
        }
    }

    val surfaceColor = Ux4gColors.white
    val onSurfaceColor = Ux4gColors.neutral900

    Box(
        modifier = modifier
            .fillMaxWidth()
            .widthIn(max = 400.dp)
            .shadow(elevation = 4.dp, shape = RoundedCornerShape(12.dp), spotColor = Color.Black.copy(alpha = 0.05f))
            .clip(RoundedCornerShape(12.dp))
            .background(surfaceColor)
            .border(1.dp, onSurfaceColor.copy(alpha = 0.1f), RoundedCornerShape(12.dp))
            .padding(24.dp)
    ) {
        if (isSubmitted) {
            // ── Success View ─────────────────────────────────────────────
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(successBackgroundColor ?: Ux4gColors.green600.copy(alpha = 0.12f))
                        .padding(horizontal = 24.dp, vertical = 32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Ux4gIconComponent(
                            icon = successIcon,
                            contentDescription = "Success",
                            tint = successIconColor ?: Ux4gColors.green600,
                            modifier = Modifier.size(32.dp)
                        )
                        Text(
                            text = successTitle,
                            textAlign = TextAlign.Center,
                            style = successTitleStyle ?: defaultTypography.hL_strong.copy(
                                color = onSurfaceColor,
                                fontWeight = FontWeight.W700
                            )
                        )
                        Text(
                            text = successMessage,
                            textAlign = TextAlign.Center,
                            style = successMessageStyle ?: defaultTypography.bM_default.copy(
                                color = onSurfaceColor.copy(alpha = 0.7f)
                            )
                        )
                    }
                }

                Ux4gButton(
                    text = "Close",
                    variant = Ux4gButtonVariant.Outline,
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        if (onCloseSuccess != null) {
                            onCloseSuccess()
                        } else {
                            selectedScore = null
                            comment = ""
                            isSubmitted = false
                        }
                    }
                )
            }
        } else {
            // ── Form View ────────────────────────────────────────────────
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Title
                Text(
                    text = title,
                    style = titleStyle ?: defaultTypography.hM_strong.copy(
                        color = onSurfaceColor,
                        fontWeight = FontWeight.W700
                    )
                )

                // Face Rating Scale
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    for (index in 0 until 5) {
                        val isSelected = selectedScore == index
                        val bgColor = if (isSelected) {
                            if (index < selectedBackgroundColors.size) selectedBackgroundColors[index] else unselectedBackgroundColor
                        } else {
                            unselectedBackgroundColor
                        }

                        val iconColor = if (isSelected) {
                            if (index < selectedIconColors.size) selectedIconColors[index] else unselectedIconColor
                        } else {
                            unselectedIconColor
                        }

                        val faceIcon = if (index < ratingFaces.size) ratingFaces[index] else Ux4gIcons.ic_face_2

                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .padding(horizontal = 2.dp)
                                .aspectRatio(1f)
                                .clip(RoundedCornerShape(8.dp))
                                .background(bgColor)
                                .clickable {
                                    selectedScore = index
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Ux4gIconComponent(
                                icon = faceIcon,
                                contentDescription = "Rating face $index",
                                tint = iconColor,
                                modifier = Modifier.size(28.dp)
                            )
                        }
                    }
                }

                // Scale Labels
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = badLabel,
                        style = defaultTypography.bS_default.copy(
                            color = onSurfaceColor.copy(alpha = 0.6f)
                        ),
                        modifier = Modifier.padding(start = 12.dp)
                    )
                    Text(
                        text = goodLabel,
                        style = defaultTypography.bS_default.copy(
                            color = onSurfaceColor.copy(alpha = 0.6f)
                        ),
                        modifier = Modifier.padding(end = 12.dp)
                    )
                }

                // Conditional Comment Text Area
                if (selectedScore != null) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Ux4gTextArea(
                        value = comment,
                        onValueChange = { comment = it },
                        placeholder = commentPlaceholder,
                        minHeight = Ux4gTextAreaMinHeight.Large,
                        maxLength = maxLength,
                        characterCountText = "${comment.length}/$maxLength",
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Submit Button
                Ux4gButton(
                    text = submitButtonText,
                    variant = Ux4gButtonVariant.Primary,
                    enabled = isFormValid,
                    modifier = Modifier.fillMaxWidth(),
                    onClick = { handleSubmit() }
                )

                // Skip Button
                if (onSkip != null) {
                    TextButton(
                        onClick = onSkip,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = skipButtonText,
                            style = defaultTypography.bM_strong.copy(color = onSurfaceColor)
                        )
                    }
                }
            }
        }
    }
}

@Preview(name = "Light", showBackground = true)
@Preview(name = "Dark", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun Ux4gFeedbackFormCsatPreview() {
    Ux4gTheme {
        Box(modifier = Modifier.padding(16.dp)) {
            Ux4gFeedbackFormCsat(
                onSubmit = { _, _ -> },
                onSkip = {}
            )
        }
    }
}
