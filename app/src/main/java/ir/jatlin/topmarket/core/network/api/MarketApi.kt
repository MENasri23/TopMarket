package ir.jatlin.topmarket.core.network.api

import ir.jatlin.topmarket.core.network.model.costumer.CustomerNetwork
import ir.jatlin.topmarket.core.network.model.product.NetworkProduct
import ir.jatlin.topmarket.core.network.model.product.NetworkProductDetails
import ir.jatlin.topmarket.core.network.model.product.category.NetworkCategoryDetails
import retrofit2.Response
import retrofit2.http.*

interface MarketApi {

    @GET(Route.PRODUCT_DETAILS)
    suspend fun getProductDetails(
        @Path(PARAMS.PRODUCT_ID) id: Int
    ): Response<NetworkProductDetails>

    @GET(Route.PRODUCTS)
    suspend fun getProductsList(
        @Query(PARAMS.PAGE) page: Int,
        @Query(PARAMS.PER_PAGE) pageSize: Int?,
        @QueryMap filters: Map<String, String>?
    ): Response<List<NetworkProduct>>

    @GET(Route.PRODUCTS_CATEGORIES)
    suspend fun getProductCategories(
        @Query(PARAMS.PAGE) page: Int,
        @Query(PARAMS.PER_PAGE) pageSize: Int?,
        @QueryMap filters: Map<String, String>?
    ): Response<List<NetworkCategoryDetails>>


    @POST(Route.CUSTOMER_NEW)
    suspend fun createCustomer(
        @Body customerNetwork: CustomerNetwork
    ): Response<CustomerNetwork>

    @GET(Route.COSTUMER)
    suspend fun getCustomer(
        @Path("id") id: Int
    ): Response<CustomerNetwork>

    @PUT(Route.COSTUMER)
    suspend fun updateCustomer(
        @Path("id") id: Int,
        @Body customer: CustomerNetwork
    )

    private object Route {
        private const val PREFIX = "/wp-json/wc/v3"
        const val PRODUCT_DETAILS = "$PREFIX/products/{id}"
        const val PRODUCTS = "$PREFIX/products"
        const val PRODUCTS_CATEGORIES = "$PREFIX/products/categories"

        /* customer */
        const val CUSTOMER_NEW = "$PREFIX/customers"
        const val COSTUMER = "$PREFIX/customers/{id}"
    }

    private object PARAMS {
        const val PRODUCT_ID = "id"
        const val PAGE = "page"
        const val PER_PAGE = "per_page"

    }


}