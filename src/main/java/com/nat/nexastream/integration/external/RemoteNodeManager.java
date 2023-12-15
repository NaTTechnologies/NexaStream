package com.nat.nexastream.integration.external;

import com.nat.nexastream.core.node.Node;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/remote/nodes")
public class RemoteNodeManager {

    private final NodeAssignmentManager nodeAssignmentManager;

    @Autowired
    public RemoteNodeManager(NodeAssignmentManager nodeAssignmentManager) {
        this.nodeAssignmentManager = nodeAssignmentManager;
    }

    @PostMapping("/register-node")
    public ResponseEntity<String> registerNode(@RequestBody Node node) {
        return nodeAssignmentManager.registerNode(node);
    }

    @PostMapping("/run-node/{nodeId}")
    public ResponseEntity<Map<String, Object>> runNode(@PathVariable String nodeId) {
        return nodeAssignmentManager.runNode(nodeId);
    }

    @PostMapping(path = "/run-task/{nameTask}", produces = { "application/xml" })
    public ResponseEntity<String> runTask(@PathVariable String nameTask) {
        return nodeAssignmentManager.runTask(nameTask);
    }

    @PutMapping("/update-node/{id}")
    public ResponseEntity<String> updateNodeResources(@PathVariable String id, @RequestBody Node updatedNode) {
        return nodeAssignmentManager.updateNodeResources(id, updatedNode);
    }

    @GetMapping("/get-node/{id}")
    public ResponseEntity<Node> getNode(@PathVariable String id) {
        return nodeAssignmentManager.getNode(id);
    }

    @GetMapping("/get-all-nodes")
    public ResponseEntity<List<Node>> getAllNodes() {
        return nodeAssignmentManager.getAllNodes();
    }

    @DeleteMapping("/remove-node/{id}")
    public ResponseEntity<String> removeNode(@PathVariable String id) {
        return nodeAssignmentManager.removeNode(id);
    }
}
