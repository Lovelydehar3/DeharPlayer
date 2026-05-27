package com.dehar.player.player

import android.content.Context
import android.graphics.Bitmap
import android.media.MediaMetadataRetriever
import android.net.Uri
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ThumbnailPreviewExtractor(private val context: Context) {
    private val retriever = MediaMetadataRetriever()
    private var isSetup = false

    fun setup(uri: Uri) {
        runCatching {
            retriever.setDataSource(context, uri)
            isSetup = true
        }.onFailure {
            isSetup = false
        }
    }

    suspend fun getFrameAt(positionMs: Long): Bitmap? = withContext(Dispatchers.IO) {
        if (!isSetup) return@withContext null
        runCatching {
            retriever.getFrameAtTime(positionMs * 1000, MediaMetadataRetriever.OPTION_CLOSEST_SYNC)
        }.getOrNull()
    }

    fun release() {
        runCatching { retriever.release() }
        isSetup = false
    }
}

