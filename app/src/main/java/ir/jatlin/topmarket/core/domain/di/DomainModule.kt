package ir.jatlin.topmarket.core.domain.di

import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ir.jatlin.topmarket.core.domain.util.CharSequenceDistance
import ir.jatlin.topmarket.core.domain.util.DefaultErrorHandler
import ir.jatlin.topmarket.core.domain.util.LevenshteinDistance
import ir.jatlin.topmarket.core.shared.fail.ErrorHandler
import java.text.SimpleDateFormat
import java.time.Clock
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*

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