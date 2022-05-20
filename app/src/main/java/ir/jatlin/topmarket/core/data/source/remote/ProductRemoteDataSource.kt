package ir.jatlin.topmarket.core.data.source.remote

import ir.jatlin.topmarket.core.network.model.product.NetworkProduct
import ir.jatlin.topmarket.core.network.model.product.NetworkProductDetails
import ir.jatlin.topmarket.core.network.model.product.category.NetworkCategoryDetails
import retrofit2.Response

interface ProductRemoteDataSource {

    suspend fun findMovieDetailsById(
        id: Int
    ): Response<NetworkProductDetails>

    suspend fun getProductsList(
        page: Int,
        pageSize: Int?,
        filters: Map<String, String>?
    ): Response<List<NetworkProduct>>

    suspend fun getProductCategories(
        page: Int,
        pageSize: Int?,
        filters: Map<String, String>?
    ): Response<List<NetworkCategoryDetails>>

}