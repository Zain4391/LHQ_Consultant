package com.LHQ_Backend.LHQ_Backend.booking.repository;

import com.LHQ_Backend.LHQ_Backend.booking.entity.AvailabilityTemplate;
import com.LHQ_Backend.LHQ_Backend.booking.enums.DayOfWeek;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AvailabilityTemplateRepository
        extends JpaRepository<AvailabilityTemplate, String> {

    Page<AvailabilityTemplate> findAllByLawyerId(String lawyerId, Pageable pageable);

    List<AvailabilityTemplate> findAllByLawyerIdAndIsActiveTrue(String lawyerId);

    Optional<AvailabilityTemplate> findByIdAndLawyerId(String id, String lawyerId);

    boolean existsByLawyerIdAndDayOfWeek(String lawyerId, DayOfWeek dayOfWeek);

    /**
     * Fetches all active templates for ALL lawyers in one query. Used by the weekly @Scheduled
     * slot-generation job to avoid per-lawyer DB round trips.
     */
    @Query("""
            SELECT t FROM AvailabilityTemplate t
            JOIN FETCH t.lawyer
            WHERE t.isActive = true
            """)
    List<AvailabilityTemplate> findAllActiveWithLawyer();

    /**
     * Deactivates all templates for a lawyer in bulk. Called when a lawyer account is suspended by
     * ADMIN.
     */
    @Modifying
    @Query("""
            UPDATE AvailabilityTemplate t
            SET t.isActive = false
            WHERE t.lawyer.id = :lawyerId
            """)
    int deactivateAllByLawyerId(@Param("lawyerId") String lawyerId);
}
