package com.tt.driver.data.di

import com.tt.driver.data.datastore.AuthDataStore
import com.tt.driver.data.datastore.UserDataStore
import com.tt.driver.data.network.APIsService
import com.tt.driver.data.network.ShipmentServiceApi
import com.tt.driver.data.network.SlotService
import com.tt.driver.data.repositories.auth.AuthRepository
import com.tt.driver.data.repositories.auth.AuthRepositoryImpl
import com.tt.driver.data.repositories.orders.OrdersRepository
import com.tt.driver.data.repositories.orders.OrdersRepositoryImpl
import com.tt.driver.data.repositories.runsheet.RunSheetRepo
import com.tt.driver.data.repositories.runsheet.RunSheetRepoImplemeneter
import com.tt.driver.data.repositories.runsheet.SlotRepoImplemeneter
import com.tt.driver.data.repositories.shipment.ShipmentRepo
import com.tt.driver.data.repositories.shipment.ShipmentRepoImplemeneter
import com.tt.driver.data.repositories.slots.SlotsRepo
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RepositoriesModule {

    @Provides
    @Singleton
    fun provideAuthRepository(
        apIsService: APIsService,
        userDataStore: UserDataStore,
        authDataStore: AuthDataStore
    ): AuthRepository =
        AuthRepositoryImpl(apIsService, userDataStore, authDataStore)

    @Provides
    @Singleton
    fun provideOrdersRepository(apIsService: APIsService): OrdersRepository =
        OrdersRepositoryImpl(apIsService)
    @Provides
    @Singleton
    fun provideRepo(apIsService: APIsService): RunSheetRepo =
        RunSheetRepoImplemeneter(apIsService)
    @Provides
    @Singleton
    fun provideRepoSlot(apIsService: SlotService): SlotsRepo =
        SlotRepoImplemeneter(apIsService)
    @Provides
    @Singleton
    fun provideRepoShipment(apIsService: ShipmentServiceApi): ShipmentRepo =
        ShipmentRepoImplemeneter(apIsService)
}