package com.nat.nexastream.core.tasks;

import com.nat.nexastream.annotations.distribution.*;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class TaskExecutionContext {
    private static TaskExecutionContext taskExecutionContext;
    private ExecutorService executorService;
    private int retryCount = 0;
    private Throwable lastException;
    private List<String> dependentTaskNames;
    private List<TaskMetadata> taskMetadataList;
    private Map<String, TaskMetadata> taskMetadataMap;

    private Map<String, Object> dependecies;

    public TaskExecutionContext(String packageName) {
        dependecies = new HashMap<>();
        // Escanear el paquete y buscar tareas anotadas
        try {
            taskMetadataList = scanPackageForTasks(packageName);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        executorService = Executors.newFixedThreadPool(1); // Un solo hilo para ejecutar tareas
    }


    public List<TaskMetadata> scanPackageForTasks(String packageName)
            throws ClassNotFoundException, IOException, InstantiationException,
            IllegalAccessException, InvocationTargetException {
        List<TaskMetadata> taskList = new ArrayList<>();

        String path = packageName.replace('.', '/');
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

        // Obtenemos el directorio de clases correspondiente al paquete
        String classpath = System.getProperty("java.class.path");
        String[] classpathEntries = classpath.split(File.pathSeparator);

        for (String classpathEntry : classpathEntries) {
            if (classpathEntry.endsWith(".jar")) {
                // Si el elemento del classpath es un archivo JAR
                try (JarFile jarFile = new JarFile(classpathEntry)) {

                    Enumeration<JarEntry> entries = jarFile.entries();

                    while (entries.hasMoreElements()) {
                        JarEntry entry = entries.nextElement();
                        if (!entry.isDirectory() && entry.getName().startsWith(path) && entry.getName().endsWith(".class")) {
                            String className = entry.getName().replace('/', '.').replace('\\', '.').replace(".class", "");

                            Class<?> clazz = Class.forName(className);
                            if (clazz.isAnnotationPresent(Node.class)) {
                                Node node = clazz.getAnnotation(Node.class);
                                Object nodeInstance = clazz.newInstance();
                                // La clase está anotada con @Node, examinar sus métodos
                                Method[] methods = clazz.getDeclaredMethods();
                                for (Method method : methods) {
                                    if (method.isAnnotationPresent(DistributableTask.class)) {
                                        DistributableTask annotation = method.getAnnotation(DistributableTask.class);
                                        TaskMetadata metadata = new TaskMetadata(className, method.getName(), annotation, node, clazz.getName());
                                        taskList.add(metadata);
                                    }

                                    if (method.isAnnotationPresent(DataDependency.class)){
                                        DataDependency dataDependency = method.getAnnotation(DataDependency.class);
                                        dependecies.put(dataDependency.dataKey(), method.invoke(nodeInstance));
                                    }
                                }
                            }
                        }
                    }
                } catch (InstantiationException e) {
                    throw new RuntimeException(e);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                } catch (InvocationTargetException e) {
                    throw new RuntimeException(e);
                }
            } else {
                // Si el elemento del classpath es un directorio
                File packageDir = new File(classpathEntry + File.separator + path);
                if (packageDir.exists() && packageDir.isDirectory()) {
                    for (File file : packageDir.listFiles()) {
                        if (file.isFile() && file.getName().endsWith(".class")) {
                            String className = packageName + "." + file.getName().replace(".class", "");
                            Class<?> clazz = Class.forName(className);
                            Object nodeInstance = clazz.newInstance();
                            if (clazz.isAnnotationPresent(Node.class)) {
                                Node node = clazz.getAnnotation(Node.class);
                                // La clase está anotada con @Node, examinar sus métodos
                                Method[] methods = clazz.getDeclaredMethods();
                                for (Method method : methods) {
                                    if (method.isAnnotationPresent(DistributableTask.class)) {
                                        DistributableTask annotation = method.getAnnotation(DistributableTask.class);
                                        TaskMetadata metadata = new TaskMetadata(className, method.getName(), annotation, node, clazz.getName());
                                        taskList.add(metadata);
                                    }

                                    if (method.isAnnotationPresent(DataDependency.class)){
                                        DataDependency dataDependency = method.getAnnotation(DataDependency.class);
                                        dependecies.put(dataDependency.dataKey(), method.invoke(nodeInstance));
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        return taskList;
    }

    public Object executeTask(String taskName)
            throws NoSuchMethodException,
            IllegalAccessException,
            InvocationTargetException,
            ClassNotFoundException,
            InstantiationException {
        // Objeto a devolver
        Object object = null;
        // Buscar la tarea por su nombre
        TaskMetadata taskMetadata = findTaskMetadataByName(taskName);

        if (taskMetadata != null) {
            DistributableTask distributableTask = taskMetadata.getAnnotation();

            for (String dependency : distributableTask.dependencies()) {
                executeTask(dependency);
            }

            // Obtener información de la tarea
            String className = taskMetadata.getClassName();
            String methodName = taskMetadata.getMethodName();

            // Utilizar reflexión para ejecutar la tarea
            Class<?> taskClass = Class.forName(className);
            Object taskInstance = taskClass.newInstance();

            // Verificar si el método está anotado con @RetryableTask
            Method taskMethod = taskClass.getMethod(methodName);

            Field[] fields = taskClass.getDeclaredFields();

            for(Field field: fields){
                if (field.isAnnotationPresent(InjectDependency.class)){
                    InjectDependency injectDependency = field.getDeclaredAnnotation(InjectDependency.class);

                    field.setAccessible(true);
                    field.set(taskInstance, dependecies.get(injectDependency.name()));
                }
            }

            if (taskMethod.isAnnotationPresent(RetryableTask.class)) {
                RetryableTask retryableTaskAnnotation = taskMethod.getAnnotation(RetryableTask.class);
                int maxRetries = retryableTaskAnnotation.maxRetries();
                long retryDelay = retryableTaskAnnotation.retryDelay();
                boolean taskCompleted = false;

                if (taskMethod.isAnnotationPresent(TaskRetryCondition.class)){
                    TaskRetryCondition taskRetryCondition = taskMethod.getAnnotation(TaskRetryCondition.class);
                    TaskRetryCondition.Condition condition = (TaskRetryCondition.Condition) taskRetryCondition.conditionClass().newInstance();

                    for (int retryCount = 1; retryCount <= maxRetries; retryCount++) {
                        retryCount++;
                        try {
                            object = taskMethod.invoke(taskInstance);
                            break;
                        } catch (Exception e) {
                            // Se produjo una excepción al ejecutar la tarea
                            this.lastException = e.getCause();
                            System.err.println("Excepción al ejecutar la tarea. Reintentando... ");

                            if (condition.shouldRetry(this, (Exception) e.getCause())){
                                // Espera antes de reintentar
                                try {
                                    Thread.sleep(taskRetryCondition.retryDelay());
                                } catch (InterruptedException ex) {
                                    Thread.currentThread().interrupt();
                                }
                                continue;
                            } else {
                                break;
                            }
                        }
                    }
                } else {
                    for (int retryCount = 1; retryCount <= maxRetries; retryCount++) {
                        try {
                            object = taskMethod.invoke(taskInstance);
                            taskCompleted = true; // La tarea se ejecutó exitosamente
                            break; // Sal del bucle si la tarea se completó con éxito
                        } catch (Exception e) {
                            // Se produjo una excepción al ejecutar la tarea
                            lastException = e.getCause();
                            System.err.println("Excepción al ejecutar la tarea. Reintentando...");

                            if (retryCount < maxRetries) {
                                // Espera antes de reintentar
                                try {
                                    Thread.sleep(retryDelay);
                                } catch (InterruptedException ex) {
                                    Thread.currentThread().interrupt();
                                }
                            }
                        }
                    }

                    if (!taskCompleted) {
                        // Si la tarea no se completó después de los reintentos, puedes manejarlo aquí
                        System.err.println("La tarea no se pudo completar después de " + maxRetries + " reintentos.");
                        // Puedes agregar lógica adicional, como registrar la excepción o realizar otras acciones.
                    }
                }
            } else {
                // El método no está anotado con @RetryableTask, ejecutarlo sin reintentos
                try {
                    object = taskMethod.invoke(taskInstance);
                } catch (Exception e) {
                    // Manejar excepciones si es necesario
                }
            }
        } else {
            throw new IllegalArgumentException("Tarea no encontrada: " + taskName);
        }
        return object;
    }

    private TaskMetadata findTaskMetadataByName(String taskName) {
        // Buscar la tarea en la lista de metadatos de tareas
        for (TaskMetadata metadata : taskMetadataList) {
            DistributableTask annotation = metadata.getAnnotation();
            if (annotation != null && annotation.name().equalsIgnoreCase(taskName)) {
                return metadata;
            }
        }
        return null; // Tarea no encontrada
    }

    public int getRetryCount() {
        return retryCount;
    }

    public void setRetryCount(int retryCount) {
        this.retryCount = retryCount;
    }

    public Throwable getLastException() {
        return lastException;
    }

    public void setLastException(Throwable lastException) {
        this.lastException = lastException;
    }

    public List<String> getDependentTaskNames() {
        return dependentTaskNames;
    }

    public void setDependentTaskNames(List<String> dependentTaskNames) {
        this.dependentTaskNames = dependentTaskNames;
    }

    public List<TaskMetadata> getTaskMetadataList() {
        return taskMetadataList;
    }

    public void setTaskMetadataList(List<TaskMetadata> taskMetadataList) {
        this.taskMetadataList = taskMetadataList;
    }

    public Map<String, TaskMetadata> getTaskMetadataMap() {
        return taskMetadataMap;
    }

    public void setTaskMetadataMap(Map<String, TaskMetadata> taskMetadataMap) {
        this.taskMetadataMap = taskMetadataMap;
    }
}
