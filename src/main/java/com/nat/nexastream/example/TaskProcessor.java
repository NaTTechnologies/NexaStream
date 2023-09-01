package com.nat.nexastream.example;

import com.nat.nexastream.annotations.distribution.*;
import com.nat.nexastream.example.condition.CustomRetryCondition;

@Node(name = "exampleTask")
public class TaskProcessor {

    @LoadBalanced
    @DistributableTask(priority = 5, dependencies = {"dataFetcher"}, name = "processTask")
    @RetryableTask(maxRetries = 3, retryDelay = 1000)
    @TaskRetryCondition(conditionClass = CustomRetryCondition.class)
    @TaskTimeout(timeoutMillis = 5000)
    public void processTask() {
        // Lógica de procesamiento de la tarea
        System.out.println("processTask ");
    }

    @DistributableTask(priority = 10, dependencies = {}, name = "dataFetcher")
    public void dataFetcher() {
        // Lógica para obtener datos necesarios
        System.out.println("dataFetcher");
    }
}
