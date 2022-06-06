package ir.jatlin.topmarket.core.data.source.local.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import timber.log.Timber
import java.io.IOException
import javax.inject.Inject


data class PurchasePrefsInfo(
    val customerId: Int,
    val activeOrderId: Int
) {


    companion object {
        const val GUEST_CUSTOMER = 0
        const val NO_ACTIVE_ORDER = -1
    }
}

class MarketPreferences @Inject constructor(
    private val marketDataStore: DataStore<Preferences>
) {


    val purchasePreferencesStream: Flow<PurchasePrefsInfo> = marketDataStore.data
        .catch { cause -> catch(cause) }
        .map { preferences ->
            val customerId = preferences[PreferencesKeys.CUSTOMER_ID]
                ?: PurchasePrefsInfo.GUEST_CUSTOMER
            val activeOrderId = preferences[PreferencesKeys.ACTIVE_ORDER_ID]
                ?: PurchasePrefsInfo.NO_ACTIVE_ORDER

            PurchasePrefsInfo(
                customerId = customerId,
                activeOrderId = activeOrderId
            )
        }

    val lastNewestProductDate: Flow<String?> = marketDataStore.data
        .catch { cause -> catch(cause) }
        .map { preferences ->
            preferences[PreferencesKeys.LAST_PRODUCT_DATE]
        }

    private suspend fun FlowCollector<Preferences>.catch(cause: Throwable) {
        if (cause is IOException) {
            emit(emptyPreferences())
        }
        Timber.e("reading from dataStore: $marketDataStore failed with error: $cause")
    }


    suspend fun saveCustomerId(id: Int) {
        marketDataStore.edit { preferences ->
            preferences[PreferencesKeys.CUSTOMER_ID] = id
        }
    }

    suspend fun saveActiveOrderId(id: Int) {
        marketDataStore.edit { preferences ->
            preferences[PreferencesKeys.ACTIVE_ORDER_ID] = id
        }
    }

    suspend fun saveLastProductDate(date: String) {
        marketDataStore.edit { preferences ->
            preferences[PreferencesKeys.LAST_PRODUCT_DATE] = date
        }
    }


    private object PreferencesKeys {
        val CUSTOMER_ID = intPreferencesKey("customer_id")
        val ACTIVE_ORDER_ID = intPreferencesKey("active_order_id")

        val LAST_PRODUCT_DATE = stringPreferencesKey("last_product_date")
    }

}