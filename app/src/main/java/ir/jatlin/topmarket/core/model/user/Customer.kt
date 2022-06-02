package ir.jatlin.topmarket.core.model.user

data class Customer(
    val id: Int,
    val email: String,
    val username: String,
    val firstName: String,
    val lastName: String,
    val avatarUrl: String,
    val isPayingCustomer: Boolean,
    val role: String,
    val dateCreatedGmt: String,
    val dateModifiedGmt: String,
)