package ir.jatlin.topmarket.core.domain.product

import ir.jatlin.topmarket.core.domain.param.DefaultDiscoverParameters

class ProductDiscoverParameters : DefaultDiscoverParameters() {

    enum class OrderBY {
        Date, Id, Include, Title, Slug, Price, Popularity, Rating
    }

    enum class StockStatus {
        InStock, OutOfStock, onBackOrder
    }

    var orderBy: OrderBY = OrderBY.Date

    var categoryId: Int? = null

    var tagId: Int? = null

    var stockStatus: StockStatus? = null

    var includeIds: List<Int>? = null

    var after: String? = null

    var before: String? = null

}