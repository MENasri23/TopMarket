package ir.jatlin.topmarket.core.domain.product

import ir.jatlin.topmarket.core.domain.param.DefaultDiscoverParameters

class ProductDiscoverParameters : DefaultDiscoverParameters() {

    enum class Order {
        Asc, Desc
    }

    enum class OrderBY {
        Date, Id, Include, Title, Slug, Price, Popularity, Rating
    }

    enum class StockStatus {
        InStock, OutOfStock, onBackOrder
    }

    var order: Order = Order.Desc

    var orderBy: OrderBY = OrderBY.Date

    var categoryId: Int? = null

    var tagId: Int? = null

    var stockStatus: StockStatus? = null

    var includeIds: List<Int>? = null


    companion object {
        const val PAGE_SIZE_INFINITE = 100
    }

}