package com.nat.nexastream.core.tasks;


import com.nat.nexastream.annotations.distribution.DistributableTask;
import com.nat.nexastream.annotations.distribution.Node;

public class TaskMetadata {
    private String className;
    private String methodName;
    private DistributableTask annotation;

    private Node node;

    private String classNameNode;

    private String taskName;

    public TaskMetadata(String className, String methodName,
                        DistributableTask annotation, Node node,
                        String classNameNode) {
        this.className = className;
        this.methodName = methodName;
        this.annotation = annotation;
        this.node = node;
        this.classNameNode = classNameNode;
        this.taskName = annotation.name();
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
}