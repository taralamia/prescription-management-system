package com.example.prescriptionapp.controller;

import com.example.prescriptionapp.model.Prescription;
import com.example.prescriptionapp.repository.DateCount;
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
            redirectAttributes.addFlashAttribute("error", "Validation failed. Please correct the form.");
            return "redirect:/prescriptions";
        }

        service.saveForUser(prescription, username);
        redirectAttributes.addFlashAttribute("success", "Prescription created successfully!");
        return "redirect:/prescriptions";
    }

    @GetMapping("/{id}/edit")
    @ResponseBody
    public Prescription getPrescriptionForEdit(Principal principal, @PathVariable Long id) {
        String username = principal.getName();
        return service.findByIdForUser(id, username)
                .orElseThrow(() -> new IllegalArgumentException("Prescription not found with id: " + id));
    }

    @PostMapping("/{id}/update")
    public String update(
            Principal principal,
            @PathVariable Long id,
            @Valid @ModelAttribute Prescription prescription,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes
    ) {
        String username = principal.getName();

        if (bindingResult.hasErrors()) {
            String errorMessage = bindingResult.getFieldErrors().stream()
                    .map(error -> error.getField() + ": " + error.getDefaultMessage())
                    .collect(Collectors.joining(", "));
            redirectAttributes.addFlashAttribute("error", "Validation failed: " + errorMessage);
            return "redirect:/prescriptions";
        }

        try {
            prescription.setId(id);
            service.saveForUser(prescription, username);
            redirectAttributes.addFlashAttribute("success", "Prescription updated successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error updating prescription: " + e.getMessage());
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
    @GetMapping("/report")
    @ResponseBody
    public List<DateCount> getPrescriptionReport(
            Principal principal,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end
    ) {
        String username = principal.getName();
        return service.dayWiseCountsForUser(username, start, end);
    }

    @GetMapping("/report-page")
    public String showReportPage(
            Principal principal,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end,
            Model model
    ) {
        String username = principal.getName();

        // Default to current month if no dates provided
        if (start == null || end == null) {
            YearMonth ym = YearMonth.now();
            start = ym.atDay(1);
            end = ym.atEndOfMonth();
        }

        List<DateCount> report = service.dayWiseCountsForUser(username, start, end);

        model.addAttribute("report", report);
        model.addAttribute("start", start);
        model.addAttribute("end", end);
        return "prescription-report.html";
    }
}