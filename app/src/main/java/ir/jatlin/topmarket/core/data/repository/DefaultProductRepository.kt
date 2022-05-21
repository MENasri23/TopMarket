package ir.jatlin.topmarket.core.data.repository

import ir.jatlin.topmarket.core.data.source.remote.ProductRemoteDataSource
import ir.jatlin.topmarket.core.network.model.product.NetworkProduct
import ir.jatlin.topmarket.core.network.model.product.NetworkProductDetails
import ir.jatlin.topmarket.core.network.model.product.category.NetworkCategoryDetails
import javax.inject.Inject

class DefaultProductRepository @Inject constructor(
    private val remoteDataSource: ProductRemoteDataSource
) : ProductRepository {

    override suspend fun findProductDetailsById(id: Int): NetworkProductDetails {
        return remoteDataSource.findProductDetailsById(id)
    }

    override suspend fun getProductsList(
        page: Int,
        pageSize: Int?,
        filters: Map<String, String>?
    ): List<NetworkProduct> {
        return remoteDataSource.getProductsList(page, pageSize, filters)
    }

    override suspend fun getProductCategories(
        page: Int,
        pageSize: Int?,
        filters: Map<String, String>?
    ): List<NetworkCategoryDetails> {
        return remoteDataSource.getProductCategories(page, pageSize, filters)
    }

}