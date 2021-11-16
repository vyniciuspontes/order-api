package com.delivery.order.api.event

import org.slf4j.Logger
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toMono
import software.amazon.awssdk.services.sqs.SqsAsyncClient
import software.amazon.awssdk.services.sqs.model.Message

@Component
class CustomSQSListener(
    sqsAsyncClient: SqsAsyncClient,
    private val logger: Logger,
    @Value("\${aws.sqs.order.url}") val queueUrl: String
) :
    AbstractSqsConsumer(sqsAsyncClient, logger, queueUrl) {

    override fun consume(message: Message): Mono<Void> {

        return message.toMono().flatMap {
            logger.info("SQS message: ${it.body()}")
            this.acknowledge(it)
        }
    }
}