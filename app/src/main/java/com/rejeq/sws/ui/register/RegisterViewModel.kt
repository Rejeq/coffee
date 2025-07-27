package com.rejeq.sws.ui.register

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.michaelbull.result.onFailure
import com.github.michaelbull.result.onSuccess
import com.rejeq.sws.domain.usecase.AuthUseCase
import com.rejeq.sws.ui.register.RegisterViewModel.NavEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

interface RegisterViewModel {
    val navigation: SharedFlow<NavEvent>

    val emailTextField: State<TextFieldValue>
    val passwordTextField: State<TextFieldValue>
    val repeatPasswordTextField: State<TextFieldValue>

    val error: StateFlow<String?>

    fun onEmailChange(value: TextFieldValue)
    fun onPasswordChange(value: TextFieldValue)
    fun onRepeatPasswordChange(value: TextFieldValue)
    fun onRegisterClick()

    sealed class NavEvent {
        object OpenShops : NavEvent()
    }
}

@HiltViewModel
class DefaultRegisterViewModel @Inject constructor(
    private val authUseCase: AuthUseCase,
) : ViewModel(),
    RegisterViewModel {
    private val _navigation = MutableSharedFlow<NavEvent>()
    override val navigation = _navigation.asSharedFlow()

    private val _emailTextField = mutableStateOf(TextFieldValue())
    override val emailTextField = _emailTextField

    private val _passwordTextField = mutableStateOf(TextFieldValue())
    override val passwordTextField = _passwordTextField

    private val _repeatPasswordTextField = mutableStateOf(TextFieldValue())
    override val repeatPasswordTextField = _repeatPasswordTextField

    private val _error = MutableStateFlow<String?>(null)
    override val error = _error.asStateFlow()

    override fun onEmailChange(value: TextFieldValue) {
        _emailTextField.value = value
    }

    override fun onPasswordChange(value: TextFieldValue) {
        _passwordTextField.value = value
    }

    override fun onRepeatPasswordChange(value: TextFieldValue) {
        _repeatPasswordTextField.value = value
    }

    override fun onRegisterClick() {
        viewModelScope.launch {
            authUseCase.register(
                login = emailTextField.value.text,
                password = passwordTextField.value.text,
                repeatPassword = repeatPasswordTextField.value.text,
            ).onSuccess {
                _navigation.emit(NavEvent.OpenShops)
            }.onFailure {
                _error.value = it.toString()
            }
        }
    }
}
