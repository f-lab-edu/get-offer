package com.get_offer.product.domain

import com.get_offer.product.service.ProductEditDto
import java.time.LocalDateTime

data class ProductEditReq(
    val productId: Long,
    val title: String?,
    val category: Category?,
    val description: String?,
    val startPrice: Int?,
    val startDate: LocalDateTime?,
    val endDate: LocalDateTime?,
    val writerId: Long,
    val images: List<String>?
) {
    companion object {
        fun of(req: ProductEditDto, imageUrls: List<String>?): ProductEditReq {
            return ProductEditReq(
                productId = req.productId,
                title = req.title,
                category = req.category,
                description = req.description,
                startPrice = req.startPrice,
                startDate = req.startDate,
                endDate = req.endDate,
                writerId = req.writerId,
                images = imageUrls
            )
        }
    }
}