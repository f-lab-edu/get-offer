package com.get_offer.search.controller

import ApiResponse
import com.get_offer.search.service.IndexResDto
import com.get_offer.search.service.IndexingService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/index")
class IndexingController(
    private val indexingService: IndexingService,
) {
    @GetMapping("/products")
    fun indexProduct(): ApiResponse<IndexResDto> {
        return ApiResponse.success(
            indexingService.indexProduct()
        )
    }
}