package com.ux4g.designsystemv1.components.feedback

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ux4g.designsystemv1.components.Ux4gChoiceChip
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
 * Backward compatibility alias for [Ux4gFeedbackFormStar].
 */
@Deprecated(
    message = "Use Ux4gFeedbackFormStar instead",
    replaceWith = ReplaceWith("Ux4gFeedbackFormStar(improvementOptions, onSubmit, minWords, maxLength, onSkip, onCloseSuccess, chipBorderRadius, title, improvementTitle, commentPlaceholder, submitButtonText, skipButtonText, successTitle, successMessage, titleStyle, improvementTitleStyle, successTitleStyle, successMessageStyle, ratingIcon, successIcon, activeRatingColor, lowRatingColor, highRatingColor, lowRatingThreshold, inactiveRatingColor, successIconColor, successBackgroundColor, modifier)")
)
@Composable
fun Ux4gFeedbackForm(
    improvementOptions: List<String>,
    onSubmit: (rating: Int, selectedChips: List<String>, comment: String) -> Unit,
    minWords: Int = 0,
    maxLength: Int = 200,
    onSkip: (() -> Unit)? = null,
    onCloseSuccess: (() -> Unit)? = null,
    chipBorderRadius: Dp = 4.dp,
    title: String = "Rate your experience",
    improvementTitle: String = "What can we improve?",
    commentPlaceholder: String = "Tell us more about your experience",
    submitButtonText: String = "Submit",
    skipButtonText: String = "Skip",
    successTitle: String = "Feedback submitted",
    successMessage: String = "Thank you for your feedback. This helps improve government services.",
    titleStyle: TextStyle? = null,
    improvementTitleStyle: TextStyle? = null,
    successTitleStyle: TextStyle? = null,
    successMessageStyle: TextStyle? = null,
    ratingIcon: Ux4gIcon = Ux4gIcons.Star,
    successIcon: Ux4gIcon = Ux4gIcons.ThumbUp,
    activeRatingColor: Color? = null,
    lowRatingColor: Color = Ux4gColors.red600,
    highRatingColor: Color = Ux4gColors.yellow600,
    lowRatingThreshold: Int = 2,
    inactiveRatingColor: Color? = null,
    successIconColor: Color? = null,
    successBackgroundColor: Color? = null,
    modifier: Modifier = Modifier
) {
    Ux4gFeedbackFormStar(
        improvementOptions = improvementOptions,
        onSubmit = onSubmit,
        minWords = minWords,
        maxLength = maxLength,
        onSkip = onSkip,
        onCloseSuccess = onCloseSuccess,
        chipBorderRadius = chipBorderRadius,
        title = title,
        improvementTitle = improvementTitle,
        commentPlaceholder = commentPlaceholder,
        submitButtonText = submitButtonText,
        skipButtonText = skipButtonText,
        successTitle = successTitle,
        successMessage = successMessage,
        titleStyle = titleStyle,
        improvementTitleStyle = improvementTitleStyle,
        successTitleStyle = successTitleStyle,
        successMessageStyle = successMessageStyle,
        ratingIcon = ratingIcon,
        successIcon = successIcon,
        activeRatingColor = activeRatingColor,
        lowRatingColor = lowRatingColor,
        highRatingColor = highRatingColor,
        lowRatingThreshold = lowRatingThreshold,
        inactiveRatingColor = inactiveRatingColor,
        successIconColor = successIconColor,
        successBackgroundColor = successBackgroundColor,
        modifier = modifier
    )
}

