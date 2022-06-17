package ir.jatlin.topmarket.ui.util

import android.text.method.LinkMovementMethod
import android.view.View
import android.widget.TextView
import androidx.core.text.HtmlCompat
import androidx.core.text.HtmlCompat.FROM_HTML_MODE_COMPACT
import androidx.databinding.BindingAdapter
import ir.jatlin.core.model.category.Category


@BindingAdapter(
    value = ["categories", "delimiter"], requireAll = false
)
fun TextView.setCategoriesNames(categories: List<Category>?, delimiter: String?) {
    if (categories.isNullOrEmpty()) return
    val names = categories.map(Category::name)
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
    text = price?.withSeparator(separator = separator ?: ",") ?: ""
}

fun String.withSeparator(
    separator: String = ","
) = buildString {
    val origin = this@withSeparator
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