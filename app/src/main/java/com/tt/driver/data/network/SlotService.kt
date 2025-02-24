package com.tt.driver.data.network

import com.tt.driver.data.models.entities.ExtraPricesResponse
import com.tt.driver.data.models.http.*
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.http.*

interface SlotService {

    @GET("driver/runsheet/slot/{id}")
    suspend fun getSlotDependOnType(@Path("id")id : Int,@QueryMap date: HashMap<String,Any>): SlotsResponse

}