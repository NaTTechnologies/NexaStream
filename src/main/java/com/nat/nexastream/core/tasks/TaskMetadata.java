package com.nat.nexastream.core.tasks;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.nat.nexastream.annotations.distribution.DistributableTask;
import com.nat.nexastream.annotations.distribution.Node;

import java.lang.reflect.Method;
import java.util.UUID;

public class TaskMetadata {
    private String id = UUID.randomUUID().toString().replace("-", "");
    private String className;
    private String methodName;

    @JsonIgnore
    private DistributableTask annotation;
    @JsonIgnore
    private Node node;

    @JsonIgnore
    private String classNameNode;

    @JsonIgnore
    private Method method;

    private String taskName;

    private DistributableTask.Enviroment enviroment;

    public DistributableTask.Enviroment getEnviroment() {
        return enviroment;
    }

    public void setEnviroment(DistributableTask.Enviroment enviroment) {
        this.enviroment = enviroment;
    }

    public TaskMetadata(String className, String methodName,
                        DistributableTask annotation, Node node,
                        String classNameNode, Method methodObject, DistributableTask.Enviroment enviroment) {
        this.className = className;
        this.methodName = methodName;
        this.annotation = annotation;
        this.node = node;
        this.classNameNode = classNameNode;
        this.taskName = annotation.name();
        this.method = methodObject;
        this.enviroment = enviroment;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    // Getters para los atributos
    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public DistributableTask getAnnotation() {
        return annotation;
    }

    public void setAnnotation(DistributableTask annotation) {
        this.annotation = annotation;
    }

    public Node getNode() {
        return node;
    }

    public void setNode(Node node) {
        this.node = node;
    }

    public String getClassNameNode() {
        return classNameNode;
    }

    public void setClassNameNode(String classNameNode) {
        this.classNameNode = classNameNode;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}