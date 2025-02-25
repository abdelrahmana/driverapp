package com.tt.driver.data.di

import com.tt.driver.data.datastore.AuthDataStore
import com.tt.driver.data.network.APIsService
import com.tt.driver.data.network.AuthorizationInterceptor
import com.tt.driver.data.network.ShipmentServiceApi
import com.tt.driver.data.network.SlotService
import com.waysgroup.speed.BuildConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {
    val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY // يمكنك تغييره حسب الحاجة
    }
    @Provides
    @Singleton
    fun provideOkHttpClient(authDataStore: AuthDataStore) =
        OkHttpClient
            .Builder()
            .addInterceptor(
                HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }
            )
            .addInterceptor(loggingInterceptor)
            .addInterceptor(AuthorizationInterceptor(authDataStore))
            .readTimeout(30, TimeUnit.SECONDS)
            .connectTimeout(30, TimeUnit.SECONDS)
            .build()

    @Provides
    @Singleton
    fun provideRetrofitClient(httpClient: OkHttpClient): Retrofit =
        Retrofit
            .Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(httpClient)
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    @Provides
    @Singleton
    fun providePIsService(retrofit: Retrofit): APIsService =
        retrofit.create(APIsService::class.java)

    @Provides
    @Singleton
    fun provideSlotService(retrofit: Retrofit): SlotService =
        retrofit.create(SlotService::class.java)

    @Provides
    @Singleton
    fun provideShipmentService(retrofit: Retrofit): ShipmentServiceApi =
        retrofit.create(ShipmentServiceApi::class.java)
}