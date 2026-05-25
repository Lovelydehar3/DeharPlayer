package com.dehar.player.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import org.json.JSONArray
import org.json.JSONObject
import java.util.Collections

data class Playlist(
    val name: String,
    val songIds: List<Long>,
    val dateCreated: Long = System.currentTimeMillis()
)

class PlaylistManager(private val context: Context) {

    private val Context.musicDataStore: DataStore<Preferences> by preferencesDataStore(name = "dehar_music_prefs")

    companion object {
        private val KEY_FAVORITES = stringPreferencesKey("favorite_song_ids")
        private val KEY_PLAYLISTS = stringPreferencesKey("custom_playlists")
    }

    // --- FAVORITES SYSTEM ---

    suspend fun getFavoriteSongIds(): Set<Long> {
        val jsonStr = context.musicDataStore.data.map { prefs ->
            prefs[KEY_FAVORITES] ?: "[]"
        }.first()
        return try {
            val jsonArray = JSONArray(jsonStr)
            val ids = mutableSetOf<Long>()
            for (i in 0 until jsonArray.length()) {
                ids.add(jsonArray.getLong(i))
            }
            ids
        } catch (e: Exception) {
            emptySet()
        }
    }

    suspend fun toggleFavorite(songId: Long): Boolean {
        val currentFavorites = getFavoriteSongIds().toMutableSet()
        val isAdded = if (currentFavorites.contains(songId)) {
            currentFavorites.remove(songId)
            false
        } else {
            currentFavorites.add(songId)
            true
        }

        val jsonArray = JSONArray()
        currentFavorites.forEach { jsonArray.put(it) }
        context.musicDataStore.edit { prefs ->
            prefs[KEY_FAVORITES] = jsonArray.toString()
        }
        return isAdded
    }

    suspend fun isFavorite(songId: Long): Boolean {
        return getFavoriteSongIds().contains(songId)
    }

    // --- PLAYLIST SYSTEM ---

    suspend fun getPlaylists(): List<Playlist> {
        val jsonStr = context.musicDataStore.data.map { prefs ->
            prefs[KEY_PLAYLISTS] ?: "[]"
        }.first()
        return try {
            val jsonArray = JSONArray(jsonStr)
            val playlists = mutableListOf<Playlist>()
            for (i in 0 until jsonArray.length()) {
                val obj = jsonArray.getJSONObject(i)
                val name = obj.getString("name")
                val dateCreated = obj.optLong("dateCreated", System.currentTimeMillis())
                
                val idsArray = obj.getJSONArray("songIds")
                val songIds = mutableListOf<Long>()
                for (j in 0 until idsArray.length()) {
                    songIds.add(idsArray.getLong(j))
                }
                playlists.add(Playlist(name, songIds, dateCreated))
            }
            playlists
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun savePlaylists(playlists: List<Playlist>) {
        try {
            val jsonArray = JSONArray()
            playlists.forEach { playlist ->
                val obj = JSONObject().apply {
                    put("name", playlist.name)
                    put("dateCreated", playlist.dateCreated)
                    
                    val idsArray = JSONArray()
                    playlist.songIds.forEach { idsArray.put(it) }
                    put("songIds", idsArray)
                }
                jsonArray.put(obj)
            }
            context.musicDataStore.edit { prefs ->
                prefs[KEY_PLAYLISTS] = jsonArray.toString()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    suspend fun createPlaylist(name: String): Boolean {
        if (name.isBlank()) return false
        val playlists = getPlaylists().toMutableList()
        if (playlists.any { it.name.equals(name, ignoreCase = true) }) {
            return false // Playlist already exists
        }
        playlists.add(Playlist(name, emptyList()))
        savePlaylists(playlists)
        return true
    }

    suspend fun renamePlaylist(oldName: String, newName: String): Boolean {
        if (newName.isBlank()) return false
        val playlists = getPlaylists().toMutableList()
        val index = playlists.indexOfFirst { it.name == oldName }
        if (index == -1) return false
        if (playlists.any { it.name.equals(newName, ignoreCase = true) && it.name != oldName }) {
            return false // Name collision
        }
        val old = playlists[index]
        playlists[index] = old.copy(name = newName)
        savePlaylists(playlists)
        return true
    }

    suspend fun deletePlaylist(name: String) {
        val playlists = getPlaylists().toMutableList()
        playlists.removeAll { it.name == name }
        savePlaylists(playlists)
    }

    suspend fun addSongToPlaylist(playlistName: String, songId: Long): Boolean {
        return addSongsToPlaylist(playlistName, listOf(songId))
    }

    suspend fun addSongsToPlaylist(playlistName: String, songIds: List<Long>): Boolean {
        if (songIds.isEmpty()) return false
        val playlists = getPlaylists().toMutableList()
        val index = playlists.indexOfFirst { it.name == playlistName }
        if (index == -1) return false
        
        val playlist = playlists[index]
        val updatedSongs = playlist.songIds.toMutableList()
        
        // Append new items while preventing sequential duplicate inserts if already present
        songIds.forEach { id ->
            if (!updatedSongs.contains(id)) {
                updatedSongs.add(id)
            }
        }

        playlists[index] = playlist.copy(songIds = updatedSongs)
        savePlaylists(playlists)
        return true
    }

    suspend fun removeSongFromPlaylist(playlistName: String, songId: Long): Boolean {
        val playlists = getPlaylists().toMutableList()
        val index = playlists.indexOfFirst { it.name == playlistName }
        if (index == -1) return false
        
        val playlist = playlists[index]
        val updatedSongs = playlist.songIds.toMutableList()
        val removed = updatedSongs.remove(songId)
        if (removed) {
            playlists[index] = playlist.copy(songIds = updatedSongs)
            savePlaylists(playlists)
        }
        return removed
    }

    suspend fun reorderSongsInPlaylist(playlistName: String, fromIndex: Int, toIndex: Int): Boolean {
        val playlists = getPlaylists().toMutableList()
        val index = playlists.indexOfFirst { it.name == playlistName }
        if (index == -1) return false
        
        val playlist = playlists[index]
        val updatedSongs = playlist.songIds.toMutableList()
        if (fromIndex in updatedSongs.indices && toIndex in updatedSongs.indices) {
            Collections.swap(updatedSongs, fromIndex, toIndex)
            playlists[index] = playlist.copy(songIds = updatedSongs)
            savePlaylists(playlists)
            return true
        }
        return false
    }
}
