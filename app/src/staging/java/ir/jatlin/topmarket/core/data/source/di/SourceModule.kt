package ir.jatlin.topmarket.core.data.source.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ir.jatlin.topmarket.core.data.source.local.customer.CustomerDatabaseDataSource
import ir.jatlin.topmarket.core.data.source.local.customer.CustomerLocalDataSource
import ir.jatlin.topmarket.core.data.source.remote.coupon.CouponRemoteDataSource
import ir.jatlin.topmarket.core.data.source.remote.coupon.CouponRetrofitDataSource
import ir.jatlin.topmarket.core.data.source.remote.customer.CustomerRemoteDataSource
import ir.jatlin.topmarket.core.data.source.remote.customer.CustomerRetrofitDataSource
import ir.jatlin.topmarket.core.data.source.remote.order.OrderRemoteDataSource
import ir.jatlin.topmarket.core.data.source.remote.order.OrderRetrofitDataSource
import ir.jatlin.topmarket.core.data.source.remote.product.ProductRemoteDataSource
import ir.jatlin.topmarket.core.data.source.remote.product.ProductRetrofitDataSource

@Module
@InstallIn(SingletonComponent::class)
interface SourceModule {

    @Binds
    fun bindsProductRemoteDataSource(
        remoteDataSource: ProductRetrofitDataSource
    ): ProductRemoteDataSource

    @Binds
    fun bindsCustomerRemoteDataSource(
        remoteDataSource: CustomerRetrofitDataSource
    ): CustomerRemoteDataSource

    @Binds
    fun bindsOrderRemoteDataSource(
        remoteDataSource: OrderRetrofitDataSource
    ): OrderRemoteDataSource

    @Binds
    fun bindsCustomerLocalDataSource(
        localDataSource: CustomerDatabaseDataSource
    ): CustomerLocalDataSource

    @Binds
    fun bindsCouponRemoteDataSource(
        remoteDataSource: CouponRetrofitDataSource
    ): CouponRemoteDataSource
}