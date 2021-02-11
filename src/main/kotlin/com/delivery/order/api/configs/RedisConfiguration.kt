package com.delivery.order.api.configs

import com.delivery.order.api.dto.OrderDetailDTO
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory
import org.springframework.data.redis.core.ReactiveRedisTemplate
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer
import org.springframework.data.redis.serializer.RedisSerializationContext
import org.springframework.data.redis.serializer.StringRedisSerializer

@Configuration
class RedisConfiguration {
    
    @Bean
    fun orderDetailsReactiveRedisTemplate(factory: ReactiveRedisConnectionFactory):
            ReactiveRedisTemplate<String, OrderDetailDTO> {
        
        val serializer: Jackson2JsonRedisSerializer<OrderDetailDTO> =
            Jackson2JsonRedisSerializer(OrderDetailDTO::class.java)
        serializer.setObjectMapper(jacksonObjectMapper())
        
        val builder =
            RedisSerializationContext.newSerializationContext<String, OrderDetailDTO>(StringRedisSerializer())
        
        val context = builder.value(serializer).build()
        
        return ReactiveRedisTemplate(factory, context);
        
    }
}
