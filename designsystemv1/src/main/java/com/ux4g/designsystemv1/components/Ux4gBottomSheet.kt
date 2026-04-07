package com.ux4g.designsystemv1.components

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ux4g.designsystemv1.components.buttons.Ux4gButton
import com.ux4g.designsystemv1.components.buttons.Ux4gOutlineButton
import com.ux4g.designsystemv1.foundation.*
import com.ux4g.designsystemv1.theme.Ux4gTheme

// ─────────────────────────────────────────────────────────────────────────────
// Enums
// ─────────────────────────────────────────────────────────────────────────────

/** Horizontal alignment of bottom sheet footer buttons. */
enum class Ux4gBottomSheetFooterAlignment {
    Left,
    Centered,
    Right,
    /** Both buttons take equal width (50% each) and fill the container. */
    Justified
}

// ─────────────────────────────────────────────────────────────────────────────
// Main Composable
// ─────────────────────────────────────────────────────────────────────────────

/**
 * A highly configurable Bottom Sheet component following Ux4G Design System specs.
 *
 * @param onDismissRequest Called when the sheet is dismissed (scrim tap, swipe down, or close button).
 * @param modifier         Root modifier for the sheet container.
 * @param sheetState       State of the modal bottom sheet.
 * @param isDraggable      Whether the sheet can be dragged/swiped. If false, the drag handle is hidden.
 * @param headerTitle      Main title shown in the header.
 * @param headerSubtleText Optional small text shown on the right side of the title.
 * @param headerIcon       Optional icon shown to the left of the title.
 * @param description      Secondary text shown below the header title.
 * @param showCloseButton  Whether to show a 'Close' icon button in the top-right.
 * @param showDividers     Whether to show horizontal dividers between header, body, and footer.
 * @param footerType       Which buttons to show in the bottom footer row.
 * @param footerAlignment  Alignment and width-filling behavior of footer buttons.
 * @param primaryButtonText Label for the filled primary button.
 * @param secondaryButtonText Label for the outline secondary button.
 * @param onPrimaryClick   Callback for primary button.
 * @param onSecondaryClick Callback for secondary button.
 * @param containerColor   Background color of the sheet.
 * @param scrimColor       Color of the scrim that obscures content behind the sheet.
 * @param content          The main body content of the bottom sheet.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Ux4gBottomSheet(
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    sheetState: SheetState = rememberModalBottomSheetState(
        confirmValueChange = { true } // We can override this if strictly needed to block gestures
    ),
    isDraggable: Boolean = true,
    // ── Header ──────────────────────────────────────────────────────────────
    headerTitle: String? = null,
    headerSubtleText: String? = null,
    headerIcon: Ux4gIcon? = null,
    showCloseButton: Boolean = true,
    // ── Body ────────────────────────────────────────────────────────────────
    description: String? = null,
    content: @Composable ColumnScope.() -> Unit,
    // ── Footer ──────────────────────────────────────────────────────────────
    footerType: Ux4gCardFooterType = Ux4gCardFooterType.None,
    footerAlignment: Ux4gBottomSheetFooterAlignment = Ux4gBottomSheetFooterAlignment.Left,
    primaryButtonText: String = "Confirm",
    secondaryButtonText: String = "Cancel",
    onPrimaryClick: () -> Unit = {},
    onSecondaryClick: () -> Unit = {},
    // ── Appearance ────────────────────────────────────────────────────────────
    showDividers: Boolean = true,
    containerColor: Color = Ux4gTheme.colorScheme.surface,
    scrimColor: Color = BottomSheetDefaults.ScrimColor,
) {
    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        modifier = modifier,
        sheetState = sheetState,
        containerColor = containerColor,
        scrimColor = scrimColor,
        dragHandle = if (isDraggable) { { BottomSheetDefaults.DragHandle() } } else null
    ) {
        Ux4gBottomSheetLayout(
            headerTitle = headerTitle,
            headerSubtleText = headerSubtleText,
            headerIcon = headerIcon,
            showCloseButton = showCloseButton,
            description = description,
            showDividers = showDividers,
            footerType = footerType,
            footerAlignment = footerAlignment,
            primaryButtonText = primaryButtonText,
            secondaryButtonText = secondaryButtonText,
            onPrimaryClick = onPrimaryClick,
            onSecondaryClick = onSecondaryClick,
            onCloseClick = onDismissRequest,
            content = content
        )
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// pure content layout (useful for previews)
// ─────────────────────────────────────────────────────────────────────────────

@Composable
fun Ux4gBottomSheetLayout(
    headerTitle: String? = null,
    headerSubtleText: String? = null,
    headerIcon: Ux4gIcon? = null,
    showCloseButton: Boolean = true,
    description: String? = null,
    showDividers: Boolean = true,
    footerType: Ux4gCardFooterType = Ux4gCardFooterType.None,
    footerAlignment: Ux4gBottomSheetFooterAlignment = Ux4gBottomSheetFooterAlignment.Left,
    primaryButtonText: String = "Confirm",
    secondaryButtonText: String = "Cancel",
    onPrimaryClick: () -> Unit = {},
    onSecondaryClick: () -> Unit = {},
    onCloseClick: () -> Unit = {},
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .navigationBarsPadding() // Ensures content doesn't sit under system nav bar
    ) {
        // ── Header Row ──────────────────────────────────────────────────────
        val hasHeader = headerTitle != null || headerSubtleText != null || headerIcon != null || showCloseButton
        if (hasHeader) {
            Column(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = Ux4gSpace.Space16, vertical = Ux4gSpace.Space12),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Leading Icon
                    if (headerIcon != null) {
                        Ux4gIconComponent(
                            icon = headerIcon,
                            contentDescription = null,
                            modifier = Modifier.size(24.dp),
                            tint = Ux4gTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.width(Ux4gSpace.Space12))
                    }

                    // Title
                    Column(modifier = Modifier.weight(1f)) {
                        if (headerTitle != null) {
                            Text(
                                text = headerTitle,
                                style = Ux4gTheme.typography.tS_strong,
                                color = Ux4gTheme.colorScheme.onSurface,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }

                    // Subtle Text (right-aligned)
                    if (headerSubtleText != null) {
                        Text(
                            text = headerSubtleText,
                            style = Ux4gTheme.typography.lS_default,
                            color = Ux4gTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                            modifier = Modifier.padding(horizontal = Ux4gSpace.Space8)
                        )
                    }

                    // Close Button
                    if (showCloseButton) {
                        IconButton(
                            onClick = onCloseClick,
                            modifier = Modifier.size(24.dp)
                        ) {
                            Ux4gIconComponent(
                                icon = Ux4gIcons.Close,
                                contentDescription = "Close",
                                modifier = Modifier.size(20.dp),
                                tint = Ux4gTheme.colorScheme.onSurface
                            )
                        }
                    }
                }

                // Description Block (below title row)
                if (description != null) {
                    Text(
                        text = description,
                        style = Ux4gTheme.typography.bS_default,
                        color = Ux4gTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                        modifier = Modifier.padding(
                            start = if (headerIcon != null) 36.dp + Ux4gSpace.Space16 else Ux4gSpace.Space16,
                            end = Ux4gSpace.Space16,
                            bottom = Ux4gSpace.Space12
                        )
                    )
                }

                if (showDividers) {
                    Ux4gDivider(color = Ux4gTheme.colorScheme.onSurface.copy(alpha = 0.2f).copy(alpha = 0.5f))
                }
            }
        }

        // ── Main Content Area (Scrollable) ──────────────────────────────────
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f, fill = false) // Ensures sheet takes only needed space but is scrollable
                .verticalScroll(rememberScrollState())
                .padding(horizontal = Ux4gSpace.Space16, vertical = Ux4gSpace.Space16)
        ) {
            content()
        }

        // ── Footer Area ─────────────────────────────────────────────────────
        if (footerType != Ux4gCardFooterType.None) {
            Column(modifier = Modifier.fillMaxWidth()) {
                if (showDividers) {
                    Ux4gDivider(color = Ux4gTheme.colorScheme.onSurface.copy(alpha = 0.2f).copy(alpha = 0.5f))
                }
                
                BottomSheetFooterRow(
                    footerType = footerType,
                    footerAlignment = footerAlignment,
                    primaryButtonText = primaryButtonText,
                    secondaryButtonText = secondaryButtonText,
                    onPrimaryClick = onPrimaryClick,
                    onSecondaryClick = onSecondaryClick
                )
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Footer Helper
// ─────────────────────────────────────────────────────────────────────────────

@Composable
private fun BottomSheetFooterRow(
    footerType: Ux4gCardFooterType,
    footerAlignment: Ux4gBottomSheetFooterAlignment,
    primaryButtonText: String,
    secondaryButtonText: String,
    onPrimaryClick: () -> Unit,
    onSecondaryClick: () -> Unit
) {
    val arrangement = when (footerAlignment) {
        Ux4gBottomSheetFooterAlignment.Left -> Arrangement.Start
        Ux4gBottomSheetFooterAlignment.Centered -> Arrangement.Center
        Ux4gBottomSheetFooterAlignment.Right -> Arrangement.End
        Ux4gBottomSheetFooterAlignment.Justified -> Arrangement.spacedBy(Ux4gSpace.Space12)
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = Ux4gSpace.Space16, vertical = Ux4gSpace.Space16),
        horizontalArrangement = arrangement,
        verticalAlignment = Alignment.CenterVertically
    ) {
        val isJustified = footerAlignment == Ux4gBottomSheetFooterAlignment.Justified
        
        when (footerType) {
            Ux4gCardFooterType.PrimaryAndSecondary -> {
                // Secondary Button
                Ux4gOutlineButton(
                    onClick = onSecondaryClick,
                    modifier = if (isJustified) Modifier.weight(1f) else Modifier.wrapContentWidth(),
                    contentPadding = PaddingValues(
                        horizontal = Ux4gSpace.Space20,
                        vertical = Ux4gSpace.Space12
                    )
                ) { Text(secondaryButtonText) }

                if (!isJustified) Spacer(modifier = Modifier.width(Ux4gSpace.Space12))

                // Primary Button
                Ux4gButton(
                    onClick = onPrimaryClick,
                    modifier = if (isJustified) Modifier.weight(1f) else Modifier.wrapContentWidth(),
                    contentPadding = PaddingValues(
                        horizontal = Ux4gSpace.Space20,
                        vertical = Ux4gSpace.Space12
                    )
                ) { Text(primaryButtonText) }
            }
            Ux4gCardFooterType.PrimaryOnly -> {
                Ux4gButton(
                    onClick = onPrimaryClick,
                    modifier = if (isJustified) Modifier.fillMaxWidth() else Modifier.wrapContentWidth(),
                    contentPadding = PaddingValues(
                        horizontal = Ux4gSpace.Space20,
                        vertical = Ux4gSpace.Space12
                    )
                ) { Text(primaryButtonText) }
            }
            Ux4gCardFooterType.SecondaryOnly -> {
                Ux4gOutlineButton(
                    onClick = onSecondaryClick,
                    modifier = if (isJustified) Modifier.fillMaxWidth() else Modifier.wrapContentWidth(),
                    contentPadding = PaddingValues(
                        horizontal = Ux4gSpace.Space20,
                        vertical = Ux4gSpace.Space12
                    )
                ) { Text(secondaryButtonText) }
            }
            Ux4gCardFooterType.None -> Unit
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Previews
// ─────────────────────────────────────────────────────────────────────────────

@Preview(name = "Basic Bottom Sheet Content", showBackground = true)
@Composable
private fun Ux4gBottomSheetPreview() {
    Ux4gTheme {
        Ux4gBottomSheetLayout(
            headerTitle = "Header",
            headerSubtleText = "Subtle text",
            headerIcon = Ux4gIcons.Settings,
            showCloseButton = true,
            description = "Write description here",
            footerType = Ux4gCardFooterType.PrimaryAndSecondary,
            footerAlignment = Ux4gBottomSheetFooterAlignment.Justified,
            primaryButtonText = "Button",
            secondaryButtonText = "Button"
        ) {
            Text(
                text = "A Drawer is a panel that slides out from the edge of the screen to show extra information or options",
                style = Ux4gTheme.typography.bM_default,
                color = Ux4gTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )
        }
    }
}

@Preview(name = "Left Aligned Footer", showBackground = true)
@Composable
private fun Ux4gBottomSheetLeftPreview() {
    Ux4gTheme {
        Ux4gBottomSheetLayout(
            headerTitle = "Header",
            showCloseButton = true,
            footerType = Ux4gCardFooterType.PrimaryAndSecondary,
            footerAlignment = Ux4gBottomSheetFooterAlignment.Left,
            primaryButtonText = "Submit",
            secondaryButtonText = "Cancel"
        ) {
            Text("Body content goes here")
        }
    }
}

@Preview(name = "Dark Mode", uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
private fun Ux4gBottomSheetDarkPreview() {
    Ux4gTheme {
        Ux4gBottomSheetLayout(
            headerTitle = "Dark Theme Sheet",
            description = "This is how it looks in dark mode",
            footerType = Ux4gCardFooterType.PrimaryOnly,
            footerAlignment = Ux4gBottomSheetFooterAlignment.Justified,
            primaryButtonText = "Got it"
        ) {
            Text("Testing dark mode appearance.")
        }
    }
}
