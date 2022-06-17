package ir.jatlin.core.domain.param

interface DiscoverParameters {

    companion object {
        const val PAGE_SIZE_INFINITE = 100
    }


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