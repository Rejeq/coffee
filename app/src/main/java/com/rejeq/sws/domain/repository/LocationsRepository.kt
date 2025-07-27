package com.rejeq.sws.domain.repository

import com.github.michaelbull.result.Result
import com.rejeq.sws.domain.entity.Shop
import kotlinx.serialization.Serializable

interface LocationsRepository {
    suspend fun getShopLocations(): Result<List<Shop>, UserActionErrorKind>
}

@Serializable
sealed interface UserActionErrorKind {
    data object InvalidRequest : UserActionErrorKind
    data object UserNotAuthorized : UserActionErrorKind
    data object UserNotFound : UserActionErrorKind
    data class NetworkError(val kind: NetworkErrorKind) : UserActionErrorKind
}
