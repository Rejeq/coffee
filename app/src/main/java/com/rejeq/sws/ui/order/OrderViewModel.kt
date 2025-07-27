package com.rejeq.sws.ui.order

import android.os.Parcelable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.ViewModel
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.parcelize.Parcelize

interface OrderViewModel {
    val order: SnapshotStateList<OrderItemState>

    fun changeQuantity(state: OrderItemState, quantity: Int)
}

@Parcelize
data class OrderState(val items: List<OrderItemState>) : Parcelable

@Parcelize
data class OrderItemState(
    val id: Long,
    val name: String,
    val price: Double,
    val quantity: Int,
) : Parcelable

@HiltViewModel(assistedFactory = DefaultOrderViewModel.Factory::class)
class DefaultOrderViewModel @AssistedInject constructor(
    @Assisted list: OrderState,
) : ViewModel(),
    OrderViewModel {
    override val order = list.items.toMutableStateList()

    override fun changeQuantity(state: OrderItemState, quantity: Int) {
        val idx = order.indexOf(state)
        order[idx] = state.copy(quantity = quantity)
    }

    @AssistedFactory
    interface Factory {
        fun create(list: OrderState): DefaultOrderViewModel
    }
}
