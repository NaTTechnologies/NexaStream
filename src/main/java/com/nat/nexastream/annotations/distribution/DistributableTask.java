package com.nat.nexastream.annotations.distribution;

public @interface DistributableTask {
    int priority();
    String[] dependencies();
}
