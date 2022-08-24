package com.rentmotors.evotorapp.presentation.di

import com.rentmotors.evotorapp.data.repositories.book.ReceiptDataSource
import com.rentmotors.evotorapp.data.repositories.book.ReceiptRepositoryImpl
import com.rentmotors.evotorapp.data.repositories.deviceInfo.DeviceInfoDataSource
import com.rentmotors.evotorapp.data.repositories.deviceInfo.DeviceInfoRepositoryImpl
import com.rentmotors.evotorapp.data.repositories.paymentInfo.PaymentInfoDataSource
import com.rentmotors.evotorapp.data.repositories.paymentInfo.PaymentInfoRepositoryImpl
import com.rentmotors.evotorapp.domain.repositories.DeviceInfoRepository
import com.rentmotors.evotorapp.domain.repositories.PaymentInfoRepository
import com.rentmotors.evotorapp.domain.repositories.ReceiptRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideReceiptRepository(receiptDataSource: ReceiptDataSource): ReceiptRepository {
        return ReceiptRepositoryImpl(receiptDataSource)
    }

    @Provides
    @Singleton
    fun provideDeviceInfoRepository(deviceInfoDataSource: DeviceInfoDataSource): DeviceInfoRepository {
        return DeviceInfoRepositoryImpl(deviceInfoDataSource)
    }

    @Provides
    @Singleton
    fun providePaymentInfoRepository(paymentInfoDataSource: PaymentInfoDataSource): PaymentInfoRepository {
        return PaymentInfoRepositoryImpl(paymentInfoDataSource)
    }

}