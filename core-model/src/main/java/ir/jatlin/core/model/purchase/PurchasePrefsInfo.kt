package ir.jatlin.core.model.purchase

data class PurchasePrefsInfo(
    val customerId: Int,
    val activeOrderId: Int
) {


    companion object {
        const val GUEST_CUSTOMER = 0
        const val NO_ACTIVE_ORDER = -1
    }
}