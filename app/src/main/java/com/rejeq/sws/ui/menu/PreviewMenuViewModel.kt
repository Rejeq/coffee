package com.rejeq.sws.ui.menu

import com.rejeq.sws.ui.order.OrderState
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow

class PreviewMenuViewModel : MenuViewModel {
    override val navigation = MutableSharedFlow<MenuViewModel.NavEvent>()

    private val menu = listOf(
        MenuItemState(
            id = 128,
            title = "Cappuccino",
            price = 150.0,
            imageUrl = "",
            quantity = 0,
        ),
        MenuItemState(
            id = 129,
            title = "Espresso",
            price = 100.0,
            imageUrl = "",
            quantity = 0,
        ),
        MenuItemState(
            id = 130,
            title = "Latte",
            price = 120.0,
            imageUrl = "",
            quantity = 0,
        ),
        MenuItemState(
            id = 131,
            title = "Mocha",
            price = 130.0,
            imageUrl = "",
            quantity = 0,
        ),
        MenuItemState(
            id = 132,
            title = "Americano",
            price = 110.0,
            imageUrl = "",
            quantity = 0,
        ),
        MenuItemState(
            id = 133,
            title = "Macchiato",
            price = 140.0,
            imageUrl = "",
            quantity = 0,
        ),
    )

    override val menuState = MutableStateFlow(
        MenuState.Success(menu),
    )

    override fun changeQuantity(state: MenuItemState, newQuantity: Int) {
        TODO("Not yet implemented")
    }

    override fun getOrderState(): OrderState? {
        TODO("Not yet implemented")
    }
}
