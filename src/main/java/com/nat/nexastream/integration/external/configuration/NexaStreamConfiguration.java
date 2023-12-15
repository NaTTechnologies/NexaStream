package com.nat.nexastream.integration.external.configuration;

import com.nat.nexastream.core.node.Instance;
import com.nat.nexastream.core.node.Node;
import com.nat.nexastream.core.node.NodeList;
import com.nat.nexastream.core.tasks.TaskAssignmentManager;
import com.nat.nexastream.core.tasks.TaskMetadata;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.net.InetAddress;
import java.net.NetworkInterface;

@Configuration
public class NexaStreamConfiguration {
    @Bean
    @Scope("singleton")
    public NodeList nodes(
            @Autowired TaskAssignmentManager taskAssignmentManager
    ){
        NodeList nodes = new NodeList();
        String ip = "0.0.0.0";

        try {
            Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
            while (networkInterfaces.hasMoreElements()) {
                NetworkInterface networkInterface = networkInterfaces.nextElement();
                Enumeration<InetAddress> inetAddresses = networkInterface.getInetAddresses();
                while (inetAddresses.hasMoreElements()) {
                    InetAddress inetAddress = inetAddresses.nextElement();
                    if (!inetAddress.isLoopbackAddress() && !inetAddress.isLinkLocalAddress() && inetAddress.isSiteLocalAddress()) {
                        ip = inetAddress.getHostAddress();
                        System.out.println("IP local: " + inetAddress.getHostAddress());
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        String finalIp = ip;
        taskAssignmentManager.getNodeTaskMap()
                .forEach((String s, List<TaskMetadata> taskMetadata) -> {
                    Node node = new Node();
                    node.setResources(0);
                    node.setId(UUID.randomUUID().toString().replace("-", ""));
                    node.setName(s);
                    node.setIp(finalIp);
                    node.setTasks(taskMetadata);

                    //Pendiente agregar las instances
                    node.setInstances(Arrays.asList(new Instance(node)));

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
