package com.rejeq.sws.ui.root

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rejeq.sws.domain.entity.UserAuthState
import com.rejeq.sws.domain.usecase.AuthUseCase
import javax.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn

class RootViewModel @Inject constructor(val authUseCase: AuthUseCase) :
    ViewModel() {
    val navState: StateFlow<NavigationState> = flow {
        val state = authUseCase.getUserState()
        emit(NavigationState.Success(state.toRoute()))
    }.stateIn(viewModelScope, SharingStarted.Eagerly, NavigationState.Loading)

    fun wannaShowScreen(): Boolean = navState.value != NavigationState.Loading
}

sealed interface NavigationState {
    object Loading : NavigationState

    class Success(val startDest: SwsRoute) : NavigationState
}

fun UserAuthState.toRoute(): SwsRoute = when (this) {
    UserAuthState.Logged -> ShopsRoute
    UserAuthState.NotLogged -> LoginRoute
    UserAuthState.NotRegistered -> RegisterRoute
}
