package com.get_offer.auction.service

import com.get_offer.TestFixtures
import com.get_offer.auction.controller.repository.AuctionResultRepository
import com.get_offer.product.repository.ProductRepository
import com.get_offer.user.repository.UserRepository
import java.util.*
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.any
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`

class AuctionServiceTest {
    private lateinit var auctionService: AuctionService
    private lateinit var mockProductRepository: ProductRepository
    private lateinit var mockAuctionRepository: AuctionResultRepository
    private lateinit var mockUserRepository: UserRepository

    @BeforeEach
    fun setUp() {
        mockProductRepository = mock(ProductRepository::class.java)
        mockAuctionRepository = mock(AuctionResultRepository::class.java)
        mockUserRepository = mock(UserRepository::class.java)
        auctionService = AuctionService(mockProductRepository, mockAuctionRepository, mockUserRepository)
    }

    @Test
    fun getSellHistoryReturnsDto() {
        val writerId = 1L

        val givenProduct = TestFixtures.createProductInProgress(writerId)
        val givenProduct2 = TestFixtures.createProductWait(writerId)

        // when
        `when`(mockProductRepository.findAllByWriterIdOrderByEndDateDesc(writerId)).thenReturn(
            listOf(
                givenProduct, givenProduct2
            )
        )

        val result = auctionService.getSellHistory(writerId)

        Assertions.assertThat(result.size).isEqualTo(2)
        Assertions.assertThat(result[0].id).isEqualTo(givenProduct.id)
        Assertions.assertThat(result[0].currentPrice).isEqualTo(givenProduct.currentPrice)
        Assertions.assertThat(result[0].writerId).isEqualTo(writerId)
        Assertions.assertThat(result[1].id).isEqualTo(givenProduct2.id)
        Assertions.assertThat(result[1].currentPrice).isEqualTo(givenProduct2.currentPrice)
        Assertions.assertThat(result[1].writerId).isEqualTo(writerId)
    }

    @Test
    fun getBuyHistoryReturnsDto() {
        val writerId = 1L
        val buyerId = 2L

        val givenAuction = TestFixtures.createAuction(buyerId)
        val givenProduct = TestFixtures.createProductCompleted(writerId)

        `when`(mockAuctionRepository.findAllByBuyerIdOrderByCreatedAtDesc(buyerId)).thenReturn(listOf(givenAuction))

        `when`(mockProductRepository.findById(any())).thenReturn(Optional.of(givenProduct))

        val result = auctionService.getBuyHistory(buyerId)

        Assertions.assertThat(result.size).isEqualTo(1)
        Assertions.assertThat(result[0].productId).isEqualTo(givenAuction.id)
        Assertions.assertThat(result[0].buyerId).isEqualTo(givenAuction.buyerId)
        Assertions.assertThat(result[0].finalPrice).isEqualTo(givenAuction.finalPrice)
        Assertions.assertThat(result[0].writerId).isEqualTo(givenProduct.writerId)
    }

    @Test
    fun getSoldAuctionDetailReturnsDto() {
        val writerId = 1L
        val buyerId = 2L

        val givenAuction = TestFixtures.createAuction(buyerId)
        val givenProduct = TestFixtures.createProductCompleted(writerId)
        val givenBuyer = TestFixtures.createBuyer(buyerId)

        `when`(mockAuctionRepository.findById(givenAuction.id)).thenReturn(Optional.of(givenAuction))
        `when`(mockProductRepository.findById(any())).thenReturn(Optional.of(givenProduct))
        `when`(mockUserRepository.findById(any())).thenReturn(Optional.of(givenBuyer))

        val result = auctionService.getSoldAuctionDetail(writerId, givenAuction.id)

        Assertions.assertThat(result.auctionId).isEqualTo(givenAuction.id)
        Assertions.assertThat(result.auctionStatus).isEqualTo(givenAuction.auctionStatus)
        Assertions.assertThat(result.product.name).isEqualTo(givenProduct.title)
        Assertions.assertThat(result.buyer.nickname).isEqualTo(givenBuyer.nickname)
    }

    @Test
    fun getBoughtAuctionDetailReturnsDto() {
        val sellerId = 1L
        val buyerId = 2L

        val givenAuction = TestFixtures.createAuction(buyerId)
        val givenProduct = TestFixtures.createProductCompleted(sellerId)
        val givenBuyer = TestFixtures.createBuyer(buyerId)

        `when`(mockAuctionRepository.findById(givenAuction.id)).thenReturn(Optional.of(givenAuction))
        `when`(mockProductRepository.findById(any())).thenReturn(Optional.of(givenProduct))
        `when`(mockUserRepository.findById(any())).thenReturn(Optional.of(givenBuyer))

        val result = auctionService.getBoughtAuctionDetail(buyerId, givenAuction.id)

        Assertions.assertThat(result.auctionId).isEqualTo(givenAuction.id)
        Assertions.assertThat(result.auctionStatus).isEqualTo(givenAuction.auctionStatus)
        Assertions.assertThat(result.product.name).isEqualTo(givenProduct.title)
        Assertions.assertThat(result.seller.nickname).isEqualTo(givenBuyer.nickname)
    }
}

