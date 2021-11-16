package com.delivery.order.api.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.client.WebClient

@Configuration
class DefaultWebClientConfiguration {
    
    @Bean
    fun webClient(): WebClient {
        
        return WebClient.builder()
            .baseUrl("http://localhost:1080/")
            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .build()
    }
}
