package com.delivery.order.api.event

import reactor.core.publisher.Mono
import software.amazon.awssdk.services.sqs.model.Message

interface SqsConsumer {
    
    fun consume(message: Message): Mono<Void>
    
    fun acknowledge(message: Message): Mono<Void>
}
