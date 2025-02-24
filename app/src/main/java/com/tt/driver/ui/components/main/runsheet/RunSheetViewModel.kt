package com.tt.driver.ui.components.main.runsheet

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tt.driver.data.models.Loading
import com.tt.driver.data.models.RemoteResult
import com.tt.driver.data.models.http.OrdersResponse
import com.tt.driver.data.models.http.RunSheetResponse
import com.tt.driver.data.repositories.orders.OrdersRepositoryImpl
import com.tt.driver.data.repositories.runsheet.RunSheetRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RunSheetViewModel @Inject constructor(
    private val ordersRepository: RunSheetRepo
) : ViewModel() {

    var selectedTab = 0

    private val _runSheet = MutableStateFlow<RemoteResult<RunSheetResponse>>(Loading())
    val runSheet: StateFlow<RemoteResult<RunSheetResponse>> get() = _runSheet

    fun getRunSheet(date: String) {
        viewModelScope.launch {
            _runSheet.emit(Loading())
            _runSheet.emit(ordersRepository.getRunSheet(date))
        }
    }

}