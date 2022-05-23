package ir.jatlin.topmarket.core.data.source.remote

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import ir.jatlin.topmarket.core.data.di.IODispatcher
import ir.jatlin.topmarket.core.network.model.product.NetworkProduct
import ir.jatlin.topmarket.core.network.model.product.NetworkProductDetails
import ir.jatlin.topmarket.core.network.model.product.category.NetworkCategoryDetails
import ir.jatlin.topmarket.util.ResourceProvider
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class FakeProductRemoteDataSource @Inject constructor(
    @IODispatcher private val dispatcher: CoroutineDispatcher,
    private val gson: Gson
) : ProductRemoteDataSource {

    override suspend fun findProductDetailsById(id: Int): NetworkProductDetails =
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
        gson.fromJson(
            ResourceProvider.readFrom("get-product-list.json"),
            object : TypeToken<MutableList<NetworkProduct>>() {}.type
        )
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
}