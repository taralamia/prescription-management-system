package com.example.prescriptionapp.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDate;
import java.time.OffsetDateTime;

@Entity
@Table(name = "prescriptions",
indexes = {
@Index(name = "idx_presc_user_date", columnList = "created_by, prescription_date")
       })


public class Prescription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Prescription date is required")
    @PastOrPresent(message = "Prescription date cannot be in the future")
    private LocalDate prescriptionDate;

    @NotBlank(message = "Patient name is required")
    @Size(max = 200)
    private String patientName;

    @Min(value = 0, message = "Age must be at least 0")
    @Max(value = 150, message = "Age must be less than or equal to 150")
    private Integer patientAge;

    @NotNull(message = "Patient gender is required")
    @Enumerated(EnumType.STRING)
    private Gender patientGender;

    @Lob
    @Column(length = 4000)
    private String diagnosis;

    @Lob
    @Column(length = 4000)
    private String medicines;

    @FutureOrPresent(message = "Next visit date cannot be in the past")
    private LocalDate nextVisitDate;

    @Column(nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    // store the username of the user who created this prescription
    @Column(nullable = false)
    private String createdBy;

    public Prescription() {}

    @PrePersist
    protected void onCreate() {
        createdAt = OffsetDateTime.now();
    }

    // getters and setters for all fields (generate in IDE or use Lombok)
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public LocalDate getPrescriptionDate() { return prescriptionDate; }
    public void setPrescriptionDate(LocalDate prescriptionDate) { this.prescriptionDate = prescriptionDate; }
    public String getPatientName() { return patientName; }
    public void setPatientName(String patientName) { this.patientName = patientName; }
    public Integer getPatientAge() { return patientAge; }
    public void setPatientAge(Integer patientAge) { this.patientAge = patientAge; }
    public Gender getPatientGender() { return patientGender; }
    public void setPatientGender(Gender patientGender) { this.patientGender = patientGender; }
    public String getDiagnosis() { return diagnosis; }
    public void setDiagnosis(String diagnosis) { this.diagnosis = diagnosis; }
    public String getMedicines() { return medicines; }
    public void setMedicines(String medicines) { this.medicines = medicines; }
    public LocalDate getNextVisitDate() { return nextVisitDate; }
    public void setNextVisitDate(LocalDate nextVisitDate) { this.nextVisitDate = nextVisitDate; }
    public OffsetDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(OffsetDateTime createdAt) { this.createdAt = createdAt; }
    public String getCreatedBy() { return createdBy; }
    public void setCreatedBy(String createdBy) { this.createdBy = createdBy; }
}
