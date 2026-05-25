package com.dehar.player.ui.navigation

import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.dehar.player.ui.screens.HomeScreen
import com.dehar.player.ui.screens.LockScreen
import com.dehar.player.ui.screens.ExternalPlayerScreen
import com.dehar.player.ui.screens.PlayerScreen
import com.dehar.player.ui.screens.MusicLibraryScreen
import com.dehar.player.ui.screens.NowPlayingScreen

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
    
    var startDestination by remember { mutableStateOf<String?>(null) }
    
    LaunchedEffect(Unit) {
        val hasPin = preferencesManager.isPinEnabled()
        startDestination = when {
            externalVideoUri != null -> Routes.EXTERNAL_PLAYER
            hasPin -> Routes.LOCK
            else -> Routes.HOME
        }
    }
    
    startDestination?.let { destination ->
        NavHost(
            navController = navController,
            startDestination = destination
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
                HomeScreen(
                    navController = navController,
                    videoRepository = videoRepository,
                    preferencesManager = preferencesManager,
                    musicPlaybackManager = musicPlaybackManager
                )
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
                MusicLibraryScreen(
                    navController = navController,
                    musicPlaybackManager = musicPlaybackManager
                )
            }

            composable(Routes.NOW_PLAYING) {
                NowPlayingScreen(
                    navController = navController,
                    musicPlaybackManager = musicPlaybackManager
                )
            }
        }
    }
}
