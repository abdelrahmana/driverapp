package com.tt.driver.data.repositories.orders

import com.tt.driver.data.models.RemoteResult
import com.tt.driver.data.models.entities.Order
import com.tt.driver.data.models.entities.PaymentReport
import com.tt.driver.data.models.http.OrderDetailsResponse
import com.tt.driver.data.models.http.OrdersReportResponse
import com.tt.driver.data.models.http.OrdersResponse
import com.tt.driver.ui.components.main.orders.OrderType
import okhttp3.RequestBody
import okhttp3.ResponseBody

interface OrdersRepository {

    suspend fun getOrders(orderType: OrderType,page : Int): RemoteResult<OrdersResponse>

    suspend fun getOrderDetails(orderId: Int): RemoteResult<OrderDetailsResponse>

    suspend fun updateOrderStatus(
        orderId: String,
        status: String,
        cancellationReason: String = ""
    ): RemoteResult<OrderDetailsResponse>

    suspend fun generatePaymentUrl(
        orderId: Int,
        paymentType: Int,
        amount: Double,
        mobile: String
    ): RemoteResult<String?>

    suspend fun getPaymentsReport(): RemoteResult<PaymentReport>

    suspend fun getOrdersReport(): RemoteResult<OrdersReportResponse.OrderReports>

    suspend fun uploadImage(requestBody: RequestBody): RemoteResult<ResponseBody>
}