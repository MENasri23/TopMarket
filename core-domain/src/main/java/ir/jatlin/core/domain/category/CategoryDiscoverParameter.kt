package ir.jatlin.core.domain.category

import ir.jatlin.core.domain.param.DefaultDiscoverParameters
import ir.jatlin.core.domain.param.DiscoverParameters
class CategoryDiscoverParameter : DefaultDiscoverParameters() {

    override var order: DiscoverParameters.Order = DiscoverParameters.Order.Asc

    enum class OrderBY {
        Id, Include, Name, Slug, Description, Count
    }

    var orderBy: OrderBY = OrderBY.Name

    var parentId: Int? = null

    var productId: Int? = null

    var hideEmpties = true

    var includeIds: List<Int>? = null

}