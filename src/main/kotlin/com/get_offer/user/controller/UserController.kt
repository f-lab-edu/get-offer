package com.get_offer.user.controller

import ApiResponse
import com.get_offer.user.service.UserInfoDto
import com.get_offer.user.service.UserService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/users")
class UserController(
    private val userService: UserService,
) {
    @GetMapping()
    fun getUserInfo(@RequestParam userId: String): ApiResponse<UserInfoDto> {
        return ApiResponse.success(userService.getUserInfo(userId.toLong()))
    }
}