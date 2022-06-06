package ir.jatlin.topmarket.core.data.repository

import ir.jatlin.topmarket.core.data.source.remote.product.ProductRemoteDataSource
import ir.jatlin.topmarket.core.network.model.product.NetworkProduct
import ir.jatlin.topmarket.core.network.model.product.NetworkProductDetails
import ir.jatlin.topmarket.core.network.model.product.category.NetworkCategoryDetails
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class DefaultProductRepository @Inject constructor(
    private val remoteDataSource: ProductRemoteDataSource
) : ProductRepository {

    override suspend fun findProductDetailsById(id: Int): NetworkProductDetails? {
        return remoteDataSource.findProductDetailsById(id)
    }

    override fun getProductsList(
        page: Int,
        pageSize: Int?,
        filters: Map<String, String>?
    ): Flow<List<NetworkProduct>> {
        return flow {
            emit(remoteDataSource.getProductsList(page, pageSize, filters))
        }
    }

    override fun getProductCategories(
        page: Int,
        pageSize: Int?,
        filters: Map<String, String>?
    ): Flow<List<NetworkCategoryDetails>> {
        return flow {
            emit(remoteDataSource.getProductCategories(page, pageSize, filters))
        }
    }

}