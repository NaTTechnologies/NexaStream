package com.nat.nexastream.core.node;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class Instance {
    private String id;
    private String name;
    private int resources;
    private String ip;
    private int port = 8080;
    private String username = "admin";
    private String password = "1234";

    private int executors = 0;

    public int getExecutors() {
        return executors;
    }

    public void setExecutors(int executors) {
        this.executors = executors;
    }

    public Instance(Node node) {
        this.node = node;
        this.setId(node.getId());
        this.setName(node.getName());
        this.setResources(node.getResources());
        this.setIp(node.getIp());
        this.setPort(node.getPort());
        this.setUsername(node.getUsername());
        this.setPassword(node.getPassword());
    }

    @JsonIgnore
    private Node node;

    public Node getNode() {
        return node;
    }

    public void setNode(Node node) {
        this.node = node;
    }

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

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
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
}
