package com.rejeq.sws.domain.repository

import com.github.michaelbull.result.Result
import com.rejeq.sws.domain.entity.CoffeeShopMenu

interface ShopRepository {
    suspend fun fetchMenu(
        id: Long,
    ): Result<List<CoffeeShopMenu>, UserActionErrorKind>
}
