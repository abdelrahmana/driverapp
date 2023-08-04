package com.tt.driver.ui.components.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tt.driver.data.models.RemoteResult
import com.tt.driver.data.repositories.auth.AuthRepository
import com.tt.driver.utils.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val repository: AuthRepository
) : ViewModel() {

    fun getHelpContact(): LiveData<RemoteResult<String>> {
        return SingleLiveEvent<RemoteResult<String>>().apply {
            viewModelScope.launch { postValue(repository.getContactHelp()) }
        }
    }
}