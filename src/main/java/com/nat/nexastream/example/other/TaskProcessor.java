package com.nat.nexastream.example;

import com.nat.nexastream.annotations.distribution.*;
import com.nat.nexastream.example.condition.CustomRetryCondition;
import com.nat.nexastream.exceptions.TemporaryFailureException;

@Node(name = "exampleNode")
public class TaskProcessor {
    public static byte retry = 0;

    @LoadBalanced
    @DistributableTask(priority = 50, name = "processTask")
    @RetryableTask(maxRetries = 5, retryDelay = 1000)
    @TaskRetryCondition(conditionClass = CustomRetryCondition.class, retryDelay = 1000)
    public void processTask() throws TemporaryFailureException, InterruptedException {
        // Logica de procesamiento de la tarea
        retry++;
        System.out.println("Entrando en processTask");
        //System.out.println("Retry: " + retry);
//        if (retry % 2 == 0){
//            Thread.sleep(1000);
//        }
        throw new TemporaryFailureException("Error temporal");
    }

    @DistributableTask(priority = 10, name = "dataFetcher")
    public void dataFetcher() {
        // Logica para obtener datos necesarios
        System.out.println("dataFetcher");
    }
}
