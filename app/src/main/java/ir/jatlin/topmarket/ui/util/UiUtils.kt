package ir.jatlin.topmarket.ui.util

import android.content.Context
import android.graphics.Rect
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.core.graphics.Insets
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
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
import ir.jatlin.topmarket.databinding.FadingSnackbarLayoutBinding
import ir.jatlin.topmarket.ui.widget.FadingSnackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import timber.log.Timber
import java.lang.NullPointerException

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

suspend fun <T> Fragment.collectOnSuccess(
    flow: Flow<Resource<T>>,
    onSuccess: ((data: T) -> Unit) = {},
) = flow.safeCollect(
    onSuccess = onSuccess,
    onFailure = { showErrorMessage(it) }
)


suspend fun <T> Flow<Resource<T>>.safeCollect(
    onLoading: suspend ((data: T?) -> Unit) = {},
    onFailure: suspend (cause: ErrorCause?) -> Unit = {},
    onSuccess: suspend ((data: T) -> Unit)
) {
    collect { state ->
        when (state) {
            is Resource.Error -> onFailure(state.cause)
            is Resource.Loading -> onLoading(state.data)
            is Resource.Success -> {
                val data = state.data
                if (data != null) {
                    onSuccess(data)
                } else onFailure(ErrorCause.Unknown(NullPointerException()))
            }
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


fun View.clearFocusOnTouchEvent(target: View) {
    setOnTouchListener { v, event ->
        if (event.action == MotionEvent.ACTION_DOWN &&
            target.isFocused
        ) {
            val rect = Rect()
            target.getGlobalVisibleRect(rect)
            if (!rect.contains(event.rawX.toInt(), event.rawY.toInt())) {
                target.clearFocus()
                target.hideKeyboard()
            }
        }
        v.performClick()
    }
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
            beforeDiscount.length > 10 -> Timber.e("Integer parsing overflow for input: $beforeDiscount")
            afterDiscount.length > 10 -> Timber.e("Integer parsing overflow for input: $beforeDiscount")
            else -> Timber.e("Unresolved number format for values: $beforeDiscount, $afterDiscount")
        }
        null
    }
}


fun View.showKeyboard() {
    val imm = context.getSystemService(
        Context.INPUT_METHOD_SERVICE
    ) as InputMethodManager
    imm.showSoftInput(this, InputMethodManager.SHOW_FORCED)
}

fun View.hideKeyboard() {
    val imm = context.getSystemService(
        Context.INPUT_METHOD_SERVICE
    ) as InputMethodManager
    imm.hideSoftInputFromWindow(windowToken, 0)
}

/**
 * Set up a listener to apply window insets. The lambda also receives this View's initial padding
 * and margin values, to aid in properly updating the view based on the insets.
 */
fun View.doOnApplyWindowInsets(
    f: (v: View, insets: WindowInsetsCompat, padding: Insets, margins: Insets) -> WindowInsetsCompat
) {
    // Create a snapshot of the view's padding and margins.
    val padding = recordPadding()
    val margins = recordMargins()
    // Set a listener which proxies to the given lambda, also passing in the recorded state.
    ViewCompat.setOnApplyWindowInsetsListener(this) { view, insets ->
        f(view, insets, padding, margins)
    }

    requestApplyInsetsWhenAttached()
}

fun View.recordPadding() = Insets.of(paddingLeft, paddingTop, paddingRight, paddingBottom)

fun View.recordMargins(): Insets {
    val lp = layoutParams as? ViewGroup.MarginLayoutParams ?: return Insets.NONE
    return Insets.of(lp.leftMargin, lp.topMargin, lp.rightMargin, lp.bottomMargin)
}

/**
 * Convenience to request window insets, or ensure that the request is made when attached to the
 * view hierarchy.
 */
fun View.requestApplyInsetsWhenAttached() {
    if (isAttachedToWindow) {
        // We're already attached, just request as normal.
        requestApplyInsets()
    } else {
        // Add a listener to request when we are attached to the hierarchy.
        addOnAttachStateChangeListener(object : View.OnAttachStateChangeListener {
            override fun onViewAttachedToWindow(v: View) {
                v.removeOnAttachStateChangeListener(this)
                v.requestApplyInsets()
            }

            override fun onViewDetachedFromWindow(v: View) = Unit
        })
    }
}
