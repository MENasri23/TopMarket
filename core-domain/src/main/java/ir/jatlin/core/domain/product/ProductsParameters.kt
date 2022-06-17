package ir.jatlin.core.domain.product

data class ProductsParameters(
    val page: Int,
    val pageSize: Int,
    val filters: Map<String, String>?
)