package com.nat.nexastream.core.node;

import com.nat.nexastream.core.tasks.TaskMetadata;

import java.util.List;

public class Node {
    private String id;
    private String name;
    private int resources;

    private String ip;

    private int port = 8080;

    private String username = "admin";

    private String password = "1234";

    private List<TaskMetadata> tasks;

    private List<Node> instances;

    public List<Node> getInstances() {
        return instances;
    }

    public void setInstances(List<Node> instances) {
        this.instances = instances;
    }

    public List<TaskMetadata> getTasks() {
        return tasks;
    }

    public void setTasks(List<TaskMetadata> tasks) {
        this.tasks = tasks;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    // getters y setters

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getResources() {
        return resources;
    }

    public void setResources(int resources) {
        this.resources = resources;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
}
