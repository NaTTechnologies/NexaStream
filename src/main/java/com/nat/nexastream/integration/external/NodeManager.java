package com.nat.nexastream.integration.external;

import com.nat.nexastream.core.node.Node;
import com.nat.nexastream.core.node.NodeList;
import com.nat.nexastream.core.tasks.TaskAssignmentManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/nodes")
public class NodeManager {

    @Autowired
    private NodeList nodeList;

    @Autowired
    private TaskAssignmentManager taskAssignmentManager;

    @PostMapping(produces = { "application/json" , "text/plain" })
    public ResponseEntity<String> registerNode(@RequestBody Node node) {
        nodeList.put(node.getId(), node);
        return ResponseEntity.status(HttpStatus.CREATED).body("Node registered successfully.");
    }

    @PostMapping(path = "/run-node/{nameNode}", produces = { "application/json" })
    public ResponseEntity<Map<String, Object>> runNode(@PathVariable String nameNode) {
        return ResponseEntity.status(HttpStatus.CREATED).body(taskAssignmentManager.runNode(nameNode));
    }

    @PostMapping(path = "/run-task/{nameTask}", produces = { "application/json" })
    public @ResponseBody ResponseEntity<Object> runTask(@PathVariable String nameTask)
            throws ClassNotFoundException, InvocationTargetException,
            NoSuchMethodException, IllegalAccessException, InstantiationException {
        return ResponseEntity.status(HttpStatus.CREATED).body(taskAssignmentManager.executeTask(nameTask));
    }

    @PutMapping(path = "/{id}", produces = { "application/json" })
    public ResponseEntity<String> updateNodeResources(@PathVariable String id, @RequestBody Node updatedNode) {
        if (nodeList.containsKey(id)) {
            Node node = nodeList.get(id);
            node.setResources(updatedNode.getResources());
            return ResponseEntity.ok("Node resources updated successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Node not found.");
        }
    }

    @GetMapping(path = "/{id}", produces = { "application/json" })
    public ResponseEntity<Node> getNode(@PathVariable String id) {
        if (nodeList.containsKey(id)) {
            return ResponseEntity.ok(nodeList.get(id));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping(produces = { "application/json" })
    public ResponseEntity<List<Node>> getAllNodes() {
        return ResponseEntity.ok(new ArrayList<>(nodeList.values()));
    }

    @DeleteMapping(path = "/{id}", produces = { "application/json", "text/plain" })
    public ResponseEntity<String> removeNode(@PathVariable String id) {
        if (nodeList.containsKey(id)) {
            nodeList.remove(id);
            return ResponseEntity.ok("Node removed successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Node not found.");
        }
    }
}

