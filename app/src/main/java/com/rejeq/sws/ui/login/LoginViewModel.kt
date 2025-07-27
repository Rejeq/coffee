package com.rejeq.sws.ui.login

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.michaelbull.result.onFailure
import com.github.michaelbull.result.onSuccess
import com.rejeq.sws.domain.usecase.AuthUseCase
import com.rejeq.sws.ui.login.LoginViewModel.NavEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

interface LoginViewModel {
    val navigation: SharedFlow<NavEvent>

    val emailTextField: State<TextFieldValue>
    val passwordTextField: State<TextFieldValue>

    val error: StateFlow<String?>

    fun onEmailChange(value: TextFieldValue)
    fun onPasswordChange(value: TextFieldValue)
    fun onLoginClick()

    sealed class NavEvent {
        object OpenShops : NavEvent()
    }
}

@HiltViewModel
class DefaultLoginViewModel @Inject constructor(
    private val authUseCase: AuthUseCase,
) : ViewModel(),
    LoginViewModel {
    private val _navigation = MutableSharedFlow<NavEvent>()
    override val navigation = _navigation.asSharedFlow()

    private val _emailTextField = mutableStateOf(TextFieldValue())
    override val emailTextField = _emailTextField

    private val _passwordTextField = mutableStateOf(TextFieldValue())
    override val passwordTextField = _passwordTextField

    private val _error = MutableStateFlow<String?>(null)
    override val error = _error.asStateFlow()

    override fun onEmailChange(value: TextFieldValue) {
        _emailTextField.value = value
    }

    override fun onPasswordChange(value: TextFieldValue) {
        _passwordTextField.value = value
    }

    override fun onLoginClick() {
        viewModelScope.launch {
            authUseCase.login(
                login = emailTextField.value.text,
                password = passwordTextField.value.text,
            ).onSuccess {
                _navigation.emit(NavEvent.OpenShops)
            }.onFailure {
                _error.value = it.toString()
            }
        }
    }
}
