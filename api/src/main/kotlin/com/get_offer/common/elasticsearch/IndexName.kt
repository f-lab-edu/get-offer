package com.get_offer.common.elasticsearch

enum class IndexName(
    private val value: String
) {
    PRODUCTS("products"),
    AUCTIONS("auctions");

    fun getValue(): String {
        return value
    }
}