package com.rejeq.sws.domain.repository

import androidx.datastore.core.IOException
import com.github.michaelbull.result.Result
import kotlinx.serialization.Serializable

interface AuthRepository {
    suspend fun login(
        login: String,
        password: String,
    ): Result<Unit, LoginErrorKind>

    suspend fun register(
        login: String,
        password: String,
    ): Result<Unit, AuthErrorKind>

    suspend fun wasRegistered(): Result<Boolean, IOException>

    suspend fun isLogged(): Result<Boolean, IOException>
}

@Serializable
sealed interface NetworkErrorKind {
    data object MalformedData : NetworkErrorKind
    data object ConnectTimeoutException : NetworkErrorKind
    data object ConnectionRefused : NetworkErrorKind
    data object UnresolvedAddress : NetworkErrorKind
    data class UnexpectedHttpError(val status: Int, val description: String) :
        NetworkErrorKind
    data object UnexpectedError : NetworkErrorKind
}

@Serializable
sealed interface LoginErrorKind {
    data object IncorrectRequest : LoginErrorKind
    data object UnknownUser : LoginErrorKind
    data class NetworkError(val kind: NetworkErrorKind) : LoginErrorKind
}

@Serializable
sealed interface AuthErrorKind {
    data object IncorrectRequest : AuthErrorKind
    data object UserAlreadyExist : AuthErrorKind
    data object PasswordNotSame : AuthErrorKind
    data class NetworkError(val kind: NetworkErrorKind) : AuthErrorKind
}
