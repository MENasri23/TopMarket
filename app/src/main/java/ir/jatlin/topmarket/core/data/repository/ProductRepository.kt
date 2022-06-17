package ir.jatlin.topmarket.core.data.repository

import ir.jatlin.core.model.category.CategoryDetails
import ir.jatlin.core.model.product.Product
import ir.jatlin.core.model.product.ProductDetails
import ir.jatlin.core.model.product.ProductReview
import kotlinx.coroutines.flow.Flow

interface ProductRepository {
    // TODO: Change return types of its method to external model
    suspend fun findProductDetailsById(
        id: Int
    ): ProductDetails

    fun getProductsListStream(
        page: Int,
        pageSize: Int?,
        filters: Map<String, String>?
    ): Flow<List<Product>>

    suspend fun getProductsList(
        page: Int,
        pageSize: Int?,
        filters: Map<String, String>?
    ): List<Product>

    fun getProductCategories(
        page: Int,
        pageSize: Int?,
        filters: Map<String, String>?
    ): Flow<List<CategoryDetails>>

    suspend fun getProductReviews(
        filters: Map<String, String>?
    ): List<ProductReview>

}