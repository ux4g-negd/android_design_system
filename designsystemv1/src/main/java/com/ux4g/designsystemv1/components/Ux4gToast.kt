package com.ux4g.designsystemv1.components

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.animation.*
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.ux4g.designsystemv1.foundation.Ux4gIconComponent
import com.ux4g.designsystemv1.foundation.Ux4gIcons
import com.ux4g.designsystemv1.foundation.Ux4gPalette
import com.ux4g.designsystemv1.foundation.Ux4gRadius
import com.ux4g.designsystemv1.foundation.Ux4gSpace
import com.ux4g.designsystemv1.theme.Ux4gTheme

/**
 * Categories for the Toast message.
 */
enum class Ux4gToastCategory {
    Info,
    Success,
    Warning,
    Error,
    Slot
}

/**
 * Layout variants for the Toast message.
 * [Full] is typically for Tablet/Desktop (one-line).
 * [Stacked] is typically for Mobile (multi-line/stacked action).
 */
enum class Ux4gToastLayout {
    Full,
    Stacked
}

/**
 * A customizable Toast message component designed to be displayed at the top of the screen.
 * Similar to a Snackbar but following the Ux4G Design System foundation.
 *
 * @param category The type of toast (Info, Success, Warning, Error).
 * @param title The bold title/label of the toast.
 * @param message The descriptive text.
 * @param modifier Root modifier.
 * @param actionText Optional text for an action button on the right.
 * @param onActionClick Callback for the action button.
 * @param onCloseClick Callback for the close (X) button.
 * @param layout The layout variant (Full or Stacked).
 */
@Composable
fun Ux4gToast(
    category: Ux4gToastCategory,
    title: String,
    modifier: Modifier = Modifier,
    subtitle: String? = null,
    actionText: String? = null,
    onActionClick: (() -> Unit)? = null,
    onCloseClick: (() -> Unit)? = null,
    showCloseButton: Boolean = true,
    layout: Ux4gToastLayout? = null
) {
    val configuration = LocalConfiguration.current
    val resolvedLayout = layout ?: if (configuration.screenWidthDp < 600) {
        Ux4gToastLayout.Stacked
    } else {
        Ux4gToastLayout.Full
    }
    
    val style = getToastStyle(category)

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(Ux4gRadius.Radius4))
            .background(style.backgroundColor)
            .padding(
                horizontal = if (resolvedLayout == Ux4gToastLayout.Full) Ux4gSpace.Space16 else Ux4gSpace.Space12,
                vertical = if (resolvedLayout == Ux4gToastLayout.Full) Ux4gSpace.Space8 else Ux4gSpace.Space12
            )
    ) {
        if (resolvedLayout == Ux4gToastLayout.Full) {
            FullLayout(
                style = style,
                title = title,
                subtitle = subtitle,
                actionText = actionText,
                onActionClick = onActionClick,
                onCloseClick = onCloseClick,
                showCloseButton = showCloseButton
            )
        } else {
            StackedLayout(
                style = style,
                title = title,
                subtitle = subtitle,
                actionText = actionText,
                onActionClick = onActionClick,
                onCloseClick = onCloseClick,
                showCloseButton = showCloseButton
            )
        }
    }
}

@Composable
private fun FullLayout(
    style: ToastStyle,
    title: String,
    subtitle: String?,
    actionText: String?,
    onActionClick: (() -> Unit)?,
    onCloseClick: (() -> Unit)?,
    showCloseButton: Boolean
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        // Icon
        Ux4gIconComponent(
            icon = style.icon,
            contentDescription = null,
            tint = style.iconColor,
            modifier = Modifier.size(20.dp)
        )

        Spacer(modifier = Modifier.width(Ux4gSpace.Space12))

        // Text Content
        Row(
            modifier = Modifier.weight(1f),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = title,
                style = Ux4gTheme.typography.bS_strong,
                color = Ux4gTheme.colorScheme.onSurface,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            if (subtitle != null) {
                Spacer(modifier = Modifier.width(Ux4gSpace.Space8))
                Text(
                    text = subtitle,
                    style = Ux4gTheme.typography.bS_default,
                    color = Ux4gTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f, fill = false)
                )
            }
            // Push action/close to end
            Spacer(modifier = Modifier.weight(1f))
        }

        // Action
        if (actionText != null && onActionClick != null) {
            Text(
                text = actionText,
                style = Ux4gTheme.typography.lM_strong.copy(color = style.actionColor),
                modifier = Modifier
                    .padding(horizontal = Ux4gSpace.Space16) // Increased spacing per tablet design
                    .clickable { onActionClick?.invoke() }
            )
        }

        // Close Icon
        if (showCloseButton && onCloseClick != null) {
            Ux4gIconComponent(
                icon = Ux4gIcons.Close,
                contentDescription = "Close",
                tint = Ux4gTheme.colorScheme.onSurface,
                modifier = Modifier
                    .size(20.dp)
                    .clickable { onCloseClick?.invoke() }
            )
        }
    }
}

