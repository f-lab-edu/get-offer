package com.get_offer.product.service

import com.get_offer.product.domain.Category
import com.get_offer.product.domain.Product
import com.get_offer.product.domain.ProductStatus
import com.get_offer.product.domain.WriterVo
import com.get_offer.user.domain.User
import java.time.LocalDateTime

data class ProductDetailDto(
    val id: Long?,
    val writer: WriterVo,
    val name: String,
    val category: Category,
    val images: List<String>,
    val description: String,
    val startPrice: Int,
    val currentPrice: Int,
    val status: ProductStatus,
    val startDate: LocalDateTime,
    val endDate: LocalDateTime,
    val isMine: Boolean,
) {
    companion object {
        fun of(product: Product, writer: User, userId: Long?): ProductDetailDto {
            return ProductDetailDto(
                id = product.id,
                writer = WriterVo(writer.id, writer.nickname, writer.image),
                name = product.title,
                category = product.category,
                images = product.images.images,
                description = product.description,
                startPrice = product.startPrice,
                currentPrice = product.currentPrice,
                status = product.status,
                startDate = product.startDate,
                endDate = product.endDate,
                isMine = product.writerId == userId,
            )
        }
    }
}