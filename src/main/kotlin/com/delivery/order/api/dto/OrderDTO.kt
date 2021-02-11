package com.delivery.order.api.dto

data class OrderDTO(
    
    val id: String,
    val customerId: String,
    val placeId: String,
    val items: List<OrderItemDTO>,
    val totalValue: Double
)
