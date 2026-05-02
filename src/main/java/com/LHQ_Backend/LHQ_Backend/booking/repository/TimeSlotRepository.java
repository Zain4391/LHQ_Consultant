package com.LHQ_Backend.LHQ_Backend.booking.repository;

import com.LHQ_Backend.LHQ_Backend.booking.entity.TimeSlot;
import com.LHQ_Backend.LHQ_Backend.booking.enums.TimeSlotStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Repository
public interface TimeSlotRepository extends JpaRepository<TimeSlot, String> {

  Page<TimeSlot> findAllByLawyerId(String lawyerId, Pageable pageable);

  Page<TimeSlot> findAllByLawyerIdAndStatus(String lawyerId, TimeSlotStatus status,
      Pageable pageable);

  Optional<TimeSlot> findByIdAndLawyerId(String id, String lawyerId);

  /**
   * Core overlap check used by the service layer (and backed by the DB unique constraint as a
   * last-resort safety net). A new slot [newStart, newEnd) overlaps an existing slot
   * [existingStart, existingEnd) when: newStart < existingEnd AND newEnd > existingStart.
   */
  @Query("""
      SELECT COUNT(t) > 0 FROM TimeSlot t
      WHERE t.lawyer.id = :lawyerId
        AND t.startTime < :endTime
        AND t.endTime > :startTime
        AND t.status <> com.LHQ_Backend.LHQ_Backend.booking.enums.TimeSlotStatus.CANCELLED
      """)
  boolean existsOverlappingSlot(@Param("lawyerId") String lawyerId,
      @Param("startTime") Instant startTime, @Param("endTime") Instant endTime);

  /**
   * Variant of overlap check that excludes a specific slot — used when updating an existing slot.
   */
  @Query("""
      SELECT COUNT(t) > 0 FROM TimeSlot t
      WHERE t.lawyer.id = :lawyerId
        AND t.id <> :excludeId
        AND t.startTime < :endTime
        AND t.endTime > :startTime
        AND t.status <> com.LHQ_Backend.LHQ_Backend.booking.enums.TimeSlotStatus.CANCELLED
      """)
  boolean existsOverlappingSlotExcluding(@Param("lawyerId") String lawyerId,
      @Param("startTime") Instant startTime, @Param("endTime") Instant endTime,
      @Param("excludeId") String excludeId);

  /**
   * Used by the scheduler to check if slots have already been generated for a template within a
   * given window — prevents duplicate generation on re-runs.
   */
  @Query("""
      SELECT COUNT(t) > 0 FROM TimeSlot t
      WHERE t.template.id = :templateId
        AND t.startTime >= :windowStart
        AND t.startTime < :windowEnd
      """)
  boolean existsByTemplateIdAndWindow(@Param("templateId") String templateId,
      @Param("windowStart") Instant windowStart, @Param("windowEnd") Instant windowEnd);

  /**
   * Used when a lawyer deletes an AvailabilityTemplate. Only AVAILABLE slots are deleted;
   * BOOKED/CONFIRMED slots are left intact.
   */
  @Modifying
  @Query("""
      DELETE FROM TimeSlot t
      WHERE t.template.id = :templateId
        AND t.status = com.LHQ_Backend.LHQ_Backend.booking.enums.TimeSlotStatus.AVAILABLE
      """)
  int deleteAvailableSlotsByTemplateId(@Param("templateId") String templateId);

  /**
   * Fetches all available slots for a lawyer within a date range. Used by the booking flow to show
   * open slots to a user.
   */
  @Query("""
      SELECT t FROM TimeSlot t
      WHERE t.lawyer.id = :lawyerId
        AND t.status = com.LHQ_Backend.LHQ_Backend.booking.enums.TimeSlotStatus.AVAILABLE
        AND t.startTime >= :from
        AND t.startTime < :to
      ORDER BY t.startTime ASC
      """)
  List<TimeSlot> findAvailableSlots(@Param("lawyerId") String lawyerId, @Param("from") Instant from,
      @Param("to") Instant to);

  @Modifying
  @Query("""
      UPDATE TimeSlot t
      SET t.status = com.LHQ_Backend.LHQ_Backend.booking.enums.TimeSlotStatus.CANCELLED
      WHERE t.lawyer.id = :lawyerId
        AND t.status = com.LHQ_Backend.LHQ_Backend.booking.enums.TimeSlotStatus.AVAILABLE
      """)
  int cancelAvailableSlotsByLawyerId(@Param("lawyerId") String lawyerId);
}
