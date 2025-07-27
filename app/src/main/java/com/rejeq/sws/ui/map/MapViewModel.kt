package com.rejeq.sws.ui.map

import android.os.Parcelable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.michaelbull.result.mapBoth
import com.rejeq.sws.domain.entity.Shop
import com.rejeq.sws.domain.usecase.ShopLocatorUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.parcelize.Parcelize

interface MapViewModel {
    val marksState: StateFlow<MarksState>
}

@HiltViewModel
class DefaultMapViewModel @Inject constructor(
    shopLocatorUseCase: ShopLocatorUseCase,
) : ViewModel(),
    MapViewModel {
    private val _marksState = MutableStateFlow<MarksState>(MarksState.Loading)
    override val marksState = _marksState.asStateFlow()

    init {
        viewModelScope.launch {
            _marksState.value = shopLocatorUseCase.getCurrentShops().mapBoth(
                success = { shops ->
                    MarksState.Success(shops.map(::toMapPlacemark))
                },
                failure = { MarksState.Error(it.toString()) },
            )
        }
    }
}

sealed interface MarksState {
    object Loading : MarksState
    data class Error(val message: String) : MarksState
    data class Success(val marks: List<MapPlacemark>) : MarksState
}

@Parcelize
data class MapPlacemark(val id: Long, val point: MapPoint, val name: String) :
    Parcelable

@Parcelize
data class MapPoint(val latitude: Double, val longitude: Double) : Parcelable

fun toMapPlacemark(shop: Shop) = MapPlacemark(
    id = shop.id,
    point = MapPoint(
        latitude = shop.point.latitude,
        longitude = shop.point.longitude,
    ),
    name = shop.name,
)
