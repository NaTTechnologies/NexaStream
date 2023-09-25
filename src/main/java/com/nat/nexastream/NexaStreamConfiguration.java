package com.nat.nexastream;

import com.nat.nexastream.core.node.Node;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class NexaStreamConfiguration {
    @Bean
    @Scope("singleton")
    public Map<String, Node> nodes(){
        return new HashMap<>();
    }
}
