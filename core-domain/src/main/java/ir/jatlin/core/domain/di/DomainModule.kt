package ir.jatlin.core.domain.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ir.jatlin.core.domain.util.CharSequenceDistance
import ir.jatlin.core.domain.util.DefaultErrorHandler
import ir.jatlin.core.domain.util.LevenshteinDistance
import ir.jatlin.core.shared.fail.ErrorHandler

@Module
@InstallIn(SingletonComponent::class)
interface DomainModule {

    @Binds
    fun bindsErrorHandler(
        handler: DefaultErrorHandler
    ): ErrorHandler


    @Binds
    fun bindsCharSequenceDistance(
        charSequenceDistance: LevenshteinDistance
    ): CharSequenceDistance

}