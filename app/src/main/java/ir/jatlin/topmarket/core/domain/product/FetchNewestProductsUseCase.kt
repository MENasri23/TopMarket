package ir.jatlin.topmarket.core.domain.product

import ir.jatlin.topmarket.core.data.source.local.datastore.MarketPreferences
import ir.jatlin.topmarket.core.network.model.product.NetworkProduct
import ir.jatlin.topmarket.core.shared.Resource
import kotlinx.coroutines.flow.catch
import timber.log.Timber
import javax.inject.Inject

class FetchNewestProductsUseCase @Inject constructor(
    private val fetchProductsInDateRangeUseCase: FetchProductsInDateRangeUseCase,
    private val marketPreferences: MarketPreferences
) {

    private var result = Resource.success(emptyList<NetworkProduct>())

    suspend operator fun invoke(): Resource<List<NetworkProduct>> {
        marketPreferences.lastNewestProductDate
            .catch { emit(null) }
            .collect { lastDate ->
                if (lastDate != null) {
                    result = fetchProductsInDateRangeUseCase(lastDate to null)
                }
            }
        Timber.d("newest product result is: $result")

        return result
    }

}