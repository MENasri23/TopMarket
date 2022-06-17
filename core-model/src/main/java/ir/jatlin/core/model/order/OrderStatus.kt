package ir.jatlin.core.model.order

enum class OrderStatus(val statusName: String) {
    Pending("pending"),
    Processing("processing"),
    OnHold("on-hold"),
    Completed("completed"),
    Cancelled("cancelled"),
    Refunded("refunded"),
    Failed("failed"),
    Trash("trash"),
}

fun String.asOrderStatusName() =
    OrderStatus.values().firstOrNull { it.statusName == this }
        ?: OrderStatus.Trash