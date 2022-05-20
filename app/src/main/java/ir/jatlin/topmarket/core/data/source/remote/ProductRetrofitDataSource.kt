package ir.jatlin.topmarket.core.data.source.remote

import ir.jatlin.topmarket.core.network.api.MarketApi
import ir.jatlin.topmarket.core.network.model.product.NetworkProduct
import ir.jatlin.topmarket.core.network.model.product.NetworkProductDetails
import ir.jatlin.topmarket.core.network.model.product.category.NetworkCategoryDetails
import retrofit2.Response
import javax.inject.Inject

class ProductRetrofitDataSource @Inject constructor(
    private val marketApi: MarketApi
) : ProductRemoteDataSource {

    override suspend fun findMovieDetailsById(id: Int): Response<NetworkProductDetails> {
        return marketApi.getProductDetails(id)
    }

    override suspend fun getProductsList(
        page: Int,
        pageSize: Int?,
        filters: Map<String, String>?
    ): Response<List<NetworkProduct>> {
        return marketApi.getProductsList(page, pageSize, filters)
    }

    override suspend fun getProductCategories(
        page: Int,
        pageSize: Int?,
        filters: Map<String, String>?
    ): Response<List<NetworkCategoryDetails>> {
        return marketApi.getProductCategories(page, pageSize, filters)
    }
}