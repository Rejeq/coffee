package com.rejeq.sws.domain.usecase

import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.rejeq.sws.domain.entity.UserAuthState
import com.rejeq.sws.domain.repository.AuthErrorKind
import com.rejeq.sws.domain.repository.AuthRepository
import com.rejeq.sws.domain.repository.LoginErrorKind
import io.mockk.coEvery
import io.mockk.mockk
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlinx.coroutines.test.runTest

class DefaultAuthUseCaseTest {
    private val authRepository = mockk<AuthRepository>()
    private val useCase = DefaultAuthUseCase(authRepository)

    @Test
    fun `register returns PasswordNotSame if passwords do not match`() =
        runTest {
            val result = useCase.register("user", "pass1", "pass2")

            assertEquals(Err(AuthErrorKind.PasswordNotSame), result)
        }

    @Test
    fun `register delegates to repository if passwords match`() = runTest {
        coEvery { authRepository.register("user", "pass") } returns Ok(Unit)

        val result = useCase.register("user", "pass", "pass")

        assertEquals(Ok(Unit), result)
    }

    @Test
    fun `login delegates to repository`() = runTest {
        coEvery { authRepository.login("user", "pass") } returns Ok(Unit)

        val result = useCase.login("user", "pass")

        assertEquals(Ok(Unit), result)
    }

    @Test
    fun `getUserState returns Logged if isLogged is true`() = runTest {
        coEvery { authRepository.isLogged() } returns Ok(true)

        val result = useCase.getUserState()

        assertEquals(UserAuthState.Logged, result)
    }

    @Test
    fun `getUserState returns NotRegistered if wasRegistered is false`() =
        runTest {
            coEvery { authRepository.isLogged() } returns Ok(false)
            coEvery { authRepository.wasRegistered() } returns Ok(false)

            val result = useCase.getUserState()

            assertEquals(UserAuthState.NotRegistered, result)
        }

    @Test
    fun `getUserState returns NotLogged if user not logged`() = runTest {
        coEvery { authRepository.isLogged() } returns Ok(false)
        coEvery { authRepository.wasRegistered() } returns Ok(true)

        val result = useCase.getUserState()

        assertEquals(UserAuthState.NotLogged, result)
    }
}
