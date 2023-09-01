package com.nat.nexastream.example.condition;

import com.nat.nexastream.annotations.distribution.TaskRetryCondition;
import com.nat.nexastream.core.tasks.TaskExecutionContext;
import com.nat.nexastream.exceptions.TemporaryFailureException;

public class CustomRetryCondition implements TaskRetryCondition.Condition {
    @Override
    public boolean shouldRetry(TaskExecutionContext context) {
        System.out.println("Ejecutando la condicion");
        // Lógica personalizada para decidir si reintentar la tarea
        return context.getRetryCount() < 3 && context.getLastException() instanceof TemporaryFailureException;
    }
}