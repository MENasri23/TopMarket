package ir.jatlin.core.domain.util

import ir.jatlin.core.domain.product.ProductDiscoverParameters
import ir.jatlin.core.domain.product.ProductsParameters


inline fun makeProductParams(
    build: ProductDiscoverParameters.() -> Unit = {}
): ProductsParameters {
    return ProductDiscoverParameters().apply(build).run {
        val filters = HashMap<String, String>().apply {
            put("order", order.name.lowercase())
            put("orderby", orderBy.name.lowercase())
            put("search", searchQuery)

            val category = categoryId
            if (category != null) put("category", category.toString())

            val tag = tagId
            if (tag != null) put("category", tag.toString())

            val stockStatus = stockStatus
            if (stockStatus != null) put("category", stockStatus.name.lowercase())

            val productIds = includeIds
            if (productIds != null) put("include", productIds.joinToString())

            val after = after
            if (after != null) put("after", after)

            val before = before
            if (before != null) put("before", before)
        }

        ProductsParameters(
            page = page,
            pageSize = pageSize,
            filters = filters
        )
    }
}


inline fun makeCategoryParams(
    build: ir.jatlin.core.domain.category.CategoryDiscoverParameter.() -> Unit = {}
): ir.jatlin.core.domain.category.FetchCategoryDetailsListUseCase.Parameters {
    return ir.jatlin.core.domain.category.CategoryDiscoverParameter().apply(build).run {
        val filters = HashMap<String, String>().apply {
            put("order", order.name.lowercase())
            put("orderby", orderBy.name.lowercase())

            val parent = parentId
            if (parent != null) put("parent", parent.toString())

            val product = productId
            if (product != null) put("product", product.toString())

            val hideEmpties = hideEmpties
            if (hideEmpties) put("hide_empty", hideEmpties.toString())

            val categoryIds = includeIds
            if (categoryIds != null) put("include", categoryIds.joinToString())
        }

        ir.jatlin.core.domain.category.FetchCategoryDetailsListUseCase.Parameters(
            page = page,
            pageSize = pageSize,
            filters = filters
        )
    }
}