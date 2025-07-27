package com.rejeq.sws.ui.order

import androidx.compose.runtime.mutableStateListOf

class PreviewOrderViewModel : OrderViewModel {
    override val order = mutableStateListOf(
        OrderItemState(
            id = 128,
            name = "Cappuccino",
            price = 150.0,
            quantity = 8,
        ),
        OrderItemState(
            id = 129,
            name = "Espresso",
            price = 100.0,
            quantity = 2,
        ),
        OrderItemState(
            id = 130,
            name = "Latte",
            price = 120.0,
            quantity = 3,
        ),
    )

    override fun changeQuantity(state: OrderItemState, quantity: Int) {
        TODO("Not yet implemented")
    }
}
