package ir.jatlin.core.data.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ir.jatlin.core.data.repository.DefaultProductRepository
import ir.jatlin.core.data.repository.ProductRepository
import ir.jatlin.core.data.repository.customer.CustomerRepository
import ir.jatlin.core.data.repository.customer.DefaultCustomerRepository
import ir.jatlin.core.data.repository.order.DefaultOrderRepository
import ir.jatlin.core.data.repository.order.OrderRepository
import ir.jatlin.core.data.repository.orderlineitem.DefaultOrderLineItemRepository
import ir.jatlin.core.data.repository.orderlineitem.OrderLineItemRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface DataModule {

    @Binds
    @Singleton
    fun bindsProductRepository(
        productRepository: DefaultProductRepository
    ): ProductRepository

    @Binds
    @Singleton
    fun bindsCustomerRepository(
        customerRepository: DefaultCustomerRepository
    ): CustomerRepository

    @Binds
    @Singleton
    fun bindsOrderRepository(
        orderRepository: DefaultOrderRepository
    ): OrderRepository

    @Binds
    @Singleton
    fun bindsOrderLineItemRepository(
        orderLineItemRepository: DefaultOrderLineItemRepository
    ): OrderLineItemRepository
}