package com.nat.nexastream.annotations.distribution;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface DistributableTask {
    int priority() default 0;
    String[] dependencies() default {};

    String name();

    boolean dependency() default false;

    Enviroment enviroment() default Enviroment.JAVA;

    public enum Enviroment{
        JAVA,
        JCTL
    }
}
