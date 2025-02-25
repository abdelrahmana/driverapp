package com.tt.driver.data.models.http
import com.google.gson.annotations.SerializedName;
import com.tt.driver.data.models.entities.Order

data class ShipmentDetailsResponse(
    @SerializedName("data")
    val `data`: Order
)
