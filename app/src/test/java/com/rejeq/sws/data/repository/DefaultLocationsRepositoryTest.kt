package com.rejeq.sws.data.repository

import androidx.datastore.core.IOException
import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.rejeq.sws.data.source.datastore.AuthPreferences
import com.rejeq.sws.data.source.network.SwsCoffeeService
import com.rejeq.sws.domain.entity.LocationPoint
import com.rejeq.sws.domain.entity.Shop
import com.rejeq.sws.domain.repository.NetworkErrorKind
import com.rejeq.sws.domain.repository.UserActionErrorKind
import io.mockk.coEvery
import io.mockk.mockk
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlinx.coroutines.test.runTest

class DefaultLocationsRepositoryTest {
    private val service = mockk<SwsCoffeeService>()
    private val authPreferences = mockk<AuthPreferences>()
    private val repository = DefaultLocationsRepository(
        service,
        authPreferences,
    )

    @Test
    fun `getShopLocations returns shops on success`() = runTest {
        val token = "token"
        val shops = listOf(Shop(1, "Shop1", LocationPoint(30.0, 30.0)))
        coEvery { authPreferences.getAuthToken() } returns Ok(token)
        coEvery { service.fetchLocations(token) } returns Ok(shops)

        val result = repository.getShopLocations()

        assertEquals(Ok(shops), result)
    }

    @Test
    fun `getShopLocations returns error if getAuthToken fails`() = runTest {
        coEvery {
            authPreferences.getAuthToken()
        } returns Err(IOException("fail"))

        val result = repository.getShopLocations()

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
