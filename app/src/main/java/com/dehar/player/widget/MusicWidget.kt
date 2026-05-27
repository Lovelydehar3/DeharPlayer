package com.dehar.player.widget

import android.content.Context
import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.glance.*
import androidx.glance.action.ActionParameters
import androidx.glance.action.clickable
import androidx.glance.appwidget.*
import androidx.glance.appwidget.action.ActionCallback
import androidx.glance.appwidget.action.actionRunCallback
import androidx.glance.appwidget.state.updateAppWidgetState
import androidx.glance.layout.*
import androidx.glance.text.*
import androidx.glance.unit.ColorProvider
import com.dehar.player.R
import com.dehar.player.player.MusicService

class MusicWidget : GlanceAppWidget() {
    
    @Composable
    override fun Content() {
        val prefs = currentState<Preferences>()
        val title = prefs[stringPreferencesKey("title")] ?: "Not playing"
        val artist = prefs[stringPreferencesKey("artist")] ?: ""
        val isPlaying = prefs[booleanPreferencesKey("is_playing")] ?: false
        
        Row(
            modifier = GlanceModifier
                .fillMaxWidth()
                .padding(8.dp)
                .background(ColorProvider(Color(0xFF1E2833)))
                .cornerRadius(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Album art placeholder
            Box(
                modifier = GlanceModifier
                    .size(48.dp)
                    .background(ColorProvider(Color.White.copy(alpha = 0.1f)))
                    .cornerRadius(6.dp),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    provider = ImageProvider(R.drawable.ic_music_note),
                    contentDescription = null,
                    modifier = GlanceModifier.size(24.dp)
                )
            }
            
            Spacer(GlanceModifier.width(8.dp))
            
            Column(modifier = GlanceModifier.defaultWeight()) {
                Text(
                    text = title,
                    maxLines = 1,
                    style = TextStyle(
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = ColorProvider(Color.White)
                    )
                )
                Text(
                    text = artist,
                    maxLines = 1,
                    style = TextStyle(
                        fontSize = 11.sp,
                        color = ColorProvider(Color.Gray)
                    )
                )
            }
            
            // Controls
            Image(
                provider = ImageProvider(R.drawable.ic_skip_previous),
                contentDescription = "Previous",
                modifier = GlanceModifier.size(32.dp).clickable(
                    actionRunCallback<WidgetPrevAction>()
                )
            )
            
            Image(
                provider = ImageProvider(
                    if (isPlaying) R.drawable.ic_pause else R.drawable.ic_play_arrow
                ),
                contentDescription = if (isPlaying) "Pause" else "Play",
                modifier = GlanceModifier.size(40.dp).clickable(
                    actionRunCallback<WidgetPlayPauseAction>()
                )
            )
            
            Image(
                provider = ImageProvider(R.drawable.ic_skip_next),
                contentDescription = "Next",
                modifier = GlanceModifier.size(32.dp).clickable(
                    actionRunCallback<WidgetNextAction>()
                )
            )
        }
    }
}

class WidgetPrevAction : ActionCallback {
    override suspend fun onAction(context: Context, glanceId: GlanceId, parameters: ActionParameters) {
        val intent = Intent(context, MusicService::class.java).apply {
            action = "ACTION_PREVIOUS"
        }
        context.startService(intent)
    }
}

class WidgetPlayPauseAction : ActionCallback {
    override suspend fun onAction(context: Context, glanceId: GlanceId, parameters: ActionParameters) {
        val intent = Intent(context, MusicService::class.java).apply {
            action = "ACTION_TOGGLE_PLAY"
        }
        context.startService(intent)
    }
}

class WidgetNextAction : ActionCallback {
    override suspend fun onAction(context: Context, glanceId: GlanceId, parameters: ActionParameters) {
        val intent = Intent(context, MusicService::class.java).apply {
            action = "ACTION_NEXT"
        }
        context.startService(intent)
    }
}

class MusicWidgetReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget = MusicWidget()
}
