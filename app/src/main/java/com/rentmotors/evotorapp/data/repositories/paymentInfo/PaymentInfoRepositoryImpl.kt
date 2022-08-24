package com.rentmotors.evotorapp.data.repositories.paymentInfo

import com.rentmotors.evotorapp.domain.common.Result
import com.rentmotors.evotorapp.domain.entities.PaymentInfo
import com.rentmotors.evotorapp.domain.repositories.PaymentInfoRepository

class PaymentInfoRepositoryImpl(
    private val paymentInfoDataSource: PaymentInfoDataSource
): PaymentInfoRepository {
    override suspend fun sendPaymentInfo(paymentInfo: PaymentInfo): Result<Boolean> {
        return paymentInfoDataSource.sendPaymentInfo(paymentInfo)
    }
}