package com.get_offer.product.service

import com.get_offer.product.controller.ProductEditReqDto
import com.get_offer.product.domain.Category
import java.time.LocalDateTime
import org.springframework.web.multipart.MultipartFile

data class ProductEditDto(
    val productId: Long,
    val writerId: Long,
    val title: String? = null,
    val category: Category? = null,
    val description: String? = null,
    val startPrice: Int? = null,
    val startDate: LocalDateTime? = null,
    val endDate: LocalDateTime? = null,
    val images: List<MultipartFile>? = null
) {
    companion object {
        fun of(productId: Long, req: ProductEditReqDto, userId: Long, images: List<MultipartFile>?): ProductEditDto {
            return ProductEditDto(
                productId = productId,
                title = req.title,
                category = req.category,
                description = req.description,
                startPrice = req.startPrice,
                startDate = req.startDate,
                endDate = req.endDate,
                writerId = userId,
                images = images
            )
        }
    }
}