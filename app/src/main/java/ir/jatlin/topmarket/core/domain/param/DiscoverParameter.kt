package ir.jatlin.topmarket.core.domain.param

interface DiscoverParameters {

    var page: Int

    var pageSize: Int

    var searchQuery: String

    var sortBy: String
}

open class DefaultDiscoverParameters : DiscoverParameters {
    override var page: Int = 1
    override var pageSize: Int = 10
    override var searchQuery: String = ""
    override var sortBy: String = ""
}