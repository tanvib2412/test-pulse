package com.testing.testpulse.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonNode;
import com.testing.testpulse.service.SonarQubeService;

@RestController
@RequestMapping("/api/sonarqube") // Base path for all endpoints in this controller
public class SonarQubeController {

    private final SonarQubeService sonarQubeService;

    // Constructor-based Dependency Injection
    @Autowired
    public SonarQubeController(SonarQubeService sonarQubeService) {
        this.sonarQubeService = sonarQubeService;
    }

    // Endpoint to get metrics of a SonarQube project by its key
    @GetMapping("/metrics")
    public ResponseEntity<?> getProjectMetrics() {
        try {
            // Use SonarQubeService to get metrics
        	   JsonNode metrics = sonarQubeService.getProjectMetrics();
            // Return the metrics as JSON
            return ResponseEntity.ok(metrics);
        } catch (Exception e) {
            // In case of errors, return an appropriate HTTP response
            return ResponseEntity.badRequest().body("Error fetching SonarQube metrics: " + e.getMessage());
        }
    }
}
