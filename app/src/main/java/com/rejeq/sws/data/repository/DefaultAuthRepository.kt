package com.rejeq.sws.data.repository

import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import com.github.michaelbull.result.map
import com.github.michaelbull.result.onSuccess
import com.rejeq.sws.data.source.datastore.AuthData
import com.rejeq.sws.data.source.datastore.AuthPreferences
import com.rejeq.sws.data.source.network.SwsCoffeeService
import com.rejeq.sws.domain.repository.AuthErrorKind
import com.rejeq.sws.domain.repository.AuthRepository
import com.rejeq.sws.domain.repository.LoginErrorKind
import javax.inject.Inject
import kotlin.time.Clock
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.ExperimentalTime
import okio.IOException

@OptIn(ExperimentalTime::class)
class DefaultAuthRepository @Inject constructor(
    private val service: SwsCoffeeService,
    private val authPreferences: AuthPreferences,
) : AuthRepository {
    override suspend fun login(
        login: String,
        password: String,
    ): Result<Unit, LoginErrorKind> =
        service.requestLogin(login, password).onSuccess {
            val data = AuthData(it.token, it.tokenLifetime, Clock.System.now())
            authPreferences.saveAuthData(data)
            authPreferences.setWasLogged(true)
        }.map {}

    override suspend fun register(
        login: String,
        password: String,
    ): Result<Unit, AuthErrorKind> =
        service.requestAuth(login, password).onSuccess {
            val data = AuthData(it.token, it.tokenLifetime, Clock.System.now())
            authPreferences.saveAuthData(data)

            authPreferences.setWasRegistered(true)
            authPreferences.setWasLogged(true)
        }.map {}

    override suspend fun wasRegistered(): Result<Boolean, IOException> =
        authPreferences.wasRegistered()

    override suspend fun isLogged(): Result<Boolean, IOException> {
        authPreferences.wasLogged().onSuccess { wasLogged ->
            if (!wasLogged) {
                return Ok(false)
            }
        }

        val data = authPreferences.getAuthData()
        val now = Clock.System.now()

        return data.map {
            it.time + it.tokenLifetime.milliseconds > now
        }
    }
}
