package com.nat.nexastream.integration.external.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Configuration
public class UserDetailsConfig {

    @Value("classpath:/users.json")
    private Resource usersJson;

    @Bean
    public UserDetailsManager userDetailsManager() throws IOException {
        List<UserDetails> userDetailsList = loadUsersFromJson();
        return new InMemoryUserDetailsManager(userDetailsList);
    }

    private List<UserDetails> loadUsersFromJson() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        List<UserDetails> userDetailsList = Arrays.asList(objectMapper.readValue(usersJson.getInputStream(), User[].class));
        return userDetailsList;
    }

    @Bean("userServiceOAuth")
    public UserDetailsService userDetailsService() {
        return new UserDetailsService() {
            @Override
            public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
                try {
                    List<UserDetails> userDetailsList = loadUsersFromJson();
                    return userDetailsList.stream()
                            .filter(userDetails -> userDetails.getUsername().equals(username))
                            .findFirst()
                            .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));
                } catch (IOException e) {
                    throw new RuntimeException("Error al cargar usuarios desde JSON", e);
                }
            }
        };
    }
}
