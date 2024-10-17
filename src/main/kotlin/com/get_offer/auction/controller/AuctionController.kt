package com.get_offer.auction.controller

import ApiResponse
import com.get_offer.auction.service.AuctionService
import com.get_offer.auction.service.BuyAuctionDetailDto
import com.get_offer.auction.service.BuyAuctionDto
import com.get_offer.auction.service.SellAuctionDetailDto
import com.get_offer.auction.service.SellAuctionDto
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/auctions")
class AuctionController(
    private val auctionService: AuctionService,
) {

    /**
     * 사용자가 판매한 모든 상품을 조회 한다.
     */
    @GetMapping("/sellHistory")
    fun getUserSellHistory(@RequestParam userId: String): ApiResponse<List<SellAuctionDto>> {
        return ApiResponse.success(auctionService.getSellHistory(userId.toLong()))
    }

    /**
     * 사용자가 경매에서 낙찰된 모든 상품을 조회한다.
     */
    @GetMapping("/buyHistory")
    fun getUserBuyHistory(@RequestParam userId: String): ApiResponse<List<BuyAuctionDto>> {
        return ApiResponse.success(auctionService.getBuyHistory(userId.toLong()))
    }

    @GetMapping("{id}/sold")
    fun getSoldAuctionDetail(@RequestParam userId: String, @PathVariable id: Long): ApiResponse<SellAuctionDetailDto> {
        return ApiResponse.success(auctionService.getSoldAuctionDetail(userId.toLong(), id))
    }

    @GetMapping("{id}/bought")
    fun getBoughtAuctionDetail(
        @RequestParam userId: String,
        @PathVariable id: Long
    ): ApiResponse<BuyAuctionDetailDto> {
        return ApiResponse.success(auctionService.getBoughtAuctionDetail(userId.toLong(), id))
    }
}