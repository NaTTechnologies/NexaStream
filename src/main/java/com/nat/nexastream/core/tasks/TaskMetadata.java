package com.nat.nexastream.core.tasks;


import com.nat.nexastream.annotations.distribution.DistributableTask;

class TaskMetadata {
    private String className;
    private String methodName;
    private DistributableTask annotation;

    public TaskMetadata(String className, String methodName, DistributableTask annotation) {
        this.className = className;
        this.methodName = methodName;
        this.annotation = annotation;
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
}