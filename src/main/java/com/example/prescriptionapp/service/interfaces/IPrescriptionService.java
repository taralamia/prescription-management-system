package com.example.prescriptionapp.service.interfaces;

import com.example.prescriptionapp.model.Prescription;
import com.example.prescriptionapp.repository.DateCount;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


public interface IPrescriptionService {
    Prescription saveForUser(Prescription prescription, String username);
    Optional<Prescription> findByIdForUser(Long id, String username);
    void deleteByIdForUser(Long id, String username);
    Page<Prescription> findByDateRangeForUser(String username, LocalDate start, LocalDate end, Pageable pageable);
    List<Prescription> findByDateRangeForUser(String username, LocalDate start, LocalDate end);
    Page<Prescription> searchByPatientNameForUser(String username, String name, Pageable pageable);
    long countByDateRangeForUser(String username, LocalDate start, LocalDate end);
    List<DateCount> dayWiseCountsForUser(String username, LocalDate start, LocalDate end);


}