package ir.jatlin.topmarket.core.data.source.remote

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import ir.jatlin.topmarket.core.data.di.IODispatcher
import ir.jatlin.topmarket.core.data.source.remote.product.ProductRemoteDataSource
import ir.jatlin.topmarket.core.network.model.product.NetworkProduct
import ir.jatlin.topmarket.core.network.model.product.NetworkProductDetails
import ir.jatlin.topmarket.core.network.model.product.category.NetworkCategoryDetails
import ir.jatlin.topmarket.util.ResourceProvider
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class FakeProductRemoteDataSource @Inject constructor(
    @IODispatcher private val dispatcher: CoroutineDispatcher,
    private val gson: Gson
) : ProductRemoteDataSource {

    private val dateFormatter =
        SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss", Locale.getDefault()).apply {
            timeZone = TimeZone.getTimeZone("GMT")
        }

    private fun <T, R : Comparable<R>> List<T>.orderBy(
        order: String,
        selector: (item: T) -> R?
    ): List<T> {
        return when (order) {
            "asc" -> sortedBy(selector)
            else -> sortedByDescending(selector)
        }
    }


    override suspend fun findProductDetailsById(id: Int): NetworkProductDetails? =
        withContext(dispatcher) {
            gson.fromJson(
                ResourceProvider.readFrom("get-product.json"),
                NetworkProductDetails::class.java
            )
        }

    override suspend fun getProductsList(
        page: Int,
        pageSize: Int?,
        filters: Map<String, String>?
    ): List<NetworkProduct> = withContext(dispatcher) {
        val result = gson.fromJson<List<NetworkProduct>?>(
            ResourceProvider.readFrom("get-product-list.json"),
            object : TypeToken<MutableList<NetworkProduct>>() {}.type
        )
        if (filters == null) return@withContext result
        val order = filters[ORDER] ?: "desc"


        when (filters[ORDER_BY]) {
            DATE -> result.orderBy(order) { dateFormatter.parse(it.createdDate) }
            POPULARITY -> result.orderBy(order) { it.totalSales }
            TOP_RATED -> result.orderBy(order) { it.totalSales }
            else -> result
        }
    }

    override suspend fun getProductCategories(
        page: Int,
        pageSize: Int?,
        filters: Map<String, String>?
    ): List<NetworkCategoryDetails> = withContext(dispatcher) {
        gson.fromJson(
            ResourceProvider.readFrom("get-product-categories-list.json"),
            object : TypeToken<MutableList<NetworkCategoryDetails>>() {}.type
        )
    }


    companion object {
        private const val ORDER = "order"
        private const val ORDER_BY = "orderby"
        private const val DATE = "date"
        private const val POPULARITY = "popularity"
        private const val TOP_RATED = "rating"
    }
}