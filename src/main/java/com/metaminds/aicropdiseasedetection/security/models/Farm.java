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
@Table(name = "farms")
@NoArgsConstructor
@AllArgsConstructor
public class Farm {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="user_id")
    private User user;

    private String farmName;
    private String location;
    private Integer sizeAcres;
    private String soilType;
    private String irrigation_type;
    private String cropType;
    private Instant createdAt;
    private Instant updatedAt;

    @OneToMany(mappedBy = "farm", cascade = CascadeType.ALL)
    private List<ScanResult> scanResults;
}