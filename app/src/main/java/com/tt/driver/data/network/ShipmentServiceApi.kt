package com.tt.driver.data.network

import com.tt.driver.data.models.http.*
import retrofit2.http.*

interface ShipmentServiceApi {

    @GET("driver/runsheet/order/{id}")
    suspend fun getShipmentDetails(@Path("id")id : Int, @QueryMap date: HashMap<String,Any>): ShipmentDetailsResponse


    @POST("driver/search-barcode")
    suspend fun getShipmentDetailsByShipmentNumber(@Body date: HashMap<String,Any>): ShipmentDetailsResponse

}