package com.dehar.player.ui.navigation

import android.net.Uri

object Routes {
    const val LOCK = "lock"
    const val HOME = "home"
    const val EXTERNAL_PLAYER = "external_player"
    const val FOLDER = "folder/{folderPath}"
    const val PLAYER = "player/{videoIndex}/{folderPath}"
    const val MUSIC_LIBRARY = "music_library"
    const val NOW_PLAYING = "now_playing"
    const val VIDEO_PLAYER = "video_player"
    const val SETTINGS = "settings"
    const val PRIVATE_VAULT = "private_vault"
    const val MEDIA_MANAGER = "media_manager/{tab}"
    
    fun folder(path: String) = "folder/${Uri.encode(path)}"
    fun player(videoIndex: Int, folderPath: String) = "player/$videoIndex/${Uri.encode(folderPath)}"
    fun mediaManager(tab: String = "recycle_bin") = "media_manager/$tab"
}
