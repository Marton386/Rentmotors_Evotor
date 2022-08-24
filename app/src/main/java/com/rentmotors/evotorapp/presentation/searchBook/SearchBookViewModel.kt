package com.rentmotors.evotorapp.presentation.searchBook

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rentmotors.evotorapp.R
import com.rentmotors.evotorapp.data.helpers.SettingsHelper
import com.rentmotors.evotorapp.domain.common.Result
import com.rentmotors.evotorapp.domain.entities.BookReceipt
import com.rentmotors.evotorapp.domain.usecases.GetDeviceInfoUseCase
import com.rentmotors.evotorapp.domain.usecases.GetReceiptUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SearchBookViewModel @Inject constructor(
    private val settingsHelper: SettingsHelper,
    private val getReceiptUseCase: GetReceiptUseCase,
    private val getDeviceInfoUseCase: GetDeviceInfoUseCase,
) : ViewModel() {

    val resNumber = MutableStateFlow<String>("")

    private val _state = MutableStateFlow<SearchBookState>(SearchBookState.Empty)
    val state: StateFlow<SearchBookState> = _state

    private val _goToDetailScreenEvent = MutableSharedFlow<BookReceipt>()
    val goToDetailScreenEvent: SharedFlow<BookReceipt> = _goToDetailScreenEvent

    val loadingState: StateFlow<Boolean> = state.map { it is SearchBookState.Loading }
        .stateIn(viewModelScope, SharingStarted.Eagerly, true)

    val searchButtonEnabled: StateFlow<Boolean> = combine(resNumber, loadingState) { res, loading ->
        res.isNotBlank() && !loading
    }.stateIn(viewModelScope, SharingStarted.Eagerly, false)

    fun getBook(context: Context){
        checkDevice(context)
    }

    fun isKkmNumberSet() : Boolean {
        return settingsHelper.getKkmNumber().isNotEmpty()
    }

    fun getUuidReceipt() : String {
        return settingsHelper.getReceiptUuid()
    }

    fun clearResNumber(){
        if (resNumber.value.isNotEmpty()) {
            if (settingsHelper.getBookReceipt() == null) {
                resNumber.value = ""
            }
        }
    }

    private fun checkDevice(context: Context) {
        try {
            val kkmNumber = settingsHelper.getKkmNumber()
            if (kkmNumber.isNotEmpty()) {
                viewModelScope.launch(Dispatchers.IO) {
                    _state.value = SearchBookState.Loading

                    when (val result = getDeviceInfoUseCase(kkmNumber)) {
                        is Result.Success -> {
                            //_state.value = AuthorizationViewModel.CheckDeviceState.Success(result.data)
                            searchBook(context)
                        }
                        is Result.Error -> {
                            withContext(Dispatchers.Main) {
                                val message =
                                    result.message ?: context.getString(R.string.error)
                                _state.value = SearchBookState.Error(message)
                            }
                        }
                    }

                }
            }
        } catch (e: Exception) {
            _state.value = SearchBookState.Error(e.stackTraceToString())
        }
    }

    private suspend fun searchBook(context: Context) {
        val result = getReceiptUseCase(resNumber.value)
        withContext(Dispatchers.Main) {
            when (result) {
                is Result.Success -> {
                    _state.value = SearchBookState.Success(result.data)
                    _goToDetailScreenEvent.emit(result.data)
                    settingsHelper.setBookReceipt(result.data)
                }
                is Result.Error -> {
                    val message = result.message ?: context.getString(R.string.error)
                    _state.value = SearchBookState.Error(message)
                }
            }
        }
    }

    sealed class SearchBookState {
        data class Success(val data: BookReceipt): SearchBookState()
        data class Error(val error: String): SearchBookState()
        object Loading: SearchBookState()
        object Empty: SearchBookState()
    }
}
