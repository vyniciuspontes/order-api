package com.delivery.order.api.service

import com.delivery.order.api.dto.DeliveryDTO
import com.delivery.order.api.dto.OrderDTO
import com.delivery.order.api.dto.OrderDetailDTO
import com.delivery.order.api.dto.PlaceDTO
import com.delivery.order.api.repository.OrderDetailsCacheRepository
import com.delivery.order.api.service.DeliveryService
import com.delivery.order.api.service.OrderService
import com.delivery.order.api.service.PlaceService
import org.slf4j.Logger
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.util.function.Tuple2
import reactor.util.function.Tuples

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
            .map(this::mapToDetails)
            .doOnSubscribe{ logger.info("Data not found on cache. Resolving from API. orderId=${orderId}") }
            .doOnNext { logger.info("Resolved Order Details from API. orderId=${orderId}") }
            .doOnSuccess {
                orderDetailsCacheRepository.put(orderId, it).subscribe()
            }
    }
    
    private fun mapToDetails(it: Tuple2<Tuple2<OrderDTO, PlaceDTO>, DeliveryDTO>): OrderDetailDTO {
        val orderDTO = it.t1.t1
        val placeDTO = it.t1.t2
        val deliveryDTO = it.t2
        
        return OrderDetailDTO(orderDTO.id,
            orderDTO.items,
            orderDTO.totalValue,
            deliveryDTO.driverFullName,
            deliveryDTO.destinationAddress,
            deliveryDTO.startDateTime,
            deliveryDTO.endDateTime,
            deliveryDTO.coordinates,
            placeDTO.name
        )
    }
    
    private fun getDelivery(orderId: String) = deliveryService.getDeliveryByOrderId(orderId)
    
    
    private fun getOrderAndPlace(orderId: String): Mono<Tuple2<OrderDTO, PlaceDTO>> =
        orderService.getOrder(orderId).flatMap { order ->
            placeService
                .getPlace(order.placeId)
                .map { place -> Tuples.of(order, place) }
        }
}
