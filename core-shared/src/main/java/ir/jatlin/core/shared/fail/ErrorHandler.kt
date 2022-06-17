package ir.jatlin.core.shared.fail

interface ErrorHandler {
    fun handle(cause: Throwable): ErrorCause
}