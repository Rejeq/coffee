package com.rejeq.sws.ui.login

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class PreviewLoginViewModel @Inject constructor() :
    ViewModel(),
    LoginViewModel {
    override val navigation = MutableSharedFlow<LoginViewModel.NavEvent>()

    override val emailTextField = mutableStateOf(TextFieldValue())
    override val passwordTextField = mutableStateOf(TextFieldValue())
    override val error = MutableStateFlow("There an error")

    override fun onEmailChange(value: TextFieldValue) {
        TODO("Not yet implemented")
    }

    override fun onPasswordChange(value: TextFieldValue) {
        TODO("Not yet implemented")
    }

    override fun onLoginClick() {
        TODO("Not yet implemented")
    }
}
