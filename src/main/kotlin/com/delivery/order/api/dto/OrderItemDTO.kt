package com.delivery.order.api.dto

data class OrderItemDTO(
    
    val id: String,
    val name: String,
    val quantity: Int,
    val value: Double
)
