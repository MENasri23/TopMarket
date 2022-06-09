package ir.jatlin.topmarket.core.network.api

import ir.jatlin.topmarket.core.network.model.costumer.CustomerNetwork
import ir.jatlin.topmarket.core.network.model.order.OrderNetwork
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


    @GET(Route.CUSTOMER_ID)
    suspend fun getCustomer(
        @Path("id") id: Int
    ): Response<CustomerNetwork>

    @GET(Route.CUSTOMER)
    suspend fun getCustomerByEmail(
        @Query("email") email: String
    ): Response<List<CustomerNetwork>>

    @POST(Route.CUSTOMER)
    suspend fun createCustomer(
        @Body customerNetwork: CustomerNetwork
    ): Response<CustomerNetwork>

    @PUT(Route.CUSTOMER_ID)
    suspend fun updateCustomer(
        @Path("id") id: Int,
        @Body customer: CustomerNetwork
    ): Response<CustomerNetwork>

    @GET(Route.ORDER_ID)
    suspend fun getOrder(
        @Path("id") id: Int
    ): Response<OrderNetwork>

    @POST(Route.ORDER)
    suspend fun createOrder(
        @Body orderNetwork: OrderNetwork
    ): Response<OrderNetwork>

    @POST(Route.ORDER)
    suspend fun createOrder(): Response<OrderNetwork>

    @PUT(Route.ORDER_ID)
    suspend fun updateOrder(
        @Path("id") id: Int,
        @Body order: OrderNetwork
    ): Response<OrderNetwork>

    private object Route {
        private const val PREFIX = "/wp-json/wc/v3"
        const val PRODUCT_DETAILS = "$PREFIX/products/{id}"
        const val PRODUCTS = "$PREFIX/products"
        const val PRODUCTS_CATEGORIES = "$PREFIX/products/categories"

        /* customer */
        const val CUSTOMER = "$PREFIX/customers"
        const val CUSTOMER_ID = "$PREFIX/customers/{id}"

        /* Orders */
        const val ORDER = "$PREFIX/orders"
        const val ORDER_ID = "$PREFIX/orders/{id}"
    }

    private object PARAMS {
        const val PRODUCT_ID = "id"
        const val PAGE = "page"
        const val PER_PAGE = "per_page"

    }


}