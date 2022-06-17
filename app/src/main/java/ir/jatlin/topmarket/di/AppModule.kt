package ir.jatlin.topmarket.di

import android.content.Context
import android.net.ConnectivityManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ir.jatlin.core.data.di.CpuDispatcher
import ir.jatlin.topmarket.ui.main.MarketNetworkManager
import ir.jatlin.topmarket.ui.main.MarketNetworkManagerImpl
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @Singleton
    fun provideApplicationScope(
        @CpuDispatcher dispatcher: CoroutineDispatcher
    ): CoroutineScope = CoroutineScope(dispatcher + SupervisorJob())


    @Provides
    fun provideConnectivityManager(@ApplicationContext context: Context): ConnectivityManager =
        context.applicationContext.getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager


    @Provides
    @Singleton
    fun provideMarketNetworkManager(
        connectivityManager: ConnectivityManager
    ): MarketNetworkManager = MarketNetworkManagerImpl(connectivityManager)
}