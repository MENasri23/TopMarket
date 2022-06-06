package ir.jatlin.topmarket.core.data.repository

import ir.jatlin.topmarket.core.network.model.product.NetworkProduct
import ir.jatlin.topmarket.core.network.model.product.NetworkProductDetails
import ir.jatlin.topmarket.core.network.model.product.category.NetworkCategoryDetails
import kotlinx.coroutines.flow.Flow

interface ProductRepository {
    // TODO: Change return types of its method to external model
    suspend fun findProductDetailsById(
        id: Int
    ): NetworkProductDetails?

    fun getProductsListStream(
        page: Int,
        pageSize: Int?,
        filters: Map<String, String>?
    ): Flow<List<NetworkProduct>>

    suspend fun getProductsList(
        page: Int,
        pageSize: Int?,
        filters: Map<String, String>?
    ): List<NetworkProduct>

    fun getProductCategories(
        page: Int,
        pageSize: Int?,
        filters: Map<String, String>?
    ): Flow<List<NetworkCategoryDetails>>

}