package com.delivery.order.api.service

import com.delivery.order.api.dto.DeliveryDTO
import com.delivery.order.api.dto.OrderDetailDTO
import com.delivery.order.api.repository.OrderDetailsCacheRepository
import org.slf4j.Logger
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.util.function.Tuple2

@Service
class OrderDetailService(
    private val orderService: OrderService,
    private val deliveryService: DeliveryService,
    private val placeService: PlaceService,
    private val orderDetailsCacheRepository: OrderDetailsCacheRepository,
    private val logger: Logger
) {

    fun getOrdersDetails(ordersId: List<String>): Flux<OrderDetailDTO> {

        return Flux.fromIterable(ordersId)
            .flatMap(this::getOrderDetails)
    }

    fun getOrderDetails(orderId: String): Mono<OrderDetailDTO> {

        return orderDetailsCacheRepository.get(orderId)
            .switchIfEmpty(resolveOrderDetails(orderId))
    }

    private fun resolveOrderDetails(orderId: String): Mono<OrderDetailDTO> {
        return getOrderAndPlace(orderId)
            .zipWith(getDelivery(orderId))
            .map(this::toOrderDetail)
            .flatMap {
                orderDetailsCacheRepository.put(orderId, it)
                    .thenReturn(it)
            }
            .doOnSubscribe { logger.info("Data not found on cache. Resolving from API. orderId=${orderId}") }
            .doOnNext { logger.info("Resolved Order Details from API. orderId=${orderId}") }
    }

    private fun toOrderDetail(tuple: Tuple2<OrderDetailDTO.Builder, DeliveryDTO>): OrderDetailDTO {
        val builder = tuple.t1
        val deliveryDTO = tuple.t2

        return builder.delivery(deliveryDTO).build()
    }

    private fun getDelivery(orderId: String) = deliveryService.getDeliveryByOrderId(orderId)

    private fun getOrderAndPlace(orderId: String): Mono<OrderDetailDTO.Builder> =
        orderService.getOrder(orderId).flatMap { order ->
            placeService
                .getPlace(order.placeId)
                .map { place -> OrderDetailDTO.Builder().order(order).place(place) }
        }
}
