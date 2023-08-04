package com.tt.driver.ui.components.main.order_report

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tt.driver.data.models.Loading
import com.tt.driver.data.models.RemoteResult
import com.tt.driver.data.models.http.OrdersReportResponse
import com.tt.driver.data.repositories.orders.OrdersRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OrderReportFragmentViewModel @Inject constructor(
    private val repository: OrdersRepository
): ViewModel() {

    val report by lazy {
        MutableStateFlow<RemoteResult<OrdersReportResponse.OrderReports>>(Loading()).apply {
            viewModelScope.launch {
                emit(repository.getOrdersReport())
            }
        }
    }
}