package ir.jatlin.topmarket.core.domain.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ir.jatlin.topmarket.core.domain.util.DefaultErrorHandler
import ir.jatlin.topmarket.core.shared.fail.ErrorHandler

@Module
@InstallIn(SingletonComponent::class)
interface DomainModule {

    @Binds
    fun bindsErrorHandler(
        handler: DefaultErrorHandler
    ): ErrorHandler
}