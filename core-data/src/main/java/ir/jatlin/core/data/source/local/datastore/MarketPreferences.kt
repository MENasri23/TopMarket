package ir.jatlin.core.data.source.local.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import ir.jatlin.core.model.Theme
import ir.jatlin.core.model.purchase.PurchasePrefsInfo
import ir.jatlin.core.shared.theme.ThemeUtils
import kotlinx.coroutines.flow.*
import timber.log.Timber
import java.io.IOException
import javax.inject.Inject


class MarketPreferences @Inject constructor(
    private val marketDataStore: DataStore<Preferences>
) {


    val purchasePreferencesStream: Flow<PurchasePrefsInfo> by lazy {
        marketData
            .map { preferences ->
                val customerId = preferences[PreferencesKeys.CUSTOMER_ID]
                    ?: PurchasePrefsInfo.GUEST_CUSTOMER
                val activeOrderId = preferences[PreferencesKeys.ACTIVE_ORDER_ID]
                    ?: PurchasePrefsInfo.NO_ACTIVE_ORDER

                PurchasePrefsInfo(
                    customerId = customerId,
                    activeOrderId = activeOrderId
                ).also { Timber.d("$it") }
            }
    }

    val lastNewestProductDate: Flow<String?> by lazy {
        marketData
            .map { preferences ->
                preferences[PreferencesKeys.LAST_PRODUCT_DATE]
            }
    }

    val selectedTheme: Flow<Theme> = marketData.map { preferences ->
        preferences[PreferencesKeys.THEME_MODE]?.let {
            Theme.fromPreferenceKey(it)
        } ?: ThemeUtils.defaultTheme()

    }.distinctUntilChanged()

    val enableNotification = marketData.map { preferences ->
        preferences[PreferencesKeys.NOTIFICATION] ?: true
    }

    val notificationInterval = marketData.map { preferences ->
        preferences[PreferencesKeys.NOTIFICATION_INTERVAL] ?: 5
    }

    private val marketData
        get() = marketDataStore.data.catch { cause -> catch(cause) }

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

    suspend fun saveTheme(theme: Theme) {
        marketDataStore.edit { preferences ->
            preferences[PreferencesKeys.THEME_MODE] = theme.prefsKey
        }
    }

    suspend fun enableNotification(isEnabled: Boolean) {
        marketDataStore.edit { preferences ->
            preferences[PreferencesKeys.NOTIFICATION] = isEnabled
        }
    }

    suspend fun saveNotificationInterval(interval: Int) {
        marketDataStore.edit { preferences ->
            preferences[PreferencesKeys.NOTIFICATION_INTERVAL] = interval
        }
    }

    suspend fun clearActiveOrder() {
        marketDataStore.edit { preferences ->
            preferences[PreferencesKeys.ACTIVE_ORDER_ID] = PurchasePrefsInfo.NO_ACTIVE_ORDER
        }
    }


    private object PreferencesKeys {
        val CUSTOMER_ID = intPreferencesKey("customer_id")
        val ACTIVE_ORDER_ID = intPreferencesKey("active_order_id")
        val LAST_PRODUCT_DATE = stringPreferencesKey("last_product_date")
        val THEME_MODE = stringPreferencesKey("theme_mode")
        val NOTIFICATION = booleanPreferencesKey("notification")
        val NOTIFICATION_INTERVAL = intPreferencesKey("notification_interval")
    }

}