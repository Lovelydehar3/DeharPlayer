package com.dehar.player.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
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
        private const val PREFIX_VIDEO_POSITION = "pos_"
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

    private fun hashString(input: String): String {
        val bytes = MessageDigest.getInstance("SHA-256").digest(input.toByteArray())
        return bytes.joinToString("") { "%02x".format(it) }
    }

    private fun sanitizeKey(raw: String): String {
        return raw.replace(Regex("[^a-zA-Z0-9_]"), "_")
    }
}
