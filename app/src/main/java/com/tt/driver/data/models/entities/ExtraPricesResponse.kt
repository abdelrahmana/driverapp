package com.tt.driver.data.models.entities

data class ExtraPricesResponse(
    val `data`: List<Data>
)

data class Data(
    val id: Int,
    val name: String,
    val price: String,
    val type: String,
    var checked : Boolean = false
)