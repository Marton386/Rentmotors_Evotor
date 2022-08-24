package com.rentmotors.evotorapp.domain.repositories

import com.rentmotors.evotorapp.domain.common.Result
import com.rentmotors.evotorapp.domain.entities.DeviceInfo

interface DeviceInfoRepository {
    suspend fun getDeviceInfo(kkmNumber: String): Result<DeviceInfo>
}