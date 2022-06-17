package ir.jatlin.topmarket

import android.app.Application
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltAndroidApp
class MarketApplication : Application(), Configuration.Provider {

    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    @Inject
    lateinit var scope: CoroutineScope

    override fun onCreate() {
        super.onCreate()
        delayInitComponents()
    }

    private fun delayInitComponents() {
        scope.launch {
            if (BuildConfig.DEBUG) Timber.plant(Timber.DebugTree())
        }
    }

    override fun getWorkManagerConfiguration(): Configuration {
        return Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .setMinimumLoggingLevel(android.util.Log.DEBUG)
            .build()
    }

    companion object {
        const val INITIAL_INTERVAL = 5
    }
}