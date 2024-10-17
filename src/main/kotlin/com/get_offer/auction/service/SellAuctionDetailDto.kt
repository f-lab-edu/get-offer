package com.get_offer.auction.service

import com.get_offer.auction.domain.AuctionResult
import com.get_offer.auction.domain.AuctionStatus
import com.get_offer.product.domain.Product
import com.get_offer.user.domain.User

data class SellAuctionDetailDto(
    val auctionId: Long,
    val auctionStatus: AuctionStatus,
    val product: AuctionProductUnitDto,
    val buyer: AuctionUserDto,
) {
    companion object {
        fun of(product: Product, buyer: User, auction: AuctionResult): SellAuctionDetailDto {
            return SellAuctionDetailDto(
                auctionId = auction.id,
                auctionStatus = auction.auctionStatus,
                product = AuctionProductUnitDto.of(product),
                buyer = AuctionUserDto.of(buyer),
            )
        }
    }
}