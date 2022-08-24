package com.rentmotors.evotorapp.domain.entities

class PaymentInfo(
    val kkmNumber: String,
    val bookReceipt: BookReceipt,
    val receiptId: Int,
    val receiptUuid: String,
    val amount: Long,
    val status: String,
    val state: Boolean,
    val codeError: String
) {
}