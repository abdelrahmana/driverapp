package com.tt.driver.data.models.http

data class HelpContactResponse(
    val `data`: Data
) {
    data class Data(
        val phone_number: String?
    )
}