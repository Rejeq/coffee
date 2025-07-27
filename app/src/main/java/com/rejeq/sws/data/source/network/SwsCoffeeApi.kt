package com.rejeq.sws.data.source.network

import com.rejeq.sws.domain.entity.CoffeeShopMenu
import com.rejeq.sws.domain.entity.Shop
import kotlinx.serialization.Serializable
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface SwsCoffeeApi {
    @GET("locations")
    suspend fun locations(@Header("Authorization") token: String): List<Shop>

    @GET("location/{id}/menu")
    suspend fun menu(
        @Header("Authorization") token: String,
        @Path("id") coffeeId: Long,
    ): List<CoffeeShopMenu>

    @POST("auth/login")
    suspend fun login(@Body auth: AuthRequest): AuthResponse

    @POST("auth/register")
    suspend fun auth(@Body auth: AuthRequest): AuthResponse
}

@Serializable
data class AuthRequest(val login: String, val password: String)
