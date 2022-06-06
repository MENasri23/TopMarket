package ir.jatlin.topmarket.core.data.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStoreFile
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataStoreModule {

    private const val PURCHASE_PREFERENCES_NAME = "purchase_preferences"
    private const val PRODUCT_PREFERENCES_NAME = "product_preferences"

    @Provides
    @Singleton
    @PurchasePreferencesDataStore
    fun providePurchasePreferencesDataStore(
        @ApplicationContext context: Context,
        @IODispatcher dispatcher: CoroutineDispatcher
    ): DataStore<Preferences> {
        return PreferenceDataStoreFactory.create(
            scope = CoroutineScope(dispatcher + SupervisorJob()),
            corruptionHandler = ReplaceFileCorruptionHandler(
                produceNewData = { emptyPreferences() }
            ),
            produceFile = {
                context.preferencesDataStoreFile(PURCHASE_PREFERENCES_NAME)
            }
        )
    }

    @Provides
    @Singleton
    @ProductPreferencesDataStore
    fun provideProductPreferencesDataStore(
        @ApplicationContext context: Context,
        @IODispatcher dispatcher: CoroutineDispatcher
    ): DataStore<Preferences> {
        return PreferenceDataStoreFactory.create(
            scope = CoroutineScope(dispatcher + SupervisorJob()),
            corruptionHandler = ReplaceFileCorruptionHandler(
                produceNewData = { emptyPreferences() }
            ),
            produceFile = {
                context.preferencesDataStoreFile(PRODUCT_PREFERENCES_NAME)
            }
        )
    }
}

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class PurchasePreferencesDataStore

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class ProductPreferencesDataStore