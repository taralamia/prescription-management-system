package com.example.prescriptionapp.service;

import com.example.prescriptionapp.service.interfaces.IDrugInteractionService;
import com.example.prescriptionapp.util.RestClient;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.stereotype.Service;

@Service
public class DrugInteractionService implements IDrugInteractionService {
    private final RestClient restClient;

    public DrugInteractionService(RestClient restClient) {
        this.restClient = restClient;
    }

    @Override
    public JsonNode getDrugInteraction(String rxcui) {
        return restClient.getInteraction(rxcui);
    }
}
