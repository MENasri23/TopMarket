package ir.jatlin.topmarket.core.data.mapper

import ir.jatlin.topmarket.core.model.common.ProductImage
import ir.jatlin.topmarket.core.model.product.Product
import ir.jatlin.topmarket.core.model.product.ProductDetails
import ir.jatlin.topmarket.core.network.model.common.NetworkImage
import ir.jatlin.topmarket.core.network.model.product.attirbute.NetworkDefaultAttribute
import ir.jatlin.topmarket.core.network.model.product.attirbute.NetworkProductAttribute
import ir.jatlin.topmarket.core.network.model.product.category.NetworkCategory


fun ir.jatlin.topmarket.core.network.model.product.NetworkProduct.asProduct() = Product(
    categories = categories.map(NetworkCategory::asCategory),
    id = id,
    images = images.map(NetworkImage::asProductImage),
    name = name,
    price = price,
    description = description,
    regularPrice = regularPrice,
    stockStatus = stockStatus,
    createdDateGmt = createdDateGmt,
    createdDate = createdDate,
    ratingCount = ratingCount,
    totalSales = totalSales,
    averageRating = averageRating,
)


fun ir.jatlin.topmarket.core.network.model.product.NetworkProductDetails.asProductDetails() =
    ProductDetails(
        attributes = attributes.map(NetworkProductAttribute::asProductAttribute),
        averageRating = averageRating,
        categories = categories.map(NetworkCategory::asCategory),
        dateCreatedGmt = dateCreatedGmt,
        defaultAttributes = defaultAttributes.map(NetworkDefaultAttribute::asDefaultAttribute),
        description = description,
        id = id,
        images = images.map(NetworkImage::asProductImage),
        name = name,
        price = price,
        purchasable = purchasable,
        ratingCount = ratingCount,
        regularPrice = regularPrice,
        relatedIds = relatedIds,
        totalSales = totalSales,
        stockQuantity = stockQuantity,
        weight = weight,
        stockStatus = stockStatus,
    )


fun NetworkImage.asProductImage() = ProductImage(
    id = id,
    name = name,
    url = url,
)