package com.tt.driver.ui.components.main.orders

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tt.driver.data.models.Loading
import com.tt.driver.data.models.RemoteResult
import com.tt.driver.data.models.http.OrdersResponse
import com.tt.driver.data.repositories.orders.OrdersRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OrdersViewModel @Inject constructor(
    private val ordersRepository: OrdersRepositoryImpl
) : ViewModel() {

    var selectedTab = 0

    private val _order = MutableStateFlow<RemoteResult<OrdersResponse>>(Loading())
    val order: StateFlow<RemoteResult<OrdersResponse>> get() = _order

    fun refreshOrders(currentPage: Int) {
        val orderType = if (selectedTab == 0) OrderType.UPCOMING else OrderType.HISTORY
        viewModelScope.launch {
            _order.emit(Loading())
            _order.emit(ordersRepository.getOrders(orderType,currentPage))
        }
    }

}