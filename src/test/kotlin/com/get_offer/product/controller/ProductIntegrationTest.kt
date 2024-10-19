package com.get_offer.product.controller

import java.nio.file.Files
import java.nio.file.Paths
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.mock.web.MockMultipartFile
import org.springframework.test.context.jdbc.Sql
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status


@SpringBootTest
@Sql("classpath:test_data.sql")
@AutoConfigureMockMvc
class ProductIntegrationTest(
    @Autowired val mockMvc: MockMvc,
) {
    @Test
    fun productListIntegrationTest() {
        mockMvc.perform(
            get("/products").param("userId", "1").param("page", "0").param("size", "30")
        ).andDo(MockMvcResultHandlers.print()).andExpect(status().isOk)
            .andExpect(jsonPath("$.data.pageNumber").value(0)).andExpect(jsonPath("$.data.pageSize").value(30))
            .andExpect(jsonPath("$.data.totalElements").value(2)).andExpect(jsonPath("$.data.totalPages").value(1))
            .andExpect(jsonPath("$.data.content[0].id").value("1"))
            .andExpect(jsonPath("$.data.content[0].writerId").value("1"))
            .andExpect(jsonPath("$.data.content[0].name").value("nintendo"))
            .andExpect(jsonPath("$.data.content[0].category").value("GAMES"))
            .andExpect(jsonPath("$.data.content[0].thumbnail").value("https://picsum.photos/200/300"))
            .andExpect(jsonPath("$.data.content[0].currentPrice").value("10000"))
            .andExpect(jsonPath("$.data.content[0].status").value("IN_PROGRESS"))
            .andExpect(jsonPath("$.data.content[0].startDate").value("2024-01-02T00:00:00"))
            .andExpect(jsonPath("$.data.content[0].endDate").value("2024-01-04T00:00:00"))
            .andExpect(jsonPath("$.data.content[0].isMine").value("true"))
    }

    @Test
    fun productDetailIntegrationTest() {
        mockMvc.perform(
            get("/products/1/detail").param("userId", "1")
        ).andDo(MockMvcResultHandlers.print()).andExpect(status().isOk).andExpect(jsonPath("$.data.id").value("1"))
            .andExpect(jsonPath("$.data.name").value("nintendo")).andExpect(jsonPath("$.data.writer.id").value("1"))
            .andExpect(jsonPath("$.data.writer.nickname").value("test"))
            .andExpect(jsonPath("$.data.writer.profileImg").value("https://drive.google.com/file/d/1R9EIOoEWWgPUhY6e-t4VFuqMgknl7rm8/view?usp=sharing"))
            .andExpect(jsonPath("$.data.name").value("nintendo")).andExpect(jsonPath("$.data.category").value("GAMES"))
            .andExpect(jsonPath("$.data.images.size()").value(2))
            .andExpect(jsonPath("$.data.images[0]").value("https://picsum.photos/200/300"))
            .andExpect(jsonPath("$.data.description").value("닌텐도 새 제품"))
            .andExpect(jsonPath("$.data.startPrice").value("5000"))
            .andExpect(jsonPath("$.data.currentPrice").value("10000"))
            .andExpect(jsonPath("$.data.status").value("IN_PROGRESS"))
            .andExpect(jsonPath("$.data.startDate").value("2024-01-02T00:00:00"))
            .andExpect(jsonPath("$.data.endDate").value("2024-01-04T00:00:00"))
            .andExpect(jsonPath("$.data.isMine").value("true"))
    }

    @Test
    fun postProductIntegrationTest() {
        // 이미지 파일 로드
        val imagePath = Paths.get("src/test/resources/img.png")
        val imageFile = MockMultipartFile(
            "images", "img.png", MediaType.IMAGE_PNG_VALUE, Files.readAllBytes(imagePath)
        )

        // JSON 데이터 생성
        val productReqDto = """
        {
            "title": "솔 타이틀",
            "description": "설명",
            "startPrice": 1000,
            "startDate": "2099-10-19T15:00:00",
            "endDate": "2099-10-23T15:00:00",
            "category": "BOOKS"
        }
    """.trimIndent()
        val productReqDtoFile = MockMultipartFile(
            "productReqDto", "productReqDto", MediaType.APPLICATION_JSON_VALUE, productReqDto.toByteArray()
        )

        // MockMvc 요청 작성 및 실행
        mockMvc.perform(
            MockMvcRequestBuilders.multipart("/products")
                .file(imageFile)
                .file(productReqDtoFile)
                .param("userId", "1")
                .contentType(MediaType.MULTIPART_FORM_DATA)
        ).andExpect(status().isOk)
            .andExpect(jsonPath("$.data.title").value("솔 타이틀"))
            .andExpect(jsonPath("$.data.writerId").value(1))
    }

    @Test
    fun updateProductIntegrationTest() {
        // 이미지 파일 로드
        val imagePath = Paths.get("src/test/resources/img_1.png")
        val imageFile = MockMultipartFile(
            "images", "img_1.png", MediaType.IMAGE_PNG_VALUE, Files.readAllBytes(imagePath)
        )

        // JSON 데이터 생성
        val productReqDto = """
        {
            "title": "수정된 제목",
            "description": "수정된 설명",
            "startPrice": 1500,
            "category": "GAMES"
        }
    """.trimIndent()
        val productReqDtoFile = MockMultipartFile(
            "productReqDto", "productReqDto", MediaType.APPLICATION_JSON_VALUE, productReqDto.toByteArray()
        )

        // MockMvc 요청 작성 및 실행
        mockMvc.perform(
            MockMvcRequestBuilders.multipart("/products/1")
                .file(imageFile)
                .file(productReqDtoFile)
                .param("userId", "1")
                .contentType(MediaType.MULTIPART_FORM_DATA)
        ).andExpect(status().isOk)
            .andExpect(jsonPath("$.data.title").value("수정된 제목"))
            .andExpect(jsonPath("$.data.writerId").value(1))
    }
}