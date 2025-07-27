package com.rejeq.sws.data.repository

import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Result
import com.github.michaelbull.result.mapBoth
import com.rejeq.sws.data.source.datastore.AuthPreferences
import com.rejeq.sws.data.source.network.SwsCoffeeService
import com.rejeq.sws.domain.entity.Shop
import com.rejeq.sws.domain.repository.LocationsRepository
import com.rejeq.sws.domain.repository.NetworkErrorKind
import com.rejeq.sws.domain.repository.UserActionErrorKind
import javax.inject.Inject

class DefaultLocationsRepository @Inject constructor(
    private val service: SwsCoffeeService,
    private val authPreferences: AuthPreferences,
) : LocationsRepository {
    override suspend fun getShopLocations():
        Result<List<Shop>, UserActionErrorKind> {
        return authPreferences.getAuthToken().mapBoth(
            success = { token ->
                service.fetchLocations(token)
            },
            failure = {
                val err = NetworkErrorKind.UnexpectedError
                return Err(UserActionErrorKind.NetworkError(err))
            },
        )
    }
}
