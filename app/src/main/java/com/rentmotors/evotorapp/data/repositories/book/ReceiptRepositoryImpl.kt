package com.rentmotors.evotorapp.data.repositories.book

import com.rentmotors.evotorapp.domain.common.Result
import com.rentmotors.evotorapp.domain.entities.BookReceipt
import com.rentmotors.evotorapp.domain.repositories.ReceiptRepository

class ReceiptRepositoryImpl(
    private val receiptDataSource: ReceiptDataSource
): ReceiptRepository {
    override suspend fun getReceipt(resNumber: String): Result<BookReceipt> {
        return receiptDataSource.getReceipt(resNumber)
    }
}