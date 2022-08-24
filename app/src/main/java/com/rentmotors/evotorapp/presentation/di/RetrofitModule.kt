package com.rentmotors.evotorapp.presentation.di

import com.rentmotors.evotorapp.data.api.BookApi
import com.rentmotors.evotorapp.data.api.DeviceInfoApi
import com.rentmotors.evotorapp.data.api.HeaderInterceptor
import com.rentmotors.evotorapp.data.api.PaymentInfoApi
import com.rentmotors.evotorapp.presentation.utils.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.apache.http.conn.ssl.AbstractVerifier.getDNSSubjectAlts
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.security.cert.Certificate
import java.security.cert.X509Certificate
import java.util.*
import java.util.regex.Pattern.matches
import javax.inject.Singleton
import javax.net.ssl.*


@Module
@InstallIn(SingletonComponent::class)
object RetrofitModule {

    @Provides
    @Singleton
    fun provideHeaderInterceptor(): HeaderInterceptor {
        return HeaderInterceptor()
    }

    @Provides
    @Singleton
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }
    }

    @Provides
    @Singleton
    fun provideHttpClient(
        logging: HttpLoggingInterceptor,
        headerInterceptor: HeaderInterceptor,
        hostnameVerifier: HostnameVerifier
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(logging)
            .addInterceptor(headerInterceptor)
            .hostnameVerifier(hostnameVerifier)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(httpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .client(httpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    @Provides
    @Singleton
    fun provideBookApi(retrofit: Retrofit): BookApi {
        return retrofit.create(BookApi::class.java)
    }

    @Provides
    @Singleton
    fun provideDeviceInfoApi(retrofit: Retrofit): DeviceInfoApi {
        return retrofit.create(DeviceInfoApi::class.java)
    }

    @Provides
    @Singleton
    fun providePaymentInfoApi(retrofit: Retrofit): PaymentInfoApi {
        return retrofit.create(PaymentInfoApi::class.java)
    }

    @Provides
    @Singleton
    fun provideHostnameVerifier() = HostnameVerifier { hostname, session ->
        val certs: Array<Certificate> = try {
            session.peerCertificates
        } catch (e: SSLException) {
            return@HostnameVerifier false
        }
        val x509 = certs[0] as X509Certificate
        // We can be case-insensitive when comparing the host we used to
        // establish the socket to the hostname in the certificate.
        // We can be case-insensitive when comparing the host we used to
        // establish the socket to the hostname in the certificate.
        val hostName = hostname.trim().lowercase(Locale.ENGLISH)
        // Verify the first CN provided. Other CNs are ignored. Firefox, wget,
        // curl, and Sun Java work this way.
        // Verify the first CN provided. Other CNs are ignored. Firefox, wget,
        // curl, and Sun Java work this way.
        val firstCn = getFirstCn(x509)
        if (matches(hostName, firstCn)) {
            return@HostnameVerifier true
        }
        for (cn in getDNSSubjectAlts(x509)) {
            if (matches(hostName, cn)) {
                return@HostnameVerifier true
            }
        }
        return@HostnameVerifier false
    }

    private fun getFirstCn(cert: X509Certificate): String? {
        val subjectPrincipal: String = cert.subjectX500Principal.toString()
        for (token in subjectPrincipal.split(",".toRegex()).toTypedArray()) {
            val x = token.indexOf("CN=")
            if (x >= 0) {
                return token.substring(x + 3)
            }
        }
        return null
    }
}