package com.delivery.order.api.dto

data class DeliveryDTO(
    
    val id: String,
    val orderId: String,
    val driverFullName: String,
    val destinationAddress: String,
    val startDateTime: String,
    val endDateTime: String,
    val coordinates: List<DeliveryCoordinateDTO>
)
