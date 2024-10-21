package com.get_offer.product.service

import com.get_offer.common.exception.NotFoundException
import com.get_offer.common.exception.UnAuthorizationException
import com.get_offer.multipart.ImageService
import com.get_offer.product.controller.ProductPostReqDto
import com.get_offer.product.domain.Product
import com.get_offer.product.domain.ProductEditReq
import com.get_offer.product.domain.ProductImagesVo
import com.get_offer.product.domain.ProductStatus
import com.get_offer.product.repository.ProductRepository
import com.get_offer.user.repository.UserRepository
import org.apache.coyote.BadRequestException
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.multipart.MultipartFile

@Service
@Transactional(readOnly = true)
class ProductService(
    private val imageService: ImageService,
    private val productRepository: ProductRepository,
    private val userRepository: UserRepository,
) {

    fun getProductList(userId: Long, pageRequest: PageRequest): Page<ProductListDto> {
        val productList: Page<Product> = productRepository.findAllByStatusInOrderByEndDateDesc(
            listOf(ProductStatus.IN_PROGRESS, ProductStatus.WAIT), pageRequest
        )
        return productList.map { ProductListDto.of(it, userId) }
    }

    fun getProductDetail(id: Long, userId: Long): ProductDetailDto {
        val product = productRepository.findById(id).orElseThrow { NotFoundException("$id 의 상품은 존재하지 않습니다.") }

        val writer = userRepository.findById(id).orElseThrow { NotFoundException("$id 의 사용자는 존재하지 않습니다.") }

        return ProductDetailDto.of(product, writer, userId)
    }

    @Transactional
    fun postProduct(req: ProductPostReqDto, userId: Long, images: List<MultipartFile>): ProductSaveDto {
        val imageUrls = imageService.saveImages(images)

        Product.validateProduct(req.startPrice, req.startDate, req.endDate)

        val product = productRepository.save(
            Product(
                title = req.title,
                category = req.category,
                writerId = userId,
                images = ProductImagesVo(imageUrls),
                description = req.description,
                startPrice = req.startPrice,
                currentPrice = req.startPrice,
                startDate = req.startDate,
                endDate = req.endDate,
                status = Product.checkStatus(req.startDate)
            )
        )
        return ProductSaveDto.of(product)
    }

    @Transactional
    fun editProduct(req: ProductEditDto): ProductSaveDto {
        var product = productRepository.findById(req.productId)
            .orElseThrow { NotFoundException("${req.productId} 의 상품은 존재하지 않습니다.") }
        // access
        if (product.writerId != req.writerId) {
            throw UnAuthorizationException()
        }

        if (product.status != ProductStatus.WAIT) {
            throw BadRequestException("진행 전 대기 상태에서만 수정 할 수 있습니다.")
        }

        val imageUrls = if (req.images != null) {
            imageService.deleteImages(product.images.images) // 기존 사진 삭제
            imageService.saveImages(req.images)
        } else null


        product = product.updateProduct(ProductEditReq.of(req, imageUrls))
        productRepository.save(product)

        return ProductSaveDto.of(product)
    }
}