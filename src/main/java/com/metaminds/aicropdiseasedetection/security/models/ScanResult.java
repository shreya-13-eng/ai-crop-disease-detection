package com.metaminds.aicropdiseasedetection.security.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.Instant;
import java.util.List;

@Builder
@Data
@Entity
@Table(name="scan_results")
@NoArgsConstructor
@AllArgsConstructor
public class ScanResult {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String imageUrl;
    private Instant scanTime;
    private List<String> detectedDiseases;
    private Float confidenceScore;
    private List<String> recommendedTreatment;

    @ManyToOne
    @JoinColumn(name = "farm_id")
    private Farm farm;
}