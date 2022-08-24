package com.rentmotors.evotorapp.data.repositories.deviceInfo

import com.rentmotors.evotorapp.domain.common.Result
import com.rentmotors.evotorapp.domain.entities.DeviceInfo
import com.rentmotors.evotorapp.domain.repositories.DeviceInfoRepository

class DeviceInfoRepositoryImpl(
    private val deviceInfoDataSource: DeviceInfoDataSource
): DeviceInfoRepository {
    override suspend fun getDeviceInfo(kkmNumber: String): Result<DeviceInfo> {
        return deviceInfoDataSource.getDeviceInfo(kkmNumber)
    }
}