package com.example.prescriptionapp.service.interfaces;

import com.fasterxml.jackson.databind.JsonNode;

public interface IDrugInteractionService {
    JsonNode getDrugInteraction(String rxcui);
}
