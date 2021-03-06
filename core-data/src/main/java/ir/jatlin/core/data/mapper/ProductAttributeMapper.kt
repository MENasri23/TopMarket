package ir.jatlin.core.data.mapper

import ir.jatlin.core.model.attirbute.DefaultAttribute
import ir.jatlin.core.model.attirbute.ProductAttribute
import ir.jatlin.core.network.model.product.attirbute.NetworkDefaultAttribute
import ir.jatlin.core.network.model.product.attirbute.NetworkProductAttribute

fun NetworkDefaultAttribute.asDefaultAttribute() = DefaultAttribute(
    id = id,
    name = name,
    option = option,
)

fun NetworkProductAttribute.asProductAttribute() = ProductAttribute(
    hasArchives = hasArchives,
    id = id,
    name = name,
    type = type,
)