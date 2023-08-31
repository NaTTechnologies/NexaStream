package com.nat.nexastream.core.tasks;

import com.nat.nexastream.annotations.distribution.DistributableTask;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class TaskLoader {
    private List<TaskMetadata> taskMetadataList;

    public TaskLoader() {
        taskMetadataList = new ArrayList<>();
        scanAndLoadTasks();
    }

    private void scanAndLoadTasks() {
        // Escaneo de clases para encontrar métodos anotados
        // (Implementa esta parte según la lógica de escaneo de clases de tu proyecto)
        List<Class<?>> annotatedClasses = scanAnnotatedClasses();

        for (Class<?> clazz : annotatedClasses) {
            Method[] methods = clazz.getDeclaredMethods();
            for (Method method : methods) {
                if (method.isAnnotationPresent(DistributableTask.class)) {
                    DistributableTask annotation = method.getAnnotation(DistributableTask.class);
                    taskMetadataList.add(new TaskMetadata(clazz.getName(), method.getName(), annotation));
                }
            }
        }
    }

    private List<Class<?>> scanAnnotatedClasses() {
        // Implementa aquí la lógica de escaneo de clases para encontrar las anotaciones
        // y devuelve la lista de clases anotadas.
        return new ArrayList<>(); // Ejemplo: devolver una lista vacía
    }

    // Métodos para acceder a la lista de metadatos de tareas
    // y otros métodos relacionados

    public List<TaskMetadata> getTaskMetadataList() {
        return taskMetadataList;
    }

    public void setTaskMetadataList(List<TaskMetadata> taskMetadataList) {
        this.taskMetadataList = taskMetadataList;
    }
}