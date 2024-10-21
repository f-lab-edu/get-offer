package com.get_offer.product.service

import com.get_offer.product.domain.Product
import java.time.LocalDateTime

data class ProductSaveDto(
    val title: String,
    val id: Long,
    val writerId: Long,
    val createdTime: LocalDateTime
) {
    companion object {
        fun of(product: Product): ProductSaveDto {
            return ProductSaveDto(
                title = product.title,
                id = product.id,
                writerId = product.writerId,
                createdTime = product.createdAt
            )
        }
    }
}