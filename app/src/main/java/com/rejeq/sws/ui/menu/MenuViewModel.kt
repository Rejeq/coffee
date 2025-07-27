package com.rejeq.sws.ui.menu

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.michaelbull.result.mapBoth
import com.rejeq.sws.domain.repository.UserActionErrorKind
import com.rejeq.sws.domain.usecase.ShopUseCase
import com.rejeq.sws.ui.menu.MenuViewModel.NavEvent
import com.rejeq.sws.ui.order.OrderItemState
import com.rejeq.sws.ui.order.OrderState
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

interface MenuViewModel {
    val navigation: SharedFlow<NavEvent>

    val menuState: StateFlow<MenuState>

    // NOTE: Should update value within single frame
    fun changeQuantity(state: MenuItemState, newQuantity: Int)

    fun getOrderState(): OrderState?

    sealed class NavEvent {
        object BackToLogin : NavEvent()
    }
}

@HiltViewModel(assistedFactory = DefaultMenuViewModel.Factory::class)
class DefaultMenuViewModel @AssistedInject constructor(
    shopUseCase: ShopUseCase,
    @Assisted private val id: Long,
) : ViewModel(),
    MenuViewModel {
    private val _navigation = MutableSharedFlow<NavEvent>()
    override val navigation = _navigation.asSharedFlow()

    private val quantities = MutableStateFlow<Map<Long, Int>>(emptyMap())

    override val menuState = combine(
        shopUseCase.getMenuFlow(id),
        quantities,
    ) { menuResult, quantities ->
        menuResult.mapBoth(
            success = { items ->
                MenuState.Success(
                    items.map { item ->
                        item.toMenuItemState(
                            quantities.getOrDefault(item.id, 0),
                        )
                    },
                )
            },
            failure = { err ->
                if (err == UserActionErrorKind.UserNotAuthorized) {
                    _navigation.emit(NavEvent.BackToLogin)
                }

                MenuState.Error(err.toString())
            },
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5_000),
        MenuState.Loading,
    )

    override fun changeQuantity(state: MenuItemState, newQuantity: Int) {
        if (newQuantity >= 0) {
            quantities.update { it + (state.id to newQuantity) }
        }
    }

    override fun getOrderState(): OrderState? {
        val state = menuState.value
        if (state !is MenuState.Success) {
            return null
        }

        return OrderState(
            items = state.menu.mapNotNull { item ->
                if (item.quantity > 0) {
                    item.toOrderItemState()
                } else {
                    null
                }
            },
        )
    }

    @AssistedFactory
    interface Factory {
        fun create(id: Long): DefaultMenuViewModel
    }
}

fun MenuItemState.toOrderItemState() = OrderItemState(
    id = id,
    name = title,
    price = price,
    quantity = quantity,
)

sealed interface MenuState {
    object Loading : MenuState
    data class Error(val message: String) : MenuState
    data class Success(val menu: List<MenuItemState>) : MenuState
}
