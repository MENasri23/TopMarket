package ir.jatlin.topmarket.core.data.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ir.jatlin.topmarket.core.data.repository.DefaultProductRepository
import ir.jatlin.topmarket.core.data.repository.ProductRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface DataModule {

    @Binds
    @Singleton
    fun bindsProductRepository(
        productRepository: DefaultProductRepository
    ): ProductRepository

}