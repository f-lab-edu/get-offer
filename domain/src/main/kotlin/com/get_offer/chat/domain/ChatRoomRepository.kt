package com.get_offer.chat.domain

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ChatRoomRepository : JpaRepository<ChatRoom, Long> {
    // 개인 채팅
    fun findByRoomTypeAndSenderIdAndProductId(roomType: ChatRoomType, buyerId: Long, itemId: Long): ChatRoom?

    // 그룹 채팅
    fun findByRoomTypeAndProductId(roomType: ChatRoomType, productId: Long): ChatRoom?
}