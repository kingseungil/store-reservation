package com.zb.config;

import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectMapper.DefaultTyping;
import java.time.Duration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

    @Value("${spring.data.redis.host}")
    private String host;

    @Value("${spring.data.redis.port}")
    private String port;

//    @Bean
//    public RedisTemplate<String, Object> redisTemplate() {
//        RedisTemplate<String, Object> template = new RedisTemplate<>();
//        template.setConnectionFactory(redisConnectionFactory());
//        template.setKeySerializer(new StringRedisSerializer());
//
//        // 직렬화할 클래스 설정
//
//        return template;
//    }

    @Bean
    public Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer() {
        ObjectMapper om = new ObjectMapper();
        om.activateDefaultTyping(
          om.getPolymorphicTypeValidator(), // 역직렬화 시 타입을 검증할 수 있는 기능
          DefaultTyping.NON_FINAL, // 역직렬화 시 타입 정보를 포함하도록 설정
          As.WRAPPER_ARRAY // 역직렬화 시 타입 정보를 배열 형태로 포함하도록 설정
        );

        return new Jackson2JsonRedisSerializer<>(om, Object.class);
    }
    
    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        RedisStandaloneConfiguration conf = new RedisStandaloneConfiguration();
        conf.setHostName(host);
        conf.setPort(Integer.parseInt(port));
        return new LettuceConnectionFactory(conf);
    }

    @Bean
    public RedisCacheManager cacheManager(RedisConnectionFactory redisConnectionFactory) {
        RedisCacheConfiguration conf = RedisCacheConfiguration.defaultCacheConfig()
                                                              .entryTtl(
                                                                Duration.ofDays(1)) // 캐시의 기본 유효시간 설정 (1일)
                                                              .serializeKeysWith(
                                                                RedisSerializationContext.SerializationPair
                                                                  .fromSerializer(
                                                                    new StringRedisSerializer())) // redis 캐시 데이터 저장시 key에 대한 직렬화 설정
                                                              .serializeValuesWith(
                                                                RedisSerializationContext.SerializationPair
                                                                  .fromSerializer(
                                                                    jackson2JsonRedisSerializer())); // redis 캐시 데이터 저장시 value에 대한 직렬화 설정

        return RedisCacheManager.builder(RedisCacheWriter.lockingRedisCacheWriter(redisConnectionFactory))
                                .cacheDefaults(conf)
                                .build();

    }
}
