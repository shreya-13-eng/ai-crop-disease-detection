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
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String fullName;
    private String email;
    @Column(length = 12)
    private Long phoneNumber;
    private String password;
    private String profileImageUrl;
    private String address;
    private Instant createdAt;
    private Instant updatedAt;
    private boolean isActive;

    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL)
    private List<Farm> farm;
}