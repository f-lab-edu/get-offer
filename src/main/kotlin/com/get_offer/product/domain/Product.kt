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

@Entity
@Table(name = "PRODUCTS")
class Product(
    val writerId: Long,

    val title: String,

    @Enumerated(EnumType.STRING)
    val category: Category,

    @Convert(converter = ProductImagesConverter::class)
    @Column(name = "IMAGES")
    val images: ProductImagesVo,

    val description: String,

    val startPrice: Int,

    var currentPrice: Int,

    @Enumerated(EnumType.STRING)
    var status: ProductStatus,

    var startDate: LocalDateTime,

    var endDate: LocalDateTime,

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
) : AuditingTimeEntity()