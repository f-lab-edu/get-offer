package com.get_offer.auction.service

import com.get_offer.auction.controller.repository.AuctionResultRepository
import com.get_offer.common.exception.NotFoundException
import com.get_offer.product.repository.ProductRepository
import org.springframework.stereotype.Service

@Service
class AuctionService(
    private val productRepository: ProductRepository,
    private val auctionRepository: AuctionResultRepository,
) {
    fun getSellHistory(userId: Long): List<SellProductListDto> {
        val productList = productRepository.findAllByWriterIdOrderByEndDateDesc(userId)

        return productList.map { SellProductListDto.of(it, userId) }
    }

    fun getBuyHistory(userId: Long): List<BuyProductListDto> {
        // 옥션 최종 정보에서 buyer가 나인걸 찾음
        val auctionResults = auctionRepository.findAllByBuyerIdOrderByCreatedAtDesc(userId)
        // 해당 상품 정보를 찾음
        return auctionResults.map {
            val product = productRepository.findById(it.productId)
                .orElseThrow { NotFoundException("${it.productId} 의 상품은 존재하지 않습니다.") }
            BuyProductListDto.of(it, product, userId)
        }
    }
}