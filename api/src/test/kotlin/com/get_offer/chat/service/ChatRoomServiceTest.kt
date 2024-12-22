package com.get_offer.chat.service

import com.get_offer.TestFixtures
import com.get_offer.chat.domain.ChatMessageRepository
import com.get_offer.chat.domain.ChatRoom
import com.get_offer.chat.domain.ChatRoomRepository
import com.get_offer.chat.domain.ChatRoomType
import com.get_offer.product.domain.ProductRepository
import java.util.*
import org.apache.coyote.BadRequestException
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.any
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`

class ChatRoomServiceTest {

    class ChatServiceTest {
        private lateinit var mockChatRoomRepository: ChatRoomRepository
        private lateinit var mockChatMessageRepository: ChatMessageRepository
        private lateinit var mockProductRepository: ProductRepository
        private lateinit var chatRoomService: ChatRoomService

        @BeforeEach
        fun setUp() {
            mockChatRoomRepository = mock(ChatRoomRepository::class.java)
            mockChatMessageRepository = mock(ChatMessageRepository::class.java)
            mockProductRepository = mock(ProductRepository::class.java)
            chatRoomService = ChatRoomService(mockChatRoomRepository, mockChatMessageRepository, mockProductRepository)
        }

        @Test
        fun returnExistingChatRoom() {
            // given
            val requesterId = 1L
            val productId = 100L
            val existingChatRoom = ChatRoom(id = 10L, senderId = requesterId, sellerId = 2L, productId = productId)

            `when`(
                mockChatRoomRepository.findByRoomTypeAndSenderIdAndProductId(
                    ChatRoomType.PERSONAL, requesterId,
                    productId
                )
            ).thenReturn(
                existingChatRoom
            )

            // when
            val result = chatRoomService.startChat(requesterId, productId)

            // then
            assertEquals(existingChatRoom.id, result.roomId)
        }

        @Test
        fun throwExceptionWhenSellerEqualsRequester() {
            // given
            val requesterId = 1L
            val productId = 100L

            `when`(
                mockChatRoomRepository.findByRoomTypeAndSenderIdAndProductId(
                    ChatRoomType.PERSONAL,
                    requesterId,
                    productId
                )
            ).thenReturn(null)
            `when`(mockProductRepository.findById(productId)).thenReturn(
                Optional.of(
                    TestFixtures.createProductWait(
                        requesterId
                    )
                )
            )
            // when & then
            val exception = assertThrows(BadRequestException::class.java) {
                chatRoomService.startChat(requesterId, productId)
            }
            assertEquals("채팅 상대방이 본인일 수 없습니다.", exception.message)
        }

        @Test
        fun createNewChatRoom() {
            // given
            val requesterId = 1L
            val productId = 100L
            val sellerId = 2L
            val newChatRoom = ChatRoom(id = 20L, senderId = requesterId, sellerId = sellerId, productId = productId)

            `when`(
                mockChatRoomRepository.findByRoomTypeAndSenderIdAndProductId(
                    ChatRoomType.PERSONAL,
                    requesterId,
                    productId
                )
            ).thenReturn(null)
            `when`(mockProductRepository.findById(productId)).thenReturn(
                Optional.of(
                    TestFixtures.createProductWait(
                        sellerId
                    )
                )
            )
            `when`(mockChatRoomRepository.save(any())).thenReturn(newChatRoom)

            // when
            val result = chatRoomService.startChat(requesterId, productId)

            // then
            assertEquals(newChatRoom.id, result.roomId)
        }
    }

}