package com.delivery.order.api.controller

import com.delivery.order.api.dto.OrderDetailDTO
import com.delivery.order.api.service.OrderDetailService
import org.slf4j.Logger
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@RestController
class OrderDetailsController(
    private val logger: Logger,
    private val orderDetailService: OrderDetailService
) {

    @GetMapping(path = ["/orders/{orderId}"])
    @ResponseStatus(HttpStatus.OK)
    fun getOrderDetails(@PathVariable orderId: String): Mono<OrderDetailDTO> {
        
        return orderDetailService
            .getOrderDetails(orderId)
            .doOnSubscribe { logger.info("Processing /orders/${orderId} request") }
            .doOnSuccess { logger.info("Result data: $it") }
    }
    
    @GetMapping(path = ["/orders"])
    @ResponseStatus(HttpStatus.OK)
    fun getOrdersDetails(@RequestParam("id") ordersIds: List<String>): Flux<OrderDetailDTO> {
        
        return orderDetailService.getOrdersDetails(ordersIds)
            .doOnSubscribe { logger.info("Processing /orders/${ordersIds} request") }
            .doOnNext { logger.info("Result data: $it") }
    }
}
