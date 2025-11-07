package com.example.prescriptionapp.repository;
import com.example.prescriptionapp.model.Prescription;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDate;
import java.util.List;
public interface PrescriptionRepository extends JpaRepository<Prescription,Long> {
    Page<Prescription> findByCreatedByAndPrescriptionDateBetween(
            String createdBy, LocalDate start, LocalDate end, Pageable pageable);
    List<Prescription> findByCreatedByAndPrescriptionDateBetweenOrderByPrescriptionDateDesc(
            String createdBy, LocalDate start, LocalDate end);

    long countByCreatedByAndPrescriptionDateBetween(String createdBy, LocalDate start, LocalDate end);
    Page<Prescription> findByCreatedByAndPatientNameContainingIgnoreCase(
            String createdBy, String name, Pageable pageable);
    @Query("""
        SELECT p.prescriptionDate AS date, COUNT(p) AS cnt
        FROM Prescription p
        WHERE p.createdBy = :createdBy
          AND p.prescriptionDate BETWEEN :start AND :end
        GROUP BY p.prescriptionDate
        ORDER BY p.prescriptionDate
        """)
    List<DateCount> countByDateBetweenForUser(
            @Param("createdBy") String createdBy,
            @Param("start") LocalDate start,
            @Param("end") LocalDate end
    );


}
