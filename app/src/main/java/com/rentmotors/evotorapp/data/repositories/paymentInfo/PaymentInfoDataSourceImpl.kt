package com.rentmotors.evotorapp.data.repositories.paymentInfo

import com.rentmotors.evotorapp.R
import com.rentmotors.evotorapp.data.api.ErrorCodes
import com.rentmotors.evotorapp.data.api.PaymentInfoApi
import com.rentmotors.evotorapp.data.api.entities.PaymentInfoDTO
import com.rentmotors.evotorapp.domain.common.Result
import com.rentmotors.evotorapp.domain.entities.PaymentInfo
import com.rentmotors.evotorapp.presentation.utils.StringHelper
import retrofit2.HttpException
import java.net.UnknownHostException

class PaymentInfoDataSourceImpl(
    private val paymentInfoApi: PaymentInfoApi,
    private val stringHelper: StringHelper
) : PaymentInfoDataSource {
    override suspend fun sendPaymentInfo(paymentInfo: PaymentInfo): Result<Boolean> {
        return try {
            val response = paymentInfoApi.sendPaymentInfo(
                paymentInfo.bookReceipt.urlSellInfo,
                PaymentInfoDTO(paymentInfo)
            )
            if (response.isSuccessful) {
                Result.Success(true)
            } else {
                throw HttpException(response)
            }
        } catch (e: HttpException) {
            val errorRes = when (e.code()) {
                ErrorCodes.NOT_FOUND -> R.string.error
                else -> 0
            }

            val message = if (errorRes > 0) stringHelper.getString(errorRes)
            else "${e.code()} ${e.message()}"

            Result.Error(message)
        } catch (e: UnknownHostException) {
            Result.Error(stringHelper.getString(R.string.check_the_internet_connection))
        } catch (e: Exception) {
            Result.Error(e.message)
        }
    }
}