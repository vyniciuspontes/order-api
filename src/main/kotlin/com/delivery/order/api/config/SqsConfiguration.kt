package com.delivery.order.api.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.sqs.SqsAsyncClient
import software.amazon.awssdk.services.sqs.SqsAsyncClientBuilder
import java.net.URI

@Configuration
class SqsConfiguration {

    @Value("\${aws.sqs.endpoint}")
    lateinit var endpoint: String

    @Value("\${aws.region}")
    lateinit var region: String

    @Bean
    fun sqsAsyncClient(): SqsAsyncClient {
        val builder: SqsAsyncClientBuilder = SqsAsyncClient.builder()

        builder.credentialsProvider(DefaultCredentialsProvider.builder().build())

        if (region.isNotBlank()) {
            builder.region(Region.of(region))
        }

        if (endpoint.isNotBlank()) {
            builder.endpointOverride(URI.create(endpoint))
        }

        return builder.build()
    }

}
