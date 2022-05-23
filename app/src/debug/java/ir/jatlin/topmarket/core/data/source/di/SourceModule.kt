package ir.jatlin.topmarket.core.data.source.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ir.jatlin.topmarket.core.data.source.remote.FakeProductRemoteDataSource
import ir.jatlin.topmarket.core.data.source.remote.ProductRemoteDataSource

@Module
@InstallIn(SingletonComponent::class)
interface SourceModule {

    @Binds
    fun bindsProductRemoteDataSource(
        remoteDataSource: FakeProductRemoteDataSource
    ): ProductRemoteDataSource
}