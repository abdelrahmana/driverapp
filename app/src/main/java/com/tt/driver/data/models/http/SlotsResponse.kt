package com.tt.driver.data.models.http
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class SlotsResponse(
    @SerializedName("data")
    val `data`: List<DataSlot>
)

data class DataSlot(
    @SerializedName("customer_name")
    val customerName: String,
    @SerializedName("delivery_date")
    val deliveryDate: String,
    @SerializedName("delivery_slot")
    val deliverySlot: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("label")
    val label: String,
    @SerializedName("mobile_no")
    val mobileNo: String,
    @SerializedName("shipment_address")
    val shipmentAddress: String
)



