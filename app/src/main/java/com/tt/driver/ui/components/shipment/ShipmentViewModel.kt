package com.tt.driver.ui.components.shipment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tt.driver.data.models.Loading
import com.tt.driver.data.models.RemoteResult
import com.tt.driver.data.models.http.ShipmentDetailsResponse
import com.tt.driver.data.repositories.shipment.ShipmentRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ShipmentViewModel @Inject constructor(
    private val shipmentRepo: ShipmentRepo
) : ViewModel() {

    private val _shipment = MutableStateFlow<RemoteResult<ShipmentDetailsResponse>>(Loading())
    val shipmentStateFlow: StateFlow<RemoteResult<ShipmentDetailsResponse>> get() = _shipment
    private val _shipmentUpdateStatus = MutableStateFlow<RemoteResult<Any>?>(null)
    val shipmentUpdateStatus: StateFlow<RemoteResult<Any>?> get() = _shipmentUpdateStatus

    fun getShipment(shipmentId : Int,hashMap: HashMap<String,Any>) {
        viewModelScope.launch {
            _shipment.emit(Loading())
            _shipment.emit(shipmentRepo.getShipmentInfo(shipmentId,hashMap))
        }
    }
    fun getShipmentByQrCode(hashMap: HashMap<String,Any>) {
        viewModelScope.launch {
            _shipment.emit(Loading())
            _shipment.emit(shipmentRepo.getShipmentInfoByShipmentNumber(hashMap))
        }
    }
    fun updateShipmentStatus(hashMap: HashMap<String,Any>) {
        viewModelScope.launch {
            _shipmentUpdateStatus.emit(Loading())
            _shipmentUpdateStatus.emit(shipmentRepo.updateShipmentStatus(hashMap))
        }
    }

}