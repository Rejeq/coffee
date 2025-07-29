package com.rejeq.sws.ui.register

import androidx.compose.ui.text.input.TextFieldValue
import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.rejeq.sws.domain.repository.AuthErrorKind
import com.rejeq.sws.domain.usecase.AuthUseCase
import io.mockk.coEvery
import io.mockk.mockk
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest

class RegisterViewModelTest {
    private val authUseCase = mockk<AuthUseCase>()
    private val vm = DefaultRegisterViewModel(authUseCase)

    @Test
    fun `onEmailChange updates emailTextField`() {
        val value = TextFieldValue("test@example.com")

        vm.onEmailChange(value)

        assertEquals(value, vm.emailTextField.value)
    }

    @Test
    fun `onPasswordChange updates passwordTextField`() {
        val value = TextFieldValue("password")

        vm.onPasswordChange(value)

        assertEquals(value, vm.passwordTextField.value)
    }

    @Test
    fun `onRepeatPasswordChange updates repeatPasswordTextField`() {
        val value = TextFieldValue("password")

        vm.onRepeatPasswordChange(value)

        assertEquals(value, vm.repeatPasswordTextField.value)
    }
}
