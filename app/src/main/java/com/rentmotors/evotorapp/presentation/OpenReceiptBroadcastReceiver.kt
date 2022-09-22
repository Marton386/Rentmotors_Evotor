package com.rentmotors.evotorapp.presentation

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.rentmotors.evotorapp.data.helpers.SettingsHelper
import ru.evotor.framework.receipt.ReceiptApi
import ru.evotor.framework.receipt.event.ReceiptCreatedEvent

class OpenReceiptBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val receiptUuid = ReceiptCreatedEvent.from(intent?.extras)?.receiptUuid
        if (context != null) {
            val settingsHelper = SettingsHelper(context)
            try {
                if (receiptUuid != null) {
                    val receipt = ReceiptApi.getReceipt(context, receiptUuid)
                    if (receipt != null) {
    /*                    Toast.makeText(
                            context,
                            "Check is open",
                            Toast.LENGTH_LONG
                        ).show()*/
                        val uuidReceipt = receipt.header.uuid
                        if (!uuidReceipt.isNullOrBlank()) {
                            settingsHelper.setReceiptUuid(uuidReceipt)
                        }
                    } else {
/*                        Toast.makeText(
                            context,
                            "Check is open, receipt is null",
                            Toast.LENGTH_LONG
                        ).show()*/
                    }
                }
            } catch (e: Exception) {
/*                Toast.makeText(
                    context,
                    e.stackTraceToString(),
                    Toast.LENGTH_LONG
                ).show()*/
            }
        }
    }
}