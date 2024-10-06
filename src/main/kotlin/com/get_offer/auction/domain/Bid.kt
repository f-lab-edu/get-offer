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
@Table(name = "BIDS")
class Bid(
    private val productId: Long,

    private val bidderId: Long,

    private val biddingPrice: Int,

    @Enumerated(EnumType.STRING)
    val auctionStatus: AuctionStatus,

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private val id: Long? = null

) : AuditingTimeEntity()