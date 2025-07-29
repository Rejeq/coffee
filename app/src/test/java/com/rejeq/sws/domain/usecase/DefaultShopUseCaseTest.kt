package com.rejeq.sws.domain.usecase

import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.rejeq.sws.domain.entity.CoffeeShopMenu
import com.rejeq.sws.domain.repository.ShopRepository
import com.rejeq.sws.domain.repository.UserActionErrorKind
import io.mockk.coEvery
import io.mockk.mockk
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest

class DefaultShopUseCaseTest {
    private val shopRepository = mockk<ShopRepository>()
    private val useCase = DefaultShopUseCase(shopRepository)

    @Test
    fun `getCurrentMenu returns menu from repository`() = runTest {
        val menu = listOf(CoffeeShopMenu(1, "Espresso", "url", 2.5))
        coEvery { shopRepository.fetchMenu(1) } returns Ok(menu)

        val result = useCase.getCurrentMenu(1)

        assertEquals(Ok(menu), result)
    }

    @Test
    fun `getCurrentMenu returns error from repository`() = runTest {
        val error = Err(UserActionErrorKind.NetworkError(mockk()))
        coEvery { shopRepository.fetchMenu(1) } returns error

        val result = useCase.getCurrentMenu(1)

        assertEquals(error, result)
    }

    @Test
    fun `getMenuFlow emits menu from repository`() = runTest {
        val menu = listOf(CoffeeShopMenu(1, "Espresso", "url", 2.5))
        coEvery { shopRepository.fetchMenu(1) } returns Ok(menu)

        val flow = useCase.getMenuFlow(1)

        assertEquals(Ok(menu), flow.first())
    }
}
