package com.rentmotors.evotorapp.data.repositories.deviceInfo

import com.rentmotors.evotorapp.R
import com.rentmotors.evotorapp.data.api.DeviceInfoApi
import com.rentmotors.evotorapp.data.api.ErrorCodes
import com.rentmotors.evotorapp.data.api.entities.toDeviceInfo
import com.rentmotors.evotorapp.domain.common.Result
import com.rentmotors.evotorapp.domain.entities.DeviceInfo
import com.rentmotors.evotorapp.presentation.utils.StringHelper
import retrofit2.HttpException
import java.net.UnknownHostException

class DeviceInfoDataSourceImpl(
    private val deviceInfoApi: DeviceInfoApi,
    private val stringHelper: StringHelper
) : DeviceInfoDataSource {
    override suspend fun getDeviceInfo(kkmNumber: String): Result<DeviceInfo> {
        return try {
            val response = deviceInfoApi.getDeviceInfo(kkmNumber)
            // TODO: 13.12.2021   check string or int Key??
            if (response.isSuccessful) {
                Result.Success(response.body()!!.toDeviceInfo())
            } else {
                throw HttpException(response)
            }
        } catch (e: HttpException) {
            val errorRes = when (e.code()) {
                ErrorCodes.NOT_FOUND -> R.string.check_kkm_number_enter_correct

                // TODO: 31.01.2022 more errors?
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