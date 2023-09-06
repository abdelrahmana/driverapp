package com.tt.driver.data.network

import com.tt.driver.data.models.http.*
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.http.*

interface APIsService {

    @Headers("Accept: application/json")
    @POST("auth/login")
    suspend fun login(@Body body: LoginRequest): LoginResponse

    @Headers("Accept: application/json")
    @GET("driver/orders/list")
    suspend fun getOrderList(
        @Query("status") status: Int,@Query("page") page : Int
    ): OrdersResponse

    @Headers("Accept: application/json")
    @GET("driver/orders/details")
    suspend fun getOrderDetails(@Query("id") id: Int): OrderDetailsResponse

    @Headers("Accept: application/json")
    @POST("driver/orders/update")
    suspend fun updateOrderStatus(@Body body: UpdateOrderStatusRequestBody): OrderDetailsResponse

    @Headers("Accept: application/json")
    @GET("generate/pay/url")
    suspend fun generatePaymentUrl(
        @Query("order_id") orderId: Int,
        @Query("payment_type") payment_type: Int,
        @Query("amount") amount: Double,
        @Query("mobile") mobile: String
    ): OrderPaymentUrlResponse

    @Headers("Accept: application/json")
    @POST("driver/toggle/online/status")
    suspend fun toggleStatus(@Body body: ToggleStatusRequest): ResponseBody

    @Headers("Accept: application/json")
    @GET("contactus")
    suspend fun getHelpContact(): HelpContactResponse

    @Headers("Accept: application/json")
    @GET("driver/orders/order-payment-report")
    suspend fun getPaymentsReport(): PaymentsReportResponse

    @Headers("Accept: application/json")
    @GET("driver/orders/order-reports")
    suspend fun getOrdersReport(): OrdersReportResponse

    @Headers("Accept: application/json")
    @POST("driver/tracking/store")
    suspend fun emitUserLocation(@Body body: UserLocationRequest): ResponseBody

  /*  @Headers("Accept: multipart/form-data")
    @POST("driver/orders/upload-signature")
    suspend fun uploadImage(@Body body: RequestBody): ResponseBody */
  @Headers("Accept: multipart/form-data")
  @POST("driver/orders/upload-images")
  suspend fun uploadImage(@Body body: RequestBody): ResponseBody
}