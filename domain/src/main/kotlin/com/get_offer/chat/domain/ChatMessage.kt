package com.get_offer.chat.domain

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.LocalDateTime

@Entity
@Table(name = "CHAT_MESSAGES")
class ChatMessage(
    var chatRoomId: Long,

    val senderId: Long,

    val content: String,

    val timestamp: LocalDateTime = LocalDateTime.now(),

    val type: String = "TEXT",

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0
) {
    override fun toString(): String {
        return "${chatRoomId} - ${senderId} - ${content} ${timestamp} ${type}"
    }
}