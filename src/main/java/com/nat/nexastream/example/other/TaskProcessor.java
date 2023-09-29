package com.nat.nexastream.example.other;

import com.nat.nexastream.annotations.distribution.*;
import com.nat.nexastream.example.condition.CustomRetryCondition;
import com.nat.nexastream.exceptions.TemporaryFailureException;
import rx.Observable;

@Node(name = "exampleNode")
public class TaskProcessor {
    public static byte retry = 0;

    @InjectDependency(name = "data")
    public Observable<String> data;

    @DataDependency(dataKey = "data")
    public Observable<String> getData(){
        return Observable.create(stringEmitter -> stringEmitter.onNext("Palabra"));
    }

    @LoadBalanced
    @DistributableTask(priority = 50, name = "processTask")
    @RetryableTask(maxRetries = 5, retryDelay = 1000)
    @TaskRetryCondition(conditionClass = CustomRetryCondition.class, retryDelay = 1000)
    public void processTask() throws TemporaryFailureException, InterruptedException {
        // Logica de procesamiento de la tarea
        retry++;
        System.out.println("Entrando en processTask");
        throw new TemporaryFailureException("Error temporal");
    }

    @DistributableTask(priority = 10, name = "dataFetcher")
    public void dataFetcher() {
        // Logica para obtener datos necesarios
        System.out.println("dataFetcher");
        data.subscribe(s -> System.out.println("La data es: " + s));
    }
}
