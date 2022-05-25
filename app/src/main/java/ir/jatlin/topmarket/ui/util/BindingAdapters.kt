package ir.jatlin.topmarket.ui.util

import android.widget.TextView
import androidx.core.text.HtmlCompat
import androidx.core.text.HtmlCompat.FROM_HTML_MODE_COMPACT
import androidx.databinding.BindingAdapter
import ir.jatlin.topmarket.core.network.model.product.category.NetworkCategory


@BindingAdapter("categories", "delimiter", requireAll = false)
fun TextView.setCategoriesNames(categories: List<NetworkCategory>?, delimiter: String?) {
    if (categories.isNullOrEmpty()) return
    val names = categories.map(NetworkCategory::name)
    text = names.joinToString(separator = delimiter ?: "/")

}

@BindingAdapter("htmlText")
fun TextView.setTextFromHtml(htmlText: String) {
    text = HtmlCompat.fromHtml(htmlText, FROM_HTML_MODE_COMPACT)
}

@BindingAdapter("price", "separator", requireAll = false)
fun TextView.setTextWithSeparator(price: String, separator: String = ",") {
    text = withSeparator(price, separator = separator)
}