package com.get_offer.chat.service

import com.get_offer.chat.domain.ChatMessage
import com.get_offer.chat.domain.ChatMessageRepository
import com.get_offer.chat.domain.ChatRoom
import com.get_offer.chat.domain.ChatRoomRepository
import com.get_offer.common.redis.RedisMessagePublisher
import com.get_offer.common.redis.RedisMessageSubscriber
import com.get_offer.product.domain.Product
import jakarta.annotation.PostConstruct
import org.springframework.data.redis.listener.ChannelTopic
import org.springframework.data.redis.listener.RedisMessageListenerContainer
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class GroupChatRoomService(
    private val chatRoomRepository: ChatRoomRepository,
    private val chatMessageRepository: ChatMessageRepository,
    private val redisMessagePublisher: RedisMessagePublisher,
    private val redisMessageListenerContainer: RedisMessageListenerContainer,
    private val redisMessageSubscriber: RedisMessageSubscriber,
) {
    @PostConstruct
    fun init() {
        chatRoomRepository.findAll()
            .forEach {
                subscribeChatRoom(it.productId)
            }
    }

    // redis message DB에 저장
    fun processMessage(message: ChatMessage) {
        val topic = "/topic/group-chat/${message.chatRoomId}"
        redisMessagePublisher.publish(topic, message)
        chatMessageRepository.save(message)
    }

    @Transactional
    fun createGroupChatRoom(product: Product) {
        // db에 채팅방 생성
        chatRoomRepository.save(ChatRoom(sellerId = product.writerId, productId = product.id))

        subscribeChatRoom(product.id)
    }

    private fun subscribeChatRoom(productId: Long) {
        redisMessageListenerContainer.addMessageListener(
            redisMessageSubscriber,
            ChannelTopic("/topic/group-chat/$productId")
        )
    }
}