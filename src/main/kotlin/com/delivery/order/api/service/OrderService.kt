package com.delivery.order.api.service

import com.delivery.order.api.dto.OrderDTO
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono

@Service
class OrderService(private val webClient: WebClient) {
    
    fun getOrder(orderId: String): Mono<OrderDTO> {
        
        return webClient
            .get()
            .uri("/orders/123")
            .retrieve()
            .bodyToMono(OrderDTO::class.java)
            //.publishOn(Schedulers.parallel())
    }
}
