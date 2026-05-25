package com.dehar.player.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.security.MessageDigest

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "dehar_player_prefs")

class PreferencesManager(private val context: Context) {

    companion object {
        private val KEY_PIN_HASH = stringPreferencesKey("pin_hash")
        private val KEY_PIN_ENABLED = booleanPreferencesKey("pin_enabled")
        private val KEY_SORT_ORDER = stringPreferencesKey("sort_order")
        private val KEY_RESIZE_MODE = intPreferencesKey("resize_mode")
        private const val PREFIX_VIDEO_POSITION = "pos_"
        
        private val KEY_VIEW_MODE = stringPreferencesKey("view_mode")
        private val KEY_LAYOUT_TYPE = stringPreferencesKey("layout_type")
        private val KEY_FIELDS_THUMBNAIL = booleanPreferencesKey("fields_thumbnail")
        private val KEY_FIELDS_LENGTH = booleanPreferencesKey("fields_length")
        private val KEY_FIELDS_EXTENSION = booleanPreferencesKey("fields_extension")
        private val KEY_FIELDS_PLAYED_TIME = booleanPreferencesKey("fields_played_time")
        private val KEY_FIELDS_RESOLUTION = booleanPreferencesKey("fields_resolution")
        private val KEY_FIELDS_FRAME_RATE = booleanPreferencesKey("fields_frame_rate")
        private val KEY_FIELDS_PATH = booleanPreferencesKey("fields_path")
        private val KEY_FIELDS_SIZE = booleanPreferencesKey("fields_size")
        private val KEY_FIELDS_DATE = booleanPreferencesKey("fields_date")
        private val KEY_ADV_DISPLAY_LENGTH = booleanPreferencesKey("adv_display_length")
        private val KEY_ADV_SHOW_HIDDEN = booleanPreferencesKey("adv_show_hidden")
        private val KEY_ADV_NOMEDIA = booleanPreferencesKey("adv_nomedia")
    }

    suspend fun getPin(): String? {
        return context.dataStore.data.map { prefs ->
            prefs[KEY_PIN_HASH]
        }.first()
    }

    suspend fun setPin(pin: String) {
        val hashed = hashString(pin)
        context.dataStore.edit { prefs ->
            prefs[KEY_PIN_HASH] = hashed
            prefs[KEY_PIN_ENABLED] = true
        }
    }

    suspend fun isPinSet(): Boolean {
        val pin = getPin()
        return !pin.isNullOrEmpty()
    }

    suspend fun isPinEnabled(): Boolean {
        return context.dataStore.data.map { prefs ->
            prefs[KEY_PIN_ENABLED] ?: false
        }.first() && isPinSet()
    }

    suspend fun setPinEnabled(enabled: Boolean) {
        context.dataStore.edit { prefs ->
            prefs[KEY_PIN_ENABLED] = enabled
        }
    }

    suspend fun verifyPin(pin: String): Boolean {
        val storedHash = getPin() ?: return false
        return storedHash == hashString(pin)
    }

    suspend fun clearPin() {
        context.dataStore.edit { prefs ->
            prefs.remove(KEY_PIN_HASH)
            prefs[KEY_PIN_ENABLED] = false
        }
    }

    suspend fun getLastPosition(videoPath: String): Long {
        val key = longPreferencesKey(PREFIX_VIDEO_POSITION + sanitizeKey(videoPath))
        return context.dataStore.data.map { prefs ->
            prefs[key] ?: 0L
        }.first()
    }

    suspend fun getLastPositions(videoPaths: List<String>): Map<String, Long> {
        val keysByPath = videoPaths.associateWith { path ->
            longPreferencesKey(PREFIX_VIDEO_POSITION + sanitizeKey(path))
        }
        return context.dataStore.data.map { prefs ->
            keysByPath.mapValues { (_, key) -> prefs[key] ?: 0L }
        }.first()
    }

    suspend fun setLastPosition(videoPath: String, position: Long) {
        val key = longPreferencesKey(PREFIX_VIDEO_POSITION + sanitizeKey(videoPath))
        context.dataStore.edit { prefs ->
            prefs[key] = position
        }
    }

    suspend fun getSortOrder(): SortOrder {
        val name = context.dataStore.data.map { prefs ->
            prefs[KEY_SORT_ORDER] ?: SortOrder.NAME_ASC.name
        }.first()
        return try {
            SortOrder.valueOf(name)
        } catch (e: Exception) {
            SortOrder.NAME_ASC
        }
    }

    suspend fun setSortOrder(order: SortOrder) {
        context.dataStore.edit { prefs ->
            prefs[KEY_SORT_ORDER] = order.name
        }
    }

