package ir.jatlin.topmarket.core.domain.util

import ir.jatlin.topmarket.core.domain.category.CategoryDiscoverParameter
import ir.jatlin.topmarket.core.domain.category.FetchCategoryDetailsListUseCase
import ir.jatlin.topmarket.core.domain.product.FetchProductsListUseCase
import ir.jatlin.topmarket.core.domain.product.ProductDiscoverParameters


inline fun makeProductParams(
    build: ProductDiscoverParameters.() -> Unit = {}
): FetchProductsListUseCase.Parameters {
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

        FetchProductsListUseCase.Parameters(
            page = page,
            pageSize = pageSize,
            filters = filters
        )
    }
}


inline fun makeCategoryParams(
    build: CategoryDiscoverParameter.() -> Unit = {}
): FetchCategoryDetailsListUseCase.Parameters {
    return CategoryDiscoverParameter().apply(build).run {
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

        FetchCategoryDetailsListUseCase.Parameters(
            page = page,
            pageSize = pageSize,
            filters = filters
        )
    }
}