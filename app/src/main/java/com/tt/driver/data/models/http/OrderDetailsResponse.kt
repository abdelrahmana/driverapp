package com.tt.driver.data.models.http

import com.tt.driver.data.models.entities.Order

data class OrderDetailsResponse(
    val `data`: Order?,
    val distance_time: DistanceTime?
) {
    data class DistanceTime(
        val destination: Destination?,
        val pickup: Pickup?
    ) {
        data class Destination(
            val distance: String?,
            val duration: String?
        )

        data class Pickup(
            val distance: String?,
            val duration: String?
        )
    }
}