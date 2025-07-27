package com.rejeq.sws.domain.usecase

import android.util.Log
import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Result
import com.github.michaelbull.result.onFailure
import com.github.michaelbull.result.onSuccess
import com.rejeq.sws.domain.entity.UserAuthState
import com.rejeq.sws.domain.repository.AuthErrorKind
import com.rejeq.sws.domain.repository.AuthRepository
import com.rejeq.sws.domain.repository.LoginErrorKind
import javax.inject.Inject

interface AuthUseCase {
    suspend fun login(
        login: String,
        password: String,
    ): Result<Unit, LoginErrorKind>

    suspend fun register(
        login: String,
        password: String,
        repeatPassword: String,
    ): Result<Unit, AuthErrorKind>

    suspend fun getUserState(): UserAuthState
}

class DefaultAuthUseCase @Inject constructor(
    private val authRepository: AuthRepository,
) : AuthUseCase {
    override suspend fun login(
        login: String,
        password: String,
    ): Result<Unit, LoginErrorKind> = authRepository.login(login, password)

    override suspend fun register(
        login: String,
        password: String,
        repeatPassword: String,
    ): Result<Unit, AuthErrorKind> {
        if (password != repeatPassword) {
            return Err(AuthErrorKind.PasswordNotSame)
        }

        return authRepository.register(login, password)
    }

    override suspend fun getUserState(): UserAuthState {
        authRepository.isLogged()
            .onSuccess { isLogged ->
                if (isLogged) {
                    return UserAuthState.Logged
                }
            }
            .onFailure { err ->
                Log.w(TAG, "Unable to check auth status: $err")
            }

        authRepository.wasRegistered()
            .onSuccess { wasRegistered ->
                if (!wasRegistered) {
                    return UserAuthState.NotRegistered
                }
            }
            .onFailure { err ->
                Log.w(TAG, "Unable to check auth status: $err")
            }

        return UserAuthState.NotLogged
    }
}

private const val TAG = "AuthUseCase"
