package ir.jatlin.topmarket.core.domain.service

import android.content.Context
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.WorkManager
import dagger.hilt.android.qualifiers.ApplicationContext
import ir.jatlin.topmarket.core.data.di.CpuDispatcher
import ir.jatlin.topmarket.core.domain.CoroutineUseCase
import ir.jatlin.topmarket.core.shared.fail.ErrorHandler
import ir.jatlin.topmarket.service.sync.FetchNewestProductsWorker
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class CancelWorkRequestUseCase @Inject constructor(
    @ApplicationContext private val context: Context,
    errorHandler: ErrorHandler,
    @CpuDispatcher dispatcher: CoroutineDispatcher
) : CoroutineUseCase<String, Unit>(errorHandler, dispatcher) {

    override suspend fun execute(params: String) {
        WorkManager.getInstance(context)
            .cancelUniqueWork(params)

    }
}