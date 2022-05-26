package ir.jatlin.topmarket.core.domain.param

interface DiscoverParameters {

    enum class Order {
        Asc, Desc
    }

    var page: Int

    var pageSize: Int

    var searchQuery: String

    var order: Order
}

open class DefaultDiscoverParameters : DiscoverParameters {
    override var page: Int = 1
    override var pageSize: Int = 10
    override var searchQuery: String = ""
    override var order: DiscoverParameters.Order = DiscoverParameters.Order.Desc
}