package com.nat.nexastream;

import com.nat.nexastream.core.node.Node;
import com.nat.nexastream.core.tasks.TaskAssignmentManager;
import com.nat.nexastream.core.tasks.TaskMetadata;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Configuration
public class NexaStreamConfiguration {
    @Bean
    @Scope("singleton")
    public Map<String, Node> nodes(
            @Autowired TaskAssignmentManager taskAssignmentManager
    ){
        Map<String, Node> nodes = new HashMap<>();

        taskAssignmentManager.getNodeTaskMap()
                .forEach((String s, List<TaskMetadata> taskMetadata) -> {
                    Node node = new Node();
                    node.setResources(0);
                    node.setId(UUID.randomUUID().toString().replace("-", ""));
                    node.setName(s);
                    nodes.put(s, node);
                });

        return nodes;
    }

    @Bean
    @Scope("singleton")
    public TaskAssignmentManager taskAssignmentManager(
            @Value("${com.nat.nexastream.packageName}") String packageName
    ) throws IOException, ClassNotFoundException, InvocationTargetException, InstantiationException, IllegalAccessException {
        TaskAssignmentManager taskAssignmentManager = new TaskAssignmentManager(packageName);
        return taskAssignmentManager;
    }
}
