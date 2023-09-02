package com.nat.nexastream.core.tasks;

import com.nat.nexastream.annotations.distribution.DistributableTask;
import com.nat.nexastream.annotations.distribution.Node;
import com.nat.nexastream.annotations.distribution.TaskRetryCondition;
import com.nat.nexastream.core.tasks.TaskExecutionContext;
import com.nat.nexastream.core.tasks.TaskMetadata;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.concurrent.*;

public class TaskAssignmentManager {

    private final TaskExecutionContext taskExecutionContext;
    private final Map<String, List<TaskMetadata>> nodeTaskMap;
    private final ExecutorService executorService;

    public TaskAssignmentManager(String packageName)
            throws ClassNotFoundException, IOException {
        this.taskExecutionContext = new TaskExecutionContext(packageName);
        this.nodeTaskMap = new HashMap<>();

        // Escanea el paquete y carga las tareas
        List<TaskMetadata> taskMetadataList =
                taskExecutionContext.scanPackageForTasks(packageName);

        // Organiza las tareas por nodo
        for (TaskMetadata metadata : taskMetadataList) {
            String node = metadata.getNode().name();
            nodeTaskMap.computeIfAbsent(node, k -> new ArrayList<>()).add(metadata);
        }

        // Crea un ExecutorService para administrar los hilos
        int numThreads = nodeTaskMap.size(); // Un hilo por nodo
        executorService = Executors.newFixedThreadPool(numThreads);
    }

    public void runNode(String nodeName) {
        List<TaskMetadata> tasks = nodeTaskMap.get(nodeName);

        if (tasks != null) {
            tasks.sort(Comparator.comparingInt(value -> ((TaskMetadata) value).getAnnotation().priority()).reversed());

            for (TaskMetadata metadata : tasks) {
                String taskName = metadata.getTaskName();
                Runnable taskRunner = () -> {
                    // Ejecuta la tarea con reintentos si es necesario
                    try {
                        executeTask(taskName);
                    } catch (ClassNotFoundException e) {
                        throw new RuntimeException(e);
                    } catch (InvocationTargetException e) {
                        throw new RuntimeException(e);
                    } catch (NoSuchMethodException e) {
                        throw new RuntimeException(e);
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException(e);
                    } catch (InstantiationException e) {
                        throw new RuntimeException(e);
                    }
                };
                executorService.submit(taskRunner);
            }

            // Espera a que todas las tareas se completen antes de continuar
            //awaitCompletion();
        } else {
            System.err.println("No se encontraron tareas para el nodo: " + nodeName);
        }
    }

    private void executeTask(String taskName) throws ClassNotFoundException, InvocationTargetException, NoSuchMethodException, IllegalAccessException, InstantiationException {
        taskExecutionContext.executeTask(taskName);
        //awaitCompletion();
    }

    private void awaitCompletion() {
        executorService.shutdown();
        try {
            executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public void shutdown() {
        executorService.shutdownNow();
    }
}