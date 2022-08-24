package com.rentmotors.evotorapp.data.repositories.deviceInfo

import android.content.Context
import com.rentmotors.evotorapp.domain.common.Result
import com.rentmotors.evotorapp.domain.entities.DeviceInfo

interface DeviceInfoDataSource {
    suspend fun getDeviceInfo(kkmNumber: String): Result<DeviceInfo>
}