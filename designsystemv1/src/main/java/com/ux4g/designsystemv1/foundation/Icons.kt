package com.ux4g.designsystemv1.foundation

import androidx.annotation.DrawableRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import com.ux4g.designsystemv1.R

// Represents an icon that can either be a standard ImageVector or a custom Drawable Resource
sealed class Ux4gIcon {
    data class Vector(val imageVector: ImageVector) : Ux4gIcon()
    data class Drawable(@DrawableRes val resId: Int) : Ux4gIcon()
}

@Composable
fun Ux4gIconComponent(
        icon: Ux4gIcon,
        contentDescription: String?,
        modifier: Modifier = Modifier,
        tint: Color = LocalContentColor.current
) {
    when (icon) {
        is Ux4gIcon.Vector -> {
            Icon(
                    imageVector = icon.imageVector,
                    contentDescription = contentDescription,
                    modifier = modifier,
                    tint = tint
            )
        }
        is Ux4gIcon.Drawable -> {
            Icon(
                    painter = painterResource(id = icon.resId),
                    contentDescription = contentDescription,
                    modifier = modifier,
                    tint = tint
            )
        }
    }
}

// A centralized object to hold Icon tokens.
// This allows us to easily swap out icons (e.g. from Material to Custom SVGs) without changing call
// sites.
object Ux4gIcons {
    // Examples of using Vector icons:
    val ArrowBack: Ux4gIcon = Ux4gIcon.Vector(Icons.Filled.ArrowBack)
    val Check: Ux4gIcon = Ux4gIcon.Vector(Icons.Filled.Check)
    val Close: Ux4gIcon = Ux4gIcon.Vector(Icons.Filled.Close)
    val Add: Ux4gIcon = Ux4gIcon.Vector(Icons.Filled.Add)
    val Edit: Ux4gIcon = Ux4gIcon.Vector(Icons.Filled.Edit)
    val Home: Ux4gIcon = Ux4gIcon.Vector(Icons.Filled.Home)
    val Settings: Ux4gIcon = Ux4gIcon.Vector(Icons.Filled.Settings)
    val Person: Ux4gIcon = Ux4gIcon.Vector(Icons.Filled.Person)
    val Search: Ux4gIcon = Ux4gIcon.Drawable(R.drawable.ic_search)
    val Mic: Ux4gIcon = Ux4gIcon.Drawable(R.drawable.ic_mic)
    val PhotoCamera: Ux4gIcon = Ux4gIcon.Drawable(R.drawable.camera_badge)
    val Remove: Ux4gIcon = Ux4gIcon.Drawable(R.drawable.remove_badge)
    val edit_badge: Ux4gIcon = Ux4gIcon.Drawable(R.drawable.edit_badge)
    val error: Ux4gIcon = Ux4gIcon.Drawable(R.drawable.ic_error_circle_red)
    val outline_error: Ux4gIcon = Ux4gIcon.Drawable(R.drawable.ic_outline_i_error)
    val warning: Ux4gIcon = Ux4gIcon.Drawable(R.drawable.ic_warning_triangle)
    val info_circle: Ux4gIcon = Ux4gIcon.Drawable(R.drawable.ic_i_desc)
    val success_circle: Ux4gIcon = Ux4gIcon.Drawable(R.drawable.ic_success)
    val ic_left_arrow: Ux4gIcon = Ux4gIcon.Drawable(R.drawable.ic_left_arrow)
    val ic_right_arrow: Ux4gIcon = Ux4gIcon.Drawable(R.drawable.ic_right_arrow)

    // Flutter foundation aliases.
    val info: Ux4gIcon = info_circle
    val success: Ux4gIcon = success_circle
    val arrowLeft: Ux4gIcon = ic_left_arrow
    val arrowRight: Ux4gIcon = ic_right_arrow

    // Example of using your own SVG properly:
    // val MyCustomSVG: Ux4gIcon = Ux4gIcon.Drawable(R.drawable.my_custom_svg)
}
