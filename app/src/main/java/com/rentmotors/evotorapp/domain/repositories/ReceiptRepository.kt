package com.rentmotors.evotorapp.domain.repositories

import com.rentmotors.evotorapp.domain.common.Result
import com.rentmotors.evotorapp.domain.entities.BookReceipt

interface ReceiptRepository {
    suspend fun getReceipt(resNumber: String): Result<BookReceipt>
}