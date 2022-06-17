package ir.jatlin.topmarket.service.sync

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.*
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import ir.jatlin.core.model.product.Product
import ir.jatlin.topmarket.core.data.di.IODispatcher
import ir.jatlin.topmarket.core.domain.product.FetchNewestProductsUseCase
import ir.jatlin.topmarket.core.shared.Resource
import ir.jatlin.topmarket.service.notification.sendNewestProductsNotification
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.util.concurrent.TimeUnit


@HiltWorker
class FetchNewestProductsWorker @AssistedInject constructor(
    private val fetchNewestProductsUseCase: FetchNewestProductsUseCase,
    @IODispatcher private val dispatcher: CoroutineDispatcher,
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        val result = withContext(dispatcher) {
            val resource = fetchNewestProductsUseCase()
            if (resource is Resource.Error) return@withContext Result.retry()

            if (resource is Resource.Success) {
                val products = resource.data
                if (products == null) {
                    Timber.d("retrieved a success result with null data")
                    return@withContext Result.failure()
                }
                val includeIds = products.map(Product::id).joinToString(separator = ",")
                applicationContext.sendNewestProductsNotification(includeIds, products.size)
//                if (products.isNotEmpty()) {
//                    applicationContext.sendNewestProductsNotification(products)
//                }
            }
            Result.success()
        }


        return result
    }

    companion object {

        const val WORKER_NAME = "ir.jatlin.topmarket.service.sync.FetchNewestProductsWorker"
        const val TAG = "FetchNewestProductsWorkerTag"

        fun setupFetchNewestProductsWorkRequest(interval: Int) =
            PeriodicWorkRequestBuilder<FetchNewestProductsWorker>(
                interval.toLong(), TimeUnit.MINUTES
            )
//                .setInitialDelay(15, TimeUnit.MINUTES)
                .addTag(TAG)
                .setConstraints(fetchNewestProductsConstraints())
                .build()


        private fun fetchNewestProductsConstraints() = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()


    }
}