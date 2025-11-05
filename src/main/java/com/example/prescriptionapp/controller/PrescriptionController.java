package com.example.prescriptionapp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
@Controller
public class PrescriptionController {
    @GetMapping("/prescriptions")
    public String prescriptions() {
        return "prescriptions";
    }
}
