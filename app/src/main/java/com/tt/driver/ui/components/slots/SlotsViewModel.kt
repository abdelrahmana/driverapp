package com.tt.driver.ui.components.slots

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tt.driver.data.models.Loading
import com.tt.driver.data.models.RemoteResult
import com.tt.driver.data.models.http.SlotsResponse
import com.tt.driver.data.repositories.slots.SlotsRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SlotsViewModel @Inject constructor(
    private val slotRepoObject: SlotsRepo
) : ViewModel() {

    var selectedTab = 0

    private val _slot = MutableStateFlow<RemoteResult<SlotsResponse>>(Loading())
    val slot: StateFlow<RemoteResult<SlotsResponse>> get() = _slot

    fun getSlotDependOnType(slotId : Int,type : String) {
        viewModelScope.launch {
            _slot.emit(Loading())
            _slot.emit(slotRepoObject.getSlotDependOnType(type,slotId))
        }
    }

}