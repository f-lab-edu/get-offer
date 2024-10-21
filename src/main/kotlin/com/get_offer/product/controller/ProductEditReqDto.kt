package com.get_offer.product.controller

import com.get_offer.product.domain.Category
import java.time.LocalDateTime

data class ProductEditReqDto(
    val title: String?,
    val category: Category?,
    val description: String?,
    val startPrice: Int?,
    val startDate: LocalDateTime?,
    val endDate: LocalDateTime?,
)