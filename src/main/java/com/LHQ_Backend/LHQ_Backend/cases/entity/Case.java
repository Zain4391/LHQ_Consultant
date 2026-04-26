package com.LHQ_Backend.LHQ_Backend.cases.entity;

import java.time.Instant;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UuidGenerator;

import com.LHQ_Backend.LHQ_Backend.cases.enums.CaseStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "cases")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class Case {
    
    @Id
    @UuidGenerator
    @Column(updatable = false, nullable = false)
    private String id;

    /*
    - We use client lawyer id
    - Booking is successful, then the user becomes a client
    - The user's matter becomes the Case
    */
   @ManyToOne(fetch = FetchType.LAZY)
   @JoinColumn(name = "client_lawyer_id", nullable = false)
   private ClientLawyer clientLawyer;

   @Column(nullable = false)
   private String title;

   @Column(columnDefinition = "TEXT")
   private String description;

   @Column(name = "case_type")
   private String caseType;

   @Enumerated(EnumType.STRING)
   @Column(nullable = false)
   @Builder.Default
   private CaseStatus status = CaseStatus.OPEN;

   @CreationTimestamp
   @Column(name = "opened_at")
    private Instant openedAt;

    @Column(name = "closed_at")
    private Instant closedAt;
}
