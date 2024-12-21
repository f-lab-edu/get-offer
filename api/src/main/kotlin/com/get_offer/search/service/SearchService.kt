package com.get_offer.search.service

import co.elastic.clients.elasticsearch.ElasticsearchClient
import co.elastic.clients.elasticsearch.core.SearchResponse
import com.get_offer.common.elasticsearch.IndexName
import com.get_offer.product.domain.Product
import com.get_offer.product.service.ProductListDto
import org.springframework.stereotype.Service

@Service
class SearchService(
    private val esClient: ElasticsearchClient,
) {
    fun searchByProductName(productName: String, userId: Long): List<ProductListDto> {
        val searchResponse: SearchResponse<Product> = esClient.search(
            { req ->
                req.index(IndexName.PRODUCTS.getValue())
                    .query {
                        it.match { match ->
                            match.field("title")
                                .query(productName)
                        }
                    }
            },
            Product::class.java
        )

        return searchResponse.hits().hits()
            .mapNotNull { it.source() }
            .map { ProductListDto.of(it, userId) }
    }
}