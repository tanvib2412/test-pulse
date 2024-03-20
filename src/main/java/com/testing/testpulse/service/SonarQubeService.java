package com.testing.testpulse.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class SonarQubeService {

    @Value("${sonarqube.url}")
    private String sonarQubeUrl;

    @Value("${sonarqube.token}")
    private String sonarQubeToken;
    
    @Value("${sonarqube.projectKey}")
    private String sonarQubeProjectKey;

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @Autowired
    public SonarQubeService(RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    public JsonNode getProjectMetrics() {
        // Setting up headers for authentication
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(sonarQubeToken);

        // Creating an entity with headers for the RestTemplate exchange method
        HttpEntity<String> entity = new HttpEntity<>(headers);

        // Constructing the URL for the SonarQube API
        String metrics = "bugs,vulnerabilities,code_smells,coverage";
        String requestUrl = String.format("%s/api/measures/component?component=%s&metricKeys=%s", 
                            sonarQubeUrl, sonarQubeProjectKey, metrics);

        // Making the API request and receiving the response
        ResponseEntity<String> response = restTemplate.exchange(requestUrl, HttpMethod.GET, entity, String.class);

        // Processing the JSON response
        try {
            JsonNode responseJson = objectMapper.readTree(response.getBody());
            return responseJson;
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse SonarQube response", e);
        }
    }
}

