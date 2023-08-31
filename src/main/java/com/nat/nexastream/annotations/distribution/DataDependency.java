package com.nat.nexastream.annotations.distribution;

public @interface DataDependency {
    String sourceTask();
    String dataKey();
}
