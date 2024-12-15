package com.get_offer.common.redis

import org.redisson.Redisson
import org.redisson.api.RedissonClient
import org.redisson.config.Config
import org.redisson.spring.data.connection.RedissonConnectionFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class RedissonConfig(
    @Value("\${spring.data.redis.host}") private val host: String,
    @Value("\${spring.data.redis.port}") private val port: String
) {
    private val REDISSION_HOST_PREFIX: String = "redis://"

    @Bean
    fun redissonClient(): RedissonClient {
        val config = Config()
        config.useSingleServer()
            .setAddress("$REDISSION_HOST_PREFIX$host:$port")
            .setPassword("admin")
        return Redisson.create(config)
    }

    @Bean
    fun redissonConnectionFactory(redisson: RedissonClient): RedissonConnectionFactory {
        return RedissonConnectionFactory(redisson)
    }
}

