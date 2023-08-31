package com.nat.nexastream.example;

import com.nat.nexastream.annotations.distribution.*;
import com.nat.nexastream.core.tasks.TaskExecutionContext;
import com.nat.nexastream.exceptions.TemporaryFailureException;

public class TaskProcessor {

    @LoadBalanced
    @DistributableTask(priority = 5, dependencies = {"dataFetcher"})
    @RetryableTask(maxRetries = 3, retryDelay = 1000)
    @TaskRetryCondition(conditionClass = CustomRetryCondition.class)
    @TaskTimeout(timeoutMillis = 5000)
    public void processTask() {
        // Lógica de procesamiento de la tarea
    }

    @DistributableTask(priority = 10, dependencies = {})
    public void dataFetcher() {
        // Lógica para obtener datos necesarios
    }
}

class CustomRetryCondition implements TaskRetryCondition.Condition {
    @Override
    public boolean shouldRetry(TaskExecutionContext context) {
        // Lógica personalizada para decidir si reintentar la tarea
        return context.getRetryCount() < 3 && context.getLastException() instanceof TemporaryFailureException;
    }
}
