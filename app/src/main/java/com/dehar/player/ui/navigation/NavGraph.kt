package com.dehar.player.ui.navigation

import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.dehar.player.data.PreferencesManager
import com.dehar.player.data.VideoRepository
import com.dehar.player.player.MusicPlaybackManager
import com.dehar.player.ui.screens.FolderScreen
import com.dehar.player.ui.screens.LockScreen
import com.dehar.player.ui.screens.ExternalPlayerScreen
import com.dehar.player.ui.screens.PlayerScreen
import com.dehar.player.feature.home.HomeScreen
import com.dehar.player.feature.musiclibrary.MusicLibraryScreen
import com.dehar.player.feature.musicplayer.MusicPlayerScreen
import com.dehar.player.feature.videoplayer.VideoPlayerScreen
import com.dehar.player.feature.settings.SettingsScreen

import com.dehar.player.feature.mediamanager.MediaManagerScreen
import com.dehar.player.feature.vault.ui.VaultBrowserScreen

@Composable
fun DeharNavGraph(
    externalVideoUri: Uri? = null,
    externalVideoName: String? = null
) {
    val navController = rememberNavController()
    val context = LocalContext.current
    val preferencesManager = remember { PreferencesManager(context) }
    val videoRepository = remember { VideoRepository(context) }
    val musicPlaybackManager = remember { MusicPlaybackManager(context) }
    
    val startDestination = remember {
        when {
            externalVideoUri != null -> Routes.EXTERNAL_PLAYER
            preferencesManager.isPinEnabled() -> Routes.LOCK
            else -> Routes.HOME
        }
    }

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
            composable(Routes.LOCK) {
                LockScreen(
                    preferencesManager = preferencesManager,
                    onUnlocked = {
                        navController.navigate(Routes.HOME) {
                            popUpTo(Routes.LOCK) { inclusive = true }
                        }
                    }
                )
            }
            
            composable(Routes.HOME) {
                HomeScreen()
            }

            composable(Routes.EXTERNAL_PLAYER) {
                externalVideoUri?.let { uri ->
                    ExternalPlayerScreen(
                        uri = uri,
                        displayName = externalVideoName ?: "Video",
                        navController = navController
                    )
                }
            }
            
            composable(
                route = Routes.FOLDER,
                arguments = listOf(navArgument("folderPath") { type = NavType.StringType })
            ) { backStackEntry ->
                val folderPath = backStackEntry.arguments?.getString("folderPath") ?: ""
                FolderScreen(
                    folderPath = folderPath,
                    navController = navController,
                    videoRepository = videoRepository,
                    preferencesManager = preferencesManager,
                    musicPlaybackManager = musicPlaybackManager
                )
            }
            
            composable(
                route = Routes.PLAYER,
                arguments = listOf(
                    navArgument("videoIndex") { type = NavType.IntType },
                    navArgument("folderPath") { type = NavType.StringType }
                )
            ) { backStackEntry ->
                val videoIndex = backStackEntry.arguments?.getInt("videoIndex") ?: 0
                val folderPath = backStackEntry.arguments?.getString("folderPath") ?: ""
                PlayerScreen(
                    videoIndex = videoIndex,
                    folderPath = folderPath,
                    navController = navController,
                    videoRepository = videoRepository,
                    preferencesManager = preferencesManager
                )
            }

            composable(Routes.MUSIC_LIBRARY) {
                MusicLibraryScreen()
            }

            composable(Routes.NOW_PLAYING) {
                MusicPlayerScreen()
            }

            composable(Routes.VIDEO_PLAYER) {
                VideoPlayerScreen()
            }

            composable(Routes.SETTINGS) {
                SettingsScreen()
            }

            composable(Routes.PRIVATE_VAULT) {
                VaultBrowserScreen(
                    onNavigateBack = { navController.popBackStack() }
                )
            }

            composable(
                route = Routes.MEDIA_MANAGER,
                arguments = listOf(navArgument("tab") { defaultValue = "recycle_bin" })
            ) { backStackEntry ->
                val tab = backStackEntry.arguments?.getString("tab")
                MediaManagerScreen(
                    initialTab = tab,
                    onBack = { navController.popBackStack() }
                )
            }
        }
    }
}
