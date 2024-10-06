package com.get_offer.auction.service

import com.get_offer.auction.domain.AuctionResult
import com.get_offer.auction.domain.AuctionStatus
import com.get_offer.product.domain.Category
import com.get_offer.product.domain.Product
import java.time.LocalDateTime

data class BuyProductListDto(
    val productId: Long?,
    val writerId: Long,
    val buyerId: Long,
    val auctionId: Long?,
    val title: String,
    val category: Category,
    val thumbnail: String,
    val finalPrice: Int,
    val actionStatus: AuctionStatus,
    val startDate: LocalDateTime,
    val endDate: LocalDateTime,
) {
    companion object {
        fun of(auctionResult: AuctionResult, product: Product, userId: Long?): BuyProductListDto {
            return BuyProductListDto(
                productId = product.id,
                writerId = product.writerId,
                buyerId = auctionResult.buyerId,
                auctionId = auctionResult.id,
                title = product.title,
                category = product.category,
                thumbnail = product.images.thumbnail(),
                finalPrice = auctionResult.finalPrice,
                actionStatus = auctionResult.auctionStatus,
                startDate = product.startDate,
                endDate = product.endDate,
            )
        }
    }
}
