package ir.jatlin.core.domain.util

import ir.jatlin.core.data.source.remote.NetworkException
import ir.jatlin.core.data.source.remote.NoBodyException
import ir.jatlin.core.shared.fail.ErrorCause
import ir.jatlin.core.shared.fail.ErrorHandler
import java.io.IOException
import java.net.SocketTimeoutException
import javax.inject.Inject

typealias StatusCode = ErrorCause.StatusCode

class DefaultErrorHandler @Inject constructor() : ErrorHandler {

    override fun handle(cause: Throwable): ErrorCause = when (cause) {
        is NoBodyException -> ErrorCause.NoContent
        is NetworkException -> {
            val networkError = cause.error
            with(StatusCode) {
                when (val code = networkError.status.code) {
                    BAD_REQUEST -> ErrorCause.BadRequest
                    FORBIDDEN -> ErrorCause.Forbidden
                    NOT_FOUND -> ErrorCause.NotFound
                    REQUEST_TIMEOUT -> ErrorCause.RequestTimeout
                    VERIFY_OTP_FAILED -> ErrorCause.VerifyOtpFailed
                    else -> ErrorCause.BadResponseCode(code)
                }
            }
        }
        is SocketTimeoutException -> ErrorCause.RequestTimeout
        is IOException -> ErrorCause.Internal
        else -> ErrorCause.Unknown(cause)
    }
}