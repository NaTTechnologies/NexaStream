package com.nat.nexastream.integration.external.configuration;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.*;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class RestTemplateConfig {

    @Bean
    @LoadBalanced
    public RestTemplate restTemplate(
            @Value("${com.nat.nexastream.security.oauth.client.password}") String password,
            @Value("${com.nat.nexastream.security.oauth.client.user}") String user
    ) {
        RestTemplate restTemplate = new RestTemplate();

        // Configura un Interceptor para agregar las credenciales de autenticación a cada solicitud
        restTemplate.getInterceptors().add((ClientHttpRequestInterceptor) (request, body, execution) -> {
            // Asegurate de que solo se apliquen las credenciales en las solicitudes POST
            // Añade las credenciales al encabezado de la solicitud
            request.getHeaders().add("Authorization", "Bearer " + getAccessToken(user, password));
            // Continúa con la ejecución de la solicitud
            return execution.execute(request, body);
        });

        return restTemplate;
    }

    @Value("${discovery.server.url}")
    private String discoveryServerUrl;

    public String getAccessToken(String username, String password) throws IOException {
        // Construir la URL completa para la obtención del token
        String tokenUrl = discoveryServerUrl + "/oauth/token?username=" + username + "&password=" + password + "&grant_type=password";
        String credentials = username + ":" + password;
        String encodedCredentials = new String(Base64.getEncoder().encode(credentials.getBytes()));
        // Configurar las cabeceras de la solicitud
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.setBasicAuth(encodedCredentials);

        // Configurar la entidad de la solicitud
        HttpEntity<String> requestEntity = new HttpEntity<>(headers);

        // Crear una instancia de RestTemplate
        RestTemplate restTemplate = new RestTemplate();

        // Realizar la solicitud POST para obtener el token
        ResponseEntity<String> responseEntity = restTemplate.exchange(
                tokenUrl,
                HttpMethod.POST,
                requestEntity,
                String.class
        );

        // Verificar si la solicitud fue exitosa
        if (responseEntity.getStatusCode().is2xxSuccessful()) {
            // Extraer el token de la respuesta
            String responseBody = responseEntity.getBody();
            // Aquí debes analizar el JSON de la respuesta para extraer el token.
            // Estoy asumiendo que el token está en formato JSON y se encuentra en un campo llamado "access_token".
            // Ajusta este proceso según la estructura real de tu respuesta.

            Map<String, String> data = new ObjectMapper().readValue(responseBody.getBytes(), HashMap.class);

            return data.get("access_token");
        } else {
            // La solicitud no fue exitosa, manejar el error según tus necesidades.
            throw new RuntimeException("Error al obtener el token");
        }
    }
}
