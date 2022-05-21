package ir.jatlin.topmarket.core.data.source.remote

import ir.jatlin.data.source.remote.ResponseConverter
import ir.jatlin.topmarket.core.network.api.MarketApi
import ir.jatlin.topmarket.core.network.model.product.NetworkProduct
import ir.jatlin.topmarket.core.network.model.product.NetworkProductDetails
import ir.jatlin.topmarket.core.network.model.product.category.NetworkCategoryDetails
import javax.inject.Inject

class ProductRetrofitDataSource @Inject constructor(
    private val marketApi: MarketApi,
    private val convertResponse: ResponseConverter
) : ProductRemoteDataSource {

    override suspend fun findMovieDetailsById(id: Int): NetworkProductDetails {
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
}