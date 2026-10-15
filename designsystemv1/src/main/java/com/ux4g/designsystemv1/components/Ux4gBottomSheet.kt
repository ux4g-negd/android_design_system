package com.ux4g.designsystemv1.components

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.ux4g.designsystemv1.components.buttons.Ux4gButton
import com.ux4g.designsystemv1.components.buttons.Ux4gButtonVariant
import com.ux4g.designsystemv1.foundation.*
import com.ux4g.designsystemv1.theme.Ux4gTheme

// ─────────────────────────────────────────────────────────────────────────────
// Enums & Types
// ─────────────────────────────────────────────────────────────────────────────

/**
 * Sheet height presets, matching UX4G specification and Figma `Size` variants:
 * - [Peek]: 0.26 (26% screen height)
 * - [Half]: 0.50 (50% screen height, default)
 * - [Expanded]: 0.75 (75% screen height)
 * - [Full]: 0.94 (94% screen height)
 */
enum class Ux4gBottomSheetSize(val ratio: Float) {
    Peek(0.26f),
    Half(0.50f),
    Expanded(0.75f),
    Full(0.94f);

    companion object {
        // Lowercase name aliases matching Flutter/Figma tokens
        val peek get() = Peek
        val half get() = Half
        val expanded get() = Expanded
        val full get() = Full
    }
}

/** Horizontal arrangement of the footer actions. */
enum class Ux4gBottomSheetFooterAlignment {
    Split,
    Left,
    Centered,
    Right,
    /** Both buttons take equal width (50% each) and fill the container. Alias for Split. */
    Justified;

    companion object {
        val split get() = Split
        val left get() = Left
        val center get() = Centered
        val centered get() = Centered
        val right get() = Right
        val justified get() = Justified
    }
}

/** Backward compatibility alias for footer alignment matching Flutter API. */
typealias Ux4gBottomSheetFooterAlign = Ux4gBottomSheetFooterAlignment

// ─────────────────────────────────────────────────────────────────────────────
// Main Composable
// ─────────────────────────────────────────────────────────────────────────────

