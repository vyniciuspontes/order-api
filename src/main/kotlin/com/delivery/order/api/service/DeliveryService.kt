package com.delivery.order.api.service

import com.delivery.order.api.dto.DeliveryDTO
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono

@Service
class DeliveryService(private val webClient: WebClient) {
    
    fun getDeliveryByOrderId(orderId: String): Mono<DeliveryDTO> {
        
        return webClient
            .get()
            .uri("/deliveries/123")
            .retrieve()
            .bodyToMono(DeliveryDTO::class.java)
        //.publishOn(Schedulers.parallel())
    }
}
