package com.nat.nexastream.core.node;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class NodeList implements Map<String, Node> {
    private Map<String, Node> nodeList;

    public NodeList() {
        this.nodeList = new HashMap<>();
    }

    @Override
    public int size() {



        return this.nodeList.size();
    }

    @Override
    public boolean isEmpty() {
        return this.nodeList.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return this.nodeList.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return this.nodeList.containsKey(value);
    }

    @Override
    public Node get(Object key) {
        return this.nodeList.get(key);
    }

    @Override
    public Node put(String key, Node value) {
        return this.nodeList.put(key, value);
    }

    @Override
    public Node remove(Object key) {
        return this.nodeList.remove(key);
    }

    @Override
    public void putAll(Map<? extends String, ? extends Node> m) {
        this.nodeList.putAll(m);
    }

    @Override
    public void clear() {
        this.nodeList.clear();
    }

    @Override
    public Set<String> keySet() {
        return this.nodeList.keySet();
    }

    @Override
    public Collection<Node> values() {
        return this.nodeList.values();
    }

    @Override
    public Set<Entry<String, Node>> entrySet() {
        return this.nodeList.entrySet();
    }
}
