package ir.jatlin.core.data.source.remote.product

import ir.jatlin.core.network.model.product.NetworkProduct
import ir.jatlin.core.network.model.product.NetworkProductDetails
import ir.jatlin.core.network.model.product.category.NetworkCategoryDetails
import ir.jatlin.core.network.model.product.review.ProductReviewNetwork

interface ProductRemoteDataSource {

    suspend fun findProductDetailsById(
        id: Int
    ): NetworkProductDetails

    suspend fun getProductsList(
        page: Int,
        pageSize: Int?,
        filters: Map<String, String>?
    ): List<NetworkProduct>

    suspend fun getProductCategories(
        page: Int,
        pageSize: Int?,
        filters: Map<String, String>?
    ): List<NetworkCategoryDetails>

    suspend fun getProductReviews(
        filters: Map<String, String>?
    ): List<ProductReviewNetwork>

}