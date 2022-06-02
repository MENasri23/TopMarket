package ir.jatlin.topmarket.core.database.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import ir.jatlin.topmarket.core.database.MarketDatabase
import javax.inject.Singleton

@Module
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
    fun providePurchaseProductDao(database: MarketDatabase) =
        database.purchaseProductDao()

}