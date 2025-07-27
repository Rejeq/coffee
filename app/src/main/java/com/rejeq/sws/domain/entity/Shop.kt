package com.rejeq.sws.domain.entity

import kotlinx.serialization.Serializable

@Serializable
data class LocationPoint(val latitude: Double, val longitude: Double)

@Serializable
data class Shop(val id: Long, val name: String, val point: LocationPoint)

// Distance - store value in meters
@Serializable
@JvmInline
value class Distance(val value: Double)

@Serializable
class NearShop(val id: Long, val name: String, val distance: Distance?)
