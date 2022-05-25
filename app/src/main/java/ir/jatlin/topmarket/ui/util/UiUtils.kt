package ir.jatlin.topmarket.ui.util

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.core.view.isVisible
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.snackbar.Snackbar
import ir.jatlin.topmarket.R
import ir.jatlin.topmarket.core.shared.Resource
import ir.jatlin.topmarket.core.shared.fail.ErrorCause
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import timber.log.Timber

/**
 * Launches a new coroutine and repeats `block` every time the Fragment's viewLifecycleOwner
 * is in and out of `minActiveState` lifecycle state.
 */
inline fun Fragment.repeatOnViewLifecycleOwner(
    minActiveState: Lifecycle.State = Lifecycle.State.STARTED,
    crossinline block: suspend CoroutineScope.() -> Unit
) {
    viewLifecycleOwner.lifecycleScope.launch {
        viewLifecycleOwner.lifecycle.repeatOnLifecycle(minActiveState) {
            block()
        }
    }
}

suspend inline fun <T> Fragment.collectOnSuccess(
    flow: Flow<Resource<T>>,
    crossinline onSuccess: ((data: T) -> Unit) = {},
) = flow.safeCollect(
    onSuccess = onSuccess,
    onFailure = { showErrorMessage(it) }
)


suspend inline fun <T> Flow<Resource<T>>.safeCollect(
    crossinline onLoading: ((data: T?) -> Unit) = {},
    crossinline onFailure: (cause: ErrorCause?) -> Unit = {},
    crossinline onSuccess: ((data: T) -> Unit) = {}
) {
    collect { state ->
        when (state) {
            is Resource.Error -> onFailure(state.cause)
            is Resource.Loading -> onLoading(state.data)
            is Resource.Success -> onSuccess(state.data!!)
        }
    }
}

fun Fragment.showErrorMessage(
    cause: ErrorCause?,
    @StringRes actionLabel: Int? = null,
    onActionClick: () -> Unit = {}
) {
    @StringRes val message = getErrorMessage(cause ?: return)
    Snackbar
        .make(requireView(), message, Snackbar.LENGTH_LONG).apply {
            if (actionLabel != null) {
                setAction(actionLabel) { onActionClick() }
            }
        }.show()
}

private fun getErrorMessage(cause: ErrorCause): Int {
    return when (cause) {
        ErrorCause.BadRequest -> R.string.error_bad_request
        ErrorCause.Forbidden -> R.string.error_forbidden
        ErrorCause.NoConnection -> R.string.error_no_connection
        ErrorCause.NoContent -> R.string.error_empty_response
        ErrorCause.NotFound -> R.string.error_not_found
        ErrorCause.RequestTimeout -> R.string.error_timeout
        else -> R.string.error_unknown
    }
}

inline fun <T : ViewDataBinding> T.executePending(block: T.() -> Unit) {
    block()
    executePendingBindings()
}


fun ImageView.loadFromUrl(
    url: String?,
    @DrawableRes placeholder: Int = R.drawable.loading_animation,

    ) {
    Glide.with(context)
        .load(url)
        .apply(
            RequestOptions()
                .placeholder(placeholder)
                .error(R.drawable.ic_broken_image)
        )
        .transition(DrawableTransitionOptions.withCrossFade())
        .into(this)
}

fun View.clipToRoundRect(clip: Boolean) {
    clipToOutline = clip
    outlineProvider = if (clip) RoundedOutlineProvided else null
}

fun View.invisible() {
    visibility = View.INVISIBLE
}

fun View.visible() {
    isVisible = true
}

fun View.gone() {
    isVisible = false
}


fun TextView.setDiscount(
    beforeDiscount: String,
    afterDiscount: String
) {
    val discount = percentDiscount(beforeDiscount, afterDiscount)
    if (discount != null) {
        text = context.getString(
            R.string.price_discount, discount
        )
        visible()
    } else invisible()

}

private fun percentDiscount(
    beforeDiscount: String, afterDiscount: String
): Int? {
    return try {

        val before = beforeDiscount.toInt()
        val after = afterDiscount.toInt()

        Timber.d("regularPrice: $before, price: $after")

        if (before <= after) null
        else ((before - after) * 100) / before

    } catch (e: NumberFormatException) {
        when {
            beforeDiscount.length > 10 -> Timber.d("Integer parsing overflow for input: $beforeDiscount")
            afterDiscount.length > 10 -> Timber.d("Integer parsing overflow for input: $beforeDiscount")
            else -> Timber.d("Unresolved number format for values: $beforeDiscount, $afterDiscount")
        }
        null
    }
}