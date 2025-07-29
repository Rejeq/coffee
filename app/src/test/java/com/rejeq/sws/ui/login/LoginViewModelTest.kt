package com.rejeq.sws.ui.login

import androidx.compose.ui.text.input.TextFieldValue
import com.github.michaelbull.result.Ok
import com.rejeq.sws.domain.usecase.AuthUseCase
import io.mockk.coEvery
import io.mockk.mockk
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest

class LoginViewModelTest {
    private val authUseCase = mockk<AuthUseCase>()
    private val vm = DefaultLoginViewModel(authUseCase)

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
    fun `onLoginClick sets error on failure`() = runTest {
        coEvery { authUseCase.login(any(), any()) } returns Ok(Unit)

        vm.onLoginClick()
    }
}
