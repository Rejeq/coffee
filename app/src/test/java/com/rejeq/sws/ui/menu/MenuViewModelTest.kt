package com.rejeq.sws.ui.menu

import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.rejeq.sws.domain.repository.UserActionErrorKind
import com.rejeq.sws.domain.usecase.ShopUseCase
import com.rejeq.sws.ui.menu.DefaultMenuViewModel
import com.rejeq.sws.ui.menu.MenuItemState
import com.rejeq.sws.ui.menu.MenuState
import io.mockk.coEvery
import io.mockk.mockk
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest

@OptIn(ExperimentalCoroutinesApi::class)
class MenuViewModelTest {
    private val shopUseCase = mockk<ShopUseCase>()
    private val menuFlow = MutableStateFlow(
        Ok(
            listOf(
                com.rejeq.sws.domain.entity.CoffeeShopMenu(
                    1,
                    "Espresso",
                    "url",
                    2.5,
                ),
            ),
        ),
    )

    @Test
    fun `menuState emits Success when use case returns menu`() = runTest {
        coEvery { shopUseCase.getMenuFlow(any()) } returns menuFlow
        val vm = DefaultMenuViewModel(shopUseCase, 1)

        val state = vm.menuState.first { it is MenuState.Success }

        assertTrue(state is MenuState.Success)
        assertEquals(1, (state as MenuState.Success).menu.size)
    }

    @Test
    fun `menuState emits Error and navigation event on UserNotAuthorized`() =
        runTest {
            val errorFlow = MutableStateFlow(
                Err(UserActionErrorKind.UserNotAuthorized),
            )
            coEvery { shopUseCase.getMenuFlow(any()) } returns errorFlow
            val vm = DefaultMenuViewModel(shopUseCase, 1)

            val state = vm.menuState.first { it is MenuState.Error }

            assertTrue(state is MenuState.Error)
        }

    @Test
    fun `changeQuantity updates state correctly`() = runTest {
        coEvery { shopUseCase.getMenuFlow(any()) } returns menuFlow
        val vm = DefaultMenuViewModel(shopUseCase, 1)
        val itemState = MenuItemState(1, "Espresso", "url", 2.5, 0)

        vm.changeQuantity(itemState, 2)
        val state = vm.menuState.first { it is MenuState.Success }

        assertEquals(2, (state as MenuState.Success).menu.first().quantity)
    }
}
