package com.tt.driver.data.models.http

import com.tt.driver.data.models.entities.User

data class LoginResponse(val data: Data) {
    data class Data(
        val access_token: String,
        val user: User
    )
}