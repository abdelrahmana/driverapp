package com.tt.driver.data.models.http

data class LoginRequest(
    val phone: String,
    val password: String,
    val fcm_token: String
)