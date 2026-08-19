package com.ux4g.designsystemv1.components.feedback

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
 * NPS (Net Promoter Score 0-10) Feedback Form matching Flutter's `Ux4gFeedbackFormNps`.
 *
 * @param onSubmit Callback when score (0 to 10) and comment are submitted.
 * @param minWords Minimum word count for comment (default: 0).
 * @param maxLength Maximum comment character length (default: 200).
 * @param onSkip Optional callback when skip is tapped.
 * @param onCloseSuccess Callback when success screen is closed.
 * @param title Form title (default: "How likely are you to recommend us?").
 * @param unlikelyLabel Label below low score scale (default: "0 - Extremely Unlikely").
 * @param likelyLabel Label below high score scale (default: "10 - Extremely Likely").
 * @param commentPlaceholder Text area placeholder text.
 * @param submitButtonText Text on submit button (default: "Submit").
 * @param skipButtonText Text on skip button (default: "Skip").
 * @param successTitle Title on success screen.
 * @param successMessage Message on success screen.
 * @param successIcon Icon on success confirmation (default: [Ux4gIcons.ThumbUp]).
 * @param lowScoreBackgroundColor Background color for scores 0-3 (default: [Ux4gColors.red100]).
 * @param lowScoreTextColor Text color for scores 0-3 (default: [Ux4gColors.red800]).
 * @param mediumScoreBackgroundColor Background color for scores 4-6 (default: [Ux4gColors.orange100]).
 * @param mediumScoreTextColor Text color for scores 4-6 (default: [Ux4gColors.orange800]).
 * @param highScoreBackgroundColor Background color for scores 7-10 (default: [Ux4gColors.green100]).
 * @param highScoreTextColor Text color for scores 7-10 (default: [Ux4gColors.green800]).
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun Ux4gFeedbackFormNps(
    onSubmit: (score: Int, comment: String) -> Unit,
    modifier: Modifier = Modifier,
    minWords: Int = 0,
    maxLength: Int = 200,
    onSkip: (() -> Unit)? = null,
    onCloseSuccess: (() -> Unit)? = null,
    title: String = "How likely are you to recommend us?",
    unlikelyLabel: String = "0 - Extremely Unlikely",
    likelyLabel: String = "10 - Extremely Likely",
    commentPlaceholder: String = "Please tell us why you gave this score",
    submitButtonText: String = "Submit",
    skipButtonText: String = "Skip",
    successTitle: String = "Feedback submitted",
    successMessage: String = "Thank you for your feedback. This helps improve government services.",
    titleStyle: TextStyle? = null,
    successTitleStyle: TextStyle? = null,
    successMessageStyle: TextStyle? = null,
    successIcon: Ux4gIcon = Ux4gIcons.ThumbUp,
    successIconColor: Color? = null,
    successBackgroundColor: Color? = null,
    selectedScoreBackgroundColor: Color? = null,
    selectedScoreTextColor: Color? = null,
    lowScoreBackgroundColor: Color = Ux4gColors.red100,
    lowScoreTextColor: Color = Ux4gColors.red800,
    mediumScoreBackgroundColor: Color = Ux4gColors.orange100,
    mediumScoreTextColor: Color = Ux4gColors.orange800,
    highScoreBackgroundColor: Color = Ux4gColors.green100,
    highScoreTextColor: Color = Ux4gColors.green800
) {
    var selectedScore by remember { mutableStateOf<Int?>(null) } // 0 to 10
    var comment by remember { mutableStateOf("") }
    var isSubmitted by remember { mutableStateOf(false) }

    fun getWordCount(text: String): Int {
        val trimmed = text.trim()
        if (trimmed.isEmpty()) return 0
        return trimmed.split(Regex("\\s+")).size
    }

    val isFormValid = selectedScore != null &&
            (minWords <= 0 || getWordCount(comment) >= minWords)

    fun handleSubmit() {
        val score = selectedScore ?: return
        if (isFormValid) {
            onSubmit(score, comment)
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
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth(),
                    style = titleStyle ?: defaultTypography.hM_strong.copy(
                        color = onSurfaceColor,
                        fontWeight = FontWeight.W700
                    )
                )

                // NPS Scale 0-10 Buttons
                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    for (index in 0..10) {
                        val isHighlighted = selectedScore != null && index <= selectedScore!!

                        val (scoreBg, scoreTextColor) = when {
                            selectedScoreBackgroundColor != null && selectedScoreTextColor != null -> {
                                selectedScoreBackgroundColor to selectedScoreTextColor
                            }
                            selectedScore != null && selectedScore!! <= 3 -> {
                                lowScoreBackgroundColor to lowScoreTextColor
                            }
                            selectedScore != null && selectedScore!! <= 6 -> {
                                mediumScoreBackgroundColor to mediumScoreTextColor
                            }
                            else -> {
                                highScoreBackgroundColor to highScoreTextColor
                            }
                        }

                        Box(
                            modifier = Modifier
                                .padding(horizontal = 2.dp)
                                .size(28.dp)
                                .clip(RoundedCornerShape(4.dp))
                                .background(if (isHighlighted) scoreBg else surfaceColor)
                                .then(
                                    if (!isHighlighted) {
                                        Modifier.border(1.dp, onSurfaceColor.copy(alpha = 0.15f), RoundedCornerShape(4.dp))
                                    } else Modifier
                                )
                                .clickable {
                                    selectedScore = index
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "$index",
                                style = defaultTypography.bM_strong.copy(
                                    color = if (isHighlighted) scoreTextColor else onSurfaceColor,
                                    fontWeight = FontWeight.W700
                                )
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
                        text = unlikelyLabel,
                        style = defaultTypography.bXS_default.copy(
                            color = onSurfaceColor.copy(alpha = 0.6f)
                        )
                    )
                    Text(
                        text = likelyLabel,
                        style = defaultTypography.bXS_default.copy(
                            color = onSurfaceColor.copy(alpha = 0.6f)
                        )
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
private fun Ux4gFeedbackFormNpsPreview() {
    Ux4gTheme {
        Box(modifier = Modifier.padding(16.dp)) {
            Ux4gFeedbackFormNps(
                onSubmit = { _, _ -> },
                onSkip = {}
            )
        }
    }
}
