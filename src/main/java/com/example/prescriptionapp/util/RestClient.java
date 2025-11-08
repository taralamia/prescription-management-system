package com.example.prescriptionapp.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

@Component
public class RestClient {
    private final org.springframework.web.client.RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public RestClient(org.springframework.web.client.RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }
    public JsonNode getInteraction(String rxcui) {
        String url = "https://rxnav.nlm.nih.gov/REST/interaction/interaction.json?rxcui=" + rxcui;
        try {
            String response = restTemplate.getForObject(url, String.class);
            return objectMapper.readTree(response);
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch or parse RxNav interaction: " + e.getMessage(), e);
        }
    }
}
