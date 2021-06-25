package com.delivery.order.api.service

import com.delivery.order.api.dto.PlaceDTO
import org.slf4j.Logger
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono
import reactor.core.scheduler.Schedulers

@Service
class PlaceService(private val logger: Logger, private val webClient: WebClient) {
    
    fun getPlace(placeId: String): Mono<PlaceDTO> {
        
        return webClient
            .get()
            .uri("/places/123")
            .retrieve()
            .bodyToMono(PlaceDTO::class.java)
            .publishOn(Schedulers.parallel())
            .doOnNext { logger.info("Place Request") }
    }
}
