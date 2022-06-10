package ir.jatlin.topmarket.core.domain.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import java.time.Clock
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*

@Module
@InstallIn(SingletonComponent::class)
object DateModule {

    @Provides
    fun defaultDateGmtFormat(): DateTimeFormatter = DateTimeFormatter.ofPattern(
        "yyyy-MM-dd'T'HH:mm:ss",
        Locale.getDefault()
    )

    @Provides
    fun clock(): Clock = Clock.system(ZoneId.of("GMT"))
}