package com.delivery.order.api.event

import org.slf4j.Logger
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.core.scheduler.Schedulers
import software.amazon.awssdk.services.sqs.SqsAsyncClient
import software.amazon.awssdk.services.sqs.model.DeleteMessageRequest
import software.amazon.awssdk.services.sqs.model.Message
import software.amazon.awssdk.services.sqs.model.ReceiveMessageRequest
import javax.annotation.PostConstruct

abstract class AbstractSqsConsumer(
    sqsAsyncClient: SqsAsyncClient,
    private val logger: Logger,
    private val queueUrl: String
) : SqsConsumer {

    private val sqsClientWrapper: SqsClientWrapper = SqsClientWrapper(sqsAsyncClient, logger)

    abstract override fun consume(message: Message): Mono<Void>

    @PostConstruct
    fun startListener() {

        sqsClientWrapper.getMessages(queueUrl)
            .subscribeOn(Schedulers.boundedElastic())
            .subscribe { message ->
                this.consume(message)
                    .onErrorResume {
                        logger.warn("Although an exception thrown, Resuming SQS consumption. exception=${it.message}")
                        Mono.empty()
                    }
                    .subscribe()
            }
    }

    override fun acknowledge(message: Message): Mono<Void> {
        return sqsClientWrapper.acknowledge(message, queueUrl)
    }
}

private class SqsClientWrapper(
    private val sqsAsyncClient: SqsAsyncClient,
    private val logger: Logger
) {

    fun getMessages(queueUrl: String): Flux<Message> {
        return Mono.fromFuture {
            sqsAsyncClient.receiveMessage(
                ReceiveMessageRequest.builder()
                    .queueUrl(queueUrl)
                    .build()
            )
        }
            .flatMapIterable { it.messages() }
            .doOnError { logger.error("Failed to get messages from sqs. exception=${it.message}") }
            .repeat()
            .retry()
    }

    fun acknowledge(message: Message, queueUrl: String): Mono<Void> {
        return Mono.fromFuture {
            sqsAsyncClient.deleteMessage(
                DeleteMessageRequest.builder().queueUrl(queueUrl).receiptHandle(message.receiptHandle())
                    .build()
            )
        }
            .doOnError { logger.error("Failed to acknowledge sqs message. exception=${it.message}") }
            .then()
    }
}
