package com.tt.driver.data.models.http
import com.google.gson.annotations.SerializedName;
data class RejectedReason(
    @SerializedName("data")
    val `data`: List<Dataa>
)

data class Dataa(
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("reason_ar")
    val reasonAr: String,
    @SerializedName("reason_en")
    val reasonEn: String,
    @SerializedName("updated_at")
    val updatedAt: String,
    val isSelected : Boolean = false,
)



