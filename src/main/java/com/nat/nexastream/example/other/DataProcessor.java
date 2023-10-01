package com.nat.nexastream.example.other;

import com.nat.nexastream.annotations.distribution.*;
import com.nat.nexastream.example.condition.CustomRetryCondition;
import com.nat.nexastream.exceptions.TemporaryFailureException;

@Node(name = "dataProcessingNode")
public class DataProcessor {

    @LoadBalanced
    @DistributableTask(priority = 50, name = "processData")
    @RetryableTask(maxRetries = 5, retryDelay = 1000)
    @TaskRetryCondition(conditionClass = CustomRetryCondition.class, retryDelay = 1000)
    public void processData() throws TemporaryFailureException {
        // Simulación de una fuente de datos en tiempo real
        String[] dataStream = {"Dato1", "Dato2", "Dato3", "Dato4", "Dato5"};

        for (String data : dataStream) {
            try {
                // Procesar el dato
                System.out.println("Procesando dato: " + data);
                // Simular un error intermitente
                if ("Dato3".equals(data)) {
                    throw new TemporaryFailureException("Error temporal en el procesamiento");
                }

                // Simular un tiempo de procesamiento
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
