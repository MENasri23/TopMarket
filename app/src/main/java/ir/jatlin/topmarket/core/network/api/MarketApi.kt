package ir.jatlin.topmarket.core.network.api

import ir.jatlin.topmarket.core.network.model.costumer.CustomerNetwork
import ir.jatlin.topmarket.core.network.model.order.OrderNetwork
import ir.jatlin.topmarket.core.network.model.product.NetworkProduct
import ir.jatlin.topmarket.core.network.model.product.NetworkProductDetails
import ir.jatlin.topmarket.core.network.model.product.category.NetworkCategoryDetails
import ir.jatlin.topmarket.core.network.model.product.review.ProductReviewNetwork
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


    @GET(Route.CUSTOMERS_ID)
    suspend fun getCustomer(
        @Path("id") id: Int
    ): Response<CustomerNetwork>

    @GET(Route.CUSTOMERS)
    suspend fun getCustomerByEmail(
        @Query("email") email: String
    ): Response<List<CustomerNetwork>>

    @POST(Route.CUSTOMERS)
    suspend fun createCustomer(
        @Body customerNetwork: CustomerNetwork
    ): Response<CustomerNetwork>

    @PUT(Route.CUSTOMERS_ID)
    suspend fun updateCustomer(
        @Path("id") id: Int,
        @Body customer: CustomerNetwork
    ): Response<CustomerNetwork>

    @GET(Route.ORDERS_ID)
    suspend fun getOrder(
        @Path("id") id: Int
    ): Response<OrderNetwork>

    @POST(Route.ORDERS)
    suspend fun createOrder(
        @Body orderNetwork: OrderNetwork
    ): Response<OrderNetwork>

    @POST(Route.ORDERS)
    suspend fun createOrder(): Response<OrderNetwork>

    @PUT(Route.ORDERS_ID)
    suspend fun updateOrder(
        @Path("id") id: Int,
        @Body order: OrderNetwork
    ): Response<OrderNetwork>

    @GET(Route.REVIEWS)
    suspend fun getProductReviews(
        @QueryMap filters: Map<String, String>?
    ): Response<List<ProductReviewNetwork>>

    private object Route {
        private const val PREFIX = "/wp-json/wc/v3"
        const val PRODUCT_DETAILS = "$PREFIX/products/{id}"
        const val PRODUCTS = "$PREFIX/products"
        const val PRODUCTS_CATEGORIES = "$PREFIX/products/categories"

        /* customer */
        const val CUSTOMERS = "$PREFIX/customers"
        const val CUSTOMERS_ID = "$PREFIX/customers/{id}"

        /* Orders */
        const val ORDERS = "$PREFIX/orders"
        const val ORDERS_ID = "$PREFIX/orders/{id}"

        /* Reviews */
        const val REVIEWS = "$PREFIX/products/reviews"
    }

    private object PARAMS {
        const val PRODUCT_ID = "id"
        const val PAGE = "page"
        const val PER_PAGE = "per_page"

    }


}