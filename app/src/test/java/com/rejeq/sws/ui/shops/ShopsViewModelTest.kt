package com.rejeq.sws.ui.shops

import com.github.michaelbull.result.Ok
import com.rejeq.sws.domain.entity.NearShop
import com.rejeq.sws.domain.usecase.ShopLocatorUseCase
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class ShopsViewModelTest {
    private val locatorUseCase = mockk<ShopLocatorUseCase>()

    @Test
    fun `shopsState emits Success when use case returns shops`() = runTest {
        val shops = listOf(NearShop(1, "Espresso", null))
        coEvery { locatorUseCase.getCurrentNearestShops() } returns Ok(shops)
        val vm = DefaultShopsViewModel(locatorUseCase)
        val state = vm.shopsState.first { it is ShopsState.Success }
        assertEquals(ShopsState.Success(shops), state)
    }
} 