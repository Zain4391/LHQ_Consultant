package com.LHQ_Backend.LHQ_Backend.lawyer.entity;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UuidGenerator;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "lawyer_profiles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LawyerProfile {
    
    @Id
    @UuidGenerator
    @Column(updatable = false, nullable = false)
    private String id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private String userId;

    @Column(columnDefinition = "TEXT")
    private String bio;

    @Column(columnDefinition = "TEXT")
    private String about;

    @Column(precision = 10, scale = 2)
    private BigDecimal rate;

    @Column(name = "bar_number", unique = true)
    private String barNumber;

    @Column(name = "years_of_experience")
    private Integer yearsOfExperience;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "lawyer_specialties",
        joinColumns = @JoinColumn(name = "lawyer_id"),
        inverseJoinColumns = @JoinColumn(name = "specialty_id")
    )
    /* 
    - We initialize the hashset as null.
    - Set is used to prevent duplicates
    - If we do not use the DEFAULT, then the HashSet<>() can end up having null values
    - This will cause errors!
    */
    @Builder.Default 
    private Set<Specialty> specialties  = new HashSet<>();

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private Instant createdAt;
}
