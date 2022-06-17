package ir.jatlin.core.data.mapper

import ir.jatlin.core.model.product.ProductReview
import ir.jatlin.core.model.product.fromReviewStatusName
import ir.jatlin.core.network.model.product.review.ProductReviewNetwork

fun ProductReviewNetwork.asProductPreview() = ProductReview(
    id = id,
    dateCreatedGmt = dateCreatedGmt,
    productId = productId,
    rating = rating,
    content = content,
    reviewerName = reviewerName,
    reviewerEmail = reviewerEmail,
    status = fromReviewStatusName(status),
    verified = verified
)