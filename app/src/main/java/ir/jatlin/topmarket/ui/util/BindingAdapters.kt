package ir.jatlin.topmarket.ui.util

import android.text.method.LinkMovementMethod
import android.text.util.Linkify
import android.view.View
import android.widget.TextView
import androidx.core.text.HtmlCompat
import androidx.core.text.HtmlCompat.FROM_HTML_MODE_COMPACT
import androidx.core.text.HtmlCompat.FROM_HTML_MODE_LEGACY
import androidx.databinding.BindingAdapter
import ir.jatlin.topmarket.core.network.model.product.category.NetworkCategory


@BindingAdapter(
    value = ["categories", "delimiter"], requireAll = false
)
fun TextView.setCategoriesNames(categories: List<NetworkCategory>?, delimiter: String?) {
    if (categories.isNullOrEmpty()) return
    val names = categories.map(NetworkCategory::name)
    text = names.joinToString(separator = delimiter ?: "/")

}

@BindingAdapter("htmlLinkText")
fun TextView.setTextFromHtmlLink(htmlLink: String?) {
    setTextFromHtml(htmlLink)
    movementMethod = LinkMovementMethod.getInstance()
}

@BindingAdapter("htmlText")
fun TextView.setTextFromHtml(htmlText: String?) {
    text = HtmlCompat.fromHtml(htmlText ?: "", FROM_HTML_MODE_COMPACT)
}

@BindingAdapter("goneWhile")
fun View.goneWhile(isGone: Boolean) {
    visibility = if (isGone) View.GONE else View.VISIBLE
}

@BindingAdapter("invisibleWhile")
fun View.invisilbeWhile(isInvisible: Boolean) {
    visibility = if (isInvisible) View.INVISIBLE else View.VISIBLE
}

@BindingAdapter(
    value = ["price", "separator"], requireAll = false
)
fun TextView.setTextWithSeparator(price: String?, separator: String?) {
    text = withSeparator(price ?: return, separator = separator ?: ",")
}

fun withSeparator(
    origin: String,
    separator: String = ","
) = buildString {
    var i = origin.lastIndex
    while (i > 2) {
        append(origin[i--])
        append(origin[i--])
        append(origin[i--])
        append(separator)
    }
    while (i >= 0) append(origin[i--])

    reverse()
}