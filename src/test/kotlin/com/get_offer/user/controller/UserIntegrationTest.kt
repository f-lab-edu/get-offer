package com.get_offer.user.controller

import com.get_offer.login.jwt.TokenService
import java.time.LocalDateTime
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.jdbc.Sql
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

@SpringBootTest
@Sql("classpath:test_data.sql")
@AutoConfigureMockMvc
class UserIntegrationTest(
    @Autowired val mockMvc: MockMvc,
    @Autowired val tokenService: TokenService,
) {

    lateinit var token: String

    @BeforeEach
    fun setUp() {
        token = tokenService.issue("1", LocalDateTime.now().plusDays(1))
    }

    @Test
    fun userInfoIntegrationTest() {
        mockMvc.perform(
            MockMvcRequestBuilders.get("/users").param("userId", "1").header("Authorization", token)
        ).andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$.data.nickname").value("test")).andExpect(
                MockMvcResultMatchers.jsonPath("$.data.profileImage")
                    .value("https://drive.google.com/file/d/1R9EIOoEWWgPUhY6e-t4VFuqMgknl7rm8/view?usp=sharing")
            ).andExpect(MockMvcResultMatchers.jsonPath("$.data.id").value(1))
    }
}