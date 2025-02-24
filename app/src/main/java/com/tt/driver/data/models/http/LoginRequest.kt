package com.tt.driver.data.models.http

data class LoginRequest(
    val civil_id: String,
    val password: String,
    val fcm_token: String
)