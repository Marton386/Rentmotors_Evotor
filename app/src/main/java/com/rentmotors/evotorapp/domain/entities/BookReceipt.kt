package com.rentmotors.evotorapp.domain.entities

data class BookReceipt(
    val idOrder: String,
    val idAprok: String,
    val idRes: String,
    val idOffer: String,
    val description: String,
    val email: String,
    val phone: String,
    val taxation: String,
    val urlSellInfo: String,
    val items: List<Item>
) {
    data class Item(
        val name: String,
        val price: Double,
        val quantity: Int,
        val amount: Double,
        val tax: String
    )
}
