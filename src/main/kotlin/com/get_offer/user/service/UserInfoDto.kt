package com.get_offer.user.service

import com.get_offer.user.domain.User

data class UserInfoDto(
    val id: Long?,
    val profileImage: String,
    val nickname: String,
) {
    companion object {
        fun of(user: User): UserInfoDto {
            return UserInfoDto(
                id = user.id,
                profileImage = user.image,
                nickname = user.nickname
            )
        }
    }
}