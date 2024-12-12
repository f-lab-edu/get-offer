package com.get_offer.common.redis

import com.fasterxml.jackson.databind.ObjectMapper
import com.get_offer.chat.domain.ChatMessage
import org.springframework.data.redis.connection.Message
import org.springframework.data.redis.connection.MessageListener
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.stereotype.Component

@Component
class RedisMessageSubscriber(
    private val redisTemplate: RedisTemplate<String, Any>,
    private val simpMessagingTemplate: SimpMessagingTemplate,
) : MessageListener {
    private val objectMapper = ObjectMapper()

    override fun onMessage(message: Message, pattern: ByteArray?) {
        try {
            val publishedMessage = redisTemplate.stringSerializer.deserialize(message.body)
            if (publishedMessage != null) {
                val roomMessage = objectMapper.readValue(publishedMessage, ChatMessage::class.java)

                simpMessagingTemplate.convertAndSend("/topic/group-chat/${roomMessage.chatRoomId}", roomMessage)
            } else {
                println("Received null message from Redis")
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
