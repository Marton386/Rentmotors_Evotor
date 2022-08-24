package com.rentmotors.evotorapp.presentation

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import ru.evotor.framework.receipt.event.ReceiptCompletedEvent

class ClosedReceiptBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val receiptUuid = ReceiptCompletedEvent.from(intent?.extras)?.receiptUuid

     /*   if (receiptUuid != null && context != null) {
            val receipt = try {
                ReceiptApi.getReceipt(context, receiptUuid)
            } catch (e: Exception) {
                // возможно здесь ошибка
                Toast.makeText(context, "Failed to get receipt: ${e.message}", Toast.LENGTH_LONG)
                    .show()
                null
            }

            if (receipt == null) {
                Toast.makeText(context, "Чек НЕ найден", Toast.LENGTH_LONG)
                    .show()
            } else {
                Toast.makeText(context, "Чек найден", Toast.LENGTH_LONG)
                    .show()
            }
        }*/
    }
}