package com.rentmotors.evotorapp.domain.usecases

import com.rentmotors.evotorapp.data.api.HeaderInterceptor
import com.rentmotors.evotorapp.domain.common.Result
import com.rentmotors.evotorapp.domain.entities.DeviceInfo
import com.rentmotors.evotorapp.domain.repositories.DeviceInfoRepository
import javax.inject.Inject

class GetDeviceInfoUseCase @Inject constructor(
    private val deviceInfoRepository: DeviceInfoRepository,
    private val headerInterceptor: HeaderInterceptor
) {
    suspend operator fun invoke(kkmNumber: String): Result<DeviceInfo> {
        val result = deviceInfoRepository.getDeviceInfo(kkmNumber)

        if (result is Result.Success) {
            headerInterceptor.setToken(result.data.token)
        }

        return result
    }
}