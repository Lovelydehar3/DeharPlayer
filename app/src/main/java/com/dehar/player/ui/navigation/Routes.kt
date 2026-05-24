package com.dehar.player.ui.navigation

import android.net.Uri

object Routes {
    const val LOCK = "lock"
    const val HOME = "home"
    const val EXTERNAL_PLAYER = "external_player"
    const val FOLDER = "folder/{folderPath}"
    const val PLAYER = "player/{videoIndex}/{folderPath}"
    
    fun folder(path: String) = "folder/${Uri.encode(path)}"
    fun player(videoIndex: Int, folderPath: String) = "player/$videoIndex/${Uri.encode(folderPath)}"
}
