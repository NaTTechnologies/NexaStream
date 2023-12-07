package com.nat.nexastream.annotations.distribution;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface LoadBalanced {
    public Strategy strategy () default Strategy.DIVIDE_AND_CONQUER;

    public enum Strategy{
        DIVIDE_AND_CONQUER, MAP_REDUCE, MASTER_SLAVE, PIPELINE, TASK_FARM
    }
}
