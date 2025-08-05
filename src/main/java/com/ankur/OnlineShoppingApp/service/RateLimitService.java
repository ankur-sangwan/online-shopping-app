package com.ankur.OnlineShoppingApp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class RateLimitService {
    private final RedisTemplate<String, Integer> redisTemplate;
    private static final int LIMIT = 20; // requests per minute
    private static final Duration WINDOW = Duration.ofMinutes(1);

    public RateLimitService(RedisTemplate<String, Integer> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public boolean isAllowed(String userKey) {
        String redisKey = "rate-limit:" + userKey;
        ValueOperations<String, Integer> ops = redisTemplate.opsForValue();

        Integer currentCount = ops.get(redisKey); // current count nikalo for the user

        if (currentCount == null) {
            // First request, set count = 1 and expiry
            ops.set(redisKey, 1, WINDOW);
            return true;
        }

        if (currentCount < LIMIT) {
            ops.increment(redisKey);
            return true;
        }

        return false;
    }
}
