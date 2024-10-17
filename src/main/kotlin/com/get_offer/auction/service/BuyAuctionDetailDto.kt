package com.get_offer.auction.service

import com.get_offer.auction.domain.AuctionResult
import com.get_offer.auction.domain.AuctionStatus
import com.get_offer.product.domain.Product
import com.get_offer.user.domain.User

data class BuyAuctionDetailDto(
    val auctionId: Long,
    val auctionStatus: AuctionStatus,
    val product: AuctionProductUnitDto,
    val seller: AuctionUserDto,
) {
    companion object {
        fun of(product: Product, seller: User, auction: AuctionResult): BuyAuctionDetailDto {
            return BuyAuctionDetailDto(
                auctionId = auction.id,
                auctionStatus = auction.auctionStatus,
                product = AuctionProductUnitDto.of(product),
                seller = AuctionUserDto.of(seller),
            )
        }
    }
}