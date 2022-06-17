package ir.jatlin.topmarket.core.data.repository

import ir.jatlin.core.model.category.CategoryDetails
import ir.jatlin.core.model.product.Product
import ir.jatlin.core.model.product.ProductDetails
import ir.jatlin.core.model.product.ProductReview
import ir.jatlin.topmarket.core.data.mapper.asCategoryDetails
import ir.jatlin.topmarket.core.data.mapper.asProduct
import ir.jatlin.topmarket.core.data.mapper.asProductDetails
import ir.jatlin.topmarket.core.data.mapper.asProductPreview
import ir.jatlin.topmarket.core.data.source.remote.product.ProductRemoteDataSource
import ir.jatlin.topmarket.core.network.model.product.NetworkProduct
import ir.jatlin.topmarket.core.network.model.product.category.NetworkCategoryDetails
import ir.jatlin.topmarket.core.network.model.product.review.ProductReviewNetwork
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class DefaultProductRepository @Inject constructor(
    private val remoteDataSource: ProductRemoteDataSource
) : ProductRepository {

    override suspend fun findProductDetailsById(id: Int): ProductDetails {
        return remoteDataSource.findProductDetailsById(id).asProductDetails()
    }

    override fun getProductsListStream(
        page: Int,
        pageSize: Int?,
        filters: Map<String, String>?
    ): Flow<List<Product>> {
        return flow {
            emit(
                remoteDataSource.getProductsList(page, pageSize, filters)
                    .map(NetworkProduct::asProduct)
            )
        }
    }

    override suspend fun getProductsList(
        page: Int,
        pageSize: Int?,
        filters: Map<String, String>?
    ): List<Product> {
        return remoteDataSource.getProductsList(page, pageSize, filters)
            .map(NetworkProduct::asProduct)
    }

    override fun getProductCategories(
        page: Int,
        pageSize: Int?,
        filters: Map<String, String>?
    ): Flow<List<CategoryDetails>> {
        return flow {
            emit(
                remoteDataSource.getProductCategories(page, pageSize, filters)
                    .map(NetworkCategoryDetails::asCategoryDetails)
            )
        }
    }

    override suspend fun getProductReviews(filters: Map<String, String>?): List<ProductReview> {
        return remoteDataSource.getProductReviews(filters)
            .map(ProductReviewNetwork::asProductPreview)
    }
}