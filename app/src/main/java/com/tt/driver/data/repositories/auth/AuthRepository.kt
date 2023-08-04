package com.tt.driver.data.repositories.auth

import com.tt.driver.data.models.RemoteResult
import com.tt.driver.data.models.entities.User

interface AuthRepository {
    suspend fun login(phone: String, password: String): RemoteResult<User>
    suspend fun toggleStatus(isOnline: Boolean): RemoteResult<Unit>
    suspend fun getContactHelp(): RemoteResult<String>
    suspend fun emitUserLocation(lat: Double, long: Double): RemoteResult<Unit>
}