package com.example.prescriptionapp.service;

import com.example.prescriptionapp.model.Prescription;
import com.example.prescriptionapp.repository.DateCount;
import com.example.prescriptionapp.repository.PrescriptionRepository;
import com.example.prescriptionapp.service.interfaces.IPrescriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.security.access.AccessDeniedException;
@Service
@Transactional
public class PrescriptionService implements IPrescriptionService {
    private final PrescriptionRepository repository;

    @Autowired
    public PrescriptionService(PrescriptionRepository repository) {
        this.repository = repository;
    }
    @Override
    public Prescription saveForUser(Prescription prescription, String username) {
        if (prescription == null) throw new IllegalArgumentException("prescription must not be null");

        if (prescription.getId() == null) {

            prescription.setCreatedBy(username);

            return repository.save(prescription);
        } else {

            Prescription existing = repository.findById(prescription.getId())
                    .orElseThrow(() -> new IllegalArgumentException("Prescription not found with id: " + prescription.getId()));

            if (!username.equals(existing.getCreatedBy())) {
                throw new AccessDeniedException("Not authorized to update this prescription");
            }

            prescription.setCreatedBy(existing.getCreatedBy());
            prescription.setCreatedAt(existing.getCreatedAt());


            return repository.save(prescription);
        }
    }
    @Override
    @Transactional(readOnly = true)
    public Optional<Prescription> findByIdForUser(Long id, String username) {
        return repository.findById(id)
                .filter(p -> username.equals(p.getCreatedBy()));
    }
    @Override
    public void deleteByIdForUser(Long id, String username) {
        Prescription p = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Prescription not found with id: " + id));

        if (!username.equals(p.getCreatedBy())) {
            throw new AccessDeniedException("Not authorized to delete this prescription");
        }

        try {
            repository.deleteById(id);
        } catch (EmptyResultDataAccessException ex) {

            throw new IllegalArgumentException("Prescription not found with id: " + id);
        }
    }
    @Override
    @Transactional(readOnly = true)
    public org.springframework.data.domain.Page<Prescription> findByDateRangeForUser(String username, LocalDate start, LocalDate end, org.springframework.data.domain.Pageable pageable) {
        // defensive: require non-null dates (controller should supply defaults)
        if (start == null || end == null) throw new IllegalArgumentException("start and end dates must be provided");
        return repository.findByCreatedByAndPrescriptionDateBetween(username, start, end, pageable);
    }
    @Override
    @Transactional(readOnly = true)
    public List<Prescription> findByDateRangeForUser(String username, LocalDate start, LocalDate end) {
        if (start == null || end == null) throw new IllegalArgumentException("start and end dates must be provided");
        return repository.findByCreatedByAndPrescriptionDateBetweenOrderByPrescriptionDateDesc(username, start, end);
    }
    @Override
    @Transactional(readOnly = true)
    public org.springframework.data.domain.Page<Prescription> searchByPatientNameForUser(String username, String name, org.springframework.data.domain.Pageable pageable) {
        if (name == null) name = "";
        return repository.findByCreatedByAndPatientNameContainingIgnoreCase(username, name, pageable);
    }
    @Override
    @Transactional(readOnly = true)
    public long countByDateRangeForUser(String username, LocalDate start, LocalDate end) {
        if (start == null || end == null) throw new IllegalArgumentException("start and end dates must be provided");
        return repository.countByCreatedByAndPrescriptionDateBetween(username, start, end);
    }
    @Override
    @Transactional(readOnly = true)
    public List<DateCount> dayWiseCountsForUser(String username, LocalDate start, LocalDate end) {
        if (start == null || end == null) throw new IllegalArgumentException("start and end dates must be provided");
        return repository.countByDateBetweenForUser(username, start, end);
    }
}
