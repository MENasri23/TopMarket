package ir.jatlin.topmarket.core.shared

import ir.jatlin.topmarket.core.shared.fail.ErrorCause

/**
 * A generic class that holds a value with its loading status.
 * @param <T>
 */
sealed class Resource<out R>(
    val data: R? = null,
    val cause: ErrorCause? = null
) {

    class Success<out T>(data: T) : Resource<T>(data)
    class Loading<out T>(data: T? = null) : Resource<T>(data)
    class Error(cause: ErrorCause) : Resource<Nothing>(cause = cause)

    override fun toString(): String {
        return when (this) {
            is Success -> "Success[data=$data]"
            is Error -> "Error[exception=$cause]"
            is Loading -> "Loading"
        }
    }

    companion object {
        fun <T> success(data: T): Resource<T> = Success(data)
        fun <T> loading(data: T? = null): Resource<T> = Loading(data)
        fun <T> error(cause: ErrorCause): Resource<T> = Error(cause)
    }

}

val <T> Resource<T>.isSuccess
    get() = this is Resource.Success && data != null

fun <T> Resource<T>.dataOnSuccessOr(default: T): T =
    (this as? Resource.Success<T>)?.data ?: default