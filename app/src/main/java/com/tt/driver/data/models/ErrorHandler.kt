package com.tt.driver.data.models

import org.json.JSONObject
import retrofit2.HttpException

object ErrorHandler {

    fun handleError(exception: Exception): String {
        if (exception is HttpException)
            return handleApiResponseError(exception)
        return "something went wrong"
    }

    private fun handleApiResponseError(exception: HttpException): String {
        return try {
            val errors = JSONObject(exception.response()?.errorBody()?.string() ?: "")
            errors.getString("message")
        } catch (e: Exception) {
            "request failed"
        }
    }

}