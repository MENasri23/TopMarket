package ir.jatlin.topmarket.core.data.source.remote.product

import ir.jatlin.topmarket.core.network.model.product.NetworkProduct
import ir.jatlin.topmarket.core.network.model.product.NetworkProductDetails
import ir.jatlin.topmarket.core.network.model.product.category.NetworkCategoryDetails
import ir.jatlin.topmarket.core.network.model.product.review.ProductReviewNetwork

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