package ir.jatlin.topmarket.service.sync

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import ir.jatlin.topmarket.core.data.di.IODispatcher
import ir.jatlin.topmarket.core.domain.product.FetchNewestProductsUseCase
import ir.jatlin.topmarket.core.shared.Resource
import ir.jatlin.topmarket.service.notification.sendNewestProductsNotification
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import timber.log.Timber


@HiltWorker
class FetchNewestProductsWorker @AssistedInject constructor(
    private val fetchNewestProductsUseCase: FetchNewestProductsUseCase,
    @IODispatcher private val dispatcher: CoroutineDispatcher,
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        val result = withContext(dispatcher) {
            val result = fetchNewestProductsUseCase()
            if (result is Resource.Error) return@withContext Result.retry()

            if (result is Resource.Success) {
                val products = result.data
                if (products == null) {
                    Timber.d("retrieved a success result with null data")
                    return@withContext Result.failure()
                }

                if (products.isNotEmpty()) {
                    applicationContext.sendNewestProductsNotification(products)
                }
            }
            Result.success()
        }


        return result
    }
}