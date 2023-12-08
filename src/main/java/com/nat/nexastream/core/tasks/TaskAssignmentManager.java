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

    public Map<String, List<TaskMetadata>> getNodeTaskMap() {
        return nodeTaskMap;
    }

    public TaskAssignmentManager(String packageName)
            throws ClassNotFoundException, IOException, InvocationTargetException, InstantiationException, IllegalAccessException {
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

    public Map<String, Object> runNode(String nodeName) {
        List<TaskMetadata> tasks = nodeTaskMap.get(nodeName);
        HashMap<String, Object> returns = new HashMap<>();

        if (tasks != null) {
            tasks.sort(Comparator.comparingInt(value -> ((TaskMetadata) value).getAnnotation().priority()).reversed());

            for (TaskMetadata metadata : tasks) {
                String taskName = metadata.getTaskName();
                Runnable taskRunner = () -> {
                    // Ejecuta la tarea con reintentos si es necesario

                    try {
                        returns.put(taskName, executeTask(taskName));

                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                        throw new RuntimeException(e);
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                        throw new RuntimeException(e);
                    } catch (NoSuchMethodException e) {
                        e.printStackTrace();
                        throw new RuntimeException(e);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                        throw new RuntimeException(e);
                    } catch (InstantiationException e) {
                        e.printStackTrace();
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

        return returns;
    }

    public Object executeTask(String taskName) throws ClassNotFoundException, InvocationTargetException, NoSuchMethodException, IllegalAccessException, InstantiationException {
        return taskExecutionContext.executeTask(taskName);
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
