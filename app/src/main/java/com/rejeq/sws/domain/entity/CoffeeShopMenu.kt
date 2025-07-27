package com.rejeq.sws.domain.entity

import kotlinx.serialization.Serializable

@Serializable
data class CoffeeShopMenu(
    val id: Long,
    val name: String,
    val imageURL: String,
    val price: Double,
)
