package ir.jatlin.topmarket.core.model.user

data class Customer(
    val id: Int,
    val email: String,
    val username: String,
    val firstName: String,
    val lastName: String,
    val avatarUrl: String,
    val role: String
) {

    companion object {
        val Empty = Customer(
            0, "", "", "", "", "", ""
        )
    }
}