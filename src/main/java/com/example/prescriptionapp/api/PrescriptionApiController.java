package com.example.prescriptionapp.api;

import com.example.prescriptionapp.model.Prescription;
import com.example.prescriptionapp.service.interfaces.IPrescriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.YearMonth;
@RestController
@RequestMapping("/api/v1/prescriptions")
public class PrescriptionApiController {
    private final IPrescriptionService prescriptionService;

    @Autowired
    public PrescriptionApiController(IPrescriptionService prescriptionService) {
        this.prescriptionService = prescriptionService;
    }
    @GetMapping
    public Page<Prescription> getPrescriptions(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate startDate,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate endDate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        if (userDetails == null) {
            throw new RuntimeException("User not authenticated");
        }

        String username = userDetails.getUsername();
        if (startDate == null || endDate == null) {
            YearMonth currentMonth = YearMonth.now();
            startDate = currentMonth.atDay(1);
            endDate = currentMonth.atEndOfMonth();
        }

        PageRequest pageable = PageRequest.of(page, size);

        return prescriptionService.findByDateRangeForUser(username, startDate, endDate, pageable);
    }
}
