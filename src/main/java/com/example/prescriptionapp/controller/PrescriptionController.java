package com.example.prescriptionapp.controller;


import com.example.prescriptionapp.model.Prescription;
import com.example.prescriptionapp.service.interfaces.IPrescriptionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Controller;

@Controller
@RequestMapping("/prescriptions")
public class PrescriptionController {
    private final IPrescriptionService service;

    @Autowired
    public PrescriptionController(IPrescriptionService service) {
        this.service = service;
    }

    @GetMapping
    public String list(
            Principal principal,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String q,
            Model model
    ) {
        String username = principal.getName();

        // default to current month
        if (start == null || end == null) {
            YearMonth ym = YearMonth.now();
            start = ym.atDay(1);
            end = ym.atEndOfMonth();
        }

        Sort sort = Sort.by(Sort.Direction.DESC, "prescriptionDate");
        Pageable pageable = PageRequest.of(Math.max(page, 0), Math.max(size, 1), sort);

        Page<Prescription> prescriptionsPage;
        if (q != null && !q.isBlank()) {
            prescriptionsPage = service.searchByPatientNameForUser(username, q.trim(), pageable);
        } else {
            prescriptionsPage = service.findByDateRangeForUser(username, start, end, pageable);
        }

        model.addAttribute("prescriptions", prescriptionsPage);
        model.addAttribute("start", start);
        model.addAttribute("end", end);
        model.addAttribute("q", q);
        return "prescriptions";
    }

    @PostMapping("/create")
    public String create(
            Principal principal,
            @Valid @ModelAttribute Prescription prescription,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes
    ) {
        String username = principal.getName();

        if (bindingResult.hasErrors()) {
            // Collect all validation errors
            String errorMessage = bindingResult.getFieldErrors().stream()
                    .map(error -> error.getField() + ": " + error.getDefaultMessage())
                    .collect(Collectors.joining(", "));
            redirectAttributes.addFlashAttribute("error", "Validation failed: " + errorMessage);
            return "redirect:/prescriptions";
        }

        try {
            service.saveForUser(prescription, username);
            redirectAttributes.addFlashAttribute("success", "Prescription created successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error creating prescription: " + e.getMessage());
        }

        return "redirect:/prescriptions";
    }
    @PostMapping("/{id}/delete")
    public String delete(
            Principal principal,
            @PathVariable Long id,
            RedirectAttributes redirectAttributes
    ) {
        String username = principal.getName();
        service.deleteByIdForUser(id, username);
        redirectAttributes.addFlashAttribute("success", "Prescription deleted successfully!");
        return "redirect:/prescriptions";
    }

}

