package com.rejeq.sws.data.repository

import com.github.michaelbull.result.Ok
import com.rejeq.sws.data.source.datastore.AuthData
import com.rejeq.sws.data.source.datastore.AuthPreferences
import com.rejeq.sws.data.source.network.AuthResponse
import com.rejeq.sws.data.source.network.SwsCoffeeService
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.mockkStatic
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.time.Clock
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.ExperimentalTime
import kotlin.time.Instant
import kotlinx.coroutines.test.runTest

@OptIn(ExperimentalTime::class)
class DefaultAuthRepositoryTest {
    private val service = mockk<SwsCoffeeService>()
    private val authPreferences = mockk<AuthPreferences>()
    private val repository = DefaultAuthRepository(service, authPreferences)

    @Test
    fun `login saves AuthData and sets wasLogged`() = runTest {
        val token = "token"
        val tokenLifetime = 1000L
        val response = AuthResponse(
            token = token,
            tokenLifetime = tokenLifetime,
        )

        coEvery { service.requestLogin(any(), any()) } returns Ok(response)
        coEvery { authPreferences.saveAuthData(any()) } returns Unit
        coEvery { authPreferences.setWasLogged(true) } returns Unit

        val result = repository.login("user", "pass")

        assertEquals(result, Ok(Unit))
        coVerify { authPreferences.saveAuthData(any()) }
        coVerify { authPreferences.setWasLogged(true) }
    }

    @Test
    fun `register saves AuthData, wasRegistered and wasLogged`() = runTest {
        val token = "token"
        val tokenLifetime = 1000L
        val response = AuthResponse(
            token = token,
            tokenLifetime = tokenLifetime,
        )

        coEvery { service.requestAuth(any(), any()) } returns Ok(response)
        coEvery { authPreferences.saveAuthData(any()) } returns Unit
        coEvery { authPreferences.setWasRegistered(true) } returns Unit
        coEvery { authPreferences.setWasLogged(true) } returns Unit

        val result = repository.register("user", "pass")

        assertEquals(result, Ok(Unit))
        coVerify { authPreferences.saveAuthData(any()) }
        coVerify { authPreferences.setWasRegistered(true) }
        coVerify { authPreferences.setWasLogged(true) }
    }

    @Test
    fun `wasRegistered delegates to authPreferences`() = runTest {
        coEvery { authPreferences.wasRegistered() } returns Ok(true)

        val result = repository.wasRegistered()

        assertEquals(Ok(true), result)
    }

    @Test
    fun `isLogged delegates to authPreferences`() = runTest {
        coEvery { authPreferences.wasLogged() } returns Ok(false)

        val result = repository.isLogged()

        assertEquals(Ok(false), result)
    }

    @Test
    fun `isLogged returns true if token is valid`() = runTest {
        coEvery { authPreferences.wasLogged() } returns Ok(true)
        val authData = AuthData(
            "token",
            10000L,
            Clock.System.now(),
        )

        coEvery { authPreferences.getAuthData() } returns Ok(authData)

        val result = repository.isLogged()

        assertEquals(Ok(true), result)
    }

    @Test
    fun `isLogged returns false if token is expired`() = runTest {
        coEvery { authPreferences.wasLogged() } returns Ok(true)
        val authData = AuthData(
            "token",
            1000L,
            Clock.System.now() - 1500.milliseconds,
        )

        coEvery { authPreferences.getAuthData() } returns Ok(authData)

        val result = repository.isLogged()

        assertEquals(Ok(false), result)
    }
}
