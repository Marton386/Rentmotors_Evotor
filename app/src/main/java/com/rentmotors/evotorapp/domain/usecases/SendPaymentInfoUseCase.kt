package com.rentmotors.evotorapp.domain.usecases

import com.rentmotors.evotorapp.domain.entities.PaymentInfo
import com.rentmotors.evotorapp.domain.repositories.PaymentInfoRepository
import javax.inject.Inject

class SendPaymentInfoUseCase @Inject constructor(
    private val paymentInfoRepository: PaymentInfoRepository,
) {
    suspend operator fun invoke(paymentInfo: PaymentInfo) =
        paymentInfoRepository.sendPaymentInfo(paymentInfo)
}