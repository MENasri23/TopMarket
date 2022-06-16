package ir.jatlin.topmarket.core.domain

import ir.jatlin.topmarket.core.shared.Resource
import ir.jatlin.topmarket.core.shared.fail.ErrorHandler
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import timber.log.Timber

abstract class CoroutineUseCase<in P, R>(
    private val errorHandler: ErrorHandler,
    private val coroutineDispatcher: CoroutineDispatcher
) {

    suspend operator fun invoke(params: P): Resource<R> {
        return try {
            withContext(coroutineDispatcher) {
                execute(params).let {
                    Resource.success(it)
                }
            }
        } catch (t: Throwable) {
            Timber.e(t.stackTraceToString())
            Resource.error(errorHandler.handle(t))
        }
    }


    protected abstract suspend fun execute(params: P): R
}