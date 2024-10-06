package com.get_offer.product.service

import com.get_offer.TestFixtures
import com.get_offer.product.domain.ProductStatus
import com.get_offer.product.repository.ProductRepository
import com.get_offer.user.domain.User
import com.get_offer.user.repository.UserRepository
import java.util.*
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.mockito.Mockito.any
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest

class ProductServiceTest {
    private lateinit var productService: ProductService
    private lateinit var mockProductRepository: ProductRepository
    private lateinit var mockUserRepository: UserRepository

    @BeforeEach
    fun setUp() {
        mockProductRepository = mock(ProductRepository::class.java)
        mockUserRepository = mock(UserRepository::class.java)
        productService = ProductService(mockProductRepository, mockUserRepository)
    }

    @Test
    @DisplayName("상품 리스트 Dto 변환")
    fun productListToDto() {
        // given
        val givenProduct = TestFixtures.createProductInProgress(1L)

        val givenProduct2 = TestFixtures.createProductWait(2L)
        val pageable = PageRequest.of(0, 3)
        val items = listOf(givenProduct, givenProduct2)

        `when`(
            mockProductRepository.findAllByStatusInOrderByEndDateDesc(
                listOf(ProductStatus.IN_PROGRESS, ProductStatus.WAIT),
                pageable
            )
        ).thenReturn(
            PageImpl(items, pageable, items.size.toLong())
        )

        // when
        val result = productService.getProductList(1L, pageable)

        // then
        Assertions.assertThat(result.content[0].name).isEqualTo("product")
        Assertions.assertThat(result.content[1].name).isEqualTo("product2")
        Assertions.assertThat(result.content[0].thumbnail).isEqualTo("https://image1.png")
        Assertions.assertThat(result.content[1].thumbnail).isEqualTo("https://image1.png")
        Assertions.assertThat(result.content[0].status).isEqualTo(ProductStatus.IN_PROGRESS)
        Assertions.assertThat(result.content[1].status).isEqualTo(ProductStatus.WAIT)
        Assertions.assertThat(result.content[0].isMine).isEqualTo(true)
        Assertions.assertThat(result.content[1].isMine).isEqualTo(false)
    }

    @Test
    @DisplayName("상품상세 Dto 변환")
    fun productDetailToDto() {
        // given
        val givenProduct = TestFixtures.createProductInProgress(1L)
        val givenUser = User("user", "https://image1.png")

        `when`(mockProductRepository.findById(any())).thenReturn(Optional.of(givenProduct))
        `when`(mockUserRepository.findById(any())).thenReturn(Optional.of(givenUser))

        // when
        val result = productService.getProductDetail(1L, 1L)

        // then
        Assertions.assertThat(result.images.size).isEqualTo(2)
        Assertions.assertThat(result.images[0]).isEqualTo("https://image1.png")
        Assertions.assertThat(result.images[1]).isEqualTo("https://image2.png")
    }
}