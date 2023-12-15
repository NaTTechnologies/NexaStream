package com.nat.nexastream.integration.external.socket;

import com.nat.nexastream.core.node.Node;
import com.nat.nexastream.core.node.NodeList;
import com.nat.nexastream.core.tasks.TaskAssignmentManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

@Controller
//@RequestMapping("/node-socket")
public class SocketNodeManager {
    @Autowired
    private NodeList nodeList;

    @Autowired
    private TaskAssignmentManager taskAssignmentManager;

    // Maneja los mensajes de registro de nodos
    @MessageMapping("/register-node")
    @SendTo("/topic/node-registered")
    public String registerNode(Node node) {
        nodeList.put(node.getId(), node);
        return "Node registered successfully.";
    }

    // Maneja los mensajes para ejecutar un nodo
    @MessageMapping("/run-node/{nameNode}")
    @SendTo("/topic/node-running")
    public Map<String, Object> runNode(@PathVariable String nameNode) {
        return  taskAssignmentManager.runNode(nameNode);
    }

    // Maneja los mensajes para ejecutar una tarea
    @MessageMapping("/run-task/{nameTask}")
    @SendTo("/topic/task-running")
    public Object runTask(@PathVariable String nameTask) throws ClassNotFoundException,
            InvocationTargetException, NoSuchMethodException,
            IllegalAccessException, InstantiationException {
        return taskAssignmentManager.executeTask(nameTask);
    }

    // Actualiza los recursos de un nodo
    @MessageMapping("/update-node-resources/{id}")
    @SendTo("/topic/node-updated")
    public String updateNodeResources(@DestinationVariable String id, Node updatedNode) {
        if (nodeList.containsKey(id)) {
            Node node = nodeList.get(id);
            node.setResources(updatedNode.getResources());
            return "Node resources updated successfully.";
        } else {
            return "Node not found.";
        }
    }

    // Obtiene la información de un nodo por su ID
    @MessageMapping("/get-node/{id}")
    @SendTo("/topic/node-info")
    public Node getNode(@DestinationVariable String id) {
        if (nodeList.containsKey(id)) {
            return nodeList.get(id);
        } else {
            return null; // Puedes manejar la respuesta cuando el nodo no se encuentra
        }
    }

    // Elimina un nodo por su ID
    @MessageMapping("/remove-node/{id}")
    @SendTo("/topic/node-removed")
    public String removeNode(@DestinationVariable String id) {
        if (nodeList.containsKey(id)) {
            nodeList.remove(id);
            return "Node removed successfully.";
        } else {
            return "Node not found.";
        }
    }
}

