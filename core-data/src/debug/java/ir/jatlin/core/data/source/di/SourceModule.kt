package ir.jatlin.topmarket.core.data.source.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ir.jatlin.core.data.source.remote.product.ProductRemoteDataSource
import ir.jatlin.topmarket.core.data.source.remote.FakeProductRemoteDataSource

@Module
@InstallIn(SingletonComponent::class)
interface SourceModule {

    @Binds
    fun bindsProductRemoteDataSource(
        remoteDataSource: FakeProductRemoteDataSource
    ): ProductRemoteDataSource
}