package ir.jatlin.topmarket.core.network.di

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class LoggingInterceptor


@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class HeaderInterceptor