package com.tt.driver.data.repositories.runsheet

import com.tt.driver.data.models.RemoteResult
import com.tt.driver.data.models.entities.Data
import com.tt.driver.data.models.entities.ExtraPricesResponse
import com.tt.driver.data.models.entities.PaymentReport
import com.tt.driver.data.models.http.OrdersReportResponse
import com.tt.driver.data.models.http.PaymentsReportResponse
import com.tt.driver.data.models.http.RunSheetResponse
import com.tt.driver.data.models.http.UpdateOrderStatusRequestBody
import com.tt.driver.data.network.APIsService
import com.tt.driver.data.repositories.BaseRepository
import com.tt.driver.ui.components.main.orders.OrderType
import okhttp3.RequestBody
import okhttp3.ResponseBody
import javax.inject.Inject

class RunSheetRepoImplemeneter @Inject constructor(
    private val remoteDataSource: APIsService
) : BaseRepository(), RunSheetRepo {
    override suspend fun getRunSheet(date: String): RemoteResult<RunSheetResponse> =
        makeApiCall { remoteDataSource. getRunSheetCall(HashMap<String, Any>().also {
            it.put("date",date)
        }) }


}