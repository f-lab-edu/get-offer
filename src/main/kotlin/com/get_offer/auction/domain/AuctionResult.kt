package com.get_offer.auction.domain

import com.get_offer.common.AuditingTimeEntity
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "AUCTION_RESULTS")
class AuctionResult(

    val productId: Long,

    val buyerId: Long,

    val finalPrice: Int,

    @Enumerated(EnumType.STRING)
    val auctionStatus: AuctionStatus,

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L,
) : AuditingTimeEntity()