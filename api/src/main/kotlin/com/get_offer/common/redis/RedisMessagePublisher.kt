package com.get_offer.common.redis

import com.fasterxml.jackson.databind.ObjectMapper
import com.get_offer.chat.domain.ChatMessage
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Component

@Component
class RedisMessagePublisher(
    private val redisTemplate: RedisTemplate<String, Any>,
    private val objectMapper: ObjectMapper,
) {
    fun publish(topic: String, message: ChatMessage) {
        redisTemplate.convertAndSend(topic, objectMapper.writeValueAsString(message))
    }
}
