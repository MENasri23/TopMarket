package ir.jatlin.topmarket.core.shared.fail

interface ErrorHandler {
    fun handle(cause: Throwable): ErrorCause
}