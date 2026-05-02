package com.LHQ_Backend.LHQ_Backend.booking.repository;

import com.LHQ_Backend.LHQ_Backend.booking.entity.Booking;
import com.LHQ_Backend.LHQ_Backend.booking.enums.BookingStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<Booking, String> {

    // ── User-side queries ──────────────────────────────────────────────────────

    Page<Booking> findAllByUserId(String userId, Pageable pageable);

    Page<Booking> findAllByUserIdAndStatus(String userId, BookingStatus status, Pageable pageable);

    // ── Lawyer-side queries ────────────────────────────────────────────────────

    Page<Booking> findAllByLawyerId(String lawyerId, Pageable pageable);

    Page<Booking> findAllByLawyerIdAndStatus(String lawyerId, BookingStatus status,
            Pageable pageable);

    // ── Ownership verification queries ────────────────────────────────────────

    Optional<Booking> findByIdAndUserId(String id, String userId);

    Optional<Booking> findByIdAndLawyerId(String id, String lawyerId);

    /**
     * Used before creating a Review — confirms the booking belongs to the user AND is in COMPLETED
     * state. Prevents reviews on pending/cancelled bookings.
     */
    @Query("""
            SELECT b FROM Booking b
            WHERE b.id = :bookingId
              AND b.user.id = :userId
              AND b.status = com.LHQ_Backend.LHQ_Backend.booking.enums.BookingStatus.COMPLETED
            """)
    Optional<Booking> findCompletedBookingByIdAndUserId(@Param("bookingId") String bookingId,
            @Param("userId") String userId);

    /**
     * Checks if a booking already exists for a given time slot. Prevents double-booking at the
     * application layer before the DB unique constraint fires.
     */
    @Query("""
            SELECT COUNT(b) > 0 FROM Booking b
            WHERE b.timeSlot.id = :timeSlotId
              AND b.status <> com.LHQ_Backend.LHQ_Backend.booking.enums.BookingStatus.CANCELLED
            """)
    boolean existsActiveBookingForTimeSlot(@Param("timeSlotId") String timeSlotId);

    /**
     * Used to auto-create a ClientLawyer relationship after a booking is CONFIRMED. Checks if the
     * relationship already exists before creating it.
     */
    @Query("""
            SELECT COUNT(b) > 0 FROM Booking b
            WHERE b.user.id = :userId
              AND b.lawyer.id = :lawyerId
              AND b.status = com.LHQ_Backend.LHQ_Backend.booking.enums.BookingStatus.CONFIRMED
            """)
    boolean existsConfirmedBookingBetween(@Param("userId") String userId,
            @Param("lawyerId") String lawyerId);
}
