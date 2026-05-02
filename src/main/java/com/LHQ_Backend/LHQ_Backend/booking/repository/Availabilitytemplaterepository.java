
package com.LHQ_Backend.LHQ_Backend.booking.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.LHQ_Backend.LHQ_Backend.booking.entity.AvailabilityTemplate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;

@Repository
public interface Availabilitytemplaterepository
        extends JpaRepository<AvailabilityTemplate, String> {
    Page<AvailabilityTemplate> findAllByLawyerId(String lawyerId, Pageable page);

    List<AvailabilityTemplate> findAllByLawyerIdAndIsActiveTrue(String lawyerId);

    Optional<AvailabilityTemplate> findByIdAndLawyerId(String id, String lawyerId);

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
     * ADMIN. Will be a part of weekly job which checks account suspension
     */
    @Modifying
    @Query("""
            UPDATE AvailabilityTemplate t
            SET t.isActive = false
            WHERE t.lawyer.id = :lawyerId
            """)
    int deactivateAllByLawyerId(@Param("lawyerId") String lawyerId);
}
