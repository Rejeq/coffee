package com.rejeq.sws.ui.order

import kotlin.test.Test
import kotlin.test.assertEquals

class OrderViewModelTest {
    @Test
    fun `changeQuantity updates the order item correctly`() {
        val initialItems = listOf(
            OrderItemState(1, "Espresso", 2.5, 1),
            OrderItemState(2, "Latte", 3.0, 2),
        )
        val vm = DefaultOrderViewModel(OrderState(initialItems))
        val updatedItem = initialItems[0].copy(quantity = 5)

        vm.changeQuantity(initialItems[0], 5)

        assertEquals(5, vm.order[0].quantity)
        assertEquals(updatedItem, vm.order[0])
    }
}
