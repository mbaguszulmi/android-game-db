package co.mbznetwork.gamesforyou.di

import co.mbznetwork.gamesforyou.model.dispatcher.DefaultDispatcherProvider
import co.mbznetwork.gamesforyou.model.dispatcher.DispatcherProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CoroutineModule {

    @Provides
    @Singleton
    fun provideDispatcherProvider(): DispatcherProvider = DefaultDispatcherProvider()

    @Provides
    @IODispatcher
    fun provideIODispatcher(): CoroutineDispatcher = Dispatchers.IO

    @Provides
    @MainDispatcher
    fun provideMainDispatcher(dispatcherProvider: DispatcherProvider) = dispatcherProvider.main

    @Provides
    @DefaultDispatcher
    fun provideDefaultDispatcher(dispatcherProvider: DispatcherProvider) = dispatcherProvider.default

}