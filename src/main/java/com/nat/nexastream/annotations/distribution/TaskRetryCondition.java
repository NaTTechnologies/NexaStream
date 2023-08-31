package com.nat.nexastream.annotations.distribution;

import com.nat.nexastream.core.tasks.TaskExecutionContext;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface TaskRetryCondition {
    Class<?> conditionClass();

    public interface Condition {
        public boolean shouldRetry(TaskExecutionContext context);
    }
}
