package com.rentmotors.evotorapp.presentation.bookingDetails

import android.content.Context
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rentmotors.evotorapp.R
import com.rentmotors.evotorapp.data.helpers.SettingsHelper
import com.rentmotors.evotorapp.domain.common.Result
import com.rentmotors.evotorapp.domain.entities.PaymentInfo
import com.rentmotors.evotorapp.domain.usecases.SendPaymentInfoUseCase
import com.rentmotors.evotorapp.presentation.utils.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.evotor.framework.receipt.ReceiptApi
import javax.inject.Inject

@HiltViewModel
class BookingDetailsViewModel @Inject constructor(
    private val settingsHelper: SettingsHelper,
    private val sendPaymentInfoUseCase: SendPaymentInfoUseCase
) : ViewModel() {
    private lateinit var payment: PaymentInfo

    private val _state = MutableStateFlow<BookingDetailsState>(BookingDetailsState.Empty)
    val state: StateFlow<BookingDetailsState> = _state

    private val _stateTryError = MutableStateFlow<TryErrorState>(TryErrorState.Empty)
    val stateTryError: StateFlow<TryErrorState> = _stateTryError

    private val _goToSearchBookScreenEvent = MutableSharedFlow<Boolean>()
    val goToSearchBookScreenEvent: SharedFlow<Boolean> = _goToSearchBookScreenEvent

    private val loadingState: StateFlow<Boolean> = state.map { it is BookingDetailsState.Loading }
        .stateIn(viewModelScope, SharingStarted.Eagerly, true)

    val payButtonEnabled: StateFlow<Boolean> =
        combine(loadingState, goToSearchBookScreenEvent) { loading, isPayComplete ->
            !loading && !isPayComplete
        }.stateIn(viewModelScope, SharingStarted.Eagerly, true)


    private fun getPaymentInfo(context: Context): PaymentInfo? {
        try {
            val uuid = settingsHelper.getReceiptUuid()
            val bookReceipt = settingsHelper.getBookReceipt()
            if (bookReceipt != null) {
                if (uuid.isNotEmpty()) {
                    val receipt = ReceiptApi.getReceipt(context, uuid)
                    if (receipt != null) {
                        val idReceipt = receipt.header.number ?: "0"
                        val amount = bookReceipt.items.sumOf { it.amount }
                        return PaymentInfo(
                            settingsHelper.getKkmNumber(),
                            bookReceipt,
                            idReceipt.toInt(),
                            uuid,
                            convertRubleToPenny(amount),
                            Constants.STATUS_CONFIRMED,
                            true,
                            Constants.NO_ERROR
                        )
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    private fun convertRubleToPenny(priceInRuble: Double): Long {
        return (priceInRuble * Constants.PENNY_IN_RUBLE).toLong()
    }

    fun sendPaymentInfo(context: Context) {
        try {
            val paymentInfo = getPaymentInfo(context)
            if (paymentInfo != null) {
                payment = paymentInfo
                viewModelScope.launch(Dispatchers.IO) {
                    _state.value = BookingDetailsState.Loading
                    var countTry = 0
                    var checkSend = false
                    while (!checkSend && (countTry != 3)) {
                        val result = sendPaymentInfoUseCase(paymentInfo)
                        withContext(Dispatchers.Main) {
                            when (result) {
                                is Result.Success -> {
                                    checkSend = true
                                    _state.value = BookingDetailsState.Success(result.data)
                                    _goToSearchBookScreenEvent.emit(true)
                                }
                                is Result.Error -> {
                                    countTry += 1
                                    if (countTry == 3) {
                                        val message = context.getString(R.string.sending_error)
                                        showErrorTryDialog(context, message)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch (e: Exception) {
            _state.value = BookingDetailsState.Error(e.stackTraceToString())
        }
    }

    fun retrySendPaymentInfo(context: Context) {
        try {
            viewModelScope.launch(Dispatchers.IO) {
                var countTry = 0
                var checkSend = false
                while (!checkSend && (countTry != 3)) {
                    val result = sendPaymentInfoUseCase(payment)
                    withContext(Dispatchers.Main) {
                        when (result) {
                            is Result.Success -> {
                                checkSend = true
                                _state.value = BookingDetailsState.Success(result.data)
                                _goToSearchBookScreenEvent.emit(true)
                            }
                            is Result.Error -> {
                                countTry += 1
                                if (countTry == 3) {
                                    val message = context.getString(R.string.last_send_error)
                                    _state.value = BookingDetailsState.Error(message)
                                    _goToSearchBookScreenEvent.emit(true)
                                }
                            }
                        }
                    }
                }
            }
        } catch (e: Exception) {
            _state.value = BookingDetailsState.Error(e.stackTraceToString())
        }
    }

    private fun showErrorTryDialog(context: Context, error: String) {
        val alertDialogBuilder = AlertDialog.Builder(context).setView(R.layout.try_dialog_message)
        val dialog = alertDialogBuilder.show()

        dialog.findViewById<TextView>(R.id.tvMessage_1)?.text = error
        dialog.findViewById<Button>(R.id.try_btn)?.setOnClickListener {
            dialog.dismiss()
            _stateTryError.value = TryErrorState.TryAgain
        }
    }

    fun clearPayingInfo() {
        settingsHelper.clearSharedPreferences()
    }

    sealed class BookingDetailsState {
        data class Success(val data: Boolean) : BookingDetailsState()
        data class Error(val error: String) : BookingDetailsState()
        object Loading : BookingDetailsState()
        object Empty : BookingDetailsState()
    }

    sealed class TryErrorState {
        object TryAgain : TryErrorState()
        object Empty : TryErrorState()
    }
}