@Composable
private fun StackedLayout(
    style: ToastStyle,
    title: String,
    subtitle: String?,
    actionText: String?,
    onActionClick: (() -> Unit)?,
    onCloseClick: (() -> Unit)?,
    showCloseButton: Boolean
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            // Icon
            Ux4gIconComponent(
                icon = style.icon,
                contentDescription = null,
                tint = style.iconColor,
                modifier = Modifier.size(20.dp)
            )

            Spacer(modifier = Modifier.width(Ux4gSpace.Space8))

            // Title
            Text(
                text = title,
                style = Ux4gTheme.typography.bS_strong,
                color = Ux4gTheme.colorScheme.onSurface,
                modifier = Modifier.weight(1f)
            )

            // Close Icon (Top Right)
            if (showCloseButton && onCloseClick != null) {
                Ux4gIconComponent(
                    icon = Ux4gIcons.Close,
                    contentDescription = "Close",
                    tint = Ux4gTheme.colorScheme.onSurface,
                    modifier = Modifier
                        .size(20.dp)
                        .clickable { onCloseClick?.invoke() }
                )
            }
        }

        // Subtitle - Indented under Title per design
        if (subtitle != null) {
            Text(
                text = subtitle,
                style = Ux4gTheme.typography.bS_default,
                color = Ux4gTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                modifier = Modifier.padding(start = 28.dp, top = 2.dp)
            )
        }

        // Action - Indented under Title per design
        if (actionText != null && onActionClick != null) {
            Text(
                text = actionText,
                style = Ux4gTheme.typography.lM_strong.copy(color = style.actionColor),
                modifier = Modifier
                    .padding(start = 28.dp, top = 6.dp)
                    .clickable { onActionClick?.invoke() }
            )
        }
    }
}

private data class ToastStyle(
    val backgroundColor: Color,
    val iconColor: Color,
    val actionColor: Color,
    val icon: com.ux4g.designsystemv1.foundation.Ux4gIcon
)

@Composable
private fun getToastStyle(category: Ux4gToastCategory): ToastStyle {
    return when (category) {
        Ux4gToastCategory.Info -> ToastStyle(
            backgroundColor = Ux4gPalette.cyan50,
            iconColor = Ux4gPalette.cyan500,
            actionColor = Ux4gPalette.cyan600,
            icon = Ux4gIcons.info_circle
        )
        Ux4gToastCategory.Success -> ToastStyle(
            backgroundColor = Ux4gPalette.green50,
            iconColor = Ux4gPalette.green500,
            actionColor = Ux4gPalette.green600,
            icon = Ux4gIcons.success_circle
        )
        Ux4gToastCategory.Warning -> ToastStyle(
            backgroundColor = Ux4gPalette.gold50,
            iconColor = Ux4gPalette.gold600,
            actionColor = Ux4gPalette.gold700,
            icon = Ux4gIcons.warning
        )
        Ux4gToastCategory.Error -> ToastStyle(
            backgroundColor = Ux4gPalette.red50,
            iconColor = Ux4gTheme.colorScheme.error,
            actionColor = Ux4gPalette.red600,
            icon = Ux4gIcons.error
        )
        Ux4gToastCategory.Slot -> ToastStyle(
            backgroundColor = Ux4gPalette.purple50,
            iconColor = Ux4gPalette.purple500,
            actionColor = Ux4gPalette.purple600,
            icon = Ux4gIcons.Settings
        )
    }
}

