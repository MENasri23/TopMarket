package ir.jatlin.topmarket.core.domain.category

import ir.jatlin.core.model.category.CategoryDetails
import ir.jatlin.core.shared.fail.ErrorHandler
import ir.jatlin.topmarket.core.data.di.IODispatcher
import ir.jatlin.topmarket.core.data.repository.ProductRepository
import ir.jatlin.topmarket.core.domain.FlowUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import timber.log.Timber
import javax.inject.Inject

class FetchCategoryDetailsListUseCase @Inject constructor(
    private val productRepository: ProductRepository,
    errorHandler: ErrorHandler,
    @IODispatcher dispatcher: CoroutineDispatcher
) : FlowUseCase<FetchCategoryDetailsListUseCase.Parameters, List<CategoryDetails>>(
    errorHandler,
    dispatcher
) {
    override fun execute(params: Parameters): Flow<List<CategoryDetails>> {
        Timber.d("execute fetch")
        return productRepository.getProductCategories(
            page = params.page,
            pageSize = params.pageSize,
            filters = params.filters
        )
    }

    data class Parameters(
        val page: Int,
        val pageSize: Int,
        val filters: Map<String, String>
    )
}