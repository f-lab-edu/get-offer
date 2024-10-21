package com.get_offer.product.domain

import com.get_offer.common.AuditingTimeEntity
import jakarta.persistence.Column
import jakarta.persistence.Convert
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import org.apache.coyote.BadRequestException

@Entity
@Table(name = "PRODUCTS")
class Product(
    val writerId: Long,

    val title: String,

    @Enumerated(EnumType.STRING)
    val category: Category,

    @Convert(converter = ProductImagesConverter::class) @Column(name = "IMAGES") val images: ProductImagesVo,

    val description: String,

    val startPrice: Int,

    var currentPrice: Int,

    @Enumerated(EnumType.STRING) var status: ProductStatus,

    var startDate: LocalDateTime,

    var endDate: LocalDateTime,

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) var id: Long = 0L,
) : AuditingTimeEntity() {
    fun updateProduct(dto: ProductEditReq): Product {
        if (dto.startPrice != null) {
            validateStartPrice(dto.startPrice)
        }

        val startDate = dto.startDate ?: this.startDate
        val endDate = dto.endDate ?: this.endDate
        validateDateRange(startDate, endDate)

        return Product(
            id = this.id,
            title = dto.title ?: this.title,
            description = dto.description ?: this.description,
            startPrice = dto.startPrice ?: this.startPrice,
            currentPrice = dto.startPrice ?: this.startPrice,
            category = dto.category ?: this.category,
            status = ProductStatus.WAIT,
            startDate = startDate,
            endDate = endDate,
            images = dto.images?.let { ProductImagesVo(it) } ?: this.images,
            writerId = this.writerId,
        )
    }

    companion object {
        fun validateProduct(startPrice: Int, startDate: LocalDateTime, endDate: LocalDateTime) {
            validateStartPrice(startPrice)
            validateDateRange(startDate, endDate)
        }

        fun checkStatus(startDate: LocalDateTime): ProductStatus {
            if (LocalDateTime.now().isBefore(startDate)) {
                return ProductStatus.WAIT
            }
            return ProductStatus.IN_PROGRESS
        }

        private fun validateStartPrice(startPrice: Int) {
            if (startPrice < 0) {
                throw BadRequestException("startPrice가 0보다 작을 수 없습니다.")
            }
        }

        private fun validateDateRange(startDate: LocalDateTime, endDate: LocalDateTime) {
            if (startDate.isAfter(endDate)) throw BadRequestException("시작 날짜가 유효하지 않습니다.")
            if (ChronoUnit.DAYS.between(startDate, endDate) > 7) throw BadRequestException("경매 기간은 7일을 넘길 수 없습니다.")
        }
    }
}