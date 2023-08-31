package com.nat.nexastream.core.tasks;

import java.util.List;

public class TaskExecutionContext {
    private int retryCount;
    private Throwable lastException;
    private List<String> dependentTaskNames;

    // Constructor, getters y setters


    public int getRetryCount() {
        return retryCount;
    }

    public Throwable getLastException() {
        return lastException;
    }

    public List<String> getDependentTaskNames() {
        return dependentTaskNames;
    }
}
