package com.em.demo.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
public class EmailValidationService {

    @Value("${email.validation.api.url}")
    private String apiUrl;

    @Value("${email.validation.api.key}")
    private String apiKey;

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public EmailValidationService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public boolean validateEmail(String email) {
        log.info("validating Email: {}", email);

        // Construct the full URL with the email and API key
        String url = apiUrl + "?api_key=" + apiKey + "&email=" + email;

        try {
            // Call the API and get the response as a JsonNode
            String response = restTemplate.getForObject(url, String.class);
            JsonNode jsonNode = objectMapper.readTree(response);

            String deliverability = jsonNode.path("deliverability").asText();

            // Check if the email is valid by reading the "is_valid" field
            return "DELIVERABLE".equals(deliverability);
        } catch (Exception e) {
            System.out.println("Error validating email: " + e.getMessage());

            return false;  // Return false or handle the exception as needed
        }
    }
}
