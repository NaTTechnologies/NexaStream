package com.nat.nexastream.annotations.distribution;

public @interface RetryableTask {
    int maxRetries();
    long retryDelay();
}
