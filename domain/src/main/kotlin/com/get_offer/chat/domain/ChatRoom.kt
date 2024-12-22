package com.get_offer.chat.domain

import com.get_offer.common.AuditingTimeEntity
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "CHAT_ROOMS")
class ChatRoom(

    val senderId: Long? = null,

    val sellerId: Long,

    val productId: Long,

    val roomType: ChatRoomType = ChatRoomType.PERSONAL,

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
) : AuditingTimeEntity()