package com.rejeq.sws.data.source.network

import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Result
import com.github.michaelbull.result.mapError
import com.rejeq.sws.domain.entity.CoffeeShopMenu
import com.rejeq.sws.domain.entity.Shop
import com.rejeq.sws.domain.repository.AuthErrorKind
import com.rejeq.sws.domain.repository.LoginErrorKind
import com.rejeq.sws.domain.repository.UserActionErrorKind
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.serialization.Serializable
import retrofit2.HttpException

@Singleton
class SwsCoffeeService @Inject constructor(private val api: SwsCoffeeApi) {
    suspend fun fetchLocations(
        token: String,
    ): Result<List<Shop>, UserActionErrorKind> = tryUserActionCall {
        api.locations("Bearer $token")
    }

    suspend fun fetchMenu(
        token: String,
        shopId: Long,
    ): Result<List<CoffeeShopMenu>, UserActionErrorKind> = tryUserActionCall {
        api.menu("Bearer $token", shopId)
    }

    suspend fun requestLogin(
        login: String,
        password: String,
    ): Result<AuthResponse, LoginErrorKind> = tryNetworkCall {
        try {
            api.login(AuthRequest(login, password))
        } catch (e: HttpException) {
            return when (e.code()) {
                400 -> Err(LoginErrorKind.IncorrectRequest)
                404 -> Err(LoginErrorKind.UnknownUser)
                else -> throw e
            }
        }
    }.mapError { LoginErrorKind.NetworkError(it) }

    suspend fun requestAuth(
        login: String,
        password: String,
    ): Result<AuthResponse, AuthErrorKind> = tryNetworkCall {
        try {
            api.auth(AuthRequest(login, password))
        } catch (e: HttpException) {
            return when (e.code()) {
                400 -> Err(AuthErrorKind.IncorrectRequest)
                406 -> Err(AuthErrorKind.UserAlreadyExist)
                else -> throw e
            }
        }
    }.mapError(AuthErrorKind::NetworkError)
}

private suspend fun <T> tryUserActionCall(
    block: suspend () -> T,
): Result<T, UserActionErrorKind> = tryNetworkCall {
    try {
        block()
    } catch (e: HttpException) {
        when (e.code()) {
            401 -> return Err(UserActionErrorKind.UserNotAuthorized)
            else -> throw e
        }
    }
}.mapError(UserActionErrorKind::NetworkError)

@Serializable
data class AuthResponse(val token: String, val tokenLifetime: Long)
