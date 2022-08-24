package com.rentmotors.evotorapp.data.api.entities

import com.rentmotors.evotorapp.domain.entities.DeviceInfo

data class DeviceInfoDTO(
    val id: Int,
    val key: String
)

fun DeviceInfoDTO.toDeviceInfo(): DeviceInfo {
    return DeviceInfo(
        id,
        key.toLong(),
        (id * key.toLong()).toString()
    )
}

