package com.tt.driver.data.di

import com.tt.driver.data.datastore.AuthDataStore
import com.tt.driver.data.datastore.UserDataStore
import com.tt.driver.data.network.APIsService
import com.tt.driver.data.repositories.auth.AuthRepository
import com.tt.driver.data.repositories.auth.AuthRepositoryImpl
import com.tt.driver.data.repositories.orders.OrdersRepository
import com.tt.driver.data.repositories.orders.OrdersRepositoryImpl
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
}