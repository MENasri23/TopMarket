package ir.jatlin.topmarket.core.model.product


data class ProductReview(
    val id: Int,
    val dateCreatedGmt: String,
    val productId: Int,
    val rating: Int,
    val content: String,
    val reviewerName: String,
    val reviewerEmail: String,
    val status: ReviewStatus,
    val verified: Boolean
)

enum class ReviewStatus {
    APPROVED,
    HOLD,
    SPAM,
    UNSPAM,
    TRASH,
    UNTRASH
}

fun fromReviewStatusName(status: String): ReviewStatus {
    return ReviewStatus.values().firstOrNull { it.name.lowercase() == status }
        ?: ReviewStatus.APPROVED
}
