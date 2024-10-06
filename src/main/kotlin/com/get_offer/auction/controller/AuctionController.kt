package com.get_offer.auction.controller

import ApiResponse
import com.get_offer.auction.service.AuctionService
import com.get_offer.auction.service.BuyProductListDto
import com.get_offer.auction.service.SellProductListDto
import org.springframework.web.bind.annotation.GetMapping
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
    fun getUserSellHistory(@RequestParam userId: String): ApiResponse<List<SellProductListDto>> {
        return ApiResponse.success(auctionService.getSellHistory(userId.toLong()))
    }

    /**
     * 사용자가 경매에서 낙찰된 모든 상품을 조회한다.
     */
    @GetMapping("/buyHistory")
    fun getUserBuyHistory(@RequestParam userId: String): ApiResponse<List<BuyProductListDto>> {
        return ApiResponse.success(auctionService.getBuyHistory(userId.toLong()))
    }
}