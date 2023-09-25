package com.nat.nexastream.core.node;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//@Rest
@RestController
@RequestMapping("/nodes")
public class NodeController {

    @Autowired
    private Map<String, Node> nodes;

    @PostMapping
    public ResponseEntity<String> registerNode(@RequestBody Node node) {
        nodes.put(node.getId(), node);
        return ResponseEntity.status(HttpStatus.CREATED).body("Node registered successfully.");
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateNodeResources(@PathVariable String id, @RequestBody Node updatedNode) {
        if (nodes.containsKey(id)) {
            Node node = nodes.get(id);
            node.setResources(updatedNode.getResources());
            return ResponseEntity.ok("Node resources updated successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Node not found.");
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Node> getNode(@PathVariable String id) {
        if (nodes.containsKey(id)) {
            return ResponseEntity.ok(nodes.get(id));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping
    public ResponseEntity<List<Node>> getAllNodes() {
        return ResponseEntity.ok(new ArrayList<>(nodes.values()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> removeNode(@PathVariable String id) {
        if (nodes.containsKey(id)) {
            nodes.remove(id);
            return ResponseEntity.ok("Node removed successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Node not found.");
        }
    }
}

