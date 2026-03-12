package com.example.gestaoPetApi.config;

import com.example.gestaoPetApi.dto.PetResponseDto;
import com.example.gestaoPetApi.dto.TutorResponseDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;
import java.util.List;

@Configuration
@EnableCaching
public class RedisConfig {

    private final ObjectMapper mapper = new ObjectMapper();

    @Bean
    public RedisCacheManager cacheManager(RedisConnectionFactory factory) {
        return RedisCacheManager.builder(factory)
                .withCacheConfiguration("tutor",
                        cacheConfig(new Jackson2JsonRedisSerializer<>(mapper, TutorResponseDto.class)))
                .withCacheConfiguration("tutorList",
                        cacheConfig(new Jackson2JsonRedisSerializer<>(mapper, listOf(TutorResponseDto.class))))
                .withCacheConfiguration("pets",
                        cacheConfig(new Jackson2JsonRedisSerializer<>(mapper, PetResponseDto.class)))
                .withCacheConfiguration("petsList",
                        cacheConfig(new Jackson2JsonRedisSerializer<>(mapper, listOf(PetResponseDto.class))))
                .build();
    }

    private RedisCacheConfiguration cacheConfig(Jackson2JsonRedisSerializer<?> serializer) {
        return RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofMinutes(30))
                .disableCachingNullValues()
                .serializeKeysWith(
                        RedisSerializationContext.SerializationPair
                                .fromSerializer(new StringRedisSerializer()))
                .serializeValuesWith(
                        RedisSerializationContext.SerializationPair
                                .fromSerializer(serializer));
    }

    private CollectionType listOf(Class<?> elementType) {
        return mapper.getTypeFactory().constructCollectionType(List.class, elementType);
    }
}