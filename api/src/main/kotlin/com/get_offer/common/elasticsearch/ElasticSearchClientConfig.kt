package com.get_offer.common.elasticsearch

import co.elastic.clients.elasticsearch.ElasticsearchClient
import co.elastic.clients.json.jackson.JacksonJsonpMapper
import co.elastic.clients.transport.rest_client.RestClientTransport
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import org.apache.http.HttpHost
import org.apache.http.HttpResponse
import org.apache.http.HttpResponseInterceptor
import org.apache.http.auth.AuthScope
import org.apache.http.auth.UsernamePasswordCredentials
import org.apache.http.client.CredentialsProvider
import org.apache.http.impl.client.BasicCredentialsProvider
import org.apache.http.message.BasicHeader
import org.apache.http.protocol.HttpContext
import org.elasticsearch.client.RestClient
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


@Configuration
class ElasticSearchClientConfig(
    @Value("\${spring.data.elasticsearch.host}") private val host: String,
    @Value("\${spring.data.elasticsearch.port}") private val port: String,
    @Value("\${spring.data.elasticsearch.username}") private val username: String,
    @Value("\${spring.data.elasticsearch.password}") private val password: String,
) {
    private val credential: CredentialsProvider = BasicCredentialsProvider()

    @Bean
    fun getElasticSearchClient(): ElasticsearchClient {
        credential.setCredentials(AuthScope.ANY, UsernamePasswordCredentials(username, password))

        val restClient = RestClient.builder(HttpHost.create("http://$host:$port"))
            .setHttpClientConfigCallback {
                it.setDefaultCredentialsProvider(credential)
                it.setDefaultHeaders(listOf(BasicHeader("Content-Type", "application/json")))
                it.addInterceptorLast(HttpResponseInterceptor { response: HttpResponse, _: HttpContext ->
                    response.addHeader(
                        "X-Elastic-Product",
                        "Elasticsearch"
                    )
                })
            }
            .build()

        val objectMapper =
            ObjectMapper().registerModule(JavaTimeModule()).disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
        val transport = RestClientTransport(restClient, JacksonJsonpMapper(objectMapper))

        return ElasticsearchClient(transport)
    }
}
