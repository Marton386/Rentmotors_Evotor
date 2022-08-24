package com.rentmotors.evotorapp.data.api.entities

import com.google.gson.annotations.SerializedName
import com.rentmotors.evotorapp.domain.entities.BookReceipt
import com.rentmotors.evotorapp.presentation.utils.Constants

data class BookDTO(
    @SerializedName("OrderId")
    val idOrder: String,
    @SerializedName("DATA")
    val bookInfo: BookInfoDTO,
    @SerializedName("NotificationURL")
    val urlNotification: String,
    @SerializedName("Receipt")
    val receipt: ReceiptDTO
) {
    data class BookInfoDTO(
        @SerializedName("aprok_id")
        val idAprok: String,
        @SerializedName("res_id")
        val idRes: String,
        @SerializedName("offer_id")
        val idOffer: String,
        @SerializedName("descr")
        val description: String,
    )

    data class ReceiptDTO(
        @SerializedName("Email")
        val email: String,
        @SerializedName("Phone")
        val phone: String,
        @SerializedName("Taxation")
        val taxation: String,
        @SerializedName("Items")
        val items: List<ItemDTO>
    ) {
        data class ItemDTO(
            @SerializedName("Name")
            val name: String,
            @SerializedName("Price")
            val price: Long,
            @SerializedName("Quantity")
            val quantity: Double,
            @SerializedName("Amount")
            val amount: Long,
            @SerializedName("Tax")
            val tax: String
        )
    }
}

fun BookDTO.toReceipt(): BookReceipt {
    return BookReceipt(
        idOrder,
        bookInfo.idAprok,
        bookInfo.idRes,
        bookInfo.idOffer,
        bookInfo.description,
        receipt.email,
        receipt.phone,
        receipt.taxation,
        urlNotification,
        receipt.items.map {
            it.toItem()
        }
    )
}

fun BookDTO.ReceiptDTO.ItemDTO.toItem(): BookReceipt.Item {
    return BookReceipt.Item(
        name,
        convertPennyToRuble(price), // FIXME: 18.11.2021 check with api
        quantity.toInt(),
        convertPennyToRuble(amount), // FIXME: 18.11.2021 check with api
        tax
    )
}

private fun convertPennyToRuble(priceInPenny: Long): Double {
    return priceInPenny.toDouble() / Constants.PENNY_IN_RUBLE
}
