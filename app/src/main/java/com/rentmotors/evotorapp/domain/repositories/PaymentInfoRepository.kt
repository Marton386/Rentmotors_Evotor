package com.rentmotors.evotorapp.domain.repositories

import com.rentmotors.evotorapp.domain.common.Result
import com.rentmotors.evotorapp.domain.entities.PaymentInfo

interface PaymentInfoRepository {
    suspend fun sendPaymentInfo(paymentInfo: PaymentInfo): Result<Boolean>
}