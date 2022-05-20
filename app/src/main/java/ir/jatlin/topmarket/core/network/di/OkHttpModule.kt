package ir.jatlin.topmarket.core.network.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ir.jatlin.topmarket.BuildConfig
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object OkHttpModule {
    private const val API_KEY = BuildConfig.CONSUMER_KEY
    private const val API_SECRET = BuildConfig.CONSUMER_SECRET


    @Singleton
    @Provides
    fun provideOkHttpClient(
        @HeaderInterceptor header: Interceptor,
        @LoggingInterceptor logging: Interceptor
    ): OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(header)
        .addInterceptor(logging)
        .build()


    @Provides
    @HeaderInterceptor
    fun provideHeaderInterceptor() = Interceptor { chain ->
        val request = chain.request()
        val newRequest = request.newBuilder().apply {
            url(
                request.url.newBuilder()
                    .addQueryParameter("consumer_key", API_KEY)
                    .addQueryParameter("consumer_secret", API_SECRET)
                    .build()
            )
        }.build()

        chain.proceed(newRequest)
    }


    @Provides
    @LoggingInterceptor
    fun provideHttpLoggingInterceptor(): Interceptor =
        HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

}