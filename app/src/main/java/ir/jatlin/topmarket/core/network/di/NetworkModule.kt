package ir.jatlin.topmarket.core.network.di

import com.google.gson.ExclusionStrategy
import com.google.gson.FieldAttributes
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ir.jatlin.topmarket.BuildConfig
import ir.jatlin.topmarket.core.network.api.MarketApi
import ir.jatlin.topmarket.core.network.serialization.ExcludeInSerialization
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    private const val BASE_URL = BuildConfig.BASE_URL

    @Singleton
    @Provides
    fun providesRetroFit(
        okHttpClient: OkHttpClient,
        gsonConverterFactory: GsonConverterFactory
    ): Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(gsonConverterFactory)
        .build()


    @Singleton
    @Provides
    fun jsonConvertorFactory(gson: Gson): GsonConverterFactory =
        GsonConverterFactory.create(gson)


    @Singleton
    @Provides
    fun provideGson(): Gson = GsonBuilder()
        .addSerializationExclusionStrategy(object : ExclusionStrategy {
            override fun shouldSkipField(f: FieldAttributes?): Boolean {
                return f?.getAnnotation(ExcludeInSerialization::class.java) != null
            }

            override fun shouldSkipClass(clazz: Class<*>?): Boolean {
                return false
            }
        })
        .create()


    @Singleton
    @Provides
    fun provideMarketApi(retrofit: Retrofit): MarketApi =
        retrofit.create(MarketApi::class.java)

}