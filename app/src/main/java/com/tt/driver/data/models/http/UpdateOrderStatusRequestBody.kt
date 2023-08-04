package com.tt.driver.data.models.http

data class UpdateOrderStatusRequestBody(
    val status: String,
    val order_id: String,
    val cancellation_reason: String
)