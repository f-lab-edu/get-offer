package com.get_offer.product.service

import com.get_offer.chat.service.ChatRoomService
import com.get_offer.chat.service.GroupChatRoomService
import com.get_offer.common.exception.ApiException
import com.get_offer.common.exception.ExceptionCode
import com.get_offer.common.multipart.ImageService
import com.get_offer.common.naver.NaverService
import com.get_offer.product.controller.ProductEditDto
import com.get_offer.product.controller.ProductPostReqDto
import com.get_offer.product.domain.Product
import com.get_offer.product.domain.ProductImagesVo
import com.get_offer.product.domain.ProductRepository
import com.get_offer.product.domain.ProductStatus
import com.get_offer.user.domain.UserRepository
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
    private val naverService: NaverService,
    private val chatRoomService: ChatRoomService,
    private val groupChatRoomService: GroupChatRoomService,
) {

    fun getProductList(userId: Long, pageRequest: PageRequest): Page<ProductListDto> {
        val productList: Page<Product> = productRepository.findAllByStatusInOrderByEndDateDesc(
            listOf(ProductStatus.IN_PROGRESS, ProductStatus.WAIT), pageRequest
        )
        return productList.map { ProductListDto.of(it, userId) }
    }

    fun getProductDetail(id: Long, userId: Long): ProductDetailDto {
        val product = productRepository.findById(id)
            .orElseThrow { ApiException(ExceptionCode.NOTFOUND, "$id 의 상품은 존재하지 않습니다.") }

        val writer =
            userRepository.findById(userId)
                .orElseThrow { ApiException(ExceptionCode.NOTFOUND, "$userId 의 사용자는 존재하지 않습니다.") }

        return ProductDetailDto.of(product, writer, userId)
    }

    @Transactional
    fun postProduct(req: ProductPostReqDto, userId: Long, images: List<MultipartFile>): ProductSaveDto {
        val imageUrls = imageService.saveImages(images)
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

        // 그룹 채팅방 생성 //TODO : 이벤트 처리 등으로 의존성 제거 고려
        groupChatRoomService.createGroupChatRoom(product)
        return ProductSaveDto.of(product)
    }

    @Transactional
    fun editProduct(req: ProductEditDto): ProductSaveDto {
        val product = productRepository.findById(req.productId)
            .orElseThrow { ApiException(ExceptionCode.NOTFOUND, "${req.productId} 의 상품은 존재하지 않습니다.") }
        // access
        if (product.writerId != req.writerId) {
            throw ApiException(ExceptionCode.UN_AUTHORIZED)
        }

        if (product.status != ProductStatus.WAIT) {
            throw BadRequestException("진행 전 대기 상태에서만 수정 할 수 있습니다.")
        }

        val imageUrls = if (req.images != null) {
            imageService.deleteImages(product.images.images) // 기존 사진 삭제
            imageService.saveImages(req.images)
        } else null

        product.update(
            req.startDate,
            req.endDate,
            req.title,
            req.category,
            req.startPrice,
            imageUrls,
        )

        return ProductSaveDto.of(product)
    }

    fun summaryProductDescription(productId: Long): ProductSummaryDto {
        val product = productRepository.findById(productId)
            .orElseThrow { ApiException(ExceptionCode.NOTFOUND, "$productId 의 상품은 존재하지 않습니다.") }

        val result = naverService.summary(product.description)
        if (result.status.code != "20000") throw IllegalArgumentException("먼가 잘못 받아옴 띠용")

        return ProductSummaryDto.of(productId, result.result.text)
    }
}