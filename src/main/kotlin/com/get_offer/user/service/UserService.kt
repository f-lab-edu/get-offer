package com.get_offer.user.service

import com.get_offer.common.exception.NotFoundException
import com.get_offer.user.repository.UserRepository
import org.springframework.stereotype.Service

@Service
class UserService(
    private val userRepository: UserRepository,
) {
    fun getUserInfo(id: Long): UserInfoDto {
        return UserInfoDto.of(userRepository
            .findById(id).orElseThrow { NotFoundException("$id 의 사용자는 존재하지 않습니다.") })
    }
}