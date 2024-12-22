package com.get_offer.chat.service

import com.get_offer.chat.domain.ChatMessageRepository
import com.get_offer.chat.domain.ChatRoom
import com.get_offer.chat.domain.ChatRoomRepository
import com.get_offer.chat.domain.ChatRoomType
import com.get_offer.common.exception.ApiException
import com.get_offer.common.exception.ExceptionCode
import com.get_offer.product.domain.ProductRepository
import org.apache.coyote.BadRequestException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ChatRoomService(
    private val chatRoomRepository: ChatRoomRepository,
    private val chatMessageRepository: ChatMessageRepository,
    private val productRepository: ProductRepository,
) {

    @Transactional
    fun startChat(requesterId: Long, productId: Long): ChatRoomDto {
        // 기존 채팅방 여부 확인
        val existingRoom = chatRoomRepository.findByRoomTypeAndProductId(ChatRoomType.GROUP, productId)
        if (existingRoom != null) return ChatRoomDto(existingRoom.id)

        val sellerId = getSellerIdFromProductId(productId)
        if (sellerId == requesterId) throw BadRequestException("채팅 상대방이 본인일 수 없습니다.")

        // 새 채팅방 생성
        val chatRoom =
            chatRoomRepository.save(ChatRoom(roomType = ChatRoomType.GROUP, sellerId = sellerId, productId = productId))
        return ChatRoomDto(chatRoom.id)
    }

    fun getMessages(roomId: Long): List<ChatMessageDto> {
        return chatMessageRepository.findByChatRoomId(roomId)
            .map { ChatMessageDto.of(it) }
    }

    private fun getSellerIdFromProductId(productId: Long): Long {
        return productRepository.findById(productId)
            .orElseThrow { ApiException(ExceptionCode.NOTFOUND, "$productId 의 상품은 존재하지 않습니다.") }
            .writerId
    }
}
