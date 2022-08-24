package com.rentmotors.evotorapp.data.api.entities

import com.google.gson.annotations.SerializedName
import com.rentmotors.evotorapp.domain.entities.PaymentInfo

data class PaymentInfoDTO(
    @SerializedName("TerminalKey")
    val kkm: String,
    @SerializedName("OrderId")
    val idOrder: String,
    @SerializedName("Success")
    val state: Boolean,
    @SerializedName("Status")
    val status: String,
    @SerializedName("PaymentId")
    val idReceipt: Int,
    @SerializedName("ErrorCode")
    val errorCode: String,
    @SerializedName("Amount")
    val amount: Long,
    @SerializedName("Token")
    val receiptUuid: String,
    @SerializedName("Data")
    val data: PaymentData
) {
    data class PaymentData(
        @SerializedName("aprok_id")
        val idAprok: String,
        @SerializedName("res_id")
        val idRes: String,
        @SerializedName("offer_id")
        val idOffer: String,
        @SerializedName("Email")
        val email: String,
        @SerializedName("Phone")
        val phone: String,
        @SerializedName("descr")
        val description: String,
    )
}

fun PaymentInfoDTO(paymentInfo: PaymentInfo): PaymentInfoDTO {
    return PaymentInfoDTO(
        paymentInfo.kkmNumber,
        paymentInfo.bookReceipt.idOrder,
        paymentInfo.state,
        paymentInfo.status,
        paymentInfo.receiptId,
        paymentInfo.codeError,
        paymentInfo.amount,
        paymentInfo.receiptUuid,
        PaymentInfoDTO.PaymentData(
            paymentInfo.bookReceipt.idAprok,
            paymentInfo.bookReceipt.idRes,
            paymentInfo.bookReceipt.idOffer,
            paymentInfo.bookReceipt.email,
            paymentInfo.bookReceipt.phone,
            paymentInfo.bookReceipt.description
        )
    )
}
