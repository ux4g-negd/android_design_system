package com.ux4g.designsystemv1.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.ux4g.designsystemv1.foundation.Ux4gIcon

/**
 * Backward compatibility alias for [Ux4gResultList].
 * Exact parity with Flutter `typedef Ux4gResultRow = Ux4gResultList;`.
 */
@Composable
fun Ux4gResultRow(
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
    Ux4gResultList(
        title = title,
        modifier = modifier,
        titleTrailing = titleTrailing,
        statusTag = statusTag,
        tagColorScheme = tagColorScheme,
        metadataSegments = metadataSegments,
        customMetadata = customMetadata,
        actionButtonText = actionButtonText,
        actionLeading = actionLeading,
        onActionPressed = onActionPressed,
        actionButtonIcon = actionButtonIcon,
        actionButtonColor = actionButtonColor,
        details = details,
        expandedChild = expandedChild,
        detailsColumns = detailsColumns,
        initialExpanded = initialExpanded,
        onToggle = onToggle,
        showBottomDivider = showBottomDivider,
        contentPadding = contentPadding,
        colors = colors
    )
}
