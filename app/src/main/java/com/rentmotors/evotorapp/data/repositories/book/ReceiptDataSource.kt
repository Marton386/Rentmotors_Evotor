package com.rentmotors.evotorapp.data.repositories.book

import com.rentmotors.evotorapp.domain.common.Result
import com.rentmotors.evotorapp.domain.entities.BookReceipt

interface ReceiptDataSource {
    suspend fun getReceipt(resNumber: String): Result<BookReceipt>
    suspend fun getRefund(resNumber: String): Result<BookReceipt>
}