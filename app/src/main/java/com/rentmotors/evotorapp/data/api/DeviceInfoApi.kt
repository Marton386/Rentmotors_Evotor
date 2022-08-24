package com.rentmotors.evotorapp.data.api

import com.rentmotors.evotorapp.data.api.entities.DeviceInfoDTO
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface DeviceInfoApi {
    @FormUrlEncoded
    @POST("/appconfig/rmadmin/php/evotor/check_kkm.php") // TODO: 13.11.2021
    suspend fun getDeviceInfo(@Field("kkm_number") kkmNumber: String): Response<DeviceInfoDTO>
}