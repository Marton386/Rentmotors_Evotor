package com.rentmotors.evotorapp.domain.usecases

import com.rentmotors.evotorapp.domain.repositories.ReceiptRepository
import javax.inject.Inject

class GetReceiptUseCase @Inject constructor(
    private val receiptRepository: ReceiptRepository
) {
    suspend operator fun invoke(resNumber: String) =
        receiptRepository.getReceipt(resNumber)
}