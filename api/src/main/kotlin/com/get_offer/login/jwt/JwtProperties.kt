package com.get_offer.login.jwt

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "jwt")
data class JwtProperties(
    var key: String,
    var accessTokenExpiration: Long,
)
