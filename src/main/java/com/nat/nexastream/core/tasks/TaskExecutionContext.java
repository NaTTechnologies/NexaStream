package com.nat.nexastream.core.tasks;

import com.nat.nexastream.annotations.distribution.DistributableTask;
import com.nat.nexastream.annotations.distribution.Node;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class TaskExecutionContext {
    private int retryCount;
    private Throwable lastException;
    private List<String> dependentTaskNames;

    private List<TaskMetadata> taskMetadataList;

    private Map<String, TaskMetadata> taskMetadataMap;

    public TaskExecutionContext(String packageName) {
        // Escanear el paquete y buscar tareas anotadas
        try {
            taskMetadataList = scanPackageForTasks(packageName);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public List<TaskMetadata> scanPackageForTasks(String packageName) throws ClassNotFoundException, IOException {
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
                                // La clase está anotada con @Node, examinar sus métodos
                                Method[] methods = clazz.getDeclaredMethods();
                                for (Method method : methods) {
                                    if (method.isAnnotationPresent(DistributableTask.class)) {
                                        DistributableTask annotation = method.getAnnotation(DistributableTask.class);
                                        TaskMetadata metadata = new TaskMetadata(className, method.getName(), annotation);
                                        taskList.add(metadata);
                                    }
                                }
                            }
                        }
                    }
                }
            } else {
                // Si el elemento del classpath es un directorio
                File packageDir = new File(classpathEntry + File.separator + path);
                if (packageDir.exists() && packageDir.isDirectory()) {
                    for (File file : packageDir.listFiles()) {
                        if (file.isFile() && file.getName().endsWith(".class")) {
                            String className = packageName + "." + file.getName().replace(".class", "");
                            Class<?> clazz = Class.forName(className);
                            if (clazz.isAnnotationPresent(Node.class)) {
                                // La clase está anotada con @Node, examinar sus métodos
                                Method[] methods = clazz.getDeclaredMethods();
                                for (Method method : methods) {
                                    if (method.isAnnotationPresent(DistributableTask.class)) {
                                        DistributableTask annotation = method.getAnnotation(DistributableTask.class);
                                        TaskMetadata metadata = new TaskMetadata(className, method.getName(), annotation);
                                        taskList.add(metadata);
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



    public void executeTask(String taskName)
            throws NoSuchMethodException,
                IllegalAccessException,
                InvocationTargetException,
                ClassNotFoundException,
                InstantiationException {
        // Buscar la tarea por su nombre
        TaskMetadata taskMetadata = findTaskMetadataByName(taskName);

        if (taskMetadata != null) {
            DistributableTask distributableTask = taskMetadata.getAnnotation();

            for (String dependency : distributableTask.dependencies()){
                executeTask(dependency);
            }

            // Obtener información de la tarea
            String className = taskMetadata.getClassName();
            String methodName = taskMetadata.getMethodName();

            // Utilizar reflexión para ejecutar la tarea
            Class<?> taskClass = Class.forName(className);
            Object taskInstance = taskClass.newInstance();

            Method taskMethod = taskClass.getMethod(methodName);
            taskMethod.invoke(taskInstance);
        } else {
            throw new IllegalArgumentException("Tarea no encontrada: " + taskName);
        }
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
