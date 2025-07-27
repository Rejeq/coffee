package com.rejeq.sws.data.source.datastore

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.core.IOException
import androidx.datastore.preferences.core.MutablePreferences
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import kotlin.math.pow
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

// DataStore chosen instead of room because it easier to setup and we do not
// have complex data to store
class DataStoreSource(private val dataStore: DataStore<Preferences>) {
    suspend fun <T> tryReadMap(
        block: suspend (Preferences) -> T,
    ): Result<Flow<T>, IOException> = try {
        Ok(dataStore.data.map { block(it) })
    } catch (e: IOException) {
        Log.e(TAG, "Failed to read data", e)
        Err(e)
    }

    suspend fun tryReadLatest(): Result<Preferences, IOException> = try {
        Ok(dataStore.data.first())
    } catch (e: IOException) {
        Log.e(TAG, "Failed to read data", e)
        Err(e)
    }

    /**
     * Attempts to edit preferences with exponential backoff retry logic.
     *
     * @param block Lambda containing preference modifications
     * @return [EditResult] indicating success or failure
     */
    suspend fun tryEdit(
        block: suspend (MutablePreferences) -> Unit,
    ): EditResult {
        repeat(RETRY_COUNT) { attempt ->
            try {
                dataStore.edit(block)
                return EditResult.Success
            } catch (e: IOException) {
                Log.e(TAG, "Failed to edit data, attempt ${attempt + 1}", e)
            }

            val backoffTime = (BASE_DELAY * 2.0.pow(attempt)).toLong()
                .coerceAtMost(MAX_DELAY)

            delay(backoffTime)
        }

        return EditResult.FailWrite
    }
}

object PreferencesKeys {
    val WAS_REGISTERED = booleanPreferencesKey("was_registered")
    val WAS_LOGGED = booleanPreferencesKey("was_logged")

    val LOG_TOKEN = stringPreferencesKey("log_token")
    val LOG_TOKEN_LIFETIME = longPreferencesKey("log_token_lifetime")
    val LOG_TOKEN_TIME = longPreferencesKey("log_token_time")
}

/**
 * Result of a preferences edit operation.
 */
sealed interface EditResult {
    /** Edit completed successfully */
    object Success : EditResult

    /** Failed to write preferences to disk after retries */
    object FailWrite : EditResult
}

private const val RETRY_COUNT: Int = 3
private const val BASE_DELAY: Long = 500
private const val MAX_DELAY: Long = 5000

private const val TAG = "DataStoreSource"
