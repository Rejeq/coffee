package com.rejeq.sws.data.repository

import androidx.datastore.core.IOException
import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.rejeq.sws.data.source.datastore.AuthPreferences
import com.rejeq.sws.data.source.network.SwsCoffeeService
import com.rejeq.sws.domain.entity.CoffeeShopMenu
import com.rejeq.sws.domain.repository.NetworkErrorKind
import com.rejeq.sws.domain.repository.UserActionErrorKind
import io.mockk.coEvery
import io.mockk.mockk
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlinx.coroutines.test.runTest

class DefaultShopRepositoryTest {
    private val service = mockk<SwsCoffeeService>()
    private val authPreferences = mockk<AuthPreferences>()
    private val repository = DefaultShopRepository(service, authPreferences)

    @Test
    fun `fetchMenu returns menu on success`() = runTest {
        val token = "token"
        val menu = listOf(CoffeeShopMenu(1, "Espresso", "url", 2.5))
        coEvery { authPreferences.getAuthToken() } returns Ok(token)
        coEvery { service.fetchMenu(token, 1) } returns Ok(menu)

        val result = repository.fetchMenu(1)
        assertEquals(Ok(menu), result)
    }

    @Test
    fun `fetchMenu returns error if getAuthToken fails`() = runTest {
        coEvery {
            authPreferences.getAuthToken()
        } returns Err(IOException("fail"))

        val result = repository.fetchMenu(1)

        assertEquals(
            Err(
                UserActionErrorKind.NetworkError(
                    NetworkErrorKind.UnexpectedError,
                ),
            ),
            result,
        )
    }
}
