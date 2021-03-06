package ir.jatlin.core.domain.product

import ir.jatlin.core.data.di.IODispatcher
import ir.jatlin.core.model.common.ProductImage
import ir.jatlin.core.shared.Resource
import ir.jatlin.core.shared.fail.ErrorCause
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class FetchProductBanners @Inject constructor(
    private val fetchProductDetailsListUseCase: FetchProductDetailsUseCase,
    @IODispatcher private val dispatcher: CoroutineDispatcher
) {

    operator fun invoke(): Flow<Resource<List<ProductImage>>> {
        return flow<Resource<List<ProductImage>>> {
            when (val result = fetchProductDetailsListUseCase(608)) {
                is Resource.Loading<*> -> emit(Resource.loading())
                is Resource.Error -> emit(
                    Resource.error(result.cause ?: ErrorCause.Unknown())
                )
                is Resource.Success<*> -> {
                    emit(Resource.success(result.data!!.images))
                }
            }
        }.flowOn(dispatcher)

    }

}