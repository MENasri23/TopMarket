package ir.jatlin.topmarket.core.database.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ir.jatlin.core.model.user.Customer
import ir.jatlin.topmarket.core.database.MarketDatabase
import ir.jatlin.topmarket.core.database.entity.asCustomerEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    private const val DATABASE_NAME = "topmarket-db"

    private lateinit var marketDatabase: MarketDatabase

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): MarketDatabase {
        return Room.databaseBuilder(
            context,
            MarketDatabase::class.java,
            DATABASE_NAME
        )
            .addCallback(object : RoomDatabase.Callback() {
                override fun onCreate(db: SupportSQLiteDatabase) {
                    super.onCreate(db)
                    prepopulateDatabase()
                }
            })
            .fallbackToDestructiveMigration()
            .build().also { marketDatabase = it }
    }

    private fun prepopulateDatabase() {
        with(Dispatchers.IO) {
            GlobalScope.launch {
                if (this@DatabaseModule::marketDatabase.isInitialized) {
                    marketDatabase.customerDao().insert(
                        Customer.Empty.asCustomerEntity()
                    )
                }
            }
        }
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