package com.tt.driver.ui.components.main.orders.order_details

import android.app.Application
import android.content.Context
import android.graphics.Bitmap
import androidx.lifecycle.*
import com.tt.driver.data.models.Loading
import com.tt.driver.data.models.RemoteResult
import com.tt.driver.data.models.entities.Order
import com.tt.driver.data.models.entities.OrderStatus
import com.tt.driver.data.models.http.OrderDetailsResponse
import com.tt.driver.data.repositories.orders.OrdersRepositoryImpl
import com.tt.driver.utils.FileUtils
import com.tt.driver.utils.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import javax.inject.Inject

@HiltViewModel
class OrderViewModel @Inject constructor(
    application: Application,
    private val ordersRepository: OrdersRepositoryImpl,
    savedStateHandle: SavedStateHandle
) : AndroidViewModel(application) {

    private val orderId by lazy { savedStateHandle.get<Int?>("orderId") }

    private val _order = MutableStateFlow<RemoteResult<OrderDetailsResponse>>(Loading())
    val order: StateFlow<RemoteResult<OrderDetailsResponse>> get() = _order

    private val _onUrlGenerated = SingleLiveEvent<Pair<PaymentType, RemoteResult<String?>>>()
    val onUrlGenerated get() = _onUrlGenerated

    private val _onImageUploaded = SingleLiveEvent<RemoteResult<ResponseBody>>()
    val onImageUploaded: LiveData<RemoteResult<ResponseBody>> get() = _onImageUploaded

    private val _showDigitalSignature = SingleLiveEvent<Int?>()
    val showDigitalSignature: LiveData<Int?> get() = _showDigitalSignature

    init {
        refreshOrderDetails()
    }

    fun refreshOrderDetails() {
        viewModelScope.launch {
            orderId?.let {
                _order.emit(Loading())
                _order.emit(
                    ordersRepository.getOrderDetails(it)
                )
            }
        }
    }

    fun updateOrderStatus(orderStatus: OrderStatus) {
        viewModelScope.launch {
            if (orderStatus == OrderStatus.COMPLETED)
                _showDigitalSignature.postValue(orderId)
            _order.emit(Loading())
            orderId?.let {
                _order.emit(
                    ordersRepository.updateOrderStatus(it.toString(), orderStatus.code)
                )
            }
        }
    }

    fun generatePaymentUrl(
        orderId: Int,
        paymentType: PaymentType,
        amount: Double,
        mobile: String
    ) {
        viewModelScope.launch {
            _onUrlGenerated.postValue(Pair(paymentType, Loading()))
            _onUrlGenerated.postValue(
                Pair(
                    paymentType,
                    ordersRepository.generatePaymentUrl(
                        orderId,
                        paymentType.code,
                        amount,
                        mobile
                    )
                )
            )
        }
    }

    fun uploadImage(orderId: Int, bitmap: Bitmap) {
        viewModelScope.launch {
            _onImageUploaded.postValue(Loading())
            _onImageUploaded.postValue(ordersRepository.uploadImage(createImageFormDataBody(orderId, bitmap)))
        }
    }

    private fun createImageFormDataBody(orderId: Int, bitmap: Bitmap): MultipartBody {
        return MultipartBody.Builder().setType(MultipartBody.FORM)
            .apply {
                val file = FileUtils.writeBitmapToFile(getApplication<Application>().applicationContext, bitmap)
                val body = RequestBody.create("image/*".toMediaTypeOrNull(), file)
                addPart(MultipartBody.Part.createFormData("signature_image", file.name, body))
                addFormDataPart("order_id", orderId.toString())
            }.build()
    }

}