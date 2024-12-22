package com.get_offer.product.controller

import com.fasterxml.jackson.annotation.JsonProperty
import com.get_offer.product.domain.Category
import java.math.BigDecimal
import java.time.LocalDateTime

data class ProductPostReqDto(
    @JsonProperty("title") val title: String,
    @JsonProperty("category") val category: Category,
    @JsonProperty("description") val description: String,
    @JsonProperty("startPrice") val startPrice: BigDecimal,
    @JsonProperty("startDate") val startDate: LocalDateTime,
    @JsonProperty("endDate") val endDate: LocalDateTime,
)
