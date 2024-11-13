package com.em.demo.service;

import io.github.bucket4j.Bucket;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class RateLimiterService {

    private static Bucket bucket = Bucket.builder()
            .addLimit(limit -> limit.capacity(3).refillGreedy(10, Duration.ofMinutes(1)))
            .build();

    public boolean tryConsume() {
        return bucket.tryConsume(1); // Try to consume 1 token
    }
}
