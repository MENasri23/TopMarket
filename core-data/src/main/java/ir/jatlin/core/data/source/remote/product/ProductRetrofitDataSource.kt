package ir.jatlin.core.data.source.remote.product

import ir.jatlin.core.data.source.remote.ResponseConverter
import ir.jatlin.core.network.api.MarketApi
import ir.jatlin.core.network.model.product.NetworkProduct
import ir.jatlin.core.network.model.product.NetworkProductDetails
import ir.jatlin.core.network.model.product.category.NetworkCategoryDetails
import ir.jatlin.core.network.model.product.review.ProductReviewNetwork
import javax.inject.Inject

class ProductRetrofitDataSource @Inject constructor(
    private val marketApi: MarketApi,
    private val convertResponse: ResponseConverter
) : ProductRemoteDataSource {

    override suspend fun findProductDetailsById(id: Int): NetworkProductDetails {
        return convertResponse(marketApi.getProductDetails(id))
    }

    override suspend fun getProductsList(
        page: Int,
        pageSize: Int?,
        filters: Map<String, String>?
    ): List<NetworkProduct> {
        return convertResponse(marketApi.getProductsList(page, pageSize, filters))
    }

    override suspend fun getProductCategories(
        page: Int,
        pageSize: Int?,
        filters: Map<String, String>?
    ): List<NetworkCategoryDetails> {
        return convertResponse(marketApi.getProductCategories(page, pageSize, filters))
    }

    override suspend fun getProductReviews(
        filters: Map<String, String>?
    ): List<ProductReviewNetwork> {
        return convertResponse(marketApi.getProductReviews(filters))
    }
}