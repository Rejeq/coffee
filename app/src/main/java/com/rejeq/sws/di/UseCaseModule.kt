package com.rejeq.sws.di

import com.rejeq.sws.domain.usecase.AuthUseCase
import com.rejeq.sws.domain.usecase.DefaultAuthUseCase
import com.rejeq.sws.domain.usecase.DefaultShopLocatorUseCase
import com.rejeq.sws.domain.usecase.DefaultShopUseCase
import com.rejeq.sws.domain.usecase.DefaultUserLocationUseCase
import com.rejeq.sws.domain.usecase.ShopLocatorUseCase
import com.rejeq.sws.domain.usecase.ShopUseCase
import com.rejeq.sws.domain.usecase.UserLocationUseCase
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class UseCaseModule {
    @Binds
    abstract fun bindAuthUseCase(useCase: DefaultAuthUseCase): AuthUseCase

    @Binds
    abstract fun bindUserLocationUseCase(
        useCase: DefaultUserLocationUseCase,
    ): UserLocationUseCase

    @Binds
    abstract fun bindShopLocatorUseCase(
        useCase: DefaultShopLocatorUseCase,
    ): ShopLocatorUseCase

    @Binds
    abstract fun bindShopUesCase(useCase: DefaultShopUseCase): ShopUseCase
}
