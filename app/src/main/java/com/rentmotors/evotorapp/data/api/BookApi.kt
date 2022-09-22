package com.rentmotors.evotorapp.data.api

import com.rentmotors.evotorapp.data.api.entities.BookDTO
import retrofit2.Response
import retrofit2.http.*

interface BookApi {
    @FormUrlEncoded
    @POST("/appconfig/rmadmin/php/evotor/create_evotor_payment.php")
    suspend fun getBook(@Field("offer_id") resNumber: String): Response<BookDTO>

    @FormUrlEncoded
    @POST("/appconfig/rmadmin/php/evotor/create_evotor_payment_refund.php")
    suspend fun getRefundBook(@Field("offer_id") resNumber: String): Response<BookDTO>
}