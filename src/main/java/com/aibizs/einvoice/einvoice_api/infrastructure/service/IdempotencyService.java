package com.aibizs.einvoice.einvoice_api.infrastructure.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@RequiredArgsConstructor
@Service
public class IdempotencyService {

    private final StringRedisTemplate redis;

    public enum Status {NEW, IN_PROGRESS, DONE}

    @Value("${app.idempotency.enabled:true}")
    private boolean enabled;
    @Value("${app.idempotency.key-prefix:idem:}")
    private String prefix;
    @Value("${app.idempotency.in-progress-ttl:60s}")
    private Duration inProgressTtl;
    @Value("${app.idempotency.result-ttl:24h}")
    private Duration resultTtl;


    /**
     * NEW = you may process it. IN_PROGRESS / DONE = duplicate.
     */
    public Status start(String key) {
        if (!enabled) return Status.NEW;
        String redisKey = prefix + key;
        boolean claimed = Boolean.TRUE.equals(redis.opsForValue().setIfAbsent(redisKey, "IN_PROGRESS", inProgressTtl));
        if (claimed) return Status.NEW;
        return "DONE".equals(redis.opsForValue().get(redisKey)) ? Status.DONE : Status.IN_PROGRESS;
    }

    /**
     * Processing succeeded: remember it for the long TTL.
     */
    public void complete(String key) {
        if (enabled) redis.opsForValue().set(prefix + key, "DONE", resultTtl);
    }

    /**
     * Processing failed: forget the key so the client can retry.
     */
    public void release(String key) {
        if (enabled) redis.delete(prefix + key);
    }

    /**
     * true = first time we see this key (go ahead), false = duplicate
     */
    public boolean firstTime(String key) {
        return Boolean.TRUE.equals(
                redis.opsForValue().setIfAbsent("idem:" + key, "1", Duration.ofHours(24)));
    }

}
