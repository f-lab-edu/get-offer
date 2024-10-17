package com.get_offer.auction.controller

import org.junit.jupiter.api.Assertions.*
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
class AuctionIntegrationTest(
    @Autowired val mockMvc: MockMvc,
) {
    @Test
    fun auctionSellIntegrationTest() {
        mockMvc.perform(
            MockMvcRequestBuilders.get("/auctions/sellHistory").param("userId", "1")
        ).andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$.data.size()").value(3))
            .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].id").value("1"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].writerId").value("1"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].title").value("nintendo"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].category").value("GAMES"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].thumbnail").value("https://picsum.photos/200/300"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.data[1].id").value("2"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.data[1].writerId").value("1"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.data[1].title").value("gucci belt"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.data[1].category").value("CLOTHES"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.data[1].thumbnail").value("https://picsum.photos/200/300"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.data[2].id").value("3"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.data[2].writerId").value("1"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.data[2].title").value("ikea chair"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.data[2].category").value("FURNITURE"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.data[2].thumbnail").value("https://picsum.photos/200/300"))
    }

    @Test
    fun auctionBuyIntegrationTest() {
        mockMvc.perform(
            MockMvcRequestBuilders.get("/auctions/buyHistory").param("userId", "2")
        ).andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$.data.size()").value(1))
            .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].productId").value("1"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].writerId").value("1"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].buyerId").value("2"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].auctionId").value("1"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].title").value("nintendo"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].category").value("GAMES"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].thumbnail").value("https://picsum.photos/200/300"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].finalPrice").value("10000"))
    }

    @Test
    fun getSoldAuctionDetailIntegrationTest() {
        mockMvc.perform(
            MockMvcRequestBuilders.get("/auctions/1/sold").param("userId", "1")
        ).andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$.data.auctionId").value("1"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.data.auctionStatus").value("COMPLETED"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.data.product.id").value("1"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.data.product.writerId").value("1"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.data.product.name").value("nintendo"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.data.buyer.id").value("2"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.data.buyer.nickname").value("test2"))
    }

    @Test
    fun getBoughtAuctionDetailIntegrationTest() {
        mockMvc.perform(
            MockMvcRequestBuilders.get("/auctions/1/bought").param("userId", "2")
        ).andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$.data.auctionId").value("1"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.data.auctionStatus").value("COMPLETED"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.data.product.id").value("1"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.data.product.writerId").value("1"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.data.product.name").value("nintendo"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.data.seller.id").value("1"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.data.seller.nickname").value("test"))
    }
}