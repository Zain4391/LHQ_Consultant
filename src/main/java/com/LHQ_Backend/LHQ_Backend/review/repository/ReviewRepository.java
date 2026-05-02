package com.LHQ_Backend.LHQ_Backend.review.repository;

import com.LHQ_Backend.LHQ_Backend.review.entity.Review;
import com.LHQ_Backend.LHQ_Backend.review.enums.Sentiment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review, String> {

    Page<Review> findAllByLawyerId(String lawyerId, Pageable pageable);

    Page<Review> findAllByUserId(String userId, Pageable pageable);

    Page<Review> findAllByLawyerIdAndSentiment(String lawyerId, Sentiment sentiment,
            Pageable pageable);

    /** 1:1 with Booking — a booking can only have one review. */
    Optional<Review> findByBookingId(String bookingId);

    boolean existsByBookingId(String bookingId);

    /**
     * Aggregate stats used to compute a lawyer's average rating. Returned as a double[]{avgRating,
     * totalCount} projection. Service maps this to a dedicated stats DTO.
     */
    @Query("""
            SELECT AVG(r.rating), COUNT(r)
            FROM Review r
            WHERE r.lawyer.id = :lawyerId
            """)
    Object[] findRatingStatsByLawyerId(@Param("lawyerId") String lawyerId);

    /**
     * Sentiment breakdown for a lawyer — used by admin analytics dashboard. Returns rows of
     * [Sentiment, count].
     */
    @Query("""
            SELECT r.sentiment, COUNT(r)
            FROM Review r
            WHERE r.lawyer.id = :lawyerId
            GROUP BY r.sentiment
            """)
    java.util.List<Object[]> findSentimentBreakdownByLawyerId(@Param("lawyerId") String lawyerId);
}
