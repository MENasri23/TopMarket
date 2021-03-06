package ir.jatlin.core.domain

import ir.jatlin.core.shared.Resource
import ir.jatlin.core.shared.fail.ErrorHandler
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.*
import timber.log.Timber


abstract class FlowUseCase<P, R>(
    private val errorHandler: ErrorHandler,
    private val dispatcher: CoroutineDispatcher
) {

    operator fun invoke(params: P): Flow<Resource<R>> = execute(params)
        .map {
            Resource.success(it)
        }
        .onStart {
            emit(Resource.loading())
        }
        .catch { cause ->
            Timber.e(cause.stackTraceToString())
            val errorCause = errorHandler.handle(cause)
            emit(Resource.error(errorCause))
        }.flowOn(dispatcher)


    protected abstract fun execute(params: P): Flow<R>
}