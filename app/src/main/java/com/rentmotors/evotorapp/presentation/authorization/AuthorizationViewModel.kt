package com.rentmotors.evotorapp.presentation.authorization

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rentmotors.evotorapp.R
import com.rentmotors.evotorapp.data.helpers.SettingsHelper
import com.rentmotors.evotorapp.domain.common.Result
import com.rentmotors.evotorapp.domain.entities.DeviceInfo
import com.rentmotors.evotorapp.domain.usecases.GetDeviceInfoUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthorizationViewModel @Inject constructor(
    private val settingsHelper: SettingsHelper,
    private val getDeviceInfoUseCase: GetDeviceInfoUseCase
) : ViewModel() {

    val kkmNumber = MutableStateFlow<String>("")
    private val _state = MutableStateFlow<CheckDeviceState>(CheckDeviceState.Empty)
    val state: StateFlow<CheckDeviceState> = _state

    private val _isDevicePermittedEvent = MutableSharedFlow<DeviceInfo>()
    val isDevicePermittedEvent: SharedFlow<DeviceInfo> = _isDevicePermittedEvent

    val loadingState: StateFlow<Boolean> = state.map { it is CheckDeviceState.Loading }
        .stateIn(viewModelScope, SharingStarted.Eagerly, true)

    val doneButtonEnabled: StateFlow<Boolean> = combine(kkmNumber, loadingState) { kkm, loading ->
        kkm.isNotBlank() && !loading
    }.stateIn(viewModelScope, SharingStarted.Eagerly, false)

    fun checkDevice(context: Context) {
        viewModelScope.launch(Dispatchers.IO) {
            _state.value = CheckDeviceState.Loading
                val result = getDeviceInfoUseCase(kkmNumber.value)
            viewModelScope.launch(Dispatchers.Main) {
                when (result) {
                    is Result.Success -> {
                        _state.value = CheckDeviceState.Success(result.data)
                        settingsHelper.setKkmNumber(kkmNumber.value)
                        _isDevicePermittedEvent.emit(result.data)
                    }

                    is Result.Error -> {
                        val message = result.message ?: context.getString(R.string.error)
                        _state.value = CheckDeviceState.Error(message) // TODO: 14.12.2021 error
                    }
                }
            }
        }
    }

    sealed class CheckDeviceState {
        data class Success(val data: DeviceInfo): CheckDeviceState()
        data class Error(val error: String): CheckDeviceState()
        object Loading: CheckDeviceState()
        object Empty: CheckDeviceState()
    }
}