package com.nat.nexastream.integration.external.socket;

import com.nat.nexastream.core.node.Node;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Map;

@Service
public class SocketNodeAssignmentManager {
    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    public String registerNode(Node node) {
        // Lógica para registrar un nodo

        // Enviar un mensaje al tópico "/topic/node-registered"
        messagingTemplate.convertAndSend("/topic/node-registered", "Node registered successfully.");
        return "Node registered successfully.";
    }

    public Map<String, Object> runNode(String nameNode) {
        // Lógica para ejecutar un nodo

        // Enviar un mensaje al tópico "/topic/node-running"
        messagingTemplate.convertAndSend("/topic/node-running", "Node " + nameNode + " running");
        return Collections.singletonMap("status", "Node " + nameNode + " running");
    }

    public Object runTask(String nameTask) {
        // Lógica para ejecutar una tarea

        // Enviar un mensaje al tópico "/topic/task-running"
        messagingTemplate.convertAndSend("/topic/task-running", "Name task " + nameTask + " running");
        return "Name task " + nameTask + " running";
    }

    public String updateNodeResources(String id, Node updatedNode) {
        // Lógica para actualizar los recursos de un nodo

        // Enviar un mensaje al tópico "/topic/node-updated"
        messagingTemplate.convertAndSend("/topic/node-updated", "Node resources updated successfully.");
        return "Node resources updated successfully.";
    }

    public Node getNode(String id) {
        // Lógica para obtener la información de un nodo

        // Enviar un mensaje al tópico "/topic/node-info"
        messagingTemplate.convertAndSend("/topic/node-info", "Node info for ID: " + id);
        return null; // Manejar la respuesta cuando el nodo no se encuentra
    }

    public String removeNode(String id) {
        // Lógica para eliminar un nodo

        // Enviar un mensaje al tópico "/topic/node-removed"
        messagingTemplate.convertAndSend("/topic/node-removed", "Node removed successfully.");
        return "Node removed successfully.";
    }
}
