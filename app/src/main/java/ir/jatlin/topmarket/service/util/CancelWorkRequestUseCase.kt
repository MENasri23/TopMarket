package ir.jatlin.topmarket.service.util

import android.content.Context
import androidx.work.WorkManager
import dagger.hilt.android.qualifiers.ApplicationContext
import ir.jatlin.core.data.di.CpuDispatcher
import ir.jatlin.core.domain.CoroutineUseCase
import ir.jatlin.core.shared.fail.ErrorHandler
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