package com.rejeq.sws.ui.shops

import com.rejeq.sws.domain.entity.Distance
import com.rejeq.sws.domain.entity.NearShop
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow

class PreviewShopsViewModel : ShopsViewModel {
    override val navigation = MutableSharedFlow<ShopsViewModel.NavEvent>()

    private val list = listOf(
        NearShop(123, "Shop 1", Distance(3000.0)),
        NearShop(124, "Shop 2", Distance(4000.0)),
        NearShop(125, "Shop 3", Distance(5000.0)),
    )

    override val shopsState = MutableStateFlow(ShopsState.Success(list))

    override fun onLocationRequested() {
        TODO("Not yet implemented")
    }
}
