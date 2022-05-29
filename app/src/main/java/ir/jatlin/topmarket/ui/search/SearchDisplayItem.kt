package ir.jatlin.topmarket.ui.search

sealed interface SearchDisplayItem {
    val id: Int

    data class HeaderItem(
        val categoryId: Int,
        val searchProducts: List<SearchProductItem>
    ) : SearchDisplayItem {
        override val id: Int
            get() = categoryId
    }

    data class BodyItem(
        val categoryId: Int,
        val categoryName: String,
        val productId: Int,
        val productName: String
    ) : SearchDisplayItem {

        override val id: Int
            get() = productId
    }

}

