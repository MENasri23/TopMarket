package ir.jatlin.topmarket.core.domain.product

import ir.jatlin.topmarket.core.data.source.local.datastore.MarketPreferences
import ir.jatlin.topmarket.core.network.model.product.NetworkProduct
import ir.jatlin.topmarket.core.shared.Resource
import ir.jatlin.topmarket.core.shared.isSuccess
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.flow.lastOrNull
import kotlinx.coroutines.sync.Mutex
import timber.log.Timber
import javax.inject.Inject

class FetchNewestProductsUseCase @Inject constructor(
    private val fetchProductsInDateRangeUseCase: FetchProductsInDateRangeUseCase,
    private val marketPreferences: MarketPreferences
) {

    suspend operator fun invoke(): Resource<List<NetworkProduct>> {
        val lastDate = marketPreferences.lastNewestProductDate
            .catch { emit(null) }
            .lastOrNull()

        val result = if (lastDate != null) {
            fetchProductsInDateRangeUseCase(lastDate to null)
        } else {
            /* It's first request, so save last date in data store */
            val firstAttempt = fetchProductsInDateRangeUseCase(null to null)
            firstAttempt.data?.lastOrNull()?.let { lastProduct ->
                marketPreferences.saveLastProductDate(lastProduct.createdDate)
            }
            Resource.success(emptyList())
        }

        if (result.isSuccess) {
            val newLastDate = result.data!!.firstOrNull()?.createdDate
            if (newLastDate != null) {
                marketPreferences.saveLastProductDate(newLastDate)
            }
        }

        Timber.d("newest product result is: $result")

        return result
    }

}