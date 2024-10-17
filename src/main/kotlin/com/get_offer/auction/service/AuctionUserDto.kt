package com.get_offer.auction.service

import com.get_offer.user.domain.User

data class AuctionUserDto(
    val id: Long,
    val profileImage: String,
    val nickname: String,
) {
    companion object {
        fun of(user: User): AuctionUserDto {
            return AuctionUserDto(
                id = user.id,
                profileImage = user.image,
                nickname = user.nickname
            )
        }
    }
}