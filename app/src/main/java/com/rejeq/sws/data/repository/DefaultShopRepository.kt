package com.rejeq.sws.data.repository

import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Result
import com.github.michaelbull.result.mapBoth
import com.rejeq.sws.data.source.datastore.AuthPreferences
import com.rejeq.sws.data.source.network.SwsCoffeeService
import com.rejeq.sws.domain.entity.CoffeeShopMenu
import com.rejeq.sws.domain.repository.NetworkErrorKind
import com.rejeq.sws.domain.repository.ShopRepository
import com.rejeq.sws.domain.repository.UserActionErrorKind
import javax.inject.Inject

class DefaultShopRepository @Inject constructor(
    private val service: SwsCoffeeService,
    private val authPreferences: AuthPreferences,
) : ShopRepository {
    override suspend fun fetchMenu(
        id: Long,
    ): Result<List<CoffeeShopMenu>, UserActionErrorKind> {
        return authPreferences.getAuthToken().mapBoth(
            success = { token ->
                service.fetchMenu(token, id)
            },
            failure = {
                val err = NetworkErrorKind.UnexpectedError
                return Err(UserActionErrorKind.NetworkError(err))
            },
        )
    }
}
