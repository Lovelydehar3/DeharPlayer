package com.dehar.player

import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.dehar.player.ui.navigation.DeharNavGraph
import com.dehar.player.ui.theme.DeharBackground
import com.dehar.player.ui.theme.DeharPlayerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val externalVideoUri = intent.takeIf { it.action == Intent.ACTION_VIEW }?.data
        val externalVideoName = externalVideoUri?.let(::resolveDisplayName)

        enableEdgeToEdge()
        setContent {
            DeharPlayerTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = DeharBackground
                ) {
                    DeharNavGraph(
                        externalVideoUri = externalVideoUri,
                        externalVideoName = externalVideoName
                    )
                }
            }
        }
    }

    private fun resolveDisplayName(uri: Uri): String {
        if (uri.scheme == "file") {
            return uri.lastPathSegment ?: "Video"
        }

        return runCatching {
            contentResolver.query(uri, arrayOf(OpenableColumns.DISPLAY_NAME), null, null, null)
                ?.use { cursor: Cursor ->
                    if (cursor.moveToFirst()) {
                        cursor.getString(cursor.getColumnIndexOrThrow(OpenableColumns.DISPLAY_NAME))
                    } else {
                        null
                    }
                }
        }.getOrNull() ?: (uri.lastPathSegment ?: "Video")
    }
}
