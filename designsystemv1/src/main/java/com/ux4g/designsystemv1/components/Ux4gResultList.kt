package com.ux4g.designsystemv1.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.ux4g.designsystemv1.components.buttons.Ux4gButton
import com.ux4g.designsystemv1.components.buttons.Ux4gButtonSize
import com.ux4g.designsystemv1.components.buttons.Ux4gButtonVariant
import com.ux4g.designsystemv1.foundation.Ux4gColors
import com.ux4g.designsystemv1.foundation.Ux4gIcon
import com.ux4g.designsystemv1.foundation.Ux4gIconComponent
import com.ux4g.designsystemv1.foundation.Ux4gIcons
import com.ux4g.designsystemv1.foundation.defaultTypography

/**
 * Data model for detailed information items in the [Ux4gResultList].
 * Exact parity with Flutter `Ux4gResultDetail`.
 *
 * @param label The metadata descriptor title (e.g. "Application No", "Status", "Date").
 * @param value The value string to display.
 * @param icon Optional leading icon preceding the value text.
 * @param valueColor Optional text and icon color override.
 * @param isBold Whether the value is rendered in bold typography.
 * @param customValueWidget Optional composable replacement for custom value content.
 */
data class Ux4gResultDetail(
    val label: String,
    val value: String,
    val icon: Ux4gIcon? = null,
    val valueColor: Color? = null,
    val isBold: Boolean = false,
    val customValueWidget: (@Composable () -> Unit)? = null
)

/**
 * Colors configuration for [Ux4gResultList].
 */
@Immutable
data class Ux4gResultListColors(
    val titleColor: Color,
    val onSurface: Color,
    val surface: Color,
    val dividerColor: Color,
    val labelColor: Color,
    val valueColor: Color,
    val expandIconColor: Color
)

object Ux4gResultListDefaults {
    @Composable
    fun colors(
        titleColor: Color = Ux4gColors.neutral900,
        onSurface: Color = Ux4gColors.neutral900,
        surface: Color = Ux4gColors.white,
        dividerColor: Color = Ux4gColors.neutral900.copy(alpha = 0.1f),
        labelColor: Color = Ux4gColors.neutral900.copy(alpha = 0.5f),
        valueColor: Color = Ux4gColors.neutral900,
        expandIconColor: Color = Ux4gColors.neutral900.copy(alpha = 0.8f)
    ): Ux4gResultListColors = Ux4gResultListColors(
        titleColor = titleColor,
        onSurface = onSurface,
        surface = surface,
        dividerColor = dividerColor,
        labelColor = labelColor,
        valueColor = valueColor,
        expandIconColor = expandIconColor
    )
}

/**
 * A configurable expandable list item component for search results and application status.
 * Exact parity with Flutter `Ux4gResultList`.
 *
 * @param title Primary title of the result item.
 * @param modifier Modifier applied to the outer container.
 * @param titleTrailing Optional composable displayed immediately after the title.
 * @param statusTag Optional status text displayed inside a rectangular tonal [Ux4gTag].
 * @param tagColorScheme Color theme for the status tag (default [Ux4gTagColor.Neutral]).
 * @param metadataSegments List of pill segments displayed inside a unified pill tag.
 * @param customMetadata Optional composable displayed instead of metadata segments.
 * @param actionButtonText Label for the outline action button on the right.
 * @param actionLeading Optional composable displayed right before the action button (e.g. an alert icon).
 * @param onActionPressed Callback triggered when the action button is clicked.
 * @param actionButtonIcon Optional icon displayed inside the action button.
 * @param actionButtonColor Optional color override for the action button's border and content.
 * @param details List of [Ux4gResultDetail] items shown when expanded.
 * @param expandedChild Optional custom composable rendered below the details grid when expanded.
 * @param detailsColumns Number of columns to lay out the details in (default 2).
 * @param initialExpanded Whether the item is initially expanded (default false).
 * @param onToggle Callback triggered when expand state changes.
 * @param showBottomDivider Whether to render a horizontal divider below the component (default true).
 * @param contentPadding Padding applied around the header row and expanded content (default 16.dp).
 * @param colors Color palette configuration.
 */
