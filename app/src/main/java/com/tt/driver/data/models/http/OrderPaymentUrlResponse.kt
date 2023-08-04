package com.tt.driver.data.models.http

data class OrderPaymentUrlResponse(val data: PaymentUrlResponseModel?) {
    data class PaymentUrlResponseModel(val url: String?)
}