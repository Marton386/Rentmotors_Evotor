package com.rentmotors.evotorapp.data.repositories.paymentInfo

import com.rentmotors.evotorapp.domain.common.Result
import com.rentmotors.evotorapp.domain.entities.PaymentInfo

interface PaymentInfoDataSource {
    suspend fun sendPaymentInfo(paymentInfo: PaymentInfo): Result<Boolean>
}