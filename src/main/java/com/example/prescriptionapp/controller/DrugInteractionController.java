package com.example.prescriptionapp.controller;

import com.example.prescriptionapp.service.interfaces.IDrugInteractionService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class DrugInteractionController {
    private final IDrugInteractionService drugInteractionService;
    private final ObjectMapper objectMapper;

    public DrugInteractionController(IDrugInteractionService drugInteractionService, ObjectMapper objectMapper) {
        this.drugInteractionService = drugInteractionService;
        this.objectMapper = objectMapper;
    }

    @GetMapping("/drug-interactions")
    public String showInteractions(
            @RequestParam(defaultValue = "34124") String rxcui,
            Model model
    ) {
        try {
            JsonNode interactionData = drugInteractionService.getDrugInteraction(rxcui);
            model.addAttribute("interactionData", interactionData);
            model.addAttribute("rxcui", rxcui);

            // Convert JSON to pretty string for display
            String prettyJson = objectMapper.writerWithDefaultPrettyPrinter()
                    .writeValueAsString(interactionData);
            model.addAttribute("prettyJson", prettyJson);

        } catch (Exception e) {
            model.addAttribute("error", "Failed to fetch drug interactions: " + e.getMessage());
        }
        return "drug-interactions";
    }
}