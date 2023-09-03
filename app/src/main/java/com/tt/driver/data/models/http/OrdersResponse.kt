package com.tt.driver.data.models.http

import com.tt.driver.data.models.entities.Order

data class OrdersResponse(val `data`: Data)

data class Data(
    val `data`: List<Order>,
    val links: Links,
    val meta: Meta
)
data class Links(
    val first: String,
    val last: String,
    val next: String,
    val prev: String
)
data class Meta(
    val current_page: Int,
    val from: Int,
    val last_page: Int,
    val path: String,
    val per_page: Int,
    val to: Int,
    val total: Int
)