    suspend fun getResizeMode(defaultMode: Int): Int {
        return context.dataStore.data.map { prefs ->
            prefs[KEY_RESIZE_MODE] ?: defaultMode
        }.first()
    }

    suspend fun setResizeMode(mode: Int) {
        context.dataStore.edit { prefs ->
            prefs[KEY_RESIZE_MODE] = mode
        }
    }

    suspend fun getLayoutSettings(): FolderLayoutSettings {
        return context.dataStore.data.map { prefs ->
            val sortName = prefs[KEY_SORT_ORDER] ?: SortOrder.NAME_ASC.name
            val sortOrder = try { SortOrder.valueOf(sortName) } catch(e: Exception) { SortOrder.NAME_ASC }
            FolderLayoutSettings(
                viewMode = prefs[KEY_VIEW_MODE] ?: "ALL_FOLDERS",
                layoutType = prefs[KEY_LAYOUT_TYPE] ?: "LIST",
                sortOrder = sortOrder,
                showThumbnail = prefs[KEY_FIELDS_THUMBNAIL] ?: true,
                showLength = prefs[KEY_FIELDS_LENGTH] ?: true,
                showExtension = prefs[KEY_FIELDS_EXTENSION] ?: false,
                showPlayedTime = prefs[KEY_FIELDS_PLAYED_TIME] ?: false,
                showResolution = prefs[KEY_FIELDS_RESOLUTION] ?: false,
                showFrameRate = prefs[KEY_FIELDS_FRAME_RATE] ?: false,
                showPath = prefs[KEY_FIELDS_PATH] ?: false,
                showSize = prefs[KEY_FIELDS_SIZE] ?: false,
                showDate = prefs[KEY_FIELDS_DATE] ?: false,
                displayLengthOverThumbnail = prefs[KEY_ADV_DISPLAY_LENGTH] ?: true,
                showHidden = prefs[KEY_ADV_SHOW_HIDDEN] ?: false,
                recognizeNoMedia = prefs[KEY_ADV_NOMEDIA] ?: true
            )
        }.first()
    }

    suspend fun setLayoutSettings(settings: FolderLayoutSettings) {
        context.dataStore.edit { prefs ->
            prefs[KEY_VIEW_MODE] = settings.viewMode
            prefs[KEY_LAYOUT_TYPE] = settings.layoutType
            prefs[KEY_SORT_ORDER] = settings.sortOrder.name
            prefs[KEY_FIELDS_THUMBNAIL] = settings.showThumbnail
            prefs[KEY_FIELDS_LENGTH] = settings.showLength
            prefs[KEY_FIELDS_EXTENSION] = settings.showExtension
            prefs[KEY_FIELDS_PLAYED_TIME] = settings.showPlayedTime
            prefs[KEY_FIELDS_RESOLUTION] = settings.showResolution
            prefs[KEY_FIELDS_FRAME_RATE] = settings.showFrameRate
            prefs[KEY_FIELDS_PATH] = settings.showPath
            prefs[KEY_FIELDS_SIZE] = settings.showSize
            prefs[KEY_FIELDS_DATE] = settings.showDate
            prefs[KEY_ADV_DISPLAY_LENGTH] = settings.displayLengthOverThumbnail
            prefs[KEY_ADV_SHOW_HIDDEN] = settings.showHidden
            prefs[KEY_ADV_NOMEDIA] = settings.recognizeNoMedia
        }
    }

    private fun hashString(input: String): String {
        val bytes = MessageDigest.getInstance("SHA-256").digest(input.toByteArray())
        return bytes.joinToString("") { "%02x".format(it) }
    }

    private fun sanitizeKey(raw: String): String {
        return raw.replace(Regex("[^a-zA-Z0-9_]"), "_")
    }
}

data class FolderLayoutSettings(
    val viewMode: String = "ALL_FOLDERS", // "ALL_FOLDERS", "FILES", "FOLDERS"
    val layoutType: String = "LIST", // "LIST", "GRID"
    val sortOrder: SortOrder = SortOrder.NAME_ASC,
    val showThumbnail: Boolean = true,
    val showLength: Boolean = true,
    val showExtension: Boolean = false,
    val showPlayedTime: Boolean = false,
    val showResolution: Boolean = false,
    val showFrameRate: Boolean = false,
    val showPath: Boolean = false,
    val showSize: Boolean = false,
    val showDate: Boolean = false,
    val displayLengthOverThumbnail: Boolean = true,
    val showHidden: Boolean = false,
    val recognizeNoMedia: Boolean = true
)