/**
 * Star Rating Feedback Form Component matching Flutter's `Ux4gFeedbackFormStar`.
 *
 * @param improvementOptions List of tag options for what can be improved.
 * @param onSubmit Callback when feedback is successfully submitted.
 * @param minWords Minimum word count required in comment (default: 0).
 * @param maxLength Maximum characters allowed in comment (default: 200).
 * @param onSkip Optional callback when skip is clicked.
 * @param onCloseSuccess Callback when success screen is closed.
 * @param chipBorderRadius Corner radius of improvement chips (default: 4.dp).
 * @param title Form title heading (default: "Rate your experience").
 * @param improvementTitle Section heading for chips (default: "What can we improve?").
 * @param commentPlaceholder Text area placeholder text.
 * @param submitButtonText Text on submit button (default: "Submit").
 * @param skipButtonText Text on skip button (default: "Skip").
 * @param successTitle Title on success confirmation screen.
 * @param successMessage Message on success confirmation screen.
 * @param ratingIcon Icon used for rating stars (default: [Ux4gIcons.Star]).
 * @param successIcon Icon used on success screen (default: [Ux4gIcons.ThumbUp]).
 * @param lowRatingColor Color for stars <= [lowRatingThreshold] (default: [Ux4gColors.red600]).
 * @param highRatingColor Color for stars > [lowRatingThreshold] (default: [Ux4gColors.yellow600]).
 * @param lowRatingThreshold Threshold for low vs high rating colors (default: 2).
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun Ux4gFeedbackFormStar(
    improvementOptions: List<String>,
    onSubmit: (rating: Int, selectedChips: List<String>, comment: String) -> Unit,
    minWords: Int = 0,
    maxLength: Int = 200,
    onSkip: (() -> Unit)? = null,
    onCloseSuccess: (() -> Unit)? = null,
    chipBorderRadius: Dp = 4.dp,
    title: String = "Rate your experience",
    improvementTitle: String = "What can we improve?",
    commentPlaceholder: String = "Tell us more about your experience",
    submitButtonText: String = "Submit",
    skipButtonText: String = "Skip",
    successTitle: String = "Feedback submitted",
    successMessage: String = "Thank you for your feedback. This helps improve government services.",
    titleStyle: TextStyle? = null,
    improvementTitleStyle: TextStyle? = null,
    successTitleStyle: TextStyle? = null,
    successMessageStyle: TextStyle? = null,
    ratingIcon: Ux4gIcon = Ux4gIcons.Star,
    successIcon: Ux4gIcon = Ux4gIcons.ThumbUp,
    activeRatingColor: Color? = null,
    lowRatingColor: Color = Ux4gColors.red600,
    highRatingColor: Color = Ux4gColors.yellow600,
    lowRatingThreshold: Int = 2,
    inactiveRatingColor: Color? = null,
    successIconColor: Color? = null,
    successBackgroundColor: Color? = null,
    modifier: Modifier = Modifier
) {
    var rating by remember { mutableIntStateOf(0) }
    val selectedChips = remember { mutableStateOf(setOf<String>()) }
    var comment by remember { mutableStateOf("") }
    var isSubmitted by remember { mutableStateOf(false) }

    fun getWordCount(text: String): Int {
        val trimmed = text.trim()
        if (trimmed.isEmpty()) return 0
        return trimmed.split(Regex("\\s+")).size
    }

    val isFormValid = rating > 0 &&
            selectedChips.value.isNotEmpty() &&
            comment.trim().isNotEmpty() &&
            (minWords <= 0 || getWordCount(comment) >= minWords)

    fun handleSubmit() {
        if (isFormValid) {
            onSubmit(rating, selectedChips.value.toList(), comment)
            isSubmitted = true
        }
    }

    val surfaceColor = Ux4gColors.white
    val onSurfaceColor = Ux4gColors.neutral900
    val primaryColor = Ux4gColors.primary

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
                            rating = 0
                            selectedChips.value = emptySet()
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
                    style = titleStyle ?: defaultTypography.hL_strong.copy(
                        color = onSurfaceColor,
                        fontWeight = FontWeight.W700
                    )
                )

                // Star Rating Row
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    for (index in 1..5) {
                        val isSelected = index <= rating
                        val starColor = if (isSelected) {
                            activeRatingColor ?: if (rating <= lowRatingThreshold) lowRatingColor else highRatingColor
                        } else {
                            inactiveRatingColor ?: onSurfaceColor.copy(alpha = 0.15f)
                        }

                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clickable(
                                    interactionSource = remember { MutableInteractionSource() },
                                    indication = null
                                ) {
                                    rating = index
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Ux4gIconComponent(
                                icon = ratingIcon,
                                contentDescription = "Rate $index stars",
                                tint = starColor,
                                modifier = Modifier.size(32.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Improvement Section
                Text(
                    text = improvementTitle,
                    style = improvementTitleStyle ?: defaultTypography.bM_strong.copy(
                        color = onSurfaceColor,
                        fontWeight = FontWeight.W700
                    )
                )

                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    improvementOptions.forEach { option ->
                        val isChipSelected = selectedChips.value.contains(option)
                        Ux4gChoiceChip(
                            text = option,
                            selected = isChipSelected,
                            borderRadius = chipBorderRadius,
                            onClick = {
                                selectedChips.value = if (isChipSelected) {
                                    selectedChips.value - option
                                } else {
                                    selectedChips.value + option
                                }
                            },
                            selectedBackgroundColor = primaryColor.copy(alpha = 0.1f),
                            selectedBorderColor = primaryColor.copy(alpha = 0.3f),
                            selectedTextColor = primaryColor
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Comment Text Area
                Ux4gTextArea(
                    value = comment,
                    onValueChange = { comment = it },
                    placeholder = commentPlaceholder,
                    minHeight = Ux4gTextAreaMinHeight.Large,
                    maxLength = maxLength,
                    characterCountText = "${comment.length}/$maxLength",
                    modifier = Modifier.fillMaxWidth()
                )

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
private fun Ux4gFeedbackFormStarPreview() {
    Ux4gTheme {
        Box(modifier = Modifier.padding(16.dp)) {
            Ux4gFeedbackFormStar(
                improvementOptions = listOf(
                    "Content accuracy",
                    "Visual design",
                    "Performance",
                    "Navigation"
                ),
                onSubmit = { _, _, _ -> },
                onSkip = {}
            )
        }
    }
}
