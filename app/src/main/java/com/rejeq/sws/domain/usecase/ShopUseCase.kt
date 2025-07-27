package com.rejeq.sws.domain.usecase

import com.github.michaelbull.result.Result
import com.rejeq.sws.domain.entity.CoffeeShopMenu
import com.rejeq.sws.domain.repository.ShopRepository
import com.rejeq.sws.domain.repository.UserActionErrorKind
import javax.inject.Inject
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.isActive

interface ShopUseCase {
    fun getMenuFlow(
        id: Long,
    ): Flow<Result<List<CoffeeShopMenu>, UserActionErrorKind>>

    suspend fun getCurrentMenu(
        id: Long,
    ): Result<List<CoffeeShopMenu>, UserActionErrorKind>
}

class DefaultShopUseCase @Inject constructor(
    private val coffeeShopRepo: ShopRepository,
) : ShopUseCase {
    override fun getMenuFlow(
        id: Long,
    ): Flow<Result<List<CoffeeShopMenu>, UserActionErrorKind>> = flow {
        while (currentCoroutineContext().isActive) {
            emit(getCurrentMenu(id))
            delay(DEFAULT_REFRESH_TIME)
        }
    }

    override suspend fun getCurrentMenu(
        id: Long,
    ): Result<List<CoffeeShopMenu>, UserActionErrorKind> =
        coffeeShopRepo.fetchMenu(id)
}

// const val DEFAULT_REFRESH_TIME: Long = 30_000
const val DEFAULT_REFRESH_TIME: Long = Long.MAX_VALUE
