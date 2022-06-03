package ir.jatlin.topmarket.core.database.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ir.jatlin.topmarket.core.database.MarketDatabase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    private const val DATABASE_NAME = "topmarket-db"

    @Provides
    @Singleton
    fun provideDatabase(context: Context): MarketDatabase {
        return Room.databaseBuilder(
            context,
            MarketDatabase::class.java,
            DATABASE_NAME
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    fun provideCustomerDao(database: MarketDatabase) =
        database.customerDao()

    @Provides
    fun provideOrderDao(database: MarketDatabase) =
        database.orderDao()

    @Provides
    fun provideOrderItemDao(database: MarketDatabase) =
        database.orderItemDao()

}