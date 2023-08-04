package com.tt.driver.ui.components.main.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tt.driver.data.models.Loading
import com.tt.driver.data.models.RemoteResult
import com.tt.driver.data.repositories.auth.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: AuthRepository
) : ViewModel() {

    fun toggleStatus(isOnline: Boolean): StateFlow<RemoteResult<Unit>> {
        return MutableStateFlow<RemoteResult<Unit>>(Loading()).apply {
            viewModelScope.launch {
                emit(repository.toggleStatus(isOnline))
            }
        }
    }

}