@Preview(name = "Light Mode")
@Preview(name = "Dark Mode", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun PreviewUx4gToast() {
    Ux4gTheme {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .background(Ux4gTheme.colorScheme.background),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text("Full Layout (Tablet/Desktop)", style = Ux4gTheme.typography.hXXS_strong)
            
            Ux4gToast(
                category = Ux4gToastCategory.Info,
                title = "Info",
                subtitle = "Write your information text here",
                actionText = "Action",
                onActionClick = {}
            )

            Ux4gToast(
                category = Ux4gToastCategory.Success,
                title = "Success",
                subtitle = "Write your success text here",
                actionText = "Action",
                onActionClick = {}
            )

            Ux4gToast(
                category = Ux4gToastCategory.Warning,
                title = "Warning",
                subtitle = "Write your warning text here",
                actionText = "Action",
                onActionClick = {}
            )

            Ux4gToast(
                category = Ux4gToastCategory.Error,
                title = "Error",
                subtitle = "Write your error text here",
                actionText = "Action",
                onActionClick = {}
            )

            Ux4gToast(
                category = Ux4gToastCategory.Slot,
                title = "Slot",
                subtitle = "Write your slot text here",
                actionText = "Action",
                onActionClick = {}
            )

            Spacer(modifier = Modifier.size(24.dp))
            Text("Stacked Layout (Mobile)", style = Ux4gTheme.typography.hXXS_strong)

            Ux4gToast(
                category = Ux4gToastCategory.Info,
                title = "Info",
                subtitle = "Write your information text here",
                actionText = "Action",
                onActionClick = {},
                layout = Ux4gToastLayout.Stacked
            )

            Ux4gToast(
                category = Ux4gToastCategory.Error,
                title = "Error",
                subtitle = "Write your error text here",
                actionText = "Action",
                onActionClick = {},
                layout = Ux4gToastLayout.Stacked
            )
        }
    }
}

/**
 * State of the [Ux4gToastHost], controls which toast is currently shown.
 */
class Ux4gToastHostState {
    var currentToastData by mutableStateOf<Ux4gToastData?>(null)
        private set

    suspend fun showToast(
        category: Ux4gToastCategory,
        title: String,
        subtitle: String? = null,
        actionText: String? = null,
        onActionClick: (() -> Unit)? = null,
        showCloseButton: Boolean = true,
        autoClose: Boolean = true,
        durationMillis: Long = 1000 // default 1 sec per user request
    ) {
        currentToastData = Ux4gToastData(
            category = category,
            title = title,
            subtitle = subtitle,
            actionText = actionText,
            onActionClick = onActionClick,
            showCloseButton = showCloseButton
        )
        if (autoClose) {
            kotlinx.coroutines.delay(durationMillis)
            currentToastData = null
        }
    }

    fun dismiss() {
        currentToastData = null
    }
}

data class Ux4gToastData(
    val category: Ux4gToastCategory,
    val title: String,
    val subtitle: String? = null,
    val actionText: String? = null,
    val onActionClick: (() -> Unit)? = null,
    val showCloseButton: Boolean = true
)

/**
 * A host for displaying Top or Bottom Toasts with smooth animations.
 *
 * @param hostState The state of the toast host.
 * @param modifier Root modifier.
 * @param isBottom Whether the toast is displayed at the bottom (influences animation direction).
 */
@Composable
fun Ux4gToastHost(
    hostState: Ux4gToastHostState,
    modifier: Modifier = Modifier,
    isBottom: Boolean = false
) {
    val data = hostState.currentToastData
    
    AnimatedVisibility(
        visible = data != null,
        enter = slideInVertically(initialOffsetY = { if (isBottom) it else -it }) + fadeIn(),
        exit = slideOutVertically(targetOffsetY = { if (isBottom) it else -it }) + fadeOut(),
        modifier = modifier
    ) {
        data?.let {
            Ux4gToast(
                category = it.category,
                title = it.title,
                subtitle = it.subtitle,
                actionText = it.actionText,
                onActionClick = it.onActionClick,
                onCloseClick = { hostState.dismiss() },
                showCloseButton = it.showCloseButton,
                modifier = Modifier.padding(Ux4gSpace.Space16)
            )
        }
    }
}

