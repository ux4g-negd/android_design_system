package com.ux4g.designsystemv1.components

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ux4g.designsystemv1.theme.Ux4gTheme

/**
 * A component that wraps [content] and opens [url] in the device's browser when tapped.
 *
 * Mirrors the Flutter `Ux4gLink` component.
 */
@Composable
fun Ux4gLink(
        url: String,
        modifier: Modifier = Modifier,
        content: @Composable () -> Unit
) {
    val context = LocalContext.current

    Box(
            modifier =
                    modifier.clickable {
                        try {
                            context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
                        } catch (e: Exception) {
                            Log.e("Ux4gLink", "Could not launch $url", e)
                        }
                    }
    ) { content() }
}

@Preview(name = "Link", showBackground = true)
@Composable
private fun Ux4gLinkPreview() {
    Ux4gTheme {
        Box(modifier = Modifier.padding(16.dp)) {
            Ux4gLink(url = "https://ux4g.gov.in") {
                Text(
                        text = "Open Ux4G website",
                        color = Ux4gTheme.colorScheme.primary,
                        style = Ux4gTheme.typography.lL_strong,
                        modifier = Modifier.padding(4.dp)
                )
            }
        }
    }
}
