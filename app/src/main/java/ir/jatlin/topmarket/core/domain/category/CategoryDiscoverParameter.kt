package ir.jatlin.topmarket.core.domain.category

import ir.jatlin.topmarket.core.domain.param.DefaultDiscoverParameters
import ir.jatlin.topmarket.core.domain.param.DiscoverParameters

class CategoryDiscoverParameter : DefaultDiscoverParameters() {

    override var order: DiscoverParameters.Order = DiscoverParameters.Order.Asc

    enum class OrderBY {
        Id, Include, Name, Slug, Description, Count
    }

    var parentId: Int? = null

    var productId: Int? = null

    var hideEmpties = true

    var includeIds: List<Int>? = null

}