package com.get_offer.product.service

import com.get_offer.TestFixtures
import com.get_offer.multipart.ImageService
import com.get_offer.product.controller.ProductPostReqDto
import com.get_offer.product.domain.Category
import com.get_offer.product.domain.Product
import com.get_offer.product.domain.ProductImagesVo
import com.get_offer.product.domain.ProductStatus
import com.get_offer.product.repository.ProductRepository
import com.get_offer.user.domain.User
import com.get_offer.user.repository.UserRepository
import java.time.LocalDateTime
import java.util.*
import org.apache.coyote.BadRequestException
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers.anyList
import org.mockito.Mockito.any
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.mock.web.MockMultipartFile

class ProductServiceTest {
    private lateinit var productService: ProductService
    private lateinit var mockProductRepository: ProductRepository
    private lateinit var mockUserRepository: UserRepository
    private lateinit var mockImageService: ImageService

    @BeforeEach
    fun setUp() {
        mockProductRepository = mock(ProductRepository::class.java)
        mockUserRepository = mock(UserRepository::class.java)
        mockImageService = mock(ImageService::class.java)
        productService = ProductService(mockImageService, mockProductRepository, mockUserRepository)
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
                listOf(ProductStatus.IN_PROGRESS, ProductStatus.WAIT), pageable
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

    @Test
    fun postProductWithValidData() {
        // given
        val userId = 1L
        val productReqDto = makeProductPostDto()

        val mockImage = MockMultipartFile("images", "test.jpg", "image/jpeg", byteArrayOf(1, 2, 3))

        val imageUrls = listOf("http://image-url.com/test.jpg")
        `when`(mockImageService.saveImages(anyList())).thenReturn(imageUrls)

        val product = Product(
            title = productReqDto.title,
            category = productReqDto.category,
            writerId = userId,
            images = ProductImagesVo(imageUrls),
            description = productReqDto.description,
            startPrice = productReqDto.startPrice,
            currentPrice = productReqDto.startPrice,
            startDate = productReqDto.startDate,
            endDate = productReqDto.endDate,
            status = ProductStatus.IN_PROGRESS
        )

        `when`(mockProductRepository.save(any(Product::class.java))).thenReturn(product)

        // when
        val result = productService.postProduct(productReqDto, userId, listOf(mockImage))

        // then
        assertNotNull(result)
        assertEquals(productReqDto.title, result.title)
    }

    @Test
    fun postProductWithInvalidStartPrice() {
        // given
        val userId = 1L
        val productReqDto = makeProductPostDto().copy(
            startPrice = -1000,  // Invalid start price
        )

        val mockImage = MockMultipartFile("images", "test.jpg", "image/jpeg", byteArrayOf(1, 2, 3))

        // when & then
        val exception = assertThrows(BadRequestException::class.java) {
            productService.postProduct(productReqDto, userId, listOf(mockImage))
        }

        assertEquals("startPrice가 0보다 작을 수 없습니다.", exception.message)
    }

    @Test
    fun `test postProduct with invalid date range`() {
        // given
        val userId = 1L
        val productReqDto = makeProductPostDto().copy(
            // Invalid start date (after end date))
            startDate = LocalDateTime.now().plusDays(10)
        )

        val mockImage = MockMultipartFile("images", "test.jpg", "image/jpeg", byteArrayOf(1, 2, 3))

        // when & then
        val exception = assertThrows(BadRequestException::class.java) {
            productService.postProduct(productReqDto, userId, listOf(mockImage))
        }

        assertEquals("시작 날짜가 유효하지 않습니다.", exception.message)
    }

    @Test
    fun editProductReturnsDto() {
        // Given
        val productId = 1L
        val writerId = 1L
        val req = ProductEditDto(
            productId = productId,
            writerId = writerId,
            title = "new title",
            description = "new description"
        )
        val existingProduct = Product(
            writerId, "old Title", Category.BOOKS, images = ProductImagesVo(listOf("images.png")),
            "old description", 1000, 1000, ProductStatus.WAIT,
            LocalDateTime.now(),
            LocalDateTime.now(),
            productId,
        )

        `when`(mockProductRepository.findById(productId)).thenReturn(Optional.of(existingProduct))
        `when`(mockProductRepository.save(any(Product::class.java))).thenReturn(existingProduct)

        // When
        val result = productService.editProduct(req)

        // Then
        assertEquals(req.title, result.title)
        assertEquals(req.writerId, result.writerId)
    }

    private fun makeProductPostDto(): ProductPostReqDto {
        return ProductPostReqDto(
            title = "Test Product",
            description = "Test Description",
            startPrice = 1000,
            startDate = LocalDateTime.now().plusDays(10),  // Invalid start date (after end date)
            endDate = LocalDateTime.now().plusDays(3),
            category = Category.BOOKS
        )
    }
}