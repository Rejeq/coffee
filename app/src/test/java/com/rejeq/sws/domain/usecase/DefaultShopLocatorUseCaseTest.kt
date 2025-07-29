package com.rejeq.sws.domain.usecase

import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.rejeq.sws.domain.entity.Distance
import com.rejeq.sws.domain.entity.LocationPoint
import com.rejeq.sws.domain.entity.NearShop
import com.rejeq.sws.domain.entity.Shop
import com.rejeq.sws.domain.repository.LocationsRepository
import com.rejeq.sws.domain.repository.UserActionErrorKind
import io.mockk.coEvery
import io.mockk.declaringKotlinFile
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.mockkStatic
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlinx.coroutines.test.runTest

class DefaultShopLocatorUseCaseTest {
    private val locationsRepo = mockk<LocationsRepository>()
    private val userLocationUseCase = mockk<UserLocationUseCase>()
    private val useCase = DefaultShopLocatorUseCase(
        locationsRepo,
        userLocationUseCase,
    )

    @Test
    fun `getCurrentShops returns shops from repository`() = runTest {
        val shops = listOf(Shop(1, "Shop1", LocationPoint(1.0, 2.0)))
        coEvery { locationsRepo.getShopLocations() } returns Ok(shops)

        val result = useCase.getCurrentShops()

        assertEquals(Ok(shops), result)
    }

    // TODO: To integration tests
//    @Test
//    fun `getCurrentNearestShops returns mapped NearShop list`() = runTest {
//        val shops = listOf(Shop(1, "Shop1", LocationPoint(1.0, 2.0)))
//        val userLoc = Ok(LocationPoint(1.0, 2.0))
//        coEvery { locationsRepo.getShopLocations() } returns Ok(shops)
//        coEvery { userLocationUseCase.getCurrentLocation() } returns userLoc
//
//        mockkStatic(::distanceBetween)
//        coEvery {
//            distanceBetween(LocationPoint(1.0, 2.0), LocationPoint(1.0, 2.0))
//        } returns Distance(0.0)
//
//        val result = useCase.getCurrentNearestShops()
//
//        assertEquals(
//            Ok(
//                shops.map {
//                    NearShop(it.id, it.name, null)
//                },
//            ),
//            result,
//        )
//    }

    @Test
    fun `getCurrentNearestShops returns error if getShopLocations fails`() =
        runTest {
            coEvery {
                locationsRepo.getShopLocations()
            } returns Err(UserActionErrorKind.NetworkError(mockk()))
            coEvery {
                userLocationUseCase.getCurrentLocation()
            } returns Err(LocationErrorKind.PermissionDenied)

            val result = useCase.getCurrentNearestShops()

            assertEquals(true, result.isErr)
        }
}
