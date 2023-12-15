package com.nat.nexastream.integration.external;

import com.nat.nexastream.core.node.Node;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
public class NodeAssignmentManager {
    @Autowired
    private RestTemplate restTemplate;

    private String host = "http://NexaStreamApp";

    public ResponseEntity<String> registerNode(Node node) {
        String url = host + "/nodes";
        return restTemplate.postForEntity(url, node, String.class);
    }

    public ResponseEntity<Map<String, Object>> runNode(String node) {
        String url = host + "/nodes/run-node/" + node;
        return restTemplate.postForEntity(url, null, (Class<Map<String, Object>>) (Class<?>) Map.class);
    }

    public ResponseEntity<Object> runTask(String nameTask) {
        String url = host + "/nodes/run-task/" + nameTask;
        return restTemplate.postForEntity(url, null, Object.class);
    }

    public ResponseEntity<String> updateNodeResources(String id, Node updatedNode) {
        String url = host + "/nodes/" + id;
        return restTemplate.exchange(url, HttpMethod.PUT, new HttpEntity<>(updatedNode), String.class);
    }

    public ResponseEntity<Node> getNode(String id) {
        String url = host + "/nodes/" + id;
        return restTemplate.getForEntity(url, Node.class);
    }

    public ResponseEntity<List<Node>> getAllNodes() {
        String url = host + "/nodes";
        return restTemplate.exchange(url, HttpMethod.GET, null, (Class<List<Node>>) (Class<?>) List.class);
    }

    public ResponseEntity<String> removeNode(String id) {
        String url = host + "/nodes/" + id;
        return restTemplate.exchange(url, HttpMethod.DELETE, null, String.class);
    }
}
