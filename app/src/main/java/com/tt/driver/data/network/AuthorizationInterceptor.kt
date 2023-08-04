package com.tt.driver.data.network

import com.tt.driver.data.datastore.AuthDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AuthorizationInterceptor @Inject constructor(
    private val authDataStore: AuthDataStore
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {

        val request = chain.request()

        val authorizedRequest = runBlocking {
            request
                .newBuilder()
                .addHeader("Authorization", getAuthorizationToken())
                .build()
        }

        return chain.proceed(authorizedRequest)

    }

    private suspend fun getAuthorizationToken(): String {
        return "Bearer ${authDataStore.getToken().first()}"
    }

}