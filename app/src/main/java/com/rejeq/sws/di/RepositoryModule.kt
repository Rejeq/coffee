package com.rejeq.sws.di

import com.rejeq.sws.data.repository.DefaultAuthRepository
import com.rejeq.sws.data.repository.DefaultLocationsRepository
import com.rejeq.sws.data.repository.DefaultShopRepository
import com.rejeq.sws.domain.repository.AuthRepository
import com.rejeq.sws.domain.repository.LocationsRepository
import com.rejeq.sws.domain.repository.ShopRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun bindAuthRepository(repo: DefaultAuthRepository): AuthRepository

    @Binds
    @Singleton
    abstract fun bindShopRepository(
        repo: DefaultShopRepository,
    ): ShopRepository

    @Binds
    @Singleton
    abstract fun bindLocationsRepository(
        repo: DefaultLocationsRepository,
    ): LocationsRepository
}
