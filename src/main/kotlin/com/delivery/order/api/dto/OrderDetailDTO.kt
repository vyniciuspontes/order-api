package com.delivery.order.api.dto

data class OrderDetailDTO(
    val id: String,
    val items: List<OrderItemDTO>,
    val totalValue: Double,
    val driverFullName: String,
    val destinationAddress: String,
    val startDelivery: String,
    val endDelivery: String,
    val coordinates: List<DeliveryCoordinateDTO>,
    val placeName: String
)
