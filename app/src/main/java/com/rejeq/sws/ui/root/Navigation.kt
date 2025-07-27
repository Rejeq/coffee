package com.rejeq.sws.ui.root

import kotlinx.serialization.Serializable

@Serializable
sealed interface SwsRoute

@Serializable
data object RegisterRoute : SwsRoute

@Serializable
data object LoginRoute : SwsRoute

@Serializable
data object ShopsRoute : SwsRoute

@Serializable
data object MapRoute : SwsRoute

@Serializable
data object MenuRoute : SwsRoute

@Serializable
data object OrderRoute : SwsRoute
