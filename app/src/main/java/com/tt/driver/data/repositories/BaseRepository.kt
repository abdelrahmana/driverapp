package com.tt.driver.data.repositories

import com.tt.driver.data.models.ErrorHandler
import com.tt.driver.data.models.Failure
import com.tt.driver.data.models.Success
import retrofit2.HttpException
import java.lang.Exception

open class BaseRepository {

    open suspend fun <T> makeApiCall(apiCall: suspend () -> T) = try {
        Success(apiCall())
    } catch (e: Exception) {
        Failure(ErrorHandler.handleError(e))
    }


}