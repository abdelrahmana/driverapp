package com.tt.driver.data.repositories.auth

import com.tt.driver.data.datastore.AuthDataStore
import com.tt.driver.data.datastore.UserDataStore
import com.tt.driver.data.models.RemoteResult
import com.tt.driver.data.models.http.LoginRequest
import com.tt.driver.data.models.http.ToggleStatusRequest
import com.tt.driver.data.models.http.UserLocationRequest
import com.tt.driver.data.network.APIsService
import com.tt.driver.data.repositories.BaseRepository
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val remoteDataSource: APIsService,
    private val userDataStore: UserDataStore,
    private val authDataStore: AuthDataStore
) : BaseRepository(), AuthRepository {

    override suspend fun login(phone: String, password: String) = makeApiCall {
        val result = remoteDataSource.login(LoginRequest(phone, password, getFCMToken())).data
        authDataStore.setUserToken(GlobalScope, result.access_token)
        userDataStore.setUserData(GlobalScope, result.user)
        result.user
    }

    override suspend fun toggleStatus(isOnline: Boolean) = makeApiCall {
        val id = userDataStore.getUser().first()?.id ?: 0
        remoteDataSource.toggleStatus(ToggleStatusRequest(id))
        Unit
    }

    override suspend fun getContactHelp() = makeApiCall {
        remoteDataSource.getHelpContact().data.phone_number ?: "0"
    }

    override suspend fun emitUserLocation(lat: Double, long: Double) = makeApiCall {
        remoteDataSource.emitUserLocation(UserLocationRequest(lat.toString(), long.toString()))
        Unit
    }

    private fun getFCMToken(): String {
        return runBlocking {
            authDataStore.getFCMToken().first()
        }
    }

}