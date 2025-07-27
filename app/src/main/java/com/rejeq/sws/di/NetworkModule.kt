package com.rejeq.sws.di

import com.rejeq.sws.data.source.network.SwsCoffeeApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides
    @Singleton
    fun provideSwsCoffeeApi(): SwsCoffeeApi {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl("http://212.41.30.90:35005/")
            .client(okHttpClient)
            .addConverterFactory(
                Json.asConverterFactory(
                    "application/json; charset=UTF8".toMediaType(),
                ),
            )
            .build()

        return retrofit.create(SwsCoffeeApi::class.java)
    }
}
