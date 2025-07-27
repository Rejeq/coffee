package com.rejeq.sws.domain.usecase

import android.location.Location
import android.util.Log
import com.github.michaelbull.result.Result
import com.github.michaelbull.result.getOr
import com.github.michaelbull.result.map
import com.github.michaelbull.result.onFailure
import com.rejeq.sws.domain.entity.Distance
import com.rejeq.sws.domain.entity.LocationPoint
import com.rejeq.sws.domain.entity.NearShop
import com.rejeq.sws.domain.entity.Shop
import com.rejeq.sws.domain.repository.LocationsRepository
import com.rejeq.sws.domain.repository.UserActionErrorKind
import javax.inject.Inject
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

interface ShopLocatorUseCase {
    suspend fun getCurrentShops(): Result<List<Shop>, UserActionErrorKind>

    suspend fun getCurrentNearestShops():
        Result<List<NearShop>, UserActionErrorKind>
}

class DefaultShopLocatorUseCase @Inject constructor(
    private val locationsRepo: LocationsRepository,
    private val userLocationUseCase: UserLocationUseCase,
) : ShopLocatorUseCase {

    override suspend fun getCurrentShops():
        Result<List<Shop>, UserActionErrorKind> =
        locationsRepo.getShopLocations()

    override suspend fun getCurrentNearestShops():
        Result<List<NearShop>, UserActionErrorKind> =
        coroutineScope {
            val userPos = async {
                userLocationUseCase.getCurrentLocation().onFailure { err ->
                    Log.w(TAG, "Unable to get user location: $err")
                }.getOr(null)
            }

            locationsRepo.getShopLocations().map { shops ->
                val userPos = userPos.await()

                shops.map { it.toNearShop(userPos) }
            }
        }
}

fun Shop.toNearShop(pos: LocationPoint?) = NearShop(
    id = this.id,
    name = this.name,
    distance = pos?.let { pos ->
        distanceBetween(
            pos,
            this.point,
        )
    },
)

fun distanceBetween(from: LocationPoint, to: LocationPoint): Distance {
    val fromLocation = Location("").apply {
        latitude = from.latitude
        longitude = from.longitude
    }

    val toLocation = Location("").apply {
        latitude = to.latitude
        longitude = to.longitude
    }

    val distance = fromLocation.distanceTo(toLocation).toDouble()
    return Distance(distance)
}

private const val TAG = "CoffeeLocatorUseCase"
