package com.tt.driver.ui.components.main.payments

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tt.driver.data.models.Loading
import com.tt.driver.data.models.RemoteResult
import com.tt.driver.data.models.entities.PaymentReport
import com.tt.driver.data.models.http.PaymentsReportResponse
import com.tt.driver.data.repositories.orders.OrdersRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PaymentsFragmentViewModel @Inject constructor(
    private val repository: OrdersRepository
) : ViewModel() {

    val reports: StateFlow<RemoteResult<PaymentReport>> by lazy {
        MutableStateFlow<RemoteResult<PaymentReport>>(Loading()).apply {
            viewModelScope.launch {
                emit(repository.getPaymentsReport())
            }
        }
    }
}