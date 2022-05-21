package ir.jatlin.topmarket.core.domain

import ir.jatlin.topmarket.core.shared.Resource
import ir.jatlin.topmarket.core.shared.fail.ErrorHandler
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.*


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
            val errorCause = errorHandler.handle(cause)
            emit(Resource.error(errorCause))
        }.flowOn(dispatcher)


    protected abstract fun execute(params: P): Flow<R>
}