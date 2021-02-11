package com.delivery.order.api.repository

import com.delivery.order.api.dto.OrderDetailDTO
import org.slf4j.Logger
import org.springframework.data.redis.core.ReactiveRedisTemplate
import org.springframework.stereotype.Repository
import reactor.core.publisher.Mono
import reactor.core.scheduler.Schedulers

@Repository
class OrderDetailsCacheRepository(
    private val redisTemplate: ReactiveRedisTemplate<String, OrderDetailDTO>,
    private val logger: Logger
) {
    
    fun get(key: String): Mono<OrderDetailDTO> {
        
        return redisTemplate.opsForValue().get(key)
            .doOnSubscribe { logger.info("Getting Order Details from cache. key=${key}") }
            .doOnSuccess { logger.info("Cache data found: $it") }
    }
    
    fun put(key: String, value: OrderDetailDTO): Mono<Boolean> {
        return redisTemplate.opsForValue().set(key, value)
            .doOnSubscribe { logger.info("Saving Order Details on cache. key=${key}") }
    }
}
