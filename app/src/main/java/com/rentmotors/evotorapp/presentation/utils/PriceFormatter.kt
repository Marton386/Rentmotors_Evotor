package com.rentmotors.evotorapp.presentation.utils

import java.text.DecimalFormat

object PriceFormatter {
    private val formatter = DecimalFormat("#,###")
    private val formatterWithDecimals = DecimalFormat("#,##0.00")

    private fun formatNoDecimals(price: Double): String {
        return formatter.format(price)
    }

    private fun formatWithDecimals(price: Double): String {
        return formatterWithDecimals.format(price)
    }

    fun format(price: Double): String {
        return if (price.rem(1).equals(0.0)) {
            formatNoDecimals(price)
        } else {
            formatWithDecimals(price)
        }
    }
}