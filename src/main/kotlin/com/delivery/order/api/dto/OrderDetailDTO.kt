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
) {

    class Builder {
        private var orderDTO: OrderDTO? = null
        private var deliveryDTO: DeliveryDTO? = null
        private var placeDTO: PlaceDTO? = null

        fun order(order: OrderDTO) = apply { this.orderDTO = order }

        fun delivery(delivery: DeliveryDTO) = apply { this.deliveryDTO = delivery }

        fun place(place: PlaceDTO) = apply { this.placeDTO = place }

        fun build() = OrderDetailDTO(
            orderDTO!!.id,
            orderDTO!!.items,
            orderDTO!!.totalValue,
            deliveryDTO!!.driverFullName,
            deliveryDTO!!.destinationAddress,
            deliveryDTO!!.startDateTime,
            deliveryDTO!!.endDateTime,
            deliveryDTO!!.coordinates,
            placeDTO!!.name
        )
    }
}
