package com.tt.driver.data.repositories.orders

import com.tt.driver.data.models.RemoteResult
import com.tt.driver.data.models.entities.PaymentReport
import com.tt.driver.data.models.http.OrdersReportResponse
import com.tt.driver.data.models.http.PaymentsReportResponse
import com.tt.driver.data.models.http.UpdateOrderStatusRequestBody
import com.tt.driver.data.network.APIsService
import com.tt.driver.data.repositories.BaseRepository
import com.tt.driver.ui.components.main.orders.OrderType
import okhttp3.RequestBody
import okhttp3.ResponseBody
import javax.inject.Inject

class OrdersRepositoryImpl @Inject constructor(
    private val remoteDataSource: APIsService
) : BaseRepository(), OrdersRepository {

    override suspend fun getOrders(orderType: OrderType) =
        makeApiCall { remoteDataSource.getOrderList(orderType.code).data }

    override suspend fun getOrderDetails(orderId: Int) =
        makeApiCall { remoteDataSource.getOrderDetails(orderId) }

    override suspend fun updateOrderStatus(
        orderId: String,
        status: String,
        cancellationReason: String
    ) = makeApiCall {
        remoteDataSource.updateOrderStatus(
            UpdateOrderStatusRequestBody(
                status, orderId, cancellationReason
            )
        )
    }

    override suspend fun generatePaymentUrl(
        orderId: Int,
        paymentType: Int,
        amount: Double,
        mobile: String
    ): RemoteResult<String?> {
        return makeApiCall {
            remoteDataSource.generatePaymentUrl(
                orderId,
                paymentType,
                amount,
                mobile
            ).data?.url
        }
    }

    override suspend fun getPaymentsReport(): RemoteResult<PaymentReport> {
        return makeApiCall {
            remoteDataSource.getPaymentsReport().data
        }
    }

    override suspend fun getOrdersReport(): RemoteResult<OrdersReportResponse.OrderReports> {
        return makeApiCall {
            remoteDataSource.getOrdersReport().data
        }
    }

    override suspend fun uploadImage(requestBody: RequestBody): RemoteResult<ResponseBody> {
        return makeApiCall {
            remoteDataSource.uploadImage(requestBody)
        }
    }

}