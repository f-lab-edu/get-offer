package com.get_offer.auction.domain

import com.get_offer.common.AuditingTimeEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.math.BigDecimal

@Entity
@Table(name = "AUCTION_RESULTS")
class AuctionResult(

    @Column(unique = true)
    val productId: Long,

    val auctionName: String,

    val buyerId: Long = 0L,

    val finalPrice: BigDecimal,

    @Enumerated(EnumType.STRING)
    var auctionStatus: AuctionStatus,

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L,
) : AuditingTimeEntity()