/**
 * **Ux4gBottomSheet**
 *
 * Highly configurable, accessible Modal Bottom Sheet built strictly to UX4G Design System guidelines:
 * - Responsive and overflow-proof across all screen heights with UX4G size presets ([Peek], [Half], [Expanded], [Full])
 * - Pinned bottom action buttons: footer remains anchored at bottom of screen while middle content scrolls smoothly
 * - 16dp top corner radius by default
 * - Top drag handle indicator pill (32x4dp)
 * - Header block with icon, title ([Ux4gTheme.typography.hS_strong]), description ([Ux4gTheme.typography.bS_default]), subtle text, and close button
 * - Native UX4G Button components with destructive action support ([isDestructive])
 * - Full Dark Mode & Light Mode support via UX4G Theme
 *
 * @param onDismissRequest Called when the sheet is dismissed (scrim tap, swipe down, or close button).
 * @param modifier         Root modifier for the sheet container.
 * @param sheetState       State of the modal bottom sheet.
 * @param size             Preset height variant ([Ux4gBottomSheetSize.Peek], [Half], [Expanded], [Full]). Defaults to [Half].
 * @param height           Explicit height in Dp. Overrides [size] if provided.
 * @param isDraggable      Whether the sheet can be dragged/swiped. If false, the drag handle is hidden.
 * @param showDragHandle   Whether to show the top drag handle indicator pill.
 * @param showHeader       Whether to render the header block. Defaults to true.
 * @param headerTitle      Main title shown in the header.
 * @param headerSubtleText Optional small muted text shown on the right side of the title.
 * @param headerIcon       Optional icon shown to the left of the title.
 * @param headerIconComposable Optional composable icon for custom leading widgets.
 * @param description      Secondary text shown below the header title.
 * @param showCloseButton  Whether to show a 'Close' icon button in the top-right. Defaults to true.
 * @param onCloseClick     Custom callback for close button press (defaults to [onDismissRequest]).
 * @param showHeaderDivider Whether to show horizontal divider under the header. Defaults to false.
 * @param showBottomDivider Whether to show horizontal divider above the footer. Defaults to false.
 * @param showDividers     Backward compatible toggle for both header & footer dividers.
 * @param scrollable       Whether main body content should be scrollable. Defaults to true.
 * @param contentPadding   Padding values for the inner content.
 * @param showFooter       Whether to render the footer bar. Defaults to true.
 * @param footerType       Which buttons to show in the bottom footer row (takes precedence if provided).
 * @param footerAlignment  Alignment and width-filling behavior of footer buttons ([Split], [Left], [Centered], [Right]).
 * @param showPrimaryButton Whether to show the primary action button. Defaults to true.
 * @param showSecondaryButton Whether to show the secondary action button. Defaults to true.
 * @param primaryButtonText Label for the filled primary button.
 * @param secondaryButtonText Label for the outline secondary button.
 * @param onPrimaryClick   Callback for primary button.
 * @param onSecondaryClick Callback for secondary button.
 * @param isDestructive    Whether the primary action uses the destructive/error color.
 * @param footerContent    Custom composable to completely override the footer.
 * @param containerColor   Background color of the sheet surface. Defaults to [Ux4gTheme.colorScheme.surface].
 * @param scrimColor       Color of the scrim that obscures content behind the sheet.
 * @param cornerRadius     Top-left and top-right corner radius. Defaults to 16.dp.
 * @param titleStyle       Custom title text style override.
 * @param descriptionStyle Custom description text style override.
 * @param content          The main body content of the bottom sheet.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Ux4gBottomSheet(
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    sheetState: SheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
    size: Ux4gBottomSheetSize = Ux4gBottomSheetSize.Half,
    height: Dp? = null,
    isDraggable: Boolean = true,
    showDragHandle: Boolean = true,
    // ── Header ──────────────────────────────────────────────────────────────
    showHeader: Boolean = true,
    headerTitle: String? = null,
    headerSubtleText: String? = null,
    headerIcon: Ux4gIcon? = null,
    headerIconComposable: (@Composable () -> Unit)? = null,
    description: String? = null,
    showCloseButton: Boolean = true,
    onCloseClick: (() -> Unit)? = null,
    showHeaderDivider: Boolean = false,
    // ── Content ─────────────────────────────────────────────────────────────
    scrollable: Boolean = true,
    showBottomDivider: Boolean = false,
    showDividers: Boolean? = null,
    contentPadding: PaddingValues = PaddingValues(horizontal = Ux4gSpace.Space16, vertical = Ux4gSpace.Space12),
    // ── Footer ──────────────────────────────────────────────────────────────
    showFooter: Boolean = true,
    footerType: Ux4gCardFooterType? = null,
    footerAlignment: Ux4gBottomSheetFooterAlignment = Ux4gBottomSheetFooterAlignment.Split,
    showPrimaryButton: Boolean = true,
    showSecondaryButton: Boolean = true,
    primaryButtonText: String = "Confirm",
    secondaryButtonText: String = "Cancel",
    onPrimaryClick: () -> Unit = onDismissRequest,
    onSecondaryClick: () -> Unit = onDismissRequest,
    isDestructive: Boolean = false,
    footerContent: (@Composable () -> Unit)? = null,
    // ── Appearance ──────────────────────────────────────────────────────────
    containerColor: Color = Ux4gTheme.colorScheme.surface,
    scrimColor: Color = Color(0xFF0F172A).copy(alpha = 0.5f),
    cornerRadius: Dp = 16.dp,
    titleStyle: TextStyle? = null,
    descriptionStyle: TextStyle? = null,
    content: @Composable ColumnScope.() -> Unit,
) {
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp
    val minSafeHeight = 160.dp
    val targetHeight = height ?: (screenHeight * size.ratio).coerceIn(minSafeHeight, screenHeight * 0.95f)

    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        modifier = modifier,
        sheetState = sheetState,
        shape = RoundedCornerShape(topStart = cornerRadius, topEnd = cornerRadius),
        containerColor = containerColor,
        scrimColor = scrimColor,
        dragHandle = null // Drag handle is rendered inside layout for exact UX4G styling
    ) {
        Ux4gBottomSheetLayout(
            modifier = Modifier
                .fillMaxWidth()
                .height(targetHeight),
            isDraggable = isDraggable,
            showDragHandle = showDragHandle,
            showHeader = showHeader,
            headerTitle = headerTitle,
            headerSubtleText = headerSubtleText,
            headerIcon = headerIcon,
            headerIconComposable = headerIconComposable,
            description = description,
            showCloseButton = showCloseButton,
            onCloseClick = onCloseClick ?: onDismissRequest,
            showHeaderDivider = showHeaderDivider,
            showBottomDivider = showBottomDivider,
            showDividers = showDividers,
            scrollable = scrollable,
            contentPadding = contentPadding,
            showFooter = showFooter,
            footerType = footerType,
            footerAlignment = footerAlignment,
            showPrimaryButton = showPrimaryButton,
            showSecondaryButton = showSecondaryButton,
            primaryButtonText = primaryButtonText,
            secondaryButtonText = secondaryButtonText,
            onPrimaryClick = onPrimaryClick,
            onSecondaryClick = onSecondaryClick,
            isDestructive = isDestructive,
            footerContent = footerContent,
            titleStyle = titleStyle,
            descriptionStyle = descriptionStyle,
            content = content
        )
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Content Layout (usable inside ModalBottomSheet, Dialog, or Previews)
// ─────────────────────────────────────────────────────────────────────────────

@Composable
fun Ux4gBottomSheetLayout(
    modifier: Modifier = Modifier,
    isDraggable: Boolean = true,
    showDragHandle: Boolean = true,
    showHeader: Boolean = true,
    headerTitle: String? = null,
    headerSubtleText: String? = null,
    headerIcon: Ux4gIcon? = null,
    headerIconComposable: (@Composable () -> Unit)? = null,
    description: String? = null,
    showCloseButton: Boolean = true,
    onCloseClick: () -> Unit = {},
    showHeaderDivider: Boolean = false,
    showBottomDivider: Boolean = false,
    showDividers: Boolean? = null,
    scrollable: Boolean = true,
    contentPadding: PaddingValues = PaddingValues(horizontal = Ux4gSpace.Space16, vertical = Ux4gSpace.Space12),
    showFooter: Boolean = true,
    footerType: Ux4gCardFooterType? = null,
    footerAlignment: Ux4gBottomSheetFooterAlignment = Ux4gBottomSheetFooterAlignment.Split,
    showPrimaryButton: Boolean = true,
    showSecondaryButton: Boolean = true,
    primaryButtonText: String = "Confirm",
    secondaryButtonText: String = "Cancel",
    onPrimaryClick: () -> Unit = {},
    onSecondaryClick: () -> Unit = {},
    isDestructive: Boolean = false,
    footerContent: (@Composable () -> Unit)? = null,
    titleStyle: TextStyle? = null,
    descriptionStyle: TextStyle? = null,
    content: @Composable ColumnScope.() -> Unit
) {
    val isDark = isSystemInDarkTheme()
    val gripColor = if (isDark) Ux4gColors.neutral600 else Ux4gColors.neutral300
    val mutedColor = if (isDark) Ux4gColors.neutral400 else Ux4gColors.neutral600
    val dividerColor = if (isDark) Ux4gColors.neutral800 else Ux4gColors.neutral200
    val footerBorderColor = if (isDark) Color.White.copy(alpha = 0.08f) else Color.Black.copy(alpha = 0.06f)

    // Resolve dividers
    val showTopDivider = showDividers ?: showHeaderDivider
    val showBotDivider = showDividers ?: showBottomDivider

    // Resolve header visibility
    val hasHeader = showHeader && (
        headerTitle != null ||
        headerSubtleText != null ||
        headerIcon != null ||
        headerIconComposable != null ||
        description != null ||
        showCloseButton
    )

    // Resolve footer visibility
    var effectiveShowFooter = showFooter
    var effectiveShowPrimary = showPrimaryButton
    var effectiveShowSecondary = showSecondaryButton

    if (footerType != null) {
        when (footerType) {
            Ux4gCardFooterType.None -> {
                effectiveShowFooter = false
            }
            Ux4gCardFooterType.PrimaryOnly -> {
                effectiveShowFooter = true
                effectiveShowPrimary = true
                effectiveShowSecondary = false
            }
            Ux4gCardFooterType.SecondaryOnly -> {
                effectiveShowFooter = true
                effectiveShowPrimary = false
                effectiveShowSecondary = true
            }
            Ux4gCardFooterType.PrimaryAndSecondary -> {
                effectiveShowFooter = true
                effectiveShowPrimary = true
                effectiveShowSecondary = true
            }
        }
    }

    val hasFooter = effectiveShowFooter && (
        footerContent != null ||
        effectiveShowPrimary ||
        effectiveShowSecondary
    )

    Column(
        modifier = modifier
            .fillMaxWidth()
            .navigationBarsPadding()
    ) {
        // ── Top Drag Handle ──────────────────────────────────────────────────
        if (showDragHandle && isDraggable) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp, bottom = 6.dp),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(width = 32.dp, height = 4.dp)
                        .background(
                            color = gripColor,
                            shape = RoundedCornerShape(2.dp)
                        )
                )
            }
        }

        // ── Header Block ─────────────────────────────────────────────────────
        if (hasHeader) {
            Column(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            start = Ux4gSpace.Space16,
                            end = Ux4gSpace.Space16,
                            top = if (showDragHandle && isDraggable) 4.dp else 16.dp,
                            bottom = if (description != null) 4.dp else 12.dp
                        ),
                    verticalAlignment = Alignment.Top
                ) {
                    // Leading Icon
                    if (headerIconComposable != null) {
                        Box(
                            modifier = Modifier
                                .padding(end = 10.dp, top = 2.dp)
                                .size(24.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            headerIconComposable()
                        }
                    } else if (headerIcon != null) {
                        Ux4gIconComponent(
                            icon = headerIcon,
                            contentDescription = null,
                            modifier = Modifier
                                .padding(end = 10.dp, top = 2.dp)
                                .size(24.dp),
                            tint = Ux4gTheme.colorScheme.onSurface
                        )
                    }

                    // Titles Block (Title + Description)
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .padding(end = Ux4gSpace.Space8),
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = headerTitle ?: "Header",
                            style = (titleStyle ?: Ux4gTheme.typography.hS_strong)
                                .copy(color = Ux4gTheme.colorScheme.onSurface),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )

                        if (description != null) {
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = description,
                                style = (descriptionStyle ?: Ux4gTheme.typography.bS_default)
                                    .copy(color = mutedColor)
                            )
                        }
                    }

                    // Subtle Text (Tag or small indicator)
                    if (headerSubtleText != null) {
                        Text(
                            text = headerSubtleText,
                            style = Ux4gTheme.typography.lM_default
                                .copy(color = mutedColor),
                            modifier = Modifier
                                .align(Alignment.CenterVertically)
                                .padding(end = Ux4gSpace.Space8)
                        )
                    }

                    // Close Button
                    if (showCloseButton) {
                        IconButton(
                            onClick = onCloseClick,
                            modifier = Modifier.size(28.dp)
                        ) {
                            Ux4gIconComponent(
                                icon = Ux4gIcons.Close,
                                contentDescription = "Close bottom sheet",
                                modifier = Modifier.size(18.dp),
                                tint = Ux4gTheme.colorScheme.onSurface
                            )
                        }
                    }
                }

                // Optional Header Divider
                if (showTopDivider) {
                    Ux4gDivider(
                        modifier = Modifier.padding(horizontal = Ux4gSpace.Space16),
                        color = dividerColor
                    )
                }
            }
        }

        // ── Main Content Area (Scrollable between Header and Fixed Footer) ───
        val scrollState = rememberScrollState()
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .then(if (scrollable) Modifier.verticalScroll(scrollState) else Modifier)
                .padding(contentPadding)
        ) {
            content()
        }

        // ── Bottom Content Divider ───────────────────────────────────────────
        if (showBotDivider) {
            Ux4gDivider(
                modifier = Modifier.padding(horizontal = Ux4gSpace.Space16, vertical = 4.dp),
                color = dividerColor
            )
        }

        // ── Fixed Footer Actions (Anchored at bottom) ─────────────────────────
        if (hasFooter) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .drawBehind {
                        drawLine(
                            color = footerBorderColor,
                            start = Offset(0f, 0f),
                            end = Offset(size.width, 0f),
                            strokeWidth = 1.dp.toPx()
                        )
                    }
                    .padding(horizontal = Ux4gSpace.Space16, vertical = 14.dp)
            ) {
                if (footerContent != null) {
                    footerContent()
                } else {
                    BottomSheetFooterRow(
                        footerAlignment = footerAlignment,
                        showPrimary = effectiveShowPrimary,
                        showSecondary = effectiveShowSecondary,
                        primaryButtonText = primaryButtonText,
                        secondaryButtonText = secondaryButtonText,
                        onPrimaryClick = onPrimaryClick,
                        onSecondaryClick = onSecondaryClick,
                        isDestructive = isDestructive
                    )
                }
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Footer Helper
// ─────────────────────────────────────────────────────────────────────────────

@Composable
private fun BottomSheetFooterRow(
    footerAlignment: Ux4gBottomSheetFooterAlignment,
    showPrimary: Boolean,
    showSecondary: Boolean,
    primaryButtonText: String,
    secondaryButtonText: String,
    onPrimaryClick: () -> Unit,
    onSecondaryClick: () -> Unit,
    isDestructive: Boolean
) {
    val isSplit = footerAlignment == Ux4gBottomSheetFooterAlignment.Split ||
            footerAlignment == Ux4gBottomSheetFooterAlignment.Justified

    val primaryBgColor = if (isDestructive) Ux4gColors.red500 else null

    val primaryBtn: @Composable (Modifier) -> Unit = { mod ->
        Ux4gButton(
            onClick = onPrimaryClick,
            text = primaryButtonText,
            variant = Ux4gButtonVariant.Primary,
            backgroundColor = primaryBgColor,
            modifier = mod
        )
    }

    val secondaryBtn: @Composable (Modifier) -> Unit = { mod ->
        Ux4gButton(
            onClick = onSecondaryClick,
            text = secondaryButtonText,
            variant = Ux4gButtonVariant.Outline,
            modifier = mod
        )
    }

    // Split / Justified layout: Two buttons expand 50/50 side-by-side
    if (isSplit) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (showPrimary && showSecondary) {
                primaryBtn(Modifier.weight(1f))
                secondaryBtn(Modifier.weight(1f))
            } else if (showPrimary) {
                primaryBtn(Modifier.weight(1f))
            } else if (showSecondary) {
                secondaryBtn(Modifier.weight(1f))
            }
        }
        return
    }

    // Directional Alignment: Left, Centered, Right
    val arrangement = when (footerAlignment) {
        Ux4gBottomSheetFooterAlignment.Left -> Arrangement.Start
        Ux4gBottomSheetFooterAlignment.Centered -> Arrangement.Center
        Ux4gBottomSheetFooterAlignment.Right -> Arrangement.End
        else -> Arrangement.Start
    }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = arrangement,
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (showSecondary) {
            secondaryBtn(Modifier.wrapContentWidth())
            if (showPrimary) {
                Spacer(modifier = Modifier.width(12.dp))
            }
        }
        if (showPrimary) {
            primaryBtn(Modifier.wrapContentWidth())
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
            footerAlignment = Ux4gBottomSheetFooterAlignment.Split,
            primaryButtonText = "Confirm",
            secondaryButtonText = "Cancel"
        ) {
            Text(
                text = "A Bottom Sheet is a panel that slides up from the edge of the screen to show extra information or options.",
                style = Ux4gTheme.typography.bM_default,
                color = Ux4gTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )
        }
    }
}

@Preview(name = "Destructive Bottom Sheet", showBackground = true)
@Composable
private fun Ux4gBottomSheetDestructivePreview() {
    Ux4gTheme {
        Ux4gBottomSheetLayout(
            headerTitle = "Delete Item?",
            description = "This action cannot be undone.",
            isDestructive = true,
            footerAlignment = Ux4gBottomSheetFooterAlignment.Split,
            primaryButtonText = "Delete",
            secondaryButtonText = "Cancel"
        ) {
            Text(
                text = "Are you sure you want to delete this record permanently?",
                style = Ux4gTheme.typography.bM_default
            )
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
            footerAlignment = Ux4gBottomSheetFooterAlignment.Right,
            primaryButtonText = "Got it",
            showSecondaryButton = false
        ) {
            Text("Testing dark mode appearance.")
        }
    }
}
