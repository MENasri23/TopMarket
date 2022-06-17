package ir.jatlin.topmarket.core.domain.service

import android.content.Context
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.WorkManager
import dagger.hilt.android.qualifiers.ApplicationContext
import ir.jatlin.core.data.di.CpuDispatcher
import ir.jatlin.core.shared.fail.ErrorHandler
import ir.jatlin.topmarket.core.domain.CoroutineUseCase
import ir.jatlin.topmarket.service.sync.FetchNewestProductsWorker
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject


class EnqueueFetchNewestProductsWorkRequestUseCase @Inject constructor(
    @ApplicationContext private val context: Context,
    errorHandler: ErrorHandler,
    @CpuDispatcher dispatcher: CoroutineDispatcher
) : CoroutineUseCase<Int, Unit>(errorHandler, dispatcher) {

    override suspend fun execute(params: Int) {
        if (params < 1) return

        WorkManager.getInstance(context)
            .enqueueUniquePeriodicWork(
                FetchNewestProductsWorker.WORKER_NAME,
                ExistingPeriodicWorkPolicy.REPLACE,
                FetchNewestProductsWorker.setupFetchNewestProductsWorkRequest(params)
            )

    }
}