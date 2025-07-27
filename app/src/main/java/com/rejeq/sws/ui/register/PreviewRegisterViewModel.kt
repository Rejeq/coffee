package com.rejeq.sws.ui.register

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.text.input.TextFieldValue
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class PreviewRegisterViewModel : RegisterViewModel {
    override val emailTextField = mutableStateOf(TextFieldValue())
    override val passwordTextField = mutableStateOf(TextFieldValue())
    override val repeatPasswordTextField = mutableStateOf(TextFieldValue())

    override val error = MutableStateFlow(null)

    override val navigation = MutableSharedFlow<RegisterViewModel.NavEvent>()

    override fun onEmailChange(value: TextFieldValue) {
        TODO("Not yet implemented")
    }

    override fun onPasswordChange(value: TextFieldValue) {
        TODO("Not yet implemented")
    }

    override fun onRepeatPasswordChange(value: TextFieldValue) {
        TODO("Not yet implemented")
    }

    override fun onRegisterClick() {
        TODO("Not yet implemented")
    }
}
