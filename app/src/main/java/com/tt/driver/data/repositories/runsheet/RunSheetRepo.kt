package com.tt.driver.data.repositories.runsheet

import com.tt.driver.data.models.RemoteResult
import com.tt.driver.data.models.entities.Data
import com.tt.driver.data.models.entities.ExtraPricesResponse
import com.tt.driver.data.models.entities.Order
import com.tt.driver.data.models.entities.PaymentReport
import com.tt.driver.data.models.http.OrderDetailsResponse
import com.tt.driver.data.models.http.OrdersReportResponse
import com.tt.driver.data.models.http.OrdersResponse
import com.tt.driver.data.models.http.RunSheetResponse
import com.tt.driver.ui.components.main.orders.OrderType
import okhttp3.RequestBody
import okhttp3.ResponseBody

interface RunSheetRepo {

    suspend fun getRunSheet(date :String): RemoteResult<RunSheetResponse>


}