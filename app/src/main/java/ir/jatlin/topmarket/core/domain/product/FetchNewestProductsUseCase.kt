package ir.jatlin.topmarket.core.domain.product

import ir.jatlin.core.data.source.local.datastore.MarketPreferences
import ir.jatlin.core.model.product.Product
import ir.jatlin.core.shared.Resource
import ir.jatlin.core.shared.isSuccess
import ir.jatlin.topmarket.core.domain.util.makeProductParams
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.firstOrNull
import timber.log.Timber
import javax.inject.Inject

class FetchNewestProductsUseCase @Inject constructor(
    private val fetchProductsInDateRangeUseCase: FetchProductsInDateRangeUseCase,
    private val fetchProductsListUseCase: FetchProductsListUseCase,
    private val marketPreferences: MarketPreferences
) {

    suspend operator fun invoke(): Resource<List<Product>> {
        val lastDate = marketPreferences.lastNewestProductDate
            .catch { emit(null) }
            .firstOrNull()

        if (lastDate != null) {
            val result = fetchProductsInDateRangeUseCase(lastDate to null)
            Timber.d("newest product result is: $result")
            saveLastProductDate(result)
            return result
        }

        /* First attempt, so fetch last product and save its date */
        val firstAttempt = fetchProductsListUseCase(
            params = makeProductParams { pageSize = 1 }
        )
        saveLastProductDate(firstAttempt)

        return Resource.success(emptyList())

    }

    private suspend fun saveLastProductDate(result: Resource<List<Product>>) {
        if (result.isSuccess) {
            val newLastDate = result.data?.firstOrNull()?.createdDate
            if (newLastDate != null) {
                marketPreferences.saveLastProductDate(newLastDate)
            }
        }
    }

}