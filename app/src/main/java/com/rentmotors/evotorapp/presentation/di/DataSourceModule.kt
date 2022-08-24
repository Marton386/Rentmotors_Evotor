package com.rentmotors.evotorapp.presentation.di

import android.content.Context
import com.rentmotors.evotorapp.data.api.BookApi
import com.rentmotors.evotorapp.data.api.DeviceInfoApi
import com.rentmotors.evotorapp.data.api.PaymentInfoApi
import com.rentmotors.evotorapp.data.helpers.SettingsHelper
import com.rentmotors.evotorapp.data.repositories.book.ReceiptDataSource
import com.rentmotors.evotorapp.data.repositories.book.ReceiptDataSourceImpl
import com.rentmotors.evotorapp.data.repositories.deviceInfo.DeviceInfoDataSource
import com.rentmotors.evotorapp.data.repositories.deviceInfo.DeviceInfoDataSourceImpl
import com.rentmotors.evotorapp.data.repositories.paymentInfo.PaymentInfoDataSource
import com.rentmotors.evotorapp.data.repositories.paymentInfo.PaymentInfoDataSourceImpl
import com.rentmotors.evotorapp.presentation.utils.StringHelper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataSourceModule {

    @Provides
    @Singleton
    fun provideReceiptDataSource(bookApi: BookApi, stringHelper: StringHelper): ReceiptDataSource {
        return ReceiptDataSourceImpl(bookApi, stringHelper)
    }

    @Provides
    @Singleton
    fun provideDeviceInfoDataSource(
        deviceInfoApi: DeviceInfoApi,
        stringHelper: StringHelper
    ): DeviceInfoDataSource {
        return DeviceInfoDataSourceImpl(deviceInfoApi, stringHelper)
    }

    @Provides
    @Singleton
    fun provideSettingsHelper(@ApplicationContext context: Context): SettingsHelper {
        return SettingsHelper(context)
    }

    @Provides
    @Singleton
    fun provideStringHelper(@ApplicationContext context: Context): StringHelper {
        return StringHelper(context)
    }

    @Provides
    @Singleton
    fun providePaymentInfoDataSource(
        paymentInfoApi: PaymentInfoApi,
        stringHelper: StringHelper
    ): PaymentInfoDataSource {
        return PaymentInfoDataSourceImpl(paymentInfoApi, stringHelper)
    }
}