@Composable
fun Ux4gResultList(
    title: String,
    modifier: Modifier = Modifier,
    titleTrailing: (@Composable () -> Unit)? = null,
    statusTag: String? = null,
    tagColorScheme: Ux4gTagColor = Ux4gTagColor.Neutral,
    metadataSegments: List<Ux4gPillSegment>? = null,
    customMetadata: (@Composable () -> Unit)? = null,
    actionButtonText: String? = null,
    actionLeading: (@Composable () -> Unit)? = null,
    onActionPressed: (() -> Unit)? = null,
    actionButtonIcon: Ux4gIcon? = null,
    actionButtonColor: Color? = null,
    details: List<Ux4gResultDetail> = emptyList(),
    expandedChild: (@Composable () -> Unit)? = null,
    detailsColumns: Int = 2,
    initialExpanded: Boolean = false,
    onToggle: ((Boolean) -> Unit)? = null,
    showBottomDivider: Boolean = true,
    contentPadding: PaddingValues = PaddingValues(16.dp),
    colors: Ux4gResultListColors = Ux4gResultListDefaults.colors()
) {
    var isExpanded by remember { mutableStateOf(initialExpanded) }

    fun handleToggle() {
        isExpanded = !isExpanded
        onToggle?.invoke(isExpanded)
    }

    val rotationAngle by animateFloatAsState(
        targetValue = if (isExpanded) 180f else 0f,
        animationSpec = tween(200),
        label = "expand_icon_rotation"
    )

    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        // Top Header Row (Tappable to toggle)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { handleToggle() }
                .padding(contentPadding),
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Left Content: Title, Status Tag, Metadata
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp)
            ) {
                // Title Row
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = title,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        style = defaultTypography.bL_strong.copy(
                            fontWeight = FontWeight.SemiBold,
                            color = colors.titleColor
                        ),
                        modifier = Modifier.weight(1f, fill = false)
                    )
                    if (titleTrailing != null) {
                        Spacer(modifier = Modifier.width(4.dp))
                        titleTrailing()
                    }
                }

                // Tags & Metadata Row
                val hasStatusTag = statusTag != null
                val hasMetadataSegments = !metadataSegments.isNullOrEmpty()
                val hasCustomMetadata = customMetadata != null

                if (hasStatusTag || hasMetadataSegments || hasCustomMetadata) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        if (statusTag != null) {
                            Ux4gTag(
                                text = statusTag,
                                colorScheme = tagColorScheme,
                                shape = Ux4gTagShape.Rectangular,
                                style = Ux4gTagStyle.Tonal
                            )
                        }

                        if (customMetadata != null) {
                            customMetadata()
                        } else if (hasMetadataSegments) {
                            Ux4gUnifiedPillTag(
                                segments = metadataSegments!!,
                                size = Ux4gTagSize.L
                            )
                        }
                    }
                }
            }

            // Right Content: Action Leading, Action Button, Expand Arrow
            Row(
                modifier = Modifier.wrapContentWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                if (actionLeading != null) {
                    actionLeading()
                }

                if (actionButtonText != null) {
                    Ux4gButton(
                        text = actionButtonText,
                        variant = Ux4gButtonVariant.Outline,
                        size = Ux4gButtonSize.Small,
                        contentColor = actionButtonColor ?: colors.onSurface,
                        borderColor = actionButtonColor ?: colors.onSurface.copy(alpha = 0.2f),
                        leadingIcon = if (actionButtonIcon != null) {
                            {
                                Ux4gIconComponent(
                                    icon = actionButtonIcon,
                                    contentDescription = null,
                                    tint = actionButtonColor ?: colors.onSurface,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        } else null,
                        onClick = { onActionPressed?.invoke() }
                    )
                }

                Icon(
                    imageVector = Icons.Filled.KeyboardArrowDown,
                    contentDescription = if (isExpanded) "Collapse" else "Expand",
                    tint = colors.expandIconColor,
                    modifier = Modifier
                        .size(24.dp)
                        .rotate(rotationAngle)
                )
            }
        }

        // Expanded Details Section
        AnimatedVisibility(
            visible = isExpanded,
            enter = expandVertically(animationSpec = tween(200)) + fadeIn(animationSpec = tween(200)),
            exit = shrinkVertically(animationSpec = tween(200)) + fadeOut(animationSpec = tween(200))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = 16.dp,
                        end = 16.dp,
                        bottom = 24.dp
                    )
            ) {
                if (details.isNotEmpty()) {
                    val chunkedDetails = details.chunked(detailsColumns.coerceAtLeast(1))

                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(20.dp)
                    ) {
                        for (chunk in chunkedDetails) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(16.dp)
                            ) {
                                for (detail in chunk) {
                                    Box(modifier = Modifier.weight(1f)) {
                                        ResultDetailItem(
                                            detail = detail,
                                            colors = colors
                                        )
                                    }
                                }

                                if (chunk.size < detailsColumns) {
                                    for (k in 0 until (detailsColumns - chunk.size)) {
                                        Spacer(modifier = Modifier.weight(1f))
                                    }
                                }
                            }
                        }
                    }
                }

                if (expandedChild != null) {
                    if (details.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                    expandedChild()
                }
            }
        }

        // Bottom Divider
        if (showBottomDivider) {
            androidx.compose.material3.HorizontalDivider(
                thickness = 1.dp,
                color = colors.dividerColor
            )
        }
    }
}

/**
 * Data detail cell rendering label and value / custom widget.
 */
@Composable
private fun ResultDetailItem(
    detail: Ux4gResultDetail,
    colors: Ux4gResultListColors
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.Start
    ) {
        // Label
        Text(
            text = detail.label,
            style = defaultTypography.bS_default.copy(
                color = colors.labelColor
            )
        )

        Spacer(modifier = Modifier.height(4.dp))

        // Value
        if (detail.customValueWidget != null) {
            detail.customValueWidget.invoke()
        } else {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (detail.icon != null) {
                    Ux4gIconComponent(
                        icon = detail.icon,
                        contentDescription = null,
                        tint = detail.valueColor ?: colors.valueColor,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                }

                Text(
                    text = detail.value,
                    style = if (detail.isBold) {
                        defaultTypography.bM_strong.copy(
                            fontWeight = FontWeight.Bold,
                            color = detail.valueColor ?: colors.valueColor
                        )
                    } else {
                        defaultTypography.bM_default.copy(
                            color = detail.valueColor ?: colors.valueColor
                        )
                    }
                )
            }
        }
    }
}
