package com.rejeq.sws.data.source.datastore

import com.github.michaelbull.result.Result
import com.github.michaelbull.result.map
import javax.inject.Inject
import kotlin.time.ExperimentalTime
import kotlin.time.Instant
import okio.IOException

interface AuthPreferences {
    suspend fun wasRegistered(): Result<Boolean, IOException>
    suspend fun setWasRegistered(state: Boolean)

    suspend fun wasLogged(): Result<Boolean, IOException>
    suspend fun setWasLogged(state: Boolean)

    suspend fun saveAuthData(data: AuthData)
    suspend fun getAuthToken(): Result<String, IOException>
    suspend fun getAuthData(): Result<AuthData, IOException>
}

class DefaultAuthPreferences @Inject constructor(val source: DataStoreSource) :
    AuthPreferences {
    override suspend fun wasRegistered(): Result<Boolean, IOException> =
        source.tryReadLatest().map { pref ->
            pref[PreferencesKeys.WAS_REGISTERED] ?: false
        }

    override suspend fun setWasRegistered(state: Boolean) {
        source.tryEdit {
            it[PreferencesKeys.WAS_REGISTERED] = state
        }
    }

    override suspend fun wasLogged(): Result<Boolean, IOException> =
        source.tryReadLatest().map { pref ->
            pref[PreferencesKeys.WAS_LOGGED] ?: false
        }

    override suspend fun setWasLogged(state: Boolean) {
        source.tryEdit {
            it[PreferencesKeys.WAS_LOGGED] = state
        }
    }

    @OptIn(ExperimentalTime::class)
    override suspend fun saveAuthData(data: AuthData) {
        source.tryEdit { pref ->
            pref[PreferencesKeys.LOG_TOKEN] = data.token
            pref[PreferencesKeys.LOG_TOKEN_LIFETIME] = data.tokenLifetime
            pref[PreferencesKeys.LOG_TOKEN_TIME] =
                data.time.toEpochMilliseconds()
        }
    }

    override suspend fun getAuthToken(): Result<String, IOException> =
        source.tryReadLatest().map { pref ->
            pref[PreferencesKeys.LOG_TOKEN] ?: ""
        }

    @OptIn(ExperimentalTime::class)
    override suspend fun getAuthData(): Result<AuthData, IOException> =
        source.tryReadLatest().map { pref ->
            val token = pref[PreferencesKeys.LOG_TOKEN] ?: ""
            val tokenLifetime = pref[PreferencesKeys.LOG_TOKEN_LIFETIME] ?: 0L
            val tokenTime = pref[PreferencesKeys.LOG_TOKEN_TIME] ?: 0L

            AuthData(
                token,
                tokenLifetime,
                Instant.fromEpochMilliseconds(tokenTime),
            )
        }
}

@OptIn(ExperimentalTime::class)
class AuthData(val token: String, val tokenLifetime: Long, val time: Instant)
