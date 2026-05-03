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

        Page<Booking> findAllByUserIdAndStatus(String userId, BookingStatus status,
                        Pageable pageable);

        // ── Lawyer-side queries ────────────────────────────────────────────────────

        Page<Booking> findAllByLawyerId(String lawyerId, Pageable pageable);

        Page<Booking> findAllByLawyerIdAndStatus(String lawyerId, BookingStatus status,
                        Pageable pageable);

        // ── Ownership verification queries ────────────────────────────────────────

        Optional<Booking> findByIdAndUserId(String id, String userId);

        Optional<Booking> findByIdAndLawyerId(String id, String lawyerId);

        /**
         * Used before creating a Review — confirms the booking belongs to the user AND is in
         * COMPLETED state. Prevents reviews on pending/cancelled bookings.
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
         * Used to auto-create a ClientLawyer relationship after a booking is CONFIRMED. Checks if
         * the relationship already exists before creating it.
         */
        @Query("""
                        SELECT COUNT(b) > 0 FROM Booking b
                        WHERE b.user.id = :userId
                          AND b.lawyer.id = :lawyerId
                          AND b.status = com.LHQ_Backend.LHQ_Backend.booking.enums.BookingStatus.CONFIRMED
                        """)
        boolean existsConfirmedBookingBetween(@Param("userId") String userId,
                        @Param("lawyerId") String lawyerId);

        /**
         * Full graph fetch for mapping to BookingResponse. Initializes: booking → user, lawyer,
         * lawyer.user, timeSlot Used by: any service method that returns BookingResponse
         */
        @Query("""
                        SELECT b FROM Booking b
                        JOIN FETCH b.user
                        JOIN FETCH b.lawyer l
                        JOIN FETCH l.user
                        JOIN FETCH b.timeSlot
                        WHERE b.id = :id
                        """)
        Optional<Booking> findByIdWithFullGraph(@Param("id") String id);

        /**
         * Full graph fetch filtered by userId — for user-side ownership check + mapping. Used by:
         * GET /bookings/{id} when called by a USER role
         */
        @Query("""
                        SELECT b FROM Booking b
                        JOIN FETCH b.user
                        JOIN FETCH b.lawyer l
                        JOIN FETCH l.user
                        JOIN FETCH b.timeSlot
                        WHERE b.id = :id AND b.user.id = :userId
                        """)
        Optional<Booking> findByIdAndUserIdWithFullGraph(@Param("id") String id,
                        @Param("userId") String userId);

        /**
         * Full graph fetch filtered by lawyerId — for lawyer-side ownership check + mapping. Used
         * by: GET /bookings/{id} when called by a LAWYER role
         */
        @Query("""
                        SELECT b FROM Booking b
                        JOIN FETCH b.user
                        JOIN FETCH b.lawyer l
                        JOIN FETCH l.user
                        JOIN FETCH b.timeSlot
                        WHERE b.id = :id AND b.lawyer.id = :lawyerId
                        """)
        Optional<Booking> findByIdAndLawyerIdWithFullGraph(@Param("id") String id,
                        @Param("userId") String lawyerId);

        /**
         * Paginated list for a user with full graph — for GET /bookings (user side). countQuery is
         * mandatory when JOIN FETCH + Pageable are combined.
         */
        @Query(value = """
                        SELECT b FROM Booking b
                        JOIN FETCH b.user
                        JOIN FETCH b.lawyer l
                        JOIN FETCH l.user
                        JOIN FETCH b.timeSlot
                        WHERE b.user.id = :userId
                        """, countQuery = """
                        SELECT COUNT(b) FROM Booking b
                        WHERE b.user.id = :userId
                        """)
        Page<Booking> findAllByUserIdWithFullGraph(@Param("userId") String userId,
                        Pageable pageable);

        /**
         * Paginated list for a lawyer with full graph — for GET /bookings (lawyer side).
         */
        @Query(value = """
                        SELECT b FROM Booking b
                        JOIN FETCH b.user
                        JOIN FETCH b.lawyer l
                        JOIN FETCH l.user
                        JOIN FETCH b.timeSlot
                        WHERE b.lawyer.id = :lawyerId
                        """, countQuery = """
                        SELECT COUNT(b) FROM Booking b
                        WHERE b.lawyer.id = :lawyerId
                        """)
        Page<Booking> findAllByLawyerIdWithFullGraph(@Param("lawyerId") String lawyerId,
                        Pageable pageable);
}
