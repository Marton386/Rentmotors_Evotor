package com.rentmotors.evotorapp.data.api

import com.rentmotors.evotorapp.data.api.entities.PaymentInfoDTO
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Url

interface PaymentInfoApi {
    @POST
    suspend fun sendPaymentInfo(
        @Url url: String,
        @Body paymentInfoApi: PaymentInfoDTO
    ): Response<ResponseBody>
}