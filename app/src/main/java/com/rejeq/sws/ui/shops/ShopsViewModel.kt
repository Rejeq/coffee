package com.rejeq.sws.ui.shops

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.michaelbull.result.mapBoth
import com.rejeq.sws.domain.entity.NearShop
import com.rejeq.sws.domain.repository.UserActionErrorKind
import com.rejeq.sws.domain.usecase.ShopLocatorUseCase
import com.rejeq.sws.ui.shops.ShopsViewModel.NavEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

interface ShopsViewModel {
    val navigation: SharedFlow<NavEvent>

    val shopsState: StateFlow<ShopsState>

    fun onLocationRequested()

    sealed class NavEvent {
        object BackToLogin : NavEvent()
    }
}

@HiltViewModel
class DefaultShopsViewModel @Inject constructor(
    private val locatorUseCase: ShopLocatorUseCase,
) : ViewModel(),
    ShopsViewModel {
    private val _navigation = MutableSharedFlow<NavEvent>()
    override val navigation = _navigation.asSharedFlow()

    private val _shopsState = MutableStateFlow<ShopsState>(ShopsState.Loading)

    override val shopsState = _shopsState.asStateFlow()

    init {
        viewModelScope.launch {
            _shopsState.value = getShopsState()
        }
    }

    override fun onLocationRequested() {
        viewModelScope.launch {
            _shopsState.value = getShopsState()
        }
    }

    private suspend fun getShopsState(): ShopsState =
        locatorUseCase.getCurrentNearestShops().mapBoth(
            success = { shops ->
                ShopsState.Success(shops)
            },
            failure = { err ->
                if (err == UserActionErrorKind.UserNotAuthorized) {
                    _navigation.emit(NavEvent.BackToLogin)
                }

                ShopsState.Error(err.toString())
            },
        )
}

sealed interface ShopsState {
    object Loading : ShopsState

    data class Error(val message: String) : ShopsState
    data class Success(val shops: List<NearShop>) : ShopsState
}
