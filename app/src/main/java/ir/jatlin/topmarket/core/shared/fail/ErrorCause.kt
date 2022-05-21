package ir.jatlin.topmarket.core.shared.fail

sealed class ErrorCause {

    class Unknown(val throwable: Throwable? = null) : ErrorCause()

    class BadResponseCode(val responseCode: Int) : ErrorCause()

    object Parse : ErrorCause()

    object NoConnection : ErrorCause()

    object RefreshTokenFailed : ErrorCause()

    object NoContent : ErrorCause()

    object Internal : ErrorCause()

    object VerifyOtpFailed : ErrorCause()
    object NotFound : ErrorCause()
    object Forbidden : ErrorCause()
    object BadRequest : ErrorCause()
    object RequestTimeout : ErrorCause()


    object StatusCode {
        const val BAD_REQUEST = 400
        const val FORBIDDEN = 403
        const val NOT_FOUND = 404
        const val REQUEST_TIMEOUT = 408
        const val VERIFY_OTP_FAILED = 1000
    }

}