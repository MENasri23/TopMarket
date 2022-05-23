package ir.jatlin.topmarket.core.data.repository

import ir.jatlin.topmarket.core.network.model.product.NetworkProduct
import ir.jatlin.topmarket.core.network.model.product.NetworkProductDetails
import ir.jatlin.topmarket.core.network.model.product.category.NetworkCategoryDetails
import kotlinx.coroutines.flow.Flow

interface ProductRepository {
    // TODO: Change return types of its method to external model
    fun findProductDetailsById(
        id: Int
    ): Flow<NetworkProductDetails>

    fun getProductsList(
        page: Int,
        pageSize: Int?,
        filters: Map<String, String>?
    ): Flow<List<NetworkProduct>>

    fun getProductCategories(
        page: Int,
        pageSize: Int?,
        filters: Map<String, String>?
    ): Flow<List<NetworkCategoryDetails>>

}