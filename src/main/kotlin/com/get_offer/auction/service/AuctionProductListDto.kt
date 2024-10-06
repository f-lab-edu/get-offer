package com.get_offer.auction.service

import com.get_offer.product.domain.Category
import com.get_offer.product.domain.Product
import com.get_offer.product.domain.ProductStatus
import java.time.LocalDateTime

data class AuctionProductListDto(
    val id: Long?,
    val writerId: Long,
    val name: String,
    val category: Category,
    val thumbnail: String,
    val currentPrice: Int,
    val status: ProductStatus,
    val startDate: LocalDateTime,
    val endDate: LocalDateTime,
    val isMine: Boolean,
) {
    companion object {
        fun of(product: Product, userId: Long?): AuctionProductListDto {
            return AuctionProductListDto(
                id = product.id,
                writerId = product.writerId,
                name = product.title,
                category = product.category,
                thumbnail = product.images.thumbnail(),
                currentPrice = product.currentPrice,
                status = product.status,
                startDate = product.startDate,
                endDate = product.endDate,
                isMine = product.writerId == userId,
            )
        }
    }
}
