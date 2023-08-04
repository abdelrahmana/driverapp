package com.tt.driver.ui.components.registration

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tt.driver.data.models.Loading
import com.tt.driver.data.models.RemoteResult
import com.tt.driver.data.models.entities.User
import com.tt.driver.data.repositories.auth.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegistrationViewModel @Inject constructor(
    private val repository: AuthRepository
) : ViewModel() {

    fun login(phone: String, password: String) =
        MutableStateFlow<RemoteResult<User>>(Loading()).apply {
            viewModelScope.launch {
                emit(repository.login(phone, password))
            }
        }

}