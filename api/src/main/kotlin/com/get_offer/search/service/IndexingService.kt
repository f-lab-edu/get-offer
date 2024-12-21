package com.get_offer.search.service

import co.elastic.clients.elasticsearch.ElasticsearchClient
import co.elastic.clients.elasticsearch.core.BulkRequest
import co.elastic.clients.elasticsearch.core.BulkResponse
import com.get_offer.common.elasticsearch.IndexName
import com.get_offer.product.domain.Product
import com.get_offer.product.domain.ProductRepository
import org.springframework.stereotype.Service

@Service
class IndexingService(
    private val esClient: ElasticsearchClient,
    private val productRepository: ProductRepository,
) {

    fun indexProduct(): IndexResDto {
        val productList = productRepository.findAll()
        val result = indexProductToEs(productList)

        return IndexResDto(
            success = !result.errors(),
            count = if (!result.errors()) result.items().size else null
        )
    }

    private fun indexProductToEs(productList: List<Product>): BulkResponse {
        val br: BulkRequest.Builder = BulkRequest.Builder()

        productList.forEach { product ->
            br.operations { op ->
                op.index { idx ->
                    idx.index(IndexName.PRODUCTS.getValue())
                        .id(product.id.toString())
                        .document(product)
                }
            }
        }
        return esClient.bulk(br.build())
    }
}