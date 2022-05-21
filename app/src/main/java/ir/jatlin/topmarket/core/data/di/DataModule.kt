package ir.jatlin.topmarket.core.data.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ir.jatlin.topmarket.core.data.repository.ProductRepository

@Module
@InstallIn(SingletonComponent::class)
interface DataModule {

    @Binds
    fun bindsProductRepository(
        productRepository: ProductRepository
    ): ProductRepository

}