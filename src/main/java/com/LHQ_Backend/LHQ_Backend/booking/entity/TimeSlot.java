package com.LHQ_Backend.LHQ_Backend.booking.entity;

import java.time.Instant;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UuidGenerator;

import com.LHQ_Backend.LHQ_Backend.booking.enums.TimeSlotStatus;
import com.LHQ_Backend.LHQ_Backend.lawyer.entity.LawyerProfile;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Table(name = "time_slots",
    /*
     * A lawyer cannot have two slots starting at the same time.
     * This is the DB-level safety net — service layer checks overlap too,
     * but this constraint is the last line of defense.
     */
    uniqueConstraints = @UniqueConstraint(
        name = "uq_lawyer_start_time",
        columnNames = {"lawyer_id", "start_time"}
    )
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TimeSlot {

    @Id
    @UuidGenerator
    @Column(updatable = false, nullable = false)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lawyer_id", nullable = false)
    private LawyerProfile lawyer;

     /*
     * NULL  = custom slot (lawyer created manually)
     * NOT NULL = generated from an AvailabilityTemplate by the scheduler
     *
     * We intentionally do NOT cascade delete from template -> slots.
     * If a lawyer deletes a template, existing booked slots must survive.
     * The service layer should only clean up AVAILABLE slots on template deletion.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "template_id", nullable = true)
    private AvailabilityTemplate template;

    @Column(name = "start_time", nullable = false)
    private Instant startTime;

    @Column(name = "end_time", nullable = false)
    private Instant endTime;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private TimeSlotStatus status = TimeSlotStatus.AVAILABLE;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private Instant createdAt;
}
