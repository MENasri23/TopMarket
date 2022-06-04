package ir.jatlin.topmarket.core.data.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.intPreferencesKey
import kotlinx.coroutines.flow.Flow
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

class PurchasePreferences @Inject constructor(
    private val customerDataStore: DataStore<Preferences>
) {


    val customerPreferencesFlow: Flow<PurchasePrefsInfo> = customerDataStore.data
        .catch { cause ->
            if (cause is IOException) {
                emit(emptyPreferences())
            }
            Timber.e("reading the customer preferences failed with error: $cause")
        }
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


    suspend fun saveCustomerId(id: Int) {
        customerDataStore.edit { preferences ->
            preferences[PreferencesKeys.CUSTOMER_ID] = id
        }
    }

    suspend fun saveAvtiveOrderId(id: Int) {
        customerDataStore.edit { preferences ->
            preferences[PreferencesKeys.ACTIVE_ORDER_ID] = id
        }
    }


    private object PreferencesKeys {
        val CUSTOMER_ID = intPreferencesKey("customer_id")
        val ACTIVE_ORDER_ID = intPreferencesKey("active_order_id")